package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成状态枚举
 *
 * @author Kephon
 */
public enum GenerateStatusEnum {
    /**
     * 枚举列表
     */
    N("未生成", 0),
    Y("已生成", 1);

    private static final Map<Integer, GenerateStatusEnum> GENERATE_STATUS_ENUM_MAP = new HashMap<>();

    static {
        for (GenerateStatusEnum statusEnum : GenerateStatusEnum.values()) {
            GENERATE_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    GenerateStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static GenerateStatusEnum toEnum(int value) {
        return GENERATE_STATUS_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "GenerateStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
