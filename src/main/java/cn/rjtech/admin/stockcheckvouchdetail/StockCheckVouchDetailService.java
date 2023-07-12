package cn.rjtech.admin.stockcheckvouchdetail;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockCheckVouchDetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 盘点单明细表
 * @ClassName: StockCheckVouchDetailService
 * @author: Administrator
 * @date: 2023-04-11 15:30
 */
public class StockCheckVouchDetailService extends BaseService<StockCheckVouchDetail> {
	private final StockCheckVouchDetail dao=new StockCheckVouchDetail().dao();
	@Override
	protected StockCheckVouchDetail dao() {
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
	public Page<StockCheckVouchDetail> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param stockCheckVouchDetail
	 * @return
	 */
	public Ret save(StockCheckVouchDetail stockCheckVouchDetail) {
		if(stockCheckVouchDetail==null || isOk(stockCheckVouchDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockCheckVouchDetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheckVouchDetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockCheckVouchDetail.getAutoid(), JBoltUserKit.getUserId(), stockCheckVouchDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockCheckVouchDetail
	 * @return
	 */
	public Ret update(StockCheckVouchDetail stockCheckVouchDetail) {
		if(stockCheckVouchDetail==null || notOk(stockCheckVouchDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockCheckVouchDetail dbStockCheckVouchDetail=findById(stockCheckVouchDetail.getAutoID());
		if(dbStockCheckVouchDetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockCheckVouchDetail.getName(), stockCheckVouchDetail.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheckVouchDetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockCheckVouchDetail.getAutoid(), JBoltUserKit.getUserId(), stockCheckVouchDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param stockCheckVouchDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockCheckVouchDetail stockCheckVouchDetail, Kv kv) {
		//addDeleteSystemLog(stockCheckVouchDetail.getAutoid(), JBoltUserKit.getUserId(),stockCheckVouchDetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockCheckVouchDetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockCheckVouchDetail stockCheckVouchDetail, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long AutoID = Long.parseLong(idStr);
				StockCheckVouchDetail dbStockcheckvouchdetail = findById(AutoID);
				ValidationUtils.notNull(dbStockcheckvouchdetail, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbStockcheckvouchdetail.delete(), JBoltMsg.FAIL);
			}

			return true;
		});
		return SUCCESS;
	}

	public List<StockCheckVouchDetail> findListByMasId(Long masid) {
		return find("select * from T_Sys_StockCheckVouchDetail where MasID=?", masid);
	}

}