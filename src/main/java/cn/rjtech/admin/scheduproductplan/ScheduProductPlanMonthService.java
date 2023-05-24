package cn.rjtech.admin.scheduproductplan;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.apsannualpland.ApsAnnualplandService;
import cn.rjtech.admin.apsannualplandqty.ApsAnnualplandQtyService;
import cn.rjtech.admin.apsweekschedule.ApsWeekscheduleService;
import cn.rjtech.admin.apsweekscheduledetails.ApsWeekscheduledetailsService;
import cn.rjtech.admin.apsweekscheduledqty.ApsWeekscheduledQtyService;
import cn.rjtech.admin.calendar.CalendarService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momotask.MoMotaskService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.service.func.u9.DateQueryInvTotalFuncService;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
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
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.hutool.core.text.StrPool.COMMA;


/**
 * 生产计划排程 Service
 * @ClassName: ScheduProductPlanYearService
 * @author: chentao
 * @date: 2023-03-30 11:26
 */
public class ScheduProductPlanMonthService extends BaseService<ApsAnnualplanm> {

    private final ApsAnnualplanm dao = new ApsAnnualplanm().dao();

    @Override
    protected ApsAnnualplanm dao() {
        return dao;
    }
    
    @Inject
    private CalendarService calendarService;
    @Inject
    private DateQueryInvTotalFuncService dateQueryInvTotalFuncService;
    @Inject
    private ScheduProductPlanMonthService scheduProductPlanMonthService;
    @Inject
    private MomDataFuncService momDataFuncService;
    @Inject
    private ApsAnnualplandService apsAnnualplandService;
    @Inject
    private ApsAnnualplandQtyService apsAnnualplandQtyService;


    @Inject
    private ApsWeekscheduleService apsWeekscheduleService;
    @Inject
    private ApsWeekscheduledetailsService apsWeekscheduledetailsService;
    @Inject
    private ApsWeekscheduledQtyService apsWeekscheduledQtyService;
    @Inject
    private MoMotaskService motaskService;
    @Inject
    private MoDocService moDocService;


