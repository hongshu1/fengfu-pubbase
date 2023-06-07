package cn.rjtech.admin.formextendfields;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.model.momdata.Form;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormExtendFields;

import java.util.ArrayList;
import java.util.List;

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
	private FormService formService;
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
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getLong("iFormId"), getLong("iFormFieldId"), get("cFieldCode"), get("cFieldName")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
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

	/**
	 * 数据源
	 */
	public void optionsiFormFieldId(){
		renderJsonData(formService.find("SELECT cFormName,iAutoId FROM Bd_Form WHERE cFormTypeSn = 2"));
	}
	public void optionsFormId(){
		renderJsonData(formService.find("SELECT cFormName,iAutoId FROM Bd_Form WHERE cFormTypeSn = 1"));
	}

}
