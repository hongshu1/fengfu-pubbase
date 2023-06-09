package cn.rjtech.admin.moprocessdefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.morouting.MoMoroutingService;
import cn.rjtech.admin.processdefect.ProcessdefectService;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
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
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;

/**
 * 工单管理-制程品异常记录
 * @ClassName :
 * @Description :
 * @Author :
 * @Date: 2023-05-25
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/moprocessdefect", viewPath = "/_view/admin/moprocessdefect")
public class MoprocessDefectAdminController extends BaseAdminController {

    @Inject
    private ProcessdefectService service;
    @Inject
    private SpecMaterialsRcvMService specMaterialsRcvMService;
    @Inject
    private MoDocService moDocService;
    @Inject
    private InventoryService inventoryService; //存货档案
    @Inject
    private UomService uomService; //单位

    @Inject
    private MoMoroutingService moMoroutingService; //工艺路线

    @Inject
    private WorkshiftmService workshiftmService; //班次

    @Inject
    private WorkregionmService workregionmService; //产线


    /**
     * 首页
     */
    public void index() {
        set("imodocid",getLong("imodocid"));
        render("index.html");
    }


    /**
     * 数据源
     */
    public void datas() {
        Kv kv = new Kv();
        kv.setIfNotNull("cdocno", get("cdocno"));
        kv.setIfNotNull("imodocid", get("imodocid"));
        kv.setIfNotNull("cinvcode1", get("cinvcode1"));
        kv.setIfNotNull("cinvname", get("cinvname"));
        kv.setIfNotNull("cinvcode", get("cinvcode"));
        kv.setIfNotNull("istatus", get("istatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        renderJsonData(service.paginateAdminDatas(getPageSize(), getPageNumber(), kv));
    }

    /**
     * 新增
     */
    public void add() {
        if(notOk(getLong("imodocid"))){
            ValidationUtils.error("信息异常");
        }
        getModoc(getLong("imodocid"));

        render("add2.html");
    }
    public void getModoc(Long imodocid){
        MoDoc moDoc=moDocService.findById(imodocid);
        if(moDoc == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record moRecod=moDoc.toRecord();
        if(isOk(moDoc.getIInventoryId())){
            //存货
            Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
            if(inventory!=null){
                moRecod.set("cinvcode",inventory.getCInvCode());//料品编码
                moRecod.set("cinvcode1",inventory.getCInvCode1());//客户部番
                moRecod.set("cinvname1",inventory.getCInvName1());//部品名称
                Uom uom=uomService.findFirst(Okv.create().
                        setIfNotNull(Uom.IAUTOID,inventory.getICostUomId()),Uom.IAUTOID,"desc");
                if(uom!=null) {
                    moRecod.set("manufactureuom", uom.getCUomName());//生产单位
                }
                moRecod.set("cinvstd",inventory.getCInvStd());//规格

            }
            //工艺路线
            MoMorouting moMorouting=moMoroutingService.findByImdocId(moDoc.getIAutoId());
            if(moMorouting!=null){
                moRecod.set("croutingname",moMorouting.getCRoutingName());//工艺路线名称
            }

        }
        //差异数量
        if(moDoc.getIQty()!=null&&moDoc.getICompQty()!=null) {
            BigDecimal cyqty =moDoc.getIQty().subtract(moDoc.getICompQty());
            moRecod.set("cyqty",cyqty);
        }
        //产线
        if(isOk(moDoc.getIWorkRegionMid())){
            Workregionm workregionm=workregionmService.findById(moDoc.getIWorkRegionMid());
            if(workregionm!=null){
                moRecod.set("cworkname",workregionm.getCWorkName());
            }
        }
        //班次
        if(isOk(moDoc.getIWorkShiftMid())){
           Workshiftm  workshiftm=workshiftmService.findById(moDoc.getIWorkShiftMid());
            if(workshiftm!=null){
                moRecod.set("cworkshiftname",workshiftm.getCworkshiftname());
            }
        }
        set("moDoc",moRecod);
    }

    public void add2() {
        ProcessDefect processDefect = service.findById(get("iautoid"));
        SpecMaterialsRcvM specMaterialsRcvM = specMaterialsRcvMService.findById(get("iissueid"));
        set("iautoid", get("iautoid"));
        set("type", get("type"));
        set("iissueid", get("iissueid"));
        set("processDefect", processDefect);
        set("specMaterialsRcvM", specMaterialsRcvM);
        if (isNull(get("iautoid"))) {
            render("add2.html");
        } else {
            if (processDefect.getIStatus() == 1) {
                set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
                render("add3.html");
            } else if (processDefect.getIStatus() == 2) {
                int getCApproach = Integer.parseInt(processDefect.getCApproach());
                set("capproach", (getCApproach == 1) ? "返修" : "报废");
                set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
                render("add4.html");
            }
        }

    }

    /**
     * 编辑
     */
    public void edit() {
        ProcessDefect processDefect = service.findById(getLong(0));
        if (processDefect == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        getModoc(processDefect.getIMoDocId());//工单信息
        set("processDefect", processDefect);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(ProcessDefect.class, "processdefect")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ProcessDefect.class, "processdefect")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }


    public void ProcessDefectupdateEditTable() {
        renderJson(service.updateEditTable(getKv()));
    }

    /**
     * 生成二维码
     */
    public void QRCode() {
        Kv kv = new Kv();
        kv.setIfNotNull("ids", get("ids"));
        renderJsonData(service.getQRCodeCheck(kv));
    }

}
