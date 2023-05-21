package cn.rjtech.admin.mrpdemandpland;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MrpDemandpland;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 物料到货计划子表 Service
 * @ClassName: MrpDemandplandService
 * @author: chentao
 * @date: 2023-05-06 15:18
 */
public class MrpDemandplandService extends BaseService<MrpDemandpland> {

	private final MrpDemandpland dao = new MrpDemandpland().dao();

	@Override
	protected MrpDemandpland dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MrpDemandpland> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param mrpDemandpland
	 * @return
	 */
	public Ret save(MrpDemandpland mrpDemandpland) {
		if(mrpDemandpland==null || isOk(mrpDemandpland.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(mrpDemandpland.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandpland.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(mrpDemandpland.getIautoid(), JBoltUserKit.getUserId(), mrpDemandpland.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param mrpDemandpland
	 * @return
	 */
	public Ret update(MrpDemandpland mrpDemandpland) {
		if(mrpDemandpland==null || notOk(mrpDemandpland.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MrpDemandpland dbMrpDemandpland=findById(mrpDemandpland.getIAutoId());
		if(dbMrpDemandpland==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(mrpDemandpland.getName(), mrpDemandpland.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandpland.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(mrpDemandpland.getIautoid(), JBoltUserKit.getUserId(), mrpDemandpland.getName());
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
	 * @param mrpDemandpland 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MrpDemandpland mrpDemandpland, Kv kv) {
		//addDeleteSystemLog(mrpDemandpland.getIautoid(), JBoltUserKit.getUserId(),mrpDemandpland.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandpland 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MrpDemandpland mrpDemandpland, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(mrpDemandpland, kv);
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