package cn.rjtech.admin.inventoryworkregion;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryWorkRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * 存货档案-产线列表
 * @ClassName: InventoryWorkRegionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 14:00
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryworkregion", viewPath = "/_view/admin/inventoryworkregion")
public class InventoryWorkRegionAdminController extends BaseAdminController {

	@Inject
	private InventoryWorkRegionService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isDefault"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(InventoryWorkRegion.class, "inventoryWorkRegion")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryWorkRegion inventoryWorkRegion=service.findById(getLong(0));
		if(inventoryWorkRegion == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryWorkRegion",inventoryWorkRegion);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryWorkRegion.class, "inventoryWorkRegion")));
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
	* 切换isDefault
	*/
	public void toggleIsDefault() {
	    renderJson(service.toggleBoolean(getLong(0),"isDefault"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	public void options(){
		Kv kv = getKv();
		Long iinventoryid = kv.getLong("iInventoryId");
		if(iinventoryid == null)
			renderJsonData(new ArrayList<>());
		else{
			List<Record> datas = service.getDatasOfSql(kv);
			renderJsonData(datas);
		}
	}
}
