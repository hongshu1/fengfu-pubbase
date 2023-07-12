package cn.rjtech.admin.schedudemandplan;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MrpDemandcomputem;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 物料需求计划 Controller
 * @ClassName: MrpDemandcomputemAdminController
 * @author: chentao
 * @date: 2023-05-02 10:00
 */
@CheckPermission(PermissionKey.DEMAND_ALGORITHM)
@UnCheckIfSystemAdmin
@Path(value = "/admin/schedudemandplan", viewPath = "/_view/admin/schedudemandplan")
public class ScheduDemandPlanController extends BaseAdminController {

	@Inject
	private ScheduDemandPlanService service;

	@Inject
	private ScheduProductPlanMonthService scheduProductPlanMonthService;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
  	
  	/**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		MrpDemandcomputem mrpDemandcomputem=service.findById(getLong(0)); 
		if(mrpDemandcomputem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("mrpDemandcomputem",mrpDemandcomputem);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MrpDemandcomputem.class, "mrpDemandcomputem")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MrpDemandcomputem.class, "mrpDemandcomputem")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

	//-----------------------------------------------------------------物料需求计划计算-----------------------------------------------

	public void demandalgorithm() {
		render("demandalgorithm.html");
	}

	/**
	 * 物料需求计划计算
	 */
	public void apsScheduDemandPlan() {
		String endDate = getKv().getStr("endDate");
		renderJsonData(service.apsScheduDemandPlan(endDate));
	}

    @UnCheck
	public void getDemandList() {
		renderJsonData(service.getDemandList(getKv()));
	}

    @UnCheck
	public void getSupplierList() {
		renderJsonData(service.getSupplierList(getKv()));
	}

