package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 生产订单-成品现品票明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoMoinvbatch<M extends BaseMoMoinvbatch<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**生产订单ID*/
    public static final String IMODOCID = "iMoDocId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**包装数量*/
    public static final String IPKGQTY = "iPkgQty";
    /**序号*/
    public static final String ISEQ = "iSeq";
    /**版本号*/
    public static final String CVERSION = "cVersion";
    /**现品票*/
    public static final String CBARCODE = "cBarcode";
    /**数量*/
    public static final String IQTY = "iQty";
    /**打印状态;1. 未打印 2. 已打印*/
    public static final String IPRINTSTATUS = "iPrintStatus";
    /**状态;0. 未报工 1. 已报工*/
    public static final String ISTATUS = "iStatus";
    /**是否有效;0. 失效 1. 有效*/
    public static final String ISEFFECTIVE = "isEffective";
    /**创建人ID*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人ID*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**删除状态;0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
    /**完整条码（现品票+版本号）*/
    public static final String CCOMPLETEBARCODE = "cCompleteBarcode";
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
	 * 组织ID
	 */
	public M setIOrgId(java.lang.Long iOrgId) {
		set("iOrgId", iOrgId);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iorgid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIOrgId() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCOrgCode(java.lang.String cOrgCode) {
		set("cOrgCode", cOrgCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=40, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public java.lang.String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCOrgName(java.lang.String cOrgName) {
		set("cOrgName", cOrgName);
		return (M)this;
	}

	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=40, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 生产订单ID
	 */
	public M setIMoDocId(java.lang.Long iMoDocId) {
		set("iMoDocId", iMoDocId);
		return (M)this;
	}

	/**
	 * 生产订单ID
	 */
	@JBoltField(name="imodocid" ,columnName="iMoDocId",type="Long", remark="生产订单ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "imodocid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoDocId() {
		return getLong("iMoDocId");
	}

	/**
	 * 存货ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 存货ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 包装数量
	 */
	public M setIPkgQty(java.lang.Integer iPkgQty) {
		set("iPkgQty", iPkgQty);
		return (M)this;
	}

	/**
	 * 包装数量
	 */
	@JBoltField(name="ipkgqty" ,columnName="iPkgQty",type="Integer", remark="包装数量", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "ipkgqty")
	public java.lang.Integer getIPkgQty() {
		return getInt("iPkgQty");
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
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="序号", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 版本号
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 版本号
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="版本号", required=true, maxLength=4, fixed=0, order=9)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
	}

	/**
	 * 现品票
	 */
	public M setCBarcode(java.lang.String cBarcode) {
		set("cBarcode", cBarcode);
		return (M)this;
	}

	/**
	 * 现品票
	 */
	@JBoltField(name="cbarcode" ,columnName="cBarcode",type="String", remark="现品票", required=true, maxLength=40, fixed=0, order=10)
	@JSONField(name = "cbarcode")
	public java.lang.String getCBarcode() {
		return getStr("cBarcode");
	}

	/**
	 * 数量
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="数量", required=true, maxLength=24, fixed=6, order=11)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 打印状态;1. 未打印 2. 已打印
	 */
	public M setIPrintStatus(java.lang.Integer iPrintStatus) {
		set("iPrintStatus", iPrintStatus);
		return (M)this;
	}

	/**
	 * 打印状态;1. 未打印 2. 已打印
	 */
	@JBoltField(name="iprintstatus" ,columnName="iPrintStatus",type="Integer", remark="打印状态;1. 未打印 2. 已打印", required=true, maxLength=10, fixed=0, order=12)
	@JSONField(name = "iprintstatus")
	public java.lang.Integer getIPrintStatus() {
		return getInt("iPrintStatus");
	}

	/**
	 * 状态;0. 未报工 1. 已报工
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 状态;0. 未报工 1. 已报工
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="状态;0. 未报工 1. 已报工", required=true, maxLength=10, fixed=0, order=13)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
	}

	/**
	 * 是否有效;0. 失效 1. 有效
	 */
	public M setIsEffective(java.lang.Boolean isEffective) {
		set("isEffective", isEffective);
		return (M)this;
	}

	/**
	 * 是否有效;0. 失效 1. 有效
	 */
	@JBoltField(name="iseffective" ,columnName="isEffective",type="Boolean", remark="是否有效;0. 失效 1. 有效", required=true, maxLength=1, fixed=0, order=14)
	@JSONField(name = "iseffective")
	public java.lang.Boolean getIsEffective() {
		return getBoolean("isEffective");
	}

	/**
	 * 创建人ID
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人ID
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=15)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=40, fixed=0, order=16)
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
	 * 更新人ID
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人ID
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=18)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCUpdateName(java.lang.String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 更新人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=40, fixed=0, order=19)
	@JSONField(name = "cupdatename")
	public java.lang.String getCUpdateName() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDUpdateTime(java.util.Date dUpdateTime) {
		set("dUpdateTime", dUpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=20)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 删除状态;0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态;0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态;0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=21)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 完整条码（现品票+版本号）
	 */
	public M setCCompleteBarcode(java.lang.String cCompleteBarcode) {
		set("cCompleteBarcode", cCompleteBarcode);
		return (M)this;
	}

	/**
	 * 完整条码（现品票+版本号）
	 */
	@JBoltField(name="ccompletebarcode" ,columnName="cCompleteBarcode",type="String", remark="完整条码（现品票+版本号）", required=false, maxLength=100, fixed=0, order=22)
	@JSONField(name = "ccompletebarcode")
	public java.lang.String getCCompleteBarcode() {
		return getStr("cCompleteBarcode");
	}

}

