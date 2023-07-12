package cn.rjtech.admin.qcitem;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.rjtech.admin.qcparam.QcParamService;
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
 * 质量建模-检验项目（分类）
 *
 * @ClassName: QcItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:31
 */
@CheckPermission(PermissionKey.QCITEM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/qcitem", viewPath = "/_view/admin/qcitem")
public class QcItemAdminController extends BaseAdminController {

    @Inject
    private QcItemService  service;
    @Inject
    private QcParamService qcParamService;

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
//        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isDeleted")));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.QCITEM_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.QCITEM_ADD)
    public void save() {
        renderJson(service.save(getModel(QcItem.class, "qcItem")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.QCITEM_EDIT)
    public void edit() {
        QcItem qcItem = service.findById(getLong(0));
        if (qcItem == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("qcItem", qcItem);
        render("edit.html");
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.QCITEM_EDIT)
    public void update() {
        renderJson(service.update(getModel(QcItem.class, "qcItem")));
    }

    /**
     * 删除
     */
    public void delete() {
        String checkResult = checkQcItemIsUse(getLong(0));
        if(StrUtil.isNotBlank(checkResult)){
            renderFail(checkResult);
            return;
        }
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 批量删除
     */
    @Before(Tx.class)
    @CheckPermission(PermissionKey.QCITEM_DELETE)
    public void deleteByIds() {
        String[] ids = get("ids").split(",");
        for (String id : ids) {
            String checkResult = checkQcItemIsUse(Long.valueOf(id));
            if(StrUtil.isNotBlank(checkResult)){
                renderFail(checkResult);
                return;
            }
        }
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
    * 检查项目是否被引用了
    */
    public String checkQcItemIsUse(Long id){
        String msg = "";
        QcItem qcItem = service.findById(id);
        List<QcParam> qcParams = qcParamService.findByIqcItemId(qcItem.getIAutoId());
        if (!qcParams.isEmpty()){
            msg = qcItem.getCQcItemName()+ " 已经在【检验参数】页面使用了，不能删除";
        }
        return msg;
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /**
     * 导出选中
     */
    @CheckPermission(PermissionKey.QCITEM_EXPORT)
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
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(service.getListByIds(ids));
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /**
     * 导出全部
     */
    @CheckPermission(PermissionKey.QCITEM_EXPORT)
    public void exportExcelAll() {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(service.findAll());
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    @UnCheck
    public void options() {
        renderJsonData(service.options());
    }

    /**
     * 数据导入
     */
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcel(file.getFile()));
    }

    @CheckPermission(PermissionKey.QCITEM_IMPORT)
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
