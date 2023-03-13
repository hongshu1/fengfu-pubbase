package cn.rjtech.enums;

/**
 * 审批角色之间的关系
 *
 * @author Kephon
 */
public enum AndOrEnum {
    /**
     * 枚举列表
     */
    OR("或", 0),
    AND("且", 1);

    private final String text;
    private final int value;

    AndOrEnum(String text, int value) {
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
        return "AndOrEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
