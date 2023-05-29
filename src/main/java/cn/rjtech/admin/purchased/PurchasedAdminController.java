package cn.rjtech.admin.purchased;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Purchased;
import cn.rjtech.util.ValidationUtils;

/**
 * 申购单管理 Controller
 * @ClassName: PurchasedAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 15:03
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchased", viewPath = "/_view/admin/purchased")
public class PurchasedAdminController extends BaseAdminController {

	@Inject
	private PurchasedService service;

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
		List<Record> purchaseds = new ArrayList<>();

		if (notNull(get("ipurchaseid"))) {
			purchaseds = service.findByPurchaseId(getLong("ipurchaseid"));
		}

		renderJsonData(purchaseds);
	}

  	/**
	*	申购单参照预算-申购单明细数据
	*/
  	@UnCheck
	public void refBudgetDatas() {
		List<Record> purchaseds = new ArrayList<>();
		if (notNull(get("ipurchaseid")))
			purchaseds = service.refBudgetDatas(getLong("ipurchaseid"));
		renderJsonData(purchaseds);
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
		Purchased purchased = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(purchased, JBoltMsg.DATA_NOT_EXIST);

		set("purchased", purchased);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(Purchased.class, "purchased")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(Purchased.class, "purchased")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}




}
