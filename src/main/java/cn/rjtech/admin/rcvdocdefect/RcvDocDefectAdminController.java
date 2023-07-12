package cn.rjtech.admin.rcvdocdefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RcvDocDefect;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

/**
 * 来料异常品记录 Controller
 *
 * @ClassName: RcvDocDefectAdminController
 * @author: RJ
 * @date: 2023-04-18 16:36
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.RCVDOCDEFECT)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rcvdocdefect", viewPath = "/_view/admin/rcvdocdefect")
public class RcvDocDefectAdminController extends BaseAdminController {

    @Inject
    private RcvDocDefectService service;
    @Inject
    private RcvDocQcFormMService rcvDocQcFormMService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     *
     */
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageSize(), getPageNumber(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.RCVDOCDEFECT_ADD)
    public void add() {
        render("add.html");
    }

    @CheckPermission(PermissionKey.RCVDOCDEFECT_ADD)
    public void add2() {
        RcvDocDefect rcvDocDefect = service.findById(get("iautoid"));
        Record rcvDocQcFormM = service.getrcvDocQcFormList(getLong("ircvdocqcformmid"));
        if (rcvDocDefect.getIStatus() == 1) {
            render("add2.html");
        } else {
            if (rcvDocDefect.getIStatus() == 2) {
                set("isfirsttime", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
                render("add3.html");
            } else if (rcvDocDefect.getIStatus() == 3) {
                int getCApproach = Integer.parseInt(rcvDocDefect.getCApproach());
                set("capproach", (getCApproach == 1) ? "特采" : "拒收");
                set("isfirsttime", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
                render("add4.html");
            }
        }
        set("iautoid", get("iautoid"));
        set("type", get("type"));
        set("rcvDocDefect", rcvDocDefect);
        set("rcvDocQcFormM", rcvDocQcFormM);
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.RCVDOCDEFECT_EDIT)
    public void edit() {
        RcvDocDefect rcvDocDefect = service.findById(getLong(0));
        if (rcvDocDefect == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("rcvdocdefect", rcvDocDefect);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.RCVDOCDEFECT_ADD)
    public void save() {
        renderJson(service.save(getModel(RcvDocDefect.class, "rcvdocdefect")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.RCVDOCDEFECT_EDIT)
    public void update() {
        renderJson(service.update(getModel(RcvDocDefect.class, "rcvdocdefect")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.RCVDOCDEFECT_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.RCVDOCDEFECT_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsFirstTime
     */
    public void toggleIsFirstTime() {
        renderJson(service.toggleIsFirstTime(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    public void updateEditTable() {
        renderJson(service.updateEditTable(getKv()));
    }


    /**
     * 生成二维码
     */
    @CheckPermission(PermissionKey.RCVDOCDEFECT_QR)
    public void QRCode() {
        Kv kv = new Kv();
        kv.setIfNotNull("ids", get("ids"));
        renderJsonData(service.getQRCodeCheck(kv));
    }

}
