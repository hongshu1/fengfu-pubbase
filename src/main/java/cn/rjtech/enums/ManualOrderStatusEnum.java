package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 手配订单状态枚举
 *
 * @author Kephon
 */
public enum ManualOrderStatusEnum {
    /**
     * 枚举列表：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已关闭
     */
    SAVED("已保存", 1),
    AWAIT_AUDITED("待审核", 2),
    AUDITTED("已审批", 3),
    REJECTED("不通过", 4),
    DELIVERIED("已发货", 5),
    CLOSED("已关闭", 6);

    private static final Map<Integer, ManualOrderStatusEnum> MANUAL_ORDER_STATUS_ENUM = new HashMap<>();
    
    static {
        for (ManualOrderStatusEnum statusEnum : ManualOrderStatusEnum.values()) {
            MANUAL_ORDER_STATUS_ENUM.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    ManualOrderStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ManualOrderStatusEnum toEnum(int value) {
        return MANUAL_ORDER_STATUS_ENUM.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ManualOrderStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
