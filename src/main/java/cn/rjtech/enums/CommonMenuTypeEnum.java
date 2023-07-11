package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum CommonMenuTypeEnum {
    /**
     * 枚举列表
     */
    PROPOSAL("禀议", 1),
    MOM("mom平台", 2);
    
    private static final Map<Integer, CommonMenuTypeEnum> COMMONMENU_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (CommonMenuTypeEnum commonMenuTypeEnum : CommonMenuTypeEnum.values()) {
        	COMMONMENU_TYPE_ENUM_MAP.put(commonMenuTypeEnum.value, commonMenuTypeEnum);
        }
    }

    private final String text;
    private final int value;
    
    CommonMenuTypeEnum(String text, int value) {
        this.value = value;
        this.text  = text;
    }
    
    public static CommonMenuTypeEnum toEnum(int value) {
        return COMMONMENU_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CommonMenuTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
