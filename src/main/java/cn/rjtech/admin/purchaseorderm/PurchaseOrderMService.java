package cn.rjtech.admin.purchaseorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.HttpUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.demandpland.DemandPlanDService;
import cn.rjtech.admin.demandplanm.DemandPlanMService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.purchaseorderd.PurchaseOrderDService;
import cn.rjtech.admin.purchaseorderdbatch.PurchaseOrderDBatchService;
import cn.rjtech.admin.purchaseorderdbatchversion.PurchaseOrderDBatchVersionService;
import cn.rjtech.admin.purchaseorderdqty.PurchaseorderdQtyService;
import cn.rjtech.admin.purchaseorderref.PurchaseOrderRefService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购/委外订单-采购订单主表
 * @ClassName: PurchaseOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 15:19
 */
public class PurchaseOrderMService extends BaseService<PurchaseOrderM> {

    private final PurchaseOrderM dao = new PurchaseOrderM().dao();

    @Inject
    private MomDataFuncService                momDataFuncService;
    @Inject
    private DemandPlanMService                demandPlanMService;
    @Inject
    private DemandPlanDService                demandPlanDService;
    @Inject
    private PurchaseorderdQtyService          purchaseorderdQtyService;
    @Inject
    private PurchaseOrderDService             purchaseOrderDService;
    @Inject
    private PurchaseOrderRefService           purchaseOrderRefService;
    @Inject
    private VendorAddrService                 vendorAddrService;
    @Inject
    private DictionaryService                 dictionaryService;
    @Inject
    private PurchaseOrderDBatchService        purchaseOrderDBatchService;
    @Inject
    private PurchaseOrderDBatchVersionService purchaseOrderDBatchVersionService;
    @Inject
    private InventoryService                  inventoryService;
    @Inject
    private WarehouseService warehouseService;
    @Inject
    private DepartmentService departmentService;

    @Override
    protected PurchaseOrderM dao() {
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
        Page<Record> page = dbTemplate("purchaseorderm.findAll", kv).paginate(pageNumber, pageSize);
        changeData(page.getList());
        return page;
    }

    private void changeData(List<Record> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        // 应付类型
        List<Dictionary> purchaseCopingTypeList = dictionaryService.getListByTypeKey("purchase_coping_type", true);

        Map<String, Dictionary> purchaseCopingTypeMap = purchaseCopingTypeList.stream()
            .collect(Collectors.toMap(p -> p.getSn(), p -> p));
        // 业务类型
        List<Dictionary> purchaseBusinessTypeList = dictionaryService.getListByTypeKey("purchase_business_type", true);

        Map<String, Dictionary> purchaseBusinessTypeMap = purchaseBusinessTypeList.stream()
            .collect(Collectors.toMap(p -> p.getSn(), p -> p));

        for (Record record : list) {
            Integer orderStatus = record.getInt(PurchaseOrderM.IORDERSTATUS);
            OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(orderStatus);
            ValidationUtils.notNull(orderStatusEnum, "未知订单状态");
            record.set(PurchaseOrderM.ADUITSTATUSTEXT, orderStatusEnum.getText());
            String iPayableType = record.getStr(PurchaseOrderM.IPAYABLETYPE);

            String iBusType = record.getStr(PurchaseOrderM.IBUSTYPE);
            if (purchaseCopingTypeMap.containsKey(iPayableType)) {
                record.set(PurchaseOrderM.PAYABLETYPETEXT, purchaseCopingTypeMap.get(iPayableType).getName());
            }
            if (purchaseBusinessTypeMap.containsKey(iBusType)) {
                record.set(PurchaseOrderM.BUSTYPETEXT, purchaseBusinessTypeMap.get(iBusType).getName());
            }
            Integer type = record.getInt(PurchaseOrderM.ITYPE);
            SourceTypeEnum sourceTypeEnum = SourceTypeEnum.toEnum(type);
            if (ObjUtil.isNotNull(sourceTypeEnum)) {
                record.set(PurchaseOrderM.TYPESTR, sourceTypeEnum.getText());
            }
        }
    }

