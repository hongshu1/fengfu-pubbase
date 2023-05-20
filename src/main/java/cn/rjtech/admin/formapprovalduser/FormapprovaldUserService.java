package cn.rjtech.admin.formapprovalduser;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormapprovaldUser;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 表单审批流审批人 Service
 * @ClassName: FormapprovaldUserService
 * @author: RJ
 * @date: 2023-04-18 17:33
 */
public class FormapprovaldUserService extends BaseService<FormapprovaldUser> {

	private final FormapprovaldUser dao = new FormapprovaldUser().dao();

	@Override
	protected FormapprovaldUser dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<FormapprovaldUser> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param formapprovaldUser
	 * @return
	 */
	public Ret save(FormapprovaldUser formapprovaldUser) {
		if(formapprovaldUser==null || isOk(formapprovaldUser.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formapprovaldUser.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldUser.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formapprovaldUser.getIautoid(), JBoltUserKit.getUserId(), formapprovaldUser.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formapprovaldUser
	 * @return
	 */
	public Ret update(FormapprovaldUser formapprovaldUser) {
		if(formapprovaldUser==null || notOk(formapprovaldUser.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormapprovaldUser dbFormapprovaldUser=findById(formapprovaldUser.getIAutoId());
		if(dbFormapprovaldUser==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formapprovaldUser.getName(), formapprovaldUser.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldUser.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formapprovaldUser.getIautoid(), JBoltUserKit.getUserId(), formapprovaldUser.getName());
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
	 * @param formapprovaldUser 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormapprovaldUser formapprovaldUser, Kv kv) {
		//addDeleteSystemLog(formapprovaldUser.getIautoid(), JBoltUserKit.getUserId(),formapprovaldUser.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formapprovaldUser 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(FormapprovaldUser formapprovaldUser, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formapprovaldUser, kv);
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
	 * 根据单据节点ID与用户ID查询
	 * @param formApprovalDid
	 * @param userId
	 * @return
	 */
	public List<FormapprovaldUser> findByDidAndUserId(Long formApprovalDid, Long userId){
		return find("select * from Bd_FormApprovalD_User where iFormApprovalDid = "+formApprovalDid +" and iUserId = "+userId);
	}

	/**
	 * 根据外键查询
	 * @param Did
	 * @return
	 */
	public List<FormapprovaldUser> findByDid(Long Did){
		return find("select * from from Bd_FormApprovalD_User where iFormApprovalDid = "+Did +" order by iSeq asc");
	}
}
