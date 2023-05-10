package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/8 11:47
 * @Description 测定目的的枚举
 */
public enum CMeasurePurposeEnum {

    /**
     * 枚举列表
     */
    FIRST(0, "定期检查"),
    SECOND(1, "初物检查"),
    THIRD(2, "委托测定"),
    FOURTH(3, "特别检查");

    private static final Map<Integer, CMeasurePurposeEnum> SOURCE_ENUM_MAP = new HashMap<>();

    static {
        for (CMeasurePurposeEnum purposeEnum : CMeasurePurposeEnum.values()) {
            SOURCE_ENUM_MAP.put(purposeEnum.value, purposeEnum);
        }
    }

    private final int    value;
    private final String text;

    CMeasurePurposeEnum(int value, String text) {
        this.value = value;
        this.text  = text;
    }

    public static CMeasurePurposeEnum toEnum(int value) {
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
        return "CMeasurePurposeEnum{" +
            "value=" + value +
            ", text='" + text + '\'' +
            '}';
    }
}
