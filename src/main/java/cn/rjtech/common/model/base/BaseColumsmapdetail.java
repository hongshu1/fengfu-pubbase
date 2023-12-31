package cn.rjtech.common.model.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 字段映射表明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseColumsmapdetail<M extends BaseColumsmapdetail<M>> extends JBoltBaseModel<M>{
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**主项目ID*/
    public static final String MASID = "MasID";
    /***/
    public static final String LEVEL = "Level";
    /***/
    public static final String FLAGTYPE = "FlagType";
    /***/
    public static final String PFLAG = "PFlag";
    /**如为数字则1代表表头  2代表明细  10代表表头第一扩展表  11代表表头第二扩展表 20代表子表第一扩展表  21代表子表第二扩展表
如为字符则代表相关结构实体，类似ERP结构需构建实体或表名之类的，如实体表名字类似*/
    public static final String FLAG = "Flag";
    /**目标字段*/
    public static final String TAGCOLUMN = "TagColumn";
    /**来源字段*/
    public static final String SOURCECOLUMN = "SourceColumn";
    /**来源所对应的标准化标识，如maplist对应的map名称*/
    public static final String SOURCEFLAG = "SourceFlag";
    /***/
    public static final String DEFAULTVALUE = "DefaultValue";
    /***/
    public static final String MEMO = "Memo";
    /**关闭人员*/
    public static final String CLOSEPERSON = "ClosePerson";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建时间*/
    public static final String CREATEDATE = "CreateDate";
    /**更新人*/
    public static final String MODIFYPERSON = "ModifyPerson";
    /**更新时间*/
    public static final String MODIFYDATE = "ModifyDate";
	/**
	 * AutoID
	 */
	public M setAutoid(Long autoid) {
		set("AutoID", autoid);
		return (M)this;
	}

	/**
	 * AutoID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="Long", remark="AutoID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "autoid", serializeUsing = ToStringSerializer.class)
	public Long getAutoid() {
		return getLong("AutoID");
	}

	/**
	 * 主项目ID
	 */
	public M setMasid(String masid) {
		set("MasID", masid);
		return (M)this;
	}

	/**
	 * 主项目ID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="主项目ID", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "masid")
	public String getMasid() {
		return getStr("MasID");
	}

	public M setLevel(Integer level) {
		set("Level", level);
		return (M)this;
	}

	@JBoltField(name="level" ,columnName="Level",type="Integer", remark="LEVEL", required=false, maxLength=10, fixed=0, order=3)
	@JSONField(name = "level")
	public Integer getLevel() {
		return getInt("Level");
	}

	public M setFlagtype(String flagtype) {
		set("FlagType", flagtype);
		return (M)this;
	}

	@JBoltField(name="flagtype" ,columnName="FlagType",type="String", remark="FLAGTYPE", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "flagtype")
	public String getFlagtype() {
		return getStr("FlagType");
	}

	public M setPflag(String pflag) {
		set("PFlag", pflag);
		return (M)this;
	}

	@JBoltField(name="pflag" ,columnName="PFlag",type="String", remark="PFLAG", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "pflag")
	public String getPflag() {
		return getStr("PFlag");
	}

	/**
	 * 如为数字则1代表表头  2代表明细  10代表表头第一扩展表  11代表表头第二扩展表 20代表子表第一扩展表  21代表子表第二扩展表
如为字符则代表相关结构实体，类似ERP结构需构建实体或表名之类的，如实体表名字类似
	 */
	public M setFlag(String flag) {
		set("Flag", flag);
		return (M)this;
	}

	/**
	 * 如为数字则1代表表头  2代表明细  10代表表头第一扩展表  11代表表头第二扩展表 20代表子表第一扩展表  21代表子表第二扩展表  如为字符则代表相关结构实体，类似ERP结构需构建实体或表名之类的，如实体表名字类似
	 */
	@JBoltField(name="flag" ,columnName="Flag",type="String", remark="如为数字则1代表表头  2代表明细  10代表表头第一扩展表  11代表表头第二扩展表 20代表子表第一扩展表  21代表子表第二扩展表  如为字符则代表相关结构实体，类似ERP结构需构建实体或表名之类的，如实体表名字类似", required=false, maxLength=30, fixed=0, order=6)
	@JSONField(name = "flag")
	public String getFlag() {
		return getStr("Flag");
	}

	/**
	 * 目标字段
	 */
	public M setTagcolumn(String tagcolumn) {
		set("TagColumn", tagcolumn);
		return (M)this;
	}

	/**
	 * 目标字段
	 */
	@JBoltField(name="tagcolumn" ,columnName="TagColumn",type="String", remark="目标字段", required=false, maxLength=50, fixed=0, order=7)
	@JSONField(name = "tagcolumn")
	public String getTagcolumn() {
		return getStr("TagColumn");
	}

	/**
	 * 来源字段
	 */
	public M setSourcecolumn(String sourcecolumn) {
		set("SourceColumn", sourcecolumn);
		return (M)this;
	}

	/**
	 * 来源字段
	 */
	@JBoltField(name="sourcecolumn" ,columnName="SourceColumn",type="String", remark="来源字段", required=false, maxLength=50, fixed=0, order=8)
	@JSONField(name = "sourcecolumn")
	public String getSourcecolumn() {
		return getStr("SourceColumn");
	}

	/**
	 * 来源所对应的标准化标识，如maplist对应的map名称
	 */
	public M setSourceflag(String sourceflag) {
		set("SourceFlag", sourceflag);
		return (M)this;
	}

	/**
	 * 来源所对应的标准化标识，如maplist对应的map名称
	 */
	@JBoltField(name="sourceflag" ,columnName="SourceFlag",type="String", remark="来源所对应的标准化标识，如maplist对应的map名称", required=false, maxLength=50, fixed=0, order=9)
	@JSONField(name = "sourceflag")
	public String getSourceflag() {
		return getStr("SourceFlag");
	}

	public M setDefaultvalue(String defaultvalue) {
		set("DefaultValue", defaultvalue);
		return (M)this;
	}

	@JBoltField(name="defaultvalue" ,columnName="DefaultValue",type="String", remark="DEFAULTVALUE", required=false, maxLength=50, fixed=0, order=10)
	@JSONField(name = "defaultvalue")
	public String getDefaultvalue() {
		return getStr("DefaultValue");
	}

	public M setMemo(String memo) {
		set("Memo", memo);
		return (M)this;
	}

	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="MEMO", required=false, maxLength=50, fixed=0, order=11)
	@JSONField(name = "memo")
	public String getMemo() {
		return getStr("Memo");
	}

	/**
	 * 关闭人员
	 */
	public M setCloseperson(String closeperson) {
		set("ClosePerson", closeperson);
		return (M)this;
	}

	/**
	 * 关闭人员
	 */
	@JBoltField(name="closeperson" ,columnName="ClosePerson",type="String", remark="关闭人员", required=false, maxLength=30, fixed=0, order=12)
	@JSONField(name = "closeperson")
	public String getCloseperson() {
		return getStr("ClosePerson");
	}

	/**
	 * 创建人
	 */
	public M setCreateperson(String createperson) {
		set("CreatePerson", createperson);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=13)
	@JSONField(name = "createperson")
	public String getCreateperson() {
		return getStr("CreatePerson");
	}

	/**
	 * 创建时间
	 */
	public M setCreatedate(java.util.Date createdate) {
		set("CreateDate", createdate);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=14)
	@JSONField(name = "createdate")
	public java.util.Date getCreatedate() {
		return getDate("CreateDate");
	}

	/**
	 * 更新人
	 */
	public M setModifyperson(String modifyperson) {
		set("ModifyPerson", modifyperson);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="更新人", required=false, maxLength=30, fixed=0, order=15)
	@JSONField(name = "modifyperson")
	public String getModifyperson() {
		return getStr("ModifyPerson");
	}

	/**
	 * 更新时间
	 */
	public M setModifydate(java.util.Date modifydate) {
		set("ModifyDate", modifydate);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=16)
	@JSONField(name = "modifydate")
	public java.util.Date getModifydate() {
		return getDate("ModifyDate");
	}

}

