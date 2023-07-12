package cn.rjtech.admin.expensebudgetitemd;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.annotation.CheckDataPermission;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import cn.jbolt.core.poi.excel.JBoltExcelPositionData;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.InvestmentBudgetTypeEnum;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.ExpenseBudgetItemd;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 费用预算项目明细 Controller
 *
 * @ClassName: ExpenseBudgetItemdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-15 10:04
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.STATISTIC_ANALYSIS_EXPENSEBUDGETITEMD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/expensebudgetitemd", viewPath = "/_view/admin/expensebudgetitemd")
public class ExpenseBudgetItemdAdminController extends BaseAdminController {

    @Inject
    private ExpenseBudgetItemdService service;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private PeriodService periodService;
    @Inject
    private ExpenseBudgetService expensebudgetService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
        ExpenseBudgetItemd expenseBudgetItemd = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(expenseBudgetItemd, JBoltMsg.DATA_NOT_EXIST);

        set("expenseBudgetItemd", expenseBudgetItemd);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(ExpenseBudgetItemd.class, "expenseBudgetItemd")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(ExpenseBudgetItemd.class, "expenseBudgetItemd")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }


    /**
     * 跳转费用预算模板下载界面
     */
    public void dowloadTplPage() {
        render("dowloadtpl_index.html");
    }

    public void dowloadTplPageSubmit() {
        Kv para = getKv();
        String cdepcode = para.getStr("cdepcode");//部门
        Integer budgetYear = para.getInt("budgetYear");//预算年度
        Integer iBudgetType = para.getInt("iBudgetType");//预算类型
        ValidationUtils.notBlank(cdepcode, "请选择U8部门!");
        ValidationUtils.notNull(budgetYear, "请选择预算年度!");
        ValidationUtils.notNull(iBudgetType, "请选择预算类型!");

        List<Record> list = service.paginateAdminDatas(getPageNumber(), getPageSize(), Kv.by("ibudgetyear", budgetYear).set("ibudgettype", iBudgetType).set("cdepcode", cdepcode)).getList();
        ValidationUtils.isTrue(list.size()>0,"无该部门数据请检查选择的类型");
        Date dstarttime = list.get(0).getDate("cbegindate");
        Date dendtime = list.get(0).getDate("cenddate");
        //查询启动的期间
        LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(dstarttime, JBoltDateUtil.YMD));
        LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(dendtime, JBoltDateUtil.YMD));
        Calendar calendarStartDate1 = JBoltDateUtil.getCalendarByDate(dstarttime);
        Calendar calendarCutoffTimetDate1 = JBoltDateUtil.getCalendarByDate(dendtime);
        long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
        String cdepname = departmentService.getCdepName(cdepcode);
        if (iBudgetType == 1) {
            List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
            List<JBoltExcelMerge> mergeList = new ArrayList<>();
            //下载模板中动态设置预算期间的起止年份、预算年度、部门、表头的年份
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 5, calendarStartDate1.get(Calendar.YEAR) + "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 6, calendarStartDate1.get(Calendar.MONTH) + 1 + "月"));
            excelPositionDatas.add(JBoltExcelPositionData.create(5, 5, calendarCutoffTimetDate1.get(Calendar.YEAR) + "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(5, 6, calendarCutoffTimetDate1.get(Calendar.MONTH) + 1 + "月"));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 7, "部门：" + cdepname));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 10, budgetYear + "年"));
            //设置动态表头部分
            int yearForMonthAmount = 0;
            for (int i = 0; i < diffMonth; i++) {
                excelPositionDatas.add(JBoltExcelPositionData.create(8, 17 + i * 2, "数量"));
                excelPositionDatas.add(JBoltExcelPositionData.create(8, 18 + i * 2, "金额"));
                Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(dstarttime);
                Calendar calendarCutoffTimetDate = JBoltDateUtil.getCalendarByDate(dendtime);
                calendarStartDate.add(Calendar.MONTH, i);
                mergeList.add(JBoltExcelMerge.create(7, 7, 17 + i * 2, 18 + i * 2, calendarStartDate.get(Calendar.MONTH) + 1 + "月", true));
                if (calendarStartDate.get(Calendar.MONTH) + 1 == 12 ||
                        (calendarStartDate.get(Calendar.YEAR) == calendarCutoffTimetDate.get(Calendar.YEAR) &&
                                calendarStartDate.get(Calendar.MONTH) == calendarCutoffTimetDate.get(Calendar.MONTH))) {
                    mergeList.add(JBoltExcelMerge.create(6, 6, 17 + yearForMonthAmount * 2, 18 + i * 2, calendarStartDate.get(Calendar.YEAR) + "年", true));
                    yearForMonthAmount = i + 1;
                }
            }
            //合计列表头生成
            mergeList.add(JBoltExcelMerge.create(6, 6, (int) (17 + diffMonth * 2), (int) (18 + diffMonth * 2), "合计(1)", true));
            mergeList.add(JBoltExcelMerge.create(6, 6, (int) (19 + diffMonth * 2), (int) (20 + diffMonth * 2), "合计(2)", true));
            mergeList.add(JBoltExcelMerge.create(7, 7, (int) (17 + diffMonth * 2), (int) (18 + diffMonth * 2), "1-12月", true));
            mergeList.add(JBoltExcelMerge.create(7, 7, (int) (19 + diffMonth * 2), (int) (20 + diffMonth * 2), "本年四月-次年三月", true));
            excelPositionDatas.add(JBoltExcelPositionData.create(8, (int) (17 + diffMonth * 2), "数量"));
            excelPositionDatas.add(JBoltExcelPositionData.create(8, (int) (18 + diffMonth * 2), "金额"));
            excelPositionDatas.add(JBoltExcelPositionData.create(8, (int) (19 + diffMonth * 2), "数量"));
            excelPositionDatas.add(JBoltExcelPositionData.create(8, (int) (20 + diffMonth * 2), "金额"));
            if (CollUtil.isNotEmpty(list)) {
                int startRow = 9;
                for (int i = 0; i < list.size(); i++) {
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 2, i + 1));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 3, list.get(i).get("cprojectcode")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 4, list.get(i).get("cprojectname")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 5, list.get(i).get("cbudgetno")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 6, list.get(i).get("chighestsubjectname")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 7, list.get(i).get("clowestsubjectname")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 8, list.get(i).get("citemname")));
                    String careertype = JBoltDictionaryCache.me.getNameBySn("career_type", list.get(i).getStr("careertype"));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 9, careertype));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 10, list.get(i).getInt("islargeamountexpense") == 0 ? "否" : "是"));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 11, list.get(i).get("cuse")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 12, list.get(i).get("cmemo")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 13, list.get(i).get("iprice")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 14, list.get(i).get("cunit")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 15, list.get(i).get("previousremain")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 16, list.get(i).get("icarryforward")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 17, list.get(i).get("previousyearmounthquantity9")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 18, list.get(i).get("previousyearmounthamount9")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 19, list.get(i).get("previousyearmounthquantity10")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 20, list.get(i).get("previousyearmounthamount10")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 21, list.get(i).get("previousyearmounthquantity11")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 22, list.get(i).get("previousyearmounthamount11")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 23, list.get(i).get("previousyearmounthquantity12")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 24, list.get(i).get("previousyearmounthamount12")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 25, list.get(i).get("nowyearmounthquantity1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 26, list.get(i).get("nowyearmounthamount1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 27, list.get(i).get("nowyearmounthquantity2")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 28, list.get(i).get("nowyearmounthamount2")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 29, list.get(i).get("nowyearmounthquantity3")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 30, list.get(i).get("nowyearmounthamount3")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 31, list.get(i).get("nowyearmounthquantity4")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 32, list.get(i).get("nowyearmounthamount4")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 33, list.get(i).get("nowyearmounthquantity5")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 34, list.get(i).get("nowyearmounthamount5")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 35, list.get(i).get("nowyearmounthquantity6")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 36, list.get(i).get("nowyearmounthamount6")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 37, list.get(i).get("nowyearmounthquantity7")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 38, list.get(i).get("nowyearmounthamount7")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 39, list.get(i).get("nowyearmounthquantity8")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 40, list.get(i).get("nowyearmounthamount8")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 41, list.get(i).get("nowyearmounthquantity9")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 42, list.get(i).get("nowyearmounthamount9")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 43, list.get(i).get("nowyearmounthquantity10")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 44, list.get(i).get("nowyearmounthamount10")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 45, list.get(i).get("nowyearmounthquantity11")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 46, list.get(i).get("nowyearmounthamount11")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 47, list.get(i).get("nowyearmounthquantity12")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 48, list.get(i).get("nowyearmounthamount12")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 49, list.get(i).get("nextyearmounthquantity1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 50, list.get(i).get("nextyearmounthamount1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 51, list.get(i).get("nextyearmounthquantity2")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 52, list.get(i).get("nextyearmounthamount2")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 53, list.get(i).get("nextyearmounthquantity3")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 54, list.get(i).get("nextyearmounthamount3")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 55, list.get(i).get("nextyearmounthquantity4")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 56, list.get(i).get("nextyearmounthamount4")));

                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 57, list.get(i).get("totalquantity1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 58, list.get(i).get("totalamount1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 59, list.get(i).get("totalquantity2")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 60, list.get(i).get("totalamount2")));

                }
            }
            //2、创建JBoltExcel
            JBoltExcel jBoltExcel = JBoltExcel
                    .createByTpl("expenseBudgetFullYearTplDetails.xlsx")//创建JBoltExcel 从模板加载创建
                    .addSheet(//设置sheet
                            JBoltExcelSheet.create("费用预算明细")//创建sheet name保持与模板中的sheet一致
                                    .setPositionDatas(excelPositionDatas)//设置定位数据
                                    .setMerges(mergeList)
                    )
                    .setFileName("费用预算导出");
            //3、导出
            renderBytesToExcelXlsxFile(jBoltExcel);
        } else {
            List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
            //下载模板中动态设置预算期间的起止年份、部门、预算类型
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 5, budgetYear + "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 6, calendarStartDate1.get(Calendar.MONTH) + 1 + "月"));
            excelPositionDatas.add(JBoltExcelPositionData.create(5, 5, budgetYear + "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(5, 6, calendarCutoffTimetDate1.get(Calendar.MONTH) + 1 + "月"));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 7, "部门：" + cdepname));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 10,  budgetYear+ "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(6, 17, budgetYear));

            //查询下期修改当年的实绩数据用于导出
            if (CollUtil.isNotEmpty(list)) {
                int startRow = 9;
                for (int i = 0; i < list.size(); i++) {
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 3, list.get(i).get("cprojectcode")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 4, list.get(i).get("cprojectname")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 5, list.get(i).get("cbudgetno")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 6, list.get(i).get("chighestsubjectname")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 7, list.get(i).get("clowestsubjectname")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 8, list.get(i).get("citemname")));
                    String careertype = JBoltDictionaryCache.me.getNameBySn("career_type", list.get(i).getStr("careertype"));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 9, careertype));

                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 10, list.get(i).getInt("islargeamountexpense") == 0 ? "否" : "是"));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, 11, list.get(i).get("cuse")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, 12, list.get(i).get("cmemo")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 13, list.get(i).get("iprice")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 14, list.get(i).get("cunit")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 15, list.get(i).get("previousremain")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 16, list.get(i).get("icarryforward")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 17, list.get(i).get("nowyearmounthquantity1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 18, list.get(i).get("nowyearmounthamount1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 19, list.get(i).get("nowyearmounthquantity2")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 20, list.get(i).get("nowyearmounthamount2")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 21, list.get(i).get("nowyearmounthquantity3")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 22, list.get(i).get("nowyearmounthamount3")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 23, list.get(i).get("nowyearmounthquantity4")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 24, list.get(i).get("nowyearmounthamount4")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 25, list.get(i).get("nowyearmounthquantity5")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 26, list.get(i).get("nowyearmounthamount5")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 27, list.get(i).get("nowyearmounthquantity6")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 28, list.get(i).get("nowyearmounthamount6")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 29, list.get(i).get("nowyearmounthquantity7")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 30, list.get(i).get("nowyearmounthamount7")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 31, list.get(i).get("nowyearmounthquantity8")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 32, list.get(i).get("nowyearmounthamount8")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 33, list.get(i).get("nowyearmounthquantity9")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 34, list.get(i).get("nowyearmounthamount9")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 35, list.get(i).get("nowyearmounthquantity10")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 36, list.get(i).get("nowyearmounthamount10")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 37, list.get(i).get("nowyearmounthquantity11")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 38, list.get(i).get("nowyearmounthamount11")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 39, list.get(i).get("nowyearmounthquantity12")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 40, list.get(i).get("nowyearmounthamount12")));


                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 41, list.get(i).get("totalquantity1")));
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 42, list.get(i).get("totalamount1")));

                }
            }
            //2、创建JBoltExcel
            JBoltExcel jBoltExcel = JBoltExcel
                    .createByTpl("expenseBudgetNextPeriodTplDetails.xlsx")//创建JBoltExcel 从模板加载创建
                    .addSheet(//设置sheet
                            JBoltExcelSheet.create("费用预算明细")//创建sheet name保持与模板中的sheet一致
                                    .setPositionDatas(excelPositionDatas)//设置定位数据
                    )
                    .setFileName("费用预算导出");
            //3、导出
            renderBytesToExcelXlsxFile(jBoltExcel);
        }
    }
    /**
     * 跳转费用预算模板打印
     * */
    public void dowloadPrint(){
        render("dowloadprint_index.html");
    }

    public  void printPage(){
        Kv para = getKv();
        String cdepcode = para.getStr("cdepcode");//部门
        Integer budgetYear = para.getInt("budgetYear");//预算年度
        Integer iBudgetType = para.getInt("ibudgettype");//预算类型
        ValidationUtils.notBlank(cdepcode, "请选择U8部门!");
        ValidationUtils.notNull(budgetYear, "请选择预算年度!");
        ValidationUtils.notNull(iBudgetType, "请选择预算类型!");
        List<Record> list = service.paginateAdminDatas(getPageNumber(), getPageSize(), Kv.by("ibudgetyear", budgetYear).set("ibudgettype", iBudgetType).set("cdepcode", cdepcode)).getList();
        ValidationUtils.isTrue(list.size()!=0,"请检查选择的数据是否正确,无该数据");
        Date   dstarttime  = list.get(0).getDate("cbegindate");
        Date   dendtime = list.get(0).getDate("cenddate");
        //查询启动的期间
        Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(dstarttime);
        Calendar calendarCutoffTimetDate = JBoltDateUtil.getCalendarByDate(dendtime);
        //开始日期
        set("calendarStartDate", calendarStartDate.get(Calendar.YEAR) +"年 "+(calendarStartDate.get(Calendar.MONTH)+1) +"月");
        //截止日期
        set("calendarCutoffTimetDate", calendarCutoffTimetDate.get(Calendar.YEAR) +"年 "+ (calendarCutoffTimetDate.get(Calendar.MONTH)+1) +"月");
        LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(dstarttime, JBoltDateUtil.YMD));
        LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(dendtime, JBoltDateUtil.YMD));
        long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
        List<String> yearlist =new ArrayList<>();
        for (int i = 0; i < diffMonth; i++) {
            Calendar calendarStartDate1 = JBoltDateUtil.getCalendarByDate(dstarttime);
            Calendar calendarCutoffTimetDate1 = JBoltDateUtil.getCalendarByDate(dendtime);
            calendarStartDate1.add(Calendar.MONTH, i);
            if (calendarStartDate1.get(Calendar.MONTH) + 1 == 12 ||
                    (calendarStartDate1.get(Calendar.YEAR) == calendarStartDate1.get(Calendar.YEAR) &&
                            calendarStartDate1.get(Calendar.MONTH) == calendarCutoffTimetDate1.get(Calendar.MONTH))) {
                yearlist.add( calendarStartDate1.get(Calendar.YEAR) + "年");
            }
        }

        for (Record record : list) {
            //类型转换
          record.set("careertype", JBoltDictionaryCache.me.getNameBySn("career_type", record.getStr("careertype")));
          record.set("islargeamountexpense",record.getInt("islargeamountexpense") == 0 ? "否" : "是");
        }
        set("cdepname",departmentService.getCdepName(cdepcode));
        set("iBudgetType", InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText());
        set("now", JBoltDateUtil.getYear());
        set("budgetYear",budgetYear);
        set("list", JsonKit.toJson(list));
        List<String> collect = yearlist.stream().distinct().collect(Collectors.toList());
        for (int i = 0; i < collect.size(); i++) {
            set("year"+(i+1), collect.get(i));
        }
        if (iBudgetType.equals(2)){
            render("next_print.html");
        }else {
            render("yearly_print.html");
        }
        }
    /**
     * 费用预算明细报表数据导出
     * */
    @UnCheck
    public void exportTableExpenseBudgetItemdReportDatas(){
    	Kv para = getKv();
    	String cdepcode = para.getStr("cdepcode");
    	Integer ibudgetyear = para.getInt("ibudgetyear");
    	Integer ibudgettype = para.getInt("ibudgettype");
    	String paraNullMsg = "请选择部门,预算年份,预算类型导出";
    	ValidationUtils.notBlank(cdepcode, paraNullMsg);
    	ValidationUtils.notNull(ibudgetyear, paraNullMsg);
    	ValidationUtils.notNull(ibudgettype, paraNullMsg);
    	ExpenseBudget expenseBudget = expensebudgetService.findModelByYearAndType(ibudgetyear, ibudgettype, cdepcode);
    	ValidationUtils.notNull(expenseBudget, "费用预算不存在，导出失败");
    	List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
    	List<JBoltExcelMerge> mergeList = new ArrayList<>(); //定位合并数据集合
    	List<Record> list = expensebudgetService.findExpenseBudgetItemDatas(expenseBudget.getIAutoId());
    	expensebudgetService.exportExpenseBudgetExcel(expenseBudget,excelPositionDatas,mergeList,list);
    	//2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .createByTpl("expenseBudgetFullYearTpl.xlsx")//创建JBoltExcel 从模板加载创建
                .addSheet(//设置sheet
                        JBoltExcelSheet.create("预算录入")//创建sheet name保持与模板中的sheet一致
                                .setPositionDatas(excelPositionDatas)//设置定位数据
                                .setMerges(mergeList)
                )
                .setFileName("费用预算导入模板");
        
		//3、导出
		renderBytesToExcelXlsxFile(jBoltExcel);
    }
}
