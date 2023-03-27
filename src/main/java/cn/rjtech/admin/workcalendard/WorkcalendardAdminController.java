package cn.rjtech.admin.workcalendard;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Workcalendard;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 工作日历明细 Controller
 *
 * @ClassName: WorkcalendardAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:32
 */
@CheckPermission(PermissionKey.WORKCALENDARM)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/workcalendard", viewPath = "/_view/admin/workcalendard")
public class WorkcalendardAdminController extends JBoltBaseController {

    @Inject
    private WorkcalendardService service;

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
        renderJsonData(service.list(getKv()));
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
        Workcalendard workcalendard = service.findById(getLong(0));
        if (workcalendard == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("workcalendard", workcalendard);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Workcalendard.class, "workcalendard")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Workcalendard.class, "workcalendard")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }


}
