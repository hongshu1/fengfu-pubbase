package cn.rjtech.admin.saletype;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SaleType;
/**
 * 销售类型 Controller
 * @ClassName: SaleTypeAdminController
 * @author: WYX
 * @date: 2023-03-28 11:04
 */
@CheckPermission(PermissionKey.SALETYPE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/saletype", viewPath = "/_view/admin/saletype")
public class SaleTypeAdminController extends BaseAdminController {

	@Inject
	private SaleTypeService service;

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

	public void selectData (){
		renderJsonData(service.selectData(getKv()));
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
		SaleType saleType=service.findById(getLong(0)); 
		if(saleType == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("saleType",saleType);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(SaleType.class, "saleType")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SaleType.class, "saleType")));
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
	* 切换toggleBstmpsMrp
	*/
	public void toggleBstmpsMrp() {
		renderJson(service.toggleBstmpsMrp(getLong(0)));
	}

  /**
	* 切换toggleBDefault
	*/
	public void toggleBDefault() {
		renderJson(service.toggleBDefault(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

}
