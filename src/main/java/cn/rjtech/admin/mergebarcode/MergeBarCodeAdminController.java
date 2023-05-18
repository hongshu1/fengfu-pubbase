package cn.rjtech.admin.mergebarcode;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;


/**
 * 合并条码 Controller
 *
 * @ClassName: MergeBarCodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/mergeBarcode", viewPath = "_view/admin/mergebarcode")
public class MergeBarCodeAdminController extends BaseAdminController {

    @Inject
    private MergeBarCodeService service;

    /**
     * 合并条码记录
     */
    public void index() {
        render("index().html");
    }

    public void barcodeIndex() {
        render("stripbarcode_select.html");
    }

    public void StripSelectDatas() {
        renderJsonData(service.StripSelectDatas(getPageNumber(), getPageSize(), getKv()));
    }

    public void detailDatas() {
        Kv kv = getKv();
        String sourceid = kv.getStr("sourceid");
        renderJsonData(service.findListByShiWu(sourceid));
    }

    /**
     * 合并条码提交
     */
    public void SubmitStripForm() {
        Kv kv = getKv();
        renderJsonData(service.SubmitStripForm(kv));

    }

    /**
     * 合并条码记录
     */
    public void datas() {
        renderJsonData(service.datas(getPageNumber(), getPageSize(), getKv()));
    }

    public void findByLogId() {
        Kv kv = getKv();
        String logid = kv.getStr("logid");
        Record byLogId = service.findByLogId(logid);
        set("bill", byLogId);
        render("edit().html");
    }

    /**
     * 合并条码详情列表页
     */
    public void formdatas() {
        Kv kv = getKv();
        String logno = kv.getStr("logno");
        renderJsonData(service.formdatas(logno));
    }

}
