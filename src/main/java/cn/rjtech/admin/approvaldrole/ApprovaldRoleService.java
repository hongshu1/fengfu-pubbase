package cn.rjtech.admin.approvaldrole;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApprovaldRole;

import java.util.List;

/**
 * 审批流节点审批角色 Service
 * @ClassName: ApprovaldRoleService
 * @author: RJ
 * @date: 2023-04-18 17:09
 */
public class ApprovaldRoleService extends BaseService<ApprovaldRole> {

	private final ApprovaldRole dao = new ApprovaldRole().dao();

	@Override
	protected ApprovaldRole dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApprovaldRole> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param approvaldRole
	 * @return
	 */
	public Ret save(ApprovaldRole approvaldRole) {
		if(approvaldRole==null || isOk(approvaldRole.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(approvaldRole.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvaldRole.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(approvaldRole.getIautoid(), JBoltUserKit.getUserId(), approvaldRole.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param approvaldRole
	 * @return
	 */
	public Ret update(ApprovaldRole approvaldRole) {
		if(approvaldRole==null || notOk(approvaldRole.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApprovaldRole dbApprovaldRole=findById(approvaldRole.getIAutoId());
		if(dbApprovaldRole==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(approvaldRole.getName(), approvaldRole.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvaldRole.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(approvaldRole.getIautoid(), JBoltUserKit.getUserId(), approvaldRole.getName());
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
	 * @param approvaldRole 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApprovaldRole approvaldRole, Kv kv) {
		//addDeleteSystemLog(approvaldRole.getIautoid(), JBoltUserKit.getUserId(),approvaldRole.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param approvaldRole 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApprovaldRole approvaldRole, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(approvaldRole, kv);
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
	 * 通过节点ID找到对应角色信息
	 * @param Did
	 * @return
	 */
	public List<ApprovaldRole> findRolesByDid(Long Did) {
		Kv kv = new Kv();
		kv.set("did",Did);
		return daoTemplate("approvald.findRolesByDid", kv).find();
	}
}
