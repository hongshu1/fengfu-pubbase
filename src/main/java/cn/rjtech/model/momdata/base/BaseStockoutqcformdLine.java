package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量管理-出库检明细列值
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseStockoutqcformdLine<M extends BaseStockoutqcformdLine<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**检验项目配置*/
    public static final String ISTOCKOUTQCFORMDID = "iStockoutQcFormDid";
    /**列顺序值*/
    public static final String ISEQ = "iSeq";
    /**填写值*/
    public static final String CVALUE = "cValue";
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
	 * 检验项目配置
	 */
	public M setIStockoutQcFormDid(java.lang.Long iStockoutQcFormDid) {
		set("iStockoutQcFormDid", iStockoutQcFormDid);
		return (M)this;
	}

	/**
	 * 检验项目配置
	 */
	@JBoltField(name="istockoutqcformdid" ,columnName="iStockoutQcFormDid",type="Long", remark="检验项目配置", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "istockoutqcformdid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIStockoutQcFormDid() {
		return getLong("iStockoutQcFormDid");
	}

	/**
	 * 列顺序值
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 列顺序值
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="列顺序值", required=true, maxLength=10, fixed=0, order=3)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 填写值
	 */
	public M setCValue(java.lang.String cValue) {
		set("cValue", cValue);
		return (M)this;
	}

	/**
	 * 填写值
	 */
	@JBoltField(name="cvalue" ,columnName="cValue",type="String", remark="填写值", required=true, maxLength=200, fixed=0, order=4)
	@JSONField(name = "cvalue")
	public java.lang.String getCValue() {
		return getStr("cValue");
	}

}

