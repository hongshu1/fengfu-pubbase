package cn.rjtech.admin.inventoryplan;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryPlan;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 料品计划档案
 *
 * @ClassName: InventoryPlanAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:57
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryplan", viewPath = "/_view/admin/inventoryplan")
public class InventoryPlanAdminController extends BaseAdminController {

    @Inject
    private InventoryPlanService service;

    /**
     * 首页
     */
    public void index() {
        render("index().html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(InventoryPlan.class, "inventoryPlan")));
    }

    /**
     * 编辑
     */
    public void edit() {
        InventoryPlan inventoryPlan = service.findById(getLong(0));
        if (inventoryPlan == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("inventoryPlan", inventoryPlan);
        render("edit().html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(InventoryPlan.class, "inventoryPlan")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    @UnCheck
    public void test() {
        service.test();
        ok();
    }
    
}
