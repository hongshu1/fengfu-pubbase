package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 存货物料表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInvPart<M extends BaseInvPart<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**物料类型;1. 存货 2. 虚拟件*/
    public static final String ITYPE = "iType";
    /**排序值*/
    public static final String ISEQ = "iSeq";
    /**物料编码*/
    public static final String CPARTCODE = "cPartCode";
    /**物料名称*/
    public static final String CPARTNAME = "cPartName";
    /**存货ID;物料类型为1时，有该值*/
    public static final String IINVENTORYID = "iInventoryId";
    /**上级物料ID*/
    public static final String IPID = "iPid";
    /**工艺配置ID*/
    public static final String IINVENTORYROUTINGCONFIGID = "iInventoryRoutingConfigId";
    /**母件存货ID*/
    public static final String IPARENTINVID = "iParentInvId";
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
	 * 物料类型;1. 存货 2. 虚拟件
	 */
	public M setIType(java.lang.Integer iType) {
		set("iType", iType);
		return (M)this;
	}

	/**
	 * 物料类型;1. 存货 2. 虚拟件
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="物料类型;1. 存货 2. 虚拟件", required=true, maxLength=10, fixed=0, order=3)
	@JSONField(name = "itype")
	public java.lang.Integer getIType() {
		return getInt("iType");
	}

	/**
	 * 排序值
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 排序值
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="排序值", required=false, maxLength=10, fixed=0, order=4)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 物料编码
	 */
	public M setCPartCode(java.lang.String cPartCode) {
		set("cPartCode", cPartCode);
		return (M)this;
	}

	/**
	 * 物料编码
	 */
	@JBoltField(name="cpartcode" ,columnName="cPartCode",type="String", remark="物料编码", required=false, maxLength=60, fixed=0, order=5)
	@JSONField(name = "cpartcode")
	public java.lang.String getCPartCode() {
		return getStr("cPartCode");
	}

	/**
	 * 物料名称
	 */
	public M setCPartName(java.lang.String cPartName) {
		set("cPartName", cPartName);
		return (M)this;
	}

	/**
	 * 物料名称
	 */
	@JBoltField(name="cpartname" ,columnName="cPartName",type="String", remark="物料名称", required=false, maxLength=200, fixed=0, order=6)
	@JSONField(name = "cpartname")
	public java.lang.String getCPartName() {
		return getStr("cPartName");
	}

	/**
	 * 存货ID;物料类型为1时，有该值
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 存货ID;物料类型为1时，有该值
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID;物料类型为1时，有该值", required=false, maxLength=19, fixed=0, order=7)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 上级物料ID
	 */
	public M setIPid(java.lang.Long iPid) {
		set("iPid", iPid);
		return (M)this;
	}

	/**
	 * 上级物料ID
	 */
	@JBoltField(name="ipid" ,columnName="iPid",type="Long", remark="上级物料ID", required=false, maxLength=19, fixed=0, order=8)
	@JSONField(name = "ipid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIPid() {
		return getLong("iPid");
	}

	/**
	 * 工艺配置ID
	 */
	public M setIInventoryRoutingConfigId(java.lang.Long iInventoryRoutingConfigId) {
		set("iInventoryRoutingConfigId", iInventoryRoutingConfigId);
		return (M)this;
	}

	/**
	 * 工艺配置ID
	 */
	@JBoltField(name="iinventoryroutingconfigid" ,columnName="iInventoryRoutingConfigId",type="Long", remark="工艺配置ID", required=true, maxLength=19, fixed=0, order=9)
	@JSONField(name = "iinventoryroutingconfigid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryRoutingConfigId() {
		return getLong("iInventoryRoutingConfigId");
	}

	/**
	 * 母件存货ID
	 */
	public M setIParentInvId(java.lang.Long iParentInvId) {
		set("iParentInvId", iParentInvId);
		return (M)this;
	}

	/**
	 * 母件存货ID
	 */
	@JBoltField(name="iparentinvid" ,columnName="iParentInvId",type="Long", remark="母件存货ID", required=false, maxLength=19, fixed=0, order=10)
	@JSONField(name = "iparentinvid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIParentInvId() {
		return getLong("iParentInvId");
	}

}

