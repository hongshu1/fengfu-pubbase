package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum InvestmentSummaryGroupOptionEnum {

    /**
     * 枚举列表
     */
	IINVESTMENTTYPE("iinvestmenttype", "投资类型"),
	ICAREERTYPE("icareertype", "事业区分"),
	IINVESTMENTDISTINCTION("iinvestmentdistinction", "投资区分"),
	CASSETTYPE("cassettype", "资产类别"),
	CEDITTYPE("cedittype", "修订区分"),
	CDEPCODE("cdepcode", "部门"),
	IBUDGETYEAR("ibudgetyear", "预算年度"),
	IBUDGETTYPE("ibudgettype", "预算类型"),
	CMODELINVCCODE("cmodelinvccode", "机种"),
	CPARTS("cparts", "部品"),
	IITEMYEAR("iitemyear", "立项年份");

    private static final Map<String, InvestmentSummaryGroupOptionEnum> GROUP_OPTION_ENUM_MAP = new HashMap<>();

    static {
        for (InvestmentSummaryGroupOptionEnum optionEnum : InvestmentSummaryGroupOptionEnum.values()) {
            GROUP_OPTION_ENUM_MAP.put(optionEnum.sn, optionEnum);
        }
    }

    private final String sn;
    private final String name;

    InvestmentSummaryGroupOptionEnum(String sn, String name) {
        this.sn = sn;
        this.name = name;
    }

    public static InvestmentSummaryGroupOptionEnum toEnum(String sn) {
        return GROUP_OPTION_ENUM_MAP.get(sn);
    }

    public String getSn() {
        return sn;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "InvestmentSummaryGroupOptionEnum{" +
                "sn=" + sn +
                ", name=" + name +
                '}';
    }

}
