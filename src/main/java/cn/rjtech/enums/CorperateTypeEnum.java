package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 法人类型
 *
 * @author Kephon
 */
public enum CorperateTypeEnum {

    /**
     * 枚举列表
     */
    CORPERATE_Y("法人机构", "1"),
    CORPERATE_N("非法人机构", "2");

    private static final Map<String, CorperateTypeEnum> CORPERATE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (CorperateTypeEnum typeEnum : CorperateTypeEnum.values()) {
            CORPERATE_TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final String value;

    CorperateTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static CorperateTypeEnum toEnum(String value) {
        return CORPERATE_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CorperateTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
