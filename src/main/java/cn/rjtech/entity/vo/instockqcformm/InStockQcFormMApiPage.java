package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.rjtech.entity.vo.base.ResultVo;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/5 9:26
 * @Description 在库检，显示主页面数据
 */
public class InStockQcFormMApiPage implements Serializable {

    private Long       iautoid;
    private Long       icustomerid;
    private Long       iinventoryid;
    private Long       iinventoryuomid1;
    private Long       iqcformid;
    private Long       iqcuserid;
    private Long       iorgid;
    private Long       iupdateby;
    private Long       icreateby;
    private String     cbatchno;
    private String     ccreatename;
    private String     cdcno;
    private String     cequipmentname;
    private String     cinvaddcode;
    private String     cinvcode1;
    private String     cinvname1;
    private String     cinvstd;
    private String     cmeasurepurpose;
    private String     cmeasurereason;
    private String     cmeasureunit;
    private String     cmemo;
    private String     corgcode;
    private String     corgname;
    private String     cqcformname;
    private String     cinvqcformno;
    private String     cupdatename;
    private Date       dcreatetime;
    private Date       dupdatetime;
    private Date       dqcdate;
    private BigDecimal iqty;
    private String     iscompleted; //是否已完成：0. 未完成 1. 已完成
    private String     iscpksigned; //是否CPK数值的输入内容按正负数显示：0. 否 1. 是
    private String     isdeleted;
    private String     isok;        //是否判定合格：0. 否 1. 是
    private String     istatus;     //检验结果：0. 未生成 1. 待检 2. 不合格 3. 合格

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIcustomerid() {
        return icustomerid;
    }

    public void setIcustomerid(Long icustomerid) {
        this.icustomerid = icustomerid;
    }

    public Long getIinventoryid() {
        return iinventoryid;
    }

    public void setIinventoryid(Long iinventoryid) {
        this.iinventoryid = iinventoryid;
    }

    public Long getIinventoryuomid1() {
        return iinventoryuomid1;
    }

    public void setIinventoryuomid1(Long iinventoryuomid1) {
        this.iinventoryuomid1 = iinventoryuomid1;
    }

    public Long getIqcformid() {
        return iqcformid;
    }

    public void setIqcformid(Long iqcformid) {
        this.iqcformid = iqcformid;
    }

    public Long getIqcuserid() {
        return iqcuserid;
    }

    public void setIqcuserid(Long iqcuserid) {
        this.iqcuserid = iqcuserid;
    }

    public Long getIorgid() {
        return iorgid;
    }

    public void setIorgid(Long iorgid) {
        this.iorgid = iorgid;
    }

    public Long getIupdateby() {
        return iupdateby;
    }

    public void setIupdateby(Long iupdateby) {
        this.iupdateby = iupdateby;
    }

    public Long getIcreateby() {
        return icreateby;
    }

    public void setIcreateby(Long icreateby) {
        this.icreateby = icreateby;
    }

    public String getCbatchno() {
        return cbatchno;
    }

    public void setCbatchno(String cbatchno) {
        this.cbatchno = cbatchno;
    }

    public String getCcreatename() {
        return ccreatename;
    }

    public void setCcreatename(String ccreatename) {
        this.ccreatename = ccreatename;
    }

    public String getCdcno() {
        return cdcno;
    }

    public void setCdcno(String cdcno) {
        this.cdcno = cdcno;
    }

    public String getCequipmentname() {
        return cequipmentname;
    }

    public void setCequipmentname(String cequipmentname) {
        this.cequipmentname = cequipmentname;
    }

    public String getCinvaddcode() {
        return cinvaddcode;
    }

    public void setCinvaddcode(String cinvaddcode) {
        this.cinvaddcode = cinvaddcode;
    }

    public String getCinvcode1() {
        return cinvcode1;
    }

    public void setCinvcode1(String cinvcode1) {
        this.cinvcode1 = cinvcode1;
    }

    public String getCinvname1() {
        return cinvname1;
    }

    public void setCinvname1(String cinvname1) {
        this.cinvname1 = cinvname1;
    }

    public String getCinvstd() {
        return cinvstd;
    }

    public void setCinvstd(String cinvstd) {
        this.cinvstd = cinvstd;
    }

    public String getCmeasurepurpose() {
        return cmeasurepurpose;
    }

    public void setCmeasurepurpose(String cmeasurepurpose) {
        this.cmeasurepurpose = cmeasurepurpose;
    }

    public String getCmeasurereason() {
        return cmeasurereason;
    }

    public void setCmeasurereason(String cmeasurereason) {
        this.cmeasurereason = cmeasurereason;
    }

    public String getCmeasureunit() {
        return cmeasureunit;
    }

    public void setCmeasureunit(String cmeasureunit) {
        this.cmeasureunit = cmeasureunit;
    }

    public String getCmemo() {
        return cmemo;
    }

    public void setCmemo(String cmemo) {
        this.cmemo = cmemo;
    }

    public String getCorgcode() {
        return corgcode;
    }

    public void setCorgcode(String corgcode) {
        this.corgcode = corgcode;
    }

    public String getCorgname() {
        return corgname;
    }

    public void setCorgname(String corgname) {
        this.corgname = corgname;
    }

    public String getCqcformname() {
        return cqcformname;
    }

    public void setCqcformname(String cqcformname) {
        this.cqcformname = cqcformname;
    }

    public String getCinvqcformno() {
        return cinvqcformno;
    }

    public void setCinvqcformno(String cinvqcformno) {
        this.cinvqcformno = cinvqcformno;
    }

    public String getCupdatename() {
        return cupdatename;
    }

    public void setCupdatename(String cupdatename) {
        this.cupdatename = cupdatename;
    }

    public Date getDcreatetime() {
        return dcreatetime;
    }

    public void setDcreatetime(Date dcreatetime) {
        this.dcreatetime = dcreatetime;
    }

    public Date getDupdatetime() {
        return dupdatetime;
    }

    public void setDupdatetime(Date dupdatetime) {
        this.dupdatetime = dupdatetime;
    }

    public Date getDqcdate() {
        return dqcdate;
    }

    public void setDqcdate(Date dqcdate) {
        this.dqcdate = dqcdate;
    }

    public BigDecimal getIqty() {
        return iqty;
    }

    public void setIqty(BigDecimal iqty) {
        this.iqty = iqty;
    }

    public String getIscompleted() {
        return iscompleted;
    }

    public void setIscompleted(String iscompleted) {
        this.iscompleted = iscompleted;
    }

    public String getIscpksigned() {
        return iscpksigned;
    }

    public void setIscpksigned(String iscpksigned) {
        this.iscpksigned = iscpksigned;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getIsok() {
        return isok;
    }

    public void setIsok(String isok) {
        this.isok = isok;
    }

    public String getIstatus() {
        return istatus;
    }

    public void setIstatus(String istatus) {
        this.istatus = istatus;
    }
}
