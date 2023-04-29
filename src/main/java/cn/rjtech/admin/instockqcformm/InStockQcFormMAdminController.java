package cn.rjtech.admin.instockqcformm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InStockQcFormM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 在库检 Controller
 *
 * @ClassName: InStockQcFormMAdminController
 * @author: RJ
 * @date: 2023-04-25 15:00
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/InStockQcFormM", viewPath = "/_view/admin/InStockQcFormM")
public class InStockQcFormMAdminController extends BaseAdminController {

    @Inject
    private InStockQcFormMService service;

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
        InStockQcFormM inStockQcFormM = service.findById(getLong(0));
        if (inStockQcFormM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("inStockQcFormM", inStockQcFormM);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(InStockQcFormM.class, "inStockQcFormM")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(InStockQcFormM.class, "inStockQcFormM")));
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
     * 切换toggleIsCompleted
     */
    public void toggleIsCompleted() {
        renderJson(service.toggleIsCompleted(getLong(0)));
    }

    /**
     * 切换toggleIsOk
     */
    public void toggleIsOk() {
        renderJson(service.toggleIsOk(getLong(0)));
    }

    /**
     * 切换toggleIsCpkSigned
     */
    public void toggleIsCpkSigned() {
        renderJson(service.toggleIsCpkSigned(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

}
