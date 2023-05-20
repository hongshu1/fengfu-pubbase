package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/5 9:42
 * @Description 跳转到检验页面
 */
public class InStockQcFormMApiJumpCheckOut implements Serializable {

    /**批次号*/
    private String  cbatchno;
    /*现品票（条码）*/
    private String  cbarcode;
    private String  ccreatename;
    /*设变号*/
    private String  cdcno;
    /*机型*/
    private String  cequipmentname;
    /*存货编码*/
    private String  cinvaddcode;
    /*客户部番*/
    private String  cinvcode1;
    /*部品名称*/
    private String  cinvname1;
    /*规格*/
    private String  cinvstd;
    /*测定目的*/
    private String  cmeasurepurpose;
    /*测定理由*/
    private String  cmeasurereason;
    /*单位*/
    private String  cmeasureunit;
    /*备注*/
    private String  cmemo;
    private String  corgcode;
    private String  corgname;
    /*检验单号*/
    private String  cinvqcformno;
    private String  cupdatename;
    /*是否合格*/
    private String  isok;
    private String  isdeleted;
    /*是否CPK数值的输入内容按正负数显示：0. 否 1. 是*/
    private String  iscpksigned;
    /*是否完成*/
    private String  iscompleted;
    /*主键*/
    private Long    iautoid;
    private Long    icreateby;
    private Long    icustomerid;
    /*存货id*/
    private Long    iinventoryid;
    private Long    iinventoryuomid1;
    private Long    iorgid;
    /*质检表格id*/
    private Long    iqcformid;
    /*检验员Id, 系统用户ID*/
    private Long    iqcuserid;
    private Long    iqty;
    private Long    iupdateby;
    /*检验结果：0. 未生成 1. 待检 2. 不合格 3. 合格*/
    private Integer istatus;
    private Date    dcreatetime;
    /*检验日期*/
    private Date    dqcdate;
    private Date    dupdatetime;
    private int     size; //数量

    public String getCbatchno() {
        return cbatchno;
    }

    public void setCbatchno(String cbatchno) {
        this.cbatchno = cbatchno;
    }

    public String getcBarcode() {
        return cbarcode;
    }

    public void setcBarcode(String cbarcode) {
        this.cbarcode = cbarcode;
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

    public String getIsok() {
        return isok;
    }

    public void setIsok(String isok) {
        this.isok = isok;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getIscpksigned() {
        return iscpksigned;
    }

    public void setIscpksigned(String iscpksigned) {
        this.iscpksigned = iscpksigned;
    }

    public String getIscompleted() {
        return iscompleted;
    }

    public void setIscompleted(String iscompleted) {
        this.iscompleted = iscompleted;
    }

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIcreateby() {
        return icreateby;
    }

    public void setIcreateby(Long icreateby) {
        this.icreateby = icreateby;
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

    public Long getIorgid() {
        return iorgid;
    }

    public void setIorgid(Long iorgid) {
        this.iorgid = iorgid;
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

    public Long getIqty() {
        return iqty;
    }

    public void setIqty(Long iqty) {
        this.iqty = iqty;
    }

    public Long getIupdateby() {
        return iupdateby;
    }

    public void setIupdateby(Long iupdateby) {
        this.iupdateby = iupdateby;
    }

    public Integer getIstatus() {
        return istatus;
    }

    public void setIstatus(Integer istatus) {
        this.istatus = istatus;
    }

    public Date getDcreatetime() {
        return dcreatetime;
    }

    public void setDcreatetime(Date dcreatetime) {
        this.dcreatetime = dcreatetime;
    }

    public Date getDqcdate() {
        return dqcdate;
    }

    public void setDqcdate(Date dqcdate) {
        this.dqcdate = dqcdate;
    }

    public Date getDupdatetime() {
        return dupdatetime;
    }

    public void setDupdatetime(Date dupdatetime) {
        this.dupdatetime = dupdatetime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
