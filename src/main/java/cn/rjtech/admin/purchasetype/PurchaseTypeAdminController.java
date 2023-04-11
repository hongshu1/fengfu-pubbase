package cn.rjtech.admin.purchasetype;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.PurchaseType;
/**
 * 采购类型 Controller
 * @ClassName: PurchaseTypeAdminController
 * @author: WYX
 * @date: 2023-04-03 10:52
 */
@CheckPermission(PermissionKey.PURCHASETYPE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/purchasetype", viewPath = "/_view/admin/purchasetype")
public class PurchaseTypeAdminController extends BaseAdminController {

	@Inject
	private PurchaseTypeService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

	public void selectAll (){
		renderJsonData(service.selectAll(getKv()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseType purchaseType=service.findById(getLong(0)); 
		if(purchaseType == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseType",purchaseType);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(PurchaseType.class, "purchaseType")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseType.class, "purchaseType")));
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
		renderJson(service.delete(getLong(0)));
	}

  /**
	* 切换toggleBPFDefault
	*/
	public void toggleBPFDefault() {
		renderJson(service.toggleBPFDefault(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

  /**
	* 切换toggleBptmpsMrp
	*/
	public void toggleBptmpsMrp() {
		renderJson(service.toggleBptmpsMrp(getLong(0)));
	}

  /**
	* 切换toggleBDefault
	*/
	public void toggleBDefault() {
		renderJson(service.toggleBDefault(getLong(0)));
	}
}
