package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 供应商档案-联系地址
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseVendorAddr<M extends BaseVendorAddr<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**供应商ID*/
    public static final String IVENDORID = "iVendorId";
    /**地点编码*/
    public static final String CDISTRICTCODE = "cDistrictCode";
    /**地点名称*/
    public static final String CDISTRICTNAME = "cDistrictName";
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
    public static final String CCOUNTY = "cCounty";
    /**地址*/
    public static final String CADDR = "cAddr";
    /**邮政编码*/
    public static final String CPOSTCODE = "cPostCode";
    /**移动电话*/
    public static final String CMOBILE = "cMobile";
    /**固定电话*/
    public static final String CPHONE = "cPhone";
    /**传真*/
    public static final String CFAX = "cFax";
    /**电子邮件*/
    public static final String CEMAIL = "cEmail";
    /**是否生效：0. 否 1. 是*/
    public static final String ISENABLED = "isEnabled";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
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
	 * 供应商ID
	 */
	public M setIVendorId(java.lang.Long iVendorId) {
		set("iVendorId", iVendorId);
		return (M)this;
	}

	/**
	 * 供应商ID
	 */
	@JBoltField(name="ivendorid" ,columnName="iVendorId",type="Long", remark="供应商ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "ivendorid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIVendorId() {
		return getLong("iVendorId");
	}

	/**
	 * 地点编码
	 */
	public M setCDistrictCode(java.lang.String cDistrictCode) {
		set("cDistrictCode", cDistrictCode);
		return (M)this;
	}

	/**
	 * 地点编码
	 */
	@JBoltField(name="cdistrictcode" ,columnName="cDistrictCode",type="String", remark="地点编码", required=true, maxLength=30, fixed=0, order=3)
	@JSONField(name = "cdistrictcode")
	public java.lang.String getCDistrictCode() {
		return getStr("cDistrictCode");
	}

	/**
	 * 地点名称
	 */
	public M setCDistrictName(java.lang.String cDistrictName) {
		set("cDistrictName", cDistrictName);
		return (M)this;
	}

	/**
	 * 地点名称
	 */
	@JBoltField(name="cdistrictname" ,columnName="cDistrictName",type="String", remark="地点名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "cdistrictname")
	public java.lang.String getCDistrictName() {
		return getStr("cDistrictName");
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
	@JBoltField(name="ccontactcode" ,columnName="cContactCode",type="String", remark="联系人编码", required=false, maxLength=50, fixed=0, order=5)
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
	@JBoltField(name="ccontactname" ,columnName="cContactName",type="String", remark="联系人名称", required=false, maxLength=50, fixed=0, order=6)
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
	@JBoltField(name="ccountry" ,columnName="cCountry",type="String", remark="国家/地区", required=false, maxLength=50, fixed=0, order=7)
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
	@JBoltField(name="cprovince" ,columnName="cProvince",type="String", remark="省/自治区", required=false, maxLength=50, fixed=0, order=8)
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
	@JBoltField(name="ccity" ,columnName="cCity",type="String", remark="城市", required=false, maxLength=50, fixed=0, order=9)
	@JSONField(name = "ccity")
	public java.lang.String getCCity() {
		return getStr("cCity");
	}

	/**
	 * 区县
	 */
	public M setCCounty(java.lang.String cCounty) {
		set("cCounty", cCounty);
		return (M)this;
	}

	/**
	 * 区县
	 */
	@JBoltField(name="ccounty" ,columnName="cCounty",type="String", remark="区县", required=false, maxLength=50, fixed=0, order=10)
	@JSONField(name = "ccounty")
	public java.lang.String getCCounty() {
		return getStr("cCounty");
	}

	/**
	 * 地址
	 */
	public M setCAddr(java.lang.String cAddr) {
		set("cAddr", cAddr);
		return (M)this;
	}

	/**
	 * 地址
	 */
	@JBoltField(name="caddr" ,columnName="cAddr",type="String", remark="地址", required=false, maxLength=50, fixed=0, order=11)
	@JSONField(name = "caddr")
	public java.lang.String getCAddr() {
		return getStr("cAddr");
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
	@JBoltField(name="cpostcode" ,columnName="cPostCode",type="String", remark="邮政编码", required=false, maxLength=20, fixed=0, order=12)
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
	@JBoltField(name="cmobile" ,columnName="cMobile",type="String", remark="移动电话", required=false, maxLength=20, fixed=0, order=13)
	@JSONField(name = "cmobile")
	public java.lang.String getCMobile() {
		return getStr("cMobile");
	}

	/**
	 * 固定电话
	 */
	public M setCPhone(java.lang.String cPhone) {
		set("cPhone", cPhone);
		return (M)this;
	}

	/**
	 * 固定电话
	 */
	@JBoltField(name="cphone" ,columnName="cPhone",type="String", remark="固定电话", required=false, maxLength=20, fixed=0, order=14)
	@JSONField(name = "cphone")
	public java.lang.String getCPhone() {
		return getStr("cPhone");
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
	@JBoltField(name="cfax" ,columnName="cFax",type="String", remark="传真", required=false, maxLength=20, fixed=0, order=15)
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
	@JBoltField(name="cemail" ,columnName="cEmail",type="String", remark="电子邮件", required=false, maxLength=20, fixed=0, order=16)
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
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否生效：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=17)
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=18)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}

