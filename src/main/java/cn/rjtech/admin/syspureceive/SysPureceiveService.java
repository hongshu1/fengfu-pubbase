package cn.rjtech.admin.syspureceive;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.constants.ErrorMsg;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购收料单
 * @ClassName: SysPureceiveService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
public class SysPureceiveService extends BaseService<SysPureceive> {

    private final SysPureceive dao = new SysPureceive().dao();

    @Inject
    private WarehouseService warehouseservice;


    @Override
    protected SysPureceive dao() {
        return dao;
    }

    @Inject
    private SysPureceivedetailService syspureceivedetailservice;

    @Inject
    private RcvDocQcFormMService rcvdocqcformmservice;

    @Inject
    private VendorService vendorservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param BillType   到货单类型;采购PO  委外OM
     * @param IsDeleted  删除状态：0. 未删除 1. 已删除
     * @return
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
     * @param sysPureceive
     * @return
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
     * @param sysPureceive
     * @return
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
     * @param sysPureceive 要删除的model
     * @param kv           携带额外参数一般用不上
     * @return
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
     * @return
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
     * @return
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
     * @param id
     * @return
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
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
            return Ret.msg("行数据不能为空");
        }
        //采购订单推u8才可以添加
        List<Record> list = jBoltTable.getSaveRecordList();
        for (int i = 0; i < list.size(); i++) {
            String sourcebillno1 = list.get(i).getStr("sourcebillno");
            Boolean sourcebillno = this.getsourcebillno(sourcebillno1);
            if(!sourcebillno){
                return Ret.msg("订单编号："+sourcebillno1+" 没有推U8");
            }
        }
        List<Record> list1 = jBoltTable.getUpdateRecordList();
        for (int i = 0; i < list1.size(); i++) {
            String sourcebillno1 = list1.get(i).getStr("sourcebillno");
            Boolean sourcebillno = this.getsourcebillno(sourcebillno1);
            if(!sourcebillno){
                return Ret.msg("订单编号："+sourcebillno1+" 没有推U8");
            }
        }



        SysPureceive sysotherin = jBoltTable.getFormModel(SysPureceive.class, "sysPureceive");
        // 获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            // 通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setCreatePerson(user.getUsername());
                sysotherin.setCreateDate(now);
                sysotherin.setBillDate(dateToString(now));
                sysotherin.setModifyPerson(user.getUsername());
                sysotherin.setState("1");
                sysotherin.setModifyDate(now);
                sysotherin.setBillNo(String.valueOf(JBoltSnowflakeKit.me.nextId()));
                // 主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                sysotherin.setModifyPerson(user.getUsername());
                sysotherin.setModifyDate(now);
                // 主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            // 查出供应商id ---(供应商字段主表去掉)
            Long veniAutoId = null;
            if(null != sysotherin.getVenCode()) {
                veniAutoId = vendorservice.findFirst("select * from  Bd_Vendor where cVenCode = ?", sysotherin.getVenCode()).getIAutoId();
            }
            // 从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin, veniAutoId);
            // 获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin, veniAutoId);
            // 获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            if ("submit".equals(jBoltTable.getForm().getString("operationType"))) {
                sysotherin.setState("2");
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            return true;
        });
        return SUCCESS;
    }

    // 可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysPureceive sysotherin, Long veniAutoId) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();
            Record row = list.get(i);
            row.set("MasID", sysotherin.getAutoID());
            row.set("AutoID", JBoltSnowflakeKit.me.nextId());
            row.set("CreateDate", now);
            row.set("ModifyDate", now);
            if (null == list.get(i).get("isinitial") || "".equals(list.get(i).get("isinitial"))) {
                row.set("IsInitial", false);
                sysPureceivedetail.setIsInitial("0");
            } else {
                //获取供应商字段
                String vencode = row.getStr("vencode");
                veniAutoId = vendorservice.findFirst("select * from  Bd_Vendor where cVenCode = ?", vencode).getIAutoId();
                if(null == vencode){
                    ValidationUtils.assertNull(false, "条码："+row.get("barcode")+" 供应商数据不能为空");
                }
                // 推送初物 PL_RcvDocQcFormM 来料
                this.insertRcvDocQcFormM(row, sysotherin, veniAutoId);
                sysPureceivedetail.setIsInitial("1");
            }
            sysPureceivedetail.setMasID(sysotherin.getAutoID());
            sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));
			// sysPureceivedetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
