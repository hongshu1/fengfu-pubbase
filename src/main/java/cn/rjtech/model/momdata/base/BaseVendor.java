package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 往来单位-供应商档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseVendor<M extends BaseVendor<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**供应商编码*/
    public static final String CVENCODE = "cVenCode";
    /**供应商名称*/
    public static final String CVENNAME = "cVenName";
    /**助记码*/
    public static final String CVENMNEMCODE = "cVenMnemCode";
    /**分管部门编码*/
    public static final String CVENDEPART = "cVenDepart";
    /**供应商简称*/
    public static final String CVENABBNAME = "cVenAbbName";
    /**默认委外仓库ID*/
    public static final String IOMWHID = "iOMWhId";
    /**默认委外仓编码*/
    public static final String COMWHCODE = "cOMWhCode";
    /**结算方式ID*/
    public static final String ISSID = "iSSid";
    /**结算方式编码*/
    public static final String CVENSSCODE = "cVenSSCode";
    /**供应商分类编码*/
    public static final String CVCCODE = "cVCCode";
    /**地区编码*/
    public static final String CDCCODE = "cDCCode";
    /**所属行业*/
    public static final String CTRADE = "cTrade";
    /**地址*/
    public static final String CVENADDRESS = "cVenAddress";
    /**邮政编码*/
    public static final String CVENPOSTCODE = "cVenPostCode";
    /**纳税人登记号*/
    public static final String CVENREGCODE = "cVenRegCode";
    /**开户银行*/
    public static final String CVENBANK = "cVenBank";
    /**银行账号*/
    public static final String CVENACCOUNT = "cVenAccount";
    /**发展日期*/
    public static final String DVENDEVDATE = "dVenDevDate";
    /**法人*/
    public static final String CVENLPERSON = "cVenLPerson";
    /**电话*/
    public static final String CVENPHONE = "cVenPhone";
    /**传真*/
    public static final String CVENFAX = "cVenFax";
    /**Email地址*/
    public static final String CVENEMAIL = "cVenEmail";
    /**联系人*/
    public static final String CVENPERSON = "cVenPerson";
    /**呼机*/
    public static final String CVENBP = "cVenBP";
    /**手机*/
    public static final String CVENHAND = "cVenHand";
    /**专管业务员编码*/
    public static final String CVENPPERSON = "cVenPPerson";
    /**扣率*/
    public static final String IVENDISRATE = "iVenDisRate";
    /**信用等级*/
    public static final String IVENCREGRADE = "iVenCreGrade";
    /**信用额度*/
    public static final String IVENCRELINE = "iVenCreLine";
    /**信用期限*/
    public static final String IVENCREDATE = "iVenCreDate";
    /**来源：1. MES 2. U8*/
    public static final String ISOURCE = "iSource";
    /**来源ID*/
    public static final String ISOURCEID = "iSourceId";
    /**专管业务员, 人员ID*/
    public static final String IDUTYPERSONID = "iDutyPersonId";
    /**币种*/
    public static final String CCURRENCY = "cCurrency";
    /**税率，通常2位小数*/
    public static final String ITAXRATE = "iTaxRate";
    /**供应商属性*/
    public static final String CPROPERTY = "cProperty";
    /**国家*/
    public static final String CCOUNTRY = "cCountry";
    /**省份*/
    public static final String CPROVINCE = "cProvince";
    /**城市*/
    public static final String CCITY = "cCity";
    /**区县*/
    public static final String CCOUNTY = "cCounty";
    /**是否启用：0. 停用 1. 启用*/
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
	 * 供应商编码
	 */
	public M setCVenCode(java.lang.String cVenCode) {
		set("cVenCode", cVenCode);
		return (M)this;
	}

	/**
	 * 供应商编码
	 */
	@JBoltField(name="cvencode" ,columnName="cVenCode",type="String", remark="供应商编码", required=true, maxLength=20, fixed=0, order=5)
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
	@JBoltField(name="cvenname" ,columnName="cVenName",type="String", remark="供应商名称", required=false, maxLength=98, fixed=0, order=6)
	@JSONField(name = "cvenname")
	public java.lang.String getCVenName() {
		return getStr("cVenName");
	}

	/**
	 * 助记码
	 */
	public M setCVenMnemCode(java.lang.String cVenMnemCode) {
		set("cVenMnemCode", cVenMnemCode);
		return (M)this;
	}

	/**
	 * 助记码
	 */
	@JBoltField(name="cvenmnemcode" ,columnName="cVenMnemCode",type="String", remark="助记码", required=false, maxLength=98, fixed=0, order=7)
	@JSONField(name = "cvenmnemcode")
	public java.lang.String getCVenMnemCode() {
		return getStr("cVenMnemCode");
	}

	/**
	 * 分管部门编码
	 */
	public M setCVenDepart(java.lang.String cVenDepart) {
		set("cVenDepart", cVenDepart);
		return (M)this;
	}

	/**
	 * 分管部门编码
	 */
	@JBoltField(name="cvendepart" ,columnName="cVenDepart",type="String", remark="分管部门编码", required=false, maxLength=98, fixed=0, order=8)
	@JSONField(name = "cvendepart")
	public java.lang.String getCVenDepart() {
		return getStr("cVenDepart");
	}

	/**
	 * 供应商简称
	 */
	public M setCVenAbbName(java.lang.String cVenAbbName) {
		set("cVenAbbName", cVenAbbName);
		return (M)this;
	}

	/**
	 * 供应商简称
	 */
	@JBoltField(name="cvenabbname" ,columnName="cVenAbbName",type="String", remark="供应商简称", required=true, maxLength=60, fixed=0, order=9)
	@JSONField(name = "cvenabbname")
	public java.lang.String getCVenAbbName() {
		return getStr("cVenAbbName");
	}

	/**
	 * 默认委外仓库ID
	 */
	public M setIOMWhId(java.lang.Long iOMWhId) {
		set("iOMWhId", iOMWhId);
		return (M)this;
	}

	/**
	 * 默认委外仓库ID
	 */
	@JBoltField(name="iomwhid" ,columnName="iOMWhId",type="Long", remark="默认委外仓库ID", required=false, maxLength=19, fixed=0, order=10)
	@JSONField(name = "iomwhid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIOMWhId() {
		return getLong("iOMWhId");
	}

	/**
	 * 默认委外仓编码
	 */
	public M setCOMWhCode(java.lang.String cOMWhCode) {
		set("cOMWhCode", cOMWhCode);
		return (M)this;
	}

	/**
	 * 默认委外仓编码
	 */
	@JBoltField(name="comwhcode" ,columnName="cOMWhCode",type="String", remark="默认委外仓编码", required=false, maxLength=20, fixed=0, order=11)
	@JSONField(name = "comwhcode")
	public java.lang.String getCOMWhCode() {
		return getStr("cOMWhCode");
	}

	/**
	 * 结算方式ID
	 */
	public M setISSid(java.lang.Long iSSid) {
		set("iSSid", iSSid);
		return (M)this;
	}

	/**
	 * 结算方式ID
	 */
	@JBoltField(name="issid" ,columnName="iSSid",type="Long", remark="结算方式ID", required=false, maxLength=19, fixed=0, order=12)
	@JSONField(name = "issid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISSid() {
		return getLong("iSSid");
	}

	/**
	 * 结算方式编码
	 */
	public M setCVenSSCode(java.lang.String cVenSSCode) {
		set("cVenSSCode", cVenSSCode);
		return (M)this;
	}

	/**
	 * 结算方式编码
	 */
	@JBoltField(name="cvensscode" ,columnName="cVenSSCode",type="String", remark="结算方式编码", required=false, maxLength=20, fixed=0, order=13)
	@JSONField(name = "cvensscode")
	public java.lang.String getCVenSSCode() {
		return getStr("cVenSSCode");
	}

	/**
	 * 供应商分类编码
	 */
	public M setCVCCode(java.lang.String cVCCode) {
		set("cVCCode", cVCCode);
		return (M)this;
	}

	/**
	 * 供应商分类编码
	 */
	@JBoltField(name="cvccode" ,columnName="cVCCode",type="String", remark="供应商分类编码", required=false, maxLength=12, fixed=0, order=14)
	@JSONField(name = "cvccode")
	public java.lang.String getCVCCode() {
		return getStr("cVCCode");
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
	@JBoltField(name="cdccode" ,columnName="cDCCode",type="String", remark="地区编码", required=false, maxLength=12, fixed=0, order=15)
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
	@JBoltField(name="ctrade" ,columnName="cTrade",type="String", remark="所属行业", required=false, maxLength=50, fixed=0, order=16)
	@JSONField(name = "ctrade")
	public java.lang.String getCTrade() {
		return getStr("cTrade");
	}

	/**
	 * 地址
	 */
	public M setCVenAddress(java.lang.String cVenAddress) {
		set("cVenAddress", cVenAddress);
		return (M)this;
	}

	/**
	 * 地址
	 */
	@JBoltField(name="cvenaddress" ,columnName="cVenAddress",type="String", remark="地址", required=false, maxLength=255, fixed=0, order=17)
	@JSONField(name = "cvenaddress")
	public java.lang.String getCVenAddress() {
		return getStr("cVenAddress");
	}

	/**
	 * 邮政编码
	 */
	public M setCVenPostCode(java.lang.String cVenPostCode) {
		set("cVenPostCode", cVenPostCode);
		return (M)this;
	}

	/**
	 * 邮政编码
	 */
	@JBoltField(name="cvenpostcode" ,columnName="cVenPostCode",type="String", remark="邮政编码", required=false, maxLength=20, fixed=0, order=18)
	@JSONField(name = "cvenpostcode")
	public java.lang.String getCVenPostCode() {
		return getStr("cVenPostCode");
	}

	/**
	 * 纳税人登记号
	 */
	public M setCVenRegCode(java.lang.String cVenRegCode) {
		set("cVenRegCode", cVenRegCode);
		return (M)this;
	}

	/**
	 * 纳税人登记号
	 */
	@JBoltField(name="cvenregcode" ,columnName="cVenRegCode",type="String", remark="纳税人登记号", required=false, maxLength=50, fixed=0, order=19)
	@JSONField(name = "cvenregcode")
	public java.lang.String getCVenRegCode() {
		return getStr("cVenRegCode");
	}

	/**
	 * 开户银行
	 */
	public M setCVenBank(java.lang.String cVenBank) {
		set("cVenBank", cVenBank);
		return (M)this;
	}

	/**
	 * 开户银行
	 */
	@JBoltField(name="cvenbank" ,columnName="cVenBank",type="String", remark="开户银行", required=false, maxLength=100, fixed=0, order=20)
	@JSONField(name = "cvenbank")
	public java.lang.String getCVenBank() {
		return getStr("cVenBank");
	}

	/**
	 * 银行账号
	 */
	public M setCVenAccount(java.lang.String cVenAccount) {
		set("cVenAccount", cVenAccount);
		return (M)this;
	}

	/**
	 * 银行账号
	 */
	@JBoltField(name="cvenaccount" ,columnName="cVenAccount",type="String", remark="银行账号", required=false, maxLength=50, fixed=0, order=21)
	@JSONField(name = "cvenaccount")
	public java.lang.String getCVenAccount() {
		return getStr("cVenAccount");
	}

	/**
	 * 发展日期
	 */
	public M setDVenDevDate(java.util.Date dVenDevDate) {
		set("dVenDevDate", dVenDevDate);
		return (M)this;
	}

	/**
	 * 发展日期
	 */
	@JBoltField(name="dvendevdate" ,columnName="dVenDevDate",type="Date", remark="发展日期", required=false, maxLength=23, fixed=3, order=22)
	@JSONField(name = "dvendevdate")
	public java.util.Date getDVenDevDate() {
		return getDate("dVenDevDate");
	}

	/**
	 * 法人
	 */
	public M setCVenLPerson(java.lang.String cVenLPerson) {
		set("cVenLPerson", cVenLPerson);
		return (M)this;
	}

	/**
	 * 法人
	 */
	@JBoltField(name="cvenlperson" ,columnName="cVenLPerson",type="String", remark="法人", required=false, maxLength=100, fixed=0, order=23)
	@JSONField(name = "cvenlperson")
	public java.lang.String getCVenLPerson() {
		return getStr("cVenLPerson");
	}

	/**
	 * 电话
	 */
	public M setCVenPhone(java.lang.String cVenPhone) {
		set("cVenPhone", cVenPhone);
		return (M)this;
	}

	/**
	 * 电话
	 */
	@JBoltField(name="cvenphone" ,columnName="cVenPhone",type="String", remark="电话", required=false, maxLength=100, fixed=0, order=24)
	@JSONField(name = "cvenphone")
	public java.lang.String getCVenPhone() {
		return getStr("cVenPhone");
	}

	/**
	 * 传真
	 */
	public M setCVenFax(java.lang.String cVenFax) {
		set("cVenFax", cVenFax);
		return (M)this;
	}

	/**
	 * 传真
	 */
	@JBoltField(name="cvenfax" ,columnName="cVenFax",type="String", remark="传真", required=false, maxLength=100, fixed=0, order=25)
	@JSONField(name = "cvenfax")
	public java.lang.String getCVenFax() {
		return getStr("cVenFax");
	}

	/**
	 * Email地址
	 */
	public M setCVenEmail(java.lang.String cVenEmail) {
		set("cVenEmail", cVenEmail);
		return (M)this;
	}

	/**
	 * Email地址
	 */
	@JBoltField(name="cvenemail" ,columnName="cVenEmail",type="String", remark="Email地址", required=false, maxLength=100, fixed=0, order=26)
	@JSONField(name = "cvenemail")
	public java.lang.String getCVenEmail() {
		return getStr("cVenEmail");
	}

	/**
	 * 联系人
	 */
	public M setCVenPerson(java.lang.String cVenPerson) {
		set("cVenPerson", cVenPerson);
		return (M)this;
	}

	/**
	 * 联系人
	 */
	@JBoltField(name="cvenperson" ,columnName="cVenPerson",type="String", remark="联系人", required=false, maxLength=50, fixed=0, order=27)
	@JSONField(name = "cvenperson")
	public java.lang.String getCVenPerson() {
		return getStr("cVenPerson");
	}

	/**
	 * 呼机
	 */
	public M setCVenBP(java.lang.String cVenBP) {
		set("cVenBP", cVenBP);
		return (M)this;
	}

	/**
	 * 呼机
	 */
	@JBoltField(name="cvenbp" ,columnName="cVenBP",type="String", remark="呼机", required=false, maxLength=20, fixed=0, order=28)
	@JSONField(name = "cvenbp")
	public java.lang.String getCVenBP() {
		return getStr("cVenBP");
	}

	/**
	 * 手机
	 */
	public M setCVenHand(java.lang.String cVenHand) {
		set("cVenHand", cVenHand);
		return (M)this;
	}

	/**
	 * 手机
	 */
	@JBoltField(name="cvenhand" ,columnName="cVenHand",type="String", remark="手机", required=false, maxLength=20, fixed=0, order=29)
	@JSONField(name = "cvenhand")
	public java.lang.String getCVenHand() {
		return getStr("cVenHand");
	}

	/**
	 * 专管业务员编码
	 */
	public M setCVenPPerson(java.lang.String cVenPPerson) {
		set("cVenPPerson", cVenPPerson);
		return (M)this;
	}

	/**
	 * 专管业务员编码
	 */
	@JBoltField(name="cvenpperson" ,columnName="cVenPPerson",type="String", remark="专管业务员编码", required=false, maxLength=20, fixed=0, order=30)
	@JSONField(name = "cvenpperson")
	public java.lang.String getCVenPPerson() {
		return getStr("cVenPPerson");
	}

	/**
	 * 扣率
	 */
	public M setIVenDisRate(java.lang.Double iVenDisRate) {
		set("iVenDisRate", iVenDisRate);
		return (M)this;
	}

	/**
	 * 扣率
	 */
	@JBoltField(name="ivendisrate" ,columnName="iVenDisRate",type="Double", remark="扣率", required=false, maxLength=53, fixed=0, order=31)
	@JSONField(name = "ivendisrate")
	public java.lang.Double getIVenDisRate() {
		return getDouble("iVenDisRate");
	}

	/**
	 * 信用等级
	 */
	public M setIVenCreGrade(java.lang.String iVenCreGrade) {
		set("iVenCreGrade", iVenCreGrade);
		return (M)this;
	}

	/**
	 * 信用等级
	 */
	@JBoltField(name="ivencregrade" ,columnName="iVenCreGrade",type="String", remark="信用等级", required=false, maxLength=6, fixed=0, order=32)
	@JSONField(name = "ivencregrade")
	public java.lang.String getIVenCreGrade() {
		return getStr("iVenCreGrade");
	}

	/**
	 * 信用额度
	 */
	public M setIVenCreLine(java.lang.Double iVenCreLine) {
		set("iVenCreLine", iVenCreLine);
		return (M)this;
	}

	/**
	 * 信用额度
	 */
	@JBoltField(name="ivencreline" ,columnName="iVenCreLine",type="Double", remark="信用额度", required=false, maxLength=53, fixed=0, order=33)
	@JSONField(name = "ivencreline")
	public java.lang.Double getIVenCreLine() {
		return getDouble("iVenCreLine");
	}

	/**
	 * 信用期限
	 */
	public M setIVenCreDate(java.lang.Integer iVenCreDate) {
		set("iVenCreDate", iVenCreDate);
		return (M)this;
	}

	/**
	 * 信用期限
	 */
	@JBoltField(name="ivencredate" ,columnName="iVenCreDate",type="Integer", remark="信用期限", required=false, maxLength=10, fixed=0, order=34)
	@JSONField(name = "ivencredate")
	public java.lang.Integer getIVenCreDate() {
		return getInt("iVenCreDate");
	}

	/**
	 * 来源：1. MES 2. U8
	 */
	public M setISource(java.lang.Integer iSource) {
		set("iSource", iSource);
		return (M)this;
	}

	/**
	 * 来源：1. MES 2. U8
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源：1. MES 2. U8", required=true, maxLength=10, fixed=0, order=35)
	@JSONField(name = "isource")
	public java.lang.Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源ID
	 */
	public M setISourceId(java.lang.String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源ID
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源ID", required=false, maxLength=32, fixed=0, order=36)
	@JSONField(name = "isourceid")
	public java.lang.String getISourceId() {
		return getStr("iSourceId");
	}

	/**
	 * 专管业务员, 人员ID
	 */
	public M setIDutyPersonId(java.lang.Long iDutyPersonId) {
		set("iDutyPersonId", iDutyPersonId);
		return (M)this;
	}

	/**
	 * 专管业务员, 人员ID
	 */
	@JBoltField(name="idutypersonid" ,columnName="iDutyPersonId",type="Long", remark="专管业务员, 人员ID", required=false, maxLength=19, fixed=0, order=37)
	@JSONField(name = "idutypersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDutyPersonId() {
		return getLong("iDutyPersonId");
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
	@JBoltField(name="ccurrency" ,columnName="cCurrency",type="String", remark="币种", required=false, maxLength=10, fixed=0, order=38)
	@JSONField(name = "ccurrency")
	public java.lang.String getCCurrency() {
		return getStr("cCurrency");
	}

	/**
	 * 税率，通常2位小数
	 */
	public M setITaxRate(java.math.BigDecimal iTaxRate) {
		set("iTaxRate", iTaxRate);
		return (M)this;
	}

	/**
	 * 税率，通常2位小数
	 */
	@JBoltField(name="itaxrate" ,columnName="iTaxRate",type="BigDecimal", remark="税率，通常2位小数", required=false, maxLength=5, fixed=4, order=39)
	@JSONField(name = "itaxrate")
	public java.math.BigDecimal getITaxRate() {
		return getBigDecimal("iTaxRate");
	}

	/**
	 * 供应商属性
	 */
	public M setCProperty(java.lang.String cProperty) {
		set("cProperty", cProperty);
		return (M)this;
	}

	/**
	 * 供应商属性
	 */
	@JBoltField(name="cproperty" ,columnName="cProperty",type="String", remark="供应商属性", required=false, maxLength=10, fixed=0, order=40)
	@JSONField(name = "cproperty")
	public java.lang.String getCProperty() {
		return getStr("cProperty");
	}

	/**
	 * 国家
	 */
	public M setCCountry(java.lang.String cCountry) {
		set("cCountry", cCountry);
		return (M)this;
	}

	/**
	 * 国家
	 */
	@JBoltField(name="ccountry" ,columnName="cCountry",type="String", remark="国家", required=false, maxLength=50, fixed=0, order=41)
	@JSONField(name = "ccountry")
	public java.lang.String getCCountry() {
		return getStr("cCountry");
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
	@JBoltField(name="cprovince" ,columnName="cProvince",type="String", remark="省份", required=false, maxLength=50, fixed=0, order=42)
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
	@JBoltField(name="ccity" ,columnName="cCity",type="String", remark="城市", required=false, maxLength=50, fixed=0, order=43)
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
	@JBoltField(name="ccounty" ,columnName="cCounty",type="String", remark="区县", required=false, maxLength=50, fixed=0, order=44)
	@JSONField(name = "ccounty")
	public java.lang.String getCCounty() {
		return getStr("cCounty");
	}

	/**
	 * 是否启用：0. 停用 1. 启用
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用：0. 停用 1. 启用
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用：0. 停用 1. 启用", required=true, maxLength=1, fixed=0, order=45)
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=46)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=47)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=48)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=49)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=50)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=51)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=52)
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=53)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}
