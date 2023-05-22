package cn.rjtech.admin.schedudemandplan;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MrpDemandcomputem;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料需求计划 Controller
 * @ClassName: MrpDemandcomputemAdminController
 * @author: chentao
 * @date: 2023-05-02 10:00
 */
@CheckPermission(PermissionKey.NONE)
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

	public void apsScheduDemandPlan() {
		String endDate = getKv().getStr("endDate");
		renderJsonData(service.apsScheduDemandPlan(endDate));
	}

	public void getDemandList() {
		renderJsonData(service.getDemandList(getKv()));
	}

	public void getSupplierList() {
		renderJsonData(service.getSupplierList(getKv()));
	}

	//-----------------------------------------------------------------物料需求计划预示-----------------------------------------------

	public void demandforecastm() {
		render("demandforecastm.html");
	}

	public void getMrpDemandForecastMPage() {
		renderJsonData(service.getMrpDemandForecastMList(getPageNumber(),getPageSize(),getKv()));
	}

	public void deleteDemandforecastm() {
		renderJson(service.deleteDemandforecastm(getLong(0)));
	}

	public void demandforecastd() {
		String idemandforecastmid = get("idemandforecastmid");
		String startdate = get("startdate");
		String enddate = get("enddate");

		LocalDate localDate = LocalDate.now();
		if (StringUtils.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StringUtils.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}
		set("idemandforecastmid",idemandforecastmid);
		set("startdate",startdate);
		set("enddate",enddate);

		List<String> collist = new ArrayList<>();
		List<String> namelist = new ArrayList<>();
		List<String> weeklist = new ArrayList<>();
		List<Record> name2list = new ArrayList<>();
		if (StringUtils.isNotBlank(startdate) && StringUtils.isNotBlank(enddate)){
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
				if (weekDay.equals("星期一")){weekType = "Mon";}
				if (weekDay.equals("星期二")){weekType = "Tue";}
				if (weekDay.equals("星期三")){weekType = "Wed";}
				if (weekDay.equals("星期四")){weekType = "Thu";}
				if (weekDay.equals("星期五")){weekType = "Fri";}
				if (weekDay.equals("星期六")){weekType = "Sat";}
				if (weekDay.equals("星期日")){weekType = "Sun";}

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
	public void getMrpDemandForecastDPage() {
		renderJsonData(service.getMrpDemandForecastDList(getPageNumber(),getPageSize(),getKv()));
	}


	//-----------------------------------------------------------------物料到货计划-----------------------------------------------

	public void demandplanm() {
		render("demandplanm.html");
	}

	public void getMrpDemandPlanMPage() {
		renderJsonData(service.getMrpDemandPlanMList(getPageNumber(),getPageSize(),getKv()));
	}

	public void deleteDemandplanm() {
		renderJson(service.deleteDemandplanm(getLong(0)));
	}

	public void demandpland() {
		String idemandplanmid = get("idemandplanmid");
		String startdate = get("startdate");
		String enddate = get("enddate");

		LocalDate localDate = LocalDate.now();
		if (StringUtils.isBlank(startdate)){
			startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		}
		if (StringUtils.isBlank(enddate)){
			enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		}
		set("idemandplanmid",idemandplanmid);
		set("startdate",startdate);
		set("enddate",enddate);

		List<String> collist = new ArrayList<>();
		List<String> namelist = new ArrayList<>();
		List<String> weeklist = new ArrayList<>();
		List<Record> name2list = new ArrayList<>();
		if (StringUtils.isNotBlank(startdate) && StringUtils.isNotBlank(enddate)){
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
				if (weekDay.equals("星期一")){weekType = "Mon";}
				if (weekDay.equals("星期二")){weekType = "Tue";}
				if (weekDay.equals("星期三")){weekType = "Wed";}
				if (weekDay.equals("星期四")){weekType = "Thu";}
				if (weekDay.equals("星期五")){weekType = "Fri";}
				if (weekDay.equals("星期六")){weekType = "Sat";}
				if (weekDay.equals("星期日")){weekType = "Sun";}

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


	//-----------------------------------------------------------------物料需求计划汇总-----------------------------------------------
}
