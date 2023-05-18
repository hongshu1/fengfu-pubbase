package cn.rjtech.admin.form;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Form;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 表单管理 Controller
 *
 * @ClassName: FormAdminController
 * @author: RJ
 * @date: 2023-04-18 17:24
 */
@CheckPermission(PermissionKey.FORM_CONFIG)
@UnCheckIfSystemAdmin
@Path(value = "/admin/form", viewPath = "/_view/admin/form")
public class FormAdminController extends BaseAdminController {

    @Inject
    private FormService service;

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
        Form form = service.findById(getLong(0));
        if (form == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("form", form);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Form.class, "form")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Form.class, "form")));
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

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

}
