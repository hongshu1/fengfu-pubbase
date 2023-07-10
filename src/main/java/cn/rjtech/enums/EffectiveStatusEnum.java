package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum EffectiveStatusEnum {

    /**
     * 枚举列表
     */
    INVAILD("未生效", 1),
    EFFECTIVED("已生效", 2),
    EXPIRED("已失效", 3),
    CANCLE("已作废", 4);

    private final String text;
    private final int value;

    private static final Map<Integer, EffectiveStatusEnum> EFFECTIVE_STATUS_ENUM_MAP = new HashMap<>();

    static {
        for (EffectiveStatusEnum statusEnum : EffectiveStatusEnum.values()) {
            EFFECTIVE_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    EffectiveStatusEnum(String text, int value) {
        this.value = value;
        this.text = text;
    }

    public static EffectiveStatusEnum toEnum(int value) {
        return EFFECTIVE_STATUS_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EffectiveStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
