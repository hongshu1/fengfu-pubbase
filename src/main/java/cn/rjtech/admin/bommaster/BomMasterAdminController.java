package cn.rjtech.admin.bommaster;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.rjtech.admin.bomcompare.BomCompareService;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.BomMaster;
import com.jfinal.upload.UploadFile;

import java.io.IOException;

/**
 * 物料建模-Bom母项
 * @ClassName: BomMasterAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 16:39
 */

@Path(value = "/admin/bommaster", viewPath = "/_view/admin/bommaster")
public class BomMasterAdminController extends BaseAdminController {

	@Inject
	private BomMasterService service;
	@Inject
	private InventoryChangeService inventoryChangeService;
	@Inject
	private EquipmentModelService equipmentModelService;
	@Inject
	private InventoryService inventoryService;
	@Inject
	private CustomerService customerService;
	@Inject
	private BomCompareService bomCompareService;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   

   /**
	* 编辑
	*/
	public void edit() {
		getBomMaster(getLong(0));
		render("edit.html");
	}
	
	private void getBomMaster(Long id){
		BomMaster bomMaster=service.findById(id);
		if(bomMaster == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("index", bomCompareService.queryCompareIndex(id));
		set("equipmentModel", equipmentModelService.findById(bomMaster.getIEquipmentModelId()));
		set("inventory", inventoryService.findById(bomMaster.getIInventoryId()));
		set("bomMaster",bomMaster);
	}
	
	/**
	 * 查看
	 */
	public void info(){
		getBomMaster(getLong(0));
		set("view",1);
		render("edit.html");
	}
	
	public void findByBomMasterId(){
		renderJsonData(bomCompareService.findByBomMasterId(getLong(0)));
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
	* 切换isEnabled
	*/
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}
	
	/**
	 * 默认给1-100个数据
	 */
	public void inventoryAutocomplete(){
		renderJsonData(inventoryChangeService.inventoryAutocomplete(getPageNumber(), 200, getKv()));
	}
	
	public void submitForm(@Para(value = "formJsonData") String formJsonData,
						   @Para(value = "tableJsonData") String tableJsonData,
						   @Para(value="commonInvData") String commonInvData,
						   @Para(value = "flag") Boolean flag){
		
		renderJsonData(service.submitForm(formJsonData, tableJsonData, commonInvData, flag));
	}
	
	public void findEquipmentModelAll(){
		renderJsonData(equipmentModelService.getAdminDataNoPage(getKv()));
	}
	
	public void getDatas(){
		renderJsonData(service.getDatas(getKv()));
	}
	
	public void getPageData(){
		renderJsonData(service.getPageData(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void testDel(){
		ok();
	}
	
	public void copyForm(){
		ValidationUtils.notNull(get(0), "未获取到指定产品id");
		set("oldId", get(0));
		render("_copy_form.html");
	}
	
	// 拷贝
	public void saveCopy(@Para(value = "cversion") String cVersion, @Para(value = "oldId") Long oldId){
		renderJson(service.saveCopy(oldId, cVersion));
	}
	
	public void importExcelFile() throws IOException {
		//上传到今天的文件夹下
		String uploadFile= JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_FILE_UPLOADER);
		UploadFile file=getFile("file", uploadFile);
		
		if (notExcel(file)) {
			renderJsonFail("请上传excel文件");
			return;
		}
		renderJsonData(service.importExcelFile(file));
	}
	
	public void bomMasterIndex(){
		render("bommaster_index.html");
	}
	
	public void versionIndex(){
		render("version_index.html");
	}
	
	public void getVersionRecord(){
		renderJsonData(service.getVersionRecord(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void del(){
		renderJson(service.del(getLong(0)));
	}
	
	public void audit(@Para(value = "bomMasterId") Long bomMasterId,
					  @Para(value = "status") Integer status){
		renderJson(service.audit(bomMasterId, status));
	}
	
	public void checkCommonInv(@Para(value = "bomMasterId") Long bomMasterId,
							   @Para(value = "tableJsonData") String tableJsonData){
		renderJsonData(service.checkCommonInv(bomMasterId, tableJsonData));
	}
	
	public void findCustomerList(){
		renderJsonData(customerService.getAdminDatas(getKv()));
	}
	
	public void findVendorList(){
		renderJsonData(customerService.findVendorList(getKv()));
	}
}
