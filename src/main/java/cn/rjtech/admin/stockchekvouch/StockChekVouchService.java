package cn.rjtech.admin.stockchekvouch;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockCheckVouch;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 盘点单
 * @ClassName: StockChekVouchService
 * @author: demo15
 * @date: 2023-04-20 11:23
 */
public class StockChekVouchService extends BaseService<StockCheckVouch> {
	private final StockCheckVouch dao=new StockCheckVouch().dao();
	@Override
	protected StockCheckVouch dao() {
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
     * @param CheckType 盘点方式;0 明盘 1 暗盘
	 * @return
	 */
	public Page<StockCheckVouch> getAdminDatas(int pageNumber, int pageSize, String CheckType) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("CheckType",CheckType);
        //排序
        sql.desc("AutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param stockChekVouch
	 * @return
	 */
	public Ret save(StockCheckVouch stockChekVouch) {
		if(stockChekVouch==null || isOk(stockChekVouch.getAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockChekVouch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockChekVouch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockChekVouch.getAutoid(), JBoltUserKit.getUserId(), stockChekVouch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockChekVouch
	 * @return
	 */
	public Ret update(StockCheckVouch stockChekVouch) {
		if(stockChekVouch==null || notOk(stockChekVouch.getAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockCheckVouch dbStockChekVouch=findById(stockChekVouch.getAutoId());
		if(dbStockChekVouch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockChekVouch.getName(), stockChekVouch.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockChekVouch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockChekVouch.getAutoid(), JBoltUserKit.getUserId(), stockChekVouch.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param stockChekVouch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockCheckVouch stockChekVouch, Kv kv) {
		//addDeleteSystemLog(stockChekVouch.getAutoid(), JBoltUserKit.getUserId(),stockChekVouch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockChekVouch model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockCheckVouch stockChekVouch, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}