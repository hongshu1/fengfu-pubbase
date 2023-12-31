package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 往来单位-客户行事日历
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseCustomerWorkDays<M extends BaseCustomerWorkDays<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**客户ID*/
    public static final String ICUSTOMERID = "iCustomerId";
    /**年*/
    public static final String IYEAR = "iYear";
    /**1月工作天数*/
    public static final String IMONTH1DAYS = "iMonth1Days";
    /**2月工作天数*/
    public static final String IMONTH2DAYS = "iMonth2Days";
    /**3月工作天数*/
    public static final String IMONTH3DAYS = "iMonth3Days";
    /**4月工作天数*/
    public static final String IMONTH4DAYS = "iMonth4Days";
    /**5月工作天数*/
    public static final String IMONTH5DAYS = "iMonth5Days";
    /**6月工作天数*/
    public static final String IMONTH6DAYS = "iMonth6Days";
    /**7月工作天数*/
    public static final String IMONTH7DAYS = "iMonth7Days";
    /**8月工作天数*/
    public static final String IMONTH8DAYS = "iMonth8Days";
    /**9月工作天数*/
    public static final String IMONTH9DAYS = "iMonth9Days";
    /**10月工作天数*/
    public static final String IMONTH10DAYS = "iMonth10Days";
    /**11月工作天数*/
    public static final String IMONTH11DAYS = "iMonth11Days";
    /**12月工作天数*/
    public static final String IMONTH12DAYS = "iMonth12Days";
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
	 * 客户ID
	 */
	public M setICustomerId(java.lang.Long iCustomerId) {
		set("iCustomerId", iCustomerId);
		return (M)this;
	}

	/**
	 * 客户ID
	 */
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
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
	 * 1月工作天数
	 */
	public M setIMonth1Days(java.lang.Integer iMonth1Days) {
		set("iMonth1Days", iMonth1Days);
		return (M)this;
	}

	/**
	 * 1月工作天数
	 */
	@JBoltField(name="imonth1days" ,columnName="iMonth1Days",type="Integer", remark="1月工作天数", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "imonth1days")
	public java.lang.Integer getIMonth1Days() {
		return getInt("iMonth1Days");
	}

	/**
	 * 2月工作天数
	 */
	public M setIMonth2Days(java.lang.Integer iMonth2Days) {
		set("iMonth2Days", iMonth2Days);
		return (M)this;
	}

	/**
	 * 2月工作天数
	 */
	@JBoltField(name="imonth2days" ,columnName="iMonth2Days",type="Integer", remark="2月工作天数", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "imonth2days")
	public java.lang.Integer getIMonth2Days() {
		return getInt("iMonth2Days");
	}

	/**
	 * 3月工作天数
	 */
	public M setIMonth3Days(java.lang.Integer iMonth3Days) {
		set("iMonth3Days", iMonth3Days);
		return (M)this;
	}

	/**
	 * 3月工作天数
	 */
	@JBoltField(name="imonth3days" ,columnName="iMonth3Days",type="Integer", remark="3月工作天数", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "imonth3days")
	public java.lang.Integer getIMonth3Days() {
		return getInt("iMonth3Days");
	}

	/**
	 * 4月工作天数
	 */
	public M setIMonth4Days(java.lang.Integer iMonth4Days) {
		set("iMonth4Days", iMonth4Days);
		return (M)this;
	}

	/**
	 * 4月工作天数
	 */
	@JBoltField(name="imonth4days" ,columnName="iMonth4Days",type="Integer", remark="4月工作天数", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "imonth4days")
	public java.lang.Integer getIMonth4Days() {
		return getInt("iMonth4Days");
	}

	/**
	 * 5月工作天数
	 */
	public M setIMonth5Days(java.lang.Integer iMonth5Days) {
		set("iMonth5Days", iMonth5Days);
		return (M)this;
	}

	/**
	 * 5月工作天数
	 */
	@JBoltField(name="imonth5days" ,columnName="iMonth5Days",type="Integer", remark="5月工作天数", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "imonth5days")
	public java.lang.Integer getIMonth5Days() {
		return getInt("iMonth5Days");
	}

	/**
	 * 6月工作天数
	 */
	public M setIMonth6Days(java.lang.Integer iMonth6Days) {
		set("iMonth6Days", iMonth6Days);
		return (M)this;
	}

	/**
	 * 6月工作天数
	 */
	@JBoltField(name="imonth6days" ,columnName="iMonth6Days",type="Integer", remark="6月工作天数", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "imonth6days")
	public java.lang.Integer getIMonth6Days() {
		return getInt("iMonth6Days");
	}

	/**
	 * 7月工作天数
	 */
	public M setIMonth7Days(java.lang.Integer iMonth7Days) {
		set("iMonth7Days", iMonth7Days);
		return (M)this;
	}

	/**
	 * 7月工作天数
	 */
	@JBoltField(name="imonth7days" ,columnName="iMonth7Days",type="Integer", remark="7月工作天数", required=true, maxLength=10, fixed=0, order=10)
	@JSONField(name = "imonth7days")
	public java.lang.Integer getIMonth7Days() {
		return getInt("iMonth7Days");
	}

	/**
	 * 8月工作天数
	 */
	public M setIMonth8Days(java.lang.Integer iMonth8Days) {
		set("iMonth8Days", iMonth8Days);
		return (M)this;
	}

	/**
	 * 8月工作天数
	 */
	@JBoltField(name="imonth8days" ,columnName="iMonth8Days",type="Integer", remark="8月工作天数", required=true, maxLength=10, fixed=0, order=11)
	@JSONField(name = "imonth8days")
	public java.lang.Integer getIMonth8Days() {
		return getInt("iMonth8Days");
	}

	/**
	 * 9月工作天数
	 */
	public M setIMonth9Days(java.lang.Integer iMonth9Days) {
		set("iMonth9Days", iMonth9Days);
		return (M)this;
	}

	/**
	 * 9月工作天数
	 */
	@JBoltField(name="imonth9days" ,columnName="iMonth9Days",type="Integer", remark="9月工作天数", required=true, maxLength=10, fixed=0, order=12)
	@JSONField(name = "imonth9days")
	public java.lang.Integer getIMonth9Days() {
		return getInt("iMonth9Days");
	}

	/**
	 * 10月工作天数
	 */
	public M setIMonth10Days(java.lang.Integer iMonth10Days) {
		set("iMonth10Days", iMonth10Days);
		return (M)this;
	}

	/**
	 * 10月工作天数
	 */
	@JBoltField(name="imonth10days" ,columnName="iMonth10Days",type="Integer", remark="10月工作天数", required=true, maxLength=10, fixed=0, order=13)
	@JSONField(name = "imonth10days")
	public java.lang.Integer getIMonth10Days() {
		return getInt("iMonth10Days");
	}

	/**
	 * 11月工作天数
	 */
	public M setIMonth11Days(java.lang.Integer iMonth11Days) {
		set("iMonth11Days", iMonth11Days);
		return (M)this;
	}

	/**
	 * 11月工作天数
	 */
	@JBoltField(name="imonth11days" ,columnName="iMonth11Days",type="Integer", remark="11月工作天数", required=true, maxLength=10, fixed=0, order=14)
	@JSONField(name = "imonth11days")
	public java.lang.Integer getIMonth11Days() {
		return getInt("iMonth11Days");
	}

	/**
	 * 12月工作天数
	 */
	public M setIMonth12Days(java.lang.Integer iMonth12Days) {
		set("iMonth12Days", iMonth12Days);
		return (M)this;
	}

	/**
	 * 12月工作天数
	 */
	@JBoltField(name="imonth12days" ,columnName="iMonth12Days",type="Integer", remark="12月工作天数", required=true, maxLength=10, fixed=0, order=15)
	@JSONField(name = "imonth12days")
	public java.lang.Integer getIMonth12Days() {
		return getInt("iMonth12Days");
	}

}

