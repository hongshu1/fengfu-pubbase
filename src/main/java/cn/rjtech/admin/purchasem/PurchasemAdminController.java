package cn.rjtech.admin.purchasem;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.user.UserService;
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
import cn.jbolt.core.service.JBoltFileService;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.exch.ExchService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.foreigncurrency.ForeignCurrencyService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.admin.proposald.ProposaldService;
import cn.rjtech.admin.proposalm.ProposalmService;
import cn.rjtech.admin.purchased.PurchasedService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.cache.PurchasedCache;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.ProposalmSourceTypeEnum;
import cn.rjtech.enums.PurchaseRefTypeEnum;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 申购单管理 Controller
 *
 * @ClassName: PurchasemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 14:52
 */
@CheckPermission(PermissionKey.PURCHASE_MANAGEMENT)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchasem", viewPath = "/_view/admin/purchasem")
public class PurchasemAdminController extends BaseAdminController {

    @Inject
    private PurchasemService service;
    @Inject
    private PurchasedService purchasedService;
    @Inject
    private JBoltFileService jBoltFileService;
    @Inject
    private UserService userService;
    @Inject
    private ProposaldService proposaldService;
    @Inject
    private GlobalConfigService globalConfigService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private VendorService vendorRecordService;
    @Inject
    private PurchaseTypeService purchaseTypeService;
    @Inject
    private ExpenseBudgetItemService expenseBudgetItemService;
    @Inject
    private ProposalmService proposalmService;
    @Inject
    private InvestmentPlanItemService investmentPlanItemService;
    @Inject
    private ForeignCurrencyService foreignCurrencyService;
    @Inject
    private ExchService exchService;
    @Inject
    private PeriodService periodService;
    @Inject
    private ExpenseBudgetService expenseBudgetService;
    @Inject
    private InvestmentPlanService investmentPlanService;
    @Inject
    private DepartmentService departmentService;
    /**
     * 首页
     */
    public void index() {
        set("purchaseManagement", true);
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getKv()));
    }

    /**
     * 详情
     */
    @UnCheck
    public void details() {
    	Record rc = service.getPurchasemByIautoid(useIfPresent(getLong(0)));
        set("purchasem", rc);
        // 控制详情页面对子表数据的操作
        set("details", true);
        Integer ireftype = rc.getInt("ireftype");
        if(ObjUtil.equal(PurchaseRefTypeEnum.PROPOSAL.getValue(), ireftype))
        	render("details.html");
        else
        	render("ref_budget_form.html");
    }

    /**
     * 打印预览
     */
    public void printData() {
        renderJson(service.printData(getLong("iautoid")));
    }

    /**
     * 提交采购（申购单推送U8）
     */
    @CheckPermission(PermissionKey.PURCHASE_MANAGEMENT_SUMBITPURCHASE)
    public void sumbitPurchase() {
        renderJsonData(service.sumbitPurchase(useIfPresent(getLong(0))));
    }

    /**
     * 撤销提购（删除U8请购单）
     */
    @CheckPermission(PermissionKey.PURCHASE_MANAGEMENT_REVOCATIONSUBMIT)
    public void revocationSubmit() {
        renderJsonData(service.revocationSubmit(get("ids")));
    }

    /**
     * 撤销提审
     */
    @CheckPermission(PermissionKey.PURCHASE_WITHDRAW)
    public void withdraw() {
        ValidationUtils.validateId(getLong("iautoid"), "禀议书ID");
        renderJson(service.withdraw(getLong("iautoid")));
    }

    /**
     * 参考费用预算或者投资计划按钮点击选择期间页面
     * */
    @UnCheck
    public void choosePeriod(){
    	render("choose_period.html");
    }
    /**
     * 参照费用预算/投资计划
     * @param iproposalmid
     */
    @UnCheck
    public void chooseExpenseBudgetOrInvestmentPlan(@Para(value = "iperiodIds") String iperiodIds){
    	ValidationUtils.notBlank(iperiodIds, JBoltMsg.PARAM_ERROR);
    	List<Period> periodList = periodService.findListModelByIds(iperiodIds);
    	Period expenseBudgetPeriod = null;
    	Period investmentPlanPeriod = null;
    	for (Period period : periodList) {
    		if(period.getIservicetype() == ServiceTypeEnum.EXPENSE_BUDGET.getValue()) expenseBudgetPeriod = period;
    		if(period.getIservicetype() == ServiceTypeEnum.INVESTMENT_PLAN.getValue()) investmentPlanPeriod = period;
		}
    	set("expenseBudgetPeriod",expenseBudgetPeriod);
    	set("investmentPlanPeriod",investmentPlanPeriod);
        render("choose_expensebudget_or_investmentplan_refbudget.html");
    }
    @UnCheck
    public void chooseExpenseBudgetOrInvestmentAppend(){
    	Kv para = getKv();
    	String cdepcode = para.getStr("cdepcode");
    	ValidationUtils.notNull(cdepcode, "申购部门为空!");
    	ExpenseBudget expenseBudget = expenseBudgetService.findEffectivedExpenseBudgetByDeptCode(para);
    	InvestmentPlan investmentPlan = investmentPlanService.findEffectivedInvestmentByDeptCode(para);
    	Period expenseBudgetPeriod = null;
    	Period investmentPlanPeriod = null;
    	if(expenseBudget!=null) expenseBudgetPeriod = periodService.findById(expenseBudget.getIPeriodId());
    	if(investmentPlan!=null) investmentPlanPeriod = periodService.findById(investmentPlan.getIPeriodId());
    	keepPara();
    	set("expenseBudgetPeriod",expenseBudgetPeriod);
    	set("investmentPlanPeriod",investmentPlanPeriod);
    	render("choose_expensebudget_or_investmentplan_append.html");
    }
    /**
     * 选择费用预算列表
     */
    @CheckPermission(PermissionKey.PROPOSALM_SAVE)
    public void chooseExpenseBudget(@Para(value = "iperiodid") Long iperiodid) {
    	Period period = periodService.findById(iperiodid);
    	keepPara();
    	if(period!=null){
	    	List<Record> yearColumnTxtList = new ArrayList<Record>();
	        List<String> monthColumnTxtList = new ArrayList<String>();
	        List<Record> quantityAndAmountColumnList = new ArrayList<Record>();
	    	periodService.calcDynamicExpenseBudgetTableColumn(period.getDstarttime(),period.getDendtime(),yearColumnTxtList,monthColumnTxtList,quantityAndAmountColumnList);
	        set("period", period);
	        set("yearcolumntxtlist",yearColumnTxtList);
	        set("monthcolumntxtlist",monthColumnTxtList);
	        set("quantityandamountcolumnlist",quantityAndAmountColumnList);
    	}
        render("choose_expensebudget.html");
    }
    
    /**
     * 选择费用预算列表数据
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void findExpenseBudgetItemDatas() {
        renderJsonData(service.findExpenseBudgetItemDatas(getKv()));
    }    
    
    /**
     * 选择投资计划
     */
    @CheckPermission(PermissionKey.PROPOSALM_SAVE)
    public void chooseInvestmentPlan(@Para(value = "iperiodid") Long iperiodid) {
    	Period period = periodService.findById(iperiodid);
    	keepPara();
    	set("period", period);
        render("choose_investmentplan.html");
    }    
    
    /**
     * 选择投资计划列表数据
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void findInvestmentPlanItemDatas() {
        renderJsonData(service.findInvestmentPlanItemDatas(getKv()));
    }    
    /**
     * 参照费用和投资打开申购单新增界面
     * */
    public void referBudgetToAdd(){
    	
    }
    /**
     * 参照预算数据填充
     *
     * @param cdepcode
     * @param itemidandtypes
     */
    public void purchaseChooseDataBuild(@Para(value = "cdepcode") String cdepcode, @Para(value = "itemidandtypes") String itemidandtypes) {
        ValidationUtils.notBlank(cdepcode, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(itemidandtypes, "缺少项目ID");

        // 填充主表数据
        Purchasem purchasem = new Purchasem();
        set("purchasem", purchasem);

        // 填充细表数据
        List<Record> purchaseds = new ArrayList<>();
        List<Record> records = service.purchaseChoose(itemidandtypes);
        for (Record record : records) {
            purchaseds.add(new Record()
                    .set("cbudgetno", record.getStr("cno"))
                    .set("isourcetype", record.get("iproposalchoosetype"))
                    .set("ibudgeid", record.get("iautoid"))
                    .set("isourceid", record.get("iautoid"))
                    .set("nflat", 1)
            );
        }
        set("purchaseds", purchaseds);

        // 设置申购金额限制
        set("amountratio", globalConfigService.getConfigValue("purchase_amount_ratio"));
        set("ratio", globalConfigService.getConfigValue("purchase_ratio"));
        set("taxrate", globalConfigService.getConfigValue("purchase_tax_rate"));

        // 设置禀议信息只读
        set("add", true);
        set("purchasechoosedatabuild", false);
        set("ibudgeids", itemidandtypes);
        render("add.html");
    }

    /**
     * 获取存货信息autocomplete
     */
    @UnCheck
    public void getInventoryAutocomplete() {
        renderJsonData(inventoryService.getInventoryAutocomplete(getKv()));
    }
    /**
     * 获取存货信息分页
     */
    @UnCheck
    public void getInventoryPaginate() {
        renderJsonData(inventoryService.getInventoryPaginate(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 获取供应商信息
     */
    @UnCheck
    public void getVendor() {
        renderJsonData(vendorRecordService.getVendorList(getKv()));
    }
    /**
     * 获取供应商信息分页
     */
    @UnCheck
    public void getVendorPaginate() {
        renderJsonData(vendorRecordService.getVendorPaginate(getPageNumber(),getPageSize(),getKv()));
    }
    /**
     * 获取金额信息
     */
    @UnCheck
    public void getMoney() {
        renderJsonData(service.getMoney(getKv()));
    }

    /**
     * 采购类型
     */
    @UnCheck
    public void getPurchaseType() {
        renderJsonData(purchaseTypeService.findAll());
    }

    /**
     * 获取供应商存货不含税价格
     */
    @UnCheck
    public void getVenPrice() {
        renderJson(service.getIUnitPrice(get("cinvcode"), get("cvencode"), null));
    }

    /**
     * 获取币种
     */
    @UnCheck
    public void getAllCexchName() {
        renderJsonData(foreignCurrencyService.getAllCexchName());
    }

    /**
     * 获取币种汇率
     */
    @UnCheck
    public void getNflat() {
        renderJsonData(exchService.getNflat(get("cexchname")));
    }

    /**
     * 选择存货
     */
    @UnCheck
    public void chooseInventory() {
        render("choose_inventory.html");
    }

    /**
     * 选择存货树形数据源
     */
    @UnCheck
    public void inventorTree() {
        renderJsonData(service.inventorTree());
    }

    /**
     * 选择供应商
     */
    @UnCheck
    public void chooseVendor() {
        render("choose_vendor.html");
    }
    
    /**
     * 选择供应商树形数据源
     */
    @UnCheck
    public void vendorclassTree() {
        renderJsonData(service.vendorclassTree());
    }

    // ----------------------------------------------------
    //  申购单编制
    // ----------------------------------------------------

    @CheckPermission(PermissionKey.PURCHASE_INSTRUMENT)
    public void instrument() {
        set("purchaseInstrument", true);
        render("index.html");
    }

    /**
     * 新增-参照禀议书
     */
    @CheckPermission(PermissionKey.PURCHASE_INSTRUMENT_CHOOSE_PROPOSALM)
    public void chooseProposalm() {
        render("choose_proposalm_v2.html");
    }

    /**
     * 新增-选择禀议细表数据
     */
    @CheckPermission(PermissionKey.PURCHASE_INSTRUMENT_CHOOSE_PROPOSALM)
    public void optionProposald() {
        ValidationUtils.notNull(get("ifirstsourceproposalid"), JBoltMsg.DATA_NOT_EXIST);
        set("ifirstsourceproposalid", get("ifirstsourceproposalid"));
        set("notexistsiprojectcardids", get("notexistsiprojectcardids"));
        set("optionProposald", true);
        render("choose_proposalm_option_proposald.html");
    }
    /**
     * 参照禀议书新增-填充基本数据
     */
    @CheckPermission(PermissionKey.PURCHASE_INSTRUMENT_CHOOSE_PROPOSALM)
    public void instrumentAdd() {
        ValidationUtils.notNull(get("iproposaldids"), JBoltMsg.PARAM_ERROR);
        String iproposaldids = get("iproposaldids");
        List<Proposald> proposalds = proposaldService.getListByIds(iproposaldids);
        ValidationUtils.notEmpty(proposalds, "勾选的禀议书项目数据不存在，请检查!");
        Long iproposalmid = proposalds.get(0).getIproposalmid();
        Proposalm proposalm = proposalmService.findById(iproposalmid);
        Long ifirstsourceproposalid = proposalm.getIFirstSourceProposalId();
        ValidationUtils.notNull(proposalm, "禀议书不存在!");
        Record moneyRc = service.getMoney(Kv.by("ifirstsourceproposalid",ifirstsourceproposalid));
        moneyRc.set("cdepcode", proposalm.getCDepCode());
        moneyRc.set("cdepname", departmentService.getCdepName(proposalm.getCDepCode()));
        set("purchasem",moneyRc);
        List<Record> purchaseds = new ArrayList<>();
        for (Proposald proposald : proposalds) {
            // 预算/投资编码
            String cbudgetno = "";
            BigDecimal ibudgetmoney = BigDecimal.ZERO;
            if (proposald.getIsourcetype() == ProposalmSourceTypeEnum.EXPENSE_BUDGET.getValue()) {
                ExpenseBudgetItem expenseBudgetItem = expenseBudgetItemService.findById(proposald.getIsourceid());
                cbudgetno = Optional.ofNullable(expenseBudgetItem).map(ExpenseBudgetItem::getCbudgetno).orElse(null);
                ibudgetmoney = expenseBudgetItem.getIamounttotal();
            } else if (proposald.getIsourcetype() == ProposalmSourceTypeEnum.INVESTMENT_PLAN.getValue()) {
                InvestmentPlanItem investmentPlanItem = investmentPlanItemService.findById(proposald.getIsourceid());
                cbudgetno = Optional.ofNullable(investmentPlanItem).map(InvestmentPlanItem::getCplanno).orElse(null);
                ibudgetmoney = investmentPlanItem.getIamounttotal();
            }
            Vendor vendor = vendorRecordService.findByCode(proposald.getCvencode());
            String cvenname = null;
            cvenname = vendor != null ? vendor.getCVenName() : cvenname;
            purchaseds.add(new Record()
                    .set("iproposaldid", proposald.getIautoid())
                    .set("itaxrate", proposald.getItaxrate())
                    .set("cvencode", proposald.getCvencode())
                    .set("cvenname", PurchasedCache.me.getCvenname(proposald.getCvencode()))
                    .set("ccurrency", proposald.getCcurrency())
                    .set("nflat", 1)
                    .set("cbudgetno", cbudgetno)
                    .set("ibudgetmoney", ibudgetmoney)
                    .set("isubitem", IsEnableEnum.NO.getValue())
                    .set("isourcetype", proposald.getIsourcetype())
                    .set("isourceid", proposald.getIsourceid())
                    .set("iprojectcardid", proposald.getIprojectcardid())
                    .set("cunit", proposald.getCunit())
                    .set("iquantity", proposald.getIquantity())
                    .set("iprice", proposald.getIunitprice())
                    .set("nflat", proposald.getNflat())
                    .set("itaxrate", proposald.getItaxrate())
                    .set("ddemandate", proposald.getDdemanddate())
                    .set("cvencode", proposald.getCvencode())
                    .set("cvenname", cvenname)
                    .set("ccurrency", proposald.getCcurrency())
                    .set("itax", proposald.getItax())
                    .set("itaxexclusivetotalamount", proposald.getImoney())
                    .set("itotalamount", proposald.getIsum())
                    .set("inatunitprice", proposald.getInatunitprice())
                    .set("inattax", proposald.getInattax())
                    .set("inatmoney", proposald.getInatmoney())
                    .set("inatsum", proposald.getInatsum())
            );
        }
        set("purchaseds", purchaseds);
        // 设置禀议信息只读
        set("add", true);
        render("add.html");
    }

    /**
     * 参照禀议书表格提交-新增/修改提交申购单
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void saveTableSubmit() {
        renderJsonData(service.saveTableSubmit(getJBoltTables()));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.PURCHASE_INSTRUMENT_EDIT)
    public void instrumentEdit() {
    	Purchasem purchasem = service.findById(useIfPresent(getLong(0)));
    	PurchaseRefTypeEnum refTypeEnum = PurchaseRefTypeEnum.toEnum(purchasem.getIRefType());
    	set("edit",true);
    	switch (refTypeEnum) {
		case PROPOSAL:
			set("purchasem", service.getPurchasemByIautoid(useIfPresent(getLong(0))));
			render("edit.html");
			break;
		case BUDGET:
			Record record = service.details(Kv.by("iautoid", purchasem.getIAutoId()));
			set("purchasem", record);
			render("ref_budget_form.html");
			break;			
		default:
			break;
		}
        
        
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.PURCHASE_INSTRUMENT_DELETE)
    public void instrumentDeleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 参照禀议书界面-禀议书主表数据查询
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void chooseProposalmDatas(){
    	renderJsonData(service.chooseProposalmDatas(getKv()));
    }
    /**
     * 参照禀议书界面-禀议书项目数据查询
     * */
    public void chooseProposalmDatasDetail(){
    	renderJsonData(service.chooseProposalmDatasDetail(getKv()));
    }
    
    /**
     * 生效
     * */
/*    @CheckPermission(PermissionKey.PURCHASE_MANAGEMENT_EFFECT)
    public void effect(){
    	renderJson(service.effect(getLong(0)));
    }*/
    
    /**
     * 新增申购单-参照预算/投资
     * @param cdepcode
     * @param itemidandtypes
     */
   public void addBudgetOrInvestmentPlan(@Para(value="cdepcode") String cdepcode,@Para(value = "expensebudgetitemid") String expensebudgetitemid,@Para(value = "investmentplanitemid") String investmentplanitemid){
	   String budgetitemids = "";
	   if(JBoltStringUtil.isNotBlank(expensebudgetitemid)) budgetitemids += expensebudgetitemid+",";
	   if(JBoltStringUtil.isNotBlank(investmentplanitemid)) budgetitemids += investmentplanitemid+",";
	   ValidationUtils.notBlank(budgetitemids, "请选择费用/计划!");
	   budgetitemids = budgetitemids.substring(0, budgetitemids.lastIndexOf(","));
	   ValidationUtils.notBlank(cdepcode, "部门为空!");
       // 填充细表数据
       List<Record> purchaseds = new ArrayList<>();
       List<Record> records = service.refBudgetItemDatas(Kv.by("budgetitemids", budgetitemids));
       for (Record record : records) {
           purchaseds.add(new Record()
                   .set("cbudgetno", record.getStr("cbudgetno"))
                   .set("isourcetype", record.get("isourcetype"))
                   .set("isourceid", record.get("iautoid"))
                   .set("iprojectcardid", record.get("iprojectcardid"))
                   .set("ibudgetmoney", record.get("ibudgetmoney"))
                   .set("ibudgetmoneyhidden", record.get("ibudgetmoney"))
                   .set("ibudgetalreadypurchasemoney", record.get("ibudgetalreadypurchasemoney"))
                   .set("itaxrate", record.get("itaxrate"))
                   .set("isubitem", IsEnableEnum.NO.getValue())
                   .set("nflat", 1)
           );
       }
       Purchasem purchasem = new Purchasem();
       set("add",true);
       String cdepname = departmentService.getCdepName(cdepcode);
       Record purchasemRc = purchasem.toRecord().set("cdepname",cdepname).set("cdepcode", cdepcode);
       set("purchasem", purchasemRc);
       set("purchaseds", purchaseds);
       render("ref_budget_form.html");
   }
   /**
    * 参照预算保存申购单
    * */
   @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
   public void refBudgetSaveTableSubmit(){
	   renderJsonData(service.refBudgetSaveTableSubmit(getJBoltTables()));
   }
   
   /**
    * 申购单可编辑表格数据导出
    * */
   @UnCheck
   public void exportTablePurchasedDatas(){
		Kv para = getKv();
		String tableDatas = para.getStr("tabledatas");
		List<Record> itemList = JBoltModelKit.getFromRecords(JSON.parseArray(tableDatas));
		List<JBoltExcelPositionData> excelPositionDatas=new ArrayList<JBoltExcelPositionData>();//定位数据集合
		int startRow = 2;
		for(int i=0; i<itemList.size(); i++){
			int row = startRow + i;
			Record excelRow = itemList.get(i);
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 1, i+1));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 2, ProposalmSourceTypeEnum.toEnum(excelRow.getInt("isourcetype")).getText()));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 3, excelRow.get("ibudgetmoney")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 4, excelRow.get("cbudgetno")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 5, excelRow.get("cinvcode")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 6, excelRow.get("cinvname")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 7, excelRow.get("cinvstd")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 8, excelRow.get("cunit")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 9, excelRow.get("caddress")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 10, excelRow.get("iquantity")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 11, excelRow.get("iprice")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 12, excelRow.get("nflat")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 13, excelRow.get("itaxrate")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 14, excelRow.get("ddemandate")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 15, excelRow.get("cvencode")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 16, excelRow.get("ccurrency")));
			excelPositionDatas.add(JBoltExcelPositionData.create(row, 17, excelRow.get("creferencepurpose")));
		}
		//2、创建JBoltExcel
		JBoltExcel jBoltExcel=JBoltExcel
				.createByTpl("purchasedexporttpl.xlsx")//创建JBoltExcel 从模板加载创建
				.addSheet(//设置sheet
						JBoltExcelSheet.create("Sheet1")//创建sheet name保持与模板中的sheet一致
						.setPositionDatas(excelPositionDatas)//设置定位数据
						)
				.setFileName("申购单明细");
		//3、导出
		renderBytesToExcelXlsxFile(jBoltExcel);
   }
   /**
    * 参照禀议-申购单明细导入
    * */
   @UnCheck
   public void importRefProposalPurchasedTplNotSave(@Para(value="ifirstsourceproposalid") Long ifirstsourceproposalid){
       String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
       UploadFile file = getFile("file", uploadPath);
       if (notExcel(file)) {
           renderJsonFail("请上传excel文件");
           return;
       }
       renderJson(service.importRefProposalPurchasedTplNotSave(file.getFile(),ifirstsourceproposalid));
   }
   /**
    * 参照预算-申购单明细导入
    * */
   @UnCheck
   public void importRefBudgetPurchasedTplNotSave(@Para(value="cdepcode") String cdepcode){
       String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
       UploadFile file = getFile("file", uploadPath);
       if (notExcel(file)) {
           renderJsonFail("请上传excel文件");
           return;
       }
       renderJson(service.importRefBudgetPurchasedTplNotSave(file.getFile(),cdepcode));
   }
   
   /**
    * 查看审批界面
    * */
   @UnCheck
   public void purchasemFormApprovalFlowIndex(){
   	Purchasem purchasem = service.findById(getLong("iautoid"));
   	set("purchasem", purchasem);
   	render("approve_process_index.html");
   }
   
   /**
    * 上传附件页面
    */
   @UnCheck
   public void attachment() {
       render("purchase_attachment_add.html");
   }
}
