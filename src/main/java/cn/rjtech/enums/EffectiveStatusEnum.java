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
    INVAILD(1, "未生效"),
    EFFECTIVED(2, "已生效"),
    EXPIRED(3, "已失效"),
    CANCLE(4, "已作废");

    private final String text;
    private final int value;

    private static final Map<Integer, EffectiveStatusEnum> EFFECTIVE_STATUS_ENUM_MAP = new HashMap<>();

    static {
        for (EffectiveStatusEnum statusEnum : EffectiveStatusEnum.values()) {
            EFFECTIVE_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    EffectiveStatusEnum(int value, String text) {
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
        return "AuditStatusEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
