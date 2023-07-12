package cn.rjtech.admin.sysmaterialsprepare;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.sysmaterialspreparedetail.SysMaterialspreparedetailService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 材料备料表
 *
 * @ClassName: SysMaterialsprepareAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-02 00:06
 */
@CheckPermission(PermissionKey.ADMIN_SYSMATERIALSPREPARE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysMaterialsprepare", viewPath = "/_view/admin/sysmaterialsprepare")
public class SysMaterialsprepareAdminController extends BaseAdminController {

    @Inject
    private SysMaterialsprepareService service;

    @Inject
    private SysMaterialspreparedetailService serviceD;

    @Inject
    private MoDocService moDocS;

    @Inject
    private InventoryService invent;

    @Inject
    private DepartmentService department;

    @Inject
    private WorkshiftmService workshiftmService;

    @Inject
    private UomService uomService;

    @Inject
    private WorkregionmService workregionmService;

    @Inject
    private EquipmentModelService equipmentModelService;

    @Inject
    private StockBarcodePositionService stockBarcodePositionService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }


    /**
     * 新增
     */
    @CheckPermission(PermissionKey.MATERIALSPREPARE_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save(SysMaterialsprepare sysMaterialsprepare) {
        renderJson(service.save(sysMaterialsprepare));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.MATERIALSPREPARE_EDIT)
    public void edit() {
        SysMaterialsprepare sysMaterialsprepare = service.findById(getLong(0));
        if (sysMaterialsprepare == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysMaterialsprepare", sysMaterialsprepare);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update(SysMaterialsprepare sysMaterialsprepare) {
        renderJson(service.update(sysMaterialsprepare));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.MATERIALSPREPARE_DELETE_ALL)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.MATERIALSPREPARE_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    @UnCheck
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    public void auto() {
        Date nowDate = new Date();
        set("dplandate", nowDate);
        render("auto.html");
    }

    public void datasForauto() {
        renderJsonData(service.getAdminDatasForauto(getPageNumber(), getPageSize(), getKv()));
    }

    public void manual() {
        SysMaterialsprepare sysMaterialsprepare = service.findById(getLong(0));
        if (sysMaterialsprepare == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        MoDoc modoc = moDocS.findFirst("select *   from Mo_MoDoc where iAutoId=?", sysMaterialsprepare.getSourceBillID());
        Department department1 = department.findFirst("select *   from Bd_Department where iAutoId=?", modoc.getIDepartmentId());
//      List<SysMaterialspreparedetail> sysMaterialspreparedetails = serviceD.find("select *   from T_Sys_MaterialsPrepareDetail where MasID=?", sysMaterialsprepare.getAutoID());
        Inventory inventory = invent.findFirst("select *   from Bd_Inventory where iAutoId=?", modoc.getIInventoryId());
        EquipmentModel equipmentModel = equipmentModelService.findFirst("select *   from Bd_EquipmentModel where iAutoId=?", inventory.getIEquipmentModelId());
        Workshiftm workshiftm = workshiftmService.findFirst("select *   from Bd_WorkShiftM where iAutoId=?", modoc.getIWorkShiftMid());
        Workregionm workregionm = workregionmService.findFirst("select *   from Bd_WorkRegionM where iAutoId=?", modoc.getIWorkRegionMid());
        if (null != workregionm && null != workregionm.getCWorkName()) {
            set("cworkname", workregionm.getCWorkName());
        }
        if (null != modoc && null != modoc.getCMoDocNo()) {
            set("cmodocno", modoc.getCMoDocNo());
        }
        if (null != modoc && null != modoc.getDPlanDate()) {
            set("dplandate", modoc.getDPlanDate());
        }
        if (null != modoc && null != modoc.getIQty()) {
            set("iqty", modoc.getIQty());
        }
        if (null != department1 && null != department1.getCDepName()) {
            set("cdepname", department1.getCDepName());
        }
        if (null != equipmentModel && null != equipmentModel.getCEquipmentModelName()) {
            set("cequipmentmodelname", equipmentModel.getCEquipmentModelName());
        }
        if (null != inventory && null != inventory.getCInvCode1()) {
            set("cinvcode1", inventory.getCInvCode1());
        }
        if (null != inventory && null != inventory.getCInvCode()) {
            set("cinvcode", inventory.getCInvCode());
        }
        if (null != inventory && null != inventory.getCInvName1()) {
            set("cinvname1", inventory.getCInvName1());
        }
        if (null != workshiftm && null != workshiftm.getCworkshiftname()) {
            set("cworkshiftname", workshiftm.getCworkshiftname());
        }
        set("sysMaterialsprepare", sysMaterialsprepare);
        render("manual.html");
    }

    public void manualDatas() {
        String data = get("data");
        String CMO = get("CMO");
        if (data==null){
            renderJsonData(null);
            return;
        }
        //库存ID itID
        String[] itIds = data.split(",");
        //去重
        List list = new ArrayList();
        for(int x=0;x<itIds.length;x++){
            if(!list.contains(itIds[x])){
                list.add(itIds[x]);
            }
        }
        renderJsonData(service.manualmanual(getPageNumber(), getPageSize(), list));
    }

    public void detailShow() {
        SysMaterialsprepare sysMaterialsprepare = service.findById(Long.valueOf(get("autoid")));
        if (sysMaterialsprepare == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        MoDoc modoc = moDocS.findFirst("select *   from Mo_MoDoc where iAutoId=?", sysMaterialsprepare.getSourceBillID());
        Department first1 = department.findFirst("select *   from Bd_Department where iAutoId=?", modoc.getIDepartmentId());
        SysMaterialspreparedetail mpd = serviceD.findFirst("select *   from T_Sys_MaterialsPrepareDetail where MasID=?", sysMaterialsprepare.getAutoID());
        Inventory inventory = invent.findFirst("select *   from Bd_Inventory where iAutoId=?", modoc.getIInventoryId());
        EquipmentModel equipmentModel = equipmentModelService.findFirst("select *   from Bd_EquipmentModel where iAutoId=?", inventory.getIEquipmentModelId() == null ? "" : inventory.getIEquipmentModelId());
        Workshiftm workshiftm = workshiftmService.findFirst("select *   from Bd_WorkShiftM where iAutoId=?", modoc.getIWorkShiftMid());
        if (null != modoc && null != modoc.getCMoDocNo()) {
            set("cmodocno", modoc.getCMoDocNo());
        }
        if (null != modoc && null != modoc.getDPlanDate()) {
            set("dplandate", modoc.getDPlanDate());
        }
        if (null != modoc && null != modoc.getIQty()) {
            set("iqty", modoc.getIQty());
        }
        if (null != modoc && null != modoc.getIStatus()) {
            if (modoc.getIStatus() == 1) {
                set("istatus", "未安排人员");
            }
            if (modoc.getIStatus() == 2) {
                set("istatus", "已安排人员");
            }
            if (modoc.getIStatus() == 3) {
                set("istatus", "生成备料表");
            }
            if (modoc.getIStatus() == 4) {
                set("istatus", "待生产");
            }
            if (modoc.getIStatus() == 5) {
                set("istatus", "生产中");
            }
            if (modoc.getIStatus() == 6) {
                set("istatus", "已完工");
            }
            if (modoc.getIStatus() == 7) {
                set("istatus", "已关闭");
            }
            if (modoc.getIStatus() == 8) {
                set("istatus", "已取消");
            }
        }
        if (null != first1 && null != first1.getCDepName()) {
            set("cdepname", first1.getCDepName());
        }
        if (null != equipmentModel && null != equipmentModel.getCEquipmentModelName()) {
            set("cequipmentmodelname", equipmentModel.getCEquipmentModelName());
        }
        if (null != inventory && null != inventory.getCInvCode1()) {
            set("cinvcode1", inventory.getCInvCode1());
        }
        if (null != inventory && null != inventory.getCInvName1()) {
            set("cinvname1", inventory.getCInvName1());
        }
        if (null != workshiftm && null != workshiftm.getCworkshiftname()) {
            set("cworkshiftname", workshiftm.getCworkshiftname());
        }
        set("sysMaterialsprepare", sysMaterialsprepare);
        set("ifinish",get("ifinish"));
        render("detailShow.html");
    }

    public void getDetail() {
        String billno = get("billno");
        Kv kv = new Kv();
        kv.set("billno", billno == null ? "" : billno);
        renderJsonData(service.getDetail(getPageNumber(), getPageSize(), kv));
    }

    public void barcodeDetail() {
        String cinvcode = get("cinvcode");
        String batch = get("batch");
        String cinvcode1 = get("cinvcode1");
        String cinvname1 = get("cinvname1");
        set("cinvcode",cinvcode);
        set("batch",batch);
        set("cinvcode1",cinvcode1);
        set("cinvname1",cinvname1);
        render("barcodeDetail.html");
    }
    public void barcodeDetail1() {
        String cinvcode = get("cinvcode");
        String batch = get("batch");
        String cinvcode1 = get("cinvcode1");
        String cinvname1 = get("cinvname1");
        set("cinvcode",cinvcode);
        set("batch",batch);
        set("cinvcode1",cinvcode1);
        set("cinvname1",cinvname1);
        render("barcodeDetail1.html");
    }

    public void getBarcode() {
        String invcode = get("cinvcode");
        String batch = get("batch");
        Kv kv = new Kv();
        kv.set("invcode", invcode == null ? "" : invcode);
        kv.set("batch", batch == null ? "" : batch);
        renderJsonData(service.getBarcodedatas(getPageNumber(), getPageSize(), kv));
    }

    public void getBarcode1() {
        String invcode = get("cinvcode");
        String batch = get("batch");
        Kv kv = new Kv();
        kv.set("invcode", invcode == null ? "" : invcode);
        kv.set("batch", batch == null ? "" : batch);
        renderJsonData(service.getBarcodedatas1(getPageNumber(), getPageSize(), kv));
    }

    @Before(Tx.class)
    public void submitAll() {
        //工单ID
        Long id = Long.valueOf(get("id"));
        renderJson(service.submitByJBoltTable(id));
    }

    public void MaterialsListchoose() {
        String cmodocno = get("cmodocno");
        set("cmodocno",cmodocno);
        render("chooseMaterials.html");
    }

    public void getManualAdddatas() {
//        String cmodocno = get("cmodocno");
//        Kv kv = new Kv();
//        kv.set("cmodocno", cmodocno == null ? "" : cmodocno);
        renderJsonData(service.getgetManualAdddatas(getPageNumber(), getPageSize(), getKv()));
    }

    /***
     * 勾选导出
     */
    public void downloadChecked(){
        Kv kv = getKv();
        String ids = kv.getStr("ids");
        if (ids != null) {
            String[] split = ids.split(",");
            String sqlids = "";
            for (String id : split) {
                sqlids += "'" + id + "',";
            }
//            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
            sqlids = sqlids.substring(0, sqlids.length() - 1);
            kv.set("sqlids", sqlids);
        }

        String sqlTemplate = "sysSaleDeliver.pageList";
        List<Record> list = service.download(kv, sqlTemplate);
        JBoltCamelCaseUtil.keyToCamelCase(list);
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .create()//创建JBoltExcel
                .addSheet(//设置sheet
                        JBoltExcelSheet.create("销售出库单列表")
                                .setHeaders(1,//sheet里添加表头
                                        JBoltExcelHeader.create("cinvcode", "存货编码", 15),
                                        JBoltExcelHeader.create("cinvcode1", "客户部番", 15),
                                        JBoltExcelHeader.create("cinvname1", "部品名称", 15),
                                        JBoltExcelHeader.create("cinvstd", "规格", 15),
                                        JBoltExcelHeader.create("cuomname", "库存单位", 15),
                                        JBoltExcelHeader.create("", "计划数量", 15),
                                        JBoltExcelHeader.create("", "出库仓库", 15),
                                        JBoltExcelHeader.create("", "出库库区", 15),
                                        JBoltExcelHeader.create("", "已备料数量", 15),
                                        JBoltExcelHeader.create("", "剩余备料数量", 15),
                                        JBoltExcelHeader.create("batch", "可用批次号", 15),
                                        JBoltExcelHeader.create("statename", "齐料扫码检查", 15)
                                ).setDataChangeHandler((data, index) -> {
                                })
                                .setRecordDatas(2, list)//设置数据
                ).setFileName("销售出库单列表-" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }
    @Before(Tx.class)
    public void deleteBill() {
        renderJson(service.deleteBill(getKv()));
    }
}