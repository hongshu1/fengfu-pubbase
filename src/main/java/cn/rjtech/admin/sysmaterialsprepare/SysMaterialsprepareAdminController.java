package cn.rjtech.admin.sysmaterialsprepare;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
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
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

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

//   /**
//	* 数据源
//	*/
//	public void datas() {
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("BillType")));
//	}

    /**
     * 新增
     */
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
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }


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
        String billno = get("billno");
        Kv kv = new Kv();
        kv.set("billno", billno == null ? "" : billno);
        renderJsonData(service.getManualdatas(getPageNumber(), getPageSize(), kv));
    }

    public void detailShow() {
        SysMaterialsprepare sysMaterialsprepare = service.findById(getLong(0));
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
        render("detailShow.html");
    }

    public void getDetail() {
        String billno = get("billno");
        Kv kv = new Kv();
        kv.set("billno", billno == null ? "" : billno);
        renderJsonData(service.getDetail(getPageNumber(), getPageSize(), kv));
    }

    public void barcodeDetail() {
        String CIN = get("cinvcode");
        Inventory inventory = invent.findFirst("select *   from Bd_Inventory where cInvCode=?", get("cinvcode"));
        StockBarcodePosition stockBarcodePosition = stockBarcodePositionService.findFirst("select *   from T_Sys_StockBarcodePosition where InvCode=?", get("cinvcode"));
        set("inventory", inventory);
        set("stockBarcodePosition", stockBarcodePosition);
        render("barcodeDetail.html");
    }

    public void getBarcode() {
        String invcode = get("invcode");
        Kv kv = new Kv();
        kv.set("invcode", invcode == null ? "" : invcode);
        renderJsonData(service.getBarcodedatas(getPageNumber(), getPageSize(), kv));
    }

    @Before(Tx.class)
    public void submitAll() {
        //工单ID
        Long id = Long.valueOf(get("id"));
        renderJson(service.submitByJBoltTable(id));
    }
}
