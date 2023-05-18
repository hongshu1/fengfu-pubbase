package cn.rjtech.admin.datapermission;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.main.DataPermission;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

/**
 * 数据权限
 *
 * @ClassName: DataPermissionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-23 16:02
 */
@CheckPermission(PermissionKey.DATA_PERMISSION)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/datapermission", viewPath = "/_view/admin/datapermission")
public class DataPermissionAdminController extends JBoltBaseController {

    @Inject
    private DataPermissionService service;

    /**
     * 首页
     */
    public void index() {
        render("mgr.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("id"), getSortType("desc"), getLong("busobjectId"), getInt("objectType"), getBoolean("isViewEnabled"), getBoolean("isEditEnabled"), getBoolean("isDeleteEnabled"), getBoolean("isDeleted", false)));
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
    @Before(Tx.class)
    public void save() {
        renderJson(service.save(getModel(DataPermission.class, "dataPermission")));
    }

    /**
     * 编辑
     */
    public void edit() {
        DataPermission dataPermission = service.findById(getLong(0));
        if (dataPermission == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("dataPermission", dataPermission);
        render("edit().html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(DataPermission.class, "dataPermission")));
    }

    /**
     * 批量删除
     */
    @Before(Tx.class)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    @Before(Tx.class)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换is_view_enabled
     */
    @Before(Tx.class)
    public void toggleIsViewEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "is_view_enabled"));
    }

    /**
     * 切换is_edit_enabled
     */
    @Before(Tx.class)
    public void toggleIsEditEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "is_edit_enabled"));
    }

    /**
     * 切换is_delete_enabled
     */
    @Before(Tx.class)
    public void toggleIsDeleteEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "is_delete_enabled"));
    }

    /**
     * 批量恢复假删数据
     */
    @Before(Tx.class)
    public void recoverByIds() {
        renderJson(service.recoverByIds(get("ids")));
    }

    /**
     * 批量 真删除
     */
    @Before(Tx.class)
    public void realDeleteByIds() {
        renderJson(service.realDeleteByIds(get("ids")));
    }

    /**
     * 数据权限
     */
    public void permissions(@Para(value = "objecttype") Integer objecttype,
                            @Para(value = "objectid") Long objectid,
                            @Para(value = "busobjectId") Long busobjectId,
                            @Para(value = "q") String q) {
        ValidationUtils.validateIntGt0(objecttype, "角色类型");
        ValidationUtils.validateId(objectid, "参数ID");
        ValidationUtils.validateId(busobjectId, "业务对象");

        renderJsonData(service.paginatePermissions(getPageNumber(), getPageSize(), objecttype, objectid, busobjectId, q));
    }

    public void savePermissions(@Para(value = "busobjectId") Long busobjectId,
                                @Para(value = "objecttype") Integer objecttype,
                                @Para(value = "objectid") Long objectid,
                                @Para(value = "permissions") String permissions) {
        ValidationUtils.validateId(busobjectId, "业务对象");
        ValidationUtils.validateIntGt0(objecttype, "业务对象类型");
        ValidationUtils.validateId(objectid, "业务对象ID");
        ValidationUtils.notBlank(permissions, "缺少权限");

        renderJson(service.savePermissions(busobjectId, objecttype, objectid, permissions, JBoltUserKit.getUser(), new Date()));
    }

}
