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
    FUALL_YEAR_BUDGET(1, "全年预算a"),
    NEXT_PERIOD_EDIT(2, "下半年修订b"),
	ACTUALBUDGET(3, "实绩预测d"),
	ACTUAL(4, "实绩e"),
	DIFF_A(5, "差异A（b-a）"),
	DIFF_E(6, "差异E（d-a）"),
	DIFF_F(7, "差异F（d-b）"),
	DIFF_G(8, "差异G（e-a）"),
	DIFF_H(9, "差异H（e-b）"),
	DIFF_J(10, "差异J（e-d）");

    private static final Map<Integer, DiffReportBudgetTypeEnum> DiffReportBudgetTypeEnum_MAP = new HashMap<>();

    static {
        for (DiffReportBudgetTypeEnum statusEnum : DiffReportBudgetTypeEnum.values()) {
        	DiffReportBudgetTypeEnum_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    DiffReportBudgetTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static DiffReportBudgetTypeEnum toEnum(int status) {
        return DiffReportBudgetTypeEnum_MAP.get(status);
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
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
