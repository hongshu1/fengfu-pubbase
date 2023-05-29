package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 申购单参照类型
 *
 * @author Kephon
 */
public enum PurchaseRefTypeEnum {

    /**
     * 枚举列表
     */
    PROPOSAL("禀议书", 1),
    BUDGET("预算", 2);

    private static final Map<Integer, PurchaseRefTypeEnum> TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (PurchaseRefTypeEnum typeEnum : PurchaseRefTypeEnum.values()) {
            TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    PurchaseRefTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static PurchaseRefTypeEnum toEnum(int value) {
        return TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PurchaseRefTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
