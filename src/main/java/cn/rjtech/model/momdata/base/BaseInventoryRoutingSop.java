package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 物料建模-存货工序作业指导书
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInventoryRoutingSop<M extends BaseInventoryRoutingSop<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**料品工艺档案配置ID*/
    public static final String IINVENTORYROUTINGCONFIGID = "iInventoryRoutingConfigId";
    /**名称*/
    public static final String CNAME = "cName";
    /**路径*/
    public static final String CPATH = "cPath";
    /**文件类型*/
    public static final String CSUFFIX = "cSuffix";
    /**大小*/
    public static final String ISIZE = "iSize";
    /**版本号*/
    public static final String IVERSION = "iVersion";
    /**启用时间*/
    public static final String DFROMDATE = "dFromDate";
    /**停用时间*/
    public static final String DTODATE = "dToDate";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**是否启用*/
    public static final String ISENABLED = "isEnabled";
	/**
	 * 主键
	 */
	public M setIAutoId(java.lang.Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 料品工艺档案配置ID
	 */
	public M setIInventoryRoutingConfigId(java.lang.Long iInventoryRoutingConfigId) {
		set("iInventoryRoutingConfigId", iInventoryRoutingConfigId);
		return (M)this;
	}

	/**
	 * 料品工艺档案配置ID
	 */
	@JBoltField(name="iinventoryroutingconfigid" ,columnName="iInventoryRoutingConfigId",type="Long", remark="料品工艺档案配置ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryroutingconfigid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryRoutingConfigId() {
		return getLong("iInventoryRoutingConfigId");
	}

	/**
	 * 名称
	 */
	public M setCName(java.lang.String cName) {
		set("cName", cName);
		return (M)this;
	}

	/**
	 * 名称
	 */
	@JBoltField(name="cname" ,columnName="cName",type="String", remark="名称", required=true, maxLength=255, fixed=0, order=3)
	@JSONField(name = "cname")
	public java.lang.String getCName() {
		return getStr("cName");
	}

	/**
	 * 路径
	 */
	public M setCPath(java.lang.String cPath) {
		set("cPath", cPath);
		return (M)this;
	}

	/**
	 * 路径
	 */
	@JBoltField(name="cpath" ,columnName="cPath",type="String", remark="路径", required=true, maxLength=255, fixed=0, order=4)
	@JSONField(name = "cpath")
	public java.lang.String getCPath() {
		return getStr("cPath");
	}

	/**
	 * 文件类型
	 */
	public M setCSuffix(java.lang.String cSuffix) {
		set("cSuffix", cSuffix);
		return (M)this;
	}

	/**
	 * 文件类型
	 */
	@JBoltField(name="csuffix" ,columnName="cSuffix",type="String", remark="文件类型", required=true, maxLength=255, fixed=0, order=5)
	@JSONField(name = "csuffix")
	public java.lang.String getCSuffix() {
		return getStr("cSuffix");
	}

	/**
	 * 大小
	 */
	public M setISize(java.lang.Integer iSize) {
		set("iSize", iSize);
		return (M)this;
	}

	/**
	 * 大小
	 */
	@JBoltField(name="isize" ,columnName="iSize",type="Integer", remark="大小", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "isize")
	public java.lang.Integer getISize() {
		return getInt("iSize");
	}

	/**
	 * 版本号
	 */
	public M setIVersion(java.lang.String iVersion) {
		set("iVersion", iVersion);
		return (M)this;
	}

	/**
	 * 版本号
	 */
	@JBoltField(name="iversion" ,columnName="iVersion",type="String", remark="版本号", required=true, maxLength=255, fixed=0, order=7)
	@JSONField(name = "iversion")
	public java.lang.String getIVersion() {
		return getStr("iVersion");
	}

	/**
	 * 启用时间
	 */
	public M setDFromDate(java.util.Date dFromDate) {
		set("dFromDate", dFromDate);
		return (M)this;
	}

	/**
	 * 启用时间
	 */
	@JBoltField(name="dfromdate" ,columnName="dFromDate",type="Date", remark="启用时间", required=true, maxLength=23, fixed=3, order=8)
	@JSONField(name = "dfromdate")
	public java.util.Date getDFromDate() {
		return getDate("dFromDate");
	}

	/**
	 * 停用时间
	 */
	public M setDToDate(java.util.Date dToDate) {
		set("dToDate", dToDate);
		return (M)this;
	}

	/**
	 * 停用时间
	 */
	@JBoltField(name="dtodate" ,columnName="dToDate",type="Date", remark="停用时间", required=true, maxLength=23, fixed=3, order=9)
	@JSONField(name = "dtodate")
	public java.util.Date getDToDate() {
		return getDate("dToDate");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=50, fixed=0, order=10)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=11)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=12)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=13)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 是否启用
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用", required=true, maxLength=1, fixed=0, order=14)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
	}

}

