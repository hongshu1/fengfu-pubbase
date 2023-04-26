package cn.rjtech.admin.stockoutqcformd;

import com.jfinal.aop.Inject;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockoutQcFormD;
/**
 * 质量管理-出库检明细列值 Controller
 * @ClassName: StockoutQcFormDAdminController
 * @author: RJ
 * @date: 2023-04-25 16:21
 */
@CheckPermission(PermissionKey.STOCKOUTQCFORMD)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockoutqcformd", viewPath = "/_view/admin/stockoutqcformd")
public class StockoutQcFormDAdminController extends BaseAdminController {

	@Inject
	private StockoutQcFormDService service;

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
		StockoutQcFormD stockoutQcFormD=service.findById(getLong(0)); 
		if(stockoutQcFormD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockoutQcFormD",stockoutQcFormD);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockoutQcFormD.class, "stockoutQcFormD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockoutQcFormD.class, "stockoutQcFormD")));
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
