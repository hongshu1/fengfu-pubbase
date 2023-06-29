package cn.rjtech.wms.print.printmachine;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.controller.BaseMesAdminController;
import cn.rjtech.common.model.Printmachine;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 打印机器 Controller
 *
 * @ClassName: PrintmachineAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-24 14:23
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/printmachine", viewPath = "/_view/admin/printmachine")
public class PrintmachineAdminController extends BaseMesAdminController {

    @Inject
    private PrintmachineService service;

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
        Printmachine printmachine = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(printmachine, JBoltMsg.DATA_NOT_EXIST);

        set("printmachine", printmachine);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Printmachine.class, "printmachine")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Printmachine.class, "printmachine")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

}
