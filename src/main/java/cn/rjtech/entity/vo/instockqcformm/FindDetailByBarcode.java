package cn.rjtech.entity.vo.instockqcformm;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/6/27 16:40
 * @Description TODO
 */
public class FindDetailByBarcode {
    /*现品票*/
    private String cbarcode;
    /*数量*/
    private String iqty;
    /*存货编码*/
    private String invcode;
    /*客户部番*/
    private String cinvcode1;
    /*部品名称*/
    private String cinvname1;
    /*bd_inventory的主键*/
    private String iinventoryid;
    /*测定理由*/
    private String cmeasurereason;
    /*设变号*/
    private String cdcno;
    /*bd_qcform表的主键*/
    private String iqcformid;

    public String getCbarcode() {
        return cbarcode;
    }

    public void setCbarcode(String cbarcode) {
        this.cbarcode = cbarcode;
    }

    public String getIqty() {
        return iqty;
    }

    public void setIqty(String iqty) {
        this.iqty = iqty;
    }

    public String getInvcode() {
        return invcode;
    }

    public void setInvcode(String invcode) {
        this.invcode = invcode;
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

    public String getIinventoryid() {
        return iinventoryid;
    }

    public void setIinventoryid(String iinventoryid) {
        this.iinventoryid = iinventoryid;
    }

    public String getCmeasurereason() {
        return cmeasurereason;
    }

    public void setCmeasurereason(String cmeasurereason) {
        this.cmeasurereason = cmeasurereason;
    }

    public String getCdcno() {
        return cdcno;
    }

    public void setCdcno(String cdcno) {
        this.cdcno = cdcno;
    }

    public String getIqcformid() {
        return iqcformid;
    }

    public void setIqcformid(String iqcformid) {
        this.iqcformid = iqcformid;
    }
}
