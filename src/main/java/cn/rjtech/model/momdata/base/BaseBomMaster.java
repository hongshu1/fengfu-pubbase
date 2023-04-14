package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 物料建模-Bom母项
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBomMaster<M extends BaseBomMaster<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**母件存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**机型ID*/
    public static final String IEQUIPMENTMODELID = "iEquipmentModelId";
    /**文件名称*/
    public static final String CDOCNAME = "cDocName";
    /**文件编码*/
    public static final String CDOCCODE = "cDocCode";
    /**版本/版次*/
    public static final String CBOMVERSION = "cBomVersion";
    /**启用日期*/
    public static final String DENABLEDATE = "dEnableDate";
    /**停用日期*/
    public static final String DDISABLEDATE = "dDisableDate";
    /**序号1*/
    public static final String CNO1 = "cNo1";
    /**设变号1*/
    public static final String CDCNO1 = "cDcNo1";
    /**设备日期1*/
    public static final String DDCDATE1 = "dDcDate1";
    /**序号2*/
    public static final String CNO2 = "cNo2";
    /**设变号2*/
    public static final String CDCNO2 = "cDcNo2";
    /**设变日期2*/
    public static final String DDCDATE2 = "dDcDate2";
    /**序号3*/
    public static final String CNO3 = "cNo3";
    /**设变号3*/
    public static final String CDCNO3 = "cDcNo3";
    /**设变日期3*/
    public static final String DDCDATE3 = "dDcDate3";
    /**客户ID*/
    public static final String ICUSTOMERID = "iCustomerId";
    /**备注共用件*/
    public static final String CCOMMONPARTMEMO = "cCommonPartMemo";
    /**上级ID*/
    public static final String IPID = "iPid";
    /**层级*/
    public static final String ILEVEL = "iLevel";
    /**来源：1.MES 2. U8*/
    public static final String ISOURCE = "iSource";
    /**来源标识ID*/
    public static final String ISOURCEID = "iSourceId";
    /**是否启用*/
    public static final String ISENABLED = "isEnabled";
    /**审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间，未走审批流时，有值*/
    public static final String DAUDITTIME = "dAuditTime";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**更新人ID*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**是否删除 1已删除*/
    public static final String ISDELETED = "isDeleted";
	/**是否生效：0. 否 1. 是*/
    public static final String ISEFFECTIVE = "isEffective";
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
	 * 母件存货ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 母件存货ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="母件存货ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 机型ID
	 */
	public M setIEquipmentModelId(java.lang.Long iEquipmentModelId) {
		set("iEquipmentModelId", iEquipmentModelId);
		return (M)this;
	}

	/**
	 * 机型ID
	 */
	@JBoltField(name="iequipmentmodelid" ,columnName="iEquipmentModelId",type="Long", remark="机型ID", required=false, maxLength=19, fixed=0, order=6)
	@JSONField(name = "iequipmentmodelid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIEquipmentModelId() {
		return getLong("iEquipmentModelId");
	}

	/**
	 * 文件名称
	 */
	public M setCDocName(java.lang.String cDocName) {
		set("cDocName", cDocName);
		return (M)this;
	}

	/**
	 * 文件名称
	 */
	@JBoltField(name="cdocname" ,columnName="cDocName",type="String", remark="文件名称", required=false, maxLength=50, fixed=0, order=7)
	@JSONField(name = "cdocname")
	public java.lang.String getCDocName() {
		return getStr("cDocName");
	}

	/**
	 * 文件编码
	 */
	public M setCDocCode(java.lang.String cDocCode) {
		set("cDocCode", cDocCode);
		return (M)this;
	}

	/**
	 * 文件编码
	 */
	@JBoltField(name="cdoccode" ,columnName="cDocCode",type="String", remark="文件编码", required=false, maxLength=50, fixed=0, order=8)
	@JSONField(name = "cdoccode")
	public java.lang.String getCDocCode() {
		return getStr("cDocCode");
	}

	/**
	 * 版本/版次
	 */
	public M setCBomVersion(java.lang.String cBomVersion) {
		set("cBomVersion", cBomVersion);
		return (M)this;
	}

	/**
	 * 版本/版次
	 */
	@JBoltField(name="cbomversion" ,columnName="cBomVersion",type="String", remark="版本/版次", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "cbomversion")
	public java.lang.String getCBomVersion() {
		return getStr("cBomVersion");
	}

	/**
	 * 启用日期
	 */
	public M setDEnableDate(java.util.Date dEnableDate) {
		set("dEnableDate", dEnableDate);
		return (M)this;
	}

	/**
	 * 启用日期
	 */
	@JBoltField(name="denabledate" ,columnName="dEnableDate",type="Date", remark="启用日期", required=false, maxLength=10, fixed=0, order=10)
	@JSONField(name = "denabledate")
	public java.util.Date getDEnableDate() {
		return getDate("dEnableDate");
	}

	/**
	 * 停用日期
	 */
	public M setDDisableDate(java.util.Date dDisableDate) {
		set("dDisableDate", dDisableDate);
		return (M)this;
	}

	/**
	 * 停用日期
	 */
	@JBoltField(name="ddisabledate" ,columnName="dDisableDate",type="Date", remark="停用日期", required=false, maxLength=10, fixed=0, order=11)
	@JSONField(name = "ddisabledate")
	public java.util.Date getDDisableDate() {
		return getDate("dDisableDate");
	}

	/**
	 * 序号1
	 */
	public M setCNo1(java.lang.String cNo1) {
		set("cNo1", cNo1);
		return (M)this;
	}

	/**
	 * 序号1
	 */
	@JBoltField(name="cno1" ,columnName="cNo1",type="String", remark="序号1", required=true, maxLength=3, fixed=0, order=12)
	@JSONField(name = "cno1")
	public java.lang.String getCNo1() {
		return getStr("cNo1");
	}

	/**
	 * 设变号1
	 */
	public M setCDcNo1(java.lang.String cDcNo1) {
		set("cDcNo1", cDcNo1);
		return (M)this;
	}

	/**
	 * 设变号1
	 */
	@JBoltField(name="cdcno1" ,columnName="cDcNo1",type="String", remark="设变号1", required=false, maxLength=50, fixed=0, order=13)
	@JSONField(name = "cdcno1")
	public java.lang.String getCDcNo1() {
		return getStr("cDcNo1");
	}

	/**
	 * 设备日期1
	 */
	public M setDDcDate1(java.util.Date dDcDate1) {
		set("dDcDate1", dDcDate1);
		return (M)this;
	}

	/**
	 * 设备日期1
	 */
	@JBoltField(name="ddcdate1" ,columnName="dDcDate1",type="Date", remark="设备日期1", required=false, maxLength=10, fixed=0, order=14)
	@JSONField(name = "ddcdate1")
	public java.util.Date getDDcDate1() {
		return getDate("dDcDate1");
	}

	/**
	 * 序号2
	 */
	public M setCNo2(java.lang.String cNo2) {
		set("cNo2", cNo2);
		return (M)this;
	}

	/**
	 * 序号2
	 */
	@JBoltField(name="cno2" ,columnName="cNo2",type="String", remark="序号2", required=true, maxLength=3, fixed=0, order=15)
	@JSONField(name = "cno2")
	public java.lang.String getCNo2() {
		return getStr("cNo2");
	}

	/**
	 * 设变号2
	 */
	public M setCDcNo2(java.lang.String cDcNo2) {
		set("cDcNo2", cDcNo2);
		return (M)this;
	}

	/**
	 * 设变号2
	 */
	@JBoltField(name="cdcno2" ,columnName="cDcNo2",type="String", remark="设变号2", required=false, maxLength=50, fixed=0, order=16)
	@JSONField(name = "cdcno2")
	public java.lang.String getCDcNo2() {
		return getStr("cDcNo2");
	}

	/**
	 * 设变日期2
	 */
	public M setDDcDate2(java.util.Date dDcDate2) {
		set("dDcDate2", dDcDate2);
		return (M)this;
	}

	/**
	 * 设变日期2
	 */
	@JBoltField(name="ddcdate2" ,columnName="dDcDate2",type="Date", remark="设变日期2", required=false, maxLength=10, fixed=0, order=17)
	@JSONField(name = "ddcdate2")
	public java.util.Date getDDcDate2() {
		return getDate("dDcDate2");
	}

	/**
	 * 序号3
	 */
	public M setCNo3(java.lang.String cNo3) {
		set("cNo3", cNo3);
		return (M)this;
	}

	/**
	 * 序号3
	 */
	@JBoltField(name="cno3" ,columnName="cNo3",type="String", remark="序号3", required=false, maxLength=3, fixed=0, order=18)
	@JSONField(name = "cno3")
	public java.lang.String getCNo3() {
		return getStr("cNo3");
	}

	/**
	 * 设变号3
	 */
	public M setCDcNo3(java.lang.String cDcNo3) {
		set("cDcNo3", cDcNo3);
		return (M)this;
	}

	/**
	 * 设变号3
	 */
	@JBoltField(name="cdcno3" ,columnName="cDcNo3",type="String", remark="设变号3", required=false, maxLength=50, fixed=0, order=19)
	@JSONField(name = "cdcno3")
	public java.lang.String getCDcNo3() {
		return getStr("cDcNo3");
	}

	/**
	 * 设变日期3
	 */
	public M setDDcDate3(java.util.Date dDcDate3) {
		set("dDcDate3", dDcDate3);
		return (M)this;
	}

	/**
	 * 设变日期3
	 */
	@JBoltField(name="ddcdate3" ,columnName="dDcDate3",type="Date", remark="设变日期3", required=false, maxLength=10, fixed=0, order=20)
	@JSONField(name = "ddcdate3")
	public java.util.Date getDDcDate3() {
		return getDate("dDcDate3");
	}

	/**
	 * 客户ID
	 */
	public M setICustomerId(java.lang.Long iCustomerId) {
		set("iCustomerId", iCustomerId);
		return (M)this;
	}

	/**
	 * 客户ID
	 */
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=false, maxLength=19, fixed=0, order=21)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
	}

	/**
	 * 备注共用件
	 */
	public M setCCommonPartMemo(java.lang.String cCommonPartMemo) {
		set("cCommonPartMemo", cCommonPartMemo);
		return (M)this;
	}

	/**
	 * 备注共用件
	 */
	@JBoltField(name="ccommonpartmemo" ,columnName="cCommonPartMemo",type="String", remark="备注共用件", required=false, maxLength=200, fixed=0, order=22)
	@JSONField(name = "ccommonpartmemo")
	public java.lang.String getCCommonPartMemo() {
		return getStr("cCommonPartMemo");
	}

	/**
	 * 上级ID
	 */
	public M setIPid(java.lang.Long iPid) {
		set("iPid", iPid);
		return (M)this;
	}

	/**
	 * 上级ID
	 */
	@JBoltField(name="ipid" ,columnName="iPid",type="Long", remark="上级ID", required=false, maxLength=19, fixed=0, order=23)
	@JSONField(name = "ipid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIPid() {
		return getLong("iPid");
	}

	/**
	 * 层级
	 */
	public M setILevel(java.lang.Integer iLevel) {
		set("iLevel", iLevel);
		return (M)this;
	}

	/**
	 * 层级
	 */
	@JBoltField(name="ilevel" ,columnName="iLevel",type="Integer", remark="层级", required=false, maxLength=10, fixed=0, order=24)
	@JSONField(name = "ilevel")
	public java.lang.Integer getILevel() {
		return getInt("iLevel");
	}

	/**
	 * 来源：1.MES 2. U8
	 */
	public M setISource(java.lang.Integer iSource) {
		set("iSource", iSource);
		return (M)this;
	}

	/**
	 * 来源：1.MES 2. U8
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源：1.MES 2. U8", required=true, maxLength=10, fixed=0, order=25)
	@JSONField(name = "isource")
	public java.lang.Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源标识ID
	 */
	public M setISourceId(java.lang.String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源标识ID
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源标识ID", required=false, maxLength=32, fixed=0, order=26)
	@JSONField(name = "isourceid")
	public java.lang.String getISourceId() {
		return getStr("iSourceId");
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
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用", required=true, maxLength=1, fixed=0, order=27)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=28)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
	}

	/**
	 * 审核时间，未走审批流时，有值
	 */
	public M setDAuditTime(java.util.Date dAuditTime) {
		set("dAuditTime", dAuditTime);
		return (M)this;
	}

	/**
	 * 审核时间，未走审批流时，有值
	 */
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间，未走审批流时，有值", required=false, maxLength=23, fixed=3, order=29)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=30)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=31)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=32)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=33)
	@JSONField(name = "ccreatename")
	public java.lang.String getCCreateName() {
		return getStr("cCreateName");
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=34)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=35)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=36)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 是否删除 1已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}
	
	/**
	 * 是否删除 1已删除
	 */
	@JBoltField(name="iseffective" ,columnName="isEffective",type="Boolean", remark="是否删除 1已删除", required=true, maxLength=1, fixed=0, order=37)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}
	
	/**
	 * 是否生效：0. 否 1. 是
	 */
	public M setIsEffective(java.lang.Boolean isEffective) {
		set("isEffective", isEffective);
		return (M)this;
	}
	
	/**
	 * 是否生效：0. 否 1. 是
	 */
	@JBoltField(name="iseffective" ,columnName="isEffective",type="Boolean", remark="是否生效：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=38)
	@JSONField(name = "iseffective")
	public java.lang.Boolean getIsEffective() {
		return getBoolean("Effective");
	}
}

