package cn.rjtech.admin.subcontractsaleorderm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Subcontractsaleorderm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 委外销售订单主表 Controller
 *
 * @ClassName: SubcontractsaleordermAdminController
 * @author: RJ
 * @date: 2023-04-12 18:57
 */
@CheckPermission(PermissionKey.SUBCONTRACTSALEORDERM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/subcontractsaleorderm", viewPath = "/_view/admin/subcontractsaleorderm")
public class SubcontractsaleordermAdminController extends BaseAdminController {

    @Inject
    private CustomerService customerService;
    @Inject
    private SubcontractsaleordermService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.SUBCONTRACTSALEORDERM_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.SUBCONTRACTSALEORDERM_EDIT)
    public void edit() {
        Page<Record> datas = service.paginateAdminDatas(1, 1, Kv.by("iautoid", getLong("iautoid")));
        ValidationUtils.notNull(datas, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(datas.getList().size() > 0, JBoltMsg.DATA_NOT_EXIST);
        set("subcontractsaleorderm", datas.getList().get(0));
        keepPara();
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Subcontractsaleorderm.class, "subcontractsaleorderm")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Subcontractsaleorderm.class, "subcontractsaleorderm")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.SUBCONTRACTSALEORDERM_DELETEBYIDS)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.SUBCONTRACTSALEORDERM_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    @CheckPermission(PermissionKey.SUBCONTRACTSALEORDERM_SAVETABLESUBMIT)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 关闭
     */
    public void close()
    {
        renderJson(service.close(get("iautoid")));
    }

    /**
     * 打开
     */
    public void open()
    {
        renderJson(service.open(get("iautoid")));
    }

    /**
     * 模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() {
        try {
            renderJxls("subcontractsaleorderm.xlsx", Kv.by("rows", null), "委外销售订单.xlsx");
        }catch (Exception e)
        {
            ValidationUtils.error( "模板下载失败");
        }
    }
}
