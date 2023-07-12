package cn.rjtech.admin.formapprovaldrole;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormapprovaldRole;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 表单审批流 Service
 * @ClassName: FormapprovaldRoleService
 * @author: RJ
 * @date: 2023-04-18 17:30
 */
public class FormapprovaldRoleService extends BaseService<FormapprovaldRole> {

	private final FormapprovaldRole dao = new FormapprovaldRole().dao();

	@Override
	protected FormapprovaldRole dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<FormapprovaldRole> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param formapprovaldRole
	 * @return
	 */
	public Ret save(FormapprovaldRole formapprovaldRole) {
		if(formapprovaldRole==null || isOk(formapprovaldRole.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formapprovaldRole.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldRole.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formapprovaldRole.getIautoid(), JBoltUserKit.getUserId(), formapprovaldRole.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formapprovaldRole
	 * @return
	 */
	public Ret update(FormapprovaldRole formapprovaldRole) {
		if(formapprovaldRole==null || notOk(formapprovaldRole.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormapprovaldRole dbFormapprovaldRole=findById(formapprovaldRole.getIAutoId());
		if(dbFormapprovaldRole==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formapprovaldRole.getName(), formapprovaldRole.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldRole.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formapprovaldRole.getIautoid(), JBoltUserKit.getUserId(), formapprovaldRole.getName());
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
	 * @param formapprovaldRole 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormapprovaldRole formapprovaldRole, Kv kv) {
		//addDeleteSystemLog(formapprovaldRole.getIautoid(), JBoltUserKit.getUserId(),formapprovaldRole.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formapprovaldRole 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(FormapprovaldRole formapprovaldRole, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formapprovaldRole, kv);
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
	 * 根据外键查询
	 * @param Did
	 * @return
	 */
	public List<FormapprovaldRole> findByDid(Long Did){
		return find("select * from Bd_FormApprovalD_Role where iFormApprovalDid = "+Did +" order by iSeq asc");
	}

	/**
	 * 删除本位及子表数据
	 */
	public Ret deleteBenAndZi(Object[] ids){
		delete("DELETE FROM Bd_FormApprovalD_RoleUsers WHERE iFormApprovaldRoleId IN (" + ArrayUtil.join(ids, COMMA) + ")");

		return realDeleteByIds(ids);
	}
}
