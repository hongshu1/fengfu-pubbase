package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum InvestmentBudgetTypeEnum {

    /**
     * 投资计划预算类型枚举列表
     */
    FUALL_YEAR_BUDGET("全年预算", 1),
    NEXT_PERIOD_EDIT("下期修改", 2),
    ACTUAL("实绩", 3);

    private static final Map<Integer, InvestmentBudgetTypeEnum> BUDGET_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (InvestmentBudgetTypeEnum typeEnum : InvestmentBudgetTypeEnum.values()) {
            BUDGET_TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    InvestmentBudgetTypeEnum(String text, int value) {
        this.value = value;
        this.text = text;
    }

    public static InvestmentBudgetTypeEnum toEnum(int value) {
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
        return "InvestmentBudgetTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
