package cn.rjtech.admin.instockqcformd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InStockQcFormD;
/**
 * 质量管理-在库检单行配置 Service
 * @ClassName: InStockQcFormDService
 * @author: RJ
 * @date: 2023-05-04 14:24
 */
public class InStockQcFormDService extends BaseService<InStockQcFormD> {

	private final InStockQcFormD dao = new InStockQcFormD().dao();

	@Override
	protected InStockQcFormD dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InStockQcFormD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param inStockQcFormD
	 * @return
	 */
	public Ret save(InStockQcFormD inStockQcFormD) {
		if(inStockQcFormD==null || isOk(inStockQcFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inStockQcFormD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inStockQcFormD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inStockQcFormD.getIAutoId(), JBoltUserKit.getUserId(), inStockQcFormD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inStockQcFormD
	 * @return
	 */
	public Ret update(InStockQcFormD inStockQcFormD) {
		if(inStockQcFormD==null || notOk(inStockQcFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InStockQcFormD dbInStockQcFormD=findById(inStockQcFormD.getIAutoId());
		if(dbInStockQcFormD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inStockQcFormD.getName(), inStockQcFormD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inStockQcFormD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inStockQcFormD.getIAutoId(), JBoltUserKit.getUserId(), inStockQcFormD.getName());
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
	 * @param inStockQcFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InStockQcFormD inStockQcFormD, Kv kv) {
		//addDeleteSystemLog(inStockQcFormD.getIAutoId(), JBoltUserKit.getUserId(),inStockQcFormD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inStockQcFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(InStockQcFormD inStockQcFormD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(inStockQcFormD, kv);
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