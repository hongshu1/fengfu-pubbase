package cn.rjtech.enums;
import java.util.HashMap;
import java.util.Map;

/**
 * 投资计划对应列枚举
 * 用于映射表格列对应字段
 *
 * @author szh_public01
 */
public enum InvestmentEnum {
    /**
     * 枚举列表
     */
	IINVESTMENTTYPE(0, "cinvestmenttypedesc", "投资类型"),
	CPRODUCTLINE(1, "cproductline", "生产线"),
	CMODELINVCCODE(2, "cmodelinvccode", "机种"),
	CPARTS(3, "cparts", "部品"),
	ICAREERTYPE(4, "careertypedesc", "事业区分"),
	IINVESTMENTDISTINCTION(5, "cinvestmentdistinctiondesc", "投资区分"),
	CPLANNO(6, "cplanno", "投资NO."),
	CITEMNAME(7, "citemname", "项目细项名称"),
	ISIMPORT(8, "isimportdesc", "是否进口"),
	IQUANTITY(9, "iquantity", "数量"),
	CUNIT(10, "cunit", "单位"),
	CASSETTYPE(11, "cassettypedesc", "资产类别"),
	CPURPOSE(12, "cpurpose", "目的"),
	CEFFECTAMOUNT(13, "ceffectamount", "效果金额"),
	CRECLAIMYEAR(14, "creclaimyear", "回收年限"),
	CLEVEL(15, "clevel", "等级区分"),
	ISPRIORREPORT(16, "ispriorreportdesc", "是否需要做事前报告"),
	CPAYMENTPROGRESS(17, "cpaymentprogressdesc", "实施阶段"),
	ITAXRATE(18, "itaxrate", "税率"),
	ITOTALAMOUNTPLAN(19, "itotalamountplan", "项目总金额-计划"),
	ITOTALAMOUNTACTUAL(20, "itotalamountactual", "项目总金额-实绩/预测/修订"),
	ITOTALAMOUNTDIFF(21, "itotalamountdiff", "项目总金额-差异"),
	ITOTALAMOUNTDIFFREASON(22, "itotalamountdiffreason", "项目总金额-差异原因"),
	IYEARTOTALAMOUNTPLAN(23, "iyeartotalamountplan", "2022年总金额-计划"),
	IYEARTOTALAMOUNTACTUAL(24, "iyeartotalamountactual", "2022年总金额-实绩/预测/修订"),
	IYEARTOTALAMOUNTDIFF(25, "iyeartotalamountdiff", "2022年总金额-差异原因"),
	IYEARTOTALAMOUNTDIFFREASON(26, "iyeartotalamountdiffreason", "2022年总金额-差异原因"),
	CEDITTYPE(27, "cedittypedesc", "修订区分"),
	CMEMO(28, "cmemo", "备注"),
	IITEMYEAR(29, "iitemyear", "立项年份"),
	CPERIODPROGRESS1(30, "cperiodprogressdesc1", "第1期-日程"),
	DPERIODDATE1(31, "dperioddate1", "第1期-日期"),
    IAMOUNT1(32, "iamount1", "第1期-金额"),
    CPERIODPROGRESS2(33, "cperiodprogressdesc2", "第2期-日程"),
    DPERIODDATE2(34, "dperioddate2", "第2期-日期"),
    IAMOUNT2(35, "iamount2", "第2期-金额"),
    CPERIODPROGRESS3(36, "cperiodprogressdesc3", "第3期-日程"),
    DPERIODDATE3(37, "dperioddate3", "第3期-日期"),
    IAMOUNT3(38, "iamount3", "第3期-金额"),
    CPERIODPROGRESS4(39, "cperiodprogressdesc4", "第4期-日程"),
    DPERIODDATE4(40, "dperioddate4", "第4期-日期"),
    IAMOUNT4(41, "iamount4", "第4期-金额"),
    CPERIODPROGRESS5(42, "cperiodprogressdesc5", "第5期-日程"),
    DPERIODDATE5(43, "dperioddate5", "第5期-日期"),
    IAMOUNT5(44, "iamount5", "第5期-金额"),
    CPERIODPROGRESS6(45, "cperiodprogressdesc6", "第6期-日程"),
    DPERIODDATE6(46, "dperioddate6", "第6期-日期"),
    IAMOUNT6(47, "iamount6", "第6期-金额");

    private static final Map<String, InvestmentEnum> INVENT_PLAN_EXCLE_TEXT_MAP = new HashMap<>();
    private static final Map<Integer, InvestmentEnum> INVENT_PLAN_EXCLE_VALUE_MAP = new HashMap<>();

    static {
        for (InvestmentEnum investmentEnum : InvestmentEnum.values()) {
            INVENT_PLAN_EXCLE_TEXT_MAP.put(investmentEnum.getField(), investmentEnum);
            INVENT_PLAN_EXCLE_VALUE_MAP.put(investmentEnum.getValue(), investmentEnum);
        }
    }

    public static InvestmentEnum textOf(String field) {
        return INVENT_PLAN_EXCLE_TEXT_MAP.get(field);
    }

    public static InvestmentEnum valueOf(int value) {
        return INVENT_PLAN_EXCLE_VALUE_MAP.get(value);
    }

    InvestmentEnum(int value, String field, String text) {
        this.value = value;
        this.field = field;
        this.text = text;
    }

    private final int value;
    private final String field;
    private final String text;

    public String getField() {
        return field;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

}
