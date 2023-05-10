package cn.rjtech.admin.momotask;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.JBoltDictionaryService;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.model.momdata.MoMotask;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 制造工单任务 Service
 * @ClassName: MoMotaskService
 * @author: chentao
 * @date: 2023-04-28 15:19
 */
public class MoMotaskService extends BaseService<MoMotask> {

	private final MoMotask dao = new MoMotask().dao();
	@Inject
	private DepartmentService departmentService;
	@Inject
	private MoDocService moDocService;
    @Inject
    private ScheduProductPlanMonthService scheduProductPlanMonthService;

	@Override
	protected MoMotask dao() {
		return dao;
	}
	/**
	 *编辑计划页面列表数据
	 */
	public  Page<Record> getPlanPage(Integer pageNumber, Integer pageSize, Kv kv) {
		MoMotask motask = findById(kv.getStr("iAutoId"));
		Page<Record> moMotaskPage = dbTemplate("modocbatch.getPersonPage",kv).paginate(pageNumber,pageSize);
		//拼日期  weeklist  colnamelist colname2list
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Record record : moMotaskPage.getList()) {
			//根据存货记录查,按日期排序
			Kv data = new Kv();
			data.set("iInventoryId",record.getStr("iInventoryId"));
			data.set("iMoTaskId",motask.getIAutoId());
			List<Record> records = dbTemplate("modocbatch.getDocList", data).find();
			Map<String, List<Record>> recordMap = new HashMap<>();
			for (Record r : records) {
				String date = r.getStr("dplandate");
				if (!recordMap.containsKey(date)) {
					recordMap.put(date, new ArrayList<>());
				}
				recordMap.get(date).add(r);
			}


			//对齐前端拼的数据
			List<String> betweenDate = Util.getBetweenDate(sdf.format(motask.getDBeginDate()), sdf.format(motask.getDEndDate()));
			List<String> planList = new ArrayList<>();
			List<String> docnoList = new ArrayList<>();
			Map<String, Map> map = new HashMap<>();
			for (String date : betweenDate) {
				//todo 三个班次的区分
				if (recordMap.containsKey(date)) {
					// 当日期对应存在记录时，将记录组装到目标格式中
					for (Record re : recordMap.get(date)) {
						docnoList.add(re.getStr("cmodocno"));
						planList.add(re.getStr("iqty"));
						docnoList.add("");
						planList.add("");
						docnoList.add("");
						planList.add("");
					}
				} else {
					// 当日期对应不存在记录时，将空数据组装到目标格式中
					docnoList.add("");
					planList.add("");
					docnoList.add("");
					planList.add("");
					docnoList.add("");
					planList.add("");
				}
			}
			record.set("planList",planList);
			record.set("docnoList",docnoList);
		}
		return moMotaskPage;
	}
	/**
	 *编辑人员页面列表
	 */
	public Page<Record> findDoc(int pageNumber, int pageSize,Long iAutoId) {
        return dbTemplate("modocbatch.getPersonPage",Okv.by("iAutoId",iAutoId)).paginate(pageNumber,pageSize);
	}

	public List<Record> findRoutingConfig(String docId) {
		return dbTemplate("modocbatch.findRoutingConfig",Okv.by("docId",docId)).find();
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv keywords) {
		Page<Record> moMotaskPage = dbTemplate("modocbatch.getPage",keywords).paginate(pageNumber,pageSize);
		List<Dictionary> listByTypeKeayName = new JBoltDictionaryService().getListByTypeKey("motask_audit");
		HashMap<String, String> map = new HashMap<>();
		for (Dictionary dictionary : listByTypeKeayName) {
			map.put(dictionary.getSn(),dictionary.getName());
		}
		for (Record record : moMotaskPage.getList()) {
			String istatus = record.getStr("istatus");
			if (istatus!=null){
				String s = map.get(istatus);
				record.set("istatusname",s);
			}
		}
		return moMotaskPage;
	}

	/**
	 * 保存
	 * @param moMotask
	 * @return
	 */
	public Ret save(MoMotask moMotask) {
		if(moMotask==null || isOk(moMotask.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMotask.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMotask.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName());
		}
		return ret(success);
	}



	/**
	 * 工单计划数据
	 */
	public List<Record> getApsMonthPlanSumPage(int pageNumber, int pageSize, Kv kv) {
		List<Record> scheduProductPlanMonthList = new ArrayList<>();

		String startDate = kv.getStr("startdate");
		String endDate = kv.getStr("enddate");
		if (notOk(startDate) || notOk(endDate)){
			ValidationUtils.isTrue(false,"开始日期-结束日期不能为空！");
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);

		pageSize = pageSize * 15;

		//TODO:根据日期及条件获取月周生产计划表数据三班汇总
		Page<Record> recordPage = dbTemplate("scheduproductplan.getApsMonthPlanSumList",kv).paginate(pageNumber,pageSize);
		List<Record> apsPlanQtyList = recordPage.getList();

		//key:产线id   value:List物料集
		Map<Long,List<String>> workInvListMap = new HashMap<>();
		//key:inv，   value:<yyyy-MM-dd，qty>
		Map<String,Map<String, BigDecimal>> invPlanDateMap = new HashMap<>();
		//key:inv   value:info
		Map<String,Record> invInfoMap = new HashMap<>();
		for (Record record : apsPlanQtyList){
			Long iWorkRegionMid = record.getLong("iWorkRegionMid");
			String cInvCode = record.getStr("cInvCode");
			String iYear = record.getStr("iYear");
			int iMonth = record.getInt("iMonth");
			int iDate = record.getInt("iDate");
			BigDecimal planQty = record.getBigDecimal("iQty3");
			//yyyy-MM-dd
			String dateKey = iYear;
			dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
			dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

			if (workInvListMap.containsKey(iWorkRegionMid)){
				List<String> list = workInvListMap.get(iWorkRegionMid);
				list.add(cInvCode);
			}else {
				List<String> list = new ArrayList<>();
				list.add(cInvCode);
				workInvListMap.put(iWorkRegionMid,list);
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



		//对产线逐个处理
		for (Long key : workInvListMap.keySet()) {
			List<String> recordList = workInvListMap.get(key);
			for (String inv : recordList){
				//inv信息
				Record invInfo = invInfoMap.get(inv);
				//key:yyyy-MM-dd   value:qty
				Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(inv);

				//数据处理 行转列并赋值
				//scheduRowToColumn(scheduProductPlanMonthList,scheduDateList,invInfo,dateQtyMap,null);
			}
		}

		Record planRecord = new Record();
		planRecord.set("cInvCode","物料编码");
		planRecord.set("cInvCode1","存货");
		planRecord.set("cInvName1","部番");
		planRecord.set("cWorkName","产线");
		planRecord.set("qty1",1);
		planRecord.set("qty2",2);
		planRecord.set("qty3",3);
		planRecord.set("qty4",4);
		planRecord.set("qty5",5);
		planRecord.set("qty6",6);
		planRecord.set("qty7",7);
		planRecord.set("qty8",8);
		planRecord.set("qty9",9);
		planRecord.set("qty10",10);


		scheduProductPlanMonthList.add(planRecord);

		Page<Record> page = new Page<>();
		page.setPageNumber(recordPage.getPageNumber());
		page.setPageSize(recordPage.getPageSize() / 15);
		page.setTotalPage(recordPage.getTotalPage());
		page.setTotalRow(recordPage.getTotalRow() /15);
		page.setList(scheduProductPlanMonthList);

		return scheduProductPlanMonthList;
	}






	/**
	 * 更新
	 * @param moMotask
	 * @return
	 */
	public Ret update(MoMotask moMotask) {
		if(moMotask==null || notOk(moMotask.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMotask dbMoMotask=findById(moMotask.getIAutoId());
		if(dbMoMotask==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMotask.getName(), moMotask.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMotask.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName());
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
	 * @param moMotask 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMotask moMotask, Kv kv) {
		//addDeleteSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(),moMotask.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMotask 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMotask moMotask, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMotask, kv);
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
	 * @param moMotask 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MoMotask moMotask,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MoMotask moMotask, String column, Kv kv) {
		//addUpdateSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName(),"的字段["+column+"]值:"+moMotask.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMotask model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoMotask moMotask, Kv kv) {
		//这里用来覆盖 检测MoMotask是否被其它表引用
		return null;
	}



}
