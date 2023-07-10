package cn.rjtech.admin.weekorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.saletype.SaleTypeService;
import cn.rjtech.admin.weekorderd.WeekOrderDService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.MonthOrderStatusEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.SaleType;
import cn.rjtech.model.momdata.WeekOrderD;
import cn.rjtech.model.momdata.WeekOrderM;
import cn.rjtech.model.momdata.base.BaseWeekOrderD;
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
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 客户订单-周间客户订单
 *
 * @ClassName: WeekOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 14:37
 */
public class WeekOrderMService extends BaseService<WeekOrderM> implements IApprovalService {

    private final WeekOrderM dao = new WeekOrderM().dao();

    @Inject
    private WeekOrderDService weekOrderDService;
    @Inject
    private CusOrderSumService cusOrderSumService;
    @Inject
    private FormApprovalService formApprovalService;
    @Inject
    private DictionaryService dictionaryService;
    @Inject
    private SaleTypeService saleTypeService;

    @Override
    protected WeekOrderM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.WEEK_ORDER.getValue();
    }

    /**
     * U8推单
     * 成功则返回U8单号
     */
    private String pushOrder(WeekOrderM weekOrderM, List<WeekOrderD> weekOrderDS) {
        Dictionary businessType = dictionaryService.getOptionListByTypeKey("order_business_type").stream().filter(item -> StrUtil.equals(item.getSn(), weekOrderM.getIBusType().toString())).findFirst().orElse(null);
        String busName = Optional.ofNullable(businessType).map(Dictionary::getName).orElse("普通销售");
        String cSTCode = Optional.ofNullable(saleTypeService.findById(weekOrderM.getISaleTypeId())).map(SaleType::getCSTCode).orElse("普通销售");

        // 封装JSON
        JSONArray jsonArray = new JSONArray();
        for (WeekOrderD weekOrderD : weekOrderDS) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DocNo", weekOrderM.getCOrderNo());
            jsonObject.put("ccuscode", weekOrderM.getCCusCode());
            jsonObject.put("cmaker", JBoltUserKit.getUserName());
            jsonObject.put("dDate", DateUtils.formatDate(weekOrderM.getDCreateTime()));
            jsonObject.put("cPersonCode", weekOrderM.getIBusPersonId());
            jsonObject.put("cBusType", busName);
            jsonObject.put("cSTCode", cSTCode);
            jsonObject.put("cexch_name", weekOrderM.getIExchangeRate());
            jsonObject.put("iExchRate", weekOrderM.getIExchangeRate());
            jsonObject.put("iTaxRate", weekOrderM.getITaxRate());
            jsonObject.put("cInvCode", weekOrderD.getCInvCode());
            jsonObject.put("cInvName", weekOrderD.getCInvName1());
            jsonObject.put("iQuantity", weekOrderD.getIQty());
            jsonObject.put("irowno", weekOrderD.getIRowNo());
            jsonObject.put("iQuotedPrice", 0);
            jsonObject.put("KL", 100);
            jsonObject.put("iNatDisCount", 0);
            jsonArray.add(jsonObject);
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
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("weekorderm.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
        change(paginate.getList());
        return paginate;
    }

    public void change(List<Record> records){
        if (CollUtil.isEmpty(records)){
            return;
        }
        List<Dictionary> saleTypeList = dictionaryService.getOptionListByTypeKey("sale_type");
        List<Dictionary> orderBusinessType = dictionaryService.getOptionListByTypeKey("order_business_type");
        Map<String, Dictionary> saleTypeMap = saleTypeList.stream().collect(Collectors.toMap(Dictionary::getSn, Function.identity()));
        Map<String, Dictionary> orderBusinessMap = orderBusinessType.stream().collect(Collectors.toMap(Dictionary::getSn, Function.identity()));
        records.forEach(record -> {
            if (orderBusinessMap.containsKey(record.getStr("ibustype"))){
                Dictionary dictionary = orderBusinessMap.get(record.getStr("ibustype"));
                record.set("bustypename", dictionary.getName());
            }
            if(saleTypeMap.containsKey(record.getStr("isaletypeid"))){
                Dictionary dictionary = saleTypeMap.get(record.getStr("isaletypeid"));
                record.set("saletypename", dictionary.getName());
            }
        });
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
        WeekOrderM weekOrderM = findById(id);
        ValidationUtils.equals(weekOrderM.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
        return updateColumn(id, "IsDeleted", true);
    }

    public List<Record> weekOrderMData(Long iWeekOrderMid) {
        return dbTemplate("weekorderm.weekOrderMData", Okv.by("iWeekOrderMid", iWeekOrderMid)).find();
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
        List<WeekOrderM> list = getListByIds(ids);
        List<WeekOrderM> notAuditList = new ArrayList<>();

        for (WeekOrderM weekOrderM : list) {
            ValidationUtils.equals(weekOrderM.getICreateBy(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
            if (WeekOrderStatusEnum.NOT_AUDIT.getValue() != weekOrderM.getIOrderStatus() && WeekOrderStatusEnum.REJECTED.getValue() != weekOrderM.getIOrderStatus()) {
                notAuditList.add(weekOrderM);
            }

            weekOrderM.setIsDeleted(true);
        }

        ValidationUtils.isTrue(notAuditList.size() == 0, "存在非已保存订单");
        ValidationUtils.isTrue(batchUpdate(list).length > 0, JBoltMsg.FAIL);

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

        return successWithData(weekOrderM.keep("iautoid"));
    }

    private void doSave(WeekOrderM weekOrderM, JBoltTable jBoltTable, Date now) {
        weekOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
        weekOrderM.setIOrderStatus(WeekOrderStatusEnum.NOT_AUDIT.getValue());

        // 组织信息
        weekOrderM.setIOrgId(getOrgId());
        weekOrderM.setCOrgCode(getOrgCode());
        weekOrderM.setCOrgName(getOrgName());
        weekOrderM.setCOrderNo(BillNoUtils.genCode(getOrgCode(), table()));
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
        } catch (Exception e) {
            ValidationUtils.error( "周间客户订单不合法,请检查订单数据!");
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
            weekOrderDService.deleteByIds(jBoltTable.getDelete());
        } catch (Exception e) {
            ValidationUtils.error( "周间客户订单不合法,请检查订单数据!");
        }
    }

    private void saveDs(List<WeekOrderD> save, long iweekordermid) {
        Integer rowno = 10;
        for (BaseWeekOrderD row : save) {
            row.set("IWeekOrderMid", iweekordermid)
                    .set("iautoid", JBoltSnowflakeKit.me.nextId())
                    .set("isdeleted", ZERO_STR);
            row.setIRowNo(rowno);
            rowno += 10;
        }
        weekOrderDService.batchSave(save);
    }

    private void updateDs(List<WeekOrderD> update) {
        if (CollUtil.isNotEmpty(update)) {
            weekOrderDService.batchUpdate(update);
        }
    }

    public Page<Record> updateCplanTimeDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        List<Record> records = new ArrayList<>();
        Map<String, Record> batchUpdateMap = new HashMap<>();
        if (notNull(kv.get("params"))) {
            JSONObject params = JSON.parseObject(kv.getStr("params"));
            // 批量调整
            List<Record> batchUpdateDatas = JBoltModelKit.getFromRecords(JSON.parseArray(params.getString("batchUpdateDatas")));
            if (notNull(batchUpdateDatas)) {
                List<String> timeDatas = batchUpdateDatas.stream().map(item -> {
                    return item.getStr("dplanaogdate") + "," + item.getStr("cplanaogtime");
                }).collect(Collectors.toList());
                kv.set("timedatas", timeDatas);
                batchUpdateMap = batchUpdateDatas.stream().collect(Collectors.toMap(record -> {
                    return record.getStr("dplanaogdate") + "," + record.getStr("cplanaogtime");
                }, Function.identity(), (key1, key2) -> key1));

            }

            // 存货调整
            List<Record> cinvUpdateDatas = JBoltModelKit.getFromRecords(JSON.parseArray(params.getString("cinvUpdateDatas")));
            if (notNull(cinvUpdateDatas)) {
                records.addAll(cinvUpdateDatas);
            }
        }
        Page<Record> page = dbTemplate("weekorderm.updateCplanTimeDatas", kv).paginate(pageNumber, pageSize);

        if (!batchUpdateMap.isEmpty()) {
            List<Record> pageList = page.getList();
            for (Record record : pageList) {
                String time = record.getStr("dplanaogdate") + "," + record.getStr("cplanaogtime");
                Record updateRecode = batchUpdateMap.get(time);
                if (notNull(updateRecode)) {
                    record.setColumns(updateRecode);
                }
            }
            records.addAll(pageList);
            // 去重
            records = records.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(record -> record.getStr("iautoid")))), ArrayList::new));
        }

        if (!records.isEmpty()) {
            page.setList(records);
        }
        return page;
    }

    public Ret saveUpdateCplanTime(JBoltTable jBoltTable) {
        return SUCCESS;
    }

    /**
     * 打开
     */
    public Ret open(String iautoid) {
        WeekOrderM weekOrderM = findById(iautoid);
        ValidationUtils.equals(WeekOrderStatusEnum.CLOSE.getValue(), weekOrderM.getIOrderStatus(), "订单非已关闭状态");
        weekOrderM.setIOrderStatus(WeekOrderStatusEnum.APPROVED.getValue());
        ValidationUtils.isTrue(weekOrderM.update(), JBoltMsg.FAIL);
        return SUCCESS;
    }

    /**
     * 处理审批通过的其他业务操作，如有异常返回错误信息
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        WeekOrderM weekOrderM = findById(formAutoId);
//        ValidationUtils.equals(WeekOrderStatusEnum.AWAIT_AUDIT.getValue(), weekOrderM.getIOrderStatus(), "订单非待审核状态");
        // 推送U8订单
        List<WeekOrderD> weekOrderDS = weekOrderDService.findByMId(formAutoId);
        String cDocNo = pushOrder(weekOrderM, weekOrderDS);
        ValidationUtils.notNull(cDocNo, "推单失败");

        // 修改客户计划汇总
        ValidationUtils.isTrue(updateColumn(formAutoId, "iPushTo", 1).isOk(), JBoltMsg.FAIL);
        ValidationUtils.isTrue(updateColumn(formAutoId, "cDocNo", cDocNo).isOk(), JBoltMsg.FAIL);
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.APPROVED.getValue()).isOk(), JBoltMsg.FAIL);
        cusOrderSumService.algorithmSum();
        return null;
    }

    /**
     * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
     */
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        WeekOrderM weekOrderM = findById(formAutoId);
//        ValidationUtils.equals(weekOrderM.getIOrderStatus(), WeekOrderStatusEnum.AWAIT_AUDIT.getValue(), "订单非待审核状态");
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.REJECTED.getValue()).isOk(), JBoltMsg.FAIL);
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
            if (updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.NOT_AUDIT.getValue()).isFail()) {
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.NOT_AUDIT.getValue()).isOk(), JBoltMsg.FAIL);
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        // 反审回第一个节点，回退状态为“已保存”
        else if (isFirst) {
            if (updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.NOT_AUDIT.getValue()).isFail())
            {
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.NOT_AUDIT.getValue()).isOk(), JBoltMsg.FAIL);
        }
        // 最后一步通过的，反审，回退状态为“待审核”
        else if (isLast) {
            if (updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.AWAIT_AUDIT.getValue()).isFail())
            {
                Log.info("更新失败");
                return "更新失败";
            }
