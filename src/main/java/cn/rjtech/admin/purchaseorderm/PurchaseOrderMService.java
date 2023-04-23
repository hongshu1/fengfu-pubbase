package cn.rjtech.admin.purchaseorderm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.DatePrinter;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.demandpland.DemandPlanDService;
import cn.rjtech.admin.demandplanm.DemandPlanMService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.purchaseorderd.PurchaseOrderDService;
import cn.rjtech.admin.purchaseorderdqty.PurchaseorderdQtyService;
import cn.rjtech.admin.purchaseorderref.PurchaseOrderRefService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 采购/委外订单-采购订单主表
 * @ClassName: PurchaseOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 15:19
 */
public class PurchaseOrderMService extends BaseService<PurchaseOrderM> {
	private final PurchaseOrderM dao=new PurchaseOrderM().dao();
	
	@Inject
	private MomDataFuncService momDataFuncService;
	@Inject
	private DemandPlanMService demandPlanMService;
	@Inject
	private DemandPlanDService demandPlanDService;
	@Inject
	private InventoryChangeService inventoryChangeService;
	@Inject
	private PurchaseorderdQtyService purchaseorderdQtyService;
	@Inject
	private PurchaseOrderDService purchaseOrderDService;
	@Inject
	private PurchaseOrderRefService purchaseOrderRefService;
	@Inject
	private VendorAddrService vendorAddrService;
	@Inject
	private DictionaryService dictionaryService;
	
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
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> page = dbTemplate("purchaseorderm.findAll").paginate(pageNumber, pageSize);
		changeData(page.getList());
		return page;
	}

	
	private void changeData(List<Record> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}
		// 应付类型
		List<Dictionary> purchaseCopingTypeList = dictionaryService.getListByTypeKey("purchase_coping_type", true);
		
		Map<String, Dictionary> purchaseCopingTypeMap = purchaseCopingTypeList.stream().collect(Collectors.toMap(p -> p.getSn(), p -> p));
		// 业务类型
		List<Dictionary> purchaseBusinessTypeList = dictionaryService.getListByTypeKey("purchase_business_type", true);
		
		Map<String, Dictionary> purchaseBusinessTypeMap = purchaseBusinessTypeList.stream().collect(Collectors.toMap(p -> p.getSn(), p -> p));
		
		for (Record record : list){
			Integer orderStatus = record.getInt(PurchaseOrderM.IORDERSTATUS);
			OrderStatusEnum orderStatusEnum = OrderStatusEnum.toEnum(orderStatus);
			ValidationUtils.notNull(orderStatusEnum, "未知订单状态");
			record.set(PurchaseOrderM.ADUITSTATUSTEXT, orderStatusEnum.getText());
			String iPayableType = record.getStr(PurchaseOrderM.IPAYABLETYPE);
			
			String iBusType = record.getStr(PurchaseOrderM.IBUSTYPE);
			if (purchaseCopingTypeMap.containsKey(iPayableType)){
				record.set(PurchaseOrderM.PAYABLETYPETEXT, purchaseCopingTypeMap.get(iPayableType).getName());
			}
			if (purchaseBusinessTypeMap.containsKey(iBusType)){
				record.set(PurchaseOrderM.BUSTYPETEXT, purchaseBusinessTypeMap.get(iBusType).getName());
			}
		}
	}
	
	/**
	 * 保存
	 * @param purchaseOrderM
	 * @return
	 */
	public Ret save(PurchaseOrderM purchaseOrderM) {
		if(purchaseOrderM==null || isOk(purchaseOrderM.getIAutoId())) {
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
		boolean success=purchaseOrderM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseOrderM
	 * @return
	 */
	public Ret update(PurchaseOrderM purchaseOrderM) {
		if(purchaseOrderM==null || notOk(purchaseOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseOrderM dbPurchaseOrderM=findById(purchaseOrderM.getIAutoId());
		if(dbPurchaseOrderM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseOrderM.getName(), purchaseOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param purchaseOrderM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseOrderM purchaseOrderM, Kv kv) {
		//addDeleteSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(),purchaseOrderM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseOrderM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseOrderM purchaseOrderM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<Record> findByInventoryId(Long inventoryId){
		ValidationUtils.notNull(inventoryId,JBoltMsg.DATA_NOT_EXIST);
		return dbTemplate("inventory.findByInventoryId", Okv.by("inventoryId",inventoryId)).find();

	}
	
	/**
	 * CG年月日4流水号
	 * @return
	 */
	public String generateCGCode() {
		String prefix = "CG";
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
	
	public Map<String, Object> getDateMap(String beginDate, String endDate, String iVendorId, Integer processType){
		
		// 主表获取存货数据 table--value
		List<Record> vendorDateList = demandPlanMService.getVendorDateList(beginDate, endDate, iVendorId, processType);
		// 细表获取存货数量
		List<Record> demandPlanDList = demandPlanDService.findByDemandPlanMList(beginDate, endDate, iVendorId, processType);
		// 记录每一个存货中存在多个物料到货计划
		Map<Long, List<PurchaseOrderRef>> puOrderRefMap = demandPlanDService.getPuOrderRefByInvId(demandPlanDList);
		// 按存货编码汇总
		Map<Long, Map<String, BigDecimal>> demandPlanDMap = demandPlanDService.getDemandPlanDMap(demandPlanDList);
		
		// 获取物料形态转换表
		List<Record> inventoryChanges = inventoryChangeService.findByOrgList(getOrgId());
//		Map<Long, BigDecimal> invChangeRateMap = inventoryChanges.stream().collect(Collectors.toMap(r -> r.getLong(InventoryChange.IAFTERINVENTORYID), r -> r.getBigDecimal(InventoryChange.ICHANGERATE), (key1, key2) -> key2));
//////
		Map<Long, Record> invChangeMap = inventoryChanges.stream().collect(Collectors.toMap(r -> r.getLong(InventoryChange.IAFTERINVENTORYID),  r -> r, (key1, key2) -> key2));
		
		Map<String, Object> repMap = new HashMap<>();
		// 获取所有日期集合
		Map<String, Integer> calendarMap = getCalendarMap(DateUtil.parseDate(beginDate), DateUtil.parseDate(endDate));
		// 设置到货计划明细数量
		demandPlanMService.setVendorDateList(vendorDateList, demandPlanDMap, calendarMap, invChangeMap, puOrderRefMap);
		
		// 第一层：年月
		// 第二层：日
		Map<String, List<Integer>> dateMap = new HashMap<>();
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
		repMap.put("tableHeadMap", dateMap);
		repMap.put("tableData", vendorDateList);
		repMap.put("tableDataStr", JSONObject.toJSONString(vendorDateList));
		repMap.put("puOrderRefMap", puOrderRefMap);
		return repMap;
	}
	
	
	public Ret submit(String dataStr, String formStr, String invTableData, Kv kv) {
		verifyData(dataStr, formStr, invTableData);
		JSONArray jsonArray = JSONObject.parseArray(dataStr);
		JSONObject formJsonObject = JSONObject.parseObject(formStr);
		JSONArray invJsonArray = JSONObject.parseArray(invTableData);
		// 创建主表对象
		PurchaseOrderM purchaseOrderM = createPurchaseOrderM(formJsonObject);
		Map<Long, JSONObject> invDataMap = jsonArray.stream().collect(Collectors.toMap(r -> ((JSONObject)r).getLong(PurchaseOrderD.IINVENTORYID.toLowerCase()),  r -> (JSONObject)r, (key1, key2) -> key2));
		Map<Long, JSONObject> invTableMap = invJsonArray.stream().collect(Collectors.toMap(r -> ((JSONObject)r).getLong(PurchaseOrderD.IINVENTORYID),  r -> (JSONObject)r, (key1, key2) -> key2));
		// 日期
		Map<String, Integer> calendarMap = getCalendarMap(DateUtil.date(purchaseOrderM.getDBeginDate()), DateUtil.date(purchaseOrderM.getDEndDate()));
		// 获取所有明细数据
		// 记录多个子件数据
		List<PurchaseOrderD> purchaseOrderDList = new ArrayList<>();
		List<PurchaseorderdQty> purchaseOrderdQtyList= new ArrayList<>();
		List<PurchaseOrderRef> purchaseOrderdRefList= new ArrayList<>();
		
		// 校验采购合同号是否存在
		Integer count = findOderNoIsNotExists(purchaseOrderM.getCOrderNo());
		ValidationUtils.isTrue(ObjectUtil.isEmpty(count) || count == 0, "采购订单号已存在");
		
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
			dataJsonObject.put(PurchaseOrderD.CADDRESS.toLowerCase(), vendorAddr.getCAddr());
			// 添加到货地址Id
			dataJsonObject.put(PurchaseOrderD.IVENDORADDRID.toLowerCase(), vendorAddr.getIAutoId());
			// 创建采购订单明细
			PurchaseOrderD purchaseOrderD = purchaseOrderDService.createPurchaseOrderD(purchaseOrderM.getIAutoId(), dataJsonObject);
			
			Long purchaseOrderDId = purchaseOrderD.getIAutoId();
			
			// 创建采购订单明细数量
			JSONArray purchaseOrderdQtyJsonArray = dataJsonObject.getJSONArray(PurchaseOrderD.PURCHASEORDERD_QTY_LIST.toLowerCase());
			List<PurchaseorderdQty> createPurchaseOrderdQtyList = purchaseorderdQtyService.getPurchaseorderdQty(purchaseOrderDId, purchaseOrderdQtyJsonArray);
			
			// 创建采购订单与到货计划关联
			JSONArray purchaseOrderRefJsonArray = dataJsonObject.getJSONArray(PurchaseOrderM.PURCHASEORDERREFLIST.toLowerCase());
			List<PurchaseOrderRef> createPurchaseOrderRefList = purchaseOrderRefService.getPurchaseOrderRefList(purchaseOrderDId, purchaseOrderRefJsonArray);
			// 添加到集合
			purchaseOrderDList.add(purchaseOrderD);
			purchaseOrderdQtyList.addAll(createPurchaseOrderdQtyList);
			purchaseOrderdRefList.addAll(createPurchaseOrderRefList);
		}
		
		// 操作
		tx( ()-> {
			// 新增
			purchaseOrderM.save();
			purchaseOrderDService.batchSave(purchaseOrderDList);
			purchaseorderdQtyService.batchSave(purchaseOrderdQtyList);
			purchaseOrderRefService.batchSave(purchaseOrderdRefList);
			// 修改物料到货计划状态
			List<Long> demandPlanDIds = purchaseOrderdRefList.stream().map(PurchaseOrderRef::getIDemandPlanDid).collect(Collectors.toList());
			// 根据到货细表id反查到货计划主表id
			List<Long> demandPlanMIds = demandPlanMService.pegging(demandPlanDIds);
			// 判断是否需要更改到货计划主表状态 改为已完成
			demandPlanMService.batchUpdateStatusByIds(demandPlanMIds, CompleteTypeEnum.COMPLETE.getValue());
			// 修改到货计划细表状态 改为已完成
			demandPlanDService.batchUpdateGenTypeByIds(demandPlanDIds, OrderGenType.PURCHASE_GEN.getValue(), CompleteTypeEnum.COMPLETE.getValue());
			return true;
		});
		return SUCCESS;
	}
	
	
	private PurchaseOrderM createPurchaseOrderM(JSONObject formJsonObject){
		PurchaseOrderM purchaseOrderM = new PurchaseOrderM();
		purchaseOrderM.setIAutoId(JBoltSnowflakeKit.me.nextId());
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
		
		purchaseOrderM.setIOrgId(getOrgId());
		purchaseOrderM.setCOrgCode(getOrgCode());
		purchaseOrderM.setCOrgName(getOrgName());
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		DateTime date = DateUtil.date();
		purchaseOrderM.setICreateBy(userId);
		purchaseOrderM.setCCreateName(userName);
		purchaseOrderM.setDCreateTime(date);
		// 修改时间
		purchaseOrderM.setIUpdateBy(userId);
		purchaseOrderM.setCUpdateName(userName);
		purchaseOrderM.setDUpdateTime(date);
		purchaseOrderM.setIsDeleted(false);
		purchaseOrderM.setHideInvalid(false);
		purchaseOrderM.setDBeginDate(formJsonObject.getDate(PurchaseOrderM.DBEGINDATE));
		purchaseOrderM.setDEndDate(formJsonObject.getDate(PurchaseOrderM.DENDDATE));
		purchaseOrderM.setIOrderStatus(OrderStatusEnum.NOT_AUDIT.getValue());
		purchaseOrderM.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
		return purchaseOrderM;
	}
	
	private void verifyData(String dataStr, String formStr, String invTableData){
		ValidationUtils.notBlank(formStr, "未获取到表单数据");
		ValidationUtils.notBlank(dataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(invTableData, JBoltMsg.JBOLTTABLE_IS_BLANK);
	}
	
	public Integer findOderNoIsNotExists(Long id, String orderNo){
		String sql = "select count(1) from PS_PurchaseOrderM where cOrderNo =? ";
		if (ObjectUtil.isNotNull(id)){
			sql = sql.concat("iAutoId <> "+ id);
		}
		return queryInt(sql, orderNo);
	}
	
	public Integer findOderNoIsNotExists(String orderNo){
		return findOderNoIsNotExists(null, orderNo);
	}
}
