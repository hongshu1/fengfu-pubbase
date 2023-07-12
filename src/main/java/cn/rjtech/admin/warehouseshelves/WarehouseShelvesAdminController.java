package cn.rjtech.admin.warehouseshelves;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.model.momdata.WarehouseShelves;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

/**
 * 货架档案 Controller
 *
 * @ClassName: WarehouseShelvesAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:44
 */
@CheckPermission(PermissionKey.WAREHOUSE_SHELVES)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/warehouseshelves", viewPath = "/_view/admin/warehouseshelves")
public class WarehouseShelvesAdminController extends JBoltBaseController {

    @Inject
    private WarehouseShelvesService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
    }

    @UnCheck
    public void list(){
        renderJsonData(service.list(getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_ADD)
    public void add() {
        set("warehouseShelves", service.getWarehouseShelvesCode());
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_EDIT)
    public void edit() {
        WarehouseShelves warehouseShelves=service.findById(getLong(0));
        if(warehouseShelves == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("warehouseShelves",warehouseShelves);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_ADD)
    public void save() {
        renderJson(service.save(getModel(WarehouseShelves.class, "warehouseShelves")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_EDIT)
    public void update() {
        renderJson(service.update(getModel(WarehouseShelves.class, "warehouseShelves")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_DELETE)
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

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_EXPORT)
    public void dataExport() throws Exception {
        List<Record> rows = service.list(getKv());
        renderJxls("warehouseshelves.xlsx", Kv.by("rows", rows), "货架列表_" + DateUtil.today() + ".xlsx");
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("warehouseshelves_import.xlsx", Kv.by("rows", null), "货架档案导入模板.xlsx");
    }

    /**
     * 数据导入
     */
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_IMPORT)
    public void importExcelData() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");

        File file = uploadFile.getFile();

        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
        ValidationUtils.equals(list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        
        renderJson(service.importExcelData(file));
    }

    /**
     * 打印
     */
    @UnCheck
    public void selectPrint(){
        renderJson(service.selectPrint(null));
    }

    /**
     * 货架打印数据
     */
    @CheckPermission(PermissionKey.WAREHOUSE_SHELVES_PRINT)
    public void printData() {
        renderJsonData(service.getPrintDataCheck(getKv()));
    }
    
}
