package cn.rjtech.admin.schedudemandplan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bommaster.BomMasterService;
import cn.rjtech.admin.mrpdemandcomputed.MrpDemandcomputedService;
import cn.rjtech.admin.mrpdemandforecastd.MrpDemandforecastdService;
import cn.rjtech.admin.mrpdemandpland.MrpDemandplandService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
	private BomMasterService bomMasterService;
    @Inject
	private MomDataFuncService momDataFuncService;
    @Inject
    private MrpDemandplandService mrpDemandplandService;
    @Inject
    private MrpDemandcomputedService mrpDemandcomputedService;
    @Inject
	private MrpDemandforecastdService mrpDemandforecastdService;
    @Inject
    private ScheduProductPlanMonthService scheduProductPlanMonthService;

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
				"a.iBaseQty as useRate\n" +
				"from Bd_BomD as a  \n" +
				"left join Bd_BomM as b on a.iPid = b.iAutoId \n" +
				"left join Bd_Inventory as c on a.iInventoryId = c.iAutoId\n" +
				"left join Bd_Inventory as d on b.iInventoryId = d.iAutoId\n" +
				"left join Bd_InventoryStockConfig as e on d.iAutoId = e.iInventoryId\n" +
				"left join Bd_Vendor as f on e.iVendorId = f.iAutoId\n" +
				"left join Bd_InventoryPlan as g on g.iInventoryId = d.iAutoId\n" +
				"LEFT JOIN Bd_InventoryWorkRegion AS h ON c.iAutoId = h.iInventoryId AND h.isDefault = 1 AND h.isDeleted = 0\n" +
				"LEFT JOIN Bd_WorkRegionM AS i ON h.iWorkRegionMid = i.iAutoId AND i.isDeleted = 0\n" +
				"where b.isDeleted = 0 \n" +
				"AND CONVERT(DATE, a.dEnableDate) <= CONVERT(DATE, GETDATE())\n" +
				"AND CONVERT(DATE, a.dDisableDate) >= CONVERT(DATE, GETDATE())\n" +
				"and b.iInventoryId in (" + CollUtil.join(itemList, COMMA) + ") \n" +
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
		//对物料集进行分组去重，顺序号取最大值
		Map<String, ScheduDemandTempDTO> invListMap = new HashMap<>();
		for(ScheduDemandTempDTO dto : scheduTempDTOList) {
			//子件去重并取值拼接 顺序号取最大值
			if (invListMap.containsKey(dto.getInvCode())) { //&& StrUtil.isNotBlank(tmpUser.getInvCode()) && StrUtil.isNotBlank(tmpUser.getPinvCode())
				ScheduDemandTempDTO tmpUser = invListMap.get(dto.getInvCode());
				String tmpPinv = tmpUser.getPinvCode() != null ? tmpUser.getPinvCode() : "";
				String dtoPinv = dto.getPinvCode() != null ? dto.getPinvCode() : "";
				tmpUser.setPinvCode(tmpPinv.concat(",").concat(dtoPinv));
				if (dto.getSort() > tmpUser.getSort()){
					tmpUser.setSort(dto.getSort());
				}
				invListMap.put(dto.getInvCode(), tmpUser);
			}
			else {
				invListMap.put(dto.getInvCode(), dto);
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


	/**
	 * 根据父级BOMid找子级
	 * @param bomCompareList 子级物料相关信息源数据集
	 * @param bomCompareCur  当前层物料
	 * @param bomMasterId    BOMid
	 */
	private void packChildren(List<BOMCompareDTO> bomCompareList,BOMCompareDTO bomCompareCur, Long bomMasterId,
							  List<Long> itemIdList) {

		//查找子级
		for (BOMCompareDTO bomCompareDTO : bomCompareList) {
			if (bomCompareDTO.getiBOMMasterId().equals(bomMasterId)) {
				//子级物料
				BOMCompareDTO bom = new BOMCompareDTO();
				bom.setiItemId(bomCompareDTO.getiItemId());
				itemIdList.add(bomCompareDTO.getiItemId());

				bom.setiBOMMasterId(bomCompareDTO.getiBOMMasterId());
				bom.setiUsageUOM(bomCompareDTO.getiUsageUOM());
				bom.setcProdAdvance(bomCompareDTO.getcProdAdvance());

				bomCompareCur.getChildrenList().add(bom);

				//判断当前子级物料是否在BOM主表中存在 存在子件
				if (StrUtil.isNotBlank(bomCompareDTO.getiBOMMasterIdListStr())) {
					List<Long> iBOMMasterIdList =  Arrays.stream(bomCompareDTO.getiBOMMasterIdListStr().split(COMMA)).map(Long::parseLong).collect(Collectors.toList());
					for (Long iBOMMasterId : iBOMMasterIdList) {
						packChildren(bomCompareList, bom, iBOMMasterId, itemIdList);
					}
				}
			}
		}
	}

	/**
	 *
	 * 找出物料集的BOM，并将重复BOM过滤
	 * @param masInvIdList 物料集ids
	 */
	public Ret refreshBOM(List<String> masInvIdList) {
		//查出全部BOM主表集合 父级物料
		List<BomMaster> bomMasterList = bomMasterService.find("SELECT iAutoId,iInventoryId FROM Bd_BOMMaster WHERE isDeleted = '0' AND isEffective = 1 " +
				"AND iInventoryId IN (" + CollUtil.join(masInvIdList, COMMA) + ") ");
		//查询子级物料相关信息
		List<Record> bomCompareList = dbTemplate("schedudemandplan.selectBOMCompare").find();
		List<BOMCompareDTO> bomCompareDTOList = new ArrayList<>();
		for (Record record : bomCompareList) {
			BOMCompareDTO bomCompareDTO = new BOMCompareDTO();
			bomCompareDTO.setiBOMMasterId(record.getLong("iBOMMasterId"));
			bomCompareDTO.setiItemId(record.getLong("iInventoryId"));
			bomCompareDTO.setiUsageUOM(record.getBigDecimal("iUsageUOM"));
			bomCompareDTO.setiBOMMasterIdListStr(record.getStr("iBOMMasterIdListStr"));
			bomCompareDTOList.add(bomCompareDTO);
		}

		//循环递归从上往下找BOM 过滤出特定的物料并将用量及提前期相互计算
		Map<Long, BOMCompareDTO> bomMMapTree = new LinkedHashMap<>();
		List<Long> itemIdList = new ArrayList<>();
		for (BomMaster bommaster : bomMasterList ) {
			BOMCompareDTO bomCompareDTO = new BOMCompareDTO();
			bomCompareDTO.setiItemId(bommaster.getIInventoryId());
			//第二层
			List<BOMCompareDTO> childrenList = new ArrayList<>();
			bomCompareDTO.setChildrenList(childrenList);

			bomMMapTree.put(bommaster.getIAutoId(),bomCompareDTO);
			packChildren(bomCompareDTOList, bomCompareDTO, bommaster.getIAutoId(), itemIdList);
		}
		List<Long> deleteKey = new ArrayList<>();
		//循环过滤子级
		for (Long key : bomMMapTree.keySet()) {
			BOMCompareDTO compareDTO = bomMMapTree.get(key);
			if (itemIdList.contains(compareDTO.getiItemId())) {
				deleteKey.add(key);
			}
		}
		if (ObjectUtils.isNotEmpty(deleteKey)) {
			for (Long key : deleteKey) {
				bomMMapTree.remove(key);
			}
		}
		return SUCCESS;
	}

	/**
	 * 物料需求计划计算
	 * @param endDate 截止日期
	 * @return
	 */
	public synchronized Ret apsScheduDemandPlan(String endDate) {
		if (StrUtil.isBlank(endDate)){
			return fail("截止日期不能为空！");
		}

		//开始日期
		String startDate = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);

		//TODO:查询物料集信息 (销售类型不为null)内作、外作、外购
		List<Record> invInfoList = dbTemplate("schedudemandplan.getInvInfoList").find();
		//本次内作物料id集
		String idsInJoin = "(";
		List<String> invIdInList = new ArrayList<>();
		//本次外作外购物料id集
		String idsOutJoin = "(";
		List<String> invIdOutList = new ArrayList<>();
		//本次所有物料(内作外作外购)id集
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


		//本次所需计算全部物料，不包含内作件
		String idsAllJoin = "(";

		//本次所需计算物料需求计划
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invPlanDateInAllMap = new HashMap<>();
		//----------------------------------------------------------------------内作销售-----------------------------------------------------------------------------
		//1、当前日期到生产计划截止日期，取月周生产计划。  从生产计划截止日期到物料需求截止日期，取客户计划汇总表中计划使用数量。
		//2、当前日期到生产计划截止日期范围展算方式：每次只展算下一级外购件及外作件，如外购件及外作件有BOM继续展算，依次类推。
		//3、从生产计划截止日期到物料需求截止日期范围展算方式：全阶BOM展算。

		//refreshBOM(invIdInList);

		//TODO：所需进行物料计算的物料（内作Bom）
		List<Record> compareInList = new ArrayList<>();
		//TODO：所需进行物料计算的物料（外作外购Bom）
		List<Record> compareOutList = new ArrayList<>();
		for (Record record : invInfoList){
			int iSaleType = record.getInt("iSaleType");
			record.set("sort",1);
			if (iSaleType == 1){
				compareInList.add(record);
			}
			else {
				compareOutList.add(record);
			}
		}

		//查找内作BOM结构
		compareInList = dataBomScheduList(compareInList,invIdInList,2);
		//去重过滤
		List<ScheduDemandTempDTO> groupInList = groupMergedList(compareInList);
		//本次内作BOM所有物料
		String idsInJoin2 = "(";
		for (ScheduDemandTempDTO dto : groupInList){
			Long invId = dto.getInvId();
			idsInJoin2 = idsInJoin2 + invId + ",";
			if (dto.getiSaleType() == null || dto.getiSaleType() != 1){
				idsAllJoin = idsAllJoin + invId + ",";
			}
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
			int iInnerInStockDays = invInfo.getiInnerInStockDays() != null ? invInfo.getiInnerInStockDays() : 1;
			int iSaleType = invInfo.getiSaleType() != null ? invInfo.getiSaleType() : -1;
			int iPsLevel = invInfo.getiLevel() != null ? invInfo.getiLevel() : -1;

			//月周计划汇总（内作）
			Map<String,BigDecimal> dateQtyApsMap = invPlanDateApsMap.get(cInvCode) != null ? invPlanDateApsMap.get(cInvCode) : new HashMap<>();
			//客户计划
			Map<String,BigDecimal> dateQtyCusMap = invPlanDateCusMap.get(cInvCode) != null ? invPlanDateCusMap.get(cInvCode) : new HashMap<>();

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
						Map<String,Record> pInvMap = pInvByInvInMap.get(cInvCode) != null ? pInvByInvInMap.get(cInvCode) : new HashMap<>();
						for (String pInv : pInvMap.keySet()){
							BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty");
							//父级需求计划
							Map<String,BigDecimal> datePQtyAllMap = invPlanDateInAllMap.get(pInv) != null ? invPlanDateInAllMap.get(pInv) : new HashMap<>();
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
					Map<String,Record> pInvMap = pInvByInvInMap.get(cInvCode) != null ? pInvByInvInMap.get(cInvCode) : new HashMap<>();
					for (String pInv : pInvMap.keySet()){
						BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty") != null ? pInvMap.get(pInv).getBigDecimal("Realqty") : BigDecimal.ONE;
						//父级需求计划
						Map<String,BigDecimal> datePQtyAllMap = invPlanDateInAllMap.get(pInv) != null ? invPlanDateInAllMap.get(pInv) : new HashMap<>();
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

		//查找外作外购BOM结构
		compareOutList = dataBomScheduList(compareOutList,invIdOutList,2);
		//去重过滤
		List<ScheduDemandTempDTO> groupOutList = groupMergedList(compareOutList);

		//本次外作外购BOM所有物料
		String idsOutJoin2 = "(";
		for (ScheduDemandTempDTO dto : groupOutList){
			Long invId = dto.getInvId();
			idsOutJoin2 = idsOutJoin2 + invId + ",";
			if (dto.getiSaleType() == null || dto.getiSaleType() != 1){
				idsAllJoin = idsAllJoin + invId + ",";
			}
		}
		idsOutJoin2 = idsOutJoin2 + "601)";
		idsAllJoin = idsAllJoin + "601)";

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
			int iInnerInStockDays = invInfo.getiInnerInStockDays() != null ? invInfo.getiInnerInStockDays() : 1;
			int iSaleType = invInfo.getiSaleType() != null ? invInfo.getiSaleType() : -1;
			int iPsLevel = invInfo.getiLevel() != null ? invInfo.getiLevel() : -1;

			//客户计划
			Map<String,BigDecimal> dateQtyCusMap = invPlanDateCusMap.get(cInvCode) != null ? invPlanDateCusMap.get(cInvCode) : new HashMap<>();

			//本次所计算物料的key:yyyy-MM-dd  value:Qty
			Map<String,BigDecimal> dateQtyMap = new HashMap<>();

			//if当前件为外购外作则：找出该件层级的排产截止日期，小于等于截止日期取月周计划，大于截止日期则取客户计划+(父级qty*用量)
			if (iSaleType == 2 || iSaleType == 3){
				for (String date : scheduDateList){
					//取客户计划+(父级qty*用量)
					BigDecimal cusQty = dateQtyCusMap.get(date) != null ? dateQtyCusMap.get(date) : BigDecimal.ZERO;
					BigDecimal sumPQty = BigDecimal.ZERO;
					//循环父级并相加
					Map<String,Record> pInvMap = pInvByInvOutMap.get(cInvCode) != null ? pInvByInvOutMap.get(cInvCode) : new HashMap<>();
					for (String pInv : pInvMap.keySet()){
						BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty");
						//父级需求计划
						Map<String,BigDecimal> datePQtyAllMap = invPlanDateOutAllMap.get(pInv) != null ? invPlanDateOutAllMap.get(pInv) : new HashMap<>();
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
						BigDecimal realQty = pInvMap.get(pInv).getBigDecimal("Realqty") != null ? pInvMap.get(pInv).getBigDecimal("Realqty") : BigDecimal.ONE;
						//父级需求计划
						Map<String,BigDecimal> datePQtyAllMap = invPlanDateOutAllMap.get(pInv) != null ? invPlanDateOutAllMap.get(pInv) : new HashMap<>();
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




		//TODO:查询物料集信息
		List<Record> invInfoAllList = dbTemplate("schedudemandplan.getInvInfoByidsList",Kv.by("ids",idsJoin)).find();
		Map<String,Record> invInfoMap = new HashMap<>();
		for (Record record : invInfoAllList){
			String invCode = record.getStr("invCode");
			invInfoMap.put(invCode,record);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(startDate));
		calendar.add(Calendar.DATE,-1);//日期-1
		//前一天
		String beforeDate = DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd");


		//第三步：先算前1天的到货实绩，算完后再算前一天的到货差异，（先判断如果前1天此物料有入库取实际入库数量，如没有则取到货计划，作为到货实绩；
		//      前1天差异数=前1天需求计划-前1天到货实绩+前2天的到货差异
		//第四步：计算前一天计划在库（等于前两天的计划在库+前一天的到货实绩+前两天的到货差异-前一天的物料需求）
		//第五步：计算当天的到货计划（等于当天的需求+后两天的需求-前一天的计划在库，为正数则订购，为小于等于0时则不订购）


		//物料实绩入库数量
		Map<String,Map<String,BigDecimal>> inStorageQtyListMap = new HashMap<>();

		//物料期初在库计划
		Map<String,BigDecimal> qichuDayQty5Map = new HashMap<>();
		//物料期初差异数量
		Map<String,BigDecimal> qichuDayQty4Map = new HashMap<>();
		//TODO:根据日期查询期初物料集需求计划
		List<Record> demandList = dbTemplate("schedudemandplan.getDemandComputeDQtyList",Okv.by("ids",idsJoin).set("startdate",beforeDate)).find();
		for (Record record : demandList){
			qichuDayQty5Map.put(record.getStr("cInvCode"),record.getBigDecimal("iQty5"));
			qichuDayQty4Map.put(record.getStr("cInvCode"),record.getBigDecimal("iQty4"));
		}

		//本次所需计算物料到货计划
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invDaoHuoDateMap = new HashMap<>();
		//本次所需计算物料在库计划
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invZaiKuDateMap = new HashMap<>();
		//本次所需计算物料到货实绩
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invShiJiDateMap = new HashMap<>();
		//本次所需计算物料差异数量
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String,BigDecimal>> invChaYiDateMap = new HashMap<>();
		for (String inv : invInfoMap.keySet()){
			Record invInfo = invInfoMap.get(inv);
			Map<String,BigDecimal> planMap = invPlanDateInAllMap.get(inv) != null ? invPlanDateInAllMap.get(inv) : new HashMap<>();//每日需求计划
			Map<String,BigDecimal> inStorageMap = inStorageQtyListMap.get(inv) != null ? inStorageQtyListMap.get(inv) : new HashMap<>();//每日实绩入库

			//期初在库计划
			BigDecimal qiChuZaiKuQty = qichuDayQty5Map.get(inv) != null ? qichuDayQty5Map.get(inv) : BigDecimal.ZERO;
			//期初差异数量
			BigDecimal qiChuChaYiQty = qichuDayQty4Map.get(inv) != null ? qichuDayQty4Map.get(inv) : BigDecimal.ZERO;

			Map<String,BigDecimal> daoHuoMap = new HashMap<>();//纪录每日到货计划
			Map<String,BigDecimal> zaiKuMap = new HashMap<>();//纪录每日在库计划
			Map<String,BigDecimal> shiJiMap = new HashMap<>();//纪录每日到货实绩
			Map<String,BigDecimal> chaYiMap = new HashMap<>();//纪录每日差异数量
			for (int i = 0; i < scheduDateList.size(); i++) {
				String toDay = scheduDateList.get(i); //当天
				String beforeDay = scheduDateList.get(i); //前一天

				BigDecimal toDayPlanQty = planMap.get(toDay);//当天的需求
				BigDecimal afterPlan1;//后一天需求
				BigDecimal afterPlan2 = BigDecimal.ZERO;//后二天需求
				if (i == scheduDateList.size() - 2){ //
					afterPlan1 = planMap.get(scheduDateList.get(i + 1));
				}else if (i == scheduDateList.size() - 1){
					afterPlan1 = BigDecimal.ZERO;
					afterPlan2 = BigDecimal.ZERO;
				}else {
					afterPlan1 = planMap.get(scheduDateList.get(i + 1));
					afterPlan2 = planMap.get(scheduDateList.get(i + 2));
				}
				//第五步-当天到货计划计算（等于当天的需求+后两天的需求-前一天的计划在库，为正数则订购，为小于等于0时则不订购）
				BigDecimal AOGQty = BigDecimal.ZERO;
				BigDecimal beforeZaiku = i == 0 ? qiChuZaiKuQty : (zaiKuMap.get(beforeDay) != null ? zaiKuMap.get(beforeDay) : BigDecimal.ZERO);
				BigDecimal qty = toDayPlanQty.add(afterPlan1).add(afterPlan2).subtract(beforeZaiku);
				if (qty.compareTo(BigDecimal.ZERO) > 0){
					AOGQty = qty;
				}
				daoHuoMap.put(toDay,AOGQty);

				//到货实绩（实绩入库有值则取，无则取到货计划）
				BigDecimal qty1 =  inStorageMap.get(toDay);//实绩入库
				BigDecimal qty2 =  daoHuoMap.get(toDay);//到货计划
				BigDecimal shiji = qty1 != null ? qty1 : qty2;
				shiJiMap.put(toDay,shiji);

				//到货差异（需求计划-到货实绩+前1天的到货差异）
				BigDecimal chayi;
				if (i == 0){
					chayi = toDayPlanQty.subtract(shiji).add(qiChuChaYiQty);
				}else {
					BigDecimal beforeShiji = shiJiMap.get(beforeDay);
					chayi = toDayPlanQty.subtract(shiji).add(beforeShiji);
				}
				chaYiMap.put(toDay,chayi);

				//计划在库（等于前一天的计划在库+到货实绩+前一天的到货差异-物料需求）
				BigDecimal zaiku;
				if (i == 0){
					zaiku = qiChuZaiKuQty.add(shiji).add(qiChuChaYiQty).subtract(toDayPlanQty);
				}else {
					BigDecimal beforeChayi = chaYiMap.get(beforeDay);
					BigDecimal beforeZaiKu = zaiKuMap.get(beforeDay) != null ? zaiKuMap.get(beforeDay) : BigDecimal.ZERO;
					zaiku = beforeZaiKu.add(shiji).add(beforeChayi).subtract(toDayPlanQty);
				}
				zaiKuMap.put(toDay,zaiku);

			}
			invDaoHuoDateMap.put(inv,daoHuoMap);
			invZaiKuDateMap.put(inv,zaiKuMap);
			invShiJiDateMap.put(inv,shiJiMap);
			invChaYiDateMap.put(inv,chaYiMap);
		}


		//key:inv+yyyy-MM-dd   value:MrpDemandcomputed
		Map<String, MrpDemandcomputed> mrpDemandMap = new HashMap<>();

		//实绩需求
		for (String inv : invPlanDateInAllMap.keySet()){
			Record invInfo = invInfoMap.get(inv);
			if (invInfo == null){
				continue;
			}
			Long invId = invInfo.getLong("invId");
			Long iVendorId = invInfo.getLong("iVendorId");
			int iPkgQty = invInfo.get("iPkgQty") != null ? invInfo.getInt("iPkgQty") : 0;

			Map<String,BigDecimal> dateQtyMap = invPlanDateInAllMap.get(inv);
			for (String date : dateQtyMap.keySet()){
				String key = inv + "+" + date;
				int year = Integer.parseInt(date.substring(0,4));
				int month = Integer.parseInt(date.substring(5,7));
				int day = Integer.parseInt(date.substring(8,10));
				BigDecimal qty = dateQtyMap.get(date);

				MrpDemandcomputed demandcomputed = new MrpDemandcomputed();
				demandcomputed.setIDemandComputeMid(1L);
				demandcomputed.setIVendorId(iVendorId);
				demandcomputed.setIInventoryId(invId);
				demandcomputed.setIPkgQty(iPkgQty);
				demandcomputed.setIYear(year);
				demandcomputed.setIMonth(month);
				demandcomputed.setIDate(day);
				demandcomputed.setIQty1(qty);
				mrpDemandMap.put(key,demandcomputed);
			}
		}
		//到货计划
		for (String inv : invDaoHuoDateMap.keySet()){
			Record invInfo = invInfoMap.get(inv);
			Long invId = invInfo.getLong("invId");
			Long iVendorId = invInfo.getLong("iVendorId");
			int iPkgQty = invInfo.get("iPkgQty") != null ? invInfo.getInt("iPkgQty") : 0;

			Map<String,BigDecimal> dateQtyMap = invDaoHuoDateMap.get(inv);
			for (String date : dateQtyMap.keySet()){
				String key = inv + "+" + date;
				int year = Integer.parseInt(date.substring(0,4));
				int month = Integer.parseInt(date.substring(5,7));
				int day = Integer.parseInt(date.substring(8,10));
				BigDecimal qty = dateQtyMap.get(date);

				MrpDemandcomputed demandcomputed;
				if (mrpDemandMap.containsKey(key)){
					demandcomputed = mrpDemandMap.get(key);
					demandcomputed.setIQty2(qty);
				}else {
					demandcomputed = new MrpDemandcomputed();
					demandcomputed.setIDemandComputeMid(1L);
					demandcomputed.setIVendorId(iVendorId);
					demandcomputed.setIInventoryId(invId);
					demandcomputed.setIPkgQty(iPkgQty);
					demandcomputed.setIYear(year);
					demandcomputed.setIMonth(month);
					demandcomputed.setIDate(day);
					demandcomputed.setIQty2(qty);
				}
				mrpDemandMap.put(key,demandcomputed);
			}
		}
		//到货实绩
		for (String inv : invShiJiDateMap.keySet()){
			Record invInfo = invInfoMap.get(inv);
			Long invId = invInfo.getLong("invId");
			Long iVendorId = invInfo.getLong("iVendorId");
			int iPkgQty = invInfo.get("iPkgQty") != null ? invInfo.getInt("iPkgQty") : 0;

			Map<String,BigDecimal> dateQtyMap = invShiJiDateMap.get(inv);
			for (String date : dateQtyMap.keySet()){
				String key = inv + "+" + date;
				int year = Integer.parseInt(date.substring(0,4));
				int month = Integer.parseInt(date.substring(5,7));
				int day = Integer.parseInt(date.substring(8,10));
				BigDecimal qty = dateQtyMap.get(date);

				MrpDemandcomputed demandcomputed;
				if (mrpDemandMap.containsKey(key)){
					demandcomputed = mrpDemandMap.get(key);
					demandcomputed.setIQty3(qty);
				}else {
					demandcomputed = new MrpDemandcomputed();
					demandcomputed.setIDemandComputeMid(1L);
					demandcomputed.setIVendorId(iVendorId);
					demandcomputed.setIInventoryId(invId);
					demandcomputed.setIPkgQty(iPkgQty);
					demandcomputed.setIYear(year);
					demandcomputed.setIMonth(month);
					demandcomputed.setIDate(day);
					demandcomputed.setIQty3(qty);
				}
				mrpDemandMap.put(key,demandcomputed);
			}
		}
		//差异数量
		for (String inv : invChaYiDateMap.keySet()){
			Record invInfo = invInfoMap.get(inv);
			Long invId = invInfo.getLong("invId");
			Long iVendorId = invInfo.getLong("iVendorId");
			int iPkgQty = invInfo.get("iPkgQty") != null ? invInfo.getInt("iPkgQty") : 0;

			Map<String,BigDecimal> dateQtyMap = invChaYiDateMap.get(inv);
			for (String date : dateQtyMap.keySet()){
				String key = inv + "+" + date;
				int year = Integer.parseInt(date.substring(0,4));
				int month = Integer.parseInt(date.substring(5,7));
				int day = Integer.parseInt(date.substring(8,10));
				BigDecimal qty = dateQtyMap.get(date);

				MrpDemandcomputed demandcomputed;
				if (mrpDemandMap.containsKey(key)){
					demandcomputed = mrpDemandMap.get(key);
					demandcomputed.setIQty4(qty);
				}else {
					demandcomputed = new MrpDemandcomputed();
					demandcomputed.setIDemandComputeMid(1L);
					demandcomputed.setIVendorId(iVendorId);
					demandcomputed.setIInventoryId(invId);
					demandcomputed.setIPkgQty(iPkgQty);
					demandcomputed.setIYear(year);
					demandcomputed.setIMonth(month);
					demandcomputed.setIDate(day);
					demandcomputed.setIQty4(qty);
				}
				mrpDemandMap.put(key,demandcomputed);
			}
		}
		//计划在库
		for (String inv : invZaiKuDateMap.keySet()){
			Record invInfo = invInfoMap.get(inv);
			Long invId = invInfo.getLong("invId");
			Long iVendorId = invInfo.getLong("iVendorId");
			int iPkgQty = invInfo.get("iPkgQty") != null ? invInfo.getInt("iPkgQty") : 0;

			Map<String,BigDecimal> dateQtyMap = invZaiKuDateMap.get(inv);
			for (String date : dateQtyMap.keySet()){
				String key = inv + "+" + date;
				int year = Integer.parseInt(date.substring(0,4));
				int month = Integer.parseInt(date.substring(5,7));
				int day = Integer.parseInt(date.substring(8,10));
				BigDecimal qty = dateQtyMap.get(date);

				MrpDemandcomputed demandcomputed;
				if (mrpDemandMap.containsKey(key)){
					demandcomputed = mrpDemandMap.get(key);
					demandcomputed.setIQty5(qty);
				}else {
					demandcomputed = new MrpDemandcomputed();
					demandcomputed.setIDemandComputeMid(1L);
					demandcomputed.setIVendorId(iVendorId);
					demandcomputed.setIInventoryId(invId);
					demandcomputed.setIPkgQty(iPkgQty);
					demandcomputed.setIYear(year);
					demandcomputed.setIMonth(month);
					demandcomputed.setIDate(day);
					demandcomputed.setIQty5(qty);
				}
				mrpDemandMap.put(key,demandcomputed);
			}
		}

		List<MrpDemandcomputed> list = new ArrayList<>();
		for (MrpDemandcomputed demandcomputed : mrpDemandMap.values()){
			list.add(demandcomputed);
		}
		if (list.size() == 0){
			tx(() -> {
				dbTemplate("schedudemandplan.deleteDemandComputeD",Kv.by("startdate",startDate)).delete();
				return true;
			});
			return SUCCESS;
		}
		Record mRecord = findFirstRecord("SELECT dBeginDate,dEndDate FROM Mrp_DemandComputeM WHERE iAutoId = 1 ");

		tx(() -> {
			if (mRecord == null){
				MrpDemandcomputem demandcomputem = new MrpDemandcomputem();
				demandcomputem.setIAutoId(1l);
				demandcomputem.setDBeginDate(DateUtils.parseDate(startDate));
				demandcomputem.setDEndDate(DateUtils.parseDate(endDate));
				demandcomputem.setIOrgId(getOrgId());
				demandcomputem.setCOrgCode(getOrgCode());
				demandcomputem.setCOrgName(getOrgName());
				demandcomputem.setICreateBy(JBoltUserKit.getUserId());
				demandcomputem.setCCreateName(JBoltUserKit.getUserUserName());
				demandcomputem.setDCreateTime(new Date());
				demandcomputem.setIUpdateBy(JBoltUserKit.getUserId());
				demandcomputem.setCUpdateName(JBoltUserKit.getUserName());
				demandcomputem.setDUpdateTime(new Date());
				demandcomputem.save();
			}else {
				MrpDemandcomputem demandcomputem = new MrpDemandcomputem();
				demandcomputem.setIAutoId(1L);
				demandcomputem.setDEndDate(DateUtils.parseDate(endDate));
				demandcomputem.setIUpdateBy(JBoltUserKit.getUserId());
				demandcomputem.setCUpdateName(JBoltUserKit.getUserName());
				demandcomputem.setDUpdateTime(new Date());
				demandcomputem.update();
			}

			dbTemplate("schedudemandplan.deleteDemandComputeD",Kv.by("startdate",startDate)).delete();
            List<List<MrpDemandcomputed>> groupList = CollUtil.split(list, 300);
			CountDownLatch countDownLatch = new CountDownLatch(groupList.size());
			ExecutorService executorService = Executors.newFixedThreadPool(groupList.size());
			for(List<MrpDemandcomputed> dataList :groupList){
				executorService.execute(()->{
					MrpDemandcomputed demandcomputed = dataList.get(0);
					if (demandcomputed.getIQty1() == null){
						demandcomputed.setIQty1(BigDecimal.ZERO);
					}
					if (demandcomputed.getIQty2() == null){
						demandcomputed.setIQty2(BigDecimal.ZERO);
					}
					if (demandcomputed.getIQty3() == null){
						demandcomputed.setIQty3(BigDecimal.ZERO);
					}
					if (demandcomputed.getIQty4() == null){
						demandcomputed.setIQty4(BigDecimal.ZERO);
					}
					if (demandcomputed.getIQty5() == null){
						demandcomputed.setIQty5(BigDecimal.ZERO);
					}
					mrpDemandcomputedService.batchSave(dataList);
				});
				countDownLatch.countDown();
			}
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			executorService.shutdown();

			return true;
		});
		return SUCCESS;
	}

	public synchronized List<Map<String,Object>> getDemandList(Kv kv) {
		//物料需求计划
		List<Map<String,Object>> dataList = new ArrayList<>();

		//TODO:查询排产开始日期与截止日期
		Record mRecord = findFirstRecord("SELECT dBeginDate,dEndDate FROM Mrp_DemandComputeM WHERE iAutoId = 1 ");
		String startDate = "";
		String endDate = "";
		if (mRecord != null){
			//startDate = DateUtils.formatDate(apsWeekschedule.getDScheduleBeginTime(),"yyyy-MM-dd");
			endDate = DateUtils.formatDate(mRecord.get("dEndDate"),"yyyy-MM-dd");
		}

		startDate = isOk(kv.get("startdate")) ? kv.getStr("startdate") : startDate;
		endDate = isOk(kv.get("enddate")) ? kv.getStr("enddate") : endDate;
		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startDate)){
			startDate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(endDate)){
			endDate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}

		kv.set("startdate",startDate);
		kv.set("enddate",endDate);
		//开始日期到截止日期集合
		List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);

		//TODO:根据条件查询物料需求计划
		List<Record> demandList = dbTemplate("schedudemandplan.getDemandComputeDList",kv).find();
		Map<String,Record> invInfoMap = new HashMap<>();
		//key:inv   value:<yyyy-MM-dd,record>
		Map<String,Map<String,Record>> invPlanMap = new HashMap<>();
		String idsJoin = "(";
		List<Long> idList = new ArrayList<>();
		for (Record record : demandList){
			Long invId = record.getLong("invId");
			String inv = record.getStr("cInvCode");
			String planDate = record.getStr("planDate");
			invInfoMap.put(inv,record);
			if (invPlanMap.containsKey(inv)){
				Map<String,Record> dateMap = invPlanMap.get(inv);
				dateMap.put(planDate,record);
			}else {
				Map<String,Record> dateMap = new HashMap<>();
				dateMap.put(planDate,record);
				invPlanMap.put(inv,dateMap);
			}
			if (!idList.contains(invId)){
				idsJoin = idsJoin + invId + ",";
				idList.add(invId);
			}
		}
		idsJoin = idsJoin + "601)";

		//TODO:根据物料集和日期查询物料到货计划Sum
		List<Record> getDemandPlanByinvList = dbTemplate("schedudemandplan.getDemandPlanByinvList",kv.set("invs",idsJoin)).find();
		//key:invId    value:List<yyyy-MM-dd>
		Map<Long,List<String>> invLockDateListMap = new HashMap<>();
		for (Record record : getDemandPlanByinvList){
			Long iInventoryId = record.getLong("iInventoryId");
			String planDate = record.getStr("planDate");
			if (invLockDateListMap.containsKey(iInventoryId)){
				List<String> list = invLockDateListMap.get(iInventoryId);
				list.add(planDate);
			}else {
				List<String> list = new ArrayList<>();
				list.add(planDate);
				invLockDateListMap.put(iInventoryId,list);
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(startDate));
		calendar.add(Calendar.DATE,-1);//日期-1
		//前一天
		String beforeDate = DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd");
		//物料期初在库计划
		Map<String,BigDecimal> qichuDayQty5Map = new HashMap<>();
		//物料期初差异数量
		Map<String,BigDecimal> qichuDayQty4Map = new HashMap<>();
		//TODO:根据日期查询期初物料集需求计划
		List<Record> demandListQiChu = dbTemplate("schedudemandplan.getDemandComputeDQtyList",Okv.by("ids",idsJoin).set("startdate",beforeDate)).find();
		for (Record record : demandListQiChu){
			qichuDayQty5Map.put(record.getStr("cInvCode"),record.getBigDecimal("iQty5"));
			qichuDayQty4Map.put(record.getStr("cInvCode"),record.getBigDecimal("iQty4"));
		}


		int seq = 1;
		//循环物料
		for (String inv : invInfoMap.keySet()){
			Record info = invInfoMap.get(inv);

			Map<String,Object> map = new HashMap<>();
			map.put("seq",seq++);
			map.put("cVenName",info.getInt("cVenName"));
			map.put("cInvCode",info.getStr("cInvCode"));
			map.put("cInvCode1",info.getStr("cInvCode1"));
			map.put("cInvName1",info.getStr("cInvName1"));
			map.put("iPkgQty", info.get("iPkgQty") != null ? info.get("iPkgQty") : 0);
			map.put("iInnerInStockDays", info.get("iInnerInStockDays") != null ? info.get("iInnerInStockDays") : 0);
			map.put("qiChuChaYi", qichuDayQty4Map.get(info.getStr("cInvCode")) != null ? qichuDayQty4Map.get(info.getStr("cInvCode")) : 0);
			map.put("qiChuZaiKu", qichuDayQty5Map.get(info.getStr("cInvCode")) != null ? qichuDayQty5Map.get(info.getStr("cInvCode")) : 0);


			//计划
			Map<String,Record> planMap = invPlanMap.get(inv);
			//已生成到货计划日期
			List<String> dateList = invLockDateListMap.get(info.getLong("invId")) != null ? invLockDateListMap.get(info.getLong("invId")) : new ArrayList<>();

			List<Object> objectList = new ArrayList<>();
			for (String date : scheduDateList) {
				Record qtyInfo = planMap.get(date) != null ? planMap.get(date) : new Record();

				Map<String,Object> dataMap = new HashMap<>();
				dataMap.put("xuqiu",qtyInfo.get("iQty1") != null ? qtyInfo.get("iQty1") : 0);
				dataMap.put("jihua",qtyInfo.get("iQty2") != null ? qtyInfo.get("iQty2") : 0);
				dataMap.put("zaiku",qtyInfo.get("iQty5") != null ? qtyInfo.get("iQty5") : 0);
				dataMap.put("shiji",qtyInfo.get("iQty3") != null ? qtyInfo.get("iQty3") : 0);
				dataMap.put("chayi",qtyInfo.get("iQty4") != null ? qtyInfo.get("iQty4") : 0);
				dataMap.put("date",date);
				int month = Integer.parseInt(date.substring(5,7));
				int day = Integer.parseInt(date.substring(8,10));
				dataMap.put("datestr", date.substring(0,4) +"年"+month+"月"+day+"日");
				dataMap.put("lock",false);//未锁定可编辑

				if (dateList.contains(date)){
					dataMap.put("lock",true);//已锁定不可编辑
				}
				objectList.add(dataMap);
			}
			map.put("day",objectList);
			dataList.add(map);
		}
		return dataList;
	}

	public List<Record> getSupplierList(Kv kv) {
		List<Record> supplierList = findRecord("SELECT iAutoId,cVenName FROM Bd_Vendor WHERE isDeleted = 0 ");
		return supplierList;
	}

	public Ret saveForetell(Kv kv) {
		String startdate = kv.getStr("startdate");
		String enddate = kv.getStr("enddate");
		//供应商id数组json
		String data = kv.getStr("data");
		if (StrUtil.isBlank(startdate) || StrUtil.isBlank(enddate) || data.equals("[]")){
			return fail("请先选择保存条件！");
		}

		JSONArray idJSONArr = JSONArray.parseArray(data);
		String iVendorIds = "(601";
		for (int i = 0; i < idJSONArr.size(); i++) {
			iVendorIds = iVendorIds + "," + idJSONArr.get(i);
		}
		iVendorIds = iVendorIds + ")";

		//TODO:根据供应商集查询物料需求计划
		List<MrpDemandcomputed> demandcomputedList = mrpDemandcomputedService.daoTemplate("schedudemandplan.getDemandComputeDByVendorIdList",
				Kv.by("ivendorids",iVendorIds).set("startdate",startdate).set("enddate",enddate)).find();


		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		Date newDate = new Date();
		String orgCode = getOrgCode();
		String orgName = getOrgName();
		Long orgId = getOrgId();
		Long iDemandForecastMid = JBoltSnowflakeKit.me.nextId();

		//计划单号
		String dateStr = DateUtils.formatDate(new Date(),"yyyyMMdd");
		String planNo = momDataFuncService.getNextRouteNo(1L, "YS"+dateStr, 2);
		//预示主表
		MrpDemandforecastm forecastm = new MrpDemandforecastm();
		forecastm.setIOrgId(orgId);
		forecastm.setCOrgCode(orgCode);
		forecastm.setCOrgName(orgName);
		forecastm.setICreateBy(userId);
		forecastm.setCCreateName(userName);
		forecastm.setDCreateTime(newDate);
		forecastm.setIUpdateBy(userId);
		forecastm.setCUpdateName(userName);
		forecastm.setDUpdateTime(newDate);
		forecastm.setIAutoId(iDemandForecastMid);
		forecastm.setDBeginDate(DateUtils.parseDate(startdate));
		forecastm.setDEndDate(DateUtils.parseDate(enddate));
		forecastm.setCForecastNo(planNo);
		forecastm.setDPlanDate(new Date());
		//预示子表
		List<MrpDemandforecastd> forecastdList = new ArrayList<>();
		for (MrpDemandcomputed computed : demandcomputedList){
			MrpDemandforecastd forecastd = new MrpDemandforecastd();
			forecastd.setIDemandForecastMid(iDemandForecastMid);
			forecastd.setIYear(computed.getIYear());
			forecastd.setIMonth(computed.getIMonth());
			forecastd.setIDate(computed.getIDate());
			forecastd.setIVendorId(computed.getIVendorId());
			forecastd.setIInventoryId(computed.getIInventoryId());
			forecastd.setIQty1(computed.getIQty1());
			forecastd.setIQty2(computed.getIQty2());
			forecastd.setIQty3(computed.getIQty5());
			forecastdList.add(forecastd);
		}

		tx(() -> {
			//int num = dbTemplate("schedudemandplan.deleteDemandComputeD",Kv.by("startdate",startDate)).delete();
			forecastm.save();
			if (forecastdList.size() == 0){
				return true;
			}
			List<List<MrpDemandforecastd>> groupList = CollUtil.split(forecastdList,300);
			CountDownLatch countDownLatch = new CountDownLatch(groupList.size());
			ExecutorService executorService = Executors.newFixedThreadPool(groupList.size());
			for(List<MrpDemandforecastd> dataList :groupList){
				executorService.execute(()->{
					mrpDemandforecastdService.batchSave(dataList);
				});
				countDownLatch.countDown();
			}
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			executorService.shutdown();

			return true;
		});

		return SUCCESS;
	}
	public Ret saveArrival(Kv kv) {
		String startdate = kv.getStr("startdate");
		String enddate = kv.getStr("enddate");
		//供应商id数组json
		String data = kv.getStr("data");
		if (StrUtil.isBlank(startdate) || StrUtil.isBlank(enddate) || data.equals("[]")){
			return fail("请先选择保存条件！");
		}

		JSONArray idJSONArr = JSONArray.parseArray(data);
		String iVendorIds = "(601";
		for (int i = 0; i < idJSONArr.size(); i++) {
			iVendorIds = iVendorIds + "," + idJSONArr.get(i);
		}
		iVendorIds = iVendorIds + ")";

		//TODO:根据供应商集查询物料需求计划
		List<MrpDemandcomputed> demandcomputedList = mrpDemandcomputedService.daoTemplate("schedudemandplan.getDemandComputeDByVendorIdList",
				Kv.by("ivendorids",iVendorIds).set("startdate",startdate).set("enddate",enddate)).find();


		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		Date newDate = new Date();
		String orgCode = getOrgCode();
		String orgName = getOrgName();
		Long orgId = getOrgId();
		Long iDemandPlanMid = JBoltSnowflakeKit.me.nextId();

		//计划单号
		String dateStr = DateUtils.formatDate(new Date(),"yyyyMMdd");
		String planNo = momDataFuncService.getNextRouteNo(1L, "DH"+dateStr, 2);
		//到货主表
		MrpDemandplanm planm = new MrpDemandplanm();
		planm.setIOrgId(orgId);
		planm.setCOrgCode(orgCode);
		planm.setCOrgName(orgName);
		planm.setICreateBy(userId);
		planm.setCCreateName(userName);
		planm.setDCreateTime(newDate);
		planm.setIUpdateBy(userId);
		planm.setCUpdateName(userName);
		planm.setDUpdateTime(newDate);
		planm.setIAutoId(iDemandPlanMid);
		planm.setDBeginDate(DateUtils.parseDate(startdate));
		planm.setDEndDate(DateUtils.parseDate(enddate));
		planm.setCPlanNo(planNo);
		planm.setIStatus(1);
		planm.setDPlanDate(new Date());
		//到货子表
		List<MrpDemandpland> forecastdList = new ArrayList<>();
		for (MrpDemandcomputed computed : demandcomputedList){
			MrpDemandpland pland = new MrpDemandpland();
			pland.setIDemandPlanMid(iDemandPlanMid);
			pland.setIYear(computed.getIYear());
			pland.setIMonth(computed.getIMonth());
			pland.setIDate(computed.getIDate());
			pland.setIVendorId(computed.getIVendorId());
			pland.setIInventoryId(computed.getIInventoryId());
			pland.setIQty(computed.getIQty2());
			pland.setIStatus(1);
			pland.setIGenType(0);
			forecastdList.add(pland);
		}

		tx(() -> {
			//int num = dbTemplate("schedudemandplan.deleteDemandComputeD",Kv.by("startdate",startDate)).delete();
			planm.save();
			if (forecastdList.size() == 0){
				return true;
			}
			List<List<MrpDemandpland>> groupList = CollUtil.split(forecastdList,300);
			CountDownLatch countDownLatch = new CountDownLatch(groupList.size());
			ExecutorService executorService = Executors.newFixedThreadPool(groupList.size());
			for(List<MrpDemandpland> dataList :groupList){
				executorService.execute(()->{
					mrpDemandplandService.batchSave(dataList);
				});
				countDownLatch.countDown();
			}
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			executorService.shutdown();

			return true;
		});

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
		List<Record> dataList = new ArrayList<>();

		String startDate = kv.getStr("startdate");
		String endDate = kv.getStr("enddate");
		if (notOk(startDate) || notOk(endDate)){
			ValidationUtils.error("开始日期-结束日期不能为空！");
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);


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
				scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyMap,"到货预示");
			}
		}

		Page<Record> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		int num = (int) Math.ceil(dataList.size() / 15);
		page.setTotalPage(num);
		page.setTotalRow(dataList.size());
		page.setList(dataList);

		return dataList;
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
		if (StrUtil.isNotBlank(colName)){
			planRecord.set("colName",colName);  //自定义列名
		}
		planRecord.set("seq",invInfo.getInt("seq"));  //用于页面排序
		planRecord.set("rowSpan",invInfo.getInt("rowSpan"));  //用于页面跨行条数
		planRecord.set("boolRowSpan",invInfo.getBoolean("boolRowSpan"));  //判断页面数据在第几行时开始跨行
		dateQtyMap = dateQtyMap != null ? dateQtyMap : new HashMap<>();

		//key:yyyy-MM   value:qtySum
		Map<String,BigDecimal> monthQtyMap = new LinkedHashMap<>();
		int monthCount = 1;
		for (int i = 0; i < scheduDateList.size(); i++) {
			String date = scheduDateList.get(i);
			String month = date.substring(0,7);
			BigDecimal qty = dateQtyMap.get(date) != null ? dateQtyMap.get(date) : BigDecimal.ZERO;
			if (monthQtyMap.containsKey(month)){
				BigDecimal monthSum = monthQtyMap.get(month);
				monthQtyMap.put(month,monthSum.add(qty));
			}else {
				monthQtyMap.put(month,qty);
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
	 * 物料需求计划到货明细
	 */
	public List<Record> getMrpDemandPlanDList(int pageNumber, int pageSize, Kv kv) {
		List<Record> dataList = new ArrayList<>();

		String startDate = kv.getStr("startdate");
		String endDate = kv.getStr("enddate");
		if (notOk(startDate) || notOk(endDate)){
			ValidationUtils.error("开始日期-结束日期不能为空！");
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);


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
				scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyMap,"到货计划");
			}
		}

		Page<Record> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		int num = (int) Math.ceil(dataList.size() / 15);
		page.setTotalPage(num);
		page.setTotalRow(dataList.size());
		page.setList(dataList);

		return dataList;
	}


	//-----------------------------------------------------------------物料需求计划汇总-----------------------------------------------

	/**
	 * 生产计划及实绩管理
	 */
	public List<Record> getDemandPlanSumPage(int pageNumber, int pageSize, Kv kv) {
		List<Record> dataList = new ArrayList<>();

		String startDate = kv.getStr("startdate");
		String endDate = kv.getStr("enddate");
		if (notOk(startDate) || notOk(endDate)){
			ValidationUtils.error("开始日期-结束日期不能为空！");
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = DateUtils.getBetweenDate(startDate,endDate);

		pageSize = pageSize * 15;

		//TODO:根据条件查询物料需求计划
		List<Record> demandList = dbTemplate("schedudemandplan.getDemandComputeDList",kv).find();

		//key:inv，   value:<yyyy-MM-dd，QtyPP> 实绩需求
		Map<String,Map<String,BigDecimal>> invPlanDateXuQiuMap = new HashMap<>();
		//key:inv，   value:<yyyy-MM-dd，Qty1S> 到货计划
		Map<String,Map<String,BigDecimal>> invPlanDateDaoHuoMap = new HashMap<>();
		//key:inv，   value:<yyyy-MM-dd，Qty2S> 到货实绩
		Map<String,Map<String,BigDecimal>> invPlanDateShiJiMap = new HashMap<>();
		//key:inv，   value:<yyyy-MM-dd，Qty3S> 差异数量
		Map<String,Map<String,BigDecimal>> invPlanDateChaYiMap = new HashMap<>();
		//key:inv，   value:<yyyy-MM-dd，QtyZK> 计划在库
		Map<String,Map<String,BigDecimal>> invPlanDateZaiKuMap = new HashMap<>();
		//key:供应商id   value:List物料集
		Map<Long,List<String>> venInvListMap = new HashMap<>();
		//key:inv   value:info
		Map<String,Record> invInfoMap = new HashMap<>();
		String invIds = "(";
		List<Long> invIdList = new ArrayList<>();
		for (Record record : demandList){
			Long iVendorId = record.getLong("iVendorId");
			Long invId = record.getLong("invId");
			String cInvCode = record.getStr("cInvCode");
			BigDecimal XuQiu = record.getBigDecimal("iQty1");
			BigDecimal DaoHuo = record.getBigDecimal("iQty2");
			BigDecimal ShiJi = record.getBigDecimal("iQty3");
			BigDecimal ChaYi = record.getBigDecimal("iQty4");
			BigDecimal ZaiKu = record.getBigDecimal("iQty5");
			//yyyy-MM-dd
			String dateKey = record.getStr("planDate");

			if (venInvListMap.containsKey(iVendorId)){
				List<String> list = venInvListMap.get(iVendorId);
				if (!list.contains(cInvCode)){
					list.add(cInvCode);
				}
			}else {
				List<String> list = new ArrayList<>();
				list.add(cInvCode);
				venInvListMap.put(iVendorId,list);
			}

			if (invPlanDateXuQiuMap.containsKey(cInvCode)){
				//key:yyyy-MM-dd   value:XuQiu
				Map<String,BigDecimal> dateQtyPPMap = invPlanDateXuQiuMap.get(cInvCode);
				dateQtyPPMap.put(dateKey,XuQiu);

				//key:yyyy-MM-dd   value:DaoHuo
				Map<String,BigDecimal> dateQty1SMap = invPlanDateDaoHuoMap.get(cInvCode);
				dateQty1SMap.put(dateKey,DaoHuo);

				//key:yyyy-MM-dd   value:ShiJi
				Map<String,BigDecimal> dateQty2SMap = invPlanDateShiJiMap.get(cInvCode);
				dateQty2SMap.put(dateKey,ShiJi);

				//key:yyyy-MM-dd   value:ChaYi
				Map<String,BigDecimal> dateQty3SMap = invPlanDateChaYiMap.get(cInvCode);
				dateQty3SMap.put(dateKey,ChaYi);

				//key:yyyy-MM-dd   value:ZaiKu
				Map<String,BigDecimal> dateQtyZKMap = invPlanDateZaiKuMap.get(cInvCode);
				dateQtyZKMap.put(dateKey,ZaiKu);
			}else {
				//key:yyyy-MM-dd   value:XuQiu
				Map<String,BigDecimal> dateQtyPPMap = new HashMap<>();
				dateQtyPPMap.put(dateKey,XuQiu);
				invPlanDateXuQiuMap.put(cInvCode,dateQtyPPMap);

				//key:yyyy-MM-dd   value:DaoHuo
				Map<String,BigDecimal> dateQty1SMap = new HashMap<>();
				dateQty1SMap.put(dateKey,DaoHuo);
				invPlanDateDaoHuoMap.put(cInvCode,dateQty1SMap);

				//key:yyyy-MM-dd   value:ShiJi
				Map<String,BigDecimal> dateQty2SMap = new HashMap<>();
				dateQty2SMap.put(dateKey,ShiJi);
				invPlanDateShiJiMap.put(cInvCode,dateQty2SMap);

				//key:yyyy-MM-dd   value:ChaYi
				Map<String,BigDecimal> dateQty3SMap = new HashMap<>();
				dateQty3SMap.put(dateKey,ChaYi);
				invPlanDateChaYiMap.put(cInvCode,dateQty3SMap);

				//key:yyyy-MM-dd   value:ZaiKu
				Map<String,BigDecimal> dateQtyZKMap = new HashMap<>();
				dateQtyZKMap.put(dateKey,ZaiKu);
				invPlanDateZaiKuMap.put(cInvCode,dateQtyZKMap);
			}
			invInfoMap.put(cInvCode,record);
			if (!invIdList.contains(invId)){
				invIds = invIds + invId + ",";
				invIdList.add(invId);
			}
		}
		invIds = invIds + "601)";


		//TODO:获取当前物料集的父级与用量   key: inv   value:<pinv,Record>
		Map<String,Map<String,Record>> pInvByInvInMap = new HashMap<>();
		//本次全部母件物料
		String pinvIds = "(";
		List<Long> pinvIdList = new ArrayList<>();
		//查询本次排程所有物料的所有父级物料及其用量提前期信息
		List<Record> pInvInfoList = dbTemplate("schedudemandplan.getPinvInfoList",Okv.by("ids",invIds)).find();
		for (Record record : pInvInfoList){
			String inv = record.get("invCode");
			String pinv = record.get("pInvCode");
			Long pInvId = record.getLong("pInvId");
			if (pInvByInvInMap.containsKey(inv)) {
				Map<String,Record> map = pInvByInvInMap.get(inv);
				map.put(pinv,record);
				pInvByInvInMap.put(inv, map);
			} else {
				Map<String,Record> map = new HashMap<>();
				map.put(pinv,record);
				pInvByInvInMap.put(inv, map);
			}
			if (!pinvIdList.contains(pInvId)){
				pinvIds = pinvIds + pInvId + ",";
				pinvIdList.add(pInvId);
			}
		}
		pinvIds = pinvIds + "601)";


		//TODO:根据物料集id及日期查询月周排产计划数据三班汇总
		List<Record> apsPlanQtyList = dbTemplate("schedudemandplan.getApsMonthPlanSumList",Kv.by("ids",pinvIds).set("startdate",startDate).set("enddate",endDate)).find();
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
		//TODO:根据物料集id及日期获取客户计划汇总表数据
		List<Record> getCusOrderSumList = scheduProductPlanMonthService.getCusOrderSumList(Okv.by("ids",pinvIds).set("startdate",startDate).set("enddate",endDate));
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


		int seq = 1;
		//对产线逐个处理
		for (Long key : venInvListMap.keySet()) {
			List<String> recordList = venInvListMap.get(key);
			for (String inv : recordList){
				//inv信息
				Record invInfo = invInfoMap.get(inv);
				invInfo.set("seq",seq++);  //用于页面排序

				Map<String,Record> pinvMap = pInvByInvInMap.get(inv) != null ? pInvByInvInMap.get(inv) : new HashMap<>();
				invInfo.set("rowSpan",pinvMap.keySet().size() + 5);  //用于页面跨行条数
				int num = 1;
				for (String pinv : pinvMap.keySet()){
					//判断页面数据在第几行时开始跨行
					invInfo = num == 1 ? invInfo.set("boolRowSpan", true) : invInfo.set("boolRowSpan", false);

					Map<String,BigDecimal> dateQtyApsMap = invPlanDateApsMap.get(pinv) != null ? invPlanDateApsMap.get(pinv) : invPlanDateCusMap.get(pinv);
					//数据处理 行转列并赋值
					scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyApsMap,"母件"+ num++ +"["+pinv+"]计划1S/2S/3S");
				}

				//判断页面数据在第几行时开始跨行
				invInfo = pinvMap.keySet().size() == 0 ? invInfo.set("boolRowSpan", true) : invInfo.set("boolRowSpan", false);

				//key:yyyy-MM-dd   value:qty  实绩需求
				Map<String,BigDecimal> dateQtyPPMap = invPlanDateXuQiuMap.get(inv);
				//数据处理 行转列并赋值
				scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyPPMap,"实绩需求");

				invInfo.set("boolRowSpan",false);
				//key:yyyy-MM-dd   value:qty  到货计划
				Map<String,BigDecimal> dateQty1SMap = invPlanDateDaoHuoMap.get(inv);
				//数据处理 行转列并赋值
				scheduRowToColumn(dataList,scheduDateList,invInfo,dateQty1SMap,"到货计划");

				//key:yyyy-MM-dd   value:qty  到货实绩
				Map<String,BigDecimal> dateQty2SMap = invPlanDateShiJiMap.get(inv);
				//数据处理 行转列并赋值
				scheduRowToColumn(dataList,scheduDateList,invInfo,dateQty2SMap,"到货实绩");

				//key:yyyy-MM-dd   value:qty  差异数量
				Map<String,BigDecimal> dateQty3SMap = invPlanDateChaYiMap.get(inv);
				//数据处理 行转列并赋值
				scheduRowToColumn(dataList,scheduDateList,invInfo,dateQty3SMap,"差异数量");

				//key:yyyy-MM-dd   value:qty  计划在库
				Map<String,BigDecimal> dateQtyZKMap = invPlanDateZaiKuMap.get(inv);
				//数据处理 行转列并赋值
				scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyZKMap,"计划在库");
			}
		}

		Page<Record> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		int num = (int) Math.ceil(dataList.size() / 15);
		page.setTotalPage(num);
		page.setTotalRow(dataList.size());
		page.setList(dataList);

		return dataList;
	}


}



















