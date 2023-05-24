package cn.rjtech.admin.project;

import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Project;
import cn.rjtech.util.ValidationUtils;

/**
 * 项目档案 Controller
 *
 * @ClassName: ProjectAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 11:45
 */
@CheckPermission(PermissionKey.PROJECT)
@UnCheckIfSystemAdmin
@Path(value = "/admin/project", viewPath = "/_view/admin/project")
public class ProjectAdminController extends BaseAdminController {

    @Inject
    private ProjectService service;


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
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Project project = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(project, JBoltMsg.DATA_NOT_EXIST);
        set("project", project);
        set("icreateby", JBoltUserCache.me.getUserName(project.getIcreateby()));
        if (project.getIupdateby() != null) {
            set("iupdateby", JBoltUserCache.me.getUserName(project.getIcreateby()));
        }
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Project.class, "project")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Project.class, "project")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 存货编码
     */
    @UnCheck
    public void u8Inventory() {
        render("project_inventory.html");
    }

    /**
     * U8存货编码
     */
    @UnCheck
    public void u8InventoryList() {
        renderJsonData(service.u8InventoryList(getPageNumber(), getPageSize(), getKv()));
    }

    @UnCheck
    public void autocomplete() {
        renderJsonData(service.getAutocompleteList(get("q"), getInt("limit", 10)));
    }

}
