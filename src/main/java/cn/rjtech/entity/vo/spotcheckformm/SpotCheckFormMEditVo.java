package cn.rjtech.entity.vo.spotcheckformm;


import java.io.Serializable;
import java.util.List;

public class SpotCheckFormMEditVo implements Serializable {



    /**
     * 工单id
     */
    private Long modocid;

    /**
     * 表格id
     */
    private Long ispotcheckformid;

    /**
     * 点检类型 itype：1.首末点检表 2.首中末点检表
     */
    private Integer itype;

    /**
     * 工序名称;
     */
    private String coperationname;

    /**
     * 存货id
     */
    private Long iinventoryid;

    /**
     * 存货工艺id
     */
    private Long routingconfigid;

    /**
     * 主表显示数据
     */
    private SpotCheckFormMVo spotcheckformm;

    /**
     *表格项目标题
     */
    private List<SpotCheckFormItemMVo> lineRoll;

    /**
     * 表格项目内容
     */
    private List<SpotCheckFormDetailVo> lineRoll2;



    public  List<SpotCheckFormItemMVo> getLineRoll(){
        return  lineRoll;
    }
    public void setLineRollt(List<SpotCheckFormItemMVo> lineRoll){
        this.lineRoll=lineRoll;
    }

    public  List<SpotCheckFormDetailVo> getLineRoll2(){
        return  lineRoll2;
    }
    public void setLineRollt2(List<SpotCheckFormDetailVo> lineRoll2){
        this.lineRoll2=lineRoll2;
    }

    public SpotCheckFormMVo getSpotCheckFormMVo(){
        return  spotcheckformm;
    }
    public void setSpotCheckFormMVo(SpotCheckFormMVo spotcheckformm){
        this.spotcheckformm=spotcheckformm;
    }

    public Long getModocid(){
        return modocid;
    }
    public void setModocid(Long modocid){
        this.modocid=modocid;
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



    public String getCoperationname(){
        return coperationname;
    }
    public void setCoperationname(String coperationname){
        this.coperationname=coperationname;
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
}
