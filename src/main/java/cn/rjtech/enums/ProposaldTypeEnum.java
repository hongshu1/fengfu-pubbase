package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 禀议项目类型
 *
 * @author Kephon
 */
public enum ProposaldTypeEnum {
    /**
     * 枚举列表
     */
    SOURCE("原禀议", 1),
    NEW("新增", 2);

    private static final Map<Integer, ProposaldTypeEnum> PROPOSALD_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (ProposaldTypeEnum typeEnum : ProposaldTypeEnum.values()) {
            PROPOSALD_TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    ProposaldTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ProposaldTypeEnum toEnum(int value) {
        return PROPOSALD_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ProposaldTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
