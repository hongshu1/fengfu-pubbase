package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

public enum CodingDTypeEnum {
    
    serialNumberType("1", "流水号"),
    inputType("2", "手工输入"),
    yearType("3", "4位年"),
    year2Type("4", "2位年"),
    monthType("5", "2位月"),
    dateType("6", "2位日");
    private static final Map<String, CodingDTypeEnum> CODING_D_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (CodingDTypeEnum codingDTypeEnum : CodingDTypeEnum.values()) {
            CODING_D_TYPE_ENUM_MAP.put(codingDTypeEnum.value, codingDTypeEnum);
        }
    }
    
    private final String value;
    private final String text;
    
    CodingDTypeEnum(String value, String text) {
        this.value = value;
        this.text  = text;
    }
    
    public static CodingDTypeEnum toEnum(String value) {
        return CODING_D_TYPE_ENUM_MAP.get(value);
    }
    
    @Override
    public String toString() {
        return "CodingDTypeEnum{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }
    
    public String getValue() {
        return value;
    }
    
    public String getText() {
        return text;
    }
}

