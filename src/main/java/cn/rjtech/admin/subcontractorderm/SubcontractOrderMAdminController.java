package cn.rjtech.admin.subcontractorderm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.demandplanm.DemandPlanMService;
import cn.rjtech.admin.foreigncurrency.ForeignCurrencyService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.subcontractorderdbatch.SubcontractOrderDBatchService;
import cn.rjtech.admin.subcontractorderdbatchversion.SubcontractOrderDBatchVersionService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.SourceTypeEnum;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.SubcontractOrderM;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购/委外订单-采购订单主表
 * @ClassName: SubcontractOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
@Path(value = "/admin/subcontractorderm", viewPath = "/_view/admin/subcontractorderm")
public class SubcontractOrderMAdminController extends BaseAdminController {

	@Inject
	private SubcontractOrderMService service;
	@Inject
	private ForeignCurrencyService foreignCurrencyService;
	@Inject
	private PurchaseTypeService purchaseTypeService;
	@Inject
	private VendorService vendorService;
	@Inject
	private DemandPlanMService demandPlanMService;
	@Inject
	private PersonService personService;
	@Inject
	private VendorAddrService vendorAddrService;
	@Inject
	private SubcontractOrderDBatchVersionService subcontractOrderDBatchVersionService;
	@Inject
	private SubcontractOrderDBatchService subcontractOrderDBatchService;
	@Inject
	private InventoryChangeService inventoryChangeService;
	
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
	public void add(@Para(value = "beginDate") String beginDate,
					@Para(value = "endDate") String endDate,
					@Para(value = "iVendorId") String iVendorId,
					@Para(value = "processType") Integer processType,
					@Para(value = "iSourceType") Integer iSourceType) {
		
		Vendor vendor = vendorService.findById(iVendorId);
		Record record = new Record();
		record.set(SubcontractOrderM.IVENDORID, vendor.getIAutoId());
		record.set(Vendor.CVENNAME, vendor.getCVenName());
		record.set(SubcontractOrderM.DBEGINDATE, beginDate);
		record.set(SubcontractOrderM.DENDDATE, endDate);
		setAttrs(service.getDateMap(beginDate, endDate, iVendorId, processType, iSourceType));
		set("subcontractOrderM", record);
		record.set(SubcontractOrderM.ITYPE, iSourceType);
		if (SourceTypeEnum.BLANK_PURCHASE_TYPE.getValue() == iSourceType){
			render("blank_add.html");
			return;
		}
		render("add.html");
	}
	
