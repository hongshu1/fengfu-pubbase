package cn.rjtech.admin.syssaledeliverplan;

import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.customeraddr.CustomerAddrService;
import cn.rjtech.admin.customerclass.CustomerClassService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.foreigncurrency.ForeignCurrencyService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.saletype.SaleTypeService;
import cn.rjtech.admin.settlestyle.SettleStyleService;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysSaledeliverplan;

/**
 * 销售出货(计划)
 * @ClassName: SysSaledeliverplanAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 10:01
 */
@CheckPermission(PermissionKey.SALES_SHIPMENT_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/salesShipmentList", viewPath = "/_view/admin/sysSaledeliverplan")
public class SysSaledeliverplanAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverplanService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("SourceBillType"), get("BillType")));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysSaledeliverplan.class, "sysSaledeliverplan")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysSaledeliverplan sysSaledeliverplan = service.findById(getLong(0));
        if (sysSaledeliverplan == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysSaledeliverplan", sysSaledeliverplan);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysSaledeliverplan.class, "sysSaledeliverplan")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }



    @Inject
    private SaleTypeService saletypeservice;
    /**
     * 获取销售类型下拉  /admin/saletype/selectData
     */

    @Inject
    private DepartmentService departmentservice;
    /**
     * 获取销售部门下拉 Bd_Department   admin/productInList/getDepartment
     */

    @Inject
    private CustomerAddrService customeraddrservice;
    /**
     * /admin/customeraddr/list/{icustomermid}
     * 获取地址下拉 Bd_CustomerAddr
     */

    @Inject
    private RdStyleService rdstyleservice;
    /**
     * 获取发运方式下拉
     */

    @Inject
    private SettleStyleService settlestyleservice;
    /**
     * 获取付款条件下拉
     */

    @Inject
    private ForeignCurrencyService foreigncurrencyservice;
    /**
     * 获取币种下拉
     */

    @Inject
    private CustomerService customerservice;
    /**
     * 获取客户下拉 Bd_Customer
     */


    /**
     * 获取订单号下拉 与 现品票一致
     */

}
