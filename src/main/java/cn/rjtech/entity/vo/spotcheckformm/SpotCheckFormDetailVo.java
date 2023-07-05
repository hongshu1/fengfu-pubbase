package cn.rjtech.entity.vo.spotcheckformm;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpotCheckFormDetailVo implements Serializable {
    /**
     * 生产表格参数录入配置id
     */
    private Long iAutoId;

    /**
     * 生产表格参数录入配置次序
     */
    private Integer iSeq;
    /**
     *参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
     */
    private Integer iType;

    /**
     * 标准值
     */
    private BigDecimal iStdVal;

    /**
     * 最大设定值
     */
    private BigDecimal iMaxVal;

    /**
     * 最小设定值
     */
    private BigDecimal iMinVal;

    /**
     *  列表可选值，多个";"分隔
     */
    private String cOptions;

    /**
     * 参数名称
     */
    private  String cProdParamName;

    /**
     * 生产表单项目次序
     */
    private  String iProdItemIseq;

    /**
     *  生产表单参数id
     */
    private Long iProdParamid;

    public void setiAutoId(Long iAutoId){
        this.iAutoId=iAutoId;
    }
    public Long getiAutoId(){
        return iAutoId;
    }
    public void setiProdParamid(Long iProdParamid){
        this.iProdParamid=iProdParamid;
    }
    public Long getiProdParamid(){
        return iProdParamid;
    }

    public void setiSeq(Integer iSeq){
        this.iSeq=iSeq;
    }
    public Integer getiSeq(){
        return iSeq;
    }
    public void setiType(Integer iType){
        this.iType=iType;
    }
    public Integer getiType(){
        return iType;
    }
    public void setiStdVal(BigDecimal iStdVal){
        this.iStdVal=iStdVal;
    }
    public BigDecimal getiStdVal(){
        return iStdVal;
    }
    public void setiMaxVal(BigDecimal iMaxVal){
        this.iMaxVal=iMaxVal;
    }
    public BigDecimal getiMaxVal(){
        return  iMaxVal;
    }
    public void setiMinVal(BigDecimal iMinVal){
        this.iMinVal=iMinVal;
    }
    public BigDecimal getiMinVal(){
        return iMinVal;
    }
    public void setcOptions(String cOptions){
        this.cOptions=cOptions;
    }
    public String getcOptions(){
        return cOptions;
    }
    public void setcProdParamName(String cProdParamName){
        this.cProdParamName=cProdParamName;
    }
    public String getcProdParamName(){
        return cProdParamName;
    }
    public void setiProdItemIseq(String iProdItemIseq){
        this.iProdItemIseq=iProdItemIseq;
    }
    public String getiProdItemIseq(){
        return iProdItemIseq;
    }
}
