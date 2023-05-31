package cn.rjtech.admin.sysmaterialsprepare;


import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import org.apache.commons.lang.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jfinal.plugin.activerecord.Db.tx;

/**
 * 材料备料表
 *
 * @ClassName: SysMaterialsprepareAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-12 18:28
 */
@CheckPermission(PermissionKey.ADMIN_SYSMATERIALSPREPARE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysmaterialspreparedetail", viewPath = "/_view/admin/sysmaterialspreparedetail")
public class SysMaterialspreparedetailAdminController extends BaseAdminController {

    @Inject
    private SysMaterialsprepareService service;

    @Inject
    private SysMaterialspreparedetailService1 serviceD;

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
     * 数据源
     */
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
//        String sourcebillid = get("sourcebillid");
//        MoDoc modoc = moDocS.findFirst("select *   from Mo_MoDoc where iAutoId=? and iStatus=2", sourcebillid);
//        SysMaterialsprepare sysMaterialsprepare = new SysMaterialsprepare();
//        SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
//        String billNo = BillNoUtils.getcDocNo(getOrgId(), "QTCK", 5);
//        Date nowDate = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String date = sdf.format(nowDate);
//        User user = JBoltUserKit.getUser();
//        sysMaterialsprepare.setBillNo(billNo);
//        sysMaterialsprepare.setBillDate(date);
//        sysMaterialsprepare.setSourceBillID(sourcebillid);
//        sysMaterialsprepare.setCreatePerson(user.getName());
//        sysMaterialsprepare.setCreateDate(nowDate);
//        sysMaterialsprepare.setOrganizeCode(getOrgCode());
//        sysMaterialsprepare.setSourceBillNo(modoc.getCMoDocNo());
//        Ret save1 = service.save(sysMaterialsprepare);
        //副表存储
//        SysMaterialsprepare sysMaterialsprepare1 = service.findFirst("select *   from T_Sys_MaterialsPrepare where SourceBillID=?", sourcebillid);
//        String autoID = sysMaterialsprepare1.get("AutoID");
//        sysMaterialspreparedetail.setMasID(Long.valueOf(autoID));
//        sysMaterialspreparedetail.setPosCode();
//        sysMaterialspreparedetail.setBarcode();
//        Inventory inventory = invent.findFirst("select *   from Bd_Inventory where iAutoId=?", modoc.getIInventoryId());
//        sysMaterialspreparedetail.setInvCode(inventory.getCInvCode());
//        sysMaterialspreparedetail.setNum();
//        sysMaterialspreparedetail.setQty(modoc.getIQty());
//        sysMaterialspreparedetail.setPackRate();
//        sysMaterialspreparedetail.setSourceBillType();
//        sysMaterialspreparedetail.setSourceBillNo();
//        sysMaterialspreparedetail.setSourceBillNoRow();
//        sysMaterialspreparedetail.setSourceBillID();
//        sysMaterialspreparedetail.setSourceBillDid();
//        sysMaterialspreparedetail.setMemo();
//        sysMaterialspreparedetail.setCreatePerson(user.getName());
//        sysMaterialspreparedetail.setCreateDate(nowDate);
//        sysMaterialspreparedetail.setModifyPerson();
//        sysMaterialspreparedetail.setModifyDate();
//        Ret save2 = serviceD.save(sysMaterialspreparedetail);
//        if (!save1.isFail()&&!save2.isFail()){
//            modoc.setIStatus(3);
//            Ret update = moDocS.update(modoc);
//            if (!update.isFail()){
//                renderJson("主表创建状态："+save1+'\n'+"副表创建状态："+save2+'\n'+"修改工单状态："+update);
//                return;
//            }
//            renderJson("主表创建状态："+save1+'\n'+"副表创建状态："+save2+'\n'+"修改工单状态："+update);
//            return;
//        }
//        renderJson("主表创建状态："+save1+'\n'+"副表创建状态："+save2+'\n'+"修改工单状态：失败");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysMaterialsprepare.class, "sysMaterialsprepare")));
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
        MoDoc modoc = moDocS.findFirst("select *   from Mo_MoDoc where iAutoId=?", sysMaterialsprepare.getSourceBillID());
        Department first1 = department.findFirst("select *   from Bd_Department where iAutoId=?", modoc.getIDepartmentId());
        SysMaterialspreparedetail mpd = serviceD.findFirst("select *   from T_Sys_MaterialsPrepareDetail where MasID=?", sysMaterialsprepare.getAutoID());
        Inventory inventory = invent.findFirst("select *   from Bd_Inventory where cInvCode=?", mpd.getInvCode());
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
        set("SysMaterialsprepare", sysMaterialsprepare);
        render("edit.html");
    }

    /**
     * 编辑
     */
    public void edit1() {
        SysMaterialsprepare sysMaterialsprepare = service.findById(getLong(0));
        if (sysMaterialsprepare == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        MoDoc modoc = moDocS.findFirst("select *   from Mo_MoDoc where iAutoId=?", sysMaterialsprepare.getSourceBillID());
        Department first1 = department.findFirst("select *   from Bd_Department where iAutoId=?", modoc.getIDepartmentId());
        SysMaterialspreparedetail mpd = serviceD.findFirst("select *   from T_Sys_MaterialsPrepareDetail where MasID=?", sysMaterialsprepare.getAutoID());
        Inventory inventory = invent.findFirst("select *   from Bd_Inventory where iAutoId=?", modoc.getIInventoryId());
        EquipmentModel equipmentModel = equipmentModelService.findFirst("select *   from Bd_EquipmentModel where iAutoId=?", inventory.getIEquipmentModelId()== null ? "" : inventory.getIEquipmentModelId());
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
        render("edit1.html");
    }

    /**
     * 编辑
     */
    public void edit2() {
        String CIN=get("cinvcode");
        Inventory inventory = invent.findFirst("select *   from Bd_Inventory where cInvCode=?", get("cinvcode"));
        StockBarcodePosition stockBarcodePosition = stockBarcodePositionService.findFirst("select *   from T_Sys_StockBarcodePosition where InvCode=?", get("cinvcode"));
        set("inventory", inventory);
        set("stockBarcodePosition", stockBarcodePosition);
        render("edit2.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysMaterialsprepare.class, "sysMaterialsprepare")));
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

    public void options() {
        renderJsonData(service.options());
    }

    public void options1() {
        renderJsonData(service.options1());
    }

    /**
     * 自动生成
     */
    public void auto() {
        Date nowDate = new Date();
        set("dplandate", nowDate);
        render("auto.html");
    }

    /**
     * 手动生成
     */
    public void manual() {
        render("manual.html");
    }

    /**
     * 材料出库单列表明细
     */
    public void getMaterialsOutLines() {
        String cmodocno = get("cmodocno");
        Kv kv = new Kv();
        kv.set("cmodocno", cmodocno == null ? "" : cmodocno);
        renderJsonData(service.getMaterialsOutLines(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 数据源
     */
    public void datasForauto() {
        renderJsonData(service.getAdminDatasForauto(getPageNumber(), getPageSize(), getKv()));
    }

    @Before(Tx.class)
    public void submitAll() {
        String sourcebillid = get("sourcebillid");
        MoDoc modoc = moDocS.findFirst("select *   from Mo_MoDoc where iAutoId=? and iStatus=2", sourcebillid);
        renderJson(service.submitByJBoltTable(sourcebillid, modoc));
    }

    public void getMaterialsOutLines1() {
        String cmodocno = get("cmodocno");
        Kv kv = new Kv();
        kv.set("cmodocno", cmodocno == null ? "" : cmodocno);
        renderJsonData(service.getMaterialsOutLines1(getPageNumber(), getPageSize(), kv));
    }

    public void getMaterialsOutLines2() {
        String invcode = get("invcode");
        Kv kv = new Kv();
        kv.set("invcode", invcode == null ? "" : invcode);
        renderJsonData(service.getMaterialsOutLines2(getPageNumber(), getPageSize(), kv));
    }

}
