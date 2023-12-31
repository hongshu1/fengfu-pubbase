package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户订单-年度计划订单年月金额
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseAnnualorderdQty<M extends BaseAnnualorderdQty<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**年度计划明细ID*/
    public static final String IANNUALORDERDID = "iAnnualOrderDid";
    /**年度*/
    public static final String IYEAR = "iYear";
    /**月份*/
    public static final String IMONTH = "iMonth";
    /**数量*/
    public static final String IQTY = "iQty";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
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
	 * 年度计划明细ID
	 */
	public M setIAnnualOrderDid(java.lang.Long iAnnualOrderDid) {
		set("iAnnualOrderDid", iAnnualOrderDid);
		return (M)this;
	}

	/**
	 * 年度计划明细ID
	 */
	@JBoltField(name="iannualorderdid" ,columnName="iAnnualOrderDid",type="Long", remark="年度计划明细ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iannualorderdid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAnnualOrderDid() {
		return getLong("iAnnualOrderDid");
	}

	/**
	 * 年度
	 */
	public M setIYear(java.lang.Integer iYear) {
		set("iYear", iYear);
		return (M)this;
	}

	/**
	 * 年度
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年度", required=true, maxLength=10, fixed=0, order=3)
	@JSONField(name = "iyear")
	public java.lang.Integer getIYear() {
		return getInt("iYear");
	}

	/**
	 * 月份
	 */
	public M setIMonth(java.lang.Integer iMonth) {
		set("iMonth", iMonth);
		return (M)this;
	}

	/**
	 * 月份
	 */
	@JBoltField(name="imonth" ,columnName="iMonth",type="Integer", remark="月份", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "imonth")
	public java.lang.Integer getIMonth() {
		return getInt("iMonth");
	}

	/**
	 * 数量
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="数量", required=true, maxLength=24, fixed=6, order=5)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=6)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}

