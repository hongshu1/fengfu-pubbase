package cn.rjtech.admin.org;

import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.OrgService;
import cn.rjtech.admin.userorg.UserOrgService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 组织
 *
 * @author Kephon
 */
@UnCheck
@Path(value = "/admin/org")
public class OrgAdminController extends JBoltBaseController {

    @Inject
    private OrgService service;
    @Inject
    private UserOrgService userOrgService;

    public void list() {
        renderJsonData(service.getList());
    }

    /**
     * 当前用户具备权限的组织
     */
    public void accessList() {
        User user = JBoltUserKit.getUser();
        if (user.getIsSystemAdmin()) {
            renderJsonData(service.getList());
            return;
        }
        renderJsonData(userOrgService.getAccessList(JBoltUserKit.getUserId()));
    }

}
