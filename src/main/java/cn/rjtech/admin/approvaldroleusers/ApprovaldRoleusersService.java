package cn.rjtech.admin.approvaldroleusers;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ApprovaldRoleusers;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 角色人员 Service
 * @ClassName: ApprovaldRoleusersService
 * @author: RJ
 * @date: 2023-06-05 14:30
 */
public class ApprovaldRoleusersService extends BaseService<ApprovaldRoleusers> {

	private final ApprovaldRoleusers dao = new ApprovaldRoleusers().dao();

	@Override
	protected ApprovaldRoleusers dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApprovaldRoleusers> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param approvaldRoleusers
	 * @return
	 */
	public Ret save(ApprovaldRoleusers approvaldRoleusers) {
		if(approvaldRoleusers==null || isOk(approvaldRoleusers.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(approvaldRoleusers.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvaldRoleusers.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(approvaldRoleusers.getIautoid(), JBoltUserKit.getUserId(), approvaldRoleusers.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param approvaldRoleusers
	 * @return
	 */
	public Ret update(ApprovaldRoleusers approvaldRoleusers) {
		if(approvaldRoleusers==null || notOk(approvaldRoleusers.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApprovaldRoleusers dbApprovaldRoleusers=findById(approvaldRoleusers.getIAutoId());
		if(dbApprovaldRoleusers==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(approvaldRoleusers.getName(), approvaldRoleusers.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvaldRoleusers.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(approvaldRoleusers.getIautoid(), JBoltUserKit.getUserId(), approvaldRoleusers.getName());
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
	 * @param approvaldRoleusers 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApprovaldRoleusers approvaldRoleusers, Kv kv) {
		//addDeleteSystemLog(approvaldRoleusers.getIautoid(), JBoltUserKit.getUserId(),approvaldRoleusers.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param approvaldRoleusers 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApprovaldRoleusers approvaldRoleusers, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(approvaldRoleusers, kv);
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
	 * 根据角色表ID删除数据
	 * @param ids
	 */
	public void deleteByApprovalId(Object[] ids){
		delete("DELETE FROM Bd_ApprovalD_RoleUsers WHERE iApprovaldRoleId IN (" + ArrayUtil.join(ids, COMMA) + ")");
	}

	/**
	 * 根据外键找数据
	 * @param id
	 * @return
	 */
	public List<ApprovaldRoleusers> getRoleUser(Long id){
		return find("select * from Bd_ApprovalD_RoleUsers where iApprovaldRoleId = "+id +" order by iSeq asc");
	}
}
