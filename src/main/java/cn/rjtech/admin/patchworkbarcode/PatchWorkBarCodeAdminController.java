package cn.rjtech.admin.patchworkbarcode;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.wms.utils.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 补打条码 Controller
 *
 * @ClassName: PatchWorkBarCodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 */
@CheckPermission(PermissionKey.PATCHWORKBARCODE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/patchworkBarcode", viewPath = "_view/admin/patchworkbarcode")
public class PatchWorkBarCodeAdminController extends BaseAdminController {

    @Inject
    private PatchWorkBarCodeService service;

    /**
     * index主页面
     */
    public void index() {
        render("index.html");
    }

    /**
     * 加载现品票信息
     */
    public void datas() {
        Kv kv = getKv();
        String cinvcode = kv.getStr("cinvcode");
        if (StringUtils.isBlank(cinvcode)) {
            renderJsonData(null);
            return;
        }
        renderJsonData(service.datas(cinvcode));
    }

    /*
     * 跳到选择页面
     * */
    public void barcodeSelect() {
        render("patchworkbarcode_select.html");
    }

    /*
     * 加载选择页面的数据
     * */
    public void selectDatas() {
        renderJsonData(service.selectDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 提交打印标签
     */
    public void SubmitForm() {
        Kv kv = getKv();
        renderJsonData(service.SubmitStripForm(kv));
    }
}
