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
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AnnualOrderStatusEnum;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.AnnualOrderD;
import cn.rjtech.model.momdata.AnnualOrderM;
import cn.rjtech.model.momdata.AnnualorderdQty;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 年度计划订单
 *
 * @ClassName: AnnualOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 17:23
 */
public class AnnualOrderMService extends BaseService<AnnualOrderM> {

    private final AnnualOrderM dao = new AnnualOrderM().dao();

    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private AnnualOrderDService annualOrderDService;
    @Inject
    private AnnualorderdQtyService annualorderdQtyService;
    @Inject
    private FormApprovalService formApprovalService;

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
        return SUCCESS;
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

    public Ret approve(Long id) {
        AnnualOrderM annualOrderM = superFindById(id);
        //2. 审核通过
        annualOrderM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
        // 3. 已审批
        annualOrderM.setIOrderStatus(AnnualOrderStatusEnum.AUDITTED.getValue());
        annualOrderM.setIUpdateBy(JBoltUserKit.getUserId());
        annualOrderM.setCUpdateName(JBoltUserKit.getUserName());
        annualOrderM.setDUpdateTime(new Date());
        annualOrderM.update();
        //审批通过生成客户计划汇总
        return cusOrderSumService.algorithmSum();
    }

    public Ret importExcel(File file) {
        StringBuilder errorMsg = new StringBuilder();


        return SUCCESS;
    }

    /**
     * 提交审批
     */
    public Ret submit(Long iautoid) {
        tx(() -> {
            Ret ret = formApprovalService.judgeType(table(), iautoid, primaryKey(),"");
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

            AnnualOrderM annualOrderM = findById(iautoid);
            annualOrderM.setIOrderStatus(WeekOrderStatusEnum.AWAIT_AUDIT.getValue());
            annualOrderM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
            ValidationUtils.isTrue(annualOrderM.update(), JBoltMsg.FAIL);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 撤回
     */
    public Ret withdraw(Long iAutoId) {
        tx(() -> {
            formApprovalService.withdraw(table(), primaryKey(), iAutoId, (formAutoId) -> null, (formAutoId) -> {
                AnnualOrderM annualOrderM = findById(formAutoId);
                annualOrderM.setIOrderStatus(WeekOrderStatusEnum.NOT_AUDIT.getValue());
                ValidationUtils.isTrue(annualOrderM.update(), "撤回失败");

                // 修改客户计划汇总
                cusOrderSumService.algorithmSum();
                return null;
            });

            return true;
        });
        return SUCCESS;

    }

    /**
     * 审批不通过
     */
    public Ret reject(Long iAutoId) {
        tx(() -> {
            formApprovalService.rejectByStatus(table(), primaryKey(), iAutoId, (formAutoId) -> null, (formAutoId) -> {
                ValidationUtils.isTrue(updateColumn(iAutoId, "iOrderStatus", WeekOrderStatusEnum.REJECTED.getValue()).isOk(), JBoltMsg.FAIL);
                return null;
            });

            return true;
        });
        return SUCCESS;
    }

    /**
     * 批量审核
     * @param ids
     * @return
     */
    public Ret batchApprove(String ids) {
        tx(() -> {
            formApprovalService.batchApproveByStatus(table(), primaryKey(), ids, (formAutoId) -> null, (formAutoId) -> {
                List<AnnualOrderM> list = getListByIds(ids);
                list = list.stream().filter(Objects::nonNull).map(item -> {
                    item.setIOrderStatus(WeekOrderStatusEnum.APPROVED.getValue());
                    return item;
                }).collect(Collectors.toList());
                ValidationUtils.isTrue(batchUpdate(list).length > 0, JBoltMsg.FAIL);

                // 修改客户计划汇总
                cusOrderSumService.algorithmSum();
                return null;
            });
            return true;
        });
        return SUCCESS;
    }

    /**
     * 批量反审批
     *
     * @param ids
     * @return
     */
    public Ret batchReverseApprove(String ids) {
        tx(() -> {
            List<AnnualOrderM> list = getListByIds(ids);
            for (AnnualOrderM annualOrderM : list) {
                // 处理订单审批数据
                formApprovalService.reverseApprove(annualOrderM.getIAutoId(),
                        table(), primaryKey(), annualOrderM.getIAuditStatus(), "cn.rjtech.admin.annualorderm.AnnualOrderMService");

                // 处理订单数据
                WeekOrderStatusEnum orderStatusEnum = WeekOrderStatusEnum.toEnum(annualOrderM.getIOrderStatus());
                switch (orderStatusEnum) {
                    case AWAIT_AUDIT:
                        annualOrderM.setIOrderStatus(WeekOrderStatusEnum.NOT_AUDIT.getValue());
                        break;
                    case APPROVED:
                        annualOrderM.setIOrderStatus(WeekOrderStatusEnum.AWAIT_AUDIT.getValue());
                        break;
                    default:
                        break;
                }
            }
            ValidationUtils.isTrue(batchUpdate(list).length > 0, "批量反审批失败");

            // 判断订单是否存在已审核的反审批
            if (list.stream().anyMatch(item -> item.getIOrderStatus() == WeekOrderStatusEnum.AWAIT_AUDIT.getValue())) {
                // 修改客户计划汇总
                cusOrderSumService.algorithmSum();
            }
            return true;
        });
        return SUCCESS;
    }
}
