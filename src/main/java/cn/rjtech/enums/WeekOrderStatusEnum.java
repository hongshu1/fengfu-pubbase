package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 周间客户订单状态枚举
 *
 * @author Kephon
 */
public enum WeekOrderStatusEnum {
    /**
     * 枚举列表：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭
     */
    SAVED("已保存", 1),
    AWAIT_AUDITED("待审核", 2),
    AUDITTED("已审批", 3),
    REJECTED("不通过", 4),
    DELIVERIED("已发货", 5),
    OK("已核对", 6),
    CLOSED("已关闭", 7);

    private static final Map<Integer, WeekOrderStatusEnum> WEEK_ORDER_STATUS_ENUM_MAP = new HashMap<>();
    
    static {
        for (WeekOrderStatusEnum statusEnum : WeekOrderStatusEnum.values()) {
            WEEK_ORDER_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    WeekOrderStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static WeekOrderStatusEnum toEnum(int value) {
        return WEEK_ORDER_STATUS_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "WeekOrderStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
