package cn.rjtech.u8.pojo.req.purchaseorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Kephon
 */
@XmlRootElement(name = "header")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PurchaseorderHeader implements Serializable {

    /**
     * 订单日期	datetime 	非空	合法的公历日期
     */
    @XmlElement(name = "date")
    private String date;

    @XmlElement(name = "define2")
    private String define2;

    @XmlElement(name = "define1")
    private String define1;

    /**
     * 订单编号, 非空，必须唯一
     */
    @XmlElement(name = "code")
    private String code;

    @XmlElement(name = "paycondition_code")
    private String payconditionCode;

    @XmlElement(name = "define12")
    private String define12;

    /**
     * 部门编号	varchar (12) 	可空	非空必须取值于部门档案
     */
    @XmlElement(name = "deptcode")
    private String deptcode;

    @XmlElement(name = "define9")
    private String define9;

    @XmlElement(name = "define13")
    private String define13;

    @XmlElement(name = "remark")
    private String remark;

    @XmlElement(name = "define8")
    private String define8;

    @XmlElement(name = "define14")
    private String define14;

    @XmlElement(name = "define7")
    private String define7;

    @XmlElement(name = "define15")
    private String define15;

    @XmlElement(name = "define6")
    private String define6;

    @XmlElement(name = "bargain")
    private String bargain;

    @XmlElement(name = "define5")
    private String define5;

    @XmlElement(name = "tax_rate")
    private BigDecimal taxRate;

    @XmlElement(name = "define4")
    private String define4;

    @XmlElement(name = "define10")
    private String define10;

    @XmlElement(name = "define3")
    private String define3;

    @XmlElement(name = "define11")
    private String define11;

    @XmlElement(name = "currency_rate")
    private Double currencyRate;

    @XmlElement(name = "recsend_type")
    private String recsendType;

    /**
     * 供应商编号	varchar (12) 	非空	必须取值与供应商档案
     */
    @XmlElement(name = "vendorcode")
    private String vendorcode;

    @XmlElement(name = "traffic_money")
    private String trafficMoney;

    @XmlElement(name = "period")
    private String period;

    /**
     * 到货地址	varchar (60) 	可空
     */
    @XmlElement(name = "address")
    private String address;

    @XmlElement(name = "currency_name")
    private String currencyName;

    @XmlElement(name = "maker")
    private String maker;

    /**
     * 采购类型编码	varchar (2) 	可空	非空时必须取值于采购类型档案
     */
    @XmlElement(name = "purchase_type_code")
    private String purchaseTypeCode;

    @XmlElement(name = "define16")
    private String define16;

    /**
     * 业务员		varchar (8) 	可空	非空时必须取值于职员档案
     */
    @XmlElement(name = "personcode")
    private String personcode;

    /**
     * 业务类型		非空	必须取值于业务类型档案
     */
    @XmlElement(name = "operation_type_code")
    private String operationTypeCode;

    @XmlElement(name = "state")
    private Integer state;

    @XmlElement(name = "idiscounttaxtype")
    private Integer discountTaxType;

    @XmlElement(name = "venperson")
    private String venperson;

    @XmlElement(name = "venbank")
    private String venbank;

    @XmlElement(name = "venaccount")
    private String venaccount;

    @XmlElement(name = "verifystateex")
    private Integer verifystateex;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDefine2() {
        return define2;
    }

    public void setDefine2(String define2) {
        this.define2 = define2;
    }

    public String getDefine1() {
        return define1;
    }

    public void setDefine1(String define1) {
        this.define1 = define1;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPayconditionCode() {
        return payconditionCode;
    }

    public void setPayconditionCode(String payconditionCode) {
        this.payconditionCode = payconditionCode;
    }

    public String getDefine12() {
        return define12;
    }

    public void setDefine12(String define12) {
        this.define12 = define12;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getDefine9() {
        return define9;
    }

    public void setDefine9(String define9) {
        this.define9 = define9;
    }

    public String getDefine13() {
        return define13;
    }

    public void setDefine13(String define13) {
        this.define13 = define13;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDefine8() {
        return define8;
    }

    public void setDefine8(String define8) {
        this.define8 = define8;
    }

    public String getDefine14() {
        return define14;
    }

    public void setDefine14(String define14) {
        this.define14 = define14;
    }

    public String getDefine7() {
        return define7;
    }

    public void setDefine7(String define7) {
        this.define7 = define7;
    }

    public String getDefine15() {
        return define15;
    }

    public void setDefine15(String define15) {
        this.define15 = define15;
    }

    public String getDefine6() {
        return define6;
    }

    public void setDefine6(String define6) {
        this.define6 = define6;
    }

    public String getBargain() {
        return bargain;
    }

    public void setBargain(String bargain) {
        this.bargain = bargain;
    }

    public String getDefine5() {
        return define5;
    }

    public void setDefine5(String define5) {
        this.define5 = define5;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getDefine4() {
        return define4;
    }

    public void setDefine4(String define4) {
        this.define4 = define4;
    }

    public String getDefine10() {
        return define10;
    }

    public void setDefine10(String define10) {
        this.define10 = define10;
    }

    public String getDefine3() {
        return define3;
    }

    public void setDefine3(String define3) {
        this.define3 = define3;
    }

    public String getDefine11() {
        return define11;
    }

    public void setDefine11(String define11) {
        this.define11 = define11;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getRecsendType() {
        return recsendType;
    }

    public void setRecsendType(String recsendType) {
        this.recsendType = recsendType;
    }

    public String getVendorcode() {
        return vendorcode;
    }

    public void setVendorcode(String vendorcode) {
        this.vendorcode = vendorcode;
    }

    public String getTrafficMoney() {
        return trafficMoney;
    }

    public void setTrafficMoney(String trafficMoney) {
        this.trafficMoney = trafficMoney;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getPurchaseTypeCode() {
        return purchaseTypeCode;
    }

    public void setPurchaseTypeCode(String purchaseTypeCode) {
        this.purchaseTypeCode = purchaseTypeCode;
    }

    public String getDefine16() {
        return define16;
    }

    public void setDefine16(String define16) {
        this.define16 = define16;
    }

    public String getPersoncode() {
        return personcode;
    }

    public void setPersoncode(String personcode) {
        this.personcode = personcode;
    }

    public String getOperationTypeCode() {
        return operationTypeCode;
    }

    public void setOperationTypeCode(String operationTypeCode) {
        this.operationTypeCode = operationTypeCode;
    }

    public Integer getDiscountTaxType() {
        return discountTaxType;
    }

    public void setDiscountTaxType(Integer discountTaxType) {
        this.discountTaxType = discountTaxType;
    }

    public String getVenperson() {
        return venperson;
    }

    public void setVenperson(String venperson) {
        this.venperson = venperson;
    }

    public String getVenbank() {
        return venbank;
    }

    public void setVenbank(String venbank) {
        this.venbank = venbank;
    }

    public String getVenaccount() {
        return venaccount;
    }

    public void setVenaccount(String venaccount) {
        this.venaccount = venaccount;
    }

    public Integer getVerifystateex() {
        return verifystateex;
    }

    public void setVerifystateex(Integer verifystateex) {
        this.verifystateex = verifystateex;
    }

    @Override
    public String toString() {
        return "PurchaseorderHeader{" +
                "date='" + date + '\'' +
                ", define2='" + define2 + '\'' +
                ", define1='" + define1 + '\'' +
                ", code='" + code + '\'' +
                ", payconditionCode='" + payconditionCode + '\'' +
                ", define12='" + define12 + '\'' +
                ", deptcode='" + deptcode + '\'' +
                ", define9='" + define9 + '\'' +
                ", define13='" + define13 + '\'' +
                ", remark='" + remark + '\'' +
                ", define8='" + define8 + '\'' +
                ", define14='" + define14 + '\'' +
                ", define7='" + define7 + '\'' +
                ", define15='" + define15 + '\'' +
                ", define6='" + define6 + '\'' +
                ", bargain='" + bargain + '\'' +
                ", define5='" + define5 + '\'' +
                ", taxRate=" + taxRate +
                ", define4='" + define4 + '\'' +
                ", define10='" + define10 + '\'' +
                ", define3='" + define3 + '\'' +
                ", define11='" + define11 + '\'' +
                ", currencyRate=" + currencyRate +
                ", recsendType='" + recsendType + '\'' +
                ", vendorcode='" + vendorcode + '\'' +
                ", trafficMoney='" + trafficMoney + '\'' +
                ", period='" + period + '\'' +
                ", address='" + address + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", maker='" + maker + '\'' +
                ", purchaseTypeCode='" + purchaseTypeCode + '\'' +
                ", define16='" + define16 + '\'' +
                ", personcode='" + personcode + '\'' +
                ", operationTypeCode='" + operationTypeCode + '\'' +
                ", state=" + state +
                ", discountTaxType=" + discountTaxType +
                ", remark='" + remark + '\'' +
                ", venperson='" + venperson + '\'' +
                ", venbank='" + venbank + '\'' +
                ", venaccount='" + venaccount + '\'' +
                ", verifystateex=" + verifystateex +
                '}';
    }

}
