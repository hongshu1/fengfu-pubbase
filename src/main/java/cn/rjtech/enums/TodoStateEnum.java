package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核状态枚举
 *
 * @author Kephon
 */
public enum TodoStateEnum {

    /**
     * 枚举列表
     */
    UNSTART(1, "未开始"),
    ACCESSING(2, "进行中"),
    FINISHED(3, "已完成"),
    CANCEL(4, " 已取消"),
	UNFINISHED(5, " 未完成");
	
    private static final Map<Integer, TodoStateEnum> TODO_STATE_ENUM_MAP = new HashMap<>();

    static {
        for (TodoStateEnum statusEnum : TodoStateEnum.values()) {
            TODO_STATE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    TodoStateEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static TodoStateEnum toEnum(int status) {
        return TODO_STATE_ENUM_MAP.get(status);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TodoStateEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
