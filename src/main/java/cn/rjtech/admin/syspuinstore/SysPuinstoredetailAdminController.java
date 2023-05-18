package cn.rjtech.admin.syspuinstore;

import com.jfinal.aop.Inject;

import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;

import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysPuinstoredetail;

/**
 * 采购入库单明细
 *
 * @ClassName: SysPuinstoredetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:39
 */
@CheckPermission(PermissionKey.PURCHASERECEIPTLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysPuinstoredetail", viewPath = "/_view/admin/sysPuinstoredetail")
public class SysPuinstoredetailAdminController extends BaseAdminController {

    @Inject
    private SysPuinstoredetailService service;

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
        renderJsonData(service
            .getAdminDatas(getPageNumber(), getPageSize(), get("SourceBillType"), get("TrackType"), getBoolean("IsDeleted")));
    }

    /**
     * 新增
     */
    public void add() {
        render("add().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysPuinstoredetail.class, "sysPuinstoredetail")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysPuinstoredetail sysPuinstoredetail = service.findById(getLong(0));
        if (sysPuinstoredetail == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysPuinstoredetail", sysPuinstoredetail);
        render("edit().html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPuinstoredetail.class, "sysPuinstoredetail")));
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

    public void findEditTableDatas() {
        renderJsonData(service.findEditTableDatas(getKv()));
    }

    public void finddetaildatas() {
        Page<Record> recordPage = service.pageDetailList(getKv());
        renderJsonData(recordPage);
    }


}
