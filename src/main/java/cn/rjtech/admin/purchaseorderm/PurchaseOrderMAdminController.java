package cn.rjtech.admin.purchaseorderm;

import cn.hutool.core.date.DateUtil;
import cn.rjtech.admin.foreigncurrency.ForeignCurrencyService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.PurchaseOrderM;

import java.util.List;


/**
 * 采购/委外订单-采购订单主表
 * @ClassName: PurchaseOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 15:19
 */
@CheckPermission(PermissionKey.PS_PURCHASE_ORDER)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("cOrderNo"), getInt("iBusType"), getInt("iPurchaseType"), getLong("iVendorId"), getLong("iDepartmentId"), getInt("iPayableType"), getInt("iOrderStatus"), getInt("iAuditStatus")));
	}

   /**
	* 新增
	*/
	public void add(@Para(value = "beginDate") String beginDate,
					@Para(value = "endDate") String endDate,
					@Para(value = "iVendorId") String iVendorId) {
		
		Vendor vendor = vendorService.findById(iVendorId);
		Record record = new Record();
		record.set(PurchaseOrderM.IVENDORID, vendor.getIAutoId());
		record.set(Vendor.CVENNAME, vendor.getCVenName());
		record.set(PurchaseOrderM.CORDERNO, service.generateCGCode());
		record.set(PurchaseOrderM.DORDERDATE, DateUtil.formatDate(DateUtil.date()));
		setAttrs(service.getDateMap(beginDate, endDate, iVendorId));
		set("purchaseOrderM", record);
		render("add.html");
	}
	
	public void checkData(@Para(value = "beginDate") String beginDate,
						  @Para(value = "endDate") String endDate,
						  @Para(value = "iVendorId") String iVendorId){
		ValidationUtils.notBlank(beginDate, "请选择日期范围");
		ValidationUtils.notBlank(endDate, "请选择日期范围");
		ValidationUtils.notBlank(iVendorId, "请选择供应商");
		List<Record> list = service.getVendorDateList(beginDate, endDate, iVendorId);
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
}
