package cn.rjtech.admin.org;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.base.JBoltProSystemLogTargetType;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.enumutil.JBoltEnableEnum;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.kit.U8DataSourceKit;
import cn.jbolt.core.model.Org;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.util.StrUtil.COMMA;

/**
 * @ClassName: OrgService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2021-01-25 16:59
 */
public class OrgService extends BaseService<Org> {

    private final Org dao = new Org().dao();

    @Override
    protected Org dao() {
        return dao;
    }

    /**
     * 后台管理分页查询true
     */
    public Page<Record> paginateAdminList(int pageNumber, int pageSize, Okv para) {
        return dbTemplate("org.list", para).paginate(pageNumber, pageSize);
    }

    private boolean notExists(String orgCode) {
        return null == queryInt("SELECT TOP 1 1 FROM jb_org o WHERE o.org_code = ?", orgCode);
    }

    private boolean notExistsU8DbName(String ip, String u8DbName) {
        return null == queryInt("SELECT TOP 1 1 FROM jb_org o WHERE o.u8_api_url_inner AND o.u8_dbname = ?", ip, u8DbName);
    }

    /**
     * 保存
     */
    public Ret save(Org org) {
        ValidationUtils.assertNull(org.getId(), JBoltMsg.PARAM_ERROR);
        ValidationUtils.isTrue(notExists(org.getOrgCode()), "已存在组织编码");
        ValidationUtils.isTrue(notExistsU8DbName(org.getU8DbUrlInner(), org.getU8Dbname()), "该主机已存在同名数据库");

        Date now = new Date();

        tx(() -> {
            org.setCreateTime(now);
            org.setUserId(JBoltUserKit.getUserId());
            org.setUpdateTime(now);
            org.setUpdateUserId(JBoltUserKit.getUserId());

            if (org.getEnable()) {
                org.setEnableTime(now);
            }

            ValidationUtils.isTrue(org.save(), ErrorMsg.SAVE_FAILED);

            // 当前为默认，则其他更新为非默认
            if (org.getIsDefault()) {
                updateOtherIsDefault0(org.getId());
            }

            // 加载数据源
            if (org.getEnable()) {
                U8DataSourceKit.ME.addDataSource(org);
            }
            return true;
        });

        // 添加日志
        // addSystemLog(org.getId(), userId, SystemLog.TYPE_SAVE, SystemLog.TARGETTYPE_xxx, org.getName())
        return SUCCESS;
    }

    private void updateOtherIsDefault0(Long id) {
        update("UPDATE jb_org SET is_default = '0' WHERE id <> ? AND is_default = '1' ", id);
    }

    /**
     * 保存
     */
    public Ret update(Org org) {
        ValidationUtils.validateModel(org, JBoltMsg.PARAM_ERROR);
        ValidationUtils.isTrue(isOk(org.getId()), JBoltMsg.PARAM_ERROR);
        
        Date now = new Date();

        tx(() -> {
            // 更新时需要判断数据存在
            Org dbOrg = findById(org.getId());
            ValidationUtils.notNull(dbOrg, JBoltMsg.DATA_NOT_EXIST);

            if (ObjUtil.notEqual(dbOrg.getU8DbUrlInner(), org.getU8DbUrlInner()) || ObjUtil.notEqual(dbOrg.getU8Dbname(), org.getU8Dbname())) {
                ValidationUtils.isTrue(notExistsU8DbName(org.getU8DbUrlInner(), org.getU8Dbname()), "该主机已存在同名数据库");
            }

            org.setUpdateTime(now);
            org.setUpdateUserId(JBoltUserKit.getUserId());

            if (ObjUtil.notEqual(org.getEnable(), dbOrg.getEnable())) {
                if (org.getEnable()) {
                    org.setEnableTime(org.getEnable() ? now : null);

                    // 加载数据源
                    U8DataSourceKit.ME.addDataSource(org);
                } else {
                    // 卸载数据源
                    U8DataSourceKit.ME.addDataSource(org);
                }
            }

            // 修改了默认
            if (ObjUtil.notEqual(org.getIsDefault(), dbOrg.getIsDefault())) {
                // 当前为默认，则其他更新为非默认
                if (org.getIsDefault()) {
                    updateOtherIsDefault0(org.getId());
                }
            }

            ValidationUtils.isTrue(org.update(), ErrorMsg.UPDATE_FAILED);
            return true;
        });

        // 添加日志
        // addSystemLog(org.getId(), userId, SystemLog.TYPE_UPDATE, SystemLog.TARGETTYPE_xxx, org.getName())
        return SUCCESS;
    }

    /**
     * 删除
     */
    @Override
    public Ret deleteByIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long id = Long.parseLong(idStr);
                Org dbOrg = findById(id);
                ValidationUtils.notNull(dbOrg, JBoltMsg.DATA_NOT_EXIST);
                ValidationUtils.isTrue(!dbOrg.getEnable(), "请先停用组织");
                ValidationUtils.isTrue(dbOrg.delete(), JBoltMsg.FAIL);
            }
            return true;
        });

        // 添加日志
        // Org org = ret.getAs("data");
        // addDeleteSystemLog(id, JBoltUserKit.getUserIdAs(), SystemLog.TARGETTYPE_xxx, org.getName());
        return SUCCESS;
    }

//    /**
//     * 切换禁用启用状态
//     */
//    public Ret toggleEnable(Long id) {
//        // 说明:如果需要日志处理 就解开下面部分内容 如果不需要直接删掉即可
//        Ret ret = toggleBoolean(id, "enable");
//        
//        // 更新启用时间
//        Org org = findById(id);
//        if (org.getEnable()) {
//            tx(() -> {
//                ValidationUtils.isTrue(org.setEnableTime(new Date()).update(), ErrorMsg.UPDATE_FAILED);
//
//                // 加载数据源
//                U8DataSourceKit.ME.addDataSource(org);
//                return true;
//            });
//        } else {
//            // 卸载数据源
//            U8DataSourceKit.ME.removeDataSource(org);
//        }
//
//        if (ret.isOk()) {
//            // 添加日志
//            // Org org=ret.getAs("data");
//            // addUpdateSystemLog(id, userId, SystemLog.TARGETTYPE_xxx, org.getName(),"的启用状态:"+org.getEnable());
//        }
//        return ret;
//    }

    @Override
    protected String afterToggleBoolean(Org org, String column, Kv kv) {
        switch (column) {
            case "enable":
                if (org.getEnable()) {
                    tx(() -> {
                        ValidationUtils.isTrue(org.setEnableTime(new Date()).update(), ErrorMsg.UPDATE_FAILED);

                        // 加载数据源
                        U8DataSourceKit.ME.addDataSource(org);
                        return true;
                    });
                } else {
                    // 卸载数据源
                    U8DataSourceKit.ME.removeDataSource(org);
                }
                break;
            case "is_default":
                if (org.getIsDefault()) {
                    updateOtherIsDefault0(org.getId());
                }
                break;
            default:
                break;
        }
        return null;
    }

    public List<Org> getList() {
        return find("SELECT id, org_name FROM jb_org WHERE enable = ? ORDER BY is_default DESC, id DESC", JBoltEnableEnum.TRUE.getValue());
    }
    public String getU8Alias(long orgId) {
        return queryColumn("SELECT u8_alias FROM jb_org WHERE id = ? ", orgId);
    }

    public String getU8DbName(long orgId) {
        return queryColumn("SELECT u8_dbname FROM jb_org WHERE id = ? ", orgId);
    }
    
    public Org findByOrgCode(String orgCode) {
        return dao.findFirst("SELECT * FROM jb_org WHERE org_code = ? ", orgCode);
    }

    @Override
    protected int systemLogTargetType() {
        return JBoltProSystemLogTargetType.ORG.getValue();
    }

    public List<Record> getU8DbList() {
        return dbTemplate("org.getU8DbList").find();
    }

    public List<Record> getListForApi() {
        return findRecords("SELECT id, org_code AS code, org_name AS name FROM jb_org WHERE enable = ? ORDER BY is_default DESC, id DESC", JBoltEnableEnum.TRUE.getValue());
    }

}