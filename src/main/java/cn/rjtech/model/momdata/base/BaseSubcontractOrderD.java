package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 采购/委外订单-采购订单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSubcontractOrderD<M extends BaseSubcontractOrderD<M>> extends JBoltBaseModel<M>{
    
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**委外订单主表ID*/
    public static final String ISUBCONTRACTORDERMID = "iSubcontractOrderMid";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**是否赠品： 0. 否 1. 是*/
    public static final String ISPRESENT = "isPresent";
    /**供应商地址ID*/
    public static final String IVENDORADDRID = "iVendorAddrId";
    /**到货地址*/
    public static final String CADDRESS = "cAddress";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
    /**包装数量*/
    public static final String IPKGQTY = "iPkgQty";
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
	 * 委外订单主表ID
	 */
	public M setISubcontractOrderMid(java.lang.Long iSubcontractOrderMid) {
		set("iSubcontractOrderMid", iSubcontractOrderMid);
		return (M)this;
	}

	/**
	 * 委外订单主表ID
	 */
	@JBoltField(name="isubcontractordermid" ,columnName="iSubcontractOrderMid",type="Long", remark="委外订单主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "isubcontractordermid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISubcontractOrderMid() {
		return getLong("iSubcontractOrderMid");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 是否赠品： 0. 否 1. 是
	 */
	public M setIsPresent(java.lang.Boolean isPresent) {
		set("isPresent", isPresent);
		return (M)this;
	}

	/**
	 * 是否赠品： 0. 否 1. 是
	 */
	@JBoltField(name="ispresent" ,columnName="isPresent",type="Boolean", remark="是否赠品： 0. 否 1. 是", required=true, maxLength=1, fixed=0, order=4)
	@JSONField(name = "ispresent")
	public java.lang.Boolean getIsPresent() {
		return getBoolean("isPresent");
	}

	/**
	 * 供应商地址ID
	 */
	public M setIVendorAddrId(java.lang.Long iVendorAddrId) {
		set("iVendorAddrId", iVendorAddrId);
		return (M)this;
	}

	/**
	 * 供应商地址ID
	 */
	@JBoltField(name="ivendoraddrid" ,columnName="iVendorAddrId",type="Long", remark="供应商地址ID", required=false, maxLength=19, fixed=0, order=5)
	@JSONField(name = "ivendoraddrid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIVendorAddrId() {
		return getLong("iVendorAddrId");
	}

	/**
	 * 到货地址
	 */
	public M setCAddress(java.lang.String cAddress) {
		set("cAddress", cAddress);
		return (M)this;
	}

	/**
	 * 到货地址
	 */
	@JBoltField(name="caddress" ,columnName="cAddress",type="String", remark="到货地址", required=true, maxLength=50, fixed=0, order=6)
	@JSONField(name = "caddress")
	public java.lang.String getCAddress() {
		return getStr("cAddress");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=7)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=8)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 包装数量
	 */
	public M setIPkgQty(java.lang.Integer iPkgQty) {
		set("iPkgQty", iPkgQty);
		return (M)this;
	}

	/**
	 * 包装数量
	 */
	@JBoltField(name="ipkgqty" ,columnName="iPkgQty",type="Integer", remark="包装数量", required=false, maxLength=10, fixed=0, order=9)
	@JSONField(name = "ipkgqty")
	public java.lang.Integer getIPkgQty() {
		return getInt("iPkgQty");
	}

}

