package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * 仓库建模-仓库档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWarehouse<M extends BaseWarehouse<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**仓库编码*/
    public static final String CWHCODE = "cWhCode";
    /**仓库名称*/
    public static final String CWHNAME = "cWhName";
    /**所属部门*/
    public static final String CDEPCODE = "cDepCode";
    /**仓库地址*/
    public static final String CWHADDRESS = "cWhAddress";
    /**电话*/
    public static final String CWHPHONE = "cWhPhone";
    /**负责人*/
    public static final String CWHPERSON = "cWhPerson";
    /**计价方式*/
    public static final String CWHVALUESTYLE = "cWhValueStyle";
    /**仓库期末处理会计期*/
    public static final String CMONTH = "cMonth";
    /**备注*/
    public static final String CWHMEMO = "cWhMemo";
    /**是否冻结*/
    public static final String BFREEZE = "bFreeze";
    /**条形码*/
    public static final String CBARCODE = "cBarCode";
    /**是否参与MRP运算*/
    public static final String BMRP = "bMRP";
    /**盘点周期*/
    public static final String IFREQUENCY = "iFrequency";
    /**盘点周期单位*/
    public static final String CFREQUENCY = "cFrequency";
    /**每第几天*/
    public static final String IDAYS = "iDays";
    /**上次盘点日期*/
    public static final String DLASTDATE = "dLastDate";
    /**仓库属性*/
    public static final String IWHPROPERTY = "iWHProperty";
    /**是否门店*/
    public static final String BSHOP = "bShop";
    /**记入成本*/
    public static final String BINCOST = "bInCost";
    /**纳入可用量计算*/
    public static final String BINAVAILCALCU = "bInAvailCalcu";
    /**代管仓*/
    public static final String BPROXYWH = "bProxyWh";
    /**销售可用量控制方式*/
    public static final String ISACONMODE = "iSAConMode";
    /**出口可用量控制方式*/
    public static final String IEXCONMODE = "iEXConMode";
    /**库存可用量控制方式*/
    public static final String ISTCONMODE = "iSTConMode";
    /**是否保税仓*/
    public static final String BBONDEDWH = "bBondedWh";
    /**资产仓*/
    public static final String BWHASSET = "bWhAsset";
    /**配额(%)*/
    public static final String FWHQUOTA = "fWhQuota";
    /**停用日期*/
    public static final String DWHENDDATE = "dWhEndDate";
    /**是否核算分项成本*/
    public static final String BCHECKSUBITEMCOST = "bCheckSubitemCost";
    /**拣货货位 */
    public static final String CPICKPOS = "cPickPos";
    /**电商仓*/
    public static final String BEB = "bEB";
    /**修改日期*/
    public static final String DMODIFYDATE = "dModifyDate";
    /**仓库核算组*/
    public static final String CWAREGROUPCODE = "cWareGroupCode";
    /**省/直辖市*/
    public static final String CPROVINCE = "cProvince";
    /**市*/
    public static final String CCITY = "cCity";
    /**区县*/
    public static final String CCOUNTY = "cCounty";
    /**删除状态: 0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
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
	/**来源编码*/
    public static final String ISOURCE = "iSource";
	/**来源ID*/
	public static final String ISOURCEID = "iSourceId";
	/**启用空间掌控：0. 否 1. 是*/
	public static final String ISSPACECONTROLENABLED = "isSpaceControlEnabled";
	/**启用库存预警：0. 否 1. 是*/
	public static final String ISSTOCKWARNENABLED = "isStockWarnEnabled";
	/**最大存储数量*/
	public static final String IMAXSTOCK = "iMaxStock";
	/**最大存储空间*/
	public static final String IMAXSPACE = "iMaxSpace";
	/**
	 * 主键ID
	 */
	public M setIAutoId(Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 组织ID
	 */
	public M setIOrgId(Long iOrgId) {
		set("iOrgId", iOrgId);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iorgid", serializeUsing = ToStringSerializer.class)
	public Long getIOrgId() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCOrgCode(String cOrgCode) {
		set("cOrgCode", cOrgCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=40, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCOrgName(String cOrgName) {
		set("cOrgName", cOrgName);
		return (M)this;
	}

	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "corgname")
	public String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 仓库编码
	 */
	public M setCWhCode(String cWhCode) {
		set("cWhCode", cWhCode);
		return (M)this;
	}

	/**
	 * 仓库编码
	 */
	@JBoltField(name="cwhcode" ,columnName="cWhCode",type="String", remark="仓库编码", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "cwhcode")
	public String getCWhCode() {
		return getStr("cWhCode");
	}

	/**
	 * 仓库名称
	 */
	public M setCWhName(String cWhName) {
		set("cWhName", cWhName);
		return (M)this;
	}

	/**
	 * 仓库名称
	 */
	@JBoltField(name="cwhname" ,columnName="cWhName",type="String", remark="仓库名称", required=false, maxLength=20, fixed=0, order=6)
	@JSONField(name = "cwhname")
	public String getCWhName() {
		return getStr("cWhName");
	}

	/**
	 * 所属部门
	 */
	public M setCDepCode(String cDepCode) {
		set("cDepCode", cDepCode);
		return (M)this;
	}

	/**
	 * 所属部门
	 */
	@JBoltField(name="cdepcode" ,columnName="cDepCode",type="String", remark="所属部门", required=false, maxLength=12, fixed=0, order=7)
	@JSONField(name = "cdepcode")
	public String getCDepCode() {
		return getStr("cDepCode");
	}

	/**
	 * 仓库地址
	 */
	public M setCWhAddress(String cWhAddress) {
		set("cWhAddress", cWhAddress);
		return (M)this;
	}

	/**
	 * 仓库地址
	 */
	@JBoltField(name="cwhaddress" ,columnName="cWhAddress",type="String", remark="仓库地址", required=false, maxLength=255, fixed=0, order=8)
	@JSONField(name = "cwhaddress")
	public String getCWhAddress() {
		return getStr("cWhAddress");
	}

	/**
	 * 电话
	 */
	public M setCWhPhone(String cWhPhone) {
		set("cWhPhone", cWhPhone);
		return (M)this;
	}

	/**
	 * 电话
	 */
	@JBoltField(name="cwhphone" ,columnName="cWhPhone",type="String", remark="电话", required=false, maxLength=100, fixed=0, order=9)
	@JSONField(name = "cwhphone")
	public String getCWhPhone() {
		return getStr("cWhPhone");
	}

	/**
	 * 负责人
	 */
	public M setCWhPerson(String cWhPerson) {
		set("cWhPerson", cWhPerson);
		return (M)this;
	}

	/**
	 * 负责人
	 */
	@JBoltField(name="cwhperson" ,columnName="cWhPerson",type="String", remark="负责人", required=false, maxLength=20, fixed=0, order=10)
	@JSONField(name = "cwhperson")
	public String getCWhPerson() {
		return getStr("cWhPerson");
	}

	/**
	 * 计价方式
	 */
	public M setCWhValueStyle(String cWhValueStyle) {
		set("cWhValueStyle", cWhValueStyle);
		return (M)this;
	}

	/**
	 * 计价方式
	 */
	@JBoltField(name="cwhvaluestyle" ,columnName="cWhValueStyle",type="String", remark="计价方式", required=true, maxLength=12, fixed=0, order=11)
	@JSONField(name = "cwhvaluestyle")
	public String getCWhValueStyle() {
		return getStr("cWhValueStyle");
	}

	/**
	 * 仓库期末处理会计期
	 */
	public M setCMonth(String cMonth) {
		set("cMonth", cMonth);
		return (M)this;
	}

	/**
	 * 仓库期末处理会计期
	 */
	@JBoltField(name="cmonth" ,columnName="cMonth",type="String", remark="仓库期末处理会计期", required=false, maxLength=6, fixed=0, order=12)
	@JSONField(name = "cmonth")
	public String getCMonth() {
		return getStr("cMonth");
	}

	/**
	 * 备注
	 */
	public M setCWhMemo(String cWhMemo) {
		set("cWhMemo", cWhMemo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="cwhmemo" ,columnName="cWhMemo",type="String", remark="备注", required=false, maxLength=20, fixed=0, order=13)
	@JSONField(name = "cwhmemo")
	public String getCWhMemo() {
		return getStr("cWhMemo");
	}

	/**
	 * 是否冻结
	 */
	public M setBFreeze(Boolean bFreeze) {
		set("bFreeze", bFreeze);
		return (M)this;
	}

	/**
	 * 是否冻结
	 */
	@JBoltField(name="bfreeze" ,columnName="bFreeze",type="Boolean", remark="是否冻结", required=true, maxLength=1, fixed=0, order=14)
	@JSONField(name = "bfreeze")
	public Boolean getBFreeze() {
		return getBoolean("bFreeze");
	}

	/**
	 * 条形码
	 */
	public M setCBarCode(String cBarCode) {
		set("cBarCode", cBarCode);
		return (M)this;
	}

	/**
	 * 条形码
	 */
	@JBoltField(name="cbarcode" ,columnName="cBarCode",type="String", remark="条形码", required=false, maxLength=30, fixed=0, order=15)
	@JSONField(name = "cbarcode")
	public String getCBarCode() {
		return getStr("cBarCode");
	}

	/**
	 * 是否参与MRP运算
	 */
	public M setBMRP(Boolean bMRP) {
		set("bMRP", bMRP);
		return (M)this;
	}

	/**
	 * 是否参与MRP运算
	 */
	@JBoltField(name="bmrp" ,columnName="bMRP",type="Boolean", remark="是否参与MRP运算", required=true, maxLength=1, fixed=0, order=16)
	@JSONField(name = "bmrp")
	public Boolean getBMRP() {
		return getBoolean("bMRP");
	}

	/**
	 * 盘点周期
	 */
	public M setIFrequency(Short iFrequency) {
		set("iFrequency", iFrequency);
		return (M)this;
	}

	/**
	 * 盘点周期
	 */
	@JBoltField(name="ifrequency" ,columnName="iFrequency",type="", remark="盘点周期", required=false, maxLength=5, fixed=0, order=17)
	@JSONField(name = "ifrequency")
	public java.lang.Short getIFrequency() {
		return getShort("iFrequency");
	}

	/**
	 * 盘点周期单位
	 */
	public M setCFrequency(java.lang.String cFrequency) {
		set("cFrequency", cFrequency);
		return (M)this;
	}

	/**
	 * 盘点周期单位
	 */
	@JBoltField(name="cfrequency" ,columnName="cFrequency",type="String", remark="盘点周期单位", required=false, maxLength=10, fixed=0, order=18)
	@JSONField(name = "cfrequency")
	public java.lang.String getCFrequency() {
		return getStr("cFrequency");
	}

	/**
	 * 每第几天
	 */
	public M setIDays(java.lang.Short iDays) {
		set("iDays", iDays);
		return (M)this;
	}

	/**
	 * 每第几天
	 */
	@JBoltField(name="idays" ,columnName="iDays",type="", remark="每第几天", required=false, maxLength=5, fixed=0, order=19)
	@JSONField(name = "idays")
	public java.lang.Short getIDays() {
		return getShort("iDays");
	}

	/**
	 * 上次盘点日期
	 */
	public M setDLastDate(java.util.Date dLastDate) {
		set("dLastDate", dLastDate);
		return (M)this;
	}

	/**
	 * 上次盘点日期
	 */
	@JBoltField(name="dlastdate" ,columnName="dLastDate",type="Date", remark="上次盘点日期", required=false, maxLength=23, fixed=3, order=20)
	@JSONField(name = "dlastdate")
	public java.util.Date getDLastDate() {
		return getDate("dLastDate");
	}

	/**
	 * 仓库属性
	 */
	public M setIWHProperty(java.lang.Short iWHProperty) {
		set("iWHProperty", iWHProperty);
		return (M)this;
	}

	/**
	 * 仓库属性
	 */
	@JBoltField(name="iwhproperty" ,columnName="iWHProperty",type="", remark="仓库属性", required=true, maxLength=5, fixed=0, order=21)
	@JSONField(name = "iwhproperty")
	public java.lang.Short getIWHProperty() {
		return getShort("iWHProperty");
	}

	/**
	 * 是否门店
	 */
	public M setBShop(java.lang.Boolean bShop) {
		set("bShop", bShop);
		return (M)this;
	}

	/**
	 * 是否门店
	 */
	@JBoltField(name="bshop" ,columnName="bShop",type="Boolean", remark="是否门店", required=true, maxLength=1, fixed=0, order=22)
	@JSONField(name = "bshop")
	public java.lang.Boolean getBShop() {
		return getBoolean("bShop");
	}

	/**
	 * 记入成本
	 */
	public M setBInCost(java.lang.Boolean bInCost) {
		set("bInCost", bInCost);
		return (M)this;
	}

	/**
	 * 记入成本
	 */
	@JBoltField(name="bincost" ,columnName="bInCost",type="Boolean", remark="记入成本", required=true, maxLength=1, fixed=0, order=24)
	@JSONField(name = "bincost")
	public java.lang.Boolean getBInCost() {
		return getBoolean("bInCost");
	}

	/**
	 * 纳入可用量计算
	 */
	public M setBInAvailCalcu(java.lang.Boolean bInAvailCalcu) {
		set("bInAvailCalcu", bInAvailCalcu);
		return (M)this;
	}

	/**
	 * 纳入可用量计算
	 */
	@JBoltField(name="binavailcalcu" ,columnName="bInAvailCalcu",type="Boolean", remark="纳入可用量计算", required=true, maxLength=1, fixed=0, order=25)
	@JSONField(name = "binavailcalcu")
	public java.lang.Boolean getBInAvailCalcu() {
		return getBoolean("bInAvailCalcu");
	}

	/**
	 * 代管仓
	 */
	public M setBProxyWh(java.lang.Boolean bProxyWh) {
		set("bProxyWh", bProxyWh);
		return (M)this;
	}

	/**
	 * 代管仓
	 */
	@JBoltField(name="bproxywh" ,columnName="bProxyWh",type="Boolean", remark="代管仓", required=true, maxLength=1, fixed=0, order=26)
	@JSONField(name = "bproxywh")
	public java.lang.Boolean getBProxyWh() {
		return getBoolean("bProxyWh");
	}

	/**
	 * 销售可用量控制方式
	 */
	public M setISAConMode(java.lang.Short iSAConMode) {
		set("iSAConMode", iSAConMode);
		return (M)this;
	}

	/**
	 * 销售可用量控制方式
	 */
	@JBoltField(name="isaconmode" ,columnName="iSAConMode",type="", remark="销售可用量控制方式", required=true, maxLength=5, fixed=0, order=27)
	@JSONField(name = "isaconmode")
	public java.lang.Short getISAConMode() {
		return getShort("iSAConMode");
	}

	/**
	 * 出口可用量控制方式
	 */
	public M setIEXConMode(Short iEXConMode) {
		set("iEXConMode", iEXConMode);
		return (M)this;
	}

	/**
	 * 出口可用量控制方式
	 */
	@JBoltField(name="iexconmode" ,columnName="iEXConMode",type="", remark="出口可用量控制方式", required=true, maxLength=5, fixed=0, order=28)
	@JSONField(name = "iexconmode")
	public Short getIEXConMode() {
		return getShort("iEXConMode");
	}

	/**
	 * 库存可用量控制方式
	 */
	public M setISTConMode(Short iSTConMode) {
		set("iSTConMode", iSTConMode);
		return (M)this;
	}

	/**
	 * 库存可用量控制方式
	 */
	@JBoltField(name="istconmode" ,columnName="iSTConMode",type="", remark="库存可用量控制方式", required=true, maxLength=5, fixed=0, order=29)
	@JSONField(name = "istconmode")
	public Short getISTConMode() {
		return getShort("iSTConMode");
	}

	/**
	 * 是否保税仓
	 */
	public M setBBondedWh(Boolean bBondedWh) {
		set("bBondedWh", bBondedWh);
		return (M)this;
	}

	/**
	 * 是否保税仓
	 */
	@JBoltField(name="bbondedwh" ,columnName="bBondedWh",type="Boolean", remark="是否保税仓", required=true, maxLength=1, fixed=0, order=30)
	@JSONField(name = "bbondedwh")
	public Boolean getBBondedWh() {
		return getBoolean("bBondedWh");
	}

	/**
	 * 资产仓
	 */
	public M setBWhAsset(Boolean bWhAsset) {
		set("bWhAsset", bWhAsset);
		return (M)this;
	}

	/**
	 * 资产仓
	 */
	@JBoltField(name="bwhasset" ,columnName="bWhAsset",type="Boolean", remark="资产仓", required=true, maxLength=1, fixed=0, order=31)
	@JSONField(name = "bwhasset")
	public Boolean getBWhAsset() {
		return getBoolean("bWhAsset");
	}

	/**
	 * 配额(%)
	 */
	public M setFWhQuota(Double fWhQuota) {
		set("fWhQuota", fWhQuota);
		return (M)this;
	}

	/**
	 * 配额(%)
	 */
	@JBoltField(name="fwhquota" ,columnName="fWhQuota",type="Double", remark="资产仓", required=false, maxLength=53, fixed=0, order=32)
	@JSONField(name = "fwhquota")
	public Double getFWhQuota() {
		return getDouble("fWhQuota");
	}

	/**
	 * 停用日期
	 */
	public M setDWhEndDate(Date dWhEndDate) {
		set("dWhEndDate", dWhEndDate);
		return (M)this;
	}

	/**
	 * 停用日期
	 */
	@JBoltField(name="dwhenddate" ,columnName="dWhEndDate",type="Date", remark="停用日期", required=false, maxLength=23, fixed=3, order=33)
	@JSONField(name = "dwhenddate")
	public Date getDWhEndDate() {
		return getDate("dWhEndDate");
	}

	/**
	 * 是否核算分项成本
	 */
	public M setBCheckSubitemCost(Boolean bCheckSubitemCost) {
		set("bCheckSubitemCost", bCheckSubitemCost);
		return (M)this;
	}

	/**
	 * 是否核算分项成本
	 */
	@JBoltField(name="bchecksubitemcost" ,columnName="bCheckSubitemCost",type="Boolean", remark="是否核算分项成本", required=false, maxLength=1, fixed=0, order=34)
	@JSONField(name = "bchecksubitemcost")
	public Boolean getBCheckSubitemCost() {
		return getBoolean("bCheckSubitemCost");
	}

	/**
	 * 拣货货位 
	 */
	public M setCPickPos(String cPickPos) {
		set("cPickPos", cPickPos);
		return (M)this;
	}

	/**
	 * 拣货货位 
	 */
	@JBoltField(name="cpickpos" ,columnName="cPickPos",type="String", remark="拣货货位 ", required=false, maxLength=40, fixed=0, order=35)
	@JSONField(name = "cpickpos")
	public String getCPickPos() {
		return getStr("cPickPos");
	}

	/**
	 * 电商仓
	 */
	public M setBEB(Boolean bEB) {
		set("bEB", bEB);
		return (M)this;
	}

	/**
	 * 电商仓
	 */
	@JBoltField(name="beb" ,columnName="bEB",type="Boolean", remark="电商仓", required=false, maxLength=1, fixed=0, order=36)
	@JSONField(name = "beb")
	public Boolean getBEB() {
		return getBoolean("bEB");
	}

	/**
	 * 修改日期
	 */
	public M setDModifyDate(Date dModifyDate) {
		set("dModifyDate", dModifyDate);
		return (M)this;
	}

	/**
	 * 修改日期
	 */
	@JBoltField(name="dmodifydate" ,columnName="dModifyDate",type="Date", remark="修改日期", required=false, maxLength=23, fixed=3, order=37)
	@JSONField(name = "dmodifydate")
	public Date getDModifyDate() {
		return getDate("dModifyDate");
	}

	/**
	 * 仓库核算组
	 */
	public M setCWareGroupCode(String cWareGroupCode) {
		set("cWareGroupCode", cWareGroupCode);
		return (M)this;
	}

	/**
	 * 仓库核算组
	 */
	@JBoltField(name="cwaregroupcode" ,columnName="cWareGroupCode",type="String", remark="仓库核算组", required=false, maxLength=100, fixed=0, order=38)
	@JSONField(name = "cwaregroupcode")
	public String getCWareGroupCode() {
		return getStr("cWareGroupCode");
	}

	/**
	 * 省/直辖市
	 */
	public M setCProvince(String cProvince) {
		set("cProvince", cProvince);
		return (M)this;
	}

	/**
	 * 省/直辖市
	 */
	@JBoltField(name="cprovince" ,columnName="cProvince",type="String", remark="省/直辖市", required=false, maxLength=100, fixed=0, order=39)
	@JSONField(name = "cprovince")
	public String getCProvince() {
		return getStr("cProvince");
	}

	/**
	 * 市
	 */
	public M setCCity(String cCity) {
		set("cCity", cCity);
		return (M)this;
	}

	/**
	 * 市
	 */
	@JBoltField(name="ccity" ,columnName="cCity",type="String", remark="市", required=false, maxLength=100, fixed=0, order=40)
	@JSONField(name = "ccity")
	public String getCCity() {
		return getStr("cCity");
	}

	/**
	 * 区县
	 */
	public M setCCounty(String cCounty) {
		set("cCounty", cCounty);
		return (M)this;
	}

	/**
	 * 区县
	 */
	@JBoltField(name="ccounty" ,columnName="cCounty",type="String", remark="区县", required=false, maxLength=100, fixed=0, order=41)
	@JSONField(name = "ccounty")
	public String getCCounty() {
		return getStr("cCounty");
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	public M setIsDeleted(Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态: 0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=42)
	@JSONField(name = "isdeleted")
	public Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 创建人
	 */
	public M setIcreateby(Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="iCreateBy" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIcreateby() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCcreatename(String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}
	/**
	 * 创建人名称
	 */
	@JBoltField(name="cCreateName" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=255, fixed=0, order=7)
	public String getCcreatename() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDCreateTime(Date dCreateTime) {
		set("dCreateTime", dCreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=43)
	@JSONField(name = "dcreatetime")
	public Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新人
	 */
	public M setIupdateby(Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="iUpdateBy" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=9)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIupdateby() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCupdatename(String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 更新人名称
	 */
	@JBoltField(name="cUpdateName" ,columnName="cUpdateName",type="String", remark="更新人名称", required=false, maxLength=255, fixed=0, order=10)
	public String getCupdatename() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDupdatetime(Date dUpdateTime) {
		set("dUpdateTime", dUpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dUpdateTime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=11)
	public Date getDupdatetime() {
		return getDate("dUpdateTime");
	}


	/**
	 * 来源1.MES 2.U8;
	 */
	public M setISource(Integer isource) {
		set("iSource", isource);
		return (M)this;
	}

	/**
	 * 来源1.MES 2.U8;
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源1.MES 2.U8", required=false, maxLength=10, fixed=0, order=15)
	public Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源ID
	 */
	public M setISourceId(String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源ID
	 */
	@JBoltField(name="iSourceId" ,columnName="iSourceId",type="String", remark="来源ID",  required=false, maxLength=100, fixed=0,  order=45)
	public String getISourceId() {
		return getStr("iSourceId");
	}

	/**
	 * 启用空间掌控：0. 否 1. 是
	 */
	public M setIsSpaceControlEnabled(Boolean isSpaceControlEnabled) {
		set("isSpaceControlEnabled", isSpaceControlEnabled);
		return (M)this;
	}

	/**
	 * 启用空间掌控：0. 否 1. 是
	 */
	@JBoltField(name="isSpaceControlEnabled" ,columnName="isSpaceControlEnabled",type="Boolean", remark="启用空间掌控：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=8)
	public Boolean getIsSpaceControlEnabled() {
		return getBoolean("isSpaceControlEnabled");
	}

	/**
	 * 启用库存预警：0. 否 1. 是
	 */
	public M setIsStockWarnEnabled(java.lang.Boolean isStockWarnEnabled) {
		set("isStockWarnEnabled", isStockWarnEnabled);
		return (M)this;
	}

	/**
	 * 启用库存预警：0. 否 1. 是
	 */
	@JBoltField(name="isStockWarnEnabled" ,columnName="isStockWarnEnabled",type="Boolean", remark="启用库存预警：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=8)
	public java.lang.Boolean getIsStockWarnEnabled() {
		return getBoolean("isStockWarnEnabled");
	}

	/**
	 * 最大存储数量
	 */
	public M setIMaxStock(java.math.BigDecimal iMaxStock) {
		set("iMaxStock", iMaxStock);
		return (M)this;
	}

	/**
	 * 最大存储数量
	 */
	@JBoltField(name="iMaxStock" ,columnName="iMaxStock",type="BigDecimal", remark="最大存储数", required=false, maxLength=24, fixed=6, order=10)
	public java.math.BigDecimal getIMaxStock() {
		return getBigDecimal("iMaxStock");
	}

	/**
	 * 最大存储空间
	 */
	public M setImaxspace(java.math.BigDecimal imaxspace) {
		set("iMaxSpace", imaxspace);
		return (M)this;
	}

	/**
	 * 最大存储空间
	 */
	@JBoltField(name="imaxspace" ,columnName="iMaxSpace",type="BigDecimal", remark="最大存储空间", required=false, maxLength=24, fixed=6, order=11)
	public java.math.BigDecimal getImaxspace() {
		return getBigDecimal("iMaxSpace");
	}
}
