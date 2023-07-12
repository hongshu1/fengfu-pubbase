package cn.rjtech.admin.formextendfields;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormExtendFields;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 系统管理-拓展字段配置表
 * @ClassName: FormExtendFieldsAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-01 11:37
 */
@CheckPermission(PermissionKey.FORM_EXTEND_FIELDS)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formExtendFields", viewPath = "/_view/admin/formextendfields")
public class FormExtendFieldsAdminController extends BaseAdminController {

	@Inject
	private FormExtendFieldsService service;
   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
   /**
	* 数据源
	*/
   @UnCheck
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getLong("iFormId"), getLong("iFormFieldId"), get("cFieldCode"), get("cFieldName"),get("ifform")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}
	public void addForm() {
		render("addForm.html");
	}


	/**
	* 保存
	*/
	public void save(@Para("formExtendFields")FormExtendFields formExtendFields) {
		renderJson(service.save(formExtendFields));
	}

   /**
	* 编辑
	*/
	public void edit() {
		FormExtendFields formExtendFields=service.findById(getLong(0));
		if(formExtendFields == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formExtendFields",formExtendFields);
		render("edit.html");
	}
	public void editForm() {
		FormExtendFields formExtendFields=service.findById(getLong(0));
		if(formExtendFields == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formExtendFields",formExtendFields);
		render("editForm.html");
	}
   /**
	* 更新
	*/
	public void update(@Para("formExtendFields")FormExtendFields formExtendFields) {
		renderJson(service.update(formExtendFields));
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

}