//            sysPureceivedetail.setWhcode(row.getStr("whcode"));
            sysPureceivedetail.setVenCode(row.getStr("vencode"));
            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setCreateDate(now);
            sysPureceivedetail.setModifyDate(now);
            sysdetaillist.add(sysPureceivedetail);
        }
        syspureceivedetailservice.batchSave(sysdetaillist);
    }

    // 可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysPureceive sysotherin, Long veniAutoId) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();
            Record row = list.get(i);
            row.set("ModifyDate", now);

            if (null == list.get(i).get("isinitial") || "".equals(list.get(i).get("isinitial"))) {
                row.set("IsInitial", false);
            } else {
                // 推送初物 PL_RcvDocQcFormM 来料
                String vencode = row.getStr("vencode");
                veniAutoId = vendorservice.findFirst("select * from  Bd_Vendor where cVenCode = ?", vencode).getIAutoId();
                if(null == vencode){
                    ValidationUtils.assertNull(false, "条码："+row.get("barcode")+" 供应商数据不能为空");
                }
                this.insertRcvDocQcFormM(row, sysotherin, veniAutoId);
                sysPureceivedetail.setIsInitial("1");
            }
            sysPureceivedetail.setMasID(sysotherin.getAutoID());
            sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));
			// sysPureceivedetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
