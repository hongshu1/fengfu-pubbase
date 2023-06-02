package cn.rjtech.admin.syspureceive;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.sysenumeration.SysEnumerationService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 采购收料单
 * @ClassName: SysPureceiveAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
@CheckPermission(PermissionKey.MATERIALRECEIPTLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/materialReceiptList", viewPath = "/_view/admin/sysPureceive")
public class SysPureceiveAdminController extends BaseAdminController {

    @Inject
    private SysPureceiveService service;
    @Inject
    private UserService userService;
    @Inject
    private SysPureceivedetailService syspureceivedetailservice;

    @Inject
    private SysEnumerationService sysenumerationservice;

    @Inject
    private VendorService vendorservice;

    @Inject
    private WarehouseService warehouseservice;

    @Inject
    private PurchaseTypeService purchasetypeservice;


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
        renderJson(service.save(getModel(SysPureceive.class, "sysPureceive")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysPureceive sysPureceive = service.findById(getLong(0));
        if (sysPureceive == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        // 关联查询出仓库编码,然后换数据源U8 查其名称
        SysPureceivedetail first = syspureceivedetailservice.findFirst("select * from  T_Sys_PUReceiveDetail where MasID = ?", sysPureceive.getAutoID());
        if (first != null && null != first.getWhcode()) {
            Warehouse first1 = warehouseservice.findFirst("select *   from Bd_Warehouse where cWhCode=?", first.getWhcode());
            set("whname", first1.getCWhName());
        }
        // 关联查询用户名字
        if (null != sysPureceive.getCreatePerson()) {
            set("username", sysenumerationservice.getUserName(sysPureceive.getCreatePerson()));
        }
        // 查供应商名称
        if (null != sysPureceive.getVenCode()) {
            Vendor first1 = vendorservice.findFirst("select * from Bd_Vendor where cVenCode = ?", sysPureceive.getVenCode());
            set("venname", first1.getCVenName());
        }
        //查询入库类别
        if (null != sysPureceive.getRdCode()) {
            PurchaseType first1 = purchasetypeservice.findFirst("select * from Bd_PurchaseType where iRdStyleId = ?", sysPureceive.getRdCode());
            set("cptname", first1.getCPTName());
        }


        set("sysPureceive", sysPureceive);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPureceive.class, "sysPureceive")));
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
     * 供应商数据源
     */
    public void venCode() {
        renderJsonData(service.getVenCodeDatas(getKv()));
    }


    /**
     * 仓库数据源
     */
    public void Whcode() {
        renderJsonData(service.getWhcodeDatas(getKv()));
    }

    /**
     * 入库类别
     */
    public void selectRdCode() {
        renderJsonData(service.selectRdCode(getKv()));
    }


    /**
     * 库区数据源
     */
    public void wareHousepos() {
        String whcode = get("whcode1");
        Kv kv = getKv();
        if(null != whcode && !"".equals(whcode)){
            Warehouse first1 = warehouseservice.findFirst("select *   from Bd_Warehouse where cWhCode=?", whcode);
            kv.set("whcodeid",first1.getIAutoId());
        }
        renderJsonData(service.getwareHousepos(kv));
    }


    /**
     * 条码数据源
     */
    @UnCheck
    public void barcodeDatas() {
        String orgCode =  getOrgCode();
        String vencode1 = get("vencode1");
        Vendor first1 = vendorservice.findFirst("select * from Bd_Vendor where cVenCode = ?", vencode1);
        if(null == first1){
            ValidationUtils.assertNull(false, "请选择供应商");
            return;
        }
        String s = String.valueOf(first1.getIAutoId());
        renderJsonData(service.getBarcodeDatas(get("q"), getInt("limit",10),get("orgCode",orgCode),s));
    }

    /**
     * 根据条码带出其他数据
     */
    @UnCheck
    public void barcode() {
        String barcode = get("barcode");
        if(null == barcode){
            ValidationUtils.assertNull(false, "请扫码");
            return;
        }
        Kv kv = new Kv();
        kv.set("barcode",barcode);
        renderJsonData(service.barcode(kv));
    }
}
