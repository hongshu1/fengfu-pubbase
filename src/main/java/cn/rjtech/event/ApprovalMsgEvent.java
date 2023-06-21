package cn.rjtech.event;

import java.util.List;

/**
 * 审批消息事件对象
 *
 * @author Kephon
 */
public class ApprovalMsgEvent {

    /**
     * 登录用户ID
     */
    private final long loginUserId;
    /**
     * 单据类型
     */
    private final String formSn;
    /**
     * 主键名
     */
    private final String primaryKeyName;
    /**
     * 单据ID
     */
    private final long formAutoId;
    /**
     * 下一节点接收通知的用户Ids
     */
    private final List<Long> nextUserIds;

    public ApprovalMsgEvent(long loginUserId, String formSn, String primaryKeyName, long formAutoId, List<Long> nextUserIds) {
        this.loginUserId = loginUserId;
        this.formSn = formSn;
        this.primaryKeyName = primaryKeyName;
        this.formAutoId = formAutoId;
        this.nextUserIds = nextUserIds;
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

    public long getFormAutoId() {
        return formAutoId;
    }

    public List<Long> getNextUserIds() {
        return nextUserIds;
    }

    @Override
    public String toString() {
        return "ApprovalMsgEvent{" +
                "loginUserId=" + loginUserId +
                ", formSn='" + formSn + '\'' +
                ", primaryKeyName='" + primaryKeyName + '\'' +
                ", formAutoId=" + formAutoId +
                ", nextUserIds=" + nextUserIds +
                '}';
    }
    
}
