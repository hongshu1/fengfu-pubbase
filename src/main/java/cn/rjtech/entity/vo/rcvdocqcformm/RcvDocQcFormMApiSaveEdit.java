package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/28 13:20
 * @Description 点击“检验”按钮，在检验页面点击“确定”按钮，将数据带到后台保存
 */
public class RcvDocQcFormMApiSaveEdit implements Serializable {

    private Long                   iautoid;
    private Long                   iqcformid;
    private Integer                iseq;
    private Integer                isubseq;
    private Integer                itype;
    private BigDecimal             imaxval;
    private BigDecimal             iminval;
    private BigDecimal             istdval;
    private String                 coptions;
    private String                 cqcformparamids;
    private String                 cqcparamname;
    private String                 cqcitemname;
    private List<CValue>           cvalueList;
    private List<SerializeElement> serializeElementList;

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIqcformid() {
        return iqcformid;
    }

    public void setIqcformid(Long iqcformid) {
        this.iqcformid = iqcformid;
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

    public String getCqcparamname() {
        return cqcparamname;
    }

    public void setCqcparamname(String cqcparamname) {
        this.cqcparamname = cqcparamname;
    }

    public String getCqcitemname() {
        return cqcitemname;
    }

    public void setCqcitemname(String cqcitemname) {
        this.cqcitemname = cqcitemname;
    }

    public List<CValue> getCvalueList() {
        return cvalueList;
    }

    public void setCvalueList(List<CValue> cvalueList) {
        this.cvalueList = cvalueList;
    }

    public List<SerializeElement> getSerializeElementList() {
        return serializeElementList;
    }

    public void setSerializeElementList(
        List<SerializeElement> serializeElementList) {
        this.serializeElementList = serializeElementList;
    }

    public static class CValue {

        public Long lineiautoid;

        public Long getLineiautoid() {
            return lineiautoid;
        }

        public void setLineiautoid(Long lineiautoid) {
            this.lineiautoid = lineiautoid;
        }
    }

    public static class SerializeElement {

        public String name;
        public String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
