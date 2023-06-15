package cn.rjtech.admin.weekorderd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.WeekOrderD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 客户订单-周间客户订单明细
 *
 * @ClassName: WeekOrderDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 16:23
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/weekorderd", viewPath = "/_view/admin/weekorderd")
public class WeekOrderDAdminController extends BaseAdminController {

    @Inject
    private WeekOrderDService service;

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
        renderJsonData(service.findData(getKv()));
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
        renderJson(service.save(getModel(WeekOrderD.class, "weekOrderD")));
    }

    /**
     * 编辑
     */
    public void edit() {
        WeekOrderD weekOrderD = service.findById(getLong(0));
        if (weekOrderD == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("weekOrderD", weekOrderD);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(WeekOrderD.class, "weekOrderD")));
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
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 保存调整计划时间
     */
    public void saveUpdateCplanTime()
    {
        renderJson(service.saveUpdateCplanTime(getJBoltTable()));
    }

}
