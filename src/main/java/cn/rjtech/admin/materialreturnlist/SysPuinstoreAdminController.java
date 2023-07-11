package cn.rjtech.admin.materialreturnlist;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
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

/**
 * 出库管理-物料退货列表 Controller
 *
 * @ClassName: SysPuinstoreAdminController
 * @author: RJ
 * @date: 2023-05-19 10:49
 */
@CheckPermission(PermissionKey.MATERIAL_RETURN_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/materialreturnlist", viewPath = "/_view/admin/materialreturnlist")
public class SysPuinstoreAdminController extends BaseAdminController {

    @Inject
    private SysPuinstoreListService service;

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
        kv.setIfNotNull("billno", get("billno"));
        kv.setIfNotNull("sourcebillno", get("sourcebillno"));
        kv.setIfNotNull("whcode", get("whcode"));
        kv.setIfNotNull("deptcode", get("deptcode"));
        kv.setIfNotNull("iorderstatus", get("iorderstatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.MATERIAL_RETURN_LIST_ADD)
    public void add() {
        SysPuinstore puinstore = new SysPuinstore();
//		Date nowDate = new Date();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String time = format.format(nowDate);
//		puinstore.setBillDate(time);
        set("puinstore", puinstore);
        String edit = get("edit");
        if ("2".equals(edit)) {
            render("edit2.html");
        } else {
            render("edit1.html");
        }

    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.MATERIAL_RETURN_LIST_EDIT)
    public void edit() {
        SysPuinstore puinstore = service.findById(getLong(0));
        if (puinstore == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        String autoid = puinstore.getAutoID();
        String OrgCode = getOrgCode();
        Record puinstoreName = service.getstockoutQcFormMList(autoid, OrgCode);
        set("type", get("type"));
        set("readonly", get("readonly"));
        set("puinstoreName", puinstoreName);
        set("puinstore", puinstore);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysPuinstore.class, "puinstore")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPuinstore.class, "puinstore")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.MATERIAL_RETURN_LIST_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.MATERIAL_RETURN_LIST_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * JBoltTable 可编辑表格整体提交 多表格
     */
    @CheckPermission(PermissionKey.MATERIAL_RETURN_LIST_EDIT)
    public void submitMulti() {
        renderJson(service.submitByJBoltTables(getJBoltTables()));
    }

    /**
     * 条码数据源
     */
    @UnCheck
    public void barcodeDatas() {
        Kv kv = new Kv();
        kv.setIfNotNull("autoid", get("autoid"));
        kv.setIfNotNull("barcodes", get("barcodes"));
        kv.setIfNotNull("q", get("q"));
        kv.setIfNotNull("limit", getInt("limit", 10));
        kv.setIfNotNull("orgCode", getOrgCode());

        renderJsonData(service.getBarcodeDatas(kv));
    }


    /**
     * 查看所以退货出库单列表明细
     */
    @UnCheck
    public void getmaterialReturnLists() {
        String autoid = get("autoid");
        String OrgCode = getOrgCode();
        Kv kv = new Kv();
        kv.set("autoid", autoid == null ? "" : autoid);
        kv.set("OrgCode", OrgCode);
        renderJsonData(service.getmaterialReturnLists(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 退货出库单列表明细
     */
    public void getmaterialReturnListLines() {
        String autoid = get("autoid");
        String OrgCode = getOrgCode();
        Kv kv = new Kv();
        kv.set("autoid", autoid == null ? "" : autoid);
        kv.set("OrgCode", OrgCode);
        renderJsonData(service.getmaterialReturnListLines(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 整单退货出库单列表明细
     */
    public void getmaterialLines() {
        String autoid = get("autoid");
        String OrgCode = getOrgCode();
        Kv kv = new Kv();
        kv.set("autoid", autoid == null ? "" : autoid);
        kv.set("OrgCode", OrgCode);
        renderJsonData(service.getmaterialLines(kv));
    }


    public void deleteList() {
        renderJson(service.deleteList(get(0)));
    }

    /**
     * 根据条码带出其他数据
     */
    @UnCheck
    public void barcode(@Para(value = "barcode") String barcode,
                        @Para(value = "autoid") Long autoid,
                        @Para(value = "detailHidden") String detailHidden) {
        ValidationUtils.notBlank(barcode, "请扫码");

        renderJsonData(service.barcode(Kv.by("barcode", barcode).set("autoid", autoid).set("detailHidden", detailHidden)));
    }


    /**
     * 入库号的选择数据Dialog
     */
    public void choosePuinstoreData() {
        render("sysPuinstoreDialog.html");
    }

    /**
     * 获取入库号明细
     */
    public void getSysPODetail() {
        Page<Record> recordPage = service.getSysPODetail(getKv(), getPageNumber(), getPageSize());
        renderJsonData(recordPage);
    }
}
