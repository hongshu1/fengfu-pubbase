package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 编码方式枚举
 *
 * @author Kephon
 */
public enum CodingTypeEnum {

    /**
     * 枚举列表
     */
    autoCodeType("自动生产编码", 1),
    manualCodeType("完全手工编码", 2);

    private static final Map<Integer, CodingTypeEnum> CODING_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (CodingTypeEnum codingTypeEnum : CodingTypeEnum.values()) {
            CODING_TYPE_ENUM_MAP.put(codingTypeEnum.value, codingTypeEnum);
        }
    }

    private final int value;
    private final String text;

    CodingTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static CodingTypeEnum toEnum(int value) {
        return CODING_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CodingTypeEnum{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }

}
