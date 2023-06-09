package cn.rjtech.admin.syspuinstore;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.PurchaseType;
import cn.rjtech.model.momdata.RdStyle;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.Vendor;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 采购入库单
 *
 * @ClassName: SysPuinstoreAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:38
 */
@CheckPermission(PermissionKey.PURCHASERECEIPTLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseReceiptList", viewPath = "/_view/admin/sysPuinstore")
public class SysPuinstoreAdminController extends BaseAdminController {

    @Inject
    private SysPuinstoreService service;
    @Inject
    private VendorService       vendorService;
    @Inject
    private DepartmentService   departmentService;
    @Inject
    private RdStyleService      rdStyleService;
    @Inject
    private PurchaseTypeService purchaseTypeService;//采购类别

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
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysPuinstore.class, "sysPuinstore")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysPuinstore sysPuinstore = service.findById(getLong(0));
        if (sysPuinstore == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Vendor vendor = vendorService.findByCode(sysPuinstore.getVenCode());
        if (vendor != null) {
            set("cvenname", vendor.getCVenName());
        }
        Department department = departmentService.findByCdepcode(getOrgId(), sysPuinstore.getDeptCode());
        if (department != null) {
            set("cdepname", department.getCDepName());
        }
        RdStyle rdStyle = rdStyleService.findBycSTCode(sysPuinstore.getRdCode());
        if (rdStyle != null) {
            set("crdname", rdStyle.getCRdName());
        }
        set("sysPuinstore", sysPuinstore);
        render("edit.html");
    }

    /*
     * 撤回
     * */
    public void backStep() {
        SysPuinstore puinstore = service.findById(getLong(0));
        if (puinstore == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        renderJson(service.backStep(puinstore));
    }

    /*
     * 批量审批
     * */
    public void autitByIds() {
        renderJson(service.autitByIds(get("ids")));
    }

    /*
     * 批量反审批
     * */
    public void resetAutitByIds() {
        renderJson(service.resetAutitByIds(get("ids")));
    }

    /*
     * 反审批
     * */
    public void resetAutitById() {
        Kv kv = getKv();
        renderJson(service.resetAutitById(kv.getStr("autoid")));
    }

    /*
     * 编辑页面的审批
     * */
    public void editAutit() {
        Kv kv = getKv();
        Long aLong = getLong(0);
        Long autoid = null;
        if (kv.getLong("autoid") != null) {
            autoid = kv.getLong("autoid");
        } else if (aLong != null) {
            autoid = aLong;
        }
        renderJson(service.editAutit(autoid));
    }

    /*
     * 查看页面的审批
     * */
    public void onlyseeAutit() {
        Kv kv = getKv();
        Long aLong = getLong(0);
        Long autoid = null;
        if (kv.getLong("autoid") != null) {
            autoid = kv.getLong("autoid");
        } else if (aLong != null) {
            autoid = aLong;
        }
        renderJson(service.onlyseeAutit(autoid));
    }

    /*
     * 查看
     * */
    public void onlysee() {
        SysPuinstore sysPuinstore = service.findById(getLong(0));
        if (sysPuinstore == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Vendor vendor = vendorService.findByCode(sysPuinstore.getVenCode());
        if (vendor != null) {
            set("cvenname", vendor.getCVenName());
        }
        Department department = departmentService.findByCdepcode(getOrgId(), sysPuinstore.getDeptCode());
        if (department != null) {
            set("cdepname", department.getCDepName());
        }
        RdStyle rdStyle = rdStyleService.findBycSTCode(sysPuinstore.getRdCode());
        if (rdStyle != null) {
            set("crdname", rdStyle.getCRdName());
        }
        PurchaseType purchaseType = purchaseTypeService.findById(sysPuinstore.getBillType());
        if (purchaseType != null){
            set("cptcode",purchaseType.getCPTCode());
            set("cptname",purchaseType.getCPTName());
        }
        set("sysPuinstore", sysPuinstore);
        render("onlysee.html");
    }

    /*
     * 打印
     * */
    public void printSysPuinstore() {
        //TODO 待定
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPuinstore.class, "sysPuinstore")));
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
     * 切换IsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "IsDeleted"));
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 订单号的选择数据Dialog
     */
    public void chooseSysPuinstoreData() {
        render("sysPuinstoreDialog.html");
    }

    /*
     * 获取采购订单视图的订单号
     * */
    public void getSysPODetail() {
        Page<Record> recordPage = service.getSysPODetail(getKv(), getPageNumber(), getPageSize());
        renderJsonData(recordPage);
    }

    /*
     * 获取采购订单视图MES的订单号
     * */
    public void getMesSysPODetails() {
        Page<Record> recordPage = service.getMesSysPODetails(getKv(), getPageNumber(), getPageSize());
        renderJsonData(recordPage);
    }

    /*
     * 获取仓库名
     * */
    public void getWareHouseName() {
        renderJson(service.getWareHouseName(getKv()));
    }

    /*
     * 获取需要打印的模板数据
     * */
    public void getPrintData() {
        renderJson(service.printData(getKv()));
    }
}
