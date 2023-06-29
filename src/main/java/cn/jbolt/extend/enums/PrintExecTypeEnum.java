package cn.jbolt.extend.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 打印
 *
 * @author Kephon
 */
public enum PrintExecTypeEnum {
    /**
     * 枚举类型
     */
    SQL("sql", 1),
    JAVA("java", 2);

    private static final Map<Integer, PrintExecTypeEnum> PRINT_EXEC_TYPE_ENUM_MAP = new HashMap<>();

    static {
        for (PrintExecTypeEnum typeEnum : PrintExecTypeEnum.values()) {
            PRINT_EXEC_TYPE_ENUM_MAP.put(typeEnum.value, typeEnum);
        }
    }

    private final String text;
    private final int value;

    PrintExecTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static PrintExecTypeEnum toEnum(int value) {
        return PRINT_EXEC_TYPE_ENUM_MAP.get(value);
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PrintExecTypeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
