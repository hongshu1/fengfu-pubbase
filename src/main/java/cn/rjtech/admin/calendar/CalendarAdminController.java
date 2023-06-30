package cn.rjtech.admin.calendar;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Calendar;
import cn.rjtech.util.DateUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 日历配置 Controller
 *
 * @ClassName: CalendarAdminController
 * @author: chentao
 * @date: 2022-11-23 15:04
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.CALENDAR)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/calendar", viewPath = "/_view/admin/calendar")
public class CalendarAdminController extends JBoltBaseController {

    @Inject
    private CalendarService service;

    /**
     * 首页
     */
    public void index() {
        set("year", DateUtils.getYear());
        render("editCalendar.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    @UnCheck
    public void getTakeDateListByYear() {
        renderJsonData(service.getTakeDateListByYear(getKv()));
    }

    public void saveCalendar() {
        renderJson(service.saveCalendar(getKv()));
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
        Calendar calendar = service.findById(getLong(0));
        if (calendar == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("calendar", calendar);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Calendar.class, "calendar")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Calendar.class, "calendar")));
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
     * 切换toggleIcaluedartype
     */
    public void toggleIcaluedartype() {
        renderJson(service.toggleIcaluedartype(getLong(0)));
    }


}
