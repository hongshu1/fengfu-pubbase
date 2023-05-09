package cn.rjtech.enums;

/**
 * 年度订单状态
 *
 * @author Kephon
 */
public enum AnnualOrderStatusEnum {
    /**
     * 枚举列表
     */
    SAVED("已保存", 1),
    AUDITTING("待审核", 2),
    AUDITTED("已审批", 3);

    private final String text;
    private final int value;

    AnnualOrderStatusEnum(String text, int value) {
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
        return "AnnualOrderStatus{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

}
