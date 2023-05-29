package cn.rjtech.admin.expensebudgetmanage;

import cn.jbolt._admin.permission.PermissionKey;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author Heming
 */
@CheckPermission(PermissionKey.EXPENSE_BUDGET_MANAGE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/expensebudgetmanage", viewPath = "/_view/admin/expensebudgetmanage")
public class ExpenseBudgetManageAdminController extends BaseAdminController {

    @Inject
    private PeriodService periodService;
    @Inject
    private ExpenseBudgetManageService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 详情
     */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_MANAGE_DETAIL)
    public void detail() {
        Record expenseBudget = service.findExpenseBudgetData(getLong(0));
        set("expenseBudget", expenseBudget);
        Date dstarttime = expenseBudget.getDate("cbegindate");
        Date dendtime = expenseBudget.getDate("cenddate");
        List<Record> yearColumnTxtList = new ArrayList<>();
        List<String> monthColumnTxtList = new ArrayList<>();
        List<Record> quantityAndAmountColumnList = new ArrayList<>();
        periodService.calcDynamicExpenseBudgetTableColumn(dstarttime,dendtime,yearColumnTxtList,monthColumnTxtList,quantityAndAmountColumnList);
        set("expenseBudget", expenseBudget);
        set("yearcolumntxtlist",yearColumnTxtList);
        set("monthcolumntxtlist",monthColumnTxtList);
        set("quantityandamountcolumnlist",quantityAndAmountColumnList);
        render("detail.html");
    }
    
    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }
    
    /**
     *	费用预算生效 
     * */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_MANAGE_EFFECT)
    public void effect(){
    	renderJson(service.effect(getLong(0)));
    }

    /**
     * 费用预算作废
     */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_MANAGE_CANCLE)
    public void cancle(){
    	renderJson(service.cancle(getLong(0)));
    }
    
}
