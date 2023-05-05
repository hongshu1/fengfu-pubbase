package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/5 10:55
 * @Description 自动加载检验页面的table数据
 */
public class InStockAutoGetCheckOutTableDatas implements Serializable {

    private Long       iautoid;
    private Long       iformparamid;
    private Long       iqcformid;
    private Long       iinstockqcformmid;
    private Integer    iseq;
    private Integer    isubseq;
    private Integer    itype;//参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
    private String     coptions;
    private String     cqcformparamids;
    private String     cqcitemname;
    private String     cqcparamname;
    private BigDecimal imaxval;
    private BigDecimal iminval;
    private BigDecimal istdval;

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

    public Long getIinstockqcformmid() {
        return iinstockqcformmid;
    }

    public void setIinstockqcformmid(Long iinstockqcformmid) {
        this.iinstockqcformmid = iinstockqcformmid;
    }

    public Integer getIseq() {
        return iseq;
    }

    public void setIseq(Integer iseq) {
        this.iseq = iseq;
    }

    public Integer getIsubseq() {
        return isubseq;
    }

    public void setIsubseq(Integer isubseq) {
        this.isubseq = isubseq;
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
}
