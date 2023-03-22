package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

public enum SourceEnum {
    /**
     * 枚举列表
     */
    MES("MES", 1),
    U8("U8", 2);

    private static final Map<Integer, SourceEnum> SOURCE_ENUM_MAP = new HashMap<>();

    static {
        for (SourceEnum statusEnum : SourceEnum.values()) {
            SOURCE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    SourceEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static SourceEnum toEnum(int value) {
        return SOURCE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SourceEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
