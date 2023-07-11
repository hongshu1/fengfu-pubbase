package cn.rjtech.admin.processdefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ProcessDefect;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 制程异常品记录 Controller
 *
 * @ClassName: ProcessDefectAdminController
 * @author: RJ
 * @date: 2023-04-21 15:49
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.PROCESSDEFECT)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/processdefect", viewPath = "/_view/admin/processdefect")
public class ProcessDefectAdminController extends BaseAdminController {

    @Inject
    private ProcessdefectService service;
    @Inject
    private SpecMaterialsRcvMService specMaterialsRcvMService;

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
        Kv kv = new Kv();
        kv.setIfNotNull("cdocno", get("cdocno"));
        kv.setIfNotNull("imodocid", get("imodocid"));
        kv.setIfNotNull("cmodocno", get("cmodocno")); //工单号
        kv.setIfNotNull("cinvcode1", get("cinvcode1"));
        kv.setIfNotNull("cinvname", get("cinvname"));
        kv.setIfNotNull("cinvcode", get("cinvcode"));
        kv.setIfNotNull("istatus", get("istatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        kv.setIfNotNull("idepartmentid", get("idepartmentid"));
        renderJsonData(service.paginateAdminDatas(getPageSize(), getPageNumber(), kv));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.PROCESSDEFECT_ADD)
    public void add() {
        render("add.html");
    }

    @CheckPermission(PermissionKey.PROCESSDEFECT_ADD)
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
                if(processDefect.getCApproach()!=null) {
                    int getCApproach = Integer.parseInt(processDefect.getCApproach());
                    set("capproach", (getCApproach == 1) ? "返修" : "报废");
                }
                set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
                render("add4.html");
            }
        }

    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.PROCESSDEFECT_EDIT)
    public void edit() {
        ProcessDefect processDefect = service.findById(getLong(0));
        if (processDefect == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("processDefect", processDefect);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.PROCESSDEFECT_ADD)
    public void save() {
        renderJson(service.save(getModel(ProcessDefect.class, "processdefect")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.PROCESSDEFECT_EDIT)
    public void update() {
        renderJson(service.update(getModel(ProcessDefect.class, "processdefect")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.PROCESSDEFECT_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.PROCESSDEFECT_DELETE)
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
     * 保存单据
     */
    public void saveprocessDefect(){
    renderJson(service.saveprocessDefect(getModel(ProcessDefect.class, "processDefect")));
    }

    /**
     * 更新单据
     */
    public void updateprocessDefect(){
        renderJson(service.updateprocessDefect(getModel(ProcessDefect.class, "processDefect")));
    }

    /**
     * 提交单据审批
     */
    public void subprocessDefect(){
        renderJson(service.subprocessDefect(getModel(ProcessDefect.class, "processDefect")));
    }

    /**
     * 生成二维码
     */
    @CheckPermission(PermissionKey.PROCESSDEFECT_QR)
    public void QRCode() {
        Kv kv = new Kv();
        kv.setIfNotNull("ids", get("ids"));
        renderJsonData(service.getQRCodeCheck(kv));
    }

    /**
     *  工序数据源
     */
    @UnCheck
    public void getOperationDatas() {
        String OrgCode = getOrgCode();
        Kv kv =new Kv();
        kv.setIfNotNull("OrgCode", OrgCode);
        renderJsonData(service.OperationDatas(kv));
    }


}
