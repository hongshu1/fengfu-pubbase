package cn.rjtech.admin.sysproductin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.SysProductin;
import cn.rjtech.model.momdata.SysProductindetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产成品入库单
 * @ClassName: SysProductinService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 09:56
 */
public class SysProductinService extends BaseService<SysProductin> {
    private final SysProductin dao = new SysProductin().dao();

    @Override
    protected SysProductin dao() {
        return dao;
    }

    @Inject
    private SysProductindetailService sysproductindetailservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     * @param pageNumber      第几页
     * @param pageSize        每页几条数据
     * @param keywords        关键词
     * @param SourceBillType  来源类型;MO生产工单
     * @param BillType        业务类型
     * @param state           状态 1已保存 2待审批 3已审批 4审批不通过
     * @param IsDeleted       删除状态：0. 未删除 1. 已删除
     * @param warehousingType 入库类别
     * @return
     */
    public Page<SysProductin> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceBillType, String BillType, String state, Boolean IsDeleted, String warehousingType) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("SourceBillType", SourceBillType);
        sql.eq("BillType", BillType);
        sql.eq("state", state);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        sql.eq("warehousingType", warehousingType);
        // 关键词模糊查询
        sql.likeMulti(keywords, "deptName", "remark", "repositoryName");
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     * @param sysProductin
     * @return
     */
    public Ret save(SysProductin sysProductin) {
        if (sysProductin == null || isOk(sysProductin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysProductin.save();
        return ret(success);
    }

    /**
     * 更新
     * @param sysProductin
     * @return
     */
    public Ret update(SysProductin sysProductin) {
        if (sysProductin == null || notOk(sysProductin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysProductin dbSysProductin = findById(sysProductin.getAutoID());
        if (dbSysProductin == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysProductin.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     * @param sysProductin 要删除的model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysProductin sysProductin, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     * @param sysProductin model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysProductin sysProductin, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysProductin sysProductin, String column, Kv kv) {
        return null;
    }

    /**
     * 后台管理数据查询
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @return
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("sysproductin.recpor", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            deleteByIds(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                delete("DELETE T_Sys_ProductInDetail   where  MasID = ?", s);
            }
            return true;
        });
        return ret(true);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        tx(() -> {
            deleteById(id);
            delete("DELETE T_Sys_ProductInDetail   where  MasID = ?", id);
            return true;
        });
        return ret(true);
    }

    /**
     * 执行JBoltTable表格整体提交
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
            return Ret.msg("行数据不能为空");
        }
        SysProductin sysotherin = jBoltTable.getFormModel(SysProductin.class, "sysProductin");
        // 获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            // 通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setCreatePerson(user.getUsername());
                sysotherin.setCreateDate(now);
                sysotherin.setModifyPerson(user.getUsername());
                sysotherin.setAuditPerson(user.getUsername());
                sysotherin.setState("1");
                sysotherin.setModifyDate(now);
                // 主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                sysotherin.setModifyPerson(user.getUsername());
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
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysProductin sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysProductindetail> sysproductindetail = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysProductindetail sysdetail = new SysProductindetail();
            sysdetail.setBarcode(row.get("barcode"));
            sysdetail.setInvCode(row.get("cinvcode"));
            sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysdetail.setCreateDate(now);
            sysdetail.setModifyDate(now);
            sysproductindetail.add(sysdetail);
        }
        sysproductindetailservice.batchSave(sysproductindetail);
    }

    // 可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysProductin sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysProductindetail> sysproductindetail = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysProductindetail sysdetail = new SysProductindetail();
            sysdetail.setAutoID(row.get("autoid").toString());
            sysdetail.setBarcode(row.get("barcode"));
            sysdetail.setInvCode(row.get("cinvcode"));
            sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysdetail.setCreateDate(now);
            sysdetail.setModifyDate(now);
            sysproductindetail.add(sysdetail);

        }
        sysproductindetailservice.batchUpdate(sysproductindetail);
    }

    // 可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        sysproductindetailservice.deleteByIds(ids);
    }

    public List<Record> getwareHouseDatas(Kv kv) {
        return dbTemplate("sysproductin.wareHouse", kv).find();
    }

    public List<Record> getRdStyleDatas(Kv kv) {
        return dbTemplate("sysproductin.RdStyle", kv).find();
    }

    public List<Record> getDepartmentDatas(Kv kv) {
        return dbTemplate("sysproductin.Department", kv).find();
    }

    public Record selectName(String username) {
        Kv kv = new Kv();
        kv.set("username", username);
        return dbTemplate("sysproductin.selectname", kv).findFirst();
    }
}