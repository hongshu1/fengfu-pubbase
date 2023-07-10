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
    FIRST("定期检查", 0),
    SECOND("初物检查", 1),
    THIRD("委托测定",2),
    FOURTH("特别检查", 3);

    private static final Map<Integer, CMeasurePurposeEnum> SOURCE_ENUM_MAP = new HashMap<>();

    static {
        for (CMeasurePurposeEnum purposeEnum : CMeasurePurposeEnum.values()) {
            SOURCE_ENUM_MAP.put(purposeEnum.value, purposeEnum);
        }
    }

    private final String text;
    private final int value;

    CMeasurePurposeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static CMeasurePurposeEnum toEnum(int value) {
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
        return "CMeasurePurposeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
