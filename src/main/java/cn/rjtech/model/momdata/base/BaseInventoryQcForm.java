package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量建模-检验适用标准
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInventoryQcForm<M extends BaseInventoryQcForm<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**检验表格ID*/
    public static final String IQCFORMID = "iQcFormId";
    /**检验图片，多张","分隔*/
    public static final String CPICS = "cPics";
    /**设变号*/
    public static final String CDCCODE = "cDcCode";
    /**测定理由*/
    public static final String CMEASURE = "cMeasure";
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
    /**检验表格名称*/
    public static final String IQCFORMNAME = "iQcFormName";
    /**机型*/
    public static final String MACHINETYPE = "machineType";
    /**存货编码*/
    public static final String IINVENTORYCODE = "iInventoryCode";
    /**存货名称*/
    public static final String IINVENTORYNAME = "iInventoryName";
    /**客户部番*/
    public static final String CUSTOMERMANAGER = "CustomerManager";
    /**部品名称*/
    public static final String COMPONENTNAME = "componentName";
    /**规格*/
    public static final String SPECS = "specs";
    /**主计量单位*/
    public static final String UNIT = "unit";
    /**检验类型*/
    public static final String INSPECTIONTYPE = "inspectionType";
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
	 * 检验表格ID
	 */
	public M setIQcFormId(java.lang.Long iQcFormId) {
		set("iQcFormId", iQcFormId);
		return (M)this;
	}

	/**
	 * 检验表格ID
	 */
	@JBoltField(name="iqcformid" ,columnName="iQcFormId",type="Long", remark="检验表格ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "iqcformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcFormId() {
		return getLong("iQcFormId");
	}

	/**
	 * 检验图片，多张","分隔
	 */
	public M setCPics(java.lang.String cPics) {
		set("cPics", cPics);
		return (M)this;
	}

	/**
	 * 检验图片，多张","分隔
	 */
	@JBoltField(name="cpics" ,columnName="cPics",type="String", remark="检验图片，多张分隔", required=false, maxLength=500, fixed=0, order=7)
	@JSONField(name = "cpics")
	public java.lang.String getCPics() {
		return getStr("cPics");
	}

	/**
	 * 设变号
	 */
	public M setCDcCode(java.lang.String cDcCode) {
		set("cDcCode", cDcCode);
		return (M)this;
	}

	/**
	 * 设变号
	 */
	@JBoltField(name="cdccode" ,columnName="cDcCode",type="String", remark="设变号", required=false, maxLength=40, fixed=0, order=8)
	@JSONField(name = "cdccode")
	public java.lang.String getCDcCode() {
		return getStr("cDcCode");
	}

	/**
	 * 测定理由
	 */
	public M setCMeasure(java.lang.String cMeasure) {
		set("cMeasure", cMeasure);
		return (M)this;
	}

	/**
	 * 测定理由
	 */
	@JBoltField(name="cmeasure" ,columnName="cMeasure",type="String", remark="测定理由", required=false, maxLength=100, fixed=0, order=9)
	@JSONField(name = "cmeasure")
	public java.lang.String getCMeasure() {
		return getStr("cMeasure");
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
	 * 检验表格名称
	 */
	public M setIQcFormName(java.lang.String iQcFormName) {
		set("iQcFormName", iQcFormName);
		return (M)this;
	}

	/**
	 * 检验表格名称
	 */
	@JBoltField(name="iqcformname" ,columnName="iQcFormName",type="String", remark="检验表格名称", required=false, maxLength=255, fixed=0, order=17)
	@JSONField(name = "iqcformname")
	public java.lang.String getIQcFormName() {
		return getStr("iQcFormName");
	}

	/**
	 * 机型
	 */
	public M setMachineType(java.lang.String machineType) {
		set("machineType", machineType);
		return (M)this;
	}

	/**
	 * 机型
	 */
	@JBoltField(name="machinetype" ,columnName="machineType",type="String", remark="机型", required=false, maxLength=255, fixed=0, order=18)
	@JSONField(name = "machinetype")
	public java.lang.String getMachineType() {
		return getStr("machineType");
	}

	/**
	 * 存货编码
	 */
	public M setIInventoryCode(java.lang.String iInventoryCode) {
		set("iInventoryCode", iInventoryCode);
		return (M)this;
	}

	/**
	 * 存货编码
	 */
	@JBoltField(name="iinventorycode" ,columnName="iInventoryCode",type="String", remark="存货编码", required=false, maxLength=255, fixed=0, order=19)
	@JSONField(name = "iinventorycode")
	public java.lang.String getIInventoryCode() {
		return getStr("iInventoryCode");
	}

	/**
	 * 存货名称
	 */
	public M setIInventoryName(java.lang.String iInventoryName) {
		set("iInventoryName", iInventoryName);
		return (M)this;
	}

	/**
	 * 存货名称
	 */
	@JBoltField(name="iinventoryname" ,columnName="iInventoryName",type="String", remark="存货名称", required=false, maxLength=255, fixed=0, order=20)
	@JSONField(name = "iinventoryname")
	public java.lang.String getIInventoryName() {
		return getStr("iInventoryName");
	}

	/**
	 * 客户部番
	 */
	public M setCustomerManager(java.lang.String CustomerManager) {
		set("CustomerManager", CustomerManager);
		return (M)this;
	}

	/**
	 * 客户部番
	 */
	@JBoltField(name="customermanager" ,columnName="CustomerManager",type="String", remark="客户部番", required=false, maxLength=255, fixed=0, order=21)
	@JSONField(name = "customermanager")
	public java.lang.String getCustomerManager() {
		return getStr("CustomerManager");
	}

	/**
	 * 部品名称
	 */
	public M setComponentName(java.lang.String componentName) {
		set("componentName", componentName);
		return (M)this;
	}

	/**
	 * 部品名称
	 */
	@JBoltField(name="componentname" ,columnName="componentName",type="String", remark="部品名称", required=false, maxLength=255, fixed=0, order=22)
	@JSONField(name = "componentname")
	public java.lang.String getComponentName() {
		return getStr("componentName");
	}

	/**
	 * 规格
	 */
	public M setSpecs(java.lang.String specs) {
		set("specs", specs);
		return (M)this;
	}

	/**
	 * 规格
	 */
	@JBoltField(name="specs" ,columnName="specs",type="String", remark="规格", required=false, maxLength=255, fixed=0, order=23)
	@JSONField(name = "specs")
	public java.lang.String getSpecs() {
		return getStr("specs");
	}

	/**
	 * 主计量单位
	 */
	public M setUnit(java.lang.String unit) {
		set("unit", unit);
		return (M)this;
	}

	/**
	 * 主计量单位
	 */
	@JBoltField(name="unit" ,columnName="unit",type="String", remark="主计量单位", required=false, maxLength=255, fixed=0, order=24)
	@JSONField(name = "unit")
	public java.lang.String getUnit() {
		return getStr("unit");
	}

	/**
	 * 检验类型
	 */
	public M setInspectionType(java.lang.String inspectionType) {
		set("inspectionType", inspectionType);
		return (M)this;
	}

	/**
	 * 检验类型
	 */
	@JBoltField(name="inspectiontype" ,columnName="inspectionType",type="String", remark="检验类型", required=false, maxLength=500, fixed=0, order=25)
	@JSONField(name = "inspectiontype")
	public java.lang.String getInspectionType() {
		return getStr("inspectionType");
	}

}

