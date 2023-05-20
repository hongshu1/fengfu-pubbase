package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统设置-编码规则明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseCodingRuleD<M extends BaseCodingRuleD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**编码规则主表ID*/
    public static final String ICODINGRULEMID = "iCodingRuleMid";
    /**序号*/
    public static final String ISEQ = "iSeq";
    /**类型： 1. 工单号 2. 流水号 3. 手工输入 4. 2位年 5. 2位月 6. 2位日*/
    public static final String CCODINGTYPE = "cCodingType";
    /**来源值*/
    public static final String CCODINGVALUE = "cCodingValue";
    /**长度*/
    public static final String ILENGTH = "iLength";
    /**起始值*/
    public static final String IBEGINVALUE = "iBeginValue";
    /**分隔符*/
    public static final String CSEPARATOR = "cSeparator";
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
	 * 编码规则主表ID
	 */
	public M setICodingRuleMid(java.lang.Long iCodingRuleMid) {
		set("iCodingRuleMid", iCodingRuleMid);
		return (M)this;
	}

	/**
	 * 编码规则主表ID
	 */
	@JBoltField(name="icodingrulemid" ,columnName="iCodingRuleMid",type="Long", remark="编码规则主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "icodingrulemid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICodingRuleMid() {
		return getLong("iCodingRuleMid");
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
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="序号", required=false, maxLength=10, fixed=0, order=3)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 类型： 1. 工单号 2. 流水号 3. 手工输入 4. 2位年 5. 2位月 6. 2位日
	 */
	public M setCCodingType(java.lang.String cCodingType) {
		set("cCodingType", cCodingType);
		return (M)this;
	}

	/**
	 * 类型： 1. 工单号 2. 流水号 3. 手工输入 4. 2位年 5. 2位月 6. 2位日
	 */
	@JBoltField(name="ccodingtype" ,columnName="cCodingType",type="String", remark="类型： 1. 工单号 2. 流水号 3. 手工输入 4. 2位年 5. 2位月 6. 2位日", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "ccodingtype")
	public java.lang.String getCCodingType() {
		return getStr("cCodingType");
	}

	/**
	 * 来源值
	 */
	public M setCCodingValue(java.lang.String cCodingValue) {
		set("cCodingValue", cCodingValue);
		return (M)this;
	}

	/**
	 * 来源值
	 */
	@JBoltField(name="ccodingvalue" ,columnName="cCodingValue",type="String", remark="来源值", required=false, maxLength=10, fixed=0, order=5)
	@JSONField(name = "ccodingvalue")
	public java.lang.String getCCodingValue() {
		return getStr("cCodingValue");
	}

	/**
	 * 长度
	 */
	public M setILength(java.lang.Integer iLength) {
		set("iLength", iLength);
		return (M)this;
	}

	/**
	 * 长度
	 */
	@JBoltField(name="ilength" ,columnName="iLength",type="Integer", remark="长度", required=false, maxLength=10, fixed=0, order=6)
	@JSONField(name = "ilength")
	public java.lang.Integer getILength() {
		return getInt("iLength");
	}

	/**
	 * 起始值
	 */
	public M setIBeginValue(java.lang.String iBeginValue) {
		set("iBeginValue", iBeginValue);
		return (M)this;
	}

	/**
	 * 起始值
	 */
	@JBoltField(name="ibeginvalue" ,columnName="iBeginValue",type="String", remark="起始值", required=false, maxLength=20, fixed=0, order=7)
	@JSONField(name = "ibeginvalue")
	public java.lang.String getIBeginValue() {
		return getStr("iBeginValue");
	}

	/**
	 * 分隔符
	 */
	public M setCSeparator(java.lang.String cSeparator) {
		set("cSeparator", cSeparator);
		return (M)this;
	}

	/**
	 * 分隔符
	 */
	@JBoltField(name="cseparator" ,columnName="cSeparator",type="String", remark="分隔符", required=false, maxLength=3, fixed=0, order=8)
	@JSONField(name = "cseparator")
	public java.lang.String getCSeparator() {
		return getStr("cSeparator");
	}

}

