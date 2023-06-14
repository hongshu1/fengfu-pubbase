package cn.rjtech.admin.expensebudget;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.NumberUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import cn.jbolt.core.poi.excel.JBoltExcelPositionData;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodeencodingm.BarcodeencodingmService;
import cn.rjtech.admin.datapermission.DataPermissionService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.expensebudgetitemd.ExpenseBudgetItemdService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.admin.subjectm.SubjectmService;
import cn.rjtech.config.AppConfig;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.model.momdata.ExpenseBudgetItemd;
import cn.rjtech.model.momdata.Period;
import cn.rjtech.model.momdata.Subjectm;
import cn.rjtech.util.ReadFullYearExpenseBudgetExcelUtil;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.ArrayUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;


/**
 * 费用预算 Service
 * @ClassName: ExpenseBudgetService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-15 09:54
 */
public class ExpenseBudgetService extends BaseService<ExpenseBudget> {

	private final ExpenseBudget dao = new ExpenseBudget().dao();
	@Inject
	private DepartmentService departmentService;
	@Inject
	private ExpenseBudgetItemService expenseBudgetItemService;
	@Inject
	private ExpenseBudgetItemdService expenseBudgetItemdService;
	@Inject
	private SubjectmService subjectmService;
	@Inject
	private DictionaryService dictionaryService;
	@Inject
	private PeriodService periodService;
	@Inject
	private DataPermissionService datapermissionService;
	@Inject
	private BarcodeencodingmService barcodeencodingmService;
	@Override
	protected ExpenseBudget dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		User user = JBoltUserKit.getUser();
        // 系统管理员 || 存在权限部门
        para.set("isSystemAdmin", user.getIsSystemAdmin())
        	.set("iorgid",getOrgId());
		Page<Record> pageRecord =  dbTemplate("expensebudget.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : pageRecord.getList()) {
			row.set("cusername",JBoltUserCache.me.getUserName(row.getLong("icreateby")));
			row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
			row.set("beginandenddate", JBoltDateUtil.format(row.getDate("cbegindate"), "yyyy.MM")+"-" + JBoltDateUtil.format(row.getDate("cenddate"), "yyyy.MM"));
		}
		return pageRecord;
	}

	/**
	 * 保存
	 * @param expenseBudget
	 * @return
	 */
	public Ret save(ExpenseBudget expenseBudget) {
		if(expenseBudget==null || isOk(expenseBudget.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(expenseBudget.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(expenseBudget.getIautoid(), JBoltUserKit.getUserId(), expenseBudget.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(ExpenseBudget expenseBudget) {
		if(expenseBudget==null || notOk(expenseBudget.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			ExpenseBudget dbExpenseBudget = findById(expenseBudget.getIautoid());
			ValidationUtils.notNull(dbExpenseBudget, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(expenseBudget.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(expenseBudget.getIautoid(), JBoltUserKit.getUserId(), expenseBudget.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID :
	 * 	1.删除费用预算项目明细数据
	 * 	2.删除费用预算项目
	 *  3.如果删除的费用预算项目是最后一条，需要删除主表数据
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				ExpenseBudget expenseBudget = findById(iAutoId);
				ValidationUtils.notNull(expenseBudget, JBoltMsg.DATA_NOT_EXIST);
				deleteExpenseBudgetItem(iAutoId);
				ValidationUtils.isTrue(expenseBudget.delete(), ErrorMsg.DELETE_FAILED);
			}
			return true;
		});
		// 添加日志
		// ExpenseBudget expenseBudget = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, expenseBudget.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param expenseBudget 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(ExpenseBudget expenseBudget, Kv kv) {
		//addDeleteSystemLog(expenseBudget.getIautoid(), JBoltUserKit.getUserId(),expenseBudget.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param expenseBudget 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(ExpenseBudget expenseBudget, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(expenseBudget, kv);
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
	 * 查询下期修改导出模板中的历史数据
	 * */
	public List<Record> findExportExpenseBudgetTplNextEditDatas(Kv para) {
		return null;
	}
	/*public Ret importExpenseBudgetTpl(String filePath) throws Exception {
		// 读取excel中数据
		int dataStartRowNum = 8;//开始读数数据行数
        HashMap<String, Object> excelMap = ReadFullYearExpenseBudgetExcelUtil.readExcelInfo(filePath,dataStartRowNum);
        List<Record> excelRecords = (List<Record>) excelMap.get("rows");
        ValidationUtils.notNull(excelRecords, "未读取到excel中的内容");
        StringBuilder alertErrorMsg = new StringBuilder();//校验提示内容
        Date now = new Date();
        //获取预算年度,预算类型,部门名称,开始年月,结束年月
	    Record rc = validateExpenseBudgetTpl(excelMap);
	    int budgetStartYear = rc.getInt("budgetStartYear");
	    int budgetStartMonth = rc.getInt("budgetStartMonth");
	    int budgetEndYear = rc.getInt("budgetEndYear");
	    int budgetEndMonth = rc.getInt("budgetEndMonth");
	    String cdepcode = rc.getStr("cdepcode");
	    Integer ibudgetType = rc.getInt("cbudgetType");
	    Integer ibudgetyear = rc.getInt("ibudgetyear");
	    Date cBeginDate = JBoltDateUtil.getDate(budgetStartYear+"-"+budgetStartMonth+"-01", JBoltDateUtil.YMD);
	    Date cEndDate = JBoltDateUtil.getDate(budgetEndYear+"-"+budgetEndMonth+"-01", JBoltDateUtil.YMD);
	    ExpenseBudget expenseBudget = new ExpenseBudget();
	    List<ExpenseBudgetItem> expenseBudgetItemList = new ArrayList<>();
	    List<ExpenseBudgetItemd> expenseBudgetItemdList = new ArrayList<>();
	    Long expenseBudgetId = JBoltSnowflakeKit.me.nextId();
	    expenseBudget.setIautoid(expenseBudgetId);
	    expenseBudget.setIorgid(getOrgId());
	    expenseBudget.setCorgcode(getOrgCode());
	    expenseBudget.setCdepcode(cdepcode);
	    expenseBudget.setIbudgettype(ibudgetType);
	    expenseBudget.setCbegindate(cBeginDate);
	    expenseBudget.setCenddate(cEndDate);
	    expenseBudget.setIauditstatus(AuditStatusEnum.NOT_AUDIT.getValue());
	    expenseBudget.setDcreatetime(now);
	    expenseBudget.setIcreateby(JBoltUserKit.getUserId());
	    expenseBudget.setIbudgetyear(ibudgetyear);
    	constructFullYearBudgetTypeModel(excelRecords,expenseBudget,cdepcode,expenseBudgetItemList,expenseBudgetItemdList,alertErrorMsg,dataStartRowNum,now);
	    if(alertErrorMsg.toString().length()>0)
	    	return fail(alertErrorMsg.toString());
	    tx(() -> {
	    	ValidationUtils.isTrue(expenseBudget.save(), ErrorMsg.SAVE_FAILED);
	    	expenseBudgetItemService.batchSave(expenseBudgetItemList);
	    	expenseBudgetItemdService.batchSave(expenseBudgetItemdList);
	    	return true;
	    });
	    return SUCCESS;
	}*/
	
	/**
	 * 删除费用预算项目和项目明细数据
	 * */
	public void deleteExpenseBudgetItem(Long iexpenseId){
		List<ExpenseBudgetItem> expenseBudgetItemList = expenseBudgetItemService.findByMainId(iexpenseId);
		if(CollUtil.isEmpty(expenseBudgetItemList)) return;
		for (ExpenseBudgetItem expenseBudgetItem : expenseBudgetItemList) {
			expenseBudgetItemdService.deleteByItemId(expenseBudgetItem.getIautoid());
			ValidationUtils.isTrue(expenseBudgetItem.delete(), ErrorMsg.DELETE_FAILED);
		}
	}
	/**
     * 费用预算编制可编辑表格导入
	 * @throws Exception
     */
	public Ret importTableExpenseBudgetTpl(String filePath,Long iexpenseId) throws Exception {
		// 读取excel中数据
		int startRow = ReadFullYearExpenseBudgetExcelUtil.START_ROW + 1;
        HashMap<String, Object> excelMap = ReadFullYearExpenseBudgetExcelUtil.readExcelInfo(filePath);
        List<Record> excelRecords = (List<Record>) excelMap.get("rows");
        ValidationUtils.notNull(excelRecords, "未读取到excel中的内容");
        Date now = new Date();
        //获取预算年度,预算类型,部门名称,开始年月,结束年月
	    Record rc = validateExpenseBudgetTpl(excelMap);
	    int budgetStartYear = rc.getInt("budgetStartYear");
	    int budgetStartMonth = rc.getInt("budgetStartMonth");
	    int budgetEndYear = rc.getInt("budgetEndYear");
	    int budgetEndMonth = rc.getInt("budgetEndMonth");
	    String cdepcode = rc.getStr("cdepcode");
	    Integer ibudgetType = rc.getInt("cbudgetType");
	    Integer ibudgetyear = rc.getInt("ibudgetyear");
	    Date cBeginDate = JBoltDateUtil.getDate(budgetStartYear+"-"+budgetStartMonth+"-01", JBoltDateUtil.YMD);
	    Date cEndDate = JBoltDateUtil.getDate(budgetEndYear+"-"+budgetEndMonth+"-01", JBoltDateUtil.YMD);
	    ExpenseBudget expenseBudget = new ExpenseBudget();
	    expenseBudget.setIorgid(getOrgId());
	    expenseBudget.setCorgcode(getOrgCode());
	    expenseBudget.setCdepcode(cdepcode);
	    expenseBudget.setIbudgettype(ibudgetType);
	    expenseBudget.setCbegindate(cBeginDate);
	    expenseBudget.setCenddate(cEndDate);
	    expenseBudget.setIauditstatus(AuditStatusEnum.NOT_AUDIT.getValue());
	    expenseBudget.setDcreatetime(now);
	    expenseBudget.setIcreateby(JBoltUserKit.getUserId());
	    expenseBudget.setIbudgetyear(ibudgetyear);
        List<Record> excelRowList = (List<Record>)excelMap.get("rows");
        StringBuilder errorMsg = new StringBuilder();
        if(CollUtil.isNotEmpty(excelRowList)){
        	constructJBoltTableExpenseBudgetItem(expenseBudget,excelRowList,errorMsg,startRow);
        }
        if(errorMsg.toString().length() > 0) return Ret.fail(errorMsg.toString());
        if(iexpenseId != null){
	        tx(()->{
	        	//删除费用预算项目和项目明细数据
	    		deleteExpenseBudgetItem(iexpenseId);
	    		expenseBudget.setIautoid(iexpenseId);
	    		saveExpenseBudgetItemForSubmitTable(excelRowList,expenseBudget,JBoltUserKit.getUserId(),now);
	    		return true;
	        });
        }
       return SUCCESS.set("list", excelRowList);
	}

	/**
	 * 校验费用预算导入模板主体内容是否合法
	 * */
	private Record validateExpenseBudgetTpl(HashMap<String, Object> excelMap) {
		  String startyearCell = (String)excelMap.get("startyear");
		  String startmonthCell = (String)excelMap.get("startmonth");
		  String cdepcodeCell = (String)excelMap.get("cdepcode");
		  String ibudgetyearCell = (String)excelMap.get("ibudgetyear");
		  String endyearCell = (String)excelMap.get("endyear");
		  String endmonthCell = (String)excelMap.get("endmonth");
		  String cbudgettypeCell = (String)excelMap.get("ibudgettype");
		  int budgetStartYear = 0;
		  int budgetStartMonth = 0;
		  int budgetEndYear = 0;
		  int budgetEndMonth = 0;
		  String cdepcode = "";
		  Integer cbudgetType = null;
		  Integer ibudgetyear = null;
		try{
	    	budgetStartYear = Integer.parseInt(startyearCell.replace("年", ""));
	    }catch(Exception e){
	    	ValidationUtils.error( "预算期间起始年份不合法,请检查导入模板!");
	    }
	    try{
	    	budgetStartMonth = Integer.parseInt(startmonthCell.replace("月", ""));
	    }catch(Exception e){
	    	ValidationUtils.error( "预算期间起始月份不合法,请检查导入模板!!");
	    }
	    String cdepname = "";
	    try {
	    	cdepname = cdepcodeCell.substring(cdepcodeCell.indexOf("：")+1);
	    	cdepcode = departmentService.getCdepCodeByName(cdepname);
		} catch (Exception e) {
			ValidationUtils.error( "预算部门获取失败,请检查导入模板!");
		}
	    try{
	    	ibudgetyear = Integer.parseInt(ibudgetyearCell.replace("年", ""));
	    }catch(Exception e){
	    	ValidationUtils.error( "预算年度不合法,请检查导入模板!");
	    }
	    try{
	    	budgetEndYear = Integer.parseInt(endyearCell.replace("年", ""));
	    }catch(Exception e){
	    	ValidationUtils.error( "预算期间结束年份不合法,请检查导入模板!");
	    }
	    try{
	    	budgetEndMonth = Integer.parseInt(endmonthCell.replace("月", ""));
	    }catch(Exception e){
	    	ValidationUtils.error( "预算期间结束月份不合法,请检查导入模板!");
	    }
	    try {
	    	for (ExpenseBudgetTypeEnum typeenum : ExpenseBudgetTypeEnum.values()) {
				if(typeenum.getText().equals(cbudgettypeCell)){
					cbudgetType = typeenum.getValue();
					break;
				}
			}
	    	ValidationUtils.notNull(cbudgetType, "预算类型不合法,请检查导入模板!");
		} catch (Exception e) {
			ValidationUtils.error( "预算类型不合法,请检查导入模板!");
		}
	    Record rc = new Record();
	    rc.set("budgetStartYear", budgetStartYear);
	    rc.set("budgetStartMonth", budgetStartMonth);
	    rc.set("budgetEndYear", budgetEndYear);
	    rc.set("budgetEndMonth", budgetEndMonth);
	    rc.set("cdepcode", cdepcode);
	    rc.set("cbudgetType", cbudgetType);
	    rc.set("ibudgetyear", ibudgetyear);
	    return rc;
	}
	public ExpenseBudget findModelByYearAndType(Integer ibudgetyear,Integer ibudgetType,String cdepcode){
		return findFirst(selectSql().eq("ibudgetyear", ibudgetyear).eq("ibudgettype", ibudgetType).eq("cdepcode", cdepcode).notEq("ieffectivestatus", EffectiveStatusEnum.CANCLE.getValue()));
	}
	private void constructJBoltTableExpenseBudgetItem(ExpenseBudget expenseBudget,List<Record> excelRecords,StringBuilder errorMsg, int dataStartRowNum) {
		//获取封装的事业类型的字典
		Record dictionaryRecord = dictionaryService.convertEnumByTypeKey(DictionaryTypeKeyEnum.CAREERTYPE.getValue());
		Date cbeginDate = expenseBudget.getCbegindate();
		Date cendDate = expenseBudget.getCenddate();
		int nowReadRowNum = dataStartRowNum;
		for (int i = 0; i < excelRecords.size(); i++) {
			nowReadRowNum = dataStartRowNum + i;
			Record row = excelRecords.get(i);
			//检验科目大类,明细科目
			//String cHighestSubjectName = row.get(FullYearBudgetEnum.CHIGHESTSUBJECTNAME.getField());
			String cLowestSubjectName = row.get(FullYearBudgetEnum.CLOWESTSUBJECTNAME.getField());
			if(JBoltStringUtil.isBlank(cLowestSubjectName)){
				errorMsg.append("第").append(nowReadRowNum+"行,").append("明细科目不能为空,请检查!<br/>");
				continue;
			}
			Subjectm lsubject = subjectmService.findByName(cLowestSubjectName);
			if(lsubject == null){
				errorMsg.append("第").append(nowReadRowNum+"行,").append(FullYearBudgetEnum.CLOWESTSUBJECTNAME.getText()).append("在科目对照表中不存在,请检查!<br/>");
				continue;
			}
			Subjectm hsubject = subjectmService.findHighestSubjectByLowestSubjectName(cLowestSubjectName);
			row.set("ihighestsubjectid",hsubject != null ? hsubject.getIAutoId():null);
			row.set("chighestsubjectname",hsubject != null ? hsubject.getCSubjectName():null);
			row.set("ilowestsubjectid",lsubject.getIAutoId());
			//事业类型中文转sn
			String careerTypeName = row.getStr(FullYearBudgetEnum.CAREERTYPENAME.getField());
			row.set("careertype",JBoltStringUtil.isBlank(careerTypeName) ? null:dictionaryRecord.getStr(careerTypeName));
			//是否大额费用转boolean
			String islargeamountexpensedesc = row.getStr(FullYearBudgetEnum.ISLARGEAMOUNTEXPENSEDESC.getField());
			row.set("islargeamountexpense",JBoltStringUtil.isBlank(islargeamountexpensedesc) ? null : IsEnableEnum.toText(islargeamountexpensedesc).getValue()+"");
			//单价
			String ipriceStr = row.getStr(FullYearBudgetEnum.IPRICE.getField());
			if(JBoltStringUtil.isNotBlank(ipriceStr) && !NumberUtil.isNumber(ipriceStr))
				errorMsg.append("第").append(nowReadRowNum).append("行,").append(FullYearBudgetEnum.IPRICE.getText()).append("非数字,请检查!<br/>");
			//计划内/外
			row.set("isscheduled",IsScheduledEnum.WITHIN_PLAN.getValue());
			//转换费用预算项目明细
			LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(cbeginDate,JBoltDateUtil.YMD));
			LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(cendDate,JBoltDateUtil.YMD));
			//起止日期计算共多少个月
			long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
			for (int j = 1; j <= diffMonth; j++) {
				Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(cbeginDate);
				calendarStartDate.add(Calendar.MONTH, j-1);
				int nowYear = calendarStartDate.get(Calendar.YEAR);
				int nowMonth = calendarStartDate.get(Calendar.MONTH)+1;
				BigDecimal iquantity = null;
				BigDecimal iamount = null;
				try {
					iquantity = row.getBigDecimal("iquantity"+j);
				} catch (Exception e) {
					errorMsg.append("第").append(nowReadRowNum).append("行,").append(nowMonth).append("月数量不合法,请检查!<br/>");
				}
				try {
					iamount = row.getBigDecimal("iamount"+j);
				} catch (Exception e) {
					errorMsg.append("第").append(nowReadRowNum).append("行,").append(nowMonth).append("月金额不合法,请检查!<br/>");
				}
				row.remove("iquantity"+j,"iamount"+j);
				row.set("iquantity"+nowYear+nowMonth, iquantity);
				row.set("iamount"+nowYear+nowMonth, iamount);
			}
			if(errorMsg.toString().length() > 0) continue;
		}
	}



	/*private void constructFullYearBudgetTypeModel(List<Record> excelRecords, ExpenseBudget expenseBudget, String cdepcode,
			List<ExpenseBudgetItem> expenseBudgetItemList, List<ExpenseBudgetItemd> expenseBudgetItemdList,
			StringBuilder alertErrorMsg,int dataStartRowNum,Date now) {
		//获取封装的事业类型的字典
		Record dictionaryRecord = dictionaryService.convertEnumByTypeKey(DictionaryTypeKeyEnum.CAREERTYPE.getValue());
		Date cbeginDate = expenseBudget.getCbegindate();
		Date cendDate = expenseBudget.getCenddate();
		for (int i = 0; i < excelRecords.size(); i++) {
			dataStartRowNum = dataStartRowNum+1+i;
			Record row = excelRecords.get(i);
			ExpenseBudgetItem expenseBudgetItem = new ExpenseBudgetItem();
			expenseBudgetItem.setIexpenseid(expenseBudget.getIautoid());
			StringBuilder errorMsg = new StringBuilder();
			//检验预算编号:如果为空则生成，不为空则赋值
			String cbudgetNo = row.get(FullYearBudgetEnum.CBUDGETNO.getField());
			expenseBudgetItem.setCbudgetno(JBoltStringUtil.isBlank(cbudgetNo) ? genExpenseBudgetNo(cdepcode,expenseBudget.getIbudgetyear()):cbudgetNo);
			//检验科目大类,明细科目
			String cHighestSubjectName = row.get(FullYearBudgetEnum.CHIGHESTSUBJECTNAME.getField());
			String cLowestSubjectName = row.get(FullYearBudgetEnum.CLOWESTSUBJECTNAME.getField());
			Subject subject = subjectService.findByName(cHighestSubjectName,cLowestSubjectName);
			if(subject == null)
				errorMsg.append("第").append(dataStartRowNum).append("行,科目档案中不存在").append(FullYearBudgetEnum.CHIGHESTSUBJECTNAME.getText()).append(":").append(cHighestSubjectName).append(",").append(FullYearBudgetEnum.CLOWESTSUBJECTNAME.getText()).append(":").append(cLowestSubjectName).append(",请检查!<br/>");
			else{
				expenseBudgetItem.setChighestsubjectcode(subject.getChighestsubjectcode());
				expenseBudgetItem.setClowestsubjectcode(subject.getClowestsubjectcode());
			}
			expenseBudgetItem.setCitemname(row.getStr(FullYearBudgetEnum.CITEMNAME.getField()));
			//事业类型中文转sn
			String careerType = row.getStr(FullYearBudgetEnum.CAREERTYPE.getField());
			expenseBudgetItem.setCareertype(dictionaryRecord.getStr(careerType));
			//是否大额费用转boolean
			String islargeamountexpense = row.getStr(FullYearBudgetEnum.ISLARGEAMOUNTEXPENSE.getField());
			expenseBudgetItem.setIslargeamountexpense(JBoltStringUtil.isBlank(islargeamountexpense) ? null : StrGenBooleanEnum.toText(islargeamountexpense).getValue());
			//用途，备注
			expenseBudgetItem.setCuse(row.getStr(FullYearBudgetEnum.CUSE.getField()));
			expenseBudgetItem.setCmemo(row.getStr(FullYearBudgetEnum.CMEMO.getField()));
			//单价
			String ipriceStr = row.getStr(FullYearBudgetEnum.IPRICE.getField());
			if(!NumberUtil.isNumber(ipriceStr))
				errorMsg.append("第").append(dataStartRowNum).append("行,").append(FullYearBudgetEnum.IPRICE.getText()).append(cbudgetNo).append("非数字,请检查!<br/>");
			expenseBudgetItem.setIprice(new BigDecimal(ipriceStr));
			expenseBudgetItem.setCunit(row.getStr(FullYearBudgetEnum.CUNIT.getField()));
			//计划内/外
			expenseBudgetItem.setIsscheduled(IsScheduledEnum.WITHIN_PLAN.getValue());
			long expenseBudgetItemId = JBoltSnowflakeKit.me.nextId();
			expenseBudgetItem.setIautoid(expenseBudgetItemId);
			//转换费用预算项目明细
			LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(cbeginDate,JBoltDateUtil.YMD));
			LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(cendDate,JBoltDateUtil.YMD));
			//起止日期计算共多少个月
			long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
			for (int j = 1; j <= diffMonth; j++) {
				Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(cbeginDate);
				calendarStartDate.add(Calendar.MONTH, j-1);
				int nowYear = calendarStartDate.get(Calendar.YEAR);
				int nowMonth = calendarStartDate.get(Calendar.MONTH)+1;
				BigDecimal iquantity = null;
				BigDecimal iamount = null;
				try {
					iquantity = row.getBigDecimal("iquantity"+j);
				} catch (Exception e) {
					errorMsg.append("第").append(dataStartRowNum).append("行,").append(nowMonth).append("月数量不合法,请检查!<br/>");
				}
				try {
					iamount = row.getBigDecimal("iamount"+j);
				} catch (Exception e) {
					errorMsg.append("第").append(dataStartRowNum).append("行,").append(nowMonth).append("月金额不合法,请检查!<br/>");
				}
				//数量不为空的情况下，如果金额不为空，金额直接取excel的值，否则金额=数量*单价
				if(iquantity == null || iquantity.compareTo(BigDecimal.ZERO) == 0) continue;
				if(iamount == null || iquantity.compareTo(BigDecimal.ZERO) == 0) iamount = iquantity.multiply(expenseBudgetItem.getIprice());
				ExpenseBudgetItemd expenseBudgetItemd = new ExpenseBudgetItemd();
				long expenseBudgetItemdId = JBoltSnowflakeKit.me.nextId();
				expenseBudgetItemd.setIautoid(expenseBudgetItemdId);
				expenseBudgetItemd.setIexpenseitemid(expenseBudgetItemId);
				expenseBudgetItemd.setIyear(nowYear);
				expenseBudgetItemd.setImonth(nowMonth);
				expenseBudgetItemd.setIquantity(iquantity);
				expenseBudgetItemd.setIamount(iamount);
				expenseBudgetItemd.setIcreateby(JBoltUserKit.getUserId());
				expenseBudgetItemd.setDcreatetime(now);
				expenseBudgetItemdList.add(expenseBudgetItemd);
			}
			alertErrorMsg.append(errorMsg);
			if(errorMsg.toString().length() > 0) continue;
			expenseBudgetItemList.add(expenseBudgetItem);
		}
	}
	private void constructNextEditBudgetTypeModel(List<Record> excelRecords, ExpenseBudget expenseBudget,
			String cdepcode, List<ExpenseBudgetItem> expenseBudgetItemList,
			List<ExpenseBudgetItemd> expenseBudgetItemdList, StringBuilder alertErrorMsg, int dataStartRowNum,
			Date now) {
		//获取封装的事业类型的字典
		Record dictionaryRecord = dictionaryService.convertEnumByTypeKey(DictionaryTypeKeyEnum.CAREERTYPE.getValue());
		Date cbeginDate = expenseBudget.getCbegindate();
		for (int i = 0; i < excelRecords.size(); i++) {
			dataStartRowNum = dataStartRowNum+1+i;
			Record row = excelRecords.get(i);
			ExpenseBudgetItem expenseBudgetItem = new ExpenseBudgetItem();
			expenseBudgetItem.setIexpenseid(expenseBudget.getIautoid());
			StringBuilder errorMsg = new StringBuilder();
			//校验项目编码
			String cprojectcode = row.get(NextEditBudgetEnum.CPROJECTCODE.getField());
			if(JBoltStringUtil.isBlank(cprojectcode)) errorMsg.append("第").append(dataStartRowNum).append("行,").append(NextEditBudgetEnum.CPROJECTCODE.getText()).append("为空<br/>");
			else{
				Project project = projectService.findByProjectCode(cprojectcode,expenseBudget.getCdepcode());
				if(project == null) errorMsg.append("第").append(dataStartRowNum).append("行,项目档案中不存在").append(NextEditBudgetEnum.CPROJECTCODE.getText()).append(cprojectcode).append("<br/>");
				else expenseBudgetItem.setIprojectid(project.getIautoid());
			}
			//检验预算编号:如果为空则生成，不为空判断是否有对应的费用预算项目
			String cbudgetNo = row.get(NextEditBudgetEnum.CBUDGETNO.getField());
			if(JBoltStringUtil.isBlank(cbudgetNo))
				expenseBudgetItem.setCbudgetno(genExpenseBudgetNo(cdepcode,expenseBudget.getIbudgetyear()));
			else if(expenseBudgetItemService.isNotExistsBudgetNo(cbudgetNo)) //不存在则提示
				errorMsg.append("第").append(dataStartRowNum).append("行,").append(NextEditBudgetEnum.CBUDGETNO.getText()).append(cbudgetNo).append("系统中不存在,请检查!<br/>");
			//检验科目大类,明细科目
			String cHighestSubjectName = row.get(NextEditBudgetEnum.CHIGHESTSUBJECTNAME.getField());
			String cLowestSubjectName = row.get(NextEditBudgetEnum.CLOWESTSUBJECTNAME.getField());
			Subject subject = subjectService.findByName(cHighestSubjectName,cLowestSubjectName);
			if(subject == null)
				errorMsg.append("第").append(dataStartRowNum).append("行,科目档案中不存在").append(NextEditBudgetEnum.CHIGHESTSUBJECTNAME.getText()).append(":").append(cHighestSubjectName).append(",").append(NextEditBudgetEnum.CLOWESTSUBJECTNAME.getText()).append(":").append(cLowestSubjectName).append(",请检查!<br/>");
			else{
				expenseBudgetItem.setChighestsubjectcode(subject.getChighestsubjectcode());
				expenseBudgetItem.setClowestsubjectcode(subject.getClowestsubjectcode());
			}
			expenseBudgetItem.setCitemname(row.getStr(NextEditBudgetEnum.CITEMNAME.getField()));
			//事业类型中文转sn
			String careerType = row.getStr(NextEditBudgetEnum.CAREERTYPE.getField());
			expenseBudgetItem.setCareertype(dictionaryRecord.getStr(careerType));
			//是否大额费用转boolean
			String islargeamountexpense = row.getStr(NextEditBudgetEnum.ISLARGEAMOUNTEXPENSE.getField());
			expenseBudgetItem.setIslargeamountexpense(JBoltStringUtil.isBlank(islargeamountexpense) ? null : StrGenBooleanEnum.toEnum(islargeamountexpense).getValue());
			//单价
			String ipriceStr = row.getStr(NextEditBudgetEnum.IPRICE.getField());
			if(!NumberUtil.isNumber(ipriceStr))
				errorMsg.append("第").append(dataStartRowNum).append("行,").append(NextEditBudgetEnum.IPRICE.getText()).append(cbudgetNo).append("非数字,请检查!<br/>");
			expenseBudgetItem.setIprice(new BigDecimal(ipriceStr));
			expenseBudgetItem.setCunit(row.getStr(NextEditBudgetEnum.CUNIT.getField()));
			//已结转
			BigDecimal carryforward = null;
			try {
				carryforward = row.getBigDecimal(FullYearBudgetEnum.CARRYFORWARD.getField());
			} catch (Exception e) {
				errorMsg.append("第").append(dataStartRowNum).append("行,").append(FullYearBudgetEnum.CARRYFORWARD.getText()).append("不合法,请检查!<br/>");
			}
			expenseBudgetItem.setIcarryforward(carryforward);
			long expenseBudgetItemId = JBoltSnowflakeKit.me.nextId();
			expenseBudgetItem.setIautoid(expenseBudgetItemId);
			//转换费用预算项目明细
			Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(cbeginDate);
			for (int j = 1; j <= 12; j++) {
				BigDecimal iquantity = null;
				BigDecimal iamount = null;
				try {
					iquantity = row.getBigDecimal("iquantity"+i);
				} catch (Exception e) {
					errorMsg.append("第").append(dataStartRowNum).append("行,").append(j).append("月数量不合法,请检查!<br/>");
				}
				try {
					iamount = row.getBigDecimal("iamount"+i);
				} catch (Exception e) {
					errorMsg.append("第").append(dataStartRowNum).append("行,").append(j).append("月金额不合法,请检查!<br/>");
				}
				//数量不为空的情况下，如果金额不为空，金额直接取excel的值，否则金额=数量*单价
				if(iquantity == null) continue;
				if(iamount == null) iamount = iquantity.multiply(expenseBudgetItem.getIprice());
				ExpenseBudgetItemd expenseBudgetItemd = new ExpenseBudgetItemd();
				long expenseBudgetItemdId = JBoltSnowflakeKit.me.nextId();
				expenseBudgetItemd.setIautoid(expenseBudgetItemdId);
				expenseBudgetItemd.setIexpenseitemid(expenseBudgetItemId);
				expenseBudgetItemd.setIyear(calendarStartDate.get(Calendar.YEAR));
				expenseBudgetItemd.setImonth(j);
				expenseBudgetItemd.setIquantity(iquantity);
				expenseBudgetItemd.setIamount(iamount);
				expenseBudgetItemd.setIcreateby(JBoltUserKit.getUserId());
				expenseBudgetItemd.setDcreatetime(now);
				expenseBudgetItemdList.add(expenseBudgetItemd);
			}
			alertErrorMsg.append(errorMsg);
			if(errorMsg.toString().length() > 0) continue;
			expenseBudgetItemList.add(expenseBudgetItem);
		}
	}*/
	/**
	 * 系统按照固定规则生成预算编号:科室-费用-年份-流水号  ,预算编号：ZW-EX-2022-001
	 * */
/*	public String genExpenseBudgetNo(String cdepcode, Integer iBudgetType){
		Record record = departmentService.findByCdepcode(cdepcode);
		String cdepnameen = record.getStr("cdepnameen");
		ValidationUtils.notBlank(cdepnameen, record.getStr("cdepname")+"部门的英文名称为空,生成预算编号失败!");
		return BillNoUtils.genExpenseBudgetNo(getOrgId(), cdepnameen+"-"+"EX"+"-"+iBudgetType+"-");
	}*/
	/**
	 * 系统根据编码规则配置生成预算编号:科室-费用-年份-流水号  ,预算编号：ZW-EX-2022-001
	 * */
	public String genExpenseBudgetNo(Kv para){
		String barcoade = barcodeencodingmService.genCode(para, ItemEnum.EXPENSE_BUDGET.getValue());
		return barcoade;
	}
	/**
	 * 编码规则获取单据内容：部门英文名称，单据年度等
	 * */
	public String getItemOrderContent(Long iautoid,String cprojectcode){
		String str = "";
		ExpenseBudget expenseBudget = findById(iautoid);
    	ValidationUtils.notNull(expenseBudget, "费用预算数据不存在,获取部门英文名称失败!");
		switch (BarCodeEnum.toEnum(cprojectcode)) {
	        case DEPT:
	        	Record record = departmentService.findByCdepcode(getOrgId(),expenseBudget.getCdepcode()).toRecord();
	    		String cdepnameen = record.getStr("cdepnameen");
	    		ValidationUtils.notBlank(cdepnameen, record.getStr("cdepname")+"部门的英文名称为空!");
	    		str = cdepnameen;
	            break;  
	        case ORDERYEAR:
	        	Integer iBudgetYear = expenseBudget.getIbudgetyear();
	        	ValidationUtils.notNull(iBudgetYear, BarCodeEnum.ORDERYEAR.getText()+"为空!");
	    		str = iBudgetYear.toString();
	            break;
	        default:
                break;
		}
		return str;
	}
	public void saveExpenseBudgetItemForSubmitTable(List<Record> listRecord,ExpenseBudget expenseBudget,Long userId,Date now){
		if(CollUtil.isEmpty(listRecord)) return;
		for (Record row : listRecord) {
			String cbudgetno = row.getStr("cbudgetno");
			cbudgetno = JBoltStringUtil.isBlank(cbudgetno) ? genExpenseBudgetNo(Kv.by("iautoid", expenseBudget.getIautoid())) : cbudgetno; 
			Long iexpenseBudgetItemId = JBoltSnowflakeKit.me.nextId();
			row.remove("chighestsubjectname","clowestsubjectname")
			.set("cbudgetno", cbudgetno)
			.set("icreateby", userId)
			.set("isscheduled", IsScheduledEnum.WITHIN_PLAN.getValue())
			.set("dcreatetime",now)
			.set("iexpenseid", expenseBudget.getIautoid())
			.set("iautoid", iexpenseBudgetItemId);
			ExpenseBudgetItem expenseBudgetItem = new ExpenseBudgetItem().put(row);
			List<ExpenseBudgetItemd> expenseBudgetItemdList = new ArrayList<>();
			// 转换费用预算项目明细
			LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(expenseBudget.getCbegindate(),JBoltDateUtil.YMD));
			LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(expenseBudget.getCenddate(),JBoltDateUtil.YMD));
			// 起止日期计算共多少个月
			long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
			BigDecimal iamountTotal = BigDecimal.ZERO; //金额合计
			BigDecimal iprice = row.getBigDecimal("iprice");
			for (int j = 1; j <= diffMonth; j++) {
				Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(expenseBudget.getCbegindate());
				calendarStartDate.add(Calendar.MONTH, j-1);
				int nowYear = calendarStartDate.get(Calendar.YEAR);
				int nowMonth = calendarStartDate.get(Calendar.MONTH)+1;
                BigDecimal iquantity = row.getBigDecimal("iquantity"+nowYear+nowMonth);
                BigDecimal iamount = row.getBigDecimal("iamount"+nowYear+nowMonth);
                //金额 ，数量，单价为0时  默认为null
				iquantity = iquantity != null && iquantity.compareTo(BigDecimal.ZERO) == 0 ? null:iquantity;
				iamount = iamount != null && iamount.compareTo(BigDecimal.ZERO) == 0 ? null:iamount;
				iprice = iprice != null && iprice.compareTo(BigDecimal.ZERO) == 0 ? null:iprice;
				//数量和金额为空不保存项目明细数据
				if(iamount == null && iquantity == null) continue;
				//金额不为空，录入的是以千元为单位需要转为元保存
				if(iamount != null) iamount = iamount.multiply(Constants.RATIO);
				//数量和单价都不为空的情况下金额=数量*单价
				if(iprice != null && iquantity != null) iamount = iquantity.multiply(iprice);
				ExpenseBudgetItemd expenseBudgetItemd = new ExpenseBudgetItemd();
				long expenseBudgetItemdId = JBoltSnowflakeKit.me.nextId();
				expenseBudgetItemd.setIautoid(expenseBudgetItemdId);
				expenseBudgetItemd.setIexpenseitemid(iexpenseBudgetItemId);
				expenseBudgetItemd.setIyear(nowYear);
				expenseBudgetItemd.setImonth(nowMonth);
				expenseBudgetItemd.setIquantity(iquantity);
				expenseBudgetItemd.setIamount(iamount);
				expenseBudgetItemd.setIcreateby(userId);
				expenseBudgetItemd.setDcreatetime(now);
				expenseBudgetItemdList.add(expenseBudgetItemd);
				if(iamount != null) iamountTotal = iamountTotal.add(iamount);
			}
			expenseBudgetItem.setIamounttotal(iamountTotal);
			ValidationUtils.isTrue(expenseBudgetItem.save(), ErrorMsg.SAVE_FAILED);
			expenseBudgetItemdService.batchSave(expenseBudgetItemdList);
		}
	}
	public Ret saveTableSubmitByAdd(JBoltTable jBoltTable) {
		ValidationUtils.notNull(jBoltTable, "参数不能为空");
		Date now = new Date();
		ExpenseBudget expenseBudget = jBoltTable.getFormModel(ExpenseBudget.class, "expenseBudget");
		String cdepcode = expenseBudget.getCdepcode();
		Integer iBudgetYear = expenseBudget.getIbudgetyear();
		Integer iBudgetType = expenseBudget.getIbudgettype();
		ValidationUtils.notNull(cdepcode, "预算部门不能为空");
		ValidationUtils.notNull(iBudgetYear, "预算年度不能为空");
		ValidationUtils.notNull(iBudgetType, "预算类型不能为空");
		tx(()->{
			ExpenseBudget expenseBudgetDB = findModelByYearAndType(iBudgetYear, iBudgetType,cdepcode);
			ValidationUtils.isTrue(expenseBudgetDB == null, "费用预算已创建，请重复操作!");
            Long iexpenseBudgetId = JBoltSnowflakeKit.me.nextId();
            Long userId = JBoltUserKit.getUserId();
            expenseBudget.setIorgid(getOrgId());
            expenseBudget.setCorgcode(getOrgCode());
            expenseBudget.setIcreateby(userId);
            expenseBudget.setDcreatetime(now);
            expenseBudget.setIautoid(iexpenseBudgetId);
            ValidationUtils.isTrue(expenseBudget.save(), ErrorMsg.SAVE_FAILED);
			List<Record> listRecord = jBoltTable.getSaveRecordList();
			if(CollUtil.isEmpty(listRecord)) return true;
			//ValidationUtils.notEmpty(listRecord, "预算项目不能为空");
			saveExpenseBudgetItemForSubmitTable(listRecord,expenseBudget,userId,now);
			return true;
		});
		return successWithData(expenseBudget.keep("iautoid"));
	}

	/**
     * 费用预算修改界面表格提交
     */
	public Ret saveTableByUpdate(JBoltTable jBoltTable) {
		ValidationUtils.notNull(jBoltTable, "参数不能为空");
		Date now = new Date();
		ExpenseBudget expenseBudgetForm = jBoltTable.getFormModel(ExpenseBudget.class, "expenseBudget");
		ExpenseBudget expenseBudget = findById(expenseBudgetForm.getIautoid());
		tx(()->{
			Long userId = JBoltUserKit.getUserId();
			//新增行
			List<Record> saveRecordList = jBoltTable.getSaveRecordList();
			saveExpenseBudgetItemForSubmitTable(saveRecordList,expenseBudget,JBoltUserKit.getUserId(),now);
			//更新行
			List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
			if(CollUtil.isNotEmpty(updateRecordList)){
				for (Record row : updateRecordList) {
					Long iexpenseBudgetItemId = row.getLong("iautoid");
					row.remove("chighestsubjectname","clowestsubjectname")
					.set("iupdateby", userId)
					.set("dupdatetime",now);
					ExpenseBudgetItem expenseBudgetItem = JBoltModelKit.getFromBean(ExpenseBudgetItem.class, JSONObject.parseObject(row.toJson()));
					List<ExpenseBudgetItemd> expenseBudgetItemdList = new ArrayList<ExpenseBudgetItemd>();
					//先删除原来的费用预算项目明细,再转换新的费用预算项目明细保存
					expenseBudgetItemdService.deleteByItemId(iexpenseBudgetItemId);
					LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(expenseBudget.getCbegindate(),JBoltDateUtil.YMD));
					LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(expenseBudget.getCenddate(),JBoltDateUtil.YMD));
					//起止日期计算共多少个月
					long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
					BigDecimal iamountTotal = BigDecimal.ZERO;
					for (int j = 1; j <= diffMonth; j++) {
						Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(expenseBudget.getCbegindate());
						calendarStartDate.add(Calendar.MONTH, j-1);
						int nowYear = calendarStartDate.get(Calendar.YEAR);
						int nowMonth = calendarStartDate.get(Calendar.MONTH)+1;
						BigDecimal iquantity = row.getBigDecimal("iquantity"+nowYear+nowMonth);
						BigDecimal iamount = row.getBigDecimal("iamount"+nowYear+nowMonth);
						BigDecimal iprice = row.getBigDecimal("iprice");
						//金额 ，数量，单价为0时  默认为null
						iquantity = iquantity != null && iquantity.compareTo(BigDecimal.ZERO) == 0 ? null:iquantity;
						iamount = iamount != null && iamount.compareTo(BigDecimal.ZERO) == 0 ? null:iamount;
						iprice = iprice != null && iprice.compareTo(BigDecimal.ZERO) == 0 ? null:iprice;
						//数量和金额为空，不保存项目明细数据
						if(iamount == null && iquantity == null) continue;
						//金额不为空，录入的是以千元为单位需要转为元保存
						if(iamount != null) iamount = iamount.multiply(Constants.RATIO);
						//数量和单价都不为空,金额=数量*单价
						if(iquantity != null && iprice != null) iamount = iquantity.multiply(iprice);
						ExpenseBudgetItemd expenseBudgetItemd = new ExpenseBudgetItemd();
						long expenseBudgetItemdId = JBoltSnowflakeKit.me.nextId();
						expenseBudgetItemd.setIautoid(expenseBudgetItemdId);
						expenseBudgetItemd.setIexpenseitemid(iexpenseBudgetItemId);
						expenseBudgetItemd.setIyear(nowYear);
						expenseBudgetItemd.setImonth(nowMonth);
						expenseBudgetItemd.setIquantity(iquantity);
						expenseBudgetItemd.setIamount(iamount);
						expenseBudgetItemd.setIcreateby(userId);
						expenseBudgetItemd.setDcreatetime(now);
						expenseBudgetItemdList.add(expenseBudgetItemd);
						if(iamount != null) iamountTotal = iamountTotal.add(iamount);
					}
					expenseBudgetItem.setIamounttotal(iamountTotal);
					expenseBudgetItemdService.batchSave(expenseBudgetItemdList);
					ValidationUtils.isTrue(expenseBudgetItem.update(), ErrorMsg.UPDATE_FAILED);
				}
			}
			//删除行:先删除项目明细，再删除项目
			Object[] ids = jBoltTable.getDelete();
			if(ArrayUtils.isNotEmpty(ids)){
				for (Object id : ids) {
					expenseBudgetItemdService.deleteByItemId(Long.parseLong(id.toString()));
					ExpenseBudgetItem expenseBudgetItem = expenseBudgetItemService.findById(id);
					ValidationUtils.isTrue(expenseBudgetItem.delete(), ErrorMsg.DELETE_FAILED);
				}
			}
			return true;
		});
		return successWithData(expenseBudget.keep("iautoid"));
	}
	/**
	 * 查询生效的费用预算
	 * */
	public ExpenseBudget findEffectivedExpenseBudgetByDeptCode(Kv para) {
		String cdepcode = para.getStr("cdepcode");
		ValidationUtils.notBlank(cdepcode,"部门为空，请检查!");
		return findFirst(selectSql().eq("cdepcode", cdepcode).eq("ieffectivestatus", EffectiveStatusEnum.EFFECTIVED.getValue()));
	}
	/**
     * 查询费用预算修改界面的项目数据
     * */
	public List<Record> findExpenseBudgetItemDatas(Long iexpenseid) {
		Kv para = Kv.by("iexpenseid",iexpenseid);
		ExpenseBudget expenseBudget = findById(iexpenseid);
		constructDynamicsDbColumn(expenseBudget,para);
		List<Record> list = dbTemplate("expensebudget.findExpenseBudgetItemDatas",para).find();
		fillExpenseBudgetItem(list);
		return list;
	}
	public void constructDynamicsDbColumn(ExpenseBudget expenseBudget,Kv para){
		LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(expenseBudget.getCbegindate(),JBoltDateUtil.YMD));
		LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(expenseBudget.getCenddate(),JBoltDateUtil.YMD));
		//起止日期计算共跨多少个月
		long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
		//起止日期计算共跨多少年
		int startYear = Integer.parseInt(JBoltDateUtil.format(expenseBudget.getCbegindate(),"YYYY"));
		int endYear = Integer.parseInt(JBoltDateUtil.format(expenseBudget.getCenddate(),"YYYY"));
		long diffYear = endYear - startYear + 1;
		List<String> monthList = new ArrayList<String>();
		for (int i=1; i<=diffMonth; i++) {
			Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(expenseBudget.getCbegindate());
			calendarStartDate.add(Calendar.MONTH, i-1);
			int nowYear = calendarStartDate.get(Calendar.YEAR);
			int nowMonth = calendarStartDate.get(Calendar.MONTH)+1;
			monthList.add(""+nowYear+nowMonth);
		}
		List<Integer> yearList = new ArrayList<Integer>();
		for (int i=0; i<diffYear ; i++) {
			yearList.add(startYear+i);
		}
		para.set("monthlist",monthList);
		para.set("yearlist",yearList);
	}
	private void fillExpenseBudgetItem(List<Record> list){
		if(CollUtil.isNotEmpty(list)){
			for (Record row : list) {
				row.set("careertypename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("careertype")));
			}
		}
	}
	/**
	 * 查询部门的上期预算类型主表数据:
	 * 	1.如果是全年预算，导入上个预算年度的实绩预测的未完成项目
	 * 	2.如果是下半年修订，导入同年份的全年预算的未完成项目
	 * 
	 *  Para:
	 *  	部门编码:cdepcode
	 *  	预算类型:ibudgettype
	 *  	预算年份:ibudgetyear
	 * */
	public ExpenseBudget findPreviousPeriodExpenseBudget(Kv para) {
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
		ExpenseBudget unfinishItemExpenseBudget = findFirst(selectSql().eq("ibudgettype", ibudgettypeNew).eq("ibudgetyear", ibudgetyearNew).eq("cdepcode", cdepcode)
				.eq("iorgid", getOrgId()));
		return unfinishItemExpenseBudget;
	}
	/**
     * 导入未完成项目界面查询费用预算项目数据
     * */
	public List<Record> findUnfinishExpenseBudgetItemDatas(Long iexpenseid) {
		Kv para = Kv.by("iexpenseid",iexpenseid).set("iservicetype",ServiceTypeEnum.EXPENSE_BUDGET.getValue())
				.set("istatus",FinishStatusEnum.UNFINISHED.getValue()).set("iorgid",getOrgId());
		constructDynamicsDbColumn(findById(iexpenseid),para);
		List<Record> list = dbTemplate(u8SourceConfigName(),"expensebudget.findUnfinishExpenseBudgetItemDatas",para).find();
		fillExpenseBudgetItem(list);
		return list;
	}
	/**
     * 导入上期项目界面查询费用预算项目数据
     * */
	public List<Record> findPreviousPeriodExpenseBudgetItemDatas(Long iexpenseid) {
		Kv para = Kv.by("iexpenseid",iexpenseid).set("iservicetype",ServiceTypeEnum.EXPENSE_BUDGET.getValue())
				.set("istatus",FinishStatusEnum.UNFINISHED.getValue()).set("iorgid",getOrgId());
		List<Record> list = dbTemplate("expensebudget.findPreviousPeriodExpenseBudgetItemDatas",para).find();
		fillExpenseBudgetItem(list);
		return list;
	}
	/**
     * 提交审核
     * */
	public Ret submitAudit(Long iexpenseid) {
		Date now = new Date();
		ValidationUtils.notNull(iexpenseid, "请先保存后再提交");
		ExpenseBudget expenseBudget = findById(iexpenseid);
		if(AppConfig.isVerifyProgressEnabled()){
			//verifyprogressService.start(iexpenseid.toString(),getOrgCode(),VeriProgressObjTypeEnum.EXPENSE_BUDGET,JBoltUserKit.getUser(),null,expenseBudget.getCdepcode(),now);
			expenseBudget.setIauditstatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
			expenseBudget.setDaudittime(now);
			ValidationUtils.isTrue(expenseBudget.update(), ErrorMsg.UPDATE_FAILED);
		}else{
			expenseBudget.setIauditstatus(AuditStatusEnum.APPROVED.getValue());
			expenseBudget.setDaudittime(now);
			ValidationUtils.isTrue(expenseBudget.update(), ErrorMsg.UPDATE_FAILED);
		}
		return SUCCESS;
	}

	public Page<Record> periodContrastDatas(int pageNumber, int pageSize, Kv para) {
		ExpenseBudget expenseBudget = new ExpenseBudget();
    	String dstarttime1 = para.getStr("dstarttime1");
    	String dendtime1 = para.getStr("dendtime1");
    	Date dstarttime1Date = JBoltDateUtil.toDate(dstarttime1,"yyyy-MM");
    	Date dendtime1Date = JBoltDateUtil.toDate(dendtime1,"yyyy-MM");
		expenseBudget.setCbegindate(dstarttime1Date);
		expenseBudget.setCenddate(dendtime1Date);
		constructDynamicsDbColumn(expenseBudget,para);
		Page<Record> list1 = dbTemplate(u8SourceConfigName(),"expensebudget.periodContrastDatas", para).paginate(pageNumber, pageSize);
		return list1;
	}
	
	/**
	 *增加行
	 */
	private List<Record> contrastScheme(List<Record> list,int state) {
		List<Record> recordArrayList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			recordArrayList.add(list.get(i));
			if (i>0){
				//两个期间对比
				if (state==0) {
					if (list.get(i).getStr("cdepname").equals(list.get(i-1).getStr("cdepname"))
							&&list.get(i).getStr("clowestsubjectname").equals(list.get(i-1).getStr("clowestsubjectname"))
							&&list.get(i).getStr("ibudgettype").equals(list.get(i-1).getStr("ibudgettype"))){
						Record record = new Record();
						record.set("ibudgettype",5);
						record.set("cdepname",list.get(i).getStr("cdepname"));
						record.set("clowestsubjectname",list.get(i).getStr("clowestsubjectname"));
						record.set("chighestsubjectname",list.get(i).getStr("chighestsubjectname"));
						record.set("previousyearmounthamount8",list.get(i).getBigDecimal("previousyearmounthamount8").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount8")));
						record.set("previousyearmounthamount9",list.get(i).getBigDecimal("previousyearmounthamount9").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount9")));
						record.set("previousyearmounthamount10",list.get(i).getBigDecimal("previousyearmounthamount10").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount10")));
						record.set("previousyearmounthamount11",list.get(i).getBigDecimal("previousyearmounthamount11").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount11")));
						record.set("previousyearmounthamount12",list.get(i).getBigDecimal("previousyearmounthamount12").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount12")));
						record.set("nowyearmounthamount1",list.get(i).getBigDecimal("nowyearmounthamount1").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount1")));
						record.set("nowyearmounthamount2",list.get(i).getBigDecimal("nowyearmounthamount2").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount2")));
						record.set("nowyearmounthamount3",list.get(i).getBigDecimal("nowyearmounthamount3").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount3")));
						record.set("nowyearmounthamount4",list.get(i).getBigDecimal("nowyearmounthamount4").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount4")));
						record.set("nowyearmounthamount5",list.get(i).getBigDecimal("nowyearmounthamount5").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount5")));
						record.set("nowyearmounthamount6",list.get(i).getBigDecimal("nowyearmounthamount6").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount6")));
						record.set("nowyearmounthamount7",list.get(i).getBigDecimal("nowyearmounthamount7").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount7")));
						record.set("nowyearmounthamount8",list.get(i).getBigDecimal("nowyearmounthamount8").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount8")));
						record.set("nowyearmounthamount9",list.get(i).getBigDecimal("nowyearmounthamount9").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount9")));
						record.set("nowyearmounthamount10",list.get(i).getBigDecimal("nowyearmounthamount10").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount10")));
						record.set("nowyearmounthamount11",list.get(i).getBigDecimal("nowyearmounthamount11").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount11")));
						record.set("nowyearmounthamount12",list.get(i).getBigDecimal("nowyearmounthamount12").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount12")));
						record.set("nextyearmounthamount1",list.get(i).getBigDecimal("nextyearmounthamount1").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount1")));
						record.set("nextyearmounthamount2",list.get(i).getBigDecimal("nextyearmounthamount2").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount2")));
						record.set("nextyearmounthamount3",list.get(i).getBigDecimal("nextyearmounthamount3").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount3")));
						record.set("nextyearmounthamount4",list.get(i).getBigDecimal("nextyearmounthamount4").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount4")));
						record.set("totalamount1",list.get(i).getBigDecimal("totalamount1").subtract(list.get(i-1).getBigDecimal("totalamount1")));
						record.set("totalamount2",list.get(i).getBigDecimal("totalamount2").subtract(list.get(i-1).getBigDecimal("totalamount2")));
						recordArrayList.add(record);
					}
				}
		}
			if (i>1){
				//三个期间对比
				if (state==1){
					if (list.get(i).getStr("cdepname").equals(list.get(i-1).getStr("cdepname"))
							&&list.get(i).getStr("cdepname").equals(list.get(i-2).getStr("cdepname"))
							&&list.get(i).getStr("clowestsubjectname").equals(list.get(i-1).getStr("clowestsubjectname"))
							&&list.get(i).getStr("clowestsubjectname").equals(list.get(i-2).getStr("clowestsubjectname"))
							&&list.get(i).getStr("ibudgettype").equals(list.get(i-1).getStr("ibudgettype"))
							&&list.get(i).getStr("ibudgettype").equals(list.get(i-2).getStr("ibudgettype"))){
						Record record = new Record();
						Record record2 = new Record();
						record.set("ibudgettype",6);
						record.set("cdepname",list.get(i).getStr("cdepname"));
						record.set("clowestsubjectname",list.get(i).getStr("clowestsubjectname"));
						record.set("chighestsubjectname",list.get(i).getStr("chighestsubjectname"));
						record.set("previousyearmounthamount8",list.get(i).getBigDecimal("previousyearmounthamount8").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount8")));
						record.set("previousyearmounthamount9",list.get(i).getBigDecimal("previousyearmounthamount9").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount9")));
						record.set("previousyearmounthamount10",list.get(i).getBigDecimal("previousyearmounthamount10").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount10")));
						record.set("previousyearmounthamount11",list.get(i).getBigDecimal("previousyearmounthamount11").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount11")));
						record.set("previousyearmounthamount12",list.get(i).getBigDecimal("previousyearmounthamount12").subtract(list.get(i-1).getBigDecimal("previousyearmounthamount12")));
						record.set("nowyearmounthamount1",list.get(i).getBigDecimal("nowyearmounthamount1").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount1")));
						record.set("nowyearmounthamount2",list.get(i).getBigDecimal("nowyearmounthamount2").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount2")));
						record.set("nowyearmounthamount3",list.get(i).getBigDecimal("nowyearmounthamount3").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount3")));
						record.set("nowyearmounthamount4",list.get(i).getBigDecimal("nowyearmounthamount4").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount4")));
						record.set("nowyearmounthamount5",list.get(i).getBigDecimal("nowyearmounthamount5").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount5")));
						record.set("nowyearmounthamount6",list.get(i).getBigDecimal("nowyearmounthamount6").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount6")));
						record.set("nowyearmounthamount7",list.get(i).getBigDecimal("nowyearmounthamount7").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount7")));
						record.set("nowyearmounthamount8",list.get(i).getBigDecimal("nowyearmounthamount8").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount8")));
						record.set("nowyearmounthamount9",list.get(i).getBigDecimal("nowyearmounthamount9").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount9")));
						record.set("nowyearmounthamount10",list.get(i).getBigDecimal("nowyearmounthamount10").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount10")));
						record.set("nowyearmounthamount11",list.get(i).getBigDecimal("nowyearmounthamount11").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount11")));
						record.set("nowyearmounthamount12",list.get(i).getBigDecimal("nowyearmounthamount12").subtract(list.get(i-1).getBigDecimal("nowyearmounthamount12")));
						record.set("nextyearmounthamount1",list.get(i).getBigDecimal("nextyearmounthamount1").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount1")));
						record.set("nextyearmounthamount2",list.get(i).getBigDecimal("nextyearmounthamount2").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount2")));
						record.set("nextyearmounthamount3",list.get(i).getBigDecimal("nextyearmounthamount3").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount3")));
						record.set("nextyearmounthamount4",list.get(i).getBigDecimal("nextyearmounthamount4").subtract(list.get(i-1).getBigDecimal("nextyearmounthamount4")));
						record.set("totalamount1",list.get(i).getBigDecimal("totalamount1").subtract(list.get(i-1).getBigDecimal("totalamount1")));
						record.set("totalamount2",list.get(i).getBigDecimal("totalamount2").subtract(list.get(i-1).getBigDecimal("totalamount2")));
						recordArrayList.add(record);

						record2.set("ibudgettype",7);
						record2.set("cdepname",list.get(i).getStr("cdepname"));
						record2.set("clowestsubjectname",list.get(i).getStr("clowestsubjectname"));
						record2.set("chighestsubjectname",list.get(i).getStr("chighestsubjectname"));
						record2.set("previousyearmounthamount8",list.get(i).getBigDecimal("previousyearmounthamount8").subtract(list.get(i-2).getBigDecimal("previousyearmounthamount8")));
						record2.set("previousyearmounthamount9",list.get(i).getBigDecimal("previousyearmounthamount9").subtract(list.get(i-2).getBigDecimal("previousyearmounthamount9")));
						record2.set("previousyearmounthamount10",list.get(i).getBigDecimal("previousyearmounthamount10").subtract(list.get(i-2).getBigDecimal("previousyearmounthamount10")));
						record2.set("previousyearmounthamount11",list.get(i).getBigDecimal("previousyearmounthamount11").subtract(list.get(i-2).getBigDecimal("previousyearmounthamount11")));
						record2.set("previousyearmounthamount12",list.get(i).getBigDecimal("previousyearmounthamount12").subtract(list.get(i-2).getBigDecimal("previousyearmounthamount12")));
						record2.set("nowyearmounthamount1",list.get(i).getBigDecimal("nowyearmounthamount1").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount1")));
						record2.set("nowyearmounthamount2",list.get(i).getBigDecimal("nowyearmounthamount2").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount2")));
						record2.set("nowyearmounthamount3",list.get(i).getBigDecimal("nowyearmounthamount3").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount3")));
						record2.set("nowyearmounthamount4",list.get(i).getBigDecimal("nowyearmounthamount4").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount4")));
						record2.set("nowyearmounthamount5",list.get(i).getBigDecimal("nowyearmounthamount5").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount5")));
						record2.set("nowyearmounthamount6",list.get(i).getBigDecimal("nowyearmounthamount6").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount6")));
						record2.set("nowyearmounthamount7",list.get(i).getBigDecimal("nowyearmounthamount7").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount7")));
						record2.set("nowyearmounthamount8",list.get(i).getBigDecimal("nowyearmounthamount8").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount8")));
						record2.set("nowyearmounthamount9",list.get(i).getBigDecimal("nowyearmounthamount9").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount9")));
						record2.set("nowyearmounthamount10",list.get(i).getBigDecimal("nowyearmounthamount10").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount10")));
						record2.set("nowyearmounthamount11",list.get(i).getBigDecimal("nowyearmounthamount11").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount11")));
						record2.set("nowyearmounthamount12",list.get(i).getBigDecimal("nowyearmounthamount12").subtract(list.get(i-2).getBigDecimal("nowyearmounthamount12")));
						record2.set("nextyearmounthamount1",list.get(i).getBigDecimal("nextyearmounthamount1").subtract(list.get(i-2).getBigDecimal("nextyearmounthamount1")));
						record2.set("nextyearmounthamount2",list.get(i).getBigDecimal("nextyearmounthamount2").subtract(list.get(i-2).getBigDecimal("nextyearmounthamount2")));
						record2.set("nextyearmounthamount3",list.get(i).getBigDecimal("nextyearmounthamount3").subtract(list.get(i-2).getBigDecimal("nextyearmounthamount3")));
						record2.set("nextyearmounthamount4",list.get(i).getBigDecimal("nextyearmounthamount4").subtract(list.get(i-2).getBigDecimal("nextyearmounthamount4")));
						record2.set("totalamount1",list.get(i).getBigDecimal("totalamount1").subtract(list.get(i-2).getBigDecimal("totalamount1")));
						record2.set("totalamount2",list.get(i).getBigDecimal("totalamount2").subtract(list.get(i-2).getBigDecimal("totalamount2")));
						recordArrayList.add(record2);
					}
				}
			}
	 }
		return recordArrayList;
	}
	/**
	 * 期间对比导出数据源
	 */
	public List<Record> periodContrastTplDatas(Kv para){
		//处理日期
		para.set("dstarttime1",para.getStr("dstarttime1") ==null ? null:para.getStr("dstarttime1")+"-01");
		para.set("dendtime1",para.getStr("dendtime1") ==null ? null:para.getStr("dendtime1")+"-01");
		para.set("dstarttime2",para.getStr("dstarttime2") ==null ? null:para.getStr("dstarttime2")+"-01");
		para.set("dendtime2",para.getStr("dendtime2") ==null ? null:para.getStr("dendtime2")+"-01");
		para.set("dstarttime3",para.getStr("dstarttime3") ==null ? null:para.getStr("dstarttime3")+"-01");
		para.set("dendtime3",para.getStr("dendtime3") ==null ? null:para.getStr("dendtime3")+"-01");

		List<Record> list = dbTemplate("expensebudget.periodContrastDatas", para).find();
		for (Record row : list) {
			row.set("cusername", JBoltUserCache.me.getUserName(row.getLong("icreateby")));
			row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
			//row.set("beginandenddate", JBoltDateUtil.format(row.getDate("cbegindate"), "yyyy.MM")+"-" + JBoltDateUtil.format(row.getDate("cenddate"), "yyyy.MM"));
			//row.set("careertypename",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("careertype")));
			//计算合计列
			BigDecimal nowyearmounthquantity1 = row.getBigDecimal("nowyearmounthquantity1") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity1");
			BigDecimal nowyearmounthquantity2 = row.getBigDecimal("nowyearmounthquantity2") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity2");
			BigDecimal nowyearmounthquantity3 = row.getBigDecimal("nowyearmounthquantity3") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity3");
			BigDecimal nowyearmounthquantity4 = row.getBigDecimal("nowyearmounthquantity4") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity4");
			BigDecimal nowyearmounthquantity5 = row.getBigDecimal("nowyearmounthquantity5") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity5");
			BigDecimal nowyearmounthquantity6 = row.getBigDecimal("nowyearmounthquantity6") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity6");
			BigDecimal nowyearmounthquantity7 = row.getBigDecimal("nowyearmounthquantity7") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity7");
			BigDecimal nowyearmounthquantity8 = row.getBigDecimal("nowyearmounthquantity8") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity8");
			BigDecimal nowyearmounthquantity9 = row.getBigDecimal("nowyearmounthquantity9") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity9");
			BigDecimal nowyearmounthquantity10 = row.getBigDecimal("nowyearmounthquantity10") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity10");
			BigDecimal nowyearmounthquantity11 = row.getBigDecimal("nowyearmounthquantity11") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity11");
			BigDecimal nowyearmounthquantity12 = row.getBigDecimal("nowyearmounthquantity12") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity12");
			BigDecimal totalquantity1 = nowyearmounthquantity1.add(nowyearmounthquantity2).add(nowyearmounthquantity3).add(nowyearmounthquantity4).add(nowyearmounthquantity5)
					.add(nowyearmounthquantity6).add(nowyearmounthquantity7).add(nowyearmounthquantity8).add(nowyearmounthquantity9).add(nowyearmounthquantity10)
					.add(nowyearmounthquantity11).add(nowyearmounthquantity12);
			row.set("totalquantity1", totalquantity1);
			BigDecimal nowyearmounthamount1 = row.getBigDecimal("nowyearmounthamount1") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount1");
			BigDecimal nowyearmounthamount2 = row.getBigDecimal("nowyearmounthamount2") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount2");
			BigDecimal nowyearmounthamount3 = row.getBigDecimal("nowyearmounthamount3") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount3");
			BigDecimal nowyearmounthamount4 = row.getBigDecimal("nowyearmounthamount4") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount4");
			BigDecimal nowyearmounthamount5 = row.getBigDecimal("nowyearmounthamount5") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount5");
			BigDecimal nowyearmounthamount6 = row.getBigDecimal("nowyearmounthamount6") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount6");
			BigDecimal nowyearmounthamount7 = row.getBigDecimal("nowyearmounthamount7") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount7");
			BigDecimal nowyearmounthamount8 = row.getBigDecimal("nowyearmounthamount8") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount8");
			BigDecimal nowyearmounthamount9 = row.getBigDecimal("nowyearmounthamount9") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount9");
			BigDecimal nowyearmounthamount10 = row.getBigDecimal("nowyearmounthamount10") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount10");
			BigDecimal nowyearmounthamount11 = row.getBigDecimal("nowyearmounthamount11") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount11");
			BigDecimal nowyearmounthamount12 = row.getBigDecimal("nowyearmounthamount12") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount12");
			BigDecimal totalamount1 = nowyearmounthamount1.add(nowyearmounthamount2).add(nowyearmounthamount3).add(nowyearmounthamount4).add(nowyearmounthamount5)
					.add(nowyearmounthamount6).add(nowyearmounthamount7).add(nowyearmounthamount8).add(nowyearmounthamount9).add(nowyearmounthamount10)
					.add(nowyearmounthamount11).add(nowyearmounthamount12);
			row.set("totalamount1", totalamount1);
			BigDecimal nextyearmounthquantity1 = row.getBigDecimal("nextyearmounthquantity1") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity1");
			BigDecimal nextyearmounthquantity2 = row.getBigDecimal("nextyearmounthquantity2") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity2");
			BigDecimal nextyearmounthquantity3 = row.getBigDecimal("nextyearmounthquantity3") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity3");
			BigDecimal nextyearmounthamount1 = row.getBigDecimal("nextyearmounthamount1") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity1");
			BigDecimal nextyearmounthamount2 = row.getBigDecimal("nextyearmounthamount2") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity2");
			BigDecimal nextyearmounthamount3 = row.getBigDecimal("nextyearmounthamount3") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity3");
			BigDecimal nextyearmounthamount4 = row.getBigDecimal("nextyearmounthamount4") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity4");
			row.set("nextyearmounthamount1",nextyearmounthamount1);
			row.set("nextyearmounthamount2",nextyearmounthamount2);
			row.set("nextyearmounthamount3",nextyearmounthamount3);
			row.set("nextyearmounthamount4",nextyearmounthamount4);
			BigDecimal totalquantity2 = nowyearmounthquantity4.add(nowyearmounthquantity5).add(nowyearmounthquantity6).add(nowyearmounthquantity7).add(nowyearmounthquantity8)
					.add(nowyearmounthquantity9).add(nowyearmounthquantity10).add(nowyearmounthquantity11).add(nowyearmounthquantity12).add(nextyearmounthquantity1)
					.add(nextyearmounthquantity2).add(nextyearmounthquantity3);
			BigDecimal totalamount2 = nowyearmounthamount4.add(nowyearmounthamount5).add(nowyearmounthamount6).add(nowyearmounthamount7).add(nowyearmounthamount8)
					.add(nowyearmounthamount9).add(nowyearmounthamount10).add(nowyearmounthamount11).add(nowyearmounthamount12).add(nextyearmounthamount1)
					.add(nextyearmounthamount2).add(nextyearmounthamount3);
			row.set("totalquantity2", totalquantity2);
			row.set("totalamount2", totalamount2);
		}
		//判断是两个对比期间还是三个
		List<Record> list1 = new ArrayList<>();
		if (para.getStr("dstarttime1")!=null&&para.getStr("dendtime1")!=null
				&&para.getStr("dstarttime2")!=null&&para.getStr("dendtime2")!=null
				&&para.getStr("dstarttime3")==null&&para.getStr("dendtime3")==null){
			list1 = contrastScheme(list,0);
		}
		if (para.getStr("dstarttime1")!=null&&para.getStr("dendtime1")!=null
				&&para.getStr("dstarttime2")!=null&&para.getStr("dendtime2")!=null
				&&para.getStr("dstarttime3")!=null&&para.getStr("dendtime3")!=null){
			list1 = contrastScheme(list,1);
		}
	 return list1;
	}
	/**
	 * 费用预算导出：费用预算编制列表-导出按钮，费用预算编辑修改界面-导出按钮
	 * 	说明：
	 * 		1.输出费用预算主表数据：部门名称，年份，预算类型，起止日期
	 * 		2.根据期间起止日期动态生成表头
	 * 		3.输出list数据
	 * 	params: 
	 * 	expenseBudget: 费用预算主表
	 * 	excelPositionDatas:  excel定位数据集合
	 * 	mergeList:	excel定位合并数据集合
	 * 	list:	导出到excel的数据
	 * */
	public void exportExpenseBudgetExcel(ExpenseBudget expenseBudget, List<JBoltExcelPositionData> excelPositionDatas,List<JBoltExcelMerge> mergeList,List<Record> list) {
		String cdepcode = expenseBudget.getCdepcode();
    	Integer iBudgetYear = expenseBudget.getIbudgetyear();
    	Integer iBudgetType = expenseBudget.getIbudgettype();
    	Date startDate = expenseBudget.getCbegindate();
        Date cutoffTime = expenseBudget.getCenddate();
        LocalDate startLocalDate = LocalDate.parse(JBoltDateUtil.format(startDate, JBoltDateUtil.YMD));
        LocalDate cutoffLocalDate = LocalDate.parse(JBoltDateUtil.format(cutoffTime, JBoltDateUtil.YMD));
        Calendar calendarStartDate1 = JBoltDateUtil.getCalendarByDate(startDate);
        Calendar calendarCutoffTimetDate1 = JBoltDateUtil.getCalendarByDate(cutoffTime);
        long diffMonth = ChronoUnit.MONTHS.between(startLocalDate, cutoffLocalDate) + 1;
        String cdepname = departmentService.getCdepName(cdepcode);
        //下载模板中动态设置预算期间的起止年份、预算类型、预算年度、部门、表头的年份
        excelPositionDatas.add(JBoltExcelPositionData.create(4, 5, calendarStartDate1.get(Calendar.YEAR) + "年"));
        excelPositionDatas.add(JBoltExcelPositionData.create(4, 6, calendarStartDate1.get(Calendar.MONTH) + 1 + "月"));
        excelPositionDatas.add(JBoltExcelPositionData.create(5, 5, calendarCutoffTimetDate1.get(Calendar.YEAR) + "年"));
        excelPositionDatas.add(JBoltExcelPositionData.create(5, 6, calendarCutoffTimetDate1.get(Calendar.MONTH) + 1 + "月"));
        excelPositionDatas.add(JBoltExcelPositionData.create(4, 7, "部门：" + cdepname));
        excelPositionDatas.add(JBoltExcelPositionData.create(4, 10, iBudgetYear + "年"));
        excelPositionDatas.add(JBoltExcelPositionData.create(5, 7, "预算类型："+ExpenseBudgetTypeEnum.toEnum(iBudgetType).getText()));
        //设置动态表头部分
        int yearForMonthAmount = 0;
        //添加的合计列数量
        int totalColumnNum = 0;
        for (int i = 0; i < diffMonth; i++) {
            excelPositionDatas.add(JBoltExcelPositionData.create(8, 13 + i * 2+totalColumnNum, "数量"));
            excelPositionDatas.add(JBoltExcelPositionData.create(8, 14 + i * 2+totalColumnNum, "金额"));
            Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(startDate);
            Calendar calendarCutoffTimetDate = JBoltDateUtil.getCalendarByDate(cutoffTime);
            calendarStartDate.add(Calendar.MONTH, i);
            mergeList.add(JBoltExcelMerge.create(7, 7, 13 + i * 2+totalColumnNum, 14 + i * 2+totalColumnNum, calendarStartDate.get(Calendar.MONTH) + 1 + "月", true));
            if (calendarStartDate.get(Calendar.MONTH) + 1 == 12 ||
                    (calendarStartDate.get(Calendar.YEAR) == calendarCutoffTimetDate.get(Calendar.YEAR) &&
                            calendarStartDate.get(Calendar.MONTH) == calendarCutoffTimetDate.get(Calendar.MONTH))) {
                mergeList.add(JBoltExcelMerge.create(6, 6, 13 + yearForMonthAmount * 2+totalColumnNum, 14 + i * 2+totalColumnNum, calendarStartDate.get(Calendar.YEAR) + "年", true));
                totalColumnNum++;
                mergeList.add(JBoltExcelMerge.create(6, 8, 14 + i * 2+totalColumnNum, 14 + i * 2+totalColumnNum, "合计", true));
                yearForMonthAmount = i + 1;
            }
        }
        for (int i=0;i<list.size();i++) {
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 2, i+1));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 3, list.get(i).getStr("cbudgetno")));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 4, list.get(i).getStr("chighestsubjectname")));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 5, list.get(i).getStr("clowestsubjectname")));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 6, list.get(i).getStr("citemname")));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 7, list.get(i).getStr("careertypename")));
        	Integer islargetamountexpense = list.get(i).getInt("islargeamountexpense");
        	String islargeamountexpenseText = islargetamountexpense == null ? null : IsEnableEnum.toEnum(islargetamountexpense).getText();
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 8, islargeamountexpenseText));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 9, list.get(i).getStr("cuse")));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 10, list.get(i).getStr("cmemo")));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 11, list.get(i).getBigDecimal("iprice")));
        	excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 12, list.get(i).getStr("cunit")));
        	//添加的合计列数量
        	int totalColumnNumOther = 0;
        	for (int j=0; j < diffMonth; j++) {
        		Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(startDate);
                Calendar calendarCutoffTimetDate = JBoltDateUtil.getCalendarByDate(cutoffTime);
                calendarStartDate.add(Calendar.MONTH, j);
        		int nowYear = calendarStartDate.get(Calendar.YEAR);
        		int nowMonth = calendarStartDate.get(Calendar.MONTH)+1;
        		excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 13+j*2+totalColumnNumOther, list.get(i).getBigDecimal("iquantity"+nowYear+nowMonth)));
        		excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 14+j*2+totalColumnNumOther, list.get(i).getBigDecimal("iamount"+nowYear+nowMonth)));
        		if (calendarStartDate.get(Calendar.MONTH) + 1 == 12 ||
                        (calendarStartDate.get(Calendar.YEAR) == calendarCutoffTimetDate.get(Calendar.YEAR) &&
                                calendarStartDate.get(Calendar.MONTH) == calendarCutoffTimetDate.get(Calendar.MONTH))) {
        			totalColumnNumOther++;
        			excelPositionDatas.add(JBoltExcelPositionData.create(9+i, 14+j*2+totalColumnNumOther, list.get(i).getBigDecimal("itotal"+nowYear)));
        		}
			}
		}		
	}
	/**
	 * 是否存在已生效的部门预算
	 * */
	public Boolean isExistsEffectivedExpenseBudget(ExpenseBudget expenseBudget) {
		List<ExpenseBudget> list = find(selectSql().eq("cdepcode", expenseBudget.getCdepcode()).eq("iBudgetYear", expenseBudget.getIbudgetyear())
				.eq("iBudgetType", expenseBudget.getIbudgettype()).eq("iEffectiveStatus", EffectiveStatusEnum.EFFECTIVED.getValue()));
		if(CollUtil.isNotEmpty(list)) return true;
		return false;
	}
	
	/**
	 * 根据起止日期计算费用预算期间对比报表的表头
	 * */	
	public void calcDynamicPeriodContrastColumn(Date dstarttime, Date dendtime,List<Record> monthColumnTxtList) {
		int colspan = 0;
        Date dendTimeElse = JBoltDateUtil.toDate(JBoltDateUtil.format(dendtime, "yyyy-MM"),"yyyy-MM");
        while(JBoltDateUtil.toDate(JBoltDateUtil.format(dstarttime, "yyyy-MM"),"yyyy-MM").compareTo(dendTimeElse) != 1){
        	colspan++;
        	int dstarttimemonth = Integer.parseInt(JBoltDateUtil.format(dstarttime, JBoltDateUtil.MM));
        	Record monthRecord = new Record();
        	monthRecord.set("colname", dstarttimemonth + "月");
        	monthRecord.set("datacolumn", "imonthamount"+colspan);
        	monthColumnTxtList.add(monthRecord);
        	dstarttime = JBoltDateUtil.nextMonthFirstDay(dstarttime);
        }
	}
	/**
	 * 期间是否应用于费用预算
	 * */
	public Boolean periodIsExists(Period dbPeriod) {
		List<ExpenseBudget> list = find(selectSql().eq("iPeriodId", dbPeriod.getIautoid()));
		return CollUtil.isNotEmpty(list);
	}
}
