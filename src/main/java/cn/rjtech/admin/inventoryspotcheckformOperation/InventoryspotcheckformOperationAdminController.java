package cn.rjtech.admin.inventoryspotcheckformOperation;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryspotcheckformOperation;
/**
 * 质量建模-存货点检工序 Controller
 * @ClassName: InventoryspotcheckformOperationAdminController
 * @author: RJ
 * @date: 2023-04-03 16:24
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/inventoryspotcheckformOperation", viewPath = "/_view/admin/inventoryspotcheckformOperation")
public class InventoryspotcheckformOperationAdminController extends BaseAdminController {

	@Inject
	private InventoryspotcheckformOperationService service;

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
		InventoryspotcheckformOperation inventoryspotcheckformOperation=service.findById(getLong(0)); 
		if(inventoryspotcheckformOperation == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryspotcheckformOperation",inventoryspotcheckformOperation);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryspotcheckformOperation.class, "inventoryspotcheckformOperation")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryspotcheckformOperation.class, "inventoryspotcheckformOperation")));
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
