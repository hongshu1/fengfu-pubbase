package cn.rjtech.common.model.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 *
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BasePrintmachine<M extends BasePrintmachine<M>> extends JBoltBaseModel<M>{
    /***/
    public static final String AUTOID = "AutoID";
    /**组织代码*/
    public static final String ORGANIZECODE = "OrganizeCode";
    /**打印机机器代码*/
    public static final String PRINTERCODE = "PrinterCode";
    /**打印机地址*/
    public static final String PRINTERADDRESS = "PrinterAddress";
    /**备注*/
    public static final String MEMO = "Memo";
    /***/
    public static final String CREATEPERSON = "CreatePerson";
    /***/
    public static final String CREATEDATE = "CreateDate";
    /***/
    public static final String MODIFYPERSON = "ModifyPerson";
    /***/
    public static final String MODIFYDATE = "ModifyDate";
	public M setAutoid(Long autoid) {
		set("AutoID", autoid);
		return (M)this;
	}

	@JBoltField(name="autoid" ,columnName="AutoID",type="Long", remark="AUTOID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "autoid", serializeUsing = ToStringSerializer.class)
	public Long getAutoid() {
		return getLong("AutoID");
	}

	/**
	 * 组织代码
	 */
	public M setOrganizecode(String organizecode) {
		set("OrganizeCode", organizecode);
		return (M)this;
	}

	/**
	 * 组织代码
	 */
	@JBoltField(name="organizecode" ,columnName="OrganizeCode",type="String", remark="组织代码", required=false, maxLength=32, fixed=0, order=2)
	@JSONField(name = "organizecode")
	public String getOrganizecode() {
		return getStr("OrganizeCode");
	}

	/**
	 * 打印机机器代码
	 */
	public M setPrintercode(String printercode) {
		set("PrinterCode", printercode);
		return (M)this;
	}

	/**
	 * 打印机机器代码
	 */
	@JBoltField(name="printercode" ,columnName="PrinterCode",type="String", remark="打印机机器代码", required=false, maxLength=32, fixed=0, order=3)
	@JSONField(name = "printercode")
	public String getPrintercode() {
		return getStr("PrinterCode");
	}

	/**
	 * 打印机地址
	 */
	public M setPrinteraddress(String printeraddress) {
		set("PrinterAddress", printeraddress);
		return (M)this;
	}

	/**
	 * 打印机地址
	 */
	@JBoltField(name="printeraddress" ,columnName="PrinterAddress",type="String", remark="打印机地址", required=false, maxLength=32, fixed=0, order=4)
	@JSONField(name = "printeraddress")
	public String getPrinteraddress() {
		return getStr("PrinterAddress");
	}

	/**
	 * 备注
	 */
	public M setMemo(String memo) {
		set("Memo", memo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="备注", required=false, maxLength=32, fixed=0, order=5)
	@JSONField(name = "memo")
	public String getMemo() {
		return getStr("Memo");
	}

	public M setCreateperson(String createperson) {
		set("CreatePerson", createperson);
		return (M)this;
	}

	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="CREATEPERSON", required=false, maxLength=32, fixed=0, order=6)
	@JSONField(name = "createperson")
	public String getCreateperson() {
		return getStr("CreatePerson");
	}

	public M setCreatedate(java.util.Date createdate) {
		set("CreateDate", createdate);
		return (M)this;
	}

	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="CREATEDATE", required=false, maxLength=23, fixed=3, order=7)
	@JSONField(name = "createdate")
	public java.util.Date getCreatedate() {
		return getDate("CreateDate");
	}

	public M setModifyperson(String modifyperson) {
		set("ModifyPerson", modifyperson);
		return (M)this;
	}

	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="MODIFYPERSON", required=false, maxLength=32, fixed=0, order=8)
	@JSONField(name = "modifyperson")
	public String getModifyperson() {
		return getStr("ModifyPerson");
	}

	public M setModifydate(java.util.Date modifydate) {
		set("ModifyDate", modifydate);
		return (M)this;
	}

	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="MODIFYDATE", required=false, maxLength=23, fixed=3, order=9)
	@JSONField(name = "modifydate")
	public java.util.Date getModifydate() {
		return getDate("ModifyDate");
	}

}

