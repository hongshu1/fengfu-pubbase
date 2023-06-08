package cn.rjtech.admin.formuploadcategory;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormUploadCategory;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;

import java.util.List;
/**
 * 分类管理
 * @ClassName: FormUploadCategoryAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:16
 */
@CheckPermission(PermissionKey.FORM_UPLOAD_CATEGORY)
@UnCheckIfSystemAdmin
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
		renderJson(service.save(getModel(FormUploadCategory.class, "formUploadCategory")));
	}

   /**
	* 编辑
	*/
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
	public void update() {
		renderJson(service.update(getModel(FormUploadCategory.class, "formUploadCategory")));
	}

   /**
	* 删除
	*/
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
	* 执行导入excel
	*/
	public void importExcel() {
		String uploadPath=JBoltUploadFolder.todayFolder(JBoltUploadFolder.IMPORT_EXCEL_TEMP_FOLDER);
        UploadFile file=getFile("file",uploadPath);
        if(notExcel(file)){
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcel(file.getFile()));
	}

   /**
	* 执行导出excel 根据查询form表单
	*/
	public void exportExcelByForm() {
		List<FormUploadCategory> allParents = service.getAllParents();
		if(notOk(allParents)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(allParents).setFileName("上传记录-分类"));
	}

    /**
	* 执行导出excel 根据表格选中数据
	*/
	public void exportExcelByCheckedIds() {
	    String ids = get("ids");
	    if(notOk(ids)){
	        renderJsonFail("未选择有效数据，无法导出");
        	return;
	    }
	    List<FormUploadCategory> datas = service.getListByIds(ids);
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("上传记录-分类"));
	}

    /**
	* 执行导出excel 所有数据
	*/
	public void exportExcelAll() {
	    List<FormUploadCategory> datas = service.findAll();
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("上传记录-分类"));
	}

	/**
	 * 获取类别档案
	 */
	@UnCheck
	public void options() {
		renderJsonData(service.list(get("q")));
	}
}
