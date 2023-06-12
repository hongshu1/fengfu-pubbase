package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 物料建模-BOM主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseBomM<M extends BaseBomM<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**新增来源;1.导入新增 2.手工新增*/
    public static final String ITYPE = "iType";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**存货编码*/
    public static final String CINVCODE = "cInvCode";
    /**存货名称*/
    public static final String CINVNAME = "cInvName";
    /**版本号*/
    public static final String CVERSION = "cVersion";
    /**启用日期*/
    public static final String DENABLEDATE = "dEnableDate";
    /**停用日期*/
    public static final String DDISABLEDATE = "dDisableDate";
    /**机型ID*/
    public static final String IEQUIPMENTMODELID = "iEquipmentModelId";
    /**文件名称*/
    public static final String CDOCNAME = "cDocName";
    /**文件编码*/
    public static final String CDOCCODE = "cDocCode";
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
    /**来源：1.MES 2. U8*/
    public static final String ISOURCE = "iSource";
    /**来源标识ID*/
    public static final String ISOURCEID = "iSourceId";
    /**是否生效：0. 否 1. 是*/
    public static final String ISEFFECTIVE = "isEffective";
    /**复制来源ID，复制操作生成*/
    public static final String ICOPYFROMID = "iCopyFromId";
    /**提审方式;1. 审核 2. 审批*/
    public static final String IAUDITWAY = "iAuditWay";
    /**提审时间*/
    public static final String DSUBMITTIME = "dSubmitTime";
    /**审核状态;0. 未审核 1. 审核中 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
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
    public static final String ISDELETED = "isDeleted";
    /**是否显示：0：否 1：是*/
    public static final String ISVIEW = "isView";
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
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=40, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 新增来源;1.导入新增 2.手工新增
	 */
	public M setIType(java.lang.Integer iType) {
		set("iType", iType);
		return (M)this;
	}

	/**
	 * 新增来源;1.导入新增 2.手工新增
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="新增来源;1.导入新增 2.手工新增", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "itype")
	public java.lang.Integer getIType() {
		return getInt("iType");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 存货编码
	 */
	public M setCInvCode(java.lang.String cInvCode) {
		set("cInvCode", cInvCode);
		return (M)this;
	}

	/**
	 * 存货编码
	 */
	@JBoltField(name="cinvcode" ,columnName="cInvCode",type="String", remark="存货编码", required=true, maxLength=200, fixed=0, order=7)
	@JSONField(name = "cinvcode")
	public java.lang.String getCInvCode() {
		return getStr("cInvCode");
	}

	/**
	 * 存货名称
	 */
	public M setCInvName(java.lang.String cInvName) {
		set("cInvName", cInvName);
		return (M)this;
	}

	/**
	 * 存货名称
	 */
	@JBoltField(name="cinvname" ,columnName="cInvName",type="String", remark="存货名称", required=true, maxLength=200, fixed=0, order=8)
	@JSONField(name = "cinvname")
	public java.lang.String getCInvName() {
		return getStr("cInvName");
	}

	/**
	 * 版本号
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 版本号
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="版本号", required=true, maxLength=4, fixed=0, order=9)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
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
	@JBoltField(name="denabledate" ,columnName="dEnableDate",type="Date", remark="启用日期", required=true, maxLength=10, fixed=0, order=10)
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
	@JBoltField(name="ddisabledate" ,columnName="dDisableDate",type="Date", remark="停用日期", required=true, maxLength=10, fixed=0, order=11)
	@JSONField(name = "ddisabledate")
	public java.util.Date getDDisableDate() {
		return getDate("dDisableDate");
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
	@JBoltField(name="iequipmentmodelid" ,columnName="iEquipmentModelId",type="Long", remark="机型ID", required=false, maxLength=19, fixed=0, order=12)
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
	@JBoltField(name="cdocname" ,columnName="cDocName",type="String", remark="文件名称", required=false, maxLength=50, fixed=0, order=13)
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
	@JBoltField(name="cdoccode" ,columnName="cDocCode",type="String", remark="文件编码", required=false, maxLength=50, fixed=0, order=14)
	@JSONField(name = "cdoccode")
	public java.lang.String getCDocCode() {
		return getStr("cDocCode");
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
	@JBoltField(name="cno1" ,columnName="cNo1",type="String", remark="序号1", required=false, maxLength=3, fixed=0, order=15)
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
	@JBoltField(name="cdcno1" ,columnName="cDcNo1",type="String", remark="设变号1", required=false, maxLength=50, fixed=0, order=16)
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
	@JBoltField(name="ddcdate1" ,columnName="dDcDate1",type="Date", remark="设备日期1", required=false, maxLength=10, fixed=0, order=17)
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
	@JBoltField(name="cno2" ,columnName="cNo2",type="String", remark="序号2", required=false, maxLength=3, fixed=0, order=18)
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
	@JBoltField(name="cdcno2" ,columnName="cDcNo2",type="String", remark="设变号2", required=false, maxLength=50, fixed=0, order=19)
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
	@JBoltField(name="ddcdate2" ,columnName="dDcDate2",type="Date", remark="设变日期2", required=false, maxLength=10, fixed=0, order=20)
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
	@JBoltField(name="cno3" ,columnName="cNo3",type="String", remark="序号3", required=false, maxLength=3, fixed=0, order=21)
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
	@JBoltField(name="cdcno3" ,columnName="cDcNo3",type="String", remark="设变号3", required=false, maxLength=50, fixed=0, order=22)
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
	@JBoltField(name="ddcdate3" ,columnName="dDcDate3",type="Date", remark="设变日期3", required=false, maxLength=10, fixed=0, order=23)
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
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=false, maxLength=19, fixed=0, order=24)
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
	@JBoltField(name="ccommonpartmemo" ,columnName="cCommonPartMemo",type="String", remark="备注共用件", required=false, maxLength=200, fixed=0, order=25)
	@JSONField(name = "ccommonpartmemo")
	public java.lang.String getCCommonPartMemo() {
		return getStr("cCommonPartMemo");
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
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源：1.MES 2. U8", required=true, maxLength=10, fixed=0, order=26)
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
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源标识ID", required=false, maxLength=32, fixed=0, order=27)
	@JSONField(name = "isourceid")
	public java.lang.String getISourceId() {
		return getStr("iSourceId");
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
	@JBoltField(name="iseffective" ,columnName="isEffective",type="Boolean", remark="是否生效：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=28)
	@JSONField(name = "iseffective")
	public java.lang.Boolean getIsEffective() {
		return getBoolean("isEffective");
	}

	/**
	 * 复制来源ID，复制操作生成
	 */
	public M setICopyFromId(java.lang.Long iCopyFromId) {
		set("iCopyFromId", iCopyFromId);
		return (M)this;
	}

	/**
	 * 复制来源ID，复制操作生成
	 */
	@JBoltField(name="icopyfromid" ,columnName="iCopyFromId",type="Long", remark="复制来源ID，复制操作生成", required=false, maxLength=19, fixed=0, order=29)
	@JSONField(name = "icopyfromid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICopyFromId() {
		return getLong("iCopyFromId");
	}

	/**
	 * 提审方式;1. 审核 2. 审批
	 */
	public M setIAuditWay(java.lang.Integer iAuditWay) {
		set("iAuditWay", iAuditWay);
		return (M)this;
	}

	/**
	 * 提审方式;1. 审核 2. 审批
	 */
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="提审方式;1. 审核 2. 审批", required=false, maxLength=10, fixed=0, order=30)
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
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=31)
	@JSONField(name = "dsubmittime")
	public java.util.Date getDSubmitTime() {
		return getDate("dSubmitTime");
	}

	/**
	 * 审核状态;0. 未审核 1. 审核中 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态;0. 未审核 1. 审核中 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态;0. 未审核 1. 审核中 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=32)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
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
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=33)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=34)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=40, fixed=0, order=35)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=36)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=37)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=40, fixed=0, order=38)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=39)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 删除状态;0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态;0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态;0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=40)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 是否显示：0：否 1：是
	 */
	public M setIsView(java.lang.Boolean isView) {
		set("isView", isView);
		return (M)this;
	}

	/**
	 * 是否显示：0：否 1：是
	 */
	@JBoltField(name="isview" ,columnName="isView",type="Boolean", remark="是否显示：0：否 1：是", required=true, maxLength=1, fixed=0, order=41)
	@JSONField(name = "isview")
	public java.lang.Boolean getIsView() {
		return getBoolean("isView");
	}

}
