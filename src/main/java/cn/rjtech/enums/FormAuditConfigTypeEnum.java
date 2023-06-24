package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 单据审批配置
 *
 * @author Kephon
 */
public enum FormAuditConfigTypeEnum {
    /**
     * 枚举列表
     */
    FLOW("审批流", 1),
    STATUS("审核状态", 2);

    private static final Map<Integer, FormAuditConfigTypeEnum> TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (FormAuditConfigTypeEnum typeEnum : FormAuditConfigTypeEnum.values()) {
            TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    FormAuditConfigTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static FormAuditConfigTypeEnum toEnum(int value) {
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
        return "AuditConfigType{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
