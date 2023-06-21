package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/28 10:17
 * @Description 跳转到检验页面后，自动加载table的数据
 */
public class AutoGetCheckOutTableDatas implements Serializable {

    /*主键*/
    private Long         iautoid;
    /*来料检主表的字段*/
    private Long         iformparamid;
    /*bd_qcform表的id(质量表格设置页面的id)*/
    private Long         iqcformid;
    /*来料检主表id*/
    private Long         ircvdocqcformmid;
    /*次序*/
    private Integer      iseq;
    /*参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间*/
    private Integer      itype;
    private String       coptions;
    private String       cqcformparamids;
    /*参数名称*/
    private String       cqcitemname;
    /*项目名称*/
    private String       cqcparamname;
    /*最大值*/
    private BigDecimal   imaxval;
    /*最小值*/
    private BigDecimal   iminval;
    /*平均值*/
    private BigDecimal   istdval;
    /*存放规格公差、检查方法、点检方法、检查项目等名称*/
    private List<String> paramnamelist;
    /*每个项目维护的值，1-10或1-20*/
    private List<cValue> cvaluelist;

    public static class cValue{
        public String cvalue;
        public String iautoid;
        public String ircvdocqcformdid;
        public String iseq;
        public String name;
    }

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIformparamid() {
        return iformparamid;
    }

    public void setIformparamid(Long iformparamid) {
        this.iformparamid = iformparamid;
    }

    public Long getIqcformid() {
        return iqcformid;
    }

    public void setIqcformid(Long iqcformid) {
        this.iqcformid = iqcformid;
    }

    public Long getIrcvdocqcformmid() {
        return ircvdocqcformmid;
    }

    public void setIrcvdocqcformmid(Long ircvdocqcformmid) {
        this.ircvdocqcformmid = ircvdocqcformmid;
    }

    public Integer getIseq() {
        return iseq;
    }

    public void setIseq(Integer iseq) {
        this.iseq = iseq;
    }

    public Integer getItype() {
        return itype;
    }

    public void setItype(Integer itype) {
        this.itype = itype;
    }

    public String getCoptions() {
        return coptions;
    }

    public void setCoptions(String coptions) {
        this.coptions = coptions;
    }

    public String getCqcformparamids() {
        return cqcformparamids;
    }

    public void setCqcformparamids(String cqcformparamids) {
        this.cqcformparamids = cqcformparamids;
    }

    public String getCqcitemname() {
        return cqcitemname;
    }

    public void setCqcitemname(String cqcitemname) {
        this.cqcitemname = cqcitemname;
    }

    public String getCqcparamname() {
        return cqcparamname;
    }

    public void setCqcparamname(String cqcparamname) {
        this.cqcparamname = cqcparamname;
    }

    public BigDecimal getImaxval() {
        return imaxval;
    }

    public void setImaxval(BigDecimal imaxval) {
        this.imaxval = imaxval;
    }

    public BigDecimal getIminval() {
        return iminval;
    }

    public void setIminval(BigDecimal iminval) {
        this.iminval = iminval;
    }

    public BigDecimal getIstdval() {
        return istdval;
    }

    public void setIstdval(BigDecimal istdval) {
        this.istdval = istdval;
    }

    public List<String> getParamnamelist() {
        return paramnamelist;
    }

    public void setParamnamelist(List<String> paramnamelist) {
        this.paramnamelist = paramnamelist;
    }

    public List<cValue> getCvaluelist() {
        return cvaluelist;
    }

    public void setCvaluelist(List<cValue> cvaluelist) {
        this.cvaluelist = cvaluelist;
    }
}
