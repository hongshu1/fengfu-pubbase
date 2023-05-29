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
	BEYOND_PLAN(0, "计划外"),
	WITHIN_PLAN(1, "计划内"),
	PREVIOUS_PLAN(2, "提前实施");
    private static final Map<Integer, IsScheduledEnum> SCHEDULED_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (IsScheduledEnum statusEnum : IsScheduledEnum.values()) {
        	SCHEDULED_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    IsScheduledEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static IsScheduledEnum toEnum(int status) {
        return SCHEDULED_TYPE_ENUM_MAP.get(status);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "IsScheduledEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
