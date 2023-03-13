package cn.rjtech.admin.busobject;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.main.Busobject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

/**
 * 业务对象
 *
 * @ClassName: BusobjectAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-23 13:41
 */
@CheckPermission(PermissionKey.BUS_OBJECT)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/busobject", viewPath = "/_view/admin/busobject")
public class BusobjectAdminController extends JBoltBaseController {

    @Inject
    private BusobjectService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("id"), getSortType("desc"), getLong("appId"), get("dataSource"), getBoolean("isEnabled"), getBoolean("isDeleted", false)));
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
        renderJson(service.save(getModel(Busobject.class, "busobject"), JBoltUserKit.getUser(), new Date()));
    }

    /**
     * 编辑
     */
    public void edit() {
        Busobject busobject = service.findById(getLong(0));
        if (busobject == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("busobject", busobject);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(Busobject.class, "busobject"), JBoltUserKit.getUser(), new Date()));
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
     * 切换is_enabled
     */
    @Before(Tx.class)
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "is_enabled"));
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

    public void options() {
        renderJsonData(service.getOptionList(Busobject.BUSOBJECT_NAME, Busobject.ID, Okv.by(Busobject.IS_DELETED, "0").set(Busobject.IS_ENABLED, "1")));
    }

}
