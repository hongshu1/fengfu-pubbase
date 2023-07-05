package cn.rjtech.entity.vo.spotcheckformm;

import java.io.Serializable;

public class SpotCheckFormMVo implements Serializable {

    /**
     * 主表id 没有保存时是没有数据的
     */
    private Long iautoid;

    /**
     * 审核状态
     */
    private Integer iauditstatus;

    /**
     * 审批方式
     */
    private Integer iauditway;

    /**
     * 点检异常记录
     */
    private String cdesc;

    /**
     *处理方法
     */

    private String cmethod;

    public Long getIautoid(){
        return iautoid;
    }
    public void setIautoid(Long iautoid){
        this.iautoid=iautoid;
    }

    public Integer getIauditway(){
        return iauditway;
    }
    public void setIauditway(Integer iauditway){
        this.iauditway=iauditway;
    }

    public Integer getIauditstatus(){
        return iauditstatus;
    }
    public void setIauditstatus(Integer iauditstatus){
        this.iauditstatus=iauditstatus;
    }

    public  String getCdesc(){
        return cdesc;
    }
    public void setCdesc(String cdesc){
        this.cdesc=cdesc;
    }
    public String getCmethod(){
        return cmethod;
    }
    public void setCmethod(String cmethod){
        this.cmethod=cmethod;
    }
}
