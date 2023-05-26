package cn.rjtech.admin.warehousebeginofperiod;

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
 * 仓库期初 Controller
 *
 * @ClassName: GenBarCodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/warehousebeginofperiod", viewPath = "_view/admin/warehousebeginofperiod")
public class WarehouseBeginofPeriodAdminController extends BaseAdminController {

    @Inject
    WarehouseBeginofPeriodService service;

    public void index() {
        render("index.html");
    }

    /**
     * 新增期初库存
     */
    public void add() {
        render("add.html");
    }

    /*
     * 新增期初条码
     * */
    public void addBarcode() {
        render("addBarcode.html");
    }

    /*
     * 条码明细
     * */
    public void detail() {
        render("detail.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.datas(getPageNumber(), getPageSize(), getKv()));
    }

    /*
     * 保存
     * */
    public void save() {
        renderJson(service.save());
    }

    /*
     * 新增页加载的数据
     * */
    public void findDetails() {
        renderJsonData(null);
    }

    /*
     * 数据导入
     * */
    public void importForm() {
        renderJson("导入成功");
    }

    /*
     * 提交
     * */
    public void submitAll() {
        renderJsonData("提交成功");
    }
}
