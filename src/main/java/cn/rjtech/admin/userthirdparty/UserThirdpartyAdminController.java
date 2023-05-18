package cn.rjtech.admin.userthirdparty;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.model.main.UserThirdparty;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 第三方系统账号信息表
 *
 * @ClassName: UserThirdpartyAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-14 21:06
 */
@UnCheck
@Path(value = "/admin/userthirdparty", viewPath = "/_view/admin/userthirdparty")
public class UserThirdpartyAdminController extends JBoltBaseController {

    @Inject
    private UserThirdpartyService service;

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
        renderJsonData(service.getAdminDatas(getLong("userId")));
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
        renderJson(service.save(getModel(UserThirdparty.class, "userThirdparty")));
    }

    /**
     * 编辑
     */
    public void edit() {
        UserThirdparty userThirdparty = service.findById(getLong(0));
        if (userThirdparty == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("userThirdparty", userThirdparty);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(UserThirdparty.class, "userThirdparty")));
    }

    /**
     * 批量删除
     */
    @Before(Tx.class)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
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
        renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("第三方系统账号信息表导入模板"));
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
        List<UserThirdparty> datas = service.getListByIds(ids);
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("第三方系统账号信息表"));
    }


}
