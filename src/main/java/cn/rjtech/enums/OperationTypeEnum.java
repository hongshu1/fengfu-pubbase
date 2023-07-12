package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 工序类型：串序，并序
 * @author timi
 */
public enum OperationTypeEnum {
   
    bunchSequence("串序", 1),
    parallelSequence("并序", 2);
    
    private final String text;
    private final int value;
    
    private static final Map<Integer, OperationTypeEnum> OPERATION_TYPE_MAP = new HashMap<>();
    
    static {
        for (OperationTypeEnum operationTypeEnum : OperationTypeEnum.values()) {
            OPERATION_TYPE_MAP.put(operationTypeEnum.value, operationTypeEnum);
        }
    }
    
    public static OperationTypeEnum toEnum(int value) {
        return OPERATION_TYPE_MAP.get(value);
    }
    
    OperationTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    
    public String getText() {
        return text;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "OperationTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
