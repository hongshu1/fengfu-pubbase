package cn.rjtech.admin.inventoryroutingequipment;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryRoutingEquipment;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 物料建模-工艺档案设备集
 * @ClassName: InventoryRoutingEquipmentAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:04
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryroutingequipment", viewPath = "/_view/admin/inventoryroutingequipment")
public class InventoryRoutingEquipmentAdminController extends BaseAdminController {

	@Inject
	private InventoryRoutingEquipmentService service;

	public void equ_dialog_index(){
		render("equ_dialog_index.html");
	}

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryRoutingEquipment.class, "inventoryRoutingEquipment")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryRoutingEquipment inventoryRoutingEquipment=service.findById(getLong(0));
		if(inventoryRoutingEquipment == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryRoutingEquipment",inventoryRoutingEquipment);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryRoutingEquipment.class, "inventoryRoutingEquipment")));
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

	public void saveEquipment(){
		Long configid = getLong("configid");
		renderJson(service.saveEquipment(getJBoltTable(),configid));
	}

	public void dataList(){
		renderJsonData(service.dataList(getKv()));
	}
}
