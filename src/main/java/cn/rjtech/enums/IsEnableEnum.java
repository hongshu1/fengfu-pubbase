package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum IsEnableEnum {
    /**
     * 枚举列表
     */
    NO("否", 0),
    YES("是", 1);

    private static final Map<Integer, IsEnableEnum> VALUE_KEY_ENUM_MAP = new HashMap<>();
    private static final Map<String, IsEnableEnum> VALUE_TEXT_ENUM_MAP = new HashMap<>();

    static {
        for (IsEnableEnum isEnableEnum : IsEnableEnum.values()) {
            VALUE_KEY_ENUM_MAP.put(isEnableEnum.value, isEnableEnum);
        }
        for (IsEnableEnum isEnableEnum : IsEnableEnum.values()) {
            VALUE_TEXT_ENUM_MAP.put(isEnableEnum.text, isEnableEnum);
        }
    }

    private final String text;
    private final int value;

    IsEnableEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public static IsEnableEnum toEnum(Integer value) {
        return VALUE_KEY_ENUM_MAP.get(value);
    }

    public static IsEnableEnum toText(String text) {
        return VALUE_TEXT_ENUM_MAP.get(text);
    }

    @Override
    public String toString() {
        return "IsEnableEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
