package cn.rjtech.admin.apsannualpland;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApsAnnualpland;
/**
 * 年度计划 Service
 * @ClassName: ApsAnnualplandService
 * @author: chentao
 * @date: 2023-04-03 16:31
 */
public class ApsAnnualplandService extends BaseService<ApsAnnualpland> {

	private final ApsAnnualpland dao = new ApsAnnualpland().dao();

	@Override
	protected ApsAnnualpland dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApsAnnualpland> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param apsAnnualpland
	 * @return
	 */
	public Ret save(ApsAnnualpland apsAnnualpland) {
		if(apsAnnualpland==null || isOk(apsAnnualpland.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(apsAnnualpland.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsAnnualpland.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(apsAnnualpland.getIautoid(), JBoltUserKit.getUserId(), apsAnnualpland.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param apsAnnualpland
	 * @return
	 */
	public Ret update(ApsAnnualpland apsAnnualpland) {
		if(apsAnnualpland==null || notOk(apsAnnualpland.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApsAnnualpland dbApsAnnualpland=findById(apsAnnualpland.getIAutoId());
		if(dbApsAnnualpland==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(apsAnnualpland.getName(), apsAnnualpland.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsAnnualpland.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(apsAnnualpland.getIautoid(), JBoltUserKit.getUserId(), apsAnnualpland.getName());
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
	 * @param apsAnnualpland 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApsAnnualpland apsAnnualpland, Kv kv) {
		//addDeleteSystemLog(apsAnnualpland.getIautoid(), JBoltUserKit.getUserId(),apsAnnualpland.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsAnnualpland 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApsAnnualpland apsAnnualpland, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(apsAnnualpland, kv);
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