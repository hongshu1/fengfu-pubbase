package cn.rjtech.util;

import cn.rjtech.event.ApprovalMsgEvent;
import cn.rjtech.event.BatchAppprovalMsgEvent;
import cn.rjtech.event.BatchApproval;
import cn.rjtech.event.RejectMsgEvent;
import net.dreamlu.event.EventKit;

import java.util.List;

/**
 * 消息通知异步调用
 *
 * @author Kephon
 */
public class MsgEventUtil {

    /**
     * 审核消息
     *
     * @param loginUserId 当前操作人ID
     * @param formSn      表单编码
     * @param formAutoId  单据ID
     * @param nextUserIds 待通知的用户ID
     */
    public static void postApprovalMsgEvent(long loginUserId, String formSn, String primaryKeyName, long formAutoId, List<Long> nextUserIds) {
        EventKit.post(new ApprovalMsgEvent(loginUserId, formSn, primaryKeyName, formAutoId, nextUserIds));
    }

    public static void postBatchApprovalMsgEvent(long loginUserId, String formSn, String primaryKeyName, List<BatchApproval> approvals) {
        EventKit.post(new BatchAppprovalMsgEvent(loginUserId, formSn, primaryKeyName, approvals));
    }

    
    /**
     * 审核不通过生成待办和邮件
     *
     * @param loginUserId 当前操作人ID
     * @param formSn      表单编码
     * @param formAutoId  单据ID
     * @param nextUserIds 待通知的用户ID
     */
    public static void postRejectMsgEvent(long loginUserId, String formSn, String primaryKeyName, long formAutoId) {
        EventKit.post(new RejectMsgEvent(loginUserId, formSn, primaryKeyName, formAutoId));
    }
}
