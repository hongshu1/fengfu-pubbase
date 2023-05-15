package cn.rjtech.admin.inventoryplan;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryPlan;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 料品计划档案
 * @ClassName: InventoryPlanService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:57
 */
public class InventoryPlanService extends BaseService<InventoryPlan> {
	private final InventoryPlan dao=new InventoryPlan().dao();
	@Override
	protected InventoryPlan dao() {
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
	public Page<InventoryPlan> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryPlan
	 * @return
	 */
	public Ret save(InventoryPlan inventoryPlan) {
		if(inventoryPlan==null || isOk(inventoryPlan.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryPlan.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryPlan.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryPlan.getIAutoId(), JBoltUserKit.getUserId(), inventoryPlan.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryPlan
	 * @return
	 */
	public Ret update(InventoryPlan inventoryPlan) {
		if(inventoryPlan==null || notOk(inventoryPlan.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryPlan dbInventoryPlan=findById(inventoryPlan.getIAutoId());
		if(dbInventoryPlan==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryPlan.getName(), inventoryPlan.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryPlan.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryPlan.getIAutoId(), JBoltUserKit.getUserId(), inventoryPlan.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryPlan 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryPlan inventoryPlan, Kv kv) {
		//addDeleteSystemLog(inventoryPlan.getIAutoId(), JBoltUserKit.getUserId(),inventoryPlan.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryPlan model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryPlan inventoryPlan, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

    public void test() {
        System.out.println(u8SourceConfigName());
    }

}