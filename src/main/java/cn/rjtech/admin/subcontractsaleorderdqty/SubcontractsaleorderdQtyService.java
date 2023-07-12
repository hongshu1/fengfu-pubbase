package cn.rjtech.admin.subcontractsaleorderdqty;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SubcontractsaleorderdQty;
/**
 * 客户订单-委外订单数量记录
 * @ClassName: SubcontractsaleorderdQtyService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-12 11:05
 */
public class SubcontractsaleorderdQtyService extends BaseService<SubcontractsaleorderdQty> {
	private final SubcontractsaleorderdQty dao=new SubcontractsaleorderdQty().dao();
	@Override
	protected SubcontractsaleorderdQty dao() {
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
	public Page<SubcontractsaleorderdQty> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param subcontractsaleorderdQty
	 * @return
	 */
	public Ret save(SubcontractsaleorderdQty subcontractsaleorderdQty) {
		if(subcontractsaleorderdQty==null || isOk(subcontractsaleorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=subcontractsaleorderdQty.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractsaleorderdQty.getIAutoId(), JBoltUserKit.getUserId(), subcontractsaleorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractsaleorderdQty
	 * @return
	 */
	public Ret update(SubcontractsaleorderdQty subcontractsaleorderdQty) {
		if(subcontractsaleorderdQty==null || notOk(subcontractsaleorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractsaleorderdQty dbSubcontractsaleorderdQty=findById(subcontractsaleorderdQty.getIAutoId());
		if(dbSubcontractsaleorderdQty==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=subcontractsaleorderdQty.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractsaleorderdQty.getIAutoId(), JBoltUserKit.getUserId(), subcontractsaleorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractsaleorderdQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractsaleorderdQty subcontractsaleorderdQty, Kv kv) {
		//addDeleteSystemLog(subcontractsaleorderdQty.getIAutoId(), JBoltUserKit.getUserId(),subcontractsaleorderdQty.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractsaleorderdQty model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractsaleorderdQty subcontractsaleorderdQty, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}