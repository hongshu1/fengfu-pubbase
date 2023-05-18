package cn.rjtech.admin.formapprovaldperson;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormapprovaldPerson;
/**
 * 表单审批流实际审批人 Service
 * @ClassName: FormapprovaldPersonService
 * @author: RJ
 * @date: 2023-04-24 10:34
 */
public class FormapprovaldPersonService extends BaseService<FormapprovaldPerson> {

	private final FormapprovaldPerson dao = new FormapprovaldPerson().dao();

	@Override
	protected FormapprovaldPerson dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<FormapprovaldPerson> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param formapprovaldPerson
	 * @return
	 */
	public Ret save(FormapprovaldPerson formapprovaldPerson) {
		if(formapprovaldPerson==null || isOk(formapprovaldPerson.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formapprovaldPerson.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldPerson.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formapprovaldPerson.getIautoid(), JBoltUserKit.getUserId(), formapprovaldPerson.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formapprovaldPerson
	 * @return
	 */
	public Ret update(FormapprovaldPerson formapprovaldPerson) {
		if(formapprovaldPerson==null || notOk(formapprovaldPerson.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormapprovaldPerson dbFormapprovaldPerson=findById(formapprovaldPerson.getIAutoId());
		if(dbFormapprovaldPerson==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formapprovaldPerson.getName(), formapprovaldPerson.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formapprovaldPerson.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formapprovaldPerson.getIautoid(), JBoltUserKit.getUserId(), formapprovaldPerson.getName());
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
	 * @param formapprovaldPerson 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormapprovaldPerson formapprovaldPerson, Kv kv) {
		//addDeleteSystemLog(formapprovaldPerson.getIautoid(), JBoltUserKit.getUserId(),formapprovaldPerson.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formapprovaldPerson 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(FormapprovaldPerson formapprovaldPerson, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formapprovaldPerson, kv);
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
