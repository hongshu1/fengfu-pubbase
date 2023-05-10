package cn.rjtech.admin.schedudemandplan;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bommaster.BomMasterService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.model.momdata.BomMaster;
import cn.rjtech.model.momdata.MrpDemandcomputem;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料需求计划 Service
 * @ClassName: MrpDemandcomputemService
 * @author: chentao
 * @date: 2023-05-02 10:00
 */
public class ScheduDemandPlanService extends BaseService<MrpDemandcomputem> {

	private final MrpDemandcomputem dao = new MrpDemandcomputem().dao();

	@Inject
	private ScheduProductPlanMonthService scheduProductPlanMonthService;

	@Inject
	private BomMasterService bomMasterService;

	@Override
	protected MrpDemandcomputem dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MrpDemandcomputem> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param mrpDemandcomputem
	 * @return
	 */
	public Ret save(MrpDemandcomputem mrpDemandcomputem) {
		if(mrpDemandcomputem==null || isOk(mrpDemandcomputem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(mrpDemandcomputem.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandcomputem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputem.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param mrpDemandcomputem
	 * @return
	 */
	public Ret update(MrpDemandcomputem mrpDemandcomputem) {
		if(mrpDemandcomputem==null || notOk(mrpDemandcomputem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MrpDemandcomputem dbMrpDemandcomputem=findById(mrpDemandcomputem.getIAutoId());
		if(dbMrpDemandcomputem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(mrpDemandcomputem.getName(), mrpDemandcomputem.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandcomputem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputem.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param mrpDemandcomputem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MrpDemandcomputem mrpDemandcomputem, Kv kv) {
		//addDeleteSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(),mrpDemandcomputem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandcomputem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MrpDemandcomputem mrpDemandcomputem, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(mrpDemandcomputem, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
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
	 * @param mrpDemandcomputem 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MrpDemandcomputem mrpDemandcomputem,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MrpDemandcomputem mrpDemandcomputem, String column, Kv kv) {
		//addUpdateSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputem.getName(),"的字段["+column+"]值:"+mrpDemandcomputem.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandcomputem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MrpDemandcomputem mrpDemandcomputem, Kv kv) {
		//这里用来覆盖 检测MrpDemandcomputem是否被其它表引用
		return null;
	}


	//-----------------------------------------------------------------物料需求计划计算-----------------------------------------------

	/**
	 * 递归 从上往下展 查询该母件id集下所有层级的子件物料
	 * compareidList 子件物料集
	 * itemList 母件id集
	 * sum  记录递归层数，避免死循环/层级排序
	 */
	public List<Record> dataBomScheduList(List<Record> compareidList,List<String> itemList, Integer sum) {
		if (sum >20 || itemList.size() <1){
			return compareidList;
		}
		//母件id
		List<BomMaster> bommasterList = bomMasterService.find("select * from Bd_BomMaster WHERE iInventoryId IN (" + CollUtil.join(itemList, COMMA) + ") ");
		if (CollUtil.isEmpty(bommasterList)){
			return compareidList;
		}
		List<String> masterids = new ArrayList<>();
		for (BomMaster bommaster : bommasterList){
			Long id = bommaster.getIAutoId();
			masterids.add(String.valueOf(id));
		}

		//根据母件id查询子表
		List<Record> bomcompareList = findRecord("select\n" +
				"a.iInventoryId as invId,c.cInvCode as invCode,\n" +
				"c.cInvCode1,\n" +
				"c.cInvName1,\n" +
				"c.iSaleType,\n" +
				"i.iPsLevel,\n" +
				"e.iVendorId,\n" +
				"f.cVenCode,\n" +
				"f.cVenName,\n" +
				"c.iPkgQty,\n" +
				"g.iInnerInStockDays,\n" +
				"b.iInventoryId as pinvId,d.cInvCode as pinvCode,\n" +
				"a.iQty as useRate\n" +
				"from Bd_BomCompare as a  \n" +
				"left join Bd_BomMaster as b on a.iBOMMasterId = b.iAutoId \n" +
				"left join Bd_Inventory as c on a.iInventoryId = c.iAutoId\n" +
				"left join Bd_Inventory as d on b.iInventoryId = d.iAutoId\n" +
				"left join Bd_InventoryStockConfig as e on d.iAutoId = e.iInventoryId\n" +
				"left join Bd_Vendor as f on e.iVendorId = f.iAutoId\n" +
				"left join Bd_InventoryPlan as g on g.iInventoryId = d.iAutoId\n" +
				"LEFT JOIN Bd_InventoryWorkRegion AS h ON c.iAutoId = h.iInventoryId AND h.isDefault = 1 AND h.isDeleted = 0\n" +
				"LEFT JOIN Bd_WorkRegionM AS i ON h.iWorkRegionMid = i.iAutoId AND i.isDeleted = 0\n" +
				"where b.isDeleted = 0 and b.isEffective = 1 \n" +
				"and a.iBOMMasterId in (" + CollUtil.join(masterids, COMMA) + ") \n" +
				"order by c.cInvCode ");
		if (CollUtil.isEmpty(bomcompareList)){
			return compareidList;
		}
		List<String> itemids = new ArrayList<>();
		for (Record bomcompare : bomcompareList){
			bomcompare.set("sort",sum);
			compareidList.add(bomcompare);

			Long itemid = bomcompare.getLong("invId");
			itemids.add(String.valueOf(itemid));
		}
		sum ++;
		return dataBomScheduList(compareidList,itemids,sum);
	}


	public List<ScheduDemandTempDTO> groupMergedList(List<Record> compareidList){
		List<ScheduDemandTempDTO> scheduTempDTOList = new ArrayList<>();
		for (Record record : compareidList){
			ScheduDemandTempDTO scheduDemandTempDTO = new ScheduDemandTempDTO();
			scheduDemandTempDTO.setInvId(record.getLong("invId"));
			scheduDemandTempDTO.setInvCode(record.getStr("invCode"));
			scheduDemandTempDTO.setcInvCode1(record.getStr("cInvCode1"));
			scheduDemandTempDTO.setcInvName1(record.getStr("cInvName1"));
			scheduDemandTempDTO.setiSaleType(record.getInt("iSaleType"));
			scheduDemandTempDTO.setiLevel(record.getInt("iPsLevel"));
			scheduDemandTempDTO.setiVendorId(record.getLong("iVendorId"));
			scheduDemandTempDTO.setcVenCode(record.getStr("cVenCode"));
			scheduDemandTempDTO.setcVenName(record.getStr("cVenName"));
			scheduDemandTempDTO.setiPkgQty(record.getInt("iPkgQty"));
			scheduDemandTempDTO.setiInnerInStockDays(record.getInt("iInnerInStockDays"));
			scheduDemandTempDTO.setPinvId(record.getLong("pinvId"));
			scheduDemandTempDTO.setPinvCode(record.getStr("pinvCode"));
			scheduDemandTempDTO.setUseRate(record.getBigDecimal("useRate"));
			scheduDemandTempDTO.setSort(record.getInt("sort"));
			scheduTempDTOList.add(scheduDemandTempDTO);
		}
		//对物料集进行分组去重
		Map<String, ScheduDemandTempDTO> invListMap = new HashMap<>();
		for(ScheduDemandTempDTO oUser : scheduTempDTOList) {
			//子件去重并取值拼接
			if (invListMap.containsKey(oUser.getInvCode())) { //&& StringUtils.isNotBlank(tmpUser.getInvCode()) && StringUtils.isNotBlank(tmpUser.getPinvCode())
				ScheduDemandTempDTO tmpUser = invListMap.get(oUser.getInvCode());
				tmpUser.setPinvCode(tmpUser.getPinvCode().concat(",").concat(oUser.getPinvCode()));
			}
			else {
				invListMap.put(oUser.getInvCode(), oUser);
			}
		}
		//将物料Map转为List并排序
		List<ScheduDemandTempDTO> newInvList = new ArrayList<>();
		for (ScheduDemandTempDTO dto : invListMap.values()){
			newInvList.add(dto);
		}
		newInvList.sort(Comparator.comparing(ScheduDemandTempDTO::getSort));

		return newInvList;
	}

	public static void main(String[] arg){

		List<ScheduDemandTempDTO> list = new ArrayList<>();

		ScheduDemandTempDTO dto = new ScheduDemandTempDTO();
		dto.setInvCode("C-1");dto.setPinvCode("B-1");
		list.add(dto);

		ScheduDemandTempDTO dto2 = new ScheduDemandTempDTO();
		dto2.setInvCode("C-2");dto2.setPinvCode("B-1");
		list.add(dto2);

		ScheduDemandTempDTO dto3 = new ScheduDemandTempDTO();
		dto3.setInvCode("B-1");dto3.setPinvCode("A-1");
		list.add(dto3);

		ScheduDemandTempDTO dto4 = new ScheduDemandTempDTO();
		dto4.setInvCode("B-2");dto4.setPinvCode("A-1");
		list.add(dto4);

		ScheduDemandTempDTO dto5 = new ScheduDemandTempDTO();
		dto5.setInvCode("C-1");dto5.setPinvCode("B-1");
		list.add(dto5);

		ScheduDemandTempDTO dto6 = new ScheduDemandTempDTO();
		dto6.setInvCode("C-2");dto6.setPinvCode("B-1");
		list.add(dto6);

		ScheduDemandTempDTO dto7 = new ScheduDemandTempDTO();
		dto7.setInvCode("C-3");dto7.setPinvCode("B-2");
		list.add(dto7);

		System.out.println(list);

		/*for (int i = 0; i < list.size(); i++) {
			ScheduDemandTempDTO tempDTO = list.get(i);

		}*/

		System.out.println(list);
	}

	/**
	 * 物料需求计划计算
	 * @param endDate 截止日期
	 * @return
	 */
	public synchronized Ret apsScheduDemandPlan(String endDate) {
		//开始日期
		String startDate = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
		//计算开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = scheduProductPlanMonthService.getBetweenDate(startDate,endDate);

		//TODO:查询物料集信息 (销售类型不为null)内作、外作、外购
		List<Record> invInfoList = dbTemplate("schedudemandplan.getInvInfoList").find();
		//本次内作物料id集
		String idsInJoin = "(";
		List<String> invIdInList = new ArrayList<>();
		//本次外作外购物料id集
		String idsOutJoin = "(";
		List<String> invIdOutList = new ArrayList<>();
		//本次所有物料id集
		String idsJoin = "(";
		for (Record record : invInfoList){
			Long invId = record.getLong("invId");
			int iSaleType = record.getInt("iSaleType");
			//内作
			if (iSaleType == 1){
				idsInJoin = idsInJoin + invId + ",";
				invIdInList.add(invId.toString());
			}
			//外作、外购
			else {
				idsOutJoin = idsOutJoin + invId + ",";
				invIdOutList.add(invId.toString());
			}
			idsJoin = idsJoin + invId + ",";
		}
		idsInJoin = idsInJoin + "601)";
		idsOutJoin = idsOutJoin + "601)";
		idsJoin = idsJoin + "601)";


		//key: iLevel   value:yyyy-MM-dd
		Map<Integer,String> scheduEndDateByLevelMap = new HashMap<>();
		//TODO: 查询各个排产层级的最大排产日期
		List<Record> getScheduEndDateList = dbTemplate("schedudemandplan.getScheduEndDateList").find();
		for (Record record : getScheduEndDateList){
			int iLevel = record.getInt("iLevel");
			String dScheduEndDate = DateUtils.formatDate(record.getDate("dScheduleEndTime"),"yyyy-MM-dd");
			scheduEndDateByLevelMap.put(iLevel,dScheduEndDate);
		}


		//TODO:根据物料集id及日期查询月周排产计划数据三班汇总  （内作）
		List<Record> apsPlanQtyList = dbTemplate("schedudemandplan.getApsMonthPlanSumList",Kv.by("ids",idsInJoin).set("startdate",startDate).set("enddate",endDate)).find();
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String, BigDecimal>> invPlanDateApsMap = new HashMap<>();
		for (Record record : apsPlanQtyList){
			String cInvCode = record.getStr("cInvCode");
			String iYear = record.getStr("iYear");
			int iMonth = record.getInt("iMonth");
			int iDate = record.getInt("iDate");
			BigDecimal planQty = record.getBigDecimal("iQty3");
			//yyyy-MM-dd
			String dateKey = iYear;
			dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
			dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

			if (invPlanDateApsMap.containsKey(cInvCode)){
				//key:yyyy-MM-dd   value:qty
				Map<String,BigDecimal> dateQtyMap = invPlanDateApsMap.get(cInvCode);
				dateQtyMap.put(dateKey,planQty);
			}else {
				Map<String,BigDecimal> dateQtyMap = new HashMap<>();
				dateQtyMap.put(dateKey,planQty);
				invPlanDateApsMap.put(cInvCode,dateQtyMap);
			}
		}
		//TODO:根据物料集id及日期获取客户计划汇总表数据  （所有）
		List<Record> getCusOrderSumList = scheduProductPlanMonthService.getCusOrderSumList(Okv.by("ids",idsJoin).set("startdate",startDate).set("enddate",endDate));
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invPlanDateCusMap = new HashMap<>();
		for (Record record : getCusOrderSumList){
			String cInvCode = record.getStr("cInvCode");
			String iYear = record.getStr("iYear");
			int iMonth = record.getInt("iMonth");
			int iDate = record.getInt("iDate");
			BigDecimal planQty = record.getBigDecimal("iQty3");
			//yyyy-MM-dd
			String dateKey = iYear;
			dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
			dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

			if (invPlanDateCusMap.containsKey(cInvCode)){
				//key:yyyy-MM-dd   value:qty
				Map<String,BigDecimal> dateQtyMap = invPlanDateCusMap.get(cInvCode);
				dateQtyMap.put(dateKey,planQty);
			}else {
				Map<String,BigDecimal> dateQtyMap = new HashMap<>();
				dateQtyMap.put(dateKey,planQty);
				invPlanDateCusMap.put(cInvCode,dateQtyMap);
			}
		}



		//本次所需计算物料需求计划
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invPlanDateInAllMap = new HashMap<>();

		//----------------------------------------------------------------------内作销售-----------------------------------------------------------------------------
		//1、当前日期到生产计划截止日期，取月周生产计划。  从生产计划截止日期到物料需求截止日期，取客户计划汇总表中计划使用数量。
		//2、当前日期到生产计划截止日期范围展算方式：每次只展算下一级外购件及外作件，如外购件及外作件有BOM继续展算，依次类推。
		//3、从生产计划截止日期到物料需求截止日期范围展算方式：全阶BOM展算。


		//TODO：所需进行物料计算的物料（内作Bom）
		List<Record> compareInList = new ArrayList<>();

		compareInList = dataBomScheduList(compareInList,invIdInList,1);
		List<ScheduDemandTempDTO> groupInList = groupMergedList(compareInList);
		//将物料Map转为List并排序
		String idsInJoin2 = "(";
		for (ScheduDemandTempDTO dto : groupInList){
			Long invId = dto.getInvId();
			idsInJoin2 = idsInJoin2 + invId + ",";
		}
		idsInJoin2 = idsInJoin2 + "601)";

		//TODO:获取当前物料集的父级与用量   key: inv   value:<pinv,Record>
		Map<String,Map<String,Record>> pInvByInvInMap = new HashMap<>();
		//查询本次排程所有物料的所有父级物料及其用量提前期信息
		List<Record> pInvInfoList = dbTemplate("schedudemandplan.getPinvInfoList",Okv.by("ids",idsInJoin2)).find();
		for (Record record : pInvInfoList){
			String inv = record.get("invCode");
			String pinv = record.get("pInvCode");
			if (pInvByInvInMap.containsKey(inv)) {
				Map<String,Record> map = pInvByInvInMap.get(inv);
				map.put(pinv,record);
				pInvByInvInMap.put(inv, map);
			} else {
				Map<String,Record> map = new HashMap<>();
				map.put(pinv,record);
				pInvByInvInMap.put(inv, map);
			}
		}

		//TODO: 判断该件是否为内作件：
				//是则找出该件层级的排产截止日期，小于等于截止日期取月周计划，大于截止日期则取客户计划+(父级qty*用量)。
				//否则直接取父级qty*用量。
		for (ScheduDemandTempDTO invInfo : groupInList){
			Long invId = invInfo.getInvId();
			String cInvCode = invInfo.getInvCode();
			Long iVendorId = invInfo.getiVendorId();
			int iInnerInStockDays = invInfo.getiInnerInStockDays();
			int iSaleType = invInfo.getiSaleType();
			int iPsLevel = invInfo.getiLevel();

			//月周计划汇总（内作）
			Map<String,BigDecimal> dateQtyApsMap = invPlanDateApsMap.get(cInvCode);
			//客户计划
			Map<String,BigDecimal> dateQtyCusMap = invPlanDateCusMap.get(cInvCode);

			//本次所计算物料的key:yyyy-MM-dd  value:Qty
			Map<String,BigDecimal> dateQtyMap = new HashMap<>();

			//if当前件为内作则：找出该件层级的排产截止日期，小于等于截止日期取月周计划，大于截止日期则取客户计划+(父级qty*用量)
			if (iSaleType == 1){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate(startDate));
				calendar.add(Calendar.DATE,-1);//日期-1
				String yesterday = DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd");
				String scheduEndDate = scheduEndDateByLevelMap.get(iPsLevel) != null ? scheduEndDateByLevelMap.get(iPsLevel) : yesterday;

				for (String date : scheduDateList){
					//取月周计划
					if (DateUtils.parseDate(date).getTime() <= DateUtils.parseDate(scheduEndDate).getTime()){
						BigDecimal qty = dateQtyApsMap.get(date) != null ? dateQtyApsMap.get(date) : BigDecimal.ZERO;
						dateQtyMap.put(date,qty);
					}
					//取客户计划+(父级qty*用量)
					else {
						BigDecimal cusQty = dateQtyCusMap.get(date) != null ? dateQtyCusMap.get(date) : BigDecimal.ZERO;
						BigDecimal sumPQty = BigDecimal.ZERO;
						//循环父级并相加
						Map<String,Record> pInvMap = pInvByInvInMap.get(cInvCode);
						for (String pInv : pInvMap.keySet()){
							BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty");
							//父级需求计划
							Map<String,BigDecimal> datePQtyAllMap = invPlanDateInAllMap.get(pInv);
							BigDecimal pQty = datePQtyAllMap.get(date);
							if (pQty != null){
								BigDecimal qty = pQty.multiply(realQty);
								sumPQty = sumPQty.add(qty);
							}
						}
						dateQtyMap.put(date,cusQty.add(sumPQty));
					}
				}
			}
			//其他件则：直接取父级qty*用量
			else {
				for (String date : scheduDateList){
					BigDecimal sumPQty = BigDecimal.ZERO;
					//循环父级并相加
					Map<String,Record> pInvMap = pInvByInvInMap.get(cInvCode);
					for (String pInv : pInvMap.keySet()){
						BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty");
						//父级需求计划
						Map<String,BigDecimal> datePQtyAllMap = invPlanDateInAllMap.get(pInv);
						BigDecimal pQty = datePQtyAllMap.get(date);
						if (pQty != null){
							BigDecimal qty = pQty.multiply(realQty);
							sumPQty = sumPQty.add(qty);
						}
					}
					dateQtyMap.put(date,sumPQty);
				}
			}
			invPlanDateInAllMap.put(cInvCode,dateQtyMap);
		}


		//本次所需计算物料需求计划2
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invPlanDateOutAllMap = new HashMap<>();

		//----------------------------------------------------------------------外作外购-----------------------------------------------------------------------------
		//1、从当前日期到物料需求计划截止日期取客户计划汇总表中计划使用数量，全阶BOM展算。

		//TODO：所需进行物料计算的物料（外作外购Bom）
		List<Record> compareOutList = new ArrayList<>();

		compareOutList = dataBomScheduList(compareOutList,invIdOutList,2);
		List<ScheduDemandTempDTO> groupOutList = groupMergedList(compareOutList);

		//将物料Map转为List并排序
		String idsOutJoin2 = "(";
		for (ScheduDemandTempDTO dto : groupOutList){
			Long invId = dto.getInvId();
			idsOutJoin2 = idsOutJoin2 + invId + ",";
		}
		idsOutJoin2 = idsOutJoin2 + "601)";

		//TODO:获取当前物料集的父级与用量   key: inv   value:<pinv,Record>
		Map<String,Map<String,Record>> pInvByInvOutMap = new HashMap<>();
		//查询本次排程所有物料的所有父级物料及其用量提前期信息
		List<Record> pInvInfoOutList = dbTemplate("schedudemandplan.getPinvInfoList",Okv.by("ids",idsOutJoin2)).find();
		for (Record record : pInvInfoOutList){
			String inv = record.get("invCode");
			String pinv = record.get("pInvCode");
			if (pInvByInvOutMap.containsKey(inv)) {
				Map<String,Record> map = pInvByInvOutMap.get(inv);
				map.put(pinv,record);
				pInvByInvOutMap.put(inv, map);
			} else {
				Map<String,Record> map = new HashMap<>();
				map.put(pinv,record);
				pInvByInvOutMap.put(inv, map);
			}
		}

		//TODO: 判断该件是否为外作外购件：
		//是则取客户计划+(父级qty*用量)。
		//否则直接取父级qty*用量。
		for (ScheduDemandTempDTO invInfo : groupOutList){
			Long invId = invInfo.getInvId();
			String cInvCode = invInfo.getInvCode();
			Long iVendorId = invInfo.getiVendorId();
			int iInnerInStockDays = invInfo.getiInnerInStockDays();
			int iSaleType = invInfo.getiSaleType();
			int iPsLevel = invInfo.getiLevel();

			//客户计划
			Map<String,BigDecimal> dateQtyCusMap = invPlanDateCusMap.get(cInvCode);

			//本次所计算物料的key:yyyy-MM-dd  value:Qty
			Map<String,BigDecimal> dateQtyMap = new HashMap<>();

			//if当前件为外购外作则：找出该件层级的排产截止日期，小于等于截止日期取月周计划，大于截止日期则取客户计划+(父级qty*用量)
			if (iSaleType == 2 || iSaleType == 3){
				for (String date : scheduDateList){
					//取客户计划+(父级qty*用量)
					BigDecimal cusQty = dateQtyCusMap.get(date) != null ? dateQtyCusMap.get(date) : BigDecimal.ZERO;
					BigDecimal sumPQty = BigDecimal.ZERO;
					//循环父级并相加
					Map<String,Record> pInvMap = pInvByInvOutMap.get(cInvCode);
					for (String pInv : pInvMap.keySet()){
						BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty");
						//父级需求计划
						Map<String,BigDecimal> datePQtyAllMap = invPlanDateOutAllMap.get(pInv);
						BigDecimal pQty = datePQtyAllMap.get(date);
						if (pQty != null){
							BigDecimal qty = pQty.multiply(realQty);
							sumPQty = sumPQty.add(qty);
						}
					}
					dateQtyMap.put(date,cusQty.add(sumPQty));
				}
			}
			//其他件则：直接取父级qty*用量
			else {
				for (String date : scheduDateList){
					BigDecimal sumPQty = BigDecimal.ZERO;
					//循环父级并相加
					Map<String,Record> pInvMap = pInvByInvOutMap.get(cInvCode);
					for (String pInv : pInvMap.keySet()){
						BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty");
						//父级需求计划
						Map<String,BigDecimal> datePQtyAllMap = invPlanDateOutAllMap.get(pInv);
						BigDecimal pQty = datePQtyAllMap.get(date);
						if (pQty != null){
							BigDecimal qty = pQty.multiply(realQty);
							sumPQty = sumPQty.add(qty);
						}
					}
					dateQtyMap.put(date,sumPQty);
				}
			}
			invPlanDateOutAllMap.put(cInvCode,dateQtyMap);
		}


		//两种算法相同物料数量相加
		for (String inv : invPlanDateOutAllMap.keySet()){
			Map<String,BigDecimal> datePlanOutMap = invPlanDateOutAllMap.get(inv); //2
			//判断两个计划Map是否存在相同的物料 存在则数量相加，没有则将2添加进1
			if (invPlanDateInAllMap.containsKey(inv)){
				Map<String,BigDecimal> datePlanInMap = invPlanDateInAllMap.get(inv); //1
				for (String date : scheduDateList){
					BigDecimal qty2 = datePlanOutMap.get(date);
					BigDecimal qty = datePlanInMap.get(date);
					datePlanInMap.put(date,qty.add(qty2));
				}
				invPlanDateInAllMap.put(inv,datePlanInMap);
			}else {
				invPlanDateInAllMap.put(inv,datePlanOutMap);
			}
		}





		return SUCCESS;
	}




	//-----------------------------------------------------------------物料需求计划预示-----------------------------------------------

	public Page<Record> getMrpDemandForecastMList(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("schedudemandplan.getMrpDemandForecastMList",kv).paginate(pageNumber,pageSize);
	}

	public Ret deleteDemandforecastm(Long id) {
		update("UPDATE Mrp_DemandForecastM SET IsDeleted = '1' WHERE iAutoId = ? ",id);
		return SUCCESS;
	}

	/**
	 * 物料需求计划预示明细
	 */
	public List<Record> getMrpDemandForecastDList(int pageNumber, int pageSize, Kv kv) {
		List<Record> scheduProductPlanMonthList = new ArrayList<>();

		String startDate = kv.getStr("startdate");
		String endDate = kv.getStr("enddate");
		if (notOk(startDate) || notOk(endDate)){
			ValidationUtils.isTrue(false,"开始日期-结束日期不能为空！");
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = scheduProductPlanMonthService.getBetweenDate(startDate,endDate);


		//TODO:根据日期及条件获取物料需求计划预示子表数据
		List<Record> recordPage = dbTemplate("schedudemandplan.getMrpDemandForecastDList",kv).find();
		List<Record> apsPlanQtyList = recordPage;

		//key:供应商id   value:List物料集
		Map<Long,List<String>> vendorInvListMap = new HashMap<>();
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invPlanDateMap = new HashMap<>();
		//key:inv   value:info
		Map<String,Record> invInfoMap = new HashMap<>();
		for (Record record : apsPlanQtyList){
			Long iVendorId = record.getLong("iVendorId");
			String cInvCode = record.getStr("cInvCode");
			String iYear = record.getStr("iYear");
			int iMonth = record.getInt("iMonth");
			int iDate = record.getInt("iDate");
			BigDecimal planQty = record.getBigDecimal("iQty2");
			//yyyy-MM-dd
			String dateKey = iYear;
			dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
			dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

			if (vendorInvListMap.containsKey(iVendorId)){
				List<String> list = vendorInvListMap.get(iVendorId);
				if (!list.contains(cInvCode)){
					list.add(cInvCode);
				}
			}else {
				List<String> list = new ArrayList<>();
				list.add(cInvCode);
				vendorInvListMap.put(iVendorId,list);
			}

			if (invPlanDateMap.containsKey(cInvCode)){
				//key:yyyy-MM-dd   value:qty
				Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(cInvCode);
				dateQtyMap.put(dateKey,planQty);
			}else {
				Map<String,BigDecimal> dateQtyMap = new HashMap<>();
				dateQtyMap.put(dateKey,planQty);
				invPlanDateMap.put(cInvCode,dateQtyMap);
			}
			invInfoMap.put(cInvCode,record);
		}


		//对供应商逐个处理
		for (Long key : vendorInvListMap.keySet()) {
			List<String> recordList = vendorInvListMap.get(key);
			for (String inv : recordList){
				//inv信息
				Record invInfo = invInfoMap.get(inv);
				//key:yyyy-MM-dd   value:qty
				Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(inv);

				//数据处理 行转列并赋值
				scheduRowToColumn(scheduProductPlanMonthList,scheduDateList,invInfo,dateQtyMap,"到货预示");
			}
		}

		Page<Record> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		int num = (int) Math.ceil(scheduProductPlanMonthList.size() / 15);
		page.setTotalPage(num);
		page.setTotalRow(scheduProductPlanMonthList.size());
		page.setList(scheduProductPlanMonthList);

		return scheduProductPlanMonthList;
	}

	/**
	 * 将dateQtyMap根据scheduDateList按日期顺序把qty存入Record并放进List
	 * @param scheduProductPlanMonthList 数据List
	 * @param scheduDateList 排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
	 * @param invInfo key:inv   value:info
	 * @param dateQtyMap key:inv，  value:<yyyy-MM-dd，qty>
	 */
	public void scheduRowToColumn(List<Record> scheduProductPlanMonthList,List<String> scheduDateList,
								  Record invInfo,Map<String,BigDecimal> dateQtyMap,String colName){
		Record planRecord = new Record();
		planRecord.set("cInvCode",invInfo.getStr("cInvCode"));
		planRecord.set("cInvCode1",invInfo.getStr("cInvCode1"));
		planRecord.set("cInvName1",invInfo.getStr("cInvName1"));
		planRecord.set("cVenName",invInfo.getStr("cVenName"));
		planRecord.set("iPkgQty",invInfo.getStr("iPkgQty"));
		planRecord.set("iInnerInStockDays",invInfo.getStr("iInnerInStockDays"));
		if (StringUtils.isNotBlank(colName)){
			planRecord.set("colName",colName);
		}

		//key:yyyy-MM   value:qtySum
		Map<String,BigDecimal> monthQtyMap = new LinkedHashMap<>();
		int monthCount = 1;
		for (int i = 0; i < scheduDateList.size(); i++) {
			String date = scheduDateList.get(i);
			String month = date.substring(0,7);
			BigDecimal qty = dateQtyMap.get(date);
			if (monthQtyMap.containsKey(month)){
				BigDecimal monthSum = monthQtyMap.get(month);
				monthQtyMap.put(month,monthSum.add(qty != null ? qty : BigDecimal.ZERO));
			}else {
				monthQtyMap.put(month,qty != null ? qty : BigDecimal.ZERO);
			}
			int seq = i + 1;
			int day = Integer.parseInt(date.substring(8));
			if (i != 0 && day == 1){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate(date));
				//上一个月份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				calendar.add(Calendar.MONTH,-1);
				String preMonth = sdf.format(calendar.getTime());

				planRecord.set("qtysum"+monthCount,monthQtyMap.get(preMonth));
				planRecord.set("qty"+seq,qty);
				monthCount ++;
				continue;
			}
			if (seq == scheduDateList.size()){
				planRecord.set("qty"+seq,qty);
				planRecord.set("qtysum"+monthCount,monthQtyMap.get(month));
				continue;
			}
			planRecord.set("qty"+seq,qty);
		}
		scheduProductPlanMonthList.add(planRecord);
	}





	//-----------------------------------------------------------------物料到货计划-----------------------------------------------

	public Page<Record> getMrpDemandPlanMList(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("schedudemandplan.getMrpDemandPlanMList",kv).paginate(pageNumber,pageSize);
	}

	public Ret deleteDemandplanm(Long id) {
		update("UPDATE Mrp_DemandPlanM SET IsDeleted = '1' WHERE iAutoId = ? ",id);
		return SUCCESS;
	}

	/**
	 * 物料需求计划预示明细
	 */
	public List<Record> getMrpDemandPlanDList(int pageNumber, int pageSize, Kv kv) {
		List<Record> scheduProductPlanMonthList = new ArrayList<>();

		String startDate = kv.getStr("startdate");
		String endDate = kv.getStr("enddate");
		if (notOk(startDate) || notOk(endDate)){
			ValidationUtils.isTrue(false,"开始日期-结束日期不能为空！");
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = scheduProductPlanMonthService.getBetweenDate(startDate,endDate);


		//TODO:根据日期及条件获取物料到货计划预示子表数据
		List<Record> recordPage = dbTemplate("schedudemandplan.getMrpDemandPlanDList",kv).find();
		List<Record> apsPlanQtyList = recordPage;

		//key:供应商id   value:List物料集
		Map<Long,List<String>> vendorInvListMap = new HashMap<>();
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invPlanDateMap = new HashMap<>();
		//key:inv   value:info
		Map<String,Record> invInfoMap = new HashMap<>();
		for (Record record : apsPlanQtyList){
			Long iVendorId = record.getLong("iVendorId");
			String cInvCode = record.getStr("cInvCode");
			String iYear = record.getStr("iYear");
			int iMonth = record.getInt("iMonth");
			int iDate = record.getInt("iDate");
			BigDecimal planQty = record.getBigDecimal("iQty");
			//yyyy-MM-dd
			String dateKey = iYear;
			dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
			dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

			if (vendorInvListMap.containsKey(iVendorId)){
				List<String> list = vendorInvListMap.get(iVendorId);
				if (!list.contains(cInvCode)){
					list.add(cInvCode);
				}
			}else {
				List<String> list = new ArrayList<>();
				list.add(cInvCode);
				vendorInvListMap.put(iVendorId,list);
			}

			if (invPlanDateMap.containsKey(cInvCode)){
				//key:yyyy-MM-dd   value:qty
				Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(cInvCode);
				dateQtyMap.put(dateKey,planQty);
			}else {
				Map<String,BigDecimal> dateQtyMap = new HashMap<>();
				dateQtyMap.put(dateKey,planQty);
				invPlanDateMap.put(cInvCode,dateQtyMap);
			}
			invInfoMap.put(cInvCode,record);
		}


		//对供应商逐个处理
		for (Long key : vendorInvListMap.keySet()) {
			List<String> recordList = vendorInvListMap.get(key);
			for (String inv : recordList){
				//inv信息
				Record invInfo = invInfoMap.get(inv);
				//key:yyyy-MM-dd   value:qty
				Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(inv);

				//数据处理 行转列并赋值
				scheduRowToColumn(scheduProductPlanMonthList,scheduDateList,invInfo,dateQtyMap,"到货计划");
			}
		}

		Page<Record> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		int num = (int) Math.ceil(scheduProductPlanMonthList.size() / 15);
		page.setTotalPage(num);
		page.setTotalRow(scheduProductPlanMonthList.size());
		page.setList(scheduProductPlanMonthList);

		return scheduProductPlanMonthList;
	}


	//-----------------------------------------------------------------物料需求计划汇总-----------------------------------------------



}



















