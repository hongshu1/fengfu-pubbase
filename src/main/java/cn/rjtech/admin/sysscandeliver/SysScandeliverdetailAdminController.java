package cn.rjtech.admin.sysscandeliver;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysScandeliverdetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 扫码发货明细
 * @ClassName: SysScandeliverdetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:48
 */
@CheckPermission(PermissionKey.DOUBLECODESCANNINGSHIPMENT)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysscandeliverdetail", viewPath = "/_view/admin/sysscandeliverdetail")
public class SysScandeliverdetailAdminController extends BaseAdminController {

	@Inject
	private SysScandeliverdetailService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("TrackType"), get("SourceBillType"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(SysScandeliverdetail.class, "sysScandeliverdetail")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysScandeliverdetail sysScandeliverdetail=service.findById(getLong(0));
		if(sysScandeliverdetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysScandeliverdetail",sysScandeliverdetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysScandeliverdetail.class, "sysScandeliverdetail")));
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
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

    @UnCheck
	public void findEditTableDatas(){
		Kv kv = new Kv();
		String id = get("sysscandeliver.AutoID");
		kv.set("id",id==null?' ':id);
		renderJsonData(service.findEditTableDatas(kv));
	}


//	===================

	/**
	 * 获取行信息
	 */
    @UnCheck
	public void getLine(){
		String autoId = get("sysscandeliver.autoid");
		Kv kv = new Kv();
		kv.set("autoId",isOk(autoId)?autoId:" ");
		renderJsonData(service.getLine(kv));
	}

	/**
	 * 获取订单行信息
	 */
    @UnCheck
	public void getOrderLine(){
		String carNo = get("carNo");
		Kv kv = new Kv();
		kv.set("carNo",isOk(carNo)?carNo:" ");
		renderJsonData(service.getOrderLine(kv));
	}
}
