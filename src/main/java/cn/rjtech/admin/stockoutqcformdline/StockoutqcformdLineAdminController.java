package cn.rjtech.admin.stockoutqcformdline;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockoutqcformdLine;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 质量管理-出库检单行配置 Controller
 *
 * @ClassName: StockoutqcformdLineAdminController
 * @author: RJ
 * @date: 2023-04-25 16:28
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockoutqcformdline", viewPath = "/_view/admin/stockoutqcformdline")
public class StockoutqcformdLineAdminController extends BaseAdminController {

    @Inject
    private StockoutqcformdLineService service;

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
        StockoutqcformdLine stockoutqcformdLine = service.findById(getLong(0));
        if (stockoutqcformdLine == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockoutqcformdLine", stockoutqcformdLine);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(StockoutqcformdLine.class, "stockoutqcformdLine")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(StockoutqcformdLine.class, "stockoutqcformdLine")));
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


}
