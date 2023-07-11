package cn.rjtech.admin.bommaster;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.JBoltUserAuthKit;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.bomcompare.BomCompareService;
import cn.rjtech.admin.bomm.BomMService;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BomM;
import cn.rjtech.model.momdata.BomMaster;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.IOException;

/**
 * 物料建模-Bom母项
 *
 * @ClassName: BomMasterAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 16:39
 */
@CheckPermission(PermissionKey.NOME)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bommaster", viewPath = "/_view/admin/bommaster")
public class BomMasterAdminController extends BaseAdminController {

    @Inject
    private BomMasterService service;
    @Inject
    private InventoryChangeService inventoryChangeService;
    @Inject
    private EquipmentModelService equipmentModelService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private CustomerService customerService;
    @Inject
    private BomCompareService bomCompareService;
    @Inject
    private BomMService bomMService;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.BOMMASTER_EXPORT)
    public void add() {
        render("add.html");
    }


    /**
     * 编辑
     */
    public void edit() {
    
//        BomM bomM = bomMService.findById(getLong(0));
//        ValidationUtils.notNull(bomM, JBoltMsg.DATA_NOT_EXIST);
//        BomSourceTypeEnum bomSourceTypeEnum = BomSourceTypeEnum.toEnum(bomM.getIType());
//        ValidationUtils.notNull(bomSourceTypeEnum, "未知新增类型");
//        BomSourceTypeEnum manualTypeAdd = BomSourceTypeEnum.MANUAL_TYPE_ADD;
//        if (manualTypeAdd.getValue() == bomSourceTypeEnum.getValue()){
//
//            return;
//        }
    
    
        getBomMaster(getLong(0));
        render("edit.html");
    }

    @UnCheck
    private void getBomMaster(Long id) {
        BomMaster bomMaster = service.findById(id);
        if (bomMaster == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("index", bomCompareService.queryCompareIndex(id));
        set("equipmentModel", equipmentModelService.findById(bomMaster.getIEquipmentModelId()));
        set("inventory", inventoryService.findById(bomMaster.getIInventoryId()));
        set("bomMaster", bomMaster);
    }

    /**
     * 查看
     */
    public void info() {
        getBomMaster(getLong(0));
        set("view", 1);
        render("edit.html");
    }

    @UnCheck
    public void findByBomMasterId() {
        renderJsonData(bomCompareService.findByBomMasterId(getLong(0)));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /**
     * 默认给1-100个数据
     */
    public void inventoryAutocomplete() {
        Integer pageSize = getInt("pageSize", 100);
        Page<Record> recordPage = inventoryChangeService.inventoryAutocomplete(getPageNumber(), pageSize, getKv());
        renderJsonData(recordPage.getList());
    }

    /**
     * 默认给1-100,带上最新的工艺路线
     */
    public void inventoryAutocompleteNew() {
        Integer pageSize = getInt("pageSize", 100);
        Page<Record> recordPage = inventoryChangeService.inventoryAutocompleteNew(getPageNumber(), pageSize, getKv());
        renderJsonData(recordPage.getList());
    }

    public void submitForm(@Para(value = "formJsonData") String formJsonData, @Para(value = "tableJsonData") String tableJsonData, @Para(value = "commonInvData") String commonInvData, @Para(value = "flag") Boolean flag) {

        renderJsonData(service.submitForm(formJsonData, tableJsonData, commonInvData, flag));
    }

    @UnCheck
    public void findEquipmentModelAll() {
        renderJsonData(equipmentModelService.getAdminDataNoPage(getKv()));
    }

    @UnCheck
    public void getDatas() {
        renderJsonData(service.getDatas(getKv()));
    }

    @UnCheck
    public void getPageData() {
        renderJsonData(service.getPageData(getPageNumber(), getPageSize(), getKv()));
    }

    public void testDel() {
        ok();
    }

    @CheckPermission(PermissionKey.BOMMASTER_VERSION_COPY)
    public void copyForm() {
        ValidationUtils.notNull(get(0), "未获取到指定产品id");
        set("oldId", get(0));
        BomM bomM = bomMService.findById(get(0));
        ValidationUtils.notNull(bomM, JBoltMsg.DATA_NOT_EXIST);
        DateTime dateTime = DateUtil.offsetDay(bomM.getDDisableDate(), 1);
        set(BomM.DENABLEDATE, DateUtil.formatDate(dateTime));
        set(BomM.CVERSION, bomMService.getNextVersion(getOrgId(), bomM.getIInventoryId()));
        render("_copy_form.html");
    }

    // 拷贝
    public void saveCopy(@Para(value = "cversion") String cVersion, @Para(value = "oldId") Long oldId) {
        renderJson(service.saveCopy(oldId, cVersion));
    }

    @CheckPermission(PermissionKey.BOMMASTER_EXPORT)
    public void importExcelFile() throws IOException {
        //上传到今天的文件夹下
        String uploadFile = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_FILE_UPLOADER);
        UploadFile file = getFile("file", uploadFile);

        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJsonData(service.importExcelFile(file));
    }

    public void bomMasterIndex() {
        keepPara();
        render("bommaster_index.html");
    }

    public void versionIndex() {
        keepPara();
        boolean hasPermission = JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), PermissionKey.BOMMASTER_VERSION_VIEW);
        set("hasPermission", !hasPermission);
        render("version_index.html");
    }
    
    public void queryFileIndex(){
        render("query_fiel_index.html");
    }

    @UnCheck
    public void getVersionRecord() {
        renderJsonData(bomMService.getVersionRecord(getPageNumber(), getPageSize(), getKv()));
    }

    public void del() {
        renderJson(service.del(getLong(0)));
    }

    public void audit(@Para(value = "bomMasterId") Long bomMasterId, @Para(value = "status") Integer status) {
        renderJson(service.audit(bomMasterId, status));
    }

    public void checkCommonInv(@Para(value = "bomMasterId") Long bomMasterId, @Para(value = "tableJsonData") String tableJsonData) {
        renderJsonData(service.checkCommonInv(bomMasterId, tableJsonData));
    }

    @UnCheck
    public void findCustomerList() {
        renderJsonData(customerService.getAdminDatas(getKv()));
    }

    @UnCheck
    public void findVendorList() {
        renderJsonData(customerService.findVendorList(getKv()));
    }

    public void inventoryDialogIndex() {
        
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
    
    public void inventoryPage() {
        renderJsonData(inventoryChangeService.inventoryAutocomplete(getPageNumber(), getPageSize(), getKv()));
    }

    public void test(@Para(value = "id") Long id) {
        renderJson(service.test(id));
    }
    
    /**
     * 下载导入模板
     */
    public void downloadTpl() throws Exception {
        renderJxls("bomMasterImportTpl.xlsx", getKv(), "物料清单_导入模板_" + DateUtil.today() + ".xlsx");
    }
}
