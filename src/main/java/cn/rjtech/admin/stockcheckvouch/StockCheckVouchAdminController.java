package cn.rjtech.admin.stockcheckvouch;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockCheckVouch;
/**
 * 盘点单
 * @ClassName: StockCheckVouchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-21 10:47
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockcheckvouch", viewPath = "/_view/admin/stockcheckvouch")
public class StockCheckVouchAdminController extends BaseAdminController {

	@Inject
	private StockCheckVouchService service;

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
		render("add().html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockCheckVouch.class, "stockCheckVouch")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		StockCheckVouch stockCheckVouch=service.findById(getLong(0));
		if(stockCheckVouch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockCheckVouch",stockCheckVouch);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockCheckVouch.class, "stockCheckVouch")));
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
