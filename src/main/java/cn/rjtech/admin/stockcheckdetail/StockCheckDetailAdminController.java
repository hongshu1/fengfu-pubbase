package cn.rjtech.admin.stockcheckdetail;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockCheckDetail;
/**
 * 库存盘点-盘点明细表 Controller
 * @ClassName: StockCheckDetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 22:49
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
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
		StockCheckDetail stockCheckDetail=service.findById(getLong(0)); 
		if(stockCheckDetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockCheckDetail",stockCheckDetail);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockCheckDetail.class, "stockCheckDetail")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockCheckDetail.class, "stockCheckDetail")));
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
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


}
