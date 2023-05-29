package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

public enum BarCodeEnum {
    /**
     * 枚举列表
     */
    FIXED("固定值", "FIXED"),
    DEPT("部门(U8部门英文简称)", "DEPT"),
    TYPE("投资类型", "TYPE"),
    ORDERYEAR("单据年份","ORDERYEAR"),
    SYSTEMDATE("系统日期","SYSTEMDATE"),
    NUM("流水号", "NUM");


    private final String text;
    private final String value;


    private static final Map<String, BarCodeEnum> BAR_CODE_ENUM_MAP = new HashMap<>();

    static {
        for (BarCodeEnum barCodeEnum : BarCodeEnum.values()) {
            BAR_CODE_ENUM_MAP.put(barCodeEnum.value, barCodeEnum);
        }
    }

    BarCodeEnum(String text, String value) {
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
        return "BarCodeEnum{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

    public static BarCodeEnum toEnum(String status) {
        return BAR_CODE_ENUM_MAP.get(status);
    }




}
