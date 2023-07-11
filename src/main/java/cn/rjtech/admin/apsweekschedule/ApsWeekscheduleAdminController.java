package cn.rjtech.admin.apsweekschedule;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApsWeekschedule;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 月周排产纪录时间 Controller
 *
 * @ClassName: ApsWeekscheduleAdminController
 * @author: chentao
 * @date: 2023-04-18 10:57
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/apsweekschedule", viewPath = "/_view/admin/apsweekschedule")
public class ApsWeekscheduleAdminController extends BaseAdminController {

    @Inject
    private ApsWeekscheduleService service;

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
        ApsWeekschedule apsWeekschedule = service.findById(getLong(0));
        if (apsWeekschedule == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("apsWeekschedule", apsWeekschedule);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(ApsWeekschedule.class, "apsWeekschedule")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ApsWeekschedule.class, "apsWeekschedule")));
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
     * 切换toggleIsLocked
     */
    public void toggleIsLocked() {
        renderJson(service.toggleIsLocked(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }


}
