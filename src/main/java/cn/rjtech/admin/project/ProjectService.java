package cn.rjtech.admin.project;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodeencodingm.BarcodeencodingmService;
import cn.rjtech.admin.proposalm.ProposalmService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.model.momdata.Project;
import cn.rjtech.model.momdata.Proposalm;
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
 * 项目档案 Service
 * @ClassName: ProjectService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 11:45
 */
public class ProjectService extends BaseService<Project> {

	private final Project dao = new Project().dao();

	@Override
	protected Project dao() {
		return dao;
	}
	@Inject
	private  UserService userService;
	@Inject
	private ExpenseBudgetItem expenseBudgetItem;
	@Inject
	private BarcodeencodingmService barcodeencodingmService;
	@Inject
	private ProposalmService proposalmService;
	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		para.set("iorgid",getOrgId());
		Page<Record> paginate = dbTemplate("project.paginateAdminDatas", para).paginate(pageNumber, pageSize);
		for (Record record : paginate.getList()) {
			record.set("icreateby",JBoltUserCache.me.getUserName(record.getLong("icreateby")));
		}
		return paginate;
	}


	/**
	 * 保存
	 * @return
	 */
	public Ret save(Project project) {
		if(project==null || isOk(project.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		project.setIcreateby(JBoltUserKit.getUserId());
		project.setDcreatetime(new Date());
		project.setIorgid(getOrgId());
		project.setCorgcode(getOrgCode());
		//project.setCprojectcode(barcodeencodingmService.genCode(null,null,null,project.getCprojectcode(), ItemEnum.PROJECT.getValue()));
		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(project.save(), ErrorMsg.SAVE_FAILED);
			// TODO 其他业务代码实现
			return true;
		});

		// 添加日志
		// addSaveSystemLog(project.getIautoid(), JBoltUserKit.getUserId(), project.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(Project project) {
		if(project==null || notOk(project.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		project.setIupdateby(JBoltUserKit.getUserId());
		project.setDupdatetime(new Date());
		tx(() -> {
			// 更新时需要判断数据存在
			Project dbProject = findById(project.getIautoid());
			ValidationUtils.notNull(dbProject, JBoltMsg.DATA_NOT_EXIST);
			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(project.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});

		//添加日志
		//addUpdateSystemLog(project.getIautoid(), JBoltUserKit.getUserId(), project.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long IAutoId = Long.parseLong(idStr);
				//查询是否有使用的项目
				Project dbProject = findById(IAutoId);
				ValidationUtils.notNull(dbProject, JBoltMsg.DATA_NOT_EXIST);
				List<Proposalm> list = proposalmService.findByProjectCode(dbProject.getCprojectcode());
				ValidationUtils.isTrue(CollUtil.isEmpty(list),"该项目在禀议书中使用,不可删除");
				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用
				ValidationUtils.isTrue(dbProject.delete(), JBoltMsg.FAIL);
			}
			return true;
		});
		// 添加日志
		// Project project = ret.getAs("data");
		// addDeleteSystemLog(IAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, project.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param project 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Project project, Kv kv) {
		//addDeleteSystemLog(project.getIautoid(), JBoltUserKit.getUserId(),project.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param project 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Project project, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(project, kv);
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
	 * 查询u8存货编码
	 */
	public Page<Record> u8InventoryList(int pageNumber, int pageSize, Kv kv){
		return dbTemplate(u8SourceConfigName(), "project.u8InventoryList", kv).paginate(pageNumber, pageSize);
	}
	/**
	/*
	 * 根据部门编码查询部门英文名称
	 */
	//public  List<Record> u8SelectByCoding(String cDepcode){
	//	return dbTemplate(u8SourceConfigName(), "project.u8DepartmentList", Kv.by("cDepcode", cDepcode)).find();
	//}

	/**
	 * 根据项目编码,部门编码查询项目档案
	 * */
	public Project findByProjectCode(String cprojectcode,String cdepcode) {
		return findFirst(selectSql().eq("cprojectcode", cprojectcode).eq("cdepcode", cdepcode));
	}

	public List<Record> getAutocompleteList(String keyword, Integer limit) {
		return dbTemplate("project.getAutocompleteList",Kv.by("keyword", keyword).set("limit",limit)).find();
	}

}