package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 生产班次 明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWorkshiftd<M extends BaseWorkshiftd<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**班次主表ID*/
    public static final String IWORKSHIFTMID = "iWorkShiftMId";
    /**类型 1工作时间 2休息时间*/
    public static final String ITYPE = "iType";
    /**开始时间*/
    public static final String DSTARTTIME = "dStartTime";
    /**结束时间*/
    public static final String DENDTIME = "dEndTime";
    /**时长(分)*/
    public static final String IMINUTE = "iMinute";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**出勤名称*/
    public static final String CNAME = "cName";
    /**次序*/
    public static final String ISEQ = "iSeq";
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
	 * 班次主表ID
	 */
	public M setIworkshiftmid(Long iworkshiftmid) {
		set("iWorkShiftMId", iworkshiftmid);
		return (M)this;
	}
	
	/**
	 * 班次主表ID
	 */
	@JBoltField(name="iworkshiftmid" ,columnName="iWorkShiftMId",type="Long", remark="班次主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIworkshiftmid() {
		return getLong("iWorkShiftMId");
	}

	/**
	 * 类型 1工作时间 2休息时间
	 */
	public M setItype(Integer itype) {
		set("iType", itype);
		return (M)this;
	}
	
	/**
	 * 类型 1工作时间 2休息时间
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="类型 1工作时间 2休息时间", required=true, maxLength=10, fixed=0, order=3)
	public Integer getItype() {
		return getInt("iType");
	}

	/**
	 * 开始时间
	 */
	public M setDstarttime(String dstarttime) {
		set("dStartTime", dstarttime);
		return (M)this;
	}
	
	/**
	 * 开始时间
	 */
	@JBoltField(name="dstarttime" ,columnName="dStartTime",type="String", remark="开始时间", required=false, maxLength=255, fixed=0, order=4)
	public String getDstarttime() {
		return getStr("dStartTime");
	}

	/**
	 * 结束时间
	 */
	public M setDendtime(String dendtime) {
		set("dEndTime", dendtime);
		return (M)this;
	}
	
	/**
	 * 结束时间
	 */
	@JBoltField(name="dendtime" ,columnName="dEndTime",type="String", remark="结束时间", required=false, maxLength=255, fixed=0, order=5)
	public String getDendtime() {
		return getStr("dEndTime");
	}

	/**
	 * 时长(分)
	 */
	public M setIminute(Integer iminute) {
		set("iMinute", iminute);
		return (M)this;
	}
	
	/**
	 * 时长(分)
	 */
	@JBoltField(name="iminute" ,columnName="iMinute",type="Integer", remark="时长(分)", required=false, maxLength=10, fixed=0, order=6)
	public Integer getIminute() {
		return getInt("iMinute");
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
	 * 出勤名称
	 */
	public M setCname(String cname) {
		set("cName", cname);
		return (M)this;
	}

	/**
	 * 出勤名称
	 */
	@JBoltField(name="cname" ,columnName="cName",type="String", remark="出勤名称", required=false, maxLength=50, fixed=0, order=8)
	public String getCname() {
		return getStr("cName");
	}

	/**
	 * 次序
	 */
	public M setIseq(Integer iseq) {
		set("iSeq", iseq);
		return (M)this;
	}
	
	/**
	 * 次序
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="次序", required=false, maxLength=10, fixed=0, order=9)
	public Integer getIseq() {
		return getInt("iSeq");
	}

}

