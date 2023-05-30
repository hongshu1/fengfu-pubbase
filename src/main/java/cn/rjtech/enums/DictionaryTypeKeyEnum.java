package cn.rjtech.enums;

/**
 * 字典类型KEY
 *
 * @author Heming
 */
public enum DictionaryTypeKeyEnum {
    /**
     * 枚举列表
     */
    CAREERTYPE("事业类型", "career_type"),
    PURPOSE("目的区分", "purpose"),
	INVESTMENT_BUDGET_TYPE("投资计划预算类型", "investment_budget_type"),
	INVESTMENT_TYPE("投资类型", "investment_type"),
	CAREER_TYPE("事业区分", "career_type"),
	INVESTMENT_DISTINCTION("投资区分", "investment_distinction"),
	CASSETTYPE("资产类别", "asset_type"),
	PAYMENT_PROGRESS("实施阶段", "payment_progress"),
	EDITTYPE("修定区分", "edit_type"),
	INVESTMENTITEMTYPE("投资计划类型", "investment_item_type"),
	PERIOD_PROGRESS("日程", "period_progress"),
    AUDIT_STATUS("审核状态", "audit_status");
    private final String text;
    private final String value;

    DictionaryTypeKeyEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "DictionaryTypeKeyEnum{" +
                "value=" + value +
                ", text=" + text +
                '}';
    }

}
