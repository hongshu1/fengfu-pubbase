package cn.rjtech.admin.expensemonthadjustmentd;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ExpenseMonthAdjustmentd;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 费用月度实绩调整明细 Controller
 * @ClassName: ExpenseMonthAdjustmentdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-12-09 14:59
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/expensemonthadjustmentd", viewPath = "/_view/admin/expensemonthadjustmentd")
public class ExpenseMonthAdjustmentdAdminController extends BaseAdminController {

	@Inject
	private ExpenseMonthAdjustmentdService service;

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
		ExpenseMonthAdjustmentd expenseMonthAdjustmentd = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(expenseMonthAdjustmentd, JBoltMsg.DATA_NOT_EXIST);

		set("expenseMonthAdjustmentd", expenseMonthAdjustmentd);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(ExpenseMonthAdjustmentd.class, "expenseMonthAdjustmentd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(ExpenseMonthAdjustmentd.class, "expenseMonthAdjustmentd")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}


}
