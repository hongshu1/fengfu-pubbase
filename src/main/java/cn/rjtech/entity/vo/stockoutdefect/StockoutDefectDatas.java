package cn.rjtech.entity.vo.stockoutdefect;

import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.model.momdata.StockoutDefect;
import cn.rjtech.model.momdata.StockoutQcFormM;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 来料异常品记录
 */

public class StockoutDefectDatas implements Serializable {

    /**
     * 主键ID
     */
    private Long iautoid;
    /**
     * 质量管理-出库检id
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


    private StockoutDefect stockoutDefect;

    /**
     * 开始时间
     */
    private Date startdate;
    /**
     * 结束时间
     */
    private Date enddate;

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


    public StockoutDefect getStockoutDefect() {
        return stockoutDefect;
    }

    public void setStockoutDefect(StockoutDefect stockoutDefect) {
        this.stockoutDefect = stockoutDefect;
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
