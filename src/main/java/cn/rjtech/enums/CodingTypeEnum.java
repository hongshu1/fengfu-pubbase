package cn.rjtech.enums;

import java.util.*;

public enum CodingTypeEnum {
    
    autoCodeType(1, "自动生产编码"),
    manualCodeType(2, "完全手工编码");
    
    private static final Map<Integer, CodingTypeEnum> CODING_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (CodingTypeEnum codingTypeEnum : CodingTypeEnum.values()) {
            CODING_TYPE_ENUM_MAP.put(codingTypeEnum.value, codingTypeEnum);
        }
    }
    
    private final int value;
    private final String text;
    
    CodingTypeEnum(int value, String text) {
        this.value = value;
        this.text  = text;
    }
    
    public static CodingTypeEnum toEnum(int value) {
        return CODING_TYPE_ENUM_MAP.get(value);
    }
    
    @Override
    public String toString() {
        return "CodingTypeEnum{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }
    
    public int getValue() {
        return value;
    }
    
    public String getText() {
        return text;
    }
}
