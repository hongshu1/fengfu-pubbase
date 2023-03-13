package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据权限 数据来源
 *
 * @author Kephon
 */
public enum DataSourceEnum {
    /**
     * 枚举列表
     */
    USER("用户", "1"),
    DEPT("部门", "2");

    private final String text;
    private final String value;

    private static final Map<String, DataSourceEnum> DATA_SOURCE_ENUM_MAP = new HashMap<>();

    static {
        for (DataSourceEnum sourceEnum : DataSourceEnum.values()) {
            DATA_SOURCE_ENUM_MAP.put(sourceEnum.value, sourceEnum);
        }
    }

    DataSourceEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static DataSourceEnum toEnum(String value) {
        return DATA_SOURCE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DataSourceEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
