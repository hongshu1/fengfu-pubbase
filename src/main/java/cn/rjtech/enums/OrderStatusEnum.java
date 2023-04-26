package cn.rjtech.enums;


import java.util.HashMap;
import java.util.Map;

public enum OrderStatusEnum {
    /**
     * 枚举列表   1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭
     */
    NOT_AUDIT("已保存", 1),
    AWAIT_AUDIT("待审核", 2),
    APPROVED("已审核", 3),
    REJECTED("审核不通过", 4),
    GENERATE_CASH_TICKETS("已生成现品票", 5),
    CLOSE("已关闭", 6);
    
    private static final Map<Integer, OrderStatusEnum> ORDER_STATUS_ENUM_MAP = new HashMap<>();
    
    static {
        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()) {
            ORDER_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }
    
    private final String text;
    private final int value;
    
    OrderStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    public static OrderStatusEnum toEnum(int value) {
        return ORDER_STATUS_ENUM_MAP.get(value);
    }
    
    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "OrderStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
