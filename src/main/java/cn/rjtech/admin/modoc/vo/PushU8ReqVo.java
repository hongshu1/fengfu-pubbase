package cn.rjtech.admin.modoc.vo;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunjun
 * @Date: 2023-06-05
 */
public class PushU8ReqVo {
    /** 工单号**/
    private String docno;
    /** 制单日期**/
    private String ddate;
    /** 仓库**/
    private String cWhCode;
    /** 生产部门*/
    private String cDepCode;
    /** 存货编号*/
    private String cinvcode;
    /** 存货名称*/
    private String cInvName;
    /**
     * 规格
     */
    private String cInvStd;
    /**
     * 生产数量
     */
    private Integer iquantity;
    /**
     * 开工日期
     */
    private String DStartDate;
    /**
     * 完工日期
     */
    private String DDueDate;
    /**
     * 行号
     */
    private String irowno;
    /**
     * 生产批号
     *
     */

    private String cBatch;

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    public String getcWhCode() {
        return cWhCode;
    }

    public void setcWhCode(String cWhCode) {
        this.cWhCode = cWhCode;
    }

    public String getcDepCode() {
        return cDepCode;
    }

    public void setcDepCode(String cDepCode) {
        this.cDepCode = cDepCode;
    }

    public String getCinvcode() {
        return cinvcode;
    }

    public void setCinvcode(String cinvcode) {
        this.cinvcode = cinvcode;
    }

    public String getcInvName() {
        return cInvName;
    }

    public void setcInvName(String cInvName) {
        this.cInvName = cInvName;
    }

    public String getcInvStd() {
        return cInvStd;
    }

    public void setcInvStd(String cInvStd) {
        this.cInvStd = cInvStd;
    }

    public Integer getIquantity() {
        return iquantity;
    }

    public void setIquantity(Integer iquantity) {
        this.iquantity = iquantity;
    }

    public String getDStartDate() {
        return DStartDate;
    }

    public void setDStartDate(String DStartDate) {
        this.DStartDate = DStartDate;
    }

    public String getDDueDate() {
        return DDueDate;
    }

    public void setDDueDate(String DDueDate) {
        this.DDueDate = DDueDate;
    }

    public String getIrowno() {
        return irowno;
    }

    public void setIrowno(String irowno) {
        this.irowno = irowno;
    }

    public String getcBatch() {
        return cBatch;
    }

    public void setcBatch(String cBatch) {
        this.cBatch = cBatch;
    }
}
