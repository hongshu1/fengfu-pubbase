package cn.rjtech.entity.vo.stockoutqcformm;

import java.io.Serializable;
import java.util.Date;

import com.jfinal.plugin.activerecord.Record;

import cn.rjtech.model.momdata.StockoutQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 11:38
 * @Description 跳转到检验页面
 */
public class StockoutQcFormMCheckout implements Serializable {
    /**批次号*/
    private String  cbatchno;
    /**创建人名称*/
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
    /*测定单位*/
    private String  cmeasureunit;
    /*备注*/
    private String  cmemo;
    /**/
    private String  corgcode;
    /*检验单号*/
    private String  corgname;
    /**/
    private String  cstockoutqcformno;
    /*更新人员*/
    private String  cupdatename;
    /*是否合格*/
    private String  isok;
    /*是否删除*/
    private String  isdeleted;
    /*是否切换cpk状态*/
    private String  iscpksigned;
    /*是否完成*/
    private String  iscompleted;
    /*主键*/
    private Long    iautoid;
    /*创建人id*/
    private Long    icreateby;
    /*客户id*/
    private Long    icustomerid;
    /*存货id*/
    private Long    iinventoryid;
    /**/
    private Long    iinventoryuomid1;
    /**/
    private Long    iorgid;
    /*质检表格id*/
    private Long    iqcformid;
    /*检验员*/
    private Long    iqcuserid;
    /*数量*/
    private Long    iqty;
    /*更新人id*/
    private Long    iupdateby;
    /*检验结果*/
    private Integer istatus;
    /*创建时间*/
    private Date    dcreatetime;
    /*检验日期*/
    private Date    dqcdate;
    /*更新时间*/
    private Date    dupdatetime;
    /*列数，例如1-10，或者1-20，1-30*/
    private int     size;

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

    public String getCstockoutqcformno() {
        return cstockoutqcformno;
    }

    public void setCstockoutqcformno(String cstockoutqcformno) {
        this.cstockoutqcformno = cstockoutqcformno;
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
