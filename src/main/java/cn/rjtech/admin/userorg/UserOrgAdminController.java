package cn.rjtech.admin.userorg;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.model.main.UserOrg;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 用户组织关系
 *
 * @ClassName: UserOrgAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-14 16:02
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/userorg", viewPath = "/_view/admin/userorg")
public class UserOrgAdminController extends JBoltBaseController {

    @Inject
    private UserOrgService service;

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
        renderJsonData(service.getAdminDatas(get("position"), getBoolean("isPrincipal"), getBoolean("isDeleted", false)));
    }

    public void list(@Para(value = "userId") Long userId) {
        ValidationUtils.validateId(userId, "用户ID");

        renderJsonData(service.getList(userId));
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
        UserOrg userOrg = service.findById(getLong(0));
        if (userOrg == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("userOrg", userOrg);
        render("edit.html");
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
        renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("用户组织关系导入模板"));
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
     * 执行导出excel 根据查询form表单
     */
    public void exportExcelByForm() {
        List<UserOrg> list = service.getAdminDatas(get("position"), getBoolean("isPrincipal"), getBoolean("isDeleted", false));
        if (notOk(list)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(list).setFileName("用户组织关系"));
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
        List<UserOrg> datas = service.getListByIds(ids);
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("用户组织关系"));
    }

    /**
     * 切换is_principal
     */
    @Before(Tx.class)
    public void toggleIsPrincipal() {
        renderJson(service.toggleBoolean(getLong(0), "is_principal"));
    }

}
