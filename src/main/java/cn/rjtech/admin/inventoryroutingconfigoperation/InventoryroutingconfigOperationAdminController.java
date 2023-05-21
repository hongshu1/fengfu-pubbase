package cn.rjtech.admin.inventoryroutingconfigoperation;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryroutingconfigOperation;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 物料建模-存货工艺工序
 * @ClassName: InventoryroutingconfigOperationAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:03
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryroutingconfigoperation", viewPath = "/_view/admin/inventoryroutingconfigoperation")
public class InventoryroutingconfigOperationAdminController extends BaseAdminController {

	@Inject
	private InventoryroutingconfigOperationService service;

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
		renderJson(service.save(getModel(InventoryroutingconfigOperation.class, "inventoryroutingconfigOperation")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryroutingconfigOperation inventoryroutingconfigOperation=service.findById(getLong(0));
		if(inventoryroutingconfigOperation == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryroutingconfigOperation",inventoryroutingconfigOperation);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryroutingconfigOperation.class, "inventoryroutingconfigOperation")));
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
