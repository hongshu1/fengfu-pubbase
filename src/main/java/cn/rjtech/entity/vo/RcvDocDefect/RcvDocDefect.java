package cn.rjtech.entity.vo.RcvDocDefect;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 来料异常品记录
 */

public class RcvDocDefect implements Serializable {

    /**
     * 主键ID
     */
    private Long iautoid;
    /**
     * 质量管理-来料检id
     */
    private Long ircvdocqcformmid;
    /**
     * 存货ID
     */
    private Long iinventoryid;
    /**
     * 供应商ID
     */
    private Long ivendorid;
    /**
     * 查看状态
     */
    private String type;
    /**
     * 不合格数量
     */
    private Integer idqqty;

    /**
     * 状态名称
     */
    private String auditstate;
    /**
     * 检验单号
     */
    private String crcvdocqcformno;
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
     * 状态
     */
    private Integer istatus;

    /**
     * 存货编码编码
     */
    private String cinvname;

    /**
     * 首发再发
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



    /**来料异常品记录List**/
    private List<RcvDocDefect> RcDocDefect;

    /**
     * 更新名称
     */
    private String cupdatename;
    /**
     * 更新时间
     */
    private Date dupdatetime;

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIrcvdocqcformmid() {
        return ircvdocqcformmid;
    }

    public void setIrcvdocqcformmid(Long ircvdocqcformmid) {
        this.ircvdocqcformmid = ircvdocqcformmid;
    }

    public Long getIinventoryid() {
        return iinventoryid;
    }

    public void setIinventoryid(Long iinventoryid) {
        this.iinventoryid = iinventoryid;
    }

    public Long getIvendorid() {
        return ivendorid;
    }

    public void setIvendorid(Long ivendorid) {
        this.ivendorid = ivendorid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIdqqty() {
        return idqqty;
    }

    public void setIdqqty(Integer idqqty) {
        this.idqqty = idqqty;
    }

    public String getAuditstate() {
        return auditstate;
    }

    public void setAuditstate(String auditstate) {
        this.auditstate = auditstate;
    }

    public String getCrcvdocqcformno() {
        return crcvdocqcformno;
    }

    public void setCrcvdocqcformno(String crcvdocqcformno) {
        this.crcvdocqcformno = crcvdocqcformno;
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

    public Integer getIstatus() {
        return istatus;
    }

    public void setIstatus(Integer istatus) {
        this.istatus = istatus;
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

    public List<RcvDocDefect> getRcDocDefect() {
        return RcDocDefect;
    }

    public void setRcDocDefect(List<RcvDocDefect> rcDocDefect) {
        RcDocDefect = rcDocDefect;
    }

    public String getCupdatename() {
        return cupdatename;
    }

    public void setCupdatename(String cupdatename) {
        this.cupdatename = cupdatename;
    }

    public Date getDupdatetime() {
        return dupdatetime;
    }

    public void setDupdatetime(Date dupdatetime) {
        this.dupdatetime = dupdatetime;
    }
}
