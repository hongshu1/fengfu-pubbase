package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布状态枚举
 *
 * @author Kephon
 */
public enum PutawayEnum {
    /**
     * 枚举列表
     */
    YES("已发布", 1),
    NO("未发布", 2);

    private static final Map<Integer, PutawayEnum> PUTAWAY_ENUM_MAP = new HashMap<>();

    static {
        for (PutawayEnum putawayEnum : PutawayEnum.values()) {
            PUTAWAY_ENUM_MAP.put(putawayEnum.value, putawayEnum);
        }
    }

    private final String text;
    private final int value;

    PutawayEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static PutawayEnum toEnum(int value) {
        return PUTAWAY_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PutawayEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
