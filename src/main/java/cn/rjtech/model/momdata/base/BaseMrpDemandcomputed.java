package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 需求计划管理-物料需求计划计算明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMrpDemandcomputed<M extends BaseMrpDemandcomputed<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**物料需求计划计算主表ID*/
    public static final String IDEMANDCOMPUTEMID = "iDemandComputeMid";
    /**供应商ID*/
    public static final String IVENDORID = "iVendorId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**年*/
    public static final String IYEAR = "iYear";
    /**月*/
    public static final String IMONTH = "iMonth";
    /**日*/
    public static final String IDATE = "iDate";
    /**实绩需求数*/
    public static final String IQTY1 = "iQty1";
    /**到货计划数*/
    public static final String IQTY2 = "iQty2";
    /**到货实绩*/
    public static final String IQTY3 = "iQty3";
    /**差异数量*/
    public static final String IQTY4 = "iQty4";
    /**计划在库*/
    public static final String IQTY5 = "iQty5";
    /**是否锁定;0. 否 1. 是*/
    public static final String ISLOCKED = "isLocked";
    /**包装数量*/
    public static final String IPKGQTY = "iPkgQty";
	/**
	 * 主键ID
	 */
	public M setIAutoId(java.lang.Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 物料需求计划计算主表ID
	 */
	public M setIDemandComputeMid(java.lang.Long iDemandComputeMid) {
		set("iDemandComputeMid", iDemandComputeMid);
		return (M)this;
	}

	/**
	 * 物料需求计划计算主表ID
	 */
	@JBoltField(name="idemandcomputemid" ,columnName="iDemandComputeMid",type="Long", remark="物料需求计划计算主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "idemandcomputemid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDemandComputeMid() {
		return getLong("iDemandComputeMid");
	}

	/**
	 * 供应商ID
	 */
	public M setIVendorId(java.lang.Long iVendorId) {
		set("iVendorId", iVendorId);
		return (M)this;
	}

	/**
	 * 供应商ID
	 */
	@JBoltField(name="ivendorid" ,columnName="iVendorId",type="Long", remark="供应商ID", required=false, maxLength=19, fixed=0, order=3)
	@JSONField(name = "ivendorid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIVendorId() {
		return getLong("iVendorId");
	}

	/**
	 * 存货ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 存货ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 年
	 */
	public M setIYear(java.lang.Integer iYear) {
		set("iYear", iYear);
		return (M)this;
	}

	/**
	 * 年
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "iyear")
	public java.lang.Integer getIYear() {
		return getInt("iYear");
	}

	/**
	 * 月
	 */
	public M setIMonth(java.lang.Integer iMonth) {
		set("iMonth", iMonth);
		return (M)this;
	}

	/**
	 * 月
	 */
	@JBoltField(name="imonth" ,columnName="iMonth",type="Integer", remark="月", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "imonth")
	public java.lang.Integer getIMonth() {
		return getInt("iMonth");
	}

	/**
	 * 日
	 */
	public M setIDate(java.lang.Integer iDate) {
		set("iDate", iDate);
		return (M)this;
	}

	/**
	 * 日
	 */
	@JBoltField(name="idate" ,columnName="iDate",type="Integer", remark="日", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "idate")
	public java.lang.Integer getIDate() {
		return getInt("iDate");
	}

	/**
	 * 实绩需求数
	 */
	public M setIQty1(java.math.BigDecimal iQty1) {
		set("iQty1", iQty1);
		return (M)this;
	}

	/**
	 * 实绩需求数
	 */
	@JBoltField(name="iqty1" ,columnName="iQty1",type="BigDecimal", remark="实绩需求数", required=false, maxLength=24, fixed=6, order=8)
	@JSONField(name = "iqty1")
	public java.math.BigDecimal getIQty1() {
		return getBigDecimal("iQty1");
	}

	/**
	 * 到货计划数
	 */
	public M setIQty2(java.math.BigDecimal iQty2) {
		set("iQty2", iQty2);
		return (M)this;
	}

	/**
	 * 到货计划数
	 */
	@JBoltField(name="iqty2" ,columnName="iQty2",type="BigDecimal", remark="到货计划数", required=false, maxLength=24, fixed=6, order=9)
	@JSONField(name = "iqty2")
	public java.math.BigDecimal getIQty2() {
		return getBigDecimal("iQty2");
	}

	/**
	 * 到货实绩
	 */
	public M setIQty3(java.math.BigDecimal iQty3) {
		set("iQty3", iQty3);
		return (M)this;
	}

	/**
	 * 到货实绩
	 */
	@JBoltField(name="iqty3" ,columnName="iQty3",type="BigDecimal", remark="到货实绩", required=false, maxLength=24, fixed=6, order=10)
	@JSONField(name = "iqty3")
	public java.math.BigDecimal getIQty3() {
		return getBigDecimal("iQty3");
	}

	/**
	 * 差异数量
	 */
	public M setIQty4(java.math.BigDecimal iQty4) {
		set("iQty4", iQty4);
		return (M)this;
	}

	/**
	 * 差异数量
	 */
	@JBoltField(name="iqty4" ,columnName="iQty4",type="BigDecimal", remark="差异数量", required=false, maxLength=24, fixed=6, order=11)
	@JSONField(name = "iqty4")
	public java.math.BigDecimal getIQty4() {
		return getBigDecimal("iQty4");
	}

	/**
	 * 计划在库
	 */
	public M setIQty5(java.math.BigDecimal iQty5) {
		set("iQty5", iQty5);
		return (M)this;
	}

	/**
	 * 计划在库
	 */
	@JBoltField(name="iqty5" ,columnName="iQty5",type="BigDecimal", remark="计划在库", required=false, maxLength=24, fixed=6, order=12)
	@JSONField(name = "iqty5")
	public java.math.BigDecimal getIQty5() {
		return getBigDecimal("iQty5");
	}

	/**
	 * 是否锁定;0. 否 1. 是
	 */
	public M setIsLocked(java.lang.Boolean isLocked) {
		set("isLocked", isLocked);
		return (M)this;
	}

	/**
	 * 是否锁定;0. 否 1. 是
	 */
	@JBoltField(name="islocked" ,columnName="isLocked",type="Boolean", remark="是否锁定;0. 否 1. 是", required=true, maxLength=1, fixed=0, order=13)
	@JSONField(name = "islocked")
	public java.lang.Boolean getIsLocked() {
		return getBoolean("isLocked");
	}

	/**
	 * 包装数量
	 */
	public M setIPkgQty(java.lang.Integer iPkgQty) {
		set("iPkgQty", iPkgQty);
		return (M)this;
	}

	/**
	 * 包装数量
	 */
	@JBoltField(name="ipkgqty" ,columnName="iPkgQty",type="Integer", remark="包装数量", required=true, maxLength=10, fixed=0, order=14)
	@JSONField(name = "ipkgqty")
	public java.lang.Integer getIPkgQty() {
		return getInt("iPkgQty");
	}

}

