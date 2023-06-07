package cn.rjtech.admin.formapprovaldroleusers;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormapprovaldRoleusers;
/**
 * 单据角色人员 Controller
 * @ClassName: FormapprovaldRoleusersAdminController
 * @author: RJ
 * @date: 2023-06-05 14:32
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/formapprovaldroleusers", viewPath = "/_view/admin/formapprovaldroleusers")
public class FormapprovaldRoleusersAdminController extends BaseAdminController {

	@Inject
	private FormapprovaldRoleusersService service;

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
		FormapprovaldRoleusers formapprovaldRoleusers=service.findById(getLong(0)); 
		if(formapprovaldRoleusers == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formapprovaldRoleusers",formapprovaldRoleusers);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(FormapprovaldRoleusers.class, "formapprovaldRoleusers")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormapprovaldRoleusers.class, "formapprovaldRoleusers")));
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