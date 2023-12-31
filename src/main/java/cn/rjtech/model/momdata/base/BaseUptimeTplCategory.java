package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 稼动时间建模-稼动时间模板类别
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseUptimeTplCategory<M extends BaseUptimeTplCategory<M>> extends JBoltBaseModel<M>{
    
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**稼动时间模板ID*/
    public static final String IUPTIMETPLMID = "iUptimeTplMid";
    /**稼动时间类别ID*/
    public static final String IUPTIMECATEGORYID = "iUptimeCategoryId";
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
	 * 稼动时间模板ID
	 */
	public M setIUptimeTplMid(java.lang.Long iUptimeTplMid) {
		set("iUptimeTplMid", iUptimeTplMid);
		return (M)this;
	}

	/**
	 * 稼动时间模板ID
	 */
	@JBoltField(name="iuptimetplmid" ,columnName="iUptimeTplMid",type="Long", remark="稼动时间模板ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iuptimetplmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUptimeTplMid() {
		return getLong("iUptimeTplMid");
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
	 * 序号
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 序号
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="序号", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

}

