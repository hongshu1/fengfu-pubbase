package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符类型
 *
 * @author Kephon
 */
public enum CusfieldsMappingCharEnum {
    /**
     * 枚举列表
     */
    CODE("编码", 1),
    SEPARATOR("分隔符", 2);

    private static final Map<Integer, CusfieldsMappingCharEnum> CUSFIELDS_MAPPING_CHAR_ENUM_MAP = new HashMap<>();
    
    static {
        for (CusfieldsMappingCharEnum charEnum : CusfieldsMappingCharEnum.values()) {
            CUSFIELDS_MAPPING_CHAR_ENUM_MAP.put(charEnum.value, charEnum);
        }
    }

    private final String text;
    private final int value;

    CusfieldsMappingCharEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static CusfieldsMappingCharEnum toEnum(int value) {
        return CUSFIELDS_MAPPING_CHAR_ENUM_MAP.get(value);
    } 

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CusfieldsMappingCharEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
