package cn.rjtech.admin.syspureceive;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStateEnum;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.model.momdata.SysPureceive;
import cn.rjtech.model.momdata.SysPureceivedetail;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

/**
 * 采购收料单
 *
 * @ClassName: SysPureceiveService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
public class SysPureceiveService extends BaseService<SysPureceive> {

    private final SysPureceive dao = new SysPureceive().dao();

    @Inject
    private VendorService vendorservice;
    @Inject
    private WarehouseService warehouseservice;
    @Inject
    private FormApprovalService formApprovalService;
    @Inject
    private RcvDocQcFormMService rcvdocqcformmservice;
    @Inject
    private SysPureceivedetailService syspureceivedetailservice;

    @Override
    protected SysPureceive dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param BillType   到货单类型;采购PO  委外OM
     * @param IsDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<SysPureceive> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, Boolean IsDeleted) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("BillType", BillType);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        // 关键词模糊查询
        sql.like("repositoryName", keywords);
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(SysPureceive sysPureceive) {
        if (sysPureceive == null || isOk(sysPureceive.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysPureceive.save();
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysPureceive sysPureceive) {
        if (sysPureceive == null || notOk(sysPureceive.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysPureceive dbSysPureceive = findById(sysPureceive.getAutoID());
        if (dbSysPureceive == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysPureceive.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysPureceive 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPureceive sysPureceive, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysPureceive model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysPureceive sysPureceive, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysPureceive sysPureceive, String column, Kv kv) {
        return null;
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("syspureceive.recpor", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            deleteByIds(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                delete("DELETE T_Sys_PUReceiveDetail   where  MasID = ?", s);
            }
            return true;
        });
        return ret(true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            deleteById(id);
            delete("DELETE T_Sys_PUReceiveDetail   where  MasID = ?", id);
            return true;
        });
        return ret(true);
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
            return fail("行数据不能为空");
        }

        // 采购订单推u8才可以添加
        List<Record> list = jBoltTable.getSaveRecordList();
        for (Record record : list) {
            ValidationUtils.isTrue(existSourcebillno(record.getStr("sourcebillno")), "订单编号：" + record.getStr("sourcebillno") + " 没有推U8");
        }

        List<Record> list1 = jBoltTable.getUpdateRecordList();
        for (Record record : list1) {
            ValidationUtils.isTrue(existSourcebillno(record.getStr("sourcebillno")), "订单编号：" + record.getStr("sourcebillno") + " 没有推U8");
        }

        SysPureceive sysotherin = jBoltTable.getFormModel(SysPureceive.class, "sysPureceive");

        // 获取当前用户信息？
        User user = JBoltUserKit.getUser();

        Date now = new Date();

        tx(() -> {
            
            // 新增
            if (ObjUtil.isNull(sysotherin.getAutoID())) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setCreatePerson(user.getUsername());
                sysotherin.setCreateDate(now);
                sysotherin.setBillDate(DateUtil.formatDate(now));
                sysotherin.setModifyPerson(user.getUsername());
                sysotherin.setIAuditStatus(Integer.valueOf(AuditStateEnum.NOT_AUDIT.getValue()));
                sysotherin.setModifyDate(now);
                sysotherin.setBillNo(JBoltSnowflakeKit.me.nextIdStr());
                sysotherin.setIAuditStatus(Integer.valueOf(AuditStateEnum.NOT_AUDIT.getValue()));
                // 主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            }
            // 修改
            else {
                sysotherin.setModifyPerson(user.getUsername());
                sysotherin.setModifyDate(now);
                // 主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }

            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin, user, now);
            // 获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin, user, now);
            // 获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);

            if (ObjUtil.equals("submit", jBoltTable.getForm().getString("operationType"))) {
                sysotherin.setIAuditStatus(Integer.valueOf(AuditStateEnum.NOT_AUDIT.getValue()));
                
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }

            return true;
        });
        
        return SUCCESS;
    }

    /**
     * 可编辑表格提交-新增数据
     */
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysPureceive sysotherin, User user, Date now) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();

        for (Record record : list) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();
            
            record.set("MasID", sysotherin.getAutoID());
            record.set("AutoID", JBoltSnowflakeKit.me.nextId());
            record.set("CreateDate", now);
            record.set("ModifyDate", now);
            
            if (StrUtil.isBlank(record.get("isinitial"))) {
                record.set("IsInitial", false);
                sysPureceivedetail.setIsInitial("0");
            } else {
                // 获取供应商字段
                String vencode = record.getStr("vencode");
                ValidationUtils.notBlank(vencode, "条码：" + record.get("barcode") + " 供应商数据不能为空");
                
                Long veniAutoId = vendorservice.queryAutoIdByCvencode(vencode);
                
                // 推送初物 PL_RcvDocQcFormM 来料
                this.insertRcvDocQcFormM(record, sysotherin, user, veniAutoId);
                
                sysPureceivedetail.setIsInitial("1");
            }
            
            sysPureceivedetail.setMasID(sysotherin.getAutoID());
            sysPureceivedetail.setSourceBillType(record.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(record.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(record.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(record.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(record.getStr("sourcebilldid"));
            // sysPureceivedetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
//            sysPureceivedetail.setWhcode(row.getStr("whcode"));
            sysPureceivedetail.setVenCode(record.getStr("vencode"));
            sysPureceivedetail.setPosCode(record.getStr("poscode"));
            sysPureceivedetail.setQty(record.getBigDecimal("qty"));
            sysPureceivedetail.setBarcode(record.get("barcode"));
            sysPureceivedetail.setCreateDate(now);
            sysPureceivedetail.setModifyDate(now);
            sysdetaillist.add(sysPureceivedetail);
        }
        syspureceivedetailservice.batchSave(sysdetaillist);
    }

    /**
     * 可编辑表格提交-修改数据
     */
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysPureceive sysotherin, User user, Date now) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();
        
        for (Record record : list) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();

            record.set("ModifyDate", now);

            if (StrUtil.isBlank(record.get("isinitial"))) {
                record.set("IsInitial", false);
            } else {
                // 推送初物 PL_RcvDocQcFormM 来料
                String vencode = record.getStr("vencode");
                ValidationUtils.notBlank(vencode, "条码：" + record.get("barcode") + " 供应商数据不能为空");
                
                Long veniAutoId = vendorservice.queryAutoIdByCvencode(vencode);
                
                this.insertRcvDocQcFormM(record, sysotherin, user, veniAutoId);
                
                sysPureceivedetail.setIsInitial("1");
            }

            sysPureceivedetail.setMasID(sysotherin.getAutoID());
            sysPureceivedetail.setSourceBillType(record.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(record.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(record.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(record.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(record.getStr("sourcebilldid"));
            // sysPureceivedetail.setRowNo(Integer.valueOf(row.getStr("rowno")))
            // sysPureceivedetail.setWhcode(row.getStr("whcode"))
            sysPureceivedetail.setVenCode(record.getStr("vencode"));
            sysPureceivedetail.setPosCode(record.getStr("poscode"));
            sysPureceivedetail.setQty(record.getBigDecimal("qty"));
            sysPureceivedetail.setBarcode(record.get("barcode"));
            sysPureceivedetail.setCreateDate(now);
            sysPureceivedetail.setModifyDate(now);

            sysdetaillist.add(sysPureceivedetail);

        }
        syspureceivedetailservice.batchUpdate(sysdetaillist);
    }

    /**
     * 可编辑表格提交-删除数据
     */
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        if (ArrayUtil.isNotEmpty(jBoltTable.getDelete())) {
            syspureceivedetailservice.deleteByIds(jBoltTable.getDelete());
        }
    }

    /**
     * 后台管理数据查询
     */
    public List<Record> getVenCodeDatas(Kv kv) {
        return dbTemplate("syspureceive.venCode", kv).find();
    }

    /**
     * 后台管理数据查询
     */
    public List<Record> getWhcodeDatas(Kv kv) {
        return dbTemplate("syspureceive.Whcode", kv).find();
    }

    /**
     * 后台管理数据查询
     */
    public List<Record> selectRdCode(Kv kv) {
        return dbTemplate("syspureceive.selectRdCode", kv).find();
    }

    /**
     * 货区
     */
    public List<Record> getwareHousepos(Kv kv) {
        return dbTemplate("syspureceive.wareHousepos", kv).find();
    }

    /**
     * 推送 PL_RcvDocQcFormM ;//来料检验
     */
    public void insertRcvDocQcFormM(Record row, SysPureceive sys, User user, Long veniAutoId) {
        Date date = new Date();

        RcvDocQcFormM rcvDocQcFormM = new RcvDocQcFormM();

        rcvDocQcFormM.setIOrgId(getOrgId());
        rcvDocQcFormM.setCOrgCode(getOrgCode());
        rcvDocQcFormM.setCOrgName(getOrgName());

        if (StrUtil.isBlank(row.getStr("sourcebillno"))) {
            rcvDocQcFormM.setCRcvDocQcFormNo("test");
        } else {
            rcvDocQcFormM.setCRcvDocQcFormNo(row.getStr("sourcebillno"));
        }

        // 质检表格ID
        rcvDocQcFormM.setIQcFormId(1L);
        rcvDocQcFormM.setIRcvDocId(Long.valueOf(sys.getAutoID()));
        rcvDocQcFormM.setCRcvDocNo(sys.getBillNo());

        if (StrUtil.isBlank(row.getStr("iinventoryid"))) {
            rcvDocQcFormM.setIInventoryId(100L);
        } else {
            rcvDocQcFormM.setIInventoryId(row.getLong("iinventoryid"));
        }

        rcvDocQcFormM.setIVendorId(veniAutoId);
        rcvDocQcFormM.setDRcvDate(sys.getCreateDate());
        rcvDocQcFormM.setIQty(Double.valueOf(row.getStr("qty").trim()).intValue());

        // 批次号
        rcvDocQcFormM.setCBatchNo("1111111Test");
        rcvDocQcFormM.setIStatus(0);
        rcvDocQcFormM.setIsCpkSigned(false);
        rcvDocQcFormM.setIMask(2);
        rcvDocQcFormM.setIsCpkSigned(false);
        rcvDocQcFormM.setICreateBy(user.getId());
        rcvDocQcFormM.setCCreateName(user.getName());
        rcvDocQcFormM.setDCreateTime(date);
        rcvDocQcFormM.setIUpdateBy(user.getId());
        rcvDocQcFormM.setCUpdateName(user.getName());
        rcvDocQcFormM.setDUpdateTime(date);
        rcvDocQcFormM.setIsDeleted(false);

        rcvdocqcformmservice.save(rcvDocQcFormM);
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode, String vencode) {
        return dbTemplate("syspureceive.getBarcodeDatas", Kv.by("q", q).set("limit", limit).set("orgCode", orgCode).set("vencode", vencode)).find();
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public Record barcode(Kv kv) {
        //先查询条码是否已添加
        Record first = dbTemplate("syspureceive.barcodeDatas", kv).findFirst();
        if(null != first){
            ValidationUtils.isTrue( false,"条码为：" + kv.getStr("barcode") + "的数据已经存在，请勿重复录入。");
        }
        first = dbTemplate("syspureceive.barcode", kv).findFirst();
        ValidationUtils.notNull(first, "未查到条码为：" + kv.getStr("barcode") + "的数据,请核实再录入。");
        return first;
    }

    /**
     * 通过采购订单判断u8是否有数据
     */
    public boolean existSourcebillno(String sourcebillno) {
        List<Record> records = dbTemplate(u8SourceConfigName(), "syspureceive.getsourcebillno", Kv.by("sourcebillno", sourcebillno)).find();
        return CollUtil.isNotEmpty(records);
    }

    /**
     * 业务逻辑发送改变，以下重新开发一个保存功能
     */
    public Ret submit(JBoltTable jBoltTable, User user) {
        String operationType = jBoltTable.getFormRecord().getStr("operationType");

        if (!"submit".equals(operationType)) {
            if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
                return fail("行数据不能为空");
            }
        }

        SysPureceive sysPureceive = jBoltTable.getFormModel(SysPureceive.class, "sysPureceive");
        
        Date now = new Date();

        HashMap<String, String> vencodemap = new HashMap<>();
        
        tx(() -> {
            if (sysPureceive.getAutoID() != null) {
                sysPureceive.setModifyPerson(user.getUsername());
                sysPureceive.setModifyDate(now);
                if ("submit".equals(operationType)) {
                    // todo 后续业务逻辑确定是哪个状态
                    sysPureceive.setIAuditStatus(Integer.valueOf(AuditStateEnum.AWAIT_AUDIT.getValue()));
                }
                // 主表修改
                sysPureceive.update();
            }

            // 从表的操作 获取新增数据
            saveData(jBoltTable, sysPureceive, operationType, user, vencodemap);
            // 获取修改数据（执行修改）
            updateData(jBoltTable, sysPureceive, operationType, user, vencodemap);
            // 获取删除数据（执行删除）
            deleteData(jBoltTable);
            
            return true;
        });

        return SUCCESS;
    }

    private void saveData(JBoltTable jBoltTable, SysPureceive sysPureceive, String operationType, User user, HashMap<String, String> map) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();

        Date now = new Date();

        for (int i = 0; i < list.size(); i++) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();

            Record row = list.get(i);

            sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));

            // 获取供应商字段
            String vencode = row.getStr("vencode");
            ValidationUtils.notBlank(vencode, "条码：" + row.get("barcode") + " 供应商数据不能为空");

            sysPureceivedetail.setVenCode(vencode);
            sysPureceivedetail.setWhcode(sysPureceive.getWhCode());

            this.determineQty(row, i);

            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setCreateDate(now);
            sysPureceivedetail.setModifyDate(now);
            sysPureceivedetail.setIsDeleted(false);

            String s = this.insertSysPureceive(sysPureceivedetail, sysPureceive, row, operationType, map);

            sysPureceivedetail.setMasID(s);

            if (StrUtil.isBlank(row.getStr("isinitial"))) {
                sysPureceivedetail.setIsInitial("0");
            } else {
                Long veniAutoId = vendorservice.queryAutoIdByCvencode(vencode);

                // 推送初物 PL_RcvDocQcFormM 来料
                SysPureceive first = findFirstBySourceBillNo(sysPureceivedetail.getSourceBillNo());

                this.insertRcvDocQcFormM(row, first, user, veniAutoId);

                sysPureceivedetail.setIsInitial("1");
            }
            sysdetaillist.add(sysPureceivedetail);
        }
        syspureceivedetailservice.batchSave(sysdetaillist);
    }

    public SysPureceive findFirstBySourceBillNo(String sourceBillNo) {
        return findFirst("select *  from T_Sys_PUReceive where SourceBillNo = ? ", sourceBillNo);
    }

    private void updateData(JBoltTable jBoltTable, SysPureceive sysPureceive, String operationType, User user, HashMap<String, String> map) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();

        Date now = new Date();

        for (int i = 0; i < list.size(); i++) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();

            Record row = list.get(i);

            sysPureceivedetail.setAutoID(row.getStr("autoid"));
            sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));

            // 获取供应商字段
            String vencode = row.getStr("vencode");
            ValidationUtils.notBlank(vencode, "条码：" + row.get("barcode") + " 供应商数据不能为空");

            sysPureceivedetail.setVenCode(vencode);
            sysPureceivedetail.setWhcode(sysPureceive.getWhCode());

            this.determineQty(row, i);

            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(row.getBigDecimal("qty"));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setModifyDate(now);
            sysPureceivedetail.setIsDeleted(false);

            String s = this.insertSysPureceive(sysPureceivedetail, sysPureceive, row, operationType, map);
            sysPureceivedetail.setMasID(s);

            if (StrUtil.isBlank(row.getStr("isinitial"))) {
                sysPureceivedetail.setIsInitial("0");
            } else {
                Long veniAutoId = vendorservice.queryAutoIdByCvencode(vencode);

                // 推送初物 PL_RcvDocQcFormM 来料
                SysPureceive first = findFirst("select *  from T_Sys_PUReceive where SourceBillNo=?", sysPureceivedetail.getSourceBillNo());

                this.insertRcvDocQcFormM(row, first, user, veniAutoId);

                sysPureceivedetail.setIsInitial("1");
            }
            sysdetaillist.add(sysPureceivedetail);
        }

        syspureceivedetailservice.batchUpdate(sysdetaillist);
    }

    private void deleteData(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        syspureceivedetailservice.deleteByIds(ids);
    }

    /**
     * 根据行表数据 通过 供应商(取MES的采购订单)判断头表是否需要添加头表数据，返回 头表id
     */
    public String insertSysPureceive(SysPureceivedetail sysPureceivedetail, SysPureceive sysPureceive, Record record, String operationType, HashMap<String, String> map) {
        // SysPureceive first = findFirst("select *  from T_Sys_PUReceive where VenCode=?", sysPureceivedetail.getVenCode())
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // 判断是否包含指定的key
                if (key.equals(sysPureceivedetail.getVenCode())) {
                    return value;
                }
            }
        }
        
        User user = JBoltUserKit.getUser();
        
        Date now = new Date();
        
        // 需要先创建头表数据
        sysPureceive.setSourceBillNo(sysPureceivedetail.getSourceBillNo());
        sysPureceive.setVenCode(sysPureceivedetail.getVenCode());
        
        Warehouse warehouse = warehouseservice.findByCwhcode(sysPureceive.getWhCode());
        ValidationUtils.notNull(warehouse, "仓库记录不存在");
        
        sysPureceive.setWhName(warehouse.getCWhName());
        sysPureceive.setRdCode(record.getStr("rdcode"));
        sysPureceive.setOrganizeCode(getOrgCode());
        sysPureceive.setCreatePerson(user.getUsername());
        sysPureceive.setCreateDate(now);
        sysPureceive.setBillDate(DateUtil.formatDate(now));
        sysPureceive.setModifyPerson(user.getUsername());
        
        if ("submit".equals(operationType)) {
            // 待后续审批流修改状态
            sysPureceive.setIAuditStatus(Integer.valueOf(AuditStateEnum.AWAIT_AUDIT.getValue()));
        } else {
            sysPureceive.setIAuditStatus(Integer.valueOf(AuditStateEnum.NOT_AUDIT.getValue()));
        }
        
        sysPureceive.setModifyDate(now);
        sysPureceive.setBillNo(JBoltSnowflakeKit.me.nextIdStr());
        sysPureceive.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
        sysPureceive.setIsDeleted(false);
        
        ValidationUtils.isTrue(sysPureceive.save(), ErrorMsg.SAVE_FAILED);
        
        map.put(sysPureceivedetail.getVenCode(), sysPureceive.getAutoID());
        
        return sysPureceive.getAutoID();
    }

    /**
     * 查询当前入库数量是否大于仓库最大存储数
     */
    public void determineQty(Record row, int i) {
        Record first = dbTemplate("syspureceive.paginateAdminDatas", Kv.by("careacode", row.getStr("poscode"))).findFirst();

        Integer imaxcapacity = first.getInt("imaxcapacity");
        BigDecimal bigDecimal = new BigDecimal(imaxcapacity);
        BigDecimal qty = row.getBigDecimal("qty");
        if (ObjUtil.isNotNull(imaxcapacity)) {
            if (qty.compareTo(bigDecimal) == 1) {
                ValidationUtils.error(String.format("第%d行%s现品票号实收数量超出对应库存最大存储量，请选择其他库区录入", i + 1, row.get("barcode")));
            }
        }
    }

    /**
     * 更新状态
     *
     * @param autoId      主键ID
     * @param beforeState 审批前状态
     * @param afterState  审批后状态
     * @return true, 更新成功
     */
    private boolean update(String autoId, String beforeState, String afterState) {
        return update("UPDATE T_Sys_PUReceive SET iAuditStatus = ? WHERE autoid = ? AND iAuditStatus = ? ", afterState, autoId, beforeState) > 0;
    }

    /**
     * 提审
     */
    public Ret submit(Long iautoid) {
        tx(() -> {

            Ret ret = formApprovalService.judgeType(table(), iautoid);
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

            // TODO 更新状态

            return true;
        });
        return SUCCESS;
    }

    public Ret withdraw(Long iAutoId) {
        tx(() -> {

            return true;
        });
        return SUCCESS;
    }

    /**
     * 审核通过
     */
    public Ret approve(String ids) {
        tx(() -> {

            return true;
        });
        
        return SUCCESS; 
    }

    public Ret reject(String ids) {

        return SUCCESS;
    }
    
}

