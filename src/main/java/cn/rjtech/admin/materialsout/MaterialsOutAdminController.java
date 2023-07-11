package cn.rjtech.admin.materialsout;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MaterialsOut;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;

/**
 * 出库管理-材料出库单列表 Controller
 *
 * @ClassName: MaterialsOutAdminController
 * @author: RJ
 * @date: 2023-05-13 16:16
 */
@CheckPermission(PermissionKey.MATERIAL_DELIVERY_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/materialDeliveryList", viewPath = "/_view/admin/materialsout")
public class MaterialsOutAdminController extends BaseAdminController {

    @Inject
    private MaterialsOutService service;

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
        Kv kv = new Kv();
        kv.setIfNotNull("selectparam", get("billno"));
        kv.setIfNotNull("selectparam", get("sourcebillno"));
        kv.setIfNotNull("selectparam", get("whname"));
        kv.setIfNotNull("selectparam", get("deptname"));
        kv.setIfNotNull("iorderstatus", get("iorderstatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.MATERIAL_DELIVERY_LIST_ADD)
    public void add() {
        MaterialsOut materialsOut = new MaterialsOut();
        Date nowDate = new Date();
        materialsOut.setBillDate(nowDate);
        set("materialsOut", materialsOut);
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.MATERIAL_DELIVERY_LIST_EDIT)
    public void edit() {
        MaterialsOut materialsOut = service.findById(getLong(0));
        if (materialsOut == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        ;
        Record MODetail = service.getrcvMODetailList(materialsOut.getAutoID());
        set("type", get("type"));
        set("readonly", get("readonly"));
        set("MODetail", MODetail);
        set("materialsOut", materialsOut);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(MaterialsOut.class, "materialsOut")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(MaterialsOut.class, "materialsOut")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.MATERIAL_DELIVERY_LIST_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.MATERIAL_DELIVERY_LIST_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * JBoltTable 可编辑表格整体提交 多表格
     */
    @CheckPermission(PermissionKey.MATERIAL_DELIVERY_LIST_EDIT)
    public void submitMulti() {
        renderJson(service.submitByJBoltTables(getJBoltTables()));
    }

    /**
     * 条码数据源
     */
    public void barcodeDatas() {
        Kv kv = new Kv();
        kv.setIfNotNull("barcodes", get("barcodes"));
        kv.setIfNotNull("detailHidden", get("detailHidden"));
        kv.setIfNotNull("q", get("q"));
        kv.setIfNotNull("orgCode", getOrgCode());

        renderJsonData(service.getBarcodeDatas(kv));
    }


    /**
     * 根据条码带出其他数据
     */
    public void barcode(@Para(value = "barcode") String barcode,
                        @Para(value = "autoid") Long autoid,
                        @Para(value = "detailHidden") String detailHidden) {
        ValidationUtils.notBlank(barcode, "请扫码");
        renderJsonData(service.barcode(Kv.by("barcode", barcode).set("autoid", autoid).set("detailHidden", detailHidden).set("orgCode", getOrgCode())));
    }

    /**
     * 材料出库单列表明细
     */
    public void getMaterialsOutLines() {
        String autoid = get("autoid");
        String OrgCode = getOrgCode();
        Kv kv = new Kv();
        kv.set("autoid", autoid == null ? "" : autoid);
        kv.set("OrgCode", OrgCode);
        renderJsonData(service.getMaterialsOutLines(kv));
    }

    /**
     * 生產工單的选择数据Dialog
     */
    public void chooseSupplierData() {
        render("modetail.html");
    }

    /**
     * 生產工單数据源
     */
    public void moDetailData() {
        Kv kv = new Kv();
        kv.setIfNotNull("monorow", get("monorow"));
        renderJsonData(service.moDetailData(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 收发类别数据源
     */
    public void RDStyle() {
        String OrgCode = getOrgCode();
        Kv kv = new Kv();
        kv.setIfNotNull("OrgCode", OrgCode);
        kv.setIfNotNull("bRdFlag", get("bRdFlag"));
        renderJsonData(service.getRDStyleDatas(kv));
    }


}
