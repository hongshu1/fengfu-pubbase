package cn.rjtech.admin.instockdefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InStockDefect;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

/**
 * 在库异常记录 Controller
 *
 * @ClassName: InStockDefectAdminController
 * @author: RJ
 * @date: 2023-04-25 14:58
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INSTOCKDEFECT)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/instockdefect", viewPath = "/_view/admin/instockdefect")
public class InStockDefectAdminController extends BaseAdminController {

    @Inject
    private InStockDefectService service;
    @Inject
    private InStockQcFormMService inStockQcFormMService;

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
        renderJsonData(service.paginateAdminDatas(getPageSize(), getPageNumber(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.INSTOCKDEFECT_ADD)
    public void add() {
        render("add.html");
    }


    @CheckPermission(PermissionKey.INSTOCKDEFECT_EDIT)
    public void add2() {
        InStockDefect inStockDefect = service.findById(get("iautoid"));
        Record inStockQcFormM = service.getinStockQcFormMList(getLong("iinstockqcformmid"));
        set("iautoid", get("iautoid"));
        set("type", get("type"));
        set("inStockDefect", inStockDefect);
        set("inStockQcFormM", inStockQcFormM);
        if (isNull(get("iautoid"))) {
            render("add2.html");
        } else {
            if (inStockDefect.getIStatus() == 1) {
                set("isfirsttime", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内" : "其他"));
                render("add3.html");
            } else if (inStockDefect.getIStatus() == 2) {
                int getCApproach = Integer.parseInt(inStockDefect.getCApproach());
                set("capproach", (getCApproach == 1) ? "报废" : (getCApproach == 2 ? "返修" : "退货"));
                set("isfirsttime", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内" : "其他"));
                render("add4.html");
            }
        }
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.INSTOCKDEFECT_EDIT)
    public void edit() {
        InStockDefect inStockDefect = service.findById(getLong(0));
        if (inStockDefect == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("inStockDefect", inStockDefect);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(InStockDefect.class, "instockdefect")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(InStockDefect.class, "instockdefect")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.INSTOCKDEFECT_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.INSTOCKDEFECT_DELETE)
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
    @CheckPermission(PermissionKey.INSTOCKDEFECT_PRINT)
    public void QRCode() {
        Kv kv = new Kv();
        kv.setIfNotNull("ids", get("ids"));
        renderJsonData(service.getQRCodeCheck(kv));
    }


}
