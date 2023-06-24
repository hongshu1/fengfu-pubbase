package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 是否枚举 char(1)类型字段
 *
 * @author Kephon
 */
public enum BoolCharEnum {
    /**
     * 枚举列表
     */
    NO("否", "0"),
    YES("是", "1");

    private static final Map<String, BoolCharEnum> BOOL_VALUE_ENUM_MAP = new HashMap<>();

    private static final Map<String, BoolCharEnum> BOOL_TEXT_ENUM_MAP = new HashMap<>();

    static {
        for (BoolCharEnum charEnum : BoolCharEnum.values()) {
            BOOL_VALUE_ENUM_MAP.put(charEnum.value, charEnum);
            BOOL_TEXT_ENUM_MAP.put(charEnum.text, charEnum);
        }
    }

    private final String text;
    private final String value;

    BoolCharEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static BoolCharEnum toEnum(String value) {
        return BOOL_VALUE_ENUM_MAP.get(value);
    }

    public static BoolCharEnum textToEnum(String text) {
        return BOOL_TEXT_ENUM_MAP.get(text);
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BoolCharEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
