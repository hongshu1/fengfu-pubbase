package cn.rjtech.u9.entity.saledeliverplan;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * @version 1.0
 * @Author cc
 * @Create 2023/7/7 15:59
 * @Description 推送销售发货单到u8
 */
@XmlRootElement(name = "SaleDeliverPlanDTO")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SaleDeliverPlanDTO {

    /*系统人员code*/
    @XmlElement(name = "userCode")
    @JSONField(name = "userCode")
    private String      userCode;
    /*组织编码*/
    @XmlElement(name = "organizeCode")
    @JSONField(name = "organizeCode")
    private String      organizeCode;
    @XmlElement(name = "token")
    @JSONField(name = "token")
    private String      token;
    /*其它数据*/
    @XmlElement(name = "PreAllocate")
    @JSONField(name = "PreAllocate")
    private PreAllocate preAllocate;
    @XmlElement(name = "MainData")
    @JSONField(name = "MainData")
    private List<Main>  MainData;

    public static class PreAllocate {

        /*系统人员code*/
        @XmlElement(name = "userCode")
        @JSONField(name = "userCode")
        private String userCode;
        /*组织编码*/
        @XmlElement(name = "organizeCode")
        @JSONField(name = "organizeCode")
        private String organizeCode;
        /*创建人员*/
        @XmlElement(name = "CreatePerson")
        @JSONField(name = "CreatePerson")
        private String CreatePerson;
        /*创建人员名字*/
        @XmlElement(name = "CreatePersonName")
        @JSONField(name = "CreatePersonName")
        private String CreatePersonName;
        /*登录时间*/
        @XmlElement(name = "loginDate")
        @JSONField(name = "loginDate")
        @XmlSchemaType(name = "dateTime")
        private String loginDate;
        /*实体类名*/
        @XmlElement(name = "tag")
        @JSONField(name = "tag")
        private String tag;
        /*同tag一致*/
        @XmlElement(name = "type")
        @JSONField(name = "type")
        private String type;

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getOrganizeCode() {
            return organizeCode;
        }

        public void setOrganizeCode(String organizeCode) {
            this.organizeCode = organizeCode;
        }

        public String getCreatePerson() {
            return CreatePerson;
        }

        public void setCreatePerson(String createPerson) {
            CreatePerson = createPerson;
        }

        public String getCreatePersonName() {
            return CreatePersonName;
        }

        public void setCreatePersonName(String createPersonName) {
            CreatePersonName = createPersonName;
        }

        public String getLoginDate() {
            return loginDate;
        }

        public void setLoginDate(String loginDate) {
            this.loginDate = loginDate;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /*主要数据*/
    public static class Main {
        /*仓库编码*/
        @XmlElement(name = "owhcode")
        @JSONField(name = "owhcode")
        private String owhcode;
        /*部门编码*/
        @XmlElement(name = "odeptcode")
        @JSONField(name = "odeptcode")
        private String odeptcode;
        /*库存编码*/
        @XmlElement(name = "oposcode")
        @JSONField(name = "oposcode")
        private String oposcode;
        /*存货编码*/
        @XmlElement(name = "invcode")
        @JSONField(name = "invcode")
        private String invcode;
        /*用户编码*/
        @XmlElement(name = "userCode")
        @JSONField(name = "userCode")
        private String userCode;
        /*组织编码*/
        @XmlElement(name = "organizeCode")
        @JSONField(name = "organizeCode")
        private String organizeCode;
        /*数量*/
        @XmlElement(name = "Qty")
        @JSONField(name = "Qty")
        private Integer qty;
        /*客户编码*/
        @XmlElement(name = "cuscode")
        @JSONField(name = "cuscode")
        private String cuscode;
        /*现品票*/
        @XmlElement(name = "barcode")
        @JSONField(name = "barcode")
        private String barcode;
        /*规格*/
        @XmlElement(name = "invstd")
        @JSONField(name = "invstd")
        private String invstd;
        /*存货名称*/
        @XmlElement(name = "invname")
        @JSONField(name = "invname")
        private String invname;
        /*计数*/
        @XmlElement(name = "index")
        @JSONField(name = "index")
        private Integer index;
        /*单据日期*/
        @XmlElement(name = "billDate")
        @JSONField(name = "billDate")
        private String billDate;
        /*销售发货主表autoid*/
        @XmlElement(name = "billid")
        @JSONField(name = "billid")
        private String billid;
        /*销售发货明细表autoid*/
        @XmlElement(name = "billdid")
        @JSONField(name = "billdid")
        private String billdid;
        /*销售发货的单号*/
        @XmlElement(name = "billno")
        @JSONField(name = "billno")
        private String billno;
        /*列数*/
        @XmlElement(name = "billrowno")
        @JSONField(name = "billrowno")
        private Integer billrowno;
        /*单号-1,-2,-3*/
        @XmlElement(name = "billnorow")
        @JSONField(name = "billnorow")
        private String billnorow;
        /*来源行iautoid*/
        @XmlElement(name = "sourcebilldid")
        @JSONField(name = "sourcebilldid")
        private String sourcebilldid;
        /*来源单号-1，-2，-3*/
        @XmlElement(name = "sourcebillnorow")
        @JSONField(name = "sourcebillnorow")
        private String sourcebillnorow;
        /*来源单号*/
        @XmlElement(name = "sourcebillno")
        @JSONField(name = "sourcebillno")
        private String sourcebillno;
        /*来源1，2，3*/
        @XmlElement(name = "sourcebillrowno")
        @JSONField(name = "sourcebillrowno")
        private Integer sourcebillrowno;
        /*SaleDispatchMES*/
        @XmlElement(name = "Tag")
        @JSONField(name = "Tag")
        private String Tag;
        /**/
        @XmlElement(name = "cusname")
        @JSONField(name = "cusname")
        private String cusname;
        /*收发类别(销售类型)*/
        @XmlElement(name = "ORdType")
        @JSONField(name = "ORdType")
        private String oRdType;

        public String getOwhcode() {
            return owhcode;
        }

        public void setOwhcode(String owhcode) {
            this.owhcode = owhcode;
        }

        public String getOdeptcode() {
            return odeptcode;
        }

        public void setOdeptcode(String odeptcode) {
            this.odeptcode = odeptcode;
        }

        public String getOposcode() {
            return oposcode;
        }

        public void setOposcode(String oposcode) {
            this.oposcode = oposcode;
        }

        public String getInvcode() {
            return invcode;
        }

        public void setInvcode(String invcode) {
            this.invcode = invcode;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getOrganizeCode() {
            return organizeCode;
        }

        public void setOrganizeCode(String organizeCode) {
            this.organizeCode = organizeCode;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public String getCuscode() {
            return cuscode;
        }

        public void setCuscode(String cuscode) {
            this.cuscode = cuscode;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getInvstd() {
            return invstd;
        }

        public void setInvstd(String invstd) {
            this.invstd = invstd;
        }

        public String getInvname() {
            return invname;
        }

        public void setInvname(String invname) {
            this.invname = invname;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getBillid() {
            return billid;
        }

        public void setBillid(String billid) {
            this.billid = billid;
        }

        public String getBilldid() {
            return billdid;
        }

        public void setBilldid(String billdid) {
            this.billdid = billdid;
        }

        public String getBillno() {
            return billno;
        }

        public void setBillno(String billno) {
            this.billno = billno;
        }

        public Integer getBillrowno() {
            return billrowno;
        }

        public void setBillrowno(Integer billrowno) {
            this.billrowno = billrowno;
        }

        public String getBillnorow() {
            return billnorow;
        }

        public void setBillnorow(String billnorow) {
            this.billnorow = billnorow;
        }

        public String getSourcebilldid() {
            return sourcebilldid;
        }

        public void setSourcebilldid(String sourcebilldid) {
            this.sourcebilldid = sourcebilldid;
        }

        public String getSourcebillnorow() {
            return sourcebillnorow;
        }

        public void setSourcebillnorow(String sourcebillnorow) {
            this.sourcebillnorow = sourcebillnorow;
        }

        public String getSourcebillno() {
            return sourcebillno;
        }

        public void setSourcebillno(String sourcebillno) {
            this.sourcebillno = sourcebillno;
        }

        public Integer getSourcebillrowno() {
            return sourcebillrowno;
        }

        public void setSourcebillrowno(Integer sourcebillrowno) {
            this.sourcebillrowno = sourcebillrowno;
        }

        public String getTag() {
            return Tag;
        }

        public void setTag(String tag) {
            Tag = tag;
        }

        public String getCusname() {
            return cusname;
        }

        public void setCusname(String cusname) {
            this.cusname = cusname;
        }

        public String getoRdType() {
            return oRdType;
        }

        public void setoRdType(String oRdType) {
            this.oRdType = oRdType;
        }
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOrganizeCode() {
        return organizeCode;
    }

    public void setOrganizeCode(String organizeCode) {
        this.organizeCode = organizeCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PreAllocate getPreAllocate() {
        return preAllocate;
    }

    public void setPreAllocate(PreAllocate preAllocate) {
        this.preAllocate = preAllocate;
    }

    public List<Main> getMainData() {
        return MainData;
    }

    public void setMainData(List<Main> mainData) {
        MainData = mainData;
    }
}
