package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 物料建模-存货工艺配置
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInventoryRoutingConfig<M extends BaseInventoryRoutingConfig<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**存货工艺档案ID*/
    public static final String IINVENTORYROUTINGID = "iInventoryRoutingId";
    /**序号*/
    public static final String ISEQ = "iSeq";
    /**合并工序*/
    public static final String CMERGEDSEQ = "cMergedSeq";
    /**工序名称*/
    public static final String COPERATIONNAME = "cOperationName";
    /**工序类型: 1. 串序 2. 并序*/
    public static final String ITYPE = "iType";
    /**半成品/成品ID*/
    public static final String IRSINVENTORYID = "iRsInventoryId";
    /**生产方式(字典值) 内作/外作*/
    public static final String CPRODUCTSN = "cProductSn";
    /**生产工艺： 1. 冲压 2. 弯管 3. 焊接*/
    public static final String CPRODUCTTECHSN = "cProductTechSn";
    /**合并要员（人）*/
    public static final String IMERGEDNUM = "iMergedNum";
    /**要员（人）*/
    public static final String IMERGERATE = "iMergeRate";
    /**合并工时（秒）*/
    public static final String IMERGESECS = "iMergeSecs";
    /**单工时（秒）*/
    public static final String ISECS = "iSecs";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**是否启用*/
    public static final String ISENABLED = "isEnabled";
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
	 * 存货工艺档案ID
	 */
	public M setIInventoryRoutingId(java.lang.Long iInventoryRoutingId) {
		set("iInventoryRoutingId", iInventoryRoutingId);
		return (M)this;
	}

	/**
	 * 存货工艺档案ID
	 */
	@JBoltField(name="iinventoryroutingid" ,columnName="iInventoryRoutingId",type="Long", remark="存货工艺档案ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryroutingid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryRoutingId() {
		return getLong("iInventoryRoutingId");
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
	 * 合并工序
	 */
	public M setCMergedSeq(java.lang.String cMergedSeq) {
		set("cMergedSeq", cMergedSeq);
		return (M)this;
	}

	/**
	 * 合并工序
	 */
	@JBoltField(name="cmergedseq" ,columnName="cMergedSeq",type="String", remark="合并工序", required=false, maxLength=20, fixed=0, order=4)
	@JSONField(name = "cmergedseq")
	public java.lang.String getCMergedSeq() {
		return getStr("cMergedSeq");
	}

	/**
	 * 工序名称
	 */
	public M setCOperationName(java.lang.String cOperationName) {
		set("cOperationName", cOperationName);
		return (M)this;
	}

	/**
	 * 工序名称
	 */
	@JBoltField(name="coperationname" ,columnName="cOperationName",type="String", remark="工序名称", required=false, maxLength=90, fixed=0, order=5)
	@JSONField(name = "coperationname")
	public java.lang.String getCOperationName() {
		return getStr("cOperationName");
	}

	/**
	 * 工序类型: 1. 串序 2. 并序
	 */
	public M setIType(java.lang.Integer iType) {
		set("iType", iType);
		return (M)this;
	}

	/**
	 * 工序类型: 1. 串序 2. 并序
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="工序类型: 1. 串序 2. 并序", required=false, maxLength=10, fixed=0, order=6)
	@JSONField(name = "itype")
	public java.lang.Integer getIType() {
		return getInt("iType");
	}

	/**
	 * 半成品/成品ID
	 */
	public M setIRsInventoryId(java.lang.Long iRsInventoryId) {
		set("iRsInventoryId", iRsInventoryId);
		return (M)this;
	}

	/**
	 * 半成品/成品ID
	 */
	@JBoltField(name="irsinventoryid" ,columnName="iRsInventoryId",type="Long", remark="半成品/成品ID", required=true, maxLength=19, fixed=0, order=7)
	@JSONField(name = "irsinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIRsInventoryId() {
		return getLong("iRsInventoryId");
	}

	/**
	 * 生产方式(字典值) 内作/外作
	 */
	public M setCProductSn(java.lang.String cProductSn) {
		set("cProductSn", cProductSn);
		return (M)this;
	}

	/**
	 * 生产方式(字典值) 内作/外作
	 */
	@JBoltField(name="cproductsn" ,columnName="cProductSn",type="String", remark="生产方式(字典值) 内作/外作", required=false, maxLength=10, fixed=0, order=8)
	@JSONField(name = "cproductsn")
	public java.lang.String getCProductSn() {
		return getStr("cProductSn");
	}

	/**
	 * 生产工艺： 1. 冲压 2. 弯管 3. 焊接
	 */
	public M setCProductTechSn(java.lang.String cProductTechSn) {
		set("cProductTechSn", cProductTechSn);
		return (M)this;
	}

	/**
	 * 生产工艺： 1. 冲压 2. 弯管 3. 焊接
	 */
	@JBoltField(name="cproducttechsn" ,columnName="cProductTechSn",type="String", remark="生产工艺： 1. 冲压 2. 弯管 3. 焊接", required=false, maxLength=10, fixed=0, order=9)
	@JSONField(name = "cproducttechsn")
	public java.lang.String getCProductTechSn() {
		return getStr("cProductTechSn");
	}

	/**
	 * 合并要员（人）
	 */
	public M setIMergedNum(java.lang.Integer iMergedNum) {
		set("iMergedNum", iMergedNum);
		return (M)this;
	}

	/**
	 * 合并要员（人）
	 */
	@JBoltField(name="imergednum" ,columnName="iMergedNum",type="Integer", remark="合并要员（人）", required=false, maxLength=10, fixed=0, order=10)
	@JSONField(name = "imergednum")
	public java.lang.Integer getIMergedNum() {
		return getInt("iMergedNum");
	}

	/**
	 * 要员（人）
	 */
	public M setIMergeRate(java.math.BigDecimal iMergeRate) {
		set("iMergeRate", iMergeRate);
		return (M)this;
	}

	/**
	 * 要员（人）
	 */
	@JBoltField(name="imergerate" ,columnName="iMergeRate",type="BigDecimal", remark="要员（人）", required=false, maxLength=5, fixed=4, order=11)
	@JSONField(name = "imergerate")
	public java.math.BigDecimal getIMergeRate() {
		return getBigDecimal("iMergeRate");
	}

	/**
	 * 合并工时（秒）
	 */
	public M setIMergeSecs(java.lang.Integer iMergeSecs) {
		set("iMergeSecs", iMergeSecs);
		return (M)this;
	}

	/**
	 * 合并工时（秒）
	 */
	@JBoltField(name="imergesecs" ,columnName="iMergeSecs",type="Integer", remark="合并工时（秒）", required=false, maxLength=10, fixed=0, order=12)
	@JSONField(name = "imergesecs")
	public java.lang.Integer getIMergeSecs() {
		return getInt("iMergeSecs");
	}

	/**
	 * 单工时（秒）
	 */
	public M setISecs(java.lang.Integer iSecs) {
		set("iSecs", iSecs);
		return (M)this;
	}

	/**
	 * 单工时（秒）
	 */
	@JBoltField(name="isecs" ,columnName="iSecs",type="Integer", remark="单工时（秒）", required=false, maxLength=10, fixed=0, order=13)
	@JSONField(name = "isecs")
	public java.lang.Integer getISecs() {
		return getInt("iSecs");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=14)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

	/**
	 * 创建人
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=15)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCCreateName(java.lang.String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=16)
	@JSONField(name = "ccreatename")
	public java.lang.String getCCreateName() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDCreateTime(java.util.Date dCreateTime) {
		set("dCreateTime", dCreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=17)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 是否启用
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用", required=true, maxLength=1, fixed=0, order=18)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
	}

}

