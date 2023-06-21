package cn.rjtech.admin.stockcheckdetail;


import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckDetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.tx.Tx;
/**
 * null管理
 * @ClassName: StockCheckDetailAdminController
 * @author: demo15
 * @date: 2023-05-03 14:26
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheck
@Path(value = "/admin/stockcheckdetail", viewPath = "/_view/admin/stockcheckdetail")
public class StockCheckDetailAdminController extends BaseAdminController {

	@Inject
	private StockCheckDetailService service;

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
	@Before(Tx.class)
	public void save() {
		renderJson(service.save(getModel(StockCheckDetail.class, "stockCheckDetail")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		StockCheckDetail stockCheckDetail=service.findById(getInt(0));
		if(stockCheckDetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockCheckDetail",stockCheckDetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
	public void update() {
		renderJson(service.update(getModel(StockCheckDetail.class, "stockCheckDetail")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
	public void delete() {
		renderJson(service.deleteById(getInt(0)));
	}


	/**
	 * 盘点单调整数量
	 */
	public void adjust() {
		Ret adjust = service.adjust(getKv());
		renderJsonData(adjust);
	}



}
