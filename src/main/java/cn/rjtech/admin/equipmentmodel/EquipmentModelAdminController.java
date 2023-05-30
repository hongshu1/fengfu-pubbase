package cn.rjtech.admin.equipmentmodel;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.EquipmentModel;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

/**
 * 物料建模-机型档案
 *
 * @ClassName: EquipmentModelAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 11:17
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.EQUIPMENT_MODEL)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/equipmentmodel", viewPath = "/_view/admin/equipmentmodel")
public class EquipmentModelAdminController extends BaseAdminController {

    @Inject
    private EquipmentModelService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
        renderJson(service.save(getModel(EquipmentModel.class, "equipmentModel")));
    }

    /**
     * 编辑
     */
    public void edit() {
        EquipmentModel equipmentModel = service.findById(getLong(0));
        if (equipmentModel == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("equipmentModel", equipmentModel);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(EquipmentModel.class, "equipmentModel")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 进入import_excel.html
     */
    public void initImportExcel() {
        render("import_excel.html");
    }

//    /**
//     * 下载导入模板
//     */
//    public void downloadTpl() {
//        renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("物料建模-机型档案导入模板"));
//    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("equipmentmodel_import.xlsx", Kv.by("rows", null), "机型档案导入模板.xlsx");
    }

    /**
     * 执行导入excel
     */
    public void importExcel() {

        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");
        File file = uploadFile.getFile();
        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
        // 截取最后一个“.”之前的文件名，作为导入格式名
        String cformatName = list.get(0);
        String extension = list.get(1);
        ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        renderJson(service.importExcel(file, cformatName));
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
        renderBytesToExcelXlsxFile(service.exportExcel(pageData.getList()).setFileName("物料建模-机型档案"));
    }

    /**
     * 执行导出excel 根据表格选中数据
     */
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
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("物料建模-机型档案"));
    }

    /**
     * 执行导出excel 所有数据
     */
    public void exportExcelAll() {
        List<Record> datas = service.getAdminDataNoPage(getKv());
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("物料建模-机型档案"));
    }

    public void options() {
        List<Record> options = service.options();
        renderJsonData(options);
    }

}
