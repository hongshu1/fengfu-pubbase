package cn.rjtech.admin.stockoutqcformdline;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockoutqcformdLine;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 质量管理-出库检单行配置 Service
 * @ClassName: StockoutqcformdLineService
 * @author: RJ
 * @date: 2023-04-25 16:28
 */
public class StockoutqcformdLineService extends BaseService<StockoutqcformdLine> {

	private final StockoutqcformdLine dao = new StockoutqcformdLine().dao();

	@Override
	protected StockoutqcformdLine dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<StockoutqcformdLine> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param stockoutqcformdLine
	 * @return
	 */
	public Ret save(StockoutqcformdLine stockoutqcformdLine) {
		if(stockoutqcformdLine==null || isOk(stockoutqcformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockoutqcformdLine.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutqcformdLine.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockoutqcformdLine.getIAutoId(), JBoltUserKit.getUserId(), stockoutqcformdLine.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockoutqcformdLine
	 * @return
	 */
	public Ret update(StockoutqcformdLine stockoutqcformdLine) {
		if(stockoutqcformdLine==null || notOk(stockoutqcformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockoutqcformdLine dbStockoutqcformdLine=findById(stockoutqcformdLine.getIAutoId());
		if(dbStockoutqcformdLine==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockoutqcformdLine.getName(), stockoutqcformdLine.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutqcformdLine.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockoutqcformdLine.getIAutoId(), JBoltUserKit.getUserId(), stockoutqcformdLine.getName());
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
	 * @param stockoutqcformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockoutqcformdLine stockoutqcformdLine, Kv kv) {
		//addDeleteSystemLog(stockoutqcformdLine.getIAutoId(), JBoltUserKit.getUserId(),stockoutqcformdLine.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockoutqcformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(StockoutqcformdLine stockoutqcformdLine, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(stockoutqcformdLine, kv);
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