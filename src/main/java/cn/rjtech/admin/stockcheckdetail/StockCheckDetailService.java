package cn.rjtech.admin.stockcheckdetail;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockCheckDetail;
/**
 * 库存盘点-盘点明细表 Service
 * @ClassName: StockCheckDetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 22:49
 */
public class StockCheckDetailService extends BaseService<StockCheckDetail> {

	private final StockCheckDetail dao = new StockCheckDetail().dao();

	@Override
	protected StockCheckDetail dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<StockCheckDetail> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("CreateDate","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param stockCheckDetail
	 * @return
	 */
	public Ret save(StockCheckDetail stockCheckDetail) {
		if(stockCheckDetail==null || isOk(stockCheckDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockCheckDetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheckDetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockCheckDetail.getAutoID(), JBoltUserKit.getUserId(), stockCheckDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockCheckDetail
	 * @return
	 */
	public Ret update(StockCheckDetail stockCheckDetail) {
		if(stockCheckDetail==null || notOk(stockCheckDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockCheckDetail dbStockCheckDetail=findById(stockCheckDetail.getAutoID());
		if(dbStockCheckDetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockCheckDetail.getName(), stockCheckDetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheckDetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockCheckDetail.getAutoID(), JBoltUserKit.getUserId(), stockCheckDetail.getName());
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
	 * @param stockCheckDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockCheckDetail stockCheckDetail, Kv kv) {
		//addDeleteSystemLog(stockCheckDetail.getAutoID(), JBoltUserKit.getUserId(),stockCheckDetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockCheckDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(StockCheckDetail stockCheckDetail, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(stockCheckDetail, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param stockCheckDetail 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(StockCheckDetail stockCheckDetail,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(StockCheckDetail stockCheckDetail, String column, Kv kv) {
		//addUpdateSystemLog(stockCheckDetail.getAutoID(), JBoltUserKit.getUserId(), stockCheckDetail.getName(),"的字段["+column+"]值:"+stockCheckDetail.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockCheckDetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockCheckDetail stockCheckDetail, Kv kv) {
		//这里用来覆盖 检测StockCheckDetail是否被其它表引用
		return null;
	}

}