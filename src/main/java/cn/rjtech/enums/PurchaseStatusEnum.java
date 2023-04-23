package cn.rjtech.enums;


import java.util.*;

public enum PurchaseStatusEnum {
    /**
     * 枚举列表   1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭
     */
    NOT_AUDIT("已保存", 1),
    AWAIT_AUDIT("待审核", 2),
    APPROVED("已审核", 3),
    REJECTED("审核不通过", 4),
    GENERATE_CASH_TICKETS("已生成现品票", 5),
    CLOSE("已关闭", 6);
    
    private static final Map<Integer, PurchaseStatusEnum> PURCHASE_STATUS_ENUM_MAP = new HashMap<>();
    
    static {
        for (PurchaseStatusEnum statusEnum : PurchaseStatusEnum.values()) {
            PURCHASE_STATUS_ENUM_MAP.put(statusEnum.value, statusEnum);
        }
    }
    
    private final String text;
    private final int value;
  
    PurchaseStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    public static PurchaseStatusEnum toEnum(int value) {
        return PURCHASE_STATUS_ENUM_MAP.get(value);
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
