package cn.rjtech.admin.inventoryworkregion;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.InventoryWorkRegion;
/**
 * 存货档案-产线列表
 * @ClassName: InventoryWorkRegionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 14:00
 */
public class InventoryWorkRegionService extends BaseService<InventoryWorkRegion> {
	private final InventoryWorkRegion dao=new InventoryWorkRegion().dao();
	@Override
	protected InventoryWorkRegion dao() {
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
     * @param isDefault 是否默认：0. 否 1. 是
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<InventoryWorkRegion> getAdminDatas(int pageNumber, int pageSize, Boolean isDefault, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isDefault",isDefault);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryWorkRegion
	 * @return
	 */
	public Ret save(InventoryWorkRegion inventoryWorkRegion) {
		if(inventoryWorkRegion==null || isOk(inventoryWorkRegion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryWorkRegion.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		inventoryWorkRegion.setIsDeleted(false);
		boolean success=inventoryWorkRegion.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryWorkRegion.getIAutoId(), JBoltUserKit.getUserId(), inventoryWorkRegion.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryWorkRegion
	 * @return
	 */
	public Ret update(InventoryWorkRegion inventoryWorkRegion) {
		if(inventoryWorkRegion==null || notOk(inventoryWorkRegion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryWorkRegion dbInventoryWorkRegion=findById(inventoryWorkRegion.getIAutoId());
		if(dbInventoryWorkRegion==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryWorkRegion.getName(), inventoryWorkRegion.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryWorkRegion.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryWorkRegion.getIAutoId(), JBoltUserKit.getUserId(), inventoryWorkRegion.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryWorkRegion 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryWorkRegion inventoryWorkRegion, Kv kv) {
		//addDeleteSystemLog(inventoryWorkRegion.getIAutoId(), JBoltUserKit.getUserId(),inventoryWorkRegion.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryWorkRegion model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryWorkRegion inventoryWorkRegion, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryWorkRegion inventoryWorkRegion, String column, Kv kv) {
		//addUpdateSystemLog(inventoryWorkRegion.getIAutoId(), JBoltUserKit.getUserId(), inventoryWorkRegion.getName(),"的字段["+column+"]值:"+inventoryWorkRegion.get(column));
		/**
		switch(column){
		    case "isDefault":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}