package cn.rjtech.admin.investmentplanmonthadjustmentitem;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentItem;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 投资月度实绩项目 Controller
 * @ClassName: InvestmentplanMonthAdjustmentItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-02-02 17:48
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/investmentplanmonthadjustmentitem", viewPath = "/_view/admin/investmentplanmonthadjustmentitem")
public class InvestmentplanMonthAdjustmentItemAdminController extends BaseAdminController {

	@Inject
	private InvestmentplanMonthAdjustmentItemService service;

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
		InvestmentplanMonthAdjustmentItem investmentplanMonthAdjustmentItem = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(investmentplanMonthAdjustmentItem, JBoltMsg.DATA_NOT_EXIST);

		set("investmentplanMonthAdjustmentItem", investmentplanMonthAdjustmentItem);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(InvestmentplanMonthAdjustmentItem.class, "investmentplanMonthAdjustmentItem")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(InvestmentplanMonthAdjustmentItem.class, "investmentplanMonthAdjustmentItem")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}


}
