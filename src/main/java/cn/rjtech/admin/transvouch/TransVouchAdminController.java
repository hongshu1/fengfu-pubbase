package cn.rjtech.admin.transvouch;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.TransVouch;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

import java.util.Date;

/**
 * 出库管理-调拨单列表 Controller
 * @ClassName: TransVouchAdminController
 * @author: RJ
 * @date: 2023-05-11 14:54
 */
@CheckPermission(PermissionKey.TRANSVOUCH)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/transvouch", viewPath = "/_view/admin/transvouch")
public class TransVouchAdminController extends BaseAdminController {

    @Inject
    private TransVouchService service;

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
        Kv kv = new Kv();
        kv.setIfNotNull("selectparam", get("billno"));
        kv.setIfNotNull("selectparam", get("iwhcode"));
        kv.setIfNotNull("selectparam", get("owhcode"));
        kv.setIfNotNull("iorderstatus", get("iorderstatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));
    }


    /**
     * 调拨单列表明细
     */
    public void getTransVouchLines() {
        String autoid = get("autoid");
        Kv kv = new Kv();
        kv.set("autoid", autoid == null ? "null" : autoid);
        renderJsonData(service.getTransVouchLines(kv));

    }


    /**
     * 新增
     */
    @CheckPermission(PermissionKey.TRANSVOUCH_ADD)
    public void add() {
        TransVouch transVouch = new TransVouch();
        Date nowDate = new Date();
        transVouch.setBillDate(nowDate);
        set("transVouch", transVouch);
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.TRANSVOUCH_EDIT)
    public void edit() {
        TransVouch transVouch = service.findById(getLong(0));
        if (transVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("transVouch", transVouch);
        set("readonly", get("readonly"));
        set("type", get("type"));
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(TransVouch.class, "transVouch")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(TransVouch.class, "transVouch")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.TRANSVOUCH_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.TRANSVOUCH_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 拉取产线和产线编码
     */
    public void workRegionMList() {
        //列表排序
        String cus = get("q");
        Kv kv = new Kv();
        kv.set("cus", StringUtils.trim(cus));
        renderJsonData(service.workRegionMList(kv));

    }

    /**
     * JBoltTable 可编辑表格整体提交 多表格
     */
    @CheckPermission(PermissionKey.TRANSVOUCH_ADD)
    public void submitMulti() {
        renderJson(service.submitByJBoltTables(getJBoltTables()));
    }

    /**
     * 根据条码带出其他数据
     */
    @UnCheck
    public void barcode(
            @Para(value = "autoid") Long autoid,
            @Para(value = "detailHidden") String detailHidden) {
        renderJsonData(service.barcode(Kv.by("autoid", autoid).set("detailHidden", detailHidden)));
    }

    /**
     * 转出仓库
     */
    public void warehouse() {
        renderJsonData(service.warehouse(getKv()));
    }

    /**
     * 转入仓库
     */
    public void iwarehouse() {
        renderJsonData(service.iwarehouse(getKv()));
    }


}
