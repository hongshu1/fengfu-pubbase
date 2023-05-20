package cn.rjtech.admin.moroutingconfig;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMoroutingconfig;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 制造工单-生产工艺路线 Service
 * @ClassName: MoMoroutingconfigService
 * @author: RJ
 * @date: 2023-05-09 16:40
 */
public class MoMoroutingconfigService extends BaseService<MoMoroutingconfig> {

	private final MoMoroutingconfig dao = new MoMoroutingconfig().dao();

	@Override
	protected MoMoroutingconfig dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMoroutingconfig> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMoroutingconfig
	 * @return
	 */
	public Ret save(MoMoroutingconfig moMoroutingconfig) {
		if(moMoroutingconfig==null || isOk(moMoroutingconfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMoroutingconfig.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutingconfig.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMoroutingconfig.getIautoid(), JBoltUserKit.getUserId(), moMoroutingconfig.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMoroutingconfig
	 * @return
	 */
	public Ret update(MoMoroutingconfig moMoroutingconfig) {
		if(moMoroutingconfig==null || notOk(moMoroutingconfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMoroutingconfig dbMoMoroutingconfig=findById(moMoroutingconfig.getIAutoId());
		if(dbMoMoroutingconfig==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMoroutingconfig.getName(), moMoroutingconfig.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutingconfig.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMoroutingconfig.getIautoid(), JBoltUserKit.getUserId(), moMoroutingconfig.getName());
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
	 * @param moMoroutingconfig 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMoroutingconfig moMoroutingconfig, Kv kv) {
		//addDeleteSystemLog(moMoroutingconfig.getIautoid(), JBoltUserKit.getUserId(),moMoroutingconfig.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMoroutingconfig 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMoroutingconfig moMoroutingconfig, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMoroutingconfig, kv);
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
