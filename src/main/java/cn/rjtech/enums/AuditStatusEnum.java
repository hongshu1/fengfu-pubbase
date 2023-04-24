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
    NOT_AUDIT("未审核", 0),
    AWAIT_AUDIT("待审核", 1),
    APPROVED("审核通过", 2),
    REJECTED("审核不通过", 3);

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
