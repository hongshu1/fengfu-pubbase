package cn.rjtech.admin.backuplog;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.backupconfig.BackupConfigService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BackupConfig;
import cn.rjtech.model.momdata.BackupLog;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import java.io.IOException;

/**
 * 开发管理-备份记录
 *
 * @ClassName: BackupLogAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 17:06
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.BACKUP_LOG)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/backuplog", viewPath = "/_view/admin/backuplog")
public class BackupLogAdminController extends BaseAdminController {

    @Inject
    private BackupLogService service;
    @Inject
    private BackupConfigService backupConfigService;

    /**
     * 首页
     */
    public void index() {
        //查询备份设置
        BackupConfig backupConfig = backupConfigService.findFirstConfig();
        if (backupConfig == null) {
            backupConfig = new BackupConfig();
            backupConfig.setIsAutoBackupEnabled(true);
        }
        set("backupConfig", backupConfig);
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas(String cName) {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), cName));
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
    public void save() {
        renderJson(service.save(getModel(BackupLog.class, "backupLog")));
    }

    /**
     * 编辑
     */
    public void edit() {
        BackupLog backupLog = service.findById(getLong(0));
        if (backupLog == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("backupLog", backupLog);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(BackupLog.class, "backupLog")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }


    /**
     * 文件下载
     */
    public void copyFile() {
        BackupLog backupLog = service.findById(getLong(0));
        if (backupLog == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        try {
            renderJson(service.copyFile(backupLog));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
