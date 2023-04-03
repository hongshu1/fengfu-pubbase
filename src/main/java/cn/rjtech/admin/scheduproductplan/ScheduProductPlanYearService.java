package cn.rjtech.admin.scheduproductplan;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.calendar.CalendarService;
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
public class ScheduProductPlanYearService extends BaseService<ApsAnnualplanm> {

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
            Calendar calendar = Calendar.getInstance();
            Date date = cn.rjtech.util.DateUtils.parseDate(startYear);
            calendar.setTime(date);
            //第二个年份
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            calendar.add(Calendar.YEAR,1);
            String endYear = sdf.format(calendar.getTime());

            Long organizeId = getOrgId();
            String CC = "CC";  //客户行事历
            String PP = "PP";    //计划使用
            String CP = "CP";    //计划数量
            String ZK = "ZK";    //计划在库


            //TODO: 所有日历的月份工作日集合  key：日历类型字典值sn，value：<年份，日历每月工作天数>
            Map<String,Map<String,ScheduProductYearViewDTO>> calendarMonthNumListMapSn = new HashMap<>();
            //根据日历字典查询出所有类型的工作日历
            List<Dictionary> calendarTypeList = JBoltDictionaryCache.me.getListByTypeKey("prodcalendar_type",true);
            for (Dictionary dictionary : calendarTypeList){
                String sn = dictionary.getSn();
                //查询查询年度每月的工作天数
                List<Record> calendarMonthNumList = getCalendarMonthNumList(organizeId,sn,startYear,endYear);
                Map<String,ScheduProductYearViewDTO> calendarMonthNumListMap = new HashMap<>();
                for (Record record : calendarMonthNumList) {
                    String dTakeYear = record.get("dTakeYear");
                    int month = record.getInt("month");
                    BigDecimal monthNum = record.getBigDecimal("monthNum");
                    if (calendarMonthNumListMap.containsKey(dTakeYear)){
                        ScheduProductYearViewDTO viewDTO = calendarMonthNumListMap.get(dTakeYear);
                        viewDTO.getClass().getMethod("setNowmonth"+month, new Class[]{BigDecimal.class}).invoke(viewDTO, monthNum);
                    }else {
                        ScheduProductYearViewDTO viewDTO = new ScheduProductYearViewDTO();
                        viewDTO.getClass().getMethod("setNowmonth"+month, new Class[]{BigDecimal.class}).invoke(viewDTO, monthNum);
                        calendarMonthNumListMap.put(dTakeYear,viewDTO);
                    }
                }
                calendarMonthNumListMapSn.put(sn,calendarMonthNumListMap);
            }

            //TODO: 所有客户的年度每月工作日集合  key：客户id，value：<年份，客户每月工作天数>
            Map<Long,Map<String,Record>> cusWorkMonthNumListMap = new HashMap<>();
            //根据客户id集查询客户年度每月工作天数
            List<Record> cusWorkMonthNumList = getCusWorkMonthNumList(organizeId,customerIds,startYear,endYear);
            for (Record record : cusWorkMonthNumList){
                Long iCustomerId = record.getLong("iCustomerId");
                String iYear = record.getStr("iYear");
                if (cusWorkMonthNumListMap.containsKey(iCustomerId)){
                    Map<String,Record> map = cusWorkMonthNumListMap.get(iCustomerId);
                    map.put(iYear,record);
                }else {
                    Map<String,Record> map = new HashMap<>();
                    map.put(iYear,record);
                    cusWorkMonthNumListMap.put(iCustomerId,map);
                }
            }


            //TODO:查询本次所有客户的订单计划
            List<Record> sourceYearOrderList = getSourceYearOrderList(Okv.by("organizeid",organizeId).set("customerids",customerIds).set("startyear",startYear));


            //本次所有物料集ids
            StringJoiner invIds = new StringJoiner(",");
            for (Record record : sourceYearOrderList) {
                Long iInventoryId = record.getLong("iInventoryId");
                invIds.add(iInventoryId.toString());
            }
            //TODO:查询物料集信息
            List<Record> invInfoList = getInvInfoList(Okv.by("organizeid",organizeId).set("invids",invIds.toString()));
            //key:invid  value:info
            Map<Long,Record> invInfoMap = new HashMap<>();
            for (Record record : invInfoList){
                invInfoMap.put(record.getLong("iAutoId"),record);
            }


