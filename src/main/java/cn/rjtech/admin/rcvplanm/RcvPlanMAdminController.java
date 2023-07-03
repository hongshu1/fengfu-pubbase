package cn.rjtech.admin.rcvplanm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Customer;
import cn.rjtech.model.momdata.RcvPlanM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 发货管理-取货计划主表
 *
 * @ClassName: RcvPlanMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-27 14:09
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/pickupPlanManage", viewPath = "/_view/admin/rcvplanm")
public class RcvPlanMAdminController extends BaseAdminController {

    @Inject
    private RcvPlanMService service;
    @Inject
    private RcvPlanDService planDService;
    @Inject
    private CustomerService customerService;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
//		renderJson(service.save(getModel(RcvPlanM.class, "rcvPlanM"),
//				getModel(RcvPlanD.class, "rcvPlanD") ));
    }

    /**
     * 编辑
     */
    public void edit() {
        RcvPlanM rcvPlanM = service.findById(getLong(0));
        if (rcvPlanM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record rcvplanm = rcvPlanM.toRecord();
        Customer customer = customerService.findById(rcvPlanM.getICustomerId());
        rcvplanm.set("ccusname", customer == null ? null : customer.getCCusName());
        set("rcvplanm", rcvplanm);
        render("edit.html");


    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(RcvPlanM.class, "rcvPlanM")));
    }

    /**
     * 批量删除主从表
     */
    public void deleteByIds() {

        renderJson(service.deleteRmRdByIds(get("ids")));
    }

    /**
     * 删除主从表
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


    /**
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }


}
