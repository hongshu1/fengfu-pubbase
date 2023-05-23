package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块枚举
 *
 * @author Xiaolx
 */
public enum ItemEnum {
    /**
     * 枚举列表
     */
    EXPENSE_BUDGET("费用预算", "01"),
    EXPENSE_BUDGET_APPENDITEM("费用预算-追加项目", "012"),
    INVESTMENT_PLAN("投资计划", "02"),
    INVESTMENT_PLAN_APPENDITEM("投资计划-追加项目", "022"),
    PROPOSAL("禀议书", "03"),
    //PROPOSAL2("禀议书-追加", "032"),
    PURCHASE("申购单", "04"),
    PROJECT("项目档案", "05");
    private final String text;
    private final String value;

    private static final Map<String, ItemEnum> ITEM_ENUM_MAP = new HashMap<>();

    static {
        for (ItemEnum barCodeEnum : ItemEnum.values()) {
            ITEM_ENUM_MAP.put(barCodeEnum.value, barCodeEnum);
        }
    }

    ItemEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BarCodeEnum{" + "text='" + text + '\'' + ", value=" + value + '}';
    }

    public static ItemEnum toEnum(String value) {
        return ITEM_ENUM_MAP.get(value);
    }

}
