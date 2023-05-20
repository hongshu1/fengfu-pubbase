package cn.rjtech.admin.sysotherin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.SysOtherindetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysOtherin;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 其它入库单
 * @ClassName: SysOtherinService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-05 17:57
 */
public class SysOtherinService extends BaseService<SysOtherin> {
    private final SysOtherin dao = new SysOtherin().dao();

    @Override
    protected SysOtherin dao() {
        return dao;
    }

    @Inject
    private SysOtherindetailService sysotherindetailservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @return
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        // 查业务类型
        Page<Record> paginate = dbTemplate("sysotherin.recpor", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 保存
     *
     * @param sysOtherin
     * @return
     */
    public Ret save(SysOtherin sysOtherin) {
        if (sysOtherin == null || isOk(sysOtherin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysOtherin.save();
        return ret(success);
    }

    /**
     * 更新
     * @param sysOtherin
     * @return
     */
    public Ret update(SysOtherin sysOtherin) {
        if (sysOtherin == null || notOk(sysOtherin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysOtherin dbSysOtherin = findById(sysOtherin.getAutoID());
        if (dbSysOtherin == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysOtherin.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     * @param sysOtherin 要删除的model
     * @param kv         携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysOtherin sysOtherin, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysOtherin model
     * @param kv         携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysOtherin sysOtherin, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        tx(() -> {
            deleteById(id);
            delete("DELETE T_Sys_OtherInDetail   where  MasID = ?", id);
            return true;
        });
        return ret(true);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            deleteByIds(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                delete("DELETE T_Sys_OtherInDetail   where  MasID = ?", s);
            }
            return true;
        });
        return ret(true);
    }


    /**
     * 执行JBoltTable表格整体提交
     *
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
            return Ret.msg("行数据不能为空");
        }
        SysOtherin sysotherin = jBoltTable.getFormModel(SysOtherin.class, "sysotherin");
        // 获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            // 通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setCreatePerson(user.getName());
                sysotherin.setCreateDate(now);
                sysotherin.setModifyPerson(user.getName());
                sysotherin.setAuditPerson(user.getName());
                sysotherin.setState("1");
                sysotherin.setModifyDate(now);
                // 主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                sysotherin.setModifyPerson(user.getName());
                sysotherin.setModifyDate(now);
                // 主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            // 从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin);
            // 获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin);
            // 获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return SUCCESS;
    }

    // 可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysOtherin sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysOtherindetail> systailsList = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysOtherindetail sysOtherindetail = new SysOtherindetail();

            sysOtherindetail.setMasID(Long.valueOf(sysotherin.getAutoID()));
            sysOtherindetail.setModifyPerson(JBoltUserKit.getUser().getName());
            sysOtherindetail.setModifyDate(now);
            sysOtherindetail.setModifyDate(now);
            sysOtherindetail.setCreateDate(now);
            sysOtherindetail.setCreatePerson(JBoltUserKit.getUser().getName());
            sysOtherindetail.setBarcode(row.get("barcode"));
            sysOtherindetail.setInvCode(row.get("cinvcode"));
            sysOtherindetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysOtherindetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysOtherindetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysOtherindetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysOtherindetail.setSourceBillID(row.getStr("sourcebilldid"));

            systailsList.add(sysOtherindetail);

        }
        sysotherindetailservice.batchSave(systailsList);
    }

    // 可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysOtherin sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysOtherindetail> systailsList = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysOtherindetail sysOtherindetail = new SysOtherindetail();
            sysOtherindetail.setMasID(Long.valueOf(sysotherin.getAutoID()));
            sysOtherindetail.setModifyPerson(JBoltUserKit.getUser().getName());
            sysOtherindetail.setModifyDate(now);
            sysOtherindetail.setCreatePerson(JBoltUserKit.getUser().getName());
            sysOtherindetail.setAutoID(Long.valueOf(row.get("autoid")));
            sysOtherindetail.setBarcode(row.get("barcode"));
            sysOtherindetail.setInvCode(row.get("cinvcode"));
            sysOtherindetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysOtherindetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysOtherindetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysOtherindetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysOtherindetail.setSourceBillID(row.getStr("sourcebilldid"));

            systailsList.add(sysOtherindetail);

        }
        sysotherindetailservice.batchUpdate(systailsList);
    }

    // 可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        sysotherindetailservice.deleteByIds(ids);

    }
}