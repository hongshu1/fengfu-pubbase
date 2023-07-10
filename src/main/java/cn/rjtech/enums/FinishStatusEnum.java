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
    UNFINISHED("未完成", 1),
    FINISHED("已完成", 2);

    private static final Map<Integer, FinishStatusEnum> FINISH_STATUS_ENUM_MAP = new HashMap<>();

    static {
        for (FinishStatusEnum statusEnum : FinishStatusEnum.values()) {
            FINISH_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    FinishStatusEnum(String text, int value) {
        this.value = value;
        this.text = text;
    }

    public static FinishStatusEnum toEnum(int status) {
        return FINISH_STATUS_ENUM_MAP.get(status);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FinishStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
