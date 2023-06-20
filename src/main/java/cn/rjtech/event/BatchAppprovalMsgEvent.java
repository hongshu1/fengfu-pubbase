package cn.rjtech.event;

import java.util.List;

/**
 * 批量审批消息
 *
 * @author Kephon
 */
public class BatchAppprovalMsgEvent {

    /**
     * 登录用户ID
     */
    private final long loginUserId;
    /**
     * 单据类型
     */
    private final String formSn;
    /**
     * 单据主键名
     */
    private final String primaryKeyName;
    /**
     * 批量通知的单据
     */
    private final List<BatchApproval> approvals;

    public BatchAppprovalMsgEvent(long loginUserId, String formSn, String primaryKeyName, List<BatchApproval> approvals) {
        this.loginUserId = loginUserId;
        this.formSn = formSn;
        this.primaryKeyName = primaryKeyName;
        this.approvals = approvals;
    }

    public long getLoginUserId() {
        return loginUserId;
    }

    public String getFormSn() {
        return formSn;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public List<BatchApproval> getApprovals() {
        return approvals;
    }

    @Override
    public String toString() {
        return "BatchAppprovalMsgEvent{" +
                "loginUserId=" + loginUserId +
                ", formSn='" + formSn + '\'' +
                ", primaryKeyName='" + primaryKeyName + '\'' +
                ", approvals=" + approvals +
                '}';
    }
    
}
