package cn.rjtech.admin.inventory;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.admin.bomm.BomMService;
import cn.rjtech.admin.bommaster.BomMasterService;
import cn.rjtech.admin.inventoryaddition.InventoryAdditionService;
import cn.rjtech.admin.inventorymfginfo.InventoryMfgInfoService;
import cn.rjtech.admin.inventoryplan.InventoryPlanService;
import cn.rjtech.admin.inventoryrouting.InventoryRoutingService;
import cn.rjtech.admin.inventorystockconfig.InventoryStockConfigService;
import cn.rjtech.admin.inventoryworkregion.InventoryWorkRegionService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;

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
	@Inject
	private InventoryRoutingService inventoryRoutingService;
	@Inject
	private BomMService bomMService;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
	}

   /**
	* 数据源
	*/
   @UnCheck
	public void options() {
		renderJsonData(service.options(getKv()));
	}

	/**
	 * 用作数据源
	 */
	public void dataBomList(){
		renderJsonData(service.dataBomList());
	}

   /**
	* 新增
	*/
   @CheckPermission(PermissionKey.INVENTORYCLASS_ADD)
   public void add() {
		Long inventoryclassid = getLong("inventoryclassid");
		Inventory inventory = new Inventory();
		inventory.setIInventoryClassId(inventoryclassid);

		//set("centityname", "itemmaster");  //扩展字段
		Long autoid = JBoltSnowflakeKit.me.nextId();
		set("iinventoryid", autoid);
		inventory.setIAutoId(autoid);
		set("inventory", inventory);
		render("add.html");
	}

	/**
	 * 新增或编辑料品
	 */
	public void formSubmit(){
		/*JBoltTable jBoltTable = getJBoltTable();
		Inventory inventory = jBoltTable.getFormBean(Inventory.class, "inventory");
		InventoryStockConfig inventorystockconfig = jBoltTable.getFormBean(InventoryStockConfig.class, "inventorystockconfig");
		InventoryPlan inventoryPlan = jBoltTable.getFormBean(InventoryPlan.class, "inventoryplan");
		InventoryAddition inventoryAddition = jBoltTable.getFormBean(InventoryAddition.class, "inventoryaddition");
		InventoryMfgInfo inventoryMfgInfo = jBoltTable.getFormBean(InventoryMfgInfo.class, "inventorymfginfo");

		Ret result = null;
		Inventory byId = service.findById(inventory.getIAutoId());
		if(inventory != null && byId != null){
			List<InventoryWorkRegion> inventoryWorkRegions = jBoltTable.getUpdateBeanList(InventoryWorkRegion.class);
			List<InventoryWorkRegion> saveBeanList = jBoltTable.getSaveBeanList(InventoryWorkRegion.class);
			Object[] delete = jBoltTable.getDelete();
			result = service.updateForm(inventory,inventoryAddition,inventoryPlan,inventoryMfgInfo,inventorystockconfig,inventoryWorkRegions,saveBeanList,delete);
		}else {
			List<InventoryWorkRegion> inventoryWorkRegions = jBoltTable.getSaveBeanList(InventoryWorkRegion.class);
			result = service.saveForm(inventory,inventoryAddition,inventoryPlan,inventoryMfgInfo,inventorystockconfig,inventoryWorkRegions);
		}

		renderJson(result);*/
	}

   /**
	* 保存
	*/
	public void save() {
		Inventory inventory = getModel(Inventory.class, "inventory");
		Ret save = service.save(inventory);

		renderJson(save);
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.INVENTORYCLASS_EDIT)
   public void edit() {
		Inventory inventory=service.findById(getLong(0));
		if(inventory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		inventory = service.setIItemAttributes(inventory);
		set("inventory",inventory);
		InventoryStockConfig inventorystockconfig = inventoryStockConfigService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		set("inventorystockconfig",inventorystockconfig);
		InventoryAddition inventoryAddition = inventoryAdditionService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		set("inventoryaddition",inventoryAddition);
		InventoryPlan inventoryplan = inventoryPlanService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		set("inventoryplan",inventoryplan);
		InventoryMfgInfo inventoryMfgInfo = inventoryMfgInfoService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		set("inventorymfginfo",inventoryMfgInfo);
		InventoryRouting inventoryRouting = inventoryRoutingService.getCurrentRouting(inventory.getIAutoId());
		if(inventoryRouting != null){
			set("iinventoryroutingid",inventoryRouting.getIAutoId());
			set("iitemroutingname",inventoryRouting.getCRoutingName());
		}
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		Inventory inventory = getModel(Inventory.class, "inventory");
		renderJson(service.update(inventory));
	}

   /**
	* 删除
	*/
   @CheckPermission(PermissionKey.INVENTORYCLASS_DELETE)
	public void delete() {
		Long id = getLong(0);
		inventoryAdditionService.deleteBy(Okv.by("iInventoryId", id));
		inventoryStockConfigService.deleteBy(Okv.by("iInventoryId", id));
		inventoryMfgInfoService.deleteBy(Okv.by("iInventoryId", id));
		inventoryPlanService.deleteBy(Okv.by("iInventoryId", id));
		inventoryWorkRegionService.deleteBy(Okv.by("iInventoryId", id));
		//删除工艺
		Object[] objects = new Object[]{id};
		inventoryRoutingService.deleteMultiByConfigids(objects);
		Ret ret = service.deleteById(id);
		renderJson(ret);
	}

   /**
	* 复制
	*/
   @CheckPermission(PermissionKey.INVENTORYCLASS_COPY)
	public void copy() {
		Long id = getLong(0);
		Inventory inventory=service.findById(id);
		if(inventory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("iAutoId",id);
		render("copy.html");
	}

	public void saveCopy(){
		Long iAutoId = getLong("iAutoId");
		String cInvCode = get("cInvCode");
		Inventory inventory=service.findById(iAutoId);
		if(inventory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		inventory.setCInvCode(cInvCode);
		InventoryStockConfig inventorystockconfig = inventoryStockConfigService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		InventoryAddition inventoryAddition = inventoryAdditionService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		InventoryPlan inventoryplan = inventoryPlanService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		InventoryMfgInfo inventoryMfgInfo = inventoryMfgInfoService.findFirst(Okv.by("iInventoryId", inventory.getIAutoId()), "iAutoId", "DESC");
		List<InventoryWorkRegion> inventoryWorkRegions = inventoryWorkRegionService.getWorkRegions(iAutoId);

		inventory.setIAutoId(null);
		if(inventorystockconfig != null)
			inventorystockconfig.setIAutoId(null);
		if(inventoryAddition != null)
			inventoryAddition.setIAutoId(null);
		if(inventoryplan != null)
			inventoryplan.setIAutoId(null);
		if(inventoryMfgInfo != null)
			inventoryMfgInfo.setIAutoId(null);
		if(inventoryWorkRegions != null && inventoryWorkRegions.size() > 0)
			for (InventoryWorkRegion inventoryWorkRegion : inventoryWorkRegions) {
				inventoryWorkRegion.setIAutoId(null);
			}

		Ret ret = service.saveForm(inventory, inventoryAddition, inventoryplan, inventoryMfgInfo, inventorystockconfig, inventoryWorkRegions, null, null);
		inventoryRoutingService.copy(iAutoId,inventory.getIAutoId());
		renderJson(ret);
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
   @CheckPermission(PermissionKey.INVENTORYCLASS_IMPORT)
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
   @CheckPermission(PermissionKey.INVENTORYCLASS_EXPORT)
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
	@CheckPermission(PermissionKey.INVENTORYCLASS_EXPORT)
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
	@CheckPermission(PermissionKey.INVENTORYCLASS_EXPORT)
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

	@UnCheck
	public void autocomplete2() {
		renderJsonData(service.getAutocompleteData2(get("q"), getInt("limit",100)));
	}

	/**
	 * 上传图片
	 */
	public void uploadImage() {
		//上传到今天的文件夹下
		String todayFolder = JBoltUploadFolder.todayFolder();
		//String uploadPath=JBoltUploadFolder.MALL_GOODS_IMAGE+"/"+todayFolder+"/"+goodsId.toString();
		String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/");
		List<UploadFile> files = getFiles(uploadPath);
		if (files == null || files.size() == 0) {
			renderJsonFail("请选择图片后上传");
			return;
		}
		StringBuilder msg = new StringBuilder();
		files.forEach(file -> {
			if (notImage(file)) {
				msg.append(file.getFileName()).append("不是图片类型文件;");
			}
		});
		if (msg.length() > 0) {
			renderJsonFail(msg.toString());
			return;
		}

		//List<JboltFile> retFiles=new ArrayList<JboltFile>();
		List<String> imgList = new ArrayList<>();
		Inventory itempicture;
		StringBuilder errormsg = new StringBuilder();
		for (UploadFile uploadFile : files) {
			itempicture = service.saveJBoltFile(uploadFile, uploadPath, JboltFile.FILE_TYPE_IMAGE);
			if (itempicture != null) {
				//retFiles.add(jboltFile);
				imgList.add(itempicture.getCPics());
			} else {
				errormsg.append(uploadFile.getOriginalFileName()).append("上传失败;");
			}
		}
		if (imgList.size() == 0) {
			renderJsonFail(errormsg.toString());
			return;
		}
		Long iAutoId = getLong("iAutoId");
		if(iAutoId != null){
			Inventory inventory = service.findById(iAutoId);
			if(inventory != null && imgList != null && imgList.size() > 0){
				StringBuilder cPics = new StringBuilder();
				for (String s : imgList) {
					cPics.append(s);
					cPics.append(",");
				}
				inventory.setCPics(cPics.substring(0,cPics.length()-1));
			}
		}
		renderJsonData(imgList, errormsg.toString());
	}

	public void inventorySpotCheckList() {
		renderJsonData(service.inventorySpotCheckList(getKv()));
	}

    @UnCheck
	public void findBomCompareByBomMasterInvId(){
		renderJsonData(bomMService.findBomCompareByBomMasterInvId(getKv(), getPageNumber(), getPageSize()));
	}

    public void batchFetch(@Para(value = "column") String column,
                           @Para(value = "values") String values) {
        ValidationUtils.notBlank(column, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(values, JBoltMsg.PARAM_ERROR);
        
        renderJson(service.batchFetch(column, values));
    }

    /**
     * 批量获取指定参数
     */
    public void fetchByCinvcode1s(@Para(value = "cinvcode1") String cinvcode1) {
        ValidationUtils.notBlank(cinvcode1, JBoltMsg.PARAM_ERROR);
        
        renderJson(service.fetchByCinvcode1s(cinvcode1));
    }

	@CheckPermission(PermissionKey.INVENTORYCLASS_SUBLIT)
	public void submitMulti() {
		renderJson(service.submitMulti(getJBoltTables()));
	}


	@CheckPermission(PermissionKey.INVENTORYCLASS_IMPORT)
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
