package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 工序档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseOperation<M extends BaseOperation<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**工序编码*/
    public static final String COPERATIONCODE = "cOperationCode";
    /**工序名称*/
    public static final String COPERATIONNAME = "cOperationName";
    /**工种档案ID*/
    public static final String IWORKCLASSID = "iWorkClassId";
    /**不良项目类型ID 多选逗号隔开*/
    public static final String CBADNESSCLASSIDS = "cBadnessClassIds";
    /**工位ID 多选逗号隔开*/
    public static final String CWORKPOSITIONIDS = "cWorkPositionIds";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**来源1.MES 2.U9;1.MES*/
    public static final String ISOURCE = "iSource";
    /**来源标识ID*/
    public static final String ISOURCEID = "iSourceId";
    /**是否启用*/
    public static final String ISENABLED = "isEnabled";
    /**是否删除 1未删除*/
    public static final String ISDELETED = "isDeleted";
	/**
	 * 主键
	 */
	public M setIautoid(Long iautoid) {
		set("iAutoId", iautoid);
		return (M)this;
	}
	
	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIautoid() {
		return getLong("iAutoId");
	}

	/**
	 * 工序编码
	 */
	public M setCoperationcode(String coperationcode) {
		set("cOperationCode", coperationcode);
		return (M)this;
	}
	
	/**
	 * 工序编码
	 */
	@JBoltField(name="coperationcode" ,columnName="cOperationCode",type="String", remark="工序编码", required=true, maxLength=255, fixed=0, order=2)
	public String getCoperationcode() {
		return getStr("cOperationCode");
	}

	/**
	 * 工序名称
	 */
	public M setCoperationname(String coperationname) {
		set("cOperationName", coperationname);
		return (M)this;
	}
	
	/**
	 * 工序名称
	 */
	@JBoltField(name="coperationname" ,columnName="cOperationName",type="String", remark="工序名称", required=true, maxLength=255, fixed=0, order=3)
	public String getCoperationname() {
		return getStr("cOperationName");
	}

	/**
	 * 工种档案ID
	 */
	public M setIworkclassid(Long iworkclassid) {
		set("iWorkClassId", iworkclassid);
		return (M)this;
	}
	
	/**
	 * 工种档案ID
	 */
	@JBoltField(name="iworkclassid" ,columnName="iWorkClassId",type="Long", remark="工种档案ID", required=false, maxLength=19, fixed=0, order=4)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIworkclassid() {
		return getLong("iWorkClassId");
	}

	/**
	 * 不良项目类型ID 多选逗号隔开
	 */
	public M setCbadnessclassids(String cbadnessclassids) {
		set("cBadnessClassIds", cbadnessclassids);
		return (M)this;
	}
	
	/**
	 * 不良项目类型ID 多选逗号隔开
	 */
	@JBoltField(name="cbadnessclassids" ,columnName="cBadnessClassIds",type="String", remark="不良项目类型ID 多选逗号隔开", required=false, maxLength=255, fixed=0, order=5)
	public String getCbadnessclassids() {
		return getStr("cBadnessClassIds");
	}

	/**
	 * 工位ID 多选逗号隔开
	 */
	public M setCworkpositionids(String cworkpositionids) {
		set("cWorkPositionIds", cworkpositionids);
		return (M)this;
	}
	
	/**
	 * 工位ID 多选逗号隔开
	 */
	@JBoltField(name="cworkpositionids" ,columnName="cWorkPositionIds",type="String", remark="工位ID 多选逗号隔开", required=false, maxLength=255, fixed=0, order=6)
	public String getCworkpositionids() {
		return getStr("cWorkPositionIds");
	}

	/**
	 * 备注
	 */
	public M setCmemo(String cmemo) {
		set("cMemo", cmemo);
		return (M)this;
	}
	
	/**
	 * 备注
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=255, fixed=0, order=7)
	public String getCmemo() {
		return getStr("cMemo");
	}

	/**
	 * 创建人
	 */
	public M setIcreateby(Long icreateby) {
		set("iCreateBy", icreateby);
		return (M)this;
	}
	
	/**
	 * 创建人
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=8)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIcreateby() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCcreatename(String ccreatename) {
		set("cCreateName", ccreatename);
		return (M)this;
	}
	
	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=255, fixed=0, order=9)
	public String getCcreatename() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDcreatetime(java.util.Date dcreatetime) {
		set("dCreateTime", dcreatetime);
		return (M)this;
	}
	
	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=10)
	public java.util.Date getDcreatetime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新人
	 */
	public M setIupdateby(Long iupdateby) {
		set("iUpdateBy", iupdateby);
		return (M)this;
	}
	
	/**
	 * 更新人
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=11)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIupdateby() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCupdatename(String cupdatename) {
		set("cUpdateName", cupdatename);
		return (M)this;
	}
	
	/**
	 * 更新人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=false, maxLength=255, fixed=0, order=12)
	public String getCupdatename() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDupdatetime(java.util.Date dupdatetime) {
		set("dUpdateTime", dupdatetime);
		return (M)this;
	}
	
	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=13)
	public java.util.Date getDupdatetime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 组织ID
	 */
	public M setIorgid(Long iorgid) {
		set("iOrgId", iorgid);
		return (M)this;
	}
	
	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=14)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIorgid() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCorgcode(String corgcode) {
		set("cOrgCode", corgcode);
		return (M)this;
	}
	
	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=255, fixed=0, order=15)
	public String getCorgcode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCorgname(String corgname) {
		set("cOrgName", corgname);
		return (M)this;
	}
	
	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=255, fixed=0, order=16)
	public String getCorgname() {
		return getStr("cOrgName");
	}

	/**
	 * 来源1.MES 2.U9;1.MES
	 */
	public M setIsource(Integer isource) {
		set("iSource", isource);
		return (M)this;
	}
	
	/**
	 * 来源1.MES 2.U9;1.MES
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源1.MES 2.U9;1.MES", required=false, maxLength=10, fixed=0, order=17)
	public Integer getIsource() {
		return getInt("iSource");
	}

	/**
	 * 来源标识ID
	 */
	public M setIsourceid(String isourceid) {
		set("iSourceId", isourceid);
		return (M)this;
	}
	
	/**
	 * 来源标识ID
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源标识ID", required=false, maxLength=255, fixed=0, order=18)
	public String getIsourceid() {
		return getStr("iSourceId");
	}

	/**
	 * 是否启用
	 */
	public M setIsenabled(Boolean isenabled) {
		set("isEnabled", isenabled);
		return (M)this;
	}
	
	/**
	 * 是否启用
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用", required=false, maxLength=1, fixed=0, order=19)
	public Boolean getIsenabled() {
		return getBoolean("isEnabled");
	}

	/**
	 * 是否删除 1未删除
	 */
	public M setIsdeleted(Boolean isdeleted) {
		set("isDeleted", isdeleted);
		return (M)this;
	}
	
	/**
	 * 是否删除 1未删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除 1未删除", required=true, maxLength=1, fixed=0, order=20)
	public Boolean getIsdeleted() {
		return getBoolean("isDeleted");
	}

}

