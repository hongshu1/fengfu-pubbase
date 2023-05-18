package cn.rjtech.admin.syssaledeliverplandetail;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysSaledeliverplandetail;

/**
 * 销售出货(计划)明细
 *
 * @ClassName: SysSaledeliverplandetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 10:13
 */
@CheckPermission(PermissionKey.SALES_SHIPMENT_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysSaledeliverplandetail", viewPath = "/_view/admin/sysSaledeliverplandetail")
public class SysSaledeliverplandetailAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverplandetailService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("TrackType"), get("SourceBillType")));
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
        renderJson(service.save(getModel(SysSaledeliverplandetail.class, "sysSaledeliverplandetail")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysSaledeliverplandetail sysSaledeliverplandetail = service.findById(getLong(0));
        if (sysSaledeliverplandetail == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysSaledeliverplandetail", sysSaledeliverplandetail);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysSaledeliverplandetail.class, "sysSaledeliverplandetail")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    public void findEditTableDatas(){
        renderJsonData(service.findEditTableDatas(getKv()));
    }
}
