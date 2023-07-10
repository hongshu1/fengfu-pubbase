package cn.rjtech.admin.investmentplan;

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
import cn.jbolt.core.poi.excel.JBoltExcelPositionData;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltRandomUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.InvestmentPlan;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;
/**
 * 投资计划主表 Controller
 * @ClassName: InvestmentPlanAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-18 09:33
 */
@CheckPermission(PermissionKey.INVESTMENT_PLAN_FORMULATE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/investmentplan", viewPath = "/_view/admin/investmentplan")
public class InvestmentPlanAdminController extends BaseAdminController {

	@Inject
	private InvestmentPlanService service;
	@Inject
	private PeriodService periodService;
	@Inject
	private DepartmentService departmentService;
	@Inject
	private ExpenseBudgetItemService expenseBudgetItemService;
	@Inject
	private ExpenseBudgetService expenseBudgetService;
	@Inject
	private InvestmentPlanService investmentPlanService;
	@Inject
	private InvestmentPlanItemService investmentPlanItemService;
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
	@CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
	@CheckPermission(PermissionKey.INVESTMENT_PLAN_FORMULATE_ADD)
	public void add() {
		Record periodRc = periodService.findPeriodByDownTpl(Kv.by("periodId", getLong(0)));
		ValidationUtils.notNull(periodRc, "未维护启用期间!");
		set("periodRc",periodRc);
		render("_addform.html");
	}

   /**
	* 编辑
	*/
	@CheckPermission(PermissionKey.INVESTMENT_PLAN_FORMULATE_ADD)
	public void edit() {
		InvestmentPlan investmentPlan = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(investmentPlan, JBoltMsg.DATA_NOT_EXIST);
		set("investmentPlan", investmentPlan);
		render("_editform.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(InvestmentPlan.class, "investmentPlan")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(InvestmentPlan.class, "investmentPlan")));
	}

   /**
	* 批量删除
	*/
	@CheckPermission(PermissionKey.INVESTMENT_PLAN_FORMULATE_DELETE)
	@CheckDataPermission(operation = DataOperationEnum.DELETE, type = BusObjectTypeEnum.DEPTARTMENT)
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

	/**
	 * 跳转投资计划模板下载界面
	 * */
	public void dowloadTplPage(){
		//查询启动的期间
		Record periodRc = periodService.findPeriodByDownTpl(Kv.by("isenabled","1").set("iservicetype",ServiceTypeEnum.INVESTMENT_PLAN.getValue()));
		ValidationUtils.notNull(periodRc, "未维护启用期间!");
		set("periodRc",periodRc);
		render("dowloadtpl_index.html");
	}

	/*public void dowloadTplPageSubmit(){
		Kv para = getKv();
		String cdepcode = para.getStr("cdepcode");//部门
		Long periodId = para.getLong("periodId");
		ValidationUtils.notBlank(cdepcode, "请选择U8部门!");
		ValidationUtils.notNull(periodId, "期间ID为空");
		//查询启动的期间
		Record periodRc = periodService.findPeriodByDownTpl(Kv.by("periodId", periodId));
		ValidationUtils.notNull(periodRc, "未维护启用期间,模板下载失败!");
		Integer budgetYear = periodRc.getInt("ibudgetyear");//预算年度
		Integer iBudgetType = periodRc.getInt("ibudgettype");//预算类型
		ValidationUtils.notNull(budgetYear, "请选择预算年度!");
		ValidationUtils.notNull(iBudgetType, "请选择预算类型!");
		List<JBoltExcelPositionData> excelPositionDatas= new ArrayList<>();//定位数据集合
		excelPositionDatas.add(JBoltExcelPositionData.create(4, 3, budgetYear+"年"));
		excelPositionDatas.add(JBoltExcelPositionData.create(4, 5, InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText()));
		excelPositionDatas.add(JBoltExcelPositionData.create(4, 7, departmentService.getCdepName(cdepcode)));
		//下半年修订，实绩预测查询导出数据:1未完成的投资计划项目，已发生实绩的期数和预算数据
		service.constructInvestmentExportDownTplData(iBudgetType,budgetYear,excelPositionDatas);
		//2、创建JBoltExcel
		JBoltExcel jBoltExcel=JBoltExcel
				.createByTpl("investmentplanTpl.xlsx")//创建JBoltExcel 从模板加载创建
				.addSheet(//设置sheet
						JBoltExcelSheet.create("投资计划录入表")//创建sheet name保持与模板中的sheet一致
						.setPositionDatas(excelPositionDatas)//设置定位数据
						)
				.setFileName("投资计划导入模板");
		//3、导出
		renderBytesToExcelXlsxFile(jBoltExcel);
	}*/
    /**
     * 投资计划导入
     */
	/*@CheckPermission(PermissionKey.INVESTMENT_PLAN_IMPORT)
    public void importInvestmentPlanTpl() throws Exception {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        String filePath = getFile().getUploadPath() + FILE_PATH_MODIFIERS + getFile().getFileName();
        renderJson(service.importInvestmentPlanTpl(filePath));
    }*/
	/**
     * 投资计划编制可编辑表格导入
     */
	@UnCheck
	@CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void importTableInvestmentPlanTpl() throws Exception {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        String filePath = getFile().getUploadPath() + FILE_PATH_MODIFIERS + getFile().getFileName();
        renderJson(service.importTableInvestmentPlanTpl(filePath,getKv()));
    }
	/**
     * 投资计划新增-表格提交
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void saveTableByAdd() {
        renderJson(service.saveTableSubmitByAdd(getJBoltTable()));
    }
    /**
     * 投资计划修改-表格提交
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void saveTableByEdit(){
    	renderJson(service.saveTableSubmitByEdit(getJBoltTable()));
    }
    
    
    /**
     * 投资计划编制导出
     * */
    @CheckPermission(PermissionKey.INVESTMENT_PLAN_FORMULATE_EXPORT)
    public void exportInvestmentPlanDatas(){
    	Kv para = getKv();
    	Long investmentPlanId = para.getLong("iplanid");
    	InvestmentPlan investmentPlan = service.findById(investmentPlanId);
    	List<Record> itemList = service.findInvestmentPlanItemDatas(investmentPlanId);
    	List<JBoltExcelPositionData> excelPositionDatas=new ArrayList<JBoltExcelPositionData>();//定位数据集合
    	service.contrustExportExcelPositionDatas(excelPositionDatas,investmentPlan,itemList);
    	//2、创建JBoltExcel
		JBoltExcel jBoltExcel=JBoltExcel
				.createByTpl("investmentplanTpl.xlsx")//创建JBoltExcel 从模板加载创建
				.addSheet(//设置sheet
						JBoltExcelSheet.create("投资计划录入表")//创建sheet name保持与模板中的sheet一致
						.setPositionDatas(excelPositionDatas)//设置定位数据
						)
				.setFileName("投资计划导出");
		//3、导出
		renderBytesToExcelXlsxFile(jBoltExcel);

    }
    /**
     * 投资计划可编辑表格数据导出
     * */
    @UnCheck
    public void exportTableInvestmentPlanDatas(){
    	Kv para = getKv();
    	InvestmentPlan investmentPlan = useIfValid(InvestmentPlan.class,"investmentPlan");
    	String tableDatas = para.getStr("tabledatas");
    	List<Record> itemList = JBoltModelKit.getFromRecords(JSON.parseArray(tableDatas));
    	List<JBoltExcelPositionData> excelPositionDatas=new ArrayList<JBoltExcelPositionData>();//定位数据集合
    	service.contrustExportExcelPositionDatas(excelPositionDatas,investmentPlan,itemList);
    	//2、创建JBoltExcel
		JBoltExcel jBoltExcel=JBoltExcel
				.createByTpl("investmentplanTpl.xlsx")//创建JBoltExcel 从模板加载创建
				.addSheet(//设置sheet
						JBoltExcelSheet.create("投资计划录入表")//创建sheet name保持与模板中的sheet一致
						.setPositionDatas(excelPositionDatas)//设置定位数据
						)
				.setFileName("投资计划导出");
		//3、导出
		renderBytesToExcelXlsxFile(jBoltExcel);
    }


    /**
     * 投资计划编制编辑界面查询投资计划项目数据
     */
    @UnCheck
    public void findInvestmentPlanItemDatas(@Para(value="iplanid") Long iplanid){
    	renderJsonData(service.findInvestmentPlanItemDatas(iplanid));
    }

    /**
     * 投资计划编制详情界面查询投资计划项目数据
     */
    @UnCheck
    public void findInvestmentPlanItemForDetail(@Para("investmentPlanId") Long investmentPlanId) {
        renderJsonData(service.findInvestmentPlanItemForDetail(investmentPlanId));
    }

    /**
     * 查询部门生效的投资计划主表数据
     */
    @UnCheck
    public void findNewestInvestmentByDeptCode(){
    	Kv para = getKv();
    	renderJsonData(service.findEffectivedInvestmentByDeptCode(para));
    }

    /**
     * 打开导入未完成项目界面
     * */
    public void importUnfinishInvestmentPlanItem(){
    	Kv para = getKv();
    	InvestmentPlan unfinishItemInvestmentPlan = service.findPreviousPeriodInvestmentPlan(para);
    	ValidationUtils.notNull(unfinishItemInvestmentPlan,"没有上期预算类型的投资计划数据，不能导入未完成项目");
    	set("unfinishItemInvestmentPlan",unfinishItemInvestmentPlan);
    	render("export_unfinish_investmentplanitem.html");
    }

    /**
     * 导入未完成项目界面 - 查询未完成项目数据
     */
    @UnCheck
    public void findUnfinishInvestmentPlanItemDatas(@Para(value="iplanid") Long iplanid){
    	renderJsonData(service.findUnfinishInvestmentPlanItemDatas(iplanid));
    }
    
    @CheckPermission(PermissionKey.INVESTMENT_BUDGET_ACTUAL_DIFFERENCE)
    public void budgetActualDifferenceIndex(){
    	render("budget_actual_difference.html");
    }

    /**
     * 投资预实差异管理表数据查询
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void findBudgetActualDifferenceDatas(){
    	renderJsonData(service.findBudgetActualDifferenceDatas(getKv()));
    }

    /**
     * 投资汇总表首页
     */
    @CheckPermission(PermissionKey.INVESTMENT_PLAN_SUMMARY)
    public void investmentPlanGroupSummaryIndex(){
    	render("investment_plan_summary.html");
    }

    /**
     * 投资汇总表数据查询
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void findInvestmentPlanGroupSummaryDatas(){
    	Kv para = getKv();
    	Integer ibudgetyear = para.getInt("ibudgetyear");
		ValidationUtils.notNull(ibudgetyear, "请选择预算年度!");
    	renderJsonData(service.findInvestmentPlanGroupSummaryDatas(para));
    }

    /**
     * 投资情况查询表首页
     */
    @CheckPermission(PermissionKey.INVESTMENT_PLAN_SITUATION_INDEX)
    public void investmentPlanSituationIndex(){
    	render("investment_plan_situation.html");
    }

    /**
     * 投资情况查询表table页面
     */
    @UnCheck
    public void investmentPlanSituationTableIndex(){
    	Kv para = getKv();
    	String dstartdate = para.getStr("dstartdate");
    	String denddate = para.getStr("denddate");
        List<Record> yearColumnTxtList = new ArrayList<Record>();
        List<Record> monthColumnTxtList = new ArrayList<Record>();
        if(JBoltStringUtil.isNotBlank(dstartdate)&&JBoltStringUtil.isNotBlank(denddate)){
	        service.calcDynamicInvestmentPlanSituationColumn(JBoltDateUtil.toDate(dstartdate, "yyyy-MM"),JBoltDateUtil.toDate(denddate, "yyyy-MM"),yearColumnTxtList,monthColumnTxtList);
	        set("yearcolumntxtlist",yearColumnTxtList);
	        set("monthcolumntxtlist",monthColumnTxtList);
	        Record daterecord = new Record();
	        daterecord.set("dstartdate", dstartdate);
	        daterecord.set("denddate",denddate);
	        set("daterecord",daterecord);
        }
        set("pageId", JBoltRandomUtil.randomNumber(6));
    	render("investment_plan_situation_table.html");
    }

    /**
     * 投资情况查询表
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void findInvestmentPlanItemSituationDatas(){
    	renderJsonData(service.findInvestmentPlanItemSituationDatas(getKv()));
    }

    /**
     * 执行进度跟踪表首页
     */
    @CheckPermission(PermissionKey.EXECUTION_PROGRESS_TRACKING)
    public void executionProgressTrackingIndex(){
    	render("execution_progress_tracking_V2.html");
    }

    /**
     * 执行进度跟踪表-费用tab页面
     */
    @UnCheck
    public void executionProgressTrackingExpense(){
    	render("execution_progress_tracking_expense.html");
    }

    /**
     * 执行进度跟踪表-投资tab页面
     */
    @UnCheck
    public void executionProgressTrackingInvestment(){
    	render("execution_progress_tracking_investment.html");
    }

    /**
     * 执行进度跟踪表-费用tab数据查询
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void findExecutionProgressTrackingExpenseDatas(){
    	renderJsonData(service.findExecutionProgressTrackingExpenseDatas(getKv()));
    }

    /**
     * 执行进度跟踪表-投资tab数据查询
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void findExecutionProgressTrackingInvestmentDatas(){
    	renderJsonData(service.findExecutionProgressTrackingInvestmentDatas(getKv()));
    }
    
}
