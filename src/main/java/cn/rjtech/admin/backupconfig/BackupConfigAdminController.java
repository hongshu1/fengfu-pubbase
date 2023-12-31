package cn.rjtech.admin.backupconfig;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BackupConfig;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 开发管理-备份设置
 *
 * @ClassName: BackupConfigAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 15:15
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.BACKUP_LOG)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/backupconfig", viewPath = "/_view/admin/backupconfig")
public class BackupConfigAdminController extends BaseAdminController {

    @Inject
    private BackupConfigService service;

    /**
     * 首页ss
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isAutoBackupEnabled")));
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
        renderJson(service.save(getModel(BackupConfig.class, "backupConfig")));
    }

    /**
     * 编辑
     */
    public void edit() {
        BackupConfig backupConfig = service.findById(getLong(0));
        if (backupConfig == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("backupConfig", backupConfig);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(BackupConfig.class, "backupConfig")));
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
     * 切换isAutoBackupEnabled
     */
    public void toggleIsAutoBackupEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isAutoBackupEnabled"));
    }


}
