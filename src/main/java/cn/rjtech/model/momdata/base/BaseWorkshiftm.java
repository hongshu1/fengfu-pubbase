package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 生产班次 主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseWorkshiftm<M extends BaseWorkshiftm<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**班次编码*/
    public static final String CWORKSHIFTCODE = "cWorkShiftCode";
    /**班次名称*/
    public static final String CWORKSHIFTNAME = "cWorkShiftName";
    /**出勤开始时间*/
    public static final String DSTARTTIME = "dStartTime";
    /**出勤结束时间*/
    public static final String DENDTIME = "dEndTime";
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
    /**是否删除 1未删除*/
    public static final String ISDELETED = "isDeleted";
    /**是否启用*/
    public static final String ISENABLED = "isEnabled";
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
	 * 班次编码
	 */
	public M setCworkshiftcode(String cworkshiftcode) {
		set("cWorkShiftCode", cworkshiftcode);
		return (M)this;
	}
	
	/**
	 * 班次编码
	 */
	@JBoltField(name="cworkshiftcode" ,columnName="cWorkShiftCode",type="String", remark="班次编码", required=true, maxLength=255, fixed=0, order=2)
	public String getCworkshiftcode() {
		return getStr("cWorkShiftCode");
	}

	/**
	 * 班次名称
	 */
	public M setCworkshiftname(String cworkshiftname) {
		set("cWorkShiftName", cworkshiftname);
		return (M)this;
	}
	
	/**
	 * 班次名称
	 */
	@JBoltField(name="cworkshiftname" ,columnName="cWorkShiftName",type="String", remark="班次名称", required=true, maxLength=255, fixed=0, order=3)
	public String getCworkshiftname() {
		return getStr("cWorkShiftName");
	}

	/**
	 * 出勤开始时间
	 */
	public M setDstarttime(String dstarttime) {
		set("dStartTime", dstarttime);
		return (M)this;
	}
	
	/**
	 * 出勤开始时间
	 */
	@JBoltField(name="dstarttime" ,columnName="dStartTime",type="String", remark="出勤开始时间", required=false, maxLength=255, fixed=0, order=4)
	public String getDstarttime() {
		return getStr("dStartTime");
	}

	/**
	 * 出勤结束时间
	 */
	public M setDendtime(String dendtime) {
		set("dEndTime", dendtime);
		return (M)this;
	}
	
	/**
	 * 出勤结束时间
	 */
	@JBoltField(name="dendtime" ,columnName="dEndTime",type="String", remark="出勤结束时间", required=false, maxLength=255, fixed=0, order=5)
	public String getDendtime() {
		return getStr("dEndTime");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=255, fixed=0, order=6)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=7)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=255, fixed=0, order=8)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=9)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=10)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=false, maxLength=255, fixed=0, order=11)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=12)
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
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=13)
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
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=255, fixed=0, order=14)
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
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=255, fixed=0, order=15)
	public String getCorgname() {
		return getStr("cOrgName");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除 1未删除", required=true, maxLength=1, fixed=0, order=16)
	public Boolean getIsdeleted() {
		return getBoolean("isDeleted");
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
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用", required=false, maxLength=1, fixed=0, order=17)
	public Boolean getIsenabled() {
		return getBoolean("isEnabled");
	}

}

