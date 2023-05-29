package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 全年预算对应列枚举
 * 用于映射导入模板对应列字段
 *
 * @author szh_public01
 */
public enum FullYearBudgetEnum {
    /**
     * 枚举列表
     */
	CBUDGETNO(2, "cbudgetno", "预算编号"),
	CHIGHESTSUBJECTNAME(3, "chighestsubjectname", "科目大类"),
	CLOWESTSUBJECTNAME(4, "clowestsubjectname", "明细科目"),
	CITEMNAME(5, "citemname", "名称"),
	CAREERTYPENAME(6, "careertypename", "事业类型"),
	ISLARGEAMOUNTEXPENSEDESC(7, "islargeamountexpensedesc", "是否大额费用"),
	CUSE(8, "cuse", "用途"),
	CMEMO(9, "cmemo", "备注"),
	IPRICE(10, "iprice", "单价"),
	CUNIT(11, "cunit", "单位");
    private static final Map<String, FullYearBudgetEnum> FULL_YEAR_BUDGET_FIELD_MAP = new HashMap<>();
    private static final Map<Integer, FullYearBudgetEnum> FULL_YEAR_BUDGET_VALUE_MAP = new HashMap<>();

    static {
        for (FullYearBudgetEnum investmentEnum : FullYearBudgetEnum.values()) {
            FULL_YEAR_BUDGET_FIELD_MAP.put(investmentEnum.getField(), investmentEnum);
            FULL_YEAR_BUDGET_VALUE_MAP.put(investmentEnum.getValue(), investmentEnum);
        }
    }

    public static FullYearBudgetEnum textOf(String field) {
        return FULL_YEAR_BUDGET_FIELD_MAP.get(field);
    }

    public static FullYearBudgetEnum valueOf(int value) {
        return FULL_YEAR_BUDGET_VALUE_MAP.get(value);
    }

    FullYearBudgetEnum(int value, String field, String text) {
        this.value = value;
        this.field = field;
        this.text = text;
    }

    private final int value;
    private final String text;
    private final String field;

    public String getField() {
        return field;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

}
