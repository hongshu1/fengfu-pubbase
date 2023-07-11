package cn.rjtech.admin.formuploadcategory;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormUploadCategory;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;
/**
 * 分类管理
 * @ClassName: FormUploadCategoryAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:16
 */
@CheckPermission(PermissionKey.FORM_UPLOAD_CATEGORY)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formUploadCategory", viewPath = "/_view/admin/formuploadcategory")
public class FormUploadCategoryAdminController extends BaseAdminController {

	@Inject
	private FormUploadCategoryService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(),getKv()));
	}

   /**
	* 新增
	*/
   @CheckPermission(PermissionKey.FORMUPLOADCATEGORY_ADD)
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
   @CheckPermission(PermissionKey.FORMUPLOADCATEGORY_ADD)
   public void save() {
		renderJson(service.save(getModel(FormUploadCategory.class, "formUploadCategory")));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.FORMUPLOADCATEGORY_EDIT)
	public void edit() {
		FormUploadCategory formUploadCategory=service.findById(getLong(0));
		if(formUploadCategory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formUploadCategory",formUploadCategory);
		render("edit.html");
	}

   /**
	* 更新
	*/
   @CheckPermission(PermissionKey.FORMUPLOADCATEGORY_EDIT)
   public void update() {
		renderJson(service.update(getModel(FormUploadCategory.class, "formUploadCategory")));
	}

   /**
	* 删除
	*/
   @CheckPermission(PermissionKey.FORMUPLOADCATEGORY_DELETE)
	public void delete() {
		renderJson(service.deleteByIds(getLong(0)));
	}

   /**
	* 进入import_excel.html
	*/
	public void initImportExcel() {
		render("import_excel.html");
	}

   /**
	* 下载导入模板
	*/
	public void downloadTpl() {
		renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("上传记录-分类导入模板"));
	}

	/**
	 * 导入
	 */
	@CheckPermission(PermissionKey.FORMUPLOADCATEGORY_IMPORT)
	public void importExcelClass() {
		String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
		UploadFile file = getFile("file", uploadPath);
		if (notExcel(file)) {
			renderJsonFail("请上传excel文件");
			return;
		}
		renderJson(service.importExcelClass(file.getFile()));
	}


    /**
	* 执行导出excel 根据表格选中数据
	*/
	@CheckPermission(PermissionKey.FORMUPLOADCATEGORY_EXPORT)
	public void exportExcelByCheckedIds() {
	    String ids = get("ids");
	    if(notOk(ids)){
	        renderJsonFail("未选择有效数据，无法导出");
        	return;
	    }
		List<Record> datas = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
		if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("生产建模-上传记录_分类"));
	}

	/**
	 * 执行导出excel 所有数据
	 */
	@CheckPermission(PermissionKey.FORMUPLOADCATEGORY_EXPORT)
	public void exportExcelAll() {
		List<Record> datas = service.list(getKv());
		if (notOk(datas)) {
			renderJsonFail("无有效数据导出");
			return;
		}
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("生产建模-上传记录_分类"));
	}

	/**
	 * 获取类别档案
	 */
	@UnCheck
	public void options() {
		renderJsonData(service.options(getKv()));
	}

	/**
	 * 获取相关产线数据
	 */
	public void workregionmOptions(){
		renderJsonData(service.workregionmOptions(getKv()));
	}

	/**
	 * 切换toggleIsenabled
	 */
	public void toggleIsenabled() {
		renderJson(service.toggleIsenabled(getLong(0)));
	}

}
