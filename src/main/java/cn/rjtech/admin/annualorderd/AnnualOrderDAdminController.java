package cn.rjtech.admin.annualorderd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.AnnualOrderD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 年度计划订单年汇总 Controller
 *
 * @ClassName: AnnualOrderDAdminController
 * @author: heming
 * @date: 2023-03-28 17:06
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/annualorderd", viewPath = "/_view/admin/annualorderd")
public class AnnualOrderDAdminController extends BaseAdminController {

    @Inject
    private AnnualOrderDService service;

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
        AnnualOrderD annualOrderD = service.findById(getLong(0));
        if (annualOrderD == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("annualOrderD", annualOrderD);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(AnnualOrderD.class, "annualOrderD")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(AnnualOrderD.class, "annualOrderD")));
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

    public void findEditTableDatas() {
        renderJsonData(service.findEditTableDatas(getKv()));
    }

}
