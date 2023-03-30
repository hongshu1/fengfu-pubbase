package cn.rjtech.admin.workclass;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.rjtech.model.momdata.Workclass;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 工种档案 Controller
 * @ClassName: WorkclassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-09 16:21
 */
@CheckPermission(PermissionKey.WORKCLASS)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/workclass", viewPath = "/_view/admin/workclass")
public class WorkclassAdminController extends JBoltBaseController {

    @Inject
    private WorkClassService service;
    // @Inject
    // private PersonService personService;

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
        Page<Record> recordPage = service.pageList(getKv());
        for (Record record : recordPage.getList()) {
            switch (record.get("ilevel").toString()){
                case "1":
                    record.set("ilevel","一级");
                    break;
                case "2":
                    record.set("ilevel","二级");
                    break;
                case "3":
                    record.set("ilevel","三级");
                    break;
                case "4":
                    record.set("ilevel","四级");
                    break;
                case "5":
                    record.set("ilevel","五级");
                    break;
            }
        }
        renderJsonData(recordPage);
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
        Workclass workclass = service.findById(getLong(0));
        if (workclass == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("workclass", workclass);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Workclass.class, "workclass")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Workclass.class, "workclass")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 切换toggleIsdeleted
     */
    public void toggleIsdeleted() {
        renderJson(service.toggleIsdeleted(getLong(0)));
    }

    /**
     * 切换toggleIsenabled
     */
    public void toggleIsenabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }

    /*
     * 导出选中的数据
     * */
    public void exportExcelByIds() {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> recordList = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if (notOk(recordList)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = service.exportExcelTpl(service.getListByIds(ids));
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /*
     * 导出所有数据
     * */
    public void exportExcelAll() {
        List<Workclass> datas = service.findAll();
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = service.exportExcelTpl(datas);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderBytesToExcelXlsFile(service.getExcelImportTpl().setFileName("工种档案导入模板"));
    }

    /*
     * 数据导入
     * */
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelData(file.getFile()));
    }

    public void options() {
        renderJsonData(service.options());
    }

}
