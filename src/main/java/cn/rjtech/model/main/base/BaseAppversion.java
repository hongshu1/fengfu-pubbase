package cn.rjtech.model.main.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 应用版本
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseAppversion<M extends BaseAppversion<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String AUTOID = "AutoID";
    /**系统类型: 1. 安卓*/
    public static final String SYSTEMTYPE = "SystemType";
    /**应用编码*/
    public static final String APPCODE = "AppCode";
    /**版本代号*/
    public static final String VERSIONCODE = "VersionCode";
    /**发布日期*/
    public static final String VERSIONDATE = "VersionDate";
    /**是否强制更新: 0. 否 1. 是*/
    public static final String ISFORCED = "IsForced";
    /**下载URI*/
    public static final String DOWNLOADURL = "DownloadUrl";
    /**版本介绍*/
    public static final String VERSIONINTRO = "VersionIntro";
    /**发布状态: 1. 已发布 2. 未发布*/
    public static final String PUTAWAY = "Putaway";
    /**发布时间*/
    public static final String PUTAWAYTIME = "PutawayTime";
    /**删除状态: 0. 未删除 1. 已删除*/
    public static final String ISDELETED = "IsDeleted";
    /**创建时间*/
    public static final String CREATETIME = "CreateTime";
    /**更新时间*/
    public static final String UPDATETIME = "UpdateTime";
    /**创建人*/
    public static final String CREATEBY = "CreateBy";
    /**变更人*/
    public static final String UPDATEBY = "UpdateBy";
    /**CDN链接*/
    public static final String CDNURL = "CdnUrl";
	/**
	 * 主键ID
	 */
	public M setAutoID(java.lang.Long AutoID) {
		set("AutoID", AutoID);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "autoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getAutoID() {
		return getLong("AutoID");
	}

	/**
	 * 系统类型: 1. 安卓
	 */
	public M setSystemType(java.lang.Integer SystemType) {
		set("SystemType", SystemType);
		return (M)this;
	}

	/**
	 * 系统类型: 1. 安卓
	 */
	@JBoltField(name="systemtype" ,columnName="SystemType",type="Integer", remark="系统类型: 1. 安卓", required=true, maxLength=10, fixed=0, order=2)
	@JSONField(name = "systemtype")
	public java.lang.Integer getSystemType() {
		return getInt("SystemType");
	}

	/**
	 * 应用编码
	 */
	public M setAppCode(java.lang.String AppCode) {
		set("AppCode", AppCode);
		return (M)this;
	}

	/**
	 * 应用编码
	 */
	@JBoltField(name="appcode" ,columnName="AppCode",type="String", remark="应用编码", required=true, maxLength=30, fixed=0, order=3)
	@JSONField(name = "appcode")
	public java.lang.String getAppCode() {
		return getStr("AppCode");
	}

	/**
	 * 版本代号
	 */
	public M setVersionCode(java.lang.String VersionCode) {
		set("VersionCode", VersionCode);
		return (M)this;
	}

	/**
	 * 版本代号
	 */
	@JBoltField(name="versioncode" ,columnName="VersionCode",type="String", remark="版本代号", required=true, maxLength=30, fixed=0, order=4)
	@JSONField(name = "versioncode")
	public java.lang.String getVersionCode() {
		return getStr("VersionCode");
	}

	/**
	 * 发布日期
	 */
	public M setVersionDate(java.util.Date VersionDate) {
		set("VersionDate", VersionDate);
		return (M)this;
	}

	/**
	 * 发布日期
	 */
	@JBoltField(name="versiondate" ,columnName="VersionDate",type="Date", remark="发布日期", required=false, maxLength=27, fixed=7, order=5)
	@JSONField(name = "versiondate")
	public java.util.Date getVersionDate() {
		return getDate("VersionDate");
	}

	/**
	 * 是否强制更新: 0. 否 1. 是
	 */
	public M setIsForced(java.lang.Boolean IsForced) {
		set("IsForced", IsForced);
		return (M)this;
	}

	/**
	 * 是否强制更新: 0. 否 1. 是
	 */
	@JBoltField(name="isforced" ,columnName="IsForced",type="Boolean", remark="是否强制更新: 0. 否 1. 是", required=true, maxLength=1, fixed=0, order=6)
	@JSONField(name = "isforced")
	public java.lang.Boolean getIsForced() {
		return getBoolean("IsForced");
	}

	/**
	 * 下载URI
	 */
	public M setDownloadUrl(java.lang.String DownloadUrl) {
		set("DownloadUrl", DownloadUrl);
		return (M)this;
	}

	/**
	 * 下载URI
	 */
	@JBoltField(name="downloadurl" ,columnName="DownloadUrl",type="String", remark="下载URI", required=true, maxLength=200, fixed=0, order=7)
	@JSONField(name = "downloadurl")
	public java.lang.String getDownloadUrl() {
		return getStr("DownloadUrl");
	}

	/**
	 * 版本介绍
	 */
	public M setVersionIntro(java.lang.String VersionIntro) {
		set("VersionIntro", VersionIntro);
		return (M)this;
	}

	/**
	 * 版本介绍
	 */
	@JBoltField(name="versionintro" ,columnName="VersionIntro",type="String", remark="版本介绍", required=false, maxLength=200, fixed=0, order=8)
	@JSONField(name = "versionintro")
	public java.lang.String getVersionIntro() {
		return getStr("VersionIntro");
	}

	/**
	 * 发布状态: 1. 已发布 2. 未发布
	 */
	public M setPutaway(java.lang.Integer Putaway) {
		set("Putaway", Putaway);
		return (M)this;
	}

	/**
	 * 发布状态: 1. 已发布 2. 未发布
	 */
	@JBoltField(name="putaway" ,columnName="Putaway",type="Integer", remark="发布状态: 1. 已发布 2. 未发布", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "putaway")
	public java.lang.Integer getPutaway() {
		return getInt("Putaway");
	}

	/**
	 * 发布时间
	 */
	public M setPutawayTime(java.util.Date PutawayTime) {
		set("PutawayTime", PutawayTime);
		return (M)this;
	}

	/**
	 * 发布时间
	 */
	@JBoltField(name="putawaytime" ,columnName="PutawayTime",type="Date", remark="发布时间", required=false, maxLength=27, fixed=7, order=10)
	@JSONField(name = "putawaytime")
	public java.util.Date getPutawayTime() {
		return getDate("PutawayTime");
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean IsDeleted) {
		set("IsDeleted", IsDeleted);
		return (M)this;
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态: 0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=11)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

	/**
	 * 创建时间
	 */
	public M setCreateTime(java.util.Date CreateTime) {
		set("CreateTime", CreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="createtime" ,columnName="CreateTime",type="Date", remark="创建时间", required=true, maxLength=27, fixed=7, order=12)
	@JSONField(name = "createtime")
	public java.util.Date getCreateTime() {
		return getDate("CreateTime");
	}

	/**
	 * 更新时间
	 */
	public M setUpdateTime(java.util.Date UpdateTime) {
		set("UpdateTime", UpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="updatetime" ,columnName="UpdateTime",type="Date", remark="更新时间", required=true, maxLength=27, fixed=7, order=13)
	@JSONField(name = "updatetime")
	public java.util.Date getUpdateTime() {
		return getDate("UpdateTime");
	}

	/**
	 * 创建人
	 */
	public M setCreateBy(java.lang.String CreateBy) {
		set("CreateBy", CreateBy);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="createby" ,columnName="CreateBy",type="String", remark="创建人", required=true, maxLength=30, fixed=0, order=14)
	@JSONField(name = "createby")
	public java.lang.String getCreateBy() {
		return getStr("CreateBy");
	}

	/**
	 * 变更人
	 */
	public M setUpdateBy(java.lang.String UpdateBy) {
		set("UpdateBy", UpdateBy);
		return (M)this;
	}

	/**
	 * 变更人
	 */
	@JBoltField(name="updateby" ,columnName="UpdateBy",type="String", remark="变更人", required=true, maxLength=30, fixed=0, order=15)
	@JSONField(name = "updateby")
	public java.lang.String getUpdateBy() {
		return getStr("UpdateBy");
	}

	/**
	 * CDN链接
	 */
	public M setCdnUrl(java.lang.String CdnUrl) {
		set("CdnUrl", CdnUrl);
		return (M)this;
	}

	/**
	 * CDN链接
	 */
	@JBoltField(name="cdnurl" ,columnName="CdnUrl",type="String", remark="CDN链接", required=false, maxLength=2147483647, fixed=0, order=16)
	@JSONField(name = "cdnurl")
	public java.lang.String getCdnUrl() {
		return getStr("CdnUrl");
	}

}
