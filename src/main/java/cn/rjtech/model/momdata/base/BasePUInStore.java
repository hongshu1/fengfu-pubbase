package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 采购入库单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BasePUInStore<M extends BasePUInStore<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String AUTOID = "AutoID";
    /**采购PO  委外OM（采购类型）*/
    public static final String BILLTYPE = "BillType";
    /**组织编码*/
    public static final String ORGANIZECODE = "OrganizeCode";
    /**入库单号*/
    public static final String BILLNO = "BillNo";
    /**单据日期*/
    public static final String BILLDATE = "BillDate";
    /**收发类别（入库类别编码）*/
    public static final String RDCODE = "RdCode";
    /**部门*/
    public static final String DEPTCODE = "DeptCode";
    /**来源单号（订单号）*/
    public static final String SOURCEBILLNO = "SourceBillNo";
    /**来源单据ID*/
    public static final String SOURCEBILLID = "SourceBillID";
    /**供应商编码*/
    public static final String VENCODE = "VenCode";
    /**备注*/
    public static final String MEMO = "Memo";
    /**创建人（包括业务员、制单人）*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建日期*/
    public static final String CREATEDATE = "CreateDate";
    /**审核人*/
    public static final String AUDITPERSON = "AuditPerson";
    /**审核日期*/
    public static final String AUDITDATE = "AuditDate";
    /**修改人*/
    public static final String MODIFYPERSON = "ModifyPerson";
    /**修改时间*/
    public static final String MODIFYDATE = "ModifyDate";
    /**状态 1已保存 2待审批 3已审批 4审批不通过 */
    public static final String STATE = "State";
	/**
	 * 主键ID
	 */
	public M setAutoID(java.lang.String AutoID) {
		set("AutoID", AutoID);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="String", remark="主键ID", required=true, maxLength=30, fixed=0, order=1)
	@JSONField(name = "autoid")
	public java.lang.String getAutoID() {
		return getStr("AutoID");
	}

	/**
	 * 采购PO  委外OM（采购类型）
	 */
	public M setBillType(java.lang.String BillType) {
		set("BillType", BillType);
		return (M)this;
	}

	/**
	 * 采购PO  委外OM（采购类型）
	 */
	@JBoltField(name="billtype" ,columnName="BillType",type="String", remark="采购PO  委外OM（采购类型）", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "billtype")
	public java.lang.String getBillType() {
		return getStr("BillType");
	}

	/**
	 * 组织编码
	 */
	public M setOrganizeCode(java.lang.String OrganizeCode) {
		set("OrganizeCode", OrganizeCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="organizecode" ,columnName="OrganizeCode",type="String", remark="组织编码", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "organizecode")
	public java.lang.String getOrganizeCode() {
		return getStr("OrganizeCode");
	}

	/**
	 * 入库单号
	 */
	public M setBillNo(java.lang.String BillNo) {
		set("BillNo", BillNo);
		return (M)this;
	}

	/**
	 * 入库单号
	 */
	@JBoltField(name="billno" ,columnName="BillNo",type="String", remark="入库单号", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "billno")
	public java.lang.String getBillNo() {
		return getStr("BillNo");
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
	@JBoltField(name="billdate" ,columnName="BillDate",type="String", remark="单据日期", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "billdate")
	public java.lang.String getBillDate() {
		return getStr("BillDate");
	}

	/**
	 * 收发类别（入库类别编码）
	 */
	public M setRdCode(java.lang.String RdCode) {
		set("RdCode", RdCode);
		return (M)this;
	}

	/**
	 * 收发类别（入库类别编码）
	 */
	@JBoltField(name="rdcode" ,columnName="RdCode",type="String", remark="收发类别（入库类别编码）", required=false, maxLength=30, fixed=0, order=6)
	@JSONField(name = "rdcode")
	public java.lang.String getRdCode() {
		return getStr("RdCode");
	}

	/**
	 * 部门
	 */
	public M setDeptCode(java.lang.String DeptCode) {
		set("DeptCode", DeptCode);
		return (M)this;
	}

	/**
	 * 部门
	 */
	@JBoltField(name="deptcode" ,columnName="DeptCode",type="String", remark="部门", required=false, maxLength=30, fixed=0, order=7)
	@JSONField(name = "deptcode")
	public java.lang.String getDeptCode() {
		return getStr("DeptCode");
	}

	/**
	 * 来源单号（订单号）
	 */
	public M setSourceBillNo(java.lang.String SourceBillNo) {
		set("SourceBillNo", SourceBillNo);
		return (M)this;
	}

	/**
	 * 来源单号（订单号）
	 */
	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="来源单号（订单号）", required=false, maxLength=30, fixed=0, order=8)
	@JSONField(name = "sourcebillno")
	public java.lang.String getSourceBillNo() {
		return getStr("SourceBillNo");
	}

	/**
	 * 来源单据ID
	 */
	public M setSourceBillID(java.lang.String SourceBillID) {
		set("SourceBillID", SourceBillID);
		return (M)this;
	}

	/**
	 * 来源单据ID
	 */
	@JBoltField(name="sourcebillid" ,columnName="SourceBillID",type="String", remark="来源单据ID", required=false, maxLength=30, fixed=0, order=9)
	@JSONField(name = "sourcebillid")
	public java.lang.String getSourceBillID() {
		return getStr("SourceBillID");
	}

	/**
	 * 供应商编码
	 */
	public M setVenCode(java.lang.String VenCode) {
		set("VenCode", VenCode);
		return (M)this;
	}

	/**
	 * 供应商编码
	 */
	@JBoltField(name="vencode" ,columnName="VenCode",type="String", remark="供应商编码", required=false, maxLength=30, fixed=0, order=10)
	@JSONField(name = "vencode")
	public java.lang.String getVenCode() {
		return getStr("VenCode");
	}

	/**
	 * 备注
	 */
	public M setMemo(java.lang.String Memo) {
		set("Memo", Memo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="备注", required=false, maxLength=32, fixed=0, order=11)
	@JSONField(name = "memo")
	public java.lang.String getMemo() {
		return getStr("Memo");
	}

	/**
	 * 创建人（包括业务员、制单人）
	 */
	public M setCreatePerson(java.lang.String CreatePerson) {
		set("CreatePerson", CreatePerson);
		return (M)this;
	}

	/**
	 * 创建人（包括业务员、制单人）
	 */
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人（包括业务员、制单人）", required=false, maxLength=30, fixed=0, order=12)
	@JSONField(name = "createperson")
	public java.lang.String getCreatePerson() {
		return getStr("CreatePerson");
	}

	/**
	 * 创建日期
	 */
	public M setCreateDate(java.util.Date CreateDate) {
		set("CreateDate", CreateDate);
		return (M)this;
	}

	/**
	 * 创建日期
	 */
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建日期", required=false, maxLength=23, fixed=3, order=13)
	@JSONField(name = "createdate")
	public java.util.Date getCreateDate() {
		return getDate("CreateDate");
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
	@JBoltField(name="auditperson" ,columnName="AuditPerson",type="String", remark="审核人", required=false, maxLength=30, fixed=0, order=14)
	@JSONField(name = "auditperson")
	public java.lang.String getAuditPerson() {
		return getStr("AuditPerson");
	}

	/**
	 * 审核日期
	 */
	public M setAuditDate(java.util.Date AuditDate) {
		set("AuditDate", AuditDate);
		return (M)this;
	}

	/**
	 * 审核日期
	 */
	@JBoltField(name="auditdate" ,columnName="AuditDate",type="Date", remark="审核日期", required=false, maxLength=23, fixed=3, order=15)
	@JSONField(name = "auditdate")
	public java.util.Date getAuditDate() {
		return getDate("AuditDate");
	}

	/**
	 * 修改人
	 */
	public M setModifyPerson(java.lang.String ModifyPerson) {
		set("ModifyPerson", ModifyPerson);
		return (M)this;
	}

	/**
	 * 修改人
	 */
	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="修改人", required=false, maxLength=30, fixed=0, order=16)
	@JSONField(name = "modifyperson")
	public java.lang.String getModifyPerson() {
		return getStr("ModifyPerson");
	}

	/**
	 * 修改时间
	 */
	public M setModifyDate(java.util.Date ModifyDate) {
		set("ModifyDate", ModifyDate);
		return (M)this;
	}

	/**
	 * 修改时间
	 */
	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="修改时间", required=false, maxLength=23, fixed=3, order=17)
	@JSONField(name = "modifydate")
	public java.util.Date getModifyDate() {
		return getDate("ModifyDate");
	}

	/**
	 * 状态 1已保存 2待审批 3已审批 4审批不通过 
	 */
	public M setState(java.lang.String State) {
		set("State", State);
		return (M)this;
	}

	/**
	 * 状态 1已保存 2待审批 3已审批 4审批不通过 
	 */
	@JBoltField(name="state" ,columnName="State",type="String", remark="状态 1已保存 2待审批 3已审批 4审批不通过 ", required=false, maxLength=30, fixed=0, order=18)
	@JSONField(name = "state")
	public java.lang.String getState() {
		return getStr("State");
	}

}

