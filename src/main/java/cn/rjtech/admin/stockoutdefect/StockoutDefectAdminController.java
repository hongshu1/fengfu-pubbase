package cn.rjtech.admin.stockoutdefect;

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
import cn.rjtech.model.momdata.StockoutDefect;
/**
 * 质量管理-出库异常品记录 Controller
 * @ClassName: StockoutDefectAdminController
 * @author: RJ
 * @date: 2023-04-26 11:59
 */
@CheckPermission(PermissionKey.STOCKOUTDEFECT)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockoutdefect", viewPath = "/_view/admin/stockoutdefect")
public class StockoutDefectAdminController extends BaseAdminController {

	@Inject
	private StockoutDefectService service;

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
		StockoutDefect stockoutDefect=service.findById(getLong(0)); 
		if(stockoutDefect == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockoutDefect",stockoutDefect);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockoutDefect.class, "stockoutDefect")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockoutDefect.class, "stockoutDefect")));
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
	* 切换toggleIsFirstTime
	*/
	public void toggleIsFirstTime() {
		renderJson(service.toggleIsFirstTime(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


}
