package cn.rjtech.admin.annualorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.annualorderd.AnnualOrderDService;
import cn.rjtech.admin.annualorderdqty.AnnualorderdQtyService;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.MonthOrderStatusEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.AnnualOrderD;
import cn.rjtech.model.momdata.AnnualOrderM;
import cn.rjtech.model.momdata.AnnualorderdQty;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.github.javaparser.utils.Log;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 年度计划订单
 *
 * @ClassName: AnnualOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 17:23
 */
public class AnnualOrderMService extends BaseService<AnnualOrderM> implements IApprovalService {

    private final AnnualOrderM dao = new AnnualOrderM().dao();

    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private AnnualOrderDService annualOrderDService;
    @Inject
    private AnnualorderdQtyService annualorderdQtyService;

    @Override
    protected AnnualOrderM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        para.set("iorgid", getOrgId());

        return dbTemplate("annualorderm.paginateAdminDatas", para).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     *
     * @param annualOrderM
     * @return
     */
    public Ret save(AnnualOrderM annualOrderM) {
        if (annualOrderM == null || isOk(annualOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(annualOrderM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = annualOrderM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(annualOrderM.getIAutoId(), JBoltUserKit.getUserId(), annualOrderM.getName());
        }
        return ret(success);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        AnnualOrderM annualOrderM = findById(id);
        ValidationUtils.equals(annualOrderM.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
        return updateColumn(id, "isdeleted", true);
    }

    /**
     * 更新
     *
     * @param annualOrderM
     * @return
     */
    public Ret update(AnnualOrderM annualOrderM) {
        if (annualOrderM == null || notOk(annualOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        AnnualOrderM dbAnnualOrderM = findById(annualOrderM.getIAutoId());
        if (dbAnnualOrderM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(annualOrderM.getName(), annualOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = annualOrderM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(annualOrderM.getIAutoId(), JBoltUserKit.getUserId(), annualOrderM.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param annualOrderM 要删除的model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(AnnualOrderM annualOrderM, Kv kv) {
        //addDeleteSystemLog(annualOrderM.getIAutoId(), JBoltUserKit.getUserId(),annualOrderM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param annualOrderM model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(AnnualOrderM annualOrderM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 执行JBoltTable表格整体提交
     *
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        AnnualOrderM annualOrderM = jBoltTable.getFormModel(AnnualOrderM.class, "annualOrderM");
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            if (annualOrderM.getIAutoId() == null) {
                annualOrderM.setIOrgId(getOrgId());
                annualOrderM.setCOrgCode(getOrgCode());
                annualOrderM.setCOrgName(getOrgName());
                annualOrderM.setCOrderNo(BillNoUtils.genCode(getOrgCode(), table()));
                annualOrderM.setICreateBy(user.getId());
                annualOrderM.setCCreateName(user.getName());
                annualOrderM.setDCreateTime(now);
                annualOrderM.setIUpdateBy(user.getId());
                annualOrderM.setCUpdateName(user.getName());
                annualOrderM.setDUpdateTime(now);
                ValidationUtils.isTrue(annualOrderM.save(), ErrorMsg.SAVE_FAILED);
            } else {
                annualOrderM.setIUpdateBy(user.getId());
                annualOrderM.setCUpdateName(user.getName());
                annualOrderM.setDUpdateTime(now);
                ValidationUtils.isTrue(annualOrderM.update(), ErrorMsg.UPDATE_FAILED);
            }
            saveTableSubmitDatas(jBoltTable, annualOrderM);
            updateTableSubmitDatas(jBoltTable, annualOrderM);
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return successWithData(annualOrderM.keep("iautoid"));
    }

    /**
     * 可编辑表格提交-新增数据
     */
    private void saveTableSubmitDatas(JBoltTable jBoltTable, AnnualOrderM annualOrderM) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (Record record : list) {
            AnnualOrderD annualOrderD = new AnnualOrderD();

            annualOrderD.setIAnnualOrderMid(annualOrderM.getIAutoId());
            annualOrderD.setIInventoryId(record.getLong("iinventoryid"));
            annualOrderD.setIYear1(annualOrderM.getIYear());
            annualOrderD.setIYear1Sum(record.getBigDecimal("iqtytotal"));
            annualOrderD.setIYear2(annualOrderM.getIYear() + 1);
            annualOrderD.setIYear2Sum(record.getBigDecimal("inextyearmonthamounttotal"));
            annualOrderD.setIsDeleted(false);

            ValidationUtils.isTrue(annualOrderD.save(), ErrorMsg.SAVE_FAILED);

            saveAnnualOrderDModel(record, annualOrderM, annualOrderD);
        }
    }

    /**
     * 可编辑表格提交-修改数据
     */
    private void updateTableSubmitDatas(JBoltTable jBoltTable, AnnualOrderM annualOrderM) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        for (Record record : list) {
            Long iAnnualOrderDid = record.getLong("iautoid");

            AnnualOrderD annualOrderD = annualOrderDService.findById(iAnnualOrderDid);
            ValidationUtils.notNull(annualOrderD, JBoltMsg.DATA_NOT_EXIST);

            annualOrderD.setIInventoryId(record.getLong("iinventoryid"));
            annualOrderD.setIYear1(annualOrderM.getIYear());
            annualOrderD.setIYear1Sum(record.getBigDecimal("iqtytotal"));

            ValidationUtils.isTrue(annualOrderD.update(), ErrorMsg.UPDATE_FAILED);

            annualorderdQtyService.deleteBy(Okv.by("iannualorderdid", annualOrderD.getIAutoId()));

            saveAnnualOrderDModel(record, annualOrderM, annualOrderD);
        }
    }

    /**
     * 可编辑表格提交-删除数据
     */
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        for (Object id : ids) {
            annualorderdQtyService.deleteBy(Okv.by("iannualorderdid", id));
            annualOrderDService.deleteById(id);
        }
    }

    private void saveAnnualOrderDModel(Record record, AnnualOrderM annualOrderM, AnnualOrderD annualOrderD) {
        AnnualorderdQty annualorderdQty;
        for (int j = 1; j <= 12; j++) {
            annualorderdQty = new AnnualorderdQty();

            annualorderdQty.setIAnnualOrderDid(annualOrderD.getIAutoId());
            annualorderdQty.setIYear(annualOrderM.getIYear());
            annualorderdQty.setIMonth(j);

            BigDecimal iqty = record.getBigDecimal("iqty" + j);
            annualorderdQty.setIQty(iqty == null ? BigDecimal.ZERO : iqty);
            annualorderdQty.setIsDeleted(false);

            ValidationUtils.isTrue(annualorderdQty.save(), ErrorMsg.SAVE_FAILED);
        }
    }

    public Ret importExcel(File file) {
        StringBuilder errorMsg = new StringBuilder();


        return SUCCESS;
    }

    /**
     * 处理审批通过的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        AnnualOrderM annualOrderM = findById(formAutoId);
        // 订单状态校验
//        ValidationUtils.equals(annualOrderM.getIOrderStatus(), MonthOrderStatusEnum.AWAIT_AUDITED.getValue(), "订单非待审核状态");

        // 订单状态修改
        annualOrderM.setIOrderStatus(MonthOrderStatusEnum.AUDITTED.getValue());
        annualOrderM.setIUpdateBy(JBoltUserKit.getUserId());
        annualOrderM.setCUpdateName(JBoltUserKit.getUserName());
        annualOrderM.setDUpdateTime(new Date());
        annualOrderM.update();
        // 审批通过生成客户计划汇总
        cusOrderSumService.algorithmSum();
        return null;
    }

    /**
     * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
     */
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.REJECTED.getValue()).isOk(), JBoltMsg.FAIL);
        return null;
    }

    /**
     * 实现反审之前的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     */
    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /**
     * 实现反审之后的其他业务操作, 如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     */
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        // 只有一个审批人
        if (isFirst && isLast) {
            if(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isFail()){
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isOk(), JBoltMsg.FAIL);
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        // 反审回第一个节点，回退状态为“已保存”
        else if (isFirst) {
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isFail()){
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isOk(), JBoltMsg.FAIL);
        }
        // 最后一步通过的，反审，回退状态为“待审核”
        else if (isLast) {
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.AWAIT_AUDITED.getValue()).isFail()) {
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.AWAIT_AUDITED.getValue()).isOk(), JBoltMsg.FAIL);
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        return null;
    }

