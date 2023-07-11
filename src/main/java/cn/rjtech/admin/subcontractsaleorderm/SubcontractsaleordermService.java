package cn.rjtech.admin.subcontractsaleorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.dictionary.DictionaryTypeKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.saletype.SaleTypeService;
import cn.rjtech.admin.subcontractsaleorderd.SubcontractsaleorderdService;
import cn.rjtech.admin.syssaledeliverplan.SysSaledeliverplanService;
import cn.rjtech.admin.weekorderm.WeekOrderMService;
import cn.rjtech.cache.FormApprovalCache;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.enums.MonthOrderStatusEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.utils.Log;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 委外销售订单主表 Service
 *
 * @ClassName: SubcontractsaleordermService
 * @author: RJ
 * @date: 2023-04-12 18:57
 */
public class SubcontractsaleordermService extends BaseService<Subcontractsaleorderm> implements IApprovalService {

    private final Subcontractsaleorderm dao = new Subcontractsaleorderm().dao();
    
    @Inject
    private DeptService deptService;
    @Inject
    private CustomerService customerService;
    @Inject
    private SaleTypeService saleTypeService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private WeekOrderMService weekOrderMService;
    @Inject
    private DictionaryService dictionaryService;
    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private SysSaledeliverplanService sysSaledeliverplanService;
    @Inject
    private SubcontractsaleorderdService subcontractsaleorderdService;
    
    @Override
    protected Subcontractsaleorderm dao() {
        return dao;
    }

