package cn.rjtech.wms.print.organize;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Organize;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

import static cn.hutool.core.util.StrUtil.COMMA;

/**
 * WMS组织 Service
 *
 * @ClassName: OrganizeService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-24 16:54
 */
public class OrganizeService extends BaseService<Organize> {

    private final Organize dao = new Organize().dao();

    @Override
    protected Organize dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Organize> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("id", "desc", pageNumber, pageSize, keywords, "name");
    }

    /**
     * 保存
     */
    public Ret save(Organize organize) {
        if (organize == null || isOk(organize.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
            ValidationUtils.isTrue(organize.save(), ErrorMsg.SAVE_FAILED);


            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSaveSystemLog(organize.getId(), JBoltUserKit.getUserId(), organize.getName());
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(Organize organize) {
        if (organize == null || notOk(organize.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // 更新时需要判断数据存在
            Organize dbOrganize = findById(organize.getId());
            ValidationUtils.notNull(dbOrganize, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(organize.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //添加日志
        //addUpdateSystemLog(organize.getId(), JBoltUserKit.getUserId(), organize.getName());
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long id = Long.parseLong(idStr);
                Organize dbOrganize = findById(id);
                ValidationUtils.notNull(dbOrganize, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbOrganize.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Organize organize = ret.getAs("data");
        // addDeleteSystemLog(id, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, organize.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param organize 要删除的model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Organize organize, Kv kv) {
        //addDeleteSystemLog(organize.getId(), JBoltUserKit.getUserId(),organize.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param organize 要删除的model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Organize organize, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(organize, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }


    /**
     * 获得组织List
     *
     * @return 组织列表
     */
    public List<Organize> getOrgList() {
        Kv paras = new Kv();
        return daoTemplate("organize.getOrgList", paras).find();
    }

    /**
     * 通过orgCode查询组织
     *
     * @param orgCode 组织编码
     * @return 组织对象
     */
    public Organize getOrgByCode(String orgCode) {
        Kv paras = Kv.by("orgCode", orgCode);
        return daoTemplate("organize.getOrgByCode", paras).findFirst();
    }


    public String getThisOrganizecode() {
        Kv paras = Kv.by("orgCode", getOrgCode());
        Organize first = daoTemplate("organize.getOrgList", paras).findFirst();
        return first.getOrganizecode();
    }
}