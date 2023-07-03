package cn.rjtech.admin.fitemss97class;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Fitemss97class;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 项目管理项目分类目录 Controller
 * @ClassName: Fitemss97classAdminController
 * @author: WYX
 * @date: 2023-03-25 16:44
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.FITEM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/fitemss97class", viewPath = "/_view/admin/fitemss97class")
public class Fitemss97classAdminController extends BaseAdminController {

	@Inject
	private Fitemss97classService service;

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
		Fitemss97class fitemss97class=service.findById(getLong(0)); 
		if(fitemss97class == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("fitemss97class",fitemss97class);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Fitemss97class.class, "fitemss97class")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Fitemss97class.class, "fitemss97class")));
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


}
