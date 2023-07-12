package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量建模-点检表格参数
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSpotCheckFormParam<M extends BaseSpotCheckFormParam<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**参数项目ID*/
    public static final String ISPOTCHECKFORMITEMID = "iSpotCheckFormItemId";
    /**参数名称ID*/
    public static final String ISPOTCHECKPARAMID = "iSpotCheckParamId";
    /**参数项目顺序值，表格项目修改顺序时，同步此值*/
    public static final String IITEMSEQ = "iItemSeq";
    /**参数名称顺序值*/
    public static final String IITEMPARAMSEQ = "iItemParamSeq";
    /**删除状态: 0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
    /**检验表格ID*/
    public static final String ISPOTCHECKFORMID = "iSpotCheckFormId";
    /**检验项目ID*/
    public static final String IQCITEMID = "iQcItemId";
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
	 * 参数项目ID
	 */
	public M setISpotCheckFormItemId(java.lang.Long iSpotCheckFormItemId) {
		set("iSpotCheckFormItemId", iSpotCheckFormItemId);
		return (M)this;
	}

	/**
	 * 参数项目ID
	 */
	@JBoltField(name="ispotcheckformitemid" ,columnName="iSpotCheckFormItemId",type="Long", remark="参数项目ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "ispotcheckformitemid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISpotCheckFormItemId() {
		return getLong("iSpotCheckFormItemId");
	}

	/**
	 * 参数名称ID
	 */
	public M setISpotCheckParamId(java.lang.Long iSpotCheckParamId) {
		set("iSpotCheckParamId", iSpotCheckParamId);
		return (M)this;
	}

	/**
	 * 参数名称ID
	 */
	@JBoltField(name="ispotcheckparamid" ,columnName="iSpotCheckParamId",type="Long", remark="参数名称ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "ispotcheckparamid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISpotCheckParamId() {
		return getLong("iSpotCheckParamId");
	}

	/**
	 * 参数项目顺序值，表格项目修改顺序时，同步此值
	 */
	public M setIItemSeq(java.lang.Integer iItemSeq) {
		set("iItemSeq", iItemSeq);
		return (M)this;
	}

	/**
	 * 参数项目顺序值，表格项目修改顺序时，同步此值
	 */
	@JBoltField(name="iitemseq" ,columnName="iItemSeq",type="Integer", remark="参数项目顺序值，表格项目修改顺序时，同步此值", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "iitemseq")
	public java.lang.Integer getIItemSeq() {
		return getInt("iItemSeq");
	}

	/**
	 * 参数名称顺序值
	 */
	public M setIItemParamSeq(java.lang.Integer iItemParamSeq) {
		set("iItemParamSeq", iItemParamSeq);
		return (M)this;
	}

	/**
	 * 参数名称顺序值
	 */
	@JBoltField(name="iitemparamseq" ,columnName="iItemParamSeq",type="Integer", remark="参数名称顺序值", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "iitemparamseq")
	public java.lang.Integer getIItemParamSeq() {
		return getInt("iItemParamSeq");
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态: 0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=6)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 检验表格ID
	 */
	public M setISpotCheckFormId(java.lang.Long iSpotCheckFormId) {
		set("iSpotCheckFormId", iSpotCheckFormId);
		return (M)this;
	}

	/**
	 * 检验表格ID
	 */
	@JBoltField(name="ispotcheckformid" ,columnName="iSpotCheckFormId",type="Long", remark="检验表格ID", required=false, maxLength=19, fixed=0, order=7)
	@JSONField(name = "ispotcheckformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISpotCheckFormId() {
		return getLong("iSpotCheckFormId");
	}

	/**
	 * 检验项目ID
	 */
	public M setIQcItemId(java.lang.Long iQcItemId) {
		set("iQcItemId", iQcItemId);
		return (M)this;
	}

	/**
	 * 检验项目ID
	 */
	@JBoltField(name="iqcitemid" ,columnName="iQcItemId",type="Long", remark="检验项目ID", required=false, maxLength=19, fixed=0, order=8)
	@JSONField(name = "iqcitemid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcItemId() {
		return getLong("iQcItemId");
	}

}

