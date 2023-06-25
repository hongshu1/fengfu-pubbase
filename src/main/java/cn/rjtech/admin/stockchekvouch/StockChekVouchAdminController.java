package cn.rjtech.admin.stockchekvouch;


import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouch;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;
/**
 * 盘点单
 * @ClassName: StockChekVouchAdminController
 * @author: demo15
 * @date: 2023-04-20 11:23
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockchekvouch", viewPath = "/_view/admin/stockchekvouch")
public class StockChekVouchAdminController extends BaseAdminController {

	@Inject
	private StockChekVouchService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("CheckType")));
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
	@Before(Tx.class)
	public void save() {
		renderJson(service.save(getModel(StockCheckVouch.class, "stockChekVouch")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		StockCheckVouch stockChekVouch=service.findById(getLong(0));
		if(stockChekVouch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockChekVouch",stockChekVouch);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
	public void update() {
		renderJson(service.update(getModel(StockCheckVouch.class, "stockChekVouch")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
