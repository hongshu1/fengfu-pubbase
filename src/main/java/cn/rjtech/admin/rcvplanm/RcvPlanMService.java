package cn.rjtech.admin.rcvplanm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.rcvpland.RcvPlanDService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.cache.FormApprovalCache;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.enums.OrderStatusEnum;
import cn.rjtech.model.momdata.GoodsPaymentD;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.RcvPlanD;
import cn.rjtech.model.momdata.RcvPlanM;
import cn.rjtech.model.momdata.base.BaseRcvPlanD;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发货管理-取货计划主表
 *
 * @ClassName: RcvPlanMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-27 14:09
 */
public class RcvPlanMService extends BaseService<RcvPlanM> implements IApprovalService {

    private final RcvPlanM dao = new RcvPlanM().dao();

    @Override
    protected RcvPlanM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private RcvPlanDService planDService;
    @Inject
    private InventoryService inventoryservice;

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("rcvplanm.getAdminDatas", kv).paginate(pageNumber, pageSize);
        for (Record row : paginate.getList()) {
            // 审核中，并且单据审批方式为审批流
            if (ObjUtil.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), row.getInt(IAUDITSTATUS)) && ObjUtil.equals(AuditWayEnum.FLOW.getValue(), row.getInt(IAUDITWAY))) {
                row.put("approvalusers", FormApprovalCache.ME.getNextApprovalUserNames(row.getLong("iautoid"), 5));
            }
        }
        return paginate;
    }

    /**
     * 保存
     */
    public Ret save(RcvPlanM rcvPlanM, List<RcvPlanD> rcvPlanD) {
        if (rcvPlanM == null || isOk(rcvPlanM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        tx(() -> {
            boolean success = rcvPlanM.save();
            if (rcvPlanD != null) {
                for (int i = 0; i < rcvPlanD.size(); i++) {
                    RcvPlanD rcvPlanD1 = rcvPlanD.get(i);
                    if (!isOk(rcvPlanD1.getIAutoId())) {
                        // 添加从表
//						rcvPlanD.save(rcvPlanD1);
                    }
                }
            }
            return success;
        });

        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(RcvPlanM rcvPlanM) {
        if (rcvPlanM == null || notOk(rcvPlanM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        RcvPlanM dbRcvPlanM = findById(rcvPlanM.getIAutoId());
        if (dbRcvPlanM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(rcvPlanM.getName(), rcvPlanM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = rcvPlanM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(rcvPlanM.getIAutoId(), JBoltUserKit.getUserId(), rcvPlanM.getName());
        }
        return ret(success);
    }

    /**
     * 批量删除
     */
    public Ret deleteBatchByIds(String ids) {
        List<RcvPlanM> rcvPlanMList = getListByIds(ids);
        ArrayList<RcvPlanM> deleteRcvPlanMS = new ArrayList<>();
        List<RcvPlanD> deletePlanDList = new ArrayList<>();
        Date date = new Date();
        for (RcvPlanM planM : rcvPlanMList) {
            commonDeleteMethod(planM, date);
            deleteRcvPlanMS.add(planM);

            List<RcvPlanD> planDList = planDService.findListByMasid(StrUtil.toString(planM.getIAutoId()));
            if (!planDList.isEmpty()){
                deletePlanDList.addAll(planDList);
            }
        }
        tx(() -> {
            //明细表：物理删除
            commonDeleteDetailMethods(deletePlanDList);
            //主表数据：逻辑删除
            ValidationUtils.isTrue(batchUpdate(deleteRcvPlanMS).length > 0, JBoltMsg.FAIL);
            return true;
        });

        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            RcvPlanM planM = findById(id);
            commonDeleteMethod(planM, new Date());

            List<RcvPlanD> planDList = planDService.findListByMasid(StrUtil.toString(planM.getIAutoId()));
            //明细表：物理删除
            commonDeleteDetailMethods(planDList);
            //主表：逻辑删除
            ValidationUtils.isTrue(planM.update(), JBoltMsg.FAIL);
            return true;
        });
        return SUCCESS;
    }

    public void commonDeleteMethod(RcvPlanM planM, Date date) {

        ValidationUtils.equals(planM.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
        ValidationUtils.equals(planM.getIStatus(), OrderStatusEnum.NOT_AUDIT.getValue(), planM.getCRcvPlanNo() + "：非已保存状态!");

        planM.setIsDeleted(true);
        planM.setCUpdateName(JBoltUserKit.getUserName());
        planM.setDUpdateTime(date);
        planM.setIUpdateBy(JBoltUserKit.getUserId());
    }

    public void commonDeleteDetailMethods(List<RcvPlanD> planDList){
        if (planDList.isEmpty()){
            return;
        }
        List<Long> collect = planDList.stream().map(BaseRcvPlanD::getIAutoId).collect(Collectors.toList());
        String ids = collect.stream().map(Object::toString).collect(Collectors.joining(", "));
        planDService.deleteByIds(ids);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param rcvPlanM 要删除的model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(RcvPlanM rcvPlanM, Kv kv) {
        //addDeleteSystemLog(rcvPlanM.getIAutoId(), JBoltUserKit.getUserId(),rcvPlanM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param rcvPlanM model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    public String checkInUse(RcvPlanM rcvPlanM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(RcvPlanM rcvPlanM, String column, Kv kv) {
        //addUpdateSystemLog(rcvPlanM.getIAutoId(), JBoltUserKit.getUserId(), rcvPlanM.getName(),"的字段["+column+"]值:"+rcvPlanM.get(column));
        /**
         switch(column){
         case "IsDeleted":
         break;
         }
         */
        return null;
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        RcvPlanM rcvplanm = jBoltTable.getFormModel(RcvPlanM.class, "rcvplanm");
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            //通过 id 判断是新增还是修改
            if (rcvplanm.getIAutoId() == null) {
                rcvplanm.setIOrgId(getOrgId());
                rcvplanm.setCOrgCode(getOrgCode());
                rcvplanm.setCOrgName(getOrgName());
                rcvplanm.setIStatus(1);
                rcvplanm.setIAuditWay(2);//走审批流
                rcvplanm.setIAuditStatus(0);
                rcvplanm.setICreateBy(user.getId());
                rcvplanm.setCCreateName(user.getName());
                rcvplanm.setDCreateTime(now);
                rcvplanm.setIUpdateBy(user.getId());
                rcvplanm.setCUpdateName(user.getName());
                rcvplanm.setDUpdateTime(now);
                rcvplanm.setIsDeleted(false);
                //主表新增
                ValidationUtils.isTrue(rcvplanm.save(), ErrorMsg.SAVE_FAILED);
            } else {
                rcvplanm.setIUpdateBy(user.getId());
                rcvplanm.setCUpdateName(user.getName());
                rcvplanm.setDUpdateTime(now);
                //主表修改
                ValidationUtils.isTrue(rcvplanm.update(), ErrorMsg.UPDATE_FAILED);
            }
            //从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, rcvplanm);
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, rcvplanm);
            //获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return successWithData(rcvplanm.keep("iautoid"));
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, RcvPlanM rcvplanm) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            /*row.keep("iAutoId", "iRcvPlanMid", "cCarNo", "cPlanCode", "cRcvDate", "cRcvTime", "cBarcode", "cVersion",
                "cAddress", "iInventoryId", "iQty", "IsDeleted", "dCreateTime", "dUpdateTime");*/
            row.keep(ArrayUtil.toArray(TableMapping.me().getTable(RcvPlanD.class).getColumnNameSet(), String.class));
            String cinvcode = row.get("cinvcode");
            Inventory inventory = inventoryservice.findBycInvCode(cinvcode);
            if (inventory ==null){
                ValidationUtils.notNull(inventory, "存货编码不存在,无法生成单据或检验单据！！！");
            }
            row.set("iinventoryid", inventory.getIAutoId());
            row.set("iautoid", JBoltSnowflakeKit.me.nextId());
            row.set("isdeleted", "0");
            row.set("ircvplanmid", rcvplanm.getIAutoId());
            row.set("dcreatetime", now);
            row.set("dupdatetime", now);
        }
        planDService.batchSaveRecords(list);

    }

    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, RcvPlanM rcvplanm) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Date now = new Date();
        for (Record row : list) {
            row.set("dupdatetime", now);
            row.keep(ArrayUtil.toArray(TableMapping.me().getTable(RcvPlanD.class).getColumnNameSet(), String.class));
        }
        planDService.batchUpdateRecords(list);
    }

    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        for (Object id : ids) {
            planDService.deleteById(id);
        }
    }

    public Ret importExcelDatas(List<UploadFile> fileList) {
        for (UploadFile uploadFile : fileList) {

        }
        return null;
    }

    /**
     * 读取excel文件
     */
    public Ret importExcel(File file, String cformatName) {
        //使用字段配置维护
        List<Record> importData = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, cformatName);
        ValidationUtils.notEmpty(importData, "导入数据不能为空");
        
        //执行批量操作
        /*boolean success=tx(() -> true);

        if(!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }*/
        return SUCCESS;
    }

    /**
     * 设置参数
     */
    private GoodsPaymentD setGoodsPaymentDModel(GoodsPaymentD goodsPaymentDModel){
        String cInvCode = goodsPaymentDModel.getCInvCode();
        Inventory inventory = inventoryservice.findFirst("select * from bd_inventory where cInvCode= ? ", cInvCode);
        System.out.println("cInvCode===="+cInvCode);
        goodsPaymentDModel.setIAutoId(JBoltSnowflakeKit.me.nextId());
        goodsPaymentDModel.setIInventoryId(StrUtil.toString(inventory.getIAutoId()));
        return goodsPaymentDModel;
    }

    /*处理审批通过的其他业务操作，如有异常返回错误信息*/
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        RcvPlanM planM = findById(formAutoId);
        comonApproveMethods(planM, OrderStatusEnum.APPROVED.getValue(), new Date());

        ValidationUtils.isTrue(planM.update(), "审批通过失败");
        return null;
    }

    /*处理审批不通过的其他业务操作，如有异常处理返回错误信息*/
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        RcvPlanM planM = findById(formAutoId);
        comonApproveMethods(planM, OrderStatusEnum.REJECTED.getValue(), new Date());
        ValidationUtils.isTrue(planM.update(), "审批不通过失败");
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
        RcvPlanM planM = findById(formAutoId);
        comonApproveMethods(planM, OrderStatusEnum.NOT_AUDIT.getValue(), new Date());

        ValidationUtils.isTrue(planM.update(), "反审批失败");
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
        RcvPlanM planM = findById(formAutoId);
        planM.setIStatus(OrderStatusEnum.AWAIT_AUDIT.getValue());
        ValidationUtils.isTrue(planM.update(), "提交审批失败");
        return null;
    }

    /*撤回审核业务处理，如有异常返回错误信息*/
    @Override
    public String postWithdrawFunc(long formAutoId) {
        RcvPlanM planM = findById(formAutoId);
        comonApproveMethods(planM, OrderStatusEnum.NOT_AUDIT.getValue(), new Date());
        ValidationUtils.isTrue(planM.update(), "撤回失败！！");
        return null;
    }

    /*从待审批中，撤回到已保存，业务实现，如有异常返回错误信息*/
    @Override
    public String withdrawFromAuditting(long formAutoId) {
        //comonWithDraw(formAutoId, OrderStatusEnum.NOT_AUDIT.getValue());
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
        ArrayList<RcvPlanM> rcvPlanMS = new ArrayList<>();
        for (Long iautoid : formAutoIds) {
            RcvPlanM planM = findById(iautoid);
            comonApproveMethods(planM, OrderStatusEnum.APPROVED.getValue(), date);
            rcvPlanMS.add(planM);
        }
        ValidationUtils.isTrue(batchUpdate(rcvPlanMS).length > 0, "批量审批通过失败");
        return null;
    }

    /*批量审批（审核）不通过，后置业务实现*/
    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        Date date = new Date();
        List<RcvPlanM> planMList = new ArrayList<>();
        for (Long iautoid : formAutoIds) {
            RcvPlanM planM = findById(iautoid);
            comonApproveMethods(planM, OrderStatusEnum.REJECTED.getValue(), date);
            planMList.add(planM);
        }
        ValidationUtils.isTrue(batchUpdate(planMList).length > 0, "批量审批不通过失败");
        return null;
    }

    /*批量撤销审批，后置业务实现*/
    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        Date date = new Date();
        List<RcvPlanM> planMList = new ArrayList<>();
        for (Long iautoid : formAutoIds) {
            RcvPlanM planM = findById(iautoid);
            comonApproveMethods(planM, OrderStatusEnum.NOT_AUDIT.getValue(), date);
            planMList.add(planM);
        }
        ValidationUtils.isTrue(batchUpdate(planMList).length > 0, "批量撤销审批失败");
        return null;
    }

    /*
     * 公共审批方法
     * */
    public void comonApproveMethods(RcvPlanM planM, int status, Date date) {
        planM.setIStatus(status);
        planM.setIUpdateBy(JBoltUserKit.getUserId());
        planM.setDUpdateTime(date);
        planM.setCUpdateName(JBoltUserKit.getUserName());
    }
}