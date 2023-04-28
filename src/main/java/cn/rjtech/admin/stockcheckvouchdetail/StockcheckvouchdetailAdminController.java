package cn.rjtech.admin.stockcheckvouchdetail;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Stockcheckvouchdetail;
/**
 * 盘点单明细表
 * @ClassName: StockcheckvouchdetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-21 10:42
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockcheckvouchdetail", viewPath = "/_view/admin/stockcheckvouchdetail")
public class StockcheckvouchdetailAdminController extends BaseAdminController {

	@Inject
	private StockcheckvouchdetailService service;

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
		renderJson(service.save(getModel(Stockcheckvouchdetail.class, "stockcheckvouchdetail")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		Stockcheckvouchdetail stockcheckvouchdetail=service.findById(getLong(0));
		if(stockcheckvouchdetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockcheckvouchdetail",stockcheckvouchdetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Stockcheckvouchdetail.class, "stockcheckvouchdetail")));
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