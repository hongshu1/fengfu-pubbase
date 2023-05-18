package cn.rjtech.admin.formapprovaldperson;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormapprovaldPerson;
/**
 * 表单审批流实际审批人 Controller
 * @ClassName: FormapprovaldPersonAdminController
 * @author: RJ
 * @date: 2023-04-24 10:34
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/formapprovaldperson", viewPath = "/_view/admin/formapprovaldperson")
public class FormapprovaldPersonAdminController extends BaseAdminController {

	@Inject
	private FormapprovaldPersonService service;

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
		FormapprovaldPerson formapprovaldPerson=service.findById(getLong(0)); 
		if(formapprovaldPerson == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formapprovaldPerson",formapprovaldPerson);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(FormapprovaldPerson.class, "formapprovaldPerson")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormapprovaldPerson.class, "formapprovaldPerson")));
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
