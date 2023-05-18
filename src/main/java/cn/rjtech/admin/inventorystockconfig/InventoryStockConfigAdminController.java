package cn.rjtech.admin.inventorystockconfig;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryStockConfig;
/**
 * 料品库存档案
 * @ClassName: InventoryStockConfigAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:53
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventorystockconfig", viewPath = "/_view/admin/inventorystockconfig")
public class InventoryStockConfigAdminController extends BaseAdminController {

	@Inject
	private InventoryStockConfigService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isNegativeInventory"), getBoolean("isLotControl"), getBoolean("isValid"), getBoolean("isStructCode"), getBoolean("IsDeleted")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryStockConfig.class, "inventoryStockConfig")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryStockConfig inventoryStockConfig=service.findById(getLong(0));
		if(inventoryStockConfig == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryStockConfig",inventoryStockConfig);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryStockConfig.class, "inventoryStockConfig")));
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
	* 切换isNegativeInventory
	*/
	public void toggleIsNegativeInventory() {
	    renderJson(service.toggleBoolean(getLong(0),"isNegativeInventory"));
	}

   /**
	* 切换isLotControl
	*/
	public void toggleIsLotControl() {
	    renderJson(service.toggleBoolean(getLong(0),"isLotControl"));
	}

   /**
	* 切换isValid
	*/
	public void toggleIsValid() {
	    renderJson(service.toggleBoolean(getLong(0),"isValid"));
	}

   /**
	* 切换isStructCode
	*/
	public void toggleIsStructCode() {
	    renderJson(service.toggleBoolean(getLong(0),"isStructCode"));
	}

   /**
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}


}
