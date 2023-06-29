package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
    private Long       iinstockqcformmid;//在库检-主页面的主键
    private Long       istockoutqcformmid;//出货检-主页面的主键
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

    /*存放规格公差、检查方法、点检方法、检查项目等名称*/
    private List<String> paramnamelist;
    /*每个项目维护的值，1-10或1-20*/
    private List<cValue> cvaluelist;

    public static class cValue {

        public String cvalue;
        public String iautoid;
        public String iinstockqcformdid;//在库检-从表的主键
        public String istockoutqcformdid;//出货检-从表的主键
        public String iseq;
        public String name;

        public String getCvalue() {
            return cvalue;
        }

        public void setCvalue(String cvalue) {
            this.cvalue = cvalue;
        }

        public String getIautoid() {
            return iautoid;
        }

        public void setIautoid(String iautoid) {
            this.iautoid = iautoid;
        }

        public String getIinstockqcformdid() {
            return iinstockqcformdid;
        }

        public void setIinstockqcformdid(String iinstockqcformdid) {
            this.iinstockqcformdid = iinstockqcformdid;
        }

        public String getIstockoutqcformdid() {
            return istockoutqcformdid;
        }

        public void setIstockoutqcformdid(String istockoutqcformdid) {
            this.istockoutqcformdid = istockoutqcformdid;
        }

        public String getIseq() {
            return iseq;
        }

        public void setIseq(String iseq) {
            this.iseq = iseq;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public Long getIstockoutqcformmid() {
        return istockoutqcformmid;
    }

    public void setIstockoutqcformmid(Long istockoutqcformmid) {
        this.istockoutqcformmid = istockoutqcformmid;
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
