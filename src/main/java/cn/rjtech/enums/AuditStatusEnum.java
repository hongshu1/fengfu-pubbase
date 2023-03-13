package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核状态
 *
 * @author Kephon
 */
public enum AuditStatusEnum {
    /**
     * 枚举列表
     */
    AWAIT_AUDIT("待审核", 0),
    APPROVED("同意", 1),
    REJECTED("不同意", 2);

    private static final Map<Integer, AuditStatusEnum> AUDIT_STATUS_ENUM_MAP = new HashMap<>();

    static {
        for (AuditStatusEnum statusEnum : AuditStatusEnum.values()) {
            AUDIT_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }

    private final String text;
    private final int value;

    AuditStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static AuditStatusEnum toEnum(int value) {
        return AUDIT_STATUS_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AuditStatusEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
