package cn.rjtech.admin.yearproductionschedule;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

import java.util.Date;

/**
 * 年度生产计划排产
 *
 * @author ：佛山市瑞杰科技有限公司
 * @description ：YearProductionScheduleAdminController
 * @date ：2023/3/20 14:36
 */
// @CheckPermission(PermissionKey.YEAR_PRODUCTION_SCHEDULE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/yearproductionschedule", viewPath = "/_view/admin/yearproductionschedule")
public class YearProductionScheduleAdminController extends JBoltBaseController {

    public void index() {
        render("index.html");
    }

    public void datas() {
        renderJson();
    }

    public void add() {
        render("add.html");
    }

    public void chooseData() {
        Kv kv = getKv();
        set("layYear", kv.isNull("layYear") ? DateUtil.year(new Date()) : kv.get("layYear"));
        set("customerName", kv.isNull("customerName") ? 1 : kv.get("customerName"));

        render("choosedata.html");
    }

    public void detail() {
        Long id = getLong(0);
        // 获取数据

        // 根据年份生成头部信息
        String yearStr = "2023";
        set("layYear", yearStr);
        set("nextLayYear", Integer.parseInt(yearStr) + 1);
        render("detail.html");
    }

}
