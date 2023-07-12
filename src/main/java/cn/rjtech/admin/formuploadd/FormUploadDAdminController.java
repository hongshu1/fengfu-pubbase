package cn.rjtech.admin.formuploadd;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormUploadD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 制造工单-上传记录明细
 * @ClassName: FormUploadDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:38
 */
@CheckPermission(PermissionKey.FORMUPLOADM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formUploadD", viewPath = "/_view/admin/formuploadd")
public class FormUploadDAdminController extends BaseAdminController {

	@Inject
	private FormUploadDService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(),getKv()));
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
	public void save() {
		renderJson(service.save(getModel(FormUploadD.class, "formUploadD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		FormUploadD formUploadD=service.findById(getLong(0));
		if(formUploadD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formUploadD",formUploadD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormUploadD.class, "formUploadD")));
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
		renderJson(service.delete(getLong(0)));
	}

	/**
	 * 删除相关附件
	 */
	public void deletePicture(@Para(value = "iautoid") Long iautoid,@Para(value = "url") String url){
		ValidationUtils.validateId(iautoid, "记录上传附件ID");
		renderJsonData(service.deletePicture(iautoid,url));

	}
}
