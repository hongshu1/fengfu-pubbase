package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统配置-导入字段配置明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCusFieldsMappingD<M extends BaseCusFieldsMappingD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**客户导入字段配置主表ID*/
    public static final String ICUSFIELDSMAPPINGMID = "iCusFieldsMappingMid";
    /**系统字段ID*/
    public static final String IFORMFIELDID = "iFormFieldId";
    /**导入字段名称*/
    public static final String CCUSFIELDNAME = "cCusFieldName";
    /**是否编码字段;0. 否 1. 是*/
    public static final String ISENCODED = "isEncoded";
    /**编码示例*/
    public static final String CDEMO = "cDemo";
    /**导入编码字段长度*/
    public static final String ILENGTH = "iLength";
    /**是否启用;0. 否 1. 是*/
    public static final String ISENABLED = "isEnabled";
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
	 * 客户导入字段配置主表ID
	 */
	public M setICusFieldsMappingMid(java.lang.Long iCusFieldsMappingMid) {
		set("iCusFieldsMappingMid", iCusFieldsMappingMid);
		return (M)this;
	}

	/**
	 * 客户导入字段配置主表ID
	 */
	@JBoltField(name="icusfieldsmappingmid" ,columnName="iCusFieldsMappingMid",type="Long", remark="客户导入字段配置主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "icusfieldsmappingmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICusFieldsMappingMid() {
		return getLong("iCusFieldsMappingMid");
	}

	/**
	 * 系统字段ID
	 */
	public M setIFormFieldId(java.lang.Long iFormFieldId) {
		set("iFormFieldId", iFormFieldId);
		return (M)this;
	}

	/**
	 * 系统字段ID
	 */
	@JBoltField(name="iformfieldid" ,columnName="iFormFieldId",type="Long", remark="系统字段ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iformfieldid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormFieldId() {
		return getLong("iFormFieldId");
	}

	/**
	 * 导入字段名称
	 */
	public M setCCusFieldName(java.lang.String cCusFieldName) {
		set("cCusFieldName", cCusFieldName);
		return (M)this;
	}

	/**
	 * 导入字段名称
	 */
	@JBoltField(name="ccusfieldname" ,columnName="cCusFieldName",type="String", remark="导入字段名称", required=true, maxLength=40, fixed=0, order=4)
	@JSONField(name = "ccusfieldname")
	public java.lang.String getCCusFieldName() {
		return getStr("cCusFieldName");
	}

	/**
	 * 是否编码字段;0. 否 1. 是
	 */
	public M setIsEncoded(java.lang.Boolean isEncoded) {
		set("isEncoded", isEncoded);
		return (M)this;
	}

	/**
	 * 是否编码字段;0. 否 1. 是
	 */
	@JBoltField(name="isencoded" ,columnName="isEncoded",type="Boolean", remark="是否编码字段;0. 否 1. 是", required=true, maxLength=1, fixed=0, order=5)
	@JSONField(name = "isencoded")
	public java.lang.Boolean getIsEncoded() {
		return getBoolean("isEncoded");
	}

	/**
	 * 编码示例
	 */
	public M setCDemo(java.lang.String cDemo) {
		set("cDemo", cDemo);
		return (M)this;
	}

	/**
	 * 编码示例
	 */
	@JBoltField(name="cdemo" ,columnName="cDemo",type="String", remark="编码示例", required=false, maxLength=200, fixed=0, order=6)
	@JSONField(name = "cdemo")
	public java.lang.String getCDemo() {
		return getStr("cDemo");
	}

	/**
	 * 导入编码字段长度
	 */
	public M setILength(java.lang.Integer iLength) {
		set("iLength", iLength);
		return (M)this;
	}

	/**
	 * 导入编码字段长度
	 */
	@JBoltField(name="ilength" ,columnName="iLength",type="Integer", remark="导入编码字段长度", required=false, maxLength=10, fixed=0, order=7)
	@JSONField(name = "ilength")
	public java.lang.Integer getILength() {
		return getInt("iLength");
	}

	/**
	 * 是否启用;0. 否 1. 是
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用;0. 否 1. 是
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用;0. 否 1. 是", required=true, maxLength=1, fixed=0, order=8)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
	}

}

