package cn.rjtech.admin.subcontractorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.demandpland.DemandPlanDService;
import cn.rjtech.admin.demandplanm.DemandPlanMService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.subcontractorderd.SubcontractOrderDService;
import cn.rjtech.admin.subcontractorderdbatch.SubcontractOrderDBatchService;
import cn.rjtech.admin.subcontractorderdbatchversion.SubcontractOrderDBatchVersionService;
import cn.rjtech.admin.subcontractorderdqty.SubcontractorderdQtyService;
import cn.rjtech.admin.subcontractorderref.SubcontractOrderRefService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购/委外订单-采购订单主表
 * @ClassName: SubcontractOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
public class SubcontractOrderMService extends BaseService<SubcontractOrderM> {
	private final SubcontractOrderM dao=new SubcontractOrderM().dao();
	
	@Inject
	private MomDataFuncService momDataFuncService;
	@Inject
	private DemandPlanMService demandPlanMService;
	@Inject
	private DemandPlanDService demandPlanDService;
	@Inject
	private SubcontractorderdQtyService subcontractorderdQtyService;
	@Inject
	private SubcontractOrderDService subcontractOrderDService;
	@Inject
	private SubcontractOrderRefService subcontractOrderRefService;
	@Inject
	private VendorAddrService vendorAddrService;
	@Inject
	private DictionaryService dictionaryService;
	@Inject
	private SubcontractOrderDBatchService subcontractOrderDBatchService;
	@Inject
	private SubcontractOrderDBatchVersionService subcontractOrderDBatchVersionService;
	@Inject
	private InventoryService inventoryService;
	@Inject
	private WarehouseService warehouseService;
	
