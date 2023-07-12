package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 采购/委外订单-采购订单明细数量
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSubcontractorderdQty<M extends BaseSubcontractorderdQty<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**委外订单明细ID*/
    public static final String ISUBCONTRACTORDERDID = "iSubcontractOrderDid";
    /**年份*/
    public static final String IYEAR = "iYear";
    /**月*/
    public static final String IMONTH = "iMonth";
    /**日*/
    public static final String IDATE = "iDate";
    /**数量*/
    public static final String IQTY = "iQty";
    /**序号*/
    public static final String ISEQ = "iSeq";
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
	 * 委外订单明细ID
	 */
	public M setISubcontractOrderDid(java.lang.Long iSubcontractOrderDid) {
		set("iSubcontractOrderDid", iSubcontractOrderDid);
		return (M)this;
	}

	/**
	 * 委外订单明细ID
	 */
	@JBoltField(name="isubcontractorderdid" ,columnName="iSubcontractOrderDid",type="Long", remark="委外订单明细ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "isubcontractorderdid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISubcontractOrderDid() {
		return getLong("iSubcontractOrderDid");
	}

	/**
	 * 年份
	 */
	public M setIYear(java.lang.Integer iYear) {
		set("iYear", iYear);
		return (M)this;
	}

	/**
	 * 年份
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年份", required=true, maxLength=10, fixed=0, order=3)
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
	 * 数量
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="数量", required=false, maxLength=18, fixed=2, order=6)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 序号
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 序号
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="序号", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

}

