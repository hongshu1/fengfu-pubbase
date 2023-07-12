package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

public enum FormTypeEnum {
    
    documentType("1", "单据类型"),
    barCodeType("2", "条码号");
    private static final Map<String, FormTypeEnum> FORM_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (FormTypeEnum formTypeEnum : FormTypeEnum.values()) {
            FORM_TYPE_ENUM_MAP.put(formTypeEnum.value, formTypeEnum);
        }
    }
    
    private final String value;
    private final String text;
    
    FormTypeEnum(String value, String text) {
        this.value = value;
        this.text  = text;
    }
    
    public static FormTypeEnum toEnum(String value) {
        return FORM_TYPE_ENUM_MAP.get(value);
    }
    
    @Override
    public String toString() {
        return "FormTypeEnum{" +
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
