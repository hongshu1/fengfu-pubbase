package cn.rjtech.admin.qcform;

import cn.rjtech.model.momdata.QcParam;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcForm;
/**
 * 质量建模-检验表格
 * @ClassName: QcFormAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:53
 */
@CheckPermission(PermissionKey.QCFORM)
@Path(value = "/admin/qcform", viewPath = "/_view/admin/qcform")
public class QcFormAdminController extends BaseAdminController {

	@Inject
	private QcFormService service;

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
		renderJsonData(service.getAdminDatas(getPageSize(), getPageNumber(), getKv()));
	}

   /**
	* 新增
	*/
	public void add() {
		QcParam qcParam = new QcParam();
		Long OrgId = getOrgId();
		String orgCode = getOrgCode();
		String orgName = getOrgName();
		qcParam.setIOrgId(OrgId);
		qcParam.setCOrgCode(orgCode);
		qcParam.setCOrgName(orgName);
		set("qcparam", qcParam);
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(QcForm.class, "qcForm")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		QcForm qcForm=service.findById(getLong(0));
		if(qcForm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcForm",qcForm);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcForm.class, "qcForm")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

   /**
	* 切换isEnabled
	*/
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}


	//选择组合件料品
	public void bomItemCombineData() {
		set("type", get("type"));
		set("excludeCodes", get("excludeCodes"));
		render("qcitem/index.html");
	}

}
