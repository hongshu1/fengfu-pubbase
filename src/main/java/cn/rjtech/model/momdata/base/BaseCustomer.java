package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 往来单位-客户档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCustomer<M extends BaseCustomer<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**客户编码*/
    public static final String CCUSCODE = "cCusCode";
    /**客户名称*/
    public static final String CCUSNAME = "cCusName";
    /**客户简称*/
    public static final String CCUSABBNAME = "cCusAbbName";
    /**客户分类编码*/
    public static final String CCCCODE = "cCCCode";
    /**地区编码*/
    public static final String CDCCODE = "cDCCode";
    /**所属行业*/
    public static final String CTRADE = "cTrade";
    /**地址*/
    public static final String CCUSADDRESS = "cCusAddress";
    /**邮政编码*/
    public static final String CCUSPOSTCODE = "cCusPostCode";
    /**纳税人登记号*/
    public static final String CCUSREGCODE = "cCusRegCode";
    /**开户银行*/
    public static final String CCUSBANK = "cCusBank";
    /**银行账号*/
    public static final String CCUSACCOUNT = "cCusAccount";
    /**发展日期*/
    public static final String DCUSDEVDATE = "dCusDevDate";
    /**法人*/
    public static final String CCUSLPERSON = "cCusLPerson";
    /**Email地址*/
    public static final String CCUSEMAIL = "cCusEmail";
    /**联系人*/
    public static final String CCUSPERSON = "cCusPerson";
    /**电话*/
    public static final String CCUSPHONE = "cCusPhone";
    /**传真*/
    public static final String CCUSFAX = "cCusFax";
    /**呼机*/
    public static final String CCUSBP = "cCusBP";
    /**手机*/
    public static final String CCUSHAND = "cCusHand";
    /**专管业务员编码*/
    public static final String CCUSPPERSON = "cCusPPerson";
    /**扣率*/
    public static final String ICUSDISRATE = "iCusDisRate";
    /**信用等级*/
    public static final String CCUSCREGRADE = "cCusCreGrade";
    /**信用额度*/
    public static final String ICUSCRELINE = "iCusCreLine";
    /**信用期限*/
    public static final String ICUSCREDATE = "iCusCreDate";
    /**付款条件编码*/
    public static final String CCUSPAYCOND = "cCusPayCond";
    /**发货地址*/
    public static final String CCUSOADDRESS = "cCusOAddress";
    /**发运方式编码*/
    public static final String CCUSOTYPE = "cCusOType";
    /**客户总公司编码*/
    public static final String CCUSHEADCODE = "cCusHeadCode";
    /**仓库编码*/
    public static final String CCUSWHCODE = "cCusWhCode";
    /**部门编码*/
    public static final String CCUSDEPART = "cCusDepart";
    /**应收余额*/
    public static final String IARMONEY = "iARMoney";
    /**专管业务员, 系统用户ID*/
    public static final String IDUTYUSERID = "iDutyUserId";
    /**省份*/
    public static final String CPROVINCE = "cProvince";
    /**城市*/
    public static final String CCITY = "cCity";
    /**区县*/
    public static final String CCOUNTY = "cCounty";
    /**币种*/
    public static final String CCURRENCY = "cCurrency";
    /**客户管理类型*/
    public static final String CCUSTYPE = "cCusType";
    /**结算方式*/
    public static final String CSETTLEWAY = "cSettleWay";
    /**出货单据类型*/
    public static final String CSHIPMENT = "cShipment";
    /**是否启用：0. 禁用 1. 启用*/
    public static final String ISENABLED = "isEnabled";
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
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
	/**客户分类ID*/
	public static final String ICUSTOMERCLASSID = "iCustomerClassId";
	/**
	 * 助记码
	 */
	public static final String MNEMONICCODE = "mnemonicCode";
	/**
	 * 供应商编码
	 */
	public static final String CVENCODE = "cVenCode";
    /**
     * 供应商名称
     */
    public static final String CVENNAME = "cVenName";
	/**
	 * 客户属性
	 */
    public static final String CCUSATTRIBUTE = "cCusAttribute";
	/**客户等级(字典值)*/
	public static final String CCUSTOMERLEVELSN = "cCustomerLevelSn";
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
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
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
	@JBoltField(name="ccuscode" ,columnName="cCusCode",type="String", remark="客户编码", required=true, maxLength=20, fixed=0, order=5)
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
	@JBoltField(name="ccusname" ,columnName="cCusName",type="String", remark="客户名称", required=false, maxLength=98, fixed=0, order=6)
	@JSONField(name = "ccusname")
	public java.lang.String getCCusName() {
		return getStr("cCusName");
	}

	/**
	 * 客户简称
	 */
	public M setCCusAbbName(java.lang.String cCusAbbName) {
		set("cCusAbbName", cCusAbbName);
		return (M)this;
	}

	/**
	 * 客户简称
	 */
	@JBoltField(name="ccusabbname" ,columnName="cCusAbbName",type="String", remark="客户简称", required=true, maxLength=60, fixed=0, order=7)
	@JSONField(name = "ccusabbname")
	public java.lang.String getCCusAbbName() {
		return getStr("cCusAbbName");
	}

	/**
	 * 客户分类编码
	 */
	public M setCCCCode(java.lang.String cCCCode) {
		set("cCCCode", cCCCode);
		return (M)this;
	}

	/**
	 * 客户分类编码
	 */
	@JBoltField(name="ccccode" ,columnName="cCCCode",type="String", remark="客户分类编码", required=false, maxLength=12, fixed=0, order=8)
	@JSONField(name = "ccccode")
	public java.lang.String getCCCCode() {
		return getStr("cCCCode");
	}

	/**
	 * 地区编码
	 */
	public M setCDCCode(java.lang.String cDCCode) {
		set("cDCCode", cDCCode);
		return (M)this;
	}

	/**
	 * 地区编码
	 */
	@JBoltField(name="cdccode" ,columnName="cDCCode",type="String", remark="地区编码", required=false, maxLength=12, fixed=0, order=9)
	@JSONField(name = "cdccode")
	public java.lang.String getCDCCode() {
		return getStr("cDCCode");
	}

	/**
	 * 所属行业
	 */
	public M setCTrade(java.lang.String cTrade) {
		set("cTrade", cTrade);
		return (M)this;
	}

	/**
	 * 所属行业
	 */
	@JBoltField(name="ctrade" ,columnName="cTrade",type="String", remark="所属行业", required=false, maxLength=50, fixed=0, order=10)
	@JSONField(name = "ctrade")
	public java.lang.String getCTrade() {
		return getStr("cTrade");
	}

	/**
	 * 地址
	 */
	public M setCCusAddress(java.lang.String cCusAddress) {
		set("cCusAddress", cCusAddress);
		return (M)this;
	}

	/**
	 * 地址
	 */
	@JBoltField(name="ccusaddress" ,columnName="cCusAddress",type="String", remark="地址", required=false, maxLength=255, fixed=0, order=11)
	@JSONField(name = "ccusaddress")
	public java.lang.String getCCusAddress() {
		return getStr("cCusAddress");
	}

	/**
	 * 邮政编码
	 */
	public M setCCusPostCode(java.lang.String cCusPostCode) {
		set("cCusPostCode", cCusPostCode);
		return (M)this;
	}

	/**
	 * 邮政编码
	 */
	@JBoltField(name="ccuspostcode" ,columnName="cCusPostCode",type="String", remark="邮政编码", required=false, maxLength=20, fixed=0, order=12)
	@JSONField(name = "ccuspostcode")
	public java.lang.String getCCusPostCode() {
		return getStr("cCusPostCode");
	}

	/**
	 * 纳税人登记号
	 */
	public M setCCusRegCode(java.lang.String cCusRegCode) {
		set("cCusRegCode", cCusRegCode);
		return (M)this;
	}

	/**
	 * 纳税人登记号
	 */
	@JBoltField(name="ccusregcode" ,columnName="cCusRegCode",type="String", remark="纳税人登记号", required=false, maxLength=50, fixed=0, order=13)
	@JSONField(name = "ccusregcode")
	public java.lang.String getCCusRegCode() {
		return getStr("cCusRegCode");
	}

	/**
	 * 开户银行
	 */
	public M setCCusBank(java.lang.String cCusBank) {
		set("cCusBank", cCusBank);
		return (M)this;
	}

	/**
	 * 开户银行
	 */
	@JBoltField(name="ccusbank" ,columnName="cCusBank",type="String", remark="开户银行", required=false, maxLength=100, fixed=0, order=14)
	@JSONField(name = "ccusbank")
	public java.lang.String getCCusBank() {
		return getStr("cCusBank");
	}

	/**
	 * 银行账号
	 */
	public M setCCusAccount(java.lang.String cCusAccount) {
		set("cCusAccount", cCusAccount);
		return (M)this;
	}

	/**
	 * 银行账号
	 */
	@JBoltField(name="ccusaccount" ,columnName="cCusAccount",type="String", remark="银行账号", required=false, maxLength=50, fixed=0, order=15)
	@JSONField(name = "ccusaccount")
	public java.lang.String getCCusAccount() {
		return getStr("cCusAccount");
	}

	/**
	 * 发展日期
	 */
	public M setDCusDevDate(java.util.Date dCusDevDate) {
		set("dCusDevDate", dCusDevDate);
		return (M)this;
	}

	/**
	 * 发展日期
	 */
	@JBoltField(name="dcusdevdate" ,columnName="dCusDevDate",type="Date", remark="发展日期", required=false, maxLength=23, fixed=3, order=16)
	@JSONField(name = "dcusdevdate")
	public java.util.Date getDCusDevDate() {
		return getDate("dCusDevDate");
	}

	/**
	 * 法人
	 */
	public M setCCusLPerson(java.lang.String cCusLPerson) {
		set("cCusLPerson", cCusLPerson);
		return (M)this;
	}

	/**
	 * 法人
	 */
	@JBoltField(name="ccuslperson" ,columnName="cCusLPerson",type="String", remark="法人", required=false, maxLength=100, fixed=0, order=17)
	@JSONField(name = "ccuslperson")
	public java.lang.String getCCusLPerson() {
		return getStr("cCusLPerson");
	}

	/**
	 * Email地址
	 */
	public M setCCusEmail(java.lang.String cCusEmail) {
		set("cCusEmail", cCusEmail);
		return (M)this;
	}

	/**
	 * Email地址
	 */
	@JBoltField(name="ccusemail" ,columnName="cCusEmail",type="String", remark="Email地址", required=false, maxLength=100, fixed=0, order=18)
	@JSONField(name = "ccusemail")
	public java.lang.String getCCusEmail() {
		return getStr("cCusEmail");
	}

	/**
	 * 联系人
	 */
	public M setCCusPerson(java.lang.String cCusPerson) {
		set("cCusPerson", cCusPerson);
		return (M)this;
	}

	/**
	 * 联系人
	 */
	@JBoltField(name="ccusperson" ,columnName="cCusPerson",type="String", remark="联系人", required=false, maxLength=50, fixed=0, order=19)
	@JSONField(name = "ccusperson")
	public java.lang.String getCCusPerson() {
		return getStr("cCusPerson");
	}

	/**
	 * 电话
	 */
	public M setCCusPhone(java.lang.String cCusPhone) {
		set("cCusPhone", cCusPhone);
		return (M)this;
	}

	/**
	 * 电话
	 */
	@JBoltField(name="ccusphone" ,columnName="cCusPhone",type="String", remark="电话", required=false, maxLength=100, fixed=0, order=20)
	@JSONField(name = "ccusphone")
	public java.lang.String getCCusPhone() {
		return getStr("cCusPhone");
	}

	/**
	 * 传真
	 */
	public M setCCusFax(java.lang.String cCusFax) {
		set("cCusFax", cCusFax);
		return (M)this;
	}

	/**
	 * 传真
	 */
	@JBoltField(name="ccusfax" ,columnName="cCusFax",type="String", remark="传真", required=false, maxLength=100, fixed=0, order=21)
	@JSONField(name = "ccusfax")
	public java.lang.String getCCusFax() {
		return getStr("cCusFax");
	}

	/**
	 * 呼机
	 */
	public M setCCusBP(java.lang.String cCusBP) {
		set("cCusBP", cCusBP);
		return (M)this;
	}

	/**
	 * 呼机
	 */
	@JBoltField(name="ccusbp" ,columnName="cCusBP",type="String", remark="呼机", required=false, maxLength=20, fixed=0, order=22)
	@JSONField(name = "ccusbp")
	public java.lang.String getCCusBP() {
		return getStr("cCusBP");
	}

	/**
	 * 手机
	 */
	public M setCCusHand(java.lang.String cCusHand) {
		set("cCusHand", cCusHand);
		return (M)this;
	}

	/**
	 * 手机
	 */
	@JBoltField(name="ccushand" ,columnName="cCusHand",type="String", remark="手机", required=false, maxLength=100, fixed=0, order=23)
	@JSONField(name = "ccushand")
	public java.lang.String getCCusHand() {
		return getStr("cCusHand");
	}

	/**
	 * 专管业务员编码
	 */
	public M setCCusPPerson(java.lang.String cCusPPerson) {
		set("cCusPPerson", cCusPPerson);
		return (M)this;
	}

	/**
	 * 专管业务员编码
	 */
	@JBoltField(name="ccuspperson" ,columnName="cCusPPerson",type="String", remark="专管业务员编码", required=false, maxLength=20, fixed=0, order=24)
	@JSONField(name = "ccuspperson")
	public java.lang.String getCCusPPerson() {
		return getStr("cCusPPerson");
	}

	/**
	 * 扣率
	 */
	public M setICusDisRate(java.lang.Double iCusDisRate) {
		set("iCusDisRate", iCusDisRate);
		return (M)this;
	}

	/**
	 * 扣率
	 */
	@JBoltField(name="icusdisrate" ,columnName="iCusDisRate",type="Double", remark="扣率", required=false, maxLength=53, fixed=0, order=25)
	@JSONField(name = "icusdisrate")
	public java.lang.Double getICusDisRate() {
		return getDouble("iCusDisRate");
	}

	/**
	 * 信用等级
	 */
	public M setCCusCreGrade(java.lang.String cCusCreGrade) {
		set("cCusCreGrade", cCusCreGrade);
		return (M)this;
	}

	/**
	 * 信用等级
	 */
	@JBoltField(name="ccuscregrade" ,columnName="cCusCreGrade",type="String", remark="信用等级", required=false, maxLength=6, fixed=0, order=26)
	@JSONField(name = "ccuscregrade")
	public java.lang.String getCCusCreGrade() {
		return getStr("cCusCreGrade");
	}

	/**
	 * 信用额度
	 */
	public M setICusCreLine(java.lang.Double iCusCreLine) {
		set("iCusCreLine", iCusCreLine);
		return (M)this;
	}

	/**
	 * 信用额度
	 */
	@JBoltField(name="icuscreline" ,columnName="iCusCreLine",type="Double", remark="信用额度", required=false, maxLength=53, fixed=0, order=27)
	@JSONField(name = "icuscreline")
	public java.lang.Double getICusCreLine() {
		return getDouble("iCusCreLine");
	}

	/**
	 * 信用期限
	 */
	public M setICusCreDate(java.lang.Integer iCusCreDate) {
		set("iCusCreDate", iCusCreDate);
		return (M)this;
	}

	/**
	 * 信用期限
	 */
	@JBoltField(name="icuscredate" ,columnName="iCusCreDate",type="Integer", remark="信用期限", required=false, maxLength=10, fixed=0, order=28)
	@JSONField(name = "icuscredate")
	public java.lang.Integer getICusCreDate() {
		return getInt("iCusCreDate");
	}

	/**
	 * 付款条件编码
	 */
	public M setCCusPayCond(java.lang.String cCusPayCond) {
		set("cCusPayCond", cCusPayCond);
		return (M)this;
	}

	/**
	 * 付款条件编码
	 */
	@JBoltField(name="ccuspaycond" ,columnName="cCusPayCond",type="String", remark="付款条件编码", required=false, maxLength=20, fixed=0, order=29)
	@JSONField(name = "ccuspaycond")
	public java.lang.String getCCusPayCond() {
		return getStr("cCusPayCond");
	}

	/**
	 * 发货地址
	 */
	public M setCCusOAddress(java.lang.String cCusOAddress) {
		set("cCusOAddress", cCusOAddress);
		return (M)this;
	}

	/**
	 * 发货地址
	 */
	@JBoltField(name="ccusoaddress" ,columnName="cCusOAddress",type="String", remark="发货地址", required=false, maxLength=255, fixed=0, order=30)
	@JSONField(name = "ccusoaddress")
	public java.lang.String getCCusOAddress() {
		return getStr("cCusOAddress");
	}

	/**
	 * 发运方式编码
	 */
	public M setCCusOType(java.lang.String cCusOType) {
		set("cCusOType", cCusOType);
		return (M)this;
	}

	/**
	 * 发运方式编码
	 */
	@JBoltField(name="ccusotype" ,columnName="cCusOType",type="String", remark="发运方式编码", required=false, maxLength=10, fixed=0, order=31)
	@JSONField(name = "ccusotype")
	public java.lang.String getCCusOType() {
		return getStr("cCusOType");
	}

	/**
	 * 客户总公司编码
	 */
	public M setCCusHeadCode(java.lang.String cCusHeadCode) {
		set("cCusHeadCode", cCusHeadCode);
		return (M)this;
	}

	/**
	 * 客户总公司编码
	 */
	@JBoltField(name="ccusheadcode" ,columnName="cCusHeadCode",type="String", remark="客户总公司编码", required=false, maxLength=20, fixed=0, order=32)
	@JSONField(name = "ccusheadcode")
	public java.lang.String getCCusHeadCode() {
		return getStr("cCusHeadCode");
	}

	/**
	 * 仓库编码
	 */
	public M setCCusWhCode(java.lang.String cCusWhCode) {
		set("cCusWhCode", cCusWhCode);
		return (M)this;
	}

	/**
	 * 仓库编码
	 */
	@JBoltField(name="ccuswhcode" ,columnName="cCusWhCode",type="String", remark="仓库编码", required=false, maxLength=10, fixed=0, order=33)
	@JSONField(name = "ccuswhcode")
	public java.lang.String getCCusWhCode() {
		return getStr("cCusWhCode");
	}

	/**
	 * 部门编码
	 */
	public M setCCusDepart(java.lang.String cCusDepart) {
		set("cCusDepart", cCusDepart);
		return (M)this;
	}

	/**
	 * 部门编码
	 */
	@JBoltField(name="ccusdepart" ,columnName="cCusDepart",type="String", remark="部门编码", required=false, maxLength=12, fixed=0, order=34)
	@JSONField(name = "ccusdepart")
	public java.lang.String getCCusDepart() {
		return getStr("cCusDepart");
	}

	/**
	 * 应收余额
	 */
	public M setIARMoney(java.lang.Double iARMoney) {
		set("iARMoney", iARMoney);
		return (M)this;
	}

	/**
	 * 应收余额
	 */
	@JBoltField(name="iarmoney" ,columnName="iARMoney",type="Double", remark="应收余额", required=false, maxLength=53, fixed=0, order=35)
	@JSONField(name = "iarmoney")
	public java.lang.Double getIARMoney() {
		return getDouble("iARMoney");
	}

	/**
	 * 专管业务员, 系统用户ID
	 */
	public M setIDutyUserId(java.lang.Long iDutyUserId) {
		set("iDutyUserId", iDutyUserId);
		return (M)this;
	}

	/**
	 * 专管业务员, 系统用户ID
	 */
	@JBoltField(name="idutyuserid" ,columnName="iDutyUserId",type="Long", remark="专管业务员, 系统用户ID", required=false, maxLength=19, fixed=0, order=36)
	@JSONField(name = "idutyuserid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDutyUserId() {
		return getLong("iDutyUserId");
	}

	/**
	 * 省份
	 */
	public M setCProvince(java.lang.String cProvince) {
		set("cProvince", cProvince);
		return (M)this;
	}

	/**
	 * 省份
	 */
	@JBoltField(name="cprovince" ,columnName="cProvince",type="String", remark="省份", required=false, maxLength=255, fixed=0, order=37)
	@JSONField(name = "cprovince")
	public java.lang.String getCProvince() {
		return getStr("cProvince");
	}

	/**
	 * 城市
	 */
	public M setCCity(java.lang.String cCity) {
		set("cCity", cCity);
		return (M)this;
	}

	/**
	 * 城市
	 */
	@JBoltField(name="ccity" ,columnName="cCity",type="String", remark="城市", required=false, maxLength=255, fixed=0, order=38)
	@JSONField(name = "ccity")
	public java.lang.String getCCity() {
		return getStr("cCity");
	}

	/**
	 * 区县
	 */
	public M setCCounty(java.lang.String cCounty) {
		set("cCounty", cCounty);
		return (M)this;
	}

	/**
	 * 区县
	 */
	@JBoltField(name="ccounty" ,columnName="cCounty",type="String", remark="区县", required=false, maxLength=255, fixed=0, order=39)
	@JSONField(name = "ccounty")
	public java.lang.String getCCounty() {
		return getStr("cCounty");
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
	@JBoltField(name="ccurrency" ,columnName="cCurrency",type="String", remark="币种", required=false, maxLength=10, fixed=0, order=40)
	@JSONField(name = "ccurrency")
	public java.lang.String getCCurrency() {
		return getStr("cCurrency");
	}

	/**
	 * 客户管理类型
	 */
	public M setCCusType(java.lang.String cCusType) {
		set("cCusType", cCusType);
		return (M)this;
	}

	/**
	 * 客户管理类型
	 */
	@JBoltField(name="ccustype" ,columnName="cCusType",type="String", remark="客户管理类型", required=false, maxLength=10, fixed=0, order=41)
	@JSONField(name = "ccustype")
	public java.lang.String getCCusType() {
		return getStr("cCusType");
	}

	/**
	 * 结算方式
	 */
	public M setCSettleWay(java.lang.String cSettleWay) {
		set("cSettleWay", cSettleWay);
		return (M)this;
	}

	/**
	 * 结算方式
	 */
	@JBoltField(name="csettleway" ,columnName="cSettleWay",type="String", remark="结算方式", required=false, maxLength=10, fixed=0, order=42)
	@JSONField(name = "csettleway")
	public java.lang.String getCSettleWay() {
		return getStr("cSettleWay");
	}

	/**
	 * 出货单据类型
	 */
	public M setCShipment(java.lang.String cShipment) {
		set("cShipment", cShipment);
		return (M)this;
	}

	/**
	 * 出货单据类型
	 */
	@JBoltField(name="cshipment" ,columnName="cShipment",type="String", remark="出货单据类型", required=false, maxLength=10, fixed=0, order=43)
	@JSONField(name = "cshipment")
	public java.lang.String getCShipment() {
		return getStr("cShipment");
	}

	/**
	 * 是否启用：0. 禁用 1. 启用
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用：0. 禁用 1. 启用
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用：0. 禁用 1. 启用", required=true, maxLength=1, fixed=0, order=44)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=240, fixed=0, order=45)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=46)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=47)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=48)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=49)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=50)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=51)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=52)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 客户分类ID
	 */
	public M setIcustomerclassid(java.lang.Long icustomerclassid) {
		set("iCustomerClassId", icustomerclassid);
		return (M)this;
	}

	/**
	 * 客户分类ID
	 */
	@JBoltField(name="icustomerclassid" ,columnName="iCustomerClassId",type="Long", remark="客户分类ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIcustomerclassid() {
		return getLong("iCustomerClassId");
	}


	/**
	 * 助记码
	 */
	public M setMnemonicCode(java.lang.String mnemonicCode) {
		set("mnemonicCode", mnemonicCode);
		return (M)this;
	}

	/**
	 * 助记码
	 */
	@JBoltField(name="mnemoniccode" ,columnName="mnemonicCode",type="String", remark="助记码", required=false, maxLength=100, fixed=0, order=17)
	@JSONField(name = "mnemoniccode")
	public java.lang.String getMnemonicCode() {
		return getStr("mnemonicCode");
	}

    /**
     * 供应商编码
     */
    public M setCVenCode(java.lang.String cVenCode) {
        set("cVenCode", cVenCode);
        return (M)this;
    }

    /**
     * 供应商编码
     */
    @JBoltField(name="cvencode" ,columnName="cVenCode",type="String", remark="供应商编码", required=false, maxLength=100,
            fixed=0, order=17)
    @JSONField(name = "cvencode")
    public java.lang.String getCVenCode() {
        return getStr("cVenCode");
    }

    /**
     * 供应商名称
     */
    public M setCVenName(java.lang.String cVenName) {
        set("cVenName", cVenName);
        return (M)this;
    }

    /**
     * 供应商名称
     */
    @JBoltField(name="cvenname" ,columnName="cVenName",type="String", remark="供应商名称", required=false, maxLength=100,
            fixed=0, order=17)
    @JSONField(name = "cvenname")
    public java.lang.String getCVenName() {
        return getStr("cVenName");
    }

	/**
	 * 供应商ID
	 */
	public M setVendorId(java.lang.Long vendorId) {
		set("vendorId", vendorId);
		return (M)this;
	}

	/**
	 * 供应商ID
	 */
	@JBoltField(name="vendorid" ,columnName="vendorId",type="Long", remark="供应商ID", required=true, maxLength=19,
			fixed=0, order=1)
	@JSONField(name = "vendorid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getVendorId() {
		return getLong("vendorId");
	}


	/**
	 * 客户属性
	 */
	public M setCCusAttribute(java.lang.String cCusAttribute) {
		set("cCusAttribute", cCusAttribute);
		return (M)this;
	}

	/**
	 * 客户属性
	 */
	@JBoltField(name="ccusattribute" ,columnName="cCusAttribute",type="String", remark="客户属性", required=false,
			maxLength=10, fixed=0, order=41)
	@JSONField(name = "ccusattribute")
	public java.lang.String getCCusAttribute() {
		return getStr("cCusAttribute");
	}

	/**
	 * 客户等级(字典值)
	 */
	public M setCcustomerlevelsn(java.lang.String ccustomerlevelsn) {
		set("cCustomerLevelSn", ccustomerlevelsn);
		return (M)this;
	}

	/**
	 * 客户等级(字典值)
	 */
	@JBoltField(name="ccustomerlevelsn" ,columnName="cCustomerLevelSn",type="String", remark="客户等级(字典值)", required=true, maxLength=255, fixed=0, order=9)
	public java.lang.String getCcustomerlevelsn() {
		return getStr("cCustomerLevelSn");
	}

}

