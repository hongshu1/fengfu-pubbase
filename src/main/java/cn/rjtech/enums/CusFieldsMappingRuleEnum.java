package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 导入映射字段规则
 *
 * @author Kephon
 */
public enum CusFieldsMappingRuleEnum {
    /**
     * 枚举列表
     */
    NONE("非定制", 1),
    ANNUAL("年度", 2),
    MONTHLY("月度", 3),
    WEEKLY("周间", 4);

    private static final Map<Integer, CusFieldsMappingRuleEnum> CUS_FIELDS_MAPPING_RULE_ENUM_MAP = new HashMap<>();
    
    static {
        for (CusFieldsMappingRuleEnum ruleEnum : CusFieldsMappingRuleEnum.values()) {
            CUS_FIELDS_MAPPING_RULE_ENUM_MAP.put(ruleEnum.value, ruleEnum);
        }
    }

    private final String text;
    private final int value;

    CusFieldsMappingRuleEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static CusFieldsMappingRuleEnum toEnum(int value) {
        return CUS_FIELDS_MAPPING_RULE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CusFieldsMappingRuleEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
