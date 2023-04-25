package cn.rjtech.admin.stockoutqcformd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockoutQcFormD;
/**
 * 质量管理-出库检明细列值 Service
 * @ClassName: StockoutQcFormDService
 * @author: RJ
 * @date: 2023-04-25 16:21
 */
public class StockoutQcFormDService extends BaseService<StockoutQcFormD> {

	private final StockoutQcFormD dao = new StockoutQcFormD().dao();

	@Override
	protected StockoutQcFormD dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<StockoutQcFormD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iSeq","ASC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param stockoutQcFormD
	 * @return
	 */
	public Ret save(StockoutQcFormD stockoutQcFormD) {
		if(stockoutQcFormD==null || isOk(stockoutQcFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockoutQcFormD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutQcFormD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockoutQcFormD.getIAutoId(), JBoltUserKit.getUserId(), stockoutQcFormD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockoutQcFormD
	 * @return
	 */
	public Ret update(StockoutQcFormD stockoutQcFormD) {
		if(stockoutQcFormD==null || notOk(stockoutQcFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockoutQcFormD dbStockoutQcFormD=findById(stockoutQcFormD.getIAutoId());
		if(dbStockoutQcFormD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockoutQcFormD.getName(), stockoutQcFormD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutQcFormD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockoutQcFormD.getIAutoId(), JBoltUserKit.getUserId(), stockoutQcFormD.getName());
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
	 * @param stockoutQcFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockoutQcFormD stockoutQcFormD, Kv kv) {
		//addDeleteSystemLog(stockoutQcFormD.getIAutoId(), JBoltUserKit.getUserId(),stockoutQcFormD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockoutQcFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(StockoutQcFormD stockoutQcFormD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(stockoutQcFormD, kv);
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