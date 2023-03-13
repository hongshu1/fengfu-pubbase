package cn.rjtech.enums;

/**
 * 邮件接收人类型
 *
 * @author Kephon
 */
public enum EmailReceiverTypeEnum {
    /**
     * 枚举列表
     */
    EXCEPTION("异常通知", (short) 1);

    private final String text;
    private final short value;

    EmailReceiverTypeEnum(String text, short value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public short getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EmailReceiverTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
