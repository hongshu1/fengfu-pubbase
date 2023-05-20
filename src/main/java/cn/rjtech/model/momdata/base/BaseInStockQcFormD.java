package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量管理-在库检单行配置
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInStockQcFormD<M extends BaseInStockQcFormD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**来料检ID*/
    public static final String IINSTOCKQCFORMMID = "iInStockQcFormMid";
    /**检验表格ID*/
    public static final String IQCFORMID = "iQcFormId";
    /**检验项目ID，Bd_QcFormTableParam.iAutoId*/
    public static final String IFORMPARAMID = "iFormParamId";
    /**项目次序，固定取参数项目名为“项目”的参数名称次序值*/
    public static final String ISEQ = "iSeq";
    /**子次序*/
    public static final String ISUBSEQ = "iSubSeq";
    /**检验参数值ID，点检方法允许为空（拼接“-”）多个逗号分隔*/
    public static final String CQCFORMPARAMIDS = "cQcFormParamIds";
    /**参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间*/
    public static final String ITYPE = "iType";
    /**标准值*/
    public static final String ISTDVAL = "iStdVal";
    /**最大设定值*/
    public static final String IMAXVAL = "iMaxVal";
    /**最小设定值*/
    public static final String IMINVAL = "iMinVal";
    /**列表可选值，多个";"分隔*/
    public static final String COPTIONS = "cOptions";
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
	 * 来料检ID
	 */
	public M setIInStockQcFormMid(java.lang.Long iInStockQcFormMid) {
		set("iInStockQcFormMid", iInStockQcFormMid);
		return (M)this;
	}

	/**
	 * 来料检ID
	 */
	@JBoltField(name="iinstockqcformmid" ,columnName="iInStockQcFormMid",type="Long", remark="来料检ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinstockqcformmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInStockQcFormMid() {
		return getLong("iInStockQcFormMid");
	}

	/**
	 * 检验表格ID
	 */
	public M setIQcFormId(java.lang.Long iQcFormId) {
		set("iQcFormId", iQcFormId);
		return (M)this;
	}

	/**
	 * 检验表格ID
	 */
	@JBoltField(name="iqcformid" ,columnName="iQcFormId",type="Long", remark="检验表格ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iqcformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcFormId() {
		return getLong("iQcFormId");
	}

	/**
	 * 检验项目ID，Bd_QcFormTableParam.iAutoId
	 */
	public M setIFormParamId(java.lang.Long iFormParamId) {
		set("iFormParamId", iFormParamId);
		return (M)this;
	}

	/**
	 * 检验项目ID，Bd_QcFormTableParam.iAutoId
	 */
	@JBoltField(name="iformparamid" ,columnName="iFormParamId",type="Long", remark="检验项目ID，Bd_QcFormTableParam.iAutoId", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iformparamid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormParamId() {
		return getLong("iFormParamId");
	}

	/**
	 * 项目次序，固定取参数项目名为“项目”的参数名称次序值
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 项目次序，固定取参数项目名为“项目”的参数名称次序值
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="项目次序，固定取参数项目名为“项目”的参数名称次序值", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 子次序
	 */
	public M setISubSeq(java.lang.Integer iSubSeq) {
		set("iSubSeq", iSubSeq);
		return (M)this;
	}

	/**
	 * 子次序
	 */
	@JBoltField(name="isubseq" ,columnName="iSubSeq",type="Integer", remark="子次序", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "isubseq")
	public java.lang.Integer getISubSeq() {
		return getInt("iSubSeq");
	}

	/**
	 * 检验参数值ID，点检方法允许为空（拼接“-”）多个逗号分隔
	 */
	public M setCQcFormParamIds(java.lang.String cQcFormParamIds) {
		set("cQcFormParamIds", cQcFormParamIds);
		return (M)this;
	}

	/**
	 * 检验参数值ID，点检方法允许为空（拼接“-”）多个逗号分隔
	 */
	@JBoltField(name="cqcformparamids" ,columnName="cQcFormParamIds",type="String", remark="检验参数值ID，点检方法允许为空（拼接“-”）多个逗号分隔", required=false, maxLength=500, fixed=0, order=7)
	@JSONField(name = "cqcformparamids")
	public java.lang.String getCQcFormParamIds() {
		return getStr("cQcFormParamIds");
	}

	/**
	 * 参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
	 */
	public M setIType(java.lang.Integer iType) {
		set("iType", iType);
		return (M)this;
	}

	/**
	 * 参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间", required=false, maxLength=10, fixed=0, order=8)
	@JSONField(name = "itype")
	public java.lang.Integer getIType() {
		return getInt("iType");
	}

	/**
	 * 标准值
	 */
	public M setIStdVal(java.math.BigDecimal iStdVal) {
		set("iStdVal", iStdVal);
		return (M)this;
	}

	/**
	 * 标准值
	 */
	@JBoltField(name="istdval" ,columnName="iStdVal",type="BigDecimal", remark="标准值", required=false, maxLength=20, fixed=7, order=9)
	@JSONField(name = "istdval")
	public java.math.BigDecimal getIStdVal() {
		return getBigDecimal("iStdVal");
	}

	/**
	 * 最大设定值
	 */
	public M setIMaxVal(java.math.BigDecimal iMaxVal) {
		set("iMaxVal", iMaxVal);
		return (M)this;
	}

	/**
	 * 最大设定值
	 */
	@JBoltField(name="imaxval" ,columnName="iMaxVal",type="BigDecimal", remark="最大设定值", required=false, maxLength=20, fixed=7, order=10)
	@JSONField(name = "imaxval")
	public java.math.BigDecimal getIMaxVal() {
		return getBigDecimal("iMaxVal");
	}

	/**
	 * 最小设定值
	 */
	public M setIMinVal(java.math.BigDecimal iMinVal) {
		set("iMinVal", iMinVal);
		return (M)this;
	}

	/**
	 * 最小设定值
	 */
	@JBoltField(name="iminval" ,columnName="iMinVal",type="BigDecimal", remark="最小设定值", required=false, maxLength=20, fixed=7, order=11)
	@JSONField(name = "iminval")
	public java.math.BigDecimal getIMinVal() {
		return getBigDecimal("iMinVal");
	}

	/**
	 * 列表可选值，多个";"分隔
	 */
	public M setCOptions(java.lang.String cOptions) {
		set("cOptions", cOptions);
		return (M)this;
	}

	/**
	 * 列表可选值，多个";"分隔
	 */
	@JBoltField(name="coptions" ,columnName="cOptions",type="String", remark="列表可选值，多个分号分隔", required=false, maxLength=500,
		fixed=0, order=12)
	@JSONField(name = "coptions")
	public java.lang.String getCOptions() {
		return getStr("cOptions");
	}

}

