package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送单据类型
 *
 * @author Kephon
 */
public enum PushToTypeEnum {

    /**
     * 枚举列表
     */
    U8("U8", 1);

    private final String text;
    private final int value;

    private static final Map<Integer, PushToTypeEnum> PUSH_TO_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (PushToTypeEnum pushToTypeEnum : PushToTypeEnum.values()) {
            PUSH_TO_TYPE_ENUM_MAP.put(pushToTypeEnum.value, pushToTypeEnum);
        }
    }

    PushToTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static PushToTypeEnum toEnum(int value) {
        return PUSH_TO_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PushToTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
