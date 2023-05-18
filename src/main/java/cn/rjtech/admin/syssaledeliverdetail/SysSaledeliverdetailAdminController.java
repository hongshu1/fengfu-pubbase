package cn.rjtech.admin.syssaledeliverdetail;

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
import cn.rjtech.model.momdata.SysSaledeliverdetail;

/**
 * 销售出库明细
 * @ClassName: SysSaledeliverdetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 13:06
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysSaledeliverdetail", viewPath = "/_view/admin/sysSaledeliverdetail")
public class SysSaledeliverdetailAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverdetailService service;

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
        renderJson(service.save(getModel(SysSaledeliverdetail.class, "sysSaledeliverdetail")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysSaledeliverdetail sysSaledeliverdetail = service.findById(getLong(0));
        if (sysSaledeliverdetail == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysSaledeliverdetail", sysSaledeliverdetail);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysSaledeliverdetail.class, "sysSaledeliverdetail")));
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
