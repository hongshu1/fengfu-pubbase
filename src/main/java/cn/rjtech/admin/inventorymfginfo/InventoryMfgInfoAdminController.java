package cn.rjtech.admin.inventorymfginfo;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryMfgInfo;
/**
 * 料品生产档案
 * @ClassName: InventoryMfgInfoAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:56
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventorymfginfo", viewPath = "/_view/admin/inventorymfginfo")
public class InventoryMfgInfoAdminController extends BaseAdminController {

	@Inject
	private InventoryMfgInfoService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("cProdCalendarTypeSn"), get("cSupplyCalendarTypeSn"), getBoolean("isIQC1"), getBoolean("isIQC2")));
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
		renderJson(service.save(getModel(InventoryMfgInfo.class, "inventoryMfgInfo")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryMfgInfo inventoryMfgInfo=service.findById(getLong(0));
		if(inventoryMfgInfo == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryMfgInfo",inventoryMfgInfo);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryMfgInfo.class, "inventoryMfgInfo")));
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
	* 切换isIQC1
	*/
	public void toggleIsIQC1() {
	    renderJson(service.toggleBoolean(getLong(0),"isIQC1"));
	}

   /**
	* 切换isIQC2
	*/
	public void toggleIsIQC2() {
	    renderJson(service.toggleBoolean(getLong(0),"isIQC2"));
	}


}
