package cn.rjtech.admin.approvalm;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApprovalM;
/**
 * 审批流主表 Controller
 * @ClassName: ApprovalMAdminController
 * @author: RJ
 * @date: 2023-04-18 17:16
 */
@CheckPermission(PermissionKey.APPROVAL_M)
@UnCheckIfSystemAdmin
@Path(value = "/admin/approvalm", viewPath = "/_view/admin/approvalm")
public class ApprovalMAdminController extends BaseAdminController {

	@Inject
	private ApprovalMService service;

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
		ApprovalM approvalM=service.findById(getLong(0));
		if(approvalM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("approvalM",approvalM);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApprovalM.class, "approvalM")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApprovalM.class, "approvalM")));
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
	* 切换toggleIsEnabled
	*/
	public void toggleIsEnabled() {
		renderJson(service.toggleIsEnabled(getLong(0)));
	}

  /**
	* 切换toggleIsApprovedOnSame
	*/
	public void toggleIsApprovedOnSame() {
		renderJson(service.toggleIsApprovedOnSame(getLong(0)));
	}

  /**
	* 切换toggleIsSkippedOnDuplicate
	*/
	public void toggleIsSkippedOnDuplicate() {
		renderJson(service.toggleIsSkippedOnDuplicate(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


	/**
	 * 提交数据保存方法
	 */
	public void submit(){
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}
}
