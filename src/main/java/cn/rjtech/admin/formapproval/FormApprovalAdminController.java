package cn.rjtech.admin.formapproval;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.*;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.cache.AuditFormConfigCache;
import cn.rjtech.enums.FormAuditConfigTypeEnum;
import cn.rjtech.model.momdata.FormApproval;
import cn.rjtech.util.MsgEventUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;

import java.util.List;

/**
 * 表单审批流 Controller
 *
 * @ClassName: FormApprovalAdminController
 * @author: RJ
 * @date: 2023-04-18 17:26
 */
@CheckPermission(PermissionKey.FORM_APPROVAL_PERMISSION)
@UnCheckIfSystemAdmin
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
    @UnCheck
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
     * 选择审批流
     */
    @CheckPermission(PermissionKey.FORM_APP_SUBMIT)
    public void chooseApproval(){
        render("dialog/select.html");
    }

    /**
     * 发起人自选
     */
    public void optional(){
        String formAutoId = get("formAutoId");
        String formSn = get("formSn");
        String primaryKeyName = get("primaryKeyName");
        String permissionKey = get("permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少单据的提审权限");
        service.optional(formAutoId,formSn,primaryKeyName);

        set("originalUrl", get("originalUrl"));
        set("formAutoId", get("formAutoId"));
        set("formSn", get("formSn"));
        set("primaryKeyName", get("primaryKeyName"));
        set("permissionKey", get("permissionKey"));
        set("className", get("className"));
        render("dialog/add.html");
    }

    /**
     * 新增审批节点
     */
    public void addApprovalD(){
        String iseq = get("iseq");
        set("iseq", iseq);
        render("approvald/add.html");
    }

    /**
     * 编辑审批节点
     */
    public void editApprovalD(){

        render("approvald/add.html");
    }

    /**
     * 自选人提交表单
     */
    public void submitFormByOptional(){
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 提交审核/提交审批
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过业务的类名
     * @param permissionKey  单据提审权限key
     */
    @CheckPermission(PermissionKey.FORM_APP_SUBMIT)
    public void submit(@Para(value = "formSn") String formSn,
                       @Para(value = "formAutoId") Long formAutoId,
                       @Para(value = "primaryKeyName") String primaryKeyName,
                       @Para(value = "className") String className,
                       @Para(value = "permissionKey") String permissionKey) {
        ValidationUtils.notBlank(formSn, "缺少表单编码");
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过业务的类名参数");
        ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少单据的提审权限");

        Ret ret = service.submit(formSn, formAutoId, primaryKeyName, className);

        renderJson(ret);

        if (ret.isOk()) {
            // 审批流处理时，获取审批人推送消息
            if (FormAuditConfigTypeEnum.toEnum(AuditFormConfigCache.ME.getAuditWay(formSn)) == FormAuditConfigTypeEnum.FLOW) {
                List<Long> list = service.getNextApprovalUserIds(formAutoId, 10);
                if (CollUtil.isNotEmpty(list)) {
                    MsgEventUtil.postApprovalMsgEvent(JBoltUserKit.getUserId(), formSn, primaryKeyName, formAutoId, list);
                }
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------------------
    // 审批处理
    // --------------------------------------------------------------------------------------------------------------------

    /**
     * 审批通过
     *
     * @param formAutoId     单据ID
     * @param formSn         表单编码
     * @param status         审批状态
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过业务的类名
     * @param remark         审批意见
     */
    @CheckPermission(PermissionKey.FORM_APP_APPROVE)
    public void approve(@Para(value = "formAutoId") Long formAutoId,
                        @Para(value = "formSn") String formSn,
                        @Para(value = "status") Integer status,
                        @Para(value = "primaryKeyName") String primaryKeyName,
                        @Para(value = "className") String className,
                        @Para(value = "remark") String remark) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过业务的类名参数");

        Ret ret = service.approve(formAutoId, formSn, status, primaryKeyName, className, false, remark);

        renderJson(ret);

        // 异步通知
        if (ret.isOk()) {
            List<Long> list = service.getNextApprovalUserIds(formAutoId, 10);
            if (CollUtil.isNotEmpty(list)) {
                MsgEventUtil.postApprovalMsgEvent(JBoltUserKit.getUserId(), formSn, primaryKeyName, formAutoId, list);
            }
        }
    }

    /**
     * 审批不通过
     *
     * @param formAutoId     单据ID
     * @param formSn         表单编码
     * @param status         审批状态
     * @param primaryKeyName 单据主键名称
     * @param className      处理审批的Service类名
     * @param remark         审批意见
     */
    @CheckPermission(PermissionKey.FORM_APP_REJECT)
    public void reject(@Para(value = "formAutoId") Long formAutoId,
                       @Para(value = "formSn") String formSn,
                       @Para(value = "status") Integer status,
                       @Para(value = "primaryKeyName") String primaryKeyName,
                       @Para(value = "className") String className,
                       @Para(value = "remark") String remark) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "处理审批的Service类名");

        Ret ret = service.reject(formAutoId, formSn, status, primaryKeyName, className, remark, false);
        renderJson(ret);

        // 异步通知:审批不通过，给制单人(提审人)发送待办和邮件
        if (ret.isOk()) {
            MsgEventUtil.postRejectMsgEvent(JBoltUserKit.getUserId(), formSn, primaryKeyName, formAutoId);
        }
    }

    /**
     * 反审批
     *
     * @param formAutoId     单据ID
     * @param formSn         表单编码
     * @param status         审批状态
     * @param primaryKeyName 单据主键名称
     * @param className      处理审批的Service类名
     * @param remark         审批意见
     */
    @CheckPermission(PermissionKey.FORM_APP_REVERSEAPPROVE)
    public void reverseApprove(@Para(value = "formAutoId") Long formAutoId,
                               @Para(value = "formSn") String formSn,
                               @Para(value = "status") Integer status,
                               @Para(value = "primaryKeyName") String primaryKeyName,
                               @Para(value = "className") String className,
                               @Para(value = "remark") String remark) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.validateIntGt0(status, "审批状态");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "处理审批的Service类名");

        renderJson(service.reverseApprove(formAutoId, formSn, primaryKeyName, status, className, remark));
    }

    /**
     * 批量审批通过
     *
     * @param ids            单据IDs
     * @param formSn         表单
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过的业务类名
     */
    @CheckPermission(PermissionKey.FORM_APP_BATCH_APPROVE)
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
    @CheckPermission(PermissionKey.FORM_APP_BATCH_REJECT)
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

