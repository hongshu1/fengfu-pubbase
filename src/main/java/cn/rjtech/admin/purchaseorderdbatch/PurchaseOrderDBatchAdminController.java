package cn.rjtech.admin.purchaseorderdbatch;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.PurchaseOrderDBatch;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外管理-采购现品票
 * @ClassName: PurchaseOrderDBatchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-24 11:05
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseorderdbatch", viewPath = "/_view/admin/purchaseorderdbatch")
public class PurchaseOrderDBatchAdminController extends BaseAdminController {

	@Inject
	private PurchaseOrderDBatchService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isEffective")));
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
		renderJson(service.save(getModel(PurchaseOrderDBatch.class, "purchaseOrderDBatch")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseOrderDBatch purchaseOrderDBatch=service.findById(getLong(0));
		if(purchaseOrderDBatch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseOrderDBatch",purchaseOrderDBatch);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseOrderDBatch.class, "purchaseOrderDBatch")));
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
	* 切换isEffective
	*/
	public void toggleIsEffective() {
	    renderJson(service.toggleBoolean(getLong(0),"isEffective"));
	}


}
