package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 出货管理-取货计划明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseRcvPlanD<M extends BaseRcvPlanD<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**取货计划主表ID*/
    public static final String IRCVPLANMID = "iRcvPlanMid";
    /**取货车次号*/
    public static final String CCARNO = "cCarNo";
    /**计划代号*/
    public static final String CPLANCODE = "cPlanCode";
    /**取货日期*/
    public static final String CRCVDATE = "cRcvDate";
    /**取货时间*/
    public static final String CRCVTIME = "cRcvTime";
    /**传票号*/
    public static final String CBARCODE = "cBarcode";
    /**传票版本号*/
    public static final String CVERSION = "cVersion";
    /**收货地点*/
    public static final String CADDRESS = "cAddress";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**计划数量*/
    public static final String IQTY = "iQty";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "IsDeleted";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**存货编码*/
    public static final String CINVCODE = "cInvCode";
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
	 * 取货计划主表ID
	 */
	public M setIRcvPlanMid(java.lang.Long iRcvPlanMid) {
		set("iRcvPlanMid", iRcvPlanMid);
		return (M)this;
	}

	/**
	 * 取货计划主表ID
	 */
	@JBoltField(name="ircvplanmid" ,columnName="iRcvPlanMid",type="Long", remark="取货计划主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "ircvplanmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIRcvPlanMid() {
		return getLong("iRcvPlanMid");
	}

	/**
	 * 取货车次号
	 */
	public M setCCarNo(java.lang.String cCarNo) {
		set("cCarNo", cCarNo);
		return (M)this;
	}

	/**
	 * 取货车次号
	 */
	@JBoltField(name="ccarno" ,columnName="cCarNo",type="String", remark="取货车次号", required=true, maxLength=30, fixed=0, order=3)
	@JSONField(name = "ccarno")
	public java.lang.String getCCarNo() {
		return getStr("cCarNo");
	}

	/**
	 * 计划代号
	 */
	public M setCPlanCode(java.lang.String cPlanCode) {
		set("cPlanCode", cPlanCode);
		return (M)this;
	}

	/**
	 * 计划代号
	 */
	@JBoltField(name="cplancode" ,columnName="cPlanCode",type="String", remark="计划代号", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "cplancode")
	public java.lang.String getCPlanCode() {
		return getStr("cPlanCode");
	}

	/**
	 * 取货日期
	 */
	public M setCRcvDate(java.util.Date cRcvDate) {
		set("cRcvDate", cRcvDate);
		return (M)this;
	}

	/**
	 * 取货日期
	 */
	@JBoltField(name="crcvdate" ,columnName="cRcvDate",type="Date", remark="取货日期", required=false, maxLength=23, fixed=3, order=5)
	@JSONField(name = "crcvdate")
	public java.util.Date getCRcvDate() {
		return getDate("cRcvDate");
	}

	/**
	 * 取货时间
	 */
	public M setCRcvTime(java.util.Date cRcvTime) {
		set("cRcvTime", cRcvTime);
		return (M)this;
	}

	/**
	 * 取货时间
	 */
	@JBoltField(name="crcvtime" ,columnName="cRcvTime",type="Date", remark="取货时间", required=false, maxLength=23, fixed=3, order=6)
	@JSONField(name = "crcvtime")
	public java.util.Date getCRcvTime() {
		return getDate("cRcvTime");
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
	@JBoltField(name="cbarcode" ,columnName="cBarcode",type="String", remark="传票号", required=false, maxLength=30, fixed=0, order=7)
	@JSONField(name = "cbarcode")
	public java.lang.String getCBarcode() {
		return getStr("cBarcode");
	}

	/**
	 * 传票版本号
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 传票版本号
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="传票版本号", required=false, maxLength=10, fixed=0, order=8)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
	}

	/**
	 * 收货地点
	 */
	public M setCAddress(java.lang.String cAddress) {
		set("cAddress", cAddress);
		return (M)this;
	}

	/**
	 * 收货地点
	 */
	@JBoltField(name="caddress" ,columnName="cAddress",type="String", remark="收货地点", required=false, maxLength=50, fixed=0, order=9)
	@JSONField(name = "caddress")
	public java.lang.String getCAddress() {
		return getStr("cAddress");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=false, maxLength=19, fixed=0, order=10)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 计划数量
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 计划数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="计划数量", required=false, maxLength=18, fixed=2, order=11)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean IsDeleted) {
		set("IsDeleted", IsDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=12)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=13)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=14)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
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
	@JBoltField(name="cinvcode" ,columnName="cInvCode",type="String", remark="存货编码", required=false, maxLength=60, fixed=0, order=15)
	@JSONField(name = "cinvcode")
	public java.lang.String getCInvCode() {
		return getStr("cInvCode");
	}

}

