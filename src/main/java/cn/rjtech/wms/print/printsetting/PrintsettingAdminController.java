package cn.rjtech.wms.print.printsetting;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.controller.BaseMesAdminController;
import cn.rjtech.common.model.Printsetting;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 打印设置 Controller
 *
 * @ClassName: PrintsettingAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-24 14:24
 */
@CheckPermission(PermissionKey.PRINTSETTING)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/printsetting", viewPath = "/_view/admin/printsetting/")
public class PrintsettingAdminController extends BaseMesAdminController {

    @Inject
    private PrintsettingService service;

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
        Printsetting printsetting = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(printsetting, JBoltMsg.DATA_NOT_EXIST);

        set("printsetting", printsetting);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Printsetting.class, "printsetting")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Printsetting.class, "printsetting")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

}
