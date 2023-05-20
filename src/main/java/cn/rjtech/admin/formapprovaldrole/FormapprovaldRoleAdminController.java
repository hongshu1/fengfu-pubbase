package cn.rjtech.admin.formapprovaldrole;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormapprovaldRole;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 表单审批流审批角色 Controller
 *
 * @ClassName: FormapprovaldRoleAdminController
 * @author: RJ
 * @date: 2023-04-18 17:30
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formapprovaldrole", viewPath = "/_view/admin/formapprovaldrole")
public class FormapprovaldRoleAdminController extends BaseAdminController {

    @Inject
    private FormapprovaldRoleService service;

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
        FormapprovaldRole formapprovaldRole = service.findById(getLong(0));
        if (formapprovaldRole == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("formapprovaldRole", formapprovaldRole);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(FormapprovaldRole.class, "formapprovaldRole")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(FormapprovaldRole.class, "formapprovaldRole")));
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
