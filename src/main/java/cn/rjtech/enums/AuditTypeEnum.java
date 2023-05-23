package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 审批类型枚举
 *
 * @author Kephon
 */
public enum AuditTypeEnum {
    /**
     * 审批
     */
    FLOW("审批流", 1),
    STATUS("审批", 2);

    private static final Map<Integer, AuditTypeEnum> AUDIT_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (AuditTypeEnum typeEnum : AuditTypeEnum.values()) {
            AUDIT_TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    AuditTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static AuditTypeEnum toEnum(int value) {
        return AUDIT_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AuditTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
}
