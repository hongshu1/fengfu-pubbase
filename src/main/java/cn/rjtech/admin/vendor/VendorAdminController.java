package cn.rjtech.admin.vendor;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.model.momdata.VendorAddr;
import cn.rjtech.model.momdata.base.BaseVendorAddr;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 往来单位-供应商档案
 * @ClassName: VendorAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 17:16
 */
@CheckPermission(PermissionKey.VENDOR)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/vendor", viewPath = "/_view/admin/vendor")
public class VendorAdminController extends BaseAdminController {

	@Inject
	private VendorService    service;
	@Inject
	private VendorAddrService vendorAddrService;
	@Inject
	private WarehouseService warehouseService;


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
//		renderJsonData(service.pageList(getKv()));
		 Page<Vendor> adminDatas = service
			.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted"),getKv());
		renderJsonData(adminDatas);
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
		renderJson(service.save(getModel(Vendor.class, "vendor")));
	}

	/**
	 * JBoltTable 可编辑表格整体提交
	 */
	@Before(Tx.class)
	public void submit() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

   /**
	* 编辑
	*/
	public void edit() {
		Vendor vendor=service.findById(getLong(0));
		if(vendor == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		Kv kv = new Kv();
		kv.set("ivendorid",vendor.getIAutoId());
		List<VendorAddr> list = vendorAddrService.list(kv);
		String addr = vendor.getCProvince()+","+vendor.getCCity()+","+vendor.getCCounty();
		vendor.setCProvince(addr);
		set("vendor",vendor);
		set("vendoraddr",!list.isEmpty()?list.get(0):"");
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Vendor.class, "vendor")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		String ids = get("ids");
		for (String id : ids.split(",")) {
			deleteVendorAddrById(Long.valueOf(id));
		}
		renderJson(service.deleteByIds(ids));
	}

   /**
	* 删除
	*/
	public void delete() {
		deleteVendorAddrById(getLong(0));
		renderJson(service.deleteById(getLong(0)));
	}

	/*
	* 删除供应商地址的关联记录
	*/
	public void deleteVendorAddrById(Long vendorIautoId){
		Vendor vendor = service.findById(vendorIautoId);
		Kv kv = new Kv();
		kv.set("ivendorid",vendor.getIAutoId());
		List<VendorAddr> list = vendorAddrService.list(kv);
		if (!list.isEmpty()){
			List<Long> collect = list.stream().map(BaseVendorAddr::getIAutoId).collect(Collectors.toList());
			vendorAddrService.deleteByIds(collect.toArray());
		}
	}

	/**
	 * 切换isEnabled
	 */
	public void toggleIsEnabled() {
		renderJson(service.toggleIsenabled(getLong(0)));
	}

	public void options() {
		renderJsonData(service.options());
	}
}