	public void checkData(@Para(value = "beginDate") String beginDate,
						  @Para(value = "endDate") String endDate,
						  @Para(value = "iVendorId") String iVendorId,
						  @Para(value = "processType") Integer processType,
						  @Para(value = "iSourceType") Integer iSourceType){
		ValidationUtils.notBlank(beginDate, "请选择日期范围");
		ValidationUtils.notBlank(endDate, "请选择日期范围");
		ValidationUtils.notBlank(iVendorId, "请选择供应商");
		if (SourceTypeEnum.MATERIAL_PLAN_TYPE.getValue() == iSourceType){
			List<Record> list = demandPlanMService.getVendorDateList(beginDate, endDate, iVendorId, processType);
			ValidationUtils.notEmpty(list, "该时间范围未找到该供应商所需求物料");
		}
		ok();
	}
	
	
	/**
	 * 编辑 isView: 判断是否只有查看功能
	 */
	public void edit(@Para(value = "isView") String isView) {
		SubcontractOrderM subcontractOrderM=service.findById(getLong(0));
		if(subcontractOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		Person person = personService.findById(subcontractOrderM.getIDutyUserId());
		if (ObjectUtil.isNotNull(person)){
			set("personname",person.getCpsnName());
		}
		
		Vendor vendor = vendorService.findById(subcontractOrderM.getIVendorId());
		ValidationUtils.notNull(vendor, "未找到供应商数据");
		set(Vendor.CVENNAME, vendor.getCVenName());
		if (StrUtil.isNotBlank(isView)){
			set("isView", 1);
		}
		set("subcontractOrderM",subcontractOrderM);
		setAttrs(service.getDateMap(subcontractOrderM));
		if (SourceTypeEnum.BLANK_PURCHASE_TYPE.getValue() == subcontractOrderM.getIType()){
			render("blank_edit.html");
			return;
		}
		render("edit.html");
	}
	
	public void cash(){
		SubcontractOrderM subcontractOrderM = service.findById(getLong(0));
		if(subcontractOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		Person person = personService.findById(subcontractOrderM.getIDutyUserId());
		if (ObjectUtil.isNotNull(person)){
			set("personname",person.getCpsnName());
		}
		
		Vendor vendor = vendorService.findById(subcontractOrderM.getIVendorId());
		ValidationUtils.notNull(vendor, "未找到供应商数据");
		set(Vendor.CVENNAME, vendor.getCVenName());
		set("subcontractOrderM",subcontractOrderM);
		render("cash.html");
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
	 * 新增作成页面
	 */
	public void consummate() {
		keepPara();
		render("consummate.html");
	}
	
	public void findForeignCurrencyAll(){
		renderJsonData(foreignCurrencyService.findAll(getKv()));
	}
	
	public void findPurchaseType(){
		renderJsonData(purchaseTypeService.selectAll(getKv()));
	}
	
	public void findByiVendorId(@Para(value = "vendorId") String vendorId,
								@Para(value = "id") String id){
		renderJsonData(vendorAddrService.findList(getKv()));
	}
	
	/**
	 * 保存
	 */
	public void submit(@Para(value = "tableData") String dataStr,
					   @Para(value = "formData") String formStr,
					   @Para(value = "invTableData") String invTableData,
					   @Para(value = "type") String type) {
		renderJson(service.submit(dataStr, formStr, invTableData, type, getKv()));
	}
	
	public void operationalState(@Para(value = "id") Long id,
								 @Para(value = "type") Integer type){
		renderJsonData(service.operationalState(id, type));
	}
	
	/**
	 * 删除操作
	 */
	public void del(){
		renderJsonData(service.del(getLong(0)));
	}
	
	/**
	 * 撤回操作
	 */
	public void withdraw(){
		renderJsonData(service.withdraw(getLong(0)));
	}
	
	/**
	 * 提审接口
	 */
	public void arraignment(){
		renderJsonData(service.arraignment(getLong(0)));
	}
	
	/**
	 * 关闭
	 */
	public void close(){
		renderJsonData(service.close(getLong(0)));
	}
	
	/**
	 * 生成现成票
	 */
	public void generateCash(){
		renderJsonData(service.generateCash(getLong(0)));
	}
	
	/**
	 * 审核接口
	 */
	public void audit(){
		renderJsonData(service.audit(getLong(0)));
	}
	
	public void batchGenerateCash(@Para(value = "ids") String ids){
		renderJsonData(service.batchGenerateCash(ids));
	}
	
	public void batchDel(@Para(value = "ids") String ids){
		renderJsonData(service.batchDel(ids));
	}
	
	
	public void findSubcontractOrderDBatch(){
		renderJsonData(subcontractOrderDBatchService.findBySubcontractOrderMId(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void updateHideInvalid(@Para(value = "id") Long id,
								  @Para(value = "hideInvalid") String hideInvalid){
		renderJsonData(service.updateHideInvalid(id, Boolean.valueOf(hideInvalid)));
	}
	
	public void updateOrderBatch(@Para(value = "subcontractOrderMId") Long subcontractOrderMid,
								 @Para(value = "id") Long id,
								 @Para(value = "cVersion") String cVersion,
								 @Para(value = "qty") BigDecimal qty){
		renderJsonData(subcontractOrderDBatchService.updateOrderBatch(subcontractOrderMid, id, cVersion, qty));
	}
	
	public void findSubcontractOrderDBatchVersion(){
		renderJsonData(subcontractOrderDBatchVersionService.findBySubcontractOrderMid(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void saveSubmit(){
		renderJson(service.saveSubmit(getJBoltTable()));
	}
	
	public void findPurchaseOrderD(@Para(value = "purchaseOrderMId") Long purchaseOrderMId){
		renderJsonData(service.findPurchaseOrderD(purchaseOrderMId));
	}
	
	public void inventory_dialog_index(){
		keepPara();
		render("inventory_dialog_index.html");
	}
	
	/**
	 * 默认给1-100个数据
	 */
	public void inventoryPage() {
		renderJsonData(inventoryChangeService.inventoryAutocomplete(getPageNumber(), getPageSize(), getKv()));
	}
}
