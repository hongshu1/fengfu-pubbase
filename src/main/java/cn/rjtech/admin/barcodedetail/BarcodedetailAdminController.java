package cn.rjtech.admin.barcodedetail;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.common.model.Barcodedetail;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 条码明细表 Controller
 * @ClassName: BarcodedetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 13:25
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/barcodedetail", viewPath = "/_view/admin/barcodedetail")
public class BarcodedetailAdminController extends BaseAdminController {

	@Inject
	private BarcodedetailService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
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
		Barcodedetail barcodedetail=service.findById(getLong(0)); 
		if(barcodedetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("barcodedetail",barcodedetail);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Barcodedetail.class, "barcodedetail")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Barcodedetail.class, "barcodedetail")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}


}
