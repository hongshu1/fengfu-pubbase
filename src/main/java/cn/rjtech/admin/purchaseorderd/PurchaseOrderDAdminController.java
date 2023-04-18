package cn.rjtech.admin.purchaseorderd;

import cn.jbolt.core.permission.UnCheck;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.PurchaseOrderD;
/**
 * 采购/委外订单-采购订单明细
 * @ClassName: PurchaseOrderDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-13 14:33
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseorderd", viewPath = "/_view/admin/purchaseorderd")
public class PurchaseOrderDAdminController extends BaseAdminController {

	@Inject
	private PurchaseOrderDService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isPresent"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(PurchaseOrderD.class, "purchaseOrderD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseOrderD purchaseOrderD=service.findById(getLong(0));
		if(purchaseOrderD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseOrderD",purchaseOrderD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseOrderD.class, "purchaseOrderD")));
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
	* 切换isPresent
	*/
	public void toggleIsPresent() {
	    renderJson(service.toggleBoolean(getLong(0),"isPresent"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
