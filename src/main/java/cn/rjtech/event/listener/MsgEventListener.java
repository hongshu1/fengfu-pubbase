package cn.rjtech.event.listener;

import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt._admin.msgcenter.TodoService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.config.MomConfigKey;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.enums.ExpenseBudgetTypeEnum;
import cn.rjtech.enums.InvestmentBudgetTypeEnum;
import cn.rjtech.event.ApprovalMsgEvent;
import cn.rjtech.event.BatchAppprovalMsgEvent;
import cn.rjtech.event.BatchApproval;
import cn.rjtech.event.RejectMsgEvent;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.EmailUtils;
import cn.rjtech.util.ExceptionEventUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import net.dreamlu.event.core.EventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息通知异步监听
 *
 * @author Kephon
 */
public class MsgEventListener {

    private final UserService userService = Aop.get(UserService.class);
    private final TodoService todoService = Aop.get(TodoService.class);
    private final FormService formService = Aop.get(FormService.class);
    private final DepartmentService departmentService = Aop.get(DepartmentService.class);
    private final GlobalConfigService globalConfigService = Aop.get(GlobalConfigService.class);
    private final FormApprovalService formApprovalService = Aop.get(FormApprovalService.class);

    /**
     * 审批消息通知监听
     */
    @EventListener(async = true)
    public void listenApprovalMsgEvent(ApprovalMsgEvent event) {
        try {
            Date now = new Date();
            String nowStr = JBoltDateUtil.format(now, JBoltDateUtil.YMDHMSS);

            List<String> emails = userService.getEmails(event.getNextUserIds());

            String content = getApprovalMsgContent(event.getFormSn(), event.getPrimaryKeyName(), event.getFormAutoId(), nowStr, "title");
            String url = getApprovalMsgContent(event.getFormSn(), event.getPrimaryKeyName(), event.getFormAutoId(), nowStr, "url");
            
            // 保存消息处理
            Db.use(DataSourceConstants.MAIN).tx(() -> {
                for (Long userId : event.getNextUserIds()) {
                    todoService.saveTodo(content, userId, now, url, event.getFormSn(), event.getFormAutoId(), event.getLoginUserId());
                }
                return true;
            });
            
            // 发送邮件处理
            EmailUtils.sendEmail(emails, "审批通知", content + ",<a href='" + globalConfigService.getConfigValue(MomConfigKey.EMAIL_LOGIN_URL) + "'>点击访问</a>");
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionEventUtil.postExceptionEvent(e);
        }
    }

