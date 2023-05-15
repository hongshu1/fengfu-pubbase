package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/11 9:28
 * @Description 是否合格的枚举
 */
public enum IsOkEnum {
    /**
     * 枚举列表
     */
    NO(0, false),
    YES(1, true);

    private static final Map<Integer, IsOkEnum> SOURCE_ENUM_MAP = new HashMap<>();

    static {
        for (IsOkEnum isOkEnum : IsOkEnum.values()) {
            SOURCE_ENUM_MAP.put(isOkEnum.value, isOkEnum);
        }
    }

    private final int    value;
    private final Boolean text;

    IsOkEnum(int value, Boolean text) {
        this.value = value;
        this.text  = text;
    }

    public static IsOkEnum toEnum(int value) {
        return SOURCE_ENUM_MAP.get(value);
    }

    public int getValue() {
        return value;
    }

    public Boolean getText() {
        return text;
    }

    @Override
    public String toString() {
        return "IsOkEnum{" +
            "value=" + value +
            ", text='" + text + '\'' +
            '}';
    }
}
