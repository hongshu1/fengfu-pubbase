package cn.rjtech.admin.projectcard;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.FinishStatusEnum;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.model.momdata.InvestmentPlanItem;
import cn.rjtech.model.momdata.ProjectCard;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.Optional;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 项目卡片 Service
 * @ClassName: ProjectCardService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-09 11:11
 */
public class ProjectCardService extends BaseService<ProjectCard> {

	private final ProjectCard dao = new ProjectCard().dao();

	@Override
	protected ProjectCard dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ProjectCard> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iautoid","desc", pageNumber, pageSize, keywords, "name");
	}

	/**
	 * 保存
	 * @param projectCard
	 * @return
	 */
	public Ret save(ProjectCard projectCard) {
		if(projectCard==null || isOk(projectCard.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(projectCard.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(projectCard.getIautoid(), JBoltUserKit.getUserId(), projectCard.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(ProjectCard projectCard) {
		if(projectCard==null || notOk(projectCard.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			ProjectCard dbProjectCard = findById(projectCard.getIautoid());
			ValidationUtils.notNull(dbProjectCard, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(projectCard.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(projectCard.getIautoid(), JBoltUserKit.getUserId(), projectCard.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				ProjectCard dbProjectCard = findById(iAutoId);
				ValidationUtils.notNull(dbProjectCard, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbProjectCard.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// ProjectCard projectCard = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, projectCard.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param projectCard 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(ProjectCard projectCard, Kv kv) {
		//addDeleteSystemLog(projectCard.getIautoid(), JBoltUserKit.getUserId(),projectCard.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param projectCard 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(ProjectCard projectCard, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(projectCard, kv);
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
	 * 生成项目卡片,并且反写费用预算项目的项目卡片ID
	 * */
	public void contructModelAndSave(Integer serviceType,ExpenseBudgetItem expenseBudgetItem, Integer finishStatus,Date now){
		ProjectCard projectCard = new ProjectCard();
		projectCard.setIorgid(getOrgId())
					.setCorgcode(getOrgCode())
					.setIservicetype(serviceType)
					.setCcode(expenseBudgetItem.getCbudgetno())
					.setIstatus(finishStatus)
					.setIcreateby(JBoltUserKit.getUserId())
					.setDcreatetime(now)
					.setIsfreeze(false);
		ValidationUtils.isTrue(projectCard.save(), ErrorMsg.SAVE_FAILED);
		expenseBudgetItem.setIprojectcardid(projectCard.getIautoid());
		expenseBudgetItem.setIupdateby(JBoltUserKit.getUserId());
		expenseBudgetItem.setDupdatetime(now);
		ValidationUtils.isTrue(expenseBudgetItem.update(), ErrorMsg.UPDATE_FAILED);
	}
	
	/**
	 * 生成项目卡片,并且反写费用预算项目的项目卡片ID
	 * */
	public void contructModelAndSave(Integer serviceType,InvestmentPlanItem investmentPlanItem, Integer finishStatus,Date now){
		ProjectCard projectCard = new ProjectCard();
		projectCard.setIorgid(getOrgId())
					.setCorgcode(getOrgCode())
					.setIservicetype(serviceType)
					.setCcode(investmentPlanItem.getCplanno())
					.setIstatus(finishStatus)
					.setIcreateby(JBoltUserKit.getUserId())
					.setDcreatetime(now)
					.setIsfreeze(false);
		ValidationUtils.isTrue(projectCard.save(), ErrorMsg.SAVE_FAILED);
		investmentPlanItem.setIprojectcardid(projectCard.getIautoid());
		investmentPlanItem.setIupdateby(JBoltUserKit.getUserId());
		investmentPlanItem.setDupdatetime(now);
		ValidationUtils.isTrue(investmentPlanItem.update(), ErrorMsg.UPDATE_FAILED);
	}

	public void deleteByServicetypeAndCode(Integer iservicetype, String ccode) {
		delete(deleteSql().eq("ccode", ccode).eq("iservicetype", iservicetype));
	}

	public Long findIdByCode(String cCode) {
		ProjectCard projectCard = findFirst(selectSql().eq("cCode", cCode));
		return Optional.ofNullable(projectCard).map(ProjectCard::getIautoid).orElse(null);
	}
	/**
	 * 切换是否冻结
	 * */
	public Ret toggleIsfreeze(Long iautoid) {
		return toggleBoolean(iautoid, "isfreeze");
	}
	/**
	 * 切换是否冻结
	 * @param istatus 
	 * */
	public Ret toggleIsFinish(String ccode, Integer istatus) {
		Integer setStatusValue = istatus == FinishStatusEnum.FINISHED.getValue() ?  FinishStatusEnum.UNFINISHED.getValue() : FinishStatusEnum.FINISHED.getValue();
		update(updateSql().set("istatus", setStatusValue).eq("ccode", ccode));
		return SUCCESS;
	}
}