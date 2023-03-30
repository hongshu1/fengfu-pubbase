package cn.rjtech.admin.inventoryroutingconfig;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Inventory;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryRoutingConfig;
/**
 * 物料建模-存货工艺配置
 * @ClassName: InventoryRoutingConfigAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:02
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryroutingconfig", viewPath = "/_view/admin/inventoryroutingconfig")
public class InventoryRoutingConfigAdminController extends BaseAdminController {

	@Inject
	private InventoryRoutingConfigService service;

	public void list() {
		Long iinventoryroutingid = getLong("iinventoryroutingid");
		renderJsonData(service.dataList(iinventoryroutingid));
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iType"), getBoolean("isEnabled")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

	public void saveItemRoutingConfig() {
		renderJsonData(service.saveItemRoutingConfig(getLong("iitemroutingid"), getInt("iseq")), "");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryRoutingConfig.class, "inventoryRoutingConfig")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryRoutingConfig inventoryRoutingConfig=service.findById(getLong(0));
		if(inventoryRoutingConfig == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryRoutingConfig",inventoryRoutingConfig);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryRoutingConfig.class, "inventoryRoutingConfig")));
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

	public void operation_dialog_index(){
		render("operation_dialog_index.html");
	}

	public void inventory_dialog_index(){
		Kv kv = getKv();
		set("inventoryid",kv.getStr("inventoryid"));
		set("",kv.getStr("cinvcode"));
		set("",kv.getStr("cinvcode1"));
		set("",kv.getStr("cinvname1"));
		set("",kv.getStr("cinvstd"));
		set("",kv.getStr("iinventoryuomid1"));
		render("inventory_dialog_index.html");
	}
}
