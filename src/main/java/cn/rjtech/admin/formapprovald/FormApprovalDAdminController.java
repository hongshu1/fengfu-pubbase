package cn.rjtech.admin.formapprovald;

import cn.rjtech.admin.approvald.ApprovalDService;
import cn.rjtech.model.momdata.ApprovalD;
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
import cn.rjtech.model.momdata.FormApprovalD;
/**
 * 表单审批流审批节点 Controller
 * @ClassName: FormApprovalDAdminController
 * @author: RJ
 * @date: 2023-04-18 17:29
 */
@CheckPermission(PermissionKey.APPROVAL_CONFIG)
@UnCheckIfSystemAdmin
@Path(value = "/admin/formapprovald", viewPath = "/_view/admin/formapprovald")
public class FormApprovalDAdminController extends BaseAdminController {

	@Inject
	private FormApprovalDService service;

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
		String iFormObjectId = get("iFormObjectId");
		Kv kv = new Kv();
		kv.set("formId",isOk(iFormObjectId)?iFormObjectId:' ');
		renderJsonData(service.datas(getPageNumber(),getPageSize(),kv));
	}

	/**
	 * 历史数据源
	 */
	public void historyDatas() {
		String iFormObjectId = get("iFormObjectId");
		Kv kv = new Kv();
		kv.set("formId",isOk(iFormObjectId)?iFormObjectId:' ');
		renderJsonData(service.historyDatas(getPageNumber(),getPageSize(),kv));
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
		FormApprovalD approvalD=service.findById(getLong(0));
		if(approvalD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formApprovalD",approvalD);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(FormApprovalD.class, "formApprovalD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormApprovalD.class, "formApprovalD")));
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
	* 切换toggleIsDirectOnMissing
	*/
	public void toggleIsDirectOnMissing() {
		renderJson(service.toggleIsDirectOnMissing(getLong(0)));
	}

	/**
	 * 查询节点上所有审批人进入页面
	 */
	public void userList() {
		set("approvalDid", getLong(0));
		System.out.println("getLong(0)"+getLong(0));
		render("users.html");
	}

	/**
	 * 审批人列表数据源
	 */
	public void userDatas(){
		String approvalDId = get("approvalDid");
		Kv kv = new Kv();
		kv.set("approvalDId",isOk(approvalDId)?approvalDId:' ');
		renderJsonData(service.userDatas(getPageNumber(),getPageSize(),kv));
	}

	/**
	 * 人员行表数据源
	 */
	public void lineDatas(){
		String Mid = get("formApprovalD.iautoid");
		Kv kv = new Kv();
		kv.set("mid",Mid==null?' ':Mid);
		renderJsonData(service.lineDatas(getPageNumber(),getPageSize(),kv));
	}

	/**
	 * 人员行表数据源
	 */
	public void roleDatas(){
		String Mid = get("formApprovalD.iautoid");
		Kv kv = new Kv();
		kv.set("mid",Mid==null?' ':Mid);
		renderJsonData(service.roleDatas(getPageNumber(),getPageSize(),kv));
	}

	/**
	 * 保存方法
	 */
	public void submit(){
		renderJson(service.submitByJBoltTables(getJBoltTables()));
	}
}
