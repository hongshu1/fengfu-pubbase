package cn.rjtech.admin.cusordersum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.annualorderd.AnnualOrderDService;
import cn.rjtech.admin.annualorderdqty.AnnualorderdQtyService;
import cn.rjtech.admin.annualorderm.AnnualOrderMService;
import cn.rjtech.admin.customerworkdays.CustomerWorkDaysService;
import cn.rjtech.admin.monthorderd.MonthorderdService;
import cn.rjtech.model.momdata.*;
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
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 客户订单-客户计划汇总
 *
 * @ClassName: CusOrderSumService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-14 16:20
 */
public class CusOrderSumService extends BaseService<CusOrderSum> {
    private final CusOrderSum dao = new CusOrderSum().dao();

    @Override
    protected CusOrderSum dao() {
        return dao;
    }

    @Inject
    private AnnualOrderMService annualOrderMService;
    @Inject
    private AnnualOrderDService annualOrderDService;
    @Inject
    private AnnualorderdQtyService annualorderdQtyService;
    //月度计划Service
    @Inject
    private MonthorderdService monthorderdService;
    @Inject
    private CustomerWorkDaysService workDaysService;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param iType      类型：1. 客户计划 2. 客户订单 3. 计划使用
     */
    public Page<CusOrderSum> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("iType", iType);
        //关键词模糊查询
        sql.likeMulti(keywords, "iInventoryId", "dCreateTime");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(CusOrderSum cusOrderSum) {
        if (cusOrderSum == null || isOk(cusOrderSum.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(cusOrderSum.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusOrderSum.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(cusOrderSum.getIAutoId(), JBoltUserKit.getUserId(), cusOrderSum.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(CusOrderSum cusOrderSum) {
        if (cusOrderSum == null || notOk(cusOrderSum.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        CusOrderSum dbCusOrderSum = findById(cusOrderSum.getIAutoId());
        if (dbCusOrderSum == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(cusOrderSum.getName(), cusOrderSum.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusOrderSum.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(cusOrderSum.getIAutoId(), JBoltUserKit.getUserId(), cusOrderSum.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param cusOrderSum 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CusOrderSum cusOrderSum, Kv kv) {
        //addDeleteSystemLog(cusOrderSum.getIAutoId(), JBoltUserKit.getUserId(),cusOrderSum.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param cusOrderSum model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CusOrderSum cusOrderSum, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 客户计划汇总计算
     */
    public Ret algorithmSum() {
        //当前年
        String curYear = DateUtils.getYear();

        //TODO:获取排产日历工作日
        List<Record> getCalendarByYearList = dbTemplate("cusordersum.getCalendarByYearList",Kv.by("year",curYear).set("csourcecode",1)).find();
        //key:yyyy-MM   value:List<dd>
        Map<String,List<Integer>> workMonthDaysMap = new HashMap<>();
        for (Record workDays : getCalendarByYearList){
            String dTakeYearMonth = workDays.getStr("dTakeYearMonth");
            String dTakeDate = workDays.getStr("dTakeDate");
            int day = Integer.parseInt(dTakeDate.substring(8, 10));
            if (workMonthDaysMap.containsKey(dTakeYearMonth)){
                List<Integer> list = workMonthDaysMap.get(dTakeYearMonth);
                list.add(day);
            }else {
                List<Integer> list = new ArrayList<>();
                list.add(day);
                workMonthDaysMap.put(dTakeYearMonth,list);
            }
        }

        //物料信息
        Map<String,Long> invMap = new HashMap<>();

        //TODO:客户计划Map
        //key:inv+2023-01   value:<day,qty>
        Map<String,Map<Integer,BigDecimal>> invMonthMap = new HashMap<>();
        //TODO:根据年份获取客户月度订单-每一天-月（相同客户相同物料相同年月日汇总）
        List<Record> getMonthOrderList = dbTemplate("cusordersum.getMonthOrderList",Kv.by("year",curYear)).find();
        for (Record record : getMonthOrderList){
            Long iCustomerId = record.getLong("iCustomerId");
            Long iInventoryId = record.getLong("iInventoryId");
            String cInvCode = record.getStr("cInvCode");
            String year = record.getStr("year");
            int month = record.getInt("month");
            String yearMonth = year + "-" + (month < 10 ? "0"+month : month);

            String key = cInvCode + "+" + yearMonth;
            //key:day  value:qty
            Map<Integer,BigDecimal> dayMap = invMonthMap.containsKey(key) ? invMonthMap.get(key) : new HashMap<>();

            for (int i = 1; i <= 31; i++) {
                int day = i;
                BigDecimal dayQty = record.getBigDecimal("day"+i) != null ? record.getBigDecimal("day"+i) : BigDecimal.ZERO;
                if (dayMap.containsKey(day)){
                    BigDecimal qty = dayMap.get(day).add(dayQty);
                    dayMap.put(day,qty);
                }else {
                    dayMap.put(day,dayQty);
                }
            }
            invMonthMap.put(key,dayMap);
            invMap.put(cInvCode,iInventoryId);
        }

        /*for (Record record : getMonthOrderList){
            Long iCustomerId = record.getLong("iCustomerId");
            String cInvCode = record.getStr("cInvCode");
            String year = record.getStr("year");
            int month = record.getInt("month");
            String yearMonth = year + (month < 10 ? "0"+month : month);

            String key = cInvCode + yearMonth;
            //key:day  value:qty
            Map<String,BigDecimal> dayMap = invMonthMap.containsKey(key) ? invMonthMap.get(key) : new HashMap<>();

            for (int i = 1; i <= 31; i++) {
                String day = String.valueOf(i);
                BigDecimal dayQty = record.getBigDecimal("day"+i) != null ? record.getBigDecimal("day"+i) : BigDecimal.ZERO;
                if (dayMap.containsKey(day)){
                    BigDecimal qty = dayMap.get(day).add(dayQty);
                    dayMap.put(day,qty);
                }else {
                    dayMap.put(day,dayQty);
                }
            }
            invMonthMap.put(key,dayMap);


            Date date = DateUtils.parseDate(yearMonth + "01");
            Calendar calendarMonth1 = Calendar.getInstance();
            calendarMonth1.setTime(date);
            calendarMonth1.add(Calendar.MONTH,1);//月份+1
            String nextYearMonth1 = DateUtils.formatDate(calendarMonth1.getTime(),"yyyy-MM");
            BigDecimal nextMonth1Qty = record.getBigDecimal("nextMonth1Qty");

            BigDecimal nextMonth2Qty = record.getBigDecimal("nextMonth2Qty");
        }*/

        //key:inv+2023-01   value:<day,qty>
        Map<String,Map<Integer,BigDecimal>> invYearMap = new HashMap<>();
        //TODO:根据年份获取客户年度订单（相同客户相同物料相同年月汇总）
        List<Record> getYearOrderList = dbTemplate("cusordersum.getYearOrderList",Kv.by("year",curYear)).find();
        for (Record record : getYearOrderList){
            Long iCustomerId = record.getLong("iCustomerId");
            Long iInventoryId = record.getLong("iInventoryId");
            String cInvCode = record.getStr("cInvCode");
            String year = record.getStr("year");

            //循环月份
            for (int i = 1; i <= 12; i++) {
                String yearMonth = year + "-" + (i < 10 ? "0"+i : i);
                String key = cInvCode + "+" + yearMonth;
                //key:day  value:qty
                Map<Integer,BigDecimal> dayMap = invYearMap.containsKey(key) ? invYearMap.get(key) : new HashMap<>();

                //月总数量
                BigDecimal monthQty = Optional.ofNullable(record.getBigDecimal("month" + i)).orElse(BigDecimal.ZERO);
                // 平均数总数
                BigDecimal avgQtySum = BigDecimal.ZERO;
                if (monthQty.compareTo(BigDecimal.ZERO) > 0){
                    List<Integer> dayList = workMonthDaysMap.get(yearMonth) != null ? workMonthDaysMap.get(yearMonth) : new ArrayList<>();
                    int workSum = dayList.size();//月工作天数
                    //平均数量
                    BigDecimal avgQty = monthQty.divide(BigDecimal.valueOf(workSum),0,BigDecimal.ROUND_UP);

                    for (Integer day : dayList){
                        avgQtySum = avgQtySum.add(avgQty);
                        if (dayMap.containsKey(day)){
                            BigDecimal qty = dayMap.get(day).add(avgQty);
                            dayMap.put(day,qty);
                        }else {
                            dayMap.put(day,avgQty);
                        }
                    }

                    System.out.println(avgQtySum);
                    // 最后一天数量 = 平均数 - （平均数总数 - 月总数） ， 保证总数一致
                    dayMap.put(dayList.stream().reduce(Integer::max).get(), avgQty.subtract(avgQtySum.subtract(monthQty)));
                }
                invYearMap.put(key,dayMap);
                invMap.put(cInvCode,iInventoryId);
            }
        }

        //循环年度计划key，如果月度计划存在则跳过，不存在则将年度计划添加进月度
        for (String invYearMonth : invYearMap.keySet()){
            if (invMonthMap.containsKey(invYearMonth)){
                continue;
            }
            Map<Integer,BigDecimal> dayMap = invYearMap.get(invYearMonth);
            invMonthMap.put(invYearMonth,dayMap);
        }


        //TODO:客户订单Map
        //key:inv+2023-01   value:<day,qty>
        Map<String,Map<Integer,BigDecimal>> invCusMap = new HashMap<>();

        //TODO:根据年份获取客户周间订单 相同物料相同年月日汇总
        List<Record> getWeekOrderList = dbTemplate("cusordersum.getWeekOrderList",Kv.by("year",curYear)).find();
        for (Record record : getWeekOrderList){
            Long iInventoryId = record.getLong("iInventoryId");
            String cInvCode = record.getStr("cInvCode");
            String dPlanAogDate = record.getStr("dPlanAogDate");
            BigDecimal iQty = record.getBigDecimal("iQty");

            String yearMonth = dPlanAogDate.substring(0, 7);
            String key = cInvCode + "+" + yearMonth;
            //key:day  value:qty
            Map<Integer,BigDecimal> dayMap = invCusMap.containsKey(key) ? invCusMap.get(key) : new HashMap<>();

            String dayStr = dPlanAogDate.substring(8, 10);
            int day = Integer.parseInt(dayStr);

            BigDecimal dayQty = iQty != null ? iQty : BigDecimal.ZERO;
            if (dayMap.containsKey(day)){
                BigDecimal qty = dayMap.get(day).add(dayQty);
                dayMap.put(day,qty);
            }else {
                dayMap.put(day,dayQty);
            }
            invCusMap.put(key,dayMap);
            invMap.put(cInvCode,iInventoryId);
        }
        //TODO:根据年份获取客户手配订单（相同物料相同年月日汇总）
        List<Record> getManualOrderList = dbTemplate("cusordersum.getManualOrderList",Kv.by("year",curYear)).find();
        for (Record record : getManualOrderList){
            Long iInventoryId = record.getLong("iInventoryId");
            String cInvCode = record.getStr("cInvCode");
            String year = record.getStr("year");
            int month = record.getInt("month");
            String yearMonth = year + "-" + (month < 10 ? "0"+month : month);

            String key = cInvCode + "+" + yearMonth;
            //key:day  value:qty
            Map<Integer,BigDecimal> dayMap = invCusMap.containsKey(key) ? invCusMap.get(key) : new HashMap<>();

            for (int i = 1; i <= 31; i++) {
                int day = i;
                BigDecimal dayQty = record.getBigDecimal("day"+i) != null ? record.getBigDecimal("day"+i) : BigDecimal.ZERO;
                if (dayMap.containsKey(day)){
                    BigDecimal qty = dayMap.get(day).add(dayQty);
                    dayMap.put(day,qty);
                }else {
                    dayMap.put(day,dayQty);
                }
            }
            invCusMap.put(key,dayMap);
            invMap.put(cInvCode,iInventoryId);
        }
        //TODO:根据年份获取客户委外销售订单（相同物料相同年月日汇总）
        List<Record> getSubcontractSaleOrderList = dbTemplate("cusordersum.getSubcontractSaleOrderList",Kv.by("year",curYear)).find();
        for (Record record : getSubcontractSaleOrderList){
            Long iInventoryId = record.getLong("iInventoryId");
            String cInvCode = record.getStr("cInvCode");
            String year = record.getStr("year");
            int month = record.getInt("month");
            String yearMonth = year + "-" + (month < 10 ? "0"+month : month);

            String key = cInvCode + "+" + yearMonth;
            //key:day  value:qty
            Map<Integer,BigDecimal> dayMap = invCusMap.containsKey(key) ? invCusMap.get(key) : new HashMap<>();

            for (int i = 1; i <= 31; i++) {
                int day = i;
                BigDecimal dayQty = record.getBigDecimal("day"+i) != null ? record.getBigDecimal("day"+i) : BigDecimal.ZERO;
                if (dayMap.containsKey(day)){
                    BigDecimal qty = dayMap.get(day).add(dayQty);
                    dayMap.put(day,qty);
                }else {
                    dayMap.put(day,dayQty);
                }
            }
            invCusMap.put(key,dayMap);
            invMap.put(cInvCode,iInventoryId);
        }


        //key:inv+yyyy-MM-dd   value:CusOrderSum
        Map<String,CusOrderSum> orderSumMap = new HashMap<>();

        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date newDate = new Date();
        String orgCode = getOrgCode();
        String orgName = getOrgName();
        Long orgId = getOrgId();
        //客户计划
        for (String invYearMonth : invMonthMap.keySet()){
            //截取
            String inv = invYearMonth.substring(0, invYearMonth.indexOf("+"));
            String yearMonth = invYearMonth.substring(invYearMonth.length() - 7);
            Long invId = invMap.get(inv);
            int year = Integer.parseInt(yearMonth.substring(0,4));
            int month = Integer.parseInt(yearMonth.substring(5,7));

            Map<Integer,BigDecimal> dayQtyMap = invMonthMap.get(invYearMonth);
            for (Integer day : dayQtyMap.keySet()){
                String key = inv + "+" + yearMonth + "-" + (day < 10 ? "0"+day : day);
                BigDecimal qty = dayQtyMap.get(day);

                CusOrderSum cusOrderSum = new CusOrderSum();
                cusOrderSum.setICreateBy(userId);
                cusOrderSum.setCCreateName(userName);
                cusOrderSum.setDCreateTime(newDate);
                cusOrderSum.setIUpdateBy(userId);
                cusOrderSum.setCUpdateName(userName);
                cusOrderSum.setDUpdateTime(newDate);
                cusOrderSum.setCOrgCode(orgCode);
                cusOrderSum.setCOrgName(orgName);
                cusOrderSum.setIOrgId(orgId);
                cusOrderSum.setIInventoryId(invId);
                cusOrderSum.setIYear(year);
                cusOrderSum.setIMonth(month);
                cusOrderSum.setIDate(day);
                cusOrderSum.setIQty1(qty);
                orderSumMap.put(key,cusOrderSum);
            }
        }

        //客户订单
        for (String invYearMonth : invCusMap.keySet()){
            //截取
            String inv = invYearMonth.substring(0, invYearMonth.indexOf("+"));
            String yearMonth = invYearMonth.substring(invYearMonth.length() - 7);
            Long invId = invMap.get(inv);
            int year = Integer.parseInt(yearMonth.substring(0,4));
            int month = Integer.parseInt(yearMonth.substring(5,7));

            Map<Integer,BigDecimal> dayQtyMap = invCusMap.get(invYearMonth);
            for (Integer day : dayQtyMap.keySet()){
                String key = inv + "+" + yearMonth + "-" + (day < 10 ? "0"+day : day);
                BigDecimal qty = dayQtyMap.get(day);

                CusOrderSum cusOrderSum;
                if (orderSumMap.containsKey(key)){
                    cusOrderSum = orderSumMap.get(key);
                    cusOrderSum.setIQty2(qty);
                }else {
                    cusOrderSum = new CusOrderSum();
                    cusOrderSum.setICreateBy(userId);
                    cusOrderSum.setCCreateName(userName);
                    cusOrderSum.setDCreateTime(newDate);
                    cusOrderSum.setIUpdateBy(userId);
                    cusOrderSum.setCUpdateName(userName);
                    cusOrderSum.setDUpdateTime(newDate);
                    cusOrderSum.setCOrgCode(orgCode);
                    cusOrderSum.setCOrgName(orgName);
                    cusOrderSum.setIOrgId(orgId);
                    cusOrderSum.setIInventoryId(invId);
                    cusOrderSum.setIYear(year);
                    cusOrderSum.setIMonth(month);
                    cusOrderSum.setIDate(day);
                    cusOrderSum.setIQty2(qty);
                }
                orderSumMap.put(key,cusOrderSum);
            }
        }

        List<CusOrderSum> cusOrderSumList = new ArrayList<>();
        //计划使用
        for (CusOrderSum cusOrderSum : orderSumMap.values()){
            BigDecimal qty1 = cusOrderSum.getIQty1();
            BigDecimal qty2 = cusOrderSum.getIQty2();
            if (qty2 != null && qty2.compareTo(BigDecimal.ZERO) > 0){
                cusOrderSum.setIQty3(qty2);
            }else {
                cusOrderSum.setIQty3(qty1);
            }
            cusOrderSumList.add(cusOrderSum);
        }
        if (cusOrderSumList.size() == 0){
            tx(() -> {
                delete("DELETE FROM Co_CusOrderSum WHERE iYear >= ? ",curYear);
                return true;
            });
            return SUCCESS;
        }

        tx(() -> {
            delete("DELETE FROM Co_CusOrderSum WHERE iYear >= ? ",curYear);
            List<List<CusOrderSum>> groupCusOrderSumList = CollUtil.split(cusOrderSumList,300);
            CountDownLatch countDownLatch = new CountDownLatch(groupCusOrderSumList.size());
            ExecutorService executorService = Executors.newFixedThreadPool(groupCusOrderSumList.size());
            for(List<CusOrderSum> cusOrderSums :groupCusOrderSumList){
                CusOrderSum cusOrderSum = cusOrderSums.get(0);
                if (cusOrderSum.getIQty1() == null){
                    cusOrderSum.setIQty1(BigDecimal.ZERO);
                }
                if (cusOrderSum.getIQty2() == null){
                    cusOrderSum.setIQty2(BigDecimal.ZERO);
                }
                if (cusOrderSum.getIQty3() == null){
                    cusOrderSum.setIQty3(BigDecimal.ZERO);
                }

                executorService.execute(()->{
                    batchSave(cusOrderSums);
                });
                countDownLatch.countDown();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            executorService.shutdown();

           // batchSave(cusOrderSumList,500);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 审批
     *
     * @param annualOrderM
     * @return
     */
    public Ret approve(AnnualOrderM annualOrderM) {

        List<CusOrderSum> cusOrderSumList = new ArrayList<>();

        CusOrderSum cusOrderSum = createCusOrderSum();

        List<AnnualOrderD> iAnnualOrderDList = annualOrderDService.findByMid(annualOrderM.getIAutoId());
        //年
        cusOrderSum.setIYear(annualOrderM.getIYear());

        //存货编码集合
        Long[] inventoryIds = iAnnualOrderDList.stream().map(AnnualOrderD::getIInventoryId).toArray(Long[]::new);
        //查询参数
        Okv para = Okv.by("inventoryIdList", ArrayUtil.join(inventoryIds, COMMA)).set("iYear", annualOrderM.getIYear());

        //遍历年度计划
        for (AnnualOrderD annualOrderD : iAnnualOrderDList) {
            //存货ID
            cusOrderSum.setIInventoryId(annualOrderD.getIInventoryId());
            //月度计划
            List<Record> records = dbTemplate("monthorderd.cusOrderSumIncCusPlan", para).find();
            //年度计划订单年月金额
            List<AnnualorderdQty> iAnnualOrderDtyList = annualorderdQtyService.findAnnualorderdQty(annualOrderD.getIAutoId(), annualOrderM);
            if (records.size() > 0) {
                for (Record record : records) {
                    //验证月度计划是否存在此存货编码  如有 优先按月度计划存储
                    for (AnnualorderdQty annualorderdQty : iAnnualOrderDtyList) {
                        //月
                        cusOrderSum.setIMonth(annualorderdQty.getIMonth());
                        if (annualorderdQty.getIMonth().equals(record.get("iMonth"))) {
								/*List<CusOrderSum> cusOrderSums = find(selectSql().eq("iYear", record.get("iYear")).
										eq("iMonth", record.get("iMonth")).
										in("iInventoryId", ArrayUtil.join(inventoryIds, COMMA)));
								saveCusOrderSum(record);
								for (int i = 1; i <= 31; i++) {
									cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
									cusOrderSum.setIDate(i);
									cusOrderSum.setIQty1(record.get("iQty" + i));
									cusOrderSum.save();
								}*/
                        } else {
                            CustomerWorkDays customerWorkDays = workDaysService.findByICustomerId(annualOrderM.getICustomerId());
                            Integer workDay = 31;
                            if (null != customerWorkDays) {
                                workDay = customerWorkDays.get("iMonth" + annualorderdQty.getIMonth() + "Days");
                            }
                            BigDecimal divide = annualorderdQty.getIQty().divide(new BigDecimal(workDay), RoundingMode.HALF_UP);
                            for (int i = 1; i <= 31; i++) {
                                cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
                                cusOrderSum.setIDate(i);
                                cusOrderSum.setIQty1(divide);
                                cusOrderSum.save();
                            }
                        }

                    }

                }
            } else {
                //若没有月度计划  按年度计划计算每日计划值
                for (AnnualorderdQty annualorderdQty : iAnnualOrderDtyList) {
                    CustomerWorkDays customerWorkDays = workDaysService.findByICustomerId(annualOrderM.getICustomerId());
                    if (null == customerWorkDays) {
                        //3. 审核不通过
                        annualOrderM.setIAuditStatus(3);
                        //  2. 待审核
                        annualOrderM.setIOrderStatus(2);
                        annualOrderM.update();
                        ValidationUtils.error("请配置客户档案工作天数！");
                    }
                    //月
                    cusOrderSum.setIMonth(annualorderdQty.getIMonth());
                    Integer workDay = customerWorkDays.get("iMonth" + annualorderdQty.getIMonth() + "Days");
                    BigDecimal divide = annualorderdQty.getIQty().divide(new BigDecimal(workDay), RoundingMode.HALF_UP);
                    for (int i = 1; i <= 31; i++) {
                        cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
                        cusOrderSum.setIDate(i);
                        cusOrderSum.setIQty1(divide);
                        cusOrderSum.save();
                    }
                }
            }

        }

        return SUCCESS;
    }

    private CusOrderSum createCusOrderSum() {
        CusOrderSum cusOrderSum = new CusOrderSum();
        //创建信息
        cusOrderSum.setICreateBy(JBoltUserKit.getUserId());
        cusOrderSum.setCCreateName(JBoltUserKit.getUserName());
        cusOrderSum.setDCreateTime(new Date());

        //更新信息
        cusOrderSum.setIUpdateBy(JBoltUserKit.getUserId());
        cusOrderSum.setCUpdateName(JBoltUserKit.getUserName());
        cusOrderSum.setDUpdateTime(new Date());

        //组织信息
        cusOrderSum.setCOrgCode(getOrgCode());
        cusOrderSum.setCOrgName(getOrgName());
        cusOrderSum.setIOrgId(getOrgId());
        return cusOrderSum;
    }

    public Object findCusOrderSum(Integer pageNumber, Integer pageSize, Kv para) throws ParseException {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //结束日期
        String end = sdf.format(para.getDate("endDate"));
        String begin = sdf.format(para.getDate("beginDate"));

        Date endDate = new SimpleDateFormat("yyyy-MM").parse(end);//定义结束日期
        Date beginDate = new SimpleDateFormat("yyyy-MM").parse(begin);//定义起始日期

        //起始日期
        calendar.setTime(beginDate);
        List<String> months = new ArrayList<>();
        Map<String, String> dateMap = new HashMap<>();


        while (calendar.getTime().compareTo(endDate) <= 0) {
            // 年月 - 年-月-日
            String str = sdf.format(calendar.getTime());
            String[] strArr = str.split("-");
            months.add(strArr[1]);
            if ("12".equals(strArr[1])) {
                dateMap.put(strArr[0], StringUtils.join(months, COMMA));
                months.clear();

            } else if (end.split("-")[0].equals(strArr[0]) && end.split("-")[1].equals(strArr[1])) {
                dateMap.put(strArr[0], StringUtils.join(months, COMMA));
            }
            calendar.add(Calendar.MONTH, 1);
        }

        para.set("dateMap", dateMap);
        Page<Record> pageData = dbTemplate("cusordersum.paginateAdminDatas", para).paginate(pageNumber, pageSize);
        String str = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31";

        para.set("roleArray", str);
        if (pageData.getTotalRow() > 0) {
            for (Record record : pageData.getList()) {
                String iinventoryid = record.getStr("iinventoryid");
                para.set("iInventoryId", iinventoryid);
                List<Record> record1 = dbTemplate("cusordersum.getiQty1", para).find();
                List<Record> record2 = dbTemplate("cusordersum.getiQty2", para).find();
                List<Record> record3 = dbTemplate("cusordersum.getiQty3", para).find();
                /*List<CusOrderSum> cusOrderSums = find(selectSql().eq("iYear", record.getStr("iYear")).
                        eq("iInventoryId", iinventoryid).isNotNull("iQty2"));
                if (cusOrderSums.size() > 0) {
                    record.set("record3", record2);
                } else {
                    record.set("record3", record1);
                }*/
                record.set("record1", record1);
                record.set("record2", record2);
                record.set("record3", record3);
            }
        }
        return pageData;
    }

    private CusOrderSum findDistinctByInYMD(Long iInventoryId, int year, int month, int date) {
        return findFirst(selectSql().eq("iInventoryId", iInventoryId)
                .eq("iYear", year).eq("iMonth", month).eq("iDate", date));
    }

    public void handelSubcontractsaleorderd(Subcontractsaleorderm subcontractsaleorderm, List<Subcontractsaleorderd> subcontractsaleorderds) {
        algorithmSum();
        /*for (Subcontractsaleorderd subcontractsaleorderd : subcontractsaleorderds) {
            List<CusOrderSum> cusOrderSums = find(selectSql().eq("iInventoryId", subcontractsaleorderd.getIInventoryId())
                    .eq("iYear", subcontractsaleorderm.getIYear()).eq("iMonth", subcontractsaleorderm.getIMonth()).orderBy(OrderBy.asc("iDate")));

            CusOrderSum cusOrderSum = createCusOrderSum();
            cusOrderSum.setIYear(subcontractsaleorderm.getIYear());
            cusOrderSum.setIMonth(subcontractsaleorderm.getIMonth());

            boolean mark = true;
            int size = cusOrderSums.size();
            for (int i = 1; i <= 31; i++) {
                BigDecimal IQty = subcontractsaleorderd.get("iQty" + i);
                if (null != IQty) {
                    if (size > 0) {
                        for (CusOrderSum orderSum : cusOrderSums) {
                            if (orderSum.getIDate() == i) {
                                BigDecimal iQty = subcontractsaleorderd.get("iQty" + orderSum.getIDate());
                                BigDecimal iQty2 = orderSum.getIQty2();
                                if (iQty2 != null && null != iQty) {
                                    orderSum.setIQty2(orderSum.getIQty2().add(iQty));
                                } else {
                                    orderSum.setIQty2(iQty);
                                }
                                //更新信息
                                orderSum.setIUpdateBy(JBoltUserKit.getUserId());
                                orderSum.setCUpdateName(JBoltUserKit.getUserName());
                                orderSum.setDUpdateTime(new Date());
                                orderSum.update();
                                mark = false;
                                size = size - 1;
                            }
                        }
                        if (mark) {
                            cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
                            cusOrderSum.setIDate(i);
                            cusOrderSum.setIQty2(IQty);
                            cusOrderSum.setIInventoryId(subcontractsaleorderd.getIInventoryId());
                            cusOrderSum.save();
                        }
                    } else {
                        cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
                        cusOrderSum.setIDate(i);
                        cusOrderSum.setIQty2(IQty);
                        cusOrderSum.setIInventoryId(subcontractsaleorderd.getIInventoryId());
                        cusOrderSum.save();
                    }
                }
            }
        }*/
    }
}