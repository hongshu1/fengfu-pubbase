package cn.rjtech.admin.scheduproductplan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.apsannualpland.ApsAnnualplandService;
import cn.rjtech.admin.apsannualplandqty.ApsAnnualplandQtyService;
import cn.rjtech.model.momdata.ApsAnnualpland;
import cn.rjtech.model.momdata.ApsAnnualplandQty;
import cn.rjtech.model.momdata.ApsAnnualplanm;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.service.func.mom.MomDataFuncService;
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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 生产计划排程 Service
 * @ClassName: ScheduProductPlanYearService
 * @author: chentao
 * @date: 2023-03-30 11:26
 */
public class ScheduProductPlanYearService extends BaseService<ApsAnnualplanm>  implements IApprovalService {

    private final ApsAnnualplanm dao = new ApsAnnualplanm().dao();

    @Override
    protected ApsAnnualplanm dao() {
        return dao;
    }
    
    @Inject
    private MomDataFuncService momDataFuncService;
    @Inject
    private ApsAnnualplandService apsAnnualplandService;
    @Inject
    private ApsAnnualplandQtyService apsAnnualplandQtyService;

    /**
     * 后台管理分页查询
     */
    public Page<ApsAnnualplanm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
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
     */
    public Ret deleteByBatchIds(String ids) {
        String[] idsArray = ids.split(",");
        update("UPDATE Aps_AnnualPlanM SET isDeleted = 1 WHERE iAutoId IN ("+ ArrayUtil.join(idsArray,",")+")");
        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        //deleteById(id,true);
        update("UPDATE Aps_AnnualPlanM SET isDeleted = 1 WHERE iAutoId = ? ",id);
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     * @param apsAnnualplanm 要删除的model
     * @param kv 携带额外参数一般用不上
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
     */
    @Override
    public String checkCanDelete(ApsAnnualplanm apsAnnualplanm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(apsAnnualplanm, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
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
     */
    @Override
    public String checkInUse(ApsAnnualplanm apsAnnualplanm, Kv kv) {
        //这里用来覆盖 检测ApsAnnualplanm是否被其它表引用
        return null;
    }

    public String getEndYear(String startYear){
        Calendar calendar = Calendar.getInstance();
        Date date = cn.rjtech.util.DateUtils.parseDate(startYear);
        calendar.setTime(date);
        //第二个年份
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        calendar.add(Calendar.YEAR,1);
        return sdf.format(calendar.getTime());
    }
    
    public String getLastYear(String startYear){
        Calendar calendar = Calendar.getInstance();
        Date date = cn.rjtech.util.DateUtils.parseDate(startYear);
        calendar.setTime(date);
        //上一个年份
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        calendar.add(Calendar.YEAR,-1);
        return sdf.format(calendar.getTime());
    }

    //-----------------------------------------------------------------年度生产计划排产-----------------------------------------------

    public List<Record> getCustomerList() {
        return findRecord("SELECT iAutoId,cCusName FROM Bd_Customer WHERE isDeleted = '0'");
    }

    public Page<Record> getApsYearPlanMasterPage(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("scheduproductplan.getApsYearPlanMasterList",kv).paginate(pageNumber,pageSize);
    }

    /**
     * 年度生产计划逻辑处理
     */
    public synchronized List<ScheduProductYearViewDTO> scheduPlanYear(Kv kv) {
        //年度生产计划集合
        List<ScheduProductYearViewDTO> scheduProductPlanYearList = new ArrayList<>();

        //tx(() -> {
            //return true;
        //});
        try {
            String startYear = kv.getStr("startyear");    //年份
            Long customerId = kv.getLong("customerId");      //客户ids
            //判断料号+设备+车型+供应商不能为空
            if (notOk(startYear) || notOk(customerId)){
                ValidationUtils.error("查询条件不能为空!");
            }
            String endYear = getEndYear(startYear);
            String lastYear = getLastYear(startYear);

            Long organizeId = getOrgId();
            String CC = "CC";  //客户行事历
            String PP = "PP";    //计划使用
            String CP = "CP";    //计划数量
            String ZK = "ZK";    //计划在库

            //TODO: 所有客户的年度每月工作日集合  key：客户id，value：<年份，客户每月工作天数>
            Map<Long,Map<String,Record>> cusWorkMonthNumListMap = new HashMap<>();
            //根据客户id查询客户年度每月工作天数
            List<Record> cusWorkMonthNumList = getCusWorkMonthNumList(organizeId,customerId,startYear,endYear);
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
            List<Record> sourceYearOrderList = getSourceYearOrderList(Okv.by("organizeid",organizeId).set("customerids",customerId).set("startyear",startYear));
            if (sourceYearOrderList.size() < 1){
                ValidationUtils.error("该客户无订单计划！");
                //throw new RuntimeException("该客户无订单计划！");
            }

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
            //TODO:查询物料集期初在库
            List<Record> lastYearZKQtyList = getLastYearZKQtyList(Kv.by("lastyear",lastYear).set("customerids",customerId).set("invids",invIds.toString()));
            Map<String,BigDecimal> lastYearZKQtyMap = new HashMap<>();
            for (Record record : lastYearZKQtyList){
                String cInvCode = record.getStr("cInvCode");
                BigDecimal iQty = record.getBigDecimal("iQty");
                lastYearZKQtyMap.put(cInvCode,iQty);
            }


            //CC:客户行事历（客户每月工作天数）
            Map<String,Record> cusWorkMonthNumMap = cusWorkMonthNumListMap.get(customerId) != null ? cusWorkMonthNumListMap.get(customerId) : new HashMap<>();
            //CC:客户行事历
            ScheduProductYearViewDTO productYearViewCC = getProductYearViewCC(CC,startYear,endYear,cusWorkMonthNumMap);
            //scheduProductPlanYearList.add(productYearViewCC);

            //TODO:查询本次所有客户的订单计划
            for (Record record : sourceYearOrderList) {
                Long iInventoryId = record.getLong("iInventoryId");
                String cInvCode = record.getStr("cInvCode");
                //物料信息
                Record invInfo = invInfoMap.get(iInventoryId);
                //期初在库
                BigDecimal safetyStock = lastYearZKQtyMap.get(cInvCode) != null ? lastYearZKQtyMap.get(cInvCode) : BigDecimal.ZERO;

                //PP:计划使用（来源客户计划汇总中的客户计划）
                ScheduProductYearViewDTO productYearViewPP = getProductYearViewPP(record,PP,startYear,endYear);
                scheduProductPlanYearList.add(productYearViewPP);

                //ZK:计划在库（下月计划使用/下月客户行事历*(最低在库天数+最高在库天数)/2）
                ScheduProductYearViewDTO productYearViewZK = getProductYearViewZK(record,ZK,startYear,endYear,productYearViewPP,productYearViewCC,invInfo);
                productYearViewZK.setNowmonth0(safetyStock);

                //CP:计划数量（当月计划在库+当月计划使用-上月计划在库）
                ScheduProductYearViewDTO productYearViewCP = getProductYearViewCP(record,CP,startYear,endYear,productYearViewPP,productYearViewZK);

                scheduProductPlanYearList.add(productYearViewCP);
                scheduProductPlanYearList.add(productYearViewZK);
            }
            //saveScheduPlan(startYear,scheduProductPlanYearList);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return scheduProductPlanYearList;
    }

    /**
     * CC:客户行事历
     * @param cusWorkMonthNumMap  客户工作天数 <年份，客户每月工作天数>
     */
    public ScheduProductYearViewDTO getProductYearViewCC(String CC,String startYear,String endYear, Map<String,Record> cusWorkMonthNumMap){
        //CC:客户行事历
        ScheduProductYearViewDTO productYearViewCC = new ScheduProductYearViewDTO();
        try {
            productYearViewCC.setPlanTypeCode(CC);
            //客户工作天数
            Record cusWorkMonthStart = cusWorkMonthNumMap.get(startYear) != null ? cusWorkMonthNumMap.get(startYear) : new Record();
            Record cusWorkMonthEnd = cusWorkMonthNumMap.get(endYear) != null ? cusWorkMonthNumMap.get(endYear) : new Record();

            productYearViewCC.setNowyear(startYear);
            BigDecimal nowMonthSum = BigDecimal.ZERO;
            BigDecimal nextMonthSum = BigDecimal.ZERO;
            for (int i = 1; i <= 12; i++) {
                //当前年
                BigDecimal startNum = cusWorkMonthStart.get("month" + i) != null ? cusWorkMonthStart.getBigDecimal("month" + i) : BigDecimal.ZERO;
                productYearViewCC.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewCC, startNum);
                nowMonthSum = nowMonthSum.add(startNum);
                //下一年
                if (i <= 4){
                    //客户工作天数
                    BigDecimal endNum = cusWorkMonthEnd.get("month" + i) != null ? cusWorkMonthEnd.getBigDecimal("month" + i) : BigDecimal.ZERO;
                    productYearViewCC.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewCC, endNum);
                    if (i <= 3){
                        nextMonthSum = nextMonthSum.add(endNum);
                    }
                }
            }
            productYearViewCC.setNowMonthSum(nowMonthSum);
            productYearViewCC.setNextyear(endYear);
            productYearViewCC.setNextMonthSum(nextMonthSum);
        }catch (Exception e){
            throw new RuntimeException("客户行事日历计算出错！"+e.getMessage());
        }
        return productYearViewCC;
    }
    /**
     * PP:计划使用数
     */
    public ScheduProductYearViewDTO getProductYearViewPP(Record record,String PP,String startYear,String endYear){
        //PP:计划使用数
        ScheduProductYearViewDTO productYearViewPP = new ScheduProductYearViewDTO();
        try {
            productYearViewPP.setPlanTypeCode(PP);
            productYearViewPP.setiCustomerId(record.getLong("iCustomerId"));
            productYearViewPP.setcCusCode(record.get("cCusCode"));
            productYearViewPP.setcCusName(record.get("cCusName"));
            productYearViewPP.setiEquipmentModelId(record.getLong("iEquipmentModelId"));
            productYearViewPP.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
            productYearViewPP.setcEquipmentModelName(record.get("cEquipmentModelName"));
            productYearViewPP.setiInventoryId(record.getLong("iInventoryId"));
            productYearViewPP.setcInvCode(record.get("cInvCode"));
            productYearViewPP.setcInvCode1(record.get("cInvCode1"));
            productYearViewPP.setcInvName1(record.get("cInvName1"));
            productYearViewPP.setNowyear(record.getStr("nowyear"));

            BigDecimal nextMonthSum = BigDecimal.ZERO;
            for (int i = 1; i <= 12; i++) {
                //当前年
                BigDecimal nowmonth = record.get("nowmonth" + i);
                productYearViewPP.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewPP, nowmonth);
                //下一年
                if (i <= 4){
                    BigDecimal nextmonth = record.get("nextmonth" + i) != null ? record.get("nextmonth" + i) : BigDecimal.ZERO;
                    productYearViewPP.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewPP, nextmonth);
                    if (i <= 3){
                        nextMonthSum = nextMonthSum.add(nextmonth);
                    }
                }
            }
            productYearViewPP.setNowMonthSum(record.getBigDecimal("nowMonthSum"));
            productYearViewPP.setNextyear(record.getStr("nextyear"));
            productYearViewPP.setNextMonthSum(nextMonthSum);
        }catch (Exception e){
            throw new RuntimeException("计划使用计算出错！"+e.getMessage());
        }
        return productYearViewPP;
    }
    /**
     * ZK:计划在库
     */
    public ScheduProductYearViewDTO getProductYearViewZK(Record record,String ZK,String startYear,String endYear,
                                                         ScheduProductYearViewDTO productYearViewPP,
                                                         ScheduProductYearViewDTO productYearViewCC,
                                                         Record invInfo){
        //最低在库天数
        int iMinInStockDays = invInfo.getInt("iMinInStockDays") != null ? invInfo.getInt("iMinInStockDays") : 1;
        //最高在库天数
        int iMaxInStockDays = invInfo.getInt("iMaxInStockDays") != null ? invInfo.getInt("iMaxInStockDays") : 1;
        BigDecimal sumStockDays = new BigDecimal((iMinInStockDays + iMaxInStockDays)) ;

        //PP:计划使用数
        ScheduProductYearViewDTO productYearViewZK = new ScheduProductYearViewDTO();
        try {
            productYearViewZK.setPlanTypeCode(ZK);
            productYearViewZK.setiCustomerId(record.getLong("iCustomerId"));
            productYearViewZK.setcCusCode(record.get("cCusCode"));
            productYearViewZK.setcCusName(record.get("cCusName"));
            productYearViewZK.setiEquipmentModelId(record.getLong("iEquipmentModelId"));
            productYearViewZK.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
            productYearViewZK.setcEquipmentModelName(record.get("cEquipmentModelName"));
            productYearViewZK.setiInventoryId(record.getLong("iInventoryId"));
            productYearViewZK.setcInvCode(record.get("cInvCode"));
            productYearViewZK.setcInvCode1(record.get("cInvCode1"));
            productYearViewZK.setcInvName1(record.get("cInvName1"));
            productYearViewZK.setNowyear(startYear);

            BigDecimal nowMonthSum = BigDecimal.ZERO;
            BigDecimal nextMonthSum = BigDecimal.ZERO;
            for (int i = 1; i <= 12; i++) {
                //计算当前年
                {
                    //下月计划使用
                    BigDecimal pp = BigDecimal.ZERO;
                    if (i == 12){
                        pp = productYearViewPP.getNextmonth1();
                    }else {
                        Object ppnum = productYearViewPP.getClass().getMethod("getNowmonth"+(i + 1),new Class[]{}).invoke(productYearViewPP, new Object[]{});
                        if (ppnum != null){
                            pp = (BigDecimal) ppnum;
                        }
                    }
                    //下月客户行事历
                    BigDecimal cc = BigDecimal.ZERO;
                    if (i == 12){
                        cc = productYearViewCC.getNextmonth1();
                    }else {
                        Object ccnum = productYearViewCC.getClass().getMethod("getNowmonth"+(i + 1),new Class[]{}).invoke(productYearViewCC);
                        if (ccnum != null){
                            cc = (BigDecimal) ccnum;
                        }
                    }
                    cc = cc.compareTo(BigDecimal.ZERO) > 0 ? cc : BigDecimal.ONE;
                    //下月计划使用/下月客户行事历*(最低在库天数+最高在库天数)/2
                    BigDecimal qty = pp.divide(cc,0, RoundingMode.UP).multiply(sumStockDays).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
                    productYearViewZK.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewZK, qty);
                    nowMonthSum = nowMonthSum.add(qty);
                }
                //计算下一年
                if (i <= 3){
                    //下月计划使用
                    BigDecimal pp = BigDecimal.ZERO;
                    Object ppnum = productYearViewPP.getClass().getMethod("getNextmonth"+(i + 1),new Class[]{}).invoke(productYearViewPP);
                    if (ppnum != null){
                        pp = (BigDecimal) ppnum;
                    }
                    //下月客户行事历
                    BigDecimal cc = BigDecimal.ZERO;
                    Object ccnum = productYearViewCC.getClass().getMethod("getNextmonth"+(i + 1),new Class[]{}).invoke(productYearViewCC);
                    if (ccnum != null){
                        cc = (BigDecimal) ccnum;
                    }
                    cc = cc.compareTo(BigDecimal.ZERO) > 0 ? cc : BigDecimal.ONE;
                    //下月计划使用/下月客户行事历*(最低在库天数+最高在库天数)/2
                    BigDecimal qty = pp.divide(cc,0, RoundingMode.UP).multiply(sumStockDays).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
                    productYearViewZK.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewZK, qty);
                    nextMonthSum = nextMonthSum.add(qty);
                }
            }
            productYearViewZK.setNowMonthSum(nowMonthSum);
            productYearViewZK.setNextyear(endYear);
            productYearViewZK.setNextMonthSum(nextMonthSum);
        }catch (Exception e){
            throw new RuntimeException("计划在库计算出错！"+e.getMessage());
        }
        return productYearViewZK;
    }
    /**
     * CP:计划数量
     */
    public ScheduProductYearViewDTO getProductYearViewCP(Record record,String CP,String startYear,String endYear,
                                                         ScheduProductYearViewDTO productYearViewPP,
                                                         ScheduProductYearViewDTO productYearViewZK){
        //PP:计划使用数
        ScheduProductYearViewDTO productYearViewCP = new ScheduProductYearViewDTO();
        try {
            productYearViewCP.setPlanTypeCode(CP);
            productYearViewCP.setiCustomerId(record.getLong("iCustomerId"));
            productYearViewCP.setcCusCode(record.get("cCusCode"));
            productYearViewCP.setcCusName(record.get("cCusName"));
            productYearViewCP.setiEquipmentModelId(record.getLong("iEquipmentModelId"));
            productYearViewCP.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
            productYearViewCP.setcEquipmentModelName(record.get("cEquipmentModelName"));
            productYearViewCP.setiInventoryId(record.getLong("iInventoryId"));
            productYearViewCP.setcInvCode(record.get("cInvCode"));
            productYearViewCP.setcInvCode1(record.get("cInvCode1"));
            productYearViewCP.setcInvName1(record.get("cInvName1"));
            productYearViewCP.setNowyear(startYear);

            BigDecimal nowMonthSum = BigDecimal.ZERO;
            BigDecimal nextMonthSum = BigDecimal.ZERO;
            for (int i = 1; i <= 12; i++) {
                //计算当前年
                {
                    //当月计划在库
                    BigDecimal zk = BigDecimal.ZERO;
                    Object zknum = productYearViewZK.getClass().getMethod("getNowmonth"+(i),new Class[]{}).invoke(productYearViewZK, new Object[]{});
                    if (zknum != null){
                        zk = (BigDecimal) zknum;
                    }
                    //当月计划使用
                    BigDecimal pp = BigDecimal.ZERO;
                    Object ppnum = productYearViewPP.getClass().getMethod("getNowmonth"+(i),new Class[]{}).invoke(productYearViewPP, new Object[]{});
                    if (ppnum != null){
                        pp = (BigDecimal) ppnum;
                    }
                    //上月计划在库
                    BigDecimal prezk = BigDecimal.ZERO;
                    Object prezknum = productYearViewZK.getClass().getMethod("getNowmonth"+(i - 1),new Class[]{}).invoke(productYearViewZK, new Object[]{});
                    if (prezknum != null){
                        prezk = (BigDecimal) prezknum;
                    }
                    //当月计划在库+当月计划使用-上月计划在库
                    BigDecimal qty = zk.add(pp).subtract(prezk);
                    productYearViewCP.getClass().getMethod("setNowmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewCP, qty);
                    nowMonthSum = nowMonthSum.add(qty);
                }
                //计算下一年
                if (i <= 3){
                    //当月计划在库
                    BigDecimal zk = BigDecimal.ZERO;
                    Object zknum = productYearViewZK.getClass().getMethod("getNextmonth"+(i),new Class[]{}).invoke(productYearViewZK, new Object[]{});
                    if (zknum != null){
                        zk = (BigDecimal) zknum;
                    }
                    //当月计划使用
                    BigDecimal pp = BigDecimal.ZERO;
                    Object ppnum = productYearViewPP.getClass().getMethod("getNextmonth"+(i),new Class[]{}).invoke(productYearViewPP, new Object[]{});
                    if (ppnum != null){
                        pp = (BigDecimal) ppnum;
                    }
                    //上月计划在库
                    BigDecimal prezk = BigDecimal.ZERO;
                    if (i == 1){
                        prezk = productYearViewZK.getNowmonth12();
                    }else {
                        Object prezknum = productYearViewZK.getClass().getMethod("getNextmonth"+(i - 1),new Class[]{}).invoke(productYearViewZK, new Object[]{});
                        if (prezknum != null){
                            prezk = (BigDecimal) prezknum;
                        }
                    }
                    //当月计划在库+当月计划使用-上月计划在库
                    BigDecimal qty = zk.add(pp).subtract(prezk);
                    productYearViewCP.getClass().getMethod("setNextmonth"+i, new Class[]{BigDecimal.class}).invoke(productYearViewCP, qty);
                    nextMonthSum = nextMonthSum.add(qty);
                }
            }
            productYearViewCP.setNowMonthSum(nowMonthSum);
            productYearViewCP.setNextyear(endYear);
            productYearViewCP.setNextMonthSum(nextMonthSum);
        }catch (Exception e){
            throw new RuntimeException("计划数量计算出错！"+e.getMessage());
        }
        return productYearViewCP;
    }

    /**
     * 保存计划 列转行
     */
    public Ret saveScheduPlan(String startYear,List<ScheduProductYearViewDTO> scheduProductPlanYearList) {

        List<ScheduProductYearViewDTO> scheduList = scheduProductPlanYearList;
        if (scheduProductPlanYearList.size() < 1){
            return SUCCESS;
        }
        String CC = "CC";  //客户行事历
        String PP = "PP";    //计划使用
        String CP = "CP";    //计划数量
        String ZK = "ZK";    //计划在库

        //计划单号
        String dateStr = DateUtils.formatDate(new Date(),"yyyyMMdd");
        String planNo = momDataFuncService.getNextRouteNo(1L, "JH"+dateStr, 2);
        Long mId = JBoltSnowflakeKit.me.nextId();

        //主表
        ApsAnnualplanm annualplanm = new ApsAnnualplanm();
        annualplanm.setIAutoId(mId);
        annualplanm.setIOrgId(getOrgId());
        annualplanm.setCOrgCode(getOrgCode());
        annualplanm.setCOrgName(getOrgName());
        annualplanm.setICustomerId(scheduList.get(1).getiCustomerId());
        annualplanm.setIYear(Integer.parseInt(startYear));
        annualplanm.setCPlanOrderNo(planNo);
        annualplanm.setIPlanOrderStatus(0);//订单状态
        annualplanm.setIAuditStatus(0);//审核状态
        annualplanm.setDAuditTime(null);//审核时间
        annualplanm.setICreateBy(JBoltUserKit.getUserId());
        annualplanm.setCCreateName(JBoltUserKit.getUserName());
        annualplanm.setDCreateTime(new Date());
        annualplanm.setIUpdateBy(null);
        annualplanm.setCUpdateName(null);
        annualplanm.setDUpdateTime(null);


        //机型物料子表集 key:机型+物料
        Map<String,ApsAnnualpland> annualplandMap = new HashMap<>();
        //数量明细子表集
        List<ApsAnnualplandQty> annualplandQtyList = new ArrayList<>();

        try {
            for (ScheduProductYearViewDTO viewDTO : scheduList){
                String planTypeCode = viewDTO.getPlanTypeCode();
                if (planTypeCode.equals(CC)){
                    continue;
                }
                Long iEquipmentModelId = viewDTO.getiEquipmentModelId();
                Long iInventoryId = viewDTO.getiInventoryId();
                String nowyear = viewDTO.getNowyear();
                String nextyear = viewDTO.getNextyear();
                Integer nowMonthSum = viewDTO.getNowMonthSum().intValue();
                Integer nextMonthSum = viewDTO.getNextMonthSum().intValue();

                String key = (iEquipmentModelId != null ? iEquipmentModelId : "") + iInventoryId.toString();
                //机型物料子表
                ApsAnnualpland annualpland = annualplandMap.containsKey(key)  ? annualplandMap.get(key) : new ApsAnnualpland();
                Long dId;
                if (annualplandMap.containsKey(key)){
                    dId = annualpland.getIAutoId();
                }else {
                    dId = JBoltSnowflakeKit.me.nextId();
                }
                annualpland.setIAutoId(dId);
                annualpland.setIAnnualPlanMid(mId);
                annualpland.setIEquipmentModelId(iEquipmentModelId);
                annualpland.setIInventoryId(iInventoryId);
                Integer type = null;
                if (planTypeCode.equals(PP)){
                    annualpland.setIYear11Qty(nowMonthSum);//计划使用 第一年
                    annualpland.setIYear21Qty(nextMonthSum);//计划使用 下一年
                    type = 1;
                }
                if (planTypeCode.equals(CP)){
                    annualpland.setIYear12Qty(nowMonthSum);//计划数量 第一年
                    annualpland.setIYear22Qty(nextMonthSum);//计划数量 下一年
                    type = 2;
                }
                if (planTypeCode.equals(ZK)){
                    annualpland.setIYear13Qty(nowMonthSum);//计划在库 第一年
                    annualpland.setIYear23Qty(nextMonthSum);//计划在库 下一年
                    type = 3;
                }
                annualplandMap.put(key,annualpland);

                for (int i = 1; i <= 12; i++) {
                    BigDecimal ppNum = (BigDecimal) viewDTO.getClass().getMethod("getNowmonth"+(i), new Class[]{}).invoke(viewDTO, new Object[]{});
                    ApsAnnualplandQty annualplandQty = new ApsAnnualplandQty();
                    annualplandQty.setIAutoId(JBoltSnowflakeKit.me.nextId());
                    annualplandQty.setIAnnualPlanDid(dId);
                    annualplandQty.setIType(type);
                    annualplandQty.setIYear(Integer.valueOf(nowyear));
                    annualplandQty.setIMonth(i);
                    annualplandQty.setIQty(ppNum.intValue());
                    annualplandQtyList.add(annualplandQty);
                    if (i <= 3){
                        BigDecimal ppNum2 = (BigDecimal) viewDTO.getClass().getMethod("getNextmonth"+(i), new Class[]{}).invoke(viewDTO, new Object[]{});
                        ApsAnnualplandQty annualplandQty2 = new ApsAnnualplandQty();
                        annualplandQty2.setIAutoId(JBoltSnowflakeKit.me.nextId());
                        annualplandQty2.setIAnnualPlanDid(dId);
                        annualplandQty2.setIType(type);
                        annualplandQty2.setIYear(Integer.valueOf(nextyear));
                        annualplandQty2.setIMonth(i);
                        annualplandQty2.setIQty(ppNum2.intValue());
                        annualplandQtyList.add(annualplandQty2);
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        if (annualplandQtyList.size() > 0){
            ApsAnnualplandQty annualplandQty = annualplandQtyList.get(0);
            if (annualplandQty.getIQty() == null){
                annualplandQty.setIQty(0);
            }
        }

        tx(() -> {
            annualplanm.save();
            List<ApsAnnualpland> annualplandList = new ArrayList<>(annualplandMap.values());
            apsAnnualplandService.batchSave(annualplandList);
            //apsAnnualplandQtyService.batchSave(annualplandQtyList);
            if (annualplandQtyList.size() > 0) {
                List<List<ApsAnnualplandQty>> groupList = CollUtil.split(annualplandQtyList, 300);
                CountDownLatch countDownLatch = new CountDownLatch(groupList.size());
                ExecutorService executorService = Executors.newFixedThreadPool(groupList.size());
                for (List<ApsAnnualplandQty> dataList : groupList) {
                    executorService.execute(() -> {
                        apsAnnualplandQtyService.batchSave(dataList);
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
        return SUCCESS;
    }

    /**
     * 编辑计划 列转行
     */
    public Ret updateScheduPlan(List<ScheduProductYearViewDTO> scheduProductPlanYearList,Long iAnnualPlanMid) {

        List<ScheduProductYearViewDTO> scheduList = scheduProductPlanYearList;
        if (scheduProductPlanYearList.size() < 1){
            return SUCCESS;
        }
        String CC = "CC";  //客户行事历
        String PP = "PP";    //计划使用
        String CP = "CP";    //计划数量
        String ZK = "ZK";    //计划在库

        Long mId = iAnnualPlanMid;


        //机型物料子表集 key:机型+物料
        Map<String,ApsAnnualpland> annualplandMap = new HashMap<>();
        //数量明细子表集
        List<ApsAnnualplandQty> annualplandQtyList = new ArrayList<>();

        List<Long> invIdList = new ArrayList<>();
        try {
            for (ScheduProductYearViewDTO viewDTO : scheduList){
                String planTypeCode = viewDTO.getPlanTypeCode();
                if (planTypeCode.equals(CC)){
                    continue;
                }
                Long iEquipmentModelId = viewDTO.getiEquipmentModelId();
                Long iInventoryId = viewDTO.getiInventoryId();
                String nowyear = viewDTO.getNowyear();
                String nextyear = viewDTO.getNextyear();
                Integer nowMonthSum = viewDTO.getNowMonthSum().intValue();
                Integer nextMonthSum = viewDTO.getNextMonthSum().intValue();

                String key = (iEquipmentModelId != null ? iEquipmentModelId : "") + iInventoryId.toString();
                //机型物料子表
                ApsAnnualpland annualpland = annualplandMap.containsKey(key)  ? annualplandMap.get(key) : new ApsAnnualpland();
                Long dId;
                if (annualplandMap.containsKey(key)){
                    dId = annualpland.getIAutoId();
                }else {
                    dId = JBoltSnowflakeKit.me.nextId();
                }
                annualpland.setIAutoId(dId);
                annualpland.setIAnnualPlanMid(mId);
                annualpland.setIEquipmentModelId(iEquipmentModelId);
                annualpland.setIInventoryId(iInventoryId);
                Integer type = null;
                if (planTypeCode.equals(PP)){
                    annualpland.setIYear11Qty(nowMonthSum);//计划使用 第一年
                    annualpland.setIYear21Qty(nextMonthSum);//计划使用 下一年
                    type = 1;
                }
                if (planTypeCode.equals(CP)){
                    annualpland.setIYear12Qty(nowMonthSum);//计划数量 第一年
                    annualpland.setIYear22Qty(nextMonthSum);//计划数量 下一年
                    type = 2;
                }
                if (planTypeCode.equals(ZK)){
                    annualpland.setIYear13Qty(nowMonthSum);//计划在库 第一年
                    annualpland.setIYear23Qty(nextMonthSum);//计划在库 下一年
                    type = 3;
                }
                annualplandMap.put(key,annualpland);
                invIdList.add(iInventoryId);

                for (int i = 1; i <= 12; i++) {
                    BigDecimal ppNum = (BigDecimal) viewDTO.getClass().getMethod("getNowmonth"+(i), new Class[]{}).invoke(viewDTO, new Object[]{});
                    ApsAnnualplandQty annualplandQty = new ApsAnnualplandQty();
                    annualplandQty.setIAutoId(JBoltSnowflakeKit.me.nextId());
                    annualplandQty.setIAnnualPlanDid(dId);
                    annualplandQty.setIType(type);
                    annualplandQty.setIYear(Integer.valueOf(nowyear));
                    annualplandQty.setIMonth(i);
                    annualplandQty.setIQty(ppNum.intValue());
                    annualplandQtyList.add(annualplandQty);
                    if (i <= 3){
                        BigDecimal ppNum2 = (BigDecimal) viewDTO.getClass().getMethod("getNextmonth"+(i), new Class[]{}).invoke(viewDTO, new Object[]{});
                        ApsAnnualplandQty annualplandQty2 = new ApsAnnualplandQty();
                        annualplandQty2.setIAutoId(JBoltSnowflakeKit.me.nextId());
                        annualplandQty2.setIAnnualPlanDid(dId);
                        annualplandQty2.setIType(type);
                        annualplandQty2.setIYear(Integer.valueOf(nextyear));
                        annualplandQty2.setIMonth(i);
                        annualplandQty2.setIQty(ppNum2.intValue());
                        annualplandQtyList.add(annualplandQty2);
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        if (annualplandQtyList.size() > 0){
            ApsAnnualplandQty annualplandQty = annualplandQtyList.get(0);
            if (annualplandQty.getIQty() == null){
                annualplandQty.setIQty(0);
            }
        }

        tx(() -> {
            delete("DELETE FROM Aps_AnnualPlanD_Qty WHERE iAnnualPlanDid IN (SELECT iAutoId FROM Aps_AnnualPlanD WHERE iAnnualPlanMid = ? AND iInventoryId IN (" + ArrayUtil.join(invIdList.toArray(), COMMA) + "))", mId);
            delete("DELETE FROM Aps_AnnualPlanD WHERE iAnnualPlanMid = ? AND iInventoryId IN (" + ArrayUtil.join(invIdList.toArray(), COMMA) + ") ", mId);
            List<ApsAnnualpland> annualplandList = new ArrayList<>(annualplandMap.values());
            apsAnnualplandService.batchSave(annualplandList);
            //apsAnnualplandQtyService.batchSave(annualplandQtyList);
            if (annualplandQtyList.size() > 0) {
                List<List<ApsAnnualplandQty>> groupList = CollUtil.split(annualplandQtyList, 300);
                CountDownLatch countDownLatch = new CountDownLatch(groupList.size());
                ExecutorService executorService = Executors.newFixedThreadPool(groupList.size());
                for (List<ApsAnnualplandQty> dataList : groupList) {
                    executorService.execute(() -> {
                        apsAnnualplandQtyService.batchSave(dataList);
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
        return SUCCESS;
    }

    /**
     * 获取计划 行转列
     */
    public List<ScheduProductYearViewDTO> getApsYearPlanList(String cplanorderno,Long icustomerid,String startYear,Kv kv) {
        List<ScheduProductYearViewDTO> scheduProductPlanYearList = new ArrayList<>();

        if (notOk(cplanorderno) && isOk(icustomerid) && isOk(startYear)){
            return scheduPlanYear(Kv.by("startyear",startYear).set("customerId",icustomerid));
            //ValidationUtils.error("查询条件不能为空!");
        }else if (notOk(cplanorderno) && notOk(icustomerid) && isOk(startYear)){
            return scheduProductPlanYearList;
        }
        try {
            kv.set("cplanorderno",cplanorderno);//必传
            kv.set("icustomerid",icustomerid);//必传

            String endYear = getEndYear(startYear);
            String lastYear = getLastYear(startYear);

            Long organizeId = getOrgId();
            String CC = "CC";  //客户行事历
            String PP = "PP";    //计划使用
            String CP = "CP";    //计划数量
            String ZK = "ZK";    //计划在库


            //TODO: 所有客户的年度每月工作日集合  key：客户id，value：<年份，客户每月工作天数>
            Map<Long,Map<String,Record>> cusWorkMonthNumListMap = new HashMap<>();
            //根据客户id集查询客户年度每月工作天数
            List<Record> cusWorkMonthNumList = getCusWorkMonthNumList(organizeId,icustomerid,startYear,endYear);
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

            //TODO:根据条件查询客户年度生产计划排程明细
            List<Record> apsYearPlanQtyList = getApsYearPlanQtyList(kv.set("startyear",startYear).set("endyear",endYear));
            if (apsYearPlanQtyList.size() < 1){
                throw new RuntimeException("该客户无订单计划！");
            }

            //本次所有物料集ids
            StringJoiner invIds = new StringJoiner(",");
            for (Record record : apsYearPlanQtyList) {
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
            //TODO:查询物料集期初在库
            List<Record> lastYearZKQtyList = getLastYearZKQtyList(Kv.by("lastyear",lastYear).set("customerids",icustomerid).set("invids",invIds.toString()));
            Map<String,BigDecimal> lastYearZKQtyMap = new HashMap<>();
            for (Record record : lastYearZKQtyList){
                String cInvCode = record.getStr("cInvCode");
                BigDecimal iQty = record.getBigDecimal("iQty");
                lastYearZKQtyMap.put(cInvCode,iQty);
            }


            //CC:客户行事历（客户每月工作天数）
            Map<String,Record> cusWorkMonthNumMap = cusWorkMonthNumListMap.get(icustomerid) != null ? cusWorkMonthNumListMap.get(icustomerid) : new HashMap<>();
            //CC:客户行事历
            ScheduProductYearViewDTO productYearViewCC = getProductYearViewCC(CC,startYear,endYear,cusWorkMonthNumMap);
            //scheduProductPlanYearList.add(productYearViewCC);

            //key:物料id+机型id   value:List<Record>
            Map<String,List<Record>> apsYearPlanQtyListMap = new HashMap<>();
            for (Record record : apsYearPlanQtyList){
                Long iInventoryId = record.getLong("iInventoryId");
                Long iEquipmentModelId = record.getLong("iEquipmentModelId");
                //Long iAnnualPlanDid = record.getLong("iAnnualPlanDid");
                String key = iInventoryId.toString() + (iEquipmentModelId != null ? iEquipmentModelId : "");

                if (apsYearPlanQtyListMap.containsKey(key)){
                    List<Record> list = apsYearPlanQtyListMap.get(key);
                    list.add(record);
                }else {
                    List<Record> list = new ArrayList<>();
                    list.add(record);
                    apsYearPlanQtyListMap.put(key,list);
                }
            }
            //对物料+机型逐个处理
            for (String key : apsYearPlanQtyListMap.keySet()){
                List<Record> recordList = apsYearPlanQtyListMap.get(key);

                //key：code     value：ScheduProductYearViewDTO
                Map<String,ScheduProductYearViewDTO> yearViewDTOMap = new LinkedHashMap<>();
                //行转列
                for (Record record : recordList){
                    Long iInventoryId = record.getLong("iInventoryId");
                    String cInvCode = record.get("cInvCode");
                    String planTypeCode = record.get("planTypeCode");
                    String iYear = record.getStr("iYear");
                    int iMonth = record.getInt("iMonth");
                    BigDecimal iQty = record.getBigDecimal("iQty");
                    //期初在库
                    BigDecimal safetyStock = lastYearZKQtyMap.get(cInvCode) != null ? lastYearZKQtyMap.get(cInvCode) : BigDecimal.ZERO;

                    ScheduProductYearViewDTO productYearView;
                    if (yearViewDTOMap.containsKey(planTypeCode)){
                        productYearView = yearViewDTOMap.get(planTypeCode);
                    }else {
                        productYearView = new ScheduProductYearViewDTO();
                        productYearView.setPlanTypeCode(planTypeCode);
                        productYearView.setiCustomerId(record.getLong("iCustomerId"));
                        productYearView.setcCusCode(record.get("cCusCode"));
                        productYearView.setcCusName(record.get("cCusName"));
                        productYearView.setiEquipmentModelId(record.getLong("iEquipmentModelId"));
                        productYearView.setcEquipmentModelCode(record.get("cEquipmentModelCode"));
                        productYearView.setcEquipmentModelName(record.get("cEquipmentModelName"));
                        productYearView.setiInventoryId(iInventoryId);
                        productYearView.setcInvCode(record.get("cInvCode"));
                        productYearView.setcInvCode1(record.get("cInvCode1"));
                        productYearView.setcInvName1(record.get("cInvName1"));
                    }

                    //当前年
                    if (startYear.equals(iYear)){
                        productYearView.setNowyear(iYear);
                        productYearView.getClass().getMethod("setNowmonth"+iMonth, new Class[]{BigDecimal.class}).invoke(productYearView, iQty);
                    }
                    //下一年
                    else {
                        productYearView.setNextyear(iYear);
                        productYearView.getClass().getMethod("setNextmonth"+iMonth, new Class[]{BigDecimal.class}).invoke(productYearView, iQty);
                    }

                    if (planTypeCode.equals(PP)){
                        productYearView.setNowMonthSum(record.getBigDecimal("iYear11Qty"));
                        productYearView.setNextMonthSum(record.getBigDecimal("iYear21Qty"));
                    }
                    if (planTypeCode.equals(CP)){
                        productYearView.setNowMonthSum(record.getBigDecimal("iYear12Qty"));
                        productYearView.setNextMonthSum(record.getBigDecimal("iYear22Qty"));
                    }
                    if (planTypeCode.equals(ZK)){
                        productYearView.setNowmonth0(safetyStock);
                        productYearView.setNowMonthSum(record.getBigDecimal("iYear13Qty"));
                        productYearView.setNextMonthSum(record.getBigDecimal("iYear23Qty"));
                    }
                    yearViewDTOMap.put(planTypeCode,productYearView);
                }
                for (ScheduProductYearViewDTO viewDTO : yearViewDTOMap.values()){
                    scheduProductPlanYearList.add(viewDTO);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("获取计划行转列出错！"+e.getMessage());
        }
        return scheduProductPlanYearList;
    }



    /*public Ret saveScheduPlanYear(JBoltTable jBoltTable) {
        tx(() -> {
            //新增
            List<ScheduProductYearViewDTO> saveRecords = jBoltTable.getSaveModelList(ScheduProductYearViewDTO.class);
            if (CollUtil.isNotEmpty(saveRecords)) {
                for (int i = 0; i < saveRecords.size(); i++) {
                    Long autoid = JBoltSnowflakeKit.me.nextId();
                    saveRecords.get(i).setIautoid(autoid);
                    saveRecords.get(i).setIitemroutingconfigid(iitemroutingconfigid);
                    saveRecords.get(i).setIcreateby(JBoltUserKit.getUserId());
                    saveRecords.get(i).setCcreatename(JBoltUserKit.getUserName());
                    saveRecords.get(i).setDcreatetime(new Date());
                }
                batchSave(saveRecords, 500);
            }

            //修改
            List<Itemroutingequipment> updateRecords = jBoltTable.getUpdateModelList(Itemroutingequipment.class);
            if (CollUtil.isNotEmpty(updateRecords)) {
                batchUpdate(updateRecords, 500);
            }


            return true;
        });
        return SUCCESS;
    }*/
    public Ret saveScheduPlanYear(String yearDataArry,Long mid) {
        if (StrUtil.isBlank(yearDataArry) || yearDataArry.equals("null")){
            ValidationUtils.error("无计划保存!");
        }
        if (isOk(mid)){
            List<ScheduProductYearViewDTO> list = JSONArray.parseArray(yearDataArry,ScheduProductYearViewDTO.class);
            return updateScheduPlan(list,mid);
        }
        List<ScheduProductYearViewDTO> list = JSONArray.parseArray(yearDataArry,ScheduProductYearViewDTO.class);
        return saveScheduPlan(list.get(0).getNowyear(),list);
    }


    //-----------------------------------------------------------------年度生产计划汇总-----------------------------------------------


    public List<ScheduProductYearViewDTO> getApsYearPlanSumPage(int pageNumber, int pageSize, Kv kv) {
        List<ScheduProductYearViewDTO> scheduProductPlanYearList = new ArrayList<>();

        String startYear = kv.getStr("startyear");
        if (notOk(startYear)){
            ValidationUtils.error("查询年份不能为空!");
            //throw new RuntimeException("该客户无订单计划！");
        }

        String endYear = getEndYear(startYear);
        pageSize = pageSize * 15;

        kv.set("endyear",endYear);
        List<Record> recordPage = dbTemplate("scheduproductplan.getApsYearPlanSumList",kv).find();
        List<Record> apsYearPlanQtyList = recordPage;

        try {
            //key:物料id+产线id   value:List<Record>
            Map<String,List<Record>> apsYearPlanQtyListMap = new HashMap<>();
            for (Record record : apsYearPlanQtyList){
                Long iInventoryId = record.getLong("iInventoryId");
                Long iWorkRegionMid = record.getLong("iWorkRegionMid");
                String key = iInventoryId.toString() + (iWorkRegionMid != null ? iWorkRegionMid : "");

                if (apsYearPlanQtyListMap.containsKey(key)){
                    List<Record> list = apsYearPlanQtyListMap.get(key);
                    list.add(record);
                }else {
                    List<Record> list = new ArrayList<>();
                    list.add(record);
                    apsYearPlanQtyListMap.put(key,list);
                }
            }
            //对物料+产线逐个处理
            for (String key : apsYearPlanQtyListMap.keySet()){
                List<Record> recordList = apsYearPlanQtyListMap.get(key);

                //key：code     value：ScheduProductYearViewDTO
                Map<String,ScheduProductYearViewDTO> yearViewDTOMap = new LinkedHashMap<>();
                //行转列
                for (Record record : recordList){
                    Long iInventoryId = record.getLong("iInventoryId");
                    String planTypeCode = record.get("planTypeCode");
                    String iYear = record.getStr("iYear");
                    int iMonth = record.getInt("iMonth");
                    BigDecimal iQty = record.getBigDecimal("iQty");

                    ScheduProductYearViewDTO productYearView;
                    if (yearViewDTOMap.containsKey(planTypeCode)){
                        productYearView = yearViewDTOMap.get(planTypeCode);
                    }else {
                        productYearView = new ScheduProductYearViewDTO();
                        productYearView.setPlanTypeCode(planTypeCode);
                        productYearView.setiWorkRegionMid(record.getLong("iWorkRegionMid"));
                        productYearView.setcWorkCode(record.get("cWorkCode"));
                        productYearView.setcWorkName(record.get("cWorkName"));
                        productYearView.setiInventoryId(iInventoryId);
                        productYearView.setcInvCode(record.get("cInvCode"));
                        productYearView.setcInvCode1(record.get("cInvCode1"));
                        productYearView.setcInvName1(record.get("cInvName1"));
                    }
                    //当前年
                    if (startYear.equals(iYear)){
                        productYearView.setNowyear(iYear);
                        productYearView.getClass().getMethod("setNowmonth"+iMonth, new Class[]{BigDecimal.class}).invoke(productYearView, iQty);
                    }
                    //下一年
                    else {
                        productYearView.setNextyear(iYear);
                        productYearView.getClass().getMethod("setNextmonth"+iMonth, new Class[]{BigDecimal.class}).invoke(productYearView, iQty);
                    }
                    productYearView.setNowMonthSum(record.getBigDecimal("iYear12Qty"));
                    productYearView.setNextMonthSum(record.getBigDecimal("iYear22Qty"));

                    yearViewDTOMap.put(planTypeCode,productYearView);
                }
                for (ScheduProductYearViewDTO viewDTO : yearViewDTOMap.values()){
                    scheduProductPlanYearList.add(viewDTO);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("获取年度生产计划汇总出错！"+e.getMessage());
        }
        return scheduProductPlanYearList;
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

    public List<Record> getLastYearZKQtyList(Kv kv){
        return dbTemplate("scheduproductplan.getLastYearZKQtyList", kv).find();
    }


    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        //ValidationUtils.isTrue(updateColumn(formAutoId, "iPushTo", 1).isOk(), JBoltMsg.FAIL);
        // ValidationUtils.isTrue(updateColumn(formAutoId, "cDocNo", cDocNo).isOk(), JBoltMsg.FAIL);
        ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 2).isOk(), JBoltMsg.FAIL);
        return null;
    }

    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 3).isOk(), JBoltMsg.FAIL);
        return null;
    }

    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        // 只有一个审批人
        if (isFirst && isLast) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 0).isOk(), JBoltMsg.FAIL);
        }
        // 反审回第一个节点，回退状态为“已保存”
        else if (isFirst) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus",0).isOk(), JBoltMsg.FAIL);
        }
        // 最后一步通过的，反审，回退状态为“待审核”
        else if (isLast) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 1).isOk(), JBoltMsg.FAIL);
        }
        return null;
    }

    @Override
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    @Override
    public String postSubmitFunc(long formAutoId) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 1).isOk(), "提审失败");
        return null;
    }

    @Override
    public String postWithdrawFunc(long formAutoId) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 0).isOk(), "撤回失败");
        return null;
    }

    @Override
    public String withdrawFromAuditting(long formAutoId) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 0).isOk(), JBoltMsg.FAIL);
        return null;
    }

    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 0).isOk(), JBoltMsg.FAIL);
        return null;
    }

    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        return null;
    }

    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        for (Long formAutoId:formAutoIds) {
            ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", 3).isOk(), JBoltMsg.FAIL);
        }
        return null;
    }

    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        List<ApsAnnualplanm> weekOrderMS = getListByIds(cn.rjtech.wms.utils.StringUtils.join(formAutoIds, COMMA));
        Boolean algorithmSum = weekOrderMS.stream().anyMatch(item -> item.getIAuditStatus().equals(2));
        weekOrderMS.stream().map(item -> {
            item.setIAuditStatus(0);
            return item;
        }).collect(Collectors.toList());
        batchUpdate(weekOrderMS);
        return null;
    }
}
