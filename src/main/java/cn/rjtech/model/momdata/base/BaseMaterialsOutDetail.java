package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 材料出库单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMaterialsOutDetail<M extends BaseMaterialsOutDetail<M>> extends JBoltBaseModel<M>{
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**MasID*/
    public static final String MASID = "MasID";
    /**仓位*/
    public static final String POSCODE = "PosCode";
    /**条码*/
    public static final String BARCODE = "Barcode";
    /**存货编码*/
    public static final String INVCODE = "InvCode";
    /**件数*/
    public static final String NUM = "Num";
    /**数量*/
    public static final String QTY = "Qty";
    /**跟单类型*/
    public static final String TRACKTYPE = "TrackType";
    /**收容数量*/
    public static final String PACKRATE = "PackRate";
    /**来源单据类型*/
    public static final String SOURCEBILLTYPE = "SourceBillType";
    /**来源单号*/
    public static final String SOURCEBILLNO = "SourceBillNo";
    /**来源单号+行号*/
    public static final String SOURCEBILLNOROW = "SourceBIllNoRow";
    /**来源单据ID*/
    public static final String SOURCEBILLID = "SourceBillID";
    /**来源单据Did*/
    public static final String SOURCEBILLDID = "SourceBillDid";
    /**备注*/
    public static final String MEMO = "Memo";
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
	public M setAutoID(java.lang.Long AutoID) {
		set("AutoID", AutoID);
		return (M)this;
	}

	/**
	 * AutoID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="Long", remark="AutoID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "autoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getAutoID() {
		return getLong("AutoID");
	}

	/**
	 * MasID
	 */
	public M setMasID(java.lang.Long MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * MasID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="Long", remark="MasID", required=false, maxLength=19, fixed=0, order=2)
	@JSONField(name = "masid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getMasID() {
		return getLong("MasID");
	}

	/**
	 * 仓位
	 */
	public M setPosCode(java.lang.String PosCode) {
		set("PosCode", PosCode);
		return (M)this;
	}

	/**
	 * 仓位
	 */
	@JBoltField(name="poscode" ,columnName="PosCode",type="String", remark="仓位", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "poscode")
	public java.lang.String getPosCode() {
		return getStr("PosCode");
	}

	/**
	 * 条码
	 */
	public M setBarcode(java.lang.String Barcode) {
		set("Barcode", Barcode);
		return (M)this;
	}

	/**
	 * 条码
	 */
	@JBoltField(name="barcode" ,columnName="Barcode",type="String", remark="条码", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "barcode")
	public java.lang.String getBarcode() {
		return getStr("Barcode");
	}

	/**
	 * 存货编码
	 */
	public M setInvCode(java.lang.String InvCode) {
		set("InvCode", InvCode);
		return (M)this;
	}

	/**
	 * 存货编码
	 */
	@JBoltField(name="invcode" ,columnName="InvCode",type="String", remark="存货编码", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "invcode")
	public java.lang.String getInvCode() {
		return getStr("InvCode");
	}

	/**
	 * 件数
	 */
	public M setNum(java.math.BigDecimal Num) {
		set("Num", Num);
		return (M)this;
	}

	/**
	 * 件数
	 */
	@JBoltField(name="num" ,columnName="Num",type="BigDecimal", remark="件数", required=false, maxLength=18, fixed=6, order=6)
	@JSONField(name = "num")
	public java.math.BigDecimal getNum() {
		return getBigDecimal("Num");
	}

	/**
	 * 数量
	 */
	public M setQty(java.math.BigDecimal Qty) {
		set("Qty", Qty);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="数量", required=false, maxLength=18, fixed=6, order=7)
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
	@JBoltField(name="tracktype" ,columnName="TrackType",type="String", remark="跟单类型", required=false, maxLength=30, fixed=0, order=8)
	@JSONField(name = "tracktype")
	public java.lang.String getTrackType() {
		return getStr("TrackType");
	}

	/**
	 * 收容数量
	 */
	public M setPackRate(java.math.BigDecimal PackRate) {
		set("PackRate", PackRate);
		return (M)this;
	}

	/**
	 * 收容数量
	 */
	@JBoltField(name="packrate" ,columnName="PackRate",type="BigDecimal", remark="收容数量", required=false, maxLength=18, fixed=6, order=9)
	@JSONField(name = "packrate")
	public java.math.BigDecimal getPackRate() {
		return getBigDecimal("PackRate");
	}

	/**
	 * 来源单据类型
	 */
	public M setSourceBillType(java.lang.String SourceBillType) {
		set("SourceBillType", SourceBillType);
		return (M)this;
	}

	/**
	 * 来源单据类型
	 */
	@JBoltField(name="sourcebilltype" ,columnName="SourceBillType",type="String", remark="来源单据类型", required=false, maxLength=30, fixed=0, order=10)
	@JSONField(name = "sourcebilltype")
	public java.lang.String getSourceBillType() {
		return getStr("SourceBillType");
	}

	/**
	 * 来源单号
	 */
	public M setSourceBillNo(java.lang.String SourceBillNo) {
		set("SourceBillNo", SourceBillNo);
		return (M)this;
	}

	/**
	 * 来源单号
	 */
	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="来源单号", required=false, maxLength=30, fixed=0, order=11)
	@JSONField(name = "sourcebillno")
	public java.lang.String getSourceBillNo() {
		return getStr("SourceBillNo");
	}

	/**
	 * 来源单号+行号
	 */
	public M setSourceBIllNoRow(java.lang.String SourceBIllNoRow) {
		set("SourceBIllNoRow", SourceBIllNoRow);
		return (M)this;
	}

	/**
	 * 来源单号+行号
	 */
	@JBoltField(name="sourcebillnorow" ,columnName="SourceBIllNoRow",type="String", remark="来源单号+行号", required=false, maxLength=50, fixed=0, order=12)
	@JSONField(name = "sourcebillnorow")
	public java.lang.String getSourceBIllNoRow() {
		return getStr("SourceBIllNoRow");
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
	@JBoltField(name="sourcebillid" ,columnName="SourceBillID",type="String", remark="来源单据ID", required=false, maxLength=30, fixed=0, order=13)
	@JSONField(name = "sourcebillid")
	public java.lang.String getSourceBillID() {
		return getStr("SourceBillID");
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
	@JBoltField(name="sourcebilldid" ,columnName="SourceBillDid",type="String", remark="来源单据Did", required=false, maxLength=30, fixed=0, order=14)
	@JSONField(name = "sourcebilldid")
	public java.lang.String getSourceBillDid() {
		return getStr("SourceBillDid");
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
	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="备注", required=false, maxLength=32, fixed=0, order=15)
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
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=16)
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
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=17)
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
	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="更新人", required=false, maxLength=30, fixed=0, order=18)
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
	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=19)
	@JSONField(name = "modifydate")
	public java.util.Date getModifyDate() {
		return getDate("ModifyDate");
	}

}

