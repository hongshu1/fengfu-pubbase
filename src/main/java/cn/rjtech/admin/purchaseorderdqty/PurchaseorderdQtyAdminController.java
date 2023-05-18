package cn.rjtech.admin.purchaseorderdqty;

import cn.jbolt.core.permission.UnCheck;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.PurchaseorderdQty;
/**
 * 采购/委外订单-采购订单明细数量
 * @ClassName: PurchaseorderdQtyAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-13 14:53
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseorderdqty", viewPath = "/_view/admin/purchaseorderdqty")
public class PurchaseorderdQtyAdminController extends BaseAdminController {

	@Inject
	private PurchaseorderdQtyService service;

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
		render("add().html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(PurchaseorderdQty.class, "purchaseorderdQty")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseorderdQty purchaseorderdQty=service.findById(getLong(0));
		if(purchaseorderdQty == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseorderdQty",purchaseorderdQty);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseorderdQty.class, "purchaseorderdQty")));
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
