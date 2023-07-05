package cn.rjtech.entity.vo.spotcheckformm;

import java.io.Serializable;
import java.util.Date;

public class SpotCheckFormMRes implements Serializable {

    /**
     * 工单id
     */
    private Long modocid;

    /**
     * 设备名称
     */
    private String cequipmentnames;

    /**
     * 设备id
     */
    private String cequipmentids;

    /**
     * 表格id
     */
    private Long ispotcheckformid;

    /**
     * 点检类型 itype：1.首末点检表 2.首中末点检表
     */
    private Integer itype;

    /**
     * 审核状态
     */
    private String iauditstatus;

    /**
     * 表格名称
     */
    private String cspotcheckformname;

    /**
     * 工序名称;
     */
    private String coperationname;

    /**
     * 生产人员
     */
    private  String ccreatename;

    /**
     * 提交人员
     */
    private  String peoplename;

    /**
     * 提交时间
     */
    private Date dcreatetime2;

    /**
     * 存货id
     */
    private Long iinventoryid;

    /**
     * 存货工艺id
     */
    private Long routingconfigid;

    public Long getModocid(){
        return modocid;
    }
    public void setModocid(Long modocid){
        this.modocid=modocid;
    }

    public String getcequipmentnames(){
        return cequipmentnames;
    }
    public void setcequipmentnames(String cequipmentnames){
        this.cequipmentnames=cequipmentnames;
    }

    public String getcequipmentids(){
        return  cequipmentids;
    }
    public  void setcequipmentids(String cequipmentids){
        this.cequipmentids=cequipmentids;
    }

    public Long getIspotcheckformid(){
        return ispotcheckformid;
    }
    public void setIspotcheckformid(Long ispotcheckformid){
        this.ispotcheckformid=ispotcheckformid;
    }

    public Integer getItype(){
        return itype;
    }
    public void setItype(Integer itype){
        this.itype=itype;
    }

    public String getIauditstatus(){
        return iauditstatus;
    }
    public void setIauditstatus(String iauditstatus){
        this.iauditstatus=iauditstatus;
    }

    public String getCspotcheckformname(){
        return cspotcheckformname;
    }
    public  void setCspotcheckformname(String cspotcheckformname){
        this.cspotcheckformname=cspotcheckformname;
    }

    public String getCoperationname(){
        return coperationname;
    }
    public void setCoperationname(String coperationname){
        this.coperationname=coperationname;
    }
    public String getCcreatename(){
        return ccreatename;
    }
    public  void setCcreatename(String ccreatename){
        this.ccreatename=ccreatename;
    }
    public Date getDcreatetime2(){
        return dcreatetime2;
    }
    public void setDcreatetime2(Date dcreatetime2){
        this.dcreatetime2=dcreatetime2;
    }

    public Long getIinventoryid(){
        return iinventoryid;
    }
    public void setIinventoryid(Long iinventoryid){
        this.iinventoryid=iinventoryid;
    }

    public  Long getRoutingconfigid(){
        return routingconfigid;
    }
    public void setRoutingconfigid(Long routingconfigid){
        this.routingconfigid=routingconfigid;
    }

    public void setPeoplename(String peoplename){
        this.peoplename=peoplename;
    }
    public String getPeoplename(){
        return peoplename;
    }
}

