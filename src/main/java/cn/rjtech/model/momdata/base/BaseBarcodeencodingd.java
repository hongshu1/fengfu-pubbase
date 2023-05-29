package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 编码规则细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseBarcodeencodingd<M extends BaseBarcodeencodingd<M>> extends JBoltBaseModel<M>{
    /**从表主键*/
    public static final String IAUTOID = "IAUTOID";
    /**主表ID*/
    public static final String MID = "MID";
    /**项目*/
    public static final String CPROJECTCODE = "CPROJECTCODE";
    /**排序值*/
    public static final String ISEQ = "ISEQ";
    /**项目值*/
    public static final String CPROJECTVALUE = "CPROJECTVALUE";
    /**后缀字符*/
    public static final String CSUFFIX = "CSUFFIX";
    /**日期格式*/
    public static final String CDATEFORMAT = "CDATEFORMAT";
    /**流水号长度*/
    public static final String IBILLNOLEN = "IBILLNOLEN";
	/**
	 * 从表主键
	 */
	public M setIautoid(java.lang.Long iautoid) {
		set("IAUTOID", iautoid);
		return (M)this;
	}

	/**
	 * 从表主键
	 */
	@JBoltField(name="iautoid" ,columnName="IAUTOID",type="Long", remark="从表主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIautoid() {
		return getLong("IAUTOID");
	}

	/**
	 * 主表ID
	 */
	public M setMid(java.lang.Long mid) {
		set("MID", mid);
		return (M)this;
	}

	/**
	 * 主表ID
	 */
	@JBoltField(name="mid" ,columnName="MID",type="Long", remark="主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getMid() {
		return getLong("MID");
	}

	/**
	 * 项目
	 */
	public M setCprojectcode(java.lang.String cprojectcode) {
		set("CPROJECTCODE", cprojectcode);
		return (M)this;
	}

	/**
	 * 项目
	 */
	@JBoltField(name="cprojectcode" ,columnName="CPROJECTCODE",type="String", remark="项目", required=true, maxLength=30, fixed=0, order=3)
	public java.lang.String getCprojectcode() {
		return getStr("CPROJECTCODE");
	}

	/**
	 * 排序值
	 */
	public M setIseq(java.lang.Integer iseq) {
		set("ISEQ", iseq);
		return (M)this;
	}

	/**
	 * 排序值
	 */
	@JBoltField(name="iseq" ,columnName="ISEQ",type="Integer", remark="排序值", required=true, maxLength=10, fixed=0, order=4)
	public java.lang.Integer getIseq() {
		return getInt("ISEQ");
	}

	/**
	 * 项目值
	 */
	public M setCprojectvalue(java.lang.String cprojectvalue) {
		set("CPROJECTVALUE", cprojectvalue);
		return (M)this;
	}

	/**
	 * 项目值
	 */
	@JBoltField(name="cprojectvalue" ,columnName="CPROJECTVALUE",type="String", remark="项目值", required=false, maxLength=255, fixed=0, order=5)
	public java.lang.String getCprojectvalue() {
		return getStr("CPROJECTVALUE");
	}

	/**
	 * 后缀字符
	 */
	public M setCsuffix(java.lang.String csuffix) {
		set("CSUFFIX", csuffix);
		return (M)this;
	}

	/**
	 * 后缀字符
	 */
	@JBoltField(name="csuffix" ,columnName="CSUFFIX",type="String", remark="后缀字符", required=false, maxLength=10, fixed=0, order=6)
	public java.lang.String getCsuffix() {
		return getStr("CSUFFIX");
	}

	/**
	 * 日期格式
	 */
	public M setCdateformat(java.lang.String cdateformat) {
		set("CDATEFORMAT", cdateformat);
		return (M)this;
	}

	/**
	 * 日期格式
	 */
	@JBoltField(name="cdateformat" ,columnName="CDATEFORMAT",type="String", remark="日期格式", required=false, maxLength=20, fixed=0, order=7)
	public java.lang.String getCdateformat() {
		return getStr("CDATEFORMAT");
	}

	/**
	 * 流水号长度
	 */
	public M setIbillnolen(java.lang.Integer ibillnolen) {
		set("IBILLNOLEN", ibillnolen);
		return (M)this;
	}

	/**
	 * 流水号长度
	 */
	@JBoltField(name="ibillnolen" ,columnName="IBILLNOLEN",type="Integer", remark="流水号长度", required=false, maxLength=10, fixed=0, order=8)
	public java.lang.Integer getIbillnolen() {
		return getInt("IBILLNOLEN");
	}

}

