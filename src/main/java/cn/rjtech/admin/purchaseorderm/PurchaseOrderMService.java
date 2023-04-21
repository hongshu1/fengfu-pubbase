package cn.rjtech.admin.purchaseorderm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.DatePrinter;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.demandpland.DemandPlanDService;
import cn.rjtech.admin.demandplanm.DemandPlanMService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.purchaseorderdqty.PurchaseorderdQtyService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.CreateByEnum;
import cn.rjtech.enums.OrderStatusTypeEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.ValidationUtils;
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
     * @param cOrderNo 订单编号
     * @param iBusType 业务类型： 1. 普通采购
     * @param iPurchaseType 采购类型
     * @param iVendorId 供应商ID
     * @param iDepartmentId 部门ID
     * @param iPayableType 应付类型：1. 材料费
     * @param iOrderStatus 订单状态：1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭
     * @param iAuditStatus 审核状态：1. 审核中 2. 审核通过 3. 审核不通过
	 * @return
	 */
	public Page<PurchaseOrderM> getAdminDatas(int pageNumber, int pageSize, String cOrderNo, Integer iBusType, Integer iPurchaseType, Long iVendorId, Long iDepartmentId, Integer iPayableType, Integer iOrderStatus, Integer iAuditStatus) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("cOrderNo",cOrderNo);
        sql.eq("iBusType",iBusType);
        sql.eq("iPurchaseType",iPurchaseType);
        sql.eq("iVendorId",iVendorId);
        sql.eq("iDepartmentId",iDepartmentId);
        sql.eq("iPayableType",iPayableType);
        sql.eq("iOrderStatus",iOrderStatus);
        sql.eq("iAuditStatus",iAuditStatus);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
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
		purchaseOrderM.setIOrderStatus(OrderStatusTypeEnum.ORDER_STATUS_SAVE.getValue());
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
		Map<Long, BigDecimal> invChangeRateMap = inventoryChanges.stream().collect(Collectors.toMap(r -> r.getLong(InventoryChange.IBEFOREINVENTORYID), r -> r.getBigDecimal(InventoryChange.ICHANGERATE), (key1, key2) -> key2));
		
		Map<Long, Record> invChangeMap = inventoryChanges.stream().collect(Collectors.toMap(r -> r.getLong(InventoryChange.IBEFOREINVENTORYID),  r -> r, (key1, key2) -> key2));
		
		Map<String, Object> repMap = new HashMap<>();
		// 获取所有日期集合
		Map<String, Integer> calendarMap = getCalendarMap(DateUtil.parseDate(beginDate), DateUtil.parseDate(endDate));
		// 设置到货计划明细数量
		demandPlanMService.setVendorDateList(vendorDateList, demandPlanDMap, calendarMap, invChangeRateMap, invChangeMap);
		
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
		repMap.put("puOrderRefMap", puOrderRefMap);
		return repMap;
	}
}
