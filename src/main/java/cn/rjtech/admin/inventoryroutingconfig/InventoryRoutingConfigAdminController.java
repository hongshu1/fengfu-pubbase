package cn.rjtech.admin.inventoryroutingconfig;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryRoutingConfig;
import cn.rjtech.wms.utils.EncodeUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

import java.io.UnsupportedEncodingException;

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

    @UnCheck
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
		renderJson(service.saveItemRoutingConfig(getLong("iitemroutingid"), getInt("iseq")));
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
		renderJson(service.deleteByBatchIds(get("ids")));
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

	public void invc_dialog_index() throws UnsupportedEncodingException {
		keepPara();
		set("configid",getLong("iautoid"));
		set(InventoryRoutingConfig.ITEMJSON, EncodeUtils.encodeUrl(get(InventoryRoutingConfig.ITEMJSON), EncodeUtils.UTF_8) );
		render("invc_dialog_index.html");
	}

	public void equipment_dialog_index(){
		keepPara();
		set("configid",getLong("iautoid"));
		set(InventoryRoutingConfig.EQUIPMENTJSON, EncodeUtils.encodeUrl(get(InventoryRoutingConfig.EQUIPMENTJSON), EncodeUtils.UTF_8) );
		render("equipment_dialog_index.html");
	}

	public void drawing_dialog_index(){
		keepPara();
		set("configid",getLong("iautoid"));
		set(InventoryRoutingConfig.ROUTINGSOPJSON, EncodeUtils.encodeUrl(get(InventoryRoutingConfig.ROUTINGSOPJSON), EncodeUtils.UTF_8) );
		render("drawing_dialog_index.html");
	}

	public void inventory_dialog_index(){
		keepPara();
		render("inventory_dialog_index.html");
	}

	public void updateRoutingConfig() {
		Record record = new Record().setColumns(getKv());
		renderJson(service.updateRoutingConfig(record));
	}


	public void moveDown() {
		Long iitemroutingid = getLong("iinventoryroutingid");
		Long iitemroutingconfigid = getLong("iitemroutingconfigid");
		Integer seq = getInt("seq");
		renderJsonData(service.moveDown(iitemroutingid, iitemroutingconfigid, seq));
	}

	public void moveUp() {
		Long iitemroutingid = getLong("iinventoryroutingid");
		Long iitemroutingconfigid = getLong("iitemroutingconfigid");
		Integer seq = getInt("seq");
		renderJsonData(service.moveUp(iitemroutingid, iitemroutingconfigid, seq));
	}
}
