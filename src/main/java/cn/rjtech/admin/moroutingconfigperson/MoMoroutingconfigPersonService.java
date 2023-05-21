package cn.rjtech.admin.moroutingconfigperson;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMoroutingconfigPerson;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 制造工单-工单工艺人员配置 Service
 * @ClassName: MoMoroutingconfigPersonService
 * @author: RJ
 * @date: 2023-05-09 16:48
 */
public class MoMoroutingconfigPersonService extends BaseService<MoMoroutingconfigPerson> {

	private final MoMoroutingconfigPerson dao = new MoMoroutingconfigPerson().dao();

	@Override
	protected MoMoroutingconfigPerson dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMoroutingconfigPerson> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMoroutingconfigPerson
	 * @return
	 */
	public Ret save(MoMoroutingconfigPerson moMoroutingconfigPerson) {
		if(moMoroutingconfigPerson==null || isOk(moMoroutingconfigPerson.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=moMoroutingconfigPerson.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMoroutingconfigPerson.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingconfigPerson.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMoroutingconfigPerson
	 * @return
	 */
	public Ret update(MoMoroutingconfigPerson moMoroutingconfigPerson) {
		if(moMoroutingconfigPerson==null || notOk(moMoroutingconfigPerson.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMoroutingconfigPerson dbMoMoroutingconfigPerson=findById(moMoroutingconfigPerson.getIAutoId());
		if(dbMoMoroutingconfigPerson==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMoroutingconfigPerson.getName(), moMoroutingconfigPerson.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutingconfigPerson.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMoroutingconfigPerson.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingconfigPerson.getName());
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
	 * @param moMoroutingconfigPerson 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMoroutingconfigPerson moMoroutingconfigPerson, Kv kv) {
		//addDeleteSystemLog(moMoroutingconfigPerson.getIAutoId(), JBoltUserKit.getUserId(),moMoroutingconfigPerson.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMoroutingconfigPerson 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMoroutingconfigPerson moMoroutingconfigPerson, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMoroutingconfigPerson, kv);
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