	@Override
	protected SubcontractOrderM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> page = dbTemplate("subcontractorderm.findAll", kv).paginate(pageNumber, pageSize);
		changeData(page.getList());
		return page;
	}
	
	
	private void changeData(List<Record> list){
		if (CollUtil.isEmpty(list)){
			return;
		}
		// 应付类型
		List<cn.jbolt.core.model.Dictionary> purchaseCopingTypeList = dictionaryService.getListByTypeKey("purchase_coping_type", true);
		
		Map<String, cn.jbolt.core.model.Dictionary> purchaseCopingTypeMap = purchaseCopingTypeList.stream().collect(Collectors.toMap(p -> p.getSn(), p -> p));
		// 业务类型
		List<cn.jbolt.core.model.Dictionary> purchaseBusinessTypeList = dictionaryService.getListByTypeKey("purchase_business_type", true);
		
		Map<String, Dictionary> purchaseBusinessTypeMap = purchaseBusinessTypeList.stream().collect(Collectors.toMap(p -> p.getSn(), p -> p));
		
		for (Record record : list){
			Integer orderStatus = record.getInt(SubcontractOrderM.IORDERSTATUS);
			OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(orderStatus);
			ValidationUtils.notNull(orderStatusEnum, "未知订单状态");
			record.set(SubcontractOrderM.ADUITSTATUSTEXT, orderStatusEnum.getText());
			String iPayableType = record.getStr(SubcontractOrderM.IPAYABLETYPE);
			
			String iBusType = record.getStr(SubcontractOrderM.IBUSTYPE);
			if (purchaseCopingTypeMap.containsKey(iPayableType)){
				record.set(SubcontractOrderM.PAYABLETYPETEXT, purchaseCopingTypeMap.get(iPayableType).getName());
			}
			if (purchaseBusinessTypeMap.containsKey(iBusType)){
				record.set(SubcontractOrderM.BUSTYPETEXT, purchaseBusinessTypeMap.get(iBusType).getName());
			}
			Integer type = record.getInt(SubcontractOrderM.ITYPE);
			SourceTypeEnum sourceTypeEnum = SourceTypeEnum.toEnum(type);
			if (ObjUtil.isNotNull(sourceTypeEnum)){
				record.set(PurchaseOrderM.TYPESTR, sourceTypeEnum.getText());
			}
		}
	}
	
	
	/**
	 * 保存
	 * @param subcontractOrderM
	 * @return
	 */
	public Ret save(SubcontractOrderM subcontractOrderM) {
		if(subcontractOrderM==null || isOk(subcontractOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractOrderM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractOrderM
	 * @return
	 */
	public Ret update(SubcontractOrderM subcontractOrderM) {
		if(subcontractOrderM==null || notOk(subcontractOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractOrderM dbSubcontractOrderM=findById(subcontractOrderM.getIAutoId());
		if(dbSubcontractOrderM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractOrderM.getName(), subcontractOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractOrderM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractOrderM subcontractOrderM, Kv kv) {
		//addDeleteSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractOrderM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractOrderM subcontractOrderM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SubcontractOrderM subcontractOrderM, String column, Kv kv) {
		//addUpdateSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderM.getName(),"的字段["+column+"]值:"+subcontractOrderM.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		    case "hideInvalid":
		        break;
		}
		*/
		return null;
	}
	
	
	
	public List<Record> findByInventoryId(Long inventoryId){
		ValidationUtils.notNull(inventoryId,JBoltMsg.DATA_NOT_EXIST);
		return dbTemplate("inventory.findByInventoryId", Okv.by("inventoryId",inventoryId)).find();
		
	}
	
	/**
	 * 委外订单年月日4流水号
	 * @return
	 */
	public String generateCGCode() {
		String prefix = "WW";
		String format = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_FORMAT);
		return momDataFuncService.getNextNo(prefix.concat(format), 4);
	}
	
	/**
	 * 判断日期 是否为当月/当周/当日
	 * @param date
	 * @param pattern
	 * @return
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
	 *
	 * @param startDate
	 * @param endDate
	 * @return  日期key：dayValue
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
			dayNum+=1;
			startCalendar.add(Calendar.DATE, 1);
		}
		return calendarMap;
	}
	
	private Calendar createCalendar(DateTime date, boolean isFrist){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, date.year());
		calendar.set(Calendar.MONTH, date.month());
		calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth());
		// 设值时分秒为00:00
		if (isFrist){
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
	
	public Map<String, Object> getDateMap(String beginDateStr, String endDateStr, String iVendorId, Integer processType, Integer iSourceType){
		
		Map<String, Object> repMap = new HashMap<>();
		// 获取日期
		Map<String, List<Record>> listMap = setDate(beginDateStr, endDateStr);
		List<Record> dateHeadList = listMap.get(SubcontractOrderM.DATEFIELD);
		List<Record> monthHeadList = listMap.get(SubcontractOrderM.MONTHFIELD);
		
		// 物料到货计划 才回去返查 到货计划明细
		if (SourceTypeEnum.MATERIAL_PLAN_TYPE.getValue() == iSourceType){
			Map<String, Record> recordMap = dateHeadList.stream().collect(Collectors.toMap(record -> record.getStr("dateStr"), record -> record));
			
			// 主表获取存货数据 table--value
			List<Record> vendorDateList = demandPlanMService.getVendorDateList(beginDateStr, endDateStr, iVendorId, processType);
			// 细表获取存货数量
			List<Record> demandPlanDList = demandPlanDService.findByDemandPlanMList(beginDateStr, endDateStr, iVendorId, processType);
			// 记录每一个存货中存在多个物料到货计划
			Map<Long, List<PurchaseOrderRef>> puOrderRefMap = demandPlanDService.getPuOrderRefByInvId(demandPlanDList);
			// 按存货编码汇总
			Map<Long, Map<String, BigDecimal>> demandPlanDMap = demandPlanDService.getDemandPlanDMap(demandPlanDList, DemandPlanM.IINVENTORYID);
			
			DateTime endDate = DateUtil.parseDate(endDateStr);
			Map<String, BigDecimal> ymQtyMap = demandPlanDList.stream().filter(
					record -> {
						String yearStr = record.getStr(PurchaseorderdQty.IYEAR);
						String monthStr = String.format("%02d", record.getInt(PurchaseorderdQty.IMONTH));
						String dateStr = String.format("%02d", record.getInt(PurchaseorderdQty.IDATE));
						String dateFormStr = yearStr.concat("-").concat(monthStr).concat("-").concat(dateStr);
						return DateUtil.compare(endDate, DateUtil.parseDate(dateFormStr)) >=0;
					}
			).collect(Collectors.groupingBy(record -> {
				String monthStr = String.format("%02d", record.getInt(PurchaseorderdQty.IMONTH));
				String invId = record.getStr(PurchaseOrderD.IINVENTORYID);
				String yearStr = record.getStr(PurchaseorderdQty.IYEAR);
				return yearStr.concat("年").concat(monthStr).concat("月").concat("_").concat(invId);
			}, Collectors.mapping(record -> record.getBigDecimal(PurchaseorderdQty.IQTY), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
			
			// 设置到货计划明细数量
			demandPlanMService.setVendorDateList(OrderGenTypeEnum.SUBCONTRACT_GEN.getValue(), vendorDateList, demandPlanDMap, recordMap, puOrderRefMap, ymQtyMap);
			repMap.put("tableData", vendorDateList);
			repMap.put("tableDataStr", JSONObject.toJSONString(vendorDateList));
		}
		repMap.put("monthHeadList", monthHeadList);
		repMap.put("dateHeadList", dateHeadList);
		return repMap;
	}
	
	/**
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private Map<String, List<Record>> setDate(String beginDate, String endDate){
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
		for (String dateStr : calendarMap.keySet()){
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
		for (String dateKey :dateMap.keySet()){
			Record record = new Record();
			List<Integer> list = dateMap.get(dateKey);
			record.set(SubcontractOrderM.SIZE, list.size()+1);
			record.set(SubcontractOrderM.DATESTR, dateKey);
			Record sumRecord = new Record();
			String sumStr = "";
			for (Integer date : list){
				String dateStr = String.format("%02d", date).concat("日");
				Record dateRecord = new Record();
				// 记录是否需要在页面显示可编辑
				dateRecord.set(SubcontractOrderM.ISEDITFIELDNAME, true);
				dateRecord.set(SubcontractOrderM.FIELDNAME, dateStr);
				dateRecord.set(SubcontractOrderM.DATESTR, dateKey.concat(dateStr));
				dateRecord.set(SubcontractOrderM.INDEX, index);
				dateHeadList.add(dateRecord);
				sumStr+=dateKey.concat(dateStr).concat("+");
				index+=1;
			}
			
			sumRecord.set(SubcontractOrderM.FIELDNAME, "合计");
			// 需要统计的月份
			sumRecord.set(SubcontractOrderM.DATESTR, dateKey);
			sumRecord.set(SubcontractOrderM.INDEX, index);
			if (StrUtil.isNotBlank(sumStr)){
				sumRecord.set(SubcontractOrderM.SUMSTR, sumStr.substring(0, sumStr.length()-1));
			}
			index+=1;
			dateHeadList.add(sumRecord);
			dateMonthHeadList.add(record);
		}
		ValidationUtils.notEmpty(dateHeadList, "未获取到日期范围数据");
		Map<String, List<Record>> rep = new HashMap<>();
		rep.put(SubcontractOrderM.MONTHFIELD, dateMonthHeadList);
		rep.put(SubcontractOrderM.DATEFIELD, dateHeadList);
		return rep;
	}
	
	public Map<String, Object> getDateMap(SubcontractOrderM subcontractOrderM){
		
		Map<String, Object> repMap = new HashMap<>();
		Map<String, List<Record>> listMap = setDate(DateUtil.formatDate(subcontractOrderM.getDBeginDate()), DateUtil.formatDate(subcontractOrderM.getDEndDate()));
		if (SourceTypeEnum.BLANK_PURCHASE_TYPE.getValue() == subcontractOrderM.getIType()){
			
			List<Record> purchaseOrderDList = getPurchaseOrderDList(subcontractOrderM, listMap);
			repMap.put("tableData", purchaseOrderDList);
			repMap.put("tableDataStr", JSONObject.toJSONString(purchaseOrderDList));
		}
		
		repMap.put("monthHeadList", listMap.get(SubcontractOrderM.MONTHFIELD));
		repMap.put("dateHeadList", listMap.get(SubcontractOrderM.DATEFIELD));
		return repMap;
	}
	
	/**
	 *
	 * @param dataStr  所有加载进来的数据
	 * @param formStr  // form表单数据
	 * @param invTableData // 操作表体数据
	 * @param type	区分是保存还是修改 0：保存； 1：修改
	 * @param kv
	 * @return
	 */
	public Ret submit(String dataStr, String formStr, String invTableData, String type, Kv kv) {
		verifyData(dataStr, formStr, invTableData);
		JSONArray jsonArray = JSONObject.parseArray(dataStr);
		JSONObject formJsonObject = JSONObject.parseObject(formStr);
		JSONArray invJsonArray = JSONObject.parseArray(invTableData);
		
		Map<Long, JSONObject> invDataMap = jsonArray.stream().collect(Collectors.toMap(r -> ((JSONObject)r).getLong(PurchaseOrderD.IINVENTORYID.toLowerCase()),  r -> (JSONObject)r, (key1, key2) -> key2));
		Map<Long, JSONObject> invTableMap = invJsonArray.stream().collect(Collectors.toMap(r -> ((JSONObject)r).getLong(PurchaseOrderD.IINVENTORYID),  r -> (JSONObject)r, (key1, key2) -> key2));
		
		switch (type){
			case "0":
				saveSubmit(formJsonObject, invDataMap, invTableMap);
				break;
			case "1":
				updateSubmit(formJsonObject, invDataMap, invTableMap);
				break;
		}
		
		return SUCCESS;
	}
	
	private void updateSubmit(JSONObject formJsonObject, Map<Long, JSONObject> invDataMap, Map<Long, JSONObject> invTableMap){
		Long id = formJsonObject.getLong(SubcontractOrderM.IAUTOID);
		ValidationUtils.notNull(id, "未获取到主键id");
		SubcontractOrderM subcontractOrderM = findById(id);
		ValidationUtils.notNull(subcontractOrderM, JBoltMsg.DATA_NOT_EXIST);
		setSubcontractOrderM(subcontractOrderM, formJsonObject, JBoltUserKit.getUserId(), JBoltUserKit.getUserName(), DateUtil.date());
		List<Long> notIds = new ArrayList<>();
		for (Long invId : invDataMap.keySet()){
			JSONObject invDJsonObject = invDataMap.get(invId);
			Long purchaseOrderDId = invDJsonObject.getLong(PurchaseOrderD.IAUTOID.toLowerCase());
			// 存在则修改，不存在这删除,将主键id添加进来
			if (invTableMap.containsKey(invId)){
				JSONObject invTableJsonObject = invTableMap.get(invId);
				invTableJsonObject.put(PurchaseOrderD.IAUTOID, purchaseOrderDId);
				continue;
			}
			// 添加删除的id
			notIds.add(purchaseOrderDId);
		}
		
		tx(()->{
			// 删除 先修改细表状态，在删除中间表数据，在修改到货计划细表及主表状态
			if (CollUtil.isNotEmpty(notIds)){
				for (Long purchaseOrderDId : notIds){
					SubcontractOrderD subcontractOrderD = subcontractOrderDService.findById(purchaseOrderDId);
					ValidationUtils.notNull(subcontractOrderD, "采购订单细表数据未找到");
					subcontractOrderD.setIsDeleted(true);
					subcontractOrderD.update();
				}
				// 根据细表id反查 中间表数据
				List<Record> subcontractOrderDRefList = subcontractOrderRefService.findBySubContractOrderDIds(notIds);
				// 获取中间表对应的物料到货计划数据
				List<Long> demandPlanDIds = subcontractOrderDRefList.stream().map(record -> record.getLong(SubcontractOrderRef.IDEMANDPLANDID)).collect(Collectors.toList());
				// 修改到货计划细表状态
				demandPlanDService.batchUpdateGenTypeByIds(demandPlanDIds, OrderGenTypeEnum.NOT_GEN.getValue(), CompleteTypeEnum.INCOMPLETE.getValue());
				// 修改主表状态
				List<Long> demandPlanMIds = demandPlanMService.pegging(demandPlanDIds);
				demandPlanMService.batchUpdateStatusByIds(demandPlanMIds, CompleteTypeEnum.INCOMPLETE.getValue());
				// 删除 中间表数据
				subcontractOrderRefService.removeBySubContractOrderDIds(notIds);
			}
			// 修改
			for (Long invId : invTableMap.keySet()){
				JSONObject invJsonObject = invTableMap.get(invId);
				Long purchaseOrderDId = invJsonObject.getLong(PurchaseOrderD.IAUTOID);
				SubcontractOrderD subcontractOrderD = subcontractOrderDService.findById(purchaseOrderDId);
				ValidationUtils.notNull(subcontractOrderD, "采购订单细表数据未找到");
				// 备注
				subcontractOrderD.setCMemo(invJsonObject.getString(PurchaseOrderD.CMEMO));
				// 添加到货地址
				VendorAddr vendorAddr = vendorAddrService.findById(invJsonObject.getLong(PurchaseOrderD.IVENDORADDRID));
				ValidationUtils.notNull(vendorAddr, "供应商地址不存在！！！");
				subcontractOrderD.setCAddress(vendorAddr.getCDistrictName());
				subcontractOrderD.setIVendorAddrId(invJsonObject.getLong(PurchaseOrderD.IVENDORADDRID));
				subcontractOrderD.update();
			}
			subcontractOrderM.update();
			return true;
		});
	}
	
	/**
	 * 保存操作
	 * @param formJsonObject
	 * @param invDataMap
	 * @param invTableMap
	 */
	private void saveSubmit(JSONObject formJsonObject, Map<Long, JSONObject> invDataMap, Map<Long, JSONObject> invTableMap){
		// 创建主表对象
		SubcontractOrderM subcontractOrderM = createSubcontractOrderM(formJsonObject);
		// 日期
		Map<String, Integer> calendarMap = getCalendarMap(DateUtil.date(subcontractOrderM.getDBeginDate()), DateUtil.date(subcontractOrderM.getDEndDate()));
		// 获取所有明细数据
		// 记录多个子件数据
		List<SubcontractOrderD> subcontractOrderDList = new ArrayList<>();
		List<SubcontractorderdQty> subcontractOrderdQtyList= new ArrayList<>();
		List<SubcontractOrderRef> subcontractOrderRefList= new ArrayList<>();
		
		// 校验采购合同号是否存在
		Integer count = findOderNoIsNotExists(subcontractOrderM.getCOrderNo());
		ValidationUtils.isTrue(ObjUtil.isEmpty(count) || count == 0, "采购订单号已存在");
		int seq = 0;
		for (Long inventoryId : invTableMap.keySet()){
			// 记录供应商地址及备注
			JSONObject invJsonObject = invTableMap.get(inventoryId);
			// 存在这个存货说明没有删除
			if (!invDataMap.containsKey(inventoryId)){
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
			SubcontractOrderD subcontractOrderD = subcontractOrderDService.createSubcontractOrderD(subcontractOrderM.getIAutoId(), dataJsonObject);
			
			Long subcontractId = subcontractOrderD.getIAutoId();
			
			// 创建采购订单明细数量
			JSONArray purchaseOrderdQtyJsonArray = dataJsonObject.getJSONArray(SubcontractOrderD.SUBCONTRACTORDERD_QTY_LIST.toLowerCase());
			List<SubcontractorderdQty> createPurchaseOrderdQtyList = subcontractorderdQtyService.getSubcontractOrderdQty(subcontractId, purchaseOrderdQtyJsonArray, seq);
			
			// 创建采购订单与到货计划关联
			JSONArray purchaseOrderRefJsonArray = dataJsonObject.getJSONArray(SubcontractOrderM.PURCHASEORDERREFLIST.toLowerCase());
			List<SubcontractOrderRef> createPurchaseOrderRefList = subcontractOrderRefService.getSubcontractOrderRefList(subcontractId, purchaseOrderRefJsonArray);
			// 添加到集合
			subcontractOrderDList.add(subcontractOrderD);
			subcontractOrderdQtyList.addAll(createPurchaseOrderdQtyList);
			subcontractOrderRefList.addAll(createPurchaseOrderRefList);
		}
		
		// 操作
		tx( ()-> {
			
			subcontractOrderDService.batchSave(subcontractOrderDList);
			subcontractorderdQtyService.batchSave(subcontractOrderdQtyList);
			subcontractOrderRefService.batchSave(subcontractOrderRefList);
			// 修改物料到货计划状态
			List<Long> demandPlanDIds = subcontractOrderRefList.stream().map(SubcontractOrderRef::getIDemandPlanDid).collect(Collectors.toList());
			// 根据到货细表id反查到货计划主表id
			List<Long> demandPlanMIds = demandPlanMService.pegging(demandPlanDIds);
			// 判断是否需要更改到货计划主表状态 改为已完成
			demandPlanMService.batchUpdateStatusByIds(demandPlanMIds, CompleteTypeEnum.COMPLETE.getValue());
			// 修改到货计划细表状态 改为已完成
			demandPlanDService.batchUpdateGenTypeByIds(demandPlanDIds, OrderGenTypeEnum.SUBCONTRACT_GEN.getValue(), CompleteTypeEnum.COMPLETE.getValue());
			subcontractOrderM.setCOrderNo(generateCGCode());
			subcontractOrderM.setDOrderDate(DateUtil.date());
			subcontractOrderM.save();
			return true;
		});
	}
	
	private SubcontractOrderM createSubcontractOrderM(JSONObject formJsonObject){
		SubcontractOrderM subcontractOrderM = new SubcontractOrderM();
		Long iAutoId = formJsonObject.getLong(SubcontractOrderM.IAUTOID);
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		DateTime date = DateUtil.date();
		
		if (iAutoId == null){
			iAutoId = JBoltSnowflakeKit.me.nextId();
			subcontractOrderM.setAdd(true);
			subcontractOrderM.setIOrgId(getOrgId());
			subcontractOrderM.setCOrgCode(getOrgCode());
			subcontractOrderM.setCOrgName(getOrgName());
			subcontractOrderM.setICreateBy(userId);
			subcontractOrderM.setCCreateName(userName);
			subcontractOrderM.setDCreateTime(date);
			subcontractOrderM.setDBeginDate(formJsonObject.getDate(SubcontractOrderM.DBEGINDATE));
			subcontractOrderM.setDEndDate(formJsonObject.getDate(SubcontractOrderM.DENDDATE));
			// 默认 已失效隐藏
			subcontractOrderM.setHideInvalid(true);
		}
		subcontractOrderM.setIAutoId(iAutoId);
		setSubcontractOrderM(subcontractOrderM, formJsonObject, userId, userName, date);
		return subcontractOrderM;
	}
	
	private void setSubcontractOrderM(SubcontractOrderM subcontractOrderM, JSONObject formJsonObject, Long userId, String userName, Date date){
		subcontractOrderM.setIDutyUserId(formJsonObject.getLong(SubcontractOrderM.IDUTYUSERID));
		subcontractOrderM.setCOrderNo(formJsonObject.getString(SubcontractOrderM.CORDERNO));
		subcontractOrderM.setIPurchaseTypeId(formJsonObject.getLong(SubcontractOrderM.IPURCHASETYPEID));
		subcontractOrderM.setIPayableType(formJsonObject.getInteger(SubcontractOrderM.IPAYABLETYPE));
		subcontractOrderM.setDOrderDate(formJsonObject.getDate(SubcontractOrderM.DORDERDATE));
		subcontractOrderM.setIVendorId(formJsonObject.getLong(SubcontractOrderM.IVENDORID));
		subcontractOrderM.setCMemo(formJsonObject.getString(SubcontractOrderM.CMEMO));
		// 币种
		subcontractOrderM.setCCurrency(formJsonObject.getString(SubcontractOrderM.CCURRENCY));
		subcontractOrderM.setIBusType(formJsonObject.getInteger(SubcontractOrderM.IBUSTYPE));
		
		subcontractOrderM.setIDepartmentId(formJsonObject.getLong(SubcontractOrderM.IDEPARTMENTID));
		subcontractOrderM.setITaxRate(formJsonObject.getBigDecimal(SubcontractOrderM.ITAXRATE));
		subcontractOrderM.setIExchangeRate(formJsonObject.getBigDecimal(SubcontractOrderM.IEXCHANGERATE));
		subcontractOrderM.setIType(formJsonObject.getInteger(SubcontractOrderM.ITYPE));
		// 修改时间
		subcontractOrderM.setIUpdateBy(userId);
		subcontractOrderM.setCUpdateName(userName);
		subcontractOrderM.setDUpdateTime(date);
		subcontractOrderM.setIsDeleted(false);
		subcontractOrderM.setIOrderStatus(OrderStatusEnum.NOT_AUDIT.getValue());
		subcontractOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
	}
	
	private void verifyData(String dataStr, String formStr, String invTableData){
		ValidationUtils.notBlank(formStr, "未获取到表单数据");
		ValidationUtils.notBlank(dataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(invTableData, JBoltMsg.JBOLTTABLE_IS_BLANK);
	}
	
	public Integer findOderNoIsNotExists(Long id, String orderNo){
		String sql = "select count(1) from PS_SubcontractOrderM where cOrderNo =? ";
		if (ObjUtil.isNotNull(id)){
			sql = sql.concat("iAutoId <> "+ id);
		}
		return queryInt(sql, orderNo);
	}
	
	public Integer findOderNoIsNotExists(String orderNo){
		return findOderNoIsNotExists(null, orderNo);
	}
	
	/**
	 * 操作状态：审批/撤回等。。。
	 * @param id
	 * @param type
	 * @return
	 */
	public Ret operationalState(Long id, Integer type) {
		ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
		ValidationUtils.notNull(type, JBoltMsg.PARAM_ERROR);
		SubcontractOrderM subcontractOrderM = findById(id);
		ValidationUtils.notNull(subcontractOrderM, JBoltMsg.DATA_NOT_EXIST);
		return SUCCESS;
	}
	
	/**
	 * 撤回操作
	 */
	public Ret withdraw(Long id){
		SubcontractOrderM subcontractOrderM = getSubcontractOrderM(id);
		// 修改审批状态为：未审批
		subcontractOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
		subcontractOrderM.setDAuditTime(null);
		subcontractOrderM.setIOrderStatus(OrderStatusEnum.NOT_AUDIT.getValue());
		subcontractOrderM.update();
		return SUCCESS;
	}
	
	
	/**
	 * 提审操作
	 */
	public Ret arraignment(Long id){
		SubcontractOrderM subcontractOrderM = getSubcontractOrderM(id);
		// 修改审批状态为：未审批
		subcontractOrderM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
		subcontractOrderM.setIOrderStatus(OrderStatusEnum.AWAIT_AUDIT.getValue());
		subcontractOrderM.update();
		return SUCCESS;
	}
	
	/**
	 * 审核操作
	 * @param id
	 * @return
	 */
	public Ret audit(Long id){
		DateTime date = DateUtil.date();
		SubcontractOrderM subcontractOrderM = getSubcontractOrderM(id);
		subcontractOrderM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
		subcontractOrderM.setDAuditTime(date);
		subcontractOrderM.setDSubmitTime(date);
		subcontractOrderM.setIOrderStatus(OrderStatusEnum.APPROVED.getValue());
		subcontractOrderM.update();
		return SUCCESS;
	}
	
	/**
	 * 关闭操作（关闭之前的状态必须生成）
	 * @param id
	 * @return
	 */
	public Ret close(Long id){
		SubcontractOrderM subcontractOrderM = getSubcontractOrderM(id);
		Integer iOrderStatus = subcontractOrderM.getIOrderStatus();
		OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(iOrderStatus);
		ValidationUtils.notNull(orderStatusEnum, "未知状态");
		boolean flag = OrderStatusEnum.APPROVED.getValue()==orderStatusEnum.getValue() || OrderStatusEnum.GENERATE_CASH_TICKETS.getValue() == orderStatusEnum.getValue();
		ValidationUtils.isTrue(flag, "已审核或已生成现品表的单据才能关闭");
		subcontractOrderM.setIOrderStatus(OrderStatusEnum.CLOSE.getValue());
		subcontractOrderM.update();
		return SUCCESS;
	}
	
	/**
	 * 生成现成票（1个订单1个文件，然后按料品、日期，生成现品票明细查询表）
	 * @param id
	 * @return
	 */
	public Ret generateCash(Long id){
		tx(()->{
			cashNotTransaction(id);
			return true;
		});
		return SUCCESS;
	}
	
	/**
	 * 操作：无事务
	 * @param id
	 */
	public void cashNotTransaction(Long id){
		SubcontractOrderM subcontractOrderM = getSubcontractOrderM(id);
		Integer iOrderStatus = subcontractOrderM.getIOrderStatus();
		OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(iOrderStatus);
		ValidationUtils.notNull(orderStatusEnum, "未知状态");
		boolean flag = OrderStatusEnum.APPROVED.getValue()==orderStatusEnum.getValue();
		ValidationUtils.isTrue(flag, "已审核的单据才能生成现成票");
		subcontractOrderM.setIOrderStatus(OrderStatusEnum.GENERATE_CASH_TICKETS.getValue());
		// 获取细表数据
		List<Record> byPurchaseOrderMId = subcontractOrderDService.findBySubcontractOrderMId(id);
		List<SubcontractOrderDBatch> subcontractOrderDBatchList = new ArrayList<>();
		// 获取数量表
		List<Record> purchaseOrderDQtyList = subcontractorderdQtyService.findBySubcontractOrderMId(id);
		ValidationUtils.notEmpty(purchaseOrderDQtyList, subcontractOrderM.getCOrderNo()+"无现品票生成");
		for (Record record : purchaseOrderDQtyList){
			// 源数量
			BigDecimal sourceQty = record.getBigDecimal(SubcontractorderdQty.IQTY);
			Long subcontractOrderDid = record.getLong(SubcontractorderdQty.ISUBCONTRACTORDERDID);
			Long iSubcontractOrderdQtyId = record.getLong(SubcontractorderdQty.IAUTOID);
			Long inventoryId = record.getLong(SubcontractOrderD.IINVENTORYID);
			
			String dateStr = demandPlanDService.getDate(record.getStr(PurchaseorderdQty.IYEAR), record.getInt(PurchaseorderdQty.IMONTH), record.getInt(PurchaseorderdQty.IDATE));
			DateTime planDate = DateUtil.parse(dateStr, DatePattern.PURE_DATE_PATTERN);
			// 包装数量
			BigDecimal pkgQty = record.getBigDecimal(Inventory.IPKGQTY);
			// 包装数量为空或者为0，生成一张条码，原始数量/打包数量
			if (ObjUtil.isNull(pkgQty) || BigDecimal.ZERO.compareTo(pkgQty) == 0 || sourceQty.compareTo(pkgQty)<=0){
				String barCode = subcontractOrderDBatchService.generateBarCode();
				SubcontractOrderDBatch subcontractOrderDBatch = subcontractOrderDBatchService.createSubcontractOrderDBatch(subcontractOrderDid, iSubcontractOrderdQtyId, inventoryId, planDate, sourceQty, barCode);
				subcontractOrderDBatchList.add(subcontractOrderDBatch);
				continue;
			}
			// 源数量/包装数量 （向上取）
			int count = sourceQty.divide(pkgQty, 0, BigDecimal.ROUND_UP).intValue();
			for (int i=0 ;i<count; i++){
				// count-1： 69/10; 9
				String barCode = subcontractOrderDBatchService.generateBarCode();
				if (i == count-1){
					BigDecimal qty = sourceQty.subtract(BigDecimal.valueOf(i).multiply(pkgQty));
					SubcontractOrderDBatch subcontractOrderDBatch = subcontractOrderDBatchService.createSubcontractOrderDBatch(subcontractOrderDid, iSubcontractOrderdQtyId, inventoryId, planDate, qty, barCode);
					subcontractOrderDBatchList.add(subcontractOrderDBatch);
					break;
				}
				SubcontractOrderDBatch subcontractOrderDBatch = subcontractOrderDBatchService.createSubcontractOrderDBatch(subcontractOrderDid, iSubcontractOrderdQtyId, inventoryId, planDate, pkgQty, barCode);
				subcontractOrderDBatchList.add(subcontractOrderDBatch);
			}
			
		}
		subcontractOrderDBatchService.batchSave(subcontractOrderDBatchList);
		subcontractOrderM.update();
	}
	
	/**
	 * 获取主表数据
	 * @param id
	 * @return
	 */
	private SubcontractOrderM getSubcontractOrderM(Long id){
		ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
		SubcontractOrderM subcontractOrderM = findById(id);
		ValidationUtils.notNull(subcontractOrderM, JBoltMsg.DATA_NOT_EXIST);
		subcontractOrderM.setDUpdateTime(DateUtil.date());
		subcontractOrderM.setIUpdateBy(JBoltUserKit.getUserId());
		subcontractOrderM.setCUpdateName(JBoltUserKit.getUserName());
		return subcontractOrderM;
	}
	
	/**
	 * 删除操作
	 * 	1.修改主表删除状态
	 * 	2.修改细表删除状态
	 * 	3.删除中间表数据
	 * 	4.修改到货计划细表订单状态及未完成状态
	 * 	5.修改到货计划主表未完成状态
	 * @param id
	 * @return
	 */
	public Ret del(Long id) {
		
		tx(()->{
			removeNoTransaction(id);
			return true;
		});
		return SUCCESS;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public Ret batchDel(String ids){
		ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
		tx(()->{
			for (String id : ids.split(",")){
				removeNoTransaction(Long.valueOf(id));
			}
			return true;
		});
		return SUCCESS;
	}
	
	private void removeNoTransaction(Long id){
		SubcontractOrderM subcontractOrderM = getSubcontractOrderM(id);
		ValidationUtils.notNull(subcontractOrderM, JBoltMsg.DATA_NOT_EXIST);
		Integer iOrderStatus = subcontractOrderM.getIOrderStatus();
		OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(iOrderStatus);
		ValidationUtils.notNull(orderStatusEnum, "未知状态");
        boolean flag = OrderStatusEnum.REJECTED.getValue() == iOrderStatus || OrderStatusEnum.NOT_AUDIT.getValue() == iOrderStatus;
        ValidationUtils.isTrue(flag, "只有审核不通过或没审核的数据才能删除");
		subcontractOrderM.setIsDeleted(true);
		List<Record> subcontractOrderRefList = subcontractOrderRefService.findBySubContractOrderMId(id);
		// 修改细表数据
		subcontractOrderDService.removeBySubcontractOrderMId(id);
		if (CollUtil.isNotEmpty(subcontractOrderRefList)){
			List<Long> demandPlanDIds = subcontractOrderRefList.stream().map(record -> record.getLong(SubcontractOrderRef.IDEMANDPLANDID)).collect(Collectors.toList());
			// 修改到货计划细表状态
			demandPlanDService.batchUpdateGenTypeByIds(demandPlanDIds, OrderGenTypeEnum.NOT_GEN.getValue(), CompleteTypeEnum.INCOMPLETE.getValue());
			// 修改主表状态
			List<Long> demandPlanMIds = demandPlanMService.pegging(demandPlanDIds);
			demandPlanMService.batchUpdateStatusByIds(demandPlanMIds, CompleteTypeEnum.INCOMPLETE.getValue());
			// 删除中间表数据
			subcontractOrderRefService.removeByPurchaseOderMId(id);
		}
		subcontractOrderM.update();
	}
	
	/**
	 * 批量生成现成票
	 * @param ids
	 * @return
	 */
	public Ret batchGenerateCash(String ids) {
		ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
		tx(()->{
            for (String id : ids.split(",")){
                cashNotTransaction(Long.valueOf(id));
            }
            return true;
        });
		return SUCCESS;
	}
	
	
	public Ret updateHideInvalid(Long id, Boolean isHide) {
		SubcontractOrderM subcontractOrderM = getSubcontractOrderM(id);
		subcontractOrderM.setHideInvalid(isHide);
		subcontractOrderM.update();
		return SUCCESS;
	}
	
	public Ret saveSubmit(JBoltTable jBoltTable) {
		List<Record> saveRecordList = jBoltTable.getSaveRecordList();
		List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
		SubcontractOrderM subcontractOrderM = createSubcontractOrderM(jBoltTable.getForm());
		JSONObject params = jBoltTable.getParams();
		Object[] deleteIds = jBoltTable.getDelete();
		tx(()->{
			// 新增
			if (CollUtil.isNotEmpty(saveRecordList)){
				saveOrUpdatePurachseD(subcontractOrderM.getIAutoId(), saveRecordList, 1);
			}
			// 修改
			if (CollUtil.isNotEmpty(updateRecordList)){
				saveOrUpdatePurachseD(subcontractOrderM.getIAutoId(), updateRecordList, 2);
			}
			// 删除
			if (deleteIds != null && deleteIds.length > 0){
				subcontractOrderDService.deleteByIds(deleteIds);
			}
			if (subcontractOrderM.isAdd()){
				subcontractOrderM.setCOrderNo(generateCGCode());
				subcontractOrderM.setDOrderDate(DateUtil.date());
				subcontractOrderM.save();
				return true;
			}
			subcontractOrderM.update();
			return true;
		});
		return SUCCESS;
	}
	
	public List<Record> findPurchaseOrderD(Long masId) {
		SubcontractOrderM subcontractOrderM = findById(masId);
		Map<String, List<Record>> listMap = setDate(DateUtil.formatDate(subcontractOrderM.getDBeginDate()), DateUtil.formatDate(subcontractOrderM.getDEndDate()));
		return getPurchaseOrderDList(subcontractOrderM, listMap);
	}
	
	public List<Record> getPurchaseOrderDList(SubcontractOrderM subcontractOrderM, Map<String, List<Record>> calendarMap){
		// 获取所有子件
		List<Record> purchaseOrderDList = subcontractOrderDService.findBySubcontractOrderMId(subcontractOrderM.getIAutoId());
		List<Record> dateList = calendarMap.get(PurchaseOrderM.DATEFIELD);
		Map<String, Record> dateMap = dateList.stream().collect(Collectors.toMap(record -> record.getStr(PurchaseOrderM.DATESTR), record -> record));
		
		// 采购订单到货数量明细
		List<Record> purchaseOrderdQtyList = subcontractorderdQtyService.findBySubcontractOrderMId(subcontractOrderM.getIAutoId());
		// 按存货编码汇总
		Map<Long, Map<String, BigDecimal>>  purchaseOrderdQtyMap = demandPlanDService.getDemandPlanDMap(purchaseOrderdQtyList, PurchaseOrderD.IINVENTORYID);
		
		// key= 2023年04月_存货id
		Map<String, BigDecimal> ymQtyMap = purchaseOrderdQtyList.stream().collect(Collectors.groupingBy(record -> {
			String monthStr = String.format("%02d", record.getInt(PurchaseorderdQty.IMONTH));
			String invId = record.getStr(PurchaseOrderD.IINVENTORYID);
			String yearStr = record.getStr(PurchaseorderdQty.IYEAR);
			return yearStr.concat("年").concat(monthStr).concat("月").concat("_").concat(invId);
		}, Collectors.mapping(record -> record.getBigDecimal(PurchaseorderdQty.IQTY), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
		
		// 设置到货计划明细数量
		subcontractOrderDService.setSubcontractDList(purchaseOrderDList, dateMap, purchaseOrderdQtyMap, ymQtyMap);
		return purchaseOrderDList;
	}
	
	/**
	 *
	 * @param recordList 集合
	 * @param type 1.新增 2.修改
	 */
	public void saveOrUpdatePurachseD(Long purchaseOrderMId, List<Record> recordList, int type){
		List<SubcontractOrderD> subcontractOrderDList = new ArrayList<>();
		List<SubcontractorderdQty> subcontractorderdQtyList = new ArrayList<>();
		List<Long> vendorAdIds = recordList.stream()
				.map(record -> {
					Long vendorAdId ;
					if( "".equals(record.get(SubcontractOrderD.IVENDORADDRID))  || record.get(SubcontractOrderD.IVENDORADDRID) == null){
						return 0L;// 如果为null，返回0L
					}
					vendorAdId=record.getLong(SubcontractOrderD.IVENDORADDRID);
					return vendorAdId;
				})
				.collect(Collectors.toList());
		List<Warehouse> warehouseList = warehouseService.findByIds(vendorAdIds);
		Map<Long, Warehouse> warehouseMap = warehouseList.stream()
				.collect(Collectors.toMap(Warehouse::getIAutoId, warehouse -> warehouse));
		int seq = 0;
		for (Record record : recordList) {

			String isPresentStr = record.getStr(PurchaseOrderD.ISPRESENT);
			Warehouse warehouse = warehouseMap.get(record.getLong(PurchaseOrderD.IVENDORADDRID));
			String wareHouseName = null;
			if(ObjUtil.isNotNull(warehouse)){
				wareHouseName = warehouse.getCWhName();
			}
			int isPresent = 0;
			if (BoolCharEnum.YES.getText().equals(isPresentStr)){
				isPresent = 1;
			}
			SubcontractOrderD subcontractOrderD = null;
			switch (type){
				case 1:
					subcontractOrderD = subcontractOrderDService.create(purchaseOrderMId,
							record.getLong(PurchaseOrderD.IVENDORADDRID),
							record.getLong(PurchaseOrderD.IINVENTORYID),
							wareHouseName,
							record.getStr(PurchaseOrderD.CMEMO),
							record.getStr(PurchaseOrderD.IPKGQTY),
							IsOkEnum.toEnum(isPresent).getText());
					break;
				default:
					subcontractOrderD = subcontractOrderDService.create(record.getLong(PurchaseOrderD.IAUTOID),
							purchaseOrderMId,
							record.getLong(PurchaseOrderD.IVENDORADDRID),
							record.getLong(PurchaseOrderD.IINVENTORYID),
							wareHouseName,
							record.getStr(PurchaseOrderD.CMEMO),
							record.getStr(PurchaseOrderD.IPKGQTY),
							IsOkEnum.toEnum(isPresent).getText());
			}
			subcontractOrderDList.add(subcontractOrderD);
			// 删除qty里的数据重新添加
			/*switch (type){
				case 2:
					purchaseorderdQtyService.delByPurchaseOrderDId(record.getLong(PurchaseOrderD.IAUTOID));
					break;
			}*/
			boolean flag = false;
			String[] columnNames = record.getColumnNames();
			for (String columnName : columnNames){
				if (columnName.contains("日")){
					seq+=1;
					DateTime dateTime = DateUtil.parseDate(columnName);
					String yearStr = DateUtil.format(dateTime, DatePattern.NORM_YEAR_PATTERN);
					String monthStr = DateUtil.format(dateTime, "MM");
					String dayStr = DateUtil.format(dateTime, "dd");
					BigDecimal qty = record.getBigDecimal(columnName);
					SubcontractorderdQty subcontractorderdQty = subcontractorderdQtyService.create(subcontractOrderD.getIAutoId(),
							Integer.parseInt(yearStr),
							Integer.parseInt(monthStr),
							Integer.parseInt(dayStr),
							qty,
							seq);
					switch (type){
						case 2:
							subcontractorderdQtyService.delete(subcontractOrderD.getIAutoId(), subcontractorderdQty.getIYear(), subcontractorderdQty.getIMonth(), subcontractorderdQty.getIDate());
							break;
					}
					if (qty.compareTo(BigDecimal.ZERO)>0){
						subcontractorderdQtyList.add(subcontractorderdQty);
						flag = true;
					}
				}
			}
			if (!flag){
				Inventory inventory = inventoryService.findById(subcontractOrderD.getIInventoryId());
				ValidationUtils.isTrue(flag, "存货编码【"+inventory.getCInvCode()+"】日期数量不能全部为空");
			}
		}
		
		if (CollUtil.isNotEmpty(subcontractOrderDList)){
			switch (type){
				case 1:
					subcontractOrderDService.batchSave(subcontractOrderDList, 500);
					break;
				default:
					subcontractOrderDService.batchUpdate(subcontractOrderDList, 500);
					break;
			}
		}
		
		if (CollUtil.isNotEmpty(subcontractorderdQtyList)){
			subcontractorderdQtyService.batchSave(subcontractorderdQtyList, 500);
		}
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
}
