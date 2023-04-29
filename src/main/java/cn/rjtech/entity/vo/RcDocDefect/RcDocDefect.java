package cn.rjtech.entity.vo.RcDocDefect;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 来料异常品记录
 */

public class RcDocDefect implements Serializable {

    /**
     * 主键ID
     */
    private Long iautoid;
    /**
     * 质量管理-来料检id
     */
    private Long istockoutqcformmid;
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

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIstockoutqcformmid() {
        return istockoutqcformmid;
    }

    public void setIstockoutqcformmid(Long istockoutqcformmid) {
        this.istockoutqcformmid = istockoutqcformmid;
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

    public String getIstatus() {
        return istatus;
    }

    public void setIstatus(String istatus) {
        this.istatus = istatus;
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

    /**
     * 状态：1. 待记录 2. 待判定 3. 已完成
     */
    private String istatus;
    /**
     * 开始时间
     */
    private Date startdate;
    /**
     * 结束时间
     */
    private Date enddate;


}
