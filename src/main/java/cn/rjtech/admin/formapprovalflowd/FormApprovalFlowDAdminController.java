package cn.rjtech.admin.formapprovalflowd;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormApprovalFlowD;
/**
 * 单据审批流 Controller
 * @ClassName: FormApprovalFlowDAdminController
 * @author: RJ
 * @date: 2023-05-05 10:18
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/formapprovalflowd", viewPath = "/_view/admin/formapprovalflowd")
public class FormApprovalFlowDAdminController extends BaseAdminController {

	@Inject
	private FormApprovalFlowDService service;

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
		FormApprovalFlowD formApprovalFlowD=service.findById(getLong(0)); 
		if(formApprovalFlowD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formApprovalFlowD",formApprovalFlowD);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(FormApprovalFlowD.class, "formApprovalFlowD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormApprovalFlowD.class, "formApprovalFlowD")));
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
