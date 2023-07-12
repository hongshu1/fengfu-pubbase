package cn.rjtech.admin.rcvpland;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RcvPlanD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 出货管理-取货计划明细
 *
 * @ClassName: RcvPlanDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-27 14:25
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rcvpland", viewPath = "/_view/admin/rcvpland")
public class RcvPlanDAdminController extends BaseAdminController {

    @Inject
    private RcvPlanDService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
        renderJson(service.save(getModel(RcvPlanD.class, "rcvPlanD")));
    }

    /**
     * 编辑
     */
    public void edit() {
        RcvPlanD rcvPlanD = service.findById(getLong(0));
        if (rcvPlanD == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("rcvPlanD", rcvPlanD);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(RcvPlanD.class, "rcvPlanD")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIdsRm(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {

        renderJson(service.delete(getLong(0)));
    }

    @UnCheck
    public void findEditTableDatas() {
        renderJsonData(service.findEditTableDatas(getKv()));
    }

}
