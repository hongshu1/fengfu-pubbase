package cn.rjtech.admin.expensebudgetmanage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.expensebudgetitemd.ExpenseBudgetItemdService;
import cn.rjtech.admin.projectcard.ProjectCardService;
import cn.rjtech.admin.proposalm.ProposalmService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.EffectiveStatusEnum;
import cn.rjtech.enums.FinishStatusEnum;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.model.momdata.ExpenseBudgetItemd;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * @author Heming
 */
public class ExpenseBudgetManageService extends BaseService<ExpenseBudget>{

	@Inject
	private DepartmentService departmentService;
	@Inject
	private ExpenseBudgetItemService expenseBudgetItemService;
	@Inject
	private ExpenseBudgetItemdService expenseBudgetItemdService;
	@Inject
	private ProjectCardService projectCardService;
	@Inject
	private ExpenseBudgetService expenseBudgetService;
	@Inject
	private ProposalmService proposalmService;

	private final ExpenseBudget dao = new ExpenseBudget().dao();

	@Override
	protected ExpenseBudget dao() {
		return dao;
	}

	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		User user = JBoltUserKit.getUser();
        // 系统管理员 || 存在权限部门
        para.set("isSystemAdmin", user.getIsSystemAdmin())
        	.set("iorgid",getOrgId());
		Page<Record> pageRecord =  dbTemplate("expensebudgetmanage.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : pageRecord.getList()) {
			row.set("cusername",JBoltUserCache.me.getName(row.getLong("icreateby")));
			row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
		}
		return pageRecord;
	}

	/**
	 * 查询详情界面费用预算主数据
	 */
	public Record findExpenseBudgetData(Long iautoid) {
		Record record = findFirstRecord(selectSql().eq("iautoid", iautoid));
		record.set("cdepname", departmentService.getCdepName(record.getStr("cdepcode")));
		return record;
	}

	/**
	 * 删除 指定多个ID :
	 * 	1.删除主表数据
	 *  2.删除费用预算项目
	 * 	3.删除费用预算项目明细数据
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				ExpenseBudget expenseBudget = findById(iAutoId);
				ValidationUtils.notNull(expenseBudget, JBoltMsg.DATA_NOT_EXIST);
				List<ExpenseBudgetItem> expenseBudgetItemList = expenseBudgetItemService.findByMainId(expenseBudget.getIAutoId());
				for (ExpenseBudgetItem expenseBudgetItem : expenseBudgetItemList) {
					List<ExpenseBudgetItemd> expenseBudgetItemdList =  expenseBudgetItemdService.findByItemId(expenseBudgetItem.getIautoid());
					for (ExpenseBudgetItemd expenseBudgetItemd : expenseBudgetItemdList) {
						ValidationUtils.isTrue(expenseBudgetItemd.delete(), ErrorMsg.DELETE_FAILED);
					}
					ValidationUtils.isTrue(expenseBudgetItem.delete(), ErrorMsg.DELETE_FAILED);
				}
				expenseBudget.delete();
			}
			return true;
		});

		// 添加日志
		// ExpenseBudget expenseBudget = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, expenseBudget.getName());
		return SUCCESS;
	}
	/**
	 * 费用预算生效：
	 * 	1.查询未生成项目卡片的预算项目生成项目卡片
	 * 	2.将整个部门上期预算类型失效
	 * */
	public Ret effect(Long iexpenseid) {
		ValidationUtils.notNull(iexpenseid, "费用预算ID为空");
		ExpenseBudget expenseBudget = findById(iexpenseid);
		Date now = new Date();
		ValidationUtils.isTrue(expenseBudget.getIAuditStatus() == AuditStatusEnum.APPROVED.getValue(), "请选择已审核的费用预算进行生效操作!");
		ValidationUtils.isTrue(expenseBudget.getIEffectiveStatus() == EffectiveStatusEnum.INVAILD.getValue()
				||expenseBudget.getIEffectiveStatus() == EffectiveStatusEnum.CANCLE.getValue()
				, "请选择未生效或已作废的费用预算进行生效操作!");
		ValidationUtils.isTrue(!expenseBudgetService.isExistsEffectivedExpenseBudget(expenseBudget), "已存在生效部门，请检查!");
		tx(()->{
			List<ExpenseBudgetItem> list = expenseBudgetItemService.findNotExistsProjectCardExpenseBudgetItem(iexpenseid);
			if(CollUtil.isNotEmpty(list)){
				for (ExpenseBudgetItem expenseBudgetItem : list) {
					projectCardService.contructModelAndSave(ServiceTypeEnum.EXPENSE_BUDGET.getValue(), expenseBudgetItem, FinishStatusEnum.UNFINISHED.getValue(),now);
				}
			}
			ExpenseBudget previousPeriodExpenseBudget = expenseBudgetService.findPreviousPeriodExpenseBudget(Kv.by("cdepcode", expenseBudget.getCDepCode())
					.set("ibudgettype",expenseBudget.getIBudgetType()).set("ibudgetyear",expenseBudget.getIBudgetYear()));
			if(previousPeriodExpenseBudget != null){
				previousPeriodExpenseBudget.setIUpdateBy(JBoltUserKit.getUserId());
				previousPeriodExpenseBudget.setDUpdateTime(now);
				previousPeriodExpenseBudget.setIEffectiveStatus(EffectiveStatusEnum.EXPIRED.getValue());
				ValidationUtils.isTrue(previousPeriodExpenseBudget.update(), ErrorMsg.UPDATE_FAILED);
			}
			expenseBudget.setIEffectiveStatus(EffectiveStatusEnum.EFFECTIVED.getValue());
			expenseBudget.setIUpdateBy(JBoltUserKit.getUserId());
			expenseBudget.setDUpdateTime(now);
			ValidationUtils.isTrue(expenseBudget.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	/**
	 * 费用预算失效：
	 * 	1.校验是否存在禀议书单据，存在则提示不能作废
	 * 	2.将整个部门费用预算失效
	 * */
	public Ret cancle(Long iexpenseid) {
		ValidationUtils.notNull(iexpenseid, "费用预算ID为空");
		ExpenseBudget expenseBudget = findById(iexpenseid);
		Date now = new Date();
		ValidationUtils.isTrue(expenseBudget.getIEffectiveStatus() == EffectiveStatusEnum.EFFECTIVED.getValue(), "请选择已生效的费用预算进行作废操作!");
		List<Record> list = proposalmService.findProposalDatasByExpenseId(iexpenseid);
		if(CollUtil.isNotEmpty(list)) {
			String cproposalNos = "";
			for (Record record : list) {
				cproposalNos += record.getStr("cproposalno") + ",";
			}
			cproposalNos = cproposalNos.substring(0,cproposalNos.lastIndexOf(","));
			ValidationUtils.error( "费用预算已发生禀议，不能作废，请删除后流程单据后继续操作，禀议书编号为"+cproposalNos);
		}
		tx(()->{
			expenseBudget.setIEffectiveStatus(EffectiveStatusEnum.CANCLE.getValue());
			expenseBudget.setIUpdateBy(JBoltUserKit.getUserId());
			expenseBudget.setDUpdateTime(now);
			ValidationUtils.isTrue(expenseBudget.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}

}
