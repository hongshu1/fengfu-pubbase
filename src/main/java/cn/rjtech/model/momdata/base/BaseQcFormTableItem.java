package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量建模-检验表格行记录
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseQcFormTableItem<M extends BaseQcFormTableItem<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**检验项目ID*/
    public static final String IQCFORMITEMID = "iQcFormItemId";
    /**检验参数ID*/
    public static final String IQCFORMPARAMID = "iQcFormParamId";
    /**检验表格行ID*/
    public static final String IQCFORMTABLEPARAMID = "iQcFormTableParamId";
    /**检验表格ID*/
    public static final String IQCFORMID = "iQCFormId";
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
	 * 检验项目ID
	 */
	public M setIQcFormItemId(java.lang.Long iQcFormItemId) {
		set("iQcFormItemId", iQcFormItemId);
		return (M)this;
	}

	/**
	 * 检验项目ID
	 */
	@JBoltField(name="iqcformitemid" ,columnName="iQcFormItemId",type="Long", remark="检验项目ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iqcformitemid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcFormItemId() {
		return getLong("iQcFormItemId");
	}

	/**
	 * 检验参数ID
	 */
	public M setIQcFormParamId(java.lang.Long iQcFormParamId) {
		set("iQcFormParamId", iQcFormParamId);
		return (M)this;
	}

	/**
	 * 检验参数ID
	 */
	@JBoltField(name="iqcformparamid" ,columnName="iQcFormParamId",type="Long", remark="检验参数ID", required=false, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iqcformparamid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcFormParamId() {
		return getLong("iQcFormParamId");
	}

	/**
	 * 检验表格行ID
	 */
	public M setIQcFormTableParamId(java.lang.Long iQcFormTableParamId) {
		set("iQcFormTableParamId", iQcFormTableParamId);
		return (M)this;
	}

	/**
	 * 检验表格行ID
	 */
	@JBoltField(name="iqcformtableparamid" ,columnName="iQcFormTableParamId",type="Long", remark="检验表格行ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iqcformtableparamid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcFormTableParamId() {
		return getLong("iQcFormTableParamId");
	}

	/**
	 * 检验表格ID
	 */
	public M setIQCFormId(java.lang.Long iQCFormId) {
		set("iQCFormId", iQCFormId);
		return (M)this;
	}

	/**
	 * 检验表格ID
	 */
	@JBoltField(name="iqcformid" ,columnName="iQCFormId",type="Long", remark="检验表格ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "iqcformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQCFormId() {
		return getLong("iQCFormId");
	}

}

