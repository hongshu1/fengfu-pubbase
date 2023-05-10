package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/8 14:07
 * @Description 整体尺寸的枚举
 */
public enum OverallSizeEnum {

    /**
     * 枚举列表
     */
    FIRST(0, "√"),
    SECOND(1, "×"),
    THIRD(2, "/");

    private static final Map<Integer, OverallSizeEnum> SOURCE_ENUM_MAP = new HashMap<>();

    static {
        for (OverallSizeEnum overallSizeEnum : OverallSizeEnum.values()) {
            SOURCE_ENUM_MAP.put(overallSizeEnum.value, overallSizeEnum);
        }
    }

    private final int    value;
    private final String text;

    OverallSizeEnum(int value, String text) {
        this.value = value;
        this.text  = text;
    }

    public static OverallSizeEnum toEnum(int value) {
        return SOURCE_ENUM_MAP.get(value);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "OverallSizeEnum{" +
            "value=" + value +
            ", text='" + text + '\'' +
            '}';
    }
}
