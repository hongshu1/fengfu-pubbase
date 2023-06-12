package cn.rjtech.enums;

import java.util.*;

public enum BomSourceTypeEnum {
    
    MANUAL_TYPE_ADD(2, "手工新增"),
    IMPORT_TYPE_ADD(1, "导入新增");
    
    private static final Map<Integer, BomSourceTypeEnum> BOM_SOURCE_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (BomSourceTypeEnum bomSourceTypeEnum : BomSourceTypeEnum.values()) {
            BOM_SOURCE_TYPE_ENUM_MAP.put(bomSourceTypeEnum.value, bomSourceTypeEnum);
        }
    }
    
    private final int value;
    private final String text;
    
    BomSourceTypeEnum(int value, String text) {
        this.value = value;
        this.text  = text;
    }
    
    public static BomSourceTypeEnum toEnum(int value) {
        return BOM_SOURCE_TYPE_ENUM_MAP.get(value);
    }
    
    public int getValue() {
        return value;
    }
    
    public String getText() {
        return text;
    }
    
    @Override
    public String toString() {
        return "BomSourceTypeEnum{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }
}
