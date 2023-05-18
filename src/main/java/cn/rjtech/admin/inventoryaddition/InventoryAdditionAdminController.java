package cn.rjtech.admin.inventoryaddition;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryAddition;
/**
 * 存货档案-附加
 * @ClassName: InventoryAdditionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:59
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryaddition", viewPath = "/_view/admin/inventoryaddition")
public class InventoryAdditionAdminController extends BaseAdminController {

	@Inject
	private InventoryAdditionService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("privatedescseg9")));
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
		renderJson(service.save(getModel(InventoryAddition.class, "inventoryAddition")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryAddition inventoryAddition=service.findById(getLong(0));
		if(inventoryAddition == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryAddition",inventoryAddition);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryAddition.class, "inventoryAddition")));
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
	* 切换privatedescseg9
	*/
	public void togglePrivatedescseg9() {
	    renderJson(service.toggleBoolean(getLong(0),"privatedescseg9"));
	}


}
