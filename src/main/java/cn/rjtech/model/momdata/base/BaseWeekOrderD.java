package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户订单-周间客户订单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
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
    /**存货编码*/
    public static final String CINVCODE = "cInvCode";
    /**存货名称*/
    public static final String CINVNAME = "cInvName";
    /**客户部番*/
    public static final String CINVCODE1 = "cInvCode1";
    /**部品名称*/
    public static final String CINVNAME1 = "cInvName1";
    /**规格*/
    public static final String CINVSTD = "cInvStd";
    /**收货地点*/
    public static final String CDISTRICTNAME = "cDistrictName";
    /**计划到货时间*/
    public static final String DPLANAOGTIME = "dPlanAogTime";
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
	public M setCPlanCode(java.lang.String cPlanCode) {
		set("cPlanCode", cPlanCode);
		return (M)this;
	}

	/**
	 * 计划代码
	 */
	@JBoltField(name="cplancode" ,columnName="cPlanCode",type="String", remark="计划代码", required=false, maxLength=40, fixed=0, order=3)
	@JSONField(name = "cplancode")
	public java.lang.String getCPlanCode() {
		return getStr("cPlanCode");
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
	@JBoltField(name="icustomeraddrid" ,columnName="iCustomerAddrId",type="Long", remark="收货地点ID", required=false, maxLength=19, fixed=0, order=4)
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
	public M setCCode(java.lang.String cCode) {
		set("cCode", cCode);
		return (M)this;
	}

	/**
	 * 传票号
	 */
	@JBoltField(name="ccode" ,columnName="cCode",type="String", remark="传票号", required=true, maxLength=40, fixed=0, order=8)
	@JSONField(name = "ccode")
	public java.lang.String getCCode() {
		return getStr("cCode");
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

	/**
	 * 存货编码
	 */
	public M setCInvCode(java.lang.String cInvCode) {
		set("cInvCode", cInvCode);
		return (M)this;
	}

	/**
	 * 存货编码
	 */
	@JBoltField(name="cinvcode" ,columnName="cInvCode",type="String", remark="存货编码", required=false, maxLength=60, fixed=0, order=12)
	@JSONField(name = "cinvcode")
	public java.lang.String getCInvCode() {
		return getStr("cInvCode");
	}

	/**
	 * 存货名称
	 */
	public M setCInvName(java.lang.String cInvName) {
		set("cInvName", cInvName);
		return (M)this;
	}

	/**
	 * 存货名称
	 */
	@JBoltField(name="cinvname" ,columnName="cInvName",type="String", remark="存货名称", required=false, maxLength=255, fixed=0, order=13)
	@JSONField(name = "cinvname")
	public java.lang.String getCInvName() {
		return getStr("cInvName");
	}

	/**
	 * 客户部番
	 */
	public M setCInvCode1(java.lang.String cInvCode1) {
		set("cInvCode1", cInvCode1);
		return (M)this;
	}

	/**
	 * 客户部番
	 */
	@JBoltField(name="cinvcode1" ,columnName="cInvCode1",type="String", remark="客户部番", required=false, maxLength=255, fixed=0, order=14)
	@JSONField(name = "cinvcode1")
	public java.lang.String getCInvCode1() {
		return getStr("cInvCode1");
	}

	/**
	 * 部品名称
	 */
	public M setCInvName1(java.lang.String cInvName1) {
		set("cInvName1", cInvName1);
		return (M)this;
	}

	/**
	 * 部品名称
	 */
	@JBoltField(name="cinvname1" ,columnName="cInvName1",type="String", remark="部品名称", required=false, maxLength=255, fixed=0, order=15)
	@JSONField(name = "cinvname1")
	public java.lang.String getCInvName1() {
		return getStr("cInvName1");
	}

	/**
	 * 规格
	 */
	public M setCInvStd(java.lang.String cInvStd) {
		set("cInvStd", cInvStd);
		return (M)this;
	}

	/**
	 * 规格
	 */
	@JBoltField(name="cinvstd" ,columnName="cInvStd",type="String", remark="规格", required=false, maxLength=255, fixed=0, order=16)
	@JSONField(name = "cinvstd")
	public java.lang.String getCInvStd() {
		return getStr("cInvStd");
	}

	/**
	 * 收货地点
	 */
	public M setCDistrictName(java.lang.String cDistrictName) {
		set("cDistrictName", cDistrictName);
		return (M)this;
	}

	/**
	 * 收货地点
	 */
	@JBoltField(name="cdistrictname" ,columnName="cDistrictName",type="String", remark="收货地点", required=false, maxLength=200, fixed=0, order=17)
	@JSONField(name = "cdistrictname")
	public java.lang.String getCDistrictName() {
		return getStr("cDistrictName");
	}

	/**
	 * 计划到货时间
	 */
	public M setCPlanAogTime(java.lang.String dPlanAogTime) {
		set("dPlanAogTime", dPlanAogTime);
		return (M)this;
	}

	/**
	 * 计划到货时间
	 */
	@JBoltField(name="dplanaogtime" ,columnName="dPlanAogTime",type="String", remark="计划到货时间", required=false, maxLength=10, fixed=0, order=18)
	@JSONField(name = "dplanaogtime")
	public java.lang.String getCPlanAogTime() {
		return getStr("dPlanAogTime");
	}

}

