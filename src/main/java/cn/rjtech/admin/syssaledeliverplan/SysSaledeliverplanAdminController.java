package cn.rjtech.admin.syssaledeliverplan;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.customeraddr.CustomerAddrService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.foreigncurrency.ForeignCurrencyService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.saletype.SaleTypeService;
import cn.rjtech.admin.settlestyle.SettleStyleService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.List;


/**
 * 销售出货(计划)
 *
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
        renderJsonData(service.getAdminDatas(getKv()));
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
        set("syssaledeliverplan", sysSaledeliverplan);
        // todo 待优化
        //查询业务类型
        //销售类型
        SaleType first = saletypeservice.findFirst("select * from Bd_SaleType where cSTCode =?", sysSaledeliverplan.getRdCode());
        if(null != first && null !=first.getCSTName()){     set("cstname",first.getCSTName());}

        //客户简称
        Kv kv = new Kv();
        List<Record> corderno = service.getorder(kv.set("corderno", sysSaledeliverplan.getBillNo()));
        if(false==corderno.isEmpty() && null != corderno.get(0).getStr("cuname")){
            set("cuname",corderno.get(0).getStr("cuname"));
        }
        //销售部门
        Department first1 = departmentservice.findFirst("select * from Bd_Department where cDepCode =?", sysSaledeliverplan.getDeptCode());
        set("cdepname",first1.getCDepName());
        //发货地址
        CustomerAddr first2 = customeraddrservice.findFirst("select * from Bd_CustomerAddr where cDistrictCode=?", sysSaledeliverplan.getShipAddress());
        if(null != first2 && null !=first2.getCContactName()){ set("cdistrictname",first2.getCContactName());}
        //发运方式
        RdStyle first3 = rdstyleservice.findFirst("select * from Bd_Rd_Style where cRdCode=?", sysSaledeliverplan.getIssue());
        if(null != first3 && null !=first3.getCRdName()){ set("crdname",first3.getCRdName());}
        //付款方式
        SettleStyle first4 = settlestyleservice.findFirst("select * from Bd_SettleStyle where cSSCode =?", sysSaledeliverplan.getCondition());
        if(null != first4 && null !=first4.getCSSName()){ set("cssname",first4.getCSSName());}
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
        renderJson(service.deleteRmRdByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }


    @Inject
    private SaleTypeService saletypeservice;

    /**
     * 获取销售类型下拉  /admin/saletype/selectData
     */
    public void saletype() {
        renderJsonData(service.saletype(getKv()));
    }


    @Inject
    private DepartmentService departmentservice;

    /**
     * 获取销售部门下拉 Bd_Department   admin/productInList/getDepartment
     */
    public void department() {
        renderJsonData(service.department(getKv()));
    }


    @Inject
    private CustomerAddrService customeraddrservice;

    /**
     * /admin/customeraddr/list/{icustomermid}
     * 获取地址下拉 Bd_CustomerAddr
     */
    public void customeraddr() {
        renderJsonData(service.customeraddr(getKv()));
//        String icustomerid = get("icustomerid");
//        if (null == icustomerid || "".equals(icustomerid)) {
//            renderJsonData(customeraddrservice.findAll());
//        } else {
//            renderJsonData(customeraddrservice.find("select * from Bd_CustomerAddr where iCustomerId=?", icustomerid));
//        }
    }


    @Inject
    private RdStyleService rdstyleservice;

    /**
     * 获取发运方式下拉
     */
    public void rdstyle() {
        renderJsonData(service.rdstyle(getKv()));
//        renderJsonData(rdstyleservice.find("select * from Bd_Rd_Style where bRdFlag = '0'"));
    }


    @Inject
    private SettleStyleService settlestyleservice;

    /**
     * 获取付款条件下拉
     */
    public void settlestyle() {
        renderJsonData(service.settlestyle(getKv()));
//        renderJsonData(settlestyleservice.findAll());
    }


    @Inject
    private ForeignCurrencyService foreigncurrencyservice;

    /**
     * 获取币种下拉
     */
    public void foreigncurrency() {
        renderJsonData(service.foreigncurrency(getKv()));
//        renderJsonData(foreigncurrencyservice.findAll());
    }

    @Inject
    private CustomerService customerservice;

    /**
     * 获取客户下拉 Bd_Customer
     */
    public void customer() {
        renderJsonData(customerservice.findAll());
    }



    /**
     * 获取订单号跟客户下拉  委派销售订单  Co_SubcontractSaleOrderM
     * 根据客户，订单号 日期 查询
     */
    public void order() {
        renderJsonData(service.getorder(getKv()));
    }

    /**
     * 生生产订单号
     */
    public void orderData() {
        render("modetail.html");
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }
}
