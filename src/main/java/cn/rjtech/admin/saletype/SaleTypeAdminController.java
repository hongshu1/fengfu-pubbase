package cn.rjtech.admin.saletype;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SaleType;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 销售类型 Controller
 *
 * @ClassName: SaleTypeAdminController
 * @author: WYX
 * @date: 2023-03-28 11:04
 */
@CheckPermission(PermissionKey.SALETYPE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/saletype", viewPath = "/_view/admin/saletype")
public class SaleTypeAdminController extends BaseAdminController {

    @Inject
    private SaleTypeService service;

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

    @UnCheck
    public void selectData() {
        renderJsonData(service.selectData(getKv()));
    }

    @UnCheck
    public void selectAll() {
        renderJsonData(service.selectAll(getPageNumber(),getPageSize(),getKv()));
    }


    /**
     * 新增
     */
    @CheckPermission(PermissionKey.SALETYPE_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.SALETYPE_EDIT)
    public void edit() {
        SaleType saleType = service.findById(getLong(0));
        if (saleType == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("saleType", saleType);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.SALETYPE_ADD)
    public void save() {
        renderJson(service.save(getModel(SaleType.class, "saleType")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.SALETYPE_EDIT)
    public void update() {
        renderJson(service.update(getModel(SaleType.class, "saleType")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.SALETYPE_DELETE)
    public void deleteByIds() {
        String s = get("ids");
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleBstmpsMrp
     */
    public void toggleBstmpsMrp() {
        renderJson(service.toggleBstmpsMrp(getLong(0)));
    }

    /**
     * 切换toggleBDefault
     */
    public void toggleBDefault() {
        renderJson(service.toggleBDefault(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

}
