package cn.rjtech.admin.approvaldroleusers;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApprovaldRoleusers;
/**
 * 角色人员 Controller
 * @ClassName: ApprovaldRoleusersAdminController
 * @author: RJ
 * @date: 2023-06-05 14:30
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/approvaldroleusers", viewPath = "/_view/admin/approvaldroleusers")
public class ApprovaldRoleusersAdminController extends BaseAdminController {

	@Inject
	private ApprovaldRoleusersService service;

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
		ApprovaldRoleusers approvaldRoleusers=service.findById(getLong(0)); 
		if(approvaldRoleusers == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("approvaldRoleusers",approvaldRoleusers);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApprovaldRoleusers.class, "approvaldRoleusers")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApprovaldRoleusers.class, "approvaldRoleusers")));
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
