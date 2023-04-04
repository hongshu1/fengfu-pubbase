package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 排程工作日历表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCalendar<M extends BaseCalendar<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**来源代码，取字典值*/
    public static final String CSOURCECODE = "cSourceCode";
    /**来源类型，取字典值*/
    public static final String CSOURCETYPE = "cSourceType";
    /**发生日期*/
    public static final String DTAKEDATE = "dTakeDate";
    /**日历类型：1. 工作日历 0. 非工作日历*/
    public static final String ICALUEDARTYPE = "iCaluedarType";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
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
	 * 来源代码，取字典值
	 */
	public M setCSourceCode(java.lang.String cSourceCode) {
		set("cSourceCode", cSourceCode);
		return (M)this;
	}

	/**
	 * 来源代码，取字典值
	 */
	@JBoltField(name="csourcecode" ,columnName="cSourceCode",type="String", remark="来源代码，取字典值", required=false, maxLength=255, fixed=0, order=5)
	@JSONField(name = "csourcecode")
	public java.lang.String getCSourceCode() {
		return getStr("cSourceCode");
	}

	/**
	 * 来源类型，取字典值
	 */
	public M setCSourceType(java.lang.String cSourceType) {
		set("cSourceType", cSourceType);
		return (M)this;
	}

	/**
	 * 来源类型，取字典值
	 */
	@JBoltField(name="csourcetype" ,columnName="cSourceType",type="String", remark="来源类型，取字典值", required=false, maxLength=255, fixed=0, order=6)
	@JSONField(name = "csourcetype")
	public java.lang.String getCSourceType() {
		return getStr("cSourceType");
	}

	/**
	 * 发生日期
	 */
	public M setDTakeDate(java.util.Date dTakeDate) {
		set("dTakeDate", dTakeDate);
		return (M)this;
	}

	/**
	 * 发生日期
	 */
	@JBoltField(name="dtakedate" ,columnName="dTakeDate",type="Date", remark="发生日期", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "dtakedate")
	public java.util.Date getDTakeDate() {
		return getDate("dTakeDate");
	}

	/**
	 * 日历类型：1. 工作日历 0. 非工作日历
	 */
	public M setICaluedarType(java.lang.Boolean iCaluedarType) {
		set("iCaluedarType", iCaluedarType);
		return (M)this;
	}

	/**
	 * 日历类型：1. 工作日历 0. 非工作日历
	 */
	@JBoltField(name="icaluedartype" ,columnName="iCaluedarType",type="Boolean", remark="日历类型：1. 工作日历 0. 非工作日历", required=false, maxLength=1, fixed=0, order=8)
	@JSONField(name = "icaluedartype")
	public java.lang.Boolean getICaluedarType() {
		return getBoolean("iCaluedarType");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=9)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=10)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=11)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=true, maxLength=19, fixed=0, order=12)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=13)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=14)
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=15)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

}

