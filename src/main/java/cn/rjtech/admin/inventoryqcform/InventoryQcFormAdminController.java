package cn.rjtech.admin.inventoryqcform;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.service.JBoltFileService;
import cn.jbolt.extend.config.ExtendUploadFolder;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;
import cn.rjtech.model.momdata.InventoryQcForm;
import com.jfinal.plugin.activerecord.Page;
import java.util.List;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryQcForm;
import org.apache.commons.lang3.StringUtils;

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
//	    renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("iAutoId"), getSortType("desc"), getBoolean("IsDeleted"), get("machineType"), get("inspectionType")));
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
		renderJson(service.save(getModel(InventoryQcForm.class, "inventoryQcForm")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryQcForm inventoryQcForm=service.findById(getLong(0));
		if(inventoryQcForm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
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
		String itemHidden = get("itemHidden");
		String keywords = StringUtils.trim(get("keywords"));
		String customerManager = StringUtils.trim(get("CustomerManager"));
		String componentName = StringUtils.trim(get("componentName"));
		Kv kv = new Kv();
		kv.setIfNotNull("itemHidden", itemHidden);
		kv.setIfNotNull("customerManager", customerManager);
		kv.setIfNotNull("keywords", keywords);
		kv.setIfNotNull("componentName", componentName);
		renderJsonData(service.resourceList(kv,getPageNumber(),getPageSize()));
	}

	/**
	 * 提交数据保存方法
	 */
	public void submit(){
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

	/**
	 * 打开文件上传Dialog
	 */
	public void openFileDialog(){
		Long lineId = getLong(0);
		set("lineId", lineId);
		InventoryQcForm inventoryQcForm=service.findById(lineId);
		if(inventoryQcForm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}

		List<JboltFile> jboltFiles = jBoltFileService.getListByIds(inventoryQcForm.getCPics());
		set("files", jboltFiles);
		render("file_dialog.html");
	}

	/**
	 * 多文件上传
	 */
	public void uploadFileAndSave(){
		//上传到今天的文件夹下
		String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.SRM_JL_PO_DELIVERY_FILE);
		List<UploadFile> files = getFiles(uploadPath);
		if (files == null || files.size() == 0) {
			renderJsonFail("请选择文件后上传");
			return;
		}

		Long lineId = getLong("lineId");
		//保存文件 并 保存到行数据里
		Ret ret = service.saveFileAndUpdateLine(files, uploadPath, lineId);
		List<JboltFile> res = ret.isOk()? (List<JboltFile>) ret.get("data") :null;
		renderJsonData(res,ret.getStr("msg"));
	}

	/**
	 * 删除文件信息
	 */
	public void deleteFile(){
		renderJson(service.deleteFile(getKv()));
	}
}
