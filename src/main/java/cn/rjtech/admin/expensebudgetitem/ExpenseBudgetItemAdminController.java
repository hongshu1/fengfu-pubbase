package cn.rjtech.admin.expensebudgetitem;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.annotation.CheckDataPermission;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import cn.jbolt.core.poi.excel.JBoltExcelPositionData;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.util.ValidationUtils;
import java.util.ArrayList;
import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

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
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void mdatas() {
        renderJsonData(service.paginateMdatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 费用预算差异管理
     */
    @CheckPermission(PermissionKey.EXPENSE_BUDGET_DIFFERENCES)
    public void expenseBudgetDifferences(){
        render("differences_index.html");
    }
    /**
     * 费用预算差异管理数据源
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void differencesManagementDatas(){
        renderJsonData(service.differencesManagementDatas(getKv()));
    }

    /**
     * 费用预算差异管理导出模板
     */
    public void differencesTplPage(){
        render("differencestpl_index.html");
    }
    /**
     * 导出费用预实差异报表数据
     * */
    @UnCheck
    public void exportDifferencesManagementDatas(){
        Kv para = getKv();
        List<JBoltExcelPositionData> excelPositionDatas = new ArrayList<>();//定位数据集合
        List<JBoltExcelMerge> mergeList = new ArrayList<JBoltExcelMerge>();
        service.constructExportDifferencesManagementDatas(para,excelPositionDatas,mergeList);
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .createByTpl("expensebudgetdiff.xlsx")//创建JBoltExcel 从模板加载创建
                .addSheet(//设置sheet
                        JBoltExcelSheet.create("费用预实差异")//创建sheet name保持与模板中的sheet一致
                                .setPositionDatas(excelPositionDatas)//设置定位数据
                                .setMerges(mergeList)
                )
                .setFileName("费用预实差异数据");
        //3、导出
        renderBytesToExcelXlsxFile(jBoltExcel);
    }    
}
