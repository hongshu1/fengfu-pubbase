package cn.rjtech.admin.stockoutqcformm;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.StockoutQcFormM;
/**
 * 质量管理-出库检
 * @ClassName: StockoutQcFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 19:18
 */
public class StockoutQcFormMService extends BaseService<StockoutQcFormM> {
	private final StockoutQcFormM dao=new StockoutQcFormM().dao();
	@Override
	protected StockoutQcFormM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<StockoutQcFormM> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("id");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param stockoutQcFormM
	 * @return
	 */
	public Ret save(StockoutQcFormM stockoutQcFormM) {
		if(stockoutQcFormM==null || isOk(stockoutQcFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockoutQcFormM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutQcFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(), stockoutQcFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockoutQcFormM
	 * @return
	 */
	public Ret update(StockoutQcFormM stockoutQcFormM) {
		if(stockoutQcFormM==null || notOk(stockoutQcFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockoutQcFormM dbStockoutQcFormM=findById(stockoutQcFormM.getIAutoId());
		if(dbStockoutQcFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockoutQcFormM.getName(), stockoutQcFormM.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutQcFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(), stockoutQcFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param stockoutQcFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockoutQcFormM stockoutQcFormM, Kv kv) {
		//addDeleteSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(),stockoutQcFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockoutQcFormM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockoutQcFormM stockoutQcFormM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}