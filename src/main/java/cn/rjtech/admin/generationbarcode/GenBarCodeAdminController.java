package cn.rjtech.admin.generationbarcode;

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

import java.math.BigDecimal;

/**
 * 生成条码 Controller
 *
 * @ClassName: GenBarCodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/generationBarcode", viewPath = "_view/admin/generationbarcode")
public class GenBarCodeAdminController extends BaseAdminController {
    @Inject
    GenBarCodeService service;

    /**
     * 合并条码记录
     */
    public void index() {
        render("index.html");
    }

    public void barcodeIndex() {
        render("stripbarcode_select.html");
    }

    public void StripSelectDatas() {
        BigDecimal zero = BigDecimal.ZERO;
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
  /*      BigDecimal qty = byLogId.getBigDecimal("qty");
        BigDecimal curqty = byLogId.getBigDecimal("curqty");
        BigDecimal hbqty = qty.subtract(curqty);
        byLogId.set("hbqty",hbqty);*/
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
