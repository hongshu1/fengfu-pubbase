package cn.rjtech.admin.momoinvbatch.vo;




import java.math.BigDecimal;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunjun
 * @Date: 2023-06-02
 */
public class SubPrintReqVo  {
    private Long  iautoid;
    private Integer iseq;
    /**
     * 条码
     */
    private  String cbarcode;
    /**
     * 生产组长
     */
    private  String  workheader;
    /**
     * 作业员
     */
    private String jobname;
    private BigDecimal iqty;

    /**
     * 客户部番
     */
    private String cinvcode1;
    /**
     * 部品名称
     */

    private  String cinvname1;
    /**
     * 产线
     */

    private String cworkname;
    /**
     * 班次
     */

    private String  cworkshiftname;
    /**
     * 工单计划日期
     */

    private String produceddate;
    /**
     * 工单计数
     */
    private Integer planiqty;
    /**
     * 备注
     */
    private String memo;
    /** 编号***/
    private String num;

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Integer getIseq() {
        return iseq;
    }

    public void setIseq(Integer iseq) {
        this.iseq = iseq;
    }

    public String getCbarcode() {
        return cbarcode;
    }

    public void setCbarcode(String cbarcode) {
        this.cbarcode = cbarcode;
    }

    public String getWorkheader() {
        return workheader;
    }

    public void setWorkheader(String workheader) {
        this.workheader = workheader;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public BigDecimal getIqty() {
        return iqty;
    }

    public void setIqty(BigDecimal iqty) {
        this.iqty = iqty;
    }



    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCinvcode1() {
        return cinvcode1;
    }

    public void setCinvcode1(String cinvcode1) {
        this.cinvcode1 = cinvcode1;
    }

    public String getCinvname1() {
        return cinvname1;
    }

    public void setCinvname1(String cinvname1) {
        this.cinvname1 = cinvname1;
    }

    public String getCworkname() {
        return cworkname;
    }

    public void setCworkname(String cworkname) {
        this.cworkname = cworkname;
    }

    public String getCworkshiftname() {
        return cworkshiftname;
    }

    public void setCworkshiftname(String cworkshiftname) {
        this.cworkshiftname = cworkshiftname;
    }

    public String getProduceddate() {
        return produceddate;
    }

    public void setProduceddate(String produceddate) {
        this.produceddate = produceddate;
    }

    public Integer getPlaniqty() {
        return planiqty;
    }

    public void setPlaniqty(Integer planiqty) {
        this.planiqty = planiqty;
    }
}
