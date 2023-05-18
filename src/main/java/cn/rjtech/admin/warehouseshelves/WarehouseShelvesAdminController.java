package cn.rjtech.admin.warehouseshelves;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.WarehouseShelves;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

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
        render("index().html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
    }

    public void list(){
        renderJsonData(service.list(getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add().html");
    }

    /**
     * 编辑
     */
    public void edit() {
        WarehouseShelves warehouseShelves=service.findById(getLong(0));
        if(warehouseShelves == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("warehouseShelves",warehouseShelves);
        render("edit().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(WarehouseShelves.class, "warehouseShelves")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(WarehouseShelves.class, "warehouseShelves")));
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

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
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
     * 货架档案Excel导入数据库
     */
    public void importExcel(){
        String uploadPath= JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file=getFile("file",uploadPath);
        if(notExcel(file)){
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelData(file.getFile()));
    }

    /**
     * 打印
     */
    @Clear
    public void selectPrint(){
        renderJson(service.selectPrint(null));
    }

}
