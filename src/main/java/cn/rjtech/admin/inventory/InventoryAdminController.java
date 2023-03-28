package cn.rjtech.admin.inventory;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.inventoryaddition.InventoryAdditionService;
import cn.rjtech.admin.inventorymfginfo.InventoryMfgInfoService;
import cn.rjtech.admin.inventoryplan.InventoryPlanService;
import cn.rjtech.admin.inventorystockconfig.InventoryStockConfigService;
import cn.rjtech.admin.inventoryworkregion.InventoryWorkRegionService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinal.plugin.activerecord.Page;
import java.util.List;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Inventory;
import net.sf.cglib.asm.$ClassTooLargeException;

/**
 * 物料建模-存货档案
 * @ClassName: InventoryAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 09:35
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventory", viewPath = "/_view/admin/inventory")
public class InventoryAdminController extends BaseAdminController {

	@Inject
	private InventoryService service;
	@Inject
	private InventoryStockConfigService inventoryStockConfigService;
	@Inject
	private InventoryMfgInfoService inventoryMfgInfoService;
	@Inject
	private InventoryPlanService inventoryPlanService;
	@Inject
	private InventoryAdditionService inventoryAdditionService;
	@Inject
	private InventoryWorkRegionService inventoryWorkRegionService;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
		Inventory inventory = getModel(Inventory.class, "inventory");
		Ret save = service.save(inventory);
		//保存库存
		InventoryStockConfig inventorystockconfig = getModel(InventoryStockConfig.class, "inventorystockconfig");
		inventorystockconfig.setIInventoryId(inventory.getIAutoId());
		inventoryStockConfigService.save(inventorystockconfig);
		/*//保持生产档案
		InventoryMfgInfo inventoryMfgInfo = getModel(InventoryMfgInfo.class, "InventoryMfgInfo");
		inventoryMfgInfo.setIInventoryId(inventory.getIAutoId());
		inventoryMfgInfoService.save(inventoryMfgInfo);
		//保存计划档案
		InventoryPlan inventoryPlan = getModel(InventoryPlan.class, "InventoryPlan");
		inventoryPlan.setIInventoryId(inventory.getIAutoId());
		inventoryPlanService.save(inventoryPlan);
		//保存附加
		InventoryAddition inventoryAddition = getModel(InventoryAddition.class, "inventoryaddition");
		inventoryAddition.setIInventoryId(inventory.getIAutoId());
		inventoryAdditionService.save(inventoryAddition);
		//保存产线列表
		InventoryWorkRegion inventoryWorkRegion = getModel(InventoryWorkRegion.class, "InventoryWorkRegion");
		inventoryWorkRegion.setIInventoryId(inventory.getIAutoId());
		inventoryWorkRegionService.save(inventoryWorkRegion);*/
		renderJson(save);
	}

   /**
	* 编辑
	*/
	public void edit() {
		Inventory inventory=service.findById(getLong(0));
		if(inventory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventory",inventory);
		InventoryStockConfig inventorystockconfig = inventoryStockConfigService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		set("inventorystockconfig",inventorystockconfig);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Inventory.class, "inventory")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 复制
	*/
	public void copy() {
		Inventory inventory=service.findById(getLong(0));
		if(inventory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		renderJson(service.copy(inventory));
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
		renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("物料建模-存货档案导入模板"));
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
	    Page<Record> pageData = service.getAdminDatas(getPageNumber(), getPageSize(),getKv());
	    if(notOk(pageData.getTotalRow())){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(pageData.getList()).setFileName("物料建模-存货档案"));
	}

    /**
	* 执行导出excel 根据表格选中数据
	*/
	public void exportExcelByCheckedIds() {
		String ids = get("ids");
		Kv kv = getKv();
		if (ids != null) {
			String[] split = ids.split(",");
			String sqlids = "";
			for (String id : split) {
				sqlids += "'" + id + "',";
			}
			ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
			sqlids = sqlids.substring(0, sqlids.length() - 1);
			kv.set("sqlids", sqlids);
		}
	    List<Record> datas = service.getAdminDatasNoPage(kv);
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("物料建模-存货档案"));
	}

    /**
	* 执行导出excel 所有数据
	*/
	public void exportExcelAll() {
	    List<Record> datas = service.getAdminDatasNoPage(getKv());
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("物料建模-存货档案"));
	}
	/**
	  * 获取存货档案列表 
	  * 通过关键字匹配 
	 * autocomplete组件使用
	 */
	@UnCheck
	public void autocomplete() {
		renderJsonData(service.getAutocompleteData(get("q"), getInt("limit",10)));
	}

}
