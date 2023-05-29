package cn.rjtech.u8.pojo.req.purchaseapp;

import cn.rjtech.u8.pojo.req.BaseHead;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 采购申请单 表头
 *
 * @author Kephon
 */
@XmlRootElement(name = "header")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PurchaseAppHeader extends BaseHead {
    @XmlElement(name = "date")
    private String date;
    @XmlElement(name = "define2")
    private String define2;
    @XmlElement(name = "define1")
    private String define1;
    @XmlElement(name = "code")
    private String code;
    @XmlElement(name = "memory")
    private String memory;
    @XmlElement(name = "purchasetypecode")
    private String purchasetypecode;
    @XmlElement(name = "define12")
    private String define12;
    @XmlElement(name = "maker")
    private String maker;
    @XmlElement(name = "define9")
    private String define9;
    @XmlElement(name = "define13")
    private String define13;
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
    @XmlElement(name = "define5")
    private String define5;
    @XmlElement(name = "define4")
    private String define4;
    @XmlElement(name = "define10")
    private String define10;
    @XmlElement(name = "businesstype")
    private String businesstype;
    @XmlElement(name = "define3")
    private String define3;
    @XmlElement(name = "define11")
    private String define11;
    @XmlElement(name = "define16")
    private String define16;
    @XmlElement(name = "personcode")
    private String personcode;
    @XmlElement(name = "departmentcode")
    private String departmentcode;

    public PurchaseAppHeader() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDefine2(String define2) {
        this.define2 = define2;
    }

    public void setDefine1(String define1) {
        this.define1 = define1;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public void setPurchasetypecode(String purchasetypecode) {
        this.purchasetypecode = purchasetypecode;
    }

    public void setDefine12(String define12) {
        this.define12 = define12;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public void setDefine9(String define9) {
        this.define9 = define9;
    }

    public void setDefine13(String define13) {
        this.define13 = define13;
    }

    public void setDefine8(String define8) {
        this.define8 = define8;
    }

    public void setDefine14(String define14) {
        this.define14 = define14;
    }

    public void setDefine7(String define7) {
        this.define7 = define7;
    }

    public void setDefine15(String define15) {
        this.define15 = define15;
    }

    public void setDefine6(String define6) {
        this.define6 = define6;
    }

    public void setDefine5(String define5) {
        this.define5 = define5;
    }

    public void setDefine4(String define4) {
        this.define4 = define4;
    }

    public void setDefine10(String define10) {
        this.define10 = define10;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public void setDefine11(String define11) {
        this.define11 = define11;
    }

    public void setDefine16(String define16) {
        this.define16 = define16;
    }

    public void setPersoncode(String personcode) {
        this.personcode = personcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode;
    }

    public void setDefine3(String define3) {
        this.define3 = define3;
    }

    public String getDate() {
        return date;
    }

    public String getDefine2() {
        return define2;
    }

    public String getDefine1() {
        return define1;
    }

    public String getCode() {
        return code;
    }

    public String getMemory() {
        return memory;
    }

    public String getPurchasetypecode() {
        return purchasetypecode;
    }

    public String getDefine12() {
        return define12;
    }

    public String getMaker() {
        return maker;
    }

    public String getDefine9() {
        return define9;
    }

    public String getDefine13() {
        return define13;
    }

    public String getDefine8() {
        return define8;
    }

    public String getDefine14() {
        return define14;
    }

    public String getDefine7() {
        return define7;
    }

    public String getDefine15() {
        return define15;
    }

    public String getDefine6() {
        return define6;
    }

    public String getDefine5() {
        return define5;
    }

    public String getDefine4() {
        return define4;
    }

    public String getDefine10() {
        return define10;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public String getDefine3() {
        return define3;
    }

    public String getDefine11() {
        return define11;
    }

    public String getDefine16() {
        return define16;
    }

    public String getPersoncode() {
        return personcode;
    }

    public String getDepartmentcode() {
        return departmentcode;
    }

    @Override
    public String toString() {
        return "PurchaseAppHeader{" +
                "data'=" + date + '\'' +
                "define2'=" + define2 + '\'' +
                "define1'=" + define1 + '\'' +
                "code'=" + code + '\'' +
                "memory'=" + memory + '\'' +
                "purchasetypecode'=" + purchasetypecode + '\'' +
                "define12'=" + define12 + '\'' +
                "maker'=" + maker + '\'' +
                "define9'=" + define9 + '\'' +
                "define13'=" + define13 + '\'' +
                "define8'=" + define8 + '\'' +
                "define14'=" + define14 + '\'' +
                "define7'=" + define7 + '\'' +
                "define15'=" + define15 + '\'' +
                "define6'=" + define6 + '\'' +
                "define5'=" + define5 + '\'' +
                "define4'=" + define4 + '\'' +
                "define10'=" + define10 + '\'' +
                "businesstype'=" + businesstype + '\'' +
                "define3'=" + define3 + '\'' +
                "define11'=" + define11 + '\'' +
                "define16'=" + define16 + '\'' +
                "personcode'=" + personcode + '\'' +
                "departmentcode'=" + departmentcode + '\'' +
                '}';
    }
}
