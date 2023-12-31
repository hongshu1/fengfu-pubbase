package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 扫码发货明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSysScandeliverdetail<M extends BaseSysScandeliverdetail<M>> extends JBoltBaseModel<M>{
    
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**MasID;T_Sys_ScanDeliver.AutoID*/
    public static final String MASID = "MasID";
    /**仓库编码*/
    public static final String WHCODE = "WhCode";
    /**仓位编码*/
    public static final String POSCODE = "PosCode";
    /**客户传票号*/
    public static final String CUSBARCODE = "CusBarcode";
    /**现品票*/
    public static final String BARCODE = "Barcode";
    /**容器编码*/
    public static final String PACKBARCODE = "PackBarcode";
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
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**修改人id*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**修改人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**修改时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**批次号*/
    public static final String BATCH = "Batch";
    /**计划数量*/
    public static final String PLANQTY = "PlanQty";
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
	 * MasID;T_Sys_ScanDeliver.AutoID
	 */
	public M setMasID(java.lang.String MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * MasID;T_Sys_ScanDeliver.AutoID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="MasID;T_Sys_ScanDeliver.AutoID", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "masid")
	public java.lang.String getMasID() {
		return getStr("MasID");
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
	@JBoltField(name="whcode" ,columnName="WhCode",type="String", remark="仓库编码", required=false, maxLength=32, fixed=0, order=3)
	@JSONField(name = "whcode")
	public java.lang.String getWhCode() {
		return getStr("WhCode");
	}

	/**
	 * 仓位编码
	 */
	public M setPosCode(java.lang.String PosCode) {
		set("PosCode", PosCode);
		return (M)this;
	}

	/**
	 * 仓位编码
	 */
	@JBoltField(name="poscode" ,columnName="PosCode",type="String", remark="仓位编码", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "poscode")
	public java.lang.String getPosCode() {
		return getStr("PosCode");
	}

	/**
	 * 客户传票号
	 */
	public M setCusBarcode(java.lang.String CusBarcode) {
		set("CusBarcode", CusBarcode);
		return (M)this;
	}

	/**
	 * 客户传票号
	 */
	@JBoltField(name="cusbarcode" ,columnName="CusBarcode",type="String", remark="客户传票号", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "cusbarcode")
	public java.lang.String getCusBarcode() {
		return getStr("CusBarcode");
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
	@JBoltField(name="barcode" ,columnName="Barcode",type="String", remark="现品票", required=false, maxLength=100, fixed=0, order=6)
	@JSONField(name = "barcode")
	public java.lang.String getBarcode() {
		return getStr("Barcode");
	}

	/**
	 * 容器编码
	 */
	public M setPackBarcode(java.lang.String PackBarcode) {
		set("PackBarcode", PackBarcode);
		return (M)this;
	}

	/**
	 * 容器编码
	 */
	@JBoltField(name="packbarcode" ,columnName="PackBarcode",type="String", remark="容器编码", required=false, maxLength=100, fixed=0, order=7)
	@JSONField(name = "packbarcode")
	public java.lang.String getPackBarcode() {
		return getStr("PackBarcode");
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
	@JBoltField(name="invcode" ,columnName="InvCode",type="String", remark="存货编码", required=false, maxLength=100, fixed=0, order=8)
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
	@JBoltField(name="num" ,columnName="Num",type="BigDecimal", remark="件数", required=false, maxLength=18, fixed=6, order=9)
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
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="数量", required=false, maxLength=18, fixed=6, order=10)
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
	@JBoltField(name="packrate" ,columnName="PackRate",type="BigDecimal", remark="收容数量", required=false, maxLength=18, fixed=6, order=11)
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
	@JBoltField(name="tracktype" ,columnName="TrackType",type="String", remark="跟单类型", required=false, maxLength=30, fixed=0, order=12)
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
	@JBoltField(name="sourcebilltype" ,columnName="SourceBillType",type="String", remark="来源单据类型", required=false, maxLength=30, fixed=0, order=13)
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
	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="来源单号", required=false, maxLength=30, fixed=0, order=14)
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
	@JBoltField(name="sourcebillnorow" ,columnName="SourceBIllNoRow",type="String", remark="来源单号+行号", required=false, maxLength=50, fixed=0, order=15)
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
	@JBoltField(name="sourcebillid" ,columnName="SourceBillID",type="String", remark="来源单据ID", required=false, maxLength=30, fixed=0, order=16)
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
	@JBoltField(name="sourcebilldid" ,columnName="SourceBillDid",type="String", remark="来源单据Did", required=false, maxLength=30, fixed=0, order=17)
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
	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="备注", required=false, maxLength=32, fixed=0, order=18)
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=19)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 创建人id
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人id
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人id", required=false, maxLength=19, fixed=0, order=20)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCCreateName(java.lang.String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=false, maxLength=30, fixed=0, order=21)
	@JSONField(name = "ccreatename")
	public java.lang.String getCCreateName() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDCreateTime(java.util.Date dCreateTime) {
		set("dCreateTime", dCreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=22)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 修改人id
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 修改人id
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="修改人id", required=false, maxLength=19, fixed=0, order=23)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

	/**
	 * 修改人名称
	 */
	public M setCUpdateName(java.lang.String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 修改人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="修改人名称", required=false, maxLength=30, fixed=0, order=24)
	@JSONField(name = "cupdatename")
	public java.lang.String getCUpdateName() {
		return getStr("cUpdateName");
	}

	/**
	 * 修改时间
	 */
	public M setDUpdateTime(java.util.Date dUpdateTime) {
		set("dUpdateTime", dUpdateTime);
		return (M)this;
	}

	/**
	 * 修改时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="修改时间", required=false, maxLength=23, fixed=3, order=25)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 批次号
	 */
	public M setBatch(java.lang.String Batch) {
		set("Batch", Batch);
		return (M)this;
	}

	/**
	 * 批次号
	 */
	@JBoltField(name="batch" ,columnName="Batch",type="String", remark="批次号", required=false, maxLength=255, fixed=0, order=26)
	@JSONField(name = "batch")
	public java.lang.String getBatch() {
		return getStr("Batch");
	}

	/**
	 * 计划数量
	 */
	public M setPlanQty(java.math.BigDecimal PlanQty) {
		set("PlanQty", PlanQty);
		return (M)this;
	}

	/**
	 * 计划数量
	 */
	@JBoltField(name="planqty" ,columnName="PlanQty",type="BigDecimal", remark="计划数量", required=false, maxLength=18, fixed=6, order=27)
	@JSONField(name = "planqty")
	public java.math.BigDecimal getPlanQty() {
		return getBigDecimal("PlanQty");
	}

}

