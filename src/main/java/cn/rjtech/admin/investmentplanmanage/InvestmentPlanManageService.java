package cn.rjtech.admin.investmentplanmanage;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.admin.projectcard.ProjectCardService;
import cn.rjtech.admin.proposalm.ProposalmService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.EffectiveStatusEnum;
import cn.rjtech.enums.FinishStatusEnum;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.InvestmentPlan;
import cn.rjtech.model.momdata.InvestmentPlanItem;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

public class InvestmentPlanManageService extends BaseService<InvestmentPlan>{
	private final InvestmentPlan dao = new InvestmentPlan().dao();

	@Inject
	private DepartmentService departmentService;
	@Inject
	private InvestmentPlanItemService investmentPlanItemService;
	@Inject
	private ProjectCardService projectCardService;
	@Inject
	private InvestmentPlanService investmentPlanService;
	@Inject
	private ProposalmService proposalmService;
	
	@Override
	protected InvestmentPlan dao() {
		return dao;
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
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param para
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		User user = JBoltUserKit.getUser();
        //系统管理员 || 存在权限部门
	    para.set("isSystemAdmin", user.getIsSystemAdmin())
	    	.set("iorgid",getOrgId());
		Page<Record> page = dbTemplate("investmentplanmanage.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : page.getList()) {
			row.set("cusername", JBoltUserCache.me.getName(row.getLong("icreateby")));
			row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
		}
		return page;
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
		return investmentPlanService.findInvestmentPlanItemDatas(investmentPlanId);
	}

	/**
     *	投资计划生效 
     * */
	public Ret effect(Long iplanid) {
		ValidationUtils.notNull(iplanid, "投资计划ID为空");
		InvestmentPlan investmentPlan = findById(iplanid);
		Date now = new Date();
		ValidationUtils.isTrue(investmentPlan.getIAuditStatus() == AuditStatusEnum.APPROVED.getValue(), "请选择已审核的投资计划进行生效操作!");
		ValidationUtils.isTrue(investmentPlan.getIEffectiveStatus() == EffectiveStatusEnum.INVAILD.getValue()
				|| investmentPlan.getIEffectiveStatus() == EffectiveStatusEnum.CANCLE.getValue()
				, "请选择未生效或已作废的投资计划进行生效操作!");
		ValidationUtils.isTrue(!investmentPlanService.isExistsEffectivedInvestment(investmentPlan), "已存在生效部门，请检查!");
		tx(()->{
			List<InvestmentPlanItem> list = investmentPlanItemService.findNotExistsProjectCardInvestmentItem(iplanid);
			if(CollUtil.isNotEmpty(list)){
				for (InvestmentPlanItem investmentPlanItem : list) {
					projectCardService.contructModelAndSave(ServiceTypeEnum.INVESTMENT_PLAN.getValue(), investmentPlanItem, FinishStatusEnum.UNFINISHED.getValue(),now);
				}
			}
			InvestmentPlan previousPeriodInvestmentPlan = investmentPlanService.findPreviousPeriodInvestmentPlan(Kv.by("cdepcode", investmentPlan.getCDepCode())
					.set("ibudgettype",investmentPlan.getIBudgetType())
					.set("ibudgetyear",investmentPlan.getIBudgetYear()));
			if(previousPeriodInvestmentPlan!=null){
				previousPeriodInvestmentPlan.setIUpdateBy(JBoltUserKit.getUserId());
				previousPeriodInvestmentPlan.setDUpdateTime(now);
				previousPeriodInvestmentPlan.setIEffectiveStatus(EffectiveStatusEnum.EXPIRED.getValue());
				ValidationUtils.isTrue(previousPeriodInvestmentPlan.update(), ErrorMsg.UPDATE_FAILED);
			}
			investmentPlan.setIUpdateBy(JBoltUserKit.getUserId());
			investmentPlan.setDUpdateTime(now);
			investmentPlan.setIEffectiveStatus(EffectiveStatusEnum.EFFECTIVED.getValue());
			ValidationUtils.isTrue(investmentPlan.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	/**
     *	投资计划作废
     * */
	public Ret cancle(Long iplanid) {
		ValidationUtils.notNull(iplanid, "投资计划ID为空");
		InvestmentPlan investmentPlan = findById(iplanid);
		Date now = new Date();
		ValidationUtils.isTrue(investmentPlan.getIEffectiveStatus() == EffectiveStatusEnum.EFFECTIVED.getValue(), "请选择已生效的投资计划进行作废操作!");
		List<Record> list = proposalmService.findProposalDatasByPlanId(iplanid);
		if(CollUtil.isNotEmpty(list)){
			String cproposalNos = "";
			for (Record record : list) {
				cproposalNos += record.getStr("cproposalno") + ",";
			}
			cproposalNos = cproposalNos.substring(0, cproposalNos.lastIndexOf(","));
			ValidationUtils.error( "投资计划已发生禀议，不能作废，请删除后流程单据后继续操作，禀议书编号为"+cproposalNos);
		}
		
		tx(()->{
			investmentPlan.setIUpdateBy(JBoltUserKit.getUserId());
			investmentPlan.setDUpdateTime(now);
			investmentPlan.setIEffectiveStatus(EffectiveStatusEnum.CANCLE.getValue());
			ValidationUtils.isTrue(investmentPlan.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	
	/**
	 * 失效：1.存在下游单据不能失效
	 * 	2.单据状态从已生效变更到未生效
	 * */
	public Ret uneffect(Long iplanid) {
		tx(()->{
			InvestmentPlan investmentPlan = findById(iplanid);
			Integer ieffectivestatus = investmentPlan.getIEffectiveStatus();
			ValidationUtils.isTrue(ieffectivestatus == EffectiveStatusEnum.EFFECTIVED.getValue(), "请操作已生效的单据!");
			ValidationUtils.isTrue(!isExistsProposalDatas(iplanid), "存在禀议数据，不能失效");
			investmentPlan.setIEffectiveStatus(EffectiveStatusEnum.INVAILD.getValue());
			ValidationUtils.isTrue(investmentPlan.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	private boolean isExistsProposalDatas(Long iplanid){
		int count = dbTemplate("investmentplanmanage.isExistsProposalDatas",Kv.by("iplanid", iplanid)).queryInt();
		return count > 0 ? true : false;
	}
}
