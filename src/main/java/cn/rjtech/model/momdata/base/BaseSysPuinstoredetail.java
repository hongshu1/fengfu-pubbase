package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 采购入库单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSysPuinstoredetail<M extends BaseSysPuinstoredetail<M>> extends JBoltBaseModel<M>{
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**来源类型;PO 采购 OM委外*/
    public static final String SOURCEBILLTYPE = "SourceBillType";
    /**来源单号（订单号）*/
    public static final String SOURCEBILLNO = "SourceBillNo";
    /**来源单号+行号*/
    public static final String SOURCEBILLNOROW = "SourceBillNoRow";
    /**来源单据ID*/
    public static final String SOURCEBILLID = "SourceBillID";
    /**来源单据DID;采购或委外单身ID*/
    public static final String SOURCEBILLDID = "SourceBillDid";
    /**行号*/
    public static final String ROWNO = "RowNo";
    /**主表ID;T_Sys_PUInStore.AutoID*/
    public static final String MASID = "MasID";
    /**重量*/
    public static final String WEIGHT = "Weight";
    /**仓库*/
    public static final String WHCODE = "Whcode";
    /**库位*/
    public static final String POSCODE = "PosCode";
    /**入库数量*/
    public static final String QTY = "Qty";
    /**跟单类型*/
    public static final String TRACKTYPE = "TrackType";
    /**备注*/
    public static final String MEMO = "Memo";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建时间*/
    public static final String CREATEDATE = "CreateDate";
    /**修改人*/
    public static final String MODIFYPERSON = "ModifyPerson";
    /**修改时间*/
    public static final String MODIFYDATE = "ModifyDate";
    /**现品票*/
    public static final String SPOTTICKET = "spotTicket";
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
	 * 来源类型;PO 采购 OM委外
	 */
	public M setSourceBillType(java.lang.String SourceBillType) {
		set("SourceBillType", SourceBillType);
		return (M)this;
	}

	/**
	 * 来源类型;PO 采购 OM委外
	 */
	@JBoltField(name="sourcebilltype" ,columnName="SourceBillType",type="String", remark="来源类型;PO 采购 OM委外", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "sourcebilltype")
	public java.lang.String getSourceBillType() {
		return getStr("SourceBillType");
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
	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="来源单号（订单号）", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "sourcebillno")
	public java.lang.String getSourceBillNo() {
		return getStr("SourceBillNo");
	}

	/**
	 * 来源单号+行号
	 */
	public M setSourceBillNoRow(java.lang.String SourceBillNoRow) {
		set("SourceBillNoRow", SourceBillNoRow);
		return (M)this;
	}

	/**
	 * 来源单号+行号
	 */
	@JBoltField(name="sourcebillnorow" ,columnName="SourceBillNoRow",type="String", remark="来源单号+行号", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "sourcebillnorow")
	public java.lang.String getSourceBillNoRow() {
		return getStr("SourceBillNoRow");
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
	@JBoltField(name="sourcebillid" ,columnName="SourceBillID",type="String", remark="来源单据ID", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "sourcebillid")
	public java.lang.String getSourceBillID() {
		return getStr("SourceBillID");
	}

	/**
	 * 来源单据DID;采购或委外单身ID
	 */
	public M setSourceBillDid(java.lang.String SourceBillDid) {
		set("SourceBillDid", SourceBillDid);
		return (M)this;
	}

	/**
	 * 来源单据DID;采购或委外单身ID
	 */
	@JBoltField(name="sourcebilldid" ,columnName="SourceBillDid",type="String", remark="来源单据DID;采购或委外单身ID", required=false, maxLength=30, fixed=0, order=6)
	@JSONField(name = "sourcebilldid")
	public java.lang.String getSourceBillDid() {
		return getStr("SourceBillDid");
	}

	/**
	 * 行号
	 */
	public M setRowNo(java.lang.Integer RowNo) {
		set("RowNo", RowNo);
		return (M)this;
	}

	/**
	 * 行号
	 */
	@JBoltField(name="rowno" ,columnName="RowNo",type="Integer", remark="行号", required=false, maxLength=10, fixed=0, order=7)
	@JSONField(name = "rowno")
	public java.lang.Integer getRowNo() {
		return getInt("RowNo");
	}

	/**
	 * 主表ID;T_Sys_PUInStore.AutoID
	 */
	public M setMasID(java.lang.String MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * 主表ID;T_Sys_PUInStore.AutoID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="主表ID;T_Sys_PUInStore.AutoID", required=false, maxLength=30, fixed=0, order=8)
	@JSONField(name = "masid")
	public java.lang.String getMasID() {
		return getStr("MasID");
	}

	/**
	 * 重量
	 */
	public M setWeight(java.lang.String Weight) {
		set("Weight", Weight);
		return (M)this;
	}

	/**
	 * 重量
	 */
	@JBoltField(name="weight" ,columnName="Weight",type="String", remark="重量", required=false, maxLength=32, fixed=0, order=9)
	@JSONField(name = "weight")
	public java.lang.String getWeight() {
		return getStr("Weight");
	}

	/**
	 * 仓库
	 */
	public M setWhcode(java.lang.String Whcode) {
		set("Whcode", Whcode);
		return (M)this;
	}

	/**
	 * 仓库
	 */
	@JBoltField(name="whcode" ,columnName="Whcode",type="String", remark="仓库", required=false, maxLength=32, fixed=0, order=10)
	@JSONField(name = "whcode")
	public java.lang.String getWhcode() {
		return getStr("Whcode");
	}

	/**
	 * 库位
	 */
	public M setPosCode(java.lang.String PosCode) {
		set("PosCode", PosCode);
		return (M)this;
	}

	/**
	 * 库位
	 */
	@JBoltField(name="poscode" ,columnName="PosCode",type="String", remark="库位", required=false, maxLength=32, fixed=0, order=11)
	@JSONField(name = "poscode")
	public java.lang.String getPosCode() {
		return getStr("PosCode");
	}

	/**
	 * 入库数量
	 */
	public M setQty(java.math.BigDecimal Qty) {
		set("Qty", Qty);
		return (M)this;
	}

	/**
	 * 入库数量
	 */
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="入库数量", required=false, maxLength=18, fixed=6, order=12)
	@JSONField(name = "qty")
	public java.math.BigDecimal getQty() {
		return getBigDecimal("Qty");
	}

	/**
	 * 跟单类型
	 */
	public M setTrackType(java.lang.String TrackType) {
		set("TrackType", TrackType);
		return (M)this;
	}

	/**
	 * 跟单类型
	 */
	@JBoltField(name="tracktype" ,columnName="TrackType",type="String", remark="跟单类型", required=false, maxLength=30, fixed=0, order=13)
	@JSONField(name = "tracktype")
	public java.lang.String getTrackType() {
		return getStr("TrackType");
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
	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="备注", required=false, maxLength=32, fixed=0, order=14)
	@JSONField(name = "memo")
	public java.lang.String getMemo() {
		return getStr("Memo");
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
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=15)
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
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=16)
	@JSONField(name = "createdate")
	public java.util.Date getCreateDate() {
		return getDate("CreateDate");
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
	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="修改人", required=false, maxLength=30, fixed=0, order=17)
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
	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="修改时间", required=false, maxLength=23, fixed=3, order=18)
	@JSONField(name = "modifydate")
	public java.util.Date getModifyDate() {
		return getDate("ModifyDate");
	}

	/**
	 * 现品票
	 */
	public M setSpotTicket(java.lang.String spotTicket) {
		set("spotTicket", spotTicket);
		return (M)this;
	}

	/**
	 * 现品票
	 */
	@JBoltField(name="spotticket" ,columnName="spotTicket",type="String", remark="现品票", required=false, maxLength=50, fixed=0, order=19)
	@JSONField(name = "spotticket")
	public java.lang.String getSpotTicket() {
		return getStr("spotTicket");
	}

}

