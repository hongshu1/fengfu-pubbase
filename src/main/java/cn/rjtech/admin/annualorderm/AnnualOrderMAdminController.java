package cn.rjtech.admin.annualorderm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.AnnualOrderM;
import cn.rjtech.model.momdata.Customer;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

/**
 * 年度计划订单
 *
 * @ClassName: AnnualOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 17:23
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.ANNUALORDERM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/annualorderm", viewPath = "/_view/admin/annualorderm")
public class AnnualOrderMAdminController extends BaseAdminController {

    @Inject
    private AnnualOrderMService service;
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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.ANNUALORDERM_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(AnnualOrderM.class, "annualOrderM")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.ANNUALORDERM_EDIT)
    public void edit() {
        AnnualOrderM annualOrderM = service.findById(get("iautoid"));
        if (annualOrderM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record annualOrderMRc = annualOrderM.toRecord();
        Customer customer = customerService.findById(annualOrderM.getICustomerId());
        annualOrderMRc.set("ccusname", customer == null ? null : customer.getCCusName());
        set("annualOrderM", annualOrderMRc);
        keepPara();
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(AnnualOrderM.class, "annualOrderM")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.ANNUALORDERM_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.ANNUALORDERM_DELETEBYIDS)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.ANNUALORDERM_SAVETABLESUBMIT)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 导入Excel数据
     */
    public void importExcel(@Para(value = "file") UploadFile file) {
        ValidationUtils.notNull(file, JBoltMsg.PARAM_ERROR);

        renderJson(service.importExcel(file.getFile()));
    }

    public void ccusname_dialog_index(){
        keepPara();
        render("ccusname_dialog_index.html");
    }

}
