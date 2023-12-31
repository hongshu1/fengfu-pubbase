package cn.rjtech.admin.annualorderdqty;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.AnnualorderdQty;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 年度计划订单年月数量 Controller
 *
 * @ClassName: AnnualorderdQtyAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 16:29
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/annualorderdqty", viewPath = "/_view/admin/annualorderdqty")
public class AnnualorderdQtyAdminController extends BaseAdminController {

    @Inject
    private AnnualorderdQtyService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
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
        AnnualorderdQty annualorderdQty = service.findById(getLong(0));
        if (annualorderdQty == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("annualorderdQty", annualorderdQty);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(AnnualorderdQty.class, "annualorderdQty")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(AnnualorderdQty.class, "annualorderdQty")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }


}
