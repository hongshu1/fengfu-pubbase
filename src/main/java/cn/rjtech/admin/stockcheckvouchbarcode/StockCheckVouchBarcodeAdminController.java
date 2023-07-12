package cn.rjtech.admin.stockcheckvouchbarcode;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouchBarcode;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 库存盘点-条码明细 Controller
 * @ClassName: StockCheckVouchBarcodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-30 11:16
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/stockcheckvouchbarcode", viewPath = "/_view/admin/stockcheckvouchbarcode")
public class StockCheckVouchBarcodeAdminController extends BaseAdminController {

	@Inject
	private StockCheckVouchBarcodeService service;

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
		StockCheckVouchBarcode stockCheckVouchBarcode=service.findById(getLong(0)); 
		if(stockCheckVouchBarcode == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockCheckVouchBarcode",stockCheckVouchBarcode);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockCheckVouchBarcode.class, "stockCheckVouchBarcode")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockCheckVouchBarcode.class, "stockCheckVouchBarcode")));
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
