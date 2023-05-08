package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/8 14:06
 * @Description 整体外观的枚举
 */
public enum AppearanceEnum {

    /**
     * 枚举列表
     */
    FIRST(0, "无异常毛刺"),
    SECOND(1, "拉花"),
    THIRD(2, "变形"),
    FOURTH(3, "压伤"),
    FIFTH(4, "开裂"),
    SIXTH(5, "起皱");

    private static final Map<Integer, AppearanceEnum> SOURCE_ENUM_MAP = new HashMap<>();

    static {
        for (AppearanceEnum appearanceEnum : AppearanceEnum.values()) {
            SOURCE_ENUM_MAP.put(appearanceEnum.value, appearanceEnum);
        }
    }

    private final int    value;
    private final String text;

    AppearanceEnum(int value, String text) {
        this.value = value;
        this.text  = text;
    }

    public static AppearanceEnum toEnum(int value) {
        return SOURCE_ENUM_MAP.get(value);
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "AppearanceEnum{" +
            "value=" + value +
            ", text='" + text + '\'' +
            '}';
    }
}
