package cn.rjtech.admin.uptimed;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.UptimeD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 稼动时间纪录 Service
 * @ClassName: UptimeDService
 * @author: chentao
 * @date: 2023-06-28 10:31
 */
public class UptimeDService extends BaseService<UptimeD> {

	private final UptimeD dao = new UptimeD().dao();

	@Override
	protected UptimeD dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<UptimeD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param uptimeD
	 * @return
	 */
	public Ret save(UptimeD uptimeD) {
		if(uptimeD==null || isOk(uptimeD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(uptimeD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=uptimeD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeD.getIautoid(), JBoltUserKit.getUserId(), uptimeD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeD
	 * @return
	 */
	public Ret update(UptimeD uptimeD) {
		if(uptimeD==null || notOk(uptimeD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeD dbUptimeD=findById(uptimeD.getIAutoId());
		if(dbUptimeD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(uptimeD.getName(), uptimeD.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=uptimeD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeD.getIautoid(), JBoltUserKit.getUserId(), uptimeD.getName());
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
	 * @param uptimeD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeD uptimeD, Kv kv) {
		//addDeleteSystemLog(uptimeD.getIautoid(), JBoltUserKit.getUserId(),uptimeD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(UptimeD uptimeD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(uptimeD, kv);
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