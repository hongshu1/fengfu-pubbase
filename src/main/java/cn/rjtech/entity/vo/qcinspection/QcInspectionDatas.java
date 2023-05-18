package cn.rjtech.entity.vo.qcinspection;

import cn.rjtech.model.momdata.QcInspection;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.model.momdata.StockoutDefect;
import com.jfinal.plugin.activerecord.Record;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 工程内品质巡查
 */

public class QcInspectionDatas implements Serializable {

    /**主键ID*/
    public  Long iautoid;
    /**组织ID*/
    public Long iorgid;
    /**组织编码*/
    public String corgcode;
    /**组织名称*/
    public  String corgname;
    /**连锁号，取自数据字典，编码*/
    public String cchainno;
    /**连锁号名称，取自数据字典，名称*/
    public String cchainname;
    /**是否首发：0. 再发 1. 首发*/
    public Boolean isfirstcase;
    /**发现日期*/
    public Date drecorddate;
    /**品质担当人员ID*/
    public Long iqcdutypersonid;
    /**责任科室部门ID*/
    public Long iqcdutydepartmentid;
    /**位置/区域*/
    public String cplace;
    /**问题点*/
    public  String cproblem;
    /**原因分析*/
    public  String canalysis;
    /**对策*/
    public  String cmeasure;
    /**日期*/
    public Date ddate;
    /**担当*/
    public  Long idutypersonid;
    /**判定: 1. OK 2. NG*/
    public  Integer iestimate;
    /**对策上传*/
    public  String cmeasureattachments;
    /**巡查单号*/
    public  String inspectionno;

    /**主表*/
    private QcInspection qcInspection;



    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIorgid() {
        return iorgid;
    }

    public void setIorgid(Long iorgid) {
        this.iorgid = iorgid;
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

    public String getCchainno() {
        return cchainno;
    }

    public void setCchainno(String cchainno) {
        this.cchainno = cchainno;
    }

    public String getCchainname() {
        return cchainname;
    }

    public void setCchainname(String cchainname) {
        this.cchainname = cchainname;
    }

    public Boolean getIsfirstcase() {
        return isfirstcase;
    }

    public void setIsfirstcase(Boolean isfirstcase) {
        this.isfirstcase = isfirstcase;
    }

    public Date getDrecorddate() {
        return drecorddate;
    }

    public void setDrecorddate(Date drecorddate) {
        this.drecorddate = drecorddate;
    }

    public Long getIqcdutypersonid() {
        return iqcdutypersonid;
    }

    public void setIqcdutypersonid(Long iqcdutypersonid) {
        this.iqcdutypersonid = iqcdutypersonid;
    }

    public Long getIqcdutydepartmentid() {
        return iqcdutydepartmentid;
    }

    public void setIqcdutydepartmentid(Long iqcdutydepartmentid) {
        this.iqcdutydepartmentid = iqcdutydepartmentid;
    }

    public String getCplace() {
        return cplace;
    }

    public void setCplace(String cplace) {
        this.cplace = cplace;
    }

    public String getCproblem() {
        return cproblem;
    }

    public void setCproblem(String cproblem) {
        this.cproblem = cproblem;
    }

    public String getCanalysis() {
        return canalysis;
    }

    public void setCanalysis(String canalysis) {
        this.canalysis = canalysis;
    }

    public String getCmeasure() {
        return cmeasure;
    }

    public void setCmeasure(String cmeasure) {
        this.cmeasure = cmeasure;
    }

    public Date getDdate() {
        return ddate;
    }

    public void setDdate(Date ddate) {
        this.ddate = ddate;
    }

    public Long getIdutypersonid() {
        return idutypersonid;
    }

    public void setIdutypersonid(Long idutypersonid) {
        this.idutypersonid = idutypersonid;
    }

    public Integer getIestimate() {
        return iestimate;
    }

    public void setIestimate(Integer iestimate) {
        this.iestimate = iestimate;
    }

    public String getCmeasureattachments() {
        return cmeasureattachments;
    }

    public void setCmeasureattachments(String cmeasureattachments) {
        this.cmeasureattachments = cmeasureattachments;
    }

    public String getInspectionno() {
        return inspectionno;
    }

    public void setInspectionno(String inspectionno) {
        this.inspectionno = inspectionno;
    }

    public QcInspection getQcInspection() {
        return qcInspection;
    }

    public void setQcInspection(QcInspection qcInspection) {
        this.qcInspection = qcInspection;
    }

}
