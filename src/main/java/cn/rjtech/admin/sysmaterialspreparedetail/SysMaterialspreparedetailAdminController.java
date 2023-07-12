package cn.rjtech.admin.sysmaterialspreparedetail;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.sysmaterialsprepare.SysMaterialsprepareService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysMaterialspreparedetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

/**
 * 备料单明细
 * @ClassName: SysMaterialspreparedetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-01 23:50
 */
@CheckPermission(PermissionKey.ADMIN_SYSMATERIALSPREPAREDETAIL)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysMaterialspreparedetail", viewPath = "/_view/admin/sysmaterialspreparedetail")
public class SysMaterialspreparedetailAdminController extends BaseAdminController {

	@Inject
	private SysMaterialspreparedetailService service;

	@Inject
	private SysMaterialsprepareService service1;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("SourceBillType"), get("State")));
	}

   /**
	* 新增
	*/
   @CheckPermission(PermissionKey.MATERIALSPREPARE_ADD1)
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save(SysMaterialspreparedetail sysMaterialspreparedetail) {
		renderJson(service.save(sysMaterialspreparedetail));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.MATERIALSPREPARE_EDIT1)
	public void edit() {
		SysMaterialspreparedetail sysMaterialspreparedetail=service.findById(getLong(0));
		if(sysMaterialspreparedetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysMaterialspreparedetail",sysMaterialspreparedetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update(SysMaterialspreparedetail sysMaterialspreparedetail) {
		renderJson(service.update(sysMaterialspreparedetail));
	}

   /**
	* 批量删除
	*/
   @CheckPermission(PermissionKey.MATERIALSPREPARE_DELETE1_ALL)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
   @CheckPermission(PermissionKey.MATERIALSPREPARE_DELETE1)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	public void MaterialsList() {
		render("MDetail.html");
	}

	public void cworkname() {
		renderJsonData(service.cworkname());
	}

	public void cworkname1() {
		renderJsonData(service.cworkname1());
	}

	public void cworkshiftcode() {
		renderJsonData(service.cworkshiftcode());
	}

	public void getMaterialsdetials() {
		String billno = get("billno");
		Kv kv = new Kv();
		kv.set("billno", billno == null ? "" : billno);
		renderJsonData(service1.getDetail(getPageNumber(), getPageSize(), kv));
	}

	public void getMaterialsdetials1() {
		renderJsonData(service.getMaterialsdetials1(getPageNumber(), getPageSize(), getKv()));
	}

	public void getBarcode() {
		String barcode = get("barcode");
		Kv kv = new Kv();
		kv.set("barcode", barcode == null ? "" : barcode);
		renderJsonData(service.getBarcode(getPageNumber(), getPageSize(), kv));
	}
	public void barcode(String barcode) {
		ValidationUtils.notBlank(barcode, "请扫码");
		renderJsonData(service.barcode(Kv.by("barcode", barcode)));
	}
	public void barcodeNull(String barcode) {
		renderJsonData(new Record());
	}

	public void choosemtool() {
		String itID = get("itID");
		String cmodocno = get("cmodocno");
		Kv kv = new Kv();
		kv.set("itID", itID == null ? "" : itID);
		kv.set("cmodocno",cmodocno== null ? "" : cmodocno);
		renderJsonData(service.getchooseM(kv));
	}
	@Before(Tx.class)
	public void go() {
		String map1 = get("data");
		renderJson(service.submitByJBoltTableGo(map1));
	}
	@Before(Tx.class)
	public void submitAll() {
		String map1 = get("data");
		renderJson(service.submitByJBoltTable(map1));
	}

	@Before(Tx.class)
	public void go1() {
		String map1 = get("data");
		renderJson(service.submitByJBoltTableGo1(map1));
	}

	public void ConfirmNum() {
		String Oldbarcode = get("barcode");
		String Oldqty = get("qty");
		String TAG = get("TAG");
		set("Oldbarcode",Oldbarcode);
		set("Oldqty",Oldqty);
		set("TAG",TAG);
		set("Newbarcode","WL"+ DateUtil.format(new Date(), "yyyyMMddHHmmss"));
		render("ConfirmNum.html");
	}

	@Before(Tx.class)
	public void NewNum(){
		String Oldbarcode = get("Oldbarcode");
		String Oldqty = get("Oldqty");
		String Newbarcode = get("Newbarcode");
		String Newqty = get("Newqty");
		String TAG = get("TAG");
		renderJson(service.submitQTY(Oldbarcode,Oldqty,Newbarcode,Newqty));
	}
}
