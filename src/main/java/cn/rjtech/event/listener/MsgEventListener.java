package cn.rjtech.event.listener;

import cn.jbolt._admin.msgcenter.TodoService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.enums.ExpenseBudgetTypeEnum;
import cn.rjtech.enums.InvestmentBudgetTypeEnum;
import cn.rjtech.event.ApprovalMsgEvent;
import cn.rjtech.event.BatchAppprovalMsgEvent;
import cn.rjtech.event.BatchApproval;
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

            String content = getContent(event.getFormSn(), event.getPrimaryKeyName(), event.getFormAutoId(), nowStr);
            
            // 保存消息处理
            Db.use(DataSourceConstants.MAIN).tx(() -> {

                for (Long userId : event.getNextUserIds()) {
                    todoService.saveTodo(content, userId, now, null, event.getFormSn(), event.getFormAutoId(), event.getLoginUserId());
                }
                
                return true;
            });

            // 发送邮件处理
            EmailUtils.sendEmail(emails, "审批通知", content);
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

            // 保存消息处理
            Db.use(DataSourceConstants.MAIN).tx(() -> {

                for (BatchApproval approval : event.getApprovals()) {

                    String content = getContent(event.getFormSn(), event.getPrimaryKeyName(), approval.getFormAutoId(), nowStr);
                    
                    for (Long userId : approval.getNextUserIds()) {
                        todoService.saveTodo(content, userId, now, null, event.getFormSn(), approval.getFormAutoId(), event.getLoginUserId());
                    }

                    emailKvs.add(Kv.by("emails", userService.getEmails(approval.getNextUserIds())).set("content", content));
                }

                return true;
            });

            // 发送邮件处理
            for (Kv row : emailKvs) {
                EmailUtils.sendEmail(row.getAs("emails"), "审批通知", row.getStr("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionEventUtil.postExceptionEvent(e);
        }
    }

    private String getContent(String formSn, String primaryKeyName, long formAutoId, String nowStr) {
        Form form = formService.findByCformSn(formSn);
        
        Record formData = formApprovalService.getApprovalForm(formSn, primaryKeyName, formAutoId);

        switch (formSn) {
            case "PL_Expense_Budget":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");

                int iBudgetYear = formData.get(ExpenseBudget.IBUDGETYEAR);
                String cdepname = departmentService.getCdepName(formData.get(ExpenseBudget.CDEPCODE));
                int iBudgetType = formData.getInt(ExpenseBudget.IBUDGETTYPE);
                
                return cdepname + iBudgetYear + ExpenseBudgetTypeEnum.toEnum(iBudgetType).getText() + "的" + form.getCFormName() + "已于" + nowStr + "更新，请尽快处理";
            case "PL_Investment_Plan":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");

                iBudgetYear = formData.get(InvestmentPlan.IBUDGETYEAR);
                cdepname = departmentService.getCdepName(formData.get(InvestmentPlan.CDEPCODE));
                iBudgetType = formData.get(InvestmentPlan.IBUDGETTYPE);
                
                return cdepname + iBudgetYear + InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText() + "的" + form.getCFormName() + "已于" + nowStr + "更新，请尽快处理";
            case "PL_ProposalM":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                
                return form.getCFormName() + formData.get(Proposalm.CPROPOSALNO) + "已于" + nowStr + "更新，请尽快处理";
            case "PL_PurchaseM":
                ValidationUtils.notNull(formData, form.getCFormName() + "不存在,生成消息失败!");
                
                return form.getCFormName() + formData.get(Purchasem.CPURCHASENO) + "已于" + nowStr + "更新，请尽快处理";
            default:
                throw new ParameterException("未知参数");
        }
    }

}
