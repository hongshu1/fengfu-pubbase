package cn.rjtech.admin.menupermission;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.MenuPermission;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.service.MenuPermissionService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

/**
 * 菜单权限
 *
 * @ClassName: MenuPermissionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-21 17:33
 */
@CheckPermission(PermissionKey.MENU_PERMISSION)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/menupermission", viewPath = "/_view/admin/menupermission")
public class MenuPermissionAdminController extends JBoltBaseController {

    @Inject
    private MenuPermissionService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getSortColumn("id"), getSortType("desc"), getInt("objectType"), getBoolean("isDeleted", false)));
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
        renderJson(service.save(getModel(MenuPermission.class, "menuPermission")));
    }

    /**
     * 编辑
     */
    public void edit() {
        MenuPermission menuPermission = service.findById(getLong(0));
        if (menuPermission == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("menuPermission", menuPermission);
        render("edit().html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(MenuPermission.class, "menuPermission")));
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
     * 保存角色/用户 菜单及按钮权限
     */
    public void savePermissions(@Para(value = "roletype") Integer roleType,
                                @Para(value = "id") Long id,
                                @Para(value = "menuIds") String menuIdsStr,
                                @Para(value = "buttonIds") String buttonIds) {
        ValidationUtils.validateIntGt0(roleType, "角色类型");
        ValidationUtils.validateId(id, "参数ID");

        renderJson(service.savePermissions(roleType, id, menuIdsStr, buttonIds, JBoltUserKit.getUser(), new Date()));
    }

}
