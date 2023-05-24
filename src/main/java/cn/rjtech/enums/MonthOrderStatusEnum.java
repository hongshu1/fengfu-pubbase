package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 月度订单 - 订单状态
 *
 * @author Kephon
 */
public enum MonthOrderStatusEnum {

    /**
     * 枚举列表：1. 已保存 2. 待审批 3. 已审批
     */
    SAVED("已保存", 1),
    AWAIT_AUDITED("待审核", 2),
    AUDITTED("已审批", 3);

    private static final Map<Integer, MonthOrderStatusEnum> MANUAL_ORDER_STATUS_ENUM = new HashMap<>();

    static {
        for (MonthOrderStatusEnum statusEnum : MonthOrderStatusEnum.values()) {
            MANUAL_ORDER_STATUS_ENUM.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    MonthOrderStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static MonthOrderStatusEnum toEnum(int value) {
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
        return "MonthOrderStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
