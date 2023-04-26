package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 采购/委外管理-采购现品票
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSubcontractOrderDBatch<M extends BaseSubcontractOrderDBatch<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**委外订单明细ID*/
    public static final String ISUBCONTRACTORDERDID = "iSubcontractOrderDid";
    /**存货ID*/
    public static final String IINVENTORYID = "iinventoryId";
    /**计划到货日期*/
    public static final String DPLANDATE = "dPlanDate";
    /**现品票（条码）*/
    public static final String CBARCODE = "cBarcode";
    /**版本*/
    public static final String CVERSION = "cVersion";
    /**数量*/
    public static final String IQTY = "iQty";
    /**是否生效：0. 否  1. 是*/
    public static final String ISEFFECTIVE = "isEffective";
    /**来源id*/
    public static final String CSOURCELD = "cSourceld";
    /**来源条码*/
    public static final String CSOURCEBARCODE = "cSourceBarcode";
    /**拆分次数*/
    public static final String CSEQ = "cSeq";
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
	 * 委外订单明细ID
	 */
	public M setISubcontractOrderDid(java.lang.Long iSubcontractOrderDid) {
		set("iSubcontractOrderDid", iSubcontractOrderDid);
		return (M)this;
	}

	/**
	 * 委外订单明细ID
	 */
	@JBoltField(name="isubcontractorderdid" ,columnName="iSubcontractOrderDid",type="Long", remark="委外订单明细ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "isubcontractorderdid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISubcontractOrderDid() {
		return getLong("iSubcontractOrderDid");
	}

	/**
	 * 存货ID
	 */
	public M setIinventoryId(java.lang.Long iinventoryId) {
		set("iinventoryId", iinventoryId);
		return (M)this;
	}

	/**
	 * 存货ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iinventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIinventoryId() {
		return getLong("iinventoryId");
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
	@JBoltField(name="dplandate" ,columnName="dPlanDate",type="Date", remark="计划到货日期", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "dplandate")
	public java.util.Date getDPlanDate() {
		return getDate("dPlanDate");
	}

	/**
	 * 现品票（条码）
	 */
	public M setCBarcode(java.lang.String cBarcode) {
		set("cBarcode", cBarcode);
		return (M)this;
	}

	/**
	 * 现品票（条码）
	 */
	@JBoltField(name="cbarcode" ,columnName="cBarcode",type="String", remark="现品票（条码）", required=true, maxLength=100, fixed=0, order=5)
	@JSONField(name = "cbarcode")
	public java.lang.String getCBarcode() {
		return getStr("cBarcode");
	}

	/**
	 * 版本
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 版本
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="版本", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
	}

	/**
	 * 数量
	 */
	public M setIQty(java.lang.Integer iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="Integer", remark="数量", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "iqty")
	public java.lang.Integer getIQty() {
		return getInt("iQty");
	}

	/**
	 * 是否生效：0. 否  1. 是
	 */
	public M setIsEffective(java.lang.Boolean isEffective) {
		set("isEffective", isEffective);
		return (M)this;
	}

	/**
	 * 是否生效：0. 否  1. 是
	 */
	@JBoltField(name="iseffective" ,columnName="isEffective",type="Boolean", remark="是否生效：0. 否  1. 是", required=true, maxLength=1, fixed=0, order=8)
	@JSONField(name = "iseffective")
	public java.lang.Boolean getIsEffective() {
		return getBoolean("isEffective");
	}

	/**
	 * 来源id
	 */
	public M setCSourceld(java.lang.String cSourceld) {
		set("cSourceld", cSourceld);
		return (M)this;
	}

	/**
	 * 来源id
	 */
	@JBoltField(name="csourceld" ,columnName="cSourceld",type="String", remark="来源id", required=false, maxLength=100, fixed=0, order=9)
	@JSONField(name = "csourceld")
	public java.lang.String getCSourceld() {
		return getStr("cSourceld");
	}

	/**
	 * 来源条码
	 */
	public M setCSourceBarcode(java.lang.String cSourceBarcode) {
		set("cSourceBarcode", cSourceBarcode);
		return (M)this;
	}

	/**
	 * 来源条码
	 */
	@JBoltField(name="csourcebarcode" ,columnName="cSourceBarcode",type="String", remark="来源条码", required=false, maxLength=100, fixed=0, order=10)
	@JSONField(name = "csourcebarcode")
	public java.lang.String getCSourceBarcode() {
		return getStr("cSourceBarcode");
	}

	/**
	 * 拆分次数
	 */
	public M setCSeq(java.lang.String cSeq) {
		set("cSeq", cSeq);
		return (M)this;
	}

	/**
	 * 拆分次数
	 */
	@JBoltField(name="cseq" ,columnName="cSeq",type="String", remark="拆分次数", required=false, maxLength=50, fixed=0, order=11)
	@JSONField(name = "cseq")
	public java.lang.String getCSeq() {
		return getStr("cSeq");
	}

}