    /**
     * 提审前业务，如有异常返回错误信息
     */
    @Override
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    /**
     * 提审后业务处理，如有异常返回错误信息
     */
    @Override
    public String postSubmitFunc(long formAutoId) {
        AnnualOrderM annualOrderM = findById(formAutoId);
        if (annualOrderM.getIOrderStatus() == WeekOrderStatusEnum.NOT_AUDIT.getValue() || annualOrderM.getIOrderStatus() == WeekOrderStatusEnum.REJECTED.getValue()) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.AWAIT_AUDITED.getValue()).isOk(), "提审失败");
        }
        return null;
    }

    /**
     * 撤回审核业务处理，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFunc(long formAutoId) {
        AnnualOrderM annualOrderM = findById(formAutoId);
//        ValidationUtils.equals(annualOrderM.getIOrderStatus(), MonthOrderStatusEnum.AWAIT_AUDITED.getValue(), "订单非待审批状态");
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isOk(), "撤回失败");
        return null;
    }

    /**
     * 从审批中，撤回到已保存，业务实现，如有异常返回错误信息
     */
    @Override
    public String withdrawFromAuditting(long formAutoId) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isOk(), JBoltMsg.FAIL);
        return null;
    }

    /**
     * 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
     */
    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /**
     * 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isOk(), JBoltMsg.FAIL);
        // 修改客户计划汇总
        cusOrderSumService.algorithmSum();
        return null;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public Ret deleteByBatchIds(String ids) {
        List<AnnualOrderM> list = getListByIds(ids);
        List<AnnualOrderM> notAuditList = new ArrayList<>();
        for (AnnualOrderM annualOrderM : list) {
            ValidationUtils.equals(annualOrderM.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
            if (WeekOrderStatusEnum.NOT_AUDIT.getValue() != annualOrderM.getIOrderStatus() && WeekOrderStatusEnum.REJECTED.getValue() != annualOrderM.getIOrderStatus()) {
                notAuditList.add(annualOrderM);
            }

            annualOrderM.setIsDeleted(true);
        }

        ValidationUtils.isTrue(notAuditList.size() == 0, "存在非已保存订单");
        ValidationUtils.isTrue(batchUpdate(list).length > 0, JBoltMsg.FAIL);

        return SUCCESS;
    }


    /**
     * 批量审批（审核）通过
     * @param formAutoIds 单据IDs
     * @return  错误信息
     */
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        // 审批通过生成客户计划汇总
        cusOrderSumService.algorithmSum();
        return null;
    }

    /**
     * 批量审批（审核）不通过
     * @param formAutoIds 单据IDs
     * @return  错误信息
     */
    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        for (Long formAutoId:formAutoIds) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.REJECTED.getValue()).isOk(), JBoltMsg.FAIL);
        }
        return null;
    }

    /**
     * 批量撤销审批
     * @param formAutoIds 单据IDs
     * @return  错误信息
     */
    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        List<AnnualOrderM> annualOrderMS = getListByIds(StringUtils.join(formAutoIds, COMMA));
        Boolean algorithmSum = annualOrderMS.stream().anyMatch(item -> item.getIOrderStatus().equals(WeekOrderStatusEnum.APPROVED.getValue()));
        annualOrderMS.stream().map(item -> {
            item.setIOrderStatus(WeekOrderStatusEnum.NOT_AUDIT.getValue());
            return item;
        }).collect(Collectors.toList());
        batchUpdate(annualOrderMS);

        if (algorithmSum) {
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        return null;
    }
}
