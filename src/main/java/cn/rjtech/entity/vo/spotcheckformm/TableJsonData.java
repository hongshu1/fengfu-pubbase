package cn.rjtech.entity.vo.spotcheckformm;

import java.io.Serializable;
import java.math.BigDecimal;

public class TableJsonData implements Serializable {

    /**
     * 检验项目ID
     */
    private Long spotcheckformtableparamid;
    /**
     * 项目次序
     */
    private Integer iseq;
    /**
     * 检验参数值ID
     */
    private Long spotcheckparamid;
    /**
     * 参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
     */
    private Integer itype;
    /**
     * 标准值
     */
    private BigDecimal istdval;

    /**
     * 最大设定值
     */
    private BigDecimal imaxval;

    /**
     *  最小设定值
     */
    private BigDecimal iminval;
    /**
     * 列表可选值;多个“,”分隔
     */
    private String coptions;
    /**
     * 填写值
     */
    private String cvalue;



    public void setSpotcheckformtableparamid(Long spotcheckformtableparamid){
        this.spotcheckformtableparamid=spotcheckformtableparamid;
    }
    public Long getSpotcheckformtableparamid(){
        return spotcheckformtableparamid;
    }
    public void setSpotcheckparamid(Long spotcheckparamid){
        this.spotcheckparamid=spotcheckparamid;
    }
    public Long getSpotcheckparamid(){
        return spotcheckparamid;
    }
    public void setiSeq(Integer iseq){
        this.iseq=iseq;
    }
    public Integer getiSeq(){
        return iseq;
    }
    public void setiType(Integer itype){
        this.itype=itype;
    }
    public Integer getiType(){
        return itype;
    }
    public void setiStdVal(BigDecimal istdval){
        this.istdval=istdval;
    }
    public BigDecimal getiStdVal(){
        return istdval;
    }
    public void setiMaxVal(BigDecimal imaxval){
        this.imaxval=imaxval;
    }
    public BigDecimal getiMaxVal(){
        return  imaxval;
    }
    public void setiMinVal(BigDecimal iminval){
        this.iminval=iminval;
    }
    public BigDecimal getiMinVal(){
        return iminval;
    }
    public void setcOptions(String coptions){
        this.coptions=coptions;
    }
    public String getcOptions(){
        return coptions;
    }

    public void setCvalue(String cvalue){
        this.cvalue=cvalue;
    }
    public String getCvalue(){
        return cvalue;
    }
}
