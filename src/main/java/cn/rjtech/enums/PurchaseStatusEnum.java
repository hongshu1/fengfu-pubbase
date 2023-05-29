package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 申购单推送状态
 *
 * @author Kephon
 */
public enum PurchaseStatusEnum {

    /**
     * 枚举列表
     */
	NOPUSH("未推送", 1),
    PUSH("已推送", 2);

    private static final Map<Integer, PurchaseStatusEnum> TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (PurchaseStatusEnum typeEnum : PurchaseStatusEnum.values()) {
            TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    PurchaseStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static PurchaseStatusEnum toEnum(int value) {
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
        return "PurchaseStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
