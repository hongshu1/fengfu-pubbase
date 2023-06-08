package cn.rjtech.admin.expensemonthadjustmentm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ExpenseMonthAdjustmentm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
/**
 * 费用月度实绩调整 Controller
 * @ClassName: ExpenseMonthAdjustmentmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-12-09 14:58
 */
@CheckPermission(PermissionKey.EXPENSE_MONTH_ADJUSTMENTM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/expensemonthadjustmentm", viewPath = "/_view/admin/expensemonthadjustmentm")
public class ExpenseMonthAdjustmentmAdminController extends BaseAdminController {

	@Inject
	private ExpenseMonthAdjustmentmService service;

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
	@CheckPermission(PermissionKey.EXPENSE_MONTH_ADJUSTMENTM_ADD)
	public void add() {
		render("add.html");
	}

   /**
	* 编辑
	*/
	@CheckPermission(PermissionKey.EXPENSE_MONTH_ADJUSTMENTM_EDIT)
	public void edit() {
		ExpenseMonthAdjustmentm expenseMonthAdjustmentm = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(expenseMonthAdjustmentm, JBoltMsg.DATA_NOT_EXIST);
		set("expenseMonthAdjustmentm", expenseMonthAdjustmentm);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(ExpenseMonthAdjustmentm.class, "expenseMonthAdjustmentm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(ExpenseMonthAdjustmentm.class, "expenseMonthAdjustmentm")));
	}

   /**
	* 批量删除
	*/
	@CheckPermission(PermissionKey.EXPENSE_MONTH_ADJUSTMENTM_DELETE)
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}
	
	/**
	 * 新增表格提交
	 * */
	public void saveTableByAdd(){
		renderJson(service.saveTableByAdd(getJBoltTable()));
	}
	
	/**
	 * 编辑表格提交
	 * */
	public void saveTableByUpdate(){
		renderJson(service.saveTableByUpdate(getJBoltTable()));
	}
	/**
	 * 修改界面查询明细表数据
	 * */
	public void findExpensemonthadjustmentdDatas(@Para(value="iadjustmentmid") Long iAdjustmentmId){
		renderJsonData(service.findExpensemonthadjustmentdDatas(iAdjustmentmId));
	}
	/**
	 * 生效
	 * */
	@CheckPermission(PermissionKey.EXPENSE_MONTH_ADJUSTMENTM_EFFECT)
	public void effect(){
		renderJson(service.effect(getLong(0)));
	}
	/**
	 * 撤销生效
	 * */
	@CheckPermission(PermissionKey.EXPENSE_MONTH_ADJUSTMENTM_UNEFFECT)
	public void unEffect(){
		renderJson(service.unEffect(getLong(0)));
	}
}
