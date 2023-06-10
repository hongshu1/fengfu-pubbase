package cn.rjtech.admin.sysmaterialspreparedetail;

import cn.rjtech.admin.syspureceive.SysPureceiveService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysMaterialspreparedetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
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
	private SysPureceiveService syspureceiveservice;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("SourceBillType"), get("State")));
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
	public void save(SysMaterialspreparedetail sysMaterialspreparedetail) {
		renderJson(service.save(sysMaterialspreparedetail));
	}

   /**
	* 编辑
	*/
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
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	public void MaterialsList() {
		render("MDetail.html");
	}

	public void cworkname() {
		renderJsonData(service.cworkname());
	}

	public void cworkshiftcode() {
		renderJsonData(service.cworkshiftcode());
	}

	public void getMaterialsdetials() {
		renderJsonData(service.getMaterialsdetials(getPageNumber(), getPageSize(), getKv()));
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
//		ValidationUtils.notBlank(barcode, "请扫码");
		renderJsonData(syspureceiveservice.barcode(Kv.by("barcode", barcode)));
	}
}
