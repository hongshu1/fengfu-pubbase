package cn.rjtech.admin.fitemss97;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Fitemss97;
/**
 * 项目管理大类项目主目录 Controller
 * @ClassName: Fitemss97AdminController
 * @author: heming
 * @date: 2023-03-27 09:29
 */
@CheckPermission(PermissionKey.FITEMSS97)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/fitemss97", viewPath = "/_view/admin/fitemss97")
public class Fitemss97AdminController extends BaseAdminController {

	@Inject
	private Fitemss97Service service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		Fitemss97 fitemss97=service.findById(getLong(0)); 
		if(fitemss97 == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("fitemss97",fitemss97);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Fitemss97.class, "fitemss97")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Fitemss97.class, "fitemss97")));
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
	* 切换toggleBclose
	*/
	public void toggleBclose() {
		renderJson(service.toggleBclose(getLong(0)));
	}
  /**
	* 切换toggleIotherused
	*/
	public void toggleIotherused() {
		renderJson(service.toggleIotherused(getLong(0)));
	}  
}
