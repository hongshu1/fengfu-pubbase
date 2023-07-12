package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 材料类别
 */
public enum PartTypeEnum {
    
    ROLL_MATERIAL_TYPE("卷料", 1),
    TUBE_MATERIAL_TYPE("管材", 2),
    PLATE_ROLL_MATERIA_TYPE("板卷", 3);
    
    private final String text;
    private final int value;
    
    private static final Map<Integer, PartTypeEnum> PART_TYPE_ENUM_MAP = new HashMap<>();
    
    static {
        for (PartTypeEnum partTypeEnum : PartTypeEnum.values()) {
            PART_TYPE_ENUM_MAP.put(partTypeEnum.value, partTypeEnum);
        }
    }
    
    public static PartTypeEnum toEnum(int value) {
        return PART_TYPE_ENUM_MAP.get(value);
    }
    
    PartTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "PartTypeEnum{" +
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
