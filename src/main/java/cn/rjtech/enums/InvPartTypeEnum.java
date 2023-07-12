package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

public enum InvPartTypeEnum {
    
    inventory("存货", 1),
    virtual("虚拟件", 2);
    
    private final String text;
    private final int value;
    
    private static final Map<Integer, InvPartTypeEnum> INV_PART_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (InvPartTypeEnum invPartTypeEnum : InvPartTypeEnum.values()) {
            INV_PART_TYPE_ENUM_MAP.put(invPartTypeEnum.value, invPartTypeEnum);
        }
    }
    
    public static InvPartTypeEnum toEnum(int value) {
        return INV_PART_TYPE_ENUM_MAP.get(value);
    }
    
    InvPartTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "InvPartTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }
}
