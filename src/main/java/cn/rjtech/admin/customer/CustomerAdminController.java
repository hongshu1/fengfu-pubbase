package cn.rjtech.admin.customer;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.Vendor;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Customer;
import com.jfinal.upload.UploadFile;

import java.util.Date;
import java.util.List;

/**
 * 往来单位-客户档案
 * @ClassName: CustomerAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 15:10
 */
@CheckPermission(PermissionKey.CUSTOMER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/customer", viewPath = "/_view/admin/customer")
public class CustomerAdminController extends BaseAdminController {

	@Inject
	private CustomerService service;
	@Inject
	private PersonService personService;
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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 新增
	 */
	public void add() {
		set("centityname", "customerm");

		set("icustomerclassid", get("autoid"));
		set("customerm",new Customer());
		render("add.html");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Customer customerm=service.findById(getLong(0));
		if(customerm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}

		if(isOk(customerm.getIDutyUserId())){
			Person person = personService.findById(customerm.getIDutyUserId());
			set("cpsnname", isOk(person)?person.getCpsnName():"");
		}
		/*if(isOk(customerm.getCVenCode())){
			Vendor vendor = service.findVendorByCode(customerm.getCVenCode());
			set("vendorname", isOk(vendor)?vendor.getCVenName():"");
		}*/
		set("centityname", "customerm");
//		set("icustomerclassid",customerm.getIcustomerclassid());
		set("customerm",customerm);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		renderJson(service.save(getModel(Customer.class, "customerm")));
	}

	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(getModel(Customer.class, "customerm")));
	}

	/**
	 * 批量删除
	 */
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

	/**
	 * 切换toggleIsdeleted
	 */
	public void toggleIsdeleted() {
		renderJson(service.toggleIsdeleted(getLong(0)));
	}

	/**
	 * 切换toggleIsenabled
	 */
	public void toggleIsenabled() {
		renderJson(service.toggleIsenabled(getLong(0)));
	}

	public void updateEditTable() {
//		renderJson(service.updateEditTable(getJBoltTable(), JBoltUserKit.getUserId(), new Date()));
		renderJson(service.submitByJBoltTables(getJBoltTables()));
	}

	/**
	 * 导出数据
	 */
	@SuppressWarnings("unchecked")
	public void dataExport() throws Exception {
		List<Record> rows = service.getDataExport(getKv());

		renderJxls("customerm.xlsx", Kv.by("rows", rows), "客户列表_" + DateUtil.today() + ".xlsx");
	}

	/**
	 * Excel模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderJxls("customerm_import.xlsx", Kv.by("rows", null), "客户档案导入模板.xlsx");
	}

	/**
	 * 客户档案Excel导入数据库
	 */
	public void importExcel(){
		String uploadPath= JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
		UploadFile file=getFile("file",uploadPath);
		if(notExcel(file)){
			renderJsonFail("请上传excel文件");
			return;
		}
		renderJson(service.importExcelData(file.getFile()));
	}

	/**
	 * 供应商信息Dialog
	 */
	public void vendorTable(){
		render("vendor_table.html");
	}

	/**
	 * 供应商数据源
	 */
	public void findVendorPage(){
		String keywords = getKeywords();
		Kv kv = new Kv();
		kv.setIfNotNull("keywords", keywords);
		renderJsonData(service.findVendorPage(getPageNumber(), getPageSize(), kv));

	}

	/**
	 * 客户数据源
	 */
	public void list(){
		Okv kv = Okv.create();
		kv.set("IsEnabled", 1);
		kv.set("IsDeleted", 0);

		renderJsonData(service.getCommonList(kv, "dCreateTime", "desc"));
	}
	/**
	  * 获取客户列表 
	  * 通过关键字匹配 
	 * autocomplete组件使用
	 */
	@UnCheck
	public void autocomplete() {
		renderJsonData(service.getAutocompleteList(get("q"), getInt("limit",10),true,"cCusCode,cCusName,cCusAbbName"));
	}
	
}
