package cn.rjtech.admin.syspuinstore;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.List;

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
    @UnCheck
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /*
     * 选择订单
     * */
    public void addConfirmForm(){
        render("addConfirmForm.html");
    }

    /**
     * 新增
     */
    public void add() {
        Record record = new Record();
        Kv kv = getKv();
        record.set("ordertype",kv.get("ordertype"));
        record.set("billno", JBoltSnowflakeKit.me.nextId());
        set("sysPuinstore", record);
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
        SysPuinstore sysPuinstore = service.findById(getLong("autoid"));
        if (sysPuinstore == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record record = service.findEditAndOnlySeeByAutoid(sysPuinstore.getAutoID());
        SysPuinstoredetail puinstoredetail = service.getSourceBillType(record.get("autoid"));
        record.set("sourcebilltype", puinstoredetail != null ? puinstoredetail.getSourceBillType() : "");
        set("sysPuinstore", record);
        keepPara();
        render("edit.html");
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
        Record record = service.findEditAndOnlySeeByAutoid(sysPuinstore.getAutoID());
        String iBusTypeKey = service.findIBusTypeKey(String.valueOf(sysPuinstore.getIBusType()), "purchase_business_type");
        SysPuinstoredetail puinstoredetail = service.getSourceBillType(record.get("autoid"));
        String order_type = service.findIBusTypeKey(puinstoredetail.getSourceBillType(), "order_type");
        record.set("sourcebilltype", order_type);
        set("ibustype", iBusTypeKey);//业务类型
        set("sysPuinstore", record);
        render("onlysee.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPuinstore.class, "sysPuinstore")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteByAutoId(getLong(0)));
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
        Kv kv = getKv();
        set("ordertype",kv.get("ordertype"));
        render("sysPuinstoreDialog.html");
    }

    /**
     * 获取采购订单视图MES的订单号
     */
    @UnCheck
    public void getMesSysPODetails() {
        Page<Record> recordPage = service.getMesSysPODetails(getKv(), getPageNumber(), getPageSize(), get("ordertype"));
        renderJsonData(recordPage);
    }

    /**
     * 详情表，新增行获取数据
     */
    @UnCheck
    public void getInsertPuinstoreDetail() {
        Kv kv = getKv();
        if (StrUtil.isBlank(kv.getStr("sourcebillno"))) {
            fail("订单号为空，请先选择订单号");
            return;
        }
        List<Record> recordList = service.getInsertPuinstoreDetail(getKv());
        renderJsonData(recordList);
    }

    /**
     * 获取仓库名
     */
    @UnCheck
    public void getWareHouseName() {
        renderJson(service.getWareHouseName(getKv()));
    }

    /**
     * 获取需要打印的模板数据
     */
    @UnCheck
    public void getPrintData() {
        renderJson(service.printData(getKv()));
    }

    /*
     * 扫描现品票
     * */
    public void scanBarcode(@Para(value = "barcode") String barcode, @Para(value = "orderType") String orderType) {
        Record record = service.scanBarcode(barcode, orderType);
        renderJsonData(record);
    }

    /*
     * 根据barcode加载数据
     * */
    public void getBarcodeDatas() {
        Kv kv = getKv();
        if (StrUtil.isBlank(kv.getStr("sourcebillno"))) {
            fail("订单号为空，请先选择订单号");
            return;
        }
        List<Record> recordList = service.getBarcodeDatas(get("q"),kv.getStr("ordertype"),kv.getStr("sourcebillno"));
        renderJsonData(recordList);
    }

}
