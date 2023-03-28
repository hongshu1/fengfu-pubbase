package cn.rjtech.admin.operation;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
//import cn.rjtech.admin.operationbadness.OperationbadnessService;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.rjtech.admin.qcparam.QcParamService;
import cn.rjtech.admin.workclass.WorkClassService;
import cn.rjtech.model.momdata.Operation;
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

import org.apache.commons.lang3.StringUtils;

/**
 * 料品工序档案配置 Controller
 *
 * @ClassName: OperationAdminController
 * @author: chentao
 * @date: 2022-11-09 19:17
 */
@CheckPermission(PermissionKey.OPERATION)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/operation", viewPath = "/_view/admin/operation")
public class OperationAdminController extends JBoltBaseController {

    @Inject
    private OperationService service;
    @Inject
    private WorkClassService workClassService;
    @Inject
    private QcParamService   qcParamService;
//    @Inject
//    private OperationbadnessService operationbadnessService;

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
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Operation operation = service.findById(getLong(0));
        if (operation == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("operation", operation);
//        String ids = service.findBadnessClassIds(operation.getIautoid());
//        set("ibadnessids", ids);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        Operation model = getModel(Operation.class, "operation");
        renderJson(service.save(model));
    }

    /**
     * 更新
     */
    public void update() {
        /*String ibadnessids = get("ibadnessids");
        if (notOk(ibadnessids)) {
            renderFail("请选择不良项目");
            return;
        }*/
        Operation model = getModel(Operation.class, "operation");
        //model.setCupdatename(ibadnessids);
        renderJson(service.update(model));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
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

    public void toggleIsenabledByQcParam(){
        renderJson(qcParamService.toggleIsenabled(getLong(0)));
    }

    /*public void badnessOptions() {
        renderJsonData(service.badnessOptions());
    }*/

    public void pageList() {
        final Page<Record> recordPage = service.pageList(getKv());
        renderJsonData(service.pageList(getKv()));
    }

    /*
     * 导出选中
     * */
    @SuppressWarnings("unchecked")
    public void exportExcelByIds() throws Exception {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> data = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if (notOk(data)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        List<Operation> operations = service.getListByIds(ids);
        for (Operation operation : operations) {
            Workclass workclass = workClassService.findById(operation.getIworkclassid());
            operation.put("iworkclassname",
                StringUtils.isNotBlank(workclass.getCworkclassname()) ? workclass.getCworkclassname() : "");
        }
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(operations);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
//        renderJxls("operation.xlsx", Kv.by("rows", data), "工序(选中导出)_" + DateUtil.today() + ".xlsx");
    }

    /*
     * 导出全部
     * */
    @SuppressWarnings("unchecked")
    public void exportExcelAll() throws Exception {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        List<Operation> operations = service.findAll();
        for (Operation operation : operations) {
            Workclass workclass = workClassService.findById(operation.getIworkclassid());
            operation.put("iworkclassname",
                StringUtils.isNotBlank(workclass.getCworkclassname()) ? workclass.getCworkclassname() : "");
        }
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(operations);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
//        renderJxls("operation.xlsx", Kv.by("rows", rows), "工序_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
//        renderJxls("operation_import.xlsx", Kv.by("rows", null), "工序导入模板.xlsx");
        renderBytesToExcelXlsFile(service.getExcelImportTpl().setFileName("工序导入模板"));
    }

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
