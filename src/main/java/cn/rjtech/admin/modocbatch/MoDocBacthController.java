package cn.rjtech.admin.modocbatch;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/modocbatch", viewPath = "/_view/admin/modocbatch")
public class MoDocBacthController extends BaseAdminController {

    @Inject
    private MoDocService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
    }
}
