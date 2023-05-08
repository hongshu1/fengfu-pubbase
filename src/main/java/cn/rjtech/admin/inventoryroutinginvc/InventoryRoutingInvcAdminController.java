package cn.rjtech.admin.inventoryroutinginvc;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryRoutingInvc;
/**
 * 物料建模-存货工艺工序物料集
 * @ClassName: InventoryRoutingInvcAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:05
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryroutinginvc", viewPath = "/_view/admin/inventoryroutinginvc")
public class InventoryRoutingInvcAdminController extends BaseAdminController {

	@Inject
	private InventoryRoutingInvcService service;

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
		renderJson(service.save(getModel(InventoryRoutingInvc.class, "inventoryRoutingInvc")));
	}

	public void saveInvc(){
		Long configid = getLong("configid");
		renderJson(service.saveInvc(getJBoltTable(), configid));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryRoutingInvc inventoryRoutingInvc=service.findById(getLong(0));
		if(inventoryRoutingInvc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryRoutingInvc",inventoryRoutingInvc);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryRoutingInvc.class, "inventoryRoutingInvc")));
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

	public void dataList(){
		renderJsonData(service.dataList(getKv()));
	}

	public void iteminvc_dialog_index(){
		render("iteminvc_dialog_index.html");
	}
}
