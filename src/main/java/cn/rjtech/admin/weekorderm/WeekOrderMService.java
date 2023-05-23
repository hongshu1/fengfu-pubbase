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
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.WeekOrderD;
import cn.rjtech.model.momdata.WeekOrderM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

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
    public Object getAdminDatas(int pageNumber, int pageSize, Kv kv) {
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
    public Ret approve(String iAutoId) {
        List<WeekOrderM> listByIds = getListByIds(iAutoId);
        ValidationUtils.notEmpty(listByIds, "订单不存在");

        tx(() -> {
            
            for (WeekOrderM orderM : listByIds) {

                switch (AuditWayEnum.toEnum(orderM.getInt(IAUDITWAY))) {
                    case STATUS:
                        formApprovalService.approveByStatus(table(), orderM.getIAutoId(), () -> null, () -> null);
                        
                        List<WeekOrderD> weekOrderds = weekOrderDService.findByMId(orderM.getIAutoId());
                        if (CollUtil.isNotEmpty(weekOrderds)) {
                            cusOrderSumService.handelWeekOrder(weekOrderds);
                        }
                        break;
                    case FLOW:
                        Ret ret = formApprovalService.approve(orderM.getIAutoId(), table(), AuditStatusEnum.APPROVED.getValue(), null);
                        ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));
                        
                        break;
                    default:
                        break;
                }

            }
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

            formApprovalService.withdraw(table(), weekOrderM.getIAutoId(), () -> null, () -> {

                // 订单状态：2. 待审批
                weekOrderM.setIOrderStatus(WeekOrderStatusEnum.SAVED.getValue());
                weekOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());

                ValidationUtils.isTrue(weekOrderM.update(), ErrorMsg.UPDATE_FAILED);

                List<WeekOrderD> weekOrderds = weekOrderDService.findByMId(weekOrderM.getIAutoId());
                if (CollUtil.isNotEmpty(weekOrderds)) {
                    cusOrderSumService.handelWeekOrder(weekOrderds);
                }

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
            ValidationUtils.equals(WeekOrderStatusEnum.SAVED.getValue(), weekOrderM.getIOrderStatus(), "只能对“已保存”状态的记录进行删除");

            weekOrderM.setIsDeleted(true);
            weekOrderM.update();
        }
        return SUCCESS;
    }

    /**
     * 批量反审批
     */
    public Ret reject(String ids) {
        tx(() -> {
            // 数据同步暂未开发 现只修改状态
            for (WeekOrderM weekOrderM : getListByIds(ids)) {

                switch (AuditWayEnum.toEnum(weekOrderM.getInt(IAUDITWAY))) {
                    case STATUS:
                        formApprovalService.rejectByStatus(table(), weekOrderM.getIAutoId(), () -> null, () -> {
                            List<WeekOrderD> weekOrderds = weekOrderDService.findByMId(weekOrderM.getIAutoId());
                            if (CollUtil.isNotEmpty(weekOrderds)) {
                                cusOrderSumService.handelWeekOrder(weekOrderds);
                            }
                            
                            return null;
                        });
                        break;
                    case FLOW:
                        Ret ret = formApprovalService.approve(weekOrderM.getIAutoId(), table(), AuditStatusEnum.REJECTED.getValue(), null);
                        ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

                        break;
                    default:
                        break;
                }
                
            }
            return true;
        });
        return SUCCESS;
    }

    public Ret submit(Long iautoid) {
        tx(() -> {

            Ret ret = formApprovalService.judgeType(table(), iautoid);
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));
            
            // 更新订单的状态
            ValidationUtils.isTrue(updateIorderStatus(iautoid, WeekOrderStatusEnum.AWAIT_AUDITED.getValue(), WeekOrderStatusEnum.SAVED.getValue()), ErrorMsg.UPDATE_FAILED);

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
            if (ObjUtil.isNull(weekOrderM)) {
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
        List<Record> save = jBoltTable.getSaveRecordList();
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

        List<Record> save = jBoltTable.getSaveRecordList();
        if (CollUtil.isNotEmpty(save)) {
            saveDs(save, weekOrderM.getIAutoId());
        }

        updateDs(jBoltTable.getUpdateRecordList());
    }

    private void saveDs(List<Record> save, long iweekordermid) {
        for (Record row : save) {
            row.set("IWeekOrderMid", iweekordermid)
                    .set("iautoid", JBoltSnowflakeKit.me.nextId())
                    .set("isdeleted", ZERO_STR);
        }
        weekOrderDService.batchSaveRecords(save);
    }

    private void updateDs(List<Record> update) {
        if (CollUtil.isNotEmpty(update)) {
            weekOrderDService.batchUpdateRecords(update);
        }
    }
    
}