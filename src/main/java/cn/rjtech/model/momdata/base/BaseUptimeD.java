package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造管理-稼动时间记录明细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseUptimeD<M extends BaseUptimeD<M>> extends JBoltBaseModel<M>{
    
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**稼动时间记录主表ID*/
    public static final String IUPTIMEMID = "iUptimeMid";
    /**稼动时间类别ID*/
    public static final String IUPTIMECATEGORYID = "iUptimeCategoryId";
    /**稼动时间参数ID*/
    public static final String IUPTIMEPARAMID = "iUptimeParamId";
    /**顺序值*/
    public static final String ISEQ = "iSeq";
    /**设定值（分钟）*/
    public static final String ISTDMINS = "iStdMins";
    /**时间（分钟）*/
    public static final String IMINS = "iMins";
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
	 * 稼动时间记录主表ID
	 */
	public M setIUptimeMid(java.lang.Long iUptimeMid) {
		set("iUptimeMid", iUptimeMid);
		return (M)this;
	}

	/**
	 * 稼动时间记录主表ID
	 */
	@JBoltField(name="iuptimemid" ,columnName="iUptimeMid",type="Long", remark="稼动时间记录主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iuptimemid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUptimeMid() {
		return getLong("iUptimeMid");
	}

	/**
	 * 稼动时间类别ID
	 */
	public M setIUptimeCategoryId(java.lang.Long iUptimeCategoryId) {
		set("iUptimeCategoryId", iUptimeCategoryId);
		return (M)this;
	}

	/**
	 * 稼动时间类别ID
	 */
	@JBoltField(name="iuptimecategoryid" ,columnName="iUptimeCategoryId",type="Long", remark="稼动时间类别ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iuptimecategoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUptimeCategoryId() {
		return getLong("iUptimeCategoryId");
	}

	/**
	 * 稼动时间参数ID
	 */
	public M setIUptimeParamId(java.lang.Long iUptimeParamId) {
		set("iUptimeParamId", iUptimeParamId);
		return (M)this;
	}

	/**
	 * 稼动时间参数ID
	 */
	@JBoltField(name="iuptimeparamid" ,columnName="iUptimeParamId",type="Long", remark="稼动时间参数ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iuptimeparamid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUptimeParamId() {
		return getLong("iUptimeParamId");
	}

	/**
	 * 顺序值
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 顺序值
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="顺序值", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 设定值（分钟）
	 */
	public M setIStdMins(java.lang.Integer iStdMins) {
		set("iStdMins", iStdMins);
		return (M)this;
	}

	/**
	 * 设定值（分钟）
	 */
	@JBoltField(name="istdmins" ,columnName="iStdMins",type="Integer", remark="设定值（分钟）", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "istdmins")
	public java.lang.Integer getIStdMins() {
		return getInt("iStdMins");
	}

	/**
	 * 时间（分钟）
	 */
	public M setIMins(java.lang.Integer iMins) {
		set("iMins", iMins);
		return (M)this;
	}

	/**
	 * 时间（分钟）
	 */
	@JBoltField(name="imins" ,columnName="iMins",type="Integer", remark="时间（分钟）", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "imins")
	public java.lang.Integer getIMins() {
		return getInt("iMins");
	}

}

