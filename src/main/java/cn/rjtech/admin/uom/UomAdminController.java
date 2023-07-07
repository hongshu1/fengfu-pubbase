package cn.rjtech.admin.uom;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Uom;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 计量单位档案 Controller
 *
 * @ClassName: UomAdminController
 * @author: chentao
 * @date: 2022-11-02 17:29
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.UOM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/uom", viewPath = "/_view/admin/uom")
public class UomAdminController extends JBoltBaseController {

    @Inject
    private UomService service;

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
        String uomclassid = get("uomclassid");
        if (notOk(uomclassid)) {
            Page<Record> recordPage = new Page<>();
            recordPage.setPageNumber(1);
            recordPage.setPageSize(getInt("pageSize"));
            recordPage.setTotalRow(0);
            recordPage.setTotalPage(1);
            renderJsonData(recordPage);
            return;
        }
        renderJsonData(service.pageList(getKv()));
    }

    public void dialog_datas() {
        Long iuomclassid = getLong("iuomclassid");
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords(), iuomclassid));
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
        Uom uom = service.findById(getLong(0));
        if (uom == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("uom", uom);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.UOMCLASS_ADD)
    public void save() {
        renderJson(service.save(getModel(Uom.class, "uom")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.UOMCLASS_EDIT)
    public void update() {
        renderJson(service.update(getModel(Uom.class, "uom")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.UOMCLASS_DELETE)
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
     * 切换toggleIsbase
     */
    public void toggleIsbase() {
        renderJson(service.toggleIsbase(getLong(0)));
    }

    /**
     * dialg_index.html
     */
    public void dialg_index() {
        Long iuomclassid = getLong("iuomclassid");
        String idField = get("idField");
        String nameField = get("nameField");
        set("iuomclassid", iuomclassid);
        set("idField", idField);
        set("nameField", nameField);
        render("dialg_index.html");
    }

    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.UOMCLASS_EXPORT)
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
        for (Record record : data) {
            record.put("isbase", StrUtil.equals("1", record.getStr("isbase")) ? "是" : "否");
        }
        renderJxls("uom.xlsx", Kv.by("rows", data), "计量单位(选中导出)_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.UOMCLASS_EXPORT)
    public void exportExcelAll() throws Exception {
        String uomclassid = get("uomclassid");
        if (notOk(uomclassid)) {
            renderJsonFail("请先选中需要导出的计量组");
            return;
        }
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        for (Record record : rows) {
            record.put("isbase", StrUtil.equals("1", record.getStr("isbase")) ? "是" : "否");
        }
        renderJxls("uom.xlsx", Kv.by("rows", rows), "计量单位_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("uom_import.xlsx", Kv.by("rows", null), "计量单位导入模板.xlsx");
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

    @UnCheck
    public void options() {
        String pid = get("pid");
        if (notOk(pid)) {
            renderJsonData(new ArrayList<>());
            return;
        }
        renderJsonData(service.getOptions(Kv.of("iUomClassId", pid)));
    }

    @CheckPermission(PermissionKey.UOMCLASS_IMPORT)
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
