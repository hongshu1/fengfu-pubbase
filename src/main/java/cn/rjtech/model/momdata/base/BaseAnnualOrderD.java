package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 年度计划-年度计划订单年汇总
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseAnnualOrderD<M extends BaseAnnualOrderD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**年度计划订单ID*/
    public static final String IANNUALORDERMID = "iAnnualOrderMid";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**年度1*/
    public static final String IYEAR1 = "iYear1";
    /**年度1合计*/
    public static final String IYEAR1SUM = "iYear1Sum";
    /**年度2*/
    public static final String IYEAR2 = "iYear2";
    /**年度2合计*/
    public static final String IYEAR2SUM = "iYear2Sum";
    /**年度3*/
    public static final String IYEAR3 = "iYear3";
    /**年度3合计*/
    public static final String IYEAR3SUM = "iYear3Sum";
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
	 * 年度计划订单ID
	 */
	public M setIAnnualOrderMid(java.lang.Long iAnnualOrderMid) {
		set("iAnnualOrderMid", iAnnualOrderMid);
		return (M)this;
	}

	/**
	 * 年度计划订单ID
	 */
	@JBoltField(name="iannualordermid" ,columnName="iAnnualOrderMid",type="Long", remark="年度计划订单ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iannualordermid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAnnualOrderMid() {
		return getLong("iAnnualOrderMid");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 年度1
	 */
	public M setIYear1(java.lang.Integer iYear1) {
		set("iYear1", iYear1);
		return (M)this;
	}

	/**
	 * 年度1
	 */
	@JBoltField(name="iyear1" ,columnName="iYear1",type="Integer", remark="年度1", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "iyear1")
	public java.lang.Integer getIYear1() {
		return getInt("iYear1");
	}

	/**
	 * 年度1合计
	 */
	public M setIYear1Sum(java.math.BigDecimal iYear1Sum) {
		set("iYear1Sum", iYear1Sum);
		return (M)this;
	}

	/**
	 * 年度1合计
	 */
	@JBoltField(name="iyear1sum" ,columnName="iYear1Sum",type="BigDecimal", remark="年度1合计", required=true, maxLength=18, fixed=2, order=5)
	@JSONField(name = "iyear1sum")
	public java.math.BigDecimal getIYear1Sum() {
		return getBigDecimal("iYear1Sum");
	}

	/**
	 * 年度2
	 */
	public M setIYear2(java.lang.Integer iYear2) {
		set("iYear2", iYear2);
		return (M)this;
	}

	/**
	 * 年度2
	 */
	@JBoltField(name="iyear2" ,columnName="iYear2",type="Integer", remark="年度2", required=false, maxLength=10, fixed=0, order=6)
	@JSONField(name = "iyear2")
	public java.lang.Integer getIYear2() {
		return getInt("iYear2");
	}

	/**
	 * 年度2合计
	 */
	public M setIYear2Sum(java.math.BigDecimal iYear2Sum) {
		set("iYear2Sum", iYear2Sum);
		return (M)this;
	}

	/**
	 * 年度2合计
	 */
	@JBoltField(name="iyear2sum" ,columnName="iYear2Sum",type="BigDecimal", remark="年度2合计", required=false, maxLength=18, fixed=2, order=7)
	@JSONField(name = "iyear2sum")
	public java.math.BigDecimal getIYear2Sum() {
		return getBigDecimal("iYear2Sum");
	}

	/**
	 * 年度3
	 */
	public M setIYear3(java.lang.Integer iYear3) {
		set("iYear3", iYear3);
		return (M)this;
	}

	/**
	 * 年度3
	 */
	@JBoltField(name="iyear3" ,columnName="iYear3",type="Integer", remark="年度3", required=false, maxLength=10, fixed=0, order=8)
	@JSONField(name = "iyear3")
	public java.lang.Integer getIYear3() {
		return getInt("iYear3");
	}

	/**
	 * 年度3合计
	 */
	public M setIYear3Sum(java.math.BigDecimal iYear3Sum) {
		set("iYear3Sum", iYear3Sum);
		return (M)this;
	}

	/**
	 * 年度3合计
	 */
	@JBoltField(name="iyear3sum" ,columnName="iYear3Sum",type="BigDecimal", remark="年度3合计", required=false, maxLength=18, fixed=2, order=9)
	@JSONField(name = "iyear3sum")
	public java.math.BigDecimal getIYear3Sum() {
		return getBigDecimal("iYear3Sum");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=10)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}

