package cn.rjtech.entity.vo.instockdefect;

import cn.rjtech.entity.vo.RcDocDefect.RcDocDefect;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 来料异常品记录
 */

public class InStockDefect implements Serializable {

    /**
     * 主键ID
     */
    private Long iautoid;
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


    /**
     * 在库纪录List
     **/
    private List<InStockDefect> inStockDefects;

    /**
     * 开始时间
     */
    private Date startdate;
    /**
     * 结束时间
     */
    private Date enddate;

    public InStockDefect(Long iautoid, Long iinstockqcformmid, String type, String cOrgName, String cdocno, String imodocid, String cinvcode1, String cinvcode, Integer istatus, String cinvname, Boolean isfirsttime, String capproach, Integer iresptype, List<InStockDefect> inStockDefects, Date startdate, Date enddate) {
        this.iautoid = iautoid;
        this.iinstockqcformmid = iinstockqcformmid;
        this.type = type;
        this.cOrgName = cOrgName;
        this.cdocno = cdocno;
        this.imodocid = imodocid;
        this.cinvcode1 = cinvcode1;
        this.cinvcode = cinvcode;
        this.istatus = istatus;
        this.cinvname = cinvname;
        this.isfirsttime = isfirsttime;
        this.capproach = capproach;
        this.iresptype = iresptype;
        this.inStockDefects = inStockDefects;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
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

    public List<InStockDefect> getInStockDefects() {
        return inStockDefects;
    }

    public void setInStockDefects(List<InStockDefect> inStockDefects) {
        this.inStockDefects = inStockDefects;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
}

