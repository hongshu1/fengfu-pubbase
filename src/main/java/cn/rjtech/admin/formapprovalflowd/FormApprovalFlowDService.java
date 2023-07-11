package cn.rjtech.admin.formapprovalflowd;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormApprovalFlowD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

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

	/**
	 * 后台管理分页查询
	 */
	public Page<FormApprovalFlowD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
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
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

    /**
     * 删除数据后执行的回调
     *
     * @param formApprovalFlowD 要删除的model
     * @param kv                携带额外参数一般用不上
     */
	@Override
	protected String afterDelete(FormApprovalFlowD formApprovalFlowD, Kv kv) {
		//addDeleteSystemLog(formApprovalFlowD.getIautoid(), JBoltUserKit.getUserId(),formApprovalFlowD.getName());
		return null;
	}

    /**
     * 检测是否可以删除
     *
     * @param formApprovalFlowD 要删除的model
     * @param kv                携带额外参数一般用不上
     */
	@Override
	public String checkCanDelete(FormApprovalFlowD formApprovalFlowD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formApprovalFlowD, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 根据节点ID与单据人员主键ID删除流程从表数据
	 */
	public Integer deleteByMidAndUserId(Long Did, String userIds){
		int res = 0;
		for (String id : StrSplitter.split(userIds, COMMA, true, true)) {
			res = delete("delete\n" +
					"from Bd_FormApprovalFlowD\n" +
					"where iFormApprovalFlowMid = (select t1.iAutoId\n" +
					"                              from Bd_FormApprovalFlowM t1\n" +
					"                              where iApprovalDid = '" + Did + "')\n" +
					"  and iUserId = (select iUserId from Bd_FormApprovalD_User where iAutoId = '" + id + "')");
		}
		return res;
	}

	/**
	 * 根据节点ID与角色表主键ID找出用户 删除流程从表数据
	 */
	public void deleteByMidAndRoleId(Long Did, String roleIds){
		for (String id : StrSplitter.split(roleIds, COMMA, true, true)) {

			int delete = delete("delete\n" +
					"from Bd_FormApprovalFlowD\n" +
					"where iFormApprovalFlowMid = (select t1.iAutoId\n" +
					"                              from Bd_FormApprovalFlowM t1\n" +
					"                              where iApprovalDid = '" + Did + "')\n" +
					"  and iUserId in (select iUserId from Bd_FormApprovalD_RoleUsers where iFormApprovaldRoleId = '" + id + "')");

			if (delete > 0){

				delete("delete from Bd_FormApprovalD_RoleUsers where iFormApprovaldRoleId = '" + id + "'");

			}

		}
	}

	/**
	 * 根据主表ID 删除流程从表数据
	 */
	public void deleteByMid(Long Mid){
		delete("delete from Bd_FormApprovalFlowD where iFormApprovalFlowMid = "+Mid);
	}

	/**
	 * 根据 ids 条件查数据
	 */
	public List<FormApprovalFlowD> findListBySid(Kv kv){
		return find("select * from Bd_FormApprovalFlowD where iAutoId in ("+kv.getStr("flowIdStr")+")");
	}
    
}
