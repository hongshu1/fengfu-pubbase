package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 待办事件单据类型枚举
 *
 * @author Kephon
 */
public enum TodoSourceTypeEnum {

    /**
     * 枚举列表
     */
    OTHER("非单据业务", 0),
    EXPENSE("费用预算", 1),
    INVESTMENT_PLAN("投资计划", 2),
    PROPOSAL("禀议书", 3),
    PURCHASE("申购单", 4);

    private static final Map<Integer, TodoSourceTypeEnum> TODO_SOURCE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (TodoSourceTypeEnum statusEnum : TodoSourceTypeEnum.values()) {
            TODO_SOURCE_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    TodoSourceTypeEnum(String text, int value) {
        this.value = value;
        this.text = text;
    }

    public static TodoSourceTypeEnum toEnum(int value) {
        return TODO_SOURCE_TYPE_ENUM_MAP.get(value);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TodoSourceTypeEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
