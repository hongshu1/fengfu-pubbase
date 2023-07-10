package cn.rjtech.admin.monthorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.monthorderd.MonthorderdService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.MonthOrderStatusEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.MonthOrderD;
import cn.rjtech.model.momdata.MonthOrderM;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 月度计划订单 Service
 *
 * @ClassName: MonthordermService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 18:20
 */
public class MonthordermService extends BaseService<MonthOrderM> implements IApprovalService {

    private final MonthOrderM dao = new MonthOrderM().dao();

    @Inject
    private MonthorderdService monthorderdService;
    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private FormApprovalService formApprovalService;

    @Override
    protected MonthOrderM dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        para.set("iorgid", getOrgId());
        return dbTemplate("monthorderm.paginateAdminDatas", para).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(MonthOrderM monthorderm) {
        if (monthorderm == null || isOk(monthorderm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(monthorderm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = monthorderm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(), monthorderm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(MonthOrderM monthorderm) {
        if (monthorderm == null || notOk(monthorderm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        MonthOrderM dbMonthorderm = findById(monthorderm.getIAutoId());
        if (dbMonthorderm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(monthorderm.getName(), monthorderm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = monthorderm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(), monthorderm.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        List<MonthOrderM> list = getListByIds(ids);
        List<MonthOrderM> notAuditList = new ArrayList<>();
        for (MonthOrderM monthOrderM : list) {
            ValidationUtils.equals(monthOrderM.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
            if (WeekOrderStatusEnum.NOT_AUDIT.getValue() != monthOrderM.getIOrderStatus() && WeekOrderStatusEnum.REJECTED.getValue() != monthOrderM.getIOrderStatus()) {
                notAuditList.add(monthOrderM);
            }

            monthOrderM.setIsDeleted(true);
        }

        ValidationUtils.isTrue(notAuditList.size() == 0, "存在非已保存订单");
        ValidationUtils.isTrue(batchUpdate(list).length > 0, JBoltMsg.FAIL);

        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        MonthOrderM monthOrderM = findById(id);
        ValidationUtils.equals(monthOrderM.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
        return updateColumn(id, "isdeleted", true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param monthorderm 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(MonthOrderM monthorderm, Kv kv) {
        //addDeleteSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(),monthorderm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param monthorderm 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(MonthOrderM monthorderm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(monthorderm, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "IsDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param monthorderm 要toggle的model
     * @param column      操作的哪一列
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(MonthOrderM monthorderm, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(MonthOrderM monthorderm, String column, Kv kv) {
        //addUpdateSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(), monthorderm.getName(),"的字段["+column+"]值:"+monthorderm.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param monthorderm model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkInUse(MonthOrderM monthorderm, Kv kv) {
        //这里用来覆盖 检测Monthorderm是否被其它表引用
        return null;
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        MonthOrderM monthorderm = jBoltTable.getFormModel(MonthOrderM.class, "monthorderm");

        User user = JBoltUserKit.getUser();

        Date now = new Date();

        tx(() -> {
            if (monthorderm.getIAutoId() == null) {
                monthorderm.setIOrgId(getOrgId());
                monthorderm.setCOrgCode(getOrgCode());
                monthorderm.setCOrgName(getOrgName());
                monthorderm.setCOrderNo(BillNoUtils.genCode(getOrgCode(), table()));
                monthorderm.setICreateBy(user.getId());
                monthorderm.setCCreateName(user.getName());
                monthorderm.setDCreateTime(now);
                monthorderm.setIUpdateBy(user.getId());
                monthorderm.setCUpdateName(user.getName());
                monthorderm.setDUpdateTime(now);
                ValidationUtils.isTrue(monthorderm.save(), ErrorMsg.SAVE_FAILED);
            } else {
                monthorderm.setIUpdateBy(user.getId());
                monthorderm.setCUpdateName(user.getName());
                monthorderm.setDUpdateTime(now);
                ValidationUtils.isTrue(monthorderm.update(), ErrorMsg.UPDATE_FAILED);
            }

            saveTableSubmitDatas(jBoltTable, monthorderm);
            updateTableSubmitDatas(jBoltTable);
            deleteTableSubmitDatas(jBoltTable);
            ValidationUtils.notEmpty(monthorderdService.findByMid(monthorderm.getIAutoId()), "细项不允许为空");
            return true;
        });
        return successWithData(monthorderm.keep("iautoid"));
    }

    /**
     * 可编辑表格提交-新增数据
     */
    private void saveTableSubmitDatas(JBoltTable jBoltTable, MonthOrderM monthorderm) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (notOk(list)) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            if (i == 0) {
                // 避免保存不到所有字段的问题
                Set<String> columnNames = TableMapping.me().getTable(MonthOrderD.class).getColumnNameSet();
                for (String columnName : columnNames) {
                    if (null == row.get(columnName)) {
                        row.set(columnName, null);
                    }
                }
            }

            row.keep("iinventoryid", "iqty1", "iqty2", "iqty3", "iqty4", "iqty5", "iqty6", "iqty7", "iqty8", "iqty9", "iqty10", "iqty11", "iqty12",
                    "iqty13", "iqty14", "iqty15", "iqty16", "iqty17", "iqty18", "iqty19", "iqty20", "iqty21", "iqty22", "iqty23", "iqty24", "iqty25",
                    "iqty26", "iqty27", "iqty28", "iqty29", "iqty30", "iqty31", "isum", "cinvcode", "cinvname", "cinvcode1", "cinvname1", "cinvstd", "imonth1qty", "imonth2qty");
            row.set("isdeleted", "0");
            row.set("imonthordermid", monthorderm.getIAutoId());
            row.set("iautoid", JBoltSnowflakeKit.me.nextId());
        }
        monthorderdService.batchSaveRecords(list);
    }

    /**
     * 可编辑表格提交-修改数据
     */
    private void updateTableSubmitDatas(JBoltTable jBoltTable) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        for (Record row : list) {
            row.keep("iautoid", "iinventoryid", "iqty1", "iqty2", "iqty3", "iqty4", "iqty5", "iqty6", "iqty7", "iqty8", "iqty9", "iqty10", "iqty11", "iqty12",
                    "iqty13", "iqty14", "iqty15", "iqty16", "iqty17", "iqty18", "iqty19", "iqty20", "iqty21", "iqty22", "iqty23", "iqty24", "iqty25",
                    "iqty26", "iqty27", "iqty28", "iqty29", "iqty30", "iqty31", "isum", "cinvcode", "cinvname", "cinvcode1", "cinvname1", "cinvstd", "imonth1qty", "imonth2qty");
        }

        monthorderdService.batchUpdateRecords(list);
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
            monthorderdService.deleteById(id);
        }
    }

    /**
     * 提交审核
     */
    public Ret submit(Long iautoid) {
        tx(() -> {

            // 根据审批状态
            Ret ret = formApprovalService.submit(table(), iautoid, primaryKey(), "cn.rjtech.admin.monthorderm.MonthordermService");
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

            // 处理其他业务
            MonthOrderM monthOrderM = findById(iautoid);
            monthOrderM.setIOrderStatus(MonthOrderStatusEnum.AWAIT_AUDITED.getValue());
            ValidationUtils.isTrue(monthOrderM.update(), JBoltMsg.FAIL);
            return true;
        });

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
        MonthOrderM monthOrderM = findById(formAutoId);
        // 订单状态校验
//        ValidationUtils.equals(monthOrderM.getIOrderStatus(), MonthOrderStatusEnum.AWAIT_AUDITED.getValue(), "订单非待审核状态");

        // 订单状态修改
        monthOrderM.setIOrderStatus(MonthOrderStatusEnum.AUDITTED.getValue());
        monthOrderM.setIUpdateBy(JBoltUserKit.getUserId());
        monthOrderM.setCUpdateName(JBoltUserKit.getUserName());
        monthOrderM.setDUpdateTime(new Date());
        monthOrderM.update();
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
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isFail()) {
                LOG.info("更新失败");
                return "更新失败";
            }
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        // 反审回第一个节点，回退状态为“已保存”
        else if (isFirst) {
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isFail()) {
                LOG.info("更新失败");
                return "更新失败";
            }
        }
        // 最后一步通过的，反审，回退状态为“待审核”
        else if (isLast) {
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.AWAIT_AUDITED.getValue()).isFail()) {
                LOG.info("更新失败");
                return "更新失败";
            }
            
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
        MonthOrderM monthOrderM = findById(formAutoId);
        if (WeekOrderStatusEnum.NOT_AUDIT.getValue() == monthOrderM.getIOrderStatus()|| WeekOrderStatusEnum.REJECTED.getValue() == monthOrderM.getIOrderStatus()) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.AWAIT_AUDITED.getValue()).isOk(), "提审失败");
        }
        return null;
    }

    /**
     * 撤回审核业务处理，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFunc(long formAutoId) {
        MonthOrderM monthOrderM = findById(formAutoId);
//        ValidationUtils.equals(monthOrderM.getIOrderStatus(), MonthOrderStatusEnum.AWAIT_AUDITED.getValue(), "订单非待审批状态");
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
        return null;
    }

    /**
     * 批量撤销审批
     * @param formAutoIds 单据IDs
     * @return  错误信息
     */
    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        List<MonthOrderM> monthOrderMS = getListByIds(StringUtils.join(formAutoIds, COMMA));
        Boolean algorithmSum = monthOrderMS.stream().anyMatch(item -> item.getIOrderStatus().equals(WeekOrderStatusEnum.APPROVED.getValue()));
        if (algorithmSum) {
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        return null;
    }

}
