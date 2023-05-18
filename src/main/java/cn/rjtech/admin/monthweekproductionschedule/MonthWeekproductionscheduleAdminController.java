package cn.rjtech.admin.monthweekproductionschedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltDateUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 月周生产计划排产
 *
 * @author ：佛山市瑞杰科技有限公司
 * @description ：MonthWeekproductionscheduleAdminController
 * @date ：2023/3/21 18:32
 */
@CheckPermission(PermissionKey.MONTHLY_AND_WEEKLY_PRODUCTION_SCHEDULE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/monthweekproductionschedule", viewPath = "/_view/admin/monthweekproductionschedule")
public class MonthWeekproductionscheduleAdminController extends JBoltBaseController {
    @Inject
    private MonthWeekproductionscheduleService service;

    public void index() {
        render("index().html");
    }

    @UnCheck
    public void chooseData() {
        Date date = JBoltDateUtil.nextMonthDate(new Date());

        // 两个月后的今天
        set("layDate", JBoltDateUtil.nextMonthDate(date));

        // 获取已排产信息
        List<Record> data = service.productionSchedulingInformation();
        set("dataList", data);
        render("chooseData.html");
    }

    public void lock() {
        DateTime dateTime = DateUtil.endOfWeek(DateUtil.date());
        // 本周周末
        set("lockDate", dateTime);
        set("lockWeek", DateUtil.weekOfYear(dateTime));
        render("lock.html");
    }

    public void unLock() {
        DateTime dateTime = DateUtil.endOfWeek(DateUtil.date());
        // 本周周末
        set("unLockDate", dateTime);
        set("unLockWeek", DateUtil.weekOfYear(dateTime));
        render("unLock.html");
    }

    public void ajaxCheckLockDate() {
        Date data = getDate("data");
        Week week = DateUtil.dayOfWeekEnum(data);
        if (Week.SUNDAY == week) {
            renderJsonSuccess();
        } else {
            renderFail("只能选择周日");
        }
    }

    public void querySchedulingData() {
        service.querySchedulingData(getKv());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("data", "");
        dataMap.put("state", "ok");
        renderJson(dataMap);
    }

    public void demo() {

    }

}
