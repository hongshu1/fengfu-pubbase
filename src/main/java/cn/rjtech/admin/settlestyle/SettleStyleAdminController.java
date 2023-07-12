package cn.rjtech.admin.settlestyle;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SettleStyle;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 结算方式 Controller
 *
 * @ClassName: SettleStyleAdminController
 * @author: WYX
 * @date: 2023-03-24 09:08
 */
@CheckPermission(PermissionKey.SETTLE_STYLE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/settlestyle", viewPath = "/_view/admin/settlestyle")
public class SettleStyleAdminController extends BaseAdminController {

    @Inject
    private SettleStyleService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.SETTLESTYLE_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.SETTLESTYLE_EDIT)
    public void edit() {
        SettleStyle settleStyle = service.findById(getLong(0));
        if (settleStyle == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("settleStyle", settleStyle);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.SETTLESTYLE_ADD)
    public void save() {
        renderJson(service.save(getModel(SettleStyle.class, "settleStyle")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.SETTLESTYLE_EDIT)
    public void update() {
        renderJson(service.update(getModel(SettleStyle.class, "settleStyle")));
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
    @CheckPermission(PermissionKey.SETTLESTYLE_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleBSSFlag
     */
    public void toggleBSSFlag() {
        renderJson(service.toggleBSSFlag(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }


}