            //TODO:查询本次所有客户的订单计划
            for (Record record : sourceYearOrderList) {
                Long iCustomerId = record.getLong("iCustomerId");
                Long iInventoryId = record.getLong("iInventoryId");
                Record invInfo = invInfoMap.get(iInventoryId);
                String cProdCalendarTypeSn = isOk(invInfo.getStr("cProdCalendarTypeSn")) ? invInfo.getStr("cProdCalendarTypeSn") : "1";


                Map<String,Record> cusWorkMonthNumMap = cusWorkMonthNumListMap.get(iCustomerId) != null ? cusWorkMonthNumListMap.get(iCustomerId) : new HashMap<>();
                Map<String,ScheduProductYearViewDTO> calendarMonthNumMap = calendarMonthNumListMapSn.get(cProdCalendarTypeSn) != null ? calendarMonthNumListMapSn.get(cProdCalendarTypeSn) : new HashMap<>();
                //CC:客户行事历
                ScheduProductYearViewDTO productYearViewCC = getProductYearViewCC(record,CC,startYear,endYear,cusWorkMonthNumMap,calendarMonthNumMap);


                //PP:计划使用
                ScheduProductYearViewDTO productYearViewPP = getProductYearViewPP(record,PP,startYear,endYear);


                //ZK:计划在库
                ScheduProductYearViewDTO productYearViewZK = getProductYearViewZK(record,ZK,startYear,endYear);











            }



        }catch (Exception e){
            throw new RuntimeException("排程计划出错！"+e.getMessage());
        }




            //return true;
        //});
        return new ArrayList<>();
    }


    /**
     * CC:客户行事历
     * @param cusWorkMonthNumMap  客户工作天数 <年份，客户每月工作天数>
     * @param calendarMonthNumMap 日历工作天数 <年份，日历每月工作天数>
     */
    public ScheduProductYearViewDTO getProductYearViewCC(Record record,String CC,String startYear,String endYear,
                                                         Map<String,Record> cusWorkMonthNumMap,
                                                         Map<String,ScheduProductYearViewDTO> calendarMonthNumMap){
        Long iCustomerId = record.getLong("iCustomerId");
        Long iInventoryId = record.getLong("iInventoryId");
        //CC:客户行事历
        ScheduProductYearViewDTO productYearViewCC = new ScheduProductYearViewDTO();
        try {
            productYearViewCC.setPlanTypeCode(CC);
            productYearViewCC.setiCustomerId(iCustomerId);
            productYearViewCC.setcCusCode(record.get("cCusCode"));
            productYearViewCC.setiEquipmentModelId(record.getLong("iEquipmentModelId"));
            productYearViewCC.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
            productYearViewCC.setiInventoryId(iInventoryId);
            productYearViewCC.setcInvCode(record.get("cInvCode"));
            productYearViewCC.setcInvCode1(record.get("cInvCode1"));
            productYearViewCC.setcInvName1(record.get("cInvName1"));

            //客户工作天数
            Record cusWorkMonthStart = cusWorkMonthNumMap.get(startYear);
            Record cusWorkMonthEnd = cusWorkMonthNumMap.get(endYear);
            //日历工作天数
            ScheduProductYearViewDTO calendarMonthStart = calendarMonthNumMap.get(startYear);
            ScheduProductYearViewDTO calendarMonthEnd = calendarMonthNumMap.get(endYear);

            productYearViewCC.setNowyear(startYear);
            BigDecimal nowMonthSum = BigDecimal.ZERO;
            BigDecimal nextMonthSum = BigDecimal.ZERO;
            for (int i = 1; i <= 12; i++) {
                //客户工作天数
                BigDecimal cusnowmonthStart = BigDecimal.ZERO;
                if (cusWorkMonthStart != null){
                    cusnowmonthStart = cusWorkMonthStart.get("month" + i);
                }
                //日历工作天数
                BigDecimal calendarnowmonthStart = BigDecimal.ZERO;
                if (calendarMonthStart != null){
                    Object num = calendarMonthStart.getClass().getMethod("getNowmonth"+i,
                            new Class[]{}).invoke(calendarMonthStart, new Object[]{});
                    if (num != null){
                        calendarnowmonthStart = (BigDecimal) num;
                    }
                }
                //客户工作天数-日历工作天数
                BigDecimal qtyStart = cusnowmonthStart.subtract(calendarnowmonthStart);
                productYearViewCC.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class})
                        .invoke(productYearViewCC, qtyStart);
                nowMonthSum = nowMonthSum.add(qtyStart);
                if (i <= 3){
                    //客户工作天数
                    BigDecimal cusnowmonthEnd = BigDecimal.ZERO;
                    if (cusWorkMonthEnd != null){
                        cusnowmonthEnd = cusWorkMonthEnd.get("month" + i);
                    }
                    //日历工作天数
                    BigDecimal calendarnowmonthEnd = BigDecimal.ZERO;
                    if (calendarMonthEnd != null){
                        Object num = calendarMonthEnd.getClass().getMethod("getNowmonth"+i,
                                new Class[]{}).invoke(calendarMonthEnd, new Object[]{});
                        if (num != null){
                            calendarnowmonthEnd = (BigDecimal) num;
                        }
                    }
                    //客户工作天数-日历工作天数
                    BigDecimal qtyEnd = cusnowmonthEnd.subtract(calendarnowmonthEnd);
                    productYearViewCC.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class})
                            .invoke(productYearViewCC, qtyEnd);
                    nextMonthSum = nextMonthSum.add(qtyEnd);
                }
            }
            productYearViewCC.setNowMonthSum(nowMonthSum);
            productYearViewCC.setNextyear(endYear);
            productYearViewCC.setNextmonthSum(nextMonthSum);
        }catch (Exception e){
            throw new RuntimeException("客户行事日历计算出错！"+e.getMessage());
        }
        return productYearViewCC;
    }
    /**
     * PP:计划使用数
     */
    public ScheduProductYearViewDTO getProductYearViewPP(Record record,String PP,String startYear,String endYear){
        Long iCustomerId = record.getLong("iCustomerId");
        Long iInventoryId = record.getLong("iInventoryId");
        //PP:计划使用数
        ScheduProductYearViewDTO productYearViewPP = new ScheduProductYearViewDTO();
        try {
            productYearViewPP.setPlanTypeCode(record.get("planTypeCode"));
            productYearViewPP.setiCustomerId(iCustomerId);
            productYearViewPP.setcCusCode(record.get("cCusCode"));
            productYearViewPP.setiEquipmentModelId(record.getLong("iEquipmentModelId"));
            productYearViewPP.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
            productYearViewPP.setiInventoryId(iInventoryId);
            productYearViewPP.setcInvCode(record.get("cInvCode"));
            productYearViewPP.setcInvCode1(record.get("cInvCode1"));
            productYearViewPP.setcInvName1(record.get("cInvName1"));
            productYearViewPP.setNowyear(record.get("nowyear"));
            for (int i = 1; i <= 12; i++) {
                BigDecimal nowmonth = record.get("nowmonth" + i);
                productYearViewPP.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewPP, nowmonth);
                if (i <= 3){
                    BigDecimal nextmonth = record.get("nextmonth" + i);
                    productYearViewPP.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewPP, nextmonth);
                }
            }
            productYearViewPP.setNowMonthSum(record.getBigDecimal("nowMonthSum"));
            productYearViewPP.setNextyear(record.get("nextyear"));
            productYearViewPP.setNextmonthSum(record.getBigDecimal("nextMonthSum"));
        }catch (Exception e){
            throw new RuntimeException("计划使用计算出错！"+e.getMessage());
        }
        return productYearViewPP;
    }
    /**
     * ZK:计划在库
     */
    public ScheduProductYearViewDTO getProductYearViewZK(Record record,String PP,String startYear,String endYear){
        Long iCustomerId = record.getLong("iCustomerId");
        Long iInventoryId = record.getLong("iInventoryId");
        //PP:计划使用数
        ScheduProductYearViewDTO productYearViewZK = new ScheduProductYearViewDTO();
        try {
            productYearViewZK.setPlanTypeCode(record.get("planTypeCode"));
            productYearViewZK.setiCustomerId(iCustomerId);
            productYearViewZK.setcCusCode(record.get("cCusCode"));
            productYearViewZK.setiEquipmentModelId(record.getLong("iEquipmentModelId"));
            productYearViewZK.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
            productYearViewZK.setiInventoryId(iInventoryId);
            productYearViewZK.setcInvCode(record.get("cInvCode"));
            productYearViewZK.setcInvCode1(record.get("cInvCode1"));
            productYearViewZK.setcInvName1(record.get("cInvName1"));
            productYearViewZK.setNowyear(record.get("nowyear"));
            for (int i = 1; i <= 12; i++) {
                BigDecimal nowmonth = record.get("nowmonth" + i);
                productYearViewZK.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewZK, nowmonth);
                if (i <= 3){
                    BigDecimal nextmonth = record.get("nextmonth" + i);
                    productYearViewZK.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewZK, nextmonth);
                }
            }
            productYearViewZK.setNowMonthSum(record.getBigDecimal("nowMonthSum"));
            productYearViewZK.setNextyear(record.get("nextyear"));
            productYearViewZK.setNextmonthSum(record.getBigDecimal("nextMonthSum"));
        }catch (Exception e){
            throw new RuntimeException("计算在库计算出错！"+e.getMessage());
        }
        return productYearViewZK;
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

    public List<Record> getCusWorkMonthNumList(Long organizeid, String customerIds, String startYear, String endYear){
        Okv okv = Okv.by("customerids", customerIds)
                .set("startyear", startYear)
                .set("endyear", endYear);
        return dbTemplate("scheduproductplan.getCusWorkMonthNumList", okv).find();
    }



}
