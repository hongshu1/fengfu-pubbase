package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum SourceTypeEnum {
    /**
     * 枚举列表
     */
    BLANK_PURCHASE_TYPE("空白采购", 1),
    MATERIAL_PLAN_TYPE("物料到货计划", 2);
    
    private static final Map<Integer, SourceTypeEnum> SOURCE_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (SourceTypeEnum sourceTypeEnum : SourceTypeEnum.values()) {
            SOURCE_TYPE_ENUM_MAP.put(sourceTypeEnum.value, sourceTypeEnum);
        }
    }

    private final String text;
    private final int value;
    
    SourceTypeEnum(String text, int value) {
        this.value = value;
        this.text  = text;
    }
    
    public static SourceTypeEnum toEnum(int value) {
        return SOURCE_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SourceTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
