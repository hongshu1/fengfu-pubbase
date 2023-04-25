package cn.rjtech.admin.cusorderresult;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

import java.text.ParseException;
import java.util.List;

/**
 * 客户计划汇总及实绩管理
 */
//@CheckPermission(PermissionKey.CUSORDER_RESULT)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusorderresult", viewPath = "/_view/admin/cusorderresult")
public class CusOrderResultController extends BaseAdminController {
    @Inject
    private CusOrderSumService service;
    @Inject
    private CusOrderResultService orderResultService;


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

      renderJsonData(orderResultService.findCusOrderResult(getPageNumber(), getPageSize(), getKv()));

    }

}
