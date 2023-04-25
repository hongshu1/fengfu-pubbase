package cn.rjtech.admin.purchaseorderdbatchversion;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.PurchaseOrderDBatchVersion;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外管理-采购现品票版本记录
 * @ClassName: PurchaseOrderDBatchVersionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-24 11:05
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseorderdbatchversion", viewPath = "/_view/admin/purchaseorderdbatchversion")
public class PurchaseOrderDBatchVersionAdminController extends BaseAdminController {

	@Inject
	private PurchaseOrderDBatchVersionService service;

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
		renderJson(service.save(getModel(PurchaseOrderDBatchVersion.class, "purchaseOrderDBatchVersion")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseOrderDBatchVersion purchaseOrderDBatchVersion=service.findById(getLong(0));
		if(purchaseOrderDBatchVersion == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseOrderDBatchVersion",purchaseOrderDBatchVersion);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseOrderDBatchVersion.class, "purchaseOrderDBatchVersion")));
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


}