//    /**
//     * 批量反审批，接口保留（不需要此实现）
//     *
//     * @param ids            单据ID
//     * @param formSn         表名
//     * @param primaryKeyName 单据主键名称
//     */
//    public void batchReverseApprove(@Para(value = "ids") String ids,
//                                    @Para(value = "formSn") String formSn,
//                                    @Para(value = "primaryKeyName") String primaryKeyName,
//                                    @Para(value = "className") String className) {
//        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
//        ValidationUtils.notBlank(formSn, "表单编码不能为空");
//        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
//        ValidationUtils.notBlank(className, "处理审批的类名");
//
//        renderJson(service.batchReverseApprove(ids, formSn, primaryKeyName, className));
//    }

    /**
     * 批量撤销审批
     *
     * @param ids            单据ID
     * @param formSn         表名
     * @param primaryKeyName 单据主键名称
     */
    @CheckPermission(PermissionKey.FORM_APP_BATCH_BACKOUT)
    public void batchBackOut(@Para(value = "ids") String ids,
                             @Para(value = "formSn") String formSn,
                             @Para(value = "primaryKeyName") String primaryKeyName,
                             @Para(value = "className") String className) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "处理审批的类名");

        renderJson(service.batchBackout(ids, formSn, primaryKeyName, className));
    }

    // --------------------------------------------------------------------------------------------------------------------
    // 审核处理
    //
    // 详情页
    // 1. 审核通过
    // 2. 审核不通过
    // 3. 反审核
    //
    // 列表页
    // 1. 批量审核通过
    // 2. 批量审核不通过
    //
    // --------------------------------------------------------------------------------------------------------------------

    /**
     * 审核通过
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      实现审批通过的业务类名
     * @param permissionKey  权限key
     */
    @CheckPermission(PermissionKey.FORM_APP_AUDIT)
    public void approveByStatus(@Para(value = "formSn") String formSn,
                                @Para(value = "formAutoId") Long formAutoId,
                                @Para(value = "primaryKeyName") String primaryKeyName,
                                @Para(value = "className") String className,
                                @Para(value = "permissionKey") String permissionKey) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少处理审核的类名");
        ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少“审核”权限");

        renderJson(service.approveByStatus(formSn, formAutoId, primaryKeyName, className, false, JBoltUserKit.getUserId()));
    }

    /**
     * 审核不通过
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      实现审批通过的业务类名
     * @param permissionKey  权限key
     */
    @CheckPermission(PermissionKey.FORM_APP_REJECT_AUDIT)
    public void rejectByStatus(@Para(value = "formSn") String formSn,
                               @Para(value = "formAutoId") Long formAutoId,
                               @Para(value = "primaryKeyName") String primaryKeyName,
                               @Para(value = "className") String className,
                               @Para(value = "permissionKey") String permissionKey) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少处理审核的类名");
        ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少“审核”权限");

        renderJson(service.rejectByStatus(formSn, formAutoId, primaryKeyName, className, false, JBoltUserKit.getUserId()));
    }

    /**
     * 反审核
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      实现审批通过的业务类名
     * @param permissionKey  权限key
     */
    @CheckPermission(PermissionKey.FORM_APP_REVERSE_AUDIT)
    public void reverseApproveByStatus(@Para(value = "formSn") String formSn,
                                       @Para(value = "formAutoId") Long formAutoId,
                                       @Para(value = "primaryKeyName") String primaryKeyName,
                                       @Para(value = "className") String className,
                                       @Para(value = "permissionKey") String permissionKey) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少处理审核的类名");
        ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少“反审核”权限");

        renderJson(service.reverseApproveByStatus(formSn, formAutoId, primaryKeyName, className, JBoltUserKit.getUserId()));
    }

    /**
     * 批量审核通过
     *
     * @param ids            单据IDs
     * @param formSn         表单
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过的业务类名
     * @param permissionKey  权限key
     */
    @CheckPermission(PermissionKey.FORM_APP_BATCH_AUDIT)
    public void batchApproveByStatus(@Para(value = "ids") String ids,
                                     @Para(value = "formSn") String formSn,
                                     @Para(value = "primaryKeyName") String primaryKeyName,
                                     @Para(value = "className") String className,
                                     @Para(value = "permissionKey") String permissionKey) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过后的业务类名");
        ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少批量“审核通过”的权限");

        renderJson(service.batchApproveByStatus(ids, formSn, primaryKeyName, className, JBoltUserKit.getUserId()));
    }

    /**
     * 批量审核不通过
     *
     * @param ids            单据IDs
     * @param formSn         表单
     * @param primaryKeyName 单据主键名称
     * @param className      实现审批通过的业务类名
     * @param permissionKey  权限key
     */
    @CheckPermission(PermissionKey.FORM_APP_BATCH_REJECT_AUDIT)
    public void batchRejectByStatus(@Para(value = "ids") String ids,
                                    @Para(value = "formSn") String formSn,
                                    @Para(value = "primaryKeyName") String primaryKeyName,
                                    @Para(value = "className") String className,
                                    @Para(value = "permissionKey") String permissionKey) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过后的业务类名");
        ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少批量“审核不通过”的权限");

        renderJson(service.batchRejectByStatus(ids, formSn, primaryKeyName, className, JBoltUserKit.getUserId()));
    }

    /**
     * 审批过程中待审批的人员
     *
     * @param formAutoId 单据ID
     * @param size       人数
     */
    public void approvalProcessUsers(@Para(value = "formAutoId") Long formAutoId,
                                     @Para(value = "size", defaultValue = "5") Integer size) {
        ValidationUtils.validateId(formAutoId, "formAutoId");

        renderJsonData(service.getNextApprovalUserIds(formAutoId, size));
    }

    /**
     * 撤回审核、审批
     */
    @CheckPermission(PermissionKey.FORM_APP_WITHDRAW)
    public void withdraw(@Para(value = "formAutoId") Long formAutoId,
                         @Para(value = "formSn") String formSn,
                         @Para(value = "primaryKeyName") String primaryKeyName,
                         @Para(value = "className") String className,
                         @Para(value = "permissionKey") String permissionKey) {
        ValidationUtils.validateId(formAutoId, "单据ID");
        ValidationUtils.notBlank(formSn, "表单编码不能为空");
        ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
        ValidationUtils.notBlank(className, "缺少实现审批通过后的业务类名");
        ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
        ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少“撤回”的权限");

        renderJsonData(service.withdraw(formSn, primaryKeyName, formAutoId, className));
    }


}
