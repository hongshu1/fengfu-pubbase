package cn.rjtech.admin.formapprovaldroleusers;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormapprovaldRoleusers;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 单据角色人员 Controller
 *
 * @ClassName: FormapprovaldRoleusersAdminController
 * @author: RJ
 * @date: 2023-06-05 14:32
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/formapprovaldroleusers", viewPath = "/_view/admin/formapprovaldroleusers")
public class FormapprovaldRoleusersAdminController extends BaseAdminController {

    @Inject
    private FormapprovaldRoleusersService service;

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
        FormapprovaldRoleusers formapprovaldRoleusers = service.findById(getLong(0));
        if (formapprovaldRoleusers == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("formapprovaldRoleusers", formapprovaldRoleusers);
        render("edit.html");
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
