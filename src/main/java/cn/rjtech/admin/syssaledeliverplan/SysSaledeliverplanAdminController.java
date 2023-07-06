package cn.rjtech.admin.syssaledeliverplan;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.customeraddr.CustomerAddrService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.saletype.SaleTypeService;
import cn.rjtech.admin.settlestyle.SettleStyleService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;

import cn.rjtech.util.ValidationUtils;

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
@CheckPermission(PermissionKey.SYSSALEDELIVERPLAN)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/salesShipmentList", viewPath = "/_view/admin/syssaledeliverplan")
public class SysSaledeliverplanAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverplanService service;
    @Inject
    private DepartmentService         departmentservice;
    @Inject
    private CustomerAddrService       customeraddrservice;
    @Inject
    private SaleTypeService           saletypeservice;
    @Inject
    private SettleStyleService        settlestyleservice;
    @Inject
    private RdStyleService            rdstyleservice;
    @Inject
    private CustomerService           customerservice;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增-跳转到弹窗确认页面
     */
    public void addConfirm() {
        render("addConfirm.html");
    }

    /**
     * 跳转到订单页面
     */
    public void saleDeliverBillnoDialog() {
        Kv kv = getKv();
        Long icustomerid = kv.getLong("icustomerid");
        ValidationUtils.isTrue(icustomerid != null, "请先选择客户");
        set("icustomerid", icustomerid);
        render("saleDeliverBillnoDialog.html");
    }

    /*
     * 获取相关订单号
     * */
    public void saleDeliverBillNoList() {
        renderJsonData(service.getSaleDeliverBillNoList(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        Kv kv = getKv();
        Record record = new Record();
        record.set("icustomerid", kv.get("icustomerid"));//客户id
        record.set("customerccuscode", kv.get("customerccuscode"));//客户编码
        record.set("starttime", kv.get("starttime"));
        record.set("endtime", kv.get("endtime"));
        record.set("ccusabbname", kv.get("ccusabbname"));//客户简称
        record.set("sourcebillid", kv.get("sourcebillid"));//订单表主键id
        record.set("corderno", kv.get("corderno"));//订单号
        set("syssaledeliverplan", record);
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
        SysSaledeliverplan sysSaledeliverplan = service.findById(getLong("autoid"));
        if (sysSaledeliverplan == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("syssaledeliverplan", sysSaledeliverplan);
        // todo 待优化
        //查询业务类型
        //销售类型
        SaleType first = saletypeservice.findFirst("select * from Bd_SaleType where cSTCode =?", sysSaledeliverplan.getRdCode());
        if (null != first && null != first.getCSTName()) {
            set("cstname", first.getCSTName());
        }

        //客户简称
        Kv kv = new Kv();
        List<Record> corderno = service.getorder(kv.set("corderno", sysSaledeliverplan.getBillNo()));
        if (false == corderno.isEmpty() && null != corderno.get(0).getStr("cuname")) {
            set("cuname", corderno.get(0).getStr("cuname"));
        }
        //销售部门
        Department first1 = departmentservice.findFirst("select * from Bd_Department where cDepCode =?",
            sysSaledeliverplan.getDeptCode());
        set("cdepname", first1.getCDepName());
        //发货地址
        CustomerAddr first2 = customeraddrservice.findFirst("select * from Bd_CustomerAddr where cDistrictCode=?",
            sysSaledeliverplan.getShipAddress());
        if (null != first2 && null != first2.getCContactName()) {
            set("cdistrictname", first2.getCContactName());
        }
        //发运方式
        RdStyle first3 = rdstyleservice.findFirst("select * from Bd_Rd_Style where cRdCode=?", sysSaledeliverplan.getIssue());
        if (null != first3 && null != first3.getCRdName()) {
            set("crdname", first3.getCRdName());
        }
        //付款方式
        SettleStyle first4 = settlestyleservice.findFirst("select * from Bd_SettleStyle where cSSCode =?",
            sysSaledeliverplan.getCondition());
        if (null != first4 && null != first4.getCSSName()) {
            set("cssname", first4.getCSSName());
        }
        render("edit.html");
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
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

    /**
     * 获取销售类型下拉  /admin/saletype/selectData
     */
    public void saletype() {
        renderJsonData(service.saletype(getKv()));
    }

    /**
     * 获取销售部门下拉 Bd_Department   admin/productInList/getDepartment
     */
    public void department() {
        renderJsonData(service.department(getKv()));
    }

    /**
     * 获取地址下拉 Bd_CustomerAddr
     */
    public void customeraddr() {
        renderJsonData(service.customeraddr(getKv()));
    }

    /**
     * 获取发运方式下拉
     */
    public void rdstyle() {
        renderJsonData(service.rdstyle(getKv()));
    }

    /**
     * 获取付款条件下拉
     */
    public void settlestyle() {
        renderJsonData(service.settlestyle(getKv()));
    }

    /**
     * 获取币种下拉
     */
    public void foreigncurrency() {
        renderJsonData(service.foreigncurrency(getKv()));
    }

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

    /*
     * 根据barcode加载数据
     * */
    public void getBarcodeDatas() {
        List<Record> recordList = service.getBarcodeDatas(get("q"), getKv());
        renderJsonData(recordList);
    }

    /*
     * 根据invcode加载数据
     * */
    public void getDatasByInvcode() {
        List<Record> recordList = service.getDatasByInvcode(get("q"), getKv());
        renderJsonData(recordList);
    }
}
