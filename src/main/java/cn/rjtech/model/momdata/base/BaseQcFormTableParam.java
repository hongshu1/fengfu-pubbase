package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量建模-检验表格参数录入配置
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseQcFormTableParam<M extends BaseQcFormTableParam<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**检验项目ID，固定取参数项目名为“项目”的参数名称ID, Bd_QcFormParam.iAutoId*/
    public static final String IFORMPARAMID = "iFormParamId";
    /**项目次序，固定取参数项目名为“项目”的参数名称次序值*/
    public static final String ISEQ = "iSeq";
    /**子次序*/
    public static final String ISUBSEQ = "iSubSeq";
    /**除项目外，其他表格项目（检验表头栏目）*/
    public static final String CQCFORMITEMIDS = "cQcFormItemIds";
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
	 * 检验项目ID，固定取参数项目名为“项目”的参数名称ID, Bd_QcFormParam.iAutoId
	 */
	public M setIFormParamId(java.lang.Long iFormParamId) {
		set("iFormParamId", iFormParamId);
		return (M)this;
	}

	/**
	 * 检验项目ID，固定取参数项目名为“项目”的参数名称ID, Bd_QcFormParam.iAutoId
	 */
	@JBoltField(name="iformparamid" ,columnName="iFormParamId",type="Long", remark="检验项目ID，固定取参数项目名为“项目”的参数名称ID, Bd_QcFormParam.iAutoId", required=true, maxLength=19, fixed=0, order=2)
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
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="项目次序，固定取参数项目名为“项目”的参数名称次序值", required=true, maxLength=10, fixed=0, order=3)
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
	@JBoltField(name="isubseq" ,columnName="iSubSeq",type="Integer", remark="子次序", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "isubseq")
	public java.lang.Integer getISubSeq() {
		return getInt("iSubSeq");
	}

	/**
	 * 除项目外，其他表格项目（检验表头栏目）
	 */
	public M setCQcFormItemIds(java.lang.String cQcFormItemIds) {
		set("cQcFormItemIds", cQcFormItemIds);
		return (M)this;
	}

	/**
	 * 除项目外，其他表格项目（检验表头栏目）
	 */
	@JBoltField(name="cqcformitemids" ,columnName="cQcFormItemIds",type="String", remark="除项目外，其他表格项目（检验表头栏目）", required=false, maxLength=500, fixed=0, order=5)
	@JSONField(name = "cqcformitemids")
	public java.lang.String getCQcFormItemIds() {
		return getStr("cQcFormItemIds");
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
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间", required=true, maxLength=10, fixed=0, order=6)
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
	@JBoltField(name="istdval" ,columnName="iStdVal",type="BigDecimal", remark="标准值", required=true, maxLength=20, fixed=7, order=7)
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
	@JBoltField(name="imaxval" ,columnName="iMaxVal",type="BigDecimal", remark="最大设定值", required=true, maxLength=20, fixed=7, order=8)
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
	@JBoltField(name="iminval" ,columnName="iMinVal",type="BigDecimal", remark="最小设定值", required=true, maxLength=20, fixed=7, order=9)
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
	@JBoltField(name="coptions" ,columnName="cOptions",type="String", remark="列表可选值，多个;分隔", required=false, maxLength=500, fixed=0, order=10)
	@JSONField(name = "coptions")
	public java.lang.String getCOptions() {
		return getStr("cOptions");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=11)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}