    /**
     * 审批消息通知监听
     */
    @EventListener(async = true)
    public void listenBatchAppprovalMsgEvent(BatchAppprovalMsgEvent event) {
        try {
            Date now = new Date();
            
            String nowStr = JBoltDateUtil.format(now, JBoltDateUtil.YMDHMSS);

            List<Kv> emailKvs = new ArrayList<>();
            
            String emailLoginUrl = globalConfigService.getConfigValue(MomConfigKey.EMAIL_LOGIN_URL);
            
            // 保存消息处理
            Db.use(DataSourceConstants.MAIN).tx(() -> {
                for (BatchApproval approval : event.getApprovals()) {

                    String content = getApprovalMsgContent(event.getFormSn(), event.getPrimaryKeyName(), approval.getFormAutoId(), nowStr, "title");
                    String url = getApprovalMsgContent(event.getFormSn(), event.getPrimaryKeyName(), approval.getFormAutoId(), nowStr, "url");
                    
                    for (Long userId : approval.getNextUserIds()) {
                        todoService.saveTodo(content, userId, now, url, event.getFormSn(), approval.getFormAutoId(), event.getLoginUserId());
                    }

                    emailKvs.add(Kv.by("emails", userService.getEmails(approval.getNextUserIds())).set("content", content));
                }

                return true;
            });

            // 发送邮件处理
            for (Kv row : emailKvs) {
                EmailUtils.sendEmail(row.getAs("emails"), "审批通知", row.getStr("content") + ",访问链接：" + emailLoginUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionEventUtil.postExceptionEvent(e);
        }
    }

    private String getApprovalMsgContent(String formSn, String primaryKeyName, long formAutoId, String nowStr, String contentType) {
        Form form = formService.findByCformSn(formSn);

        Record formData = formApprovalService.getApprovalForm(formSn, primaryKeyName, formAutoId);

        switch (formSn) {
            case "PL_Expense_Budget":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");

                int iBudgetYear = formData.get(ExpenseBudget.IBUDGETYEAR);
                String submitUserNameE = JBoltUserCache.me.getName(formData.get(ExpenseBudget.ICREATEBY));
                String cdepname = departmentService.getCdepName(formData.get(ExpenseBudget.CDEPCODE));
                int iBudgetType = formData.getInt(ExpenseBudget.IBUDGETTYPE);
                if ("title".equals(contentType)) {
                    return "由" + submitUserNameE + "提审的" + cdepname + iBudgetYear + ExpenseBudgetTypeEnum.toEnum(iBudgetType).getText() + "的" + form.getCFormName() + "已于" + nowStr + "更新，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/expensebudgetmanage/detail/" + formAutoId;
                }
            case "PL_Investment_Plan":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");

                iBudgetYear = formData.get(InvestmentPlan.IBUDGETYEAR);
                String submitUserNameI = JBoltUserCache.me.getName(formData.get(InvestmentPlan.ICREATEBY));
                cdepname = departmentService.getCdepName(formData.get(InvestmentPlan.CDEPCODE));
                iBudgetType = formData.get(InvestmentPlan.IBUDGETTYPE);
                if ("title".equals(contentType)) {
                    return "由" + submitUserNameI + "提审的" + cdepname + iBudgetYear + InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText() + "的" + form.getCFormName() + "已于" + nowStr + "更新，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/investmentplanmanage/detail/" + formAutoId;
                }
            case "PL_ProposalM":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                String submitUserNamePm = JBoltUserCache.me.getName(formData.get(Proposalm.ICREATEBY));
                if ("title".equals(contentType)) {
                    return "由" + submitUserNamePm + "提审的" + form.getCFormName() + formData.get(Proposalm.CPROPOSALNO) + "已于" + nowStr + "更新，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/proposalm/edit?iautoid=" + formAutoId;
                }
            case "PL_PurchaseM":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                String submitUserNamePc = JBoltUserCache.me.getName(formData.get(Purchasem.ICREATEBY));
                if ("title".equals(contentType)) {
                    return "由" + submitUserNamePc + "提审的" + form.getCFormName() + formData.get(Purchasem.CPURCHASENO) + "已于" + nowStr + "更新，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/purchasem/details/" + formAutoId;
                }
            default:
                throw new ParameterException("未知参数");
        }
    }

    /**
     * 审批不通过消息通知监听
     */
    @EventListener(async = true)
    public void listenRejectMsgEvent(RejectMsgEvent event) {
        try {
            Date now = new Date();
            
            String nowStr = JBoltDateUtil.format(now, JBoltDateUtil.YMDHMSS);
            Form form = formService.findByCformSn(event.getFormSn());
            Record formData = formApprovalService.getApprovalForm(event.getFormSn(), event.getPrimaryKeyName(), event.getFormAutoId());
            Long iuserid = formData.getLong("icreateby");
            String content = getRejectMsgContent(event.getFormSn(), form, formData, event.getFormAutoId(), nowStr, "title");
            String url = getRejectMsgContent(event.getFormSn(), form, formData, event.getFormAutoId(), nowStr, "url");
            
            // 保存消息处理
            Db.use(DataSourceConstants.MAIN).tx(() -> {
                todoService.saveTodo(content, iuserid, now, url, event.getFormSn(), event.getFormAutoId(), event.getLoginUserId());
                return true;
            });
            
            // 发送邮件处理
            List<Long> emailUserIdList = new ArrayList<>();
            emailUserIdList.add(iuserid);
            List<String> emails = userService.getEmails(emailUserIdList);
            EmailUtils.sendEmail(emails, "审批不通过通知", content + ",<a href='" + globalConfigService.getConfigValue(MomConfigKey.EMAIL_LOGIN_URL) + "'>点击访问</a>");
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionEventUtil.postExceptionEvent(e);
        }
    }

    private String getRejectMsgContent(String formSn, Form form, Record formData, Long formAutoId, String nowStr, String contentType) {
        switch (formSn) {
            case "PL_Expense_Budget":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                int iBudgetYear = formData.get(ExpenseBudget.IBUDGETYEAR);
                String cdepname = departmentService.getCdepName(formData.get(ExpenseBudget.CDEPCODE));
                int iBudgetType = formData.getInt(ExpenseBudget.IBUDGETTYPE);
                if ("title".equals(contentType)) {
                    return cdepname + iBudgetYear + ExpenseBudgetTypeEnum.toEnum(iBudgetType).getText() + "的" + form.getCFormName() + "已于" + nowStr + "审批不通过，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/expensebudget/edit/" + formAutoId;
                }
            case "PL_Investment_Plan":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                iBudgetYear = formData.get(InvestmentPlan.IBUDGETYEAR);
                cdepname = departmentService.getCdepName(formData.get(InvestmentPlan.CDEPCODE));
                iBudgetType = formData.get(InvestmentPlan.IBUDGETTYPE);
                if ("title".equals(contentType)) {
                    return cdepname + iBudgetYear + InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText() + "的" + form.getCFormName() + "已于" + nowStr + "审批不通过，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/investmentplan/edit/" + formAutoId;
                }
            case "PL_ProposalM":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                if ("title".equals(contentType)) {
                    return form.getCFormName() + formData.get(Proposalm.CPROPOSALNO) + "已于" + nowStr + "审批不通过，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/proposalm/edit?iautoid=" + formAutoId;
                }
            case "PL_PurchaseM":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                if ("title".equals(contentType)) {
                    return form.getCFormName() + formData.get(Purchasem.CPURCHASENO) + "已于" + nowStr + "审批不通过，请尽快处理";
                } else if ("url".equals(contentType)) {
                    return "admin/purchasem/instrumentEdit/" + formAutoId;
                }
            default:
                throw new ParameterException("未知参数");
        }
    }
}
