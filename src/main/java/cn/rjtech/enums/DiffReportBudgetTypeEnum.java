package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum DiffReportBudgetTypeEnum {

    /**
     * 投资计划预算类型枚举列表
     */
    FUALL_YEAR_BUDGET("全年预算a", 1),
    NEXT_PERIOD_EDIT("下半年修订b", 2),
    ACTUALBUDGET("实绩预测d", 3),
    ACTUAL("实绩e", 4),
    DIFF_A("差异A（b-a）", 5),
    DIFF_E("差异E（d-a）", 6),
    DIFF_F("差异F（d-b）", 7),
    DIFF_G("差异G（e-a）", 8),
    DIFF_H("差异H（e-b）", 9),
    DIFF_J("差异J（e-d）", 10);

    private static final Map<Integer, DiffReportBudgetTypeEnum> REPORT_BUDGET_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (DiffReportBudgetTypeEnum statusEnum : DiffReportBudgetTypeEnum.values()) {
            REPORT_BUDGET_TYPE_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    DiffReportBudgetTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static DiffReportBudgetTypeEnum toEnum(int value) {
        return REPORT_BUDGET_TYPE_ENUM_MAP.get(value);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "DiffReportBudgetTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
