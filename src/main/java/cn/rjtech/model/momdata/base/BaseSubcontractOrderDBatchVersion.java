package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 采购/委外管理-采购现品票版本记录
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSubcontractOrderDBatchVersion<M extends BaseSubcontractOrderDBatchVersion<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**委外订单主表ID*/
    public static final String ISUBCONTRACTORDERMID = "iSubcontractOrderMid";
    /**委外订单现票票记录ID*/
    public static final String ISUBCONTRACTORDERDBATCHID = "iSubcontractOrderdBatchId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**计划到货日期*/
    public static final String DPLANDATE = "dPlanDate";
    /**传票号*/
    public static final String CBARCODE = "cBarcode";
    /**版本号*/
    public static final String CVERSION = "cVersion";
    /**数量*/
    public static final String IQTY = "iQty";
    /**替代传票*/
    public static final String CSOURCEBARCODE = "cSourceBarcode";
    /**原版本号*/
    public static final String CSOURCEVERSION = "cSourceVersion";
    /**原数量*/
    public static final String ISOURCEQTY = "iSourceQty";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
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
	 * 委外订单主表ID
	 */
	public M setISubcontractOrderMid(java.lang.Long iSubcontractOrderMid) {
		set("iSubcontractOrderMid", iSubcontractOrderMid);
		return (M)this;
	}

	/**
	 * 委外订单主表ID
	 */
	@JBoltField(name="isubcontractordermid" ,columnName="iSubcontractOrderMid",type="Long", remark="委外订单主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "isubcontractordermid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISubcontractOrderMid() {
		return getLong("iSubcontractOrderMid");
	}

	/**
	 * 委外订单现票票记录ID
	 */
	public M setISubcontractOrderdBatchId(java.lang.Long iSubcontractOrderdBatchId) {
		set("iSubcontractOrderdBatchId", iSubcontractOrderdBatchId);
		return (M)this;
	}

	/**
	 * 委外订单现票票记录ID
	 */
	@JBoltField(name="isubcontractorderdbatchid" ,columnName="iSubcontractOrderdBatchId",type="Long", remark="委外订单现票票记录ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "isubcontractorderdbatchid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISubcontractOrderdBatchId() {
		return getLong("iSubcontractOrderdBatchId");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 计划到货日期
	 */
	public M setDPlanDate(java.util.Date dPlanDate) {
		set("dPlanDate", dPlanDate);
		return (M)this;
	}

	/**
	 * 计划到货日期
	 */
	@JBoltField(name="dplandate" ,columnName="dPlanDate",type="Date", remark="计划到货日期", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "dplandate")
	public java.util.Date getDPlanDate() {
		return getDate("dPlanDate");
	}

	/**
	 * 传票号
	 */
	public M setCBarcode(java.lang.String cBarcode) {
		set("cBarcode", cBarcode);
		return (M)this;
	}

	/**
	 * 传票号
	 */
	@JBoltField(name="cbarcode" ,columnName="cBarcode",type="String", remark="传票号", required=true, maxLength=100, fixed=0, order=6)
	@JSONField(name = "cbarcode")
	public java.lang.String getCBarcode() {
		return getStr("cBarcode");
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
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="版本号", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
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
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="数量", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 替代传票
	 */
	public M setCSourceBarcode(java.lang.String cSourceBarcode) {
		set("cSourceBarcode", cSourceBarcode);
		return (M)this;
	}

	/**
	 * 替代传票
	 */
	@JBoltField(name="csourcebarcode" ,columnName="cSourceBarcode",type="String", remark="替代传票", required=false, maxLength=100, fixed=0, order=9)
	@JSONField(name = "csourcebarcode")
	public java.lang.String getCSourceBarcode() {
		return getStr("cSourceBarcode");
	}

	/**
	 * 原版本号
	 */
	public M setCSourceVersion(java.lang.String cSourceVersion) {
		set("cSourceVersion", cSourceVersion);
		return (M)this;
	}

	/**
	 * 原版本号
	 */
	@JBoltField(name="csourceversion" ,columnName="cSourceVersion",type="String", remark="原版本号", required=false, maxLength=10, fixed=0, order=10)
	@JSONField(name = "csourceversion")
	public java.lang.String getCSourceVersion() {
		return getStr("cSourceVersion");
	}

	/**
	 * 原数量
	 */
	public M setISourceQty(java.math.BigDecimal iSourceQty) {
		set("iSourceQty", iSourceQty);
		return (M)this;
	}

	/**
	 * 原数量
	 */
	@JBoltField(name="isourceqty" ,columnName="iSourceQty",type="BigDecimal", remark="原数量", required=false, maxLength=10, fixed=0, order=11)
	@JSONField(name = "isourceqty")
	public java.math.BigDecimal getISourceQty() {
		return getBigDecimal("iSourceQty");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=12)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=13)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=14)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

}

