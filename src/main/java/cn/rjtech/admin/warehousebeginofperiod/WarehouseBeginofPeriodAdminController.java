package cn.rjtech.admin.warehousebeginofperiod;

import java.util.List;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.common.model.Barcodemaster;
import cn.rjtech.wms.utils.StringUtils;

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

    /*
     * 主页面
     * */
    public void index() {
        render("index.html");
    }

    /**
     * 新增期初库存页面
     */
    public void add() {
        render("add.html");
    }

    /*
     * 新增期初条码页面
     * */
    public void addBarcode() {
        render("addBarcode.html");
    }

    /*
     * 条码明细页面
     * */
    public void detail() {
        List<Barcodemaster> barcodemasters = service.findBySourceId(String.valueOf(getLong(0)));
        if (barcodemasters.isEmpty()){
//            fail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sourceid",getLong(0));
        render("detail.html");
    }

    /*
     * 自动条码明细页面加载数据
     * */
    public void detailDatas() {
        Kv kv = getKv();
        if (StringUtils.isBlank(kv.getStr("sourceid"))){
            renderJsonData(null);
            return;
        }
        renderJsonData(service.detailDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.datas(getPageNumber(), getPageSize(), getKv()));
    }

    /*public void save(JBoltTable jBoltTable) {
        List<Record> saveRecordList = jBoltTable.getSaveRecordList();
        renderJson(service.save());
    }*/

    /*
     * 保存新增期初库存
     * */
    public void submitStock(JBoltPara jBoltPara) {
        renderJsonData(service.submitStock(jBoltPara));
    }

    /*
     * 保存新增期初条码
     * */
    public void saveBarcode(JBoltPara jBoltPara) {
        renderJsonData(service.saveBarcode(jBoltPara));
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
    public void downloadTpl() throws Exception {
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

    /**
     * 打印条码明细
     */
    public void detailPrintData(){
        renderJsonData(service.printtpl(getKv()));
    }

    /*
     * 期初库存的仓库编码
     * */
    public void whoptions() {
        String cwhcode = get("cwhcode");
        renderJsonData(service.whoptions(Kv.by("whcode",cwhcode)));
    }

    /*
    * 根据仓库编码查询库区编码
    * */
    public void findAreaByWhcode(){
        String cwhcode = get("cwhcode");
        renderJsonData(service.findAreaByWhcode(cwhcode));
    }
}
