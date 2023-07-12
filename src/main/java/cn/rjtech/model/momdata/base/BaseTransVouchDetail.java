package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 调拨单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseTransVouchDetail<M extends BaseTransVouchDetail<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**MasID*/
    public static final String MASID = "MasID";
    /**调入货位*/
    public static final String IPOSCODE = "IPosCode";
    /**调出货位*/
    public static final String OPOSCODE = "OPosCode";
    /**条码*/
    public static final String BARCODE = "Barcode";
    /**存货编码*/
    public static final String INVCODE = "InvCode";
    /**件数*/
    public static final String NUM = "Num";
    /**数量*/
    public static final String QTY = "Qty";
    /**收容数量*/
    public static final String PACKRATE = "PackRate";
    /**跟单类型*/
    public static final String TRACKTYPE = "TrackType";
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
    /**是否删除：0. 否 1. 是*/
    public static final String ISDELETED = "isDeleted";
    /**创建人id*/
    public static final String ICREATEBY = "icreateby";
    /**创建人名称*/
    public static final String CCREATENAME = "ccreatename";
    /**创建时间*/
    public static final String DCREATETIME = "dcreatetime";
    /**修改人id*/
    public static final String IUPDATEBY = "iupdateby";
    /**修改人名称*/
    public static final String CUPDATENAME = "cupdatename";
    /**修改时间*/
    public static final String DUPDATETIME = "dupdatetime";
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
	 * MasID
	 */
	public M setMasID(java.lang.Long MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * MasID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="MasID", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "masid")
	public java.lang.Long getMasID() {
		return getLong("MasID");
	}

	/**
	 * 调入货位
	 */
	public M setIPosCode(java.lang.String IPosCode) {
		set("IPosCode", IPosCode);
		return (M)this;
	}

	/**
	 * 调入货位
	 */
	@JBoltField(name="iposcode" ,columnName="IPosCode",type="String", remark="调入货位", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "iposcode")
	public java.lang.String getIPosCode() {
		return getStr("IPosCode");
	}

	/**
	 * 调出货位
	 */
	public M setOPosCode(java.lang.String OPosCode) {
		set("OPosCode", OPosCode);
		return (M)this;
	}

	/**
	 * 调出货位
	 */
	@JBoltField(name="oposcode" ,columnName="OPosCode",type="String", remark="调出货位", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "oposcode")
	public java.lang.String getOPosCode() {
		return getStr("OPosCode");
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
	@JBoltField(name="barcode" ,columnName="Barcode",type="String", remark="条码", required=false, maxLength=30, fixed=0, order=5)
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
	@JBoltField(name="invcode" ,columnName="InvCode",type="String", remark="存货编码", required=false, maxLength=30, fixed=0, order=6)
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
	@JBoltField(name="num" ,columnName="Num",type="BigDecimal", remark="件数", required=false, maxLength=18, fixed=6, order=7)
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
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="数量", required=false, maxLength=18, fixed=6, order=8)
	@JSONField(name = "qty")
	public java.math.BigDecimal getQty() {
		return getBigDecimal("Qty");
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
	 * 跟单类型
	 */
	public M setTrackType(java.lang.String TrackType) {
		set("TrackType", TrackType);
		return (M)this;
	}

	/**
	 * 跟单类型
	 */
	@JBoltField(name="tracktype" ,columnName="TrackType",type="String", remark="跟单类型", required=false, maxLength=30, fixed=0, order=10)
	@JSONField(name = "tracktype")
	public java.lang.String getTrackType() {
		return getStr("TrackType");
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
	@JBoltField(name="sourcebilltype" ,columnName="SourceBillType",type="String", remark="来源单据类型", required=false, maxLength=30, fixed=0, order=11)
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
	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="来源单号", required=false, maxLength=30, fixed=0, order=12)
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
	@JBoltField(name="sourcebillnorow" ,columnName="SourceBIllNoRow",type="String", remark="来源单号+行号", required=false, maxLength=50, fixed=0, order=13)
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
	@JBoltField(name="sourcebillid" ,columnName="SourceBillID",type="String", remark="来源单据ID", required=false, maxLength=30, fixed=0, order=14)
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
	@JBoltField(name="sourcebilldid" ,columnName="SourceBillDid",type="String", remark="来源单据Did", required=false, maxLength=30, fixed=0, order=15)
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
	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="备注", required=false, maxLength=32, fixed=0, order=16)
	@JSONField(name = "memo")
	public java.lang.String getMemo() {
		return getStr("Memo");
	}

	/**
	 * 是否删除：0. 否 1. 是
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 是否删除：0. 否 1. 是
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=17)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 创建人id
	 */
	public M setIcreateby(java.lang.Long icreateby) {
		set("icreateby", icreateby);
		return (M)this;
	}

	/**
	 * 创建人id
	 */
	@JBoltField(name="icreateby" ,columnName="icreateby",type="Long", remark="创建人id", required=false, maxLength=19, fixed=0, order=18)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIcreateby() {
		return getLong("icreateby");
	}

	/**
	 * 创建人名称
	 */
	public M setCcreatename(java.lang.String ccreatename) {
		set("ccreatename", ccreatename);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="ccreatename",type="String", remark="创建人名称", required=false, maxLength=30, fixed=0, order=19)
	@JSONField(name = "ccreatename")
	public java.lang.String getCcreatename() {
		return getStr("ccreatename");
	}

	/**
	 * 创建时间
	 */
	public M setDcreatetime(java.util.Date dcreatetime) {
		set("dcreatetime", dcreatetime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dcreatetime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=20)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDcreatetime() {
		return getDate("dcreatetime");
	}

	/**
	 * 修改人id
	 */
	public M setIupdateby(java.lang.Long iupdateby) {
		set("iupdateby", iupdateby);
		return (M)this;
	}

	/**
	 * 修改人id
	 */
	@JBoltField(name="iupdateby" ,columnName="iupdateby",type="Long", remark="修改人id", required=false, maxLength=19, fixed=0, order=21)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iupdateby");
	}

	/**
	 * 修改人名称
	 */
	public M setCupdatename(java.lang.String cupdatename) {
		set("cupdatename", cupdatename);
		return (M)this;
	}

	/**
	 * 修改人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cupdatename",type="String", remark="修改人名称", required=false, maxLength=30, fixed=0, order=22)
	@JSONField(name = "cupdatename")
	public java.lang.String getCupdatename() {
		return getStr("cupdatename");
	}

	/**
	 * 修改时间
	 */
	public M setDupdatetime(java.util.Date dupdatetime) {
		set("dupdatetime", dupdatetime);
		return (M)this;
	}

	/**
	 * 修改时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dupdatetime",type="Date", remark="修改时间", required=false, maxLength=23, fixed=3, order=23)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDupdatetime() {
		return getDate("dupdatetime");
	}

}

