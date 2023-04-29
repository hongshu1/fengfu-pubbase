package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 17:48
 * @Description 点击左侧导航栏-出库检，显示主页面数据
 */
public class RcvDocQcFormMPage implements Serializable {

    private Long    iautoid;
    private Long    ivendorid;
    private Long    iupdateby;
    private Long    ircvdocid;
    private Long    iqcuserid;
    private Long    iqcformid;
    private Long    iorgid;
    private Long    icreateby;
    private Long    iinventoryid;
    private Long    iinventoryuomid1;
    private Integer istatus;            //用istatus关键字在数据字典查询，可以看到分类
    private Integer iqty;
    private Integer imask;
    private Date    dcreatetime;
    private Date    dqctime;
    private Date    drcvdate;
    private Date    dupdatetime;
    private String  cbatchno;
    private String  ccreatename;
    private String  cdcno;
    private String  cequipmentname;
    private String  cinvaddcode;
    private String  cinvcode1;
    private String  cinvname1;
    private String  cinvstd;
    private String  cmeasurepurpose;     //用cmeasurepurpose关键字在数据字典查询
    private String  cmeasurereason;      //用cmeasureunit关键字在数据字典查询
    private String  cmemo;
    private String  corgcode;
    private String  corgname;
    private String  cqcformname;
    private String  crcvdocno;
    private String  crcvdocqcformno;
    private String  cupdatename;
    private String  cvenname;
    private String  iscompleted;
    private String  iscpksigned;         //用iscpksigned关键字在数据字典查询
    private String  isdeleted;
    private String  isok;                //用isok关键字在数据字典查询

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIvendorid() {
        return ivendorid;
    }

    public void setIvendorid(Long ivendorid) {
        this.ivendorid = ivendorid;
    }

    public Long getIupdateby() {
        return iupdateby;
    }

    public void setIupdateby(Long iupdateby) {
        this.iupdateby = iupdateby;
    }

    public Long getIrcvdocid() {
        return ircvdocid;
    }

    public void setIrcvdocid(Long ircvdocid) {
        this.ircvdocid = ircvdocid;
    }

    public Long getIqcuserid() {
        return iqcuserid;
    }

    public void setIqcuserid(Long iqcuserid) {
        this.iqcuserid = iqcuserid;
    }

    public Long getIqcformid() {
        return iqcformid;
    }

    public void setIqcformid(Long iqcformid) {
        this.iqcformid = iqcformid;
    }

    public Long getIorgid() {
        return iorgid;
    }

    public void setIorgid(Long iorgid) {
        this.iorgid = iorgid;
    }

    public Long getIcreateby() {
        return icreateby;
    }

    public void setIcreateby(Long icreateby) {
        this.icreateby = icreateby;
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

    public Integer getIstatus() {
        return istatus;
    }

    public void setIstatus(Integer istatus) {
        this.istatus = istatus;
    }

    public Integer getIqty() {
        return iqty;
    }

    public void setIqty(Integer iqty) {
        this.iqty = iqty;
    }

    public Integer getImask() {
        return imask;
    }

    public void setImask(Integer imask) {
        this.imask = imask;
    }

    public Date getDcreatetime() {
        return dcreatetime;
    }

    public void setDcreatetime(Date dcreatetime) {
        this.dcreatetime = dcreatetime;
    }

    public Date getDqctime() {
        return dqctime;
    }

    public void setDqctime(Date dqctime) {
        this.dqctime = dqctime;
    }

    public Date getDrcvdate() {
        return drcvdate;
    }

    public void setDrcvdate(Date drcvdate) {
        this.drcvdate = drcvdate;
    }

    public Date getDupdatetime() {
        return dupdatetime;
    }

    public void setDupdatetime(Date dupdatetime) {
        this.dupdatetime = dupdatetime;
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

    public String getCrcvdocno() {
        return crcvdocno;
    }

    public void setCrcvdocno(String crcvdocno) {
        this.crcvdocno = crcvdocno;
    }

    public String getCrcvdocqcformno() {
        return crcvdocqcformno;
    }

    public void setCrcvdocqcformno(String crcvdocqcformno) {
        this.crcvdocqcformno = crcvdocqcformno;
    }

    public String getCupdatename() {
        return cupdatename;
    }

    public void setCupdatename(String cupdatename) {
        this.cupdatename = cupdatename;
    }

    public String getCvenname() {
        return cvenname;
    }

    public void setCvenname(String cvenname) {
        this.cvenname = cvenname;
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
}
