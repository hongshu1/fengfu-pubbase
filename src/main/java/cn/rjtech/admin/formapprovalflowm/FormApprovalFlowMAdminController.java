package cn.rjtech.admin.formapprovalflowm;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormApprovalFlowM;
/**
 * 单据审批流 Controller
 * @ClassName: FormApprovalFlowMAdminController
 * @author: RJ
 * @date: 2023-05-05 10:19
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/formapprovalflowm", viewPath = "/_view/admin/formapprovalflowm")
public class FormApprovalFlowMAdminController extends BaseAdminController {

	@Inject
	private FormApprovalFlowMService service;

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
		FormApprovalFlowM formApprovalFlowM=service.findById(getLong(0)); 
		if(formApprovalFlowM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formApprovalFlowM",formApprovalFlowM);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(FormApprovalFlowM.class, "formApprovalFlowM")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormApprovalFlowM.class, "formApprovalFlowM")));
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
