package cn.rjtech.enums;

import java.util.*;

/**
 * 推送单据类型
 */
public enum PushToTypeEnum {
    
    U8(1, "U8");
    private final String text;
    private final int value;
    
    private static final Map<Integer, PushToTypeEnum> PUSH_TO_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (PushToTypeEnum pushToTypeEnum : PushToTypeEnum.values()) {
            PUSH_TO_TYPE_ENUM_MAP.put(pushToTypeEnum.value, pushToTypeEnum);
        }
    }
    
    PushToTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }
    
    @Override
    public String toString() {
        return "PushToTypeEnum{" +
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
