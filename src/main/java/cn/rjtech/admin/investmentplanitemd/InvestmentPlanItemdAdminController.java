package cn.rjtech.admin.investmentplanitemd;

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
import cn.jbolt.core.poi.excel.JBoltExcelPositionData;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.DictionaryTypeKeyEnum;
import cn.rjtech.enums.InvestmentBudgetTypeEnum;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.model.momdata.InvestmentPlan;
import cn.rjtech.model.momdata.InvestmentPlanItemd;
import cn.rjtech.util.ReadInventmentExcelUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资计划项目明细 Controller
 * @ClassName: InvestmentPlanItemdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-18 09:38
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.STATISTIC_ANALYSIS_INVESTMENTPLANITEMD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/investmentplanitemd", viewPath = "/_view/admin/investmentplanitemd")
public class InvestmentPlanItemdAdminController extends BaseAdminController {

	@Inject
	private InvestmentPlanItemdService service;
	@Inject
	private PeriodService periodService;
	@Inject
	private DepartmentService departmentService;
	@Inject
	private InvestmentPlanService investmentPlanService;

	/**
	 * 首页
	 */
	public void index() {
		set("now", JBoltDateUtil.getYear());
		render("index.html");
	}

	/**
	 * 数据源
	 */
	@UnCheck
	@CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		InvestmentPlanItemd investmentPlanItemd = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(investmentPlanItemd, JBoltMsg.DATA_NOT_EXIST);

