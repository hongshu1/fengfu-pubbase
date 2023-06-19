package cn.rjtech.event;

import java.util.List;

/**
 * 审批消息事件对象
 *
 * @author Kephon
 */
public class ApprovalMsgEvent {

    /**
     * 单据类型
     */
    private final int type;
    /**
     * 单据ID
     */
    private final long formAutoId;
    /**
     * 下一节点接收通知的用户Ids
     */
    private final List<Long> nextUserIds;

    public ApprovalMsgEvent(int type, long formAutoId, List<Long> nextUserIds) {
        this.type = type;
        this.formAutoId = formAutoId;
        this.nextUserIds = nextUserIds;
    }

    public int getType() {
        return type;
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
                "type=" + type +
                ", formAutoId=" + formAutoId +
                ", nextUserIds=" + nextUserIds +
                '}';
    }
    
}
