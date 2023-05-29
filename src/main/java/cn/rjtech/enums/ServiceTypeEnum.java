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
    EXPENSE_BUDGET(1, "费用预算"),
    INVESTMENT_PLAN(2, "投资计划");

    private static final Map<Integer, ServiceTypeEnum> SERVICE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (ServiceTypeEnum statusEnum : ServiceTypeEnum.values()) {
            SERVICE_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    ServiceTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static ServiceTypeEnum toEnum(int status) {
        return SERVICE_TYPE_ENUM_MAP.get(status);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "ServiceTypeEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
