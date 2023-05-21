package cn.rjtech.admin.apsannualplandqty;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ApsAnnualplandQty;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 年度计划 Service
 * @ClassName: ApsAnnualplandQtyService
 * @author: chentao
 * @date: 2023-04-03 16:32
 */
public class ApsAnnualplandQtyService extends BaseService<ApsAnnualplandQty> {

	private final ApsAnnualplandQty dao = new ApsAnnualplandQty().dao();

	@Override
	protected ApsAnnualplandQty dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApsAnnualplandQty> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param apsAnnualplandQty
	 * @return
	 */
	public Ret save(ApsAnnualplandQty apsAnnualplandQty) {
		if(apsAnnualplandQty==null || isOk(apsAnnualplandQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(apsAnnualplandQty.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsAnnualplandQty.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(apsAnnualplandQty.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplandQty.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param apsAnnualplandQty
	 * @return
	 */
	public Ret update(ApsAnnualplandQty apsAnnualplandQty) {
		if(apsAnnualplandQty==null || notOk(apsAnnualplandQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApsAnnualplandQty dbApsAnnualplandQty=findById(apsAnnualplandQty.getIAutoId());
		if(dbApsAnnualplandQty==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(apsAnnualplandQty.getName(), apsAnnualplandQty.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsAnnualplandQty.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(apsAnnualplandQty.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplandQty.getName());
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
	 * @param apsAnnualplandQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApsAnnualplandQty apsAnnualplandQty, Kv kv) {
		//addDeleteSystemLog(apsAnnualplandQty.getIautoid(), JBoltUserKit.getUserId(),apsAnnualplandQty.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsAnnualplandQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApsAnnualplandQty apsAnnualplandQty, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(apsAnnualplandQty, kv);
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