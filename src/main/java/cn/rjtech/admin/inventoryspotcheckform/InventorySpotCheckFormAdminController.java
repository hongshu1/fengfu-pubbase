package cn.rjtech.admin.inventoryspotcheckform;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryspotcheckformOperation.InventoryspotcheckformOperationService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    @CheckPermission(PermissionKey.INVENTORYSPOTCHECKFORM_ADD)
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
    @CheckPermission(PermissionKey.INVENTORYSPOTCHECKFORM_EDIT)
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
    @CheckPermission(PermissionKey.INVENTORYSPOTCHECKFORM_DELETE)
    public void deleteByIds() {
        //质量建模-存货点检工序的数据也要删除
        String[] ids = get("ids").split(",");
        for (String id : ids) {
            List<InventoryspotcheckformOperation> list = inventoryspotcheckformOperationService
                .listByIinventorySpotCheckFormId(Long.valueOf(id));
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
    @CheckPermission(PermissionKey.INVENTORYSPOTCHECKFORM_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换IsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "IsDeleted"));
    }

    @CheckPermission(PermissionKey.INVENTORYSPOTCHECKFORM_SUBMIT)
    public void updateEditTable() {
        renderJson(service.updateTable(getJBoltTable()));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    public void dataExport() throws Exception {
        //1、查询全部
        List<Record> recordList = service.list(getKv());
        //2、生成excel文件
        ArrayList<InventorySpotCheckForm> list = new ArrayList<>();
        recordList.stream().forEach(e->{
            list.add(service.findById(e.get("iautoid")));
        });
        JBoltExcel jBoltExcel = service.exportExcelTpl(list);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("inventoryspotcheckform_improt.xlsx", Kv.by("rows", null), "点检适用标准导入模板.xlsx");
    }

    /**
     * 数据导入
     */
    @CheckPermission(PermissionKey.INVENTORYSPOTCHECKFORM_IMPORT)
    public void importExcelData() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");

        File file = uploadFile.getFile();

        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
        ValidationUtils.equals(list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        
        renderJson(service.importExcelData(file));
    }

}
