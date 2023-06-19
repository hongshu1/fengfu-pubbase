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
    OTHER((short) 0, "非单据业务"),
    EXPENSE((short) 1, "费用预算"),
    INVESTMENT_PLAN((short) 2, "投资计划"),
    PROPOSAL((short) 3, "禀议书"),
    PURCHASE((short) 4, " 申购单");

    private static final Map<Short, TodoSourceTypeEnum> TODO_SOURCE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (TodoSourceTypeEnum statusEnum : TodoSourceTypeEnum.values()) {
            TODO_SOURCE_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final short value;

    TodoSourceTypeEnum(short value, String text) {
        this.value = value;
        this.text = text;
    }

    public static TodoSourceTypeEnum toEnum(short status) {
        return TODO_SOURCE_TYPE_ENUM_MAP.get(status);
    }

    public short getValue() {
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
