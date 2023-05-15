package cn.rjtech.admin.materialsout;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MaterialsOut;
/**
 * 出库管理-材料出库单列表 Controller
 * @ClassName: MaterialsOutAdminController
 * @author: RJ
 * @date: 2023-05-13 16:16
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/materialsout", viewPath = "/_view/admin/materialsout")
public class MaterialsOutAdminController extends BaseAdminController {

	@Inject
	private MaterialsOutService service;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
  	
  	/**
	* 数据源
	*/

	/**
	 * 数据源
	 */
	public void datas() {
		Kv kv =new Kv();
		kv.setIfNotNull("selectparam", get("billno"));
		kv.setIfNotNull("selectparam", get("sourcebillno"));
		kv.setIfNotNull("selectparam", get("whname"));
		kv.setIfNotNull("selectparam", get("deptname"));
		kv.setIfNotNull("iorderstatus", get("iorderstatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),kv));
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
		MaterialsOut materialsOut=service.findById(getLong(0)); 
		if(materialsOut == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("materialsOut",materialsOut);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MaterialsOut.class, "materialsOut")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MaterialsOut.class, "materialsOut")));
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
