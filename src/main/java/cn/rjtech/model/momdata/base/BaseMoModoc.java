package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造管理-制造工单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoModoc<M extends BaseMoModoc<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**来源类型：1. APS 2. 手工新增*/
    public static final String ITYPE = "iType";
    /**制造工单任务ID*/
    public static final String IMOTASKID = "iMoTaskId";
    /**产线ID*/
    public static final String IWORKREGIONMID = "iWorkRegionMid";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**生产工单号*/
    public static final String CMODOCNO = "cMoDocNo";
    /**计划日期*/
    public static final String DPLANDATE = "dPlanDate";
    /**年*/
    public static final String IYEAR = "iYear";
    /**月*/
    public static final String IMONTH = "iMonth";
    /**日*/
    public static final String IDATE = "iDate";
    /**生产部门（系）ID*/
    public static final String IDEPARTMENTID = "iDepartmentId";
    /**班次ID*/
    public static final String IWORKSHIFTMID = "iWorkShiftMid";
    /**计划数量*/
    public static final String IQTY = "iQty";
    /**完工数量*/
    public static final String ICOMPQTY = "iCompQty";
    /**人数*/
    public static final String IPERSONNUM = "iPersonNum";
    /**产线组长人员ID*/
    public static final String IDUTYPERSONID = "iDutyPersonId";
    /**状态：0.已保存 1. 未安排人员 2. 已安排人员 3. 生成备料表 4. 待生产 5. 生产中 6. 已完工 7. 已关闭 8. 已取消*/
    public static final String ISTATUS = "iStatus";
    /**工艺路线ID*/
    public static final String IINVENTORYROUTING = "iInventoryRouting";
    /**工艺路线名称*/
    public static final String CROUTINGNAME = "cRoutingName";
    /**工艺路线版本*/
    public static final String CVERSION = "cVersion";
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
    /**是否被修改：0. 否 1. 是*/
    public static final String ISMODIFIED = "isModified";
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
	 * 来源类型：1. APS 2. 手工新增
	 */
	public M setIType(java.lang.Integer iType) {
		set("iType", iType);
		return (M)this;
	}

	/**
	 * 来源类型：1. APS 2. 手工新增
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="来源类型：1. APS 2. 手工新增", required=true, maxLength=10, fixed=0, order=2)
	@JSONField(name = "itype")
	public java.lang.Integer getIType() {
		return getInt("iType");
	}

	/**
	 * 制造工单任务ID
	 */
	public M setIMoTaskId(java.lang.Long iMoTaskId) {
		set("iMoTaskId", iMoTaskId);
		return (M)this;
	}

	/**
	 * 制造工单任务ID
	 */
	@JBoltField(name="imotaskid" ,columnName="iMoTaskId",type="Long", remark="制造工单任务ID", required=false, maxLength=19, fixed=0, order=3)
	@JSONField(name = "imotaskid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoTaskId() {
		return getLong("iMoTaskId");
	}

	/**
	 * 产线ID
	 */
	public M setIWorkRegionMid(java.lang.Long iWorkRegionMid) {
		set("iWorkRegionMid", iWorkRegionMid);
		return (M)this;
	}

	/**
	 * 产线ID
	 */
	@JBoltField(name="iworkregionmid" ,columnName="iWorkRegionMid",type="Long", remark="产线ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iworkregionmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWorkRegionMid() {
		return getLong("iWorkRegionMid");
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
	 * 生产工单号
	 */
	public M setCMoDocNo(java.lang.String cMoDocNo) {
		set("cMoDocNo", cMoDocNo);
		return (M)this;
	}

	/**
	 * 生产工单号
	 */
	@JBoltField(name="cmodocno" ,columnName="cMoDocNo",type="String", remark="生产工单号", required=true, maxLength=50, fixed=0, order=6)
	@JSONField(name = "cmodocno")
	public java.lang.String getCMoDocNo() {
		return getStr("cMoDocNo");
	}

	/**
	 * 计划日期
	 */
	public M setDPlanDate(java.util.Date dPlanDate) {
		set("dPlanDate", dPlanDate);
		return (M)this;
	}

	/**
	 * 计划日期
	 */
	@JBoltField(name="dplandate" ,columnName="dPlanDate",type="Date", remark="计划日期", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "dplandate")
	public java.util.Date getDPlanDate() {
		return getDate("dPlanDate");
	}

	/**
	 * 年
	 */
	public M setIYear(java.lang.Integer iYear) {
		set("iYear", iYear);
		return (M)this;
	}

	/**
	 * 年
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "iyear")
	public java.lang.Integer getIYear() {
		return getInt("iYear");
	}

	/**
	 * 月
	 */
	public M setIMonth(java.lang.Integer iMonth) {
		set("iMonth", iMonth);
		return (M)this;
	}

	/**
	 * 月
	 */
	@JBoltField(name="imonth" ,columnName="iMonth",type="Integer", remark="月", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "imonth")
	public java.lang.Integer getIMonth() {
		return getInt("iMonth");
	}

	/**
	 * 日
	 */
	public M setIDate(java.lang.Integer iDate) {
		set("iDate", iDate);
		return (M)this;
	}

	/**
	 * 日
	 */
	@JBoltField(name="idate" ,columnName="iDate",type="Integer", remark="日", required=true, maxLength=10, fixed=0, order=10)
	@JSONField(name = "idate")
	public java.lang.Integer getIDate() {
		return getInt("iDate");
	}

	/**
	 * 生产部门（系）ID
	 */
	public M setIDepartmentId(java.lang.Long iDepartmentId) {
		set("iDepartmentId", iDepartmentId);
		return (M)this;
	}

	/**
	 * 生产部门（系）ID
	 */
	@JBoltField(name="idepartmentid" ,columnName="iDepartmentId",type="Long", remark="生产部门（系）ID", required=true, maxLength=19, fixed=0, order=11)
	@JSONField(name = "idepartmentid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDepartmentId() {
		return getLong("iDepartmentId");
	}

	/**
	 * 班次ID
	 */
	public M setIWorkShiftMid(java.lang.Long iWorkShiftMid) {
		set("iWorkShiftMid", iWorkShiftMid);
		return (M)this;
	}

	/**
	 * 班次ID
	 */
	@JBoltField(name="iworkshiftmid" ,columnName="iWorkShiftMid",type="Long", remark="班次ID", required=true, maxLength=19, fixed=0, order=12)
	@JSONField(name = "iworkshiftmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWorkShiftMid() {
		return getLong("iWorkShiftMid");
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
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="计划数量", required=true, maxLength=18, fixed=2, order=13)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 完工数量
	 */
	public M setICompQty(java.math.BigDecimal iCompQty) {
		set("iCompQty", iCompQty);
		return (M)this;
	}

	/**
	 * 完工数量
	 */
	@JBoltField(name="icompqty" ,columnName="iCompQty",type="BigDecimal", remark="完工数量", required=false, maxLength=18, fixed=2, order=14)
	@JSONField(name = "icompqty")
	public java.math.BigDecimal getICompQty() {
		return getBigDecimal("iCompQty");
	}

	/**
	 * 人数
	 */
	public M setIPersonNum(java.lang.Integer iPersonNum) {
		set("iPersonNum", iPersonNum);
		return (M)this;
	}

	/**
	 * 人数
	 */
	@JBoltField(name="ipersonnum" ,columnName="iPersonNum",type="Integer", remark="人数", required=true, maxLength=10, fixed=0, order=15)
	@JSONField(name = "ipersonnum")
	public java.lang.Integer getIPersonNum() {
		return getInt("iPersonNum");
	}

	/**
	 * 产线组长人员ID
	 */
	public M setIDutyPersonId(java.lang.Long iDutyPersonId) {
		set("iDutyPersonId", iDutyPersonId);
		return (M)this;
	}

	/**
	 * 产线组长人员ID
	 */
	@JBoltField(name="idutypersonid" ,columnName="iDutyPersonId",type="Long", remark="产线组长人员ID", required=false, maxLength=19, fixed=0, order=16)
	@JSONField(name = "idutypersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDutyPersonId() {
		return getLong("iDutyPersonId");
	}

	/**
	 * 状态：0.已保存 1. 未安排人员 2. 已安排人员 3. 生成备料表 4. 待生产 5. 生产中 6. 已完工 7. 已关闭 8. 已取消
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 状态：0.已保存 1. 未安排人员 2. 已安排人员 3. 生成备料表 4. 待生产 5. 生产中 6. 已完工 7. 已关闭 8. 已取消
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="状态：0.已保存 1. 未安排人员 2. 已安排人员 3. 生成备料表 4. 待生产 5. 生产中 6. 已完工 7. 已关闭 8. 已取消", required=true, maxLength=10, fixed=0, order=17)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
	}

	/**
	 * 工艺路线ID
	 */
	public M setIInventoryRouting(java.lang.Long iInventoryRouting) {
		set("iInventoryRouting", iInventoryRouting);
		return (M)this;
	}

	/**
	 * 工艺路线ID
	 */
	@JBoltField(name="iinventoryrouting" ,columnName="iInventoryRouting",type="Long", remark="工艺路线ID", required=true, maxLength=19, fixed=0, order=18)
	@JSONField(name = "iinventoryrouting", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryRouting() {
		return getLong("iInventoryRouting");
	}

	/**
	 * 工艺路线名称
	 */
	public M setCRoutingName(java.lang.String cRoutingName) {
		set("cRoutingName", cRoutingName);
		return (M)this;
	}

	/**
	 * 工艺路线名称
	 */
	@JBoltField(name="croutingname" ,columnName="cRoutingName",type="String", remark="工艺路线名称", required=true, maxLength=100, fixed=0, order=19)
	@JSONField(name = "croutingname")
	public java.lang.String getCRoutingName() {
		return getStr("cRoutingName");
	}

	/**
	 * 工艺路线版本
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 工艺路线版本
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="工艺路线版本", required=true, maxLength=10, fixed=0, order=20)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=false, maxLength=19, fixed=0, order=21)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=false, maxLength=60, fixed=0, order=22)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=23)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=false, maxLength=19, fixed=0, order=24)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=false, maxLength=60, fixed=0, order=25)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=26)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 是否被修改：0. 否 1. 是
	 */
	public M setIsModified(java.lang.Boolean isModified) {
		set("isModified", isModified);
		return (M)this;
	}

	/**
	 * 是否被修改：0. 否 1. 是
	 */
	@JBoltField(name="ismodified" ,columnName="isModified",type="Boolean", remark="是否被修改：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=27)
	@JSONField(name = "ismodified")
	public java.lang.Boolean getIsModified() {
		return getBoolean("isModified");
	}

}

