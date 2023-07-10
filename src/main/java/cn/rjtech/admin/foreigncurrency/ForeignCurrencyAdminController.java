package cn.rjtech.admin.foreigncurrency;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ForeignCurrency;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 币种档案 Controller
 *
 * @ClassName: ForeignCurrencyAdminController
 * @author: WYX
 * @date: 2023-03-20 21:09
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.FOREIGN_CURRENCY_INDEX)
@Path(value = "/admin/foreigncurrency", viewPath = "/_view/admin/foreigncurrency")
public class ForeignCurrencyAdminController extends BaseAdminController {

    @Inject
    private ForeignCurrencyService service;

    /**
     * 首页
     */
    public void index() {
    	List<JsTreeBean> list = service.findForeigncurrencyRootTressList();
    	if(CollUtil.isNotEmpty(list)) {
    		set("treedefaultselect",list.get(0).getId());
    	}
        render("foreigncurrency_exch.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
        ForeignCurrency foreignCurrency = service.findById(getLong(0));
        if (foreignCurrency == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("foreignCurrency", foreignCurrency);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(ForeignCurrency.class, "foreignCurrency")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ForeignCurrency.class, "foreignCurrency")));
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
     * 切换toggleBcal
     */
    @UnCheck
    public void toggleBcal() {
        renderJson(service.toggleBcal(getLong(0)));
    }
    /**
     * 切换toggleIotherused
     */
    @UnCheck
    public void toggleIotherused() {
        renderJson(service.toggleIotherused(getLong(0)));
    }
    /**
     * 切换toggleIsDeleted
     */
    @UnCheck
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }
    @UnCheck
    public void findForeigncurrencyRootTressList(){
    	renderJsonData(service.findForeigncurrencyRootTressList());
    }
    @UnCheck
    public void findFlatTableDatas(){
    	renderJsonData(service.findFlatTableDatas(getKv()));
    }
    @UnCheck
    public void findForeignCurrencyForEdit(){
    	renderJsonData(service.findModelByExchName(getKv()));
    }
    @CheckPermission(PermissionKey.FOREIGNCURRENCY_SUBMIT)
    public void saveOrUpdateForeignCurrency(){
    	renderJson(service.saveOrUpdateForeignCurrency(getKv()));
    }
    @UnCheck
    public void saveCellTable(){
    	renderJson(service.saveCellTable(getKv()));
    }
    @CheckPermission(PermissionKey.FOREIGNCURRENCY_DELETE)
    public void deleteForeignCurrencyAndExch(){
    	renderJson(service.deleteForeignCurrencyAndExch(getKv()));
    }
}
