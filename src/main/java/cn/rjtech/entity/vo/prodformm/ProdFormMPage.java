package cn.rjtech.entity.vo.prodformm;

import java.io.Serializable;
import java.util.Date;

public class ProdFormMPage implements Serializable {
    /**主键ID*/
    private  Long  iAutoId;
    /**组织ID*/
    private  Long iOrgId;
    /**组织编码*/
    private  String  cOrgCode;
    /**组织名称*/
    private  String  cOrgName;
    /**表格名称*/
    private  Long  iProdFormId;
    /**班次ID*/
    private  Long  iWorkShiftMid;
    /**产线ID*/
    private  Long  iWorkRegionMid;
    /**日期*/
    private Date dDate;
    /**生产人员ID*/
    private   Long   iPersonId;
    /**生产人员名称*/
    private   String   cPersonName;
    /**备注*/
    private  String  cMemo;
    /**审核状态;0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    private  Integer  iAuditStatus;
    /**审核时间*/
    private  Date  dAuditTime;
    /**审批方式;1. 审批状态 2. 审批流*/
    private  Boolean  iAuditWay;
    /**审核人ID*/
    private  Long  iAuditBy;
    /**审核人名称*/
    private  String  cAuditName;
    /**提审时间*/
    private Date dSubmitTime;
    /**创建人ID*/
    private  Long  iCreateBy;
    /**创建人名称*/
    private  String  cCreateName;
    /**创建时间*/
    private  Date  dCreateTime;
    /**更新人ID*/
    private  Long iUpdateBy;
    /**更新人名称*/
    private  String  cUpdateName;
    /**更新时间*/
    private  Date  dUpdateTime;
    /**删除状态;0. 未删除 1. 已删除*/
    private  Boolean  isDeleted;




    public Long getiAutoId(){
        return iAutoId;
    }
    public void  setiAutoId(Long iAutoId){
        this.iAutoId=iAutoId;
    }

    public Long getiOrgId(){
        return iOrgId;
    }
    public void setiOrgId(Long iOrgId){
        this.iOrgId=iOrgId;
    }

    public String getcOrgCode(){
        return cOrgCode;
    }
    public void setcOrgCode(String cOrgCode){
        this.cOrgCode=cOrgCode;
    }

    public String getcOrgName(){
        return cOrgName;
    }
    public void setcOrgName(String cOrgName){
        this.cOrgName=cOrgName;
    }

    public Long getiProdFormId(){
        return iProdFormId;
    }
    public void setiProdFormId(Long iProdFormId){
        this.iProdFormId=iProdFormId;
    }

    public Long getiWorkShiftMid(){
        return iWorkShiftMid;
    }
    public void setiWorkShiftMid(Long iWorkShiftMid){
        this.iWorkShiftMid=iWorkShiftMid;
    }

    public Long getiWorkRegionMid(){
        return iWorkRegionMid;
    }
    public void setiWorkRegionMid(Long iWorkRegionMid){
        this.iWorkRegionMid=iWorkRegionMid;
    }

    public Date getdDate(){
        return dDate;
    }
    public void setdDate(Date dDate){
        this.dDate=dDate;
    }

    public String getcMemo(){
        return cMemo;
    }
    public void setcMemo(String cMemo){
        this.cMemo=cMemo;
    }

    public Integer getiAuditStatus(){
        return iAuditStatus;
    }
    public void  setiAuditStatus(Integer iAuditStatus){
        this.iAuditStatus=iAuditStatus;
    }

    public Date getdAuditTime(){
        return dAuditTime;
    }
    public void setdAuditTime(Date dAuditTime){
        this.dAuditTime=dAuditTime;
    }

    public Boolean getiAuditWay(){
        return iAuditWay;
    }
    public void setiAuditWay(Boolean iAuditWay){
        this.iAuditWay=iAuditWay;
    }

    public Long getiAuditBy(){
        return iAuditBy;
    }
    public void setiAuditBy(Long iAuditBy){
        this.iAuditBy=iAuditBy;
    }

    public String getcAuditName(){
        return cAuditName;
    }
    public void setcAuditName(String cAuditName){
        this.cAuditName=cAuditName;
    }

    public Date getdSubmitTime(){
        return dSubmitTime;
    }
    public void setdSubmitTime(Date dSubmitTime){
        this.dSubmitTime=dSubmitTime;
    }

    public Long getiCreateBy(){
        return iCreateBy;
    }
    public void setiCreateBy(Long iCreateBy){
        this.iCreateBy=iCreateBy;
    }

    public String getcCreateName(){
        return cCreateName;
    }
    public void setcCreateName(String cCreateName){
        this.cCreateName=cCreateName;
    }

    public Date getdCreateTime(){
        return dCreateTime;
    }
    public void setdCreateTime(Date dCreateTime){
        this.dCreateTime=dCreateTime;
    }

    public Long getiUpdateBy(){
        return iUpdateBy;
    }
    public void setiUpdateBy(Long iUpdateBy){
        this.iUpdateBy=iUpdateBy;
    }

    public String getcUpdateName(){
        return  cUpdateName;
    }
    public void  setcUpdateName(String cUpdateName){
        this.cUpdateName=cUpdateName;
    }

    public Date getdUpdateTime(){
        return dUpdateTime;
    }
    public void setdUpdateTime(Date dUpdateTime){
        this.dUpdateTime=dUpdateTime;
    }

    public Boolean getIsDeleted(){
        return isDeleted;
    }
    public void setIsDeleted(Boolean isDeleted){
        this.isDeleted=isDeleted;
    }

    public  void setiPersonId(Long iPersonId){
        this.iPersonId= iPersonId;
    }
    public Long getiPersonId(){
        return iPersonId;
    }


    public  void seticPersonName(String cPersonName){
        this.cPersonName= cPersonName;
    }
    public String geticPersonName(){
        return cPersonName;
    }




}
