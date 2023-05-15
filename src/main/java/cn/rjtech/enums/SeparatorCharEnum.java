package cn.rjtech.enums;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 分隔符
 *
 * @author Kephon
 */
public enum SeparatorCharEnum {
    /**
     * 枚举列表
     */
    SPACE(StrUtil.SPACE, "1"),
    DASH(StrUtil.DASHED, "2"),
    DOT(StrUtil.DOT, "3"),
    SLASH(StrUtil.SLASH, "4");

    private static final Map<String, SeparatorCharEnum> SEPARATOR_CHAR_ENUM_MAP = new HashMap<>();
    
    static {
        for (SeparatorCharEnum charEnum : SeparatorCharEnum.values()) {
            SEPARATOR_CHAR_ENUM_MAP.put(charEnum.value, charEnum);
        }
    }

    private final String text;
    private final String value;

    SeparatorCharEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static SeparatorCharEnum toEnum(String value) {
        return SEPARATOR_CHAR_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SeparatorCharEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
