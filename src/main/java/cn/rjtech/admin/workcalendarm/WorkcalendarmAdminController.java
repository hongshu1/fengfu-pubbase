package cn.rjtech.admin.workcalendarm;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Workcalendarm;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.Date;
import java.util.List;

/**
 * 工作日历 Controller
 *
 * @ClassName: WorkcalendarmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:31
 */
@CheckPermission(PermissionKey.WORKCALENDARM)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/workcalendarm", viewPath = "/_view/admin/workcalendarm")
public class WorkcalendarmAdminController extends JBoltBaseController {

    @Inject
    private WorkcalendarmService service;

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
        Workcalendarm workcalendarm = service.findById(getLong(0));
        if (workcalendarm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("workcalendarm", workcalendarm);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Workcalendarm.class, "workcalendarm")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Workcalendarm.class, "workcalendarm")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 切换toggleIsenabled
     */
    public void toggleIsenabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }

    /**
     * 切换toggleIsdeleted
     */
    public void toggleIsdeleted() {
        renderJson(service.toggleIsdeleted(getLong(0)));
    }

    public void updateEditTable() {
        renderJson(service.updateEditTable(getJBoltTable(), JBoltUserKit.getUserId(), new Date()));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    public void dataExport() throws Exception {
        List<Record> rows = service.getDataExport(getKv());

        renderJxls("workcalendarm.xlsx", Kv.by("rows", rows), "生产日历_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    public void downloadTemplate() throws Exception {
        renderJxls("workcalendarm_import.xlsx", Kv.by("rows", null), "不良项目导入模板.xlsx");
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("workcalendarm_import.xlsx", Kv.by("rows", null), "生产日历导入模板.xlsx");
    }

    /**
     * 生产班次Excel导入数据库
     */
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelData(file.getFile()));
    }

}
