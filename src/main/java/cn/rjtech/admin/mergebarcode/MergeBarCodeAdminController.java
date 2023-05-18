package cn.rjtech.admin.mergebarcode;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
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
        render("index.html");
    }

    /*
     * 跳转到新增页面
     * */
    public void stripbarcodeSelect() {
        render("stripbarcode_select.html");
    }

    /*
     * 新增页面自动加载table数据
     * */
    public void StripSelectDatas() {
        renderJsonData(service.StripSelectDatas(getPageNumber(), getPageSize(), getKv()));
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

    /*
     * 点击查看按钮，跳转到查看页面
     * */
    public void findByLogno() {
        Kv kv = getKv();
        Record byLogId = service.findByLogId(kv.getStr("logno"));
        if (null == byLogId) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("bill", byLogId);
        render("edit.html");
    }

    /*
     * 跳转到“查看”页面自动加载数据
     * */
    public void detailDatas() {
        Kv kv = getKv();
        renderJsonData(service.findShiWuByCSourceId(kv.getStr("csourceid")));
    }

}
