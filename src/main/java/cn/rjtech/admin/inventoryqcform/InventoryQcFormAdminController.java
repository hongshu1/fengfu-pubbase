package cn.rjtech.admin.inventoryqcform;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.MultipleUploadFile;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.JBoltFileService;
import cn.rjtech.admin.qcform.QcFormService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryQcForm;
import cn.rjtech.model.momdata.QcForm;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 质量建模-检验适用标准
 * @ClassName: InventoryQcFormAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 14:20
 */
@CheckPermission(PermissionKey.ADMIN_INVENTORYQCFORM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryqcform", viewPath = "/_view/admin/inventoryqcform")
public class InventoryQcFormAdminController extends BaseAdminController {

	@Inject
	private InventoryQcFormService service;
	@Inject
	private JBoltFileService jBoltFileService;
	@Inject
	private QcFormService qcFormService;
	@Inject
	private JBoltFileService jboltFileService;


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
   @CheckPermission(PermissionKey.INVENTORYQCFORM_ADD)
	public void add() {
		set("isAdd", 1);
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryQcForm.class, "inventoryQcForm")));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.INVENTORYQCFORM_EDIT)
	public void edit() {
		InventoryQcForm inventoryQcForm=service.findById(getLong(0));
		if(inventoryQcForm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		QcForm qcForm = qcFormService.findById(inventoryQcForm.getIQcFormId());
		if (ObjUtil.isNotNull(qcForm)){
			set("qcFormName", qcForm.getCQcFormName());
		}
		set("inventoryQcForm",inventoryQcForm);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryQcForm.class, "inventoryQcForm")));
	}

   /**
	* 批量删除
	*/
   @CheckPermission(PermissionKey.INVENTORYQCFORM_DELETE)
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
	* 进入import_excel.html
	*/
	public void initImportExcel() {
		render("import_excel.html");
	}

   /**
	* 下载导入模板
	*/
	public void downloadTpl() {
		renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("质量建模-检验适用标准导入模板"));
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
	    Page<InventoryQcForm> pageData = service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("iAutoId"), getSortType("desc"), getBoolean("IsDeleted"), get("machineType"), get("inspectionType"));
	    if(notOk(pageData.getTotalRow())){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(pageData.getList()).setFileName("质量建模-检验适用标准"));
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
	    List<InventoryQcForm> datas = service.getListByIds(ids);
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("质量建模-检验适用标准"));
	}

   /**
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}

   /**
	* 切换iQcFormName
	*/
	public void toggleIQcFormName() {
	    renderJson(service.toggleBoolean(getLong(0),"iQcFormName"));
	}

   /**
	* 切换machineType
	*/
	public void toggleMachineType() {
	    renderJson(service.toggleBoolean(getLong(0),"machineType"));
	}

   /**
	* 切换iInventoryCode
	*/
	public void toggleIInventoryCode() {
	    renderJson(service.toggleBoolean(getLong(0),"iInventoryCode"));
	}

   /**
	* 切换iInventoryName
	*/
	public void toggleIInventoryName() {
	    renderJson(service.toggleBoolean(getLong(0),"iInventoryName"));
	}

   /**
	* 切换CustomerManager
	*/
	public void toggleCustomerManager() {
	    renderJson(service.toggleBoolean(getLong(0),"CustomerManager"));
	}

   /**
	* 切换componentName
	*/
	public void toggleComponentName() {
	    renderJson(service.toggleBoolean(getLong(0),"componentName"));
	}

   /**
	* 切换specs
	*/
	public void toggleSpecs() {
	    renderJson(service.toggleBoolean(getLong(0),"specs"));
	}

   /**
	* 切换unit
	*/
	public void toggleUnit() {
	    renderJson(service.toggleBoolean(getLong(0),"unit"));
	}

   /**
	* 切换inspectionType
	*/
	public void toggleInspectionType() {
	    renderJson(service.toggleBoolean(getLong(0),"inspectionType"));
	}

	/**
	 * 检验表格弹窗
	 */
	public void chooseFormData(){
		render("formDialog.html");
	}

	/**
	 * 检验表格数据源
	 */
    @UnCheck
	public void getFormList(){
		renderJsonData(service.getFormList(getKv()));
	}

	/**
	 * 行数据数据源
	 */
	public void listData(){
		renderJsonData(service.listData(getKv()));
	}

	/**
	 * 拉取资源Dialog
	 */
	public void chooseItem(){
		String itemHidden = get("itemHidden");
		set("itemHidden", itemHidden);
		render("resource_list.html");
	}

	/**
	 * 获取资源的数据源
	 */
	public void resourceList(){
		renderJsonData(service.resourceList(getKv(),getPageNumber(),getPageSize()));
	}

	/**
	 * 提交数据保存方法
	 */
	@CheckPermission(PermissionKey.INVENTORYQCFORM_SUBMIT)
	public void submit(){
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

	/**
	 * 打开文件上传Dialog
	 */
	public void openFileDialog(@Para(value = "iinventoryid") Long invId, @Para(value = "cpics") String cpics){
		keepPara();
		render("file_dialog.html");
	}

	/**
	 * 多文件上传
	 */
	public void uploadFileAndSave(){
		
		//上传到今天的文件夹下
		String uploadPath=JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_IMAGE_UPLOADER);
		List<UploadFile> files=getFiles(uploadPath);
		if(files==null || files.size()==0) {
			renderJsonFail("请选择图片后上传");
			return;
		}
		StringBuilder msg = new StringBuilder();
		files.forEach(file->{
			if(notImage(file)){
				msg.append(file.getFileName()+"不是图片类型文件;");
			}
		});
		if(msg.length()>0) {
			renderJsonFail(msg.toString());
			return;
		}
		
		List<String> retFiles=new ArrayList<String>();
		Ret ret;
		StringBuilder errormsg = new StringBuilder();
		for(UploadFile uploadFile:files) {
			ret=jBoltFileService.saveImageFile(uploadFile,uploadPath);
			if(ret.isOk()){
				retFiles.add(ret.getStr("data"));
			}else {
				errormsg.append(uploadFile.getOriginalFileName()+"上传失败;");
			}
		}
		if(retFiles.size()==0) {
			renderJsonFail(errormsg.toString());
			return;
		}
		renderJsonData(retFiles,errormsg.toString());
		
	}
	
	
	

	/**
	 * 删除文件信息
	 */
	public void deleteFile(){
		renderJson(service.deleteFile(getKv()));
	}

	/**
	 * 上传文件 同步批量上传文件
	 */
	public void upload() {
		//上传到今天的文件夹下
		String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_FILE_UPLOADER);
		List<UploadFile> files = getFiles(uploadPath);
		if (!isOk(files)) {
			renderBootFileUploadFail("文件上传失败!");
			return;
		}
		List<MultipleUploadFile> retFiles = new ArrayList<MultipleUploadFile>();
		Ret ret;
		for (UploadFile uploadFile : files) {
			ret = jboltFileService.saveVideoFile(uploadFile, uploadPath);
			retFiles.add(new MultipleUploadFile(uploadFile.getOriginalFileName(), uploadFile.getOriginalFileName(), ret.getStr("data")));
		}
		renderJsonData(retFiles);
	}

	@CheckPermission(PermissionKey.INVENTORYQCFORM_IMPORT)
	public void importExcelClass() {
		String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
		UploadFile file = getFile("file", uploadPath);
		if (notExcel(file)) {
			renderJsonFail("请上传excel文件");
			return;
		}
		renderJson(service.importExcelClass(file.getFile()));
	}


}
