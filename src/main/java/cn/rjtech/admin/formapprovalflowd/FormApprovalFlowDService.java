package cn.rjtech.admin.formapprovalflowd;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.FormApprovalFlowD;
import cn.rjtech.model.momdata.Operation;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 单据审批流 Service
 * @ClassName: FormApprovalFlowDService
 * @author: RJ
 * @date: 2023-05-05 10:18
 */
public class FormApprovalFlowDService extends BaseService<FormApprovalFlowD> {

	private final FormApprovalFlowD dao = new FormApprovalFlowD().dao();

	@Override
	protected FormApprovalFlowD dao() {
		return dao;
	}

	@Inject
	private FormApprovalService formApprovalService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<FormApprovalFlowD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param formApprovalFlowD
	 * @return
	 */
	public Ret save(FormApprovalFlowD formApprovalFlowD) {
		if(formApprovalFlowD==null || isOk(formApprovalFlowD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formApprovalFlowD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formApprovalFlowD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formApprovalFlowD.getIautoid(), JBoltUserKit.getUserId(), formApprovalFlowD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formApprovalFlowD
	 * @return
	 */
	public Ret update(FormApprovalFlowD formApprovalFlowD) {
		if(formApprovalFlowD==null || notOk(formApprovalFlowD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormApprovalFlowD dbFormApprovalFlowD=findById(formApprovalFlowD.getIAutoId());
		if(dbFormApprovalFlowD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formApprovalFlowD.getName(), formApprovalFlowD.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formApprovalFlowD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formApprovalFlowD.getIautoid(), JBoltUserKit.getUserId(), formApprovalFlowD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param formApprovalFlowD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormApprovalFlowD formApprovalFlowD, Kv kv) {
		//addDeleteSystemLog(formApprovalFlowD.getIautoid(), JBoltUserKit.getUserId(),formApprovalFlowD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formApprovalFlowD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(FormApprovalFlowD formApprovalFlowD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formApprovalFlowD, kv);
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
	 * 根据节点ID与人员ID删除流程从表数据
	 * @param Did
	 * @param userIds
	 */
	public void deleteByMidAndUserId(Long Did, String userIds){
		for (String id : StrSplitter.split(userIds, COMMA, true, true)) {
			delete("delete\n" +
					"from Bd_FormApprovalFlowD\n" +
					"where iFormApprovalFlowMid = (select t1.iAutoId\n" +
					"                              from Bd_FormApprovalFlowM t1\n" +
					"                              where iApprovalDid = '"+Did+"')\n" +
					"  and iUserId = '"+id+"'");
		}
	}

	/**
	 * 根据节点ID与角色ID找出用户 删除流程从表数据
	 * @param Did
	 * @param roleIds
	 */
	public void deleteByMidAndRoleId(Long Did, String roleIds){
		for (String id : StrSplitter.split(roleIds, COMMA, true, true)) {

			List<User> users = formApprovalService.getRoles(Long.valueOf(id));

			if (users.size() > 0) {
				List<String> userIds = new ArrayList<>();
				users.forEach(u -> {
					userIds.add(u.getId().toString());
				});

				String join = String.join(",", userIds);
				deleteByMidAndUserId(Did, join);
			}
		}
	}

	/**
	 * 根据主表ID 删除流程从表数据
	 * @param Mid
	 */
	public void deleteByMid(Long Mid){
		delete("delete from Bd_FormApprovalFlowD where iFormApprovalFlowMid = "+Mid);
	}
}
