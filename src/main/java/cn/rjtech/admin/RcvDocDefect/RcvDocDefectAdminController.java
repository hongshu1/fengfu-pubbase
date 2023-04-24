package cn.rjtech.admin.RcvDocDefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RcvDocDefect;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;

/**
 * 来料异常品记录 Controller
 *
 * @ClassName: RcvDocDefectAdminController
 * @author: RJ
 * @date: 2023-04-18 16:36
 */
@CheckPermission(PermissionKey.NONE)
@Path(value = "/admin/RcvDocDefect", viewPath = "/_view/admin/RcvDocDefect")
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
     */
    public void datas() {
        Okv kv = new Okv();
        kv.setIfNotNull("cDocNo", get("cDocNo"));
        kv.setIfNotNull("iRcvDocQcFormMid", get("iRcvDocQcFormMid"));
        kv.setIfNotNull("cInvCode", get("cInvCode"));
        kv.setIfNotNull("iInventoryId", get("iInventoryId"));
        kv.setIfNotNull("cInvName", get("cInvName"));
        kv.setIfNotNull("iStatus", get("iStatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        renderJsonData(service.paginateAdminDatas(getPageSize(), getPageNumber(), kv));
    }


    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }


    public void add2() {
        RcvDocDefect rcvDocDefect = service.findById(get("iautoid"));
        RcvDocQcFormM rcvDocQcFormM = rcvDocQcFormMService.findById(get("ircvdocqcformmid"));
        set("iautoid", get("iautoid"));
        set("type", get("type"));
        if (isNull(get("iautoid"))) {
            set("rcvDocDefect", rcvDocDefect);
            set("rcvDocQcFormM", rcvDocQcFormM);
            render("add2.html");
        } else {
            if (rcvDocDefect.getIStatus() == 1) {
                set("istatus", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
                set("rcvDocDefect", rcvDocDefect);
                set("rcvDocQcFormM", rcvDocQcFormM);
                render("add3.html");
            } else if (rcvDocDefect.getIStatus() == 2) {
                int getCApproach = Integer.parseInt(rcvDocDefect.getCApproach());
                set("capproach", (getCApproach == 1) ? "特采" : "拒收");
                set("istatus", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
                set("rcvDocDefect", rcvDocDefect);
                set("rcvDocQcFormM", rcvDocQcFormM);
                render("add4.html");
            }
        }


    }

    /**
     * 编辑
     */
    public void edit() {
        RcvDocDefect rcvDocDefect = service.findById(getLong(0));
        if (rcvDocDefect == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("rcvDocDefect", rcvDocDefect);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(RcvDocDefect.class, "rcvDocDefect")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(RcvDocDefect.class, "rcvDocDefect")));
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
        renderJson(service.updateEditTable(getJBoltTable(), getKv()));
    }


}
