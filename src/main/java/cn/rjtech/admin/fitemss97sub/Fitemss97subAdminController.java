package cn.rjtech.admin.fitemss97sub;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Fitemss97sub;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 项目管理大类项目子目录 Controller
 *
 * @ClassName: Fitemss97subAdminController
 * @author: heming
 * @date: 2023-03-27 10:22
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.FITEMSS97SUB)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/fitemss97sub", viewPath = "/_view/admin/fitemss97sub")
public class Fitemss97subAdminController extends BaseAdminController {

    @Inject
    private Fitemss97subService service;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 右表
     */
    public void fitemss97subTable(){
        render("_table_fitemss97sub.html");
    }






    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 树结构数据源
     */
    public void mgrTree() {
        renderJsonData(service.getMgrTree( getInt("openLevel", 0),get("sn",null)));
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
        Fitemss97sub fitemss97sub = service.findById(getLong(0));
        if (fitemss97sub == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("fitemss97sub", fitemss97sub);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Fitemss97sub.class, "fitemss97sub")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Fitemss97sub.class, "fitemss97sub")));
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
     * 切换toggleIclose
     */
    public void toggleIclose() {
        renderJson(service.toggleIclose(getLong(0)));
    }
}
