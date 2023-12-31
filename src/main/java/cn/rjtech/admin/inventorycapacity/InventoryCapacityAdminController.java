package cn.rjtech.admin.inventorycapacity;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryCapacity;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

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
	@Inject
	private WorkshiftmService workshiftmService;
	
   /**
	* 首页
	*/
	public void index() {
		render("index.html");
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
		render("add.html");
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
		render("edit.html");
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

    @UnCheck
	public void findByInvId(@Para(value = "invId") Long invId){
		renderJsonData(service.findByInvId(invId));
	}

    @UnCheck
	public void getSelect(){
		renderJsonData(workshiftmService.getSelect());
	}
    
}
