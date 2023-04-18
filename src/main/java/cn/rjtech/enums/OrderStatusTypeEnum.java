package cn.rjtech.enums;

/**
 * Create By ${刘帅} on 2023/4/12.
 */
public enum OrderStatusTypeEnum {
            ORDER_STATUS_SAVE("已保存",1),
            ORDER_STATUS_WAIT("待审批",2),
            ORDER_STATUS_PASS("已审批",3),
            ORDER_STATUS_NO("审批不通过",4),
            ORDER_STATUS_OK("已生成现品票",5),
            ORDER_STATUS_CLOSE(" 已关闭",6)
        ;
    private final String text;
    private final int value;

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    OrderStatusTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
}
