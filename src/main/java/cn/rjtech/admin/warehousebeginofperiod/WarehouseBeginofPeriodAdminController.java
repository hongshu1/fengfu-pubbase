package cn.rjtech.admin.warehousebeginofperiod;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.common.model.Barcodemaster;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

/**
 * 仓库期初 Controller
 *
 * @ClassName: GenBarCodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 */
@CheckPermission(PermissionKey.WAREHOUSEBEGINOFPERIOD)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/warehousebeginofperiod", viewPath = "_view/admin/warehousebeginofperiod")
public class WarehouseBeginofPeriodAdminController extends BaseAdminController {

    @Inject
    private WarehouseBeginofPeriodService service;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private WarehouseService warehouseService;

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
        Barcodemaster barcodemaster = service.findByAutoid(String.valueOf(getLong(0)));
        if (barcodemaster == null) {
            fail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("barcodemaster", barcodemaster);
        render("detail.html");
    }

    /*
     * 自动条码明细页面加载数据
     * */
    public void detailDatas() {
        Kv kv = getKv();
        if (StrUtil.isBlank(kv.getStr("masid"))) {
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

    /*
     * 条码明细删除
     * */
    public void deleteByIds() {
        Kv kv = getKv();
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /*
     * 保存期初库存
     * */
    public void submitAllByStock(JBoltPara jBoltPara) {
        renderJsonData(service.submitByStock(jBoltPara));
    }

    /*
     * 保存期初库存
     * */
    public void submitAllBybarcode(JBoltPara jBoltPara) {
        renderJsonData(service.submitAllBybarcode(jBoltPara));
    }


    /*
     * 生成期初库存
     * */
    public void submitStock(JBoltPara jBoltPara) {
        renderJsonData(service.submitStock(jBoltPara));
    }

    /*
     * 生成期初条码
     * */
    public void submitAddBarcode(JBoltPara jBoltPara) {
        renderJsonData(service.submitAddBarcode(jBoltPara));
    }

    /**
     * 新增页加载的数据
     */
    @UnCheck
    public void findDetails() {
        renderJsonData(null);
    }

    /*
     * 期初库存导入模板
     * */
    @SuppressWarnings("unchecked")
    public void downloadStockTpl() throws Exception {
        renderJxls("warehousebeginstock_import.xlsx", Kv.by("rows", null), "仓库期初库存导入模板.xlsx");
    }

    /*
     * 期初条码导入模板
     * */
    @SuppressWarnings("unchecked")
    public void downloadBarcodeTpl() throws Exception {
        renderJxls("warehousebeginbarcode_import.xlsx", Kv.by("rows", null), "仓库期初条码导入模板.xlsx");
    }

    /*
     * 获取期初库存导入的数据
     * */
    public void getImportData(@Para(value = "list") String list) {
        ValidationUtils.notBlank(list, "导入数据不能为空");

        renderJsonData(service.getImportData(list));
    }

    /*
     * 期初条码数据导入
     * */
    public void importBarcodeExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importBarcodeExcel(file.getFile()));
    }

    /**
     * 打印条码明细
     */
    public void detailPrintData() {
        renderJsonData(service.detailPrintData(getKv()));
    }

    public void addPrintData() {
        renderJsonData(service.addPrintData(getKv()));
    }

    /**
     * 根据仓库编码查询库区编码
     */
    @UnCheck
    public void findAreaByWhcode() {
        renderJsonData(service.findAreaByWhcode());
    }

    public void inventoryDialogIndex(@Para(value = "index") String index, @Para(value = "type") String type) {
        ValidationUtils.notBlank(index, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(type, JBoltMsg.PARAM_ERROR);
        // 部品存货id
        String invItemId = get("invItemId");
        // 原材料存货id
        String originalItemId = get("originalItemId");
        // 分条料存货id
        String slicingInvItemId = get("slicingInvItemId");
        // 落料存货id
        String blankingItemId = get("blankingItemId");

        String invId = null;
        if (StrUtil.isNotBlank(invItemId)) {
            invId = invItemId;
        } else if (StrUtil.isNotBlank(originalItemId)) {
            invId = originalItemId;
        } else if (StrUtil.isNotBlank(slicingInvItemId)) {
            invId = slicingInvItemId;
        } else if (StrUtil.isNotBlank(blankingItemId)) {
            invId = blankingItemId;
        }
        if (StrUtil.isNotBlank(invId)) {
            Inventory inventory = inventoryService.findById(invId);
            set("cInvCode", inventory.getCInvCode());
        }
        keepPara();
        render("inventory_dialog_index.html");
    }

    public void warehouseDialogIndex(@Para(value = "index") String index, @Para(value = "type") String type) {
        ValidationUtils.notBlank(index, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(type, JBoltMsg.PARAM_ERROR);
        // 部品存货id
        String cwhcode = "";
        if (StrUtil.isNotBlank(get("cwhcode"))) {
            cwhcode = get("cwhcode");
            Warehouse warehouse = warehouseService.findByWhCode(cwhcode);
            set("cwhcode", warehouse.getCWhCode());
        }
        keepPara();
        render("warehouse_dialog_index.html");
    }

    public void warehouseAreaDialogIndex(@Para(value = "index") String index, @Para(value = "type") String type) {
        ValidationUtils.notBlank(index, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(type, JBoltMsg.PARAM_ERROR);
        // 部品存货id
        if (StrUtil.isNotBlank(get("careacode"))) {
            set("careacode", get("careacode"));
        }
        keepPara();
        render("warehousearea_dialog_index.html");
    }

    public void cvencodeDialogIndex(@Para(value = "index") String index, @Para(value = "type") String type) {
        ValidationUtils.notBlank(index, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(type, JBoltMsg.PARAM_ERROR);
        // 部品存货id
        if (StrUtil.isNotBlank(get("cvencode"))) {
            set("cvencode", get("cvencode"));
        }
        keepPara();
        render("cvencode_dialog_index.html");
    }

    public void wareHouseOptions() {
        renderJsonData(service.wareHouseOptions(getKv()));
    }
}
