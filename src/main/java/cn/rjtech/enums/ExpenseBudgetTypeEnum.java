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
    FUALL_YEAR_BUDGET(1, "全年预算"),
    NEXT_PERIOD_EDIT(2, "下期修改");


    private static final Map<Integer, ExpenseBudgetTypeEnum> BUDGET_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (ExpenseBudgetTypeEnum statusEnum : ExpenseBudgetTypeEnum.values()) {
            BUDGET_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    ExpenseBudgetTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static ExpenseBudgetTypeEnum toEnum(int status) {
        return BUDGET_TYPE_ENUM_MAP.get(status);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "BudgetTypeEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
