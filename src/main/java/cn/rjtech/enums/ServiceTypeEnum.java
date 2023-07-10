package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Heming
 */
public enum ServiceTypeEnum {

    /**
     * 枚举列表
     */
    EXPENSE_BUDGET("费用预算", 1),
    INVESTMENT_PLAN("投资计划", 2);

    private static final Map<Integer, ServiceTypeEnum> SERVICE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (ServiceTypeEnum statusEnum : ServiceTypeEnum.values()) {
            SERVICE_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    ServiceTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ServiceTypeEnum toEnum(int value) {
        return SERVICE_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ServiceTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
