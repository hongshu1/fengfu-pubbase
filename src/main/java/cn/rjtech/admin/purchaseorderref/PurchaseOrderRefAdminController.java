package cn.rjtech.admin.purchaseorderref;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.PurchaseOrderRef;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外-采购订单与到货计划关联
 * @ClassName: PurchaseOrderRefAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-23 11:18
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseorderref", viewPath = "/_view/admin/purchaseorderref")
public class PurchaseOrderRefAdminController extends BaseAdminController {

	@Inject
	private PurchaseOrderRefService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
		renderJson(service.save(getModel(PurchaseOrderRef.class, "purchaseOrderRef")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseOrderRef purchaseOrderRef=service.findById(getLong(0));
		if(purchaseOrderRef == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseOrderRef",purchaseOrderRef);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseOrderRef.class, "purchaseOrderRef")));
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
