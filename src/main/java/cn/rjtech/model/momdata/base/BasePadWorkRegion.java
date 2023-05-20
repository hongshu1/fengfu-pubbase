package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统管理-平板关联产线
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BasePadWorkRegion<M extends BasePadWorkRegion<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**平板ID*/
    public static final String IPADID = "iPadId";
    /**产线ID*/
    public static final String IWORKREGIONMID = "iWorkRegionMid";
    /**是否默认；0. 否 1. 是*/
    public static final String ISDEFAULT = "isDefault";
    /**备注*/
    public static final String CMEMO = "cMemo";
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
	 * 平板ID
	 */
	public M setIPadId(java.lang.Long iPadId) {
		set("iPadId", iPadId);
		return (M)this;
	}

	/**
	 * 平板ID
	 */
	@JBoltField(name="ipadid" ,columnName="iPadId",type="Long", remark="平板ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "ipadid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIPadId() {
		return getLong("iPadId");
	}

	/**
	 * 产线ID
	 */
	public M setIWorkRegionMid(java.lang.Long iWorkRegionMid) {
		set("iWorkRegionMid", iWorkRegionMid);
		return (M)this;
	}

	/**
	 * 产线ID
	 */
	@JBoltField(name="iworkregionmid" ,columnName="iWorkRegionMid",type="Long", remark="产线ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iworkregionmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWorkRegionMid() {
		return getLong("iWorkRegionMid");
	}

	/**
	 * 是否默认；0. 否 1. 是
	 */
	public M setIsDefault(java.lang.Boolean isDefault) {
		set("isDefault", isDefault);
		return (M)this;
	}

	/**
	 * 是否默认；0. 否 1. 是
	 */
	@JBoltField(name="isdefault" ,columnName="isDefault",type="Boolean", remark="是否默认；0. 否 1. 是", required=true, maxLength=1, fixed=0, order=4)
	@JSONField(name = "isdefault")
	public java.lang.Boolean getIsDefault() {
		return getBoolean("isDefault");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=5)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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

