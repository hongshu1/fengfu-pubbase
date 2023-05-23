package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 审批方式
 *
 * @author Kephon
 */
public enum AuditWayEnum {
    /**
     * 枚举列表
     */
    STATUS("审核", 1),
    FLOW("审批流", 2);

    private static final Map<Integer, AuditWayEnum> AUDIT_WAY_ENUM_MAP = new HashMap<>();
    
    static {
        for (AuditWayEnum auditWayEnum : AuditWayEnum.values()) {
            AUDIT_WAY_ENUM_MAP.put(auditWayEnum.value, auditWayEnum);
        }
    }

    private final String text;
    private final int value;

    AuditWayEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static AuditWayEnum toEnum(int value) {
        return AUDIT_WAY_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AuditWayEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
