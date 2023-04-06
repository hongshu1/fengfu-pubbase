package cn.rjtech.admin.scheduproductplan;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ScheduProductYearViewDTO implements Serializable {

    //客户ID
    private Long iCustomerId;
    //客户编码
    private String cCusCode;
    //机型ID
    private Long iEquipmentModelId;
    //机型编码
    private String cEquipmentModelCode;
    //存货ID
    private Long iInventoryId;
    //存货编码
    private String cInvCode;
    //客户部番
    private String cInvCode1;
    //部品名称
    private String cInvName1;
    //当前年份
    private String nowyear;
    //当前年份1月的数量
    private BigDecimal nowmonth1;
    private BigDecimal nowmonth2;
    private BigDecimal nowmonth3;
    private BigDecimal nowmonth4;
    private BigDecimal nowmonth5;
    private BigDecimal nowmonth6;
    private BigDecimal nowmonth7;
    private BigDecimal nowmonth8;
    private BigDecimal nowmonth9;
    private BigDecimal nowmonth10;
    private BigDecimal nowmonth11;
    private BigDecimal nowmonth12;
    //当前年份1月-12月的数量汇总
    private BigDecimal nowMonthSum;
    //下一年份
    private String nextyear;
    //下一年份1月的数量
    private BigDecimal nextmonth1;
    private BigDecimal nextmonth2;
    private BigDecimal nextmonth3;
    //下一年份1月-3月的数量汇总
    private BigDecimal nextmonthSum;

    //计划类型编码（PP:计划使用数）
    private String planTypeCode;


    public Long getiCustomerId() {
        return iCustomerId;
    }

    public void setiCustomerId(Long iCustomerId) {
        this.iCustomerId = iCustomerId;
    }

    public String getcCusCode() {
        return cCusCode;
    }

    public void setcCusCode(String cCusCode) {
        this.cCusCode = cCusCode;
    }

    public Long getiEquipmentModelId() {
        return iEquipmentModelId;
    }

    public void setiEquipmentModelId(Long iEquipmentModelId) {
        this.iEquipmentModelId = iEquipmentModelId;
    }

    public String getcEquipmentModelCode() {
        return cEquipmentModelCode;
    }

    public void setcEquipmentModelCode(String cEquipmentModelCode) {
        this.cEquipmentModelCode = cEquipmentModelCode;
    }

    public Long getiInventoryId() {
        return iInventoryId;
    }

    public void setiInventoryId(Long iInventoryId) {
        this.iInventoryId = iInventoryId;
    }

    public String getcInvCode() {
        return cInvCode;
    }

    public void setcInvCode(String cInvCode) {
        this.cInvCode = cInvCode;
    }

    public String getcInvCode1() {
        return cInvCode1;
    }

    public void setcInvCode1(String cInvCode1) {
        this.cInvCode1 = cInvCode1;
    }

    public String getcInvName1() {
        return cInvName1;
    }

    public void setcInvName1(String cInvName1) {
        this.cInvName1 = cInvName1;
    }

    public String getNowyear() {
        return nowyear;
    }

    public void setNowyear(String nowyear) {
        this.nowyear = nowyear;
    }

    public BigDecimal getNowmonth1() {
        return nowmonth1;
    }

    public void setNowmonth1(BigDecimal nowmonth1) {
        this.nowmonth1 = nowmonth1;
    }

    public BigDecimal getNowmonth2() {
        return nowmonth2;
    }

    public void setNowmonth2(BigDecimal nowmonth2) {
        this.nowmonth2 = nowmonth2;
    }

    public BigDecimal getNowmonth3() {
        return nowmonth3;
    }

    public void setNowmonth3(BigDecimal nowmonth3) {
        this.nowmonth3 = nowmonth3;
    }

    public BigDecimal getNowmonth4() {
        return nowmonth4;
    }

    public void setNowmonth4(BigDecimal nowmonth4) {
        this.nowmonth4 = nowmonth4;
    }

    public BigDecimal getNowmonth5() {
        return nowmonth5;
    }

    public void setNowmonth5(BigDecimal nowmonth5) {
        this.nowmonth5 = nowmonth5;
    }

    public BigDecimal getNowmonth6() {
        return nowmonth6;
    }

    public void setNowmonth6(BigDecimal nowmonth6) {
        this.nowmonth6 = nowmonth6;
    }

    public BigDecimal getNowmonth7() {
        return nowmonth7;
    }

    public void setNowmonth7(BigDecimal nowmonth7) {
        this.nowmonth7 = nowmonth7;
    }

    public BigDecimal getNowmonth8() {
        return nowmonth8;
    }

    public void setNowmonth8(BigDecimal nowmonth8) {
        this.nowmonth8 = nowmonth8;
    }

    public BigDecimal getNowmonth9() {
        return nowmonth9;
    }

    public void setNowmonth9(BigDecimal nowmonth9) {
        this.nowmonth9 = nowmonth9;
    }

    public BigDecimal getNowmonth10() {
        return nowmonth10;
    }

    public void setNowmonth10(BigDecimal nowmonth10) {
        this.nowmonth10 = nowmonth10;
    }

    public BigDecimal getNowmonth11() {
        return nowmonth11;
    }

    public void setNowmonth11(BigDecimal nowmonth11) {
        this.nowmonth11 = nowmonth11;
    }

    public BigDecimal getNowmonth12() {
        return nowmonth12;
    }

    public void setNowmonth12(BigDecimal nowmonth12) {
        this.nowmonth12 = nowmonth12;
    }

    public BigDecimal getNowMonthSum() {
        return nowMonthSum;
    }

    public void setNowMonthSum(BigDecimal nowMonthSum) {
        this.nowMonthSum = nowMonthSum;
    }

    public String getNextyear() {
        return nextyear;
    }

    public void setNextyear(String nextyear) {
        this.nextyear = nextyear;
    }

    public BigDecimal getNextmonth1() {
        return nextmonth1;
    }

    public void setNextmonth1(BigDecimal nextmonth1) {
        this.nextmonth1 = nextmonth1;
    }

    public BigDecimal getNextmonth2() {
        return nextmonth2;
    }

    public void setNextmonth2(BigDecimal nextmonth2) {
        this.nextmonth2 = nextmonth2;
    }

    public BigDecimal getNextmonth3() {
        return nextmonth3;
    }

    public void setNextmonth3(BigDecimal nextmonth3) {
        this.nextmonth3 = nextmonth3;
    }

    public BigDecimal getNextmonthSum() {
        return nextmonthSum;
    }

    public void setNextmonthSum(BigDecimal nextmonthSum) {
        this.nextmonthSum = nextmonthSum;
    }

    public String getPlanTypeCode() {
        return planTypeCode;
    }

    public void setPlanTypeCode(String planTypeCode) {
        this.planTypeCode = planTypeCode;
    }
}
