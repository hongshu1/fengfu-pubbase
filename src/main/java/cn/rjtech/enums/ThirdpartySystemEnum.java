package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方系统
 *
 * @author Kephon
 */
public enum ThirdpartySystemEnum {
    /**
     * 枚举列表
     */
    DING("钉钉", "1"),
    E_WECHAT("企业微信", "2"),
    ERP("ERP", "3"),
    U9_SUPPLIER("U9供应商", "4"),
    U9("U9","5"),
    U8("U8操作员", "6");

    private static final Map<String, ThirdpartySystemEnum> THIRDPARTY_SYSTEM_ENUM_MAP = new HashMap<>();

    static {
        for (ThirdpartySystemEnum systemEnum : ThirdpartySystemEnum.values()) {
            THIRDPARTY_SYSTEM_ENUM_MAP.put(systemEnum.value, systemEnum);
        }
    }

    private final String text;
    private final String value;

    ThirdpartySystemEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static ThirdpartySystemEnum toEnum(String value) {
        return THIRDPARTY_SYSTEM_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ThirdpartySystemEnum{" +
                "text='" + text + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