    /**
     * 保存
     */
    public Ret save(PurchaseOrderM purchaseOrderM) {
        if (purchaseOrderM == null || isOk(purchaseOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        purchaseOrderM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
        purchaseOrderM.setICreateBy(user.getId());
        purchaseOrderM.setCCreateName(user.getName());
        purchaseOrderM.setDCreateTime(now);
        purchaseOrderM.setIUpdateBy(user.getId());
        purchaseOrderM.setCUpdateName(user.getName());
        purchaseOrderM.setDUpdateTime(now);
        purchaseOrderM.setIsDeleted(false);
        //if(existsName(purchaseOrderM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = purchaseOrderM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(PurchaseOrderM purchaseOrderM) {
        if (purchaseOrderM == null || notOk(purchaseOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        PurchaseOrderM dbPurchaseOrderM = findById(purchaseOrderM.getIAutoId());
        if (dbPurchaseOrderM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(purchaseOrderM.getName(), purchaseOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = purchaseOrderM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderM.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param purchaseOrderM 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(PurchaseOrderM purchaseOrderM, Kv kv) {
        //addDeleteSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(),purchaseOrderM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param purchaseOrderM model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkInUse(PurchaseOrderM purchaseOrderM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<Record> findByInventoryId(Long inventoryId) {
        ValidationUtils.notNull(inventoryId, JBoltMsg.DATA_NOT_EXIST);
        return dbTemplate("inventory.findByInventoryId", Okv.by("inventoryId", inventoryId)).find();

    }

    /**
     * CG年月日4流水号
     */
    public String generateCGCode() {
        String prefix = "CG";
        String format = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_FORMAT);
        return momDataFuncService.getNextNo(prefix.concat(format), 4);
    }

    /**
     * 判断日期 是否为当月/当周/当日
     */
    public static boolean isThisTime(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }


    /**
     * @return 日期key：dayValue
     */
    public Map<String, Integer> getCalendarMap(DateTime startDate, DateTime endDate) {
        Calendar startCalendar = createCalendar(startDate, true);
        Calendar endCalendar = createCalendar(endDate, false);
        int dayNum = 0;
        // 记录有多少个日期
        Map<String, Integer> calendarMap = new HashMap<>();
        while (endCalendar.after(startCalendar)) {
            DateTime date = DateUtil.date(startCalendar);
            calendarMap.put(DateUtil.formatDate(date), dayNum);
            dayNum += 1;
            startCalendar.add(Calendar.DATE, 1);
        }
        return calendarMap;
    }

    private Calendar createCalendar(DateTime date, boolean isFrist) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.year());
        calendar.set(Calendar.MONTH, date.month());
        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth());
        // 设值时分秒为00:00
        if (isFrist) {
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            return calendar;
        }
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public Map<String, Object> getDateMap(String beginDateStr, String endDateStr, String iVendorId, Integer processType,
                                          Integer iSourceType) {
        Map<String, Object> repMap = new HashMap<>();
        // 获取日期
        Map<String, List<Record>> listMap = setDate(beginDateStr, endDateStr);
        List<Record> dateHeadList = listMap.get(PurchaseOrderM.DATEFIELD);
        List<Record> monthHeadList = listMap.get(PurchaseOrderM.MONTHFIELD);
        // 物料到货计划 才回去返查 到货计划明细
        if (SourceTypeEnum.MATERIAL_PLAN_TYPE.getValue() == iSourceType) {

            Map<String, Record> recordMap = dateHeadList.stream()
                .collect(Collectors.toMap(record -> record.getStr("dateStr"), record -> record));

            // 主表获取存货数据 table--value
            List<Record> vendorDateList = demandPlanMService.getVendorDateList(beginDateStr, endDateStr, iVendorId, processType);
            // 细表获取存货数量
            List<Record> demandPlanDList = demandPlanDService
                .findByDemandPlanMList(beginDateStr, endDateStr, iVendorId, processType);
            // 记录每一个存货中存在多个物料到货计划
            Map<Long, List<PurchaseOrderRef>> puOrderRefMap = demandPlanDService.getPuOrderRefByInvId(demandPlanDList);
            // 按存货编码汇总
            Map<Long, Map<String, BigDecimal>> demandPlanDMap = demandPlanDService
                .getDemandPlanDMap(demandPlanDList, DemandPlanM.IINVENTORYID);

            // key= 2023年04月_存货id
            DateTime endDate = DateUtil.parseDate(endDateStr);
            Map<String, BigDecimal> ymQtyMap = demandPlanDList.stream().filter(
                record -> {
                    String yearStr = record.getStr(PurchaseorderdQty.IYEAR);
                    String monthStr = String.format("%02d", record.getInt(PurchaseorderdQty.IMONTH));
                    String dateStr = String.format("%02d", record.getInt(PurchaseorderdQty.IDATE));
                    String dateFormStr = yearStr.concat("-").concat(monthStr).concat("-").concat(dateStr);
                    return DateUtil.compare(endDate, DateUtil.parseDate(dateFormStr)) >= 0;
                }
            ).collect(Collectors.groupingBy(record -> {
                String monthStr = String.format("%02d", record.getInt(PurchaseorderdQty.IMONTH));
                String invId = record.getStr(PurchaseOrderD.IINVENTORYID);
                String yearStr = record.getStr(PurchaseorderdQty.IYEAR);
                return yearStr.concat("年").concat(monthStr).concat("月").concat("_").concat(invId);
            }, Collectors.mapping(record -> record.getBigDecimal(PurchaseorderdQty.IQTY),
                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

            // 设置到货计划明细数量
            demandPlanMService
                .setVendorDateList(OrderGenTypeEnum.PURCHASE_GEN.getValue(), vendorDateList, demandPlanDMap, recordMap,
                    puOrderRefMap, ymQtyMap);
            // 设置
            repMap.put("tableData", vendorDateList);
            repMap.put("tableDataStr", JSONObject.toJSONString(vendorDateList));
        }
        repMap.put("monthHeadList", monthHeadList);
        repMap.put("dateHeadList", dateHeadList);
        return repMap;
    }

    /**
     *
     */
    private Map<String, List<Record>> setDate(String beginDate, String endDate) {
        // 获取所有日期集合
        Map<String, Integer> calendarMap = getCalendarMap(DateUtil.parseDate(beginDate), DateUtil.parseDate(endDate));
        // 用于统计月份
        List<Record> dateMonthHeadList = new ArrayList<>();
        // 用于统计日期，每月最后一天添加合计字段
        List<Record> dateHeadList = new ArrayList<>();
        // 第一层：年月
        // 第二层：日
        Map<String, List<Integer>> dateMap = new TreeMap<>();
        // 日期：月份
        for (String dateStr : calendarMap.keySet()) {
            // yyyy-MM-dd
            DateTime dateTime = DateUtil.parse(dateStr, DatePattern.NORM_DATE_PATTERN);
            // yyyy-MM
            String YMKey = DateUtil.format(dateTime, "yyyy年MM月");
            // 日期
            int dayOfMonth = dateTime.dayOfMonth();
            List<Integer> monthDays = dateMap.containsKey(YMKey) ? dateMap.get(YMKey) : new ArrayList<>();
            monthDays.add(dayOfMonth);
            Collections.sort(monthDays);
            dateMap.put(YMKey, monthDays);
        }
        // 记录日期下标
        int index = 0;
        // 日期：天数
        for (String dateKey : dateMap.keySet()) {
            Record record = new Record();
            List<Integer> list = dateMap.get(dateKey);
            record.set(PurchaseOrderM.SIZE, list.size() + 1);
            record.set(PurchaseOrderM.DATESTR, dateKey);
            Record sumRecord = new Record();
            String sumStr = "";
            for (Integer date : list) {
                String dateStr = String.format("%02d", date).concat("日");
                Record dateRecord = new Record();
                // 记录是否需要在页面显示可编辑
                dateRecord.set(PurchaseOrderM.ISEDITFIELDNAME, true);
                dateRecord.set(PurchaseOrderM.FIELDNAME, dateStr);
                dateRecord.set(PurchaseOrderM.DATESTR, dateKey.concat(dateStr));
                dateRecord.set(PurchaseOrderM.INDEX, index);
                dateHeadList.add(dateRecord);
                index += 1;
                sumStr+=dateKey.concat(dateStr).concat("+");
            }
            sumRecord.set(PurchaseOrderM.FIELDNAME, "合计");
            // 需要统计的月份
            sumRecord.set(PurchaseOrderM.DATESTR, dateKey);
            sumRecord.set(PurchaseOrderM.INDEX, index);
            if (StrUtil.isNotBlank(sumStr)){
                sumRecord.set(PurchaseOrderM.SUMSTR, sumStr.substring(0, sumStr.length()-1));
            }
           
            index += 1;
            dateHeadList.add(sumRecord);
            dateMonthHeadList.add(record);
        }
        ValidationUtils.notEmpty(dateHeadList, "未获取到日期范围数据");
        Map<String, List<Record>> rep = new HashMap<>();
        rep.put(PurchaseOrderM.MONTHFIELD, dateMonthHeadList);
        rep.put(PurchaseOrderM.DATEFIELD, dateHeadList);
        return rep;
    }

    public Map<String, Object> getDateMap(PurchaseOrderM purchaseOrderM) {

        Map<String, Object> repMap = new HashMap<>();
        Map<String, List<Record>> listMap = setDate(DateUtil.formatDate(purchaseOrderM.getDBeginDate()),
            DateUtil.formatDate(purchaseOrderM.getDEndDate()));
        if (SourceTypeEnum.BLANK_PURCHASE_TYPE.getValue() == purchaseOrderM.getIType()) {
            List<Record> purchaseOrderDList = getPurchaseOrderDList(purchaseOrderM, listMap);
            repMap.put("tableData", purchaseOrderDList);
            repMap.put("tableDataStr", JSONObject.toJSONString(purchaseOrderDList));
        }

        repMap.put("monthHeadList", listMap.get(PurchaseOrderM.MONTHFIELD));
        repMap.put("dateHeadList", listMap.get(PurchaseOrderM.DATEFIELD));
        return repMap;
    }

    /**
     * @param dataStr      所有加载进来的数据
     * @param formStr      // form表单数据
     * @param invTableData // 操作表体数据
     * @param type         区分是保存还是修改 0：保存； 1：修改
     */
    public Ret submit(String dataStr, String formStr, String invTableData, String type, Kv kv) {
        verifyData(dataStr, formStr, invTableData);
        JSONArray jsonArray = JSONObject.parseArray(dataStr);
        JSONObject formJsonObject = JSONObject.parseObject(formStr);
        JSONArray invJsonArray = JSONObject.parseArray(invTableData);

        Map<Long, JSONObject> invDataMap = jsonArray.stream().collect(Collectors
            .toMap(r -> ((JSONObject) r).getLong(PurchaseOrderD.IINVENTORYID.toLowerCase()), r -> (JSONObject) r,
                (key1, key2) -> key2));
        Map<Long, JSONObject> invTableMap = invJsonArray.stream().collect(Collectors
            .toMap(r -> ((JSONObject) r).getLong(PurchaseOrderD.IINVENTORYID), r -> (JSONObject) r, (key1, key2) -> key2));

        switch (type) {
            case "0":
                saveSubmit(formJsonObject, invDataMap, invTableMap);
                break;
            case "1":
                updateSubmit(formJsonObject, invDataMap, invTableMap);
                break;
        }

        return SUCCESS;
    }

    private void updateSubmit(JSONObject formJsonObject, Map<Long, JSONObject> invDataMap, Map<Long, JSONObject> invTableMap) {
        Long id = formJsonObject.getLong(PurchaseOrderM.IAUTOID);
        ValidationUtils.notNull(id, "未获取到主键id");
        PurchaseOrderM purchaseOrderM = findById(id);
        ValidationUtils.notNull(purchaseOrderM, JBoltMsg.DATA_NOT_EXIST);
        setPurchaseOrderM(purchaseOrderM, formJsonObject, JBoltUserKit.getUserId(), JBoltUserKit.getUserName(), DateUtil.date());
        List<Long> notIds = new ArrayList<>();
        for (Long invId : invDataMap.keySet()) {
            JSONObject invDJsonObject = invDataMap.get(invId);
            Long purchaseOrderDId = invDJsonObject.getLong(PurchaseOrderD.IAUTOID.toLowerCase());
            // 存在则修改，不存在这删除,将主键id添加进来
            if (invTableMap.containsKey(invId)) {
                JSONObject invTableJsonObject = invTableMap.get(invId);
                invTableJsonObject.put(PurchaseOrderD.IAUTOID, purchaseOrderDId);
                continue;
            }
            // 添加删除的id
            notIds.add(purchaseOrderDId);
        }

        tx(() -> {
            // 删除 先修改细表状态，在删除中间表数据，在修改到货计划细表及主表状态
            if (CollUtil.isNotEmpty(notIds)) {
                for (Long purchaseOrderDId : notIds) {
                    PurchaseOrderD purchaseOrderD = purchaseOrderDService.findById(purchaseOrderDId);
                    ValidationUtils.notNull(purchaseOrderD, "采购订单细表数据未找到");
                    purchaseOrderD.setIsDeleted(true);
                    purchaseOrderD.update();
                }
                // 根据细表id反查 中间表数据
                List<Record> purchaseOrderDRefList = purchaseOrderRefService.findByPurchaseOrderDIds(notIds);
                // 获取中间表对应的物料到货计划数据
                List<Long> demandPlanDIds = purchaseOrderDRefList.stream()
                    .map(record -> record.getLong(PurchaseOrderRef.IDEMANDPLANDID)).collect(Collectors.toList());
                // 修改到货计划细表状态
                demandPlanDService.batchUpdateGenTypeByIds(demandPlanDIds, OrderGenTypeEnum.NOT_GEN.getValue(),
                    CompleteTypeEnum.INCOMPLETE.getValue());
                // 修改主表状态
                List<Long> demandPlanMIds = demandPlanMService.pegging(demandPlanDIds);
                demandPlanMService.batchUpdateStatusByIds(demandPlanMIds, CompleteTypeEnum.INCOMPLETE.getValue());
                // 删除 中间表数据
                purchaseOrderRefService.removeByPurchaseOrderDIds(notIds);
            }
            // 修改
            for (Long invId : invTableMap.keySet()) {
                JSONObject invJsonObject = invTableMap.get(invId);
                Long purchaseOrderDId = invJsonObject.getLong(PurchaseOrderD.IAUTOID);
                PurchaseOrderD purchaseOrderD = purchaseOrderDService.findById(purchaseOrderDId);
                ValidationUtils.notNull(purchaseOrderD, "采购订单细表数据未找到");
                // 备注
                purchaseOrderD.setCMemo(invJsonObject.getString(PurchaseOrderD.CMEMO));
                // 添加到货地址
                VendorAddr vendorAddr = vendorAddrService.findById(invJsonObject.getLong(PurchaseOrderD.IVENDORADDRID));
                ValidationUtils.notNull(vendorAddr, "供应商地址不存在！！！");
                purchaseOrderD.setCAddress(vendorAddr.getCDistrictName());
                purchaseOrderD.setIVendorAddrId(invJsonObject.getLong(PurchaseOrderD.IVENDORADDRID));
                purchaseOrderD.update();
            }
            purchaseOrderM.update();
            return true;
        });
    }

    /**
     * 保存操作
     */
    private void saveSubmit(JSONObject formJsonObject, Map<Long, JSONObject> invDataMap, Map<Long, JSONObject> invTableMap) {
        // 创建主表对象
        PurchaseOrderM purchaseOrderM = createPurchaseOrderM(formJsonObject);
        // 日期
        Map<String, Integer> calendarMap = getCalendarMap(DateUtil.date(purchaseOrderM.getDBeginDate()),
            DateUtil.date(purchaseOrderM.getDEndDate()));
        // 获取所有明细数据
        // 记录多个子件数据
        List<PurchaseOrderD> purchaseOrderDList = new ArrayList<>();
        List<PurchaseorderdQty> purchaseOrderdQtyList = new ArrayList<>();
        List<PurchaseOrderRef> purchaseOrderdRefList = new ArrayList<>();

        // 校验采购合同号是否存在
        Integer count = findOderNoIsNotExists(purchaseOrderM.getCOrderNo());
        ValidationUtils.isTrue(ObjUtil.isEmpty(count) || count == 0, "采购订单号已存在");
        int seq = 0;
        for (Long inventoryId : invTableMap.keySet()) {
            // 记录供应商地址及备注
            JSONObject invJsonObject = invTableMap.get(inventoryId);
            // 存在这个存货说明没有删除
            if (!invDataMap.containsKey(inventoryId)) {
                continue;
            }
            JSONObject dataJsonObject = invDataMap.get(inventoryId);
            // 添加备注
            dataJsonObject.put(PurchaseOrderD.CMEMO.toLowerCase(), invJsonObject.getString(PurchaseOrderD.CMEMO));
            // 添加到货地址
            VendorAddr vendorAddr = vendorAddrService.findById(invJsonObject.getLong(PurchaseOrderD.IVENDORADDRID));
            ValidationUtils.notNull(vendorAddr, "供应商地址不存在！！！");
            dataJsonObject.put(PurchaseOrderD.CADDRESS.toLowerCase(), vendorAddr.getCDistrictName());
            // 添加到货地址Id
            dataJsonObject.put(PurchaseOrderD.IVENDORADDRID.toLowerCase(), vendorAddr.getIAutoId());
            // 创建采购订单明细
            PurchaseOrderD purchaseOrderD = purchaseOrderDService
                .createPurchaseOrderD(purchaseOrderM.getIAutoId(), dataJsonObject);

            Long purchaseOrderDId = purchaseOrderD.getIAutoId();

            // 创建采购订单明细数量
            JSONArray purchaseOrderdQtyJsonArray = dataJsonObject
                .getJSONArray(PurchaseOrderD.PURCHASEORDERD_QTY_LIST.toLowerCase());
            List<PurchaseorderdQty> createPurchaseOrderdQtyList = purchaseorderdQtyService
                .getPurchaseorderdQty(purchaseOrderDId, purchaseOrderdQtyJsonArray, seq);

            // 创建采购订单与到货计划关联
            JSONArray purchaseOrderRefJsonArray = dataJsonObject.getJSONArray(PurchaseOrderM.PURCHASEORDERREFLIST.toLowerCase());
            List<PurchaseOrderRef> createPurchaseOrderRefList = purchaseOrderRefService
                .getPurchaseOrderRefList(purchaseOrderDId, purchaseOrderRefJsonArray);
            // 添加到集合
            purchaseOrderDList.add(purchaseOrderD);
            purchaseOrderdQtyList.addAll(createPurchaseOrderdQtyList);
            purchaseOrderdRefList.addAll(createPurchaseOrderRefList);
        }

        // 操作
        tx(() -> {

            purchaseOrderDService.batchSave(purchaseOrderDList);
            purchaseorderdQtyService.batchSave(purchaseOrderdQtyList);
            purchaseOrderRefService.batchSave(purchaseOrderdRefList);
            // 修改物料到货计划状态
            List<Long> demandPlanDIds = purchaseOrderdRefList.stream().map(PurchaseOrderRef::getIDemandPlanDid)
                .collect(Collectors.toList());
            // 根据到货细表id反查到货计划主表id
            List<Long> demandPlanMIds = demandPlanMService.pegging(demandPlanDIds);
            // 判断是否需要更改到货计划主表状态 改为已完成
            demandPlanMService.batchUpdateStatusByIds(demandPlanMIds, CompleteTypeEnum.COMPLETE.getValue());
            // 修改到货计划细表状态 改为已完成
            demandPlanDService.batchUpdateGenTypeByIds(demandPlanDIds, OrderGenTypeEnum.PURCHASE_GEN.getValue(),
                CompleteTypeEnum.COMPLETE.getValue());
            purchaseOrderM.setCOrderNo(generateCGCode());
            purchaseOrderM.setDOrderDate(DateUtil.date());
            purchaseOrderM.save();
            return true;
        });
    }

    private PurchaseOrderM createPurchaseOrderM(JSONObject formJsonObject) {
        PurchaseOrderM purchaseOrderM = new PurchaseOrderM();
        Long iAutoId = formJsonObject.getLong(PurchaseOrderM.IAUTOID);
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        DateTime date = DateUtil.date();
        if (iAutoId == null) {
            iAutoId = JBoltSnowflakeKit.me.nextId();
            purchaseOrderM.setAdd(true);
            purchaseOrderM.setIOrgId(getOrgId());
            purchaseOrderM.setCOrgCode(getOrgCode());
            purchaseOrderM.setCOrgName(getOrgName());
            purchaseOrderM.setICreateBy(userId);
            purchaseOrderM.setCCreateName(userName);
            purchaseOrderM.setDCreateTime(date);
            purchaseOrderM.setDBeginDate(formJsonObject.getDate(PurchaseOrderM.DBEGINDATE));
            purchaseOrderM.setDEndDate(formJsonObject.getDate(PurchaseOrderM.DENDDATE));
            // 默认 已失效隐藏
            purchaseOrderM.setHideInvalid(true);
        }
        purchaseOrderM.setIAutoId(iAutoId);
        setPurchaseOrderM(purchaseOrderM, formJsonObject, userId, userName, date);
        return purchaseOrderM;
    }

    private void setPurchaseOrderM(PurchaseOrderM purchaseOrderM, JSONObject formJsonObject, Long userId, String userName,
                                   Date date) {
        purchaseOrderM.setIDutyUserId(formJsonObject.getLong(PurchaseOrderM.IDUTYUSERID));
        purchaseOrderM.setCOrderNo(formJsonObject.getString(PurchaseOrderM.CORDERNO));
        purchaseOrderM.setIPurchaseTypeId(formJsonObject.getLong(PurchaseOrderM.IPURCHASETYPEID));
        purchaseOrderM.setIPayableType(formJsonObject.getInteger(PurchaseOrderM.IPAYABLETYPE));
        purchaseOrderM.setDOrderDate(formJsonObject.getDate(PurchaseOrderM.DORDERDATE));
        purchaseOrderM.setIVendorId(formJsonObject.getLong(PurchaseOrderM.IVENDORID));
        purchaseOrderM.setCMemo(formJsonObject.getString(PurchaseOrderM.CMEMO));
        // 币种
        purchaseOrderM.setCCurrency(formJsonObject.getString(PurchaseOrderM.CCURRENCY));
        purchaseOrderM.setIBusType(formJsonObject.getInteger(PurchaseOrderM.IBUSTYPE));

        purchaseOrderM.setIDepartmentId(formJsonObject.getLong(PurchaseOrderM.IDEPARTMENTID));
        purchaseOrderM.setITaxRate(formJsonObject.getBigDecimal(PurchaseOrderM.ITAXRATE));
        purchaseOrderM.setIExchangeRate(formJsonObject.getBigDecimal(PurchaseOrderM.IEXCHANGERATE));

        purchaseOrderM.setIType(formJsonObject.getInteger(PurchaseOrderM.ITYPE));

        // 修改时间
        purchaseOrderM.setIUpdateBy(userId);
        purchaseOrderM.setCUpdateName(userName);
        purchaseOrderM.setDUpdateTime(date);
        purchaseOrderM.setIsDeleted(false);
        purchaseOrderM.setIOrderStatus(OrderStatusEnum.NOT_AUDIT.getValue());
        purchaseOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
    }

    private void verifyData(String dataStr, String formStr, String invTableData) {
        ValidationUtils.notBlank(formStr, "未获取到表单数据");
        ValidationUtils.notBlank(dataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
        ValidationUtils.notBlank(invTableData, JBoltMsg.JBOLTTABLE_IS_BLANK);
    }

    public Integer findOderNoIsNotExists(Long id, String orderNo) {
        String sql = "select count(1) from PS_PurchaseOrderM where cOrderNo =? ";
        if (ObjUtil.isNotNull(id)) {
            sql = sql.concat("iAutoId <> " + id);
        }
        return queryInt(sql, orderNo);
    }

    public Integer findOderNoIsNotExists(String orderNo) {
        return findOderNoIsNotExists(null, orderNo);
    }

    /**
     * 操作状态：审批/撤回等。。。
     */
    public Ret operationalState(Long id, Integer type) {
        ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(type, JBoltMsg.PARAM_ERROR);
        PurchaseOrderM purchaseOrderM = findById(id);
        ValidationUtils.notNull(purchaseOrderM, JBoltMsg.DATA_NOT_EXIST);
        return SUCCESS;
    }

    /**
     * 撤回操作
     */
    public Ret withdraw(Long id) {
        PurchaseOrderM purchaseOrderM = getPurchaseOrderM(id);
        // 修改审批状态为：未审批
        purchaseOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
        purchaseOrderM.setDAuditTime(null);
        purchaseOrderM.setIOrderStatus(OrderStatusEnum.NOT_AUDIT.getValue());
        purchaseOrderM.update();
        return SUCCESS;
    }


    /**
     * 提审操作
     */
    public Ret arraignment(Long id) {
        PurchaseOrderM purchaseOrderM = getPurchaseOrderM(id);
        // 修改审批状态为：未审批
        purchaseOrderM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
        purchaseOrderM.setIOrderStatus(OrderStatusEnum.AWAIT_AUDIT.getValue());
        purchaseOrderM.update();
        return SUCCESS;
    }

    /**
     * 审核操作
     */
    public Ret audit(Long id) {
        DateTime date = DateUtil.date();
        PurchaseOrderM purchaseOrderM = getPurchaseOrderM(id);
        purchaseOrderM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
        purchaseOrderM.setDAuditTime(date);
        purchaseOrderM.setDSubmitTime(date);
        purchaseOrderM.setIOrderStatus(OrderStatusEnum.APPROVED.getValue());
        Map<String, String> stringStringMap = pushPurchase(id);
        purchaseOrderM.setCDocNo(stringStringMap.get("remark"));
        purchaseOrderM.setIPushTo(PushToTypeEnum.U8.getValue());
        purchaseOrderM.update();

        return SUCCESS;
    }

    /**
     * 关闭操作（关闭之前的状态必须生成）
     */
    public Ret close(Long id) {
        PurchaseOrderM purchaseOrderM = getPurchaseOrderM(id);
        Integer iOrderStatus = purchaseOrderM.getIOrderStatus();
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(iOrderStatus);
        ValidationUtils.notNull(orderStatusEnum, "未知状态");
        boolean flag = OrderStatusEnum.APPROVED.getValue() == orderStatusEnum.getValue()
            || OrderStatusEnum.GENERATE_CASH_TICKETS.getValue() == orderStatusEnum.getValue();
        ValidationUtils.isTrue(flag, "已审核或已生成现品表的单据才能关闭");
        purchaseOrderM.setIOrderStatus(OrderStatusEnum.CLOSE.getValue());
        purchaseOrderM.update();
        return SUCCESS;
    }

    /**
     * 生成现成票（1个订单1个文件，然后按料品、日期，生成现品票明细查询表）
     */
    public Ret generateCash(Long id) {
        tx(() -> {
            cashNotTransaction(id);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 操作：无事务
     */
    public void cashNotTransaction(Long id) {
        PurchaseOrderM purchaseOrderM = getPurchaseOrderM(id);
        Integer iOrderStatus = purchaseOrderM.getIOrderStatus();
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(iOrderStatus);
        ValidationUtils.notNull(orderStatusEnum, "未知状态");
        boolean flag = OrderStatusEnum.APPROVED.getValue() == orderStatusEnum.getValue();
        ValidationUtils.isTrue(flag, "已审核的单据才能生成现成票");
        purchaseOrderM.setIOrderStatus(OrderStatusEnum.GENERATE_CASH_TICKETS.getValue());
        // 获取细表数据
        List<Record> byPurchaseOrderMId = purchaseOrderDService.findByPurchaseOrderMId(id);
        List<PurchaseOrderDBatch> purchaseOrderDBatchList = new ArrayList<>();
        // 获取数量表
        List<Record> purchaseOrderDQtyList = purchaseorderdQtyService.findByPurchaseOrderMId(id);
        ValidationUtils.notEmpty(purchaseOrderDQtyList, purchaseOrderM.getCOrderNo() + "无现品票生成");
        for (Record record : purchaseOrderDQtyList) {
            // 源数量
            BigDecimal sourceQty = record.getBigDecimal(PurchaseorderdQty.IQTY);
            Long purchaseOrderDId = record.getLong(PurchaseorderdQty.IPURCHASEORDERDID);
            Long iPurchaseOrderdQtyId = record.getLong(PurchaseorderdQty.IAUTOID);
            Long inventoryId = record.getLong(PurchaseOrderD.IINVENTORYID);

            String dateStr = demandPlanDService
                .getDate(record.getStr(PurchaseorderdQty.IYEAR), record.getInt(PurchaseorderdQty.IMONTH),
                    record.getInt(PurchaseorderdQty.IDATE));
            DateTime planDate = DateUtil.parse(dateStr, DatePattern.PURE_DATE_PATTERN);
            // 包装数量
            BigDecimal pkgQty = record.getBigDecimal(Inventory.IPKGQTY);
            // 包装数量为空或者为0，生成一张条码，原始数量/打包数量
            if (ObjUtil.isNull(pkgQty) || BigDecimal.ZERO.compareTo(pkgQty) == 0 || sourceQty.compareTo(pkgQty) <= 0) {
                String barCode = purchaseOrderDBatchService.generateBarCode();
                PurchaseOrderDBatch purchaseOrderDBatch = purchaseOrderDBatchService
                    .createPurchaseOrderDBatch(purchaseOrderDId, iPurchaseOrderdQtyId, inventoryId, planDate, sourceQty, barCode);
                purchaseOrderDBatchList.add(purchaseOrderDBatch);
                continue;
            }
            // 源数量/包装数量 （向上取）
            int count = sourceQty.divide(pkgQty, 0, BigDecimal.ROUND_UP).intValue();
            for (int i = 0; i < count; i++) {
                // count-1： 69/10; 9
                String barCode = purchaseOrderDBatchService.generateBarCode();
                if (i == count - 1) {
                    BigDecimal qty = sourceQty.subtract(BigDecimal.valueOf(i).multiply(pkgQty));
                    PurchaseOrderDBatch purchaseOrderDBatch = purchaseOrderDBatchService
                        .createPurchaseOrderDBatch(purchaseOrderDId, iPurchaseOrderdQtyId, inventoryId, planDate, qty, barCode);
                    purchaseOrderDBatchList.add(purchaseOrderDBatch);
                    break;
                }
                PurchaseOrderDBatch purchaseOrderDBatch = purchaseOrderDBatchService
                    .createPurchaseOrderDBatch(purchaseOrderDId, iPurchaseOrderdQtyId, inventoryId, planDate, pkgQty, barCode);
                purchaseOrderDBatchList.add(purchaseOrderDBatch);
            }

        }
        purchaseOrderDBatchService.batchSave(purchaseOrderDBatchList);
        purchaseOrderM.update();
    }

    /**
     * 获取主表数据
     */
    private PurchaseOrderM getPurchaseOrderM(Long id) {
        ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
        PurchaseOrderM purchaseOrderM = findById(id);
        ValidationUtils.notNull(purchaseOrderM, JBoltMsg.DATA_NOT_EXIST);
        purchaseOrderM.setDUpdateTime(DateUtil.date());
        purchaseOrderM.setIUpdateBy(JBoltUserKit.getUserId());
        purchaseOrderM.setCUpdateName(JBoltUserKit.getUserName());
        return purchaseOrderM;
    }

    /**
     * 删除操作
     * 1.修改主表删除状态
     * 2.修改细表删除状态
     * 3.删除中间表数据
     * 4.修改到货计划细表订单状态及未完成状态
     * 5.修改到货计划主表未完成状态
     */
    public Ret del(Long id) {

        tx(() -> {
            removeNoTransaction(id);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 批量删除
     */
    public Ret batchDel(String ids) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        tx(() -> {
            for (String id : ids.split(",")) {
                removeNoTransaction(Long.valueOf(id));
            }
            return true;
        });
        return SUCCESS;
    }

    private void removeNoTransaction(Long id) {
        PurchaseOrderM purchaseOrderM = getPurchaseOrderM(id);
        ValidationUtils.notNull(purchaseOrderM, JBoltMsg.DATA_NOT_EXIST);
        Integer iOrderStatus = purchaseOrderM.getIOrderStatus();
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(iOrderStatus);
        ValidationUtils.notNull(orderStatusEnum, "未知状态");
        boolean flag =
            OrderStatusEnum.REJECTED.getValue() == iOrderStatus || OrderStatusEnum.NOT_AUDIT.getValue() == iOrderStatus;
        ValidationUtils.isTrue(flag, "只有审核不通过或没审核的数据才能删除");
        purchaseOrderM.setIsDeleted(true);
        List<Record> purchaseOrderRefList = purchaseOrderRefService.findByPurchaseOderMId(id);
        // 修改细表数据
        purchaseOrderDService.removeByPurchaseOrderMId(id);
        if (CollUtil.isNotEmpty(purchaseOrderRefList)) {
            List<Long> demandPlanDIds = purchaseOrderRefList.stream()
                .map(record -> record.getLong(PurchaseOrderRef.IDEMANDPLANDID)).collect(Collectors.toList());
            // 修改到货计划细表状态
            demandPlanDService.batchUpdateGenTypeByIds(demandPlanDIds, OrderGenTypeEnum.NOT_GEN.getValue(),
                CompleteTypeEnum.INCOMPLETE.getValue());
            // 修改主表状态
            List<Long> demandPlanMIds = demandPlanMService.pegging(demandPlanDIds);
            demandPlanMService.batchUpdateStatusByIds(demandPlanMIds, CompleteTypeEnum.INCOMPLETE.getValue());
            // 删除中间表数据
            purchaseOrderRefService.removeByPurchaseOderMId(id);
        }
        purchaseOrderM.update();
    }

    /**
     * 批量生成现成票
     */
    public Ret batchGenerateCash(String ids) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        for (String id : ids.split(",")) {
            cashNotTransaction(Long.valueOf(id));
        }
        return SUCCESS;
    }


    public Ret updateHideInvalid(Long id, Boolean isHide) {
        PurchaseOrderM purchaseOrderM = getPurchaseOrderM(id);
        purchaseOrderM.setHideInvalid(isHide);
        purchaseOrderM.update();
        return SUCCESS;
    }

    public Ret saveSubmit(JBoltTable jBoltTable) {
        List<Record> saveRecordList = jBoltTable.getSaveRecordList();
        List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
        PurchaseOrderM purchaseOrderM = createPurchaseOrderM(jBoltTable.getForm());
        JSONObject params = jBoltTable.getParams();
        Object[] deleteIds = jBoltTable.getDelete();
        tx(() -> {
            // 新增
            if (CollUtil.isNotEmpty(saveRecordList)) {
                saveOrUpdatePurachseD(purchaseOrderM.getIAutoId(), saveRecordList, 1);
            }
            // 修改
            if (CollUtil.isNotEmpty(updateRecordList)) {
                saveOrUpdatePurachseD(purchaseOrderM.getIAutoId(), updateRecordList, 2);
            }
            // 删除
            if (deleteIds != null && deleteIds.length > 0) {
                purchaseOrderDService.deleteByIds(deleteIds);
            }
            if (purchaseOrderM.isAdd()) {
                purchaseOrderM.setCOrderNo(generateCGCode());
                purchaseOrderM.setDOrderDate(DateUtil.date());
                purchaseOrderM.save();
                return true;
            }
            purchaseOrderM.update();
            return true;
        });
        return SUCCESS;
    }

    public List<Record> findPurchaseOrderD(Long purchaseOrderMId) {
        PurchaseOrderM purchaseOrderM = findById(purchaseOrderMId);
        Map<String, List<Record>> listMap = setDate(DateUtil.formatDate(purchaseOrderM.getDBeginDate()),
            DateUtil.formatDate(purchaseOrderM.getDEndDate()));
        return getPurchaseOrderDList(purchaseOrderM, listMap);
    }

    public List<Record> getPurchaseOrderDList(PurchaseOrderM purchaseOrderM, Map<String, List<Record>> calendarMap) {
        // 获取所有子件
        List<Record> purchaseOrderDList = purchaseOrderDService.findByPurchaseOrderMId(purchaseOrderM.getIAutoId());
        List<Record> dateList = calendarMap.get(PurchaseOrderM.DATEFIELD);
        Map<String, Record> dateMap = dateList.stream()
            .collect(Collectors.toMap(record -> record.getStr(PurchaseOrderM.DATESTR), record -> record));

        // 采购订单到货数量明细
        List<Record> purchaseOrderdQtyList = purchaseorderdQtyService.findByPurchaseOrderMId(purchaseOrderM.getIAutoId());
        // 按存货编码汇总
        Map<Long, Map<String, BigDecimal>> purchaseOrderdQtyMap = demandPlanDService
            .getDemandPlanDMap(purchaseOrderdQtyList, PurchaseOrderD.IINVENTORYID);

        // key= 2023年04月_存货id
        String formatDate = DateUtil.formatDate(purchaseOrderM.getDEndDate());
        Map<String, BigDecimal> ymQtyMap = purchaseOrderdQtyList.stream().collect(Collectors.groupingBy(record -> {
            String monthStr = String.format("%02d", record.getInt(PurchaseorderdQty.IMONTH));
            String invId = record.getStr(PurchaseOrderD.IINVENTORYID);
            String yearStr = record.getStr(PurchaseorderdQty.IYEAR);
            return yearStr.concat("年").concat(monthStr).concat("月").concat("_").concat(invId);
        }, Collectors.mapping(record -> record.getBigDecimal(PurchaseorderdQty.IQTY),
            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        // 设置到货计划明细数量
        purchaseOrderDService.setPurchaseOrderDList(purchaseOrderDList, dateMap, purchaseOrderdQtyMap, ymQtyMap);
        return purchaseOrderDList;
    }

    /**
     * @param recordList 集合
     * @param type       1.新增 2.修改
     */
    public void saveOrUpdatePurachseD(Long purchaseOrderMId, List<Record> recordList, int type) {
        List<PurchaseOrderD> purchaseOrderDList = new ArrayList<>();
        List<PurchaseorderdQty> purchaseOrderQtyList = new ArrayList<>();
        List<Long> vendorAdIds = recordList.stream()
                .map(record -> {
                    Long vendorAdId ;
                    if( "".equals(record.getStr(PurchaseOrderD.IVENDORADDRID))  || record.get(PurchaseOrderD.IVENDORADDRID) == null){
                        return 0L;// 如果为null，返回0L
                    }
                    vendorAdId=record.getLong(PurchaseOrderD.IVENDORADDRID);
                    return vendorAdId;
                })
                .collect(Collectors.toList());
        List<Warehouse> warehouseList = warehouseService.findByIds(vendorAdIds);
        Map<Long, Warehouse> warehouseMap = warehouseList.stream()
            .collect(Collectors.toMap(Warehouse::getIAutoId, warehouse -> warehouse));
        int seq = 0;
        for (Record record : recordList) {
            String isPresentStr = record.getStr(PurchaseOrderD.ISPRESENT);
            Warehouse warehouse = warehouseMap.get(record.get(PurchaseOrderD.IVENDORADDRID));
            String wareHouseName = null;
            if(ObjUtil.isNotNull(warehouse)){
                wareHouseName = warehouse.getCWhName();
            }
            int isPresent = 0;
            if (BoolCharEnum.YES.getText().equals(isPresentStr)) {
                isPresent = 1;
            }
            PurchaseOrderD purchaseOrderD = null;
            switch (type) {
                case 1:
                    purchaseOrderD = purchaseOrderDService.create(purchaseOrderMId,
                        record.getLong(PurchaseOrderD.IVENDORADDRID),
                        record.getLong(PurchaseOrderD.IINVENTORYID),
                        wareHouseName,
                        record.getStr(PurchaseOrderD.CMEMO),
                        record.getStr(PurchaseOrderD.IPKGQTY),
                        IsOkEnum.toEnum(isPresent).getText());
                    break;
                default:
                    purchaseOrderD = purchaseOrderDService.create(record.getLong(PurchaseOrderD.IAUTOID),
                        purchaseOrderMId,
                        record.getLong(PurchaseOrderD.IVENDORADDRID),
                        record.getLong(PurchaseOrderD.IINVENTORYID),
                        wareHouseName,
                        record.getStr(PurchaseOrderD.CMEMO),
                        record.getStr(PurchaseOrderD.IPKGQTY),
                        IsOkEnum.toEnum(isPresent).getText());
            }
            
            purchaseOrderDList.add(purchaseOrderD);
            // 删除qty里的数据重新添加
			/*switch (type){
				case 2:
					purchaseorderdQtyService.delByPurchaseOrderDId(record.getLong(PurchaseOrderD.IAUTOID));
					break;
			}*/
            boolean flag = false;

            String[] columnNames = record.getColumnNames();
            for (String columnName : columnNames) {
                if (columnName.contains("日")) {
                    seq += 1;
                    DateTime dateTime = DateUtil.parseDate(columnName);
                    String yearStr = DateUtil.format(dateTime, DatePattern.NORM_YEAR_PATTERN);
                    String monthStr = DateUtil.format(dateTime, "MM");
                    String dayStr = DateUtil.format(dateTime, "dd");
                    BigDecimal qty = record.getBigDecimal(columnName);
                    PurchaseorderdQty purchaseorderdQty = purchaseorderdQtyService.create(purchaseOrderD.getIAutoId(),
                        Integer.parseInt(yearStr),
                        Integer.parseInt(monthStr),
                        Integer.parseInt(dayStr),
                        qty,
                        seq);
                    switch (type) {
                        case 2:
                            purchaseorderdQtyService
                                .delete(purchaseOrderD.getIAutoId(), purchaseorderdQty.getIYear(), purchaseorderdQty.getIMonth(),
                                    purchaseorderdQty.getIDate());
                            break;
                    }
                    if (qty.compareTo(BigDecimal.ZERO) > 0) {
                        purchaseOrderQtyList.add(purchaseorderdQty);
                        flag = true;
                    }
                }
            }
            if (!flag) {
                Inventory inventory = inventoryService.findById(purchaseOrderD.getIInventoryId());
                ValidationUtils.isTrue(flag, "存货编码【" + inventory.getCInvCode() + "】日期数量不能全部为空");
            }
        }

        if (CollUtil.isNotEmpty(purchaseOrderDList)) {
            switch (type) {
                case 1:
                    purchaseOrderDService.batchSave(purchaseOrderDList, 500);
                    break;
                default:
                    purchaseOrderDService.batchUpdate(purchaseOrderDList, 500);
                    break;
            }
        }

        if (CollUtil.isNotEmpty(purchaseOrderQtyList)) {
            purchaseorderdQtyService.batchSave(purchaseOrderQtyList, 500);
        }
    }

	/**
	 * U8推单
	 * @param iautoid
	 * @return
	 */
	public Map<String, String> pushPurchase(Long iautoid){
		List<Record> orderList = dbTemplate("purchaseorderm.findBycOrder", Kv.by("iautoid",iautoid)).find();
		Map<String,String> map =new HashMap<>();
			JSONArray jsonArray = new JSONArray();
			for (Record order : orderList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("DocNo", order.get("DocNo"));
				jsonObject.put("cvencode", order.get("cvencode"));
				jsonObject.put("cmaker", order.get("cmaker"));
				jsonObject.put("dDate", order.get("dDate"));
				jsonObject.put("cPersonCode", order.get("cPersonCode"));
				jsonObject.put("cBusType", order.get("cBusType"));
				jsonObject.put("cPTCode", order.get("cPTCode"));
				jsonObject.put("iExchRate", order.getBigDecimal("iExchRate"));
				jsonObject.put("iTaxRate", order.getBigDecimal("iTaxRate"));
				jsonObject.put("cexch_name", order.get("cexch_name"));
				jsonObject.put("cmemo", order.get("cmemo"));
				jsonObject.put("inum", order.get("inum"));
				jsonObject.put("iQuantity", order.get("iQuantity"));
				jsonObject.put("cInvCode", order.get("cInvCode"));
				jsonObject.put("cInvName", order.get("cInvName"));
				jsonObject.put("dPlanDate", order.get("dPlanDate"));
				jsonObject.put("iQuotedPrice", order.get("iQuotedPrice"));
				jsonObject.put("irowno", order.get("irowno"));
				jsonObject.put("KL", order.get("KL"));
				jsonObject.put("iNatDisCount", order.get("iNatDisCount"));
                jsonObject.put("cPayType", order.get("cPayType"));
                //String cdepcode = departmentService.getRefDepId(order.getStr("cdepcode"));
                jsonObject.put("cdepcode",order.getStr("cdepcode"));
				jsonArray.add(jsonObject);
			}
			JSONObject params = new JSONObject();
			params.put("data",jsonArray);
			LOG.info(params.toJSONString());
			tx(() -> {
				String result = HttpUtil.post("http://120.24.44.82:8099/api/cwapi/PODocAdd?dbname=U8Context", params.toString());
				JSONObject jsonObject = JSONObject.parseObject(result);
				String remark=jsonObject.getString("remark");
				if(!jsonObject.getString("status").equals("S")){
					ValidationUtils.error(remark);
				}
				map.put("remark",remark);
				map.put("json",params.toString());
				return true;
			});
			return map;
	}
	public List<Record> findByMidxlxs(Kv kv){
		return dbTemplate("purchaseorderm.findBycOrderNo",kv).find();
	}

	public List<Record> findByMidxlxs2(Long iautoid){
		return dbTemplate("purchaseorderm.findBycOrderNo2",Kv.by("iautoid",iautoid)).find();
	}
	public List<Record> findByBarcode(Long iautoid){
		return  dbTemplate("purchaseorderm.findByBarcodeOnOrder",Kv.by("iautoid",iautoid)).find();

	}
	public List<Record> findByBarcode2(Long iautoid){
		return  dbTemplate("purchaseorderm.findByBarcodeOnOrder2",Kv.by("iautoid",iautoid)).find();

	}
	/**
	 * 导出PDF条码
	 * @return
	 */
	public Kv pageOnePdf(Long iautoid,Integer page,Integer type,Kv kv) throws IOException {
        if (Boolean.parseBoolean(kv.getStr("isEnabled_type"))){
            kv.remove("isEnabled_type");
        }else{
            kv.set("isEnabled_type", "1");
        }
		List<Record> rowDatas = new ArrayList<>();
		List<Record> barcodeDatas = new ArrayList<>();
		if(type==0){
			// 采购现品票清单数据
			rowDatas = findByMidxlxs(kv);
			// 采购现品票条码数据
			barcodeDatas=findByBarcode(iautoid);
		}else {
			// 传票更改清单数据
			rowDatas = findByMidxlxs2(iautoid);
			// 传票清单条码数据
			barcodeDatas=findByBarcode2(iautoid);
		}

		// 采购现品票明细数据sheet分页数组
		List<String> sheetNames = new ArrayList<>();

		List<Kv> rows = new ArrayList<>();

		List<Record> leftDatas = new ArrayList<>();
		List<Record> rightDatas = new ArrayList<>();

		int counter = 0;
		int i = 0;

		for (Record row : rowDatas) {

			if (counter < 15) {
				leftDatas.add(row);
			} else {
				rightDatas.add(row);
			}
			counter++;
			if (counter == 30) {
				String sheetName = "订货清单" + (i + 1);
				sheetNames.add(sheetName);
				rows.add(Kv.by("sheetName", sheetName).set("leftDatas", leftDatas).set("rightDatas", rightDatas));
				leftDatas = new ArrayList<>();
				rightDatas = new ArrayList<>();
				counter = 0;
				i++;
			}
		}

		// 如果 rows 的数量不是 30 的整数倍，将剩余的数据添加到 datas 中
		Kv remainData = Kv.create();

		if (CollUtil.isNotEmpty(leftDatas)) {
			remainData.set("leftDatas", leftDatas);
		}
		if (CollUtil.isNotEmpty(rightDatas)) {
			remainData.set("rightDatas", rightDatas);
		}

		if (MapUtil.isNotEmpty(remainData)) {
			rows.add(remainData);
		}

		if (rowDatas.size() < 30) {
			sheetNames.add("采购清单");
		}
		List<Kv> kvs = new ArrayList<>();

		// 采购现品票明细条码数据sheet分页数组
		List<String> sheetNames2 = new ArrayList<>();

		List<Record> list1 = new ArrayList<>();
		List<Record> list2 = new ArrayList<>();
		List<Record> list3 = new ArrayList<>();
		List<Record> list4 = new ArrayList<>();
		List<Record> list5 = new ArrayList<>();
		List<Record> list6 = new ArrayList<>();
		List<Record> list7 = new ArrayList<>();
		List<Record> list8 = new ArrayList<>();
		int cont = 0;
		int j = 0;
		//一页一个条码
		if (page == 1) {
			for (Record row : barcodeDatas) {
				String sheetName = "订货条码" + (j + 1);
				sheetNames2.add(sheetName);

				BufferedImage bufferedImage = QrCodeUtil.generate(row.get("cBarcode"), BarcodeFormat.CODE_39,100,20);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "jpeg", os);
				row.set("img", os.toByteArray());

				BufferedImage bufferedImage2 = QrCodeUtil.generate(row.get("cBarcode"), BarcodeFormat.QR_CODE,100, 100);
				ByteArrayOutputStream os2 = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage2, "jpeg", os2);
				row.set("img2", os2.toByteArray());

				kvs.add(Kv.by("sheetName", sheetName).set("list1", Collections.singletonList(row)));

				j++;
			}
		}
		//一页八个条码
		else {
			for (Record row : barcodeDatas) {
				//条形码
				BufferedImage bufferedImage = QrCodeUtil.generate(row.get("cBarcode"), BarcodeFormat.CODE_39, 100, 20);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "jpeg", os);
				row.set("img", os.toByteArray());
				//二维码
				BufferedImage bufferedImage2 = QrCodeUtil.generate(row.get("cBarcode"), BarcodeFormat.QR_CODE, 100, 100);
				ByteArrayOutputStream os2 = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage2, "jpeg", os2);
				row.set("img2", os2.toByteArray());
				if (cont < 1) {
					list1.add(row);
				} else if (cont < 2) {
					list2.add(row);
				} else if (cont < 3) {
					list3.add(row);
				} else if (cont < 4) {
					list4.add(row);
				} else if (cont < 5) {
					list5.add(row);
				} else if (cont < 6) {
					list6.add(row);
				} else if (cont < 7) {
					list7.add(row);
				} else if (cont < 8) {
					list8.add(row);
				}
				cont++;
				if (cont == 8) {
					String sheetName = "订货清单" + (j + 1);
					sheetNames2.add(sheetName);
					kvs.add(Kv.by("sheetName", sheetName)
							.set("list1", list1)
							.set("list2", list2)
							.set("list3", list3)
							.set("list4", list4)
							.set("list5", list5)
							.set("list6", list6)
							.set("list7", list7)
							.set("list8", list8));
					list1 = new ArrayList<>();
					list2 = new ArrayList<>();
					list3 = new ArrayList<>();
					list4 = new ArrayList<>();
					list5 = new ArrayList<>();
					list6 = new ArrayList<>();
					list7 = new ArrayList<>();
					list8 = new ArrayList<>();
					cont = 0;
					j++;
				}

			}

			if (rowDatas.size() < 8) {
				sheetNames2.add("订货条码");
			}
			if (rowDatas.size() > 8) {
				sheetNames2.add("订货条码0");
			}
			Kv remainData2 = Kv.create();

			//
			if (CollUtil.isNotEmpty(list1)) {
				remainData2.set("list1", list1);
			}
			if (CollUtil.isNotEmpty(list2)) {
				remainData2.set("list2", list2);
			}
			if (CollUtil.isNotEmpty(list3)) {
				remainData2.set("list3", list3);
			}
			if (CollUtil.isNotEmpty(list4)) {
				remainData2.set("list4", list4);
			}
			if (CollUtil.isNotEmpty(list5)) {
				remainData2.set("list5", list5);
			}
			if (CollUtil.isNotEmpty(list6)) {
				remainData2.set("list6", list6);
			}
			if (CollUtil.isNotEmpty(list7)) {
				remainData2.set("list7", list7);
			}
			if (CollUtil.isNotEmpty(list8)) {
				remainData2.set("list8", list8);
			}

			if (MapUtil.isNotEmpty(remainData2)) {
				kvs.add(remainData2);
			}
		}

		Kv data = Kv.by("rows", rows)
				.set("sheetNames", sheetNames)
				.set("rows2", kvs)
				.set("sheetNames2", sheetNames2);
		return data;
	}

    public PurchaseOrderM findByCOrerNo(String corderno) {
        return findFirst("select * from PS_PurchaseOrderM where corderno=?", corderno);
    }

    /**
     * 处理审批通过的其他业务操作，如有异常返回错误信息
     */
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    /**
     * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
     */
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    /**
     * 实现反审之前的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     */
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
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /**
     * 提审前业务，如有异常返回错误信息
     */
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    /**
     * 提审后业务处理，如有异常返回错误信息
     */
    public String postSubmitFunc(long formAutoId) {
        return null;
    }

    /**
     * 撤回审核业务处理，如有异常返回错误信息
     */
    public String postWithdrawFunc(long formAutoId) {
        return null;
    }

    /**
     * 从审批中，撤回到已保存，业务实现，如有异常返回错误信息
     */
    public String withdrawFromAuditting(long formAutoId) {
        return null;
    }

    /**
     * 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
     */
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /**
     * 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
     */
    public String postWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /**
     * 批量审批（审核）通过
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    public String postBatchApprove(List<Long> formAutoIds) {
        return null;
    }

    /**
     * 批量审批（审核）不通过
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    public String postBatchReject(List<Long> formAutoIds) {
        return null;
    }

    /**
     * 批量撤销审批
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    public String postBatchBackout(List<Long> formAutoIds) {
        return null;
    }

    public List<Record> getWhcodeAll(String q, Integer limit) {
        return dbTemplate("purchaseorderm.getWhcodeAll", Okv.by("q", q).set("limit", limit)).find();
    }
}
