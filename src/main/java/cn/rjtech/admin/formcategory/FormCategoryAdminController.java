package cn.rjtech.admin.formcategory;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormCategory;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 系统设置-表单类别
 *
 * @ClassName: FormCategoryAdminController
 * @author: Kephon
 * @date: 2023-05-01 22:03
 */
@CheckPermission(PermissionKey.FORM_CATEGORY)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formcategory", viewPath = "/_view/admin/formcategory")
public class FormCategoryAdminController extends BaseAdminController {

    @Inject
    private FormCategoryService service;

    /**
     * 首页
     */
    public void index() {
        render("index().html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(FormCategory.class, "formCategory")));
    }

    /**
     * 编辑
     */
    public void edit() {
        FormCategory formCategory = service.findById(getLong(0));
        if (formCategory == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("formCategory", formCategory);
        render("edit().html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(FormCategory.class, "formCategory")));
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
     * 得到树形结构数据
     */
    @UnCheck
    public void tree(@Para(value = "keywords") String keywords) {
        renderJsonData(service.getTreeDatas(keywords));
    }

    /**
     * 表单类别 + 表单 JsTree
     */
    @UnCheck
    public void jstree() {
        renderJsonData(service.getJsTreeDatas(getInt("openLevel", 0), getKeywords()));
    }

    /**
     * 表单类别 + 表单 JsTree
     */
    @UnCheck
    public void treeDatas() {
        renderJsonData(service.getTreeDatas());
    }

}
