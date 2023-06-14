package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 采购收料单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSysPureceivedetail<M extends BaseSysPureceivedetail<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**来源类型;PO 采购 OM委外*/
    public static final String SOURCEBILLTYPE = "SourceBillType";
    /**来源单号*/
    public static final String SOURCEBILLNO = "SourceBillNo";
    /**来源单号+行号*/
    public static final String SOURCEBILLNOROW = "SourceBillNoRow";
    /**来源单据ID*/
    public static final String SOURCEBILLID = "SourceBillID";
    /**来源单据DID;采购或委外单身ID*/
    public static final String SOURCEBILLDID = "SourceBillDid";
    /**行号*/
    public static final String ROWNO = "RowNo";
    /**主表ID;T_Sys_PUReceive.AutoID*/
    public static final String MASID = "MasID";
    /**重量*/
    public static final String WEIGHT = "Weight";
    /**入库区*/
    public static final String WHCODE = "Whcode";
    /**库位*/
    public static final String POSCODE = "PosCode";
    /**实收数量*/
    public static final String QTY = "Qty";
    /**跟单类型*/
    public static final String TRACKTYPE = "TrackType";
    /**备注*/
    public static final String MEMO = "Memo";
    /**条码类型*/
    public static final String BARCODETYPE = "BarcodeType";
    /**现品票*/
    public static final String BARCODE = "Barcode";
    /**是否初物*/
    public static final String ISINITIAL = "IsInitial";
    /**组号*/
    public static final String COMBINATION = "Combination";
    /***/
    public static final String VENCODE = "VenCode";
    /**是否删除：0. 否 1. 是*/
    public static final String ISDELETED = "isDeleted";
    /**创建人id*/
    public static final String ICREATEBY = "icreateby";
    /**创建人名称*/
    public static final String CCREATENAME = "ccreatename";
    /**创建时间*/
    public static final String DCREATETIME = "dcreatetime";
    /**更新人id*/
    public static final String IUPDATEBY = "iupdateby";
    /**更新人名称*/
    public static final String CUPDATENAME = "cupdatename";
    /**更新时间*/
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
	 * 来源单号
	 */
	public M setSourceBillNo(java.lang.String SourceBillNo) {
		set("SourceBillNo", SourceBillNo);
		return (M)this;
	}

	/**
	 * 来源单号
	 */
	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="来源单号", required=false, maxLength=30, fixed=0, order=3)
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
	 * 主表ID;T_Sys_PUReceive.AutoID
	 */
	public M setMasID(java.lang.String MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * 主表ID;T_Sys_PUReceive.AutoID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="主表ID;T_Sys_PUReceive.AutoID", required=false, maxLength=30, fixed=0, order=8)
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
	 * 入库区
	 */
	public M setWhcode(java.lang.String Whcode) {
		set("Whcode", Whcode);
		return (M)this;
	}

	/**
	 * 入库区
	 */
	@JBoltField(name="whcode" ,columnName="Whcode",type="String", remark="入库区", required=false, maxLength=32, fixed=0, order=10)
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
	 * 实收数量
	 */
	public M setQty(java.math.BigDecimal Qty) {
		set("Qty", Qty);
		return (M)this;
	}

	/**
	 * 实收数量
	 */
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="实收数量", required=false, maxLength=18, fixed=2, order=12)
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
	 * 条码类型
	 */
	public M setBarcodeType(java.lang.String BarcodeType) {
		set("BarcodeType", BarcodeType);
		return (M)this;
	}

	/**
	 * 条码类型
	 */
	@JBoltField(name="barcodetype" ,columnName="BarcodeType",type="String", remark="条码类型", required=true, maxLength=50, fixed=0, order=15)
	@JSONField(name = "barcodetype")
	public java.lang.String getBarcodeType() {
		return getStr("BarcodeType");
	}

	/**
	 * 现品票
	 */
	public M setBarcode(java.lang.String Barcode) {
		set("Barcode", Barcode);
		return (M)this;
	}

	/**
	 * 现品票
	 */
	@JBoltField(name="barcode" ,columnName="Barcode",type="String", remark="现品票", required=false, maxLength=50, fixed=0, order=16)
	@JSONField(name = "barcode")
	public java.lang.String getBarcode() {
		return getStr("Barcode");
	}

	/**
	 * 是否初物
	 */
	public M setIsInitial(java.lang.String IsInitial) {
		set("IsInitial", IsInitial);
		return (M)this;
	}

	/**
	 * 是否初物
	 */
	@JBoltField(name="isinitial" ,columnName="IsInitial",type="String", remark="是否初物", required=false, maxLength=30, fixed=0, order=17)
	@JSONField(name = "isinitial")
	public java.lang.String getIsInitial() {
		return getStr("IsInitial");
	}

	/**
	 * 组号
	 */
	public M setCombination(java.lang.String Combination) {
		set("Combination", Combination);
		return (M)this;
	}

	/**
	 * 组号
	 */
	@JBoltField(name="combination" ,columnName="Combination",type="String", remark="组号", required=false, maxLength=50, fixed=0, order=18)
	@JSONField(name = "combination")
	public java.lang.String getCombination() {
		return getStr("Combination");
	}

	public M setVenCode(java.lang.String VenCode) {
		set("VenCode", VenCode);
		return (M)this;
	}

	@JBoltField(name="vencode" ,columnName="VenCode",type="String", remark="VENCODE", required=false, maxLength=50, fixed=0, order=19)
	@JSONField(name = "vencode")
	public java.lang.String getVenCode() {
		return getStr("VenCode");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=20)
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
	@JBoltField(name="icreateby" ,columnName="icreateby",type="Long", remark="创建人id", required=false, maxLength=19, fixed=0, order=21)
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
	@JBoltField(name="ccreatename" ,columnName="ccreatename",type="String", remark="创建人名称", required=false, maxLength=30, fixed=0, order=22)
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
	@JBoltField(name="dcreatetime" ,columnName="dcreatetime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=23)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDcreatetime() {
		return getDate("dcreatetime");
	}

	/**
	 * 更新人id
	 */
	public M setIupdateby(java.lang.Long iupdateby) {
		set("iupdateby", iupdateby);
		return (M)this;
	}

	/**
	 * 更新人id
	 */
	@JBoltField(name="iupdateby" ,columnName="iupdateby",type="Long", remark="更新人id", required=false, maxLength=19, fixed=0, order=24)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iupdateby");
	}

	/**
	 * 更新人名称
	 */
	public M setCupdatename(java.lang.String cupdatename) {
		set("cupdatename", cupdatename);
		return (M)this;
	}

	/**
	 * 更新人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cupdatename",type="String", remark="更新人名称", required=false, maxLength=30, fixed=0, order=25)
	@JSONField(name = "cupdatename")
	public java.lang.String getCupdatename() {
		return getStr("cupdatename");
	}

	/**
	 * 更新时间
	 */
	public M setDupdatetime(java.util.Date dupdatetime) {
		set("dupdatetime", dupdatetime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dupdatetime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=26)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDupdatetime() {
		return getDate("dupdatetime");
	}

}

