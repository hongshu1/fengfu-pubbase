package cn.rjtech.admin.application;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.RjApplication;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.RjApplicationService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

/**
 * 应用系统
 *
 * @ClassName: RjApplicationAdminController
 * @author: Kephon
 * @date: 2022-11-07 13:49
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.RJ_APPLICATION)
@Path(value = "/admin/application", viewPath = "/_view/admin/application")
public class RjApplicationAdminController extends JBoltBaseController {

    @Inject
    private RjApplicationService service;

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
        // renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("id"), getSortType("desc"), getBoolean("isLeaf"), getBoolean("isEffective"), getBoolean("isDeleted", false)))
        renderJsonData(service.getDatasWithLevel());
    }

    /**
     * 新增
     */
    public void add() {
        RjApplication rjApplication = new RjApplication();
        rjApplication.setParentId(getLong(0, 0L));
        rjApplication.setNodeLevel(getInt(1, 1));
        set("application", rjApplication);
        render("add.html");
    }

    /**
     * 保存
     */
    @Before(Tx.class)
    public void save() {
        renderJson(service.save(getModel(RjApplication.class, "application"), JBoltUserKit.getUser(), new Date()));
    }

    /**
     * 编辑
     */
    public void edit() {
        RjApplication rjApplication = service.findById(getLong(0));
        if (rjApplication == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("application", rjApplication);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(RjApplication.class, "application"), JBoltUserKit.getUser(), new Date()));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delApplicationById(getLong(0)));
    }

    /**
     * 切换is_leaf
     */
    @Before(Tx.class)
    public void toggleIsLeaf() {
        renderJson(service.toggleBoolean(getLong(0), "is_leaf"));
    }

    /**
     * 切换is_effective
     */
    @Before(Tx.class)
    public void toggleIsEffective() {
        renderJson(service.toggleBoolean(getLong(0), "is_effective"));
    }

    /**
     * 恢复假删数据
     */
    @Before(Tx.class)
    public void recover() {
        renderJson(service.recoverById(getLong(0)));
    }

    /**
     * real delete 真删除
     */
    @Before(Tx.class)
    public void realDelete() {
        renderJson(service.realDeleteById(getLong(0)));
    }

    public void options() {
        renderJsonData(service.getAllPermissionsOptionsWithLevel());
    }

    public void up() {
        renderJson(service.up(getLong(0), JBoltUserKit.getUser(), new Date()));
    }

    public void down() {
        renderJson(service.down(getLong(0), JBoltUserKit.getUser(), new Date()));
    }


}