//            sysPureceivedetail.setWhcode(row.getStr("whcode"));
            sysPureceivedetail.setVenCode(row.getStr("vencode"));
            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setCreateDate(now);
            sysPureceivedetail.setModifyDate(now);

            sysdetaillist.add(sysPureceivedetail);

        }
        syspureceivedetailservice.batchUpdate(sysdetaillist);
    }

    // 可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        syspureceivedetailservice.deleteByIds(ids);
    }

    /**
     * 后台管理数据查询
     *
     * @return
     */
    public List<Record> getVenCodeDatas(Kv kv) {
        List<Record> records = dbTemplate("syspureceive.venCode", kv).find();
        return records;
    }


    /**
     * 后台管理数据查询
     *
     * @return
     */
    public List<Record> getWhcodeDatas(Kv kv) {
        return dbTemplate("syspureceive.Whcode", kv).find();
    }
    /**
     * 后台管理数据查询
     *
     * @return
     */
    public List<Record> selectRdCode(Kv kv) {
        return dbTemplate("syspureceive.selectRdCode", kv).find();
    }


    /**
     * 货区
     *
     * @return
     */
    public List<Record> getwareHousepos(Kv kv) {
        return dbTemplate("syspureceive.wareHousepos", kv).find();
    }

    /**
     * 时间转字符串
     */
    public String dateToString(Date date) {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf3.format(date);
    }

    /**
     * 字符串转时间
     */
    public Date stringToDate(String str) throws ParseException {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf3.parse(str);
    }


    /**
     * 推送 PL_RcvDocQcFormM ;//来料检验
     */
    public void insertRcvDocQcFormM(Record row, SysPureceive sys, Long veniAutoId) {
        Date date = new Date();
        RcvDocQcFormM rcvDocQcFormM = new RcvDocQcFormM();
        rcvDocQcFormM.setIOrgId(getOrgId());
        rcvDocQcFormM.setCOrgCode(getOrgCode());
        rcvDocQcFormM.setCOrgName(getOrgName());
        if (null == row.getStr("sourcebillno") || "".equals(row.getStr("sourcebillno"))) {
            rcvDocQcFormM.setCRcvDocQcFormNo("test");
        } else {
            rcvDocQcFormM.setCRcvDocQcFormNo(row.getStr("sourcebillno"));
        }
        // 质检表格ID
        rcvDocQcFormM.setIQcFormId(1L);
        rcvDocQcFormM.setIRcvDocId(Long.valueOf(sys.getAutoID()));
        rcvDocQcFormM.setCRcvDocNo(sys.getBillNo());
        if (null == row.getStr("iinventoryid") || "".equals(row.getStr("iinventoryid"))) {
            rcvDocQcFormM.setIInventoryId(100L);
        } else {
            rcvDocQcFormM.setIInventoryId(Long.valueOf(row.getStr("iinventoryid").trim()));
        }
        rcvDocQcFormM.setIVendorId(veniAutoId);
        rcvDocQcFormM.setDRcvDate(sys.getCreateDate());
        rcvDocQcFormM.setIQty(Double.valueOf(String.valueOf(row.getStr("qty").trim())).intValue());
        // 批次号
        rcvDocQcFormM.setCBatchNo("1111111Test");
        rcvDocQcFormM.setIStatus(0);
        rcvDocQcFormM.setIsCpkSigned(false);
        rcvDocQcFormM.setIMask(2);
        rcvDocQcFormM.setIsCpkSigned(false);
        rcvDocQcFormM.setICreateBy(JBoltUserKit.getUser().getId());
        rcvDocQcFormM.setCCreateName(JBoltUserKit.getUser().getName());
        rcvDocQcFormM.setDCreateTime(date);
        rcvDocQcFormM.setIUpdateBy(JBoltUserKit.getUser().getId());
        rcvDocQcFormM.setCUpdateName(JBoltUserKit.getUser().getName());
        rcvDocQcFormM.setDUpdateTime(date);
        rcvDocQcFormM.setIsDeleted(false);
        rcvdocqcformmservice.save(rcvDocQcFormM);
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode,String vencode) {
        //.set("vencode",vencode)
        return dbTemplate("syspureceive.getBarcodeDatas",Kv.by("q", q).set("limit",limit).set("orgCode",orgCode).set("vencode",vencode)).find();
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public Record barcode(Kv kv) {
        Record first = dbTemplate("syspureceive.barcode", kv).findFirst();
        if(null == first){
            ValidationUtils.assertNull(false, "未查到条码为："+kv.getStr("barcode")+"的数据");
        }
        return first;
    }


    /**
     *  通过采购订单判断u8是否有数据
     *  * @param kv
     * @return
     */
    public Boolean getsourcebillno(String sourcebillno) {
        Kv kv = new Kv();
        kv.set("sourcebillno",sourcebillno);
        List<Record> records = dbTemplate(u8SourceConfigName(), "syspureceive.getsourcebillno", kv).find();
        if(records.isEmpty()){
            return false;
        }
        return true;
    }

    // 业务逻辑发送改变，以下重新开发一个保存功能
    public Ret submit(JBoltTable jBoltTable) {
        String operationType = jBoltTable.getFormRecord().getStr("operationType");
        if(!"submit".equals(operationType)){
            if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
                return Ret.msg("行数据不能为空");
            }
        }
        SysPureceive sysPureceive = jBoltTable.getFormModel(SysPureceive.class, "sysPureceive");
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        if (sysPureceive.getAutoID() != null){
            sysPureceive.setModifyPerson(user.getUsername());
            sysPureceive.setModifyDate(now);
            if("submit".equals(operationType)){
                sysPureceive.setState("2");
            }
            // 主表修改
            sysPureceive.update();
        }
        // 从表的操作 获取新增数据
        saveData(jBoltTable,sysPureceive,operationType);
        // 获取修改数据（执行修改）
        updateData(jBoltTable,sysPureceive,operationType);
        // 获取删除数据（执行删除）
        deleteData(jBoltTable);

        return SUCCESS;
    }
    private void saveData(JBoltTable jBoltTable,SysPureceive sysPureceive,String operationType){
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) return;
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
            //获取供应商字段
            String vencode = row.getStr("vencode");
            if(null == vencode){
                ValidationUtils.assertNull(false, "条码："+row.get("barcode")+" 供应商数据不能为空");
            }
            sysPureceivedetail.setVenCode(vencode);
            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setCreateDate(now);
            sysPureceivedetail.setModifyDate(now);
            String s = this.insertSysPureceive(sysPureceivedetail, sysPureceive, row,operationType);
            sysPureceivedetail.setMasID(s);
            if (null == row.get("isinitial") || "".equals(row.get("isinitial"))) {
                sysPureceivedetail.setIsInitial("0");
            } else {
                Long veniAutoId = vendorservice.findFirst("select * from  Bd_Vendor where cVenCode = ?", vencode).getIAutoId();
                // 推送初物 PL_RcvDocQcFormM 来料
                SysPureceive first = findFirst("select *  from T_Sys_PUReceive where SourceBillNo=?", sysPureceivedetail.getSourceBillNo());
                this.insertRcvDocQcFormM(row, first, veniAutoId);
                sysPureceivedetail.setIsInitial("1");
            }
            sysdetaillist.add(sysPureceivedetail);
        }
        syspureceivedetailservice.batchSave(sysdetaillist);
    }

    private void updateData(JBoltTable jBoltTable,SysPureceive sysPureceive,String operationType){
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
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
            //获取供应商字段
            String vencode = row.getStr("vencode");
            if(null == vencode){
                ValidationUtils.assertNull(false, "条码："+row.get("barcode")+" 供应商数据不能为空");
            }
            sysPureceivedetail.setVenCode(vencode);
            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setModifyDate(now);
            String s = this.insertSysPureceive(sysPureceivedetail, sysPureceive, row,operationType);
            sysPureceivedetail.setMasID(s);
            if (null == row.get("isinitial") || "".equals(row.get("isinitial")) || "0".equals(row.get("isinitial"))) {
                sysPureceivedetail.setIsInitial("0");
            } else {
                Long veniAutoId = vendorservice.findFirst("select * from  Bd_Vendor where cVenCode = ?", vencode).getIAutoId();
                // 推送初物 PL_RcvDocQcFormM 来料
                SysPureceive first = findFirst("select *  from T_Sys_PUReceive where SourceBillNo=?", sysPureceivedetail.getSourceBillNo());
                this.insertRcvDocQcFormM(row, first, veniAutoId);
                sysPureceivedetail.setIsInitial("1");
            }
            sysdetaillist.add(sysPureceivedetail);
        }
        syspureceivedetailservice.batchUpdate(sysdetaillist);
    }

    private void deleteData(JBoltTable jBoltTable){
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        syspureceivedetailservice.deleteByIds(ids);
    }

    // 根据行表数据 通过 供应商(取MES的采购订单)判断头表是否需要添加头表数据，返回 头表id
    public String insertSysPureceive(SysPureceivedetail sysPureceivedetail,SysPureceive sysPureceive,Record record,String operationType){
        SysPureceive first = findFirst("select *  from T_Sys_PUReceive where VenCode=?", sysPureceivedetail.getVenCode());
        if(null != first){
            return first.getAutoID();
        }else {
            User user = JBoltUserKit.getUser();
            Date now = new Date();
            //需要先创建头表数据
            sysPureceive.setSourceBillNo(sysPureceivedetail.getSourceBillNo());
            sysPureceive.setVenCode(sysPureceivedetail.getVenCode());
            Warehouse first1 = warehouseservice.findFirst("select *   from Bd_Warehouse where cWhCode=?", sysPureceive.getWhCode());
            sysPureceive.setWhName(first1.getCWhName());
            sysPureceive.setRdCode(record.getStr("rdcode"));
            sysPureceive.setOrganizeCode(getOrgCode());
            sysPureceive.setCreatePerson(user.getUsername());
            sysPureceive.setCreateDate(now);
            sysPureceive.setBillDate(dateToString(now));
            sysPureceive.setModifyPerson(user.getUsername());
            if("".equals(operationType)){
                //待后续审批流修改状态
                sysPureceive.setState("2");
            }else {
                sysPureceive.setState("1");
            }
            sysPureceive.setModifyDate(now);
            sysPureceive.setBillNo(String.valueOf(JBoltSnowflakeKit.me.nextId()));
            String s = String.valueOf(JBoltSnowflakeKit.me.nextId());
            sysPureceive.setAutoID(s);
            sysPureceive.save();
            return s;
        }
    }



}

