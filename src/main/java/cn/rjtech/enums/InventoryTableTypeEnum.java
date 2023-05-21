package cn.rjtech.enums;

import java.util.*;

/**
 * 存货档案提交table名称：inventoryWorkRegion 所属生产线，inventorRouting 工艺路线（工序）
 * @author timi
 */
public enum InventoryTableTypeEnum {
    
    INVENTORYWORKREGION("inventoryWorkRegion", 1),
    INVENTORROUTING("inventorRouting", 2);
    
    private final String text;
    private final int value;
    
    private static final Map<Integer, InventoryTableTypeEnum> INVENTORY_TABLE_TYPE_MAP = new HashMap<>();
    
    static {
        for (InventoryTableTypeEnum inventoryTableTypeEnum : InventoryTableTypeEnum.values()) {
            INVENTORY_TABLE_TYPE_MAP.put(inventoryTableTypeEnum.value, inventoryTableTypeEnum);
        }
    }
    
    InventoryTableTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "InventoryTableTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
    
    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }
}
