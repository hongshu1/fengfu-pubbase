package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

public enum CompleteTypeEnum {
    
    /**
     * 状态：1. 未完成 2. 已完成
     */
    INCOMPLETE("未完成", 1),
    COMPLETE("已完成", 2);
    
    private static final Map<Integer, CompleteTypeEnum> COMPLETE_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (CompleteTypeEnum typeEnum : CompleteTypeEnum.values()) {
            COMPLETE_TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }
    
    private final String text;
    private final int value;
    
    CompleteTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    public static CompleteTypeEnum toEnum(int value) {
        return COMPLETE_TYPE_ENUM_MAP.get(value);
    }
    
    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }
    
    
    @Override
    public String toString() {
        return "CompleteTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
