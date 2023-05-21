package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量管理-制程异常品记录
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseProcessDefect<M extends BaseProcessDefect<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**领料单ID*/
    public static final String IISSUEID = "iIssueId";
    /**生产工单ID*/
    public static final String IMODOCID = "iMoDocId";
    /**生产部门ID*/
    public static final String IDEPARTMENTID = "iDepartmentId";
    /**需求日期*/
    public static final String DDEMANDDATE = "dDemandDate";
    /**状态：1. 已保存 2. 待审批 3. 已审批*/
    public static final String ISTATUS = "iStatus";
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
    /**处置区分*/
    public static final String CAPPROACH = "cApproach";
    /**不良数量*/
    public static final String IDQQTY = "iDqQty";
    /**责任区：1. 本工序 2. 其他*/
    public static final String IRESPTYPE = "iRespType";
    /**不良项目，字典编码，多个“,”分隔*/
    public static final String CBADNESSSNS = "cBadnessSns";
    /**再首发：1.首发 2.再发*/
    public static final String ISFIRSTTIME = "isFirstTime";
    /**不良内容描述*/
    public static final String CDESC = "cDesc";
    /**工序名称*/
    public static final String PROCESSNAME = "ProcessName";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**异常品单号*/
    public static final String CDOCNO = "cDocNo";
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
	 * 领料单ID
	 */
	public M setIIssueId(java.lang.Long iIssueId) {
		set("iIssueId", iIssueId);
		return (M)this;
	}

	/**
	 * 领料单ID
	 */
	@JBoltField(name="iissueid" ,columnName="iIssueId",type="Long", remark="领料单ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "iissueid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIIssueId() {
		return getLong("iIssueId");
	}

	/**
	 * 生产工单ID
	 */
	public M setIMoDocId(java.lang.Long iMoDocId) {
		set("iMoDocId", iMoDocId);
		return (M)this;
	}

	/**
	 * 生产工单ID
	 */
	@JBoltField(name="imodocid" ,columnName="iMoDocId",type="Long", remark="生产工单ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "imodocid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoDocId() {
		return getLong("iMoDocId");
	}

	/**
	 * 生产部门ID
	 */
	public M setIDepartmentId(java.lang.Long iDepartmentId) {
		set("iDepartmentId", iDepartmentId);
		return (M)this;
	}

	/**
	 * 生产部门ID
	 */
	@JBoltField(name="idepartmentid" ,columnName="iDepartmentId",type="Long", remark="生产部门ID", required=true, maxLength=19, fixed=0, order=7)
	@JSONField(name = "idepartmentid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDepartmentId() {
		return getLong("iDepartmentId");
	}

	/**
	 * 需求日期
	 */
	public M setDDemandDate(java.util.Date dDemandDate) {
		set("dDemandDate", dDemandDate);
		return (M)this;
	}

	/**
	 * 需求日期
	 */
	@JBoltField(name="ddemanddate" ,columnName="dDemandDate",type="Date", remark="需求日期", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "ddemanddate")
	public java.util.Date getDDemandDate() {
		return getDate("dDemandDate");
	}

	/**
	 * 状态：1. 已保存 2. 待审批 3. 已审批
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 状态：1. 已保存 2. 待审批 3. 已审批
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="状态：1. 已保存 2. 待审批 3. 已审批", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=10)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=11)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=12)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=13)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=14)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=15)
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=16)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

	/**
	 * 处置区分
	 */
	public M setCApproach(java.lang.String cApproach) {
		set("cApproach", cApproach);
		return (M)this;
	}

	/**
	 * 处置区分
	 */
	@JBoltField(name="capproach" ,columnName="cApproach",type="String", remark="处置区分", required=false, maxLength=50, fixed=0, order=17)
	@JSONField(name = "capproach")
	public java.lang.String getCApproach() {
		return getStr("cApproach");
	}

	/**
	 * 不良数量
	 */
	public M setIDqQty(java.math.BigDecimal iDqQty) {
		set("iDqQty", iDqQty);
		return (M)this;
	}

	/**
	 * 不良数量
	 */
	@JBoltField(name="idqqty" ,columnName="iDqQty",type="BigDecimal", remark="不良数量", required=false, maxLength=18, fixed=2, order=18)
	@JSONField(name = "idqqty")
	public java.math.BigDecimal getIDqQty() {
		return getBigDecimal("iDqQty");
	}

	/**
	 * 责任区：1. 本工序 2. 其他
	 */
	public M setIRespType(java.lang.Integer iRespType) {
		set("iRespType", iRespType);
		return (M)this;
	}

	/**
	 * 责任区：1. 本工序 2. 其他
	 */
	@JBoltField(name="iresptype" ,columnName="iRespType",type="Integer", remark="责任区：1. 本工序 2. 其他", required=true, maxLength=10, fixed=0, order=19)
	@JSONField(name = "iresptype")
	public java.lang.Integer getIRespType() {
		return getInt("iRespType");
	}

	/**
	 * 不良项目，字典编码，多个“,”分隔
	 */
	public M setCBadnessSns(java.lang.String cBadnessSns) {
		set("cBadnessSns", cBadnessSns);
		return (M)this;
	}

	/**
	 * 不良项目，字典编码，多个“,”分隔
	 */
	@JBoltField(name="cbadnesssns" ,columnName="cBadnessSns",type="String", remark="不良项目，字典编码，多个“,”分隔", required=true, maxLength=200, fixed=0, order=20)
	@JSONField(name = "cbadnesssns")
	public java.lang.String getCBadnessSns() {
		return getStr("cBadnessSns");
	}

	/**
	 * 再首发：1.首发 2.再发
	 */
	public M setIsFirstTime(java.lang.Boolean isFirstTime) {
		set("isFirstTime", isFirstTime);
		return (M)this;
	}

	/**
	 * 再首发：1.首发 2.再发
	 */
	@JBoltField(name="isfirsttime" ,columnName="isFirstTime",type="Boolean", remark="再首发：1.首发 2.再发", required=true, maxLength=1, fixed=0, order=21)
	@JSONField(name = "isfirsttime")
	public java.lang.Boolean getIsFirstTime() {
		return getBoolean("isFirstTime");
	}

	/**
	 * 不良内容描述
	 */
	public M setCDesc(java.lang.String cDesc) {
		set("cDesc", cDesc);
		return (M)this;
	}

	/**
	 * 不良内容描述
	 */
	@JBoltField(name="cdesc" ,columnName="cDesc",type="String", remark="不良内容描述", required=false, maxLength=500, fixed=0, order=22)
	@JSONField(name = "cdesc")
	public java.lang.String getCDesc() {
		return getStr("cDesc");
	}

	/**
	 * 工序名称
	 */
	public M setProcessName(java.lang.String ProcessName) {
		set("ProcessName", ProcessName);
		return (M)this;
	}

	/**
	 * 工序名称
	 */
	@JBoltField(name="processname" ,columnName="ProcessName",type="String", remark="工序名称", required=true, maxLength=50, fixed=0, order=23)
	@JSONField(name = "processname")
	public java.lang.String getProcessName() {
		return getStr("ProcessName");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=24)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 异常品单号
	 */
	public M setCDocNo(java.lang.String cDocNo) {
		set("cDocNo", cDocNo);
		return (M)this;
	}

	/**
	 * 异常品单号
	 */
	@JBoltField(name="cdocno" ,columnName="cDocNo",type="String", remark="异常品单号", required=true, maxLength=50, fixed=0, order=25)
	@JSONField(name = "cdocno")
	public java.lang.String getCDocNo() {
		return getStr("cDocNo");
	}

}

