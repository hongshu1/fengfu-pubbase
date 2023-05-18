package cn.rjtech.admin.inventoryspotcheckform;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.jfinal.aop.Inject;

import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryspotcheckformOperation.InventoryspotcheckformOperationService;
import cn.rjtech.admin.operation.OperationService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;

import com.jfinal.core.Path;
import com.jfinal.aop.Before;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.EquipmentModel;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.InventorySpotCheckForm;
import cn.rjtech.model.momdata.InventoryspotcheckformOperation;
import cn.rjtech.model.momdata.Operation;
import cn.rjtech.model.momdata.SpotCheckForm;
import cn.rjtech.model.momdata.VendorClass;
import cn.rjtech.model.momdata.base.BaseInventoryspotcheckformOperation;

/**
 * 质量建模-点检适用标准
 *
 * @ClassName: InventorySpotCheckFormAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 17:27
 */
@CheckPermission(PermissionKey.INVENTORYSPOTCHECKFORM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryspotcheckform", viewPath = "/_view/admin/inventoryspotcheckform")
public class InventorySpotCheckFormAdminController extends BaseAdminController {

    @Inject
    private InventorySpotCheckFormService service;                //点检适用标准
    @Inject
    private InventoryService              inventoryService;        //存货档案
    @Inject
    private OperationService              operationService;        //工序
    @Inject
    private EquipmentModelService         equipmentModelService; //机型档案
    @Inject
    private SpotCheckFormService          spotCheckFormService; //点检表格
    @Inject
    private DictionaryService             dictionaryService;  //字典
    @Inject
    private InventoryspotcheckformOperationService inventoryspotcheckformOperationService; //质量建模-存货点检工序

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
        List<Dictionary> dictionaryList = dictionaryService.getOptionListByTypeKey("iType");
        Page<Record> recordPage = service.pageList(getKv());
        for (Record record : recordPage.getList()) {
            String iTypeValue = record.get("iType").toString();
            Dictionary dictionary = dictionaryList.stream().filter(e -> e.getSn().equals(iTypeValue)).findFirst()
                .orElse(new Dictionary());
            record.set("itypename", dictionary.getName());
        }
        renderJsonData(recordPage);
    }

    /**
     * 新增的data
     * */
    public void addDatas() {
        InventorySpotCheckForm spotCheckForm = service.findById(getKv().get("iautoid"));
        if (null == spotCheckForm){
            renderJsonData(spotCheckForm);
            return;
        }
        renderJsonData(service.pageList(getKv()));
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
        renderJson(service.save(getModel(InventorySpotCheckForm.class, "inventorySpotCheckForm")));
    }

    /**
     * 编辑
     */
    public void edit() {
        InventorySpotCheckForm inventorySpotCheckForm = service.findById(getLong(0));
        if (inventorySpotCheckForm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Inventory inventory = inventoryService.findById(inventorySpotCheckForm.getIInventoryId());
        SpotCheckForm spotCheckForm = spotCheckFormService.findById(inventorySpotCheckForm.getISpotCheckFormId());
        EquipmentModel equipmentModel = equipmentModelService.findById(inventory.getIEquipmentModelId());
        set("inventoryspotcheckform", inventorySpotCheckForm);//点检适用标准
        set("inventory", inventory);//存货档案
        set("equipmentModel", equipmentModel);//机型档案
        set("spotCheckForm", spotCheckForm); //点检表格
        render("edit.html");
    }

    /**
     * 新增适用存货的弹窗页面
     * */
    public void equipmentqcparamSelect() {
        render("_item_qcbasis_select.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(InventorySpotCheckForm.class, "inventorySpotCheckForm")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        //质量建模-存货点检工序的数据也要删除
        String[] ids = get("ids").split(",");
        for (String id : ids) {
            List<InventoryspotcheckformOperation> list = inventoryspotcheckformOperationService
                .listByIinventorySpotCheckFormId(id);
            for (InventoryspotcheckformOperation operation : list) {
                inventoryspotcheckformOperationService.delete(operation.getIAutoId());
            }
        }
        //然后删除点检适用标准表
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换IsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "IsDeleted"));
    }

    public void updateEditTable() {
        renderJson(service.updateEditTable(getJBoltTable(), JBoltUserKit.getUserId(), new Date(), getKv()));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    public void dataExport() throws Exception {
        //1、查询全部
        List<InventorySpotCheckForm> list = service.list(getKv());
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(list);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        //renderJxls("vendorclass_import.xlsx", Kv.by("rows", null), "供应商分类导入模板.xlsx");
        renderBytesToExcelXlsFile(service.getExcelImportTpl().setFileName("供应商分类导入模板"));
    }

    /**
     * 供应商分类Excel导入数据库
     */
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelData(file.getFile()));
    }

}
