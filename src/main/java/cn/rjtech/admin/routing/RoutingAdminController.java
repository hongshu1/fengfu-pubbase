package cn.rjtech.admin.routing;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

/**
 * 物料建模-工艺路线
 * @ClassName: BomMasterAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-17 16:28
 */

@Path(value = "/admin/routing", viewPath = "/_view/admin/routing")
public class RoutingAdminController extends BaseAdminController {

	@Inject
	private RoutingService service;
	@Inject
	private InventoryChangeService inventoryChangeService;
	@Inject
	private EquipmentModelService equipmentModelService;
	@Inject
	private InventoryRoutingConfigService inventoryRoutingConfigService;
	/**
	 * 首页
	 */
	public void index() {
		render("index.html");
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
		Record routing = service.findByIdRoutingVersion(getLong(0));
		if(routing == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("routing",routing);
		render("edit.html");
	}


	/**
	 * 批量删除
	 */
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

	/**
	 * 删除
	 */
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	/**
	 * 切换isEnabled
	 */
	public void toggleIsEnabled() {
		renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

	/**
	 * 切换isDeleted
	 */
	public void toggleIsDeleted() {
		renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	/**
	 * 默认给1-100个数据
	 */
	public void inventoryAutocomplete(){
		renderJsonData(inventoryChangeService.inventoryAutocomplete(getPageNumber(), 100, getKv()));
	}

	public void findEquipmentModelAll(){
		renderJsonData(equipmentModelService.getAdminDataNoPage(getKv()));
	}

	

	public void getRoutingDetails(){
		renderJsonData(service.getRoutingDetails(getKv()));
	}
	
	// 工艺路线
	public void findRoutingAll() {
		renderJsonData(service.findRoutingAll(getKv()));
	}
	
	public void table(){
		render("table.html");
	}
	
	public void versionTable(){
		render("version_table.html");
	}
	
	public void versionAll(){
		renderJsonData(service.findRoutingVersion(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void getRoutingConfigDetail(){
		renderJsonData(inventoryRoutingConfigService.dataList(getLong(0)));
	}
	
	public void audit(@Para(value = "routingId") Long routingId,
					  @Para(value = "status") Integer status){
		renderJsonData(service.audit(routingId, status));
	}
}
