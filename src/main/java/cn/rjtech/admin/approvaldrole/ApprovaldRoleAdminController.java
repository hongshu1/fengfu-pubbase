package cn.rjtech.admin.approvaldrole;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApprovaldRole;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 审批流节点审批角色 Controller
 *
 * @ClassName: ApprovaldRoleAdminController
 * @author: RJ
 * @date: 2023-04-18 17:09
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/approvaldrole", viewPath = "/_view/admin/approvaldrole")
public class ApprovaldRoleAdminController extends BaseAdminController {

    @Inject
    private ApprovaldRoleService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
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
        ApprovaldRole approvaldRole = service.findById(getLong(0));
        if (approvaldRole == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("approvaldRole", approvaldRole);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(ApprovaldRole.class, "approvaldRole")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ApprovaldRole.class, "approvaldRole")));
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


}
