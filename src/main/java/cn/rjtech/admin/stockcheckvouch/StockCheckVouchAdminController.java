package cn.rjtech.admin.stockcheckvouch;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouch;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 盘点单
 *
 * @ClassName: StockCheckVouchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-21 10:47
 */
@CheckPermission(PermissionKey.STOCKCHECKVOUCH)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockcheckvouch", viewPath = "/_view/admin/stockcheckvouch")
public class StockCheckVouchAdminController extends BaseAdminController {

    @Inject
    private StockCheckVouchService service;

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
//        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("CheckType")));
        renderJsonData(service.pageList(getKv()));
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
        renderJson(service.save(getModel(StockCheckVouch.class, "stockCheckVouch")));
    }

    /*
     * 保存整体表单
     * */
    public void saveSubmit() {
        renderJson(service.saveSubmit(getKv()));
    }

    /*
     * 新增
     * */
    public void addFormSave() {
        renderJson(service.addFormSave(getKv()));
    }

    /**
     * 编辑
     */
    public void edit() {
        StockCheckVouch stockCheckVouch = service.findById(getLong(0));
        if (stockCheckVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockCheckVouch", stockCheckVouch);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(StockCheckVouch.class, "stockCheckVouch")));
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

    /*
     * 继续盘点
     * */
    public void keepCheckVouch() {
        StockCheckVouch stockCheckVouch = service.findById(getLong(0));
        if (stockCheckVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockcheckvouch", stockCheckVouch);
        render("stockEdit.html");
    }

    /*
     * 加载继续盘点页面的数据
     * */
    public void keepCheckVouchDatas() {
        renderJsonData(service.keepCheckVouchDatas());
    }

    /*
     * 继续盘点
     * */
    public void onlysee() {
        StockCheckVouch stockCheckVouch = service.findById(getLong(0));
        if (stockCheckVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockcheckvouch", stockCheckVouch);
        render("onlysee.html");
    }

    /*
     * 导出选中
     * */
    public void exportExcelByIds() {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
    }

    /*
     * 导出全部
     * */
    public void exportExcelAll() {

    }

}
