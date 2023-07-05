package cn.rjtech.entity.vo.spotcheckformm;

import java.io.Serializable;

public class FormJsonData implements Serializable {

    /**
     * 主表id 保存过后才有值
     */
    private Long spotcheckformmid;
    /**
     *工序工艺ID
     */
    private Long routingconfigid;
    /**
     *生产订单ID
     */
    private Long modocid;
    /**
     *工序名称
     */
    private String coperationname;
    /**
     *点检表格ID
     */
    private Long iprodformid;
    /**
     *类型1.首末点检表 2.首中末点检表
     */
    private Integer itype;
    /**
     *点检记录
     */
    private String cdesc;
    /**
     *处理方式
     */
    private String cmethod;
    public void setRoutingconfigid(Long routingconfigid){
        this.routingconfigid= routingconfigid;
    }
    public Long getRoutingconfigid(){
        return routingconfigid;
    }
    public void setIprodformid(Long iprodformid){
        this.iprodformid=iprodformid;
    }
    public Long getIprodformid(){
        return iprodformid;
    }
    public void setModocid(Long modocid){
        this.modocid=modocid;
    }
    public Long getModocid(){
        return modocid;
    }
    public void setSpotcheckformmid(Long spotcheckformmid){
        this.spotcheckformmid=spotcheckformmid;
    }
    public Long getSpotcheckformmid(){
        return spotcheckformmid;
    }
    public void setCoperationname(String coperationname){
        this.coperationname=coperationname;
    }
    public String getCoperationname(){
        return coperationname;
    }
    public void setItype(Integer itype){
        this.itype=itype;
    }
    public Integer getItype(){
        return itype;
    }
    public void setCdesc(String cdesc){
        this.cdesc=cdesc;
    }
    public String getCdesc(){
        return cdesc;
    }
    public void setCmethod(String cmethod){
        this.cmethod=cmethod;
    }
    public String getCmethod(){
        return cmethod;
    }



}
