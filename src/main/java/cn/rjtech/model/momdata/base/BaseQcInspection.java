package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量管理-工程内品质巡查
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseQcInspection<M extends BaseQcInspection<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**连锁号，取自数据字典，编码*/
    public static final String CCHAINNO = "cChainNo";
    /**连锁号名称，取自数据字典，名称*/
    public static final String CCHAINNAME = "cChainName";
    /**是否首发：0. 再发 1. 首发*/
    public static final String ISFIRSTCASE = "isFirstCase";
    /**发现日期*/
    public static final String DRECORDDATE = "dRecordDate";
    /**品质担当人员ID*/
    public static final String IQCDUTYPERSONID = "iQcDutyPersonId";
    /**责任科室部门ID*/
    public static final String IQCDUTYDEPARTMENTID = "iQcDutyDepartmentId";
    /**位置/区域*/
    public static final String CPLACE = "cPlace";
    /**问题点*/
    public static final String CPROBLEM = "cProblem";
    /**原因分析*/
    public static final String CANALYSIS = "cAnalysis";
    /**对策*/
    public static final String CMEASURE = "cMeasure";
    /**日期*/
    public static final String DDATE = "dDate";
    /**担当*/
    public static final String IDUTYPERSONID = "iDutyPersonId";
    /**判定: 1. OK 2. NG*/
    public static final String IESTIMATE = "iEstimate";
    /**对策上传*/
    public static final String CMEASUREATTACHMENTS = "cMeasureAttachments";
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
    public static final String ISDELETED = "IsDeleted";
	/**巡查单号*/
	public static final String INSPECTIONNO = "InspectionNo";
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
	 * 连锁号，取自数据字典，编码
	 */
	public M setCChainNo(java.lang.String cChainNo) {
		set("cChainNo", cChainNo);
		return (M)this;
	}

	/**
	 * 连锁号，取自数据字典，编码
	 */
	@JBoltField(name="cchainno" ,columnName="cChainNo",type="String", remark="连锁号，取自数据字典，编码", required=true, maxLength=40, fixed=0, order=5)
	@JSONField(name = "cchainno")
	public java.lang.String getCChainNo() {
		return getStr("cChainNo");
	}

	/**
	 * 连锁号名称，取自数据字典，名称
	 */
	public M setCChainName(java.lang.String cChainName) {
		set("cChainName", cChainName);
		return (M)this;
	}

	/**
	 * 连锁号名称，取自数据字典，名称
	 */
	@JBoltField(name="cchainname" ,columnName="cChainName",type="String", remark="连锁号名称，取自数据字典，名称", required=true, maxLength=50, fixed=0, order=6)
	@JSONField(name = "cchainname")
	public java.lang.String getCChainName() {
		return getStr("cChainName");
	}

	/**
	 * 是否首发：0. 再发 1. 首发
	 */
	public M setIsFirstCase(java.lang.Boolean isFirstCase) {
		set("isFirstCase", isFirstCase);
		return (M)this;
	}

	/**
	 * 是否首发：0. 再发 1. 首发
	 */
	@JBoltField(name="isfirstcase" ,columnName="isFirstCase",type="Boolean", remark="是否首发：0. 再发 1. 首发", required=true, maxLength=1, fixed=0, order=7)
	@JSONField(name = "isfirstcase")
	public java.lang.Boolean getIsFirstCase() {
		return getBoolean("isFirstCase");
	}

	/**
	 * 发现日期
	 */
	public M setDRecordDate(java.util.Date dRecordDate) {
		set("dRecordDate", dRecordDate);
		return (M)this;
	}

	/**
	 * 发现日期
	 */
	@JBoltField(name="drecorddate" ,columnName="dRecordDate",type="Date", remark="发现日期", required=false, maxLength=10, fixed=0, order=8)
	@JSONField(name = "drecorddate")
	public java.util.Date getDRecordDate() {
		return getDate("dRecordDate");
	}

	/**
	 * 品质担当人员ID
	 */
	public M setIQcDutyPersonId(java.lang.Long iQcDutyPersonId) {
		set("iQcDutyPersonId", iQcDutyPersonId);
		return (M)this;
	}

	/**
	 * 品质担当人员ID
	 */
	@JBoltField(name="iqcdutypersonid" ,columnName="iQcDutyPersonId",type="Long", remark="品质担当人员ID", required=false, maxLength=19, fixed=0, order=9)
	@JSONField(name = "iqcdutypersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcDutyPersonId() {
		return getLong("iQcDutyPersonId");
	}

	/**
	 * 责任科室部门ID
	 */
	public M setIQcDutyDepartmentId(java.lang.Long iQcDutyDepartmentId) {
		set("iQcDutyDepartmentId", iQcDutyDepartmentId);
		return (M)this;
	}

	/**
	 * 责任科室部门ID
	 */
	@JBoltField(name="iqcdutydepartmentid" ,columnName="iQcDutyDepartmentId",type="Long", remark="责任科室部门ID", required=false, maxLength=19, fixed=0, order=10)
	@JSONField(name = "iqcdutydepartmentid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcDutyDepartmentId() {
		return getLong("iQcDutyDepartmentId");
	}

	/**
	 * 位置/区域
	 */
	public M setCPlace(java.lang.String cPlace) {
		set("cPlace", cPlace);
		return (M)this;
	}

	/**
	 * 位置/区域
	 */
	@JBoltField(name="cplace" ,columnName="cPlace",type="String", remark="位置/区域", required=false, maxLength=200, fixed=0, order=11)
	@JSONField(name = "cplace")
	public java.lang.String getCPlace() {
		return getStr("cPlace");
	}

	/**
	 * 问题点
	 */
	public M setCProblem(java.lang.String cProblem) {
		set("cProblem", cProblem);
		return (M)this;
	}

	/**
	 * 问题点
	 */
	@JBoltField(name="cproblem" ,columnName="cProblem",type="String", remark="问题点", required=false, maxLength=200, fixed=0, order=12)
	@JSONField(name = "cproblem")
	public java.lang.String getCProblem() {
		return getStr("cProblem");
	}

	/**
	 * 原因分析
	 */
	public M setCAnalysis(java.lang.String cAnalysis) {
		set("cAnalysis", cAnalysis);
		return (M)this;
	}

	/**
	 * 原因分析
	 */
	@JBoltField(name="canalysis" ,columnName="cAnalysis",type="String", remark="原因分析", required=false, maxLength=200, fixed=0, order=13)
	@JSONField(name = "canalysis")
	public java.lang.String getCAnalysis() {
		return getStr("cAnalysis");
	}

	/**
	 * 对策
	 */
	public M setCMeasure(java.lang.String cMeasure) {
		set("cMeasure", cMeasure);
		return (M)this;
	}

	/**
	 * 对策
	 */
	@JBoltField(name="cmeasure" ,columnName="cMeasure",type="String", remark="对策", required=false, maxLength=200, fixed=0, order=14)
	@JSONField(name = "cmeasure")
	public java.lang.String getCMeasure() {
		return getStr("cMeasure");
	}

	/**
	 * 日期
	 */
	public M setDDate(java.util.Date dDate) {
		set("dDate", dDate);
		return (M)this;
	}

	/**
	 * 日期
	 */
	@JBoltField(name="ddate" ,columnName="dDate",type="Date", remark="日期", required=false, maxLength=10, fixed=0, order=15)
	@JSONField(name = "ddate")
	public java.util.Date getDDate() {
		return getDate("dDate");
	}

	/**
	 * 担当
	 */
	public M setIDutyPersonId(java.lang.Long iDutyPersonId) {
		set("iDutyPersonId", iDutyPersonId);
		return (M)this;
	}

	/**
	 * 担当
	 */
	@JBoltField(name="idutypersonid" ,columnName="iDutyPersonId",type="Long", remark="担当", required=false, maxLength=19, fixed=0, order=16)
	@JSONField(name = "idutypersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDutyPersonId() {
		return getLong("iDutyPersonId");
	}

	/**
	 * 判定: 1. OK 2. NG
	 */
	public M setIEstimate(java.lang.Integer iEstimate) {
		set("iEstimate", iEstimate);
		return (M)this;
	}

	/**
	 * 判定: 1. OK 2. NG
	 */
	@JBoltField(name="iestimate" ,columnName="iEstimate",type="Integer", remark="判定: 1. OK 2. NG", required=true, maxLength=10, fixed=0, order=17)
	@JSONField(name = "iestimate")
	public java.lang.Integer getIEstimate() {
		return getInt("iEstimate");
	}

	/**
	 * 对策上传
	 */
	public M setCMeasureAttachments(java.lang.String cMeasureAttachments) {
		set("cMeasureAttachments", cMeasureAttachments);
		return (M)this;
	}

	/**
	 * 对策上传
	 */
	@JBoltField(name="cmeasureattachments" ,columnName="cMeasureAttachments",type="String", remark="对策上传", required=false, maxLength=2147483647, fixed=0, order=18)
	@JSONField(name = "cmeasureattachments")
	public java.lang.String getCMeasureAttachments() {
		return getStr("cMeasureAttachments");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=19)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=20)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=21)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=22)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=23)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=24)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=25)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}


	/**
	 * 巡查单号
	 */
	public M setCInspectionNo(java.lang.String InspectionNo) {
		set("InspectionNo", InspectionNo);
		return (M)this;
	}

	/**
	 * 巡查单号
	 */
	@JBoltField(name="inspectionno" ,columnName="InspectionNo",type="String", remark="巡查单号", required=true, maxLength=50, fixed=0, order=5)
	@JSONField(name = "inspectionno")
	public java.lang.String getCInspectionNo() {
		return getStr("InspectionNo");
	}

}

