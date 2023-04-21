package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 需求计划管理-到货计划细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseDemandPlanD<M extends BaseDemandPlanD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**到货计划主表ID*/
    public static final String IDEMANDPLANMID = "iDemandPlanMid";
    /**年*/
    public static final String IYEAR = "iYear";
    /**月*/
    public static final String IMONTH = "iMonth";
    /**日*/
    public static final String IDATE = "iDate";
    /**到货计划数*/
    public static final String IQTY = "iQty";
    /**状态：1. 未完成 2. 已完成*/
    public static final String ISTATUS = "iStatus";
    /**生成类型：0. 未生成 1. 采购订单 2. 销售订单*/
    public static final String IGENTYPE = "iGenType";
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
	 * 到货计划主表ID
	 */
	public M setIDemandPlanMid(java.lang.Long iDemandPlanMid) {
		set("iDemandPlanMid", iDemandPlanMid);
		return (M)this;
	}

	/**
	 * 到货计划主表ID
	 */
	@JBoltField(name="idemandplanmid" ,columnName="iDemandPlanMid",type="Long", remark="到货计划主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "idemandplanmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDemandPlanMid() {
		return getLong("iDemandPlanMid");
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
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年", required=true, maxLength=10, fixed=0, order=3)
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
	@JBoltField(name="imonth" ,columnName="iMonth",type="Integer", remark="月", required=true, maxLength=10, fixed=0, order=4)
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
	@JBoltField(name="idate" ,columnName="iDate",type="Integer", remark="日", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "idate")
	public java.lang.Integer getIDate() {
		return getInt("iDate");
	}

	/**
	 * 到货计划数
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 到货计划数
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="到货计划数", required=true, maxLength=18, fixed=2, order=6)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 状态：1. 未完成 2. 已完成
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 状态：1. 未完成 2. 已完成
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="状态：1. 未完成 2. 已完成", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
	}

	/**
	 * 生成类型：0. 未生成 1. 采购订单 2. 销售订单
	 */
	public M setIGenType(java.lang.Integer iGenType) {
		set("iGenType", iGenType);
		return (M)this;
	}

	/**
	 * 生成类型：0. 未生成 1. 采购订单 2. 销售订单
	 */
	@JBoltField(name="igentype" ,columnName="iGenType",type="Integer", remark="生成类型：0. 未生成 1. 采购订单 2. 销售订单", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "igentype")
	public java.lang.Integer getIGenType() {
		return getInt("iGenType");
	}

}

