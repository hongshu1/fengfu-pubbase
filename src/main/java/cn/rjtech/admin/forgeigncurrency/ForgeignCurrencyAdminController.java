package cn.rjtech.admin.forgeigncurrency;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ForgeignCurrency;
/**
 * 币种档案 Controller
 * @ClassName: ForgeignCurrencyAdminController
 * @author: WYX
 * @date: 2023-03-20 21:09
 */
@CheckPermission(PermissionKey.FORGEIGN_CURRENCY_INDEX)
@UnCheckIfSystemAdmin
public class ForgeignCurrencyAdminController extends BaseAdminController {

	@Inject
	private ForgeignCurrencyService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		ForgeignCurrency forgeignCurrency = service.findById(getLong(0)); 
		if(forgeignCurrency == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("forgeignCurrency",forgeignCurrency);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ForgeignCurrency.class, "forgeignCurrency")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ForgeignCurrency.class, "forgeignCurrency")));
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
	* 切换toggleBcal
	*/
	public void toggleBcal() {
		renderJson(service.toggleBcal(getLong(0)));
	}
	/**
	 * 切换toggleIotherused
	 * */
	public void toggleIotherused() {
		renderJson(service.toggleIotherused(getLong(0)));
	}
	
  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


}
