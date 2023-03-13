package cn.rjtech.u9.entity.pr;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

/**
 * 请购单行（PRLineDTO）
 * 料号	ItemCode	String	Y	料品编码
 * 数量	Qty	Decimal(18,9)	Y
 * 要求交货日期	RequiredDeliveryDate	Date	Y
 * 建议供应商	SuggestedSupplier	string		供应商编码
 * 采购部门	PurDepartment	string		部门编码
 * 需求人员	PurPerson	string		需求部门的业务员编码
 * 存储地点	WHCode	string		存储地点编码
 * 备注	Memo	String
 * 公共字段1到50	PubDescSeg1到PubDescSeg50	String
 * 私有字段字段1到30	PrivateDescSeg1到PrivateDescSeg30	String
 */
@XmlRootElement(name = "PRLineDTOs")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PRLineDTO {

    @XmlElement(name = "ItemCode")
    @JSONField(name = "ItemCode")
    private String ItemCode;

    @XmlElement(name = "ReqQty")
    @JSONField(name = "ReqQty")
    private BigDecimal Qty;

    @XmlElement(name = "RequiredDeliveryDate")
    @JSONField(name = "RequiredDeliveryDate")
    @XmlSchemaType(name = "dateTime")
    private String RequiredDeliveryDate;

    @XmlElement(name = "SuggestedSupplier")
    @JSONField(name = "SuggestedSupplier")
    private String SuggestedSupplier;

    @XmlElement(name = "PurDepartment")
    @JSONField(name = "PurDepartment")
    private String PurDepartment;

    @XmlElement(name = "PurPerson")
    @JSONField(name = "PurPerson")
    private String PurPerson;

    @XmlElement(name = "Warehouse")
    @JSONField(name = "Warehouse")
    private String WHCode;

    @XmlElement(name = "Memo")
    @JSONField(name = "Memo")
    private String Memo;


    private String PubDescSeg1;
    private String PubDescSeg2;
    private String PubDescSeg3;
    private String PubDescSeg4;
    private String PubDescSeg5;
    private String PubDescSeg6;
    private String PubDescSeg7;
    private String PubDescSeg8;
    private String PubDescSeg9;
    private String PubDescSeg10;

    private String PubDescSeg11;
    private String PubDescSeg12;
    private String PubDescSeg13;
    private String PubDescSeg14;
    private String PubDescSeg15;
    private String PubDescSeg16;
    private String PubDescSeg17;
    private String PubDescSeg18;
    private String PubDescSeg19;
    private String PubDescSeg20;

    private String PubDescSeg21;
    private String PubDescSeg22;
    private String PubDescSeg23;
    private String PubDescSeg24;
    private String PubDescSeg25;
    private String PubDescSeg26;
    private String PubDescSeg27;
    private String PubDescSeg28;
    private String PubDescSeg29;
    private String PubDescSeg30;

    private String PubDescSeg31;
    private String PubDescSeg32;
    private String PubDescSeg33;
    private String PubDescSeg34;
    private String PubDescSeg35;
    private String PubDescSeg36;
    private String PubDescSeg37;
    private String PubDescSeg38;
    private String PubDescSeg39;
    private String PubDescSeg40;

    private String PubDescSeg41;
    private String PubDescSeg42;
    private String PubDescSeg43;
    private String PubDescSeg44;
    private String PubDescSeg45;
    private String PubDescSeg46;
    private String PubDescSeg47;
    private String PubDescSeg48;
    private String PubDescSeg49;
    private String PubDescSeg50;

    private String PrivateDescSeg1;
    private String PrivateDescSeg2;
    private String PrivateDescSeg3;
    private String PrivateDescSeg4;
    private String PrivateDescSeg5;
    private String PrivateDescSeg6;
    private String PrivateDescSeg7;
    private String PrivateDescSeg8;
    private String PrivateDescSeg9;
    private String PrivateDescSeg10;

    private String PrivateDescSeg11;
    private String PrivateDescSeg12;
    private String PrivateDescSeg13;
    private String PrivateDescSeg14;
    private String PrivateDescSeg15;
    private String PrivateDescSeg16;
    private String PrivateDescSeg17;
    private String PrivateDescSeg18;
    private String PrivateDescSeg19;
    private String PrivateDescSeg20;

    private String PrivateDescSeg21;
    private String PrivateDescSeg22;
    private String PrivateDescSeg23;
    private String PrivateDescSeg24;
    private String PrivateDescSeg25;
    private String PrivateDescSeg26;
    private String PrivateDescSeg27;
    private String PrivateDescSeg28;
    private String PrivateDescSeg29;
    private String PrivateDescSeg30;

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public BigDecimal getQty() {
        return Qty;
    }

    public void setQty(BigDecimal qty) {
        Qty = qty;
    }

    public String getRequiredDeliveryDate() {
        return RequiredDeliveryDate;
    }

    public void setRequiredDeliveryDate(String requiredDeliveryDate) {
        RequiredDeliveryDate = requiredDeliveryDate;
    }

    public String getSuggestedSupplier() {
        return SuggestedSupplier;
    }

    public void setSuggestedSupplier(String suggestedSupplier) {
        SuggestedSupplier = suggestedSupplier;
    }

    public String getPurDepartment() {
        return PurDepartment;
    }

    public void setPurDepartment(String purDepartment) {
        PurDepartment = purDepartment;
    }

    public String getPurPerson() {
        return PurPerson;
    }

    public void setPurPerson(String purPerson) {
        PurPerson = purPerson;
    }

    public String getWHCode() {
        return WHCode;
    }

    public void setWHCode(String WHCode) {
        this.WHCode = WHCode;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getPubDescSeg1() {
        return PubDescSeg1;
    }

    public void setPubDescSeg1(String pubDescSeg1) {
        PubDescSeg1 = pubDescSeg1;
    }

    public String getPubDescSeg2() {
        return PubDescSeg2;
    }

    public void setPubDescSeg2(String pubDescSeg2) {
        PubDescSeg2 = pubDescSeg2;
    }

    public String getPubDescSeg3() {
        return PubDescSeg3;
    }

    public void setPubDescSeg3(String pubDescSeg3) {
        PubDescSeg3 = pubDescSeg3;
    }

    public String getPubDescSeg4() {
        return PubDescSeg4;
    }

    public void setPubDescSeg4(String pubDescSeg4) {
        PubDescSeg4 = pubDescSeg4;
    }

    public String getPubDescSeg5() {
        return PubDescSeg5;
    }

    public void setPubDescSeg5(String pubDescSeg5) {
        PubDescSeg5 = pubDescSeg5;
    }

    public String getPubDescSeg6() {
        return PubDescSeg6;
    }

    public void setPubDescSeg6(String pubDescSeg6) {
        PubDescSeg6 = pubDescSeg6;
    }

    public String getPubDescSeg7() {
        return PubDescSeg7;
    }

    public void setPubDescSeg7(String pubDescSeg7) {
        PubDescSeg7 = pubDescSeg7;
    }

    public String getPubDescSeg8() {
        return PubDescSeg8;
    }

    public void setPubDescSeg8(String pubDescSeg8) {
        PubDescSeg8 = pubDescSeg8;
    }

    public String getPubDescSeg9() {
        return PubDescSeg9;
    }

    public void setPubDescSeg9(String pubDescSeg9) {
        PubDescSeg9 = pubDescSeg9;
    }

    public String getPubDescSeg10() {
        return PubDescSeg10;
    }

    public void setPubDescSeg10(String pubDescSeg10) {
        PubDescSeg10 = pubDescSeg10;
    }

    public String getPubDescSeg11() {
        return PubDescSeg11;
    }

    public void setPubDescSeg11(String pubDescSeg11) {
        PubDescSeg11 = pubDescSeg11;
    }

    public String getPubDescSeg12() {
        return PubDescSeg12;
    }

    public void setPubDescSeg12(String pubDescSeg12) {
        PubDescSeg12 = pubDescSeg12;
    }

    public String getPubDescSeg13() {
        return PubDescSeg13;
    }

    public void setPubDescSeg13(String pubDescSeg13) {
        PubDescSeg13 = pubDescSeg13;
    }

    public String getPubDescSeg14() {
        return PubDescSeg14;
    }

    public void setPubDescSeg14(String pubDescSeg14) {
        PubDescSeg14 = pubDescSeg14;
    }

    public String getPubDescSeg15() {
        return PubDescSeg15;
    }

    public void setPubDescSeg15(String pubDescSeg15) {
        PubDescSeg15 = pubDescSeg15;
    }

    public String getPubDescSeg16() {
        return PubDescSeg16;
    }

    public void setPubDescSeg16(String pubDescSeg16) {
        PubDescSeg16 = pubDescSeg16;
    }

    public String getPubDescSeg17() {
        return PubDescSeg17;
    }

    public void setPubDescSeg17(String pubDescSeg17) {
        PubDescSeg17 = pubDescSeg17;
    }

    public String getPubDescSeg18() {
        return PubDescSeg18;
    }

    public void setPubDescSeg18(String pubDescSeg18) {
        PubDescSeg18 = pubDescSeg18;
    }

    public String getPubDescSeg19() {
        return PubDescSeg19;
    }

    public void setPubDescSeg19(String pubDescSeg19) {
        PubDescSeg19 = pubDescSeg19;
    }

    public String getPubDescSeg20() {
        return PubDescSeg20;
    }

    public void setPubDescSeg20(String pubDescSeg20) {
        PubDescSeg20 = pubDescSeg20;
    }

    public String getPubDescSeg21() {
        return PubDescSeg21;
    }

    public void setPubDescSeg21(String pubDescSeg21) {
        PubDescSeg21 = pubDescSeg21;
    }

    public String getPubDescSeg22() {
        return PubDescSeg22;
    }

    public void setPubDescSeg22(String pubDescSeg22) {
        PubDescSeg22 = pubDescSeg22;
    }

    public String getPubDescSeg23() {
        return PubDescSeg23;
    }

    public void setPubDescSeg23(String pubDescSeg23) {
        PubDescSeg23 = pubDescSeg23;
    }

    public String getPubDescSeg24() {
        return PubDescSeg24;
    }

    public void setPubDescSeg24(String pubDescSeg24) {
        PubDescSeg24 = pubDescSeg24;
    }

    public String getPubDescSeg25() {
        return PubDescSeg25;
    }

    public void setPubDescSeg25(String pubDescSeg25) {
        PubDescSeg25 = pubDescSeg25;
    }

    public String getPubDescSeg26() {
        return PubDescSeg26;
    }

    public void setPubDescSeg26(String pubDescSeg26) {
        PubDescSeg26 = pubDescSeg26;
    }

    public String getPubDescSeg27() {
        return PubDescSeg27;
    }

    public void setPubDescSeg27(String pubDescSeg27) {
        PubDescSeg27 = pubDescSeg27;
    }

    public String getPubDescSeg28() {
        return PubDescSeg28;
    }

    public void setPubDescSeg28(String pubDescSeg28) {
        PubDescSeg28 = pubDescSeg28;
    }

    public String getPubDescSeg29() {
        return PubDescSeg29;
    }

    public void setPubDescSeg29(String pubDescSeg29) {
        PubDescSeg29 = pubDescSeg29;
    }

    public String getPubDescSeg30() {
        return PubDescSeg30;
    }

    public void setPubDescSeg30(String pubDescSeg30) {
        PubDescSeg30 = pubDescSeg30;
    }

    public String getPubDescSeg31() {
        return PubDescSeg31;
    }

    public void setPubDescSeg31(String pubDescSeg31) {
        PubDescSeg31 = pubDescSeg31;
    }

    public String getPubDescSeg32() {
        return PubDescSeg32;
    }

    public void setPubDescSeg32(String pubDescSeg32) {
        PubDescSeg32 = pubDescSeg32;
    }

    public String getPubDescSeg33() {
        return PubDescSeg33;
    }

    public void setPubDescSeg33(String pubDescSeg33) {
        PubDescSeg33 = pubDescSeg33;
    }

    public String getPubDescSeg34() {
        return PubDescSeg34;
    }

    public void setPubDescSeg34(String pubDescSeg34) {
        PubDescSeg34 = pubDescSeg34;
    }

    public String getPubDescSeg35() {
        return PubDescSeg35;
    }

    public void setPubDescSeg35(String pubDescSeg35) {
        PubDescSeg35 = pubDescSeg35;
    }

    public String getPubDescSeg36() {
        return PubDescSeg36;
    }

    public void setPubDescSeg36(String pubDescSeg36) {
        PubDescSeg36 = pubDescSeg36;
    }

    public String getPubDescSeg37() {
        return PubDescSeg37;
    }

    public void setPubDescSeg37(String pubDescSeg37) {
        PubDescSeg37 = pubDescSeg37;
    }

    public String getPubDescSeg38() {
        return PubDescSeg38;
    }

    public void setPubDescSeg38(String pubDescSeg38) {
        PubDescSeg38 = pubDescSeg38;
    }

    public String getPubDescSeg39() {
        return PubDescSeg39;
    }

    public void setPubDescSeg39(String pubDescSeg39) {
        PubDescSeg39 = pubDescSeg39;
    }

    public String getPubDescSeg40() {
        return PubDescSeg40;
    }

    public void setPubDescSeg40(String pubDescSeg40) {
        PubDescSeg40 = pubDescSeg40;
    }

    public String getPubDescSeg41() {
        return PubDescSeg41;
    }

    public void setPubDescSeg41(String pubDescSeg41) {
        PubDescSeg41 = pubDescSeg41;
    }

    public String getPubDescSeg42() {
        return PubDescSeg42;
    }

    public void setPubDescSeg42(String pubDescSeg42) {
        PubDescSeg42 = pubDescSeg42;
    }

    public String getPubDescSeg43() {
        return PubDescSeg43;
    }

    public void setPubDescSeg43(String pubDescSeg43) {
        PubDescSeg43 = pubDescSeg43;
    }

    public String getPubDescSeg44() {
        return PubDescSeg44;
    }

    public void setPubDescSeg44(String pubDescSeg44) {
        PubDescSeg44 = pubDescSeg44;
    }

    public String getPubDescSeg45() {
        return PubDescSeg45;
    }

    public void setPubDescSeg45(String pubDescSeg45) {
        PubDescSeg45 = pubDescSeg45;
    }

    public String getPubDescSeg46() {
        return PubDescSeg46;
    }

    public void setPubDescSeg46(String pubDescSeg46) {
        PubDescSeg46 = pubDescSeg46;
    }

    public String getPubDescSeg47() {
        return PubDescSeg47;
    }

    public void setPubDescSeg47(String pubDescSeg47) {
        PubDescSeg47 = pubDescSeg47;
    }

    public String getPubDescSeg48() {
        return PubDescSeg48;
    }

    public void setPubDescSeg48(String pubDescSeg48) {
        PubDescSeg48 = pubDescSeg48;
    }

    public String getPubDescSeg49() {
        return PubDescSeg49;
    }

    public void setPubDescSeg49(String pubDescSeg49) {
        PubDescSeg49 = pubDescSeg49;
    }

    public String getPubDescSeg50() {
        return PubDescSeg50;
    }

    public void setPubDescSeg50(String pubDescSeg50) {
        PubDescSeg50 = pubDescSeg50;
    }

    public String getPrivateDescSeg1() {
        return PrivateDescSeg1;
    }

    public void setPrivateDescSeg1(String privateDescSeg1) {
        PrivateDescSeg1 = privateDescSeg1;
    }

    public String getPrivateDescSeg2() {
        return PrivateDescSeg2;
    }

    public void setPrivateDescSeg2(String privateDescSeg2) {
        PrivateDescSeg2 = privateDescSeg2;
    }

    public String getPrivateDescSeg3() {
        return PrivateDescSeg3;
    }

    public void setPrivateDescSeg3(String privateDescSeg3) {
        PrivateDescSeg3 = privateDescSeg3;
    }

    public String getPrivateDescSeg4() {
        return PrivateDescSeg4;
    }

    public void setPrivateDescSeg4(String privateDescSeg4) {
        PrivateDescSeg4 = privateDescSeg4;
    }

    public String getPrivateDescSeg5() {
        return PrivateDescSeg5;
    }

    public void setPrivateDescSeg5(String privateDescSeg5) {
        PrivateDescSeg5 = privateDescSeg5;
    }

    public String getPrivateDescSeg6() {
        return PrivateDescSeg6;
    }

    public void setPrivateDescSeg6(String privateDescSeg6) {
        PrivateDescSeg6 = privateDescSeg6;
    }

    public String getPrivateDescSeg7() {
        return PrivateDescSeg7;
    }

    public void setPrivateDescSeg7(String privateDescSeg7) {
        PrivateDescSeg7 = privateDescSeg7;
    }

    public String getPrivateDescSeg8() {
        return PrivateDescSeg8;
    }

    public void setPrivateDescSeg8(String privateDescSeg8) {
        PrivateDescSeg8 = privateDescSeg8;
    }

    public String getPrivateDescSeg9() {
        return PrivateDescSeg9;
    }

    public void setPrivateDescSeg9(String privateDescSeg9) {
        PrivateDescSeg9 = privateDescSeg9;
    }

    public String getPrivateDescSeg10() {
        return PrivateDescSeg10;
    }

    public void setPrivateDescSeg10(String privateDescSeg10) {
        PrivateDescSeg10 = privateDescSeg10;
    }

    public String getPrivateDescSeg11() {
        return PrivateDescSeg11;
    }

    public void setPrivateDescSeg11(String privateDescSeg11) {
        PrivateDescSeg11 = privateDescSeg11;
    }

    public String getPrivateDescSeg12() {
        return PrivateDescSeg12;
    }

    public void setPrivateDescSeg12(String privateDescSeg12) {
        PrivateDescSeg12 = privateDescSeg12;
    }

    public String getPrivateDescSeg13() {
        return PrivateDescSeg13;
    }

    public void setPrivateDescSeg13(String privateDescSeg13) {
        PrivateDescSeg13 = privateDescSeg13;
    }

    public String getPrivateDescSeg14() {
        return PrivateDescSeg14;
    }

    public void setPrivateDescSeg14(String privateDescSeg14) {
        PrivateDescSeg14 = privateDescSeg14;
    }

    public String getPrivateDescSeg15() {
        return PrivateDescSeg15;
    }

    public void setPrivateDescSeg15(String privateDescSeg15) {
        PrivateDescSeg15 = privateDescSeg15;
    }

    public String getPrivateDescSeg16() {
        return PrivateDescSeg16;
    }

    public void setPrivateDescSeg16(String privateDescSeg16) {
        PrivateDescSeg16 = privateDescSeg16;
    }

    public String getPrivateDescSeg17() {
        return PrivateDescSeg17;
    }

    public void setPrivateDescSeg17(String privateDescSeg17) {
        PrivateDescSeg17 = privateDescSeg17;
    }

    public String getPrivateDescSeg18() {
        return PrivateDescSeg18;
    }

    public void setPrivateDescSeg18(String privateDescSeg18) {
        PrivateDescSeg18 = privateDescSeg18;
    }

    public String getPrivateDescSeg19() {
        return PrivateDescSeg19;
    }

    public void setPrivateDescSeg19(String privateDescSeg19) {
        PrivateDescSeg19 = privateDescSeg19;
    }

    public String getPrivateDescSeg20() {
        return PrivateDescSeg20;
    }

    public void setPrivateDescSeg20(String privateDescSeg20) {
        PrivateDescSeg20 = privateDescSeg20;
    }

    public String getPrivateDescSeg21() {
        return PrivateDescSeg21;
    }

    public void setPrivateDescSeg21(String privateDescSeg21) {
        PrivateDescSeg21 = privateDescSeg21;
    }

    public String getPrivateDescSeg22() {
        return PrivateDescSeg22;
    }

    public void setPrivateDescSeg22(String privateDescSeg22) {
        PrivateDescSeg22 = privateDescSeg22;
    }

    public String getPrivateDescSeg23() {
        return PrivateDescSeg23;
    }

    public void setPrivateDescSeg23(String privateDescSeg23) {
        PrivateDescSeg23 = privateDescSeg23;
    }

    public String getPrivateDescSeg24() {
        return PrivateDescSeg24;
    }

    public void setPrivateDescSeg24(String privateDescSeg24) {
        PrivateDescSeg24 = privateDescSeg24;
    }

    public String getPrivateDescSeg25() {
        return PrivateDescSeg25;
    }

    public void setPrivateDescSeg25(String privateDescSeg25) {
        PrivateDescSeg25 = privateDescSeg25;
    }

    public String getPrivateDescSeg26() {
        return PrivateDescSeg26;
    }

    public void setPrivateDescSeg26(String privateDescSeg26) {
        PrivateDescSeg26 = privateDescSeg26;
    }

    public String getPrivateDescSeg27() {
        return PrivateDescSeg27;
    }

    public void setPrivateDescSeg27(String privateDescSeg27) {
        PrivateDescSeg27 = privateDescSeg27;
    }

    public String getPrivateDescSeg28() {
        return PrivateDescSeg28;
    }

    public void setPrivateDescSeg28(String privateDescSeg28) {
        PrivateDescSeg28 = privateDescSeg28;
    }

    public String getPrivateDescSeg29() {
        return PrivateDescSeg29;
    }

    public void setPrivateDescSeg29(String privateDescSeg29) {
        PrivateDescSeg29 = privateDescSeg29;
    }

    public String getPrivateDescSeg30() {
        return PrivateDescSeg30;
    }

    public void setPrivateDescSeg30(String privateDescSeg30) {
        PrivateDescSeg30 = privateDescSeg30;
    }
}
