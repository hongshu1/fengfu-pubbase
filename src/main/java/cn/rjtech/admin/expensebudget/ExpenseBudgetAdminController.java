package cn.rjtech.admin.expensebudget;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.annotation.CheckDataPermission;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import cn.jbolt.core.poi.excel.JBoltExcelPositionData;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltRandomUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.expensebudgetmanage.ExpenseBudgetManageService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.annotations.RequestLimit;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.ExpenseBudgetTypeEnum;
import cn.rjtech.interceptor.RequestLimitInterceptor;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * 费用预算 Controller
 *
 * @ClassName: ExpenseBudgetAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-15 09:54
 */
@CheckPermission(PermissionKey.EXPENSE_BUDGET_FORMULATE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/expensebudget", viewPath = "/_view/admin/expensebudget")
public class ExpenseBudgetAdminController extends BaseAdminController {

    @Inject
    private ExpenseBudgetService service;
    @Inject
    private ExpenseBudgetManageService expenseBudgetManageService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private PeriodService periodService;
    @Inject
    private ExpenseBudgetItemService expenseBudgetItemService;
    @Inject
    private InvestmentPlanService investmentPlanService;

    private static final String FILE_PATH_MODIFIERS = "\\";

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
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(ExpenseBudget.class, "expenseBudget")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(ExpenseBudget.class, "expenseBudget")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_FORMULATE_DELETE)
    @CheckDataPermission(operation = DataOperationEnum.DELETE, type = BusObjectTypeEnum.DEPTARTMENT)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 跳转费用预算模板下载界面
     */
    /*@CheckPermission(PermissionKey.EXPENSE_BUDGET_TPLDOWNLOAD)
    public void dowloadTplPage() {
    	 //查询启动的期间
        Record periodRc = periodService.findPeriodByDownTpl(Kv.by("isenabled", "1").set("iservicetype", ServiceTypeEnum.EXPENSE_BUDGET.getValue()));
        ValidationUtils.notNull(periodRc, "未维护启用期间!");
        set("periodRc",periodRc);
        render("dowloadtpl_index.html");
    }*/

    @UnCheck
    public void dowloadTplPageSubmit() {
        Kv para = getKv();
        String cdepcode = para.getStr("cdepcode");//部门
        ValidationUtils.notBlank(cdepcode, "请选择U8部门!");
        Long periodId = para.getLong("periodId");
        ValidationUtils.notNull(periodId, "期间ID为空,模板下载失败!");
        //查询启动的期间
        Record periodRc = periodService.findPeriodByDownTpl(Kv.by("periodId", periodId));
        ValidationUtils.notNull(periodRc, "未维护期间,模板下载失败!");
        Integer budgetYear = periodRc.getInt("ibudgetyear");//预算年度
        Integer iBudgetType = periodRc.getInt("ibudgettype");//预算类型
        ValidationUtils.notNull(budgetYear, "预算年度为空,请检查!");
        ValidationUtils.notNull(iBudgetType, "预算类型为空,请检查!");
        Date startDate = periodRc.getDate("dstarttime");
        Date cutoffTime = periodRc.getDate("dendtime");
        LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(startDate, JBoltDateUtil.YMD));
        LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(cutoffTime, JBoltDateUtil.YMD));
        Calendar calendarStartDate1 = JBoltDateUtil.getCalendarByDate(startDate);
        Calendar calendarCutoffTimetDate1 = JBoltDateUtil.getCalendarByDate(cutoffTime);
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
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 10, periodRc.getInt("ibudgetyear") + "年"));
            //设置动态表头部分
            int yearForMonthAmount = 0;
            for (int i = 0; i < diffMonth; i++) {
                excelPositionDatas.add(JBoltExcelPositionData.create(8, 17 + i * 2, "数量"));
                excelPositionDatas.add(JBoltExcelPositionData.create(8, 18 + i * 2, "金额"));
                Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(startDate);
                Calendar calendarCutoffTimetDate = JBoltDateUtil.getCalendarByDate(cutoffTime);
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
        } else {
            List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
            //下载模板中动态设置预算期间的起止年份、部门、预算类型
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 5, budgetYear + "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 6, calendarStartDate1.get(Calendar.MONTH) + 1 + "月"));
            excelPositionDatas.add(JBoltExcelPositionData.create(5, 5, budgetYear + "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(5, 6, calendarCutoffTimetDate1.get(Calendar.MONTH) + 1 + "月"));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 7, "部门：" + cdepname));
            excelPositionDatas.add(JBoltExcelPositionData.create(4, 10, periodRc.getInt("ibudgetyear") + "年"));
            excelPositionDatas.add(JBoltExcelPositionData.create(6, 16, budgetYear));
            //查询下期修改当年的实绩数据用于导出
            List<Record> list = service.findExportExpenseBudgetTplNextEditDatas(para);
            if (CollUtil.isNotEmpty(list)) {
                int startRow = 7;
                for (int i = 0; i < list.size(); i++) {
                    excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 1, list.get(i).get("cdepname")));
                }
            }
            //2、创建JBoltExcel
            JBoltExcel jBoltExcel = JBoltExcel
                    .createByTpl("expenseBudgetNextPeriodTpl.xlsx")//创建JBoltExcel 从模板加载创建
                    .addSheet(//设置sheet
                            JBoltExcelSheet.create("预算录入")//创建sheet name保持与模板中的sheet一致
                                    .setPositionDatas(excelPositionDatas)//设置定位数据
                    )
                    .setFileName("费用预算导入模板");
            //3、导出
            renderBytesToExcelXlsxFile(jBoltExcel);
        }
    }

    /**
     * 费用预算导入
     *//*
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_IMPORT)
    public void importExpenseBudgetTpl() throws Exception {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        String filePath = getFile().getUploadPath() + FILE_PATH_MODIFIERS + getFile().getFileName();
        renderJson(service.importExpenseBudgetTpl(filePath));
    }*/
    /**
     * 费用预算编制-新增和修改界面 --可编辑表格导入
     */
	@UnCheck
	@CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void importTableExpenseBudgetTpl() throws Exception {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        String filePath = getFile().getUploadPath() + FILE_PATH_MODIFIERS + getFile().getFileName();
        renderJson(service.importTableExpenseBudgetTpl(filePath,getKv()));
    }
	/**
	 * 新增前打开期间选择
	 * */
	public void choosePeriodPage(){
		render("choose_period.html");
	}
    /**
     * 新增
     */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_FORMULATE_ADD)
    public void add() {//查询启动的期间
    	Record periodRc = periodService.findPeriodByDownTpl(Kv.by("periodId", getLong(0)));
        ValidationUtils.notNull(periodRc, "未维护启用期间!");
        Date dstarttime = periodRc.getDate("dstarttime");
        Date dendtime = periodRc.getDate("dendtime");
        List<Record> yearColumnTxtList = new ArrayList<Record>();
        List<String> monthColumnTxtList = new ArrayList<String>();
        List<Record> quantityAndAmountColumnList = new ArrayList<Record>();
        periodService.calcDynamicExpenseBudgetTableColumn(dstarttime,dendtime,yearColumnTxtList,monthColumnTxtList,quantityAndAmountColumnList);
        set("periodRc",periodRc);
        set("yearcolumntxtlist",yearColumnTxtList);
        set("monthcolumntxtlist",monthColumnTxtList);
        set("quantityandamountcolumnlist",quantityAndAmountColumnList);
        render("_addform.html");
    }
    /**
     * 费用预算新增表格提交
     */
    @UnCheck
    @RequestLimit(time=15,count=1)
    @Before(RequestLimitInterceptor.class)
    @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void saveTableByAdd() {
        renderJson(service.saveTableSubmitByAdd(getJBoltTable()));
    }
    
    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_FORMULATE_EDIT)
    public void edit() {
        ExpenseBudget expenseBudget = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(expenseBudget, JBoltMsg.DATA_NOT_EXIST);
        Date dstarttime = expenseBudget.getCBeginDate();
        Date dendtime = expenseBudget.getCEndDate();
        List<Record> yearColumnTxtList = new ArrayList<Record>();
        List<String> monthColumnTxtList = new ArrayList<String>();
        List<Record> quantityAndAmountColumnList = new ArrayList<Record>();
        periodService.calcDynamicExpenseBudgetTableColumn(dstarttime,dendtime,yearColumnTxtList,monthColumnTxtList,quantityAndAmountColumnList);
        set("expenseBudget", expenseBudget);
        set("yearcolumntxtlist",yearColumnTxtList);
        set("monthcolumntxtlist",monthColumnTxtList);
        set("quantityandamountcolumnlist",quantityAndAmountColumnList);
        render("_editform.html");
    }

    /**
     * 费用预算修改界面表格提交
     */
    @RequestLimit(time=15,count=1)
    @Before(RequestLimitInterceptor.class)
    @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void saveTableByUpdate() {
        renderJson(service.saveTableByUpdate(getJBoltTable()));
    }
    
    /**
     * 查询部门启用期间的费用预算主表数据
     * */
    @UnCheck
    public void findNewestExpenseBudgetByDeptCode(){
    	Kv para = getKv();
    	renderJsonData(service.findEffectivedExpenseBudgetByDeptCode(para));
    }

    /**
     * 查询费用预算修改界面的项目数据
     */
    @UnCheck
    public void findExpenseBudgetItemDatas(@Para(value="iexpenseid") Long iexpenseid){
    	renderJsonData(service.findExpenseBudgetItemDatas(iexpenseid));
    }
    
    /**
     * 打开导入未完成项目界面
     * */
    public void importUnfinishExpenseBudgetItem(){
    	Kv para = getKv();
    	ExpenseBudget unfinishItemExpenseBudget = service.findPreviousPeriodExpenseBudget(para);
    	ValidationUtils.notNull(unfinishItemExpenseBudget,"没有上期预算类型的费用预算数据，不能导入项目");
    	Date dstarttime = unfinishItemExpenseBudget.getCBeginDate();
        Date dendtime = unfinishItemExpenseBudget.getCEndDate();
        List<Record> yearColumnTxtList = new ArrayList<Record>();
        List<String> monthColumnTxtList = new ArrayList<String>();
        List<Record> quantityAndAmountColumnList = new ArrayList<Record>();
        periodService.calcDynamicExpenseBudgetTableColumn(dstarttime,dendtime,yearColumnTxtList,monthColumnTxtList,quantityAndAmountColumnList);
        set("unfinishItemExpenseBudget",unfinishItemExpenseBudget);
        set("yearcolumntxtlist",yearColumnTxtList);
        set("monthcolumntxtlist",monthColumnTxtList);
        set("quantityandamountcolumnlist",quantityAndAmountColumnList);
    	render("export_unfinish_expensebudgetitem.html");
    }
    /**
     * 打开导入上期项目界面
     * */
    public void importPreviousPeriodExpenseBudgetItem(){
    	Kv para = getKv();
    	ExpenseBudget previousPeriodItemExpenseBudget = service.findPreviousPeriodExpenseBudget(para);
    	ValidationUtils.notNull(previousPeriodItemExpenseBudget,"没有上期预算类型的费用预算数据，不能导入项目");
        set("previousPeriodItemExpenseBudget",previousPeriodItemExpenseBudget);
    	render("export_previousperiod_expensebudgetitem.html");
    }

    /**
     * 导入未完成项目界面查询费用预算项目数据
     */
    @UnCheck
    public void findUnfinishExpenseBudgetItemDatas(@Para(value="iexpenseid") Long iexpenseid){
    	renderJsonData(service.findUnfinishExpenseBudgetItemDatas(iexpenseid));
    }

    /**
     * 导入上期项目界面查询费用预算项目数据
     */
    @UnCheck
    public void findPreviousPeriodExpenseBudgetItemDatas(@Para(value="iexpenseid") Long iexpenseid){
    	renderJsonData(service.findPreviousPeriodExpenseBudgetItemDatas(iexpenseid));
    }
    /**
     * 导出费用预算编制列表数据
     * */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_FORMULATE_EXPORT)
    public void exportExpenseBudgetDatas(){
        Kv para = getKv();
        Long iexpenseid = para.getLong("iexpenseid");
        ExpenseBudget expenseBudget = service.findById(iexpenseid);
        List<Record> list = service.findExpenseBudgetItemDatas(iexpenseid);
        List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
    	List<JBoltExcelMerge> mergeList = new ArrayList<>(); //定位合并数据集合
    	service.exportExpenseBudgetExcel(expenseBudget,excelPositionDatas,mergeList,list);
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
    /**
     * 费用预算可编辑表格数据导出
     * */
    @UnCheck
    public void exportTableExpenseBudgetDatas(){
    	Kv para = getKv();
    	ExpenseBudget expenseBudget = useIfValid(ExpenseBudget.class,"expenseBudget");
    	List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
    	List<JBoltExcelMerge> mergeList = new ArrayList<>(); //定位合并数据集合
    	String tableDatas = para.getStr("tabledatas");
    	List<Record> list = JBoltModelKit.getFromRecords(JSON.parseArray(tableDatas));
    	service.exportExpenseBudgetExcel(expenseBudget,excelPositionDatas,mergeList,list);
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
    
/*    *//**
     * 提交审核
     * *//*
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_FORMULATE_SUBMIT)
    public void submit(){
    	Long iexpenseid = getLong(0);
    	renderJson(service.submit(iexpenseid));
    }*/

    /**
     * 费用预算期间对比数据源
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void periodContrastDatas(){
        renderJsonData(service.periodContrastDatas(getPageNumber(),getPageSize(),getKv()));
    }
    /**
     * 费用预算期间对比页面
     */
    @CheckPermission(PermissionKey.EXPENSEBUDGET_CONTRAST)
    public void periodContrastIndex(){
        render("period_contrast_index.html");
    }
    
	/**
	 * 费用预算期间对比table页面
	 * */
    @UnCheck
    public void periodContrastTableIndex(){
    	Kv para = getKv();
    	String dstarttime1 = para.getStr("dstarttime1");
    	String dendtime1 = para.getStr("dendtime1");
    	Integer ibudgettype1 = para.getInt("ibudgettype1");
    	String dstarttime2 = para.getStr("dstarttime2");
    	String dendtime2 = para.getStr("dendtime2");
    	Integer ibudgettype2 = para.getInt("ibudgettype2");
    	String dstarttime3 = para.getStr("dstarttime3");
    	String dendtime3 = para.getStr("dendtime3");
    	Integer ibudgettype3 = para.getInt("ibudgettype3");
    	//至少选择两种对比方案
    	Boolean flg = JBoltStringUtil.isNotBlank(dstarttime1) && JBoltStringUtil.isNotBlank(dendtime1) && ObjUtil.isNotNull(ibudgettype1)
    			&& JBoltStringUtil.isNotBlank(dstarttime2) && JBoltStringUtil.isNotBlank(dendtime2) && ObjUtil.isNotNull(ibudgettype2);
    	ValidationUtils.isTrue(flg, "至少选择两种完善对比方案!");
    	//选择的对比方案起止日期的月份要一致，并且起止日期包含的月份数量要一致
    	Date dstarttime1Date = JBoltDateUtil.toDate(dstarttime1,"yyyy-MM");
    	Date dendtime1Date = JBoltDateUtil.toDate(dendtime1,"yyyy-MM");
    	Date dstarttime2Date = JBoltDateUtil.toDate(dstarttime2,"yyyy-MM");
    	Date dendtime2Date = JBoltDateUtil.toDate(dendtime2,"yyyy-MM");
		LocalDate starTimet1LocalDate = LocalDate.parse(JBoltDateUtil.format(dstarttime1Date,JBoltDateUtil.YMD));
		LocalDate endTime1LocalDate1 = LocalDate.parse(JBoltDateUtil.format(dendtime1Date,JBoltDateUtil.YMD));
		LocalDate starTimet2LocalDate = LocalDate.parse(JBoltDateUtil.format(dstarttime2Date,JBoltDateUtil.YMD));
		LocalDate endTime2LocalDate1 = LocalDate.parse(JBoltDateUtil.format(dendtime2Date,JBoltDateUtil.YMD));
    	int dstarttime1Month = starTimet1LocalDate.getMonthValue();
    	int dendtime1Month = endTime1LocalDate1.getMonthValue();
    	int dstarttime2Month = starTimet2LocalDate.getMonthValue();
    	int dendtime2Month = endTime2LocalDate1.getMonthValue();
		long diffMonth1 = ChronoUnit.MONTHS.between(starTimet1LocalDate, endTime1LocalDate1) + 1;
		long diffMonth2 = ChronoUnit.MONTHS.between(starTimet2LocalDate, endTime2LocalDate1) + 1;
		Boolean flg2 = ObjUtil.equal(dstarttime1Month, dstarttime2Month) 
				&& ObjUtil.equal(dendtime1Month, dendtime2Month)
				&& ObjUtil.equal(diffMonth1, diffMonth2);
		ValidationUtils.isTrue(flg2, "请正确选择对比方案的起止日期!");
    	if(JBoltStringUtil.isNotBlank(dstarttime3) && JBoltStringUtil.isNotBlank(dendtime3) && ObjUtil.isNotNull(ibudgettype3)){
    		Date dstarttime3Date = JBoltDateUtil.toDate(dstarttime3,"yyyy-MM");
        	Date dendtime3Date = JBoltDateUtil.toDate(dendtime3,"yyyy-MM");
    		LocalDate starTimet3LocalDate = LocalDate.parse(JBoltDateUtil.format(dstarttime3Date,JBoltDateUtil.YMD));
    		LocalDate endTime3LocalDate1 = LocalDate.parse(JBoltDateUtil.format(dendtime3Date,JBoltDateUtil.YMD));
    		int dstarttime3Month = starTimet3LocalDate.getMonthValue();
        	int dendtime3Month = endTime3LocalDate1.getMonthValue();
        	long diffMonth3 = ChronoUnit.MONTHS.between(starTimet3LocalDate, endTime3LocalDate1) + 1;
    		Boolean flg3 = ObjUtil.equal(dstarttime1Month, dstarttime3Month) 
    				&& ObjUtil.equal(dendtime1Month, dendtime3Month)
    				&& ObjUtil.equal(diffMonth1, diffMonth3);
    		ValidationUtils.isTrue(flg3, "请正确选择第三种对比方案的起止日期!");
    	}else{
    		para.remove("dstarttime3");
    		para.remove("dendtime3");
    		para.remove("ibudgettype3");
    	}
        List<Record> monthColumnTxtList = new ArrayList<Record>();
    	service.calcDynamicPeriodContrastColumn(dstarttime1Date,dendtime1Date,monthColumnTxtList);
        set("monthcolumntxtlist",monthColumnTxtList);
        Record daterecord = new Record();
        daterecord.set("dstartdate", dstarttime1);
        daterecord.set("denddate",dendtime1);
        set("diffMonth", diffMonth1);
        set("daterecord",daterecord);
        set("pageId", JBoltRandomUtil.randomNumber(6));
    	render("period_contrast_index_table.html");
    }
    
    /**
     * 费用预算期间对比导出条件
     */
    public void contrastTplPage(){
        render("contrasttpl_index.html");
    }

    /**
     * 费用预算期间对比导出
     */
    public void contrastTplPageSubmit(){
        Kv kv = getKv();
        List<Record> list = service.periodContrastTplDatas(kv);
        String cdepname= "";
        String ibudgettype= ExpenseBudgetTypeEnum.toEnum(kv.getInt("ibudgettype")).getText();
        String dstarttime1=  kv.getStr("dstarttime1")==null? "": JBoltDateUtil.format(kv.getDate("dstarttime1"), "yyyy年MM月");
        String dstarttime2=  kv.getStr("dstarttime2")==null? "":JBoltDateUtil.format(kv.getDate("dstarttime2"), "yyyy年MM月");
        String dstarttime3=  kv.getStr("dstarttime3")==null? "":JBoltDateUtil.format(kv.getDate("dstarttime3"), "yyyy年MM月");
        String dendtime1=  kv.getStr("dendtime1")==null ? "":JBoltDateUtil.format(kv.getDate("dendtime1"), "yyyy年MM月");
        String dendtime2=  kv.getStr("dendtime2")==null ? "":JBoltDateUtil.format(kv.getDate("dendtime1"), "yyyy年MM月");
        String dendtime3=  kv.getStr("dendtime3")==null ? "":JBoltDateUtil.format(kv.getDate("dendtime1"), "yyyy年MM月");
        List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
        //下载模板中动态设置预算期间的起止年份、部门、预算类型
        excelPositionDatas.add(JBoltExcelPositionData.create(2, 5, dstarttime1));
        excelPositionDatas.add(JBoltExcelPositionData.create(2, 8, dstarttime2));
        excelPositionDatas.add(JBoltExcelPositionData.create(2, 11, dstarttime3));
        excelPositionDatas.add(JBoltExcelPositionData.create(3, 5, dendtime1));
        excelPositionDatas.add(JBoltExcelPositionData.create(3, 8, dendtime2));
        excelPositionDatas.add(JBoltExcelPositionData.create(3, 11, dendtime3));
        excelPositionDatas.add(JBoltExcelPositionData.create(5, 5, ibudgettype));
        excelPositionDatas.add(JBoltExcelPositionData.create(5, 8, ibudgettype));
        excelPositionDatas.add(JBoltExcelPositionData.create(5, 11, kv.getStr("dstarttime3")==null? "":ibudgettype));
        if (CollUtil.isNotEmpty(list)) {
            int startRow = 9;
            for (int i = 0; i < list.size(); i++) {
                cdepname =  list.get(i).getStr("cdepname");
                excelPositionDatas.add(JBoltExcelPositionData.create(2, 2, cdepname));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 1, list.get(i).get("chighestsubjectname")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 2, list.get(i).get("clowestsubjectname")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 3, list.get(i).get("contrast")));
                String s = "5".equals(list.get(i).getStr("ibudgettype")) ? "差异(②-①)" : "6".equals(list.get(i).getStr("ibudgettype")) ? "差异(③-①)" : "7".equals(list.get(i).getStr("ibudgettype")) ?
                        "差异(③-②)" : "1".equals(list.get(i).getStr("ibudgettype")) ?
                        "全年预算": "2".equals(list.get(i).getStr("ibudgettype")) ? "下期修改":"";
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 4, s));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 5, list.get(i).get("clowestsubjectname")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 6, list.get(i).get("previousyearmounthamount8")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 7, list.get(i).get("previousyearmounthamount9")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 8, list.get(i).get("previousyearmounthamount10")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 9, list.get(i).get("previousyearmounthamount11")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 10, list.get(i).get("previousyearmounthamount12")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 11, list.get(i).get("nowyearmounthamount1")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 12, list.get(i).get("nowyearmounthamount2")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 13, list.get(i).get("nowyearmounthamount3")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 14, list.get(i).get("nowyearmounthamount4")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 15, list.get(i).get("nowyearmounthamount5")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 16, list.get(i).get("nowyearmounthamount6")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 17, list.get(i).get("nowyearmounthamount7")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 18, list.get(i).get("nowyearmounthamount8")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 19, list.get(i).get("nowyearmounthamount9")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 20, list.get(i).get("nowyearmounthamount10")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 21, list.get(i).get("nowyearmounthamount11")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 22, list.get(i).get("nowyearmounthamount12")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 23, list.get(i).get("nextyearmounthamount1")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 24, list.get(i).get("nextyearmounthamount2")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 25, list.get(i).get("nextyearmounthamount3")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 26, list.get(i).get("nextyearmounthamount4")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 27, list.get(i).get("totalamount1")));
                excelPositionDatas.add(JBoltExcelPositionData.create(startRow + i, 28, list.get(i).get("totalamount2")));
            }
        }
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .createByTpl("expenseBudgetContrastTpl.xlsx")//创建JBoltExcel 从模板加载创建
                .addSheet(//设置sheet
                        JBoltExcelSheet.create("费用预算期间对比表")//创建sheet name保持与模板中的sheet一致
                                .setPositionDatas(excelPositionDatas)//设置定位数据
                )
                .setFileName("费用预算期间对比导出");
        //3、导出
        renderBytesToExcelXlsxFile(jBoltExcel);
    }
}

