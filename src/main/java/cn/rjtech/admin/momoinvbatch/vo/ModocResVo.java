package cn.rjtech.admin.momoinvbatch.vo;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunjun
 * @Date: 2023-06-02
 */
public class ModocResVo {
    private String cinvcode1;

    private  String cinvname1;

    private String cworkname;

    private String  cworkshiftname;

    private String produceddate;

    private Integer planiqty;

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
