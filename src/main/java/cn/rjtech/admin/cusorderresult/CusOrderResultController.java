package cn.rjtech.admin.cusorderresult;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.Constants;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

/**
 * 客户计划汇总及实绩管理
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.CUSORDER_RESULT)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusorderresult", viewPath = "/_view/admin/cusorderresult")
public class CusOrderResultController extends BaseAdminController {

    @Inject
    private CusOrderResultService orderResultService;
    @Inject
    private ScheduProductPlanMonthService service;

    /**
     * 首页
     */
    public void index() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        set("startdate", calendar.getTime());
        int lastDayOfMonth = DateUtil.getLastDayOfMonth(DateUtil.date());
        calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
        set("enddate", calendar.getTime());
        render("index.html");
    }
    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
      renderJsonData(orderResultService.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }
    /**
     * 获取表格
     */
    @UnCheck
    public void getYear() {
        Kv kv = getKv();

        Date startdate = kv.getDate("startdate");
        Date endDate = kv.getDate("enddate");

        ValidationUtils.notNull(startdate, "开始时间不能为空");
        ValidationUtils.notNull(endDate, "结束时间不能为空");
        ValidationUtils.isTrue(DateUtil.compare(endDate, startdate) >= 0, "结束时间必须大于等于开始时间");

        // 保留参数
        keepPara();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startdate);

        Map<String, List<Date>> dateMap = new LinkedHashMap<>();

        while (DateUtil.compare(endDate, calendar.getTime()) >= 0) {
            // 年月 - 年-月-日
            String key = calendar.get(Calendar.YEAR) + StrUtil.UNDERLINE + (calendar.get(Calendar.MONTH) + 1);
            List<Date> dateList = dateMap.containsKey(key) ? dateMap.get(key) : new ArrayList<>();
            dateList.add(calendar.getTime());
            dateMap.put(key, dateList);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        set("dateMap", dateMap);

        render("year.html");
    }

    /**
     * 刷新
     */
    public void algorithmSum() {
        renderJson(service.algorithmSum());
    }

    /**
     * 导出z
     */
    @CheckPermission(PermissionKey.CUSORDERRESULT_EXPORT)
    public void export()
    {

    }
}
