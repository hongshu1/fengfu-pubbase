package cn.rjtech.admin.bommaster;

import cn.rjtech.admin.inventorychange.InventoryChangeService;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.BomMaster;
/**
 * 物料建模-Bom母项
 * @ClassName: BomMasterAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 16:39
 */

@Path(value = "/admin/bommaster", viewPath = "/_view/admin/bommaster")
public class BomMasterAdminController extends BaseAdminController {

	@Inject
	private BomMasterService service;
	@Inject
	private InventoryChangeService inventoryChangeService;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(BomMaster.class, "bomMaster")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		BomMaster bomMaster=service.findById(getLong(0));
		if(bomMaster == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bomMaster",bomMaster);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(BomMaster.class, "bomMaster")));
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

}
