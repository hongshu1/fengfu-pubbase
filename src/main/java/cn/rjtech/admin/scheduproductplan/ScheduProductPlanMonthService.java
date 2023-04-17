package cn.rjtech.admin.scheduproductplan;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.apsannualpland.ApsAnnualplandService;
import cn.rjtech.admin.apsannualplandqty.ApsAnnualplandQtyService;
import cn.rjtech.admin.calendar.CalendarService;
import cn.rjtech.model.momdata.ApsAnnualpland;
import cn.rjtech.model.momdata.ApsAnnualplandQty;
import cn.rjtech.model.momdata.ApsAnnualplanm;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.service.func.u9.DateQueryInvTotalFuncService;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private MomDataFuncService momDataFuncService;

    @Inject
    private ApsAnnualplandService apsAnnualplandService;
    @Inject
    private ApsAnnualplandQtyService apsAnnualplandQtyService;


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

    //-----------------------------------------------------------------年度生产计划排产-----------------------------------------------

    public List<Record> getCustomerList() {
        return findRecord("SELECT iAutoId,cCusName FROM Bd_Customer WHERE isDeleted = '0'");
    }





    /**
     * 年度生产计划逻辑处理
     * @param kv
     * @return
     */
    public synchronized List<ScheduProductYearViewDTO> scheduPlanMonth(Kv kv) {
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











    //-----------------------------------------------------------------年度生产计划汇总-----------------------------------------------





    /**
     * 日历
     * @param outCalendarList 工作日历集合
     * @param mleadTime 生产提前期
     * @param dateStr 上级汇总数排程日期
     * @param dateStart 排程开始日期
     * @return map
     */
    public Map<String,String> getCalendar(List<String> outCalendarList,int mleadTime,String dateStr,Date dateStart) {
        Map<String,String> map = new HashMap<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date sdfDate = sdf.parse(dateStr);

            //TODO:计划日期
            Date planDate = sdfDate;  //takeDate
            String yearPP = String.format("%tY", planDate);
            String monPP = String.format("%tm", planDate);
            String targetmonthPP = yearPP.concat("-").concat(monPP);
            String dayPP = String.format("%td", planDate);
            map.put("targetmonthPP", targetmonthPP);
            map.put("dayPP", dayPP);
            map.put("plancodePP", "PP");

            int time = mleadTime;
            int k = 0;
            String temporaryDateStr;
            for (int i = 0;i<time;i++){
                k++;
                temporaryDateStr = DateUtils.getDate(planDate,"yyyy-MM-dd",-k,Calendar.DATE);
                Date sdfDate2 = new SimpleDateFormat("yyyy-MM-dd").parse(temporaryDateStr);
                if (sdfDate2.getTime() < dateStart.getTime()){
                    break;
                }
                if (!outCalendarList.contains(temporaryDateStr)){
                    time++;
                }
            }
            String dateLTstr = DateUtils.getDate(planDate,"yyyy-MM-dd",-k,Calendar.DATE);
            Date dateLt = new SimpleDateFormat("yyyy-MM-dd").parse(dateLTstr);

            //TODO:LT日期
            Date dateLTs = dateLt;
            String yearMLT = String.format("%tY", dateLTs);
            String monMLT = String.format("%tm", dateLTs);
            String targetmonthMLT = yearMLT.concat("-").concat(monMLT);
            String dayMLT = String.format("%td", dateLTs);
            map.put("targetmonthMLT", targetmonthMLT);
            map.put("dayMLT", dayMLT);
            map.put("plancodeMLT", "MLT");
            map.put("dateMLT", dateLTstr);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("工作日历计算出错！"+e.getMessage());
        }
        return map;
    }



    List<Record> getSourceYearOrderList(Okv okv){
        return dbTemplate("scheduproductplan.getSourceYearOrderList", okv).find();
    }

    List<Record> getInvInfoList(Okv okv){
        return dbTemplate("scheduproductplan.getInvInfoList", okv).find();
    }

    /**
     * 根据日期+编码查询查询年度每月的工作天数
     * @param startYear: yyyy
     * @return List
     */
    public List<Record> getCalendarMonthNumList(Long organizeid, String caledarCode, String startYear, String endYear){
        Okv okv = Okv.by("csourcecode", caledarCode)  //来源编码
                .set("icaluedartype", "1")  //日历类型 工作日
                .set("startyear", startYear)
                .set("endyear", endYear);
        return dbTemplate("scheduproductplan.getCalendarMonthNumList", okv).find();
    }
    /**
     * 根据日期+编码查询查询年度的工作天数
     * @param startYear: yyyy
     * @return List
     */
    public List<Record> getCalendarYearNumList(Long organizeid, String caledarCode, String startYear, String endYear){
        Okv okv = Okv.by("csourcecode", caledarCode)  //来源编码
                .set("icaluedartype", "1")  //日历类型 工作日
                .set("startyear", startYear)
                .set("endyear", endYear);
        return dbTemplate("scheduproductplan.getCalendarYearNumList", okv).find();
    }

    public List<Record> getCusWorkMonthNumList(Long organizeid, Long customerIds, String startYear, String endYear){
        Okv okv = Okv.by("customerids", customerIds)
                .set("startyear", startYear)
                .set("endyear", endYear);
        return dbTemplate("scheduproductplan.getCusWorkMonthNumList", okv).find();
    }

    public List<Record> getApsYearPlanQtyList(Kv kv){
        return dbTemplate("scheduproductplan.getApsYearPlanQtyList", kv).find();
    }




}
