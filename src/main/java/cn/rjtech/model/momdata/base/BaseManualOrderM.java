package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户订单-手配订单主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseManualOrderM<M extends BaseManualOrderM<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**客户ID*/
    public static final String ICUSTOMERID = "iCustomerId";
    /**订单号*/
    public static final String CORDERNO = "cOrderNo";
    /**年份*/
    public static final String IYEAR = "iYear";
    /**月份*/
    public static final String IMONTH = "iMonth";
    /**订单日期*/
    public static final String DORDERDATE = "dOrderDate";
    /**业务类型*/
    public static final String IBUSTYPE = "iBusType";
    /**销售类型*/
    public static final String ISALETYPEID = "iSaleTypeId";
    /**付款条件*/
    public static final String CPAYMENTTERM = "cPaymentTerm";
    /**销售部门ID*/
    public static final String IDEPARTMENTID = "iDepartmentId";
    /**业务员ID;人员ID*/
    public static final String IBUSPERSONID = "iBusPersonId";
    /**税率（小数）*/
    public static final String ITAXRATE = "iTaxRate";
    /**币种*/
    public static final String CCURRENCY = "cCurrency";
    /**汇率*/
    public static final String IEXCHANGERATE = "iExchangeRate";
    /**扩展字段1*/
    public static final String CDEFINE1 = "cDefine1";
    /**扩展字段2*/
    public static final String CDEFINE2 = "cDefine2";
    /**扩展字段3*/
    public static final String CDEFINE3 = "cDefine3";
    /**扩展字段4*/
    public static final String CDEFINE4 = "cDefine4";
    /**扩展字段5*/
    public static final String CDEFINE5 = "cDefine5";
    /**扩展字段6*/
    public static final String CDEFINE6 = "cDefine6";
    /**扩展字段7*/
    public static final String CDEFINE7 = "cDefine7";
    /**扩展字段8*/
    public static final String CDEFINE8 = "cDefine8";
    /**扩展字段9*/
    public static final String CDEFINE9 = "cDefine9";
    /**扩展字段10*/
    public static final String CDEFINE10 = "cDefine10";
    /**扩展字段11*/
    public static final String CDEFINE11 = "cDefine11";
    /**扩展字段12*/
    public static final String CDEFINE12 = "cDefine12";
    /**扩展字段13*/
    public static final String CDEFINE13 = "cDefine13";
    /**扩展字段14*/
    public static final String CDEFINE14 = "cDefine14";
    /**扩展字段15*/
    public static final String CDEFINE15 = "cDefine15";
    /**扩展字段16*/
    public static final String IDEFINE16 = "iDefine16";
    /**扩展字段17*/
    public static final String IDEFINE17 = "iDefine17";
    /**扩展字段18*/
    public static final String IDEFINE18 = "iDefine18";
    /**扩展字段19*/
    public static final String IDEFINE19 = "iDefine19";
    /**扩展字段20*/
    public static final String IDEFINE20 = "iDefine20";
    /**扩展字段21*/
    public static final String IDEFINE21 = "iDefine21";
    /**扩展字段22*/
    public static final String IDEFINE22 = "iDefine22";
    /**扩展字段23*/
    public static final String IDEFINE23 = "iDefine23";
    /**扩展字段24*/
    public static final String IDEFINE24 = "iDefine24";
    /**扩展字段25*/
    public static final String IDEFINE25 = "iDefine25";
    /**扩展字段26*/
    public static final String DDEFINE26 = "dDefine26";
    /**扩展字段27*/
    public static final String DDEFINE27 = "dDefine27";
    /**扩展字段28*/
    public static final String DDEFINE28 = "dDefine28";
    /**扩展字段29*/
    public static final String DDEFINE29 = "dDefine29";
    /**扩展字段30*/
    public static final String DDEFINE30 = "dDefine30";
    /**订单状态;1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已关闭*/
    public static final String IORDERSTATUS = "iOrderStatus";
    /**审核状态;0. 未审批 1. 待审批 2. 审批通过 3. 审批不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
    /**备注*/
    public static final String CMEMO = "cMemo";
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
    public static final String ISDELETED = "IsDeleted";
    /**审批方式;1. 审批状态 2. 审批流*/
    public static final String IAUDITWAY = "iAuditWay";
    /**提审时间*/
    public static final String DSUBMITTIME = "dSubmitTime";
    /**推送单据到：1. U8, 默认为1*/
    public static final String IPUSHTO = "iPushTo";
    /**推送单号*/
    public static final String CDOCNO = "cDocNo";
    /**客户编码*/
    public static final String CCUSCODE = "cCusCode";
    /**客户名称*/
    public static final String CCUSNAME = "cCusName";
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
	 * 客户ID
	 */
	public M setICustomerId(java.lang.Long iCustomerId) {
		set("iCustomerId", iCustomerId);
		return (M)this;
	}

	/**
	 * 客户ID
	 */
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
	}

	/**
	 * 订单号
	 */
	public M setCOrderNo(java.lang.String cOrderNo) {
		set("cOrderNo", cOrderNo);
		return (M)this;
	}

	/**
	 * 订单号
	 */
	@JBoltField(name="corderno" ,columnName="cOrderNo",type="String", remark="订单号", required=true, maxLength=50, fixed=0, order=6)
	@JSONField(name = "corderno")
	public java.lang.String getCOrderNo() {
		return getStr("cOrderNo");
	}

	/**
	 * 年份
	 */
	public M setIYear(java.lang.Integer iYear) {
		set("iYear", iYear);
		return (M)this;
	}

	/**
	 * 年份
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年份", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "iyear")
	public java.lang.Integer getIYear() {
		return getInt("iYear");
	}

	/**
	 * 月份
	 */
	public M setIMonth(java.lang.Integer iMonth) {
		set("iMonth", iMonth);
		return (M)this;
	}

	/**
	 * 月份
	 */
	@JBoltField(name="imonth" ,columnName="iMonth",type="Integer", remark="月份", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "imonth")
	public java.lang.Integer getIMonth() {
		return getInt("iMonth");
	}

	/**
	 * 订单日期
	 */
	public M setDOrderDate(java.util.Date dOrderDate) {
		set("dOrderDate", dOrderDate);
		return (M)this;
	}

	/**
	 * 订单日期
	 */
	@JBoltField(name="dorderdate" ,columnName="dOrderDate",type="Date", remark="订单日期", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "dorderdate")
	public java.util.Date getDOrderDate() {
		return getDate("dOrderDate");
	}

	/**
	 * 业务类型
	 */
	public M setIBusType(java.lang.Integer iBusType) {
		set("iBusType", iBusType);
		return (M)this;
	}

	/**
	 * 业务类型
	 */
	@JBoltField(name="ibustype" ,columnName="iBusType",type="Integer", remark="业务类型", required=false, maxLength=10, fixed=0, order=10)
	@JSONField(name = "ibustype")
	public java.lang.Integer getIBusType() {
		return getInt("iBusType");
	}

	/**
	 * 销售类型
	 */
	public M setISaleTypeId(java.lang.Long iSaleTypeId) {
		set("iSaleTypeId", iSaleTypeId);
		return (M)this;
	}

	/**
	 * 销售类型
	 */
	@JBoltField(name="isaletypeid" ,columnName="iSaleTypeId",type="Long", remark="销售类型", required=false, maxLength=19, fixed=0, order=11)
	@JSONField(name = "isaletypeid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISaleTypeId() {
		return getLong("iSaleTypeId");
	}

	/**
	 * 付款条件
	 */
	public M setCPaymentTerm(java.lang.String cPaymentTerm) {
		set("cPaymentTerm", cPaymentTerm);
		return (M)this;
	}

	/**
	 * 付款条件
	 */
	@JBoltField(name="cpaymentterm" ,columnName="cPaymentTerm",type="String", remark="付款条件", required=false, maxLength=200, fixed=0, order=12)
	@JSONField(name = "cpaymentterm")
	public java.lang.String getCPaymentTerm() {
		return getStr("cPaymentTerm");
	}

	/**
	 * 销售部门ID
	 */
	public M setIDepartmentId(java.lang.Long iDepartmentId) {
		set("iDepartmentId", iDepartmentId);
		return (M)this;
	}

	/**
	 * 销售部门ID
	 */
	@JBoltField(name="idepartmentid" ,columnName="iDepartmentId",type="Long", remark="销售部门ID", required=false, maxLength=19, fixed=0, order=13)
	@JSONField(name = "idepartmentid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDepartmentId() {
		return getLong("iDepartmentId");
	}

	/**
	 * 业务员ID;人员ID
	 */
	public M setIBusPersonId(java.lang.Long iBusPersonId) {
		set("iBusPersonId", iBusPersonId);
		return (M)this;
	}

	/**
	 * 业务员ID;人员ID
	 */
	@JBoltField(name="ibuspersonid" ,columnName="iBusPersonId",type="Long", remark="业务员ID;人员ID", required=false, maxLength=19, fixed=0, order=14)
	@JSONField(name = "ibuspersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIBusPersonId() {
		return getLong("iBusPersonId");
	}

	/**
	 * 税率（小数）
	 */
	public M setITaxRate(java.math.BigDecimal iTaxRate) {
		set("iTaxRate", iTaxRate);
		return (M)this;
	}

	/**
	 * 税率（小数）
	 */
	@JBoltField(name="itaxrate" ,columnName="iTaxRate",type="BigDecimal", remark="税率（小数）", required=false, maxLength=24, fixed=6, order=15)
	@JSONField(name = "itaxrate")
	public java.math.BigDecimal getITaxRate() {
		return getBigDecimal("iTaxRate");
	}

	/**
	 * 币种
	 */
	public M setCCurrency(java.lang.String cCurrency) {
		set("cCurrency", cCurrency);
		return (M)this;
	}

	/**
	 * 币种
	 */
	@JBoltField(name="ccurrency" ,columnName="cCurrency",type="String", remark="币种", required=false, maxLength=10, fixed=0, order=16)
	@JSONField(name = "ccurrency")
	public java.lang.String getCCurrency() {
		return getStr("cCurrency");
	}

	/**
	 * 汇率
	 */
	public M setIExchangeRate(java.math.BigDecimal iExchangeRate) {
		set("iExchangeRate", iExchangeRate);
		return (M)this;
	}

	/**
	 * 汇率
	 */
	@JBoltField(name="iexchangerate" ,columnName="iExchangeRate",type="BigDecimal", remark="汇率", required=false, maxLength=10, fixed=5, order=17)
	@JSONField(name = "iexchangerate")
	public java.math.BigDecimal getIExchangeRate() {
		return getBigDecimal("iExchangeRate");
	}

	/**
	 * 扩展字段1
	 */
	public M setCDefine1(java.lang.String cDefine1) {
		set("cDefine1", cDefine1);
		return (M)this;
	}

	/**
	 * 扩展字段1
	 */
	@JBoltField(name="cdefine1" ,columnName="cDefine1",type="String", remark="扩展字段1", required=false, maxLength=200, fixed=0, order=18)
	@JSONField(name = "cdefine1")
	public java.lang.String getCDefine1() {
		return getStr("cDefine1");
	}

	/**
	 * 扩展字段2
	 */
	public M setCDefine2(java.lang.String cDefine2) {
		set("cDefine2", cDefine2);
		return (M)this;
	}

	/**
	 * 扩展字段2
	 */
	@JBoltField(name="cdefine2" ,columnName="cDefine2",type="String", remark="扩展字段2", required=false, maxLength=200, fixed=0, order=19)
	@JSONField(name = "cdefine2")
	public java.lang.String getCDefine2() {
		return getStr("cDefine2");
	}

	/**
	 * 扩展字段3
	 */
	public M setCDefine3(java.lang.String cDefine3) {
		set("cDefine3", cDefine3);
		return (M)this;
	}

	/**
	 * 扩展字段3
	 */
	@JBoltField(name="cdefine3" ,columnName="cDefine3",type="String", remark="扩展字段3", required=false, maxLength=200, fixed=0, order=20)
	@JSONField(name = "cdefine3")
	public java.lang.String getCDefine3() {
		return getStr("cDefine3");
	}

	/**
	 * 扩展字段4
	 */
	public M setCDefine4(java.lang.String cDefine4) {
		set("cDefine4", cDefine4);
		return (M)this;
	}

	/**
	 * 扩展字段4
	 */
	@JBoltField(name="cdefine4" ,columnName="cDefine4",type="String", remark="扩展字段4", required=false, maxLength=200, fixed=0, order=21)
	@JSONField(name = "cdefine4")
	public java.lang.String getCDefine4() {
		return getStr("cDefine4");
	}

	/**
	 * 扩展字段5
	 */
	public M setCDefine5(java.lang.String cDefine5) {
		set("cDefine5", cDefine5);
		return (M)this;
	}

	/**
	 * 扩展字段5
	 */
	@JBoltField(name="cdefine5" ,columnName="cDefine5",type="String", remark="扩展字段5", required=false, maxLength=200, fixed=0, order=22)
	@JSONField(name = "cdefine5")
	public java.lang.String getCDefine5() {
		return getStr("cDefine5");
	}

	/**
	 * 扩展字段6
	 */
	public M setCDefine6(java.lang.String cDefine6) {
		set("cDefine6", cDefine6);
		return (M)this;
	}

	/**
	 * 扩展字段6
	 */
	@JBoltField(name="cdefine6" ,columnName="cDefine6",type="String", remark="扩展字段6", required=false, maxLength=200, fixed=0, order=23)
	@JSONField(name = "cdefine6")
	public java.lang.String getCDefine6() {
		return getStr("cDefine6");
	}

	/**
	 * 扩展字段7
	 */
	public M setCDefine7(java.lang.String cDefine7) {
		set("cDefine7", cDefine7);
		return (M)this;
	}

	/**
	 * 扩展字段7
	 */
	@JBoltField(name="cdefine7" ,columnName="cDefine7",type="String", remark="扩展字段7", required=false, maxLength=200, fixed=0, order=24)
	@JSONField(name = "cdefine7")
	public java.lang.String getCDefine7() {
		return getStr("cDefine7");
	}

	/**
	 * 扩展字段8
	 */
	public M setCDefine8(java.lang.String cDefine8) {
		set("cDefine8", cDefine8);
		return (M)this;
	}

	/**
	 * 扩展字段8
	 */
	@JBoltField(name="cdefine8" ,columnName="cDefine8",type="String", remark="扩展字段8", required=false, maxLength=200, fixed=0, order=25)
	@JSONField(name = "cdefine8")
	public java.lang.String getCDefine8() {
		return getStr("cDefine8");
	}

	/**
	 * 扩展字段9
	 */
	public M setCDefine9(java.lang.String cDefine9) {
		set("cDefine9", cDefine9);
		return (M)this;
	}

	/**
	 * 扩展字段9
	 */
	@JBoltField(name="cdefine9" ,columnName="cDefine9",type="String", remark="扩展字段9", required=false, maxLength=200, fixed=0, order=26)
	@JSONField(name = "cdefine9")
	public java.lang.String getCDefine9() {
		return getStr("cDefine9");
	}

	/**
	 * 扩展字段10
	 */
	public M setCDefine10(java.lang.String cDefine10) {
		set("cDefine10", cDefine10);
		return (M)this;
	}

	/**
	 * 扩展字段10
	 */
	@JBoltField(name="cdefine10" ,columnName="cDefine10",type="String", remark="扩展字段10", required=false, maxLength=200, fixed=0, order=27)
	@JSONField(name = "cdefine10")
	public java.lang.String getCDefine10() {
		return getStr("cDefine10");
	}

	/**
	 * 扩展字段11
	 */
	public M setCDefine11(java.lang.String cDefine11) {
		set("cDefine11", cDefine11);
		return (M)this;
	}

	/**
	 * 扩展字段11
	 */
	@JBoltField(name="cdefine11" ,columnName="cDefine11",type="String", remark="扩展字段11", required=false, maxLength=500, fixed=0, order=28)
	@JSONField(name = "cdefine11")
	public java.lang.String getCDefine11() {
		return getStr("cDefine11");
	}

	/**
	 * 扩展字段12
	 */
	public M setCDefine12(java.lang.String cDefine12) {
		set("cDefine12", cDefine12);
		return (M)this;
	}

	/**
	 * 扩展字段12
	 */
	@JBoltField(name="cdefine12" ,columnName="cDefine12",type="String", remark="扩展字段12", required=false, maxLength=500, fixed=0, order=29)
	@JSONField(name = "cdefine12")
	public java.lang.String getCDefine12() {
		return getStr("cDefine12");
	}

	/**
	 * 扩展字段13
	 */
	public M setCDefine13(java.lang.String cDefine13) {
		set("cDefine13", cDefine13);
		return (M)this;
	}

	/**
	 * 扩展字段13
	 */
	@JBoltField(name="cdefine13" ,columnName="cDefine13",type="String", remark="扩展字段13", required=false, maxLength=500, fixed=0, order=30)
	@JSONField(name = "cdefine13")
	public java.lang.String getCDefine13() {
		return getStr("cDefine13");
	}

	/**
	 * 扩展字段14
	 */
	public M setCDefine14(java.lang.String cDefine14) {
		set("cDefine14", cDefine14);
		return (M)this;
	}

	/**
	 * 扩展字段14
	 */
	@JBoltField(name="cdefine14" ,columnName="cDefine14",type="String", remark="扩展字段14", required=false, maxLength=500, fixed=0, order=31)
	@JSONField(name = "cdefine14")
	public java.lang.String getCDefine14() {
		return getStr("cDefine14");
	}

	/**
	 * 扩展字段15
	 */
	public M setCDefine15(java.lang.String cDefine15) {
		set("cDefine15", cDefine15);
		return (M)this;
	}

	/**
	 * 扩展字段15
	 */
	@JBoltField(name="cdefine15" ,columnName="cDefine15",type="String", remark="扩展字段15", required=false, maxLength=500, fixed=0, order=32)
	@JSONField(name = "cdefine15")
	public java.lang.String getCDefine15() {
		return getStr("cDefine15");
	}

	/**
	 * 扩展字段16
	 */
	public M setIDefine16(java.math.BigDecimal iDefine16) {
		set("iDefine16", iDefine16);
		return (M)this;
	}

	/**
	 * 扩展字段16
	 */
	@JBoltField(name="idefine16" ,columnName="iDefine16",type="BigDecimal", remark="扩展字段16", required=false, maxLength=24, fixed=6, order=33)
	@JSONField(name = "idefine16")
	public java.math.BigDecimal getIDefine16() {
		return getBigDecimal("iDefine16");
	}

	/**
	 * 扩展字段17
	 */
	public M setIDefine17(java.math.BigDecimal iDefine17) {
		set("iDefine17", iDefine17);
		return (M)this;
	}

	/**
	 * 扩展字段17
	 */
	@JBoltField(name="idefine17" ,columnName="iDefine17",type="BigDecimal", remark="扩展字段17", required=false, maxLength=24, fixed=6, order=34)
	@JSONField(name = "idefine17")
	public java.math.BigDecimal getIDefine17() {
		return getBigDecimal("iDefine17");
	}

	/**
	 * 扩展字段18
	 */
	public M setIDefine18(java.math.BigDecimal iDefine18) {
		set("iDefine18", iDefine18);
		return (M)this;
	}

	/**
	 * 扩展字段18
	 */
	@JBoltField(name="idefine18" ,columnName="iDefine18",type="BigDecimal", remark="扩展字段18", required=false, maxLength=24, fixed=6, order=35)
	@JSONField(name = "idefine18")
	public java.math.BigDecimal getIDefine18() {
		return getBigDecimal("iDefine18");
	}

	/**
	 * 扩展字段19
	 */
	public M setIDefine19(java.math.BigDecimal iDefine19) {
		set("iDefine19", iDefine19);
		return (M)this;
	}

	/**
	 * 扩展字段19
	 */
	@JBoltField(name="idefine19" ,columnName="iDefine19",type="BigDecimal", remark="扩展字段19", required=false, maxLength=24, fixed=6, order=36)
	@JSONField(name = "idefine19")
	public java.math.BigDecimal getIDefine19() {
		return getBigDecimal("iDefine19");
	}

	/**
	 * 扩展字段20
	 */
	public M setIDefine20(java.math.BigDecimal iDefine20) {
		set("iDefine20", iDefine20);
		return (M)this;
	}

	/**
	 * 扩展字段20
	 */
	@JBoltField(name="idefine20" ,columnName="iDefine20",type="BigDecimal", remark="扩展字段20", required=false, maxLength=24, fixed=6, order=37)
	@JSONField(name = "idefine20")
	public java.math.BigDecimal getIDefine20() {
		return getBigDecimal("iDefine20");
	}

	/**
	 * 扩展字段21
	 */
	public M setIDefine21(java.lang.Integer iDefine21) {
		set("iDefine21", iDefine21);
		return (M)this;
	}

	/**
	 * 扩展字段21
	 */
	@JBoltField(name="idefine21" ,columnName="iDefine21",type="Integer", remark="扩展字段21", required=false, maxLength=10, fixed=0, order=38)
	@JSONField(name = "idefine21")
	public java.lang.Integer getIDefine21() {
		return getInt("iDefine21");
	}

	/**
	 * 扩展字段22
	 */
	public M setIDefine22(java.lang.Integer iDefine22) {
		set("iDefine22", iDefine22);
		return (M)this;
	}

	/**
	 * 扩展字段22
	 */
	@JBoltField(name="idefine22" ,columnName="iDefine22",type="Integer", remark="扩展字段22", required=false, maxLength=10, fixed=0, order=39)
	@JSONField(name = "idefine22")
	public java.lang.Integer getIDefine22() {
		return getInt("iDefine22");
	}

	/**
	 * 扩展字段23
	 */
	public M setIDefine23(java.lang.Integer iDefine23) {
		set("iDefine23", iDefine23);
		return (M)this;
	}

	/**
	 * 扩展字段23
	 */
	@JBoltField(name="idefine23" ,columnName="iDefine23",type="Integer", remark="扩展字段23", required=false, maxLength=10, fixed=0, order=40)
	@JSONField(name = "idefine23")
	public java.lang.Integer getIDefine23() {
		return getInt("iDefine23");
	}

	/**
	 * 扩展字段24
	 */
	public M setIDefine24(java.lang.Integer iDefine24) {
		set("iDefine24", iDefine24);
		return (M)this;
	}

	/**
	 * 扩展字段24
	 */
	@JBoltField(name="idefine24" ,columnName="iDefine24",type="Integer", remark="扩展字段24", required=false, maxLength=10, fixed=0, order=41)
	@JSONField(name = "idefine24")
	public java.lang.Integer getIDefine24() {
		return getInt("iDefine24");
	}

	/**
	 * 扩展字段25
	 */
	public M setIDefine25(java.lang.Integer iDefine25) {
		set("iDefine25", iDefine25);
		return (M)this;
	}

	/**
	 * 扩展字段25
	 */
	@JBoltField(name="idefine25" ,columnName="iDefine25",type="Integer", remark="扩展字段25", required=false, maxLength=10, fixed=0, order=42)
	@JSONField(name = "idefine25")
	public java.lang.Integer getIDefine25() {
		return getInt("iDefine25");
	}

	/**
	 * 扩展字段26
	 */
	public M setDDefine26(java.util.Date dDefine26) {
		set("dDefine26", dDefine26);
		return (M)this;
	}

	/**
	 * 扩展字段26
	 */
	@JBoltField(name="ddefine26" ,columnName="dDefine26",type="Date", remark="扩展字段26", required=false, maxLength=23, fixed=3, order=43)
	@JSONField(name = "ddefine26")
	public java.util.Date getDDefine26() {
		return getDate("dDefine26");
	}

	/**
	 * 扩展字段27
	 */
	public M setDDefine27(java.util.Date dDefine27) {
		set("dDefine27", dDefine27);
		return (M)this;
	}

	/**
	 * 扩展字段27
	 */
	@JBoltField(name="ddefine27" ,columnName="dDefine27",type="Date", remark="扩展字段27", required=false, maxLength=23, fixed=3, order=44)
	@JSONField(name = "ddefine27")
	public java.util.Date getDDefine27() {
		return getDate("dDefine27");
	}

	/**
	 * 扩展字段28
	 */
	public M setDDefine28(java.util.Date dDefine28) {
		set("dDefine28", dDefine28);
		return (M)this;
	}

	/**
	 * 扩展字段28
	 */
	@JBoltField(name="ddefine28" ,columnName="dDefine28",type="Date", remark="扩展字段28", required=false, maxLength=23, fixed=3, order=45)
	@JSONField(name = "ddefine28")
	public java.util.Date getDDefine28() {
		return getDate("dDefine28");
	}

	/**
	 * 扩展字段29
	 */
	public M setDDefine29(java.util.Date dDefine29) {
		set("dDefine29", dDefine29);
		return (M)this;
	}

	/**
	 * 扩展字段29
	 */
	@JBoltField(name="ddefine29" ,columnName="dDefine29",type="Date", remark="扩展字段29", required=false, maxLength=23, fixed=3, order=46)
	@JSONField(name = "ddefine29")
	public java.util.Date getDDefine29() {
		return getDate("dDefine29");
	}

	/**
	 * 扩展字段30
	 */
	public M setDDefine30(java.util.Date dDefine30) {
		set("dDefine30", dDefine30);
		return (M)this;
	}

	/**
	 * 扩展字段30
	 */
	@JBoltField(name="ddefine30" ,columnName="dDefine30",type="Date", remark="扩展字段30", required=false, maxLength=23, fixed=3, order=47)
	@JSONField(name = "ddefine30")
	public java.util.Date getDDefine30() {
		return getDate("dDefine30");
	}

	/**
	 * 订单状态;1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已关闭
	 */
	public M setIOrderStatus(java.lang.Integer iOrderStatus) {
		set("iOrderStatus", iOrderStatus);
		return (M)this;
	}

	/**
	 * 订单状态;1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已关闭
	 */
	@JBoltField(name="iorderstatus" ,columnName="iOrderStatus",type="Integer", remark="订单状态;1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已关闭", required=true, maxLength=10, fixed=0, order=48)
	@JSONField(name = "iorderstatus")
	public java.lang.Integer getIOrderStatus() {
		return getInt("iOrderStatus");
	}

	/**
	 * 审核状态;0. 未审批 1. 待审批 2. 审批通过 3. 审批不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态;0. 未审批 1. 待审批 2. 审批通过 3. 审批不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态;0. 未审批 1. 待审批 2. 审批通过 3. 审批不通过", required=true, maxLength=10, fixed=0, order=49)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
	}

	/**
	 * 审核时间
	 */
	public M setDAuditTime(java.util.Date dAuditTime) {
		set("dAuditTime", dAuditTime);
		return (M)this;
	}

	/**
	 * 审核时间
	 */
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=50)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=51)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=52)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=60, fixed=0, order=53)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=54)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=55)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=60, fixed=0, order=56)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=57)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 删除状态;0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean IsDeleted) {
		set("IsDeleted", IsDeleted);
		return (M)this;
	}

	/**
	 * 删除状态;0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态;0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=58)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

	/**
	 * 审批方式;1. 审批状态 2. 审批流
	 */
	public M setIAuditWay(java.lang.Integer iAuditWay) {
		set("iAuditWay", iAuditWay);
		return (M)this;
	}

	/**
	 * 审批方式;1. 审批状态 2. 审批流
	 */
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="审批方式;1. 审批状态 2. 审批流", required=false, maxLength=10, fixed=0, order=59)
	@JSONField(name = "iauditway")
	public java.lang.Integer getIAuditWay() {
		return getInt("iAuditWay");
	}

	/**
	 * 提审时间
	 */
	public M setDSubmitTime(java.util.Date dSubmitTime) {
		set("dSubmitTime", dSubmitTime);
		return (M)this;
	}

	/**
	 * 提审时间
	 */
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=60)
	@JSONField(name = "dsubmittime")
	public java.util.Date getDSubmitTime() {
		return getDate("dSubmitTime");
	}

	/**
	 * 推送单据到：1. U8, 默认为1
	 */
	public M setIPushTo(java.lang.Integer iPushTo) {
		set("iPushTo", iPushTo);
		return (M)this;
	}

	/**
	 * 推送单据到：1. U8, 默认为1
	 */
	@JBoltField(name="ipushto" ,columnName="iPushTo",type="Integer", remark="推送单据到：1. U8, 默认为1", required=false, maxLength=10, fixed=0, order=61)
	@JSONField(name = "ipushto")
	public java.lang.Integer getIPushTo() {
		return getInt("iPushTo");
	}

	/**
	 * 推送单号
	 */
	public M setCDocNo(java.lang.String cDocNo) {
		set("cDocNo", cDocNo);
		return (M)this;
	}

	/**
	 * 推送单号
	 */
	@JBoltField(name="cdocno" ,columnName="cDocNo",type="String", remark="推送单号", required=false, maxLength=40, fixed=0, order=62)
	@JSONField(name = "cdocno")
	public java.lang.String getCDocNo() {
		return getStr("cDocNo");
	}

	/**
	 * 客户编码
	 */
	public M setCCusCode(java.lang.String cCusCode) {
		set("cCusCode", cCusCode);
		return (M)this;
	}

	/**
	 * 客户编码
	 */
	@JBoltField(name="ccuscode" ,columnName="cCusCode",type="String", remark="客户编码", required=false, maxLength=40, fixed=0, order=63)
	@JSONField(name = "ccuscode")
	public java.lang.String getCCusCode() {
		return getStr("cCusCode");
	}

	/**
	 * 客户名称
	 */
	public M setCCusName(java.lang.String cCusName) {
		set("cCusName", cCusName);
		return (M)this;
	}

	/**
	 * 客户名称
	 */
	@JBoltField(name="ccusname" ,columnName="cCusName",type="String", remark="客户名称", required=false, maxLength=200, fixed=0, order=64)
	@JSONField(name = "ccusname")
	public java.lang.String getCCusName() {
		return getStr("cCusName");
	}

}

