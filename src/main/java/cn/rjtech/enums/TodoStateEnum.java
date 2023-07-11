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
    UNSTART("未开始", 1),
    ACCESSING("进行中", 2),
    FINISHED("已完成", 3),
    CANCEL("已取消", 4),
	UNFINISHED("未完成", 5);
	
    private static final Map<Integer, TodoStateEnum> TODO_STATE_ENUM_MAP = new HashMap<>();

    static {
        for (TodoStateEnum statusEnum : TodoStateEnum.values()) {
            TODO_STATE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    TodoStateEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static TodoStateEnum toEnum(int value) {
        return TODO_STATE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TodoStateEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
