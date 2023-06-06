package cn.rjtech.enums;

/**
 * 审核状态
 *
 * @author Kephon
 */
public enum AuditStateEnum {
    /**
     * 枚举列表
     */
    NOT_AUDIT("已保存", "1"),
    AWAIT_AUDIT("待审核", "2"),
    APPROVED("已审批", "3"),
    REJECTED("审批不通过", "4");

    private final String text;
    private final String value;

    AuditStateEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AuditStateEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
