package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户订单-周间客户订单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWeekOrderD<M extends BaseWeekOrderD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**周间客户订单主表ID*/
    public static final String IWEEKORDERMID = "iWeekOrderMid";
    /**计划代码*/
    public static final String CPLANCODE = "cPlanCode";
    /**收货地点ID*/
    public static final String ICUSTOMERADDRID = "iCustomerAddrId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**数量*/
    public static final String IQTY = "iQty";
    /**计划到货日期*/
    public static final String DPLANAOGDATE = "dPlanAogDate";
    /**传票号*/
    public static final String CCODE = "cCode";
    /**版本号*/
    public static final String CVERSION = "cVersion";
    /**备注*/
    public static final String CMEMO = "cMemo";
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
	 * 周间客户订单主表ID
	 */
	public M setIWeekOrderMid(java.lang.Long iWeekOrderMid) {
		set("iWeekOrderMid", iWeekOrderMid);
		return (M)this;
	}

	/**
	 * 周间客户订单主表ID
	 */
	@JBoltField(name="iweekordermid" ,columnName="iWeekOrderMid",type="Long", remark="周间客户订单主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iweekordermid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWeekOrderMid() {
		return getLong("iWeekOrderMid");
	}

	/**
	 * 计划代码
	 */
	public M setCPlanCode(java.lang.Boolean cPlanCode) {
		set("cPlanCode", cPlanCode);
		return (M)this;
	}

	/**
	 * 计划代码
	 */
	@JBoltField(name="cplancode" ,columnName="cPlanCode",type="Boolean", remark="计划代码", required=false, maxLength=1, fixed=0, order=3)
	@JSONField(name = "cplancode")
	public java.lang.Boolean getCPlanCode() {
		return getBoolean("cPlanCode");
	}

	/**
	 * 收货地点ID
	 */
	public M setICustomerAddrId(java.lang.Long iCustomerAddrId) {
		set("iCustomerAddrId", iCustomerAddrId);
		return (M)this;
	}

	/**
	 * 收货地点ID
	 */
	@JBoltField(name="icustomeraddrid" ,columnName="iCustomerAddrId",type="Long", remark="收货地点ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "icustomeraddrid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerAddrId() {
		return getLong("iCustomerAddrId");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
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
	@JBoltField(name="iqty" ,columnName="iQty",type="Integer", remark="数量", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "iqty")
	public java.lang.Integer getIQty() {
		return getInt("iQty");
	}

	/**
	 * 计划到货日期
	 */
	public M setDPlanAogDate(java.util.Date dPlanAogDate) {
		set("dPlanAogDate", dPlanAogDate);
		return (M)this;
	}

	/**
	 * 计划到货日期
	 */
	@JBoltField(name="dplanaogdate" ,columnName="dPlanAogDate",type="Date", remark="计划到货日期", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "dplanaogdate")
	public java.util.Date getDPlanAogDate() {
		return getDate("dPlanAogDate");
	}

	/**
	 * 传票号
	 */
	public M setCCode(java.lang.Boolean cCode) {
		set("cCode", cCode);
		return (M)this;
	}

	/**
	 * 传票号
	 */
	@JBoltField(name="ccode" ,columnName="cCode",type="Boolean", remark="传票号", required=true, maxLength=1, fixed=0, order=8)
	@JSONField(name = "ccode")
	public java.lang.Boolean getCCode() {
		return getBoolean("cCode");
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
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="版本号", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=10)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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

