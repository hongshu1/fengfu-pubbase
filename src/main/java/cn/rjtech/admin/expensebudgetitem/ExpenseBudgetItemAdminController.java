package cn.rjtech.admin.expensebudgetitem;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.util.ValidationUtils;

/**
 * 费用预算项目 Controller
 *
 * @ClassName: ExpenseBudgetItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-15 10:02
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/expensebudgetitem", viewPath = "/_view/admin/expensebudgetitem")
public class ExpenseBudgetItemAdminController extends BaseAdminController {

    @Inject
    private ExpenseBudgetItemService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
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
        ExpenseBudgetItem expenseBudgetItem = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(expenseBudgetItem, JBoltMsg.DATA_NOT_EXIST);

        set("expenseBudgetItem", expenseBudgetItem);
        render("edit.html");
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 禀议参照选择数据
     */
    public void mdatas() {
        renderJsonData(service.paginateMdatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 费用预算差异管理
     */
    public void expenseBudgetDifferences(){
        render("differences_index.html");
    }
    /**
     * 费用预算差异管理数据源
     */
    public void  differencesManagementDatas(){
        renderJsonData(service.differencesManagementDatas(getKv()));
    }

    /**
     * 费用预算差异管理导出模板
     */
    public void differencesTplPage(){
        render("differencestpl_index.html");
    }
    
}
