package cn.rjtech.admin.stockcheckvouchdetail;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouchDetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;
/**
 * 盘点单明细表
 * @ClassName: StockCheckVouchDetailAdminController
 * @author: Administrator
 * @date: 2023-04-11 15:30
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheck
@Path(value = "/admin/stockcheckvouchdetail", viewPath = "/_view/admin/stockcheckvouchdetail")
public class StockCheckVouchDetailAdminController extends BaseAdminController {

	@Inject
	private StockCheckVouchDetailService service;

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
		renderJson(service.save(getModel(StockCheckVouchDetail.class, "stockCheckVouchDetail")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		StockCheckVouchDetail stockCheckVouchDetail=service.findById(getLong(0));
		if(stockCheckVouchDetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockCheckVouchDetail",stockCheckVouchDetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
	public void update() {
		renderJson(service.update(getModel(StockCheckVouchDetail.class, "stockCheckVouchDetail")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	/**
	 * 批量删除
	 */
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}
}
