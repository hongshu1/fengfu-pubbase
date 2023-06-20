package cn.rjtech.event;

import java.util.List;

/**
 * @author Kephon
 */
public class BatchApproval {
    
    private final long formAutoId;

    private final List<Long> nextUserIds;

    public BatchApproval(long formAutoId, List<Long> nextUserIds) {
        this.formAutoId = formAutoId;
        this.nextUserIds = nextUserIds;
    }

    public long getFormAutoId() {
        return formAutoId;
    }

    public List<Long> getNextUserIds() {
        return nextUserIds;
    }

    @Override
    public String toString() {
        return "BatchApproval{" +
                "formAutoId=" + formAutoId +
                ", nextUserIds=" + nextUserIds +
                '}';
    }
    
}
