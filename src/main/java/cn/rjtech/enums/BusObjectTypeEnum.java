package cn.rjtech.enums;

/**
 * 业务对象类型
 *
 * @author Kephon
 */
public enum BusObjectTypeEnum {
    /**
     * 枚举列表
     */
    USER("用户", "01"),
    DEPTARTMENT("部门", "02");

    private final String text;
    private final String value;

    BusObjectTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BusObjectTypeEnum{" +
                "text='" + text + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
