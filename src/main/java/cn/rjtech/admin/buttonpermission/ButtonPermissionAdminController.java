package cn.rjtech.admin.buttonpermission;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.model.ButtonPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.ButtonPermissionService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 按钮权限
 *
 * @ClassName: ButtonPermissionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-21 17:57
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/buttonpermission", viewPath = "/_view/admin/buttonpermission")
public class ButtonPermissionAdminController extends JBoltBaseController {

    @Inject
    private ButtonPermissionService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getInt("objectType"), getBoolean("isDeleted", false)));
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
    @Before(Tx.class)
    public void save() {
        renderJson(service.save(getModel(ButtonPermission.class, "buttonPermission")));
    }

    /**
     * 编辑
     */
    public void edit() {
        ButtonPermission buttonPermission = service.findById(getLong(0));
        if (buttonPermission == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("buttonPermission", buttonPermission);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(ButtonPermission.class, "buttonPermission")));
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


}
