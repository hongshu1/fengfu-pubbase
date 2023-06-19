package cn.rjtech.event.listener;

import cn.jbolt._admin.msgcenter.TodoService;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.proposalm.ProposalmService;
import cn.rjtech.admin.purchasem.PurchasemService;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.enums.ExpenseBudgetTypeEnum;
import cn.rjtech.enums.InvestmentBudgetTypeEnum;
import cn.rjtech.enums.TodoSourceTypeEnum;
import cn.rjtech.event.ApprovalMsgEvent;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.InvestmentPlan;
import cn.rjtech.model.momdata.Proposalm;
import cn.rjtech.model.momdata.Purchasem;
import cn.rjtech.util.ExceptionEventUtil;
import cn.rjtech.util.ValidationUtils;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import net.dreamlu.event.core.EventListener;

/**
 * 消息通知异步监听
 *
 * @author Kephon
 */
@EventListener
public class MsgEventListener {

    private final TodoService todoService = Aop.get(TodoService.class);
    private final ExpenseBudgetService expenseBudgetService = Aop.get(ExpenseBudgetService.class);
    private final InvestmentPlanService investmentPlanService = Aop.get(InvestmentPlanService.class);
    private final ProposalmService proposalmService = Aop.get(ProposalmService.class);
    private final PurchasemService purchasemService = Aop.get(PurchasemService.class);
    private final DepartmentService departmentService = Aop.get(DepartmentService.class);
    /**
     * 审批消息通知监听
     */
    @EventListener(async = true)
    public void listenApprovalMsgEvent(ApprovalMsgEvent event) {
        try {
            // 保存消息处理
            Db.use(DataSourceConstants.MAIN).tx(() -> {
            	Date now = new Date();
            	String nowStr = JBoltDateUtil.format(now, JBoltDateUtil.YMDHMSS);
            	short formType = (short) event.getType();
            	TodoSourceTypeEnum typeTodoSourceTypeEnum = TodoSourceTypeEnum.toEnum(formType);
            	List<Long> userIds = event.getNextUserIds();
            	Long iformAutoId = event.getFormAutoId();
            	String formObjectName = typeTodoSourceTypeEnum.getText();
            	ValidationUtils.notEmpty(userIds, "用户为空，生成消息失败!");
            	ValidationUtils.notNull(iformAutoId, "单据ID为空，生成消息失败!");
            	String title =  "";
            	Long loginUserId = JBoltUserKit.getUserId();
            	Integer iBudgetYear = null;
            	String cdepname = null;
            	Integer iBudgetType = null;
            	switch (typeTodoSourceTypeEnum) {
				case EXPENSE:
					ExpenseBudget expenseBudget = expenseBudgetService.findById(iformAutoId);
					ValidationUtils.notNull(expenseBudget, formObjectName+"不存在,生成消息失败!");
					iBudgetYear = expenseBudget.getIBudgetYear();
					cdepname = departmentService.getCdepName(expenseBudget.getCDepCode());
					iBudgetType = expenseBudget.getIBudgetType();
					title = cdepname + iBudgetYear + ExpenseBudgetTypeEnum.toEnum(iBudgetType).getText() + "的"+formObjectName+"已于"+nowStr+"更新，请尽快处理";
					break;
				case INVESTMENT_PLAN:
					InvestmentPlan investmentPlan = investmentPlanService.findById(iformAutoId);
					ValidationUtils.notNull(investmentPlan, formObjectName+"不存在,生成消息失败!");
					iBudgetYear = investmentPlan.getIBudgetYear();
					cdepname = departmentService.getCdepName(investmentPlan.getCDepCode());
					iBudgetType = investmentPlan.getIBudgetType();
					title = cdepname + iBudgetYear + InvestmentBudgetTypeEnum.toEnum(iBudgetType).getText() + "的"+formObjectName+"已于"+nowStr+"更新，请尽快处理";
					break;				
				case PROPOSAL:
					Proposalm proposalm = proposalmService.findById(iformAutoId);
					ValidationUtils.notNull(proposalm, formObjectName+"不存在,生成消息失败!");
					String proposalNo = proposalm.getCProposalNo();
					title = formObjectName + proposalNo+"已于"+nowStr+"更新，请尽快处理";
					break;
				case PURCHASE:
					Purchasem purchasem = purchasemService.findById(iformAutoId);
					ValidationUtils.notNull(purchasem, formObjectName+"不存在,生成消息失败!");
					String purchaseNo = purchasem.getCPurchaseNo();
					title = formObjectName + purchaseNo+"已于"+nowStr+"更新，请尽快处理";
					break;							
				default:
					break;
				}
            	for (Long userId : userIds) {
            		todoService.saveTodo(title, userId, now, null, formType, iformAutoId, loginUserId);
				}
                return true;
            });
            
            // 发送邮件处理
            
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionEventUtil.postExceptionEvent(e);
        }
    }

}
