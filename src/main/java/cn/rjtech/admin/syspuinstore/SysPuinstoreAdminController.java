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
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.util.ValidationUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
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
        Record record = service.findEditAndOnlySeeByAutoid(sysPuinstore.getAutoID());
        String iBusTypeKey = service.findIBusTypeKey(String.valueOf(sysPuinstore.getIBusType()));
        set("ibustype", iBusTypeKey);//业务类型
        set("sysPuinstore", record);
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
        String iBusTypeKey = service.findIBusTypeKey(String.valueOf(sysPuinstore.getIBusType()));
        set("ibustype", iBusTypeKey);//业务类型
        set("sysPuinstore", record);
        render("onlysee.html");
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
    /*public void editAutit() {
        Kv kv = getKv();
        renderJson(service.editAutit(kv.getLong("autoid")));
    }*/

    /*
     * 查看页面的审批
     * */
    public void onlyseeAutit() {
        Kv kv = getKv();
        renderJson(service.onlyseeAutit(kv.getLong("autoid")));
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
    /*public void deleteByIds() {
        renderJson(service.deleteRmRdByIds(get("ids")));
    }*/

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteByAutoid(getLong(0)));
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

    /*
     * 批量审核通过
     * */
    public void batchApprove(@Para(value = "ids") String ids) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);

        renderJson(service.batchApprove(ids));
    }

    /*
     * 批量反审核
     * */
    public void batchReverseApprove(@Para(value = "ids") String ids) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);

        renderJson(service.batchReverseApprove(ids));
    }

    /*
     * 审核通过
     * */
    public void approve(long autoid) {
        renderJson(service.approve(getLong(0)));
    }

    /*
     * 审核不通过
     * */
    public void reject(long autoid) {
        renderJson(service.reject(getLong(0)));
    }

    /*
     * 撤回
     * */
    public void withdraw(Long iAutoId) {
        ValidationUtils.validateId(iAutoId, "iAutoId");

        renderJson(service.withdraw(iAutoId));
    }

    /*
     * 反审核
     * */
    public void reverseApprove(long autoid) {
        ValidationUtils.validateId(autoid, "autoid");

        renderJson(service.reverseApprove(autoid));
    }

    /*
     * 提交审核
     * */
    public void submit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "autoid");

        renderJson(service.submit(iautoid));
    }
}
