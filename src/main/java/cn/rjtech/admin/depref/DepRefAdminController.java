package cn.rjtech.admin.depref;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.DepRef;
/**
 * 部门对照档案 Controller
 * @ClassName: DepRefAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 20:58
 */
@CheckPermission(PermissionKey.DEPREF)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/depref", viewPath = "/_view/admin/depref")
public class DepRefAdminController extends BaseAdminController {

	@Inject
	private DepRefService service;

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
		DepRef depRef=service.findById(getLong(0)); 
		if(depRef == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("depRef",depRef);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(DepRef.class, "depRef")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(DepRef.class, "depRef")));
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
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

  /**
	* 切换toggleIsDefault
	*/
	public void toggleIsDefault() {
		renderJson(service.toggleIsDefault(getLong(0)));
	}


}
