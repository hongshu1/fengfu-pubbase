package cn.rjtech.admin.stockcheckvouchdetail;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Stockcheckvouchdetail;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 盘点单明细表
 * @ClassName: StockcheckvouchdetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-21 10:42
 */
public class StockcheckvouchdetailService extends BaseService<Stockcheckvouchdetail> {
	private final Stockcheckvouchdetail dao=new Stockcheckvouchdetail().dao();
	@Override
	protected Stockcheckvouchdetail dao() {
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
	public Page<Stockcheckvouchdetail> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param stockcheckvouchdetail
	 * @return
	 */
	public Ret save(Stockcheckvouchdetail stockcheckvouchdetail) {
		if(stockcheckvouchdetail==null || isOk(stockcheckvouchdetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockcheckvouchdetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockcheckvouchdetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockcheckvouchdetail.getAutoID(), JBoltUserKit.getUserId(), stockcheckvouchdetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockcheckvouchdetail
	 * @return
	 */
	public Ret update(Stockcheckvouchdetail stockcheckvouchdetail) {
		if(stockcheckvouchdetail==null || notOk(stockcheckvouchdetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Stockcheckvouchdetail dbStockcheckvouchdetail=findById(stockcheckvouchdetail.getAutoID());
		if(dbStockcheckvouchdetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockcheckvouchdetail.getName(), stockcheckvouchdetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockcheckvouchdetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockcheckvouchdetail.getAutoID(), JBoltUserKit.getUserId(), stockcheckvouchdetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param stockcheckvouchdetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Stockcheckvouchdetail stockcheckvouchdetail, Kv kv) {
		//addDeleteSystemLog(stockcheckvouchdetail.getAutoID(), JBoltUserKit.getUserId(),stockcheckvouchdetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockcheckvouchdetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Stockcheckvouchdetail stockcheckvouchdetail, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}