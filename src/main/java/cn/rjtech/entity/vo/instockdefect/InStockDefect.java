package cn.rjtech.entity.vo.instockdefect;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 在库异常品记录
 */

public class InStockDefect implements Serializable {


    /**
     * 不良项目，字典编码，多个“,”分隔
     */
    private  String cbadnesssns;
    /**
     * 创建人
     */
    private  String ccreatename;
    /**不良内容描述*/
    private String cdesc;

    /**组织编码*/
    private  String corgcode;

    /**创建时间*/
    private  Date dcreatetime;

    /**检验时间*/
    private  Date dqctime;

    /**检验用户ID*/
    private  Long iqcuserid;

    /**删除状态*/
    private  Boolean isdeleted;

    /**质检表格ID*/
    private  Long iQcFormId;

    /**质检表格ID*/
    private  Integer iqty;



    /**
     * 主键ID
     */
    private Long iautoid;

    /**
     * 状态名称
     */
    private String auditstate;

    /**
     * 状态编号
     */
    private String istatus;
    /**
     * 质量管理-在库检id
     */
    private Long iinstockqcformmid;
    /**
     * 查看状态
     */
    private String type;
    /**
     * 组织名称
     */
    private String cOrgName;
    /**
     * 异常品单号
     */
    private String cdocno;
    /**
     * 来料检单
     */
    private String imodocid;
    /**
     * 客户部番
     */
    private String cinvcode1;
    /**
     * 存货编码编码
     */
    private String cinvcode;


    /**
     * 存货编码编码
     */
    private String cinvname;

    /**
     * 首发再发编码
     */
    private Boolean isfirsttime;


    /**
     * 处置区分
     */
    private String capproach;

    /**
     * 责任区
     */
    private Integer iresptype;


    /**
     * 在库纪录List
     **/
    private List<InStockDefect> inStockDefects;

    /**
     * 检验时间
     */
    private Date  cinvqcformno;
    /**
     * 更新时间
     */
    private Date dupdatetime;
    /**
     * 更新人
     */
    private String cupdatename;
    /**
     * 存货id
     */
    private Long iinventoryid;

    public String getCbadnesssns() {
        return cbadnesssns;
    }

    public void setCbadnesssns(String cbadnesssns) {
        this.cbadnesssns = cbadnesssns;
    }

    public String getCcreatename() {
        return ccreatename;
    }

    public void setCcreatename(String ccreatename) {
        this.ccreatename = ccreatename;
    }

    public String getCdesc() {
        return cdesc;
    }

    public void setCdesc(String cdesc) {
        this.cdesc = cdesc;
    }

    public String getCorgcode() {
        return corgcode;
    }

    public void setCorgcode(String corgcode) {
        this.corgcode = corgcode;
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

    public Long getIqcuserid() {
        return iqcuserid;
    }

    public void setIqcuserid(Long iqcuserid) {
        this.iqcuserid = iqcuserid;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Long getiQcFormId() {
        return iQcFormId;
    }

    public void setiQcFormId(Long iQcFormId) {
        this.iQcFormId = iQcFormId;
    }

    public Integer getIqty() {
        return iqty;
    }

    public void setIqty(Integer iqty) {
        this.iqty = iqty;
    }

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public String getAuditstate() {
        return auditstate;
    }

    public void setAuditstate(String auditstate) {
        this.auditstate = auditstate;
    }

    public String getIstatus() {
        return istatus;
    }

    public void setIstatus(String istatus) {
        this.istatus = istatus;
    }

    public Long getIinstockqcformmid() {
        return iinstockqcformmid;
    }

    public void setIinstockqcformmid(Long iinstockqcformmid) {
        this.iinstockqcformmid = iinstockqcformmid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getcOrgName() {
        return cOrgName;
    }

    public void setcOrgName(String cOrgName) {
        this.cOrgName = cOrgName;
    }

    public String getCdocno() {
        return cdocno;
    }

    public void setCdocno(String cdocno) {
        this.cdocno = cdocno;
    }

    public String getImodocid() {
        return imodocid;
    }

    public void setImodocid(String imodocid) {
        this.imodocid = imodocid;
    }

    public String getCinvcode1() {
        return cinvcode1;
    }

    public void setCinvcode1(String cinvcode1) {
        this.cinvcode1 = cinvcode1;
    }

    public String getCinvcode() {
        return cinvcode;
    }

    public void setCinvcode(String cinvcode) {
        this.cinvcode = cinvcode;
    }

    public String getCinvname() {
        return cinvname;
    }

    public void setCinvname(String cinvname) {
        this.cinvname = cinvname;
    }

    public Boolean getIsfirsttime() {
        return isfirsttime;
    }

    public void setIsfirsttime(Boolean isfirsttime) {
        this.isfirsttime = isfirsttime;
    }

    public String getCapproach() {
        return capproach;
    }

    public void setCapproach(String capproach) {
        this.capproach = capproach;
    }

    public Integer getIresptype() {
        return iresptype;
    }

    public void setIresptype(Integer iresptype) {
        this.iresptype = iresptype;
    }

    public List<InStockDefect> getInStockDefects() {
        return inStockDefects;
    }

    public void setInStockDefects(List<InStockDefect> inStockDefects) {
        this.inStockDefects = inStockDefects;
    }

    public Date getCinvqcformno() {
        return cinvqcformno;
    }

    public void setCinvqcformno(Date cinvqcformno) {
        this.cinvqcformno = cinvqcformno;
    }

    public Date getDupdatetime() {
        return dupdatetime;
    }

    public void setDupdatetime(Date dupdatetime) {
        this.dupdatetime = dupdatetime;
    }

    public String getCupdatename() {
        return cupdatename;
    }

    public void setCupdatename(String cupdatename) {
        this.cupdatename = cupdatename;
    }

    public Long getIinventoryid() {
        return iinventoryid;
    }

    public void setIinventoryid(Long iinventoryid) {
        this.iinventoryid = iinventoryid;
    }
}

