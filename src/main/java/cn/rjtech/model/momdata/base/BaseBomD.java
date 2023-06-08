package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 物料建模-BOM明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseBomD<M extends BaseBomD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**BOM主表ID*/
    public static final String IBOMMID = "iBomMid";
    /**子件BOM表ID*/
    public static final String IINVPARTBOMMID = "iInvPartBomMid";
    /**版本号*/
    public static final String CVERSION = "cVersion";
    /**子件存货编码*/
    public static final String CINVCODE = "cInvCode";
    /**子件存货名称*/
    public static final String CINVNAME = "cInvName";
    /**子件规格*/
    public static final String CINVSTD = "cInvStd";
    /**库存计量单位Id*/
    public static final String IINVENTORYUOMID1 = "iInventoryUomId1";
    /**启用日期*/
    public static final String DENABLEDATE = "dEnableDate";
    /**停用日期*/
    public static final String DDISABLEDATE = "dDisableDate";
    /**材料类别*/
    public static final String IPARTTYPE = "iPartType";
    /**基本用量*/
    public static final String IBASEQTY = "iBaseQty";
    /**重量*/
    public static final String IWEIGHT = "iWeight";
    /**供应商ID*/
    public static final String IVENDORID = "iVendorId";
    /**供应商编码*/
    public static final String CVENDCODE = "cVendCode";
    /**供应商名称*/
    public static final String CVENNAME = "cVenName";
    /**是否虚拟件;0.否 1. 是*/
    public static final String ISVIRTUAL = "isVirtual";
    /**是否委外;是否外作，0. 否 1. 是*/
    public static final String BPROXYFOREIGN = "bProxyForeign";
    /**备注*/
    public static final String CMEMO = "cMemo";
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
	 * BOM主表ID
	 */
	public M setIBomMid(java.lang.Long iBomMid) {
		set("iBomMid", iBomMid);
		return (M)this;
	}

	/**
	 * BOM主表ID
	 */
	@JBoltField(name="ibommid" ,columnName="iBomMid",type="Long", remark="BOM主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "ibommid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIBomMid() {
		return getLong("iBomMid");
	}

	/**
	 * 子件BOM表ID
	 */
	public M setIInvPartBomMid(java.lang.Long iInvPartBomMid) {
		set("iInvPartBomMid", iInvPartBomMid);
		return (M)this;
	}

	/**
	 * 子件BOM表ID
	 */
	@JBoltField(name="iinvpartbommid" ,columnName="iInvPartBomMid",type="Long", remark="子件BOM表ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinvpartbommid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInvPartBomMid() {
		return getLong("iInvPartBomMid");
	}

	/**
	 * 版本号
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 版本号
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="版本号", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
	}

	/**
	 * 子件存货编码
	 */
	public M setCInvCode(java.lang.String cInvCode) {
		set("cInvCode", cInvCode);
		return (M)this;
	}

	/**
	 * 子件存货编码
	 */
	@JBoltField(name="cinvcode" ,columnName="cInvCode",type="String", remark="子件存货编码", required=true, maxLength=255, fixed=0, order=5)
	@JSONField(name = "cinvcode")
	public java.lang.String getCInvCode() {
		return getStr("cInvCode");
	}

	/**
	 * 子件存货名称
	 */
	public M setCInvName(java.lang.String cInvName) {
		set("cInvName", cInvName);
		return (M)this;
	}

	/**
	 * 子件存货名称
	 */
	@JBoltField(name="cinvname" ,columnName="cInvName",type="String", remark="子件存货名称", required=true, maxLength=200, fixed=0, order=6)
	@JSONField(name = "cinvname")
	public java.lang.String getCInvName() {
		return getStr("cInvName");
	}

	/**
	 * 子件规格
	 */
	public M setCInvStd(java.lang.String cInvStd) {
		set("cInvStd", cInvStd);
		return (M)this;
	}

	/**
	 * 子件规格
	 */
	@JBoltField(name="cinvstd" ,columnName="cInvStd",type="String", remark="子件规格", required=true, maxLength=200, fixed=0, order=7)
	@JSONField(name = "cinvstd")
	public java.lang.String getCInvStd() {
		return getStr("cInvStd");
	}

	/**
	 * 库存计量单位Id
	 */
	public M setIInventoryUomId1(java.lang.Long iInventoryUomId1) {
		set("iInventoryUomId1", iInventoryUomId1);
		return (M)this;
	}

	/**
	 * 库存计量单位Id
	 */
	@JBoltField(name="iinventoryuomid1" ,columnName="iInventoryUomId1",type="Long", remark="库存计量单位Id", required=true, maxLength=19, fixed=0, order=8)
	@JSONField(name = "iinventoryuomid1", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryUomId1() {
		return getLong("iInventoryUomId1");
	}

	/**
	 * 启用日期
	 */
	public M setDEnableDate(java.util.Date dEnableDate) {
		set("dEnableDate", dEnableDate);
		return (M)this;
	}

	/**
	 * 启用日期
	 */
	@JBoltField(name="denabledate" ,columnName="dEnableDate",type="Date", remark="启用日期", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "denabledate")
	public java.util.Date getDEnableDate() {
		return getDate("dEnableDate");
	}

	/**
	 * 停用日期
	 */
	public M setDDisableDate(java.util.Date dDisableDate) {
		set("dDisableDate", dDisableDate);
		return (M)this;
	}

	/**
	 * 停用日期
	 */
	@JBoltField(name="ddisabledate" ,columnName="dDisableDate",type="Date", remark="停用日期", required=true, maxLength=10, fixed=0, order=10)
	@JSONField(name = "ddisabledate")
	public java.util.Date getDDisableDate() {
		return getDate("dDisableDate");
	}

	/**
	 * 材料类别
	 */
	public M setIPartType(java.lang.Integer iPartType) {
		set("iPartType", iPartType);
		return (M)this;
	}

	/**
	 * 材料类别
	 */
	@JBoltField(name="iparttype" ,columnName="iPartType",type="Integer", remark="材料类别", required=true, maxLength=10, fixed=0, order=11)
	@JSONField(name = "iparttype")
	public java.lang.Integer getIPartType() {
		return getInt("iPartType");
	}

	/**
	 * 基本用量
	 */
	public M setIBaseQty(java.math.BigDecimal iBaseQty) {
		set("iBaseQty", iBaseQty);
		return (M)this;
	}

	/**
	 * 基本用量
	 */
	@JBoltField(name="ibaseqty" ,columnName="iBaseQty",type="BigDecimal", remark="基本用量", required=true, maxLength=24, fixed=6, order=12)
	@JSONField(name = "ibaseqty")
	public java.math.BigDecimal getIBaseQty() {
		return getBigDecimal("iBaseQty");
	}

	/**
	 * 重量
	 */
	public M setIWeight(java.math.BigDecimal iWeight) {
		set("iWeight", iWeight);
		return (M)this;
	}

	/**
	 * 重量
	 */
	@JBoltField(name="iweight" ,columnName="iWeight",type="BigDecimal", remark="重量", required=false, maxLength=24, fixed=6, order=13)
	@JSONField(name = "iweight")
	public java.math.BigDecimal getIWeight() {
		return getBigDecimal("iWeight");
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
	@JBoltField(name="ivendorid" ,columnName="iVendorId",type="Long", remark="供应商ID", required=false, maxLength=19, fixed=0, order=14)
	@JSONField(name = "ivendorid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIVendorId() {
		return getLong("iVendorId");
	}

	/**
	 * 供应商编码
	 */
	public M setCVendCode(java.lang.String cVendCode) {
		set("cVendCode", cVendCode);
		return (M)this;
	}

	/**
	 * 供应商编码
	 */
	@JBoltField(name="cvendcode" ,columnName="cVendCode",type="String", remark="供应商编码", required=false, maxLength=200, fixed=0, order=15)
	@JSONField(name = "cvendcode")
	public java.lang.String getCVendCode() {
		return getStr("cVendCode");
	}

	/**
	 * 供应商名称
	 */
	public M setCVenName(java.lang.String cVenName) {
		set("cVenName", cVenName);
		return (M)this;
	}

	/**
	 * 供应商名称
	 */
	@JBoltField(name="cvenname" ,columnName="cVenName",type="String", remark="供应商名称", required=false, maxLength=200, fixed=0, order=16)
	@JSONField(name = "cvenname")
	public java.lang.String getCVenName() {
		return getStr("cVenName");
	}

	/**
	 * 是否虚拟件;0.否 1. 是
	 */
	public M setIsVirtual(java.lang.Boolean isVirtual) {
		set("isVirtual", isVirtual);
		return (M)this;
	}

	/**
	 * 是否虚拟件;0.否 1. 是
	 */
	@JBoltField(name="isvirtual" ,columnName="isVirtual",type="Boolean", remark="是否虚拟件;0.否 1. 是", required=false, maxLength=1, fixed=0, order=17)
	@JSONField(name = "isvirtual")
	public java.lang.Boolean getIsVirtual() {
		return getBoolean("isVirtual");
	}

	/**
	 * 是否委外;是否外作，0. 否 1. 是
	 */
	public M setBProxyForeign(java.lang.Boolean bProxyForeign) {
		set("bProxyForeign", bProxyForeign);
		return (M)this;
	}

	/**
	 * 是否委外;是否外作，0. 否 1. 是
	 */
	@JBoltField(name="bproxyforeign" ,columnName="bProxyForeign",type="Boolean", remark="是否委外;是否外作，0. 否 1. 是", required=false, maxLength=1, fixed=0, order=18)
	@JSONField(name = "bproxyforeign")
	public java.lang.Boolean getBProxyForeign() {
		return getBoolean("bProxyForeign");
	}

	/**
	 * 备注
	 */
	public M setCMemo(java.lang.String cMemo) {
		set("cMemo", cMemo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=19)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

}

