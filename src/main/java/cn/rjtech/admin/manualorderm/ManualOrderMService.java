package cn.rjtech.admin.manualorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryqcform.InventoryQcFormService;
import cn.rjtech.admin.manualorderd.ManualOrderDService;
import cn.rjtech.admin.stockoutqcformm.StockoutQcFormMService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.enums.ManualOrderStatusEnum;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private InventoryService inventoryService;
    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private ManualOrderDService manualOrderDService;
    @Inject
    private StockoutQcFormMService stockoutQcFormMService;
    @Inject
    private InventoryQcFormService inventoryQcFormService;

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
     * 设置参数
     */
    private void setManualOrderM(ManualOrderM manualOrderM) {
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
        tx(() -> {
            if (manualOrderM.getIAutoId() == null) {
                setManualOrderM(manualOrderM);
                manualOrderM.setIAuditStatus(0);
                ValidationUtils.isTrue(manualOrderM.save(), ErrorMsg.SAVE_FAILED);
            } else {
                //更新时需要判断数据存在
                ManualOrderM dbManualOrderM = findById(manualOrderM.getIAutoId());
                ValidationUtils.notNull(dbManualOrderM, JBoltMsg.DATA_NOT_EXIST);
                
                ValidationUtils.isTrue(manualOrderM.update(), ErrorMsg.UPDATE_FAILED);
            }
            
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
        return SUCCESS;
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
                    ValidationUtils.isTrue(conformto, "订单(" + record.getStr("corderno") + ")不能进行该操作!");
                }
                
                record.set("iorderstatus", status);
                if (status == 3) {
                    record.set("iauditstatus", AuditStatusEnum.APPROVED.getValue());
                    // 自动生成出货质检任务
                    Ret ret = approve(record.getLong("iautoid"));
                    ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));
                }
                
                updateRecord(record);
            }
        }
        return SUCCESS;
    }

    /**
     * 生成出货检
     */
    public Ret approve(Long iautoid) {
        ManualOrderM manualOrderM = findById(iautoid);
        ValidationUtils.notNull(manualOrderM, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(!manualOrderM.getIsDeleted(), "单据已被删除");
        ValidationUtils.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), manualOrderM.getIAuditStatus(), "非待审核状态");
        ValidationUtils.equals(AuditWayEnum.STATUS.getValue(), manualOrderM.getIOrderStatus(), "单据走审批流的，禁止点“审批通过”按钮进行操作！");

        tx(() -> {

            manualOrderM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
            manualOrderM.setIOrderStatus(ManualOrderStatusEnum.AUDITTED.getValue());
            ValidationUtils.isTrue(manualOrderM.update(), ErrorMsg.UPDATE_FAILED);

            // 判断是否有外购品
            List<ManualOrderD> manualOrderds = manualOrderDService.find(selectSql().eq("iManualOrderMid", manualOrderM.getIAutoId()));
            if (manualOrderds != null && manualOrderds.size() > 0) {

                List<Long> ids = new ArrayList<>();
                for (ManualOrderD manualOrderD : manualOrderds) {
                    ids.add(manualOrderD.getIInventoryId());
                }
                List<Inventory> inventories = inventoryService.getListByIds(CollUtil.join(ids, COMMA));

                if (CollUtil.isNotEmpty(inventories)) {

                    Date date = new Date();

                    String formatDate = DateUtil.formatDate(date);

                    for (Inventory inventory : inventories) {
                        if (inventory.getBPurchase()) {
                            InventoryQcForm inventoryQcForm = inventoryQcFormService.findFirst(selectSql().select(" q.* ").from("Bd_InventoryQcForm", "q").innerJoin("Bd_InventoryQcFormType", " qt ", " q.iAutoId = qt.iInventoryQcFormId and qt.iType = 3 ").eq("iInventoryId", inventory.getIAutoId()));
                            StockoutQcFormM stockoutQcFormM = new StockoutQcFormM();
                            stockoutQcFormM.setIInventoryId(inventory.getIAutoId());
                            stockoutQcFormM.setICustomerId(manualOrderM.getICustomerId());
                            stockoutQcFormM.setIQcFormId(inventoryQcForm.getIAutoId());

                            for (ManualOrderD manualOrderD : manualOrderds) {

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
                                                e.printStackTrace();
                                                continue;
                                            }
                                        }
                                    }
                                    break;
                                }

                            }

                            stockoutQcFormM.setCStockoutQcFormNo(formatDate);
                            stockoutQcFormM.setCBatchNo(formatDate);
                            stockoutQcFormM.setIStatus(0);
                            stockoutQcFormM.setIsCompleted(false);
                            stockoutQcFormM.setIOrgId(getOrgId());
                            stockoutQcFormM.setCOrgCode(getOrgCode());
                            stockoutQcFormM.setCOrgName(getOrgName());
                            stockoutQcFormM.setIsCpkSigned(false);
                            stockoutQcFormM.setICreateBy(JBoltUserKit.getUserId());
                            stockoutQcFormM.setIUpdateBy(JBoltUserKit.getUserId());
                            stockoutQcFormM.setCCreateName(JBoltUserKit.getUserName());
                            stockoutQcFormM.setCUpdateName(JBoltUserKit.getUserName());
                            stockoutQcFormM.setDCreateTime(date);
                            stockoutQcFormM.setDUpdateTime(date);

                            stockoutQcFormMService.save(stockoutQcFormM);
                        }
                    }
                }
            }

            cusOrderSumService.algorithmSum();
            
            return true;
        });
        
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
            //batchUpdateRecords(records);
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

//    public void withdraw(Long iautoid) {
//        ManualOrderM orderM = findById(iautoid);
//        ValidationUtils.notNull(orderM, JBoltMsg.DATA_NOT_EXIST);
//        ValidationUtils.isTrue(!orderM.getIsDeleted(), "已被删除");
//        ValidationUtils.equals(orderM.getIAuditStatus(), );
//        
//        switch (Au)
//        
//    }
    
}