package cn.rjtech.enums;

import java.util.*;

public enum OrderGenTypeEnum {
    
    /**
     * 订单生产类型   0. 未生成 1. 采购订单 2. 委外订单
     */
    NOT_GEN("未生成", 0),
    PURCHASE_GEN("采购订单", 1),
    SUBCONTRACT_GEN("委外订单", 2);
    
    private static final Map<Integer, OrderGenTypeEnum> ORDER_GEN_TYPE_MAP = new HashMap<>();
    
    static {
        for (OrderGenTypeEnum typeEnum : OrderGenTypeEnum.values()) {
            ORDER_GEN_TYPE_MAP.put(typeEnum.value, typeEnum);
        }
    }
    
    private final String text;
    private final int value;
    
    OrderGenTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    public static OrderGenTypeEnum toEnum(int value) {
        return ORDER_GEN_TYPE_MAP.get(value);
    }
    
    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }
    
    
    @Override
    public String toString() {
        return "OrderGenTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
