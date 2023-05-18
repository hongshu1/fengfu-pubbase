package cn.rjtech.admin.monthorderd;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Monthorderd;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 月度计划订单明细 Controller
 *
 * @ClassName: MonthorderdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 18:20
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/monthorderd", viewPath = "/_view/admin/monthorderd")
public class MonthorderdAdminController extends BaseAdminController {

    @Inject
    private MonthorderdService service;

    /**
     * 首页
     */
    public void index() {
        render("index().html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add().html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Monthorderd monthorderd = service.findById(getLong(0));
        if (monthorderd == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("monthorderd", monthorderd);
        render("edit().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Monthorderd.class, "monthorderd")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Monthorderd.class, "monthorderd")));
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
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    @UnCheck
    public void findEditTableDatas() {
        renderJsonData(service.findEditTableDatas(getKv()));
    }

}
