package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字段类型
 *
 * @author Kephon
 */
public enum FormFieldEnum {
    /**
     * 枚举列表 1. 字符类型 2. 数值类型 3. 日期类型 4. 长整型
     */
    STRING("字符类型", "1"),
    NUMBER("数值类型", "2"),
    DATE("日期类型", "3"),
    LONG("长整型", "4");

    private static final Map<String, FormFieldEnum> FORM_FIELD_ENUM_MAP = new HashMap<>();
    
    static {
        for (FormFieldEnum formFieldEnum : FormFieldEnum.values()) {
            FORM_FIELD_ENUM_MAP.put(formFieldEnum.value, formFieldEnum);
        }
    }

    private final String text;
    private final String value;

    FormFieldEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static FormFieldEnum toEnum(String value) {
        return FORM_FIELD_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FormFieldEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
