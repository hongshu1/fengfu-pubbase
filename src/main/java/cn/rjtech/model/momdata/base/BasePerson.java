package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 组织建模-人员档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BasePerson<M extends BasePerson<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**人员编码*/
    public static final String CPSN_NUM = "cPsn_Num";
    /**姓名*/
    public static final String CPSN_NAME = "cPsn_Name";
    /**部门编码*/
    public static final String CDEPT_NUM = "cDept_num";
    /**显示序号*/
    public static final String IRECORDID = "iRecordID";
    /**出生日期*/
    public static final String DBIRTHDATE = "dBirthDate";
    /**证件号码*/
    public static final String VIDNO = "vIDNo";
    /**曾用名*/
    public static final String VALIANAME = "vAliaName";
    /**参加工作时间*/
    public static final String DJOINWORKDATE = "dJoinworkDate";
    /**进入本行业时间*/
    public static final String DENTERDATE = "dEnterDate";
    /**转正时间*/
    public static final String DREGULARDATE = "dRegularDate";
    /**社会保障号*/
    public static final String VSSNO = "vSSNo";
    /**考勤卡号*/
    public static final String VCARDNO = "vCardNo";
    /**最后修改时间*/
    public static final String DLASTDATE = "dLastDate";
    /**与工资同步状态标志*/
    public static final String VSTATUS1 = "vstatus1";
    /**是否业务员*/
    public static final String BPSNPERSON = "bPsnPerson";
    /**手机*/
    public static final String CPSNMOBILEPHONE = "cPsnMobilePhone";
    /**家庭电话*/
    public static final String CPSNFPHONE = "cPsnFPhone";
    /**办公电话*/
    public static final String CPSNOPHONE = "cPsnOPhone";
    /**内线电话*/
    public static final String CPSNINPHONE = "cPsnInPhone";
    /**Email*/
    public static final String CPSNEMAIL = "cPsnEmail";
    /**通讯地址*/
    public static final String CPSNPOSTADDR = "cPsnPostAddr";
    /**邮政编码*/
    public static final String CPSNPOSTCODE = "cPsnPostCode";
    /**家庭住址*/
    public static final String CPSNFADDR = "cPsnFAddr";
    /**QQ*/
    public static final String CPSNQQCODE = "cPsnQQCode";
    /**个人网址*/
    public static final String CPSNURL = "cPsnURL";
    /**工位*/
    public static final String CPSNOSEAT = "CpsnOSeat";
    /**到职日期*/
    public static final String DENTERUNITDATE = "dEnterUnitDate";
    /**人员属性*/
    public static final String CPSNPROPERTY = "cPsnProperty";
    /**银行*/
    public static final String CPSNBANKCODE = "cPsnBankCode";
    /**账号*/
    public static final String CPSNACCOUNT = "cPsnAccount";
    /**是否是试用人员*/
    public static final String BPROBATION = "bProbation";
    /**班组*/
    public static final String CDUTYCLASS = "cDutyclass";
    /**离职日期*/
    public static final String DLEAVEDATE = "dLeaveDate";
    /**用工形式*/
    public static final String EMPLOYMENTFORM = "EmploymentForm";
    /**是否排班锁定*/
    public static final String BDUTYLOCK = "bDutyLock";
    /**是否营业员*/
    public static final String BPSNSHOP = "bpsnshop";
    /**发卡状态*/
    public static final String CARDSTATE = "CardState";
    /**合同初签日期*/
    public static final String FIRSTHTBEGINDATE = "FirstHTBeginDate";
    /**合同到期日期*/
    public static final String LASTHTENDDATE = "LastHTEndDate";
    /**英文名*/
    public static final String CPSN_NAMEEN = "cPsn_NameEN";
    /**年龄*/
    public static final String SYSAGE = "sysAge";
    /**司龄*/
    public static final String SYSCOMPAGE = "SysCompage";
    /**工龄*/
    public static final String SYSWORKAGE = "SysWorkAge";
    /**福利地区*/
    public static final String CREGION = "CRegion";
    /**户口性质*/
    public static final String NATRUETYPE = "NatrueType";
    /**所属用户ID*/
    public static final String IUSERID = "iUserId";
    /**性别：1. 男 2. 女*/
    public static final String ISEX = "iSex";
    /**电子卡号*/
    public static final String CECARDNO = "cEcardNo";
    /**入职日期*/
    public static final String DHIREDATE = "dHireDate";
    /**是否启用：0. 否 1. 是*/
    public static final String ISENABLED = "isEnabled";
    /**来源：1. MES 2. U8*/
    public static final String ISOURCE = "iSource";
    /**来源ID, U8为hr_hi_person的cpsn_num*/
    public static final String ISOURCEID = "iSourceId";
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
    /**工号*/
    public static final String JOBNUMBER = "JobNumber";
    /**人员类别编码，增加字典：rPersonType*/
    public static final String RPERSONTYPE = "rPersonType";
    /**雇佣状态编码，增加字典：rEmployState*/
    public static final String REMPLOYSTATE = "rEmployState";
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
	 * 人员编码
	 */
	public M setCpsnNum(java.lang.String cpsnNum) {
		set("cPsn_Num", cpsnNum);
		return (M)this;
	}

	/**
	 * 人员编码
	 */
	@JBoltField(name="cpsnnum" ,columnName="cPsn_Num",type="String", remark="人员编码", required=true, maxLength=20, fixed=0, order=5)
	@JSONField(name = "cpsnnum")
	public java.lang.String getCpsnNum() {
		return getStr("cPsn_Num");
	}

	/**
	 * 姓名
	 */
	public M setCpsnName(java.lang.String cpsnName) {
		set("cPsn_Name", cpsnName);
		return (M)this;
	}

	/**
	 * 姓名
	 */
	@JBoltField(name="cpsnname" ,columnName="cPsn_Name",type="String", remark="姓名", required=true, maxLength=40, fixed=0, order=6)
	@JSONField(name = "cpsnname")
	public java.lang.String getCpsnName() {
		return getStr("cPsn_Name");
	}

	/**
	 * 部门编码
	 */
	public M setCdeptNum(java.lang.String cdeptNum) {
		set("cDept_num", cdeptNum);
		return (M)this;
	}

	/**
	 * 部门编码
	 */
	@JBoltField(name="cdeptnum" ,columnName="cDept_num",type="String", remark="部门编码", required=false, maxLength=30, fixed=0, order=7)
	@JSONField(name = "cdeptnum")
	public java.lang.String getCdeptNum() {
		return getStr("cDept_num");
	}

	/**
	 * 显示序号
	 */
	public M setIRecordID(java.lang.Integer iRecordID) {
		set("iRecordID", iRecordID);
		return (M)this;
	}

	/**
	 * 显示序号
	 */
	@JBoltField(name="irecordid" ,columnName="iRecordID",type="Integer", remark="显示序号", required=false, maxLength=10, fixed=0, order=8)
	@JSONField(name = "irecordid")
	public java.lang.Integer getIRecordID() {
		return getInt("iRecordID");
	}

	/**
	 * 出生日期
	 */
	public M setDBirthDate(java.lang.String dBirthDate) {
		set("dBirthDate", dBirthDate);
		return (M)this;
	}

	/**
	 * 出生日期
	 */
	@JBoltField(name="dbirthdate" ,columnName="dBirthDate",type="String", remark="出生日期", required=false, maxLength=10, fixed=0, order=9)
	@JSONField(name = "dbirthdate")
	public java.lang.String getDBirthDate() {
		return getStr("dBirthDate");
	}

	/**
	 * 证件号码
	 */
	public M setVIDNo(java.lang.String vIDNo) {
		set("vIDNo", vIDNo);
		return (M)this;
	}

	/**
	 * 证件号码
	 */
	@JBoltField(name="vidno" ,columnName="vIDNo",type="String", remark="证件号码", required=false, maxLength=18, fixed=0, order=10)
	@JSONField(name = "vidno")
	public java.lang.String getVIDNo() {
		return getStr("vIDNo");
	}

	/**
	 * 曾用名
	 */
	public M setVAliaName(java.lang.String vAliaName) {
		set("vAliaName", vAliaName);
		return (M)this;
	}

	/**
	 * 曾用名
	 */
	@JBoltField(name="valianame" ,columnName="vAliaName",type="String", remark="曾用名", required=false, maxLength=20, fixed=0, order=11)
	@JSONField(name = "valianame")
	public java.lang.String getVAliaName() {
		return getStr("vAliaName");
	}

	/**
	 * 参加工作时间
	 */
	public M setDJoinworkDate(java.lang.String dJoinworkDate) {
		set("dJoinworkDate", dJoinworkDate);
		return (M)this;
	}

	/**
	 * 参加工作时间
	 */
	@JBoltField(name="djoinworkdate" ,columnName="dJoinworkDate",type="String", remark="参加工作时间", required=false, maxLength=10, fixed=0, order=12)
	@JSONField(name = "djoinworkdate")
	public java.lang.String getDJoinworkDate() {
		return getStr("dJoinworkDate");
	}

	/**
	 * 进入本行业时间
	 */
	public M setDEnterDate(java.lang.String dEnterDate) {
		set("dEnterDate", dEnterDate);
		return (M)this;
	}

	/**
	 * 进入本行业时间
	 */
	@JBoltField(name="denterdate" ,columnName="dEnterDate",type="String", remark="进入本行业时间", required=false, maxLength=10, fixed=0, order=13)
	@JSONField(name = "denterdate")
	public java.lang.String getDEnterDate() {
		return getStr("dEnterDate");
	}

	/**
	 * 转正时间
	 */
	public M setDRegularDate(java.lang.String dRegularDate) {
		set("dRegularDate", dRegularDate);
		return (M)this;
	}

	/**
	 * 转正时间
	 */
	@JBoltField(name="dregulardate" ,columnName="dRegularDate",type="String", remark="转正时间", required=false, maxLength=10, fixed=0, order=14)
	@JSONField(name = "dregulardate")
	public java.lang.String getDRegularDate() {
		return getStr("dRegularDate");
	}

	/**
	 * 社会保障号
	 */
	public M setVSSNo(java.lang.String vSSNo) {
		set("vSSNo", vSSNo);
		return (M)this;
	}

	/**
	 * 社会保障号
	 */
	@JBoltField(name="vssno" ,columnName="vSSNo",type="String", remark="社会保障号", required=false, maxLength=25, fixed=0, order=15)
	@JSONField(name = "vssno")
	public java.lang.String getVSSNo() {
		return getStr("vSSNo");
	}

	/**
	 * 考勤卡号
	 */
	public M setVCardNo(java.lang.String vCardNo) {
		set("vCardNo", vCardNo);
		return (M)this;
	}

	/**
	 * 考勤卡号
	 */
	@JBoltField(name="vcardno" ,columnName="vCardNo",type="String", remark="考勤卡号", required=false, maxLength=20, fixed=0, order=16)
	@JSONField(name = "vcardno")
	public java.lang.String getVCardNo() {
		return getStr("vCardNo");
	}

	/**
	 * 最后修改时间
	 */
	public M setDLastDate(java.lang.String dLastDate) {
		set("dLastDate", dLastDate);
		return (M)this;
	}

	/**
	 * 最后修改时间
	 */
	@JBoltField(name="dlastdate" ,columnName="dLastDate",type="String", remark="最后修改时间", required=false, maxLength=19, fixed=0, order=17)
	@JSONField(name = "dlastdate")
	public java.lang.String getDLastDate() {
		return getStr("dLastDate");
	}

	/**
	 * 与工资同步状态标志
	 */
	public M setVstatus1(java.lang.String vstatus1) {
		set("vstatus1", vstatus1);
		return (M)this;
	}

	/**
	 * 与工资同步状态标志
	 */
	@JBoltField(name="vstatus1" ,columnName="vstatus1",type="String", remark="与工资同步状态标志", required=false, maxLength=50, fixed=0, order=18)
	@JSONField(name = "vstatus1")
	public java.lang.String getVstatus1() {
		return getStr("vstatus1");
	}

	/**
	 * 是否业务员
	 */
	public M setBPsnPerson(java.lang.Boolean bPsnPerson) {
		set("bPsnPerson", bPsnPerson);
		return (M)this;
	}

	/**
	 * 是否业务员
	 */
	@JBoltField(name="bpsnperson" ,columnName="bPsnPerson",type="Boolean", remark="是否业务员", required=true, maxLength=1, fixed=0, order=19)
	@JSONField(name = "bpsnperson")
	public java.lang.Boolean getBPsnPerson() {
		return getBoolean("bPsnPerson");
	}

	/**
	 * 手机
	 */
	public M setCPsnMobilePhone(java.lang.String cPsnMobilePhone) {
		set("cPsnMobilePhone", cPsnMobilePhone);
		return (M)this;
	}

	/**
	 * 手机
	 */
	@JBoltField(name="cpsnmobilephone" ,columnName="cPsnMobilePhone",type="String", remark="手机", required=false, maxLength=100, fixed=0, order=20)
	@JSONField(name = "cpsnmobilephone")
	public java.lang.String getCPsnMobilePhone() {
		return getStr("cPsnMobilePhone");
	}

	/**
	 * 家庭电话
	 */
	public M setCPsnFPhone(java.lang.String cPsnFPhone) {
		set("cPsnFPhone", cPsnFPhone);
		return (M)this;
	}

	/**
	 * 家庭电话
	 */
	@JBoltField(name="cpsnfphone" ,columnName="cPsnFPhone",type="String", remark="家庭电话", required=false, maxLength=100, fixed=0, order=21)
	@JSONField(name = "cpsnfphone")
	public java.lang.String getCPsnFPhone() {
		return getStr("cPsnFPhone");
	}

	/**
	 * 办公电话
	 */
	public M setCPsnOPhone(java.lang.String cPsnOPhone) {
		set("cPsnOPhone", cPsnOPhone);
		return (M)this;
	}

	/**
	 * 办公电话
	 */
	@JBoltField(name="cpsnophone" ,columnName="cPsnOPhone",type="String", remark="办公电话", required=false, maxLength=100, fixed=0, order=22)
	@JSONField(name = "cpsnophone")
	public java.lang.String getCPsnOPhone() {
		return getStr("cPsnOPhone");
	}

	/**
	 * 内线电话
	 */
	public M setCPsnInPhone(java.lang.String cPsnInPhone) {
		set("cPsnInPhone", cPsnInPhone);
		return (M)this;
	}

	/**
	 * 内线电话
	 */
	@JBoltField(name="cpsninphone" ,columnName="cPsnInPhone",type="String", remark="内线电话", required=false, maxLength=100, fixed=0, order=23)
	@JSONField(name = "cpsninphone")
	public java.lang.String getCPsnInPhone() {
		return getStr("cPsnInPhone");
	}

	/**
	 * Email
	 */
	public M setCPsnEmail(java.lang.String cPsnEmail) {
		set("cPsnEmail", cPsnEmail);
		return (M)this;
	}

	/**
	 * Email
	 */
	@JBoltField(name="cpsnemail" ,columnName="cPsnEmail",type="String", remark="Email", required=false, maxLength=100, fixed=0, order=24)
	@JSONField(name = "cpsnemail")
	public java.lang.String getCPsnEmail() {
		return getStr("cPsnEmail");
	}

	/**
	 * 通讯地址
	 */
	public M setCPsnPostAddr(java.lang.String cPsnPostAddr) {
		set("cPsnPostAddr", cPsnPostAddr);
		return (M)this;
	}

	/**
	 * 通讯地址
	 */
	@JBoltField(name="cpsnpostaddr" ,columnName="cPsnPostAddr",type="String", remark="通讯地址", required=false, maxLength=255, fixed=0, order=25)
	@JSONField(name = "cpsnpostaddr")
	public java.lang.String getCPsnPostAddr() {
		return getStr("cPsnPostAddr");
	}

	/**
	 * 邮政编码
	 */
	public M setCPsnPostCode(java.lang.String cPsnPostCode) {
		set("cPsnPostCode", cPsnPostCode);
		return (M)this;
	}

	/**
	 * 邮政编码
	 */
	@JBoltField(name="cpsnpostcode" ,columnName="cPsnPostCode",type="String", remark="邮政编码", required=false, maxLength=6, fixed=0, order=26)
	@JSONField(name = "cpsnpostcode")
	public java.lang.String getCPsnPostCode() {
		return getStr("cPsnPostCode");
	}

	/**
	 * 家庭住址
	 */
	public M setCPsnFAddr(java.lang.String cPsnFAddr) {
		set("cPsnFAddr", cPsnFAddr);
		return (M)this;
	}

	/**
	 * 家庭住址
	 */
	@JBoltField(name="cpsnfaddr" ,columnName="cPsnFAddr",type="String", remark="家庭住址", required=false, maxLength=255, fixed=0, order=27)
	@JSONField(name = "cpsnfaddr")
	public java.lang.String getCPsnFAddr() {
		return getStr("cPsnFAddr");
	}

	/**
	 * QQ
	 */
	public M setCPsnQQCode(java.lang.String cPsnQQCode) {
		set("cPsnQQCode", cPsnQQCode);
		return (M)this;
	}

	/**
	 * QQ
	 */
	@JBoltField(name="cpsnqqcode" ,columnName="cPsnQQCode",type="String", remark="QQ", required=false, maxLength=20, fixed=0, order=28)
	@JSONField(name = "cpsnqqcode")
	public java.lang.String getCPsnQQCode() {
		return getStr("cPsnQQCode");
	}

	/**
	 * 个人网址
	 */
	public M setCPsnURL(java.lang.String cPsnURL) {
		set("cPsnURL", cPsnURL);
		return (M)this;
	}

	/**
	 * 个人网址
	 */
	@JBoltField(name="cpsnurl" ,columnName="cPsnURL",type="String", remark="个人网址", required=false, maxLength=50, fixed=0, order=29)
	@JSONField(name = "cpsnurl")
	public java.lang.String getCPsnURL() {
		return getStr("cPsnURL");
	}

	/**
	 * 工位
	 */
	public M setCpsnOSeat(java.lang.String CpsnOSeat) {
		set("CpsnOSeat", CpsnOSeat);
		return (M)this;
	}

	/**
	 * 工位
	 */
	@JBoltField(name="cpsnoseat" ,columnName="CpsnOSeat",type="String", remark="工位", required=false, maxLength=20, fixed=0, order=30)
	@JSONField(name = "cpsnoseat")
	public java.lang.String getCpsnOSeat() {
		return getStr("CpsnOSeat");
	}

	/**
	 * 到职日期
	 */
	public M setDEnterUnitDate(java.lang.String dEnterUnitDate) {
		set("dEnterUnitDate", dEnterUnitDate);
		return (M)this;
	}

	/**
	 * 到职日期
	 */
	@JBoltField(name="denterunitdate" ,columnName="dEnterUnitDate",type="String", remark="到职日期", required=false, maxLength=10, fixed=0, order=31)
	@JSONField(name = "denterunitdate")
	public java.lang.String getDEnterUnitDate() {
		return getStr("dEnterUnitDate");
	}

	/**
	 * 人员属性
	 */
	public M setCPsnProperty(java.lang.String cPsnProperty) {
		set("cPsnProperty", cPsnProperty);
		return (M)this;
	}

	/**
	 * 人员属性
	 */
	@JBoltField(name="cpsnproperty" ,columnName="cPsnProperty",type="String", remark="人员属性", required=false, maxLength=20, fixed=0, order=32)
	@JSONField(name = "cpsnproperty")
	public java.lang.String getCPsnProperty() {
		return getStr("cPsnProperty");
	}

	/**
	 * 银行
	 */
	public M setCPsnBankCode(java.lang.String cPsnBankCode) {
		set("cPsnBankCode", cPsnBankCode);
		return (M)this;
	}

	/**
	 * 银行
	 */
	@JBoltField(name="cpsnbankcode" ,columnName="cPsnBankCode",type="String", remark="银行", required=false, maxLength=5, fixed=0, order=33)
	@JSONField(name = "cpsnbankcode")
	public java.lang.String getCPsnBankCode() {
		return getStr("cPsnBankCode");
	}

	/**
	 * 账号
	 */
	public M setCPsnAccount(java.lang.String cPsnAccount) {
		set("cPsnAccount", cPsnAccount);
		return (M)this;
	}

	/**
	 * 账号
	 */
	@JBoltField(name="cpsnaccount" ,columnName="cPsnAccount",type="String", remark="账号", required=false, maxLength=50, fixed=0, order=34)
	@JSONField(name = "cpsnaccount")
	public java.lang.String getCPsnAccount() {
		return getStr("cPsnAccount");
	}

	/**
	 * 是否是试用人员
	 */
	public M setBProbation(java.lang.Boolean bProbation) {
		set("bProbation", bProbation);
		return (M)this;
	}

	/**
	 * 是否是试用人员
	 */
	@JBoltField(name="bprobation" ,columnName="bProbation",type="Boolean", remark="是否是试用人员", required=true, maxLength=1, fixed=0, order=35)
	@JSONField(name = "bprobation")
	public java.lang.Boolean getBProbation() {
		return getBoolean("bProbation");
	}

	/**
	 * 班组
	 */
	public M setCDutyclass(java.lang.String cDutyclass) {
		set("cDutyclass", cDutyclass);
		return (M)this;
	}

	/**
	 * 班组
	 */
	@JBoltField(name="cdutyclass" ,columnName="cDutyclass",type="String", remark="班组", required=false, maxLength=50, fixed=0, order=36)
	@JSONField(name = "cdutyclass")
	public java.lang.String getCDutyclass() {
		return getStr("cDutyclass");
	}

	/**
	 * 离职日期
	 */
	public M setDLeaveDate(java.lang.String dLeaveDate) {
		set("dLeaveDate", dLeaveDate);
		return (M)this;
	}

	/**
	 * 离职日期
	 */
	@JBoltField(name="dleavedate" ,columnName="dLeaveDate",type="String", remark="离职日期", required=false, maxLength=10, fixed=0, order=37)
	@JSONField(name = "dleavedate")
	public java.lang.String getDLeaveDate() {
		return getStr("dLeaveDate");
	}

	/**
	 * 用工形式
	 */
	public M setEmploymentForm(java.lang.String EmploymentForm) {
		set("EmploymentForm", EmploymentForm);
		return (M)this;
	}

	/**
	 * 用工形式
	 */
	@JBoltField(name="employmentform" ,columnName="EmploymentForm",type="String", remark="用工形式", required=false, maxLength=40, fixed=0, order=38)
	@JSONField(name = "employmentform")
	public java.lang.String getEmploymentForm() {
		return getStr("EmploymentForm");
	}

	/**
	 * 是否排班锁定
	 */
	public M setBDutyLock(java.lang.Boolean bDutyLock) {
		set("bDutyLock", bDutyLock);
		return (M)this;
	}

	/**
	 * 是否排班锁定
	 */
	@JBoltField(name="bdutylock" ,columnName="bDutyLock",type="Boolean", remark="是否排班锁定", required=true, maxLength=1, fixed=0, order=39)
	@JSONField(name = "bdutylock")
	public java.lang.Boolean getBDutyLock() {
		return getBoolean("bDutyLock");
	}

	/**
	 * 是否营业员
	 */
	public M setBpsnshop(java.lang.Boolean bpsnshop) {
		set("bpsnshop", bpsnshop);
		return (M)this;
	}

	/**
	 * 是否营业员
	 */
	@JBoltField(name="bpsnshop" ,columnName="bpsnshop",type="Boolean", remark="是否营业员", required=false, maxLength=1, fixed=0, order=40)
	@JSONField(name = "bpsnshop")
	public java.lang.Boolean getBpsnshop() {
		return getBoolean("bpsnshop");
	}

	/**
	 * 发卡状态
	 */
	public M setCardState(java.lang.String CardState) {
		set("CardState", CardState);
		return (M)this;
	}

	/**
	 * 发卡状态
	 */
	@JBoltField(name="cardstate" ,columnName="CardState",type="String", remark="发卡状态", required=false, maxLength=40, fixed=0, order=41)
	@JSONField(name = "cardstate")
	public java.lang.String getCardState() {
		return getStr("CardState");
	}

	/**
	 * 合同初签日期
	 */
	public M setFirstHTBeginDate(java.lang.String FirstHTBeginDate) {
		set("FirstHTBeginDate", FirstHTBeginDate);
		return (M)this;
	}

	/**
	 * 合同初签日期
	 */
	@JBoltField(name="firsthtbegindate" ,columnName="FirstHTBeginDate",type="String", remark="合同初签日期", required=false, maxLength=10, fixed=0, order=42)
	@JSONField(name = "firsthtbegindate")
	public java.lang.String getFirstHTBeginDate() {
		return getStr("FirstHTBeginDate");
	}

	/**
	 * 合同到期日期
	 */
	public M setLastHTEndDate(java.lang.String LastHTEndDate) {
		set("LastHTEndDate", LastHTEndDate);
		return (M)this;
	}

	/**
	 * 合同到期日期
	 */
	@JBoltField(name="lasthtenddate" ,columnName="LastHTEndDate",type="String", remark="合同到期日期", required=false, maxLength=10, fixed=0, order=43)
	@JSONField(name = "lasthtenddate")
	public java.lang.String getLastHTEndDate() {
		return getStr("LastHTEndDate");
	}

	/**
	 * 英文名
	 */
	public M setCpsnNameen(java.lang.String cpsnNameen) {
		set("cPsn_NameEN", cpsnNameen);
		return (M)this;
	}

	/**
	 * 英文名
	 */
	@JBoltField(name="cpsnnameen" ,columnName="cPsn_NameEN",type="String", remark="英文名", required=false, maxLength=50, fixed=0, order=44)
	@JSONField(name = "cpsnnameen")
	public java.lang.String getCpsnNameen() {
		return getStr("cPsn_NameEN");
	}

	/**
	 * 年龄
	 */
	public M setSysAge(java.math.BigDecimal sysAge) {
		set("sysAge", sysAge);
		return (M)this;
	}

	/**
	 * 年龄
	 */
	@JBoltField(name="sysage" ,columnName="sysAge",type="BigDecimal", remark="年龄", required=false, maxLength=10, fixed=2, order=45)
	@JSONField(name = "sysage")
	public java.math.BigDecimal getSysAge() {
		return getBigDecimal("sysAge");
	}

	/**
	 * 司龄
	 */
	public M setSysCompage(java.math.BigDecimal SysCompage) {
		set("SysCompage", SysCompage);
		return (M)this;
	}

	/**
	 * 司龄
	 */
	@JBoltField(name="syscompage" ,columnName="SysCompage",type="BigDecimal", remark="司龄", required=false, maxLength=10, fixed=2, order=46)
	@JSONField(name = "syscompage")
	public java.math.BigDecimal getSysCompage() {
		return getBigDecimal("SysCompage");
	}

	/**
	 * 工龄
	 */
	public M setSysWorkAge(java.math.BigDecimal SysWorkAge) {
		set("SysWorkAge", SysWorkAge);
		return (M)this;
	}

	/**
	 * 工龄
	 */
	@JBoltField(name="sysworkage" ,columnName="SysWorkAge",type="BigDecimal", remark="工龄", required=false, maxLength=10, fixed=2, order=47)
	@JSONField(name = "sysworkage")
	public java.math.BigDecimal getSysWorkAge() {
		return getBigDecimal("SysWorkAge");
	}

	/**
	 * 福利地区
	 */
	public M setCRegion(java.lang.String CRegion) {
		set("CRegion", CRegion);
		return (M)this;
	}

	/**
	 * 福利地区
	 */
	@JBoltField(name="cregion" ,columnName="CRegion",type="String", remark="福利地区", required=false, maxLength=50, fixed=0, order=48)
	@JSONField(name = "cregion")
	public java.lang.String getCRegion() {
		return getStr("CRegion");
	}

	/**
	 * 户口性质
	 */
	public M setNatrueType(java.lang.String NatrueType) {
		set("NatrueType", NatrueType);
		return (M)this;
	}

	/**
	 * 户口性质
	 */
	@JBoltField(name="natruetype" ,columnName="NatrueType",type="String", remark="户口性质", required=false, maxLength=50, fixed=0, order=49)
	@JSONField(name = "natruetype")
	public java.lang.String getNatrueType() {
		return getStr("NatrueType");
	}

	/**
	 * 所属用户ID
	 */
	public M setIUserId(java.lang.Long iUserId) {
		set("iUserId", iUserId);
		return (M)this;
	}

	/**
	 * 所属用户ID
	 */
	@JBoltField(name="iuserid" ,columnName="iUserId",type="Long", remark="所属用户ID", required=false, maxLength=19, fixed=0, order=50)
	@JSONField(name = "iuserid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUserId() {
		return getLong("iUserId");
	}

	/**
	 * 性别：1. 男 2. 女
	 */
	public M setISex(java.lang.Integer iSex) {
		set("iSex", iSex);
		return (M)this;
	}

	/**
	 * 性别：1. 男 2. 女
	 */
	@JBoltField(name="isex" ,columnName="iSex",type="Integer", remark="性别：1. 男 2. 女", required=false, maxLength=10, fixed=0, order=51)
	@JSONField(name = "isex")
	public java.lang.Integer getISex() {
		return getInt("iSex");
	}

	/**
	 * 电子卡号
	 */
	public M setCEcardNo(java.lang.String cEcardNo) {
		set("cEcardNo", cEcardNo);
		return (M)this;
	}

	/**
	 * 电子卡号
	 */
	@JBoltField(name="cecardno" ,columnName="cEcardNo",type="String", remark="电子卡号", required=false, maxLength=30, fixed=0, order=52)
	@JSONField(name = "cecardno")
	public java.lang.String getCEcardNo() {
		return getStr("cEcardNo");
	}

	/**
	 * 入职日期
	 */
	public M setDHireDate(java.util.Date dHireDate) {
		set("dHireDate", dHireDate);
		return (M)this;
	}

	/**
	 * 入职日期
	 */
	@JBoltField(name="dhiredate" ,columnName="dHireDate",type="Date", remark="入职日期", required=false, maxLength=10, fixed=0, order=53)
	@JSONField(name = "dhiredate")
	public java.util.Date getDHireDate() {
		return getDate("dHireDate");
	}

	/**
	 * 是否启用：0. 否 1. 是
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用：0. 否 1. 是
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=54)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
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
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源：1. MES 2. U8", required=true, maxLength=10, fixed=0, order=55)
	@JSONField(name = "isource")
	public java.lang.Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源ID, U8为hr_hi_person的cpsn_num
	 */
	public M setISourceId(java.lang.String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源ID, U8为hr_hi_person的cpsn_num
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源ID, U8为hr_hi_person的cpsn_num", required=false, maxLength=32, fixed=0, order=56)
	@JSONField(name = "isourceid")
	public java.lang.String getISourceId() {
		return getStr("iSourceId");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=57)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=58)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=59)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=60)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=61)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=62)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=63)
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=64)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 工号
	 */
	public M setJobNumber(java.lang.String JobNumber) {
		set("JobNumber", JobNumber);
		return (M)this;
	}

	/**
	 * 工号
	 */
	@JBoltField(name="jobnumber" ,columnName="JobNumber",type="String", remark="工号", required=false, maxLength=50, fixed=0, order=65)
	@JSONField(name = "jobnumber")
	public java.lang.String getJobNumber() {
		return getStr("JobNumber");
	}

	/**
	 * 人员类别编码，增加字典：rPersonType
	 */
	public M setRPersonType(java.lang.String rPersonType) {
		set("rPersonType", rPersonType);
		return (M)this;
	}

	/**
	 * 人员类别编码，增加字典：rPersonType
	 */
	@JBoltField(name="rpersontype" ,columnName="rPersonType",type="String", remark="人员类别编码，增加字典：rPersonType", required=false, maxLength=10, fixed=0, order=66)
	@JSONField(name = "rpersontype")
	public java.lang.String getRPersonType() {
		return getStr("rPersonType");
	}

	/**
	 * 雇佣状态编码，增加字典：rEmployState
	 */
	public M setREmployState(java.lang.String rEmployState) {
		set("rEmployState", rEmployState);
		return (M)this;
	}

	/**
	 * 雇佣状态编码，增加字典：rEmployState
	 */
	@JBoltField(name="remploystate" ,columnName="rEmployState",type="String", remark="雇佣状态编码，增加字典：rEmployState", required=false, maxLength=10, fixed=0, order=67)
	@JSONField(name = "remploystate")
	public java.lang.String getREmployState() {
		return getStr("rEmployState");
	}

}

