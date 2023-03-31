package cn.rjtech.admin.scheduproductplan;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.calendar.CalendarService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.service.func.u9.DateQueryInvTotalFuncService;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.kit.TypeKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.rmi.MarshalledObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 生产计划排程 Service
 * @ClassName: ScheduProductPlanYearService
 * @author: chentao
 * @date: 2023-03-30 11:26
 */
public class ScheduProductPlanYearService extends BaseService<AnnualOrderM>{
    //private final Schedubaseplan dao=new Schedubaseplan().dao();
    @Override
    protected AnnualOrderM dao() {
        return dao();
    }
    @Inject
    private CalendarService calendarService;
    @Inject
    private DateQueryInvTotalFuncService dateQueryInvTotalFuncService;
    @Inject
    private MomDataFuncService momDataFuncService;


    /**
     * 后台管理分页查询
     * @param pageNumber
     * @param pageSize
     * @param keywords
     * @return
     */
    /*public Page<Schedubaseplan> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("cItemCode,cTargetMonth","DESC", pageNumber, pageSize, keywords, "cItemCode");
    }*/

    /**
     * 保存
     * @param schedubaseplan
     * @return
     */
    /*public Ret save(Schedubaseplan schedubaseplan) {
        if(schedubaseplan==null || isOk(schedubaseplan.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(schedubaseplan.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success=schedubaseplan.save();
        if(success) {
            //添加日志
            //addSaveSystemLog(schedubaseplan.getIautoid(), JBoltUserKit.getUserId(), schedubaseplan.getName());
        }
        return ret(success);
    }*/

    /**
     * 更新
     * @param schedubaseplan
     * @return
     */
    /*public Ret update(Schedubaseplan schedubaseplan) {
        if(schedubaseplan==null || notOk(schedubaseplan.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Schedubaseplan dbSchedubaseplan=findById(schedubaseplan.getIautoid());
        if(dbSchedubaseplan==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
        //if(existsName(schedubaseplan.getName(), schedubaseplan.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success=schedubaseplan.update();
        if(success) {
            //添加日志
            //addUpdateSystemLog(schedubaseplan.getIautoid(), JBoltUserKit.getUserId(), schedubaseplan.getName());
        }
        return ret(success);
    }*/

    /**
     * 删除 指定多个ID
     * @param ids
     * @return
     */
    /*public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids,true);
    }*/

    /**
     * 删除
     * @param id
     * @return
     */
    /*public Ret delete(Long id) {
        return deleteById(id,true);
    }*/

    /**
     * 删除数据后执行的回调
     * @param schedubaseplan 要删除的model
     * @param kv 携带额外参数一般用不上
     * @return
     */
    /*@Override
    protected String afterDelete(Schedubaseplan schedubaseplan, Kv kv) {
        //addDeleteSystemLog(schedubaseplan.getIautoid(), JBoltUserKit.getUserId(),schedubaseplan.getName());
        return null;
    }*/

    /**
     * 检测是否可以删除
     * @param schedubaseplan 要删除的model
     * @param kv 携带额外参数一般用不上
     * @return
     */
    /*@Override
    public String checkCanDelete(Schedubaseplan schedubaseplan, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(schedubaseplan, kv);
    }*/

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     * @return
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }





    /**
     * APS排程逻辑处理
     * @param kv
     * @return
     */
    public synchronized List<Record> scheduPlanYear(Kv kv) {
        //tx(() -> {
        try {
            String startYear = kv.getStr("year");    //年份
            String customerIds = kv.getStr("customerIds");      //客户ids
            //判断料号+设备+车型+供应商不能为空
            if (notOk(startYear) || notOk(customerIds)){
                ValidationUtils.isTrue(false,"查询条件不能为空!");
            }

            Long organizeId = getOrgId();

            //TODO:查询本次所有客户的订单计划
            List<Record> sourceYearOrderList = getSourceYearOrderList(Okv.by("organizeid",organizeId).set("customerids",customerIds).set("startyear",startYear));
            for (Record record : sourceYearOrderList) {
                ScheduProductYearViewDTO productYearView = new ScheduProductYearViewDTO();
                productYearView.setiCustomerId(record.get("iCustomerId"));
                productYearView.setcCusCode(record.get("cCusCode"));
                productYearView.setiEquipmentModelId(record.get("iEquipmentModelId"));
                productYearView.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
                productYearView.setiInventoryId(record.get("iInventoryId"));
                productYearView.setcInvCode(record.get("cInvCode"));
                productYearView.setcInvCode1(record.get("cInvCode1"));
                productYearView.setcInvName1(record.get("cInvName1"));
                productYearView.setNowyear(record.get("nowyear"));
                for (int i = 1; i <= 12; i++) {
                    BigDecimal nowmonth = record.get("nowmonth" + i);
                    productYearView.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearView, nowmonth);
                    if (i <= 3){
                        BigDecimal nextmonth = record.get("nextmonth" + i);
                        productYearView.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearView, nextmonth);
                    }
                }
                productYearView.setNowMonthSum(record.get("nowMonthSum"));
                productYearView.setNextyear(record.get("nextyear"));
                productYearView.setNextmonthSum(record.get("nextmonthSum"));
                productYearView.setPlanTypeCode(record.get("planTypeCode"));







            }



        }catch (Exception e){
            throw new RuntimeException("排程计划出错！"+e.getMessage());
        }




            //return true;
        //});
        return new ArrayList<>();
    }






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
                temporaryDateStr = cn.rjtech.util.DateUtils.getDate(planDate,"yyyy-MM-dd",-k,Calendar.DATE);
                Date sdfDate2 = new SimpleDateFormat("yyyy-MM-dd").parse(temporaryDateStr);
                if (sdfDate2.getTime() < dateStart.getTime()){
                    break;
                }
                if (!outCalendarList.contains(temporaryDateStr)){
                    time++;
                }
            }
            String dateLTstr = cn.rjtech.util.DateUtils.getDate(planDate,"yyyy-MM-dd",-k,Calendar.DATE);
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



}
