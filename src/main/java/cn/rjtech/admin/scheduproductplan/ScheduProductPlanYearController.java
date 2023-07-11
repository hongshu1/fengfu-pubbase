package cn.rjtech.admin.scheduproductplan;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApsAnnualplanm;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

/**
 * 生产计划排程 Controller
 * @ClassName: ScheduProductPlanYearController
 * @author: chentao
 * @date: 2023-03-30 11:26
 */
@CheckPermission(PermissionKey.SCHEDUPRODUCTPLANYEAR)
@UnCheckIfSystemAdmin
@Path(value = "/admin/scheduproductplanyear", viewPath = "/_view/admin/scheduproductplan")
public class ScheduProductPlanYearController extends BaseAdminController {

    @Inject
    private ScheduProductPlanYearService service;

    public void planyear() {
        render("planyear.html");
    }
    public void addview() {
        String cplanorderno = get("cplanorderno");
        Long icustomerid = getLong("icustomerid");
        String startyear = get("startyear");
        String cinvcode = get("cinvcode");
        String cinvcode1 = get("cinvcode1");
        String cinvname1 = get("cinvname1");
        set("cinvcode",cinvcode);
        set("cinvcode1",cinvcode1);
        set("cinvname1",cinvname1);

        set("cplanorderno",cplanorderno);
        set("icustomerid",icustomerid);
        if (StrUtil.isBlank(cplanorderno)){
            startyear = DateUtils.formatDate(new Date(),"yyyy");
        }
        set("startyear",startyear);
        set("endyear",Integer.parseInt(startyear) + 1);

        Boolean isedit = getBoolean("isedit");
        if (isedit == null){
            isedit = true;
        }
        set("isedit",isedit);

        ApsAnnualplanm apsAnnualplanm = service.findById(get("iautoid"));
        set("iautoid",get("iautoid"));
        set("apsAnnualplanm",apsAnnualplanm);


        String endYear = service.getEndYear(startyear);
        Long organizeId = getOrgId();
        String CC = "CC";  //客户行事历
        //TODO: 所有客户的年度每月工作日集合  key：客户id，value：<年份，客户每月工作天数>
        Map<Long,Map<String,Record>> cusWorkMonthNumListMap = new HashMap<>();
        if (isOk(icustomerid)){
            //根据客户id集查询客户年度每月工作天数
            List<Record> cusWorkMonthNumList = service.getCusWorkMonthNumList(organizeId,icustomerid,startyear,endYear);
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
        }

        //CC:客户行事历（客户每月工作天数）
        Map<String,Record> cusWorkMonthNumMap = cusWorkMonthNumListMap.get(icustomerid) != null ? cusWorkMonthNumListMap.get(icustomerid) : new HashMap<>();
        //CC:客户行事历
        ScheduProductYearViewDTO productYearViewCC = service.getProductYearViewCC(CC,startyear,endYear,cusWorkMonthNumMap);
        List<String> weeklist = new ArrayList<>();
        try {
            for (int i = 1; i <= 12; i++) {
                BigDecimal preQtycp = (BigDecimal) productYearViewCC.getClass().getMethod("getNowmonth" + i, new Class[]{}).invoke(productYearViewCC, new Object[]{});
                weeklist.add(preQtycp.toString());
            }
            weeklist.add(productYearViewCC.getNowMonthSum().toString());

            for (int i = 1; i <= 3; i++) {
                BigDecimal preQtycp = (BigDecimal) productYearViewCC.getClass().getMethod("getNextmonth" + i, new Class[]{}).invoke(productYearViewCC, new Object[]{});
                weeklist.add(preQtycp.toString());
            }
            weeklist.add(productYearViewCC.getNextMonthSum().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        set("weeklist", weeklist);
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


    //-----------------------------------------------------------------年度生产计划排产-----------------------------------------------

    @UnCheck
    public void getCustomerList() {
        renderJsonData(service.getCustomerList());
    }

    /**
     * 作成计划
     */
    public void schedulingPlan() {
        renderJson(service.scheduPlanYear(getKv()));
    }
    /**
     * 保存计划
     */
    public void saveScheduPlanYear(String yearDataArry,Long mid) {
        renderJsonData(service.saveScheduPlanYear(yearDataArry,mid));
    }

    /**
     * 获取计划
     */
    @UnCheck
    public void getApsYearPlanList() {
        String cplanorderno = get("cplanorderno");
        Long icustomerid = getLong("icustomerid");
        String startYear = get("startyear");
        renderJsonData(service.getApsYearPlanList(cplanorderno,icustomerid,startYear,getKv()));
    }

    /**
     * 获取订单计划
     */
    @UnCheck
    public void getApsYearPlanMasterPage() {
        renderJsonData(service.getApsYearPlanMasterPage(getPageNumber(),getPageSize(),getKv()));
    }

    public void dataExportYear() throws Exception {
        String cplanorderno = get("cplanorderno");
        Long icustomerid = getLong("icustomerid");
        String startYear = get("startyear");
        String nextYear = String.valueOf(Integer.parseInt(startYear) + 1);

        final String fileName = "年度生产计划";
        JBoltExcel jBoltExcel = JBoltExcel.create();
        JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create(fileName);

        List<JBoltExcelHeader> headerList = new ArrayList<>();
        headerList.add(JBoltExcelHeader.create("ccusname","客户名称"));
        headerList.add(JBoltExcelHeader.create("cequipmentmodelname","机型"));
        headerList.add(JBoltExcelHeader.create("cinvcode","存货编码"));
        headerList.add(JBoltExcelHeader.create("cinvcode1","客户部番"));
        headerList.add(JBoltExcelHeader.create("cinvname1","部品名称"));
        headerList.add(JBoltExcelHeader.create("plantypecode","项目"));
        for (int i = 1; i <= 12; i++) {
            if (i < 10){
                headerList.add(JBoltExcelHeader.create("nowmonth"+i,startYear.concat("-0").concat(String.valueOf(i))));
            }else {
                headerList.add(JBoltExcelHeader.create("nowmonth"+i,startYear.concat("-").concat(String.valueOf(i))));
            }
        }
        headerList.add(JBoltExcelHeader.create("nowmonthsum",startYear.concat("合计")));
        for (int i = 1; i <= 3; i++) {
            headerList.add(JBoltExcelHeader.create("nextmonth"+i,nextYear.concat("-0").concat(String.valueOf(i))));
        }
        headerList.add(JBoltExcelHeader.create("nextmonthsum",nextYear.concat("合计")));

        jBoltExcelSheet.setMerges().setHeaders(1,headerList);
        List<ScheduProductYearViewDTO> dataList = service.getApsYearPlanList(cplanorderno,icustomerid,startYear,getKv());
        for (ScheduProductYearViewDTO data : dataList){
            if (data.getPlanTypeCode().equals("PP")){
                data.setPlanTypeCode("计划使用");
            }
            if (data.getPlanTypeCode().equals("CP")){
                data.setPlanTypeCode("计划数量");
            }
            if (data.getPlanTypeCode().equals("ZK")){
                data.setPlanTypeCode("计划在库");
            }
            if (data.getPlanTypeCode().equals("CC")){
                data.setPlanTypeCode("客户行事历");
            }
        }
        List<Record> recordList = new ArrayList<>();
        for (ScheduProductYearViewDTO data : dataList) {
            Record record = new Record().setColumns(BeanUtil.beanToMap(data));
            recordList.add(record);
        }
        jBoltExcelSheet.setRecordDatas(2,recordList);
        jBoltExcel.addSheet(jBoltExcelSheet);

        jBoltExcel.setFileName(fileName+DateUtils.getDate("yyyy-MM-dd"));
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    //-----------------------------------------------------------------年度生产计划汇总-----------------------------------------------


    public void planyearsum() {
        String startyear = get("startyear");
        String cworkname = get("cworkname");
        String cinvcode = get("cinvcode");
        String cinvcode1 = get("cinvcode1");
        String cinvname1 = get("cinvname1");

        if (StrUtil.isBlank(startyear)){
            startyear = DateUtils.getYear();
        }

        set("startyear",startyear);
        set("endyear",Integer.parseInt(startyear)+ 1);
        set("cworkname",cworkname);
        set("cinvcode",cinvcode);
        set("cinvcode1",cinvcode1);
        set("cinvname1",cinvname1);

        render("planyearsum.html");
    }

    /**
     * 获取年度生产计划汇总
     */
    @UnCheck
    public void getApsYearPlanSumPage() {
        renderJsonData(service.getApsYearPlanSumPage(getPageNumber(),getPageSize(),getKv()));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.PLANYEARSUM_EXPORT)
    public void dataExport() throws Exception {
        String startYear = get("startyear");
        if (notOk(startYear)){
            ValidationUtils.error("查询年份不能为空!");
        }
        String nextYear = String.valueOf(Integer.parseInt(startYear) + 1);

        final String fileName = "年度生产计划汇总";
        JBoltExcel jBoltExcel = JBoltExcel.create();
        JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create(fileName);

        List<JBoltExcelHeader> headerList = new ArrayList<>();
        headerList.add(JBoltExcelHeader.create("cworkname","产线名称"));
        headerList.add(JBoltExcelHeader.create("cinvcode","存货编码"));
        headerList.add(JBoltExcelHeader.create("cinvcode1","客户部番"));
        headerList.add(JBoltExcelHeader.create("cinvname1","部品名称"));
        for (int i = 1; i <= 12; i++) {
            if (i < 10){
                headerList.add(JBoltExcelHeader.create("nowmonth"+i,startYear.concat("-0").concat(String.valueOf(i))));
            }else {
                headerList.add(JBoltExcelHeader.create("nowmonth"+i,startYear.concat("-").concat(String.valueOf(i))));
            }
        }
        headerList.add(JBoltExcelHeader.create("nowmonthsum",startYear.concat("合计")));
        for (int i = 1; i <= 3; i++) {
            headerList.add(JBoltExcelHeader.create("nextmonth"+i,nextYear.concat("-0").concat(String.valueOf(i))));
        }
        headerList.add(JBoltExcelHeader.create("nextmonthsum",nextYear.concat("合计")));

        jBoltExcelSheet.setMerges().setHeaders(1,headerList);
        List<ScheduProductYearViewDTO> dataList = service.getApsYearPlanSumPage(1,15,getKv());
        List<Record> recordList = new ArrayList<>();
        for (ScheduProductYearViewDTO data : dataList) {
            Record record = new Record().setColumns(BeanUtil.beanToMap(data));
            recordList.add(record);
        }
        jBoltExcelSheet.setRecordDatas(2,recordList);
        jBoltExcel.addSheet(jBoltExcelSheet);

        jBoltExcel.setFileName(fileName+DateUtils.getDate("yyyy-MM-dd"));
        renderBytesToExcelXlsFile(jBoltExcel);
    }




















    /**
     * 查询排程计划
     */
    public void querySchedulingData() {
        JSONObject data = new JSONObject();

        //yyyy-MM 开始月份
        String startMonth = getKv().getStr("month");



        LOG.info("开始查询排程数据！");
        long start = System.currentTimeMillis();



        long end = System.currentTimeMillis();
        LOG.info("查询总时间 = " + (end - start));

        //data.put("schedulingDataList", schedulingDataList);

        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("data",data);
        dataMap.put("state","ok");
        renderJson(dataMap);
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



}