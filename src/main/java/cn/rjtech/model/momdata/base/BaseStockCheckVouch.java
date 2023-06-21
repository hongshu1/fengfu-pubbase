package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 盘点单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseStockCheckVouch<M extends BaseStockCheckVouch<M>> extends JBoltBaseModel<M>{
    /**组织编码*/
    public static final String ORGANIZECODE = "OrganizeCode";
    /**盘点方式;0 明盘 1 暗盘*/
    public static final String CHECKTYPE = "CheckType";
    /**单号*/
    public static final String BILLNO = "BillNo";
    /**单据日期*/
    public static final String BILLDATE = "BillDate";
    /**盘点人*/
    public static final String CHECKPERSON = "CheckPerson";
    /**仓库编码*/
    public static final String WHCODE = "WhCode";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建时间*/
    public static final String CREATEDATE = "CreateDate";
    /**更新人*/
    public static final String MODIFYPERSON = "ModifyPerson";
    /**更新时间*/
    public static final String MODIFYDATE = "ModifyDate";
    /**主键id*/
    public static final String AUTOID = "AutoId";
    /**库位编码,多个用,分隔*/
    public static final String POSCODES = "poscodes";
	/**单据状态;0盘点中,1待审核,2已完成*/
	public static final String STATUS = "status";
	/**
	 * 组织编码
	 */
	public M setOrganizeCode(String OrganizeCode) {
		set("OrganizeCode", OrganizeCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="OrganizeCode" ,columnName="OrganizeCode",type="String", remark="组织编码", required=true, maxLength=30, fixed=0, order=1)
	public String getOrganizeCode() {
		return getStr("OrganizeCode");
	}

	/**
	 * 盘点方式;0 明盘 1 暗盘
	 */
	public M setCheckType(String CheckType) {
		set("CheckType", CheckType);
		return (M)this;
	}


	/**
	 *单据状态;0盘点中,1待审核,2已完成
	 */
	public M setStatus(String Status) {
		set("Status", Status);
		return (M)this;
	}

	/**
	 * 盘点方式;0 明盘 1 暗盘
	 */
	@JBoltField(name="CheckType" ,columnName="CheckType",type="String", remark="盘点方式;0 明盘 1 暗盘", required=true, maxLength=30, fixed=0, order=2)
	public String getCheckType() {
		return getStr("CheckType");
	}

	/**
	 * 盘点方式;0 明盘 1 暗盘
	 */
	@JBoltField(name="Status" ,columnName="Status",type="String", remark="单据状态;0盘点中,1待审核,2已完成", required=true, maxLength=30, fixed=0, order=2)
	public String getStatus() {
		return getStr("Status");
	}



	/**
	 * 单号
	 */
	public M setBillNo(String BillNo) {
		set("BillNo", BillNo);
		return (M)this;
	}

	/**
	 * 单号
	 */
	@JBoltField(name="BillNo" ,columnName="BillNo",type="String", remark="单号", required=true, maxLength=30, fixed=0, order=3)
	public String getBillNo() {
		return getStr("BillNo");
	}

	/**
	 * 单据日期
	 */
	public M setBillDate(String BillDate) {
		set("BillDate", BillDate);
		return (M)this;
	}

	/**
	 * 单据日期
	 */
	@JBoltField(name="BillDate" ,columnName="BillDate",type="String", remark="单据日期", required=true, maxLength=30, fixed=0, order=4)
	public String getBillDate() {
		return getStr("BillDate");
	}

	/**
	 * 盘点人
	 */
	public M setCheckPerson(String CheckPerson) {
		set("CheckPerson", CheckPerson);
		return (M)this;
	}

	/**
	 * 盘点人
	 */
	@JBoltField(name="CheckPerson" ,columnName="CheckPerson",type="String", remark="盘点人", required=true, maxLength=30, fixed=0, order=5)
	public String getCheckPerson() {
		return getStr("CheckPerson");
	}

	/**
	 * 仓库编码
	 */
	public M setWhCode(String WhCode) {
		set("WhCode", WhCode);
		return (M)this;
	}

	/**
	 * 仓库编码
	 */
	@JBoltField(name="WhCode" ,columnName="WhCode",type="String", remark="仓库编码", required=true, maxLength=30, fixed=0, order=6)
	public String getWhCode() {
		return getStr("WhCode");
	}

	/**
	 * 创建人
	 */
	public M setCreatePerson(String CreatePerson) {
		set("CreatePerson", CreatePerson);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="CreatePerson" ,columnName="CreatePerson",type="String", remark="创建人", required=true, maxLength=30, fixed=0, order=7)
	public String getCreatePerson() {
		return getStr("CreatePerson");
	}

	/**
	 * 创建时间
	 */
	public M setCreateDate(java.util.Date CreateDate) {
		set("CreateDate", CreateDate);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="CreateDate" ,columnName="CreateDate",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=8)
	public java.util.Date getCreateDate() {
		return getDate("CreateDate");
	}

	/**
	 * 更新人
	 */
	public M setModifyPerson(String ModifyPerson) {
		set("ModifyPerson", ModifyPerson);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="ModifyPerson" ,columnName="ModifyPerson",type="String", remark="更新人", required=false, maxLength=30, fixed=0, order=9)
	public String getModifyPerson() {
		return getStr("ModifyPerson");
	}

	/**
	 * 更新时间
	 */
	public M setModifyDate(java.util.Date ModifyDate) {
		set("ModifyDate", ModifyDate);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="ModifyDate" ,columnName="ModifyDate",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=10)
	public java.util.Date getModifyDate() {
		return getDate("ModifyDate");
	}

	/**
	 * 主键id
	 */
	public M setAutoId(Long AutoId) {
		set("AutoId", AutoId);
		return (M)this;
	}

	/**
	 * 主键id
	 */
	@JBoltField(name="AutoId" ,columnName="AutoId",type="Long", remark="主键id", required=true, maxLength=19, fixed=0, order=11)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getAutoId() {
		return getLong("AutoId");
	}

	/**
	 * 库位编码,多个用,分隔
	 */
	public M setPoscodes(String poscodes) {
		set("poscodes", poscodes);
		return (M)this;
	}

	/**
	 * 库位编码,多个用,分隔
	 */
	@JBoltField(name="poscodes" ,columnName="poscodes",type="String", remark="库位编码,多个用,分隔", required=false, maxLength=255, fixed=0, order=12)
	public String getPoscodes() {
		return getStr("poscodes");
	}

}

