package cn.rjtech.enums;

import java.util.*;

public enum SourceTypeEnum {
    /**
     * 枚举列表
     */
    MATERIAL_PLAN_TYPE(2, "物料到货计划"),
    BLANK_PURCHASE_TYPE(1, "空白采购");
    
    private static final Map<Integer, SourceTypeEnum> SOURCE_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (SourceTypeEnum sourceTypeEnum : SourceTypeEnum.values()) {
            SOURCE_TYPE_ENUM_MAP.put(sourceTypeEnum.value, sourceTypeEnum);
        }
    }
    
    private final int value;
    private final String text;
    
    SourceTypeEnum(int value, String text) {
        this.value = value;
        this.text  = text;
    }
    
    public static SourceTypeEnum toEnum(int value) {
        return SOURCE_TYPE_ENUM_MAP.get(value);
    }
    
    public int getValue() {
        return value;
    }
    
    public String getText() {
        return text;
    }
    
    @Override
    public String toString() {
        return "SourceTypeEnum{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }
}
