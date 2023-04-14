package cn.rjtech.admin.stockoutqcformm;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockoutQcFormM;
/**
 * 质量管理-出库检
 * @ClassName: StockoutQcFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 19:18
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockoutqcformm", viewPath = "/_view/admin/stockoutqcformm")
public class StockoutQcFormMAdminController extends BaseAdminController {

	@Inject
	private StockoutQcFormMService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
		renderJson(service.save(getModel(StockoutQcFormM.class, "stockoutQcFormM")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		StockoutQcFormM stockoutQcFormM=service.findById(getLong(0));
		if(stockoutQcFormM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockoutQcFormM",stockoutQcFormM);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockoutQcFormM.class, "stockoutQcFormM")));
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


}