	public void getSwitchDate() {
		Calendar calendarMonth = Calendar.getInstance();
		calendarMonth.setTime(new Date());
		calendarMonth.add(Calendar.MONTH, 2);//月份+1
		//下一个月份
		renderJsonData(DateUtils.formatDate(calendarMonth.getTime(),"yyyy-MM-dd"));
	}
	/**
	 * 获取表格表头日期展示
	 */
	public void getTableHead() {
		//TODO:查询排产开始日期与截止日期
		Record mRecord = service.findFirstRecord("SELECT dBeginDate,dEndDate FROM Mrp_DemandComputeM WHERE iAutoId = 1 ");
		String startDate = "";
		String endDate = "";
		if (mRecord != null){
			//startDate = DateUtils.formatDate(apsWeekschedule.getDScheduleBeginTime(),"yyyy-MM-dd");
			endDate = DateUtils.formatDate(mRecord.get("dEndDate"),"yyyy-MM-dd");
		}

		startDate = isOk(get("startDate")) ? get("startDate") : startDate;
		endDate = isOk(get("endDate")) ? get("endDate") : endDate;
		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startDate)){
			startDate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(endDate)){
			endDate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}

		List<Record> monthlist = new ArrayList<>();
		List<String> namelist = new ArrayList<>();
		List<String> weeklist = new ArrayList<>();
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);
		//页面顶部colspan列  key:2023年1月  value:colspan="13"
		Map<String,Integer> yearMonthMap = new HashMap<>();
		for (String s : scheduDateList) {
			String year = s.substring(0, 4);
			int month = Integer.parseInt(s.substring(5, 7));
			String yearMonth = year + "年" + month + "月";
			if (yearMonthMap.containsKey(yearMonth)) {
				int count = yearMonthMap.get(yearMonth);
				yearMonthMap.put(yearMonth, count + 1);
			} else {
				yearMonthMap.put(yearMonth, 1);
			}
		}

		List<String> name2listStr = new ArrayList<>();
		for (int i = 0; i < scheduDateList.size(); i++) {
			String year = scheduDateList.get(i).substring(0,4);
			int month = Integer.parseInt(scheduDateList.get(i).substring(5,7));
			String yearMonth = year + "年" + month + "月";
			if (!name2listStr.contains(yearMonth)){
				name2listStr.add(yearMonth);
				Record record = new Record();
				record.set("colname",yearMonth);
				record.set("colsum",yearMonthMap.get(yearMonth));
				monthlist.add(record);
			}

			String weekDay = DateUtils.formatDate(DateUtils.parseDate(scheduDateList.get(i)),"E");
			String weekType = "";
			if (weekDay.equals("星期一") || weekDay.equals("Mon")){weekType = "Mon";}
			if (weekDay.equals("星期二") || weekDay.equals("Tue")){weekType = "Tue";}
			if (weekDay.equals("星期三") || weekDay.equals("Wed")){weekType = "Wed";}
			if (weekDay.equals("星期四") || weekDay.equals("Thu")){weekType = "Thu";}
			if (weekDay.equals("星期五") || weekDay.equals("Fri")){weekType = "Fri";}
			if (weekDay.equals("星期六") || weekDay.equals("Sat")){weekType = "Sat";}
			if (weekDay.equals("星期日") || weekDay.equals("Sun")){weekType = "Sun";}

			int day = Integer.parseInt(scheduDateList.get(i).substring(8));

			namelist.add(day+"日");
			weeklist.add(weekType);
		}

		Map<String,Object> map = new HashMap<>();
		map.put("month",monthlist);
		map.put("day",namelist);
		map.put("week",weeklist);

		renderJsonData(map);
	}

	/**
	 * 预示保存
	 */
	public void saveForetell() {
		String dataStr = get("startdate");
		renderJson(service.saveForetell(getKv()));
	}
	/**
	 * 到货保存
	 */
	public void saveArrival() {
		String dataStr = get("startdate");
		//JSONArray dataJSONArr = JSONArray.parseArray(dataStr);
		renderJson(service.saveArrival(getKv()));
	}

	//-----------------------------------------------------------------物料需求计划预示-----------------------------------------------

	public void demandforecastm() {
		render("demandforecastm.html");
	}

    @UnCheck
	public void getMrpDemandForecastMPage() {
		renderJsonData(service.getMrpDemandForecastMList(getPageNumber(),getPageSize(),getKv()));
	}

	@CheckPermission(PermissionKey.DEMANDFORECASTM_DELETE)
	public void deleteDemandforecastm() {
		renderJson(service.deleteDemandforecastm(getLong(0)));
	}

	@CheckPermission(PermissionKey.DEMANDFORECASTM_SHOW)
	public void demandforecastd() {
		String idemandforecastmid = get("idemandforecastmid");
		String startdate = get("startdate");
		String enddate = get("enddate");

		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}
		set("idemandforecastmid",idemandforecastmid);
		set("startdate",startdate);
		set("enddate",enddate);

		List<String> collist = new ArrayList<>();
		List<String> namelist = new ArrayList<>();
		List<String> weeklist = new ArrayList<>();
		List<Record> name2list = new ArrayList<>();
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startdate,enddate);
		//页面顶部colspan列  key:2023年1月  value:colspan="13"
		Map<String,Integer> yearMonthMap = new HashMap<>();
		for (String s : scheduDateList) {
			String year = s.substring(0, 4);
			int month = Integer.parseInt(s.substring(5, 7));
			String yearMonth = year + "年" + month + "月";
			if (yearMonthMap.containsKey(yearMonth)) {
				int count = yearMonthMap.get(yearMonth);
				yearMonthMap.put(yearMonth, count + 1);
			} else {
				yearMonthMap.put(yearMonth, 2);
			}
		}

		int monthCount = 1;
		List<String> name2listStr = new ArrayList<>();
		for (int i = 0; i < scheduDateList.size(); i++) {
			String year = scheduDateList.get(i).substring(0,4);
			int month = Integer.parseInt(scheduDateList.get(i).substring(5,7));
			String yearMonth = year + "年" + month + "月";
			if (!name2listStr.contains(yearMonth)){
				name2listStr.add(yearMonth);
				Record record = new Record();
				record.set("colname",yearMonth);
				record.set("colsum",yearMonthMap.get(yearMonth));
				name2list.add(record);
			}

			String weekDay = DateUtils.formatDate(DateUtils.parseDate(scheduDateList.get(i)),"E");
			String weekType = "";
			if (weekDay.equals("星期一") || weekDay.equals("Mon")){weekType = "Mon";}
			if (weekDay.equals("星期二") || weekDay.equals("Tue")){weekType = "Tue";}
			if (weekDay.equals("星期三") || weekDay.equals("Wed")){weekType = "Wed";}
			if (weekDay.equals("星期四") || weekDay.equals("Thu")){weekType = "Thu";}
			if (weekDay.equals("星期五") || weekDay.equals("Fri")){weekType = "Fri";}
			if (weekDay.equals("星期六") || weekDay.equals("Sat")){weekType = "Sat";}
			if (weekDay.equals("星期日") || weekDay.equals("Sun")){weekType = "Sun";}

			int seq = i + 1;
			int day = Integer.parseInt(scheduDateList.get(i).substring(8));
			if (i != 0 && day == 1){
				collist.add("qtysum"+monthCount);
				collist.add("qty"+seq);

				namelist.add("合计");
				namelist.add(day+"日");

				weeklist.add(" ");
				weeklist.add(weekType);
				monthCount ++;
				continue;
			}
			if (seq == scheduDateList.size()){
				collist.add("qty"+seq);
				collist.add("qtysum"+monthCount);

				namelist.add(day+"日");
				namelist.add("合计");

				weeklist.add(weekType);
				weeklist.add(" ");
				continue;
			}
			collist.add("qty"+seq);
			namelist.add(day+"日");
			weeklist.add(weekType);
		}

		set("collist", collist);
		set("colnamelist", namelist);
		set("weeklist", weeklist);
		set("colname2list", name2list);
		render("demandforecastd.html");
	}


	/**
	 * 物料需求计划预示
	 */
    @UnCheck
	public void getMrpDemandForecastDPage() {
		renderJsonData(service.getMrpDemandForecastDList(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 导出数据
	 */
	@CheckPermission(PermissionKey.DEMANDFORECASTM_EXPORT)
	@SuppressWarnings("unchecked")
	public void dataExport() throws Exception {
		String startdate = get("startdate");
		String enddate = get("enddate");
		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = DateUtils.getBetweenDate(startdate,enddate);

		String fileName = "物料需求计划预示";
		JBoltExcel jBoltExcel = JBoltExcel.create();
		JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create(fileName);

		List<JBoltExcelHeader> headerList = new ArrayList<>();
		headerList.add(JBoltExcelHeader.create("cworkname","供应商名称"));
		headerList.add(JBoltExcelHeader.create("cinvcode","存货编码"));
		headerList.add(JBoltExcelHeader.create("cinvcode1","客户部番"));
		headerList.add(JBoltExcelHeader.create("cinvname1","部品名称"));
		headerList.add(JBoltExcelHeader.create("ipkgqty","包装数量"));
		headerList.add(JBoltExcelHeader.create("iinnerinstockdays","标准在库天数"));
		headerList.add(JBoltExcelHeader.create("colname","项目"));

		String preYearMonth = "";
		int monthCount = 1;
		for (int i = 0; i < scheduDateList.size(); i++) {
			String date = scheduDateList.get(i);
			String yearMonth = date.substring(0,7);

			int seq = i + 1;
			int day = Integer.parseInt(date.substring(8));
			if (i != 0 && day == 1){
				headerList.add(JBoltExcelHeader.create("qtysum"+monthCount,preYearMonth.concat("合计")));
				headerList.add(JBoltExcelHeader.create("qty"+seq,date));
				monthCount ++;
				continue;
			}
			if (seq == scheduDateList.size()){
				headerList.add(JBoltExcelHeader.create("qty"+seq,date));
				headerList.add(JBoltExcelHeader.create("qtysum"+monthCount,yearMonth.concat("合计")));
				continue;
			}
			headerList.add(JBoltExcelHeader.create("qty"+seq,date));
			preYearMonth = yearMonth;
		}
		jBoltExcelSheet.setMerges().setHeaders(1,headerList);

		List<Record> recordList = service.getMrpDemandForecastDList(1,15,getKv());
		jBoltExcelSheet.setRecordDatas(2,recordList);
		jBoltExcel.addSheet(jBoltExcelSheet);

		jBoltExcel.setFileName(fileName+DateUtils.getDate("yyyy-MM-dd"));
		renderBytesToExcelXlsFile(jBoltExcel);
	}

	//-----------------------------------------------------------------物料到货计划-----------------------------------------------

	public void demandplanm() {
		render("demandplanm.html");
	}

    @UnCheck
	public void getMrpDemandPlanMPage() {
		renderJsonData(service.getMrpDemandPlanMList(getPageNumber(),getPageSize(),getKv()));
	}

	@CheckPermission(PermissionKey.DEMANDPLANM_DELETE)
	public void deleteDemandplanm() {
		renderJson(service.deleteDemandplanm(getLong(0)));
	}

	@CheckPermission(PermissionKey.DEMANDPLANM_SHOW)
	public void demandpland() {
		String idemandplanmid = get("idemandplanmid");
		String startdate = get("startdate");
		String enddate = get("enddate");

		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}
		set("idemandplanmid",idemandplanmid);
		set("startdate",startdate);
		set("enddate",enddate);

		List<String> collist = new ArrayList<>();
		List<String> namelist = new ArrayList<>();
		List<String> weeklist = new ArrayList<>();
		List<Record> name2list = new ArrayList<>();
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startdate,enddate);
		//页面顶部colspan列  key:2023年1月  value:colspan="13"
		Map<String,Integer> yearMonthMap = new HashMap<>();
		for (String s : scheduDateList) {
			String year = s.substring(0, 4);
			int month = Integer.parseInt(s.substring(5, 7));
			String yearMonth = year + "年" + month + "月";
			if (yearMonthMap.containsKey(yearMonth)) {
				int count = yearMonthMap.get(yearMonth);
				yearMonthMap.put(yearMonth, count + 1);
			} else {
				yearMonthMap.put(yearMonth, 2);
			}
		}

		int monthCount = 1;
		List<String> name2listStr = new ArrayList<>();
		for (int i = 0; i < scheduDateList.size(); i++) {
			String year = scheduDateList.get(i).substring(0,4);
			int month = Integer.parseInt(scheduDateList.get(i).substring(5,7));
			String yearMonth = year + "年" + month + "月";
			if (!name2listStr.contains(yearMonth)){
				name2listStr.add(yearMonth);
				Record record = new Record();
				record.set("colname",yearMonth);
				record.set("colsum",yearMonthMap.get(yearMonth));
				name2list.add(record);
			}

			String weekDay = DateUtils.formatDate(DateUtils.parseDate(scheduDateList.get(i)),"E");
			String weekType = "";
			if (weekDay.equals("星期一") || weekDay.equals("Mon")){weekType = "Mon";}
			if (weekDay.equals("星期二") || weekDay.equals("Tue")){weekType = "Tue";}
			if (weekDay.equals("星期三") || weekDay.equals("Wed")){weekType = "Wed";}
			if (weekDay.equals("星期四") || weekDay.equals("Thu")){weekType = "Thu";}
			if (weekDay.equals("星期五") || weekDay.equals("Fri")){weekType = "Fri";}
			if (weekDay.equals("星期六") || weekDay.equals("Sat")){weekType = "Sat";}
			if (weekDay.equals("星期日") || weekDay.equals("Sun")){weekType = "Sun";}

			int seq = i + 1;
			int day = Integer.parseInt(scheduDateList.get(i).substring(8));
			if (i != 0 && day == 1){
				collist.add("qtysum"+monthCount);
				collist.add("qty"+seq);

				namelist.add("合计");
				namelist.add(day+"日");

				weeklist.add(" ");
				weeklist.add(weekType);
				monthCount ++;
				continue;
			}
			if (seq == scheduDateList.size()){
				collist.add("qty"+seq);
				collist.add("qtysum"+monthCount);

				namelist.add(day+"日");
				namelist.add("合计");

				weeklist.add(weekType);
				weeklist.add(" ");
				continue;
			}
			collist.add("qty"+seq);
			namelist.add(day+"日");
			weeklist.add(weekType);
		}

		set("collist", collist);
		set("colnamelist", namelist);
		set("weeklist", weeklist);
		set("colname2list", name2list);
		render("demandpland.html");
	}


	/**
	 * 物料到货计划
	 */
	public void getMrpDemandPlanDPage() {
		renderJsonData(service.getMrpDemandPlanDList(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 导出数据
	 */
	@SuppressWarnings("unchecked")
	public void dataExportPlan() throws Exception {
		String startdate = get("startdate");
		String enddate = get("enddate");
		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = DateUtils.getBetweenDate(startdate,enddate);

		String fileName = "物料到货计划";
		JBoltExcel jBoltExcel = JBoltExcel.create();
		JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create(fileName);

		List<JBoltExcelHeader> headerList = new ArrayList<>();
		headerList.add(JBoltExcelHeader.create("cworkname","供应商名称"));
		headerList.add(JBoltExcelHeader.create("cinvcode","存货编码"));
		headerList.add(JBoltExcelHeader.create("cinvcode1","客户部番"));
		headerList.add(JBoltExcelHeader.create("cinvname1","部品名称"));
		headerList.add(JBoltExcelHeader.create("ipkgqty","包装数量"));
		headerList.add(JBoltExcelHeader.create("iinnerinstockdays","标准在库天数"));
		headerList.add(JBoltExcelHeader.create("colname","项目"));

		String preYearMonth = "";
		int monthCount = 1;
		for (int i = 0; i < scheduDateList.size(); i++) {
			String date = scheduDateList.get(i);
			String yearMonth = date.substring(0,7);

			int seq = i + 1;
			int day = Integer.parseInt(date.substring(8));
			if (i != 0 && day == 1){
				headerList.add(JBoltExcelHeader.create("qtysum"+monthCount,preYearMonth.concat("合计")));
				headerList.add(JBoltExcelHeader.create("qty"+seq,date));
				monthCount ++;
				continue;
			}
			if (seq == scheduDateList.size()){
				headerList.add(JBoltExcelHeader.create("qty"+seq,date));
				headerList.add(JBoltExcelHeader.create("qtysum"+monthCount,yearMonth.concat("合计")));
				continue;
			}
			headerList.add(JBoltExcelHeader.create("qty"+seq,date));
			preYearMonth = yearMonth;
		}
		jBoltExcelSheet.setMerges().setHeaders(1,headerList);

		List<Record> recordList = service.getMrpDemandPlanDList(1,15,getKv());
		jBoltExcelSheet.setRecordDatas(2,recordList);
		jBoltExcel.addSheet(jBoltExcelSheet);

		jBoltExcel.setFileName(fileName+DateUtils.getDate("yyyy-MM-dd"));
		renderBytesToExcelXlsFile(jBoltExcel);
	}

	//-----------------------------------------------------------------物料需求计划汇总-----------------------------------------------
	public void demandplansumview() {
		String startdate = get("startdate");
		String enddate = get("enddate");
		String cworkname = get("cworkname");
		String cinvcode = get("cinvcode");
		String cinvcode1 = get("cinvcode1");
		String cinvname1 = get("cinvname1");
		String active = get("active");

		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}

		set("startdate",startdate);
		set("enddate",enddate);
		set("cworkname",cworkname);
		set("cinvcode",cinvcode);
		set("cinvcode1",cinvcode1);
		set("cinvname1",cinvname1);
		set("active", isOk(active) ? active : 1);

		List<String> collist = new ArrayList<>();
		List<String> namelist = new ArrayList<>();
		List<String> weeklist = new ArrayList<>();
		List<Record> name2list = new ArrayList<>();
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = Util.getBetweenDate(startdate,enddate);
		//页面顶部colspan列  key:2023年1月  value:colspan="13"
		Map<String,Integer> yearMonthMap = new HashMap<>();
		for (int i = 0; i < scheduDateList.size(); i++) {
			String year = scheduDateList.get(i).substring(0,4);
			int month = Integer.parseInt(scheduDateList.get(i).substring(5,7));
			String yearMonth = year + "年" + month + "月";
			if (yearMonthMap.containsKey(yearMonth)){
				int count = yearMonthMap.get(yearMonth);
				yearMonthMap.put(yearMonth,count + 1);
			}else {
				yearMonthMap.put(yearMonth,2);
			}
		}

		int monthCount = 1;
		List<String> name2listStr = new ArrayList<>();
		for (int i = 0; i < scheduDateList.size(); i++) {
			String year = scheduDateList.get(i).substring(0,4);
			int month = Integer.parseInt(scheduDateList.get(i).substring(5,7));
			String yearMonth = year + "年" + month + "月";
			if (!name2listStr.contains(yearMonth)){
				name2listStr.add(yearMonth);
				Record record = new Record();
				record.set("colname",yearMonth);
				record.set("colsum",yearMonthMap.get(yearMonth));
				name2list.add(record);
			}

			String weekDay = DateUtils.formatDate(DateUtils.parseDate(scheduDateList.get(i)),"E");
			String weekType = "";
			if (weekDay.equals("星期一") || weekDay.equals("Mon")){weekType = "Mon";}
			if (weekDay.equals("星期二") || weekDay.equals("Tue")){weekType = "Tue";}
			if (weekDay.equals("星期三") || weekDay.equals("Wed")){weekType = "Wed";}
			if (weekDay.equals("星期四") || weekDay.equals("Thu")){weekType = "Thu";}
			if (weekDay.equals("星期五") || weekDay.equals("Fri")){weekType = "Fri";}
			if (weekDay.equals("星期六") || weekDay.equals("Sat")){weekType = "Sat";}
			if (weekDay.equals("星期日") || weekDay.equals("Sun")){weekType = "Sun";}

			int seq = i + 1;
			int day = Integer.parseInt(scheduDateList.get(i).substring(8));
			if (i != 0 && day == 1){
				collist.add("qtysum"+monthCount);
				collist.add("qty"+seq);

				namelist.add("合计");
				namelist.add(day+"日");

				weeklist.add(" ");
				weeklist.add(weekType);
				monthCount ++;
				continue;
			}
			if (seq == scheduDateList.size()){
				collist.add("qty"+seq);
				collist.add("qtysum"+monthCount);

				namelist.add(day+"日");
				namelist.add("合计");

				weeklist.add(weekType);
				weeklist.add(" ");
				continue;
			}
			collist.add("qty"+seq);
			namelist.add(day+"日");
			weeklist.add(weekType);
		}

		set("collist", collist);
		set("colnamelist", namelist);
		set("weeklist", weeklist);
		set("colname2list", name2list);
		render("demandplansum.html");
	}

	/**
	 * 获取生产计划及实绩管理
	 */
	public void getDemandPlanSumPage() {
		renderJsonData(service.getDemandPlanSumPage(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 导出数据
	 */
	@CheckPermission(PermissionKey.DEMANDPLANSUMVIEW_EXPORT)
	@SuppressWarnings("unchecked")
	public void dataExportSum() throws Exception {
		String startdate = get("startdate");
		String enddate = get("enddate");
		LocalDate localDate = LocalDate.now();
		if (StrUtil.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StrUtil.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}
		//排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
		List<String> scheduDateList = DateUtils.getBetweenDate(startdate,enddate);

		String fileName = "物料需求计划汇总";
		JBoltExcel jBoltExcel = JBoltExcel.create();
		JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create(fileName);

		List<JBoltExcelHeader> headerList = new ArrayList<>();
		headerList.add(JBoltExcelHeader.create("cworkname","供应商名称"));
		headerList.add(JBoltExcelHeader.create("cinvcode","存货编码"));
		headerList.add(JBoltExcelHeader.create("cinvcode1","客户部番"));
		headerList.add(JBoltExcelHeader.create("cinvname1","部品名称"));
		headerList.add(JBoltExcelHeader.create("ipkgqty","包装数量"));
		headerList.add(JBoltExcelHeader.create("iinnerinstockdays","标准在库天数"));
		headerList.add(JBoltExcelHeader.create("colname","项目"));

		String preYearMonth = "";
		int monthCount = 1;
		for (int i = 0; i < scheduDateList.size(); i++) {
			String date = scheduDateList.get(i);
			String yearMonth = date.substring(0,7);

			int seq = i + 1;
			int day = Integer.parseInt(date.substring(8));
			if (i != 0 && day == 1){
				headerList.add(JBoltExcelHeader.create("qtysum"+monthCount,preYearMonth.concat("合计")));
				headerList.add(JBoltExcelHeader.create("qty"+seq,date));
				monthCount ++;
				continue;
			}
			if (seq == scheduDateList.size()){
				headerList.add(JBoltExcelHeader.create("qty"+seq,date));
				headerList.add(JBoltExcelHeader.create("qtysum"+monthCount,yearMonth.concat("合计")));
				continue;
			}
			headerList.add(JBoltExcelHeader.create("qty"+seq,date));
			preYearMonth = yearMonth;
		}
		jBoltExcelSheet.setMerges().setHeaders(1,headerList);

		List<Record> recordList = service.getDemandPlanSumPage(1,15,getKv());
		jBoltExcelSheet.setRecordDatas(2,recordList);
		jBoltExcel.addSheet(jBoltExcelSheet);

		jBoltExcel.setFileName(fileName+DateUtils.getDate("yyyy-MM-dd"));
		renderBytesToExcelXlsFile(jBoltExcel);
	}
}
