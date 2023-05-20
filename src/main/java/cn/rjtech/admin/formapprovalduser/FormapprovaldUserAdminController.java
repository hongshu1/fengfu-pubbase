package cn.rjtech.admin.formapprovalduser;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormapprovaldUser;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 表单审批流审批人 Controller
 *
 * @ClassName: FormapprovaldUserAdminController
 * @author: RJ
 * @date: 2023-04-18 17:33
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formapprovalduser", viewPath = "/_view/admin/formapprovalduser")
public class FormapprovaldUserAdminController extends BaseAdminController {

    @Inject
    private FormapprovaldUserService service;

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
        FormapprovaldUser formapprovaldUser = service.findById(getLong(0));
        if (formapprovaldUser == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("formapprovaldUser", formapprovaldUser);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(FormapprovaldUser.class, "formapprovaldUser")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(FormapprovaldUser.class, "formapprovaldUser")));
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
