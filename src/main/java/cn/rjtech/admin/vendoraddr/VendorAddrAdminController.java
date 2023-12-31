package cn.rjtech.admin.vendoraddr;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.VendorAddr;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import java.util.Date;

/**
 * 供应商档案-联系地址
 *
 * @ClassName: VendorAddrAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 17:59
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/vendoraddr", viewPath = "/_view/admin/vendoraddr")
public class VendorAddrAdminController extends BaseAdminController {

    @Inject
    private VendorAddrService service;

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
        renderJsonData(service.pageList(getKv()));
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
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
        renderJson(service.save(getModel(VendorAddr.class, "vendorAddr")));
    }

    /**
     * 编辑
     */
    public void edit() {
        VendorAddr vendorAddr = service.findById(getLong(0));
        if (vendorAddr == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("vendorAddr", vendorAddr);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(VendorAddr.class, "vendorAddr")));
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

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    @UnCheck
    public void list() {
        renderJsonData(service.list(getKv()));
    }

    public void updateEditTable() {
        renderJson(service.updateEditTable(getJBoltTable(), JBoltUserKit.getUserId(), new Date()));
    }

}
