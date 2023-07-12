package cn.rjtech.admin.scheduproductplan;


import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.rjtech.admin.apsweekschedule.ApsWeekscheduleService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApsAnnualplanm;
import cn.rjtech.model.momdata.ApsWeekschedule;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 生产计划排程 Controller
 * @ClassName: ScheduProductPlanMonthController
 * @author: chentao
 * @date: 2023-03-30 11:26
 */
@CheckPermission(PermissionKey.SCHEDUPRODUCTPLANMONTH)
@UnCheckIfSystemAdmin
@Path(value = "/admin/scheduproductplanmonth", viewPath = "/_view/admin/scheduproductplan")
public class ScheduProductPlanMonthController extends BaseAdminController {

    @Inject
    private ScheduProductPlanMonthService service;
    @Inject
    private ApsWeekscheduleService apsWeekscheduleService;

    public void planmonth() {
        render("planmonth.html");
    }
    public void addview() {
        set("cplanorderno",get("cplanorderno"));
        set("icustomerid",get("icustomerid"));
        set("startyear",get("startyear"));
        render("planyear_add.html");
    }

    public void addviewparm() {
        set("startyear", DateUtils.formatDate(new Date(),"yyyy"));
        render("planyearparm.html");
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
        ApsAnnualplanm apsAnnualplanm=service.findById(getLong(0));
        if(apsAnnualplanm == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("apsAnnualplanm",apsAnnualplanm);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ApsAnnualplanm.class, "apsAnnualplanm")));
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


    /**
     * 测试计划
     */
    public void scheduPlanMonthTest() {
        renderJsonData(service.scheduPlanMonthTest());
    }
    //-----------------------------------------------------------------月周生产计划排产-----------------------------------------------

    @UnCheck
    public void getApsWeekscheduleList() {
        renderJsonData(service.getApsWeekscheduleList());
    }

    public void deleteApsWeekschedule() {
        renderJson(service.deleteApsWeekschedule(getKv()));
    }

    /**
     * 作成计划
     */
    public void scheduPlanMonth() {
        //排产层级
        int level = getInt("level");
        //截止日期
        String endDate = get("endDate");
        renderJsonData(service.scheduPlanMonth(level,endDate));
    }


    /**
     * 查看计划
     */
    @UnCheck
    public void getScheduPlanMonthList() {
        renderJson(service.getScheduPlanMonthList(getKv()));
    }

    /**
     * 根据层级获取最近锁定时间
     */
    @UnCheck
    public void getLockDate() {
        //排产层级
        String level = get("level");
        //TODO:获取当前层级上次排产锁定日期
        ApsWeekschedule apsWeekschedule = apsWeekscheduleService.daoTemplate("scheduproductplan.getApsWeekscheduleLock", Kv.by("level",level)).findFirst();
        //上次锁定截止日期
        Date lockPreDate;
        if (apsWeekschedule != null && apsWeekschedule.getDLockEndTime() != null){
            lockPreDate = apsWeekschedule.getDLockEndTime();
        }else {
            lockPreDate = new Date();
        }
        //锁定日期集
        List<Map> list = new ArrayList<>();
        int weekNum = 1; //周次
        for (int i = 1; i <= 35; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lockPreDate);
            calendar.add(Calendar.DATE,i);//日期+1

            String weekDay = DateUtils.formatDate(calendar.getTime(),"E");
            if (weekDay.equals("星期日") || weekDay.equals("Sun")){
                String lockDate = DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd");
                Map<String,Object> map = new HashMap<>();
                map.put("lockDate",lockDate);
                map.put("weekNum","第"+ weekNum ++ +"周");
                list.add(map);
            }
        }
        renderJsonData(list);
    }
    /**
     * 根据层级获取最近解锁时间
     */
    public void getUnLockDate() {
        //排产层级
        String level = get("level");
        //上次锁定截止日期
        Date lockPreDate;
        //TODO:获取当前层级上次排产锁定日期
        ApsWeekschedule apsWeekschedule = apsWeekscheduleService.daoTemplate("scheduproductplan.getApsWeekscheduleLock", Kv.by("level",level)).findFirst();
        if (apsWeekschedule != null && apsWeekschedule.getDLockEndTime() != null){
            lockPreDate = apsWeekschedule.getDLockEndTime();
        }else {
            lockPreDate = new Date();
        }
        //解锁日期集
        List<Map> list = new ArrayList<>();
        int weekNum = 1; //周次
        for (int i = 1; i <= 35; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lockPreDate);
            calendar.add(Calendar.DATE,-i);//日期-1

            String weekDay = DateUtils.formatDate(calendar.getTime(),"E");
            if (weekDay.equals("星期日") || weekDay.equals("Sun")){
                String lockDate = DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd");
                Map<String,Object> map = new HashMap<>();
                map.put("unLockDate",lockDate);
                map.put("weekNum","第"+ weekNum ++ +"周");
                list.add(map);
            }
        }
        renderJsonData(list);
    }

    /**
     * 锁定计划
     */
    public void lockScheduPlan() {
        renderJson(service.lockScheduPlan(getKv()));
    }
    /**
     * 解锁计划
     */
    public void unLockScheduPlan() {
        renderJson(service.unLockScheduPlan(getKv()));
    }

    /**
     * 保存层级
     */
    public void saveLevel() {
        renderJson(service.saveLevel(getKv()));
    }


    /**
     * 获取表格表头日期展示
     */
    public void getTableHead() {
        //排产层级
        String level = get("level");
        //TODO:查询排产开始日期与截止日期
        ApsWeekschedule apsWeekschedule = apsWeekscheduleService.findFirst("SELECT iLevel,dScheduleBeginTime,dScheduleEndTime FROM Aps_WeekSchedule WHERE iLevel = ? ",level);
        String startDate = "";
        String endDate = "";
        if (apsWeekschedule != null){
            startDate = DateUtils.formatDate(new Date(),"yyyy-MM").concat("-01");
            endDate = DateUtils.formatDate(apsWeekschedule.getDScheduleEndTime(),"yyyy-MM-dd");
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

    //-----------------------------------------------------------------月周生产计划汇总-----------------------------------------------

    public void planmonthsum() {
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
        render("planmonthsum.html");
    }

    /**
     * 获取月周生产计划汇总
     */
    public void getApsMonthPlanSumPage() {
        int active = getInt("active");
        if (active == 1){
            renderJsonData(service.getApsMonthPlanSumPage(getPageNumber(),getPageSize(),getKv()));
        }else {
            renderJsonData(service.getApsMonthPeopleSumPage(getPageNumber(),getPageSize(),getKv()));
        }
    }

    /**
     * 导出数据
     */
    @CheckPermission(PermissionKey.PLANMONTHSUM_EXPORT)
    @SuppressWarnings("unchecked")
    public void dataExport() throws Exception {
        String startdate = get("startdate");
        String enddate = get("enddate");
        int active = isOk(get("active")) ? getInt("active") : 1;
        LocalDate localDate = LocalDate.now();
        if (StrUtil.isBlank(startdate)){
            startdate =localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
        }
        if (StrUtil.isBlank(enddate)){
            enddate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
        }
        //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
        List<String> scheduDateList = DateUtils.getBetweenDate(startdate,enddate);

        String fileName;
        if (active == 1){
            fileName = "月周生产计划汇总";
        }else {
            fileName = "月周人数汇总";
        }
        JBoltExcel jBoltExcel = JBoltExcel.create();
        JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create(fileName);

        List<JBoltExcelHeader> headerList = new ArrayList<>();
        headerList.add(JBoltExcelHeader.create("cworkname","产线名称"));
        headerList.add(JBoltExcelHeader.create("cinvcode","存货编码"));
        headerList.add(JBoltExcelHeader.create("cinvcode1","客户部番"));
        headerList.add(JBoltExcelHeader.create("cinvname1","部品名称"));

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

        List<Record> recordList;
        if (active == 1){
            recordList = service.getApsMonthPlanSumPage(1,15,getKv());
        }else {
            recordList = service.getApsMonthPeopleSumPage(1,15,getKv());
        }
        jBoltExcelSheet.setRecordDatas(2,recordList);
        jBoltExcel.addSheet(jBoltExcelSheet);

        jBoltExcel.setFileName(fileName+DateUtils.getDate("yyyy-MM-dd"));
        renderBytesToExcelXlsFile(jBoltExcel);
    }


    //-----------------------------------------------------------------生产计划及实绩管理-----------------------------------------------

    public void planandactualview() {
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
        render("planandactual.html");
    }

    /**
     * 获取生产计划及实绩管理
     */
    public void getApsPlanAndActualPage() {
        renderJsonData(service.getApsPlanAndActualPage(getPageNumber(),getPageSize(),getKv()));
    }

    /**
     * 导出数据
     */
    @CheckPermission(PermissionKey.PLANANDACTUALVIEW_EXPORT)
    @SuppressWarnings("unchecked")
    public void dataExportActual() throws Exception {
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

        String fileName = "生产计划及实绩";
        JBoltExcel jBoltExcel = JBoltExcel.create();
        JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create(fileName);

        List<JBoltExcelHeader> headerList = new ArrayList<>();
        headerList.add(JBoltExcelHeader.create("cworkname","产线名称"));
        headerList.add(JBoltExcelHeader.create("cinvcode","存货编码"));
        headerList.add(JBoltExcelHeader.create("cinvcode1","客户部番"));
        headerList.add(JBoltExcelHeader.create("cinvname1","部品名称"));
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

        List<Record> recordList = service.getApsPlanAndActualPage(1,15,getKv());
        jBoltExcelSheet.setRecordDatas(2,recordList);
        jBoltExcel.addSheet(jBoltExcelSheet);

        jBoltExcel.setFileName(fileName+DateUtils.getDate("yyyy-MM-dd"));
        renderBytesToExcelXlsFile(jBoltExcel);
    }







    /**
     * 锁定计划
     */
    public void locking() {
        //renderJson(service.lockingPlan(getKv()));
    }

    /**
     * 取消锁定计划
     */
    public void lockingCancel() {
        //renderJson(service.lockingCancelPlan(getKv()));
    }

    /**
     * 获取最新BOM
     */
    public void refreshBOM() {
        //renderJson(service.refreshBOM(getKv()));
    }

    /**
     *计划选择
     */
    @UnCheck
    public void selectaprm() {
        set("month",get("month"));
        render("selectaprm.html");
    }

    public void monthweekSchedule() {
        render("month_week_schedule.html");
    }

}
