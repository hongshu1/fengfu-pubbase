package cn.rjtech.admin.qcparam;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.rjtech.admin.qcitem.QcItemService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.model.momdata.QcParam;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 质量建模-检验参数
 *
 * @ClassName: QcParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:15
 */
@CheckPermission(PermissionKey.QCPARAM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/qcparam", viewPath = "/_view/admin/qcparam")
public class QcParamAdminController extends BaseAdminController {

    @Inject
    private QcParamService service;
    @Inject
    private QcItemService qcItemService;

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
        renderJsonData(service.pageList(getKv()));
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.QCPARAM_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.QCPARAM_ADD)
    public void save() {
        QcParam model = getModel(QcParam.class, "qcParam");
        renderJson(service.save(model));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.QCPARAM_EDIT)
    public void edit() {
        QcParam qcParam = service.findById(getLong(0));
        if (qcParam == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("qcParam", qcParam);
        render("edit.html");
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.QCPARAM_EDIT)
    public void update() {
        renderJson(service.update(getModel(QcParam.class, "qcParam")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.QCPARAM_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 批量删除
     */
    @Before(Tx.class)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /*
     * 导出选中
     */
    @CheckPermission(PermissionKey.QCPARAM_EXPORT)
    public void exportExcelByIds() {
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
        List<QcParam> params = service.getListByIds(ids);
        for (QcParam param : params) {
            QcItem qcItem = qcItemService.findById(param.getIqcitemid());
            param.put("cqcitemname", qcItem.getCQcItemName());
        }
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(params);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /*
     * 导出全部
     */
    @CheckPermission(PermissionKey.QCPARAM_EXPORT)
    public void exportExcelAll() {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        List<QcParam> params = service.findAll();
        for (QcParam param : params) {
            QcItem qcItem = qcItemService.findById(param.getIqcitemid());
            param.put("cqcitemname", qcItem.getCQcItemName());
        }
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(params);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /*
     * 数据导入
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

    @CheckPermission(PermissionKey.QCPARAM_IMPORT)
    public void importExcelClass() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelClass(file.getFile()));
    }
}
