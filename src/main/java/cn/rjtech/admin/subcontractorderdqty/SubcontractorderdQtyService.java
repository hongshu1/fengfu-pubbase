package cn.rjtech.admin.subcontractorderdqty;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SubcontractorderdQty;
/**
 * 采购/委外订单-采购订单明细数量
 * @ClassName: SubcontractorderdQtyService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:32
 */
public class SubcontractorderdQtyService extends BaseService<SubcontractorderdQty> {
	private final SubcontractorderdQty dao=new SubcontractorderdQty().dao();
	@Override
	protected SubcontractorderdQty dao() {
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
	public Page<SubcontractorderdQty> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param subcontractorderdQty
	 * @return
	 */
	public Ret save(SubcontractorderdQty subcontractorderdQty) {
		if(subcontractorderdQty==null || isOk(subcontractorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractorderdQty.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractorderdQty.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractorderdQty.getIAutoId(), JBoltUserKit.getUserId(), subcontractorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractorderdQty
	 * @return
	 */
	public Ret update(SubcontractorderdQty subcontractorderdQty) {
		if(subcontractorderdQty==null || notOk(subcontractorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractorderdQty dbSubcontractorderdQty=findById(subcontractorderdQty.getIAutoId());
		if(dbSubcontractorderdQty==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractorderdQty.getName(), subcontractorderdQty.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractorderdQty.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractorderdQty.getIAutoId(), JBoltUserKit.getUserId(), subcontractorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractorderdQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractorderdQty subcontractorderdQty, Kv kv) {
		//addDeleteSystemLog(subcontractorderdQty.getIAutoId(), JBoltUserKit.getUserId(),subcontractorderdQty.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractorderdQty model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractorderdQty subcontractorderdQty, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}