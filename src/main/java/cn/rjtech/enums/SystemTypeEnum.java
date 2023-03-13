package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备类型枚举
 *
 * @author Kephon
 */
public enum SystemTypeEnum {
    /**
     * 枚举列表
     */
    ANDROID("安卓", 1);

    private final String text;
    private final int value;

    private static final Map<Integer, SystemTypeEnum> SYSTEM_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (SystemTypeEnum systemTypeEnum : SystemTypeEnum.values()) {
            SYSTEM_TYPE_ENUM_MAP.put(systemTypeEnum.value, systemTypeEnum);
        }
    }

    SystemTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static SystemTypeEnum toEnum(int value) {
        return SYSTEM_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SystemTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
