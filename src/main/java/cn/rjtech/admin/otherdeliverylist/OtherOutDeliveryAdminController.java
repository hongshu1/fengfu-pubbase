package cn.rjtech.admin.otherdeliverylist;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.OtherOut;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

import java.util.Date;

/**
 * 出库管理-其他出库单列表 Controller
 *
 * @ClassName: OtherOutAdminController
 * @author: RJ
 * @date: 2023-05-17 09:35
 */
@CheckPermission(PermissionKey.OTHER_DELIVERY_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/otherdeliverylist", viewPath = "/_view/admin/otherdeliverylist")
public class OtherOutDeliveryAdminController extends BaseAdminController {

    @Inject
    private OtherOutDeliveryService service;

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
        kv.setIfNotNull("whname", get("whname"));
        kv.setIfNotNull("deptname", get("deptname"));
        kv.setIfNotNull("iorderstatus", get("iorderstatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));

    }


    /**
     * 其他出库单列表-明细
     */
    public void getOtherOutLines() {
        String autoid = get("autoid");
        Kv kv = new Kv();
        kv.set("autoid", autoid == null ? "null" : autoid);
        renderJsonData(service.getOtherOutLines(kv));

    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.OTHER_DELIVERY_LIST_ADD)
    public void add() {
        OtherOut otherOut = new OtherOut();
        Date nowDate = new Date();
        otherOut.setBillDate(nowDate);
        set("otherOut", otherOut);
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.OTHER_DELIVERY_LIST_EDIT)
    public void edit() {
        OtherOut otherOut = service.findById(getLong(0));
        if (otherOut == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("otherOut", otherOut);
        set("type", get("type"));
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(OtherOut.class, "otherOut")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(OtherOut.class, "otherOut")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.OTHER_DELIVERY_LIST_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.OTHER_DELIVERY_LIST_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * JBoltTable 可编辑表格整体提交 多表格
     */
    @CheckPermission(PermissionKey.OTHER_DELIVERY_LIST_EDIT)
    public void submitMulti() {
        renderJson(service.submitByJBoltTables(getJBoltTables()));
    }

    /**
     * 通过新增行获取条码列表
     */
    public void otherOutBarcodeDatas() {
        Kv kv = new Kv();
        kv.setIfNotNull("barcodes", get("barcodes"));
        kv.setIfNotNull("q", get("q"));
        kv.setIfNotNull("orgCode", getOrgCode());
        renderJsonData(service.otherOutBarcodeDatas(kv));
    }

    /**
     * 根据扫描现品票获取条码
     */
    @UnCheck
    public void barcode(@Para(value = "barcode") String barcode,
                        @Para(value = "autoid") Long autoid,
                        @Para(value = "detailHidden") String detailHidden) {
        ValidationUtils.notBlank(barcode, "请扫码");
        renderJsonData(service.barcode(Kv.by("barcode", barcode).set("autoid", autoid).set("detailHidden", detailHidden).set("orgCode", getOrgCode())));
    }

    /**
     * 获取项目大类目录数据
     * 通过关键字匹配
     */
    @UnCheck
    public void getCItemCCodeLines() {
        String orgCode = getOrgCode();
        renderJsonData(service.getCItemCCodeLines(get("q"), get("orgCode", orgCode)));
    }


    /**
     * 获取项目大类主目录数据
     * 通过关键字匹配
     * autocomplete组件使用
     */
    @UnCheck
    public void getItemCodeLines() {
        Kv kv = getKv();
        renderJsonData(service.getItemCodeLines(kv));
    }

}
