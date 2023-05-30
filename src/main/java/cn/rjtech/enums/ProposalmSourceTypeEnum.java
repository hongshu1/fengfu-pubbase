package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 禀议书来源类型
 *
 * @author Kephon
 */
public enum ProposalmSourceTypeEnum {

    /**
     * 枚举列表
     */
    EXPENSE_BUDGET("费用预算", 1),
    INVESTMENT_PLAN("投资计划", 2);

    private static final Map<Integer, ProposalmSourceTypeEnum> TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (ProposalmSourceTypeEnum typeEnum : ProposalmSourceTypeEnum.values()) {
            TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    ProposalmSourceTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ProposalmSourceTypeEnum toEnum(int value) {
        return TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PrposalmSourceTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