//            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.AWAIT_AUDIT.getValue()).isOk(), JBoltMsg.FAIL);
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
        WeekOrderM weekOrderM = findById(formAutoId);
        if (WeekOrderStatusEnum.NOT_AUDIT.getValue() == weekOrderM.getIOrderStatus()|| WeekOrderStatusEnum.REJECTED.getValue() == weekOrderM.getIOrderStatus()) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.AWAIT_AUDIT.getValue()).isOk(), "提审失败");
        }
        return null;
    }

    /**
     * 撤回审核业务处理，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFunc(long formAutoId) {
        WeekOrderM weekOrderM = findById(formAutoId);
//        ValidationUtils.equals(weekOrderM.getIOrderStatus(), WeekOrderStatusEnum.AWAIT_AUDIT.getValue(), "订单非待审批状态");
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.NOT_AUDIT.getValue()).isOk(), "撤回失败");
        return null;
    }

    /**
     * 从审批中，撤回到已保存，业务实现，如有异常返回错误信息
     */
    @Override
    public String withdrawFromAuditting(long formAutoId) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.NOT_AUDIT.getValue()).isOk(), JBoltMsg.FAIL);
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
        ValidationUtils.isTrue(updateColumn(formAutoId, "iOrderStatus", WeekOrderStatusEnum.NOT_AUDIT.getValue()).isOk(), JBoltMsg.FAIL);
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
        List<WeekOrderM> weekOrderMS = getListByIds(StringUtils.join(formAutoIds, COMMA));
        Boolean algorithmSum = weekOrderMS.stream().anyMatch(item -> item.getIOrderStatus().equals(WeekOrderStatusEnum.APPROVED.getValue()));
        weekOrderMS.stream().map(item -> {
            item.setIOrderStatus(WeekOrderStatusEnum.NOT_AUDIT.getValue());
            return item;
        }).collect(Collectors.toList());
        batchUpdate(weekOrderMS);

        if (algorithmSum) {
            // 修改客户计划汇总
            cusOrderSumService.algorithmSum();
        }
        return null;
    }
}
