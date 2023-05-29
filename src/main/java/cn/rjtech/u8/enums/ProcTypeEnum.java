package cn.rjtech.u8.enums;

/**
 * 操作类型枚举
 *
 * @author Kephon
 */
public enum ProcTypeEnum {
    /**
     * 枚举列表
     */
    ADD("add"),
    MODIFY("modify"),
    QUERY("query"),
    DELETE("delete");

    private final String text;

    ProcTypeEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "ProcTypeEnum{" +
                "text='" + text + '\'' +
                '}';
    }

}
