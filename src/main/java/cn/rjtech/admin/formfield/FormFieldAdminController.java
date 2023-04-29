package cn.rjtech.admin.formfield;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormField;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统配置-表单字段
 *
 * @ClassName: FormFieldAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-29 20:18
 */
@CheckPermission(PermissionKey.FORM_FIELD)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formfield", viewPath = "/_view/admin/formfield")
public class FormFieldAdminController extends BaseAdminController {

    @Inject
    private FormFieldService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("cFieldTypeSn"), getBoolean("isImportField"), getBoolean("isDeleted")));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(FormField.class, "formField")));
    }

    /**
     * 编辑
     */
    public void edit() {
        FormField formField = service.findById(getLong(0));
        if (formField == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("formField", formField);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(FormField.class, "formField")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换isImportField
     */
    public void toggleIsImportField() {
        renderJson(service.toggleBoolean(getLong(0), "isImportField"));
    }


}
