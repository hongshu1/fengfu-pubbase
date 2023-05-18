package cn.rjtech.admin.approvalduser;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApprovaldUser;
/**
 * 审批流节点审批人 Controller
 * @ClassName: ApprovaldUserAdminController
 * @author: RJ
 * @date: 2023-04-18 17:12
 */
@CheckPermission(PermissionKey.APPROVAL_CONFIG)
@UnCheckIfSystemAdmin
@Path(value = "/admin/approvalduser", viewPath = "/_view/admin/approvalduser")
public class ApprovaldUserAdminController extends BaseAdminController {

	@Inject
	private ApprovaldUserService service;

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
		ApprovaldUser approvaldUser=service.findById(getLong(0));
		if(approvaldUser == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("approvaldUser",approvaldUser);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApprovaldUser.class, "approvaldUser")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApprovaldUser.class, "approvaldUser")));
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
