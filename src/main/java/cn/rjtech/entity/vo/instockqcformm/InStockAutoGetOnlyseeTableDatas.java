package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/5 10:59
 * @Description 跳转到查看页面，自动加载table数据
 */
public class InStockAutoGetOnlyseeTableDatas implements Serializable {

    private Long         iautoid;
    private Long         iformparamid;
    private Long         iqcformid;
    private String       coptions;
    private String       cqcformparamids;
    private String       cqcitemname;
    private String       cqcparamname;
    private Integer      iseq;
    private Integer      isubseq;
    private Integer      itype;
    private BigDecimal   imaxval;
    private BigDecimal   iminval;
    private BigDecimal   istdval;
    private List<Cvalue> cvaluelist;

    public static class Cvalue{
        private Long iautoid;
        private Long iformparamid;
        private Long iqcformid;
        private Long iinstockqcformmid;
        private Long lineiautoid;
        private String coptions;
        private String cqcformparamids;
        private String cqcitemname;
        private String cqcparamname;
        private String name;
        private String cvalue;
        private Integer iseq;
        private Integer isubseq;
        private Integer itype;
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

        public Long getLineiautoid() {
            return lineiautoid;
        }

        public void setLineiautoid(Long lineiautoid) {
            this.lineiautoid = lineiautoid;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCvalue() {
            return cvalue;
        }

        public void setCvalue(String cvalue) {
            this.cvalue = cvalue;
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

    public List<Cvalue> getCvaluelist() {
        return cvaluelist;
    }

    public void setCvaluelist(List<Cvalue> cvaluelist) {
        this.cvaluelist = cvaluelist;
    }
}
