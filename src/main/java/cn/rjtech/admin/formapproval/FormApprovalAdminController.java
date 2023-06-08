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
     * @param formSn     表单编码
     * @param formAutoId 单据ID
     */
    public void judgeType(@Para(value = "formSn") String formSn,
                          @Para(value = "formAutoId") Long formAutoId) {
        ValidationUtils.notBlank(formSn, "缺少表单编码");
        ValidationUtils.validateId(formAutoId, "单据ID");

        renderJson(service.judgeType(formSn, formAutoId));
    }

    /**
     * 审批
     *
     * @param formAutoId 单据ID
     * @param formSn     表单编码
     * @param status     审批状态
     */
    public void approve(@Para(value = "formAutoId") Long formAutoId,
                        @Para(value = "formSn") String formSn,
                        @Para(value = "status") Integer status) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");

        renderJson(service.approve(formAutoId, formSn, status));
    }

    /**
     * 拒审
     *
     * @param formAutoId 单据ID
     * @param formSn     表单编码
     * @param status     审批状态
     */
    public void rejectApprove(@Para(value = "formAutoId") Long formAutoId,
                        @Para(value = "formSn") String formSn,
                        @Para(value = "status") Integer status) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");

        renderJson(service.rejectApprove(formAutoId, formSn, status));
    }

    /**
     * 反审批
     *
     * @param formAutoId 单据ID
     * @param formSn     表单编码
     * @param status     审批状态
     */
    public void reverseApprove(@Para(value = "formAutoId") Long formAutoId,
                        @Para(value = "formSn") String formSn,
                        @Para(value = "status") Integer status) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");

        renderJson(service.reverseApprove(formAutoId, formSn, status));
    }

    /**
     * 批量审批
     * @param ids
     * @param formSn
     * @param status
     */
    public void batchApprove(@Para(value = "ids") String ids,
                             @Para(value = "formSn") String formSn,
                             @Para(value = "status") Integer status){
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        renderJson(service.batchApprove(ids, formSn, status));
    }

    /**
     * 批量拒审
     * @param ids
     * @param formSn
     * @param status
     */
    public void batchRejectApprove(@Para(value = "ids") String ids,
                             @Para(value = "formSn") String formSn,
                             @Para(value = "status") Integer status){
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        renderJson(service.batchRejectApprove(ids, formSn, status));
    }

    /**
     * 批量反审批
     * @param ids
     * @param formSn
     * @param status
     */
    public void batchReverseApprove(@Para(value = "ids") String ids,
                             @Para(value = "formSn") String formSn,
                             @Para(value = "status") Integer status){
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        renderJson(service.batchReverseApprove(ids, formSn, status));
    }

    /**
     * 判断审核还是审批
     */
    public void auditOrApprove(@Para(value = "formSn") String formSn){
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        renderJson(service.auditOrApprove(formSn));
    }

    /**
     * 审批过程中待审批的人员
     */
    public void approvalProcessUsers(@Para(value = "formAutoId") Long formAutoId,@Para(value = "size") Integer size){
        renderJsonData(service.approvalProcessUsers(formAutoId,size));
    }
}