    /**
     * U8推单
     * 成功则返回U8单号
     */
    private String pushOrder(Subcontractsaleorderm subcontractsaleorderm, List<Subcontractsaleorderd> subcontractsaleorderds) {
        Dictionary businessType = dictionaryService.getOptionListByTypeKey("order_business_type").stream().filter(item -> StrUtil.equals(item.getSn(), subcontractsaleorderm.getIBusType().toString())).findFirst().orElse(null);
        String busName = Optional.ofNullable(businessType).map(Dictionary::getName).orElse("普通销售");
        String cSTCode = Optional.ofNullable(saleTypeService.findById(subcontractsaleorderm.getISaleTypeId())).map(SaleType::getCSTCode).orElse("普通销售");

        // 封装JSON
        JSONArray jsonArray = new JSONArray();
        for (Subcontractsaleorderd subcontractsaleorderd : subcontractsaleorderds) {
            for (int i = 1; i <= 31; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("DocNo", subcontractsaleorderm.getCOrderNo());
                jsonObject.put("ccuscode", Optional.ofNullable(customerService.findById(subcontractsaleorderm.getICustomerId())).map(Customer::getCCusCode).orElse(""));
                jsonObject.put("cmaker", JBoltUserKit.getUserName());
                jsonObject.put("dDate", DateUtils.formatDate(subcontractsaleorderm.getDCreateTime()));
                jsonObject.put("cPersonCode", subcontractsaleorderm.getIBusPersonId());
                jsonObject.put("cBusType", busName);
                jsonObject.put("cSTCode", cSTCode);
                jsonObject.put("cexch_name", subcontractsaleorderm.getIExchangeRate());
                jsonObject.put("iExchRate", subcontractsaleorderm.getIExchangeRate());
                jsonObject.put("iTaxRate", subcontractsaleorderm.getITaxRate());
                Inventory inventory = inventoryService.findById(subcontractsaleorderd.getIInventoryId());
                jsonObject.put("cInvCode", inventory.getCInvCode());
                jsonObject.put("cInvName", inventory.getCInvName1());
                jsonObject.put("iQuantity", subcontractsaleorderd.getInt("iqty" + i));
                jsonObject.put("irowno", subcontractsaleorderd.getIRowNo());
                jsonObject.put("iQuotedPrice", 0);
                jsonObject.put("KL", 100);
                jsonObject.put("iNatDisCount", 0);
                jsonArray.add(jsonObject);
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("data", jsonArray);

        // 推送U8
        Map<String, String> header = new HashMap<>(5);
        header.put("Content-Type", "application/json");
        String url = "http://120.24.44.82:8099/api/cwapi/SODocAdd?dbname=U8Context";
        String post = HttpApiUtils.httpHutoolPost(url, data, header);
        JSONObject jsonObject = JSON.parseObject(post);
        ValidationUtils.notNull(jsonObject, "推送U8失败");
        ValidationUtils.equals("S", jsonObject.getString("status"), "失败推单:" + jsonObject.getString("remark"));

        String remark = jsonObject.getString("remark");
        return remark.substring(remark.indexOf("执行结果为") + 5);
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        para.set("iorgid", getOrgId());
        Page<Record> pageList = dbTemplate("subcontractsaleorderm.paginateAdminDatas", para).paginate(pageNumber, pageSize);
        List<Record> list = pageList.getList();
        for (Record row : list) {
            Dept dept = deptService.findById(row.getLong("idepartmentid"));
            row.set("cdepname", dept == null ? null : dept.getName());
            row.set("cbususername", JBoltUserCache.me.getUserName(row.getLong("iBusPersonId")));
            row.set("ccurrencyname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.ccurrency.name(), row.getStr("cCurrency")));

            // 审核中，并且单据审批方式为审批流
            if (ObjUtil.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), row.getInt(IAUDITSTATUS)) && ObjUtil.equals(AuditWayEnum.FLOW.getValue(), row.getInt(IAUDITWAY))) {
                row.put("approvalusers", FormApprovalCache.ME.getNextApprovalUserNames(row.getLong("iautoid"), 5));
            }
        }
        weekOrderMService.change(list);
        pageList.setList(list);

        return pageList;
    }

    /**
     * 保存
     */
    public Ret save(Subcontractsaleorderm subcontractsaleorderm) {
        if (subcontractsaleorderm == null || isOk(subcontractsaleorderm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // if(existsName(subcontractsaleorderm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = subcontractsaleorderm.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderm.getName())
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Subcontractsaleorderm subcontractsaleorderm) {
        if (subcontractsaleorderm == null || notOk(subcontractsaleorderm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        Subcontractsaleorderm dbSubcontractsaleorderm = findById(subcontractsaleorderm.getIAutoId());
        if (dbSubcontractsaleorderm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(subcontractsaleorderm.getName(), subcontractsaleorderm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = subcontractsaleorderm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderm.getName())
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        List<Subcontractsaleorderm> list = getListByIds(ids);
        List<Subcontractsaleorderm> notAuditList = new ArrayList<>();
        for (Subcontractsaleorderm subcontractsaleorderm : list) {
            ValidationUtils.equals(subcontractsaleorderm.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
            if (WeekOrderStatusEnum.NOT_AUDIT.getValue() != subcontractsaleorderm.getIOrderStatus() && WeekOrderStatusEnum.REJECTED.getValue() != subcontractsaleorderm.getIOrderStatus()) {
                notAuditList.add(subcontractsaleorderm);
            }

            subcontractsaleorderm.setIsDeleted(true);
        }

        ValidationUtils.assertEmpty(notAuditList, "存在非已保存订单");
        ValidationUtils.isTrue(batchUpdate(list).length > 0, JBoltMsg.FAIL);

        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        Subcontractsaleorderm subcontractsaleorderm = findById(id);
        ValidationUtils.equals(subcontractsaleorderm.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
        return updateColumn(id, "isdeleted", true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param subcontractsaleorderm 要删除的model
     * @param kv                    携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Subcontractsaleorderm subcontractsaleorderm, Kv kv) {
        //addDeleteSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(),subcontractsaleorderm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param subcontractsaleorderm 要删除的model
     * @param kv                    携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Subcontractsaleorderm subcontractsaleorderm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(subcontractsaleorderm, kv);
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
     * @param subcontractsaleorderm 要toggle的model
     * @param column                操作的哪一列
     * @param kv                    携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Subcontractsaleorderm subcontractsaleorderm, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Subcontractsaleorderm subcontractsaleorderm, String column, Kv kv) {
        //addUpdateSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderm.getName(),"的字段["+column+"]值:"+subcontractsaleorderm.get(column))
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param subcontractsaleorderm model
     * @param kv                    携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Subcontractsaleorderm subcontractsaleorderm, Kv kv) {
        //这里用来覆盖 检测Subcontractsaleorderm是否被其它表引用
        return null;
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        Subcontractsaleorderm subcontractsaleorderm = jBoltTable.getFormModel(Subcontractsaleorderm.class, "subcontractsaleorderm");
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            if (subcontractsaleorderm.getIAutoId() == null) {
                subcontractsaleorderm.setIOrgId(getOrgId());
                subcontractsaleorderm.setCOrgCode(getOrgCode());
                subcontractsaleorderm.setCOrgName(getOrgName());
                subcontractsaleorderm.setCOrderNo(BillNoUtils.genCode(getOrgCode(), table()));
                subcontractsaleorderm.setICreateBy(user.getId());
                subcontractsaleorderm.setCCreateName(user.getName());
                subcontractsaleorderm.setDCreateTime(now);
                subcontractsaleorderm.setIUpdateBy(user.getId());
                subcontractsaleorderm.setCUpdateName(user.getName());
                subcontractsaleorderm.setDUpdateTime(now);
                subcontractsaleorderm.setIsDeleted(false);
                ValidationUtils.isTrue(subcontractsaleorderm.save(), ErrorMsg.SAVE_FAILED);
            } else {
                subcontractsaleorderm.setIUpdateBy(user.getId());
                subcontractsaleorderm.setCUpdateName(user.getName());
                subcontractsaleorderm.setDUpdateTime(now);
                ValidationUtils.isTrue(subcontractsaleorderm.update(), ErrorMsg.UPDATE_FAILED);
            }
            saveTableSubmitDatas(jBoltTable, subcontractsaleorderm);
            updateTableSubmitDatas(jBoltTable, subcontractsaleorderm);
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return successWithData(subcontractsaleorderm.keep("iautoid"));
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, Subcontractsaleorderm subcontractsaleorderm) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            if (i == 0) {
                // 避免保存不到所有字段的问题
                Set<String> columnNames = TableMapping.me().getTable(Subcontractsaleorderm.class).getColumnNameSet();
                for (String columnName : columnNames) {
                    if (null == row.get(columnName)) {
                        row.set(columnName, null);
                    }
                }
            }
            row.keep("iautoid", "iinventoryid", "iqty1", "iqty2", "iqty3", "iqty4", "iqty5", "iqty6", "iqty7", "iqty8", "iqty9", "iqty10", "iqty11", "iqty12",
                    "iqty13", "iqty14", "iqty15", "iqty16", "iqty17", "iqty18", "iqty19", "iqty20", "iqty21", "iqty22", "iqty23", "iqty24", "iqty25",
                    "iqty26", "iqty27", "iqty28", "iqty29", "iqty30", "iqty31", "isum");
            row.set("isdeleted", "0");
            row.set("isubcontractsaleordermid", subcontractsaleorderm.getIAutoId());
            row.set("iautoid", JBoltSnowflakeKit.me.nextId());
            row.set("irowno", i* 10);
        }
        subcontractsaleorderdService.batchSaveRecords(list);
    }

    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, Subcontractsaleorderm subcontractsaleorderm) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (Record row : list) {
            row.keep("iautoid", "iinventoryid", "iqty1", "iqty2", "iqty3", "iqty4", "iqty5", "iqty6", "iqty7", "iqty8", "iqty9", "iqty10", "iqty11", "iqty12",
                    "iqty13", "iqty14", "iqty15", "iqty16", "iqty17", "iqty18", "iqty19", "iqty20", "iqty21", "iqty22", "iqty23", "iqty24", "iqty25",
                    "iqty26", "iqty27", "iqty28", "iqty29", "iqty30", "iqty31", "isum");
        }
        subcontractsaleorderdService.batchUpdateRecords(list);
    }

    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        for (Object id : ids) {
            subcontractsaleorderdService.deleteById(id);
        }
    }

    /**
     * 关闭
     */
    public Ret close(String iautoid) {
        Subcontractsaleorderm subcontractsaleorderm = findById(iautoid);
        ValidationUtils.equals(WeekOrderStatusEnum.APPROVED.getValue(), subcontractsaleorderm.getIOrderStatus(), "订单非已审批状态");
        return updateColumn(iautoid, "iOrderStatus", WeekOrderStatusEnum.CLOSE.getValue());
    }

    /**
     * 打开
     */
    public Ret open(String iautoid) {
        Subcontractsaleorderm subcontractsaleorderm = findById(iautoid);
        ValidationUtils.equals(WeekOrderStatusEnum.CLOSE.getValue(), subcontractsaleorderm.getIOrderStatus(), "订单非已关闭状态");
        return updateColumn(iautoid, "iOrderStatus", WeekOrderStatusEnum.APPROVED.getValue());
    }

    /**
     * 处理审批通过的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        Subcontractsaleorderm subcontractsaleorderm = findById(formAutoId);
        List<Subcontractsaleorderd> subcontractsaleorderds = subcontractsaleorderdService.findByMId(formAutoId);

        // 订单状态校验
//        ValidationUtils.equals(subcontractsaleorderm.getIOrderStatus(), MonthOrderStatusEnum.AWAIT_AUDITED.getValue(), "订单非待审核状态");

        // 生成销售出库单
        ValidationUtils.isTrue(sysSaledeliverplanService.saveBySubcontractSaleOrderDatas(subcontractsaleorderm, subcontractsaleorderds), "生成销售发货单失败");

        // 推送U8订单
        String cDocNo = pushOrder(subcontractsaleorderm, subcontractsaleorderds);
        ValidationUtils.notNull(cDocNo, "推单失败");

        // 订单状态修改
        subcontractsaleorderm.setIOrderStatus(MonthOrderStatusEnum.AUDITTED.getValue());
        subcontractsaleorderm.setIUpdateBy(JBoltUserKit.getUserId());
        subcontractsaleorderm.setCUpdateName(JBoltUserKit.getUserName());
        subcontractsaleorderm.setDUpdateTime(new Date());
        subcontractsaleorderm.setIPushTo(1);
        subcontractsaleorderm.setCDocNo(cDocNo);
        subcontractsaleorderm.update();
        // 审批通过生成客户计划汇总
        cusOrderSumService.algorithmSum();
        return null;
    }

    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    /**
     * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
     */
    public String postRejectFunc(long formAutoId, Boolean isWithinBatch) {
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
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isFail())
            {
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isOk(), JBoltMsg.FAIL);
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        // 反审回第一个节点，回退状态为“已保存”
        else if (isFirst) {
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isFail())
            {
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.SAVED.getValue()).isOk(), JBoltMsg.FAIL);
        }
        // 最后一步通过的，反审，回退状态为“待审核”
        else if (isLast) {
            if (updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.AWAIT_AUDITED.getValue()).isFail())
            {
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
        Subcontractsaleorderm subcontractsaleorderm = findById(formAutoId);
        if (WeekOrderStatusEnum.NOT_AUDIT.getValue() == subcontractsaleorderm.getIOrderStatus() || WeekOrderStatusEnum.REJECTED.getValue() == subcontractsaleorderm.getIOrderStatus()) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", MonthOrderStatusEnum.AWAIT_AUDITED.getValue()).isOk(), "提审失败");
        }
        return null;
    }

    /**
     * 撤回审核业务处理，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFunc(long formAutoId) {
        Subcontractsaleorderm subcontractsaleorderm = findById(formAutoId);
//        ValidationUtils.equals(subcontractsaleorderm.getIOrderStatus(), MonthOrderStatusEnum.AWAIT_AUDITED.getValue(), "订单非待审批状态");
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
        List<Subcontractsaleorderm> subcontractsaleorderms = getListByIds(StringUtils.join(formAutoIds, COMMA));
        
        boolean algorithmSum = subcontractsaleorderms.stream().anyMatch(item -> item.getIOrderStatus().equals(WeekOrderStatusEnum.APPROVED.getValue()));
        
        subcontractsaleorderms = subcontractsaleorderms.stream().peek(item -> item.setIOrderStatus(WeekOrderStatusEnum.NOT_AUDIT.getValue())).collect(Collectors.toList());
        batchUpdate(subcontractsaleorderms);

        if (algorithmSum) {
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        return null;
    }
    
}