package cn.rjtech.admin.warehousebeginofperiod;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;

/**
 * 仓库期初 Controller
 *
 * @ClassName: GenBarCodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/warehousebeginofperiod", viewPath = "_view/admin/warehousebeginofperiod")
public class WarehouseBeginofPeriodAdminController extends BaseAdminController {

    @Inject
    WarehouseBeginofPeriodService service;

    public void index() {
        render("index.html");
    }

    /**
     * 新增期初库存
     */
    public void add() {
        render("add.html");
    }

    /*
     * 新增期初条码
     * */
    public void addBarcode() {
        render("addBarcode.html");
    }

    /*
     * 条码明细
     * */
    public void detail() {
        render("detail.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.datas(getPageNumber(), getPageSize(), getKv()));
    }

    /*
     * 保存
     * */
    public void save() {
        renderJson(service.save());
    }

    /*
     * 新增页加载的数据
     * */
    public void findDetails() {
        renderJsonData(null);
    }

    /*
     * 模板下载
     * */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception{
        renderJxls("warehousearea_import.xlsx", Kv.by("rows", null), "仓库期初导入模板.xlsx");
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
        renderJson("导入成功");
    }

    /*
     * 提交
     * */
    public void submitAll() {
        renderJsonData("提交成功");
    }

    /*
     * 打印
     * */
    public void printtpl() {
        renderJsonData(service.printtpl(getLong(0)));
    }
}
