package cn.rjtech.event;

/**
 * 审批不通过消息事件对象
 *
 * @author Kephon
 */
public class RejectMsgEvent {

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

    public RejectMsgEvent(long loginUserId, String formSn, String primaryKeyName, long formAutoId) {
        this.loginUserId = loginUserId;
        this.formSn = formSn;
        this.primaryKeyName = primaryKeyName;
        this.formAutoId = formAutoId;
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

    @Override
    public String toString() {
        return "RejectMsgEvent{" +
                "loginUserId=" + loginUserId +
                ", formSn='" + formSn + '\'' +
                ", primaryKeyName='" + primaryKeyName + '\'' +
                ", formAutoId=" + formAutoId +
                '}';
    }
    
}
