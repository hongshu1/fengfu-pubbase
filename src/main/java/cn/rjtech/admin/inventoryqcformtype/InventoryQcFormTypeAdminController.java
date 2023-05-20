package cn.rjtech.admin.inventoryqcformtype;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryQcFormType;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 质量建模-检验适用标准类型
 * @ClassName: InventoryQcFormTypeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 19:01
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryQcFormType", viewPath = "/_view/admin/inventoryQcFormType")
public class InventoryQcFormTypeAdminController extends BaseAdminController {

	@Inject
	private InventoryQcFormTypeService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iType"), get("cTypeName")));
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
		renderJson(service.save(getModel(InventoryQcFormType.class, "inventoryQcFormType")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryQcFormType inventoryQcFormType=service.findById(getLong(0));
		if(inventoryQcFormType == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryQcFormType",inventoryQcFormType);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryQcFormType.class, "inventoryQcFormType")));
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
