package cn.rjtech.entity.vo.prodformm;

import java.io.Serializable;

public class ProdFormMFormItemVo implements Serializable {

    /**
     * 标题名称
     */
    private  String cproditemname ;

    /**
     * 生产表单id
     */
    private Long iprodformid;
     /**
      * 生产项目id
      */
     private  Long iproditemid;

     /**
      * 检验项目id
      */
     private Long iqcitemid;

     /**
      * 次序
      */
     private  Integer iseq;


     public String getCproditemname(){
         return cproditemname;
     }
     public void setCproditemname(String cproditemname){
         this.cproditemname=cproditemname;
     }
     public Integer getIseq(){
         return iseq;
     }
     public void setIseq(Integer iseq){
         this.iseq=iseq;
     }

     public Long getIprodformid(){
         return iprodformid;
     }
     public void setIprodformid(Long iprodformid){
         this.iprodformid=iprodformid;
     }
     public Long getIproditemid(){
        return  iproditemid;
     }
    public void setIproditemid(Long iproditemid){
        this.iproditemid=iproditemid;
    }
    public Long getIqcitemid(){
         return iqcitemid;
    }
    public void setIqcitemid(Long iqcitemid){
         this.iqcitemid=iqcitemid;
    }


}
