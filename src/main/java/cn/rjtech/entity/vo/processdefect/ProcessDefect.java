package cn.rjtech.entity.vo.processdefect;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 制造异常品记录
 */

public class ProcessDefect implements Serializable {


    /**组织编码*/
    private  String corgcode;



    /**生产部门ID*/
    private Long idepartmentid;
    /**需求日期*/
    private Date ddemanddate;
    /**状态名称：1. 已保存 2. 待审批 3. 已审批*/
    private String auditstate;
    /**创建人ID*/
    private Long icreateby;
    /**创建人名称*/
    private String ccreatename;
    /**创建时间*/
    private Date dcreatetime;
    /**更新人ID*/
    private Long iupdateby;
    /**更新人名称*/
    private String cupdatename;
    /**更新时间*/
    private Date dupdatetime;
    /**删除状态：0. 未删除 1. 已删除*/
    private  Boolean isdeleted;

    /**不良数量*/
    private Integer idqqty;

    /**不良项目，字典编码，多个“,”分隔*/
    private String cbadnesssns;

    /**不良内容描述*/
    private String cdesc;
    /**工序名称*/
    private String processname;
    /**存货ID*/
    private Long iinventoryid;

    /**
     * 主键ID
     */
    private Long iautoid;
    /**领料单ID*/
    private Long iissueid;
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



    /**制造异常品记录List**/
    private List<ProcessDefect> ProcessDefect;

    public String getCorgcode() {
        return corgcode;
    }

    public void setCorgcode(String corgcode) {
        this.corgcode = corgcode;
    }

    public Long getIdepartmentid() {
        return idepartmentid;
    }

    public void setIdepartmentid(Long idepartmentid) {
        this.idepartmentid = idepartmentid;
    }

    public Date getDdemanddate() {
        return ddemanddate;
    }

    public void setDdemanddate(Date ddemanddate) {
        this.ddemanddate = ddemanddate;
    }

    public String getAuditstate() {
        return auditstate;
    }

    public void setAuditstate(String auditstate) {
        this.auditstate = auditstate;
    }

    public Long getIcreateby() {
        return icreateby;
    }

    public void setIcreateby(Long icreateby) {
        this.icreateby = icreateby;
    }

    public String getCcreatename() {
        return ccreatename;
    }

    public void setCcreatename(String ccreatename) {
        this.ccreatename = ccreatename;
    }

    public Date getDcreatetime() {
        return dcreatetime;
    }

    public void setDcreatetime(Date dcreatetime) {
        this.dcreatetime = dcreatetime;
    }

    public Long getIupdateby() {
        return iupdateby;
    }

    public void setIupdateby(Long iupdateby) {
        this.iupdateby = iupdateby;
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

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Integer getIdqqty() {
        return idqqty;
    }

    public void setIdqqty(Integer idqqty) {
        this.idqqty = idqqty;
    }

    public String getCbadnesssns() {
        return cbadnesssns;
    }

    public void setCbadnesssns(String cbadnesssns) {
        this.cbadnesssns = cbadnesssns;
    }

    public String getCdesc() {
        return cdesc;
    }

    public void setCdesc(String cdesc) {
        this.cdesc = cdesc;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public Long getIinventoryid() {
        return iinventoryid;
    }

    public void setIinventoryid(Long iinventoryid) {
        this.iinventoryid = iinventoryid;
    }

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIissueid() {
        return iissueid;
    }

    public void setIissueid(Long iissueid) {
        this.iissueid = iissueid;
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

    public List<cn.rjtech.entity.vo.processdefect.ProcessDefect> getProcessDefect() {
        return ProcessDefect;
    }

    public void setProcessDefect(List<cn.rjtech.entity.vo.processdefect.ProcessDefect> processDefect) {
        ProcessDefect = processDefect;
    }
}
