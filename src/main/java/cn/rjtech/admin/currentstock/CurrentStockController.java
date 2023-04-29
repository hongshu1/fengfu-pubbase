package cn.rjtech.admin.currentstock;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.controller.BaseMesAdminController;
import cn.rjtech.admin.stockcheckvouch.StockCheckVouchService;
import cn.rjtech.model.momdata.StockCheckVouch;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 盘点单 Controller
 *
 * @ClassName: CurrentStockController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-31 14:38
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/currentstock", viewPath = "_view/admin/currentstock")
public class CurrentStockController extends BaseMesAdminController {

	@Inject
	CurrentStockService service;
	@Inject
	StockCheckVouchService stockCheckVouchService;


   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}

	/**
	 * 列表也数据
	 */
	public void datas() {
		renderJsonData(service.datas(getPageNumber(),getPageSize(),getKv()));
	}

	public void invDatas() {
		renderJsonData(service.invDatas(getPageNumber(),getPageSize(),getKv()));
	}



   public void add(){
		render("add.html");
   }
   /**
    * 继续盘点页面*/
   public void stockEdit(){
	   Kv kv = getKv();
	   String autoid = kv.getStr("autoid");
	   StockCheckVouch stockcheckvouch = stockCheckVouchService.findById(autoid);

	   set("bill", stockcheckvouch);
	   render("stockEdit.html");
   }

	public void autocompleteWareHouse() {
		renderJsonData(service.autocompleteWareHouse(getKv()));
	}

	public void autocompletePosition() {
		renderJsonData(service.autocompletePosition(getKv()));
	}

	public void autocompleteUser() {
		renderJsonData(service.autocompleteUser(getKv()));
	}
	/**
	 * 新增提交
	 */
   public void save(){
	   Kv kv = getKv();
	   //SysStockchekvouch sysStockchekvouch = useIfValid(SysStockchekvouch.class, "sysstockchekvouch");
	   //renderJson(service.save());
	   renderJson(service.save(useIfValid(StockCheckVouch.class, "SysStockchekvouch")));

	   //renderJsonData(service.SaveStockchekvouch(getKv()));
   }

   public void saveSubmit(){
   	renderJson(service.saveSubmit(getKv()));
   }

}
