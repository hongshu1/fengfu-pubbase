package cn.rjtech.admin.personswipelog;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Personswipelog;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 质量管理-在库检单行配置 Controller
 *
 * @ClassName: PersonswipelogAdminController
 * @author: RJ
 * @date: 2023-05-05 20:31
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/personswipelog", viewPath = "/_view/admin/personswipelog")
public class PersonswipelogAdminController extends BaseAdminController {

    @Inject
    private PersonswipelogService service;

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
        Personswipelog personswipelog = service.findById(getLong(0));
        if (personswipelog == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("personswipelog", personswipelog);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Personswipelog.class, "personswipelog")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Personswipelog.class, "personswipelog")));
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


}
