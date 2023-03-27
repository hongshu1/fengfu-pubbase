package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 料品计划档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInventoryPlan<M extends BaseInventoryPlan<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**料品档案ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**计划策略(字典值)*/
    public static final String CPLANPOLICYSN = "cPlanPolicySn";
    /**订购方法(字典值)*/
    public static final String CORDERMETHODSN = "cOrderMethodSn";
    /**批量规则(字典值)*/
    public static final String CBATCH = "cBatch";
    /**批量增量*/
    public static final String IBATCHADD = "iBatchAdd";
    /**供应天数*/
    public static final String ISUPPLYDAYS = "iSupplyDays";
    /**固定批量*/
    public static final String IFIXEDBATCH = "iFixedBatch";
    /**提前期*/
    public static final String IADVANCE = "iAdvance";
    /**日均损耗*/
    public static final String IDAILYLOSS = "iDailyLoss";
    /**尾数处理(字典值)*/
    public static final String CMANTISSASN = "cMantissaSn";
	/**
	 * 主键
	 */
	public M setIAutoId(java.lang.Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 料品档案ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 料品档案ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="料品档案ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 计划策略(字典值)
	 */
	public M setCPlanPolicySn(java.lang.String cPlanPolicySn) {
		set("cPlanPolicySn", cPlanPolicySn);
		return (M)this;
	}

	/**
	 * 计划策略(字典值)
	 */
	@JBoltField(name="cplanpolicysn" ,columnName="cPlanPolicySn",type="String", remark="计划策略(字典值)", required=false, maxLength=255, fixed=0, order=3)
	@JSONField(name = "cplanpolicysn")
	public java.lang.String getCPlanPolicySn() {
		return getStr("cPlanPolicySn");
	}

	/**
	 * 订购方法(字典值)
	 */
	public M setCOrderMethodSn(java.lang.String cOrderMethodSn) {
		set("cOrderMethodSn", cOrderMethodSn);
		return (M)this;
	}

	/**
	 * 订购方法(字典值)
	 */
	@JBoltField(name="cordermethodsn" ,columnName="cOrderMethodSn",type="String", remark="订购方法(字典值)", required=false, maxLength=255, fixed=0, order=4)
	@JSONField(name = "cordermethodsn")
	public java.lang.String getCOrderMethodSn() {
		return getStr("cOrderMethodSn");
	}

	/**
	 * 批量规则(字典值)
	 */
	public M setCBatch(java.lang.String cBatch) {
		set("cBatch", cBatch);
		return (M)this;
	}

	/**
	 * 批量规则(字典值)
	 */
	@JBoltField(name="cbatch" ,columnName="cBatch",type="String", remark="批量规则(字典值)", required=false, maxLength=255, fixed=0, order=5)
	@JSONField(name = "cbatch")
	public java.lang.String getCBatch() {
		return getStr("cBatch");
	}

	/**
	 * 批量增量
	 */
	public M setIBatchAdd(java.lang.Integer iBatchAdd) {
		set("iBatchAdd", iBatchAdd);
		return (M)this;
	}

	/**
	 * 批量增量
	 */
	@JBoltField(name="ibatchadd" ,columnName="iBatchAdd",type="Integer", remark="批量增量", required=false, maxLength=10, fixed=0, order=6)
	@JSONField(name = "ibatchadd")
	public java.lang.Integer getIBatchAdd() {
		return getInt("iBatchAdd");
	}

	/**
	 * 供应天数
	 */
	public M setISupplyDays(java.math.BigDecimal iSupplyDays) {
		set("iSupplyDays", iSupplyDays);
		return (M)this;
	}

	/**
	 * 供应天数
	 */
	@JBoltField(name="isupplydays" ,columnName="iSupplyDays",type="BigDecimal", remark="供应天数", required=false, maxLength=24, fixed=6, order=7)
	@JSONField(name = "isupplydays")
	public java.math.BigDecimal getISupplyDays() {
		return getBigDecimal("iSupplyDays");
	}

	/**
	 * 固定批量
	 */
	public M setIFixedBatch(java.math.BigDecimal iFixedBatch) {
		set("iFixedBatch", iFixedBatch);
		return (M)this;
	}

	/**
	 * 固定批量
	 */
	@JBoltField(name="ifixedbatch" ,columnName="iFixedBatch",type="BigDecimal", remark="固定批量", required=false, maxLength=24, fixed=6, order=8)
	@JSONField(name = "ifixedbatch")
	public java.math.BigDecimal getIFixedBatch() {
		return getBigDecimal("iFixedBatch");
	}

	/**
	 * 提前期
	 */
	public M setIAdvance(java.math.BigDecimal iAdvance) {
		set("iAdvance", iAdvance);
		return (M)this;
	}

	/**
	 * 提前期
	 */
	@JBoltField(name="iadvance" ,columnName="iAdvance",type="BigDecimal", remark="提前期", required=false, maxLength=24, fixed=6, order=9)
	@JSONField(name = "iadvance")
	public java.math.BigDecimal getIAdvance() {
		return getBigDecimal("iAdvance");
	}

	/**
	 * 日均损耗
	 */
	public M setIDailyLoss(java.math.BigDecimal iDailyLoss) {
		set("iDailyLoss", iDailyLoss);
		return (M)this;
	}

	/**
	 * 日均损耗
	 */
	@JBoltField(name="idailyloss" ,columnName="iDailyLoss",type="BigDecimal", remark="日均损耗", required=false, maxLength=24, fixed=6, order=10)
	@JSONField(name = "idailyloss")
	public java.math.BigDecimal getIDailyLoss() {
		return getBigDecimal("iDailyLoss");
	}

	/**
	 * 尾数处理(字典值)
	 */
	public M setCMantissaSn(java.lang.String cMantissaSn) {
		set("cMantissaSn", cMantissaSn);
		return (M)this;
	}

	/**
	 * 尾数处理(字典值)
	 */
	@JBoltField(name="cmantissasn" ,columnName="cMantissaSn",type="String", remark="尾数处理(字典值)", required=false, maxLength=255, fixed=0, order=11)
	@JSONField(name = "cmantissasn")
	public java.lang.String getCMantissaSn() {
		return getStr("cMantissaSn");
	}

}

