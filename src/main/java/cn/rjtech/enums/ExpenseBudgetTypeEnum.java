package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum ExpenseBudgetTypeEnum {

    /**
     * 费用预算类型枚举列表
     */
    FUALL_YEAR_BUDGET("全年预算", 1),
    NEXT_PERIOD_EDIT("下期修改", 2);

    private static final Map<Integer, ExpenseBudgetTypeEnum> BUDGET_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (ExpenseBudgetTypeEnum statusEnum : ExpenseBudgetTypeEnum.values()) {
            BUDGET_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    ExpenseBudgetTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ExpenseBudgetTypeEnum toEnum(int value) {
        return BUDGET_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ExpenseBudgetTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
