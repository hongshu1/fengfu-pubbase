package cn.rjtech.admin.appversion;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.Constants;
import cn.rjtech.model.main.Appversion;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;

/**
 * @ClassName: AppversionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2020-09-16 17:03
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.APPVERSION)
@Path(value = "/admin/appversion", viewPath = "/_view/admin/appversion")
public class AppversionAdminController extends BaseAdminController {

    @Inject
    private AppversionService service;

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
        Page<Appversion> page = service.paginateAdminList(getPageNumber(), getPageSize(), getSortColumn(), getSortType());

        for (Appversion row : page.getList()) {
            row.setDownloadUrl(Constants.getFullUrl(row.getDownloadUrl()));
        }

        ok(page);
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
        Long autoid = getLong(0);
        Appversion appversion = service.findById(autoid);
        ValidationUtils.notNull(appversion, JBoltMsg.DATA_NOT_EXIST);

        set("appversion", appversion);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Appversion.class, "appversion", "APP版本")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Appversion.class, "appversion", "APP版本")));
    }

    /**
     * 导出下载Excel文件
     */
    public void download() {
        renderBytesToExcelXlsFile(service.getExcelReport().setFileName("APP版本信息导出"));
    }

    /**
     * 改变发布状态
     */
    public void putaway() {
        renderJson(service.putaway(getLong(0)));
    }

    /**
     * 是否强制更新
     */
    public void updateisforced() {
        renderJson(service.updateisforced(getLong(0)));
    }

}