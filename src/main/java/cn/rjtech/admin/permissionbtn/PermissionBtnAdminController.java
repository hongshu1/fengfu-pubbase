package cn.rjtech.admin.permissionbtn;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.model.PermissionBtn;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 菜单按钮
 *
 * @ClassName: PermissionBtnAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-10 13:47
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.PERMISSION_BTN)
@Path(value = "/admin/permissionbtn", viewPath = "/_view/admin/permissionbtn")
public class PermissionBtnAdminController extends JBoltBaseController {

    @Inject
    private PermissionBtnService service;

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
        renderJsonData(service.getAdminDatas(getLong("permissionId")));
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
        renderJson(service.save(getModel(PermissionBtn.class, "permissionBtn")));
    }

    /**
     * 编辑
     */
    public void edit() {
        PermissionBtn permissionBtn = service.findById(getLong(0));
        if (permissionBtn == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("permissionBtn", permissionBtn);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(PermissionBtn.class, "permissionBtn")));
    }

    /**
     * 删除
     */
    @Before(Tx.class)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 排序 上移
     */
    @Before(Tx.class)
    public void up() {
        renderJson(service.up(getLong(0)));
    }

    /**
     * 排序 下移
     */
    @Before(Tx.class)
    public void down() {
        renderJson(service.down(getLong(0)));
    }

    /**
     * 排序 初始化
     */
    @Before(Tx.class)
    public void initSortRank() {
        renderJson(service.initSortRank());
    }

    /**
     * 进入import_excel.html
     */
    public void initImportExcel() {
        render("import_excel.html");
    }

    /**
     * 下载导入模板
     */
    public void downloadTpl() {
        renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("菜单按钮导入模板"));
    }

    /**
     * 执行导入excel
     */
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.IMPORT_EXCEL_TEMP_FOLDER);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcel(file.getFile()));
    }

    /**
     * 执行导出excel 根据表格选中数据
     */
    public void exportExcelByCheckedIds() {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<PermissionBtn> datas = service.getListByIds(ids);
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("菜单按钮"));
    }

    /**
     * 执行导出excel 所有数据
     */
    public void exportExcelAll() {
        List<PermissionBtn> datas = service.findAll();
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("菜单按钮"));
    }


}
