package cn.rjtech.admin.appenditem;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.DataPermissionKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodeencodingm.BarcodeencodingmService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.admin.projectcard.ProjectCardService;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 追加项目 Service
 * @ClassName: AppendItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-12-07 17:43
 */
public class AppendItemService extends BaseService<AppendItem> {

	@Inject
	private DepartmentService departmentService;
	@Inject
	private ProjectCardService projectCardService;
	@Inject
	private ExpenseBudgetService expenseBudgetService;
	@Inject
	private InvestmentPlanService investmentPlanService;
	@Inject
	private ExpenseBudgetItemService expenseBudgetItemService;
	@Inject
	private InvestmentPlanItemService investmentPlanItemService;
	@Inject
	private BarcodeencodingmService barcodeencodingmService;
	@Inject
	private GlobalConfigService globalConfigService;
	
	private final AppendItem dao = new AppendItem().dao();

	@Override
	protected AppendItem dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		para.set("iorgid",getOrgId());
		Page<Record> pageList = dbTemplate("appenditem.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : pageList.getList()) {
			row.set("cusername", JBoltUserCache.me.getName(row.getLong("icreateby")));
			row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
		}
		return pageList;
	}
	/**
	 * 保存和修改前数据的校验
	 * */
	public void formCheck(AppendItem appendItem){
		Integer isScheduled = appendItem.getIsscheduled();
		ValidationUtils.notNull(isScheduled, "计划类型不能为空!");
		BigDecimal iBudgetMoney = appendItem.getIbudgetmoney();
		//计划类型为提前实施的追加项目的预算总金额必须大于0
		if(isScheduled == IsScheduledEnum.PREVIOUS_PLAN.getValue() && (iBudgetMoney == null || iBudgetMoney.compareTo(BigDecimal.ZERO)!=1)){
			ValidationUtils.error( "提前实施的追加项目的预算总金额必须大于0!");
		}
	}
	/**
	 * 保存
	 * @param appendItem
	 * @return
	 */
	public Ret save(AppendItem appendItem) {
		if(appendItem==null || isOk(appendItem.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		formCheck(appendItem);
		DataPermissionKit.validateAccess(appendItem.getCdepcode());
		appendItem.setIorgid(getOrgId());
		appendItem.setCorgcode(getOrgCode());
		BigDecimal iBudgetMoney = appendItem.getIbudgetmoney() == null ? BigDecimal.ZERO:appendItem.getIbudgetmoney();
		appendItem.setIbudgetmoney(iBudgetMoney.multiply(Constants.RATIO));
		appendItem.setIcreateby(JBoltUserKit.getUserId());
		appendItem.setDcreatetime(new Date());
		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(appendItem.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(appendItem.getIautoid(), JBoltUserKit.getUserId(), appendItem.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(AppendItem appendItem) {
		if(appendItem==null || notOk(appendItem.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		formCheck(appendItem);
		DataPermissionKit.validateAccess(appendItem.getCdepcode());
		tx(() -> {
			// 更新时需要判断数据存在
			AppendItem dbAppendItem = findById(appendItem.getIautoid());
			ValidationUtils.notNull(dbAppendItem, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			appendItem.setIbudgetmoney(appendItem.getIbudgetmoney().multiply(Constants.RATIO));
			ValidationUtils.isTrue(appendItem.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});

		//添加日志
		//addUpdateSystemLog(appendItem.getIautoid(), JBoltUserKit.getUserId(), appendItem.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				AppendItem dbAppendItem = findById(iAutoId);
				ValidationUtils.isTrue(dbAppendItem.getIeffectivestatus() == EffectiveStatusEnum.INVAILD.getValue(), "请删除未生效的数据!");
				ValidationUtils.notNull(dbAppendItem, JBoltMsg.DATA_NOT_EXIST);
				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用
				ValidationUtils.isTrue(dbAppendItem.delete(), JBoltMsg.FAIL);
			}
			return true;
		});

		// 添加日志
		// AppendItem appendItem = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, appendItem.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param appendItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(AppendItem appendItem, Kv kv) {
		//addDeleteSystemLog(appendItem.getIautoid(), JBoltUserKit.getUserId(),appendItem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param appendItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(AppendItem appendItem, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(appendItem, kv);
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
	 * 切换islargeamountexpense属性
	 */
	public Ret toggleIslargeamountexpense(Long id) {
		return toggleBoolean(id, "isLargeAmountExpense");
	}

	/**
	 * 切换isimport属性
	 */
	public Ret toggleIsimport(Long id) {
		return toggleBoolean(id, "isImport");
	}

	/**
	 * 切换ispriorreport属性
	 */
	public Ret toggleIspriorreport(Long id) {
		return toggleBoolean(id, "isPriorReport");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param appendItem 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(AppendItem appendItem,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(AppendItem appendItem, String column, Kv kv) {
		//addUpdateSystemLog(appendItem.getIautoid(), JBoltUserKit.getUserId(), appendItem.getName(),"的字段["+column+"]值:"+appendItem.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param appendItem model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(AppendItem appendItem, Kv kv) {
		//这里用来覆盖 检测AppendItem是否被其它表引用
		return null;
	}

	public Record findForEditById(Long iautoid) {
		return dbTemplate("appenditem.findForEditById",Kv.by("iautoid", iautoid)).findFirst();
	}
	/**
	 * 生成追加项目-费用预算的预算编号
	 * */
	public String genBudgetNo(ExpenseBudget expenseBudget){
		//采用编码规则配置生成
		String barcoade = barcodeencodingmService.genCode(Kv.by("iautoid", expenseBudget.getIAutoId()), ItemEnum.EXPENSE_BUDGET_APPENDITEM.getValue());
		return barcoade;
	}
	/**
	 * 生成追加项目-投资计划的预算编号
	 * */
	public String genBudgetNo(InvestmentPlan investmentPlan,String iInvestmentType){
		//采用编码规则配置生成
		String barcoade = barcodeencodingmService.genCode(Kv.by("iautoid", investmentPlan.getIAutoId()).set("iinvestmenttype",iInvestmentType), ItemEnum.INVESTMENT_PLAN_APPENDITEM.getValue());
		return barcoade;
	}
	/**
	 * 追加项目生效:
	 * 	1.生成费用预算项目数据或者投资计划项目数据
	 *  2.生成项目卡片
	 * 	2.更新生效状态,更新时间，更新人
	 * */
	public Ret effect(Long iautoid) {
		ValidationUtils.notNull(iautoid, "ID为空");
		AppendItem appendItem = findById(iautoid);
		Date now = new Date();
		ValidationUtils.isTrue(appendItem.getIeffectivestatus() == EffectiveStatusEnum.INVAILD.getValue(), "请选择未生效的数据进行生效操作!");
		tx(()->{
			String cdepcode = appendItem.getCdepcode();
			String ccode = null;
			if(appendItem.getIservicetype() == ServiceTypeEnum.EXPENSE_BUDGET.getValue()){
				ExpenseBudgetItem expenseBudgetItem = new ExpenseBudgetItem();
				ExpenseBudget expenseBudget = expenseBudgetService.findEffectivedExpenseBudgetByDeptCode(Kv.by("cdepcode", cdepcode));
				ValidationUtils.notNull(expenseBudget, "勾选数据未能查询到对应部门生效的预算数据,请检查!");
				expenseBudgetItem.setIexpenseid(expenseBudget.getIAutoId());
				ccode = genBudgetNo(expenseBudget);
				expenseBudgetItem.setCbudgetno(ccode);
				expenseBudgetItem.setIhighestsubjectid(appendItem.getIhighestsubjectid());
				expenseBudgetItem.setIlowestsubjectid(appendItem.getIlowestsubjectid());
				expenseBudgetItem.setCareertype(appendItem.getIcareertype());
				expenseBudgetItem.setCitemname(appendItem.getCitemname());
				expenseBudgetItem.setIslargeamountexpense(appendItem.getIslargeamountexpense());
				expenseBudgetItem.setCuse(appendItem.getCuse());
				expenseBudgetItem.setCmemo(appendItem.getCmemo());
				expenseBudgetItem.setIsscheduled(appendItem.getIsscheduled());
				expenseBudgetItem.setIamounttotal(appendItem.getIbudgetmoney());
				expenseBudgetItem.setIcreateby(appendItem.getIcreateby());
				expenseBudgetItem.setDcreatetime(appendItem.getDcreatetime());
				ValidationUtils.isTrue(expenseBudgetItem.save(), ErrorMsg.SAVE_FAILED);
				projectCardService.contructModelAndSave(appendItem.getIservicetype(), expenseBudgetItem, FinishStatusEnum.UNFINISHED.getValue(),now);
			}else{
				InvestmentPlanItem investmentPlanItem = new InvestmentPlanItem();
				InvestmentPlan investmentPlan = investmentPlanService.findEffectivedInvestmentByDeptCode(Kv.by("cdepcode", cdepcode));
				ValidationUtils.notNull(investmentPlan, "勾选数据未能查询到对应部门生效的预算数据,请检查!");
				investmentPlanItem.setIplanid(investmentPlan.getIAutoId());
				ccode = genBudgetNo(investmentPlan,appendItem.getIinvestmenttype());
				investmentPlanItem.setCplanno(ccode);
				investmentPlanItem.setIinvestmenttype(appendItem.getIinvestmenttype());
				investmentPlanItem.setCproductline(appendItem.getCproductline());
				investmentPlanItem.setCmodelinvccode(appendItem.getCmodelinvccode());
				investmentPlanItem.setCparts(appendItem.getCparts());
				investmentPlanItem.setIcareertype(appendItem.getIcareertype());
				investmentPlanItem.setIinvestmentdistinction(appendItem.getIinvestmentdistinction());
				investmentPlanItem.setCitemname(appendItem.getCitemname());
				investmentPlanItem.setIsimport(appendItem.getIsimport());
				investmentPlanItem.setIquantity(appendItem.getIquantity());
				investmentPlanItem.setCunit(appendItem.getCunit());
				investmentPlanItem.setCassettype(appendItem.getCassettype());
				investmentPlanItem.setCpurpose(appendItem.getCpurpose());
				investmentPlanItem.setCeffectamount(appendItem.getCeffectamount());
				investmentPlanItem.setItaxrate(globalConfigService.getTaxRate());//追加投资计划项目的税率从全局参数中获取
				investmentPlanItem.setCreclaimyear(appendItem.getCreclaimyear());
				investmentPlanItem.setClevel(appendItem.getClevel());
				investmentPlanItem.setIspriorreport(appendItem.getIspriorreport());
				investmentPlanItem.setCpaymentprogress(appendItem.getCpaymentprogress());
				investmentPlanItem.setCmemo(appendItem.getCmemo());
				investmentPlanItem.setIsscheduled(appendItem.getIsscheduled());
				investmentPlanItem.setIamounttotal(appendItem.getIbudgetmoney());
				investmentPlanItem.setIcreateby(appendItem.getIcreateby());
				investmentPlanItem.setDcreatetime(appendItem.getDcreatetime());
				ValidationUtils.isTrue(investmentPlanItem.save(), ErrorMsg.SAVE_FAILED);
				projectCardService.contructModelAndSave(appendItem.getIservicetype(), investmentPlanItem, FinishStatusEnum.UNFINISHED.getValue(),now);
			}
			appendItem.setCcode(ccode);
			appendItem.setIeffectivestatus(EffectiveStatusEnum.EFFECTIVED.getValue());
			appendItem.setIupdateby(JBoltUserKit.getUserId());
			appendItem.setDupdatetime(new Date());
			ValidationUtils.isTrue(appendItem.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	/**
	 * 追加项目撤销:
	 * 	1.删除费用预算项目数据或者投资计划项目数据
	 *  2.删除项目卡片
	 * 	2.更新生效状态,更新时间，更新人
	 * */
	public Ret unEffect(Long iautoid) {
		ValidationUtils.notNull(iautoid, "ID为空");
		AppendItem appendItem = findById(iautoid);
		ValidationUtils.isTrue(appendItem.getIeffectivestatus() == EffectiveStatusEnum.EFFECTIVED.getValue(), "请选择已生效的数据进行撤销操作!");
		tx(()->{
			if(appendItem.getIservicetype() == ServiceTypeEnum.EXPENSE_BUDGET.getValue()){
				expenseBudgetItemService.deleteByCbudgetNo(appendItem.getCcode());
			}else if(appendItem.getIservicetype() == ServiceTypeEnum.INVESTMENT_PLAN.getValue()){
				investmentPlanItemService.deleteByCplanNo(appendItem.getCcode());
			}
			projectCardService.deleteByServicetypeAndCode(appendItem.getIservicetype(),appendItem.getCcode());
			appendItem.setCcode(null);
			appendItem.setIeffectivestatus(EffectiveStatusEnum.INVAILD.getValue());
			appendItem.setIupdateby(JBoltUserKit.getUserId());
			appendItem.setDupdatetime(new Date());
			ValidationUtils.isTrue(appendItem.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
}