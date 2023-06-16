package cn.rjtech.admin.formapproval;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormApproval;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 表单审批流 Controller
 *
 * @ClassName: FormApprovalAdminController
 * @author: RJ
 * @date: 2023-04-18 17:26
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formapproval", viewPath = "/_view/admin/formapproval")
public class FormApprovalAdminController extends BaseAdminController {

    @Inject
    private FormApprovalService service;

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
        FormApproval formApproval = service.findById(getLong(0));
        if (formApproval == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("formApproval", formApproval);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(FormApproval.class, "formApproval")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(FormApproval.class, "formApproval")));
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
     * 切换toggleIsSkippedOnDuplicate
     */
    public void toggleIsSkippedOnDuplicate() {
        renderJson(service.toggleIsSkippedOnDuplicate(getLong(0)));
    }

    /**
     * 切换toggleIsApprovedOnSame
     */
    public void toggleIsApprovedOnSame() {
        renderJson(service.toggleIsApprovedOnSame(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /**
     * 判断走审核还是审批
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过业务的类名
     */
    public void submit(@Para(value = "formSn") String formSn,
                       @Para(value = "formAutoId") Long formAutoId,
                       @Para(value = "primaryKeyName") String primaryKeyName,
                       @Para(value = "className") String className) {
        ValidationUtils.notBlank(formSn, "缺少表单编码");
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过业务的类名参数");

        renderJson(service.submit(formSn, formAutoId, primaryKeyName, className));
    }

    /**
     * 审批通过
     *
     * @param formAutoId     单据ID
     * @param formSn         表单编码
     * @param status         审批状态
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过业务的类名
     */
    public void approve(@Para(value = "formAutoId") Long formAutoId,
                        @Para(value = "formSn") String formSn,
                        @Para(value = "status") Integer status,
                        @Para(value = "primaryKeyName") String primaryKeyName,
                        @Para(value = "className") String className) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过业务的类名参数");

        renderJson(service.approve(formAutoId, formSn, status, primaryKeyName, className));
    }

    /**
     * 审批不通过
     *
     * @param formAutoId     单据ID
     * @param formSn         表单编码
     * @param status         审批状态
     * @param primaryKeyName 单据主键名称
     * @param className      处理审批的Service类名
     */
    public void reject(@Para(value = "formAutoId") Long formAutoId,
                       @Para(value = "formSn") String formSn,
                       @Para(value = "status") Integer status,
                       @Para(value = "primaryKeyName") String primaryKeyName,
                       @Para(value = "className") String className) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "处理审批的Service类名");

        renderJson(service.reject(formAutoId, formSn, status, primaryKeyName, className));
    }

    /**
     * 反审批
     *
     * @param formAutoId     单据ID
     * @param formSn         表单编码
     * @param status         审批状态
     * @param primaryKeyName 单据主键名称
     * @param className      处理审批的Service类名
     */
    public void reverseApprove(@Para(value = "formAutoId") Long formAutoId,
                               @Para(value = "formSn") String formSn,
                               @Para(value = "status") Integer status,
                               @Para(value = "primaryKeyName") String primaryKeyName,
                               @Para(value = "className") String className) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "处理审批的Service类名");

        renderJson(service.reverseApprove(formAutoId, formSn, primaryKeyName, status, className));
    }

    /**
     * 批量审批
     *
     * @param ids            单据IDs
     * @param formSn         表单
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过的业务类名
     */
    public void batchApprove(@Para(value = "ids") String ids,
                             @Para(value = "formSn") String formSn,
                             @Para(value = "primaryKeyName") String primaryKeyName,
                             @Para(value = "className") String className) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过后的业务类名");

        renderJson(service.batchApprove(ids, formSn, primaryKeyName, className));
    }

    /**
     * 批量审批不通过
     *
     * @param ids            单据IDs
     * @param formSn         表单
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过的业务类名
     */
    public void batchReject(@Para(value = "ids") String ids,
                            @Para(value = "formSn") String formSn,
                            @Para(value = "primaryKeyName") String primaryKeyName,
                            @Para(value = "className") String className) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过后的业务类名");

        renderJson(service.batchReject(ids, formSn, primaryKeyName, className));
    }

    /**
     * 批量反审批
     *
     * @param ids            单据ID
     * @param formSn         表名
     * @param primaryKeyName 单据主键名称
     */
    public void batchReverseApprove(@Para(value = "ids") String ids,
                                    @Para(value = "formSn") String formSn,
                                    @Para(value = "primaryKeyName") String primaryKeyName,
                                    @Para(value = "className") String className) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "处理审批的类名");

        renderJson(service.batchReverseApprove(ids, formSn, primaryKeyName, className));
    }

    /**
     * 判断审核还是审批
     */
    public void auditOrApprove(@Para(value = "formSn") String formSn) {
        ValidationUtils.notBlank(formSn, "表单编码不能为空");

        renderJson(service.auditOrApprove(formSn));
    }

    /**
     * 审批过程中待审批的人员
     */
    public void approvalProcessUsers(@Para(value = "formAutoId") Long formAutoId,
                                     @Para(value = "size", defaultValue = "5") Integer size) {
        ValidationUtils.validateId(formAutoId, "formAutoId");

        renderJsonData(service.approvalProcessUsers(formAutoId, size));
    }

}
