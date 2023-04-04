package cn.rjtech.admin.inventorychange;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.rjtech.util.Util;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import cn.rjtech.model.momdata.InventoryChange;
import com.jfinal.plugin.activerecord.Page;
import java.util.List;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryChange;
/**
 * 物料建模-物料形态对照表
 * @ClassName: InventoryChangeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 15:45
 */
@CheckPermission(PermissionKey.INVENTORYCHANGE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventorychange", viewPath = "/_view/admin/inventorychange")
public class InventoryChangeAdminController extends BaseAdminController {

	@Inject
	private InventoryChangeService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getSortColumn("iAutoId"), getSortType("desc"), getKv()));
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
   @CheckPermission(PermissionKey.INVENTORYCHANGE_ADD)
	public void save() {
		renderJson(service.save(getModel(InventoryChange.class, "inventoryChange")));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.INVENTORYCHANGE_EDIT)
	public void edit() {
		Record inventoryChange=service.findByIdRecord(get(0));
		if(inventoryChange == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryChange",inventoryChange);
		render("edit.html");
	}

   /**
	* 更新
	*/
   @CheckPermission(PermissionKey.INVENTORYCHANGE_EDIT)
	public void update() {
		renderJson(service.update(getModel(InventoryChange.class, "inventoryChange")));
	}

   /**
	* 批量删除
	*/
   @CheckPermission(PermissionKey.INVENTORYCHANGE_DEL)
	public void deleteByIds() {
		renderJson(service.removeByIds(get("ids")));
	}

   /**
	* 删除
	*/
   @CheckPermission(PermissionKey.INVENTORYCHANGE_DEL)
	public void delete() {
		renderJson(service.removeByIds(get(0)));
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
		renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("物料建模-物料形态对照表导入模板"));
	}

   /**
	* 执行导入excel
	*/
   @CheckPermission(PermissionKey.INVENTORYCHANGE_IMPORT)
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
   @CheckPermission(PermissionKey.INVENTORYCHANGE_EXPORT)
	public void exportExcelByForm() {
	    Page<Record> pageData = service.getAdminDatas(getPageNumber(), getPageSize(), getSortColumn("iAutoId"), getSortType("desc"), getKv());
	    if(notOk(pageData.getTotalRow())){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(pageData.getList()).setFileName("物料建模-物料形态对照表"));
	}

    /**
	* 执行导出excel 根据表格选中数据
	*/
	@CheckPermission(PermissionKey.INVENTORYCHANGE_EXPORT)
	public void exportExcelByCheckedIds() {
	    String ids = get("ids");
	    if(notOk(ids)){
	        renderJsonFail("未选择有效数据，无法导出");
        	return;
	    }
		Kv kv = getKv().set("ids", Util.getInSqlByIds(ids));
		List<Record> datas = service.findAll(getSortColumn("iAutoId"), getSortType("desc"), kv);
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("物料建模-物料形态对照表"));
	}

    /**
	* 执行导出excel 所有数据
	*/
	@CheckPermission(PermissionKey.INVENTORYCHANGE_EXPORT)
	public void exportExcelAll() {
	    List<Record> datas = service.findAll(getSortColumn("iAutoId"), getSortType("desc"), getKv());
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("物料建模-物料形态对照表"));
	}
	
	/**
	 * 默认给1-100个数据
	 */
	public void inventoryAutocomplete(){
		renderJsonData(service.inventoryAutocomplete(getPageNumber(), 100, getKv()));
	}


}
