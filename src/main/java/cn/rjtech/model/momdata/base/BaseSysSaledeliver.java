package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 销售出库
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysSaledeliver<M extends BaseSysSaledeliver<M>> extends JBoltBaseModel<M>{
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**来源类型;MO生产工单*/
    public static final String SOURCEBILLTYPE = "SourceBillType";
    /**来源单据Did*/
    public static final String SOURCEBILLDID = "SourceBillDid";
    /**收发类别*/
    public static final String RDCODE = "RdCode";
    /**组织代码*/
    public static final String ORGANIZECODE = "OrganizeCode";
    /**单号*/
    public static final String BILLNO = "BillNo";
    /**业务类型*/
    public static final String BILLTYPE = "BillType";
    /**单据日期*/
    public static final String BILLDATE = "BillDate";
    /**部门编码*/
    public static final String DEPTCODE = "DeptCode";
    /**币别*/
    public static final String EXCHNAME = "ExchName";
    /**汇率*/
    public static final String EXCHRATE = "ExchRate";
    /**税率*/
    public static final String TAXRATE = "TaxRate";
    /**发货地址*/
    public static final String SHIPADDRESS = "ShipAddress";
    /**收货地址*/
    public static final String RECEIVEADDRESS = "ReceiveAddress";
    /**供应商*/
    public static final String VENCODE = "VenCode";
    /**审核人*/
    public static final String AUDITPERSON = "AuditPerson";
    /**审核日期*/
    public static final String AUDITDATE = "AuditDate";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建时间*/
    public static final String CREATEDATE = "CreateDate";
    /**更新人*/
    public static final String MODIFYPERSON = "ModifyPerson";
    /**更新时间*/
    public static final String MODIFYDATE = "ModifyDate";
	/**
	 * AutoID
	 */
	public M setAutoID(java.lang.String AutoID) {
		set("AutoID", AutoID);
		return (M)this;
	}

	/**
	 * AutoID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="String", remark="AutoID", required=true, maxLength=30, fixed=0, order=1)
	@JSONField(name = "autoid")
	public java.lang.String getAutoID() {
		return getStr("AutoID");
	}

	/**
	 * 来源类型;MO生产工单
	 */
	public M setSourceBillType(java.lang.String SourceBillType) {
		set("SourceBillType", SourceBillType);
		return (M)this;
	}

	/**
	 * 来源类型;MO生产工单
	 */
	@JBoltField(name="sourcebilltype" ,columnName="SourceBillType",type="String", remark="来源类型;MO生产工单", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "sourcebilltype")
	public java.lang.String getSourceBillType() {
		return getStr("SourceBillType");
	}

	/**
	 * 来源单据Did
	 */
	public M setSourceBillDid(java.lang.String SourceBillDid) {
		set("SourceBillDid", SourceBillDid);
		return (M)this;
	}

	/**
	 * 来源单据Did
	 */
	@JBoltField(name="sourcebilldid" ,columnName="SourceBillDid",type="String", remark="来源单据Did", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "sourcebilldid")
	public java.lang.String getSourceBillDid() {
		return getStr("SourceBillDid");
	}

	/**
	 * 收发类别
	 */
	public M setRdCode(java.lang.String RdCode) {
		set("RdCode", RdCode);
		return (M)this;
	}

	/**
	 * 收发类别
	 */
	@JBoltField(name="rdcode" ,columnName="RdCode",type="String", remark="收发类别", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "rdcode")
	public java.lang.String getRdCode() {
		return getStr("RdCode");
	}

	/**
	 * 组织代码
	 */
	public M setOrganizeCode(java.lang.String OrganizeCode) {
		set("OrganizeCode", OrganizeCode);
		return (M)this;
	}

	/**
	 * 组织代码
	 */
	@JBoltField(name="organizecode" ,columnName="OrganizeCode",type="String", remark="组织代码", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "organizecode")
	public java.lang.String getOrganizeCode() {
		return getStr("OrganizeCode");
	}

	/**
	 * 单号
	 */
	public M setBillNo(java.lang.String BillNo) {
		set("BillNo", BillNo);
		return (M)this;
	}

	/**
	 * 单号
	 */
	@JBoltField(name="billno" ,columnName="BillNo",type="String", remark="单号", required=false, maxLength=30, fixed=0, order=6)
	@JSONField(name = "billno")
	public java.lang.String getBillNo() {
		return getStr("BillNo");
	}

	/**
	 * 业务类型
	 */
	public M setBillType(java.lang.String BillType) {
		set("BillType", BillType);
		return (M)this;
	}

	/**
	 * 业务类型
	 */
	@JBoltField(name="billtype" ,columnName="BillType",type="String", remark="业务类型", required=false, maxLength=30, fixed=0, order=7)
	@JSONField(name = "billtype")
	public java.lang.String getBillType() {
		return getStr("BillType");
	}

	/**
	 * 单据日期
	 */
	public M setBillDate(java.lang.String BillDate) {
		set("BillDate", BillDate);
		return (M)this;
	}

	/**
	 * 单据日期
	 */
	@JBoltField(name="billdate" ,columnName="BillDate",type="String", remark="单据日期", required=false, maxLength=30, fixed=0, order=8)
	@JSONField(name = "billdate")
	public java.lang.String getBillDate() {
		return getStr("BillDate");
	}

	/**
	 * 部门编码
	 */
	public M setDeptCode(java.lang.String DeptCode) {
		set("DeptCode", DeptCode);
		return (M)this;
	}

	/**
	 * 部门编码
	 */
	@JBoltField(name="deptcode" ,columnName="DeptCode",type="String", remark="部门编码", required=false, maxLength=30, fixed=0, order=9)
	@JSONField(name = "deptcode")
	public java.lang.String getDeptCode() {
		return getStr("DeptCode");
	}

	/**
	 * 币别
	 */
	public M setExchName(java.lang.String ExchName) {
		set("ExchName", ExchName);
		return (M)this;
	}

	/**
	 * 币别
	 */
	@JBoltField(name="exchname" ,columnName="ExchName",type="String", remark="币别", required=false, maxLength=30, fixed=0, order=10)
	@JSONField(name = "exchname")
	public java.lang.String getExchName() {
		return getStr("ExchName");
	}

	/**
	 * 汇率
	 */
	public M setExchRate(java.math.BigDecimal ExchRate) {
		set("ExchRate", ExchRate);
		return (M)this;
	}

	/**
	 * 汇率
	 */
	@JBoltField(name="exchrate" ,columnName="ExchRate",type="BigDecimal", remark="汇率", required=false, maxLength=18, fixed=6, order=11)
	@JSONField(name = "exchrate")
	public java.math.BigDecimal getExchRate() {
		return getBigDecimal("ExchRate");
	}

	/**
	 * 税率
	 */
	public M setTaxRate(java.math.BigDecimal TaxRate) {
		set("TaxRate", TaxRate);
		return (M)this;
	}

	/**
	 * 税率
	 */
	@JBoltField(name="taxrate" ,columnName="TaxRate",type="BigDecimal", remark="税率", required=false, maxLength=9, fixed=2, order=12)
	@JSONField(name = "taxrate")
	public java.math.BigDecimal getTaxRate() {
		return getBigDecimal("TaxRate");
	}

	/**
	 * 发货地址
	 */
	public M setShipAddress(java.lang.String ShipAddress) {
		set("ShipAddress", ShipAddress);
		return (M)this;
	}

	/**
	 * 发货地址
	 */
	@JBoltField(name="shipaddress" ,columnName="ShipAddress",type="String", remark="发货地址", required=false, maxLength=100, fixed=0, order=13)
	@JSONField(name = "shipaddress")
	public java.lang.String getShipAddress() {
		return getStr("ShipAddress");
	}

	/**
	 * 收货地址
	 */
	public M setReceiveAddress(java.lang.String ReceiveAddress) {
		set("ReceiveAddress", ReceiveAddress);
		return (M)this;
	}

	/**
	 * 收货地址
	 */
	@JBoltField(name="receiveaddress" ,columnName="ReceiveAddress",type="String", remark="收货地址", required=false, maxLength=100, fixed=0, order=14)
	@JSONField(name = "receiveaddress")
	public java.lang.String getReceiveAddress() {
		return getStr("ReceiveAddress");
	}

	/**
	 * 供应商
	 */
	public M setVenCode(java.lang.String VenCode) {
		set("VenCode", VenCode);
		return (M)this;
	}

	/**
	 * 供应商
	 */
	@JBoltField(name="vencode" ,columnName="VenCode",type="String", remark="供应商", required=false, maxLength=30, fixed=0, order=15)
	@JSONField(name = "vencode")
	public java.lang.String getVenCode() {
		return getStr("VenCode");
	}

	/**
	 * 审核人
	 */
	public M setAuditPerson(java.lang.String AuditPerson) {
		set("AuditPerson", AuditPerson);
		return (M)this;
	}

	/**
	 * 审核人
	 */
	@JBoltField(name="auditperson" ,columnName="AuditPerson",type="String", remark="审核人", required=false, maxLength=30, fixed=0, order=16)
	@JSONField(name = "auditperson")
	public java.lang.String getAuditPerson() {
		return getStr("AuditPerson");
	}

	/**
	 * 审核日期
	 */
	public M setAuditDate(java.lang.String AuditDate) {
		set("AuditDate", AuditDate);
		return (M)this;
	}

	/**
	 * 审核日期
	 */
	@JBoltField(name="auditdate" ,columnName="AuditDate",type="String", remark="审核日期", required=false, maxLength=30, fixed=0, order=17)
	@JSONField(name = "auditdate")
	public java.lang.String getAuditDate() {
		return getStr("AuditDate");
	}

	/**
	 * 创建人
	 */
	public M setCreatePerson(java.lang.String CreatePerson) {
		set("CreatePerson", CreatePerson);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=18)
	@JSONField(name = "createperson")
	public java.lang.String getCreatePerson() {
		return getStr("CreatePerson");
	}

	/**
	 * 创建时间
	 */
	public M setCreateDate(java.util.Date CreateDate) {
		set("CreateDate", CreateDate);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=19)
	@JSONField(name = "createdate")
	public java.util.Date getCreateDate() {
		return getDate("CreateDate");
	}

	/**
	 * 更新人
	 */
	public M setModifyPerson(java.lang.String ModifyPerson) {
		set("ModifyPerson", ModifyPerson);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="更新人", required=false, maxLength=30, fixed=0, order=20)
	@JSONField(name = "modifyperson")
	public java.lang.String getModifyPerson() {
		return getStr("ModifyPerson");
	}

	/**
	 * 更新时间
	 */
	public M setModifyDate(java.util.Date ModifyDate) {
		set("ModifyDate", ModifyDate);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=21)
	@JSONField(name = "modifydate")
	public java.util.Date getModifyDate() {
		return getDate("ModifyDate");
	}

}
