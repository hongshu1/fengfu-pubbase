package cn.rjtech.admin.stockcheck;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheck;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 库存盘点单主表 Controller
 * @ClassName: StockCheckAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 22:45
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/stockcheck", viewPath = "/_view/admin/stockcheck")
public class StockCheckAdminController extends BaseAdminController {

	@Inject
	private StockCheckService service;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
  	
  	/**
	* 数据源
	*/
    @UnCheck
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
		StockCheck stockCheck=service.findById(getLong(0)); 
		if(stockCheck == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockCheck",stockCheck);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(StockCheck.class, "stockCheck")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockCheck.class, "stockCheck")));
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