    /**
     * 后台管理分页查询
     * @param pageNumber
     * @param pageSize
     * @param keywords
     * @return
     */
    public Page<ApsAnnualplanm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     * @param apsAnnualplanm
     * @return
     */
    public Ret save(ApsAnnualplanm apsAnnualplanm) {
        if(apsAnnualplanm==null || isOk(apsAnnualplanm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(apsAnnualplanm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success=apsAnnualplanm.save();
        if(success) {
            //添加日志
            //addSaveSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplanm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     * @param apsAnnualplanm
     * @return
     */
    public Ret update(ApsAnnualplanm apsAnnualplanm) {
        if(apsAnnualplanm==null || notOk(apsAnnualplanm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        ApsAnnualplanm dbApsAnnualplanm=findById(apsAnnualplanm.getIAutoId());
        if(dbApsAnnualplanm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
        //if(existsName(apsAnnualplanm.getName(), apsAnnualplanm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success=apsAnnualplanm.update();
        if(success) {
            //添加日志
            //addUpdateSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplanm.getName());
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
     * @param apsAnnualplanm 要删除的model
     * @param kv 携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(ApsAnnualplanm apsAnnualplanm, Kv kv) {
        //addDeleteSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(),apsAnnualplanm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     * @param apsAnnualplanm 要删除的model
     * @param kv 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanDelete(ApsAnnualplanm apsAnnualplanm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(apsAnnualplanm, kv);
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
     * @param apsAnnualplanm 要toggle的model
     * @param column 操作的哪一列
     * @param kv 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanToggle(ApsAnnualplanm apsAnnualplanm,String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(ApsAnnualplanm apsAnnualplanm, String column, Kv kv) {
        //addUpdateSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplanm.getName(),"的字段["+column+"]值:"+apsAnnualplanm.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     * @param apsAnnualplanm model
     * @param kv 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(ApsAnnualplanm apsAnnualplanm, Kv kv) {
        //这里用来覆盖 检测ApsAnnualplanm是否被其它表引用
        return null;
    }


    public String getEndYear(String startYear){
        Calendar calendar = Calendar.getInstance();
        Date date = DateUtils.parseDate(startYear);
        calendar.setTime(date);
        //第二个年份
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        calendar.add(Calendar.YEAR,1);
        String endYear = sdf.format(calendar.getTime());
        return endYear;
    }

    //-----------------------------------------------------------------月周生产计划排产-----------------------------------------------

    /**
     * 测试
     * @param kv
     * @return
     */
    public synchronized List<ScheduProductYearViewDTO> scheduPlanMonthTest(Kv kv) {
        //年度生产计划集合
        List<ScheduProductYearViewDTO> scheduProductPlanYearList = new ArrayList<>();
        try {

            //根据产线进行循环排程


            //同一产线的物料
            String[] product_type = {"a", "b", "c", "d"};

            //期初库存
            Map<String, Integer> inventory_original = new HashMap<>();
            inventory_original.put("a", 1719);
            inventory_original.put("b", 1814);
            inventory_original.put("c", 2541);
            inventory_original.put("d", 857);

            //物料月每日需求数
            Map<String, int[]> plan = new HashMap<>();
            int plan_a[] = {598, 600, 0, 0, 0, 420, 420, 330, 596, 0, 420, 360, 330, 590, 600, 0, 0, 0, 420, 540, 360, 360, 0, 0, 240, 330, 420, 550, 390, 0, 0}; // 客户需求量
            int plan_b[] = {0, 800, 0, 0, 0, 270, 288, 342, 341, 0, 0, 300, 420, 120, 360, 0, 0, 240, 120, 360, 240, 120, 240, 0, 300, 300, 540, 300, 240, 0, 0};
            int plan_c[] = {300, 600, 0, 0, 0, 570, 739, 881, 570, 0, 570, 570, 450, 450, 540, 0, 0, 360, 450, 120, 240, 570, 300, 0, 460, 420, 120, 120, 600, 0, 0};
            int plan_d[] = {74, 66, 0, 0, 0, 156, 0, 0, 134, 0, 300, 0, 0, 154, 0, 0, 0, 300, 0, 0, 136, 0, 0, 0, 300, 0, 0, 0, 0, 0, 0};

            plan.put("a", plan_a);
            plan.put("b", plan_b);
            plan.put("c", plan_c);
            plan.put("d", plan_d);

            //下月平均计划需求数
            Map<String, Integer> plan_nextMonthAverage = new HashMap<>();
            plan_nextMonthAverage.put("a", 633);
            plan_nextMonthAverage.put("b", 376);
            plan_nextMonthAverage.put("c", 586);
            plan_nextMonthAverage.put("d", 57);

            //各物料的各班次产量数据
            Map<String, int[]> capability = new HashMap<>();
            int[] capability_a = {450, 450, 329}; // 各型号的各班次的产量数据
            int[] capability_b = {420, 420, 309};
            int[] capability_c = {450, 450, 329};
            int[] capability_d = {369, 369, 267};
            capability.put("a", capability_a);
            capability.put("b", capability_b);
            capability.put("c", capability_c);
            capability.put("d", capability_d);

            //月星期几
            Weekday[] workday = {Weekday.fri, Weekday.sat, Weekday.sun, Weekday.mon, Weekday.tue, Weekday.wed, Weekday.thu, Weekday.fri,
                    Weekday.sat, Weekday.sun, Weekday.mon, Weekday.tue, Weekday.wed, Weekday.thu, Weekday.fri, Weekday.sat, Weekday.sun,
                    Weekday.mon, Weekday.tue, Weekday.wed, Weekday.thu, Weekday.fri, Weekday.sat, Weekday.sun, Weekday.mon, Weekday.tue,
                    Weekday.wed, Weekday.thu, Weekday.fri, Weekday.sat, Weekday.sun};


            ApsScheduling apsScheduling = ApsUtil.apsCalculation(product_type, inventory_original, plan, 2, plan_nextMonthAverage, capability, workday, 0.7, 0.3);

            String[] productInformationByShift0 = new String[workday.length];
            int[] productNumberByShift0 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift0, productNumberByShift0, 0);
            System.out.println("早班："+ Arrays.toString(productInformationByShift0));
            System.out.println("早班："+ Arrays.toString(productNumberByShift0));
            System.out.println();

            String[] productInformationByShift1 = new String[workday.length];
            int[] productNumberByShift1 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift1, productNumberByShift1, 1);
            System.out.println("中班："+ Arrays.toString(productInformationByShift1));
            System.out.println("中班："+ Arrays.toString(productNumberByShift1));
            System.out.println();

            String[] productInformationByShift2 = new String[workday.length];
            int[] productNumberByShift2 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift2, productNumberByShift2, 2);
            System.out.println("加班："+ Arrays.toString(productInformationByShift2));
            System.out.println("加班："+ Arrays.toString(productNumberByShift2));
            System.out.println();

            String[] productInformationByShift3 = new String[workday.length];
            int[] productNumberByShift3 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift3, productNumberByShift3, 3);
            System.out.println("晚班："+ Arrays.toString(productInformationByShift3));
            System.out.println("晚班："+ Arrays.toString(productNumberByShift3));

        }catch (Exception e){
            throw new RuntimeException("排程计划出错！"+e.getMessage());
        }
        return scheduProductPlanYearList;
    }




    /**
     *
     * 作成计划弹框显示排产纪录
     */
    public List<Record> getApsWeekscheduleList() {
        return dbTemplate("scheduproductplan.getApsWeekscheduleList").find();
    }


    /**
     * 月周度生产计划逻辑处理
     * @param level 排产层级
     * @param endDateStr 截止时间yyyy-MM-dd
     * @return
     */
    public synchronized Ret scheduPlanMonth(Integer level,String endDateStr) {
        //TODO:获取当前层级上次排产截止日期+1
        ApsWeekschedule apsWeekschedule = apsWeekscheduleService.daoTemplate("scheduproductplan.getApsWeekschedule",Kv.by("level",level)).findFirst();
        //排产开始年月日
        Date startDate;
        if (apsWeekschedule != null){
            Date dScheduleEndTime = apsWeekschedule.getDScheduleEndTime();
            Calendar calendar = Calendar.getInstance();
            //Date date = cn.rjtech.util.DateUtils.parseDate(dScheduleEndTime);//yyyy-MM-dd
            calendar.setTime(dScheduleEndTime);
            calendar.add(Calendar.DATE,1);//日期+1
            startDate = DateUtils.parseDate(DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd"));
        }else {
            startDate = DateUtils.parseDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
        }
        String startDateStr = DateUtils.formatDate(startDate,"yyyy-MM-dd");
        //排产截止年月日
        Date endDate = DateUtils.parseDate(endDateStr);
        if (startDate.getTime() >= endDate.getTime()){
            return fail("截止日期不能小于上次排产截止日期");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE,-1);//日期-1
        //过去一天年月日
        Date lastDate = DateUtils.parseDate(DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd"));
        int lastyear = Integer.parseInt(DateUtils.formatDate(lastDate,"yyyy"));
        int lastmonth = Integer.parseInt(DateUtils.formatDate(lastDate,"MM"));
        int lastday = Integer.parseInt(DateUtils.formatDate(lastDate,"dd"));

        Calendar calendarMonth = Calendar.getInstance();
        calendarMonth.setTime(endDate);
        calendarMonth.add(Calendar.MONTH,1);//月份+1
        //下一个年月日
        Date nextDate = DateUtils.parseDate(DateUtils.formatDate(calendarMonth.getTime(),"yyyy-MM-dd"));
        int nextyear = Integer.parseInt(DateUtils.formatDate(nextDate,"yyyy"));
        int nextmonth = Integer.parseInt(DateUtils.formatDate(nextDate,"MM"));
        int nextMonthDaySum = DateUtils.getMonthHasDays(nextDate);

        //排产开始日期到截止日期之间的天数 包含开始到结束那天
        int scheduDayNum = ((int) DateUtils.getDistanceOfTwoDate(startDate,endDate)) + 1;
        //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
        List<String> scheduDateList = Util.getBetweenDate(DateUtils.formatDate(startDate,"yyyy-MM-dd"),endDateStr);

        //排产日历类型
        String calendarType = "1";
        //库存率因子
        BigDecimal aps_inventoryRate = getConfigValue(Kv.by("configkey","aps_inventoryRate"));
        Double inventoryRate = aps_inventoryRate != null ? aps_inventoryRate.doubleValue() : 0.7;
        //加班因子
        BigDecimal aps_workFactor = getConfigValue(Kv.by("configkey","aps_workFactor"));
        Double workFactor = aps_workFactor != null ? aps_workFactor.doubleValue() : 0.3;
        Long organizeId = getOrgId();


        //TODO:根据层级查询本次排产物料集信息
        List<Record> invInfoByLevelList ;
        if (level == 1){
            invInfoByLevelList = getInvInfoByLevelList(Okv.by("ipslevel",level).set("isaletype",1));
        }else {
            invInfoByLevelList = getInvInfoByLevelList(Okv.by("ipslevel",level));
        }
        //根据产线作key依次进行排产
        //key:产线id   value:List物料集
        Map<Long,List<String>> workInvListMap = new HashMap<>();
        //key:inv   value:info
        Map<String,Record> invInfoMap = new HashMap<>();
        //本次排产物料id集
        String idsJoin = "(";
        for (Record record : invInfoByLevelList){
            Long iWorkRegionMid = record.getLong("iWorkRegionMid");
            String cInvCode = record.getStr("cInvCode");
            Long invId = record.getLong("invId");
            if (workInvListMap.containsKey(iWorkRegionMid)){
                List<String> list = workInvListMap.get(iWorkRegionMid);
                if (!list.contains(cInvCode)){
                    list.add(cInvCode);
                }
            }else {
                List<String> list = new ArrayList<>();
                list.add(cInvCode);
                workInvListMap.put(iWorkRegionMid,list);
            }
            invInfoMap.put(cInvCode,record);
            idsJoin = idsJoin + invId + ",";
        }
        idsJoin = idsJoin + "601)";

        //TODO: 根据日历类型字典查询工作日历集合
        List<String> calendarList = getCalendarDateList(organizeId,calendarType,DateUtils.formatDate(startDate,"yyyy-MM-dd"),endDateStr);
        //初始化工作日历
        Weekday[] workday = new Weekday[scheduDayNum];
        for (int i = 0; i < scheduDateList.size(); i++) {
            String scheduDate = scheduDateList.get(i);
            String weekDay = DateUtils.formatDate(DateUtils.parseDate(scheduDate),"E");
            if (weekDay.equals("星期一")){workday[i] = Weekday.mon;}
            if (weekDay.equals("星期二")){workday[i] = Weekday.tue;}
            if (weekDay.equals("星期三")){workday[i] = Weekday.wed;}
            if (weekDay.equals("星期四")){workday[i] = Weekday.thu;}
            if (weekDay.equals("星期五")){workday[i] = Weekday.fri;}
            if (weekDay.equals("星期六")){workday[i] = Weekday.sat;}
            if (weekDay.equals("星期日")){workday[i] = Weekday.sun;}
        }
        //TODO:根据物料集查询各班次产能
        List<Record> invCapacityList = dbTemplate("scheduproductplan.getInvCapacityList",Kv.by("ids",idsJoin)).find();
        //key:inv    value:list
        Map<String,List<Record>> invCapacityListMap = new HashMap<>();
        for (Record record : invCapacityList){
            String cInvCode = record.getStr("cInvCode");
            if (invCapacityListMap.containsKey(cInvCode)){
                List<Record> list = invCapacityListMap.get(cInvCode);
                list.add(record);
                invCapacityListMap.put(cInvCode,list);
            }else {
                List<Record> list = new ArrayList<>();
                list.add(record);
                invCapacityListMap.put(cInvCode,list);
            }
        }
        //TODO:查询物料集期初在库
        List<Record> getLastDateZKQtyList = getLastDateZKQtyList(Kv.by("lastyear",lastyear).set("lastmonth",lastmonth).set("lastday",lastday).set("ids",idsJoin));
        //key:inv   value:qty
        Map<String,Integer> lastDateZKQtyMap = new HashMap<>();
        for (Record record : getLastDateZKQtyList){
            String cInvCode = record.getStr("cInvCode");
            int iQty5 = record.getBigDecimal("iQty5").intValue();
            lastDateZKQtyMap.put(cInvCode,iQty5);
        }
        //TODO:根据物料集id及年月查询年度生产计划（用于计算下一月平均需求数）
        List<Record> getYearMonthQtySumByinvList = getYearMonthQtySumByinvList(Kv.by("nextyear",nextyear).set("nextmonth",nextmonth).set("ids",idsJoin));
        //key:inv   value:qty
        Map<String,Integer> nextMonthAvgQtyByinvMap = new HashMap<>();
        for (Record record : getYearMonthQtySumByinvList){
            String cInvCode = record.getStr("cInvCode");
            int iQty = record.getInt("iQty");
            int avgQty = iQty / nextMonthDaySum;
            nextMonthAvgQtyByinvMap.put(cInvCode,avgQty);
        }


        //TODO:根据物料集id及日期获取客户计划汇总表数据
        List<Record> getCusOrderSumList = getCusOrderSumList(Okv.by("ids",idsJoin).set("startdate",startDateStr).set("enddate",endDateStr));
        //key:inv，   value:<yyyy-MM-dd，qty>     1、来源于客户计划汇总中内作销售类型产品的计划使用
        Map<String,Map<String,BigDecimal>> invPlanDateMap = new HashMap<>();
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

            if (invPlanDateMap.containsKey(cInvCode)){
                //key:yyyy-MM-dd   value:qty
                Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(cInvCode);
                dateQtyMap.put(dateKey,planQty);
            }else {
                Map<String,BigDecimal> dateQtyMap = new HashMap<>();
                dateQtyMap.put(dateKey,planQty);
                invPlanDateMap.put(cInvCode,dateQtyMap);
            }
        }
        //key:inv，   value:<yyyy-MM-dd，qty>       2、上一层母件的计划数量*QTY汇总值
        Map<String,Map<String,BigDecimal>> invPlanListWeekMap = new HashMap<>();

        //顶层（来源于客户计划汇总中内作销售类型产品的计划使用）
        if (level == 1){}
        //下层（1.来源于客户汇总表中的计划使用数+2.上一层母件的计划数量*QTY汇总值）
        else {
            //TODO:根据物料集id查询父级物料信息及用量
            List<Record> pinvInfoByinvList = getPinvInfoByinvList(Okv.by("ids",idsJoin));
            //key:inv   value:List父级物料信息及用量
            Map<String,List<Record>> pinvListByinvMap = new HashMap<>();
            //本次排产物料的父级id集
            String pidsJoin = "(";
            List<Long> idList = new ArrayList<>();
            for (Record record : pinvInfoByinvList){
                String invCode = record.getStr("invCode");
                Long pinvId = record.getLong("pinvId");
                if (pinvListByinvMap.containsKey(invCode)){
                    List<Record> list = pinvListByinvMap.get(invCode);
                    list.add(record);
                }else {
                    List<Record> list = new ArrayList<>();
                    list.add(record);
                    pinvListByinvMap.put(invCode,list);
                }
                if (!idList.contains(pinvId)){
                    pidsJoin = pidsJoin + pinvId + ",";
                    idList.add(pinvId);
                }
            }
            pidsJoin = pidsJoin + "601)";

            //TODO:根据父物料集id及日期获取月周生产计划表数据
            List<Record> getWeekScheduSumList = getWeekScheduSumList(Okv.by("ids",pidsJoin).set("startdate",startDateStr).set("enddate",endDateStr));
            //key:pinv，   value:List<多个月计划数>
            Map<String,List<Record>> pinvPlanListMap = new HashMap<>();
            for (Record record : getWeekScheduSumList){
                String cInvCode = record.getStr("cInvCode");
                if (pinvPlanListMap.containsKey(cInvCode)){
                    List<Record> list = pinvPlanListMap.get(cInvCode);
                    list.add(record);
                }else {
                    List<Record> list = new ArrayList<>();
                    list.add(record);
                    pinvPlanListMap.put(cInvCode,list);
                }
            }
            //将多个父级物料计划数*用量比相加
            for (String inv : pinvListByinvMap.keySet()){
                //纪录相同日期的计划数(多个父物料计划*用量相加)  key:yyyy-MM-dd   value:qty
                Map<String,BigDecimal> dateQtyMap = new HashMap<>();

                for (Record record : pinvListByinvMap.get(inv)){
                    String pinvCode = record.getStr("pinvCode");
                    BigDecimal useRate = record.getBigDecimal("iQty");
                    //父级计划
                    List<Record> pinvPlanList = pinvPlanListMap.get(pinvCode) != null ? pinvPlanListMap.get(pinvCode) : new ArrayList<>();
                    for (Record precord : pinvPlanList){
                        String iYear = precord.getStr("iYear");
                        int iMonth = precord.getInt("iMonth");
                        int iDate = precord.getInt("iDate");
                        BigDecimal iQty3 = precord.getBigDecimal("iQty3");
                        BigDecimal planQty = iQty3.multiply(useRate);//计划*用量
                        //yyyy-MM-dd
                        String dateKey = iYear;
                        dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
                        dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;
                        if (dateQtyMap.containsKey(dateKey)){
                            BigDecimal sumQty = dateQtyMap.get(dateKey).add(planQty);
                            dateQtyMap.put(dateKey,sumQty);
                        }else {
                            dateQtyMap.put(dateKey,planQty);
                        }
                    }
                }
                invPlanListWeekMap.put(inv,dateQtyMap);
            }
        }

        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date newDate = new Date();
        String orgCode = getOrgCode();
        String orgName = getOrgName();
        Long orgId = getOrgId();
        Long iWeekScheduleId = JBoltSnowflakeKit.me.nextId();

        //排产主表
        ApsWeekschedule weekschedule = new ApsWeekschedule();
        weekschedule.setIOrgId(orgId);
        weekschedule.setCOrgCode(orgCode);
        weekschedule.setCOrgName(orgName);
        weekschedule.setICreateBy(userId);
        weekschedule.setCCreateName(userName);
        weekschedule.setDCreateTime(newDate);
        weekschedule.setIUpdateBy(userId);
        weekschedule.setCUpdateName(userName);
        weekschedule.setDUpdateTime(newDate);
        weekschedule.setIAutoId(iWeekScheduleId);
        weekschedule.setILevel(level);
        weekschedule.setDScheduleBeginTime(startDate);
        weekschedule.setDScheduleEndTime(endDate);
        weekschedule.setIsLocked(false);

        //排产物料明细表
        List<ApsWeekscheduledetails> detailsList = new ArrayList<>();
        //排产数量明细表
        List<ApsWeekscheduledQty> detailsQtyList = new ArrayList<>();

        int seq = 1;
        //循环产线
        for (Long WorkIdKey : workInvListMap.keySet()){
            //物料集
            List<String> invList = workInvListMap.get(WorkIdKey);

            //纪录排产之后1S的计划数
            Map<String, int[]> invPlanMap1S = new HashMap<>();
            //纪录排产之后2S的计划数
            Map<String, int[]> invPlanMap2S = new HashMap<>();
            //纪录排产之后3S的计划数
            Map<String, int[]> invPlanMap3S = new HashMap<>();

            //初始化需排产物料
            String[] invArrar = invList.toArray(new String[0]);
            //各物料期初库存  key:inv
            Map<String, Integer> inventory_originalMap = new HashMap<>();
            //各物料的各班次产量数据  key:inv
            Map<String, int[]> capabilityMap = new HashMap<>();
            //各物料月每日需求数
            Map<String, int[]> planMap = new HashMap<>();
            //各物料下月平均计划需求数  key:inv
            Map<String, Integer> plan_nextMonthAverageMap = new HashMap<>();

            //循环物料
            for (String inv : invList){
                invPlanMap1S.put(inv, new int[scheduDayNum]);
                invPlanMap2S.put(inv, new int[scheduDayNum]);
                invPlanMap3S.put(inv, new int[scheduDayNum]);

                //每日需求数
                int[] planArrar = new int[scheduDayNum];
                //key:yyyy-MM-dd   value:qty
                Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(inv) != null ? invPlanDateMap.get(inv) : new HashMap<>();
                //key:yyyy-MM-dd   value:qty
                Map<String,BigDecimal> dateQtyWeekMap = invPlanListWeekMap.get(inv) != null ? invPlanListWeekMap.get(inv) : new HashMap<>();
                for (int i = 0; i < scheduDateList.size(); i++) {
                    String scheduDate = scheduDateList.get(i);
                    int planQty = dateQtyMap.get(scheduDate) != null ? dateQtyMap.get(scheduDate).intValue() : 0;
                    int planWeekQty = dateQtyWeekMap.get(scheduDate) != null ? dateQtyWeekMap.get(scheduDate).intValue() : 0;
                    planArrar[i] = planQty + planWeekQty;
                }
                planMap.put(inv,planArrar);

                //期初库存
                int stockQty = lastDateZKQtyMap.get(inv) != null ? lastDateZKQtyMap.get(inv) : 0;
                inventory_originalMap.put(inv, stockQty);

                //各班次产量数据
                int[] capabilityArrar = new int[3];
                List<Record> capacityList = invCapacityListMap.get(inv) != null ? invCapacityListMap.get(inv) : new ArrayList<>();
                for (int i = 0; i < capacityList.size(); i++) {
                    String cWorkShiftCode = capacityList.get(i).getStr("capacityList");
                    int iCapacity = capacityList.get(i).getInt("iCapacity");
                    if (cWorkShiftCode.contains("1S")){
                        capabilityArrar[0] = iCapacity;
                    }
                    if (cWorkShiftCode.contains("2S")){
                        capabilityArrar[1] = iCapacity;
                    }
                    if (cWorkShiftCode.contains("3S")){
                        capabilityArrar[2] = iCapacity;
                    }
                }
                capabilityMap.put(inv, capabilityArrar);

                //下月平均计划需求数
                int averageQty = nextMonthAvgQtyByinvMap.get(inv) != null ? nextMonthAvgQtyByinvMap.get(inv) : 0;
                plan_nextMonthAverageMap.put(inv, averageQty);
            }
            //进行APS算法分析
            ApsScheduling apsScheduling = ApsUtil.apsCalculation(invArrar, inventory_originalMap, planMap, 2, plan_nextMonthAverageMap, capabilityMap, workday, inventoryRate, workFactor);

            //1S
            String[] productInformationByShift0 = new String[workday.length];
            int[] productNumberByShift0 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift0, productNumberByShift0, 0);
            //System.out.println("早班："+ Arrays.toString(productInformationByShift0));
            //System.out.println("早班："+ Arrays.toString(productNumberByShift0));
            getInvPlanMap(productInformationByShift0,productNumberByShift0,invPlanMap1S);
            System.out.println();

            //2S
            String[] productInformationByShift1 = new String[workday.length];
            int[] productNumberByShift1 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift1, productNumberByShift1, 1);
            //System.out.println("中班："+ Arrays.toString(productInformationByShift1));
            //System.out.println("中班："+ Arrays.toString(productNumberByShift1));
            getInvPlanMap(productInformationByShift1,productNumberByShift1,invPlanMap2S);
            System.out.println();

            String[] productInformationByShift2 = new String[workday.length];
            int[] productNumberByShift2 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift2, productNumberByShift2, 2);
            System.out.println("加班："+ Arrays.toString(productInformationByShift2));
            System.out.println("加班："+ Arrays.toString(productNumberByShift2));
            System.out.println();

            //3S
            String[] productInformationByShift3 = new String[workday.length];
            int[] productNumberByShift3 = new int[workday.length];
            apsScheduling.getProductInfo(productInformationByShift3, productNumberByShift3, 3);
            //System.out.println("晚班："+ Arrays.toString(productInformationByShift3));
            //System.out.println("晚班："+ Arrays.toString(productNumberByShift3));
            getInvPlanMap(productInformationByShift3,productNumberByShift3,invPlanMap3S);


            //循环物料
            for (String inv : invList){
                Record info = invInfoMap.get(inv);

                Long iWeekScheduleDid = JBoltSnowflakeKit.me.nextId();
                ApsWeekscheduledetails scheduleDetails = new ApsWeekscheduledetails();
                scheduleDetails.setIOrgId(orgId);
                scheduleDetails.setCOrgCode(orgCode);
                scheduleDetails.setCOrgName(orgName);
                scheduleDetails.setICreateBy(userId);
                scheduleDetails.setCCreateName(userName);
                scheduleDetails.setDCreateTime(newDate);
                scheduleDetails.setIUpdateBy(userId);
                scheduleDetails.setCUpdateName(userName);
                scheduleDetails.setDUpdateTime(newDate);
                scheduleDetails.setIAutoId(iWeekScheduleDid);
                scheduleDetails.setIWeekScheduleId(iWeekScheduleId);
                scheduleDetails.setILevel(level);
                scheduleDetails.setIInventoryId(info.getLong("invId"));
                detailsList.add(scheduleDetails);

                int[] invPlan = planMap.get(inv);
                int[] invPlan1S = invPlanMap1S.get(inv);
                int[] invPlan2S = invPlanMap2S.get(inv);
                int[] invPlan3S = invPlanMap3S.get(inv);
                BigDecimal qiChuZaiKu = BigDecimal.valueOf(inventory_originalMap.get(inv));

                for (int i = 0; i < scheduDateList.size(); i++) {
                    String date = scheduDateList.get(i);
                    int year = Integer.parseInt(date.substring(0,4));
                    int month = Integer.parseInt(date.substring(5,7));
                    int day = Integer.parseInt(date.substring(8,10));

                    BigDecimal xuQiu = BigDecimal.valueOf(invPlan[i]);
                    BigDecimal one = BigDecimal.valueOf(invPlan1S[i]);
                    BigDecimal two = BigDecimal.valueOf(invPlan2S[i]);
                    BigDecimal three = BigDecimal.valueOf(invPlan3S[i]);
                    BigDecimal zaiKu = qiChuZaiKu.add(one).add(two).add(three).subtract(xuQiu);

                    ApsWeekscheduledQty scheduledQty = new ApsWeekscheduledQty();
                    scheduledQty.setIWeekScheduleDid(iWeekScheduleDid);
                    scheduledQty.setIYear(year);
                    scheduledQty.setIMonth(month);
                    scheduledQty.setIDate(day);
                    scheduledQty.setIQty1(xuQiu);
                    scheduledQty.setIQty2(one);
                    scheduledQty.setIQty3(two);
                    scheduledQty.setIQty4(three);
                    scheduledQty.setIQty5(zaiKu);
                    scheduledQty.setIQty6(BigDecimal.valueOf(0));
                    detailsQtyList.add(scheduledQty);
                    qiChuZaiKu = zaiKu;
                }
            }
        }
        tx(() -> {
            weekschedule.save();
            apsWeekscheduledetailsService.batchSave(detailsList);
            if (detailsQtyList.size() > 0){
                List<List<ApsWeekscheduledQty>> groupCusOrderSumList = CollectionUtils.partition(detailsQtyList,300);
                CountDownLatch countDownLatch = new CountDownLatch(groupCusOrderSumList.size());
                ExecutorService executorService = Executors.newFixedThreadPool(groupCusOrderSumList.size());
                for(List<ApsWeekscheduledQty> cusOrderSums :groupCusOrderSumList){
                    executorService.execute(()->{
                        apsWeekscheduledQtyService.batchSave(cusOrderSums);
                    });
                    countDownLatch.countDown();
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                executorService.shutdown();
            }
            return true;
        });

        /*try {

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();



            //同一产线的物料
            String[] product_type = {"a", "b", "c", "d"};

            //期初库存
            Map<String, Integer> inventory_original = new HashMap<>();
            inventory_original.put("a", 1719);
            inventory_original.put("b", 1814);
            inventory_original.put("c", 2541);
            inventory_original.put("d", 857);

            //物料月每日需求数
            Map<String, int[]> plan = new HashMap<>();
            int plan_a[] = {598, 600, 0, 0, 0, 420, 420, 330, 596, 0, 420, 360, 330, 590, 600, 0, 0, 0, 420, 540, 360, 360, 0, 0, 240, 330, 420, 550, 390, 0, 0}; // 客户需求量
            int plan_b[] = {0, 800, 0, 0, 0, 270, 288, 342, 341, 0, 0, 300, 420, 120, 360, 0, 0, 240, 120, 360, 240, 120, 240, 0, 300, 300, 540, 300, 240, 0, 0};
            int plan_c[] = {300, 600, 0, 0, 0, 570, 739, 881, 570, 0, 570, 570, 450, 450, 540, 0, 0, 360, 450, 120, 240, 570, 300, 0, 460, 420, 120, 120, 600, 0, 0};
            int plan_d[] = {74, 66, 0, 0, 0, 156, 0, 0, 134, 0, 300, 0, 0, 154, 0, 0, 0, 300, 0, 0, 136, 0, 0, 0, 300, 0, 0, 0, 0, 0, 0};

            plan.put("a", plan_a);
            plan.put("b", plan_b);
            plan.put("c", plan_c);
            plan.put("d", plan_d);

            //下月平均计划需求数
            Map<String, Integer> plan_nextMonthAverage = new HashMap<>();
            plan_nextMonthAverage.put("a", 633);
            plan_nextMonthAverage.put("b", 376);
            plan_nextMonthAverage.put("c", 586);
            plan_nextMonthAverage.put("d", 57);

            //各物料的各班次产量数据
            Map<String, int[]> capability = new HashMap<>();
            int[] capability_a = {450, 450, 329}; // 各型号的各班次的产量数据
            int[] capability_b = {420, 420, 309};
            int[] capability_c = {450, 450, 329};
            int[] capability_d = {369, 369, 267};
            capability.put("a", capability_a);
            capability.put("b", capability_b);
            capability.put("c", capability_c);
            capability.put("d", capability_d);

            //月星期几
            Weekday[] workdays = {Weekday.fri, Weekday.sat, Weekday.sun, Weekday.mon, Weekday.tue, Weekday.wed, Weekday.thu, Weekday.fri,
                    Weekday.sat, Weekday.sun, Weekday.mon, Weekday.tue, Weekday.wed, Weekday.thu, Weekday.fri, Weekday.sat, Weekday.sun,
                    Weekday.mon, Weekday.tue, Weekday.wed, Weekday.thu, Weekday.fri, Weekday.sat, Weekday.sun, Weekday.mon, Weekday.tue,
                    Weekday.wed, Weekday.thu, Weekday.fri, Weekday.sat, Weekday.sun};


            ApsScheduling apsScheduling = ApsUtil.apsCalculation(product_type, inventory_original, plan, 2, plan_nextMonthAverage, capability, workdays, 0.7, 0.3);

            Map<String, int[]> invPlanMap = new HashMap<>();
            invPlanMap.put("a", new int[31]);
            invPlanMap.put("b", new int[31]);
            invPlanMap.put("c", new int[31]);
            invPlanMap.put("d", new int[31]);

            String[] productInformationByShift0 = new String[workdays.length];
            int[] productNumberByShift0 = new int[workdays.length];
            apsScheduling.getProductInfo(productInformationByShift0, productNumberByShift0, 0);
            System.out.println("早班："+ Arrays.toString(productInformationByShift0));
            System.out.println("早班："+ Arrays.toString(productNumberByShift0));
            getInvPlanMap(productInformationByShift0,productNumberByShift0,invPlanMap);
            System.out.println("早班计划a："+ Arrays.toString(invPlanMap.get("a")));
            System.out.println("早班计划b："+ Arrays.toString(invPlanMap.get("b")));
            System.out.println("早班计划c："+ Arrays.toString(invPlanMap.get("c")));
            System.out.println("早班计划d："+ Arrays.toString(invPlanMap.get("d")));
            System.out.println();

            String[] productInformationByShift1 = new String[workdays.length];
            int[] productNumberByShift1 = new int[workdays.length];
            apsScheduling.getProductInfo(productInformationByShift1, productNumberByShift1, 1);
            System.out.println("中班："+ Arrays.toString(productInformationByShift1));
            System.out.println("中班："+ Arrays.toString(productNumberByShift1));
            System.out.println();

            String[] productInformationByShift2 = new String[workdays.length];
            int[] productNumberByShift2 = new int[workdays.length];
            apsScheduling.getProductInfo(productInformationByShift2, productNumberByShift2, 2);
            System.out.println("加班："+ Arrays.toString(productInformationByShift2));
            System.out.println("加班："+ Arrays.toString(productNumberByShift2));
            System.out.println();

            String[] productInformationByShift3 = new String[workdays.length];
            int[] productNumberByShift3 = new int[workdays.length];
            apsScheduling.getProductInfo(productInformationByShift3, productNumberByShift3, 3);
            System.out.println("晚班："+ Arrays.toString(productInformationByShift3));
            System.out.println("晚班："+ Arrays.toString(productNumberByShift3));


        }catch (Exception e){
            throw new RuntimeException("排程计划出错！"+e.getMessage());
        }*/
        return SUCCESS;
    }
    public void getInvPlanMap(String[] productInformationByShift,int[] productNumberByShift,Map<String, int[]> invPlanMap){
        for (int i = 0; i < productInformationByShift.length; i++) {
            String inv = productInformationByShift[i];
            if (inv.equals("z")){
                continue;
            }
            int qty = productNumberByShift[i];
            int[] invPlan = invPlanMap.get(inv);
            invPlan[i] = qty;
        }
    }




    /**
     * 获取计划
     */
    public List<Map<String,Object>> getScheduPlanMonthList(Kv kv) {
        //排产纪录id
        Long iWeekScheduleId = kv.getLong("iWeekScheduleId");
        if (notOk(iWeekScheduleId)){
            return null;
        }
        //TODO:查询排产开始日期与截止日期
        ApsWeekschedule apsWeekschedule = apsWeekscheduleService.findFirst("SELECT iLevel,dScheduleBeginTime,dScheduleEndTime FROM Aps_WeekSchedule WHERE iAutoId = ? ",iWeekScheduleId);
        int iLevel = apsWeekschedule.getILevel();
        String startDate = DateUtils.formatDate(apsWeekschedule.getDScheduleBeginTime(),"yyyy-MM-dd");
        String endDate = DateUtils.formatDate(apsWeekschedule.getDScheduleEndTime(),"yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate(startDate));
        calendar.add(Calendar.DATE,-1);//日期-1
        //过去一天年月日
        Date lastDate = DateUtils.parseDate(DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd"));
        int lastyear = Integer.parseInt(DateUtils.formatDate(lastDate,"yyyy"));
        int lastmonth = Integer.parseInt(DateUtils.formatDate(lastDate,"MM"));
        int lastday = Integer.parseInt(DateUtils.formatDate(lastDate,"dd"));

        kv.set("level",iLevel).set("startdate",startDate).set("enddate",endDate);
        //TODO:根据层级及日期获取月周生产计划表数据
        List<Record> getWeekScheduPlanList = getWeekScheduPlanList(kv);

        //key:产线id   value:List物料集
        Map<Long,List<String>> workInvListMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Record>
        Map<String,Map<String,Record>> invPlanDateMap = new HashMap<>();
        //key:inv   value:invInfo
        Map<String,Record> invInfoMap = new HashMap<>();
        //本次排产物料id集
        String idsJoin = "(";
        List<Long> idList = new ArrayList<>();
        for (Record record : getWeekScheduPlanList){
            Long iWorkRegionMid = record.getLong("iWorkRegionMid");
            Long invId = record.getLong("invId");
            String cInvCode = record.getStr("cInvCode");
            String iYear = record.getStr("iYear");
            int iMonth = record.getInt("iMonth");
            int iDate = record.getInt("iDate");
            //yyyy-MM-dd
            String dateKey = iYear;
            dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
            dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

            if (workInvListMap.containsKey(iWorkRegionMid)){
                List<String> list = workInvListMap.get(iWorkRegionMid);
                if (!list.contains(cInvCode)){
                    list.add(cInvCode);
                }
            }else {
                List<String> list = new ArrayList<>();
                list.add(cInvCode);
                workInvListMap.put(iWorkRegionMid,list);
            }

            if (invPlanDateMap.containsKey(cInvCode)){
                //key:yyyy-MM-dd   value:qty
                Map<String,Record> dateQtyMap = invPlanDateMap.get(cInvCode);
                dateQtyMap.put(dateKey,record);
            }else {
                Map<String,Record> dateQtyMap = new LinkedHashMap<>();
                dateQtyMap.put(dateKey,record);
                invPlanDateMap.put(cInvCode,dateQtyMap);
            }

            if (!invInfoMap.containsKey(cInvCode)) {
                invInfoMap.put(cInvCode,record);
            }

            if (!idList.contains(invId)){
                idsJoin = idsJoin + invId + ",";
                idList.add(invId);
            }
        }
        idsJoin = idsJoin + "601)";


        //TODO:查询物料集期初在库
        List<Record> getLastDateZKQtyList = getLastDateZKQtyList(Kv.by("lastyear",lastyear).set("lastmonth",lastmonth).set("lastday",lastday).set("ids",idsJoin));
        //key:inv   value:qty
        Map<String,Integer> lastDateZKQtyMap = new HashMap<>();
        for (Record record : getLastDateZKQtyList){
            String cInvCode = record.getStr("cInvCode");
            int iQty5 = record.getBigDecimal("iQty5").intValue();
            lastDateZKQtyMap.put(cInvCode,iQty5);
        }

        //上次锁定截止日期
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(Calendar.DATE,-1);//日期-1
        Date lockPreDate = calendar2.getTime();
        //TODO:获取当前层级上次排产锁定日期
        ApsWeekschedule weekscheduleLock = apsWeekscheduleService.daoTemplate("scheduproductplan.getApsWeekscheduleLock", Kv.by("level",iLevel)).findFirst();
        if (weekscheduleLock != null && weekscheduleLock.getDLockEndTime() != null){
            Date preLock = DateUtils.parseDate(DateUtils.formatDate(weekscheduleLock.getDLockEndTime(),"yyyy-MM-dd"));
            if (preLock.getTime() >= DateUtils.parseDate(DateUtils.formatDate(new Date(),"yyyy-MM-d")).getTime()){
                lockPreDate = weekscheduleLock.getDLockEndTime();
            }
        }
        lockPreDate = DateUtils.parseDate(DateUtils.formatDate(lockPreDate,"yyyy-MM-dd"));


        List<Map<String,Object>> dataList = new ArrayList<>();
        int seq = 1;
        //循环产线
        for (Long WorkIdKey : workInvListMap.keySet()){
            //物料集
            List<String> invList = workInvListMap.get(WorkIdKey);

            //循环物料
            for (String inv : invList){
                Record info = invInfoMap.get(inv);

                Map<String,Object> map = new HashMap<>();
                map.put("seq",seq++);
                map.put("iPsLevel",info.getInt("iPsLevel"));
                map.put("cWorkName",info.getStr("cWorkName"));
                map.put("cInvCode",info.getStr("cInvCode"));
                map.put("cInvCode1",info.getStr("cInvCode1"));
                map.put("cInvName1",info.getStr("cInvName1"));

                List<Object> objectList = new ArrayList<>();
                Map<String,Record> planDateMap = invPlanDateMap.get(inv);
                for(String date : planDateMap.keySet()){
                    Record record = planDateMap.get(date);

                    Map<String,Object> dataMap = new HashMap<>();
                    dataMap.put("shiyong",record.getBigDecimal("iQty1"));
                    dataMap.put("one",record.getBigDecimal("iQty2"));
                    dataMap.put("two",record.getBigDecimal("iQty3"));
                    dataMap.put("three",record.getBigDecimal("iQty4"));
                    dataMap.put("zaiku",record.getBigDecimal("iQty5"));
                    dataMap.put("tianshu",record.getBigDecimal("iQty6"));
                    dataMap.put("date",date);
                    dataMap.put("lock",false);
                    if (DateUtils.parseDate(date).getTime() <= lockPreDate.getTime()){
                        dataMap.put("lock",true);
                    }
                    objectList.add(dataMap);
                }
                map.put("day",objectList);
                dataList.add(map);
            }
        }
        return dataList;
    }


    /**
     * 锁定计划
     */
    public Ret lockScheduPlan(Kv kv) {
        //排产纪录id
        Long iWeekScheduleId = kv.getLong("iWeekScheduleId");
        if (notOk(iWeekScheduleId)){
            return fail("排产纪录id不能为空！");
        }
        //锁定截止日期
        String lockEndDate = kv.getStr("endDate");
        if (StringUtils.isBlank(lockEndDate)){
            return fail("锁定截止日期不能为空！");
        }
        //排产层级
        int level = apsWeekscheduleService.findFirst("SELECT iLevel FROM Aps_WeekSchedule WHERE iAutoId = ? ",iWeekScheduleId).getILevel();

        //TODO:获取当前层级上次排产锁定日期+1
        ApsWeekschedule apsWeekschedule = apsWeekscheduleService.daoTemplate("scheduproductplan.getApsWeekscheduleLock",Kv.by("level",level)).findFirst();
        //锁定开始日期
        Date startDate;
        if (apsWeekschedule != null && apsWeekschedule.getDLockEndTime() != null){
            Date dLockEndTime = apsWeekschedule.getDLockEndTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dLockEndTime);
            calendar.add(Calendar.DATE,1);//日期+1
            startDate = DateUtils.parseDate(DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd"));
        }else {
            startDate = DateUtils.parseDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
        }
        String lockStartDate = DateUtils.formatDate(startDate,"yyyy-MM-dd");
        //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
        List<String> scheduDateList = Util.getBetweenDate(lockStartDate,lockEndDate);


        //TODO:根据层级及日期获取月周生产计划表数据及部门信息
        List<Record> apsPlanQtyList = dbTemplate("scheduproductplan.getApsScheduPlanList",Kv.by("level",level).set("startdate",lockStartDate).set("enddate",lockEndDate)).find();
        //key:inv，   value:<yyyy-MM-dd，Qty1S> 计划/1S
        Map<String,Map<String,BigDecimal>> invPlanDate1SMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Qty2S> 计划/2S
        Map<String,Map<String,BigDecimal>> invPlanDate2SMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Qty3S> 计划/3S
        Map<String,Map<String,BigDecimal>> invPlanDate3SMap = new HashMap<>();
        //key:部门id   value:List物料集
        Map<Long,List<String>> deptInvListMap = new HashMap<>();
        //key:inv   value:info
        Map<String,Record> invInfoMap = new HashMap<>();
        //本次排产物料id集
        String idsJoin = "(";
        List<Long> idList = new ArrayList<>();
        for (Record record : apsPlanQtyList){
            Long iDepId = record.getLong("iDepId");
            Long invId = record.getLong("invId");
            String cInvCode = record.getStr("cInvCode");
            String iYear = record.getStr("iYear");
            int iMonth = record.getInt("iMonth");
            int iDate = record.getInt("iDate");
            BigDecimal Qty1S = record.getBigDecimal("Qty1S");
            BigDecimal Qty2S = record.getBigDecimal("Qty2S");
            BigDecimal Qty3S = record.getBigDecimal("Qty3S");
            //yyyy-MM-dd
            String dateKey = iYear;
            dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
            dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

            if (deptInvListMap.containsKey(iDepId)){
                List<String> list = deptInvListMap.get(iDepId);
                if (!list.contains(cInvCode)){
                    list.add(cInvCode);
                }
            }else {
                List<String> list = new ArrayList<>();
                list.add(cInvCode);
                deptInvListMap.put(iDepId,list);
            }

            if (invPlanDate1SMap.containsKey(cInvCode)){
                //key:yyyy-MM-dd   value:Qty1S
                Map<String,BigDecimal> dateQty1SMap = invPlanDate1SMap.get(cInvCode);
                dateQty1SMap.put(dateKey,Qty1S);

                //key:yyyy-MM-dd   value:Qty2S
                Map<String,BigDecimal> dateQty2SMap = invPlanDate2SMap.get(cInvCode);
                dateQty2SMap.put(dateKey,Qty2S);

                //key:yyyy-MM-dd   value:Qty3S
                Map<String,BigDecimal> dateQty3SMap = invPlanDate3SMap.get(cInvCode);
                dateQty3SMap.put(dateKey,Qty3S);
            }else {
                //key:yyyy-MM-dd   value:Qty1S
                Map<String,BigDecimal> dateQty1SMap = new HashMap<>();
                dateQty1SMap.put(dateKey,Qty1S);
                invPlanDate1SMap.put(cInvCode,dateQty1SMap);

                //key:yyyy-MM-dd   value:Qty2S
                Map<String,BigDecimal> dateQty2SMap = new HashMap<>();
                dateQty2SMap.put(dateKey,Qty2S);
                invPlanDate2SMap.put(cInvCode,dateQty2SMap);

                //key:yyyy-MM-dd   value:Qty3S
                Map<String,BigDecimal> dateQty3SMap = new HashMap<>();
                dateQty3SMap.put(dateKey,Qty3S);
                invPlanDate3SMap.put(cInvCode,dateQty3SMap);
            }
            invInfoMap.put(cInvCode,record);
            if (!idList.contains(invId)){
                idsJoin = idsJoin + invId + ",";
                idList.add(invId);
            }
        }
        idsJoin = idsJoin + "601)";


        //TODO:周分类
        //key:1   value:日期集 有序
        Map<Integer,List<String>> weekDateMap = new HashMap<>();
        int weekNum = 1; //周次
        for (String date : scheduDateList) {
            List<String> list = weekDateMap.containsKey(weekNum) ? weekDateMap.get(weekNum) : new ArrayList<>();
            list.add(date);
            weekDateMap.put(weekNum,list);
            String weekDay = DateUtils.formatDate(DateUtils.parseDate(date),"E");
            if (weekDay.equals("星期日")){
                weekNum ++;
            }
        }
        //TODO:根据物料集查询各班次
        List<Record> invCapacityList = dbTemplate("scheduproductplan.getInvCapacityList",Kv.by("ids",idsJoin)).find();
        //key:inv    value:iWorkShiftMid
        Map<String,Long> invCapacity1SMap = new HashMap<>();
        //key:inv    value:iWorkShiftMid
        Map<String,Long> invCapacity2SMap = new HashMap<>();
        //key:inv    value:iWorkShiftMid
        Map<String,Long> invCapacity3SMap = new HashMap<>();
        for (Record record : invCapacityList){
            String cInvCode = record.getStr("cInvCode");
            Long iWorkShiftMid = record.getLong("iWorkShiftMid");
            String cWorkShiftCode = record.getStr("cWorkShiftCode");
            if (cWorkShiftCode.contains("1S")){
                invCapacity1SMap.put(cInvCode,iWorkShiftMid);
            }
            if (cWorkShiftCode.contains("2S")){
                invCapacity2SMap.put(cInvCode,iWorkShiftMid);
            }
            if (cWorkShiftCode.contains("3S")){
                invCapacity3SMap.put(cInvCode,iWorkShiftMid);
            }
        }

        //任务单
        List<MoMotask> moTaskList = new ArrayList<>();
        //工单
        List<MoDoc> moDocList = new ArrayList<>();

        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        String orgCode = getOrgCode();
        String orgName = getOrgName();
        Long orgId = getOrgId();
        Date nowDate = new Date();
        String dateStr = DateUtils.formatDate(nowDate,"yyyyMMdd");
        //对部门逐个处理
        for (Long deptId : deptInvListMap.keySet()) {
            for (Integer week : weekDateMap.keySet()){
                //当前周日期集
                List<String> dateList = weekDateMap.get(week);

                //任务单号
                String planNo = momDataFuncService.getNextRouteNo(1L, "MO"+dateStr, 3);
                Long taskId = JBoltSnowflakeKit.me.nextId();
                Date startdate = DateUtils.parseDate(dateList.get(0));
                Date endDate = DateUtils.parseDate(dateList.get(dateList.size() - 1));
                //主表
                MoMotask motask = new MoMotask();
                motask.setIAutoId(taskId);
                motask.setIOrgId(deptId);
                motask.setCMoPlanNo(planNo);
                motask.setDBeginDate(startdate);
                motask.setDEndDate(endDate);
                motask.setIStatus(1);
                motask.setIAuditStatus(0);
                motask.setIAuditWay(1);
                motask.setICreateBy(userId);
                motask.setDCreateTime(nowDate);
                motask.setCCreateName(userName);
                motask.setIUpdateBy(userId);
                motask.setDUpdateTime(nowDate);
                motask.setCUpdateName(userName);
                motask.setIOrgId(orgId);
                motask.setCOrgCode(orgCode);
                motask.setCOrgName(orgName);
                moTaskList.add(motask);

                //inv集
                List<String> recordList = deptInvListMap.get(deptId);
                for (String inv : recordList){
                    //inv信息
                    Record invInfo = invInfoMap.get(inv);
                    //key:yyyy-MM-dd   value:qty1S
                    Map<String,BigDecimal> dateQtyMap = invPlanDate1SMap.get(inv);
                    //班次ID 1S
                    Long iWorkShiftMid = invCapacity1SMap.get(inv);

                    Long invId = invInfo.getLong("invId");
                    Long iWorkRegionMid = invInfo.getLong("iWorkRegionMid");
                    Long iInventoryRoutingId = invInfo.getLong("iInventoryRoutingId");
                    String cRoutingName = invInfo.getStr("cRoutingName");
                    String cVersion = invInfo.getStr("cVersion");

                    for (String date : dateList){
                        Date dPlanDate = DateUtils.parseDate(date);
                        int year = Integer.parseInt(date.substring(0,4));
                        int month = Integer.parseInt(date.substring(5,7));
                        int day = Integer.parseInt(date.substring(8,10));
                        BigDecimal qty = dateQtyMap.get(date);
                        if (qty != null && (qty.compareTo(BigDecimal.ZERO) > 0)){
                            //任务单号
                            String planNo2 = momDataFuncService.getNextRouteNo(1L, "MOD"+dateStr, 3);

                            MoDoc moDoc = new MoDoc();
                            moDoc.setIAutoId(JBoltSnowflakeKit.me.nextId());
                            moDoc.setIType(1);
                            moDoc.setIMoTaskId(taskId);
                            moDoc.setIWorkRegionMid(iWorkRegionMid);
                            moDoc.setIInventoryId(invId);
                            moDoc.setCMoDocNo(planNo2);
                            moDoc.setDPlanDate(dPlanDate);
                            moDoc.setIYear(year);
                            moDoc.setIMonth(month);
                            moDoc.setIDate(day);
                            moDoc.setIDepartmentId(deptId);
                            moDoc.setIWorkShiftMid(iWorkShiftMid);
                            moDoc.setIQty(qty);
                            moDoc.setIStatus(1);
                            moDoc.setIInventoryRouting(iInventoryRoutingId);
                            moDoc.setCRoutingName(cRoutingName);
                            moDoc.setCVersion(cVersion);
                            moDocList.add(moDoc);
                        }
                    }
                }
            }
        }
        tx(() -> {
            update("UPDATE Aps_WeekSchedule SET dLockEndTime = ? WHERE iAutoId = ? ",DateUtils.parseDate(lockEndDate),iWeekScheduleId);
            motaskService.batchSave(moTaskList);
            moDocService.batchSave(moDocList);
            return true;
        });
        return SUCCESS;
    }
    /**
     * 解锁计划
     */
    public Ret unLockScheduPlan(Kv kv) {
        //排产纪录id
        Long iWeekScheduleId = kv.getLong("iWeekScheduleId");
        if (notOk(iWeekScheduleId)){
            return fail("排产纪录id不能为空！");
        }
        //解锁开始日期
        String unLockStartDate = kv.getStr("endDate");
        if (StringUtils.isBlank(unLockStartDate)){
            return fail("解锁开始日期不能为空！");
        }

        //TODO:查询任务工单表结束日期 > 解锁开始日期&&未审核数据
        List<MoMotask> getMotaskByEndDateList =  motaskService.daoTemplate("scheduproductplan.getMotaskByEndDateList",Kv.by("unlockstartdate",unLockStartDate)).find();
        List<Long> taskIdList = new ArrayList<>();
        for (MoMotask motask : getMotaskByEndDateList) {
            taskIdList.add(motask.getIAutoId());
        }

        tx(() -> {
            if (taskIdList.size() > 0) {
                update("UPDATE Mo_MoTask SET IsDeleted = 1 WHERE iAutoId IN (" + CollUtil.join(taskIdList, COMMA) + ") ");
                update("UPDATE Aps_WeekSchedule SET dLockEndTime = ? WHERE iAutoId = ? ",DateUtils.parseDate(unLockStartDate),iWeekScheduleId);
            }
            return true;
        });
        return SUCCESS;
    }

    //-----------------------------------------------------------------月周生产计划汇总-----------------------------------------------

    /**
     * 月周计划汇总
     */
    public List<Record> getApsMonthPlanSumPage(int pageNumber, int pageSize, Kv kv) {
        List<Record> dataList = new ArrayList<>();

        String startDate = kv.getStr("startdate");
        String endDate = kv.getStr("enddate");
        if (notOk(startDate) || notOk(endDate)){
            ValidationUtils.error("开始日期-结束日期不能为空！");
        }
        //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
        List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);

        pageSize = pageSize * 15;

        //TODO:根据日期及条件获取月周生产计划表数据三班汇总
        List<Record> recordPage = dbTemplate("scheduproductplan.getApsMonthPlanSumList",kv).find();
        List<Record> apsPlanQtyList = recordPage;

        //key:产线id   value:List物料集
        Map<Long,List<String>> workInvListMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，qty>
        Map<String,Map<String,BigDecimal>> invPlanDateMap = new HashMap<>();
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
                if (!list.contains(cInvCode)){
                    list.add(cInvCode);
                }
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
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyMap,null);

                /*Record planRecord = new Record();
                planRecord.set("cInvCode",inv);
                planRecord.set("cInvCode1",invInfo.getStr("cInvCode1"));
                planRecord.set("cInvName1",invInfo.getStr("cInvName1"));
                planRecord.set("cWorkName",invInfo.getStr("cWorkName"));

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
                scheduProductPlanMonthList.add(planRecord);*/
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
     * 月周人数汇总
     */
    public List<Record> getApsMonthPeopleSumPage(int pageNumber, int pageSize, Kv kv) {
        List<Record> scheduProductPeopleMonthList = new ArrayList<>();

        String startDate = kv.getStr("startdate");
        String endDate = kv.getStr("enddate");
        if (notOk(startDate) || notOk(endDate)){
            ValidationUtils.error("开始日期-结束日期不能为空！");
        }
        //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
        List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);

        pageSize = pageSize * 15;

        //TODO:根据日期及条件获取月周生产计划表数据三班汇总
        List<Record> recordPage = dbTemplate("scheduproductplan.getApsMonthPlanSumList",kv).find();
        List<Record> apsPlanQtyList = recordPage;

        //key:产线id   value:List物料集
        Map<Long,List<String>> workInvListMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，qty>
        Map<String,Map<String,BigDecimal>> invPlanDateMap = new HashMap<>();
        //key:inv   value:info
        Map<String,Record> invInfoMap = new HashMap<>();
        //本次排产物料id集
        String idsJoin = "(";
        List<Long> idList = new ArrayList<>();
        for (Record record : apsPlanQtyList){
            Long iWorkRegionMid = record.getLong("iWorkRegionMid");
            Long invId = record.getLong("invId");
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
                if (!list.contains(cInvCode)){
                    list.add(cInvCode);
                }
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
            if (!idList.contains(invId)){
                idsJoin = idsJoin + invId + ",";
                idList.add(invId);
            }
        }
        idsJoin = idsJoin + "601)";

        //TODO:根据物料集查询默认工艺路线工序的人数汇总
        List<Record> getInvMergeRateSumList = dbTemplate("scheduproductplan.getInvMergeRateSumList",Kv.by("ids",idsJoin)).find();
        //key:inv   value:mergeRateSum
        Map<String,BigDecimal> invMergeRateSumMap = new HashMap<>();
        for (Record record : getInvMergeRateSumList){
            String cInvCode = record.getStr("cInvCode");
            BigDecimal iMergeRateSum = record.getBigDecimal("iMergeRateSum");
            invMergeRateSumMap.put(cInvCode,iMergeRateSum);
        }



        //对产线逐个处理
        for (Long key : workInvListMap.keySet()) {
            List<String> recordList = workInvListMap.get(key);
            for (String inv : recordList){
                //inv信息
                Record invInfo = invInfoMap.get(inv);
                //key:yyyy-MM-dd   value:qty
                Map<String,BigDecimal> dateQtyMap = invPlanDateMap.get(inv);
                //人数汇总
                BigDecimal mergeRateSum = invMergeRateSumMap.get(inv) != null ? invMergeRateSumMap.get(inv) : BigDecimal.ZERO;

                Record planRecord = new Record();
                planRecord.set("cInvCode",inv);
                planRecord.set("cInvCode1",invInfo.getStr("cInvCode1"));
                planRecord.set("cInvName1",invInfo.getStr("cInvName1"));
                planRecord.set("cWorkName",invInfo.getStr("cWorkName"));

                //key:yyyy-MM   value:qtySum
                Map<String,BigDecimal> monthQtyMap = new LinkedHashMap<>();
                int monthCount = 1;
                for (int i = 0; i < scheduDateList.size(); i++) {
                    String date = scheduDateList.get(i);
                    String month = date.substring(0,7);
                    BigDecimal qty = dateQtyMap.get(date);
                    if (qty != null && (qty.compareTo(BigDecimal.ZERO)) == 1){
                        qty = mergeRateSum;
                    }

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

                scheduProductPeopleMonthList.add(planRecord);
            }
        }

        Page<Record> page = new Page<>();
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        int num = (int) Math.ceil(scheduProductPeopleMonthList.size() / 15);
        page.setTotalPage(num);
        page.setTotalRow(scheduProductPeopleMonthList.size());
        page.setList(scheduProductPeopleMonthList);

        return scheduProductPeopleMonthList;
    }



    //-----------------------------------------------------------------生产计划及实绩管理-----------------------------------------------

    /**
     * 生产计划及实绩管理
     */
    public List<Record> getApsPlanAndActualPage(int pageNumber, int pageSize, Kv kv) {
        List<Record> dataList = new ArrayList<>();

        String startDate = kv.getStr("startdate");
        String endDate = kv.getStr("enddate");
        if (notOk(startDate) || notOk(endDate)){
            ValidationUtils.error("开始日期-结束日期不能为空！");
        }
        //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
        List<String> scheduDateList = Util.getBetweenDate(startDate,endDate);

        pageSize = pageSize * 15;

        //TODO:根据日期及条件获取月周生产计划表数据
        List<Record> recordPage = dbTemplate("scheduproductplan.getApsMonthPlanList",kv).find();
        List<Record> apsPlanQtyList = recordPage;

        //key:inv，   value:<yyyy-MM-dd，QtyPP> 计划使用
        Map<String,Map<String,BigDecimal>> invPlanDatePPMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Qty1S> 计划/1S
        Map<String,Map<String,BigDecimal>> invPlanDate1SMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Qty2S> 计划/2S
        Map<String,Map<String,BigDecimal>> invPlanDate2SMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Qty3S> 计划/3S
        Map<String,Map<String,BigDecimal>> invPlanDate3SMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，QtyZK> 计划在库
        Map<String,Map<String,BigDecimal>> invPlanDateZKMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，QtySUM> 计划汇总
        Map<String,Map<String,BigDecimal>> invPlanDateSUMMap = new HashMap<>();
        //key:产线id   value:List物料集
        Map<Long,List<String>> workInvListMap = new HashMap<>();
        //key:inv   value:info
        Map<String,Record> invInfoMap = new HashMap<>();
        //本次排产物料id集
        String idsJoin = "(";
        List<Long> idList = new ArrayList<>();
        for (Record record : apsPlanQtyList){
            Long iWorkRegionMid = record.getLong("iWorkRegionMid");
            Long invId = record.getLong("invId");
            String cInvCode = record.getStr("cInvCode");
            String iYear = record.getStr("iYear");
            int iMonth = record.getInt("iMonth");
            int iDate = record.getInt("iDate");
            BigDecimal QtyPP = record.getBigDecimal("QtyPP");
            BigDecimal Qty1S = record.getBigDecimal("Qty1S");
            BigDecimal Qty2S = record.getBigDecimal("Qty2S");
            BigDecimal Qty3S = record.getBigDecimal("Qty3S");
            BigDecimal QtyZK = record.getBigDecimal("QtyZK");
            BigDecimal QtySUM = record.getBigDecimal("QtySUM");
            //yyyy-MM-dd
            String dateKey = iYear;
            dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
            dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

            if (workInvListMap.containsKey(iWorkRegionMid)){
                List<String> list = workInvListMap.get(iWorkRegionMid);
                if (!list.contains(cInvCode)){
                    list.add(cInvCode);
                }
            }else {
                List<String> list = new ArrayList<>();
                list.add(cInvCode);
                workInvListMap.put(iWorkRegionMid,list);
            }

            if (invPlanDatePPMap.containsKey(cInvCode)){
                //key:yyyy-MM-dd   value:QtyPP
                Map<String,BigDecimal> dateQtyPPMap = invPlanDatePPMap.get(cInvCode);
                dateQtyPPMap.put(dateKey,QtyPP);

                //key:yyyy-MM-dd   value:Qty1S
                Map<String,BigDecimal> dateQty1SMap = invPlanDate1SMap.get(cInvCode);
                dateQty1SMap.put(dateKey,Qty1S);

                //key:yyyy-MM-dd   value:Qty2S
                Map<String,BigDecimal> dateQty2SMap = invPlanDate2SMap.get(cInvCode);
                dateQty2SMap.put(dateKey,Qty2S);

                //key:yyyy-MM-dd   value:Qty3S
                Map<String,BigDecimal> dateQty3SMap = invPlanDate3SMap.get(cInvCode);
                dateQty3SMap.put(dateKey,Qty3S);

                //key:yyyy-MM-dd   value:QtyZK
                Map<String,BigDecimal> dateQtyZKMap = invPlanDateZKMap.get(cInvCode);
                dateQtyZKMap.put(dateKey,QtyZK);

                //key:yyyy-MM-dd   value:QtySUM
                Map<String,BigDecimal> dateQtySUMMap = invPlanDateSUMMap.get(cInvCode);
                dateQtySUMMap.put(dateKey,QtySUM);
            }else {
                //key:yyyy-MM-dd   value:QtyPP
                Map<String,BigDecimal> dateQtyPPMap = new HashMap<>();
                dateQtyPPMap.put(dateKey,QtyPP);
                invPlanDatePPMap.put(cInvCode,dateQtyPPMap);

                //key:yyyy-MM-dd   value:Qty1S
                Map<String,BigDecimal> dateQty1SMap = new HashMap<>();
                dateQty1SMap.put(dateKey,Qty1S);
                invPlanDate1SMap.put(cInvCode,dateQty1SMap);

                //key:yyyy-MM-dd   value:Qty2S
                Map<String,BigDecimal> dateQty2SMap = new HashMap<>();
                dateQty2SMap.put(dateKey,Qty2S);
                invPlanDate2SMap.put(cInvCode,dateQty2SMap);

                //key:yyyy-MM-dd   value:Qty3S
                Map<String,BigDecimal> dateQty3SMap = new HashMap<>();
                dateQty3SMap.put(dateKey,Qty3S);
                invPlanDate3SMap.put(cInvCode,dateQty3SMap);

                //key:yyyy-MM-dd   value:QtyZK
                Map<String,BigDecimal> dateQtyZKMap = new HashMap<>();
                dateQtyZKMap.put(dateKey,QtyZK);
                invPlanDateZKMap.put(cInvCode,dateQtyZKMap);

                //key:yyyy-MM-dd   value:QtySUM
                Map<String,BigDecimal> dateQtySUMMap = new HashMap<>();
                dateQtySUMMap.put(dateKey,QtySUM);
                invPlanDateSUMMap.put(cInvCode,dateQtySUMMap);
            }
            invInfoMap.put(cInvCode,record);
            if (!idList.contains(invId)){
                idsJoin = idsJoin + invId + ",";
                idList.add(invId);
            }
        }
        idsJoin = idsJoin + "601)";


        //TODO:根据物料集及日期及条件获取APS下发的计划工单实绩数(三个班次 已完工数)
        List<Record> actualList = dbTemplate("scheduproductplan.getMoDocMonthActualList",Kv.by("ids",idsJoin).set("startdate",startDate).set("enddate",endDate)).find();
        //key:inv，   value:<yyyy-MM-dd，Qty1S> 实绩/1S
        Map<String,Map<String,BigDecimal>> invActualDate1SMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Qty2S> 实绩/2S
        Map<String,Map<String,BigDecimal>> invActualDate2SMap = new HashMap<>();
        //key:inv，   value:<yyyy-MM-dd，Qty3S> 实绩/3S
        Map<String,Map<String,BigDecimal>> invActualDate3SMap = new HashMap<>();
        for (Record record : actualList){
            String cInvCode = record.getStr("cInvCode");
            String cWorkShiftCode = record.getStr("cWorkShiftCode");
            String dPlanDate = DateUtils.formatDate(record.getDate("dPlanDate"),"yyyy-MM-dd");
            BigDecimal iCompQty = record.getBigDecimal("iCompQty");
            // 实绩/1S
            if (cWorkShiftCode.contains("1S")){
                if (invActualDate1SMap.containsKey(cInvCode)){
                    //key:yyyy-MM-dd   value:Qty1S
                    Map<String,BigDecimal> dateQty1SMap = invActualDate1SMap.get(cInvCode);
                    dateQty1SMap.put(dPlanDate,iCompQty);
                }else {
                    //key:yyyy-MM-dd   value:Qty1S
                    Map<String,BigDecimal> dateQty1SMap = new HashMap<>();
                    dateQty1SMap.put(dPlanDate,iCompQty);
                    invActualDate1SMap.put(cInvCode,dateQty1SMap);
                }
            }
            // 实绩/2S
            if (cWorkShiftCode.contains("2S")){
                if (invActualDate2SMap.containsKey(cInvCode)){
                    //key:yyyy-MM-dd   value:Qty2S
                    Map<String,BigDecimal> dateQty3SMap = invActualDate2SMap.get(cInvCode);
                    dateQty3SMap.put(dPlanDate,iCompQty);
                }else {
                    //key:yyyy-MM-dd   value:Qty2S
                    Map<String,BigDecimal> dateQty2SMap = new HashMap<>();
                    dateQty2SMap.put(dPlanDate,iCompQty);
                    invActualDate2SMap.put(cInvCode,dateQty2SMap);
                }
            }
            // 实绩/3S
            if (cWorkShiftCode.contains("3S")){
                if (invActualDate3SMap.containsKey(cInvCode)){
                    //key:yyyy-MM-dd   value:Qty3S
                    Map<String,BigDecimal> dateQty3SMap = invActualDate3SMap.get(cInvCode);
                    dateQty3SMap.put(dPlanDate,iCompQty);
                }else {
                    //key:yyyy-MM-dd   value:Qty3S
                    Map<String,BigDecimal> dateQty3SMap = new HashMap<>();
                    dateQty3SMap.put(dPlanDate,iCompQty);
                    invActualDate3SMap.put(cInvCode,dateQty3SMap);
                }
            }
        }

        //TODO:根据物料集及日期及条件获取APS下发的计划工单实绩数(三个班次汇总 已完工数)
        List<Record> actualSumList = dbTemplate("scheduproductplan.getMoDocMonthActualSumList",Kv.by("ids",idsJoin).set("startdate",startDate).set("enddate",endDate)).find();
        //key:inv，   value:<yyyy-MM-dd，QtySUM> 实绩汇总
        Map<String,Map<String,BigDecimal>> invActualDateSUMMap = new HashMap<>();
        for (Record record : actualSumList){
            String cInvCode = record.getStr("cInvCode");
            String dPlanDate = DateUtils.formatDate(record.getDate("dPlanDate"),"yyyy-MM-dd");
            BigDecimal CompQtySUM = record.getBigDecimal("CompQtySUM");

            if (invActualDateSUMMap.containsKey(cInvCode)){
                //key:yyyy-MM-dd   value:QtySUM
                Map<String,BigDecimal> dateQtySUMMap = invActualDateSUMMap.get(cInvCode);
                dateQtySUMMap.put(dPlanDate,CompQtySUM);
            }else {
                //key:yyyy-MM-dd   value:QtySUM
                Map<String,BigDecimal> dateQtySUMMap = new HashMap<>();
                dateQtySUMMap.put(dPlanDate,CompQtySUM);
                invActualDateSUMMap.put(cInvCode,dateQtySUMMap);
            }
        }


        //对产线逐个处理
        for (Long key : workInvListMap.keySet()) {
            List<String> recordList = workInvListMap.get(key);
            for (String inv : recordList){
                //inv信息
                Record invInfo = invInfoMap.get(inv);

                //key:yyyy-MM-dd   value:qty  计划使用
                Map<String,BigDecimal> dateQtyPPMap = invPlanDatePPMap.get(inv);
                //数据处理 行转列并赋值
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyPPMap,"计划使用");

                //key:yyyy-MM-dd   value:qty  计划/1S
                Map<String,BigDecimal> dateQty1SMap = invPlanDate1SMap.get(inv);
                //数据处理 行转列并赋值
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQty1SMap,"计划/1S");

                //key:yyyy-MM-dd   value:qty  计划/2S
                Map<String,BigDecimal> dateQty2SMap = invPlanDate2SMap.get(inv);
                //数据处理 行转列并赋值
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQty2SMap,"计划/2S");

                //key:yyyy-MM-dd   value:qty  计划/3S
                Map<String,BigDecimal> dateQty3SMap = invPlanDate3SMap.get(inv);
                //数据处理 行转列并赋值
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQty3SMap,"计划/3S");

                //key:yyyy-MM-dd   value:qty  实绩/1S
                Map<String,BigDecimal> dateQtyActual1SMap = invActualDate1SMap.get(inv);
                //数据处理 行转列并赋值
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyActual1SMap,"实绩/1S");

                //key:yyyy-MM-dd   value:qty  实绩/2S
                Map<String,BigDecimal> dateQtyActual2SMap = invActualDate2SMap.get(inv);
                //数据处理 行转列并赋值
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyActual2SMap,"实绩/2S");

                //key:yyyy-MM-dd   value:qty  实绩/3S
                Map<String,BigDecimal> dateQtyActual3SMap = invActualDate3SMap.get(inv);
                //数据处理 行转列并赋值
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyActual3SMap,"实绩/3S");

                //key:yyyy-MM-dd   value:qty  计划汇总
                Map<String,BigDecimal> dateQtyPlanSUMMap = invPlanDateSUMMap.get(inv);
                //key:yyyy-MM-dd   value:qty  实绩汇总
                Map<String,BigDecimal> dateQtyActualSUMMap = invActualDateSUMMap.get(inv);
                //数据处理 行转列并赋值 实绩汇总-计划汇总(合计差异)
                scheduRowToColumn(dataList,scheduDateList,invInfo,dateQtyActualSUMMap,dateQtyPlanSUMMap,"合计差异");

                //key:yyyy-MM-dd   value:qty  计划在库
                Map<String,BigDecimal> dateQtyZKMap = invPlanDateZKMap.get(inv);
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
        planRecord.set("cWorkName",invInfo.getStr("cWorkName"));
        if (StringUtils.isNotBlank(colName)){
            planRecord.set("colName",colName);
        }
        dateQtyMap = dateQtyMap != null ? dateQtyMap : new HashMap<>();

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
    /**
     * 将dateQtyMap减去dateQtyMap2根据scheduDateList按日期顺序把qty存入Record并放进List
     * @param scheduProductPlanMonthList 数据List
     * @param scheduDateList 排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
     * @param invInfo key:inv   value:info
     * @param dateQtyMap key:inv，  value:<yyyy-MM-dd，qty>
     * @param dateQtyMap2 key:inv，  value:<yyyy-MM-dd，qty>
     */
    public void scheduRowToColumn(List<Record> scheduProductPlanMonthList,List<String> scheduDateList,
                                  Record invInfo,Map<String,BigDecimal> dateQtyMap,Map<String,BigDecimal> dateQtyMap2,String colName){
        Record planRecord = new Record();
        planRecord.set("cInvCode",invInfo.getStr("cInvCode"));
        planRecord.set("cInvCode1",invInfo.getStr("cInvCode1"));
        planRecord.set("cInvName1",invInfo.getStr("cInvName1"));
        planRecord.set("cWorkName",invInfo.getStr("cWorkName"));
        if (StringUtils.isNotBlank(colName)){
            planRecord.set("colName",colName);
        }
        dateQtyMap = dateQtyMap != null ? dateQtyMap : new HashMap<>();
        dateQtyMap2 = dateQtyMap2 != null ? dateQtyMap2 : new HashMap<>();

        //key:yyyy-MM   value:qtySum
        Map<String,BigDecimal> monthQtyMap = new LinkedHashMap<>();
        int monthCount = 1;
        for (int i = 0; i < scheduDateList.size(); i++) {
            String date = scheduDateList.get(i);
            String month = date.substring(0,7);
            BigDecimal qty = dateQtyMap.get(date) != null ? dateQtyMap.get(date) : BigDecimal.ZERO;
            BigDecimal qty2 = dateQtyMap2.get(date) != null ? dateQtyMap2.get(date) : BigDecimal.ZERO;
            qty = qty.subtract(qty2);

            if (monthQtyMap.containsKey(month)){
                BigDecimal monthSum = monthQtyMap.get(month);
                monthQtyMap.put(month,monthSum.add(qty));
            }else {
                monthQtyMap.put(month, qty);
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


    
    public List<Record> getInvInfoByLevelList(Okv okv){
        return dbTemplate("scheduproductplan.getInvInfoByLevelList", okv).find();
    }
    public List<Record> getPinvInfoByinvList(Okv okv){
        return dbTemplate("scheduproductplan.getPinvInfoByinvList", okv).find();
    }

    public List<Record> getCusOrderSumList(Okv okv){
        return dbTemplate("scheduproductplan.getCusOrderSumList", okv).find();
    }
    public List<Record> getWeekScheduSumList(Okv okv){
        return dbTemplate("scheduproductplan.getWeekScheduSumList", okv).find();
    }

    public List<Record> getWeekScheduPlanList(Kv okv){
        return dbTemplate("scheduproductplan.getWeekScheduPlanList", okv).find();
    }
    /**
     * 根据日期+编码查询查询工作日期
     * @param startDate: yyyy-MM-dd
     * @param endDate: yyyy-MM-dd
     * @return List
     */
    public List<String> getCalendarDateList(Long organizeid, String caledarCode, String startDate, String endDate) {
        Okv okv = Okv.by("csourcecode", caledarCode)  //来源编码
                .set("icaluedartype", "1")  //日历类型 工作日
                .set("startdate", startDate)
                .set("enddate", endDate);
        return dbTemplate("scheduproductplan.getCalendarDateList", okv).query();
    }

    public List<Record> getLastDateZKQtyList(Kv kv){
        return dbTemplate("scheduproductplan.getLastDateZKQtyList", kv).find();
    }

    public List<Record> getYearMonthQtySumByinvList(Kv kv){
        return dbTemplate("scheduproductplan.getYearMonthQtySumByinvList", kv).find();
    }

    public BigDecimal getConfigValue(Kv kv){
        return dbTemplate("scheduproductplan.getConfigValue", kv).queryBigDecimal();
    }

}
