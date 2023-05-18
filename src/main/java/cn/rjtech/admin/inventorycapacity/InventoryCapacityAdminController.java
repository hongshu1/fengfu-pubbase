package cn.rjtech.admin.inventorycapacity;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryCapacity;
/**
 * 物料班次产能 Controller
 * @ClassName: InventoryCapacityAdminController
 * @author: chentao
 * @date: 2023-04-18 19:04
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/inventorycapacity", viewPath = "/_view/admin/inventorycapacity")
public class InventoryCapacityAdminController extends BaseAdminController {

	@Inject
	private InventoryCapacityService service;

   /**
	* 首页
	*/
	public void index() {
		render("index().html");
	}
  	
  	/**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryCapacity inventoryCapacity=service.findById(getLong(0)); 
		if(inventoryCapacity == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryCapacity",inventoryCapacity);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryCapacity.class, "inventoryCapacity")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryCapacity.class, "inventoryCapacity")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}


}
