package cn.rjtech.admin.equipment;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Equipment;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 设备管理-设备档案
 *
 * @ClassName: EquipmentAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 11:32
 */
@CheckPermission(PermissionKey.EQUIPMENT)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/equipment", viewPath = "/_view/admin/equipment")
public class EquipmentAdminController extends BaseAdminController {

    @Inject
    private EquipmentService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.EQUIPMENT_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.EQUIPMENT_ADD)
    public void save() {
        renderJson(service.save(getModel(Equipment.class, "equipment")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.EQUIPMENT_EDIT)
    public void edit() {
        Equipment equipment = service.findById(getLong(0));
        if (equipment == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("equipment", equipment);
        render("edit.html");
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.EQUIPMENT_EDIT)
    public void update() {
        renderJson(service.update(getModel(Equipment.class, "equipment")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.EQUIPMENT_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
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
        renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("设备管理-设备档案导入模板"));
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
        Page<Record> pageData = service.getAdminDatas(getPageNumber(), getPageSize(), getKv());
        if (notOk(pageData.getTotalRow())) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(pageData.getList()).setFileName("设备管理-设备档案"));
    }

    /**
     * 执行导出excel 根据表格选中数据
     */
    @CheckPermission(PermissionKey.EQUIPMENT_EXPORT)
    public void exportExcelByCheckedIds() {
        String ids = get("ids");
        Kv kv = getKv();
        if (ids != null) {
            String[] split = ids.split(",");
            String sqlids = "";
            for (String id : split) {
                sqlids += "'" + id + "',";
            }
            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
            sqlids = sqlids.substring(0, sqlids.length() - 1);
            kv.set("sqlids", sqlids);
        }
        List<Record> datas = service.getAdminDataNoPage(kv);
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("设备管理-设备档案"));
    }

    /**
     * 执行导出excel 所有数据
     */
    @CheckPermission(PermissionKey.EQUIPMENT_EXPORT)
    public void exportExcelAll() {
        List<Record> datas = service.getAdminDataNoPage(getKv());
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("设备管理-设备档案"));
    }

    /**
     * 切换isNozzleSwitchEnabled
     */
    public void toggleIsNozzleSwitchEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isNozzleSwitchEnabled"));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

    @UnCheck
    public void selectLine() {
        renderJsonData(service.selectWorkRegs());
    }

    /**
     * 获取设备列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    @UnCheck
    public void autocomplete() {
        renderJsonData(service.getAutocompleteDatas(get("q"), getInt("limit", 10)));
    }

    public void dataList() {
        renderJsonData(service.dataList());
    }

    @CheckPermission(PermissionKey.EQUIPMENT_IMPORT)
    public void importExcelClass() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelClass(file.getFile()));
    }
    /**
     * 真删除
     */
    @Before(Tx.class)
    public void realDelete() {
        renderJson(service.realDeleteById(getLong(0),true));
    }

}
