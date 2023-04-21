package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 盘点单明细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseStockcheckvouchdetail<M extends BaseStockcheckvouchdetail<M>> extends JBoltBaseModel<M>{
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**主表ID;T_Sys_StockCheckVouch.AutoID*/
    public static final String MASID = "MasID";
    /**来源ID*/
    public static final String SOURCEID = "SourceID";
    /**库位*/
    public static final String POSCODE = "PosCode";
    /**存货编码*/
    public static final String INVCODE = "InvCode";
    /**账面数量*/
    public static final String QTY = "Qty";
    /**账面件数*/
    public static final String NUM = "Num";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建时间*/
    public static final String CREATEDATE = "CreateDate";
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
	 * 主表ID;T_Sys_StockCheckVouch.AutoID
	 */
	public M setMasID(java.lang.String MasID) {
		set("MasID", MasID);
		return (M)this;
	}

	/**
	 * 主表ID;T_Sys_StockCheckVouch.AutoID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="主表ID;T_Sys_StockCheckVouch.AutoID", required=true, maxLength=30, fixed=0, order=2)
	@JSONField(name = "masid")
	public java.lang.String getMasID() {
		return getStr("MasID");
	}

	/**
	 * 来源ID
	 */
	public M setSourceID(java.lang.String SourceID) {
		set("SourceID", SourceID);
		return (M)this;
	}

	/**
	 * 来源ID
	 */
	@JBoltField(name="sourceid" ,columnName="SourceID",type="String", remark="来源ID", required=false, maxLength=50, fixed=0, order=3)
	@JSONField(name = "sourceid")
	public java.lang.String getSourceID() {
		return getStr("SourceID");
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
	@JBoltField(name="poscode" ,columnName="PosCode",type="String", remark="库位", required=false, maxLength=50, fixed=0, order=4)
	@JSONField(name = "poscode")
	public java.lang.String getPosCode() {
		return getStr("PosCode");
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
	 * 账面数量
	 */
	public M setQty(java.math.BigDecimal Qty) {
		set("Qty", Qty);
		return (M)this;
	}

	/**
	 * 账面数量
	 */
	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="账面数量", required=true, maxLength=18, fixed=6, order=6)
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
	@JBoltField(name="num" ,columnName="Num",type="BigDecimal", remark="账面件数", required=true, maxLength=18, fixed=6, order=7)
	@JSONField(name = "num")
	public java.math.BigDecimal getNum() {
		return getBigDecimal("Num");
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
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=8)
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
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=9)
	@JSONField(name = "createdate")
	public java.util.Date getCreateDate() {
		return getDate("CreateDate");
	}

}

