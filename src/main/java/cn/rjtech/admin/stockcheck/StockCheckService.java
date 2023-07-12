package cn.rjtech.admin.stockcheck;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockCheck;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 库存盘点单主表 Service
 * @ClassName: StockCheckService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 22:45
 */
public class StockCheckService extends BaseService<StockCheck> {

	private final StockCheck dao = new StockCheck().dao();

	@Override
	protected StockCheck dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<StockCheck> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param stockCheck
	 * @return
	 */
	public Ret save(StockCheck stockCheck) {
		if(stockCheck==null || isOk(stockCheck.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockCheck.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheck.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockCheck.getAutoID(), JBoltUserKit.getUserId(), stockCheck.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockCheck
	 * @return
	 */
	public Ret update(StockCheck stockCheck) {
		if(stockCheck==null || notOk(stockCheck.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockCheck dbStockCheck=findById(stockCheck.getAutoID());
		if(dbStockCheck==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockCheck.getName(), stockCheck.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheck.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockCheck.getAutoID(), JBoltUserKit.getUserId(), stockCheck.getName());
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
	 * @param stockCheck 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockCheck stockCheck, Kv kv) {
		//addDeleteSystemLog(stockCheck.getAutoID(), JBoltUserKit.getUserId(),stockCheck.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockCheck 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(StockCheck stockCheck, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(stockCheck, kv);
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
	 * @param stockCheck 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(StockCheck stockCheck,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(StockCheck stockCheck, String column, Kv kv) {
		//addUpdateSystemLog(stockCheck.getAutoID(), JBoltUserKit.getUserId(), stockCheck.getName(),"的字段["+column+"]值:"+stockCheck.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockCheck model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockCheck stockCheck, Kv kv) {
		//这里用来覆盖 检测StockCheck是否被其它表引用
		return null;
	}

}