package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum FinishStatusEnum {

    /**
     * 枚举列表
     */
    UNFINISHED(1, "未完成"),
    FINISHED(2, "已完成");

    private static final Map<Integer, FinishStatusEnum> VALUE_STATUS_ENUM_MAP = new HashMap<>();

    static {
        for (FinishStatusEnum statusEnum : FinishStatusEnum.values()) {
        	VALUE_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    FinishStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static FinishStatusEnum toEnum(int status) {
        return VALUE_STATUS_ENUM_MAP.get(status);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "FinishStatusEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
