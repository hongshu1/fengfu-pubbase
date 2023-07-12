package cn.rjtech.admin.syssaledeliverplan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.saletype.SaleTypeService;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.syssaledeliverplandetail.SysSaledeliverplandetailService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.enums.OrderStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.base.BaseSysSaledeliverplandetail;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.u9.entity.saledeliverplan.SaleDeliverPlanDTO;
import cn.rjtech.u9.entity.saledeliverplan.SaleDeliverPlanDTO.Main;
import cn.rjtech.u9.entity.saledeliverplan.SaleDeliverPlanDTO.PreAllocate;
import cn.rjtech.u9.entity.saledeliverplan.SaleDeliverPlanDeleteDTO;
import cn.rjtech.util.BaseInU8Util;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 销售出货(计划)
 *
 * @ClassName: SysSaledeliverplanService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 10:01
 */
public class SysSaledeliverplanService extends BaseService<SysSaledeliverplan> implements IApprovalService {

    private final SysSaledeliverplan dao = new SysSaledeliverplan().dao();

    @Override
    protected SysSaledeliverplan dao() {
        return dao;
    }

    @Inject
    private SysSaledeliverplandetailService syssaledeliverplandetailservice;
    @Inject
    private SysPuinstoreService             puinstoreService;
    @Inject
    private DictionaryService               dictionaryService;
    @Inject
    private SaleTypeService                 saleTypeService;
    @Inject
    private CustomerService                 customerService;
    @Inject
    private InventoryService                inventoryService;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("syssaledeliverplan.syssaledeliverplanList", kv).paginate(pageNumber, pageSize);
        for (Record record : paginate.getList()) {
            if (ObjUtil.equal(record.getStr("sourcebilltype"), "手工新增")) {
                Record corderReocrd = findCOrderNoBySourceBillId(Kv.by("iautoid", record.get("sourcebillid")));
                record.set("corderno", corderReocrd.getStr("corderno"));
                record.set("billtype", findIBusTypeKey(record.getStr("billtype"), "purchase_service_type"));
            }
        }
        return paginate;
    }

    public Record findCOrderNoBySourceBillId(Kv kv) {
        return dbTemplate("syssaledeliverplan.findCOrderNoBySourceBillId", kv)
            .findFirst();
    }

    /*
     * 字典维护的数据
     * */
    public String findIBusTypeKey(String billtype, String key) {
        if (StrUtil.isNotBlank(billtype)) {
            List<Dictionary> dictionaryList = dictionaryService.getOptionListByTypeKey(key);
            Dictionary dictionary = dictionaryList.stream().filter(e -> e.getSn().equals(billtype))
                .findFirst()
                .orElse(new Dictionary());
            return dictionary.getName();

        }
        return "";
    }

    /**
     * 保存
     */
    public Ret save(SysSaledeliverplan sysSaledeliverplan) {
        if (sysSaledeliverplan == null || isOk(sysSaledeliverplan.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // if(existsName(sysSaledeliverplan.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplan.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplan.getName());
        }
        return ret(success);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            List<SysSaledeliverplan> saledeliverplanList = getListByIds(ids);
            Date date = new Date();
            List<SysSaledeliverplan> deleteSaledeliverplanList = new ArrayList<>();
            List<SysSaledeliverplandetail> deleteSaledeliverplanDetailList = new ArrayList<>();
            for (SysSaledeliverplan saledeliverplan : saledeliverplanList) {
                commonDeleteMethods(saledeliverplan, date);
                deleteSaledeliverplanList.add(saledeliverplan);

                List<SysSaledeliverplandetail> saledeliverplandetailList = syssaledeliverplandetailservice
                    .findListByMasid(saledeliverplan.getAutoID());
                deleteSaledeliverplanDetailList.addAll(saledeliverplandetailList);
            }
            //明细表：物理删除
            commonDetailDeleteMethods(deleteSaledeliverplanDetailList);
            //主表数据：逻辑删除
            ValidationUtils.isTrue(batchUpdate(deleteSaledeliverplanList).length > 0, "批量删除失败");

            return true;
        });
        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            SysSaledeliverplan saledeliverplan = findById(id);
            commonDeleteMethods(saledeliverplan, new Date());

            List<SysSaledeliverplandetail> saledeliverplandetailList = syssaledeliverplandetailservice
                .findListByMasid(saledeliverplan.getAutoID());
            //明细表：物理删除
            commonDetailDeleteMethods(saledeliverplandetailList);
            //主表数据：逻辑删除
            ValidationUtils.isTrue(saledeliverplan.update(), "删除失败");
            return true;
        });
        return SUCCESS;
    }

    /*
     * 主表公共删除方法
     * */
    public void commonDeleteMethods(SysSaledeliverplan saledeliverplan, Date date) {
        ValidationUtils.equals(saledeliverplan.getIcreateby(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
        ValidationUtils.equals(saledeliverplan.getIAuditStatus(), OrderStatusEnum.NOT_AUDIT.getValue(),
            saledeliverplan.getBillNo() + "：非已保存状态，不能删除!");

        saledeliverplan.setIsDeleted(true);
        saledeliverplan.setCupdatename(JBoltUserKit.getUserName());
        saledeliverplan.setDupdatetime(date);
        saledeliverplan.setIupdateby(JBoltUserKit.getUserId());
    }

    /*
     * 明细表公共删除方法
     * */
    public void commonDetailDeleteMethods(List<SysSaledeliverplandetail> saledeliverplandetailList) {
        if (saledeliverplandetailList.isEmpty()) {
            return;
        }
        String ids = saledeliverplandetailList.stream().map(BaseSysSaledeliverplandetail::getAutoID)
            .collect(Collectors.joining(","));
        syssaledeliverplandetailservice.deleteByIds(ids);
    }

    /**
     * 更新
     */
    public Ret update(SysSaledeliverplan sysSaledeliverplan) {
        if (sysSaledeliverplan == null || notOk(sysSaledeliverplan.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysSaledeliverplan dbSysSaledeliverplan = findById(sysSaledeliverplan.getAutoID());
        if (dbSysSaledeliverplan == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        // if(existsName(sysSaledeliverplan.getName(), sysSaledeliverplan.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplan.update();
        if (success) {
            // 添加日志
            // addUpdateSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplan.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysSaledeliverplan 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysSaledeliverplan sysSaledeliverplan, Kv kv) {
        // addDeleteSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(),sysSaledeliverplan.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysSaledeliverplan model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysSaledeliverplan sysSaledeliverplan, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        SysSaledeliverplan sysotherin = jBoltTable.getFormModel(SysSaledeliverplan.class, "syssaledeliverplan");
        if (null == sysotherin) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }
        Record formRecord = jBoltTable.getFormRecord();
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            //通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setIcreateby(user.getId());
                sysotherin.setCcreatename(user.getName());
                sysotherin.setDcreatetime(now);
                sysotherin.setSourceBillType("手工新增");
                sysotherin.setSourceBillID(formRecord.getStr("sourcebillid"));//来源id
                sysotherin.setIsDeleted(false);
                sysotherin.setIAuditWay(AuditWayEnum.STATUS.getValue());
                sysotherin.setICustomerId(formRecord.getStr("icustomerid"));
                saveSaleDeliverPlanModel(sysotherin, formRecord, now, user);
                //主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                saveSaleDeliverPlanModel(sysotherin, formRecord, now, user);
                //主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            List<Record> savelist = jBoltTable.getSaveRecordList();
            List<Record> updatelist = jBoltTable.getUpdateRecordList();
            Object[] ids = jBoltTable.getDelete();
            //从表的操作
            // 保存
            saveTableSubmitDatas(jBoltTable, sysotherin, user, now);
            //更新
            updateTableSubmitDatas(jBoltTable, sysotherin, user, now);
            //删除
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return successWithData(sysotherin.keep("autoid"));
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysSaledeliverplan sysotherin, User user, Date now) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        ArrayList<SysSaledeliverplandetail> sysproductindetail = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysSaledeliverplandetail sysdetail = new SysSaledeliverplandetail();
            sysdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            sysdetail.setBarcode(row.getStr("barcode"));
            sysdetail.setInvCode(row.getStr("cinvcode"));
            sysdetail.setWhCode(row.getStr("whcode"));
            sysdetail.setQty(row.getBigDecimal("qty"));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setIsDeleted(false);
            sysdetail.setIcreateby(user.getId());
            sysdetail.setCcreatename(user.getName());
            sysdetail.setDcreatetime(now);
            sysdetail.setSourceBillID(sysotherin.getSourceBillID());
            saveSaleDeliverPlanDetailModel(sysdetail, row, now, user, i);
            //
            sysproductindetail.add(sysdetail);
        }
        syssaledeliverplandetailservice.batchSave(sysproductindetail);
    }

    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysSaledeliverplan sysotherin, User user, Date now) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        ArrayList<SysSaledeliverplandetail> sysproductindetail = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysSaledeliverplandetail sysdetail = new SysSaledeliverplandetail();
            sysdetail.setAutoID(row.getStr("autoid"));
            sysdetail.setBarcode(row.getStr("barcode"));
            sysdetail.setInvCode(row.getStr("cinvcode"));
            sysdetail.setWhCode(row.getStr("whcode"));
            sysdetail.setQty(row.getBigDecimal("qty"));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysotherin.setIupdateby(user.getId());
            sysotherin.setCupdatename(user.getName());
            sysotherin.setDupdatetime(now);
            sysproductindetail.add(sysdetail);
        }
        syssaledeliverplandetailservice.batchUpdate(sysproductindetail);
    }

    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        syssaledeliverplandetailservice.deleteByIds(ids);
    }

    public void saveSaleDeliverPlanModel(SysSaledeliverplan sysotherin, Record formRecord, Date now, User user) {
        sysotherin.setRdCode(formRecord.getStr("rdcode"));
        sysotherin.setBillNo(formRecord.getStr("billno"));
        sysotherin.setBillType(formRecord.getStr("billtype"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        sysotherin.setBillDate(format.format(now));
        sysotherin.setShipAddress(formRecord.getStr("shipaddress"));
        sysotherin.setDeptCode(formRecord.getStr("deptcode"));
        sysotherin.setExchName(formRecord.getStr("exchname"));
        sysotherin.setExchRate(formRecord.getBigDecimal("exchrate"));
        sysotherin.setTaxRate(formRecord.getBigDecimal("taxrate"));
//                sysotherin.setReceiveAddress(formRecord.getStr(""));//收获地址
//                sysotherin.setVenCode(formRecord.getStr(""));//供应商编码
        sysotherin.setPreDeliverDate(formRecord.getStr("predeliverdate"));//发货日期
        sysotherin.setInvoice(formRecord.getStr("invoice"));
        sysotherin.setMemo(formRecord.getStr("memo"));
        sysotherin.setCondition(formRecord.getStr("condition"));
        sysotherin.setIssue(formRecord.getStr("issue"));
        sysotherin.setIupdateby(user.getId());
        sysotherin.setCupdatename(user.getName());
        sysotherin.setDupdatetime(now);
    }

    public void saveSaleDeliverPlanDetailModel(SysSaledeliverplandetail detail, Record row, Date now, User user, int i) {
        detail.setSourceBillType(row.getStr("sourcebilltype"));
        detail.setSourceBillNo(row.getStr("sourcebillno"));
        detail.setSourceBillDid(row.getStr("sourcebilldid"));
        detail.setIupdateby(user.getId());
        detail.setCupdatename(user.getName());
        detail.setDupdatetime(now);
        detail.setSourceBillDid(null);
        detail.setSourceBIllNoRow(StrUtil.toString(i));
        detail.setSourceBillNo(null);
        detail.setSourceBillType("手工新增");
        detail.setPredictDeliverDate(row.getDate("predictdeliverdate"));
        detail.setPosCode(row.getStr("careacode"));
        detail.setInvCode(row.getStr("invcode"));
    }

    public Record findRecordByAutoid(Kv kv) {
        return dbTemplate("syssaledeliverplan.findRecordByAutoid", kv).findFirst();
    }

    public Page<Record> findTableDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("syssaledeliverplan.findTableDatas", kv.set("orgcode", getOrgCode()))
            .paginate(pageNumber, pageSize);
        return paginate;
    }


    public List<Record> department(Kv kv) {
        return dbTemplate("syssaledeliverplan.department", kv).find();
    }

    public List<Record> customeraddr(Kv kv) {
        return dbTemplate("syssaledeliverplan.customeraddr", kv).find();
    }

    public List<Record> rdstyle(Kv kv) {
        return dbTemplate("syssaledeliverplan.RdStyle", kv).find();
    }

    public List<Record> settlestyle(Kv kv) {
        return dbTemplate("syssaledeliverplan.settlestyle", kv).find();
    }

    public Object foreigncurrency(Kv kv) {
        return dbTemplate("syssaledeliverplan.foreigncurrency", kv).find();
    }

    public Page<Record> getSaleDeliverBillNoList(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("syssaledeliverplan.getSaleDeliverBillNoList", kv).paginate(pageNumber, pageSize);

        return paginate;
    }

    /*
     * 根据barcode加载数据
     * */
    public List<Record> getBarcodeDatas(String q, Kv kv) {
        List<Record> recordList = new ArrayList<>();
       /* if (kv.get("sourcebillid") != null) {
            kv.set("sourcebillid", kv.get("sourcebillid"));
            List<Record> list = dbTemplate("syssaledeliverplan.selectInvocodeByMaskid", kv).find();
            String cinvcodes = list.stream().map(e -> e.getStr("cinvcode")).collect(Collectors.joining(","));
            recordList = dbTemplate("syssaledeliverplan.scanBarcode",
                Kv.by("q", q).set("limit", 20).set("orgCode", getOrgCode()).set("cinvcodes", cinvcodes)).find();
        } else {
            recordList = dbTemplate("syssaledeliverplan.scanBarcode",
                Kv.by("q", q).set("limit", 20).set("orgCode", getOrgCode())).find();
        }*/
        recordList = dbTemplate("syssaledeliverplan.scanBarcode",
            Kv.by("q", q).set("limit", 20).set("orgCode", getOrgCode())).find();
        return recordList;
    }

    public List<Record> scanBarcode(Kv kv) {
        List<SysSaledeliverplandetail> listByBarcode = syssaledeliverplandetailservice.findListByBarcode(kv.getStr("barcode"));
        ValidationUtils.isTrue(listByBarcode.isEmpty(), kv.getStr("barcode") + "：已在其他单维护，不需要重复维护");
        List<Record> recordList = dbTemplate("syssaledeliverplan.scanBarcode", kv.set("orgCode", getOrgCode())).find();
        ValidationUtils.isTrue(!recordList.isEmpty(), "现品票不存在");

        return recordList;
    }

    /*
     * 根据invcode加载数据
     * */
    public List<Record> getDatasByInvcode(String q, Kv kv) {
        List<Record> recordList = new ArrayList<>();
        if (kv.get("sourcebillid") != null) {
            kv.set("sourcebillid", kv.get("sourcebillid"));
            List<Record> list = dbTemplate("syssaledeliverplan.selectInvocodeByMaskid", kv).find();
            String cinvcodes = list.stream().map(e -> e.getStr("cinvcode")).collect(Collectors.joining(","));
            recordList = dbTemplate("syssaledeliverplan.scanInvcode",
                Kv.by("q", q).set("limit", 20).set("orgCode", getOrgCode()).set("cinvcodes", cinvcodes)).find();
        } else {
            recordList = dbTemplate("syssaledeliverplan.scanInvcode",
                Kv.by("q", q).set("limit", 20).set("orgCode", getOrgCode())).find();
        }
        return recordList;
    }

    /*处理审批通过的其他业务操作，如有异常返回错误信息*/
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);
        commonApproveFunc(saledeliverplan, new Date());
        ValidationUtils.isTrue(saledeliverplan.update(), "审批通过失败");
        return null;
    }

    /*处理审批不通过的其他业务操作，如有异常处理返回错误信息*/
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);

        return null;
    }

    /*实现反审之前的其他业务操作，如有异常返回错误信息*/
    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /*实现反审之后的其他业务操作, 如有异常返回错误信息*/
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        if (isLast) {
            commonReverseApproveFunc(StrUtil.toString(formAutoId));
        }
        return null;
    }

    /*提审前业务，如有异常返回错误信息*/
    @Override
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    /*提审后业务处理，如有异常返回错误信息*/
    @Override
    public String postSubmitFunc(long formAutoId) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);

        return null;
    }

    /*撤回审核业务处理，如有异常返回错误信息*/
    @Override
    public String postWithdrawFunc(long formAutoId) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);
        comonApproveMethods(saledeliverplan, new Date());

        ValidationUtils.isTrue(saledeliverplan.update(), "撤回失败");
        return null;
    }

    /*从待审批中，撤回到已保存，业务实现，如有异常返回错误信息*/
    @Override
    public String withdrawFromAuditting(long formAutoId) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);
        comonApproveMethods(saledeliverplan, new Date());

        ValidationUtils.isTrue(saledeliverplan.update(), "撤回失败");
        return null;
    }

    /*从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息*/
    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /*从已审核，撤回到已保存，业务实现，如有异常返回错误信息*/
    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /*批量审核（审批）通过，后置业务实现*/
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        Date date = new Date();
        ArrayList<SysSaledeliverplan> sysSaledeliverplans = new ArrayList<>();
        for (Long autoid : formAutoIds) {
            SysSaledeliverplan saledeliverplan = findById(autoid);
            commonApproveFunc(saledeliverplan, date);
            sysSaledeliverplans.add(saledeliverplan);
        }
        ValidationUtils.isTrue(batchUpdate(sysSaledeliverplans).length > 0, "批量审批通过失败！");
        return null;
    }

    /*批量审批（审核）不通过，后置业务实现*/
    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        Date date = new Date();
        ArrayList<SysSaledeliverplan> sysSaledeliverplans = new ArrayList<>();
        for (Long autoid : formAutoIds) {
            SysSaledeliverplan saledeliverplan = findById(autoid);
            comonApproveMethods(saledeliverplan, date);
            sysSaledeliverplans.add(saledeliverplan);
        }
        ValidationUtils.isTrue(batchUpdate(sysSaledeliverplans).length > 0, "批量审批不通过失败！");
        return null;
    }

    /*批量撤销审批，后置业务实现*/
    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        Date date = new Date();
        List<SysSaledeliverplandetail> saledeliverplandetailList = new ArrayList<>();
        return null;
    }

    /*
     * 公共审批通过方法
     * */
    public void commonApproveFunc(SysSaledeliverplan saledeliverplan, Date date){
        //2、同步于U8
        String json = getSaledeliverplanDto(saledeliverplan, date);
        String u8Billno = new BaseInU8Util().base_in(json);
        //3、如果成功，给u8的单据号；不成功，把单据号置为空，状态改为审核不通过
        saledeliverplan.setU8BillNo(u8Billno);
        comonApproveMethods(saledeliverplan, date);
    }

    /*
     * 公共方法
     * */
    public void comonApproveMethods(SysSaledeliverplan saledeliverplan, Date date) {
        saledeliverplan.setDupdatetime(date);
        saledeliverplan.setIupdateby(JBoltUserKit.getUserId());
        saledeliverplan.setCupdatename(JBoltUserKit.getUserName());
    }

    /*
     * 公共反审批的方法
     * */
    public String commonReverseApproveFunc(String autoid) {
        Date date = new Date();
        SysSaledeliverplan saledeliverplan = findById(autoid);
        List<SysSaledeliverplan> saledeliverplanList = new ArrayList<>();
        //打u8接口，通知u8删除单据，然后更新mom平台的数据
        String json = getSaledeliverplanDeleteDTO(saledeliverplan.getU8BillNo());
        try {
            String post = new BaseInU8Util().deleteVouchProcessDynamicSubmitUrl(json);
            LOG.info(post);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        //
        User user = JBoltUserKit.getUser();
        saledeliverplan.setU8BillNo(null);//将u8的单据号置为空
        saledeliverplan.setDAuditTime(date);
        saledeliverplan.setIAuditby(user.getId());
        saledeliverplan.setCAuditname(user.getUsername());
        comonApproveMethods(saledeliverplan, date);
        saledeliverplanList.add(saledeliverplan);
        //更新
        batchUpdate(saledeliverplanList);
        return null;
    }

    /*
     * 获取请求u8的json
     * */
    public String getSaledeliverplanDto(SysSaledeliverplan saledeliverplan, Date date) {

        List<Main> mains = new ArrayList<>();
        User user = JBoltUserKit.getUser();
        List<SysSaledeliverplandetail> detailList = syssaledeliverplandetailservice.findListByMasid(saledeliverplan.getAutoID());
        Customer customer = customerService.findById(saledeliverplan.getICustomerId());
        int i = 1;
        for (SysSaledeliverplandetail detail : detailList) {
            Main main = new Main();
            main.setBarcode(detail.getBarcode());
            main.setBillDate(saledeliverplan.getBillDate());
            main.setBilldid(detail.getAutoID());
            main.setBillid(saledeliverplan.getAutoID());
            main.setBillno(saledeliverplan.getBillNo());
            main.setBillnorow(saledeliverplan.getBillNo() + "-" + i);
            main.setBillrowno(i);
            main.setCuscode(customer.getCCusCode());
            main.setCusname(customer.getCCusName());
            main.setIndex(i);
            main.setInvcode(detail.getInvCode());
            Inventory inventory = inventoryService.findBycInvCode(detail.getInvCode());
            if (inventory != null) {
                main.setInvname(inventory.getCInvName());
                main.setInvstd(inventory.getCInvStd());
            }
            main.setOdeptcode(saledeliverplan.getDeptCode());
            main.setOposcode(detail.getPosCode());
            main.setoRdType(saledeliverplan.getRdCode());
            main.setOrganizeCode(saledeliverplan.getOrganizeCode());
            main.setOwhcode(detail.getWhCode());
            main.setQty(detail.getQty().intValue());
            main.setSourcebilldid(detail.getSourceBillDid());
            main.setSourcebillno(detail.getSourceBillNo());
            main.setSourcebillnorow(detail.getSourceBillNo() + "-" + i);
            main.setSourcebillrowno(i);
            main.setTag("SaleDispatchMES");
            main.setUserCode(user.getUsername());
            mains.add(main);
        }
        SaleDeliverPlanDTO dto = new SaleDeliverPlanDTO();
        PreAllocate preAllocate = new PreAllocate();
        preAllocate.setCreatePerson(user.getUsername());
        preAllocate.setCreatePersonName(saledeliverplan.getCcreatename());
        preAllocate.setLoginDate(DateUtil.formatDate(saledeliverplan.getDcreatetime()));
        preAllocate.setOrganizeCode(saledeliverplan.getOrganizeCode());
        preAllocate.setTag("SaleDispatchMES");
        preAllocate.setType("SaleDispatchMES");
        preAllocate.setUserCode(user.getUsername());
        //放入dto
        dto.setMainData(mains);
        dto.setPreAllocate(preAllocate);
        dto.setUserCode(user.getUsername());
        dto.setOrganizeCode(saledeliverplan.getOrganizeCode());
        dto.setToken("");
        return JSON.toJSONString(dto);
    }

    /*
     * 获取删除的json
     * */
    public String getSaledeliverplanDeleteDTO(String u8billno) {
        User user = JBoltUserKit.getUser();

        Record userRecord = puinstoreService.findU8UserByUserCode(user.getUsername());
        Record u8Record = puinstoreService.findU8RdRecord01Id(u8billno);

        SaleDeliverPlanDeleteDTO deleteDTO = new SaleDeliverPlanDeleteDTO();
        SaleDeliverPlanDeleteDTO.data data = new SaleDeliverPlanDeleteDTO.data();
        data.setAccid(getOrgCode());
        data.setPassword(userRecord.get("u8_pwd"));
        data.setUserID(userRecord.get("u8_code"));
        Long id = u8Record.getLong("id");
        data.setVouchId(String.valueOf(id));//u8单据id
        deleteDTO.setData(data);

        return JSON.toJSONString(deleteDTO);
    }

    /**
     * 根据客户订单-委外销售订单生成数据
     */
    public Boolean saveBySubcontractSaleOrderDatas(Subcontractsaleorderm subcontractsaleorderm,
                                                   List<Subcontractsaleorderd> subcontractsaleorderds) {
        return tx(() -> {
            Dictionary businessType = dictionaryService.getOptionListByTypeKey("order_business_type").stream()
                .filter(item -> StrUtil.equals(item.getSn(), subcontractsaleorderm.getIBusType().toString())).findFirst()
                .orElse(null);
            String busName = Optional.ofNullable(businessType).map(Dictionary::getName).orElse("普通销售");
            String cSTCode = Optional.ofNullable(saleTypeService.findById(subcontractsaleorderm.getISaleTypeId()))
                .map(SaleType::getCSTCode).orElse("普通销售");
            SysSaledeliverplan sysSaledeliverplan = new SysSaledeliverplan();
            sysSaledeliverplan.setSourceBillType("委外销售订单");
            sysSaledeliverplan.setSourceBillID(subcontractsaleorderm.getIAutoId().toString());
            sysSaledeliverplan.setRdCode(cSTCode);
            sysSaledeliverplan.setOrganizeCode(getOrgCode());
            sysSaledeliverplan.setBillNo(BillNoUtils.getcDocNo(getOrgId(), "FH", 6));
            sysSaledeliverplan.setBillType(busName);
            sysSaledeliverplan.setBillDate(DateUtils.getDate());
            sysSaledeliverplan.setDeptCode(
                Optional.ofNullable(customerService.findById(subcontractsaleorderm.getICustomerId())).map(Customer::getCCusCode)
                    .orElse(null));
            sysSaledeliverplan.setExchName(subcontractsaleorderm.getCCurrency());
            sysSaledeliverplan.setExchRate(subcontractsaleorderm.getIExchangeRate());
            sysSaledeliverplan.setTaxRate(subcontractsaleorderm.getITaxRate());
            sysSaledeliverplan.setCondition(subcontractsaleorderm.getCPaymentTerm());
            sysSaledeliverplan
                .setICustomerId(Optional.ofNullable(subcontractsaleorderm.getICustomerId()).map(Object::toString).orElse(null));
            sysSaledeliverplan.setIsDeleted(false);
            sysSaledeliverplan.setIcreateby(JBoltUserKit.getUserId());
            sysSaledeliverplan.setCcreatename(JBoltUserKit.getUserName());
            sysSaledeliverplan.setDcreatetime(new Date());
            sysSaledeliverplan.setIupdateby(JBoltUserKit.getUserId());
            sysSaledeliverplan.setCupdatename(JBoltUserKit.getUserName());
            sysSaledeliverplan.setDupdatetime(new Date());
            sysSaledeliverplan.save();

            List<SysSaledeliverplandetail> sysSaledeliverplandetails = new ArrayList<>();
            for (Subcontractsaleorderd subcontractsaleorderd : subcontractsaleorderds) {
                for (int i = 1; i <= 31; i++) {
                    SysSaledeliverplandetail sysSaledeliverplandetail = new SysSaledeliverplandetail();

                    String row = (i > 9 ? i + "" : "0" + i);
                    String data = subcontractsaleorderm.getIYear() + "-" + subcontractsaleorderm.getIMonth() + "-" + row;
                    Date predictDeliverDate = DateUtils.parseDate(data);
                    sysSaledeliverplandetail.setMasID(sysSaledeliverplan.getAutoID());
                    sysSaledeliverplandetail.setInvCode(subcontractsaleorderd.getCInvCode());
                    sysSaledeliverplandetail.setQty(subcontractsaleorderd.getBigDecimal("iqty" + i));
                    sysSaledeliverplandetail.setIsDeleted(false);
                    sysSaledeliverplandetail.setSourceBillType("委外销售订单");
                    sysSaledeliverplandetail.setSourceBillNo(subcontractsaleorderm.getCOrderNo());
                    sysSaledeliverplandetail.setSourceBIllNoRow(subcontractsaleorderm.getCOrderNo() + "-" + (i * 10));
                    sysSaledeliverplandetail.setSourceBillDid(subcontractsaleorderd.getIAutoId().toString());
                    sysSaledeliverplandetail.setIcreateby(JBoltUserKit.getUserId());
                    sysSaledeliverplandetail.setCcreatename(JBoltUserKit.getUserName());
                    sysSaledeliverplandetail.setDcreatetime(new Date());
                    sysSaledeliverplandetail.setIupdateby(JBoltUserKit.getUserId());
                    sysSaledeliverplandetail.setCupdatename(JBoltUserKit.getUserName());
                    sysSaledeliverplandetail.setDupdatetime(new Date());
                    sysSaledeliverplandetail.setPredictDeliverDate(predictDeliverDate);
                    sysSaledeliverplandetails.add(sysSaledeliverplandetail);
                }
            }
            syssaledeliverplandetailservice.batchSave(sysSaledeliverplandetails);
            return true;
        });
    }
}