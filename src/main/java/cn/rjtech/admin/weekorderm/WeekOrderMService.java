package cn.rjtech.admin.weekorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.weekorderd.WeekOrderDService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.OrderStatusEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.WeekOrderD;
import cn.rjtech.model.momdata.WeekOrderM;
import cn.rjtech.model.momdata.base.BaseWeekOrderD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户订单-周间客户订单
 *
 * @ClassName: WeekOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 14:37
 */
public class WeekOrderMService extends BaseService<WeekOrderM> {

    private final WeekOrderM dao = new WeekOrderM().dao();

    @Inject
    private WeekOrderDService weekOrderDService;
    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private FormApprovalService formApprovalService;

    @Override
    protected WeekOrderM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.WEEK_ORDER.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("weekorderm.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param weekOrderM 要删除的model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(WeekOrderM weekOrderM, Kv kv) {
        addDeleteSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(), weekOrderM.getIAutoId().toString());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param weekOrderM model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkInUse(WeekOrderM weekOrderM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 新增前查询
     */
    public WeekOrderM findAddData() {
        return findFirst(selectSql().orderBy("dCreateTime", "desc"));
    }

    public Ret delete(Long id) {
        return toggleBoolean(id, "IsDeleted");
    }

    public List<Record> weekOrderMData(Long iWeekOrderMid) {
        return dbTemplate("weekorderm.weekOrderMData", Okv.by("iWeekOrderMid", iWeekOrderMid)).find();
    }

    /**
     * 审批
     */
    public Ret approve(Long iautoid) {
        tx(() -> {
            // 校验订单状态
            WeekOrderM weekOrderM = findById(iautoid);
            ValidationUtils.equals(OrderStatusEnum.AWAIT_AUDIT.getValue(), weekOrderM.getIOrderStatus(), "订单非待审核状态");
            formApprovalService.approveByStatus(table(), primaryKey(), iautoid, (fromAutoId) -> null, (fromAutoId) -> {
                ValidationUtils.isTrue(updateColumn(iautoid, "iOrderStatus", OrderStatusEnum.APPROVED.getValue()).isOk(), JBoltMsg.FAIL);
                return null;
            });

            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
            return true;
        });

        return SUCCESS;
    }

    /**
     * 撤回
     */
    public Ret withdraw(Long iAutoId) {
        WeekOrderM weekOrderM = findById(iAutoId);

        tx(() -> {
            // 校验订单状态
            ValidationUtils.equals(OrderStatusEnum.AWAIT_AUDIT.getValue(), weekOrderM.getIOrderStatus(), "订单非待审核状态");
            // 订单状态：2. 待审批
            formApprovalService.withdraw(table(), primaryKey(), iAutoId, (formAutoId) -> null, (formAutoId) -> {

                weekOrderM.setIOrderStatus(WeekOrderStatusEnum.SAVED.getValue());
                ValidationUtils.isTrue(weekOrderM.update(), ErrorMsg.UPDATE_FAILED);

                cusOrderSumService.algorithmSum();

                return null;
            });

            return true;
        });

        return SUCCESS;
    }

    /**
     * 关闭功能
     */
    public Ret closeWeekOrder(String iAutoId) {
        if (notOk(iAutoId)) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        WeekOrderM weekOrderM = findById(iAutoId);
        //订单状态：7. 已关闭
        weekOrderM.setIOrderStatus(7);
        boolean result = weekOrderM.update();
        return ret(result);
    }

    /**
     * 批量删除
     */
    public Ret deleteByIdS(String ids) {
        List<WeekOrderM> orderms = getListByIds(ids);
        ValidationUtils.notEmpty(orderms, "订单不存在");

        for (WeekOrderM weekOrderM : orderms) {
            //ValidationUtils.equals(WeekOrderStatusEnum.SAVED.getValue(), weekOrderM.getIOrderStatus(), "只能对“已保存”状态的记录进行删除");

            weekOrderM.setIsDeleted(true);
            weekOrderM.update();
        }
        return SUCCESS;
    }

    /**
     * 审批不通过
     */
    public Ret reject(Long iautoid) {
        tx(() -> {
            // 数据同步暂未开发 现只修改状态
            formApprovalService.rejectByStatus(table(), primaryKey(), iautoid, (fromAutoId) -> null, (fromAutoId) -> {
                ValidationUtils.isTrue(updateColumn(iautoid, "iOrderStatus", OrderStatusEnum.REJECTED.getValue()).isOk(), JBoltMsg.FAIL);
                //cusOrderSumService.algorithmSum();
                return null;
            });

            return true;
        });
        return SUCCESS;
    }

    public Ret submit(Long iautoid) {
        tx(() -> {

            Ret ret = formApprovalService.judgeType(table(), iautoid, primaryKey());
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

            // 更新订单的状态
            ValidationUtils.isTrue(updateColumn(iautoid, "iOrderStatus", WeekOrderStatusEnum.AWAIT_AUDITED.getValue()).isOk(), "提审失败");

            return true;
        });
        return SUCCESS;
    }

    private boolean updateIorderStatus(long iautoid, int iAfterStatus, int iBeforeStatus) {
        return update("UPDATE Co_WeekOrderM SET iOrderStatus = ? WHERE iAutoId = ? AND iOrderStatus = ? ", iAfterStatus, iautoid, iBeforeStatus) > 0;
    }

    public Ret saveTableSubmit(JBoltTable jBoltTable) {
        ValidationUtils.notNull(jBoltTable, JBoltMsg.PARAM_ERROR);
        ValidationUtils.isTrue(jBoltTable.isNotBlank(), JBoltMsg.PARAM_ERROR);

        WeekOrderM weekOrderM = jBoltTable.getFormModel(WeekOrderM.class, "weekOrderM");
        ValidationUtils.notNull(weekOrderM, JBoltMsg.PARAM_ERROR);

        Date now = new Date();

        tx(() -> {

            // 新增
            if (ObjUtil.isNull(weekOrderM.getIAutoId())) {
                doSave(weekOrderM, jBoltTable, now);
            } else {
                doUpdate(weekOrderM, jBoltTable, now);
            }

            return true;
        });

        return SUCCESS;
    }

    private void doSave(WeekOrderM weekOrderM, JBoltTable jBoltTable, Date now) {
        weekOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
        weekOrderM.setIOrderStatus(WeekOrderStatusEnum.SAVED.getValue());

        // 组织信息
        weekOrderM.setIOrgId(getOrgId());
        weekOrderM.setCOrgCode(getOrgCode());
        weekOrderM.setCOrgName(getOrgName());
        // 创建信息
        weekOrderM.setCCreateName(JBoltUserKit.getUserName());
        weekOrderM.setDCreateTime(now);
        weekOrderM.setICreateBy(JBoltUserKit.getUserId());
        // 更新信息
        weekOrderM.setCUpdateName(JBoltUserKit.getUserName());
        weekOrderM.setDUpdateTime(now);
        weekOrderM.setIUpdateBy(JBoltUserKit.getUserId());
        // 订单创建日期
        weekOrderM.setDOrderDate(now);
        weekOrderM.save();

        // 保存明细
        List<WeekOrderD> save = new ArrayList<>();
        try {
            save = jBoltTable.getSaveBeanList(WeekOrderD.class);
        }
        catch (Exception e) {
            ValidationUtils.isTrue(false, "周间客户订单不合法,请检查订单数据!");
        }
        ValidationUtils.notEmpty(save, JBoltMsg.PARAM_ERROR);

        saveDs(save, weekOrderM.getIAutoId());
    }

    private void doUpdate(WeekOrderM weekOrderM, JBoltTable jBoltTable, Date now) {
        // 更新时需要判断数据存在
        WeekOrderM dbWeekOrderM = findById(weekOrderM.getIAutoId());
        ValidationUtils.notNull(dbWeekOrderM, JBoltMsg.DATA_NOT_EXIST);

        // 更新信息
        weekOrderM.setCUpdateName(JBoltUserKit.getUserName());
        weekOrderM.setDUpdateTime(now);
        weekOrderM.setIUpdateBy(JBoltUserKit.getUserId());
        ValidationUtils.isTrue(weekOrderM.update(), ErrorMsg.UPDATE_FAILED);
        try {
            List<WeekOrderD> save = jBoltTable.getSaveBeanList(WeekOrderD.class);
            if (CollUtil.isNotEmpty(save)) {
                saveDs(save, weekOrderM.getIAutoId());
            }
            List<WeekOrderD> updateBeanList = jBoltTable.getUpdateBeanList(WeekOrderD.class);
            updateDs(updateBeanList);
        } catch (Exception e) {
            ValidationUtils.isTrue(false, "周间客户订单不合法,请检查订单数据!");
        }
    }

    private void saveDs(List<WeekOrderD> save, long iweekordermid) {
        for (BaseWeekOrderD row : save) {
            row.set("IWeekOrderMid", iweekordermid)
                    .set("iautoid", JBoltSnowflakeKit.me.nextId())
                    .set("isdeleted", ZERO_STR);
        }
        weekOrderDService.batchSave(save);
    }

    private void updateDs(List<WeekOrderD> update) {
        if (CollUtil.isNotEmpty(update)) {
            weekOrderDService.batchUpdate(update);
        }
    }

    public Page<Record> updateCplanTimeDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        return dbTemplate("weekorderm.updateCplanTimeDatas", kv).paginate(pageNumber, pageSize);
    }

    public Ret saveUpdateCplanTime(JBoltTable jBoltTable) {
        return SUCCESS;
    }
}