		set("investmentPlanItemd", investmentPlanItemd);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		renderJson(service.save(useIfValid(InvestmentPlanItemd.class, "investmentPlanItemd")));
	}

	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(useIfValid(InvestmentPlanItemd.class, "investmentPlanItemd")));
	}

	/**
	 * 批量删除
	 */
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

	/**
	 * 跳转投资计划模板下载界面
	 * */
	public void dowloadTplPage(){
		render("dowloadtpl_index.html");
	}


	/**
	 * 投资计划明细导出
	 */
	@UnCheck
	public  void dowloadTplPageSubmit(){
		Kv para = getKv();
		String cdepcode = para.getStr("cdepcode");//部门
		Integer budgetYear = para.getInt("budgetYear");//预算年度
		Integer iBudgetType = para.getInt("ibudgettype");//预算类型
		ValidationUtils.notBlank(cdepcode, "请选择U8部门!");
		ValidationUtils.notNull(budgetYear, "请选择预算年度!");
		ValidationUtils.notNull(iBudgetType, "请选择预算类型!");
		//根据选择的数据获取符合的数据导出
		List<Record> list = service.paginateAdminDatas(getPageNumber(), getPageSize(), Kv.by("ibudgetyear", budgetYear).set("ibudgettype", iBudgetType).set("cdepcode", cdepcode)).getList();
		List<JBoltExcelPositionData> excelPositionDatas=new ArrayList<JBoltExcelPositionData>();//定位数据集合


		excelPositionDatas.add(JBoltExcelPositionData.create(4, 3, budgetYear+"年"));
		excelPositionDatas.add(JBoltExcelPositionData.create(4, 5, InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText()));
		excelPositionDatas.add(JBoltExcelPositionData.create(4, 7, departmentService.getCdepName(cdepcode)));
		if(CollUtil.isNotEmpty(list)){
			int startRow = ReadInventmentExcelUtil.START_ROW+1;
			int startColumn = ReadInventmentExcelUtil.START_COLUMN;
			for (int i=0;i<list.size();i++) {
				Record row = list.get(i);
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn, i+1));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+1, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_TYPE.getValue(), row.getStr("iinvestmenttype"))));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+2, row.getStr("cprojectcode")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+3, row.getStr("cprojectname")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+4, row.getStr("cproductline")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+5, row.getStr("cmodelinvccode")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+6, row.getStr("cparts")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+7, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREER_TYPE.getValue(), row.getStr("icareertype"))));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+8, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_DISTINCTION.getValue(), row.getStr("iinvestmentdistinction"))));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+9, row.getStr("cplanno")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+10, row.getStr("citemname")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+11, IsEnableEnum.toEnum(row.getInt("isimport")).getText()));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+12, row.getStr("iquantity")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+13, row.getStr("cunit")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+14, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CASSETTYPE.getValue(), row.getStr("cassettype"))));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+15, row.getStr("cpurpose")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+16, row.getStr("ceffectamount")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+17, row.getStr("creclaimyear")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+18, row.getStr("clevel")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+19, IsEnableEnum.toEnum(row.getInt("ispriorreport")).getText()));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+20, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.PAYMENT_PROGRESS.getValue(), row.getStr("cpaymentprogress"))));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+21, row.getBigDecimal("itaxrate")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+22, row.getBigDecimal("itotalamountplan")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+23, row.getBigDecimal("itotalamountactual")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+24, row.getBigDecimal("itotalamountdiff")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+25, row.getStr("itotalamountdiffreason")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+26, row.getBigDecimal("iyeartotalamountplan")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+27, row.getBigDecimal("iyeartotalamountactual")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+28, row.getBigDecimal("iyeartotalamountdiff")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+29, row.getStr("iyeartotalamountdiffreason")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+30, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.EDITTYPE.getValue(), row.getStr("cedittype"))));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+31, row.getStr("cmemo")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+32, row.getInt("iitemyear")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+33, row.getStr("cperiodprogress1")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+34, row.getStr("dperioddate1")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+35, row.getBigDecimal("iamount1")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+36, row.getStr("cperiodprogress2")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+37, row.getStr("dperioddate2")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+38, row.getBigDecimal("iamount2")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+39, row.getStr("cperiodprogress3")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+40, row.getStr("dperioddate3")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+41, row.getBigDecimal("iamount3")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+42, row.getStr("cperiodprogress4")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+43, row.getStr("dperioddate4")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+44, row.getBigDecimal("iamount4")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+45, row.getStr("cperiodprogress5")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+46, row.getStr("dperioddate5")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+47, row.getBigDecimal("iamount5")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+48, row.getStr("cperiodprogress6")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+49, row.getStr("dperioddate6")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+50, row.getBigDecimal("iamount6")));
				excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+51,JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.AUDIT_STATUS.getValue(), row.getStr("iauditstatus"))));
			}
		}
		//2、创建JBoltExcel
		JBoltExcel jBoltExcel=JBoltExcel
				.createByTpl("investmentplanitemdTpl.xlsx")//创建JBoltExcel 从模板加载创建
				.addSheet(//设置sheet
						JBoltExcelSheet.create("投资计划明细表")//创建sheet name保持与模板中的sheet一致
								.setPositionDatas(excelPositionDatas)//设置定位数据
				)
				.setFileName("投资计划明细导出");
		//3、导出
		renderBytesToExcelXlsxFile(jBoltExcel);
	}
	/**
	 * 跳转投资计划模板打印
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
		for (Record record : list) {
			//类型转换
			record.set("iinvestmenttype",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_TYPE.getValue(), record.getStr("iinvestmenttype")));
			record.set("icareertype",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), record.getStr("icareertype")));
			record.set("iinvestmentdistinction",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_DISTINCTION.getValue(), record.getStr("iinvestmentdistinction")));
			record.set("isimport",IsEnableEnum.toEnum(record.getInt("isimport")).getText());
			record.set("cassettype",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CASSETTYPE.getValue(), record.getStr("cassettype")));
			record.set("ispriorreport",IsEnableEnum.toEnum(record.getInt("ispriorreport")));
			record.set("cpaymentprogress",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.PAYMENT_PROGRESS.getValue(), record.getStr("cpaymentprogress")));
			record.set("cedittype",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.EDITTYPE.getValue(), record.getStr("cedittype")));
		}
		set("cdepname",departmentService.getCdepName(cdepcode));
		set("iBudgetType",InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText());
		set("now", JBoltDateUtil.getYear());
		set("budgetYear",budgetYear);
		set("list", JsonKit.toJson(list));
		render("print.html");
	}
	
    /**
     * 投资计划明细报表导出
     * */
    @UnCheck
    public void exportInvestmentPlanItemdReportDatas(){
    	Kv para = getKv();
    	String cdepcode = para.getStr("cdepcode");
    	Integer ibudgetyear = para.getInt("ibudgetyear");
    	Integer ibudgettype = para.getInt("ibudgettype");
    	String paraNullMsg = "请选择部门,预算年份,预算类型导出";
    	ValidationUtils.notBlank(cdepcode, paraNullMsg);
    	ValidationUtils.notNull(ibudgetyear, paraNullMsg);
    	ValidationUtils.notNull(ibudgettype, paraNullMsg);
    	InvestmentPlan investmentPlan = investmentPlanService.findModelByYearAndType(ibudgetyear, ibudgettype, cdepcode);
    	ValidationUtils.notNull(investmentPlan, "投资计划为空,导出失败!");
    	List<Record> itemList = investmentPlanService.findInvestmentPlanItemDatas(investmentPlan.getIAutoId());
    	List<JBoltExcelPositionData> excelPositionDatas=new ArrayList<JBoltExcelPositionData>();//定位数据集合
    	investmentPlanService.contrustExportExcelPositionDatas(excelPositionDatas,investmentPlan,itemList);
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
}
