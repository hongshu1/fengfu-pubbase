package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户档案-联系地址
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCustomerAddr<M extends BaseCustomerAddr<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**客户档案ID*/
    public static final String ICUSTOMERID = "iCustomerId";
    /**位置编码*/
    public static final String CDISTRICTCODE = "cDistrictCode";
    /**位置名称*/
    public static final String CDISTRICTNAME = "cDistrictName";
    /**仓库ID*/
    public static final String IWAREHOUSEID = "iWarehouseId";
    /**计划代码*/
    public static final String CPLANCODE = "cPlanCode";
    /**搜索码*/
    public static final String CSEARCHCODE = "cSearchCode";
    /**联系人编码*/
    public static final String CCONTACTCODE = "cContactCode";
    /**联系人名称*/
    public static final String CCONTACTNAME = "cContactName";
    /**国家/地区*/
    public static final String CCOUNTRY = "cCountry";
    /**省/自治区*/
    public static final String CPROVINCE = "cProvince";
    /**城市*/
    public static final String CCITY = "cCity";
    /**区县*/
    public static final String CDISTRICT = "cDistrict";
    /**邮政编码*/
    public static final String CPOSTCODE = "cPostCode";
    /**移动电话*/
    public static final String CMOBILE = "cMobile";
    /**传真*/
    public static final String CFAX = "cFax";
    /**电子邮件*/
    public static final String CEMAIL = "cEmail";
    /**是否生效：0. 否 1. 是*/
    public static final String ISENABLED = "isEnabled";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
	/**发货提前期*/
	public static final String IDELIVERYADVANCE = "iDeliveryAdvance";
	/**固定电话*/
	public static final String CTELE = "cTele";
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
	 * 客户档案ID
	 */
	public M setICustomerId(java.lang.Long iCustomerId) {
		set("iCustomerId", iCustomerId);
		return (M)this;
	}

	/**
	 * 客户档案ID
	 */
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户档案ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
	}

	/**
	 * 位置编码
	 */
	public M setCDistrictCode(java.lang.String cDistrictCode) {
		set("cDistrictCode", cDistrictCode);
		return (M)this;
	}

	/**
	 * 位置编码
	 */
	@JBoltField(name="cdistrictcode" ,columnName="cDistrictCode",type="String", remark="位置编码", required=true, maxLength=30, fixed=0, order=3)
	@JSONField(name = "cdistrictcode")
	public java.lang.String getCDistrictCode() {
		return getStr("cDistrictCode");
	}

	/**
	 * 位置名称
	 */
	public M setCDistrictName(java.lang.String cDistrictName) {
		set("cDistrictName", cDistrictName);
		return (M)this;
	}

	/**
	 * 位置名称
	 */
	@JBoltField(name="cdistrictname" ,columnName="cDistrictName",type="String", remark="位置名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "cdistrictname")
	public java.lang.String getCDistrictName() {
		return getStr("cDistrictName");
	}

	/**
	 * 仓库ID
	 */
	public M setIWarehouseId(java.lang.Long iWarehouseId) {
		set("iWarehouseId", iWarehouseId);
		return (M)this;
	}

	/**
	 * 仓库ID
	 */
	@JBoltField(name="iwarehouseid" ,columnName="iWarehouseId",type="Long", remark="仓库ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "iwarehouseid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWarehouseId() {
		return getLong("iWarehouseId");
	}

	/**
	 * 计划代码
	 */
	public M setCPlanCode(java.lang.String cPlanCode) {
		set("cPlanCode", cPlanCode);
		return (M)this;
	}

	/**
	 * 计划代码
	 */
	@JBoltField(name="cplancode" ,columnName="cPlanCode",type="String", remark="计划代码", required=false, maxLength=50, fixed=0, order=6)
	@JSONField(name = "cplancode")
	public java.lang.String getCPlanCode() {
		return getStr("cPlanCode");
	}

	/**
	 * 搜索码
	 */
	public M setCSearchCode(java.lang.String cSearchCode) {
		set("cSearchCode", cSearchCode);
		return (M)this;
	}

	/**
	 * 搜索码
	 */
	@JBoltField(name="csearchcode" ,columnName="cSearchCode",type="String", remark="搜索码", required=false, maxLength=50, fixed=0, order=7)
	@JSONField(name = "csearchcode")
	public java.lang.String getCSearchCode() {
		return getStr("cSearchCode");
	}

	/**
	 * 联系人编码
	 */
	public M setCContactCode(java.lang.String cContactCode) {
		set("cContactCode", cContactCode);
		return (M)this;
	}

	/**
	 * 联系人编码
	 */
	@JBoltField(name="ccontactcode" ,columnName="cContactCode",type="String", remark="联系人编码", required=false, maxLength=50, fixed=0, order=8)
	@JSONField(name = "ccontactcode")
	public java.lang.String getCContactCode() {
		return getStr("cContactCode");
	}

	/**
	 * 联系人名称
	 */
	public M setCContactName(java.lang.String cContactName) {
		set("cContactName", cContactName);
		return (M)this;
	}

	/**
	 * 联系人名称
	 */
	@JBoltField(name="ccontactname" ,columnName="cContactName",type="String", remark="联系人名称", required=false, maxLength=50, fixed=0, order=9)
	@JSONField(name = "ccontactname")
	public java.lang.String getCContactName() {
		return getStr("cContactName");
	}

	/**
	 * 国家/地区
	 */
	public M setCCountry(java.lang.String cCountry) {
		set("cCountry", cCountry);
		return (M)this;
	}

	/**
	 * 国家/地区
	 */
	@JBoltField(name="ccountry" ,columnName="cCountry",type="String", remark="国家/地区", required=false, maxLength=50, fixed=0, order=10)
	@JSONField(name = "ccountry")
	public java.lang.String getCCountry() {
		return getStr("cCountry");
	}

	/**
	 * 省/自治区
	 */
	public M setCProvince(java.lang.String cProvince) {
		set("cProvince", cProvince);
		return (M)this;
	}

	/**
	 * 省/自治区
	 */
	@JBoltField(name="cprovince" ,columnName="cProvince",type="String", remark="省/自治区", required=false, maxLength=50, fixed=0, order=11)
	@JSONField(name = "cprovince")
	public java.lang.String getCProvince() {
		return getStr("cProvince");
	}

	/**
	 * 城市
	 */
	public M setCCity(java.lang.String cCity) {
		set("cCity", cCity);
		return (M)this;
	}

	/**
	 * 城市
	 */
	@JBoltField(name="ccity" ,columnName="cCity",type="String", remark="城市", required=false, maxLength=50, fixed=0, order=12)
	@JSONField(name = "ccity")
	public java.lang.String getCCity() {
		return getStr("cCity");
	}

	/**
	 * 区县
	 */
	public M setCDistrict(java.lang.String cDistrict) {
		set("cDistrict", cDistrict);
		return (M)this;
	}

	/**
	 * 区县
	 */
	@JBoltField(name="cdistrict" ,columnName="cDistrict",type="String", remark="区县", required=false, maxLength=50, fixed=0, order=13)
	@JSONField(name = "cdistrict")
	public java.lang.String getCDistrict() {
		return getStr("cDistrict");
	}

	/**
	 * 邮政编码
	 */
	public M setCPostCode(java.lang.String cPostCode) {
		set("cPostCode", cPostCode);
		return (M)this;
	}

	/**
	 * 邮政编码
	 */
	@JBoltField(name="cpostcode" ,columnName="cPostCode",type="String", remark="邮政编码", required=false, maxLength=20, fixed=0, order=14)
	@JSONField(name = "cpostcode")
	public java.lang.String getCPostCode() {
		return getStr("cPostCode");
	}

	/**
	 * 移动电话
	 */
	public M setCMobile(java.lang.String cMobile) {
		set("cMobile", cMobile);
		return (M)this;
	}

	/**
	 * 移动电话
	 */
	@JBoltField(name="cmobile" ,columnName="cMobile",type="String", remark="移动电话", required=false, maxLength=20, fixed=0, order=15)
	@JSONField(name = "cmobile")
	public java.lang.String getCMobile() {
		return getStr("cMobile");
	}

	/**
	 * 传真
	 */
	public M setCFax(java.lang.String cFax) {
		set("cFax", cFax);
		return (M)this;
	}

	/**
	 * 传真
	 */
	@JBoltField(name="cfax" ,columnName="cFax",type="String", remark="传真", required=false, maxLength=20, fixed=0, order=16)
	@JSONField(name = "cfax")
	public java.lang.String getCFax() {
		return getStr("cFax");
	}

	/**
	 * 电子邮件
	 */
	public M setCEmail(java.lang.String cEmail) {
		set("cEmail", cEmail);
		return (M)this;
	}

	/**
	 * 电子邮件
	 */
	@JBoltField(name="cemail" ,columnName="cEmail",type="String", remark="电子邮件", required=false, maxLength=20, fixed=0, order=17)
	@JSONField(name = "cemail")
	public java.lang.String getCEmail() {
		return getStr("cEmail");
	}

	/**
	 * 是否生效：0. 否 1. 是
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否生效：0. 否 1. 是
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否生效：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=18)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=19)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 固定电话
	 */
	public M setCtele(java.lang.String ctele) {
		set("cTele", ctele);
		return (M)this;
	}

	/**
	 * 固定电话
	 */
	@JBoltField(name="ctele" ,columnName="cTele",type="String", remark="固定电话", required=false, maxLength=255, fixed=0, order=16)
	public java.lang.String getCtele() {
		return getStr("cTele");
	}

	/**
	 * 发货提前期
	 */
	public M setIdeliveryadvance(java.lang.Integer ideliveryadvance) {
		set("iDeliveryAdvance", ideliveryadvance);
		return (M)this;
	}
	/**
	 * 发货提前期
	 */
	@JBoltField(name="ideliveryadvance" ,columnName="iDeliveryAdvance",type="Integer", remark="发货提前期", required=true, maxLength=255, fixed=0, order=24)
	public java.lang.Integer getIdeliveryadvance() {
		return getInt("iDeliveryAdvance");
	}

}

