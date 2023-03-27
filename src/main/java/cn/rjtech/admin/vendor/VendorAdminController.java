package cn.rjtech.admin.vendor;

import java.util.List;

import com.jfinal.aop.Inject;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Vendor;
/**
 * 往来单位-供应商档案
 * @ClassName: VendorAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 17:16
 */
@CheckPermission(PermissionKey.VENDOR)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/vendor", viewPath = "/_view/admin/vendor")
public class VendorAdminController extends BaseAdminController {

	@Inject
	private VendorService    service;

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
		renderJsonData(service.pageList(getKv()));
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(Vendor.class, "vendor")));
	}

	/**
	 * JBoltTable 可编辑表格整体提交
	 */
	@Before(Tx.class)
	public void submit() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

   /**
	* 编辑
	*/
	public void edit() {
		Vendor vendor=service.findById(getLong(0));
		if(vendor == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("vendor",vendor);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Vendor.class, "vendor")));
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


	public void options() {
		renderJsonData(service.options());
	}
}
