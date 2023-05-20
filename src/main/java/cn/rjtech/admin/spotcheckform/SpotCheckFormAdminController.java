package cn.rjtech.admin.spotcheckform;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SpotCheckForm;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 点检表格 Controller
 *
 * @ClassName: SpotCheckFormAdminController
 * @author: RJ
 * @date: 2023-04-03 10:42
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/spotcheckform", viewPath = "/_view/admin/spotcheckform")
public class SpotCheckFormAdminController extends BaseAdminController {

    @Inject
    private SpotCheckFormService service;

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
        SpotCheckForm spotCheckForm = service.findById(getLong(0));
        if (spotCheckForm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("spotCheckForm", spotCheckForm);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SpotCheckForm.class, "spotCheckForm")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SpotCheckForm.class, "spotCheckForm")));
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

    /**
     * 切换toggleIsEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleIsEnabled(getLong(0)));
    }


    public void options() {
        renderJsonData(service.options());
    }
}
