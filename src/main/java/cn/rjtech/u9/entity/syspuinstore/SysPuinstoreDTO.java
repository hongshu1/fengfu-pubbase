package cn.rjtech.u9.entity.syspuinstore;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/15 10:18
 * @Description 推送采购入库单到U8系统的实体类
 */

@XmlRootElement(name = "SysPuinstoreDTO")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SysPuinstoreDTO {

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
    private List<Main> MainData;

    /*主要数据*/
    public static class Main {
        /*条码code*/
        @XmlElement(name = "BarCode")
        @JSONField(name = "BarCode")
        private String BarCode;
        /*单据日期*/
        @XmlElement(name = "BillDate")
        @JSONField(name = "BillDate")
        @XmlSchemaType(name = "dateTime")
        private String BillDate;
        /**/
        @XmlElement(name = "BillDid")
        @JSONField(name = "BillDid")
        private String BillDid;
        /**/
        @XmlElement(name = "BillID")
        @JSONField(name = "BillID")
        private String BillID;
        /*入库单号*/
        @XmlElement(name = "BillNo")
        @JSONField(name = "BillNo")
        private String BillNo;
        /*入库单号+行号*/
        @XmlElement(name = "BillNoRow")
        @JSONField(name = "BillNoRow")
        private String BillNoRow;
        /*创建人*/
        @XmlElement(name = "CreatePerson")
        @JSONField(name = "CreatePerson")
        private String CreatePerson;
        /**/
        @XmlElement(name = "ISsurplusqty")
        @JSONField(name = "ISsurplusqty")
        private Boolean ISsurplusqty;
        /*收发类别（入库类别编码）*/
        @XmlElement(name = "IcRdCode")
        @JSONField(name = "IcRdCode")
        private String IcRdCode;
        /*库位编码*/
        @XmlElement(name = "IsWhpos")
        @JSONField(name = "IsWhpos")
        private String IsWhpos;
        /**/
        @XmlElement(name = "Num")
        @JSONField(name = "Num")
        private Integer Num;
        /**/
        @XmlElement(name = "PackRate")
        @JSONField(name = "PackRate")
        private String PackRate;
        /*数量*/
        @XmlElement(name = "Qty")
        @JSONField(name = "Qty")
        private String Qty;
        /*来源单号+行号*/
        @XmlElement(name = "SourceBillNoRow")
        @JSONField(name = "SourceBillNoRow")
        private String SourceBillNoRow;
        /*供应商编码*/
        @XmlElement(name = "VenCode")
        @JSONField(name = "VenCode")
        private String VenCode;
        /*供应商名称*/
        @XmlElement(name = "VenName")
        @JSONField(name = "VenName")
        private String VenName;
        /**/
        @XmlElement(name = "index")
        @JSONField(name = "index")
        private String index;
        /**/
        @XmlElement(name = "iposcode")
        @JSONField(name = "iposcode")
        private String iposcode;
        /*仓库编码*/
        @XmlElement(name = "iwhcode")
        @JSONField(name = "iwhcode")
        private String iwhcode;
        /*组织编码*/
        @XmlElement(name = "organizeCode")
        @JSONField(name = "organizeCode")
        private String organizeCode;
        /*来源单据DID;采购或委外单身ID*/
        @XmlElement(name = "sourceBillDid")
        @JSONField(name = "sourceBillDid")
        private String sourceBillDid;
        /*来源单号（订单号）*/
        @XmlElement(name = "sourceBillNo")
        @JSONField(name = "sourceBillNo")
        private String sourceBillNo;
        /*来源类型;PO 采购 OM委外*/
        @XmlElement(name = "sourceBillType")
        @JSONField(name = "sourceBillType")
        private String sourceBillType;
        /*实体类名*/
        @XmlElement(name = "tag")
        @JSONField(name = "tag")
        private String tag;

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String barCode) {
            BarCode = barCode;
        }

        public String getBillDate() {
            return BillDate;
        }

        public void setBillDate(String billDate) {
            BillDate = billDate;
        }

        public String getBillDid() {
            return BillDid;
        }

        public void setBillDid(String billDid) {
            BillDid = billDid;
        }

        public String getBillID() {
            return BillID;
        }

        public void setBillID(String billID) {
            BillID = billID;
        }

        public String getBillNo() {
            return BillNo;
        }

        public void setBillNo(String billNo) {
            BillNo = billNo;
        }

        public String getBillNoRow() {
            return BillNoRow;
        }

        public void setBillNoRow(String billNoRow) {
            BillNoRow = billNoRow;
        }

        public String getCreatePerson() {
            return CreatePerson;
        }

        public void setCreatePerson(String createPerson) {
            CreatePerson = createPerson;
        }

        public Boolean getISsurplusqty() {
            return ISsurplusqty;
        }

        public void setISsurplusqty(Boolean ISsurplusqty) {
            this.ISsurplusqty = ISsurplusqty;
        }

        public String getIcRdCode() {
            return IcRdCode;
        }

        public void setIcRdCode(String icRdCode) {
            IcRdCode = icRdCode;
        }

        public String getIsWhpos() {
            return IsWhpos;
        }

        public void setIsWhpos(String isWhpos) {
            IsWhpos = isWhpos;
        }

        public Integer getNum() {
            return Num;
        }

        public void setNum(Integer num) {
            Num = num;
        }

        public String getPackRate() {
            return PackRate;
        }

        public void setPackRate(String packRate) {
            PackRate = packRate;
        }

        public String getQty() {
            return Qty;
        }

        public void setQty(String qty) {
            Qty = qty;
        }

        public String getSourceBillNoRow() {
            return SourceBillNoRow;
        }

        public void setSourceBillNoRow(String sourceBillNoRow) {
            SourceBillNoRow = sourceBillNoRow;
        }

        public String getVenCode() {
            return VenCode;
        }

        public void setVenCode(String venCode) {
            VenCode = venCode;
        }

        public String getVenName() {
            return VenName;
        }

        public void setVenName(String venName) {
            VenName = venName;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getIposcode() {
            return iposcode;
        }

        public void setIposcode(String iposcode) {
            this.iposcode = iposcode;
        }

        public String getIwhcode() {
            return iwhcode;
        }

        public void setIwhcode(String iwhcode) {
            this.iwhcode = iwhcode;
        }

        public String getOrganizeCode() {
            return organizeCode;
        }

        public void setOrganizeCode(String organizeCode) {
            this.organizeCode = organizeCode;
        }

        public String getSourceBillDid() {
            return sourceBillDid;
        }

        public void setSourceBillDid(String sourceBillDid) {
            this.sourceBillDid = sourceBillDid;
        }

        public String getSourceBillNo() {
            return sourceBillNo;
        }

        public void setSourceBillNo(String sourceBillNo) {
            this.sourceBillNo = sourceBillNo;
        }

        public String getSourceBillType() {
            return sourceBillType;
        }

        public void setSourceBillType(String sourceBillType) {
            this.sourceBillType = sourceBillType;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

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

    @Override
    public String toString() {
        return "SysPuinstoreDTO{" +
            "userCode='" + userCode + '\'' +
            ", organizeCode='" + organizeCode + '\'' +
            ", token='" + token + '\'' +
            ", preAllocate=" + preAllocate +
            ", MainData=" + MainData +
            '}';
    }
}
