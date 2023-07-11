package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Heming
 */
public enum IsScheduledEnum {

    /**
     * 枚举列表
     */
    BEYOND_PLAN("计划外", 0),
    WITHIN_PLAN("计划内", 1),
    PREVIOUS_PLAN("提前实施", 2);

    private static final Map<Integer, IsScheduledEnum> SCHEDULED_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (IsScheduledEnum statusEnum : IsScheduledEnum.values()) {
            SCHEDULED_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    IsScheduledEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static IsScheduledEnum toEnum(int value) {
        return SCHEDULED_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IsScheduledEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
