package cn.rjtech.admin.investmentplanitem;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.annotation.CheckDataPermission;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InvestmentPlanItem;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 投资计划项目 Controller
 *
 * @ClassName: InvestmentPlanItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-18 09:37
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/investmentplanitem", viewPath = "/_view/admin/investmentplanitem")
public class InvestmentPlanItemAdminController extends BaseAdminController {

    @Inject
    private InvestmentPlanItemService service;

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
        InvestmentPlanItem investmentPlanItem = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(investmentPlanItem, JBoltMsg.DATA_NOT_EXIST);

        set("investmentPlanItem", investmentPlanItem);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(InvestmentPlanItem.class, "investmentPlanItem")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(InvestmentPlanItem.class, "investmentPlanItem")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 切换toggleIsimport
     */
    public void toggleIsimport() {
        renderJson(service.toggleIsimport(getLong(0)));
    }

    /**
     * 切换toggleIspriorreport
     */
    public void toggleIspriorreport() {
        renderJson(service.toggleIspriorreport(getLong(0)));
    }
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void mdatas() {
        renderJsonData(service.paginateMdatas(getPageNumber(), getPageSize(), getKv()));
    }

}
