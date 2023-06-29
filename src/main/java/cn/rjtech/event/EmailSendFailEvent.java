package cn.rjtech.event;

/**
 * 邮件发送失败事件
 */
public class EmailSendFailEvent {

    private final String email;
    private final String subject;
    private final String text;
    private final String errMsg;

    public EmailSendFailEvent(String email, String subject, String text, String errMsg) {
        this.email = email;
        this.subject = subject;
        this.text = text;
        this.errMsg = errMsg;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public String toString() {
        return "EmailSendFailEvent{" +
                "email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }

}
