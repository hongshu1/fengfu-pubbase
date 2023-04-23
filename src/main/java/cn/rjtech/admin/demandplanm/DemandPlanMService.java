package cn.rjtech.admin.demandplanm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.rjtech.admin.purchaseorderdqty.PurchaseorderdQtyService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

/**
 * 需求计划管理-到货计划主表
 * @ClassName: DemandPlanMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-19 16:15
 */
public class DemandPlanMService extends BaseService<DemandPlanM> {
	
	private final DemandPlanM dao=new DemandPlanM().dao();
	
	@Inject
	private PurchaseorderdQtyService purchaseorderdQtyService;
	
	
	@Override
	protected DemandPlanM dao() {
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
	 * @param keywords   关键词
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<DemandPlanM> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param demandPlanM
	 * @return
	 */
	public Ret save(DemandPlanM demandPlanM) {
		if(demandPlanM==null || isOk(demandPlanM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(demandPlanM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(), demandPlanM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param demandPlanM
	 * @return
	 */
	public Ret update(DemandPlanM demandPlanM) {
		if(demandPlanM==null || notOk(demandPlanM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		DemandPlanM dbDemandPlanM=findById(demandPlanM.getIAutoId());
		if(dbDemandPlanM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(demandPlanM.getName(), demandPlanM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(), demandPlanM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param demandPlanM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(DemandPlanM demandPlanM, Kv kv) {
		//addDeleteSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(),demandPlanM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param demandPlanM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(DemandPlanM demandPlanM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(DemandPlanM demandPlanM, String column, Kv kv) {
		//addUpdateSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(), demandPlanM.getName(),"的字段["+column+"]值:"+demandPlanM.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public List<Record> findByVendorDate(String beginDate, String endDate, String iVendorId, Integer processType){
		Okv okv = Okv.by("beginDate", beginDate).set("endDate", endDate).set("iVendorId", iVendorId).set("processType", processType);
		okv.set("orgId", getOrgId());
		return dbTemplate("demandplanm.findByVendorDate", okv).find();
	}
	
	public List<Record> getVendorDateList(String beginDate, String endDate, String iVendorId, Integer processType){
		ValidationUtils.notBlank(beginDate, "请选择日期范围");
		ValidationUtils.notBlank(endDate, "请选择日期范围");
		ValidationUtils.notBlank(iVendorId, "请选择供应商");
		return findByVendorDate(beginDate, endDate, iVendorId, processType);
	}
	
	/**
	 *
	 * @param vendorDateList 	到货计划主表按存货去重集合
	 * @param demandPlanDMap	可根据存货对象获取到货计划细表
	 * @param calendarMap		日期集合
	 * @param invChangeMap		可根据存货对象获取转换后的对象
	 * @param puOrderRefMap
	 */
	public void setVendorDateList(List<Record> vendorDateList , Map<Long, Map<String, BigDecimal>> demandPlanDMap, Map<String, Integer> calendarMap, Map<Long, Record> invChangeMap, Map<Long, List<PurchaseOrderRef>> puOrderRefMap){
		// 同一种的存货编码需要汇总在一起。
		// 将日期设值。
		for (Record record : vendorDateList){
			// 存货id
			Long invId = record.getLong("iInventoryId");
			// 找出对应的到货细表id
			if (puOrderRefMap.containsKey(invId)){
				record.set(PurchaseOrderM.PURCHASEORDERREFLIST, puOrderRefMap.get(invId));
			}
			
			BigDecimal[] arr = new BigDecimal[calendarMap.keySet().size()];
			record.set(PurchaseOrderM.ARR, arr);
			// 存货编码为key，可以获取存货编码下 所有日期范围的值
			if (!demandPlanDMap.containsKey(invId)){
				continue;
			}
			// 记录每一个到货日期的数量
			List<PurchaseorderdQty> purchaseorderdQtyList = new ArrayList<>();
			// 当前日期下的数量
			Map<String, BigDecimal> dateQtyMap = demandPlanDMap.get(invId);
			// 统计合计数量
			BigDecimal amount = BigDecimal.ZERO;
			// 转换前合计数量
			BigDecimal sourceSum = BigDecimal.ZERO;
			for (String dateStr : dateQtyMap.keySet()){
				// 原数量
				BigDecimal qty = dateQtyMap.get(dateStr);
				// yyyyMMdd
				DateTime dateTime = DateUtil.parse(dateStr, DatePattern.PURE_DATE_FORMAT);
				// yyyy-MM-dd
				String formatDateStr = DateUtil.format(dateTime, DatePattern.NORM_DATE_PATTERN);
				String yearStr = DateUtil.format(dateTime, DatePattern.NORM_YEAR_PATTERN);
				String monthStr = DateUtil.format(dateTime, "MM");
				String dayStr = DateUtil.format(dateTime, "dd");
				
				// 当前日期存在，则取值
				if (calendarMap.containsKey(formatDateStr)){
					Integer index = calendarMap.get(formatDateStr);
					arr[index] = qty;
					// 转换率，默认为1
					BigDecimal rate = BigDecimal.ONE;
					// 判断当前存货是否存在物料转换
					if (invChangeMap.containsKey(invId)){
						Record invChangeRecord = invChangeMap.get(invId);
						// 获取转换率
						rate = invChangeRecord.getBigDecimal(InventoryChange.ICHANGERATE);
						// 获取转换后的id
						record.set(PurchaseOrderD.ISOURCEINVENTORYID, invChangeRecord.getLong(PurchaseOrderM.IAFTERINVENTORYID));
						// 需更改转换后的数据
						record.set(PurchaseOrderM.AFTERCINVCODE, invChangeRecord.getLong(PurchaseOrderM.AFTERCINVCODE));
						record.set(PurchaseOrderM.AFTERCINVCODE1, invChangeRecord.getLong(PurchaseOrderM.AFTERCINVCODE1));
						record.set(PurchaseOrderM.AFTERCINVNAME1, invChangeRecord.getLong(PurchaseOrderM.AFTERCINVNAME1));
						record.set(PurchaseOrderM.ISGAVEPRESENT, invChangeRecord.getLong(PurchaseOrderM.ISGAVEPRESENT));
						record.set(PurchaseOrderM.IPKGQTY, invChangeRecord.getLong(PurchaseOrderM.IPKGQTY));
						record.set(PurchaseOrderM.CINVSTD, invChangeRecord.getLong(PurchaseOrderM.CINVSTD));
						record.set(PurchaseOrderM.CUOMNAME, invChangeRecord.getLong(PurchaseOrderM.CUOMNAME));
					}
					PurchaseorderdQty purchaseorderdQty = purchaseorderdQtyService.createPurchaseorderdQty(Integer.parseInt(yearStr),
							Integer.parseInt(monthStr),
							Integer.parseInt(dayStr),
							qty.multiply(rate),
							qty
					);
					purchaseorderdQtyList.add(purchaseorderdQty);
					// 统计数量汇总
					amount = amount.add(qty.multiply(rate));
					sourceSum = sourceSum.add(qty);
				}
			}
			// 字段记录
			if (CollectionUtil.isNotEmpty(purchaseorderdQtyList)){
				record.set(PurchaseOrderD.PURCHASEORDERD_QTY_LIST, purchaseorderdQtyList);
			}
			record.set(PurchaseOrderD.ISUM, amount);
			record.set(PurchaseOrderD.ISOURCESUM, sourceSum);
		}
	}
}
