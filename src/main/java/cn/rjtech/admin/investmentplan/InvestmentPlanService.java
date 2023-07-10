package cn.rjtech.admin.investmentplan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.DataPermissionKit;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.kit.U8DataSourceKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcelPositionData;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltArrayUtil;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodeencodingm.BarcodeencodingmService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.admin.investmentplanitemd.InvestmentPlanItemdService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.admin.projectcard.ProjectCardService;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.InvestmentPlan;
import cn.rjtech.model.momdata.InvestmentPlanItem;
import cn.rjtech.model.momdata.InvestmentPlanItemd;
import cn.rjtech.model.momdata.Period;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ReadInventmentExcelUtil;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 投资计划主表 Service
 * @ClassName: InvestmentPlanService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-18 09:33
 */
public class InvestmentPlanService extends BaseService<InvestmentPlan> implements IApprovalService{

	private final InvestmentPlan dao = new InvestmentPlan().dao();

	@Inject
	private DepartmentService departmentService;
	@Inject
	private DictionaryService dictionaryService;
	@Inject
	private InvestmentPlanItemService investmentPlanItemService;
	@Inject
	private InvestmentPlanItemdService investmentPlanItemdService;
	@Inject
	private PeriodService periodService;
	@Inject
	private ExpenseBudgetItemService expenseBudgetItemService;
	@Inject
	private ProjectCardService projectCardService;
	@Inject
	private ExpenseBudgetService expenseBudgetService;
	@Inject
	private BarcodeencodingmService barcodeencodingmService;
	@Inject
    private FormApprovalService formApprovalService;
	@Override
	protected InvestmentPlan dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param para
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		User user = JBoltUserKit.getUser();
        // 系统管理员 || 存在权限部门
        para.set("isSystemAdmin", user.getIsSystemAdmin())
        	.set("iorgid",getOrgId());
		Page<Record> page = dbTemplate("investmentplan.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : page.getList()) {
			row.set("cusername", JBoltUserCache.me.getName(row.getLong("icreateby")));
			row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
		}
		return page;
	}

	/**
	 * 保存
	 * @param investmentPlan
	 * @return
	 */
	public Ret save(InvestmentPlan investmentPlan) {
		if(investmentPlan==null || isOk(investmentPlan.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(investmentPlan.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(investmentPlan.getIAutoId(), JBoltUserKit.getUserId(), investmentPlan.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(InvestmentPlan investmentPlan) {
		if(investmentPlan==null || notOk(investmentPlan.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			InvestmentPlan dbInvestmentPlan = findById(investmentPlan.getIAutoId());
			ValidationUtils.notNull(dbInvestmentPlan, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(investmentPlan.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(investmentPlan.getIAutoId(), JBoltUserKit.getUserId(), investmentPlan.getName());
		return SUCCESS;
	}
	public void deleteInvestmentPlanItem(Long iplanid){
		List<InvestmentPlanItem> investmentPlanItemList = investmentPlanItemService.findModelByPlanId(iplanid);
		if(CollUtil.isEmpty(investmentPlanItemList)) return;
		for (InvestmentPlanItem investmentPlanItem : investmentPlanItemList) {
			List<InvestmentPlanItemd> investmentPlanItemdList = investmentPlanItemdService.findByInvestmentItemId(investmentPlanItem.getIautoid());
			for (InvestmentPlanItemd investmentPlanItemd : investmentPlanItemdList) {
				ValidationUtils.isTrue(investmentPlanItemd.delete(), ErrorMsg.DELETE_FAILED);
			}
			ValidationUtils.isTrue(investmentPlanItem.delete(), ErrorMsg.DELETE_FAILED);
		}
	}
	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				InvestmentPlan dbInvestmentPlan = findById(iAutoId);
				DataPermissionKit.validateAccess(dbInvestmentPlan.getCDepCode());
				ValidationUtils.notNull(dbInvestmentPlan, JBoltMsg.DATA_NOT_EXIST);
				deleteInvestmentPlanItem(iAutoId);
				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用
				ValidationUtils.isTrue(dbInvestmentPlan.delete(), ErrorMsg.DELETE_FAILED);
			}
			return true;
		});

		// 添加日志
		// InvestmentPlan investmentPlan = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, investmentPlan.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param investmentPlan 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(InvestmentPlan investmentPlan, Kv kv) {
		//addDeleteSystemLog(investmentPlan.getIAutoId(), JBoltUserKit.getUserId(),investmentPlan.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param investmentPlan 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(InvestmentPlan investmentPlan, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(investmentPlan, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}
	
    public void contrustExportExcelPositionDatas(List<JBoltExcelPositionData> excelPositionDatas,InvestmentPlan investmentPlan,
			List<Record> itemList) {
    	excelPositionDatas.add(JBoltExcelPositionData.create(4, 3, investmentPlan.getIBudgetYear()+"年"));
		excelPositionDatas.add(JBoltExcelPositionData.create(4, 5, InvestmentBudgetTypeEnum.toEnum(investmentPlan.getIBudgetType()).getText()));
		excelPositionDatas.add(JBoltExcelPositionData.create(4, 7, departmentService.getCdepName(investmentPlan.getCDepCode())));
    	if(CollUtil.isNotEmpty(itemList)){
    		int startRow = ReadInventmentExcelUtil.START_ROW+1;
    		int startColumn = ReadInventmentExcelUtil.START_COLUMN;
    		for (int i=0;i<itemList.size();i++) {
    			Record row = itemList.get(i);
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn, i+1));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+1, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_TYPE.getValue(), row.getStr("iinvestmenttype"))));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+2, row.getStr("cproductline")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+3, row.getStr("cmodelinvccode")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+4, row.getStr("cparts")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+5, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREER_TYPE.getValue(), row.getStr("icareertype"))));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+6, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_DISTINCTION.getValue(), row.getStr("iinvestmentdistinction"))));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+7, row.getStr("cplanno")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+8, row.getStr("citemname")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+9, row.getInt("isimport") == null?null:IsEnableEnum.toEnum(row.getInt("isimport")).getText()));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+10, row.getStr("iquantity")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+11, row.getStr("cunit")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+12, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CASSETTYPE.getValue(), row.getStr("cassettype"))));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+13, row.getStr("cpurpose")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+14, row.getStr("ceffectamount")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+15, row.getStr("creclaimyear")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+16, row.getStr("clevel")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+17, row.getInt("ispriorreport") == null?null:IsEnableEnum.toEnum(row.getInt("ispriorreport")).getText()));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+18, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.PAYMENT_PROGRESS.getValue(), row.getStr("cpaymentprogress"))));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+19, row.getBigDecimal("itaxrate")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+20, row.getBigDecimal("itotalamountplan")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+21, row.getBigDecimal("itotalamountactual")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+22, row.getBigDecimal("itotalamountdiff")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+23, row.getStr("itotalamountdiffreason")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+24, row.getBigDecimal("iyeartotalamountplan")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+25, row.getBigDecimal("iyeartotalamountactual")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+26, row.getBigDecimal("iyeartotalamountdiff")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+27, row.getStr("iyeartotalamountdiffreason")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+28, JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.EDITTYPE.getValue(), row.getStr("cedittype"))));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+29, row.getStr("cmemo")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+30, row.getInt("iitemyear")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+31, row.getStr("cperiodprogress1")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+32, row.getStr("dperioddate1")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+33, row.getBigDecimal("iamount1")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+34, row.getStr("cperiodprogress2")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+35, row.getStr("dperioddate2")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+36, row.getBigDecimal("iamount2")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+37, row.getStr("cperiodprogress3")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+38, row.getStr("dperioddate3")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+39, row.getBigDecimal("iamount3")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+40, row.getStr("cperiodprogress4")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+41, row.getStr("dperioddate4")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+42, row.getBigDecimal("iamount4")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+43, row.getStr("cperiodprogress5")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+44, row.getStr("dperioddate5")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+45, row.getBigDecimal("iamount5")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+46, row.getStr("cperiodprogress6")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+47, row.getStr("dperioddate6")));
    			excelPositionDatas.add(JBoltExcelPositionData.create(startRow+i, startColumn+48, row.getBigDecimal("iamount6")));
			}
    	}
	}
	
	@SuppressWarnings("unchecked")
	public Ret importTableInvestmentPlanTpl(String filePath,Kv para) throws Exception  {
		Long iplanid = para.getLong("iplanid");
		String cdepcodePara = para.getStr("investmentPlan.cdepcode");
		// 读取excel中数据
		int startRow = ReadInventmentExcelUtil.START_ROW + 1;
        HashMap<String, Object> excelMap = ReadInventmentExcelUtil.readExcelInfo(filePath);
        String ibudgetyear = (String)excelMap.get("ibudgetyear");
        String ibudgettype = (String)excelMap.get("ibudgettype");
        String cdepcode = "";
        String cdepname = (String)excelMap.get("cdepcode");
        ValidationUtils.notNull(ibudgetyear, "预算年份不能为空");
        ValidationUtils.notNull(ibudgettype, "预算类型不能为空");
        ValidationUtils.notNull(cdepname, "部门不能为空");
        Integer ibudgetTypeDb = null;
        Integer ibudgetYearDb = null;
        try {
        	ibudgetYearDb = Integer.parseInt(ibudgetyear.replace("年", ""));
		} catch (Exception e) {
			ValidationUtils.error( "预算年度不合法,请检查导入模板");
		}
        try {
	    	for (InvestmentBudgetTypeEnum typeenum : InvestmentBudgetTypeEnum.values()) {
				if(typeenum.getText().equals(ibudgettype)){
					ibudgetTypeDb = typeenum.getValue();
					break;
				}
			}
	    	ValidationUtils.notNull(ibudgetTypeDb, "预算类型不合法,请检查导入模板!");
		} catch (Exception e) {
			ValidationUtils.error( "预算类型不合法,请检查导入模板!");
		}
        try {
	    	List<Department> list = departmentService.treeDatasForProposalSystem(Kv.by("isProposal", true).set("depName",cdepname));
	    	cdepcode = list.get(0).getCDepCode();
		} catch (Exception e) {
			ValidationUtils.error( "预算部门获取失败,请检查导入模板!");
		}
        ValidationUtils.equals(cdepcodePara, cdepcode, ErrorMsg.BUDGET_IMPORT_CDEPCODE_NOT_EQUIL);
        DataPermissionKit.validateAccess(cdepcode);
        List<Record> excelRowList = (List<Record>)excelMap.get("rows");
        StringBuilder errorMsg = new StringBuilder();
        if(CollUtil.isNotEmpty(excelRowList)){
        	for (Record record : excelRowList) {
        		constructJBoltTableInvestmentPlanItem(record,errorMsg,startRow);
        		startRow++;
			}
        }
        if(iplanid != null){
        	InvestmentPlan investmentPlan = new InvestmentPlan();
        	investmentPlan.setIAutoId(iplanid);
        	investmentPlan.setCDepCode(cdepcode);
        	investmentPlan.setIBudgetType(ibudgetTypeDb);
        	investmentPlan.setIBudgetYear(ibudgetYearDb);
        	tx(()->{
        		deleteInvestmentPlanItem(iplanid);
        		saveInvestmentPlanItemForSubmitTable(excelRowList,investmentPlan,JBoltUserKit.getUserId(),new Date());
        		return true;
        	});
        }
		return SUCCESS.set("list", excelRowList);
	}
	/**
	 * 投资计划编辑excel导入到可编辑表格通过读取excel内容构造可编辑表格内容
	 * */
	public void constructJBoltTableInvestmentPlanItem(Record excelRow,StringBuilder errorMsg,int dataStartRow){
		excelRow.set("iinvestmenttype",constructDictionarySnByName(DictionaryTypeKeyEnum.INVESTMENT_TYPE.getValue(),
				excelRow.getStr(InvestmentEnum.IINVESTMENTTYPE.getField()),InvestmentEnum.IINVESTMENTTYPE.getText(),errorMsg,dataStartRow));
		excelRow.set("icareertype",constructDictionarySnByName(DictionaryTypeKeyEnum.CAREER_TYPE.getValue(),
				excelRow.getStr(InvestmentEnum.ICAREERTYPE.getField()),InvestmentEnum.ICAREERTYPE.getText(),errorMsg,dataStartRow));
		excelRow.set("iinvestmentdistinction",constructDictionarySnByName(DictionaryTypeKeyEnum.INVESTMENT_DISTINCTION.getValue(),
				excelRow.getStr(InvestmentEnum.IINVESTMENTDISTINCTION.getField()),InvestmentEnum.IINVESTMENTDISTINCTION.getText(),errorMsg,dataStartRow));
		String isImport = excelRow.getStr(InvestmentEnum.ISIMPORT.getField());
		excelRow.set("isimport",JBoltStringUtil.isBlank(isImport) ? null: IsEnableEnum.toText(isImport).getValue()+"");
		String citemname = excelRow.getStr(InvestmentEnum.CITEMNAME.getField());
		if(JBoltStringUtil.isBlank(citemname)) errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.CITEMNAME.getText()+"不能为空<br/>");
		try {
			excelRow.getInt(InvestmentEnum.IQUANTITY.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IQUANTITY.getText()+"不合法<br/>");
		}
		excelRow.set("cassettype",constructDictionarySnByName(DictionaryTypeKeyEnum.CASSETTYPE.getValue(),
				excelRow.getStr(InvestmentEnum.CASSETTYPE.getField()),InvestmentEnum.CASSETTYPE.getText(),errorMsg,dataStartRow));
		String isperiorReport = excelRow.getStr(InvestmentEnum.ISPRIORREPORT.getField());
		excelRow.set("ispriorreport",JBoltStringUtil.isBlank(isperiorReport) ? null : IsEnableEnum.toText(isperiorReport).getValue()+"");
		excelRow.set("cpaymentprogress",constructDictionarySnByName(DictionaryTypeKeyEnum.PAYMENT_PROGRESS.getValue(),
				excelRow.getStr(InvestmentEnum.CPAYMENTPROGRESS.getField()),InvestmentEnum.CPAYMENTPROGRESS.getText(),errorMsg,dataStartRow));
		excelRow.set("cedittypedesc",constructDictionarySnByName(DictionaryTypeKeyEnum.EDITTYPE.getValue(),
				excelRow.getStr(InvestmentEnum.CEDITTYPE.getField()),InvestmentEnum.CEDITTYPE.getText(),errorMsg,dataStartRow));
		try {
			excelRow.getInt(InvestmentEnum.IITEMYEAR.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IITEMYEAR.getText()+"不合法<br/>");
		}
		//税率:除以100存到数据库
		try {
			excelRow.getBigDecimal(InvestmentEnum.ITAXRATE.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.ITAXRATE.getText()+"不合法<br/>");
		}
		constructJBoltTableInvestmentPlanitemd(excelRow,errorMsg,dataStartRow);
	}
	/**
	 * 投资计划编辑excel导入到可编辑表格通过读取excel内容构造可编辑表格的期数内容
	 * */
	private void constructJBoltTableInvestmentPlanitemd(Record excelRow, StringBuilder errorMsg,int dataStartRow){
		//第一期
		try {
			excelRow.set("dperioddate1",getDate(excelRow.getStr(InvestmentEnum.DPERIODDATE1.getField()),"yyyy-MM"));
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.DPERIODDATE1.getText()+"格式不正确,请参考格式：2022-10<br/>");
		}
		try {
			excelRow.getBigDecimal(InvestmentEnum.IAMOUNT1.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IAMOUNT1.getText()+"不合法<br/>");
		}
		excelRow.set("cperiodprogress1",excelRow.getStr(InvestmentEnum.CPERIODPROGRESS1.getField()));
		//第二期
		try {
			excelRow.set("dperioddate2",getDate(excelRow.getStr(InvestmentEnum.DPERIODDATE2.getField()),"yyyy-MM"));
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.DPERIODDATE2.getText()+"格式不正确,请参考格式：2022-10<br/>");
		}
		try {
			excelRow.getBigDecimal(InvestmentEnum.IAMOUNT2.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IAMOUNT2.getText()+"不合法<br/>");
		}
		excelRow.set("cperiodprogress2",excelRow.getStr(InvestmentEnum.CPERIODPROGRESS2.getField()));
		//第三期
		try {
			excelRow.set("dperioddate3", getDate(excelRow.getStr(InvestmentEnum.DPERIODDATE3.getField()),"yyyy-MM"));
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.DPERIODDATE3.getText()+"格式不正确,请参考格式：2022-10<br/>");
		}
		try {
			excelRow.getBigDecimal(InvestmentEnum.IAMOUNT3.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IAMOUNT3.getText()+"不合法<br/>");
		}
		excelRow.set("cperiodprogress3",excelRow.getStr(InvestmentEnum.CPERIODPROGRESS3.getField()));
		//第四期
		try {
			excelRow.set("dperioddate4", getDate(excelRow.getStr(InvestmentEnum.DPERIODDATE4.getField()),"yyyy-MM"));
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.DPERIODDATE4.getText()+"格式不正确,请参考格式：2022-10<br/>");
		}
		try {
			excelRow.getBigDecimal(InvestmentEnum.IAMOUNT4.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IAMOUNT4.getText()+"不合法<br/>");
		}
		excelRow.set("cperiodprogress4",excelRow.getStr(InvestmentEnum.CPERIODPROGRESS4.getField()));
		//第五期
		try {
			excelRow.set("dperioddate5", getDate(excelRow.getStr(InvestmentEnum.DPERIODDATE5.getField()),"yyyy-MM"));
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.DPERIODDATE5.getText()+"格式不正确,请参考格式：2022-10<br/>");
		}
		try {
			excelRow.getBigDecimal(InvestmentEnum.IAMOUNT5.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IAMOUNT5.getText()+"不合法<br/>");
		}
		excelRow.set("cperiodprogress5",excelRow.getStr(InvestmentEnum.CPERIODPROGRESS5.getField()));
		//第六期
		try {
			excelRow.set("dperioddate6", getDate(excelRow.getStr(InvestmentEnum.DPERIODDATE6.getField()),"yyyy-MM"));
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.DPERIODDATE6.getText()+"格式不正确,请参考格式：2022-10<br/>");
		}
		try {
			excelRow.getBigDecimal(InvestmentEnum.IAMOUNT6.getField());
		} catch (Exception e) {
			errorMsg.append("第"+dataStartRow+"行,"+InvestmentEnum.IAMOUNT6.getText()+"不合法<br/>");
		}
		excelRow.set("cperiodprogress6",excelRow.getStr(InvestmentEnum.CPERIODPROGRESS6.getField()));
	}
	
	
	
	

	/**
	 * 根据数据字典将导入excel中的name转sn：
	 * */
	private String constructDictionarySnByName(String typeKey,String name,String fieldText,StringBuilder errorMsg,int dataStartRow){
		if(JBoltStringUtil.isBlank(name)) return null;
		Record record  = dictionaryService.convertEnumByTypeKey(typeKey);
		String sn = record.getStr(name);
		if(JBoltStringUtil.isBlank(sn))
			errorMsg.append("第"+dataStartRow+"行,"+fieldText+"未维护数据字典<br/>");
		return sn;
	}
	/**
     * 将字符串转为时间(向上抛出异常)
     * @param date
     * @param format
     * @return
	 * @throws ParseException
     */
    public Date getDate(String date,String format) throws ParseException{
    	if(JBoltStringUtil.isBlank(date)) {
    		return null;
    	}
    	 if(date.contains("T")){
         	date=date.replaceAll("T", " ");
         }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

	public boolean isExistsInvestmentPlan(Kv para){
		InvestmentPlan investmentPlan = findFirst(selectSql().eq("iorgid", getOrgId()).eq("corgcode", getOrgCode()).
				eq("ibudgetyear", para.getInt("ibudgetyear")).eq("ibudgettype", para.getInt("ibudgettype"))
				.eq("cdepcode", para.getStr("cdepcode"))
				);
		return investmentPlan != null;
	}
	/**
	 * 系统根据编码规则配置获取投资NO.:
	 * 		投资NO规则：科室-投资类型-年份-流水号，投资NO：QH-I1-2022-001，QH：科室缩写，可以使用U8部门英文名称，
	 * 			I1：新机种投资，I2：一般投资，2022：年份，001：3位流水码
	 * */
	public String getCplanno(Kv para){
		return barcodeencodingmService.genCode(para, ItemEnum.INVESTMENT_PLAN.getValue());
	}	
	/**
     * 投资计划新增表格提交
     */
	public Ret saveTableSubmitByAdd(JBoltTable jBoltTable) {
		ValidationUtils.notNull(jBoltTable, "参数不能为空");
		Date now = new Date();
		InvestmentPlan investmentPlan = jBoltTable.getFormModel(InvestmentPlan.class, "investmentPlan");
		DataPermissionKit.validateAccess(investmentPlan.getCDepCode());
		//从启用期间中获取预算类型和预算年度
		Integer iBudgetType = investmentPlan.getIBudgetType();
		Integer iBudgetYear = investmentPlan.getIBudgetYear();
		String cdepcode = investmentPlan.getCDepCode();
		InvestmentPlan investmentPlanDbs = findModelByYearAndType(iBudgetYear, iBudgetType,cdepcode);
		ValidationUtils.isTrue(investmentPlanDbs == null, "投资计划已创建，请重复操作!");
		tx(()->{
			Long userId = JBoltUserKit.getUserId();
			investmentPlan.setIOrgId(getOrgId());
			investmentPlan.setCOrgCode(getOrgCode());
			investmentPlan.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
			investmentPlan.setIEffectiveStatus(EffectiveStatusEnum.INVAILD.getValue());
			investmentPlan.setICreateBy(userId);
			investmentPlan.setDCreateTime(now);
			ValidationUtils.isTrue(investmentPlan.save(), ErrorMsg.SAVE_FAILED);
			List<Record> listRecord = jBoltTable.getSaveRecordList();
			saveInvestmentPlanItemForSubmitTable(listRecord,investmentPlan,userId,now);
			return true;
		});
		return successWithData(investmentPlan.keep("iautoid"));
	}
	/**
	 * 投资计划新增时，构建每行中的投资计划项目明细数据
	 * */
	private void constructInvestmentPlanItemd(Record row, InvestmentPlan investmentPlan,Long iplanitemid,
			List<InvestmentPlanItemd> investmentPlanItemdList, Date now) {
		//第一期
		String cperiodprogress1 = row.getStr("cperiodprogress1");
		Date dperioddate1 = row.getDate(InvestmentEnum.DPERIODDATE1.getField());
		BigDecimal iamount1 = row.getBigDecimal(InvestmentEnum.IAMOUNT1.getField());
		if(JBoltStringUtil.isNotBlank(cperiodprogress1) && dperioddate1 != null && iamount1 !=null){
			InvestmentPlanItemd investmentPlanItemd = new InvestmentPlanItemd();
			investmentPlanItemd.setIAutoId(JBoltSnowflakeKit.me.nextId());
			investmentPlanItemd.setIPlanItemId(iplanitemid);
			investmentPlanItemd.setIPeriodNum(1);
			investmentPlanItemd.setCPeriodProgress(cperiodprogress1);
			investmentPlanItemd.setDPeriodDate(dperioddate1);
			investmentPlanItemd.setIAmount(iamount1.multiply(Constants.RATIO));
			investmentPlanItemd.setICreateBy(JBoltUserKit.getUserId());
			investmentPlanItemd.setDCreateTime(now);
			investmentPlanItemdList.add(investmentPlanItemd);
		}
		//第二期
		String cperiodprogress2 = row.getStr("cperiodprogress2");
		Date dperioddate2 = row.getDate(InvestmentEnum.DPERIODDATE2.getField());
		BigDecimal iamount2 = row.getBigDecimal(InvestmentEnum.IAMOUNT2.getField());
		if(JBoltStringUtil.isNotBlank(cperiodprogress2) && dperioddate2 != null && iamount2 !=null){
			InvestmentPlanItemd investmentPlanItemd = new InvestmentPlanItemd();
			investmentPlanItemd.setIAutoId(JBoltSnowflakeKit.me.nextId());
			investmentPlanItemd.setIPlanItemId(iplanitemid);
			investmentPlanItemd.setIPeriodNum(2);
			investmentPlanItemd.setCPeriodProgress(cperiodprogress2);
			investmentPlanItemd.setDPeriodDate(dperioddate2);
			investmentPlanItemd.setIAmount(iamount2.multiply(Constants.RATIO));
			investmentPlanItemd.setICreateBy(JBoltUserKit.getUserId());
			investmentPlanItemd.setDCreateTime(now);
			investmentPlanItemdList.add(investmentPlanItemd);
		}
		//第三期
		String cperiodprogress3 = row.getStr("cperiodprogress3");
		Date dperioddate3 = row.getDate(InvestmentEnum.DPERIODDATE3.getField());
		BigDecimal iamount3 = row.getBigDecimal(InvestmentEnum.IAMOUNT3.getField());
		if(JBoltStringUtil.isNotBlank(cperiodprogress3) && dperioddate3 != null && iamount3 !=null){
			InvestmentPlanItemd investmentPlanItemd = new InvestmentPlanItemd();
			investmentPlanItemd.setIAutoId(JBoltSnowflakeKit.me.nextId());
			investmentPlanItemd.setIPlanItemId(iplanitemid);
			investmentPlanItemd.setIPeriodNum(3);
			investmentPlanItemd.setCPeriodProgress(cperiodprogress3);
			investmentPlanItemd.setDPeriodDate(dperioddate3);
			investmentPlanItemd.setIAmount(iamount3.multiply(Constants.RATIO));
			investmentPlanItemd.setICreateBy(JBoltUserKit.getUserId());
			investmentPlanItemd.setDCreateTime(now);
			investmentPlanItemdList.add(investmentPlanItemd);
		}
		//第四期
		String cperiodprogress4 = row.getStr("cperiodprogress4");
		Date dperioddate4 = row.getDate(InvestmentEnum.DPERIODDATE4.getField());
		BigDecimal iamount4 = row.getBigDecimal(InvestmentEnum.IAMOUNT4.getField());
		if(JBoltStringUtil.isNotBlank(cperiodprogress4) && dperioddate4 != null && iamount4 !=null){
			InvestmentPlanItemd investmentPlanItemd = new InvestmentPlanItemd();
			investmentPlanItemd.setIAutoId(JBoltSnowflakeKit.me.nextId());
			investmentPlanItemd.setIPlanItemId(iplanitemid);
			investmentPlanItemd.setIPeriodNum(4);
			investmentPlanItemd.setCPeriodProgress(cperiodprogress4);
			investmentPlanItemd.setDPeriodDate(dperioddate4);
			investmentPlanItemd.setIAmount(iamount4.multiply(Constants.RATIO));
			investmentPlanItemd.setICreateBy(JBoltUserKit.getUserId());
			investmentPlanItemd.setDCreateTime(now);
			investmentPlanItemdList.add(investmentPlanItemd);
		}
		//第五期
		String cperiodprogress5 = row.getStr("cperiodprogress5");
		Date dperioddate5 = row.getDate(InvestmentEnum.DPERIODDATE5.getField());
		BigDecimal iamount5 = row.getBigDecimal(InvestmentEnum.IAMOUNT5.getField());
		if(JBoltStringUtil.isNotBlank(cperiodprogress5) && dperioddate5 != null && iamount5 !=null){
			InvestmentPlanItemd investmentPlanItemd = new InvestmentPlanItemd();
			investmentPlanItemd.setIAutoId(JBoltSnowflakeKit.me.nextId());
			investmentPlanItemd.setIPlanItemId(iplanitemid);
			investmentPlanItemd.setIPeriodNum(5);
			investmentPlanItemd.setCPeriodProgress(cperiodprogress5);
			investmentPlanItemd.setDPeriodDate(dperioddate5);
			investmentPlanItemd.setIAmount(iamount5.multiply(Constants.RATIO));
			investmentPlanItemd.setICreateBy(JBoltUserKit.getUserId());
			investmentPlanItemd.setDCreateTime(now);
			investmentPlanItemdList.add(investmentPlanItemd);
		}
		//第六期
		String cperiodprogress6 = row.getStr("cperiodprogress6");
		Date dperioddate6 = row.getDate(InvestmentEnum.DPERIODDATE6.getField());
		BigDecimal iamount6 = row.getBigDecimal(InvestmentEnum.IAMOUNT6.getField());
		if(JBoltStringUtil.isNotBlank(cperiodprogress6) && dperioddate6 != null && iamount6 !=null){
			InvestmentPlanItemd investmentPlanItemd = new InvestmentPlanItemd();
			investmentPlanItemd.setIAutoId(JBoltSnowflakeKit.me.nextId());
			investmentPlanItemd.setIPlanItemId(iplanitemid);
			investmentPlanItemd.setIPeriodNum(6);
			investmentPlanItemd.setCPeriodProgress(cperiodprogress6);
			investmentPlanItemd.setDPeriodDate(dperioddate6);
			investmentPlanItemd.setIAmount(iamount6.multiply(Constants.RATIO));
			investmentPlanItemd.setICreateBy(JBoltUserKit.getUserId());
			investmentPlanItemd.setDCreateTime(now);
			investmentPlanItemdList.add(investmentPlanItemd);
		}
	}
	/**
	 * 计算费用预算项目明细金额合计
	 * */
	public BigDecimal calculateTotalAmount(List<InvestmentPlanItemd> investmentPlanItemdList){
		if(CollUtil.isEmpty(investmentPlanItemdList)) return BigDecimal.ZERO;
		BigDecimal rsBigDecial = BigDecimal.ZERO;
		for (InvestmentPlanItemd investmentPlanItemd : investmentPlanItemdList) {
			BigDecimal iamount = investmentPlanItemd.getIAmount();
			if(iamount == null) continue;
			rsBigDecial = rsBigDecial.add(iamount);
		}
		return rsBigDecial;
	}
	/**
	 * 计算某年份费用预算项目明细金额合计
	 * */
	public BigDecimal calculateTotalAmount(List<InvestmentPlanItemd> investmentPlanItemdList,Integer iBudgetYear){
		if(CollUtil.isEmpty(investmentPlanItemdList)) return BigDecimal.ZERO;
		BigDecimal rsBigDecial = BigDecimal.ZERO;
		for (InvestmentPlanItemd investmentPlanItemd : investmentPlanItemdList) {
			Date dPeriodDate = investmentPlanItemd.getDPeriodDate();
			BigDecimal iamount = investmentPlanItemd.getIAmount();
			if(dPeriodDate == null || iamount == null) continue;
			if(iBudgetYear == JBoltDateUtil.getCalendarByDate(dPeriodDate).get(Calendar.YEAR))
				rsBigDecial = rsBigDecial.add(iamount);
		}
		return rsBigDecial;
	}
	/**
	 * 保存费用预算项目和项目明细
	 * 	使用场景：
	 * 		1.费用预算编制-新增，修改的保存按钮 
	 * 		2.费用预算编制-新增，修改的导入按钮
	 * */
	public void saveInvestmentPlanItemForSubmitTable(List<Record> listRecord,InvestmentPlan investmentPlan,Long userId,Date now){
		if(CollUtil.isEmpty(listRecord)) return;
		for (Record row: listRecord) {
			Long iInvestmentPlanItemId = JBoltSnowflakeKit.me.nextId();
			List<InvestmentPlanItemd> investmentPlanItemdList = new ArrayList<InvestmentPlanItemd>();
			//根据表格数据构造项目明细
			constructInvestmentPlanItemd(row,investmentPlan,iInvestmentPlanItemId,investmentPlanItemdList,now);
			//导入的未完成项目或者导入excel保存的情况存在已有计划号的预算项目，新增行的方式可能没有计划号
			String cplanno = row.get("cplanno");
			BigDecimal iTotalAmountPlan = BigDecimal.ZERO; //项目总金额-计划金额
			BigDecimal iTotalAmountActual = BigDecimal.ZERO;//项目总金额-实绩/预测/修订金额
			BigDecimal iYearTotalAmountPlan = BigDecimal.ZERO;//预算年份的项目总金额-计划金额
			BigDecimal iYearTotalAmountActual = BigDecimal.ZERO;//预算年份的项目总金额-实绩/预测/修订金额
			/**
			 * 新增项目的计划号为空:
			 * 		项目总金额-计划金额和预算年份的项目总金额-计划金额为1-6期的金额合计
			 * 		项目总金额-实绩/预测/修订金额和预算年份的项目总金额-实绩/预测/修订金额为0
			 *	不为空：
			 *		项目总金额-计划金额和预算年份的项目总金额-计划金额为可编辑表格中对应金额 (取上期的金额)
			 * 		项目总金额-实绩/预测/修订和预算年份的项目总金额-实绩/预测/修订金额金额为1-6期的金额合计
			 * */
			BigDecimal iamountTotal = calculateTotalAmount(investmentPlanItemdList);
			BigDecimal iamountTotalYear = calculateTotalAmount(investmentPlanItemdList,investmentPlan.getIBudgetYear());
			if(JBoltStringUtil.isBlank(cplanno)){
				iTotalAmountPlan = iamountTotal;
				iYearTotalAmountPlan = iamountTotalYear;
			}else{
				iTotalAmountPlan = row.getBigDecimal("itotalamountplan").multiply(Constants.RATIO);
				iYearTotalAmountPlan = row.getBigDecimal("iyeartotalamountplan").multiply(Constants.RATIO);
				iTotalAmountActual = iamountTotal;
				iYearTotalAmountActual = iamountTotalYear;
			}
			if(JBoltStringUtil.isBlank(cplanno))
				cplanno = getCplanno(Kv.by("iautoid", investmentPlan.getIAutoId()).set("iinvestmenttype",row.getStr("iinvestmenttype")));
			row.set("cplanno", cplanno)
			.set("isscheduled", IsScheduledEnum.WITHIN_PLAN.getValue())
			.set("itotalamountplan", iTotalAmountPlan)
			.set("itotalamountactual", iTotalAmountActual)
			.set("iYearTotalAmountPlan", iYearTotalAmountPlan)
			.set("iYearTotalAmountActual", iYearTotalAmountActual)
			.set("icreateby", userId)
			.set("dcreatetime",now)
			.set("iplanid", investmentPlan.getIAutoId())
			.set("iamounttotal", iamountTotal)
			.set("iautoid", iInvestmentPlanItemId);
			InvestmentPlanItem investmentPlanItem = JBoltModelKit.getFromBean(InvestmentPlanItem.class, JSONObject.parseObject(row.toJson()));
			ValidationUtils.isTrue(investmentPlanItem.save(), ErrorMsg.SAVE_FAILED);
			investmentPlanItemdService.batchSave(investmentPlanItemdList);
		}
	}
	/**
     * 投资计划修改-表格提交
     */
	public Ret saveTableSubmitByEdit(JBoltTable jBoltTable) {
		ValidationUtils.notNull(jBoltTable, "参数不能为空");
		Date now = new Date();
		InvestmentPlan investmentPlan = jBoltTable.getFormModel(InvestmentPlan.class, "investmentPlan");
		DataPermissionKit.validateAccess(investmentPlan.getCDepCode());
		Long iInvestmentPlanId = investmentPlan.getIAutoId();
		InvestmentPlan investmentPlanDbs = findById(iInvestmentPlanId);
		tx(()->{
			Long userId = JBoltUserKit.getUserId();
			//保存新增行： 
			List<Record> listRecord = jBoltTable.getSaveRecordList();
			saveInvestmentPlanItemForSubmitTable(listRecord,investmentPlanDbs,userId,now);
			//更新编辑行：先根据投资计划项目ID删除它所有的明细,再重新保存明细
			List<Record> updateList = jBoltTable.getUpdateRecordList();
			if(CollUtil.isNotEmpty(updateList)){
				for (Record row: updateList) {
					Long iItemId = row.getLong("iautoid");
					List<InvestmentPlanItemd> investmentPlanItemdList = new ArrayList<InvestmentPlanItemd>();
					constructInvestmentPlanItemd(row,investmentPlanDbs,iItemId,investmentPlanItemdList,now);
					BigDecimal iTotalAmountPlan = BigDecimal.ZERO; //项目总金额-计划金额
					BigDecimal iTotalAmountActual = BigDecimal.ZERO;//项目总金额-实绩/预测/修订金额
					BigDecimal iYearTotalAmountPlan = BigDecimal.ZERO;//预算年份的项目总金额-计划金额
					BigDecimal iYearTotalAmountActual = BigDecimal.ZERO;//预算年份的项目总金额-实绩/预测/修订金额
					/**
					 * 修改项目的项目总金额-实绩/预测/修订金额为空或者0:
					 * 		项目总金额-计划金额和预算年份的项目总金额-计划金额为1-6期的金额合计
					 * 		项目总金额-实绩/预测/修订金额和预算年份的项目总金额-实绩/预测/修订金额为0
					 *	不为空：
					 *		项目总金额-计划金额和预算年份的项目总金额-实绩/预测/修订金额可编辑表格中对应金额 (取上期的金额)
					 * 		项目总金额-实绩/预测/修订和预算年份的项目总金额-实绩/预测/修订金额金额为1-6期的金额合计
					 * */
					BigDecimal iamountTotal = calculateTotalAmount(investmentPlanItemdList);
					BigDecimal iamountTotalYear = calculateTotalAmount(investmentPlanItemdList,investmentPlan.getIBudgetYear());
					BigDecimal iTotalAmountActualRow = row.getBigDecimal("itotalamountactual");
					if(iTotalAmountActualRow == null || iTotalAmountActualRow.compareTo(BigDecimal.ZERO) == 0){
						iTotalAmountPlan = iamountTotal;
						iYearTotalAmountPlan = iamountTotalYear;
					}else{
						iTotalAmountPlan = row.getBigDecimal("itotalamountplan").multiply(Constants.RATIO);
						iYearTotalAmountPlan = row.getBigDecimal("iyeartotalamountplan").multiply(Constants.RATIO);
						iTotalAmountActual = iamountTotal;
						iYearTotalAmountActual = iamountTotalYear;
					}
					row.set("itotalamountplan", iTotalAmountPlan)
					.set("itotalamountactual", iTotalAmountActual)
					.set("iYearTotalAmountPlan", iYearTotalAmountPlan)
					.set("iYearTotalAmountActual", iYearTotalAmountActual)
					.set("iamounttotal", iamountTotal)
					.set("iupdateby", userId)
					.set("dupdatetime",now);
					InvestmentPlanItem investmentPlanItem = JBoltModelKit.getFromBean(InvestmentPlanItem.class, JSONObject.parseObject(row.toJson()));
					ValidationUtils.isTrue(investmentPlanItem.update(), ErrorMsg.UPDATE_FAILED);
					investmentPlanItemdService.deleteByItemId(investmentPlanItem.getIautoid());
					investmentPlanItemdService.batchSave(investmentPlanItemdList);
				}
			}
			//删除行：先删除项目明细，再删除项目数据
			Object[] ids = jBoltTable.getDelete();
			if(JBoltArrayUtil.notEmpty(ids)){
				for (Object id : ids) {
					investmentPlanItemdService.deleteByItemId(Long.parseLong(id.toString()));
					investmentPlanItemService.deleteById(id);
				}
			}
			return true;
		});
		return successWithData(investmentPlan.keep("iautoid"));
	}
	public InvestmentPlan findModelByYearAndType(Integer iBudgetYear, Integer iBudgetType, String cdepcode) {
		return findFirst(selectSql().eq("ibudgetyear", iBudgetYear).eq("ibudgettype", iBudgetType).eq("cdepcode", cdepcode).notEq("ieffectivestatus", EffectiveStatusEnum.CANCLE.getValue()));
	}

	/**
	 * 查询详情界面投资计划主数据
	 */
	public Record findInvestmentPlanDataForDetail(Long iautoid) {
		Record record = findFirstRecord(selectSql().eq("iautoid", iautoid));
		record.set("cdepname", departmentService.getCdepName(record.getStr("cdepcode")));
		return record;
	}

	/**
	 * 投资计划详情界面查询投资计划项目数据
	 * */
	public List<Record> findInvestmentPlanItemForDetail(Long investmentPlanId) {
		List<Record> list = dbTemplate("investmentplan.findInvestmentPlanItemForDetail",Kv.by("investmentplanid", investmentPlanId)).find();
		for (Record record : list) {
            Constants.fillPlanItem(record);
		}
		return list;
	}

	public InvestmentPlan findEffectivedInvestmentByDeptCode(Kv para) {
		String cdepcode = para.getStr("cdepcode");
		ValidationUtils.notBlank(cdepcode, "部门为空，请检查!");
		return findFirst(selectSql().eq("cdepcode", cdepcode).eq("ieffectivestatus", EffectiveStatusEnum.EFFECTIVED.getValue()));
	}

	public Page<Record> appendItemIndexDatas(Integer pageNumber, Integer pageSize, Kv para) {
		para.set("isscheduled",IsScheduledEnum.BEYOND_PLAN.getValue());
		Page<Record> page = dbTemplate("investmentplan.appendItemIndexDatas",para).paginate(pageNumber, pageSize);
		for (Record row : page.getList()) {
			row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
			row.set("cusername", JBoltUserCache.me.getName(row.getLong("icreateby")));
		}
		return page;
	}

	public List<Record> findInvestmentPlanItemDatas(Long iplanid) {
		List<Record> list = dbTemplate("investmentplan.findInvestmentPlanItemDatas",Kv.by("iplanid", iplanid)).find();
		for (Record record : list) {
            Constants.fillPlanItem(record);
		}
		return list;
	}
	/**
	 * 查询投资计划未完成项目: 
	 * 	1.全年预算查询上一年预算年度的下半年修订的未完成项目
	 * 	2.下半年修订查询同预算年度的全年预算的未完成项目
	 * */
	public List<Record> findUnfinishInvestmentPlanItemDatas(Long iplanid) {
		Kv para = Kv.by("iplanid",iplanid).set("iservicetype",ServiceTypeEnum.INVESTMENT_PLAN.getValue())
				.set("istatus",FinishStatusEnum.UNFINISHED.getValue());
		List<Record> list = dbTemplate(u8SourceConfigName(),"investmentplan.findUnfinishInvestmentPlanItemDatas",para).find();
		for (Record record : list) {
            Constants.fillPlanItem(record);
		}
		return list;
	}
	/**
	 * 查询上期投资计划主表数据:
	 * 	1.如果是全年预算，导入上个预算年度的实绩预测的未完成项目
	 * 	2.如果是下半年修订，导入同年份的全年预算的未完成项目
	 * 	3.如果是实绩预测，导入同年份的下半年修订的未完成项目
	 * */
	public InvestmentPlan findPreviousPeriodInvestmentPlan(Kv para) {
		Integer ibudgettype = para.getInt("ibudgettype");
		Integer ibudgetyear = para.getInt("ibudgetyear");
		String cdepcode = para.getStr("cdepcode");
		ValidationUtils.notBlank(cdepcode, "预算部门不能为空");
		ValidationUtils.notNull(ibudgettype, "预算类型不能为空");
		ValidationUtils.notNull(ibudgetyear, "预算年份不能为空");
		Integer ibudgettypeNew = new Integer(ibudgettype);
		Integer ibudgetyearNew = new Integer(ibudgetyear);
		if(ibudgettype == InvestmentBudgetTypeEnum.FUALL_YEAR_BUDGET.getValue()) {
			ibudgettypeNew = InvestmentBudgetTypeEnum.NEXT_PERIOD_EDIT.getValue();
			ibudgetyearNew = ibudgetyear - 1;
		}
		else if(ibudgettype == InvestmentBudgetTypeEnum.NEXT_PERIOD_EDIT.getValue()) ibudgettypeNew = InvestmentBudgetTypeEnum.FUALL_YEAR_BUDGET.getValue();
		//else if(ibudgettype == InvestmentBudgetTypeEnum.ACTUAL_BUDGET.getValue()) ibudgettypeNew = InvestmentBudgetTypeEnum.NEXT_PERIOD_EDIT.getValue();
		InvestmentPlan unfinishItemInvestmentPlan = findFirst(selectSql().eq("ibudgettype", ibudgettypeNew).eq("ibudgetyear", ibudgetyearNew).eq("cdepcode", cdepcode));
		return unfinishItemInvestmentPlan;
	}
/*	*//**
	 * 提交审核: 
	 * 	1. 未审核、未通过状态的投资计划可提交审核
	 * 	2. 调用提交流程方法
	 * 	3. 更改投资计划审核状态为待审核
	 * *//*
	public Ret submit(Long iplanid) {
		Date now = new Date();
		ValidationUtils.notNull(iplanid, "请先保存后再提交");
		InvestmentPlan investmentPlan = findById(iplanid);
		if(AppConfig.isVerifyProgressEnabled()){
			// 根据审批状态
            Ret ret = formApprovalService.submit(table(), iplanid, primaryKey(),"cn.rjtech.admin.investmentplan.InvestmentPlanService");
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));
            
            //生成待办和发送邮件
		}else{
			investmentPlan.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
			investmentPlan.setDAuditTime(now);
			ValidationUtils.isTrue(investmentPlan.update(), ErrorMsg.UPDATE_FAILED);
		}
		return SUCCESS;
	}*/
	
	
    /**
     * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
     */
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }
	
    /**
     * 实现反审之后的其他业务操作, 如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     */
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        // 反审回第一个节点，回退状态为“已保存”
        if (isFirst) {
       
        }
        // 最后一步通过的，反审，回退状态为“待审核”
        if (isLast) {
           
        }
        return null;
    }		
	
	/**
     * 投资预实差异管理表数据查询
     * */
	public List<Record> findBudgetActualDifferenceDatas(Kv para) {
		para.set("u8dbname",U8DataSourceKit.ME.getU8DbName(getOrgCode()));
    	List<String> list = DataPermissionKit.getAccessCdepcodes();
    	para.set("accesscdepcodes", null);
        if (CollUtil.isNotEmpty(list)) {
            String sqlInStrCdepcode = "";
            for (String cdepcode : list) {
                sqlInStrCdepcode += "'" + cdepcode + "',";
            }
            sqlInStrCdepcode += "''";
            para.set("accesscdepcodes", sqlInStrCdepcode);
        }
		List<Record> rsList = dbTemplate("investmentplan.findBudgetActualDifferenceDatas",para).find();
		for (Record record : rsList) {
			Constants.fillPlanItem(record);
		}
		return rsList;
	}
    /**
     * 投资汇总表数据查询
     * */
	public List<Record> findInvestmentPlanGroupSummaryDatas(Kv para) {
		para.set("u8dbname",U8DataSourceKit.ME.getU8DbName(getOrgCode()));
    	List<String> list = DataPermissionKit.getAccessCdepcodes();
    	para.set("accesscdepcodes", null);
        if (CollUtil.isNotEmpty(list)) {
            String sqlInStrCdepcode = "";
            for (String cdepcode : list) {
                sqlInStrCdepcode += "'" + cdepcode + "',";
            }
            sqlInStrCdepcode += "''";
            para.set("accesscdepcodes", sqlInStrCdepcode);
        }
		List<Record> rsList = dbTemplate("investmentplan.findInvestmentPlanGroupSummaryDatas",para).find();
		String cgroupkey = para.getStr("cgroupkey");
		ValidationUtils.notBlank(cgroupkey, "请选择字段筛选!");
		InvestmentSummaryGroupOptionEnum groupOptionEnum = InvestmentSummaryGroupOptionEnum.toEnum(cgroupkey);
		for (Record row : rsList) {
			switch (groupOptionEnum) {
			case IINVESTMENTTYPE:
				row.set("cgroupname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_TYPE.getValue(), row.getStr("cgroupsn")));
				break;
			case ICAREERTYPE:
				row.set("cgroupname",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("cgroupsn")));
				break;
			case IINVESTMENTDISTINCTION:
				row.set("cgroupname",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_DISTINCTION.getValue(), row.getStr("cgroupsn")));
				break;
			case CASSETTYPE:
				row.set("cgroupname",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CASSETTYPE.getValue(), row.getStr("cgroupsn")));
				break;
			case CEDITTYPE:
				row.set("cgroupname",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.EDITTYPE.getValue(), row.getStr("cgroupsn")));
				break;
			case CDEPCODE:
				row.set("cgroupname",departmentService.getCdepName(row.getStr("cgroupsn")));
				break;
			case IBUDGETYEAR:
				row.set("cgroupname",row.getStr("cgroupsn"));
				break;
			case IBUDGETTYPE:
				row.set("cgroupname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_BUDGET_TYPE.getValue(), row.getStr("cgroupsn")));
				break;
			case CMODELINVCCODE:
				row.set("cgroupname",row.getStr("cgroupsn"));
				break;
			case CPARTS:
				row.set("cgroupname",row.getStr("cgroupsn"));
				break;
			case IITEMYEAR:
				row.set("cgroupname",row.getStr("cgroupsn"));
				break;
			default:
				break;
			}
			//row.set("cgroupname", value);
		}
		return rsList;
	}
	/**
	 * 投资情况查询表
	 * */
	public List<Record> findInvestmentPlanItemSituationDatas(Kv para){
		String dstartdate = para.getStr("dstartdate");
		String denddate = para.getStr("denddate");
		if(JBoltStringUtil.isBlank(dstartdate) || JBoltStringUtil.isBlank(denddate)) return null;
		ValidationUtils.notBlank(dstartdate, "请选择起止日期");
		ValidationUtils.notBlank(denddate, "请选择起止日期");
		ExpenseBudget expenseBudget = new ExpenseBudget();
		dstartdate = dstartdate + "-01";
		denddate = denddate + "-01";
		para.set("dstartdate",dstartdate);
		para.set("denddate",denddate);
		expenseBudget.setCBeginDate(JBoltDateUtil.toDate(dstartdate,"yyyy-MM-dd"));
		expenseBudget.setCEndDate(JBoltDateUtil.toDate(denddate,"yyyy-MM-dd"));
		expenseBudgetService.constructDynamicsDbColumn(expenseBudget, para);
    	List<String> list = DataPermissionKit.getAccessCdepcodes();
    	para.set("accesscdepcodes", null);
        if (CollUtil.isNotEmpty(list)) {
            String sqlInStrCdepcode = "";
            for (String cdepcode : list) {
                sqlInStrCdepcode += "'" + cdepcode + "',";
            }
            sqlInStrCdepcode += "''";
            para.set("accesscdepcodes", sqlInStrCdepcode);
        }
		List<Record> rsList = dbTemplate(u8SourceConfigName(),"investmentplan.findInvestmentPlanItemSituationDatas",para).find();
		if(CollUtil.isNotEmpty(rsList)){
			for (Record row : rsList) {
				row.set("cinvestmenttypedesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_TYPE.getValue(), row.getStr("iinvestmenttype")));
				row.set("careertypedesc",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("icareertype")));
				row.set("cinvestmentdistinctiondesc",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_DISTINCTION.getValue(), row.getStr("iinvestmentdistinction")));
				row.set("cassettypedesc",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CASSETTYPE.getValue(), row.getStr("cassettype")));
			}
		}
		return rsList;
	}
	/**
	 * 根据启用期间的起止日期计算投资情况查询表的表头
	 * */	
	public void calcDynamicInvestmentPlanSituationColumn(Date dstarttime, Date dendtime, List<Record> yearColumnTxtList,
			List<Record> monthColumnTxtList) {
		int colspan = 0;
        Date dendTimeElse = JBoltDateUtil.toDate(JBoltDateUtil.format(dendtime, "yyyy-MM"),"yyyy-MM");
        while(JBoltDateUtil.toDate(JBoltDateUtil.format(dstarttime, "yyyy-MM"),"yyyy-MM").compareTo(dendTimeElse) != 1){
        	colspan++;
        	int dstarttimemonth = Integer.parseInt(JBoltDateUtil.format(dstarttime, JBoltDateUtil.MM));
        	//年度是否结束标识:是否为年度最后一个月,12月或者期间结束日期的月份
        	boolean isYearFinalFlg = dstarttimemonth == 12
            		|| JBoltDateUtil.toDate(JBoltDateUtil.format(dstarttime, "yyyy-MM"),"yyyy-MM").compareTo(dendTimeElse) == 0;
        	//年度金额合计列的data-column属性
        	String totaldatacolumn = "itotal"+JBoltDateUtil.format(dstarttime, "yyyy");
        	if(isYearFinalFlg){
        		Record yearColumnRc = new Record();
        		yearColumnRc.set("colname", JBoltDateUtil.format(dstarttime, "yyyy")+"年");
        		yearColumnRc.set("colspan", colspan);
        		yearColumnRc.set("totaldatacolumn", totaldatacolumn);
        		colspan = 0;
        		yearColumnTxtList.add(yearColumnRc);
        	}
        	Record monthRecord = new Record();
        	monthRecord.set("colname", dstarttimemonth + "月");
        	monthRecord.set("datacolumn", "imonthamount"+JBoltDateUtil.format(dstarttime, "yyyy")+dstarttimemonth);
        	if(isYearFinalFlg){
        		monthRecord.set("totaldatacolumn", totaldatacolumn);	
        	}
        	monthColumnTxtList.add(monthRecord);
        	dstarttime = JBoltDateUtil.nextMonthFirstDay(dstarttime);
        }
	}
    /**
     * 执行进度跟踪表-费用tab数据查询
     * */
	public List<Record> findExecutionProgressTrackingExpenseDatas(Kv para) {
		para.set("u8dbname",U8DataSourceKit.ME.getU8DbName(getOrgCode()));
    	List<String> list = DataPermissionKit.getAccessCdepcodes();
    	para.set("accesscdepcodes", null);
        if (CollUtil.isNotEmpty(list)) {
            String sqlInStrCdepcode = "";
            for (String cdepcode : list) {
                sqlInStrCdepcode += "'" + cdepcode + "',";
            }
            sqlInStrCdepcode += "''";
            para.set("accesscdepcodes", sqlInStrCdepcode);
        }
		return dbTemplate("investmentplan.findExecutionProgressTrackingExpenseDatas",para).find();
	}	
    /**
     * 执行进度跟踪表-投资tab数据查询
     * */
	public List<Record> findExecutionProgressTrackingInvestmentDatas(Kv para) {
		para.set("u8dbname",U8DataSourceKit.ME.getU8DbName(getOrgCode()));
    	List<String> list = DataPermissionKit.getAccessCdepcodes();
    	para.set("accesscdepcodes", null);
        if (CollUtil.isNotEmpty(list)) {
            String sqlInStrCdepcode = "";
            for (String cdepcode : list) {
                sqlInStrCdepcode += "'" + cdepcode + "',";
            }
            sqlInStrCdepcode += "''";
            para.set("accesscdepcodes", sqlInStrCdepcode);
        }
		return dbTemplate("investmentplan.findExecutionProgressTrackingInvestmentDatas",para).find();
	}	
	
	/**
	 * 部门是否存在已生效的投资计划
	 * */
	public boolean isExistsEffectivedInvestment(InvestmentPlan investmentPlan) {
		List<InvestmentPlan> list = find(selectSql().eq("cdepcode", investmentPlan.getCDepCode()).eq("iBudgetYear", investmentPlan.getIBudgetYear())
				.eq("iBudgetType", investmentPlan.getIBudgetType()).eq("iEffectiveStatus", EffectiveStatusEnum.EFFECTIVED.getValue()));
		if(CollUtil.isNotEmpty(list)) return true;
		return false;
	}
	/**
	 * 编码规则获取单据内容：部门英文名称，单据年度等
	 * */
	public String getItemOrderContent(Long iautoid,String cprojectcode) {
		String str = "";
		InvestmentPlan investmentPlan = findById(iautoid);
    	ValidationUtils.notNull(investmentPlan, "投资计划数据不存在,获取部门英文名称失败!");
		switch (BarCodeEnum.toEnum(cprojectcode)) {
	        case DEPT:
	        	Record record = departmentService.findByCdepcode(getOrgId(),investmentPlan.getCDepCode()).toRecord();
	    		String cdepnameen = record.getStr("cdepnameen");
	    		ValidationUtils.notBlank(cdepnameen, record.getStr("cdepname")+"部门的英文名称为空!");
	    		str = cdepnameen;
	            break;  
	        case ORDERYEAR:
	        	Integer iBudgetYear = investmentPlan.getIBudgetYear();
	        	ValidationUtils.notNull(iBudgetYear, BarCodeEnum.ORDERYEAR.getText()+"为空!");
	    		str = iBudgetYear.toString();
	            break;
	        default:
                break;
		}
		return str;
	}
	/**
	 * 期间是否应用于投资计划
	 * */
	public boolean periodIsExists(Period dbPeriod) {
		List<InvestmentPlan> list = find(selectSql().eq("iPeriodId", dbPeriod.getIautoid()));
		return CollUtil.isNotEmpty(list);
	}

	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preSubmitFunc(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postSubmitFunc(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postWithdrawFunc(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String withdrawFromAuditting(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preWithdrawFromAuditted(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postWithdrawFromAuditted(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postBatchApprove(List<Long> formAutoIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postBatchReject(List<Long> formAutoIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		// TODO Auto-generated method stub
		return null;
	}
}