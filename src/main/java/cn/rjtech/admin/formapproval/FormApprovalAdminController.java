package cn.rjtech.admin.formapproval;

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
import cn.rjtech.model.momdata.FormApproval;
/**
 * 表单审批流 Controller
 * @ClassName: FormApprovalAdminController
 * @author: RJ
 * @date: 2023-04-18 17:26
 */
@CheckPermission(PermissionKey.APPROVAL_CONFIG)
@UnCheckIfSystemAdmin
@Path(value = "/admin/formapproval", viewPath = "/_view/admin/formapproval")
public class FormApprovalAdminController extends BaseAdminController {

	@Inject
	private FormApprovalService service;

   /**
	* 首页
	*/
	public void index() {
		render("index().html");
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
		FormApproval formApproval=service.findById(getLong(0));
		if(formApproval == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formApproval",formApproval);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(FormApproval.class, "formApproval")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormApproval.class, "formApproval")));
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
	* 切换toggleIsSkippedOnDuplicate
	*/
	public void toggleIsSkippedOnDuplicate() {
		renderJson(service.toggleIsSkippedOnDuplicate(getLong(0)));
	}

  /**
	* 切换toggleIsApprovedOnSame
	*/
	public void toggleIsApprovedOnSame() {
		renderJson(service.toggleIsApprovedOnSame(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

	/**
	 * 判断走审核还是审批
	 */
	public void judgeType(){
        String formSn = get("formSn"); //表单编码
        String formAutoId = get("formAutoId"); //单据ID
        Kv kv = new Kv();
        kv.set("formSn",formSn);
        kv.set("formAutoId",formAutoId);
        renderJson(service.judgeType(kv));
	}

	/**
	 * 审批
	 */
	public void approve(){
		String formAutoId = get("formAutoId"); //单据ID
		String formSn = get("formSn");
		Integer status = getInt("status");
		Kv kv = new Kv();
		kv.set("formAutoId",formAutoId);
		kv.set("formSn",formSn);
		kv.set("status",status);
		renderJson(service.approve(kv));
	}
}
