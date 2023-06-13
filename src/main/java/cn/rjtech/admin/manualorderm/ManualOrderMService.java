package cn.rjtech.admin.manualorderm;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryqcform.InventoryQcFormService;
import cn.rjtech.admin.manualorderd.ManualOrderDService;
import cn.rjtech.admin.stockoutqcformm.StockoutQcFormMService;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static cn.jbolt.core.util.JBoltArrayUtil.COMMA;

/**
 * 客户订单-手配订单主表
 *
 * @ClassName: ManualOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 15:18
 */
public class ManualOrderMService extends BaseService<ManualOrderM> {

    private final ManualOrderM dao = new ManualOrderM().dao();

    @Inject
    private ManualOrderDService manualOrderDService;
    @Inject
    private StockoutQcFormMService stockoutQcFormMService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private InventoryQcFormService inventoryQcFormService;
    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private FormApprovalService formApprovalService;

    @Override
    protected ManualOrderM dao() {
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
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("manualorderm.list", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(ManualOrderM manualOrderM) {
        if (manualOrderM == null || isOk(manualOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        if (exists("cOrderNo", manualOrderM.getCOrderNo())) {
            return fail(JBoltMsg.DATA_SAME_NAME_EXIST);
        }
        setManualOrderM(manualOrderM);
        manualOrderM.setIAuditStatus(0);
        boolean success = manualOrderM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(), manualOrderM.getName());
        }
        return ret(success);
    }

    /**
     * 设置参数
     */
    private ManualOrderM setManualOrderM(ManualOrderM manualOrderM) {
        manualOrderM.setCOrgCode(getOrgCode());
        manualOrderM.setCOrgName(getOrgName());
        manualOrderM.setIOrgId(getOrgId());
        manualOrderM.setIsDeleted(false);
        Long userId = JBoltUserKit.getUserId();
        manualOrderM.setICreateBy(userId);
        manualOrderM.setIUpdateBy(userId);
        String userName = JBoltUserKit.getUserName();
        manualOrderM.setCCreateName(userName);
        manualOrderM.setCUpdateName(userName);
        Date date = new Date();
        manualOrderM.setDCreateTime(date);
        manualOrderM.setDUpdateTime(date);
        manualOrderM.setDOrderDate(date);
        return manualOrderM;
    }

    /**
     * 更新
     */
    public Ret update(ManualOrderM manualOrderM) {
        if (manualOrderM == null || notOk(manualOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        ManualOrderM dbManualOrderM = findById(manualOrderM.getIAutoId());
        if (dbManualOrderM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(manualOrderM.getName(), manualOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = manualOrderM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(), manualOrderM.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param manualOrderM 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(ManualOrderM manualOrderM, Kv kv) {
        //addDeleteSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(),manualOrderM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param manualOrderM model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(ManualOrderM manualOrderM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 保存
     */
    public Ret saveForm(ManualOrderM manualOrderM, JBoltTable jBoltTable) {
        AtomicReference<Ret> res = new AtomicReference<>();
        res.set(SUCCESS);
        tx(() -> {
            if (manualOrderM.getIAutoId() == null) {
                Ret save = save(manualOrderM);
                if (!save.isOk()) {
                    res.set(save);
                    return false;
                }
            } else update(manualOrderM);
            List<ManualOrderD> saveBeanList = jBoltTable.getSaveBeanList(ManualOrderD.class);
            if (saveBeanList != null && saveBeanList.size() > 0) {
                for (ManualOrderD manualOrderD : saveBeanList) {
                    manualOrderD.setIManualOrderMid(manualOrderM.getIAutoId());
                    manualOrderD.setIsDeleted(false);
                }
                manualOrderDService.batchSave(saveBeanList);
            }
            List<ManualOrderD> updateBeanList = jBoltTable.getUpdateBeanList(ManualOrderD.class);
            if (updateBeanList != null && updateBeanList.size() > 0) {
                for (ManualOrderD manualOrderD : updateBeanList) {
                    manualOrderD.setIManualOrderMid(manualOrderM.getIAutoId());
                }
                manualOrderDService.batchUpdate(saveBeanList);
            }
            Object[] deletes = jBoltTable.getDelete();
            if (ArrayUtil.isNotEmpty(deletes)) {
                deleteMultiByIds(deletes);
            }
            return true;
        });
        return res.get();
    }

    public void deleteMultiByIds(Object[] deletes) {
        delete("DELETE FROM Co_ManualOrderD WHERE iAutoId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
    }

    /**
     * 批量生成
     */
    public Ret batchHandle(Kv kv, int status, int[] conformtos) {
        List<Record> records = getDatasByIds(kv);
        if (records != null && records.size() > 0) {
            for (Record record : records) {
                if (conformtos.length > 0) {
                    boolean conformto = false;
                    for (int exception : conformtos) {
                        if (exception == record.getInt("iorderstatus")) {
                            conformto = true;
                        }
                    }
                    if (!conformto) return fail("订单(" + record.getStr("corderno") + ")不能进行该操作!");
                }
                record.set("iorderstatus", status);
                if (status == 3) {
                    record.set("iauditstatus", 2);
                    //自动生成出货质检任务
                    Ret stockoutQcFormM = createStockoutQcFormM(record.getLong("icustomerid"), record.getLong("iautoid"));
                    if (stockoutQcFormM.isFail()) return stockoutQcFormM;
                }
                updateRecord(record);
            }
            //batchUpdateRecords(records);
        }
        return SUCCESS;
    }

    /**
     * 生成出货检
     */
    public Ret createStockoutQcFormM(Long icustomerid, Long iautoid) {
        try {
            //判断是否有外购品
            List<ManualOrderD> manualOrderDS = manualOrderDService.find(manualOrderDService.selectSql().eq("iManualOrderMid", iautoid));
            if (manualOrderDS != null && manualOrderDS.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (ManualOrderD manualOrderD : manualOrderDS) {
                    ids.append(manualOrderD.getIInventoryId()).append(",");
                }
                List<Inventory> inventories = inventoryService.getListByIds(ids.length() > 1 ? ids.substring(0, ids.length() - 1) : "");
                if (inventories != null && inventories.size() > 0) {

                    for (Inventory inventory : inventories) {
                        if (inventory.getBPurchase()) {
                            InventoryQcForm inventoryQcForm = inventoryQcFormService.findFirst(selectSql().select(" q.* ").from("Bd_InventoryQcForm", "q").innerJoin("Bd_InventoryQcFormType", " qt ", " q.iAutoId = qt.iInventoryQcFormId and qt.iType = 3 ").eq("iInventoryId", inventory.getIAutoId()));
                            StockoutQcFormM stockoutQcFormM = new StockoutQcFormM();
                            stockoutQcFormM.setIInventoryId(inventory.getIAutoId());
                            stockoutQcFormM.setICustomerId(icustomerid);
                            stockoutQcFormM.setIQcFormId(inventoryQcForm.getIAutoId());

                            Date date = new Date();
                            String format = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            for (ManualOrderD manualOrderD : manualOrderDS) {
                                if (manualOrderD.getIInventoryId().longValue() == stockoutQcFormM.getIInventoryId().longValue()) {
                                    String dd = new SimpleDateFormat("dd").format(date);
                                    int idd = Integer.parseInt(dd);
                                    Method[] m = manualOrderD.getClass().getMethods();
                                    for (Method method : m) {
                                        if (("getiqty" + idd).equalsIgnoreCase(method.getName())) {
                                            try {
                                                BigDecimal invoke = (BigDecimal) method.invoke(manualOrderD);
                                                stockoutQcFormM.setIQty(invoke);
                                            } catch (Exception e) {
                                                continue;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            stockoutQcFormM.setCStockoutQcFormNo(format);
                            stockoutQcFormM.setCBatchNo(format);
                            stockoutQcFormM.setIStatus(0);
                            stockoutQcFormM.setIsCompleted(false);
                            stockoutQcFormM.setIOrgId(getOrgId());
                            stockoutQcFormM.setCOrgCode(getOrgCode());
                            stockoutQcFormM.setCOrgName(getOrgName());
                            stockoutQcFormM.setIsCpkSigned(false);
                            Long userId = JBoltUserKit.getUserId();
                            stockoutQcFormM.setICreateBy(userId);
                            stockoutQcFormM.setIUpdateBy(userId);
                            String userName = JBoltUserKit.getUserName();
                            stockoutQcFormM.setCCreateName(userName);
                            stockoutQcFormM.setCUpdateName(userName);
                            stockoutQcFormM.setDCreateTime(date);
                            stockoutQcFormM.setDUpdateTime(date);
                            stockoutQcFormMService.save(stockoutQcFormM);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return fail("生成出货质检任务失败!");
        }
        return SUCCESS;
    }

    public Ret batchDetect(Kv kv) {
        List<Record> records = getDatasByIds(kv);
        if (records != null && records.size() > 0) {
            for (Record record : records) {
                Integer iorderstatus = record.getInt("iorderstatus");
                if (iorderstatus != 1 && iorderstatus != 4) {
                    return fail("订单(" + record.getStr("corderno") + ")不能删除!");
                }

                record.set("isdeleted", 1);
                updateRecord(record);
            }
        }
        return SUCCESS;
    }

    public List<Record> getDatasByIds(Kv kv) {
        String ids = kv.getStr("ids");
        if (ids != null) {
            String[] split = ids.split(",");
            StringBuilder sqlids = new StringBuilder();
            for (String id : split) {
                sqlids.append("'").append(id).append("',");
            }
            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
            sqlids = new StringBuilder(sqlids.substring(0, sqlids.length() - 1));
            kv.set("sqlids", sqlids.toString());
        }
        return dbTemplate("manualorderm.getDatasByIds", kv).find();
    }

    /**
     * 审批
     *
     * @param iautoid
     * @return
     */
    public Ret approve(Long iautoid) {
        tx(() -> {
            ManualOrderM manualOrderM = findById(iautoid);
            ValidationUtils.equals(WeekOrderStatusEnum.AWAIT_AUDIT.getValue(), manualOrderM.getIOrderStatus(), "订单非待审核状态");

            formApprovalService.approveByStatus(table(), primaryKey(), iautoid, (formAutoId) -> null, (formAutoId) -> {
                ValidationUtils.isTrue(updateColumn(iautoid, "iOrderStatus", WeekOrderStatusEnum.APPROVED.getValue()).isOk(), JBoltMsg.FAIL);
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
     *
     * @param iAutoId
     * @return
     */
    public Ret withdraw(Long iAutoId) {
        tx(() -> {
            ManualOrderM manualOrderM = findById(iAutoId);
            ValidationUtils.equals(WeekOrderStatusEnum.AWAIT_AUDIT.getValue(), manualOrderM.getIOrderStatus(), "只允许待审核状态订单撤回");
            formApprovalService.withdraw(table(), primaryKey(), iAutoId, (formAutoId) -> null, (formAutoId) -> {
                manualOrderM.setIOrderStatus(WeekOrderStatusEnum.NOT_AUDIT.getValue());
                ValidationUtils.isTrue(manualOrderM.update(), "撤回失败");

                // 修改客户计划汇总
                cusOrderSumService.algorithmSum();
                return null;
            });
            return true;
        });
        return SUCCESS;
    }

    /**
     * 提交审批
     *
     * @param iautoid
     * @return
     */
    public Ret submit(Long iautoid) {
        tx(() -> {
            Ret ret = formApprovalService.judgeType(table(), iautoid, primaryKey(), "");
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

            ManualOrderM manualOrderM = findById(iautoid);
            manualOrderM.setIOrderStatus(WeekOrderStatusEnum.AWAIT_AUDIT.getValue());
            ValidationUtils.isTrue(manualOrderM.update(), JBoltMsg.FAIL);
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
                List<ManualOrderM> list = getListByIds(ids);
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
     * @param ids
     * @return
     */
    public Ret batchReverseApprove(String ids) {
        tx(() -> {
            List<ManualOrderM> list = getListByIds(ids);
            // 非已审批数据
            List<ManualOrderM> noApprovedDatas = list.stream().filter(item -> !(item.getIOrderStatus() == WeekOrderStatusEnum.APPROVED.getValue())).collect(Collectors.toList());
            ValidationUtils.isTrue(noApprovedDatas.size() <= 0, "存在非已审批数据");
            for (ManualOrderM manualOrderM : list) {
                Long id = manualOrderM.getIAutoId();
                formApprovalService.reverseApproveByStatus(id, table(), primaryKey(), (formAutoId) -> null, (formAutoId) -> {
                    // 处理订单状态
                    ValidationUtils.isTrue(updateColumn(id, "iOrderStatus", WeekOrderStatusEnum.AWAIT_AUDIT.getValue()).isOk(), JBoltMsg.FAIL);
                    return null;
                });
            }

            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
            return true;
        });
        return SUCCESS;
    }

    /**
     * 打开
     * @param iautoid
     * @return
     */
    public Ret open(String iautoid) {
        ManualOrderM manualOrderM = findById(iautoid);
        ValidationUtils.equals(WeekOrderStatusEnum.CLOSE.getValue(), manualOrderM.getIOrderStatus(), "订单非已关闭状态");
        manualOrderM.setIOrderStatus(WeekOrderStatusEnum.APPROVED.getValue());
        ValidationUtils.isTrue(manualOrderM.update(), JBoltMsg.FAIL);
        return SUCCESS;
    }
}