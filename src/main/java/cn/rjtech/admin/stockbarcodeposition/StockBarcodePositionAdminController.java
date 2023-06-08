package cn.rjtech.admin.stockbarcodeposition;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockBarcodePosition;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 条码库存表 Controller
 * @ClassName: StockBarcodePositionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 13:39
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/stockbarcodeposition", viewPath = "/_view/admin/stockbarcodeposition")
public class StockBarcodePositionAdminController extends BaseAdminController {

	@Inject
	private StockBarcodePositionService service;

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
		StockBarcodePosition stockBarcodePosition=service.findById(getLong(0)); 
		if(stockBarcodePosition == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockBarcodePosition",stockBarcodePosition);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockBarcodePosition.class, "stockBarcodePosition")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockBarcodePosition.class, "stockBarcodePosition")));
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
