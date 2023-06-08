package cn.rjtech.admin.formapprovaldroleusers;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormapprovaldRoleusers;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 单据角色人员 Service
 * @ClassName: FormapprovaldRoleusersService
 * @author: RJ
 * @date: 2023-06-05 14:32
 */
public class FormapprovaldRoleusersService extends BaseService<FormapprovaldRoleusers> {

	private final FormapprovaldRoleusers dao = new FormapprovaldRoleusers().dao();

	@Override
	protected FormapprovaldRoleusers dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<FormapprovaldRoleusers> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param formapprovaldRoleusers
	 * @return
	 */
	public Ret save(FormapprovaldRoleusers formapprovaldRoleusers) {
		if(formapprovaldRoleusers==null || isOk(formapprovaldRoleusers.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formapprovaldRoleusers.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldRoleusers.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formapprovaldRoleusers.getIautoid(), JBoltUserKit.getUserId(), formapprovaldRoleusers.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formapprovaldRoleusers
	 * @return
	 */
	public Ret update(FormapprovaldRoleusers formapprovaldRoleusers) {
		if(formapprovaldRoleusers==null || notOk(formapprovaldRoleusers.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormapprovaldRoleusers dbFormapprovaldRoleusers=findById(formapprovaldRoleusers.getIAutoId());
		if(dbFormapprovaldRoleusers==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formapprovaldRoleusers.getName(), formapprovaldRoleusers.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldRoleusers.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formapprovaldRoleusers.getIautoid(), JBoltUserKit.getUserId(), formapprovaldRoleusers.getName());
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
	 * @param formapprovaldRoleusers 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormapprovaldRoleusers formapprovaldRoleusers, Kv kv) {
		//addDeleteSystemLog(formapprovaldRoleusers.getIautoid(), JBoltUserKit.getUserId(),formapprovaldRoleusers.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formapprovaldRoleusers 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(FormapprovaldRoleusers formapprovaldRoleusers, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formapprovaldRoleusers, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

}
