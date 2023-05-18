package cn.rjtech.admin.org;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Org;
import cn.jbolt.core.model.User;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.JBoltOrgService;
import cn.rjtech.admin.userorg.UserOrgService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;

/**
 * 组织
 *
 * @author Kephon
 */
@UnCheck
@CheckPermission(PermissionKey.ORGMGR)
@Path(value = "/admin/org", viewPath = "/_view/admin/org")
public class OrgAdminController extends BaseAdminController {
    
    @Inject
    private JBoltOrgService services;
    @Inject
    private UserOrgService userOrgService;
    @Inject
    private OrgService service;

    /**
     * 登陆页面下拉框列表
     */
    @Clear(JBoltAdminAuthInterceptor.class)
    public void list() {
        ok(service.getList());
    }

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
        ok(service.paginateAdminList(getPageNumber(), getPageSize(), Okv.create().set(getKv())));
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
        Org org = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(org, JBoltMsg.DATA_NOT_EXIST);

        set("org", org);
        render("edit().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Org.class, "org")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Org.class, "org")));
    }

    /**
     * 删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(useIfNotBlank(get("ids"))));
    }

    /**
     * 切换启用状态
     */
    public void toggleEnable() {
        renderJson(service.toggleEnable(useIfPresent(getLong(0))));
    }

    /**
     * 切换启用状态
     */
    public void toggleIsDefault() {
        renderJson(service.toggleIsDefault(useIfPresent(getLong(0))));
    }

    /**
     * 获取U8数据库列表
     */
    public void u8dbs() {
        renderJsonData(service.getU8DbList());
    }

    /**
     * 当前用户具备权限的组织
     */
    public void accessList() {
        User user = JBoltUserKit.getUser();
        if (user.getIsSystemAdmin()) {
            renderJsonData(services.getEnabledList());
            return;
        }
        renderJsonData(userOrgService.getAccessList(JBoltUserKit.getUserId()));
    }
}