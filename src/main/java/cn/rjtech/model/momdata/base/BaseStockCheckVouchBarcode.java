package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 库存盘点-条码明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseStockCheckVouchBarcode<M extends BaseStockCheckVouchBarcode<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**主表ID*/
    public static final String MASID = "MasID";
    /**条码*/
    public static final String BARCODE = "Barcode";
    /**存货编码*/
    public static final String INVCODE = "InvCode";
    /**账面数量*/
    public static final String QTY = "Qty";
    /**账面件数*/
    public static final String NUM = "Num";
    /**实际数量*/
    public static final String REALQTY = "RealQty";
    /**实际件数*/
    public static final String REALNUM = "RealNum";
    /**调整数量*/
    public static final String ADJUSTQTY = "AdjustQty";
    /**调整件数*/
    public static final String ADJUSTNUM = "AdjustNum";
    /**创建人ID*/
    public static final String ICREATEBY = "icreateby";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建时间*/
    public static final String CREATEDATE = "CreateDate";
    /**是否删除：0. 否 1. 是*/
    public static final String ISDELETED = "isDeleted";
	/**
	 * AutoID
	 */
	public M setAutoID(java.lang.Integer AutoID) {
		set("AutoID", AutoID);
		return (M)this;
	}

	/**
	 * AutoID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="Integer", remark="AutoID", required=true, maxLength=10, fixed=0, order=1)
	@JSONField(name = "autoid")
	public java.lang.Integer getAutoID() {
		return getInt("AutoID");
	}

	/**
	 * 主表ID
	 */
	public M setMasID(java.lang.Integer MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * 主表ID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="Integer", remark="主表ID", required=true, maxLength=10, fixed=0, order=2)
	@JSONField(name = "masid")
	public java.lang.Integer getMasID() {
		return getInt("MasID");
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
	@JBoltField(name="barcode" ,columnName="Barcode",type="String", remark="条码", required=true, maxLength=300, fixed=0, order=3)
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
	@JBoltField(name="invcode" ,columnName="InvCode",type="String", remark="存货编码", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "invcode")
	public java.lang.String getInvCode() {
		return getStr("InvCode");
	}

	/**
	 * 账面数量
	 */
	public M setQty(java.math.BigDecimal Qty) {
		set("Qty", Qty);
		return (M)this;
	}

	/**
	 * 账面数量
	 */
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="账面数量", required=false, maxLength=18, fixed=6, order=5)
	@JSONField(name = "qty")
	public java.math.BigDecimal getQty() {
		return getBigDecimal("Qty");
	}

	/**
	 * 账面件数
	 */
	public M setNum(java.math.BigDecimal Num) {
		set("Num", Num);
		return (M)this;
	}

	/**
	 * 账面件数
	 */
	@JBoltField(name="num" ,columnName="Num",type="BigDecimal", remark="账面件数", required=false, maxLength=18, fixed=6, order=6)
	@JSONField(name = "num")
	public java.math.BigDecimal getNum() {
		return getBigDecimal("Num");
	}

	/**
	 * 实际数量
	 */
	public M setRealQty(java.math.BigDecimal RealQty) {
		set("RealQty", RealQty);
		return (M)this;
	}

	/**
	 * 实际数量
	 */
	@JBoltField(name="realqty" ,columnName="RealQty",type="BigDecimal", remark="实际数量", required=false, maxLength=18, fixed=6, order=7)
	@JSONField(name = "realqty")
	public java.math.BigDecimal getRealQty() {
		return getBigDecimal("RealQty");
	}

	/**
	 * 实际件数
	 */
	public M setRealNum(java.math.BigDecimal RealNum) {
		set("RealNum", RealNum);
		return (M)this;
	}

	/**
	 * 实际件数
	 */
	@JBoltField(name="realnum" ,columnName="RealNum",type="BigDecimal", remark="实际件数", required=false, maxLength=18, fixed=6, order=8)
	@JSONField(name = "realnum")
	public java.math.BigDecimal getRealNum() {
		return getBigDecimal("RealNum");
	}

	/**
	 * 调整数量
	 */
	public M setAdjustQty(java.math.BigDecimal AdjustQty) {
		set("AdjustQty", AdjustQty);
		return (M)this;
	}

	/**
	 * 调整数量
	 */
	@JBoltField(name="adjustqty" ,columnName="AdjustQty",type="BigDecimal", remark="调整数量", required=false, maxLength=18, fixed=6, order=9)
	@JSONField(name = "adjustqty")
	public java.math.BigDecimal getAdjustQty() {
		return getBigDecimal("AdjustQty");
	}

	/**
	 * 调整件数
	 */
	public M setAdjustNum(java.math.BigDecimal AdjustNum) {
		set("AdjustNum", AdjustNum);
		return (M)this;
	}

	/**
	 * 调整件数
	 */
	@JBoltField(name="adjustnum" ,columnName="AdjustNum",type="BigDecimal", remark="调整件数", required=false, maxLength=18, fixed=6, order=10)
	@JSONField(name = "adjustnum")
	public java.math.BigDecimal getAdjustNum() {
		return getBigDecimal("AdjustNum");
	}

	/**
	 * 创建人ID
	 */
	public M setIcreateby(java.lang.String icreateby) {
		set("icreateby", icreateby);
		return (M)this;
	}

	/**
	 * 创建人ID
	 */
	@JBoltField(name="icreateby" ,columnName="icreateby",type="String", remark="创建人ID", required=false, maxLength=30, fixed=0, order=11)
	@JSONField(name = "icreateby")
	public java.lang.String getIcreateby() {
		return getStr("icreateby");
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
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=12)
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
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=13)
	@JSONField(name = "createdate")
	public java.util.Date getCreateDate() {
		return getDate("CreateDate");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=14)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}

