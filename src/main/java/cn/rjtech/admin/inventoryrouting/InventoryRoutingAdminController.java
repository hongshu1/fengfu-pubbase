package cn.rjtech.admin.inventoryrouting;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryRouting;
/**
 * 物料建模-存货工艺档案
 * @ClassName: InventoryRoutingAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:02
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryrouting", viewPath = "/_view/admin/inventoryrouting")
public class InventoryRoutingAdminController extends BaseAdminController {

	@Inject
	private InventoryRoutingService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 新增
	*/
	public void dialog_index() {
		Long iinventoryid=getLong("iinventoryid");
		String citemcode=get("citemcode");
		String citemname=get("citemname");
		set("iinventoryid",iinventoryid);
		set("citemcode",citemcode);
		set("citemname",citemname);
		render("dialog_index.html");
	}

	public void dataList(){
		Long iinventoryid=getLong("iinventoryid");
		renderJsonData(service.dataList(iinventoryid));
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryRouting.class, "inventoryRouting")));
	}

	public void saveItemRouting(){
		Long iinventoryid = getLong("iinventoryid");
		renderJson(service.saveItemRouting(getJBoltTable(),iinventoryid));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryRouting inventoryRouting=service.findById(getLong(0));
		if(inventoryRouting == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryRouting",inventoryRouting);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryRouting.class, "inventoryRouting")));
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

	public void process_view(){
		Long iinventoryroutingid = getLong("iinventoryroutingid");
		String processViewStr = service.getprocessViewStr(iinventoryroutingid);
		set("processViewStr",processViewStr);
		set("cInvCode",get("cInvCode"));
		set("cInvCode1",get("cInvCode1"));
		set("cInvName1",get("cInvName1"));
		render("process_view.html");
	}

	public void technological_Structure(){
		Long iinventoryroutingid = getLong("iinventoryroutingid");
		set("iinventoryroutingid",iinventoryroutingid);
		set("cInvCode1",get("cInvCode1"));
		set("cInvName1",get("cInvName1"));
		render("technological_structure.html");
	}

	public void mgrTree(){
		Long iinventoryroutingid = getLong("iinventoryroutingid");
		renderJsonData(service.getMgrTree(iinventoryroutingid));
	}
}
