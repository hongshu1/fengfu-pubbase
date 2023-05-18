package cn.rjtech.admin.inventoryqcformd;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryQcFormD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 质量管理-来料检单行配置表 Controller
 * @ClassName: InventoryQcFormDAdminController
 * @author: RJ
 * @date: 2023-04-13 16:39
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryqcformd", viewPath = "/_view/admin/inventoryqcformd")
public class InventoryQcFormDAdminController extends BaseAdminController {

	@Inject
	private InventoryQcFormDService service;

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

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryQcFormD inventoryQcFormD=service.findById(getLong(0)); 
		if(inventoryQcFormD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryQcFormD",inventoryQcFormD);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryQcFormD.class, "inventoryQcFormD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryQcFormD.class, "inventoryQcFormD")));
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


}
