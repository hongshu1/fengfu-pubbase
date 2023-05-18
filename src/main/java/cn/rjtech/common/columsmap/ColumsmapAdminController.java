package cn.rjtech.common.columsmap;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.common.model.Columsmap;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 字段映射 Controller
 *
 * @ClassName: ColumsmapAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-12-07 15:15
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/columsmap", viewPath = "/_view/admin/columsmap")
public class ColumsmapAdminController extends BaseAdminController {

    @Inject
    private ColumsmapService service;

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
        render("add().html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Columsmap columsmap = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(columsmap, JBoltMsg.DATA_NOT_EXIST);

        set("columsmap", columsmap);
        render("edit().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Columsmap.class, "columsmap")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Columsmap.class, "columsmap")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

}
