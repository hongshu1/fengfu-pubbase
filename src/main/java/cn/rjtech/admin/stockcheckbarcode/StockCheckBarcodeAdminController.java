package cn.rjtech.admin.stockcheckbarcode;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockCheckBarcode;
/**
 * 库存盘点-条码明细 Controller
 * @ClassName: StockCheckBarcodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 22:47
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/stockcheckbarcode", viewPath = "/_view/admin/stockcheckbarcode")
public class StockCheckBarcodeAdminController extends BaseAdminController {

	@Inject
	private StockCheckBarcodeService service;

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
		StockCheckBarcode stockCheckBarcode=service.findById(getLong(0)); 
		if(stockCheckBarcode == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockCheckBarcode",stockCheckBarcode);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockCheckBarcode.class, "stockCheckBarcode")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockCheckBarcode.class, "stockCheckBarcode")));
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
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


}
