package cn.rjtech.admin.purchasetype;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.PurchaseType;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 采购类型 Controller
 *
 * @ClassName: PurchaseTypeAdminController
 * @author: WYX
 * @date: 2023-04-03 10:52
 */
@CheckPermission(PermissionKey.PURCHASETYPE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchasetype", viewPath = "/_view/admin/purchasetype")
public class PurchaseTypeAdminController extends BaseAdminController {

    @Inject
    private PurchaseTypeService service;

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

    @UnCheck
    public void selectAll() {
        renderJsonData(service.selectAll(getKv()));
    }

    @UnCheck
    public void selectDatas() {
        renderJsonData(service.selectDatas(getPageNumber(),getPageSize(),getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.PURCHASETYPE_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.PURCHASETYPE_EDIT)
    public void edit() {
        PurchaseType purchaseType = service.findById(getLong(0));
        if (purchaseType == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("purchaseType", purchaseType);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.PURCHASETYPE_ADD)
    public void save() {
        renderJson(service.save(getModel(PurchaseType.class, "purchaseType")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.PURCHASETYPE_EDIT)
    public void update() {
        renderJson(service.update(getModel(PurchaseType.class, "purchaseType")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.PURCHASETYPE_DELETE)
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
     * 切换toggleBPFDefault
     */
    public void toggleBPFDefault() {
        renderJson(service.toggleBPFDefault(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /**
     * 切换toggleBptmpsMrp
     */
    public void toggleBptmpsMrp() {
        renderJson(service.toggleBptmpsMrp(getLong(0)));
    }

    /**
     * 切换toggleBDefault
     */
    public void toggleBDefault() {
        renderJson(service.toggleBDefault(getLong(0)));
    }
}
