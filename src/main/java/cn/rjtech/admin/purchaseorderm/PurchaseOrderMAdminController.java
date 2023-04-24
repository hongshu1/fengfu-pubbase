package cn.rjtech.admin.purchaseorderm;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.demandplanm.DemandPlanMService;
import cn.rjtech.admin.foreigncurrency.ForeignCurrencyService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.PurchaseOrderM;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;


/**
 * 采购/委外订单-采购订单主表
 * @ClassName: PurchaseOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 15:19
 */

@Path(value = "/admin/purchaseorderm", viewPath = "/_view/admin/purchaseorderm")
public class PurchaseOrderMAdminController extends BaseAdminController {

	@Inject
	private PurchaseOrderMService service;
	@Inject
	private ForeignCurrencyService foreignCurrencyService;
	@Inject
	private PurchaseTypeService purchaseTypeService;
	@Inject
	private VendorService vendorService;
	@Inject
	private DemandPlanMService demandPlanMService;
	
	@Inject
	private VendorAddrService vendorAddrService;
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
					@Para(value = "processType") Integer processType) {
		
		Vendor vendor = vendorService.findById(iVendorId);
		Record record = new Record();
		record.set(PurchaseOrderM.IVENDORID, vendor.getIAutoId());
		record.set(Vendor.CVENNAME, vendor.getCVenName());
		record.set(PurchaseOrderM.DBEGINDATE, beginDate);
		record.set(PurchaseOrderM.DENDDATE, endDate);
		setAttrs(service.getDateMap(beginDate, endDate, iVendorId, processType));
		set("purchaseOrderM", record);
		render("add.html");
	}
	
	public void checkData(@Para(value = "beginDate") String beginDate,
						  @Para(value = "endDate") String endDate,
						  @Para(value = "iVendorId") String iVendorId,
						  @Para(value = "processType") Integer processType){
		ValidationUtils.notBlank(beginDate, "请选择日期范围");
		ValidationUtils.notBlank(endDate, "请选择日期范围");
		ValidationUtils.notBlank(iVendorId, "请选择供应商");
		List<Record> list = demandPlanMService.getVendorDateList(beginDate, endDate, iVendorId, processType);
		ValidationUtils.notEmpty(list, "该时间范围未找到该供应商所需求物料");
		ok();
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(PurchaseOrderM.class, "purchaseOrderM")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseOrderM purchaseOrderM=service.findById(getLong(0));
		if(purchaseOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseOrderM",purchaseOrderM);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseOrderM.class, "purchaseOrderM")));
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
					   @Para(value = "invTableData") String invTableData) {
		renderJson(service.submit(dataStr, formStr, invTableData, getKv()));
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
	public void generate(){
		renderJsonData(service.generate(getLong(0)));
	}
	
	/**
	 * 审核接口
	 */
	public void audit(){
		renderJsonData(service.audit(getLong(0)));
	}
	
	public void batchGenerate(@Para(value = "ids") String ids){
		renderJsonData(service.batchGenerate(ids));
	}
}
