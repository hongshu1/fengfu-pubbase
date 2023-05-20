package cn.rjtech.admin.materialsoutdetail;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MaterialsOutDetail;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 出库管理-材料出库单列表 Controller
 * @ClassName: MaterialsOutDetailAdminController
 * @author: RJ
 * @date: 2023-05-13 16:09
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/materialsoutdetail", viewPath = "/_view/admin/materialsoutdetail")
public class MaterialsOutDetailAdminController extends BaseAdminController {

	@Inject
	private MaterialsOutDetailService service;

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
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		MaterialsOutDetail materialsOutDetail=service.findById(getLong(0)); 
		if(materialsOutDetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("materialsOutDetail",materialsOutDetail);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MaterialsOutDetail.class, "materialsOutDetail")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MaterialsOutDetail.class, "materialsOutDetail")));
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
