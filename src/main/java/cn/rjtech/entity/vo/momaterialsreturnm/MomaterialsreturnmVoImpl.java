package cn.rjtech.entity.vo.momaterialsreturnm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MomaterialsreturnmVoImpl implements Serializable {

    /**主键ID*/
    private  Long  iAutoId;
    /**组织ID*/
    private  Long  iOrgId;
    /**组织编码*/
    private  String  cOrgCode;
    /**组织名称*/
    private  String  cOrgName;
    /**生产工单ID*/
    private  Long  iMoDocId;
    /**审批方式：1. 审批状态 2. 审批流*/
    private Integer iAuditWay;
    /**提审时间*/
    private Date dSubmitTime;
    /**审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    private Integer iAuditStatus;
    /**审核时间*/
    private Date dAuditTime;
    /**备注*/
    private String cMemo;
    /**创建人ID*/
    private  Long  iCreateBy;
    /**创建人名称*/
    private  String  cCreateName;
    /**创建时间*/
    private  Date  dCreateTime;
    /**更新人ID*/
    private  Long  iUpdateBy;
    /**更新人名称*/
    private  String  cUpdateName;
    /**更新时间*/
    private Date dUpdateTime;
    /**删除状态;0. 未删除 1. 已删除*/
    private  Boolean  isDeleted;
    /**生产退料单ID*/
    private Long iMaterialsReturnMid;
    /**存货ID*/
    private Long iInventoryId;
    /**条码号*/
    private  String cBarcode;
    /**数量*/
    private BigDecimal iQty;

    public Long getiMaterialsReturnMid() {
        return iMaterialsReturnMid;
    }

    public void setiMaterialsReturnMid(Long iMaterialsReturnMid) {
        this.iMaterialsReturnMid = iMaterialsReturnMid;
    }

    public Long getiInventoryId() {
        return iInventoryId;
    }

    public void setiInventoryId(Long iInventoryId) {
        this.iInventoryId = iInventoryId;
    }

    public String getcBarcode() {
        return cBarcode;
    }

    public void setcBarcode(String cBarcode) {
        this.cBarcode = cBarcode;
    }

    public BigDecimal getiQty() {
        return iQty;
    }

    public void setiQty(BigDecimal iQty) {
        this.iQty = iQty;
    }

    public Long getiAutoId() {
        return iAutoId;
    }

    public void setiAutoId(Long iAutoId) {
        this.iAutoId = iAutoId;
    }

    public Long getiOrgId() {
        return iOrgId;
    }

    public void setiOrgId(Long iOrgId) {
        this.iOrgId = iOrgId;
    }

    public String getcOrgCode() {
        return cOrgCode;
    }

    public void setcOrgCode(String cOrgCode) {
        this.cOrgCode = cOrgCode;
    }

    public String getcOrgName() {
        return cOrgName;
    }

    public void setcOrgName(String cOrgName) {
        this.cOrgName = cOrgName;
    }

    public Long getiMoDocId() {
        return iMoDocId;
    }

    public void setiMoDocId(Long iMoDocId) {
        this.iMoDocId = iMoDocId;
    }

    public Integer getiAuditWay() {
        return iAuditWay;
    }

    public void setiAuditWay(Integer iAuditWay) {
        this.iAuditWay = iAuditWay;
    }

    public Date getdSubmitTime() {
        return dSubmitTime;
    }

    public void setdSubmitTime(Date dSubmitTime) {
        this.dSubmitTime = dSubmitTime;
    }

    public Integer getiAuditStatus() {
        return iAuditStatus;
    }

    public void setiAuditStatus(Integer iAuditStatus) {
        this.iAuditStatus = iAuditStatus;
    }

    public Date getdAuditTime() {
        return dAuditTime;
    }

    public void setdAuditTime(Date dAuditTime) {
        this.dAuditTime = dAuditTime;
    }

    public String getcMemo() {
        return cMemo;
    }

    public void setcMemo(String cMemo) {
        this.cMemo = cMemo;
    }

    public Long getiCreateBy() {
        return iCreateBy;
    }

    public void setiCreateBy(Long iCreateBy) {
        this.iCreateBy = iCreateBy;
    }

    public String getcCreateName() {
        return cCreateName;
    }

    public void setcCreateName(String cCreateName) {
        this.cCreateName = cCreateName;
    }

    public Date getdCreateTime() {
        return dCreateTime;
    }

    public void setdCreateTime(Date dCreateTime) {
        this.dCreateTime = dCreateTime;
    }

    public Long getiUpdateBy() {
        return iUpdateBy;
    }

    public void setiUpdateBy(Long iUpdateBy) {
        this.iUpdateBy = iUpdateBy;
    }

    public String getcUpdateName() {
        return cUpdateName;
    }

    public void setcUpdateName(String cUpdateName) {
        this.cUpdateName = cUpdateName;
    }

    public Date getdUpdateTime() {
        return dUpdateTime;
    }

    public void setdUpdateTime(Date dUpdateTime) {
        this.dUpdateTime = dUpdateTime;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
