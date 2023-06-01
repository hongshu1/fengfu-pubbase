package cn.rjtech.entity.vo.formuploadcategory;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yjllzy
 */
public class FormUploadCategoryVoImpl implements Serializable {

    /**主键ID*/
    private  Long  iAutoId;
    /**组织ID*/
    private  Long  iOrgId;
    /**组织编码*/
    private  String  cOrgCode;
    /**组织名称*/
    private  String  cOrgName;
    /**产线ID*/
    private  Long iWorkRegionMid;
    /**分类编码*/
    private  String  cCategoryCode;
    /**分类名称*/
    private  String cCategoryName;
    /**上级分类ID*/
    private  Long iPid;
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

    public Long getiWorkRegionMid(){
        return iWorkRegionMid;
    }
    public void setiWorkRegionMid(Long iWorkRegionMid){
        this.iWorkRegionMid=iWorkRegionMid;
    }

    public String getcCategoryCode(){
        return cCategoryCode;
    }
    public void setcCategoryCode(String cCategoryCode){
        this.cCategoryCode=cCategoryCode;
    }

    public String getcCategoryName(){
        return cCategoryName;
    }
    public void setcCategoryName(String cCategoryName){
        this.cCategoryName=cCategoryName;
    }

    public Long getiPid(){
        return iPid;
    }
    public void setiPid(Long iPid){
        this.iPid=iPid;
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
}
