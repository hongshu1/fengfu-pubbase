package cn.rjtech.admin.padloginlog;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.bean.JBoltDateRange;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

import java.util.Date;

/**
 * 日志监控-平板登录日志
 *
 * @ClassName: PadLoginLogAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 10:35
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.PAD_LOGINLOG)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/padloginlog", viewPath = "/_view/admin/padloginlog")
public class PadLoginLogAdminController extends BaseAdminController {

    @Inject
    private PadLoginLogService service;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        JBoltDateRange dateRange = getDateRange();
        Date startTime = dateRange.getStartDate();
        Date endTime = dateRange.getEndDate();
        Kv kv = getKv();
        kv.set("starttime", startTime);
        kv.set("endtime", endTime);
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), kv));
    }

}
