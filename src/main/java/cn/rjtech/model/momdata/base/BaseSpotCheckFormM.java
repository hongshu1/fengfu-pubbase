package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造管理-点检表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSpotCheckFormM<M extends BaseSpotCheckFormM<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**类型;1.首末点检表 2.首中末点检表*/
    public static final String ITYPE = "iType";
    /**点检表格ID*/
    public static final String ISPOTCHECKFORMID = "iSpotCheckFormId";
    /**生产订单ID*/
    public static final String IMODOCID = "iMoDocId";
    /**工序工艺ID*/
    public static final String IMOROUTINGCONFIGID = "iMoRoutingConfigId";
    /**工序名称*/
    public static final String COPERATIONNAME = "cOperationName";
    /**生产订单号*/
    public static final String CMODOCNO = "cMoDocNo";
    /**生产人员ID*/
    public static final String IPERSONID = "iPersonId";
    /**生产人员名称*/
    public static final String CPERSONNAME = "cPersonName";
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
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**审核状态;0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间，不走审批流时有该值*/
    public static final String DAUDITTIME = "dAuditTime";
    /**审核人ID，不走审批流时有该值*/
    public static final String IAUDITBY = "iAuditBy";
    /**审核人名称，不走审批流时有该值*/
    public static final String CAUDITNAME = "cAuditName";
    /**审批方式;1. 审批状态 2. 审批流*/
    public static final String IAUDITWAY = "iAuditWay";
    /**提审时间*/
    public static final String DSUBMITTIME = "dSubmitTime";
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
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=60, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 类型;1.首末点检表 2.首中末点检表
	 */
	public M setIType(java.lang.Integer iType) {
		set("iType", iType);
		return (M)this;
	}

	/**
	 * 类型;1.首末点检表 2.首中末点检表
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="类型;1.首末点检表 2.首中末点检表", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "itype")
	public java.lang.Integer getIType() {
		return getInt("iType");
	}

	/**
	 * 点检表格ID
	 */
	public M setISpotCheckFormId(java.lang.Long iSpotCheckFormId) {
		set("iSpotCheckFormId", iSpotCheckFormId);
		return (M)this;
	}

	/**
	 * 点检表格ID
	 */
	@JBoltField(name="ispotcheckformid" ,columnName="iSpotCheckFormId",type="Long", remark="点检表格ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "ispotcheckformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISpotCheckFormId() {
		return getLong("iSpotCheckFormId");
	}

	/**
	 * 生产订单ID
	 */
	public M setIMoDocId(java.lang.Long iMoDocId) {
		set("iMoDocId", iMoDocId);
		return (M)this;
	}

	/**
	 * 生产订单ID
	 */
	@JBoltField(name="imodocid" ,columnName="iMoDocId",type="Long", remark="生产订单ID", required=true, maxLength=19, fixed=0, order=7)
	@JSONField(name = "imodocid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoDocId() {
		return getLong("iMoDocId");
	}

	/**
	 * 工序工艺ID
	 */
	public M setIMoRoutingConfigId(java.lang.String iMoRoutingConfigId) {
		set("iMoRoutingConfigId", iMoRoutingConfigId);
		return (M)this;
	}

	/**
	 * 工序工艺ID
	 */
	@JBoltField(name="imoroutingconfigid" ,columnName="iMoRoutingConfigId",type="String", remark="工序工艺ID", required=true, maxLength=255, fixed=0, order=8)
	@JSONField(name = "imoroutingconfigid")
	public java.lang.String getIMoRoutingConfigId() {
		return getStr("iMoRoutingConfigId");
	}

	/**
	 * 工序名称
	 */
	public M setCOperationName(java.lang.String cOperationName) {
		set("cOperationName", cOperationName);
		return (M)this;
	}

	/**
	 * 工序名称
	 */
	@JBoltField(name="coperationname" ,columnName="cOperationName",type="String", remark="工序名称", required=true, maxLength=255, fixed=0, order=9)
	@JSONField(name = "coperationname")
	public java.lang.String getCOperationName() {
		return getStr("cOperationName");
	}

	/**
	 * 生产订单号
	 */
	public M setCMoDocNo(java.lang.String cMoDocNo) {
		set("cMoDocNo", cMoDocNo);
		return (M)this;
	}

	/**
	 * 生产订单号
	 */
	@JBoltField(name="cmodocno" ,columnName="cMoDocNo",type="String", remark="生产订单号", required=true, maxLength=40, fixed=0, order=10)
	@JSONField(name = "cmodocno")
	public java.lang.String getCMoDocNo() {
		return getStr("cMoDocNo");
	}

	/**
	 * 生产人员ID
	 */
	public M setIPersonId(java.lang.Long iPersonId) {
		set("iPersonId", iPersonId);
		return (M)this;
	}

	/**
	 * 生产人员ID
	 */
	@JBoltField(name="ipersonid" ,columnName="iPersonId",type="Long", remark="生产人员ID", required=true, maxLength=19, fixed=0, order=11)
	@JSONField(name = "ipersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIPersonId() {
		return getLong("iPersonId");
	}

	/**
	 * 生产人员名称
	 */
	public M setCPersonName(java.lang.String cPersonName) {
		set("cPersonName", cPersonName);
		return (M)this;
	}

	/**
	 * 生产人员名称
	 */
	@JBoltField(name="cpersonname" ,columnName="cPersonName",type="String", remark="生产人员名称", required=true, maxLength=60, fixed=0, order=12)
	@JSONField(name = "cpersonname")
	public java.lang.String getCPersonName() {
		return getStr("cPersonName");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=13)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=60, fixed=0, order=14)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=15)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=16)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=60, fixed=0, order=17)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=18)
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态;0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=19)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=20)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

	/**
	 * 审核状态;0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态;0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态;0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=21)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
	}

	/**
	 * 审核时间，不走审批流时有该值
	 */
	public M setDAuditTime(java.util.Date dAuditTime) {
		set("dAuditTime", dAuditTime);
		return (M)this;
	}

	/**
	 * 审核时间，不走审批流时有该值
	 */
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间，不走审批流时有该值", required=false, maxLength=23, fixed=3, order=22)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
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
	@JBoltField(name="iauditby" ,columnName="iAuditBy",type="Long", remark="审核人ID，不走审批流时有该值", required=false, maxLength=19, fixed=0, order=23)
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
	@JBoltField(name="cauditname" ,columnName="cAuditName",type="Date", remark="审核人名称，不走审批流时有该值", required=false, maxLength=23, fixed=3, order=24)
	@JSONField(name = "cauditname")
	public java.util.Date getCAuditName() {
		return getDate("cAuditName");
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
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="审批方式;1. 审批状态 2. 审批流", required=true, maxLength=10, fixed=0, order=25)
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
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=26)
	@JSONField(name = "dsubmittime")
	public java.util.Date getDSubmitTime() {
		return getDate("dSubmitTime");
	}

}

