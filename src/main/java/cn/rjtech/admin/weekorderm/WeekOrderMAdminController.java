package cn.rjtech.admin.weekorderm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.WeekOrderM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import org.apache.commons.lang3.StringUtils;

/**
 * 客户订单-周间客户订单
 *
 * @ClassName: WeekOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 14:37
 */
@CheckPermission(PermissionKey.WEEK_ORDERM)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/weekorderm", viewPath = "/_view/admin/weekorderm")
public class WeekOrderMAdminController extends BaseAdminController {

    @Inject
    private WeekOrderMService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        WeekOrderM addData = new WeekOrderM();
        set("weekOrderM", addData);
        set("mark", "ADD");
        render("add.html");
    }

    /**
     * 保存
     */
    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }

    /**
     * 编辑
     */
    public void edit() {
        WeekOrderM weekOrderM = service.findById(getLong(0));
        if (weekOrderM== null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("weekOrderM", weekOrderM);
        render("edit.html");
    }

    /**
     * 批量删除
     */
    public void deleteByIds(@Para(value = "ids") String ids) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        
        renderJson(service.deleteByIdS(ids));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 提审
     */
    public void submit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "id");

        renderJson(service.submit(iautoid));
    }

    /**
     * 撤回已提审
     */
    public void withdraw(Long iAutoId) {
        ValidationUtils.validateId(iAutoId, "iAutoId");

        renderJson(service.withdraw(iAutoId));
    }

    /**
     * 审批通过
     */
    public void approve(String ids) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        
        renderJson(service.approve(ids));
    }

    /**
     * 审批不通过
     */
    public void reject(String ids) {
        if (StringUtils.isEmpty(ids)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.reject(ids));
    }

    /**
     * 手动关闭
     */
    public void closeWeekOrder(String iAutoId) {
        if (StringUtils.isEmpty(iAutoId)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.closeWeekOrder(iAutoId));
    }

}
