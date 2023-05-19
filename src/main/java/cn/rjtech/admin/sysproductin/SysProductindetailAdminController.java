package cn.rjtech.admin.sysproductin;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysProductindetail;

/**
 * 产成品入库单明细
 * @ClassName: SysProductindetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 09:58
 */
@CheckPermission(PermissionKey.PRODUCTINLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysProductindetail", viewPath = "/_view/admin/sysProductindetail")
public class SysProductindetailAdminController extends BaseAdminController {

    @Inject
    private SysProductindetailService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("TrackType"), get("SourceBillType"), getBoolean("IsDeleted")));
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
        renderJson(service.save(getModel(SysProductindetail.class, "sysProductindetail")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysProductindetail sysProductindetail = service.findById(getLong(0));
        if (sysProductindetail == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysProductindetail", sysProductindetail);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysProductindetail.class, "sysProductindetail")));
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
}
