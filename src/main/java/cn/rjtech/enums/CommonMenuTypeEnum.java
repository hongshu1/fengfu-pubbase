package cn.rjtech.enums;

import java.util.*;

public enum CommonMenuTypeEnum {
    /**
     * 枚举列表
     */
    PROPOSAL(1, "禀议"),
    MOM(2, "mom平台");
    
    private static final Map<Integer, CommonMenuTypeEnum> COMMONMENU_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (CommonMenuTypeEnum commonMenuTypeEnum : CommonMenuTypeEnum.values()) {
        	COMMONMENU_TYPE_ENUM_MAP.put(commonMenuTypeEnum.value, commonMenuTypeEnum);
        }
    }
    
    private final int value;
    private final String text;
    
    CommonMenuTypeEnum(int value, String text) {
        this.value = value;
        this.text  = text;
    }
    
    public static CommonMenuTypeEnum toEnum(int value) {
        return COMMONMENU_TYPE_ENUM_MAP.get(value);
    }
    
    public int getValue() {
        return value;
    }
    
    public String getText() {
        return text;
    }
    
    @Override
    public String toString() {
        return "CommonMenuTypeEnum{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }
}
