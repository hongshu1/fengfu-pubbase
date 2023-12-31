package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 料品生产档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseInventoryMfgInfo<M extends BaseInventoryMfgInfo<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**料品档案Id*/
    public static final String IINVENTORYID = "iInventoryId";
    /**生产单价*/
    public static final String IMFGPRICE = "iMfgPrice";
    /**生产提前期*/
    public static final String CPRODADVANCE = "cProdAdvance";
    /**生产日历*/
    public static final String CPRODCALENDARTYPESN = "cProdCalendarTypeSn";
    /**供应日历*/
    public static final String CSUPPLYCALENDARTYPESN = "cSupplyCalendarTypeSn";
    /**质量等级*/
    public static final String IQUALITYLEVEL = "iQualityLevel";
    /**SPM(计划时间)*/
    public static final String CSPM = "cSpm";
    /**采购周期：1. 年 2. 月*/
    public static final String IPURCHASEPERIOD = "iPurchasePeriod";
    /**加工时间(CT/秒)*/
    public static final String CPROCESSTIME = "cProcessTime";
    /**生产部门*/
    public static final String IDEPID = "iDepId";
    /**超量完工类型 (字典维护)*/
    public static final String COVERFINISHSN = "cOverFinishSn";
    /**完工超额量*/
    public static final String IOVERQTY = "iOverQty";
    /**完工超额比例*/
    public static final String IOVERRATE = "iOverRate";
    /**最少生产数量*/
    public static final String IMINPRODQTY = "iMinProdQty";
    /**是否来料质检 1开启*/
    public static final String ISIQC1 = "isIQC1";
    /**是否出货检验 1开启*/
    public static final String ISIQC2 = "isIQC2";
	/**
	 * 主键
	 */
	public M setIAutoId(java.lang.Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 料品档案Id
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 料品档案Id
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="料品档案Id", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 生产单价
	 */
	public M setIMfgPrice(java.math.BigDecimal iMfgPrice) {
		set("iMfgPrice", iMfgPrice);
		return (M)this;
	}

	/**
	 * 生产单价
	 */
	@JBoltField(name="imfgprice" ,columnName="iMfgPrice",type="BigDecimal", remark="生产单价", required=false, maxLength=24, fixed=6, order=3)
	@JSONField(name = "imfgprice")
	public java.math.BigDecimal getIMfgPrice() {
		return getBigDecimal("iMfgPrice");
	}

	/**
	 * 生产提前期
	 */
	public M setCProdAdvance(java.lang.String cProdAdvance) {
		set("cProdAdvance", cProdAdvance);
		return (M)this;
	}

	/**
	 * 生产提前期
	 */
	@JBoltField(name="cprodadvance" ,columnName="cProdAdvance",type="String", remark="生产提前期", required=false, maxLength=255, fixed=0, order=4)
	@JSONField(name = "cprodadvance")
	public java.lang.String getCProdAdvance() {
		return getStr("cProdAdvance");
	}

	/**
	 * 生产日历
	 */
	public M setCProdCalendarTypeSn(java.lang.String cProdCalendarTypeSn) {
		set("cProdCalendarTypeSn", cProdCalendarTypeSn);
		return (M)this;
	}

	/**
	 * 生产日历
	 */
	@JBoltField(name="cprodcalendartypesn" ,columnName="cProdCalendarTypeSn",type="String", remark="生产日历", required=false, maxLength=90, fixed=0, order=5)
	@JSONField(name = "cprodcalendartypesn")
	public java.lang.String getCProdCalendarTypeSn() {
		return getStr("cProdCalendarTypeSn");
	}

	/**
	 * 供应日历
	 */
	public M setCSupplyCalendarTypeSn(java.lang.String cSupplyCalendarTypeSn) {
		set("cSupplyCalendarTypeSn", cSupplyCalendarTypeSn);
		return (M)this;
	}

	/**
	 * 供应日历
	 */
	@JBoltField(name="csupplycalendartypesn" ,columnName="cSupplyCalendarTypeSn",type="String", remark="供应日历", required=false, maxLength=90, fixed=0, order=6)
	@JSONField(name = "csupplycalendartypesn")
	public java.lang.String getCSupplyCalendarTypeSn() {
		return getStr("cSupplyCalendarTypeSn");
	}

	/**
	 * 质量等级
	 */
	public M setIQualityLevel(java.lang.Integer iQualityLevel) {
		set("iQualityLevel", iQualityLevel);
		return (M)this;
	}

	/**
	 * 质量等级
	 */
	@JBoltField(name="iqualitylevel" ,columnName="iQualityLevel",type="Integer", remark="质量等级", required=false, maxLength=10, fixed=0, order=7)
	@JSONField(name = "iqualitylevel")
	public java.lang.Integer getIQualityLevel() {
		return getInt("iQualityLevel");
	}

	/**
	 * SPM(计划时间)
	 */
	public M setCSpm(java.lang.String cSpm) {
		set("cSpm", cSpm);
		return (M)this;
	}

	/**
	 * SPM(计划时间)
	 */
	@JBoltField(name="cspm" ,columnName="cSpm",type="String", remark="SPM(计划时间)", required=false, maxLength=50, fixed=0, order=8)
	@JSONField(name = "cspm")
	public java.lang.String getCSpm() {
		return getStr("cSpm");
	}

	/**
	 * 采购周期：1. 年 2. 月
	 */
	public M setIPurchasePeriod(java.lang.Integer iPurchasePeriod) {
		set("iPurchasePeriod", iPurchasePeriod);
		return (M)this;
	}

	/**
	 * 采购周期：1. 年 2. 月
	 */
	@JBoltField(name="ipurchaseperiod" ,columnName="iPurchasePeriod",type="Integer", remark="采购周期：1. 年 2. 月", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "ipurchaseperiod")
	public java.lang.Integer getIPurchasePeriod() {
		return getInt("iPurchasePeriod");
	}

	/**
	 * 加工时间(CT/秒)
	 */
	public M setCProcessTime(java.lang.String cProcessTime) {
		set("cProcessTime", cProcessTime);
		return (M)this;
	}

	/**
	 * 加工时间(CT/秒)
	 */
	@JBoltField(name="cprocesstime" ,columnName="cProcessTime",type="String", remark="加工时间(CT/秒)", required=false, maxLength=30, fixed=0, order=10)
	@JSONField(name = "cprocesstime")
	public java.lang.String getCProcessTime() {
		return getStr("cProcessTime");
	}

	/**
	 * 生产部门
	 */
	public M setIDepId(java.lang.Long iDepId) {
		set("iDepId", iDepId);
		return (M)this;
	}

	/**
	 * 生产部门
	 */
	@JBoltField(name="idepid" ,columnName="iDepId",type="Long", remark="生产部门", required=false, maxLength=19, fixed=0, order=11)
	@JSONField(name = "idepid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDepId() {
		return getLong("iDepId");
	}

	/**
	 * 超量完工类型 (字典维护)
	 */
	public M setCOverFinishSn(java.lang.String cOverFinishSn) {
		set("cOverFinishSn", cOverFinishSn);
		return (M)this;
	}

	/**
	 * 超量完工类型 (字典维护)
	 */
	@JBoltField(name="coverfinishsn" ,columnName="cOverFinishSn",type="String", remark="超量完工类型 (字典维护)", required=false, maxLength=255, fixed=0, order=12)
	@JSONField(name = "coverfinishsn")
	public java.lang.String getCOverFinishSn() {
		return getStr("cOverFinishSn");
	}

	/**
	 * 完工超额量
	 */
	public M setIOverQty(java.math.BigDecimal iOverQty) {
		set("iOverQty", iOverQty);
		return (M)this;
	}

	/**
	 * 完工超额量
	 */
	@JBoltField(name="ioverqty" ,columnName="iOverQty",type="BigDecimal", remark="完工超额量", required=false, maxLength=24, fixed=6, order=13)
	@JSONField(name = "ioverqty")
	public java.math.BigDecimal getIOverQty() {
		return getBigDecimal("iOverQty");
	}

	/**
	 * 完工超额比例
	 */
	public M setIOverRate(java.math.BigDecimal iOverRate) {
		set("iOverRate", iOverRate);
		return (M)this;
	}

	/**
	 * 完工超额比例
	 */
	@JBoltField(name="ioverrate" ,columnName="iOverRate",type="BigDecimal", remark="完工超额比例", required=false, maxLength=24, fixed=6, order=14)
	@JSONField(name = "ioverrate")
	public java.math.BigDecimal getIOverRate() {
		return getBigDecimal("iOverRate");
	}

	/**
	 * 最少生产数量
	 */
	public M setIMinProdQty(java.lang.Integer iMinProdQty) {
		set("iMinProdQty", iMinProdQty);
		return (M)this;
	}

	/**
	 * 最少生产数量
	 */
	@JBoltField(name="iminprodqty" ,columnName="iMinProdQty",type="Integer", remark="最少生产数量", required=false, maxLength=10, fixed=0, order=15)
	@JSONField(name = "iminprodqty")
	public java.lang.Integer getIMinProdQty() {
		return getInt("iMinProdQty");
	}

	/**
	 * 是否来料质检 1开启
	 */
	public M setIsIQC1(java.lang.Boolean isIQC1) {
		set("isIQC1", isIQC1);
		return (M)this;
	}

	/**
	 * 是否来料质检 1开启
	 */
	@JBoltField(name="isiqc1" ,columnName="isIQC1",type="Boolean", remark="是否来料质检 1开启", required=true, maxLength=1, fixed=0, order=16)
	@JSONField(name = "isiqc1")
	public java.lang.Boolean getIsIQC1() {
		return getBoolean("isIQC1");
	}

	/**
	 * 是否出货检验 1开启
	 */
	public M setIsIQC2(java.lang.Boolean isIQC2) {
		set("isIQC2", isIQC2);
		return (M)this;
	}

	/**
	 * 是否出货检验 1开启
	 */
	@JBoltField(name="isiqc2" ,columnName="isIQC2",type="Boolean", remark="是否出货检验 1开启", required=true, maxLength=1, fixed=0, order=17)
	@JSONField(name = "isiqc2")
	public java.lang.Boolean getIsIQC2() {
		return getBoolean("isIQC2");
	}

}

