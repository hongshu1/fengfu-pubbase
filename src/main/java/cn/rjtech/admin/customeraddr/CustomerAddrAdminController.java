package cn.rjtech.admin.customeraddr;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CustomerAddr;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

/**
 * 客户档案-联系地址
 *
 * @ClassName: CustomerAddrAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 13:16
 */
@CheckPermission(PermissionKey.CUSTOMER)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/customeraddr", viewPath = "/_view/admin/customeraddr")
public class CustomerAddrAdminController extends BaseAdminController {

    @Inject
    private CustomerAddrService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    @UnCheck
    public void list(@Para(value = "icustomermid") Long icustomermid) {
//        ValidationUtils.validateId(icustomermid, "客户ID");
        icustomermid = isOk(icustomermid)?icustomermid : ' ';
        renderJsonData(service.list(icustomermid));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        CustomerAddr customerd = service.findById(getLong(0));
        if (customerd == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("customerd", customerd);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(CustomerAddr.class, "customerd")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CustomerAddr.class, "customerd")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 切换toggleIsenabled
     */
    public void toggleIsenabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }

    /**
     *  仓库试图数据源
     */
    public void wareHouse() {
        String OrgCode = getOrgCode();
        Kv kv = new Kv();
        kv.setIfNotNull("orgCode", OrgCode);
        kv.setIfNotNull("q", get("q"));
        renderJsonData(service.getwareHouseDatas(kv));
    }


}
