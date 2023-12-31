package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 禀议书主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseProposalm<M extends BaseProposalm<M>> extends JBoltBaseModel<M>{
    
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**禀议书编号*/
    public static final String CPROPOSALNO = "cProposalNo";
    /**申请部门*/
    public static final String CDEPCODE = "cDepCode";
    /**申请日期*/
    public static final String DAPPLYDATE = "dApplyDate";
    /**申请人编码*/
    public static final String CAPPLYPERSONCODE = "cApplyPersonCode";
    /**申请人名称*/
    public static final String CAPPLYPERSONNAME = "cApplyPersonName";
    /**项目名称*/
    public static final String CPROJECTNAME = "cProjectName";
    /**项目编码*/
    public static final String CPROJECTCODE = "cProjectCode";
    /**目的区分，字典SN*/
    public static final String CPURPOSESN = "cPurposeSn";
    /**类别区分*/
    public static final String ICATEGORYID = "iCategoryId";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**事业计划: 0-计划外 1-计划内 2-提前实施*/
    public static final String ISSCHEDULED = "IsScheduled";
    /**审批状态*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**原禀议书ID*/
    public static final String ISOURCEPROPOSALID = "iSourceProposalId";
    /**说明*/
    public static final String CDESC = "cDesc";
    /**原禀议书编号*/
    public static final String CSOURCEPROPOSALNO = "cSourceProposalNo";
    /**本币无税金额*/
    public static final String INATMONEY = "iNatMoney";
    /**本币价税合计*/
    public static final String INATSUM = "iNatSum";
    /**是否追加*/
    public static final String ISSUPPLEMENTAL = "IsSupplemental";
    /**预算总金额（无税）*/
    public static final String IBUDGETMONEY = "iBudgetMoney";
    /**预算总金额（含税）*/
    public static final String IBUDGETSUM = "iBudgetSum";
    /**追加申请金额（无税）*/
    public static final String ISUPPLEMENTALMONEY = "iSupplementalMoney";
    /**追加申请金额（含税）*/
    public static final String ISUPPLEMENTALSUM = "iSupplementalSum";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
    /**生效时间*/
    public static final String DEFFECTIVETIME = "dEffectiveTime";
    /**累计申请金额（无税）*/
    public static final String ITOTALMONEY = "iTotalMoney";
    /**累计申请金额（含税）*/
    public static final String ITOTALSUM = "iTotalSum";
    /**剩余预算金额（无税）*/
    public static final String IBUDGETREMAINMONEY = "iBudgetRemainMoney";
    /**剩余预算金额（含税）*/
    public static final String IBUDGETREMAINSUM = "iBudgetRemainSum";
    /**追加说明*/
    public static final String CSUPPLEMENTALDESC = "cSupplementalDesc";
    /**生效状态(1.未生效 2.已生效 3.失效)*/
    public static final String IEFFECTIVESTATUS = "iEffectiveStatus";
    /**追加的初始禀议书ID*/
    public static final String IFIRSTSOURCEPROPOSALID = "iFirstSourceProposalId";
    /**审批方式：1. 审批状态 2. 审批流*/
    public static final String IAUDITWAY = "iAuditWay";
    /**提审时间*/
    public static final String DSUBMITTIME = "dSubmitTime";
    /**审核人ID，不走审批流时有该值*/
    public static final String IAUDITBY = "iAuditBy";
    /**审核人名称，不走审批流时有该值*/
    public static final String CAUDITNAME = "cAuditName";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "IsDeleted";
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
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=20, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public java.lang.String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 禀议书编号
	 */
	public M setCProposalNo(java.lang.String cProposalNo) {
		set("cProposalNo", cProposalNo);
		return (M)this;
	}

	/**
	 * 禀议书编号
	 */
	@JBoltField(name="cproposalno" ,columnName="cProposalNo",type="String", remark="禀议书编号", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "cproposalno")
	public java.lang.String getCProposalNo() {
		return getStr("cProposalNo");
	}

	/**
	 * 申请部门
	 */
	public M setCDepCode(java.lang.String cDepCode) {
		set("cDepCode", cDepCode);
		return (M)this;
	}

	/**
	 * 申请部门
	 */
	@JBoltField(name="cdepcode" ,columnName="cDepCode",type="String", remark="申请部门", required=true, maxLength=20, fixed=0, order=5)
	@JSONField(name = "cdepcode")
	public java.lang.String getCDepCode() {
		return getStr("cDepCode");
	}

	/**
	 * 申请日期
	 */
	public M setDApplyDate(java.util.Date dApplyDate) {
		set("dApplyDate", dApplyDate);
		return (M)this;
	}

	/**
	 * 申请日期
	 */
	@JBoltField(name="dapplydate" ,columnName="dApplyDate",type="Date", remark="申请日期", required=true, maxLength=23, fixed=3, order=6)
	@JSONField(name = "dapplydate")
	public java.util.Date getDApplyDate() {
		return getDate("dApplyDate");
	}

	/**
	 * 申请人编码
	 */
	public M setCApplyPersonCode(java.lang.String cApplyPersonCode) {
		set("cApplyPersonCode", cApplyPersonCode);
		return (M)this;
	}

	/**
	 * 申请人编码
	 */
	@JBoltField(name="capplypersoncode" ,columnName="cApplyPersonCode",type="String", remark="申请人编码", required=true, maxLength=20, fixed=0, order=7)
	@JSONField(name = "capplypersoncode")
	public java.lang.String getCApplyPersonCode() {
		return getStr("cApplyPersonCode");
	}

	/**
	 * 申请人名称
	 */
	public M setCApplyPersonName(java.lang.String cApplyPersonName) {
		set("cApplyPersonName", cApplyPersonName);
		return (M)this;
	}

	/**
	 * 申请人名称
	 */
	@JBoltField(name="capplypersonname" ,columnName="cApplyPersonName",type="String", remark="申请人名称", required=true, maxLength=100, fixed=0, order=8)
	@JSONField(name = "capplypersonname")
	public java.lang.String getCApplyPersonName() {
		return getStr("cApplyPersonName");
	}

	/**
	 * 项目名称
	 */
	public M setCProjectName(java.lang.String cProjectName) {
		set("cProjectName", cProjectName);
		return (M)this;
	}

	/**
	 * 项目名称
	 */
	@JBoltField(name="cprojectname" ,columnName="cProjectName",type="String", remark="项目名称", required=true, maxLength=100, fixed=0, order=9)
	@JSONField(name = "cprojectname")
	public java.lang.String getCProjectName() {
		return getStr("cProjectName");
	}

	/**
	 * 项目编码
	 */
	public M setCProjectCode(java.lang.String cProjectCode) {
		set("cProjectCode", cProjectCode);
		return (M)this;
	}

	/**
	 * 项目编码
	 */
	@JBoltField(name="cprojectcode" ,columnName="cProjectCode",type="String", remark="项目编码", required=false, maxLength=20, fixed=0, order=10)
	@JSONField(name = "cprojectcode")
	public java.lang.String getCProjectCode() {
		return getStr("cProjectCode");
	}

	/**
	 * 目的区分，字典SN
	 */
	public M setCPurposeSn(java.lang.String cPurposeSn) {
		set("cPurposeSn", cPurposeSn);
		return (M)this;
	}

	/**
	 * 目的区分，字典SN
	 */
	@JBoltField(name="cpurposesn" ,columnName="cPurposeSn",type="String", remark="目的区分，字典SN", required=true, maxLength=2, fixed=0, order=11)
	@JSONField(name = "cpurposesn")
	public java.lang.String getCPurposeSn() {
		return getStr("cPurposeSn");
	}

	/**
	 * 类别区分
	 */
	public M setICategoryId(java.lang.String iCategoryId) {
		set("iCategoryId", iCategoryId);
		return (M)this;
	}

	/**
	 * 类别区分
	 */
	@JBoltField(name="icategoryid" ,columnName="iCategoryId",type="String", remark="类别区分", required=true, maxLength=2147483647, fixed=0, order=12)
	@JSONField(name = "icategoryid")
	public java.lang.String getICategoryId() {
		return getStr("iCategoryId");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=100, fixed=0, order=13)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

	/**
	 * 事业计划: 0-计划外 1-计划内 2-提前实施
	 */
	public M setIsScheduled(java.lang.Integer IsScheduled) {
		set("IsScheduled", IsScheduled);
		return (M)this;
	}

	/**
	 * 事业计划: 0-计划外 1-计划内 2-提前实施
	 */
	@JBoltField(name="isscheduled" ,columnName="IsScheduled",type="Integer", remark="事业计划: 0-计划外 1-计划内 2-提前实施", required=true, maxLength=10, fixed=0, order=14)
	@JSONField(name = "isscheduled")
	public java.lang.Integer getIsScheduled() {
		return getInt("IsScheduled");
	}

	/**
	 * 审批状态
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审批状态
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审批状态", required=true, maxLength=10, fixed=0, order=15)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=16)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=17)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 创建人
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=18)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 更新人
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=19)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

	/**
	 * 原禀议书ID
	 */
	public M setISourceProposalId(java.lang.Long iSourceProposalId) {
		set("iSourceProposalId", iSourceProposalId);
		return (M)this;
	}

	/**
	 * 原禀议书ID
	 */
	@JBoltField(name="isourceproposalid" ,columnName="iSourceProposalId",type="Long", remark="原禀议书ID", required=false, maxLength=19, fixed=0, order=20)
	@JSONField(name = "isourceproposalid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISourceProposalId() {
		return getLong("iSourceProposalId");
	}

	/**
	 * 说明
	 */
	public M setCDesc(java.lang.String cDesc) {
		set("cDesc", cDesc);
		return (M)this;
	}

	/**
	 * 说明
	 */
	@JBoltField(name="cdesc" ,columnName="cDesc",type="String", remark="说明", required=false, maxLength=1000, fixed=0, order=21)
	@JSONField(name = "cdesc")
	public java.lang.String getCDesc() {
		return getStr("cDesc");
	}

	/**
	 * 原禀议书编号
	 */
	public M setCSourceProposalNo(java.lang.String cSourceProposalNo) {
		set("cSourceProposalNo", cSourceProposalNo);
		return (M)this;
	}

	/**
	 * 原禀议书编号
	 */
	@JBoltField(name="csourceproposalno" ,columnName="cSourceProposalNo",type="String", remark="原禀议书编号", required=false, maxLength=50, fixed=0, order=22)
	@JSONField(name = "csourceproposalno")
	public java.lang.String getCSourceProposalNo() {
		return getStr("cSourceProposalNo");
	}

	/**
	 * 本币无税金额
	 */
	public M setINatMoney(java.math.BigDecimal iNatMoney) {
		set("iNatMoney", iNatMoney);
		return (M)this;
	}

	/**
	 * 本币无税金额
	 */
	@JBoltField(name="inatmoney" ,columnName="iNatMoney",type="BigDecimal", remark="本币无税金额", required=true, maxLength=20, fixed=2, order=23)
	@JSONField(name = "inatmoney")
	public java.math.BigDecimal getINatMoney() {
		return getBigDecimal("iNatMoney");
	}

	/**
	 * 本币价税合计
	 */
	public M setINatSum(java.math.BigDecimal iNatSum) {
		set("iNatSum", iNatSum);
		return (M)this;
	}

	/**
	 * 本币价税合计
	 */
	@JBoltField(name="inatsum" ,columnName="iNatSum",type="BigDecimal", remark="本币价税合计", required=true, maxLength=20, fixed=2, order=24)
	@JSONField(name = "inatsum")
	public java.math.BigDecimal getINatSum() {
		return getBigDecimal("iNatSum");
	}

	/**
	 * 是否追加
	 */
	public M setIsSupplemental(java.lang.Boolean IsSupplemental) {
		set("IsSupplemental", IsSupplemental);
		return (M)this;
	}

	/**
	 * 是否追加
	 */
	@JBoltField(name="issupplemental" ,columnName="IsSupplemental",type="Boolean", remark="是否追加", required=true, maxLength=1, fixed=0, order=25)
	@JSONField(name = "issupplemental")
	public java.lang.Boolean getIsSupplemental() {
		return getBoolean("IsSupplemental");
	}

	/**
	 * 预算总金额（无税）
	 */
	public M setIBudgetMoney(java.math.BigDecimal iBudgetMoney) {
		set("iBudgetMoney", iBudgetMoney);
		return (M)this;
	}

	/**
	 * 预算总金额（无税）
	 */
	@JBoltField(name="ibudgetmoney" ,columnName="iBudgetMoney",type="BigDecimal", remark="预算总金额（无税）", required=true, maxLength=20, fixed=2, order=26)
	@JSONField(name = "ibudgetmoney")
	public java.math.BigDecimal getIBudgetMoney() {
		return getBigDecimal("iBudgetMoney");
	}

	/**
	 * 预算总金额（含税）
	 */
	public M setIBudgetSum(java.math.BigDecimal iBudgetSum) {
		set("iBudgetSum", iBudgetSum);
		return (M)this;
	}

	/**
	 * 预算总金额（含税）
	 */
	@JBoltField(name="ibudgetsum" ,columnName="iBudgetSum",type="BigDecimal", remark="预算总金额（含税）", required=true, maxLength=20, fixed=2, order=27)
	@JSONField(name = "ibudgetsum")
	public java.math.BigDecimal getIBudgetSum() {
		return getBigDecimal("iBudgetSum");
	}

	/**
	 * 追加申请金额（无税）
	 */
	public M setISupplementalMoney(java.math.BigDecimal iSupplementalMoney) {
		set("iSupplementalMoney", iSupplementalMoney);
		return (M)this;
	}

	/**
	 * 追加申请金额（无税）
	 */
	@JBoltField(name="isupplementalmoney" ,columnName="iSupplementalMoney",type="BigDecimal", remark="追加申请金额（无税）", required=true, maxLength=20, fixed=2, order=28)
	@JSONField(name = "isupplementalmoney")
	public java.math.BigDecimal getISupplementalMoney() {
		return getBigDecimal("iSupplementalMoney");
	}

	/**
	 * 追加申请金额（含税）
	 */
	public M setISupplementalSum(java.math.BigDecimal iSupplementalSum) {
		set("iSupplementalSum", iSupplementalSum);
		return (M)this;
	}

	/**
	 * 追加申请金额（含税）
	 */
	@JBoltField(name="isupplementalsum" ,columnName="iSupplementalSum",type="BigDecimal", remark="追加申请金额（含税）", required=true, maxLength=20, fixed=2, order=29)
	@JSONField(name = "isupplementalsum")
	public java.math.BigDecimal getISupplementalSum() {
		return getBigDecimal("iSupplementalSum");
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
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=30)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
	}

	/**
	 * 生效时间
	 */
	public M setDEffectiveTime(java.util.Date dEffectiveTime) {
		set("dEffectiveTime", dEffectiveTime);
		return (M)this;
	}

	/**
	 * 生效时间
	 */
	@JBoltField(name="deffectivetime" ,columnName="dEffectiveTime",type="Date", remark="生效时间", required=false, maxLength=23, fixed=3, order=31)
	@JSONField(name = "deffectivetime")
	public java.util.Date getDEffectiveTime() {
		return getDate("dEffectiveTime");
	}

	/**
	 * 累计申请金额（无税）
	 */
	public M setITotalMoney(java.math.BigDecimal iTotalMoney) {
		set("iTotalMoney", iTotalMoney);
		return (M)this;
	}

	/**
	 * 累计申请金额（无税）
	 */
	@JBoltField(name="itotalmoney" ,columnName="iTotalMoney",type="BigDecimal", remark="累计申请金额（无税）", required=false, maxLength=20, fixed=2, order=32)
	@JSONField(name = "itotalmoney")
	public java.math.BigDecimal getITotalMoney() {
		return getBigDecimal("iTotalMoney");
	}

	/**
	 * 累计申请金额（含税）
	 */
	public M setITotalSum(java.math.BigDecimal iTotalSum) {
		set("iTotalSum", iTotalSum);
		return (M)this;
	}

	/**
	 * 累计申请金额（含税）
	 */
	@JBoltField(name="itotalsum" ,columnName="iTotalSum",type="BigDecimal", remark="累计申请金额（含税）", required=false, maxLength=20, fixed=2, order=33)
	@JSONField(name = "itotalsum")
	public java.math.BigDecimal getITotalSum() {
		return getBigDecimal("iTotalSum");
	}

	/**
	 * 剩余预算金额（无税）
	 */
	public M setIBudgetRemainMoney(java.math.BigDecimal iBudgetRemainMoney) {
		set("iBudgetRemainMoney", iBudgetRemainMoney);
		return (M)this;
	}

	/**
	 * 剩余预算金额（无税）
	 */
	@JBoltField(name="ibudgetremainmoney" ,columnName="iBudgetRemainMoney",type="BigDecimal", remark="剩余预算金额（无税）", required=true, maxLength=20, fixed=2, order=34)
	@JSONField(name = "ibudgetremainmoney")
	public java.math.BigDecimal getIBudgetRemainMoney() {
		return getBigDecimal("iBudgetRemainMoney");
	}

	/**
	 * 剩余预算金额（含税）
	 */
	public M setIBudgetRemainSum(java.math.BigDecimal iBudgetRemainSum) {
		set("iBudgetRemainSum", iBudgetRemainSum);
		return (M)this;
	}

	/**
	 * 剩余预算金额（含税）
	 */
	@JBoltField(name="ibudgetremainsum" ,columnName="iBudgetRemainSum",type="BigDecimal", remark="剩余预算金额（含税）", required=true, maxLength=20, fixed=2, order=35)
	@JSONField(name = "ibudgetremainsum")
	public java.math.BigDecimal getIBudgetRemainSum() {
		return getBigDecimal("iBudgetRemainSum");
	}

	/**
	 * 追加说明
	 */
	public M setCSupplementalDesc(java.lang.String cSupplementalDesc) {
		set("cSupplementalDesc", cSupplementalDesc);
		return (M)this;
	}

	/**
	 * 追加说明
	 */
	@JBoltField(name="csupplementaldesc" ,columnName="cSupplementalDesc",type="String", remark="追加说明", required=false, maxLength=1000, fixed=0, order=36)
	@JSONField(name = "csupplementaldesc")
	public java.lang.String getCSupplementalDesc() {
		return getStr("cSupplementalDesc");
	}

	/**
	 * 生效状态(1.未生效 2.已生效 3.失效)
	 */
	public M setIEffectiveStatus(java.lang.Integer iEffectiveStatus) {
		set("iEffectiveStatus", iEffectiveStatus);
		return (M)this;
	}

	/**
	 * 生效状态(1.未生效 2.已生效 3.失效)
	 */
	@JBoltField(name="ieffectivestatus" ,columnName="iEffectiveStatus",type="Integer", remark="生效状态(1.未生效 2.已生效 3.失效)", required=true, maxLength=10, fixed=0, order=37)
	@JSONField(name = "ieffectivestatus")
	public java.lang.Integer getIEffectiveStatus() {
		return getInt("iEffectiveStatus");
	}

	/**
	 * 追加的初始禀议书ID
	 */
	public M setIFirstSourceProposalId(java.lang.Long iFirstSourceProposalId) {
		set("iFirstSourceProposalId", iFirstSourceProposalId);
		return (M)this;
	}

	/**
	 * 追加的初始禀议书ID
	 */
	@JBoltField(name="ifirstsourceproposalid" ,columnName="iFirstSourceProposalId",type="Long", remark="追加的初始禀议书ID", required=true, maxLength=19, fixed=0, order=38)
	@JSONField(name = "ifirstsourceproposalid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFirstSourceProposalId() {
		return getLong("iFirstSourceProposalId");
	}

	/**
	 * 审批方式：1. 审批状态 2. 审批流
	 */
	public M setIAuditWay(java.lang.Integer iAuditWay) {
		set("iAuditWay", iAuditWay);
		return (M)this;
	}

	/**
	 * 审批方式：1. 审批状态 2. 审批流
	 */
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="审批方式：1. 审批状态 2. 审批流", required=false, maxLength=10, fixed=0, order=39)
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
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=40)
	@JSONField(name = "dsubmittime")
	public java.util.Date getDSubmitTime() {
		return getDate("dSubmitTime");
	}

	/**
	 * 审核人ID，不走审批流时有该值
	 */
	public M setIAuditBy(java.lang.Long iAuditBy) {
		set("iAuditBy", iAuditBy);
		return (M)this;
	}

	/**
	 * 审核人ID，不走审批流时有该值
	 */
	@JBoltField(name="iauditby" ,columnName="iAuditBy",type="Long", remark="审核人ID，不走审批流时有该值", required=false, maxLength=19, fixed=0, order=41)
	@JSONField(name = "iauditby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAuditBy() {
		return getLong("iAuditBy");
	}

	/**
	 * 审核人名称，不走审批流时有该值
	 */
	public M setCAuditName(java.util.Date cAuditName) {
		set("cAuditName", cAuditName);
		return (M)this;
	}

	/**
	 * 审核人名称，不走审批流时有该值
	 */
	@JBoltField(name="cauditname" ,columnName="cAuditName",type="Date", remark="审核人名称，不走审批流时有该值", required=false, maxLength=23, fixed=3, order=42)
	@JSONField(name = "cauditname")
	public java.util.Date getCAuditName() {
		return getDate("cAuditName");
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=43)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

}

