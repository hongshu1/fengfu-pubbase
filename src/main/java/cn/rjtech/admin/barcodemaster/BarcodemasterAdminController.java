package cn.rjtech.admin.barcodemaster;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.common.model.Barcodemaster;
/**
 * 条码表 Controller
 * @ClassName: BarcodemasterAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 13:21
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/barcodemaster", viewPath = "/_view/admin/barcodemaster")
public class BarcodemasterAdminController extends BaseAdminController {

	@Inject
	private BarcodemasterService service;

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
		Barcodemaster barcodemaster=service.findById(getLong(0)); 
		if(barcodemaster == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("barcodemaster",barcodemaster);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Barcodemaster.class, "barcodemaster")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Barcodemaster.class, "barcodemaster")));
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

  /**
	* 切换toggleIspack
	*/
	public void toggleIspack() {
		renderJson(service.toggleIspack(getLong(0)));
	}


}
