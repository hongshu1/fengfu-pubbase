package cn.rjtech.admin.syssaledeliver;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysSaledeliver;

/**
 * 销售出库
 *
 * @ClassName: SysSaledeliverAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 13:05
 */
@CheckPermission(PermissionKey.SALES_DELIVERY_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/salesDeliveryList", viewPath = "/_view/admin/sysSaledeliver")
public class SysSaledeliverAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("SourceBillType"), get("BillType")));
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
        renderJson(service.save(getModel(SysSaledeliver.class, "sysSaledeliver")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysSaledeliver sysSaledeliver = service.findById(getLong(0));
        if (sysSaledeliver == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysSaledeliver", sysSaledeliver);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysSaledeliver.class, "sysSaledeliver")));
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


}
