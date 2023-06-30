package cn.rjtech.admin.splitbarcode;

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
 * 拆分条码 Controller
 *
 * @ClassName: SplitBarCodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-31 14:38
 */
@CheckPermission(PermissionKey.SPLIT_BARCODE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/splitBarcode", viewPath = "_view/admin/splitbarcode")
public class SplitBarCodeAdminController extends BaseAdminController {

    @Inject
    private SplitBarCodeService service;

    /**
     * 拆分条码记录
     */
    public void index() {
        render("index.html");
    }

    public void datas(){
        renderJsonData(null);
    }


    public void barcodeSelect() {
        render("barcode_select.html");
    }

    /**
     * 实物编码选择数据
     */
    public void barcodeSelectDatas() {
        renderJsonData(service.BarCodeSelectDatas(getPageNumber(),getPageSize(),getKv()));
    }


    public void SubmitForm() {
        renderJsonData(service.SubmitForm(getKv()));

    }

    /*
     * 查询拆分条码记录
     * */
    public void barcodeDatas() {
        renderJsonData(service.barcodeDatas(getKv()));
    }

    public void edit() {
        Kv kv = getKv();
        Record byShiWu = service.findByShiWu(kv.getStr("logno"));
        if (byShiWu == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("bill", byShiWu);
        render("edit.html");
    }

    public void detailDatas() {
        Kv kv = getKv();
        renderJsonData(service.findListByCsourceid(kv.getStr("csourceid")));
    }
}
