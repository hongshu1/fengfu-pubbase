package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 组装拆卸及形态转换单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSysAssemdetail<M extends BaseSysAssemdetail<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**主表ID;T_Sys_Assem.AutoID*/
    public static final String MASID = "MasID";
    /***/
    public static final String BARCODE = "Barcode";
    /**来源类型*/
    public static final String SOURCETYPE = "SourceType";
    /**来源单号*/
    public static final String SOURCEBILLNO = "SourceBillNo";
    /**来源单号+行号*/
    public static final String SOURCEBILLNOROW = "SourceBillNoRow";
    /**来源单据ID*/
    public static final String SOURCEBILLID = "SourceBillID";
    /**来源单据DID*/
    public static final String SOURCEBILLDID = "SourceBillDid";
    /**转换状态;转换前 及转换后*/
    public static final String ASSEMTYPE = "AssemType";
    /**仓库编码*/
    public static final String WHCODE = "WhCode";
    /**货位编码*/
    public static final String POSCODE = "PosCode";
    /**组号*/
    public static final String COMBINATIONNO = "CombinationNo";
    /**行号*/
    public static final String ROWNO = "RowNo";
    /**入库数量*/
    public static final String QTY = "Qty";
    /**跟单类型*/
    public static final String TRACKTYPE = "TrackType";
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
	 * 主表ID;T_Sys_Assem.AutoID
	 */
	public M setMasID(java.lang.String MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * 主表ID;T_Sys_Assem.AutoID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="主表ID;T_Sys_Assem.AutoID", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "masid")
	public java.lang.String getMasID() {
		return getStr("MasID");
	}

	public M setBarcode(java.lang.String Barcode) {
		set("Barcode", Barcode);
		return (M)this;
	}

	@JBoltField(name="barcode" ,columnName="Barcode",type="String", remark="BARCODE", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "barcode")
	public java.lang.String getBarcode() {
		return getStr("Barcode");
	}

	/**
	 * 来源类型
	 */
	public M setSourceType(java.lang.String SourceType) {
		set("SourceType", SourceType);
		return (M)this;
	}

	/**
	 * 来源类型
	 */
	@JBoltField(name="sourcetype" ,columnName="SourceType",type="String", remark="来源类型", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "sourcetype")
	public java.lang.String getSourceType() {
		return getStr("SourceType");
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
	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="来源单号", required=false, maxLength=32, fixed=0, order=5)
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
	@JBoltField(name="sourcebillnorow" ,columnName="SourceBillNoRow",type="String", remark="来源单号+行号", required=false, maxLength=32, fixed=0, order=6)
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
	@JBoltField(name="sourcebillid" ,columnName="SourceBillID",type="String", remark="来源单据ID", required=false, maxLength=32, fixed=0, order=7)
	@JSONField(name = "sourcebillid")
	public java.lang.String getSourceBillID() {
		return getStr("SourceBillID");
	}

	/**
	 * 来源单据DID
	 */
	public M setSourceBillDid(java.lang.String SourceBillDid) {
		set("SourceBillDid", SourceBillDid);
		return (M)this;
	}

	/**
	 * 来源单据DID
	 */
	@JBoltField(name="sourcebilldid" ,columnName="SourceBillDid",type="String", remark="来源单据DID", required=false, maxLength=30, fixed=0, order=8)
	@JSONField(name = "sourcebilldid")
	public java.lang.String getSourceBillDid() {
		return getStr("SourceBillDid");
	}

	/**
	 * 转换状态;转换前 及转换后
	 */
	public M setAssemType(java.lang.String AssemType) {
		set("AssemType", AssemType);
		return (M)this;
	}

	/**
	 * 转换状态;转换前 及转换后
	 */
	@JBoltField(name="assemtype" ,columnName="AssemType",type="String", remark="转换状态;转换前 及转换后", required=false, maxLength=30, fixed=0, order=9)
	@JSONField(name = "assemtype")
	public java.lang.String getAssemType() {
		return getStr("AssemType");
	}

	/**
	 * 仓库编码
	 */
	public M setWhCode(java.lang.String WhCode) {
		set("WhCode", WhCode);
		return (M)this;
	}

	/**
	 * 仓库编码
	 */
	@JBoltField(name="whcode" ,columnName="WhCode",type="String", remark="仓库编码", required=false, maxLength=30, fixed=0, order=10)
	@JSONField(name = "whcode")
	public java.lang.String getWhCode() {
		return getStr("WhCode");
	}

	/**
	 * 货位编码
	 */
	public M setPosCode(java.lang.String PosCode) {
		set("PosCode", PosCode);
		return (M)this;
	}

	/**
	 * 货位编码
	 */
	@JBoltField(name="poscode" ,columnName="PosCode",type="String", remark="货位编码", required=false, maxLength=30, fixed=0, order=11)
	@JSONField(name = "poscode")
	public java.lang.String getPosCode() {
		return getStr("PosCode");
	}

	/**
	 * 组号
	 */
	public M setCombinationNo(java.lang.Integer CombinationNo) {
		set("CombinationNo", CombinationNo);
		return (M)this;
	}

	/**
	 * 组号
	 */
	@JBoltField(name="combinationno" ,columnName="CombinationNo",type="Integer", remark="组号", required=false, maxLength=10, fixed=0, order=12)
	@JSONField(name = "combinationno")
	public java.lang.Integer getCombinationNo() {
		return getInt("CombinationNo");
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
	@JBoltField(name="rowno" ,columnName="RowNo",type="Integer", remark="行号", required=false, maxLength=10, fixed=0, order=13)
	@JSONField(name = "rowno")
	public java.lang.Integer getRowNo() {
		return getInt("RowNo");
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
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="入库数量", required=false, maxLength=18, fixed=6, order=14)
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
	@JBoltField(name="tracktype" ,columnName="TrackType",type="String", remark="跟单类型", required=false, maxLength=30, fixed=0, order=15)
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

