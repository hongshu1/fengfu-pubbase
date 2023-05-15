package cn.rjtech.admin.approvalduser;

import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.ApprovaldRole;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApprovaldUser;

import java.util.List;

/**
 * 审批流节点审批人 Service
 * @ClassName: ApprovaldUserService
 * @author: RJ
 * @date: 2023-04-18 17:12
 */
public class ApprovaldUserService extends BaseService<ApprovaldUser> {

	private final ApprovaldUser dao = new ApprovaldUser().dao();

	@Override
	protected ApprovaldUser dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApprovaldUser> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param approvaldUser
	 * @return
	 */
	public Ret save(ApprovaldUser approvaldUser) {
		if(approvaldUser==null || isOk(approvaldUser.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(approvaldUser.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvaldUser.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(approvaldUser.getIautoid(), JBoltUserKit.getUserId(), approvaldUser.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param approvaldUser
	 * @return
	 */
	public Ret update(ApprovaldUser approvaldUser) {
		if(approvaldUser==null || notOk(approvaldUser.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApprovaldUser dbApprovaldUser=findById(approvaldUser.getIAutoId());
		if(dbApprovaldUser==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(approvaldUser.getName(), approvaldUser.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvaldUser.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(approvaldUser.getIautoid(), JBoltUserKit.getUserId(), approvaldUser.getName());
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
	 * @param approvaldUser 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApprovaldUser approvaldUser, Kv kv) {
		//addDeleteSystemLog(approvaldUser.getIautoid(), JBoltUserKit.getUserId(),approvaldUser.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param approvaldUser 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApprovaldUser approvaldUser, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(approvaldUser, kv);
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
	 * 通过节点ID找到审批人信息
	 * @param Did
	 * @return
	 */
	public List<ApprovaldUser> findUsersByDid(Long Did) {
		Kv kv = new Kv();
		kv.set("did",Did);
		return daoTemplate("approvald.findUsersByDid", kv).find();
	}
}
