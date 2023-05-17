package cn.rjtech.entity.vo.modoc;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunjun
 * @Date: 2023-05-15
 */
public class ModocApiPage {
    /**
     * 工单ID
     */
    private  Long iAutoId;
    /**
     * 制造工单任务ID
     */
    private  Long iMoTaskId;
    private  Long iInventoryId;
    private  String cInvAddCode;
    /**
     * 工单号
     */
    private  String cMoDocNo;
    /**
     * 计划日期
     */
    private Date dPlanDate;
    private  Long iDepartmentId;
    /**
     * 计划数量
     */
    private BigDecimal iQty;
    /**
     * 完工数量
     */
    private  BigDecimal iCompQty;
    private  Integer iPersonNum;
    private  Long iDutyPersonId;
    /**
     * 状态
     */
    private  Integer iStatus;
    /**
     *
     */
    private  String iStatusName;
    /**
     * 存货编码
     */
    private String cInvCode;
    /**
     * 客户部番
     */
    private String cInvCode1;
    /**
     * 部品名称
     */
    private String cInvName1;
    /**
     * 部门
     */
    private String  cDepName;
    /**
     * 产线名称
     */
    private  String cWorkName;
    /**
     * 班次名称
     */
    private  String cWorkShiftName;

    public Long getiAutoId() {
        return iAutoId;
    }

    public void setiAutoId(Long iAutoId) {
        this.iAutoId = iAutoId;
    }

    public Long getiMoTaskId() {
        return iMoTaskId;
    }

    public void setiMoTaskId(Long iMoTaskId) {
        this.iMoTaskId = iMoTaskId;
    }

    public Long getiInventoryId() {
        return iInventoryId;
    }

    public void setiInventoryId(Long iInventoryId) {
        this.iInventoryId = iInventoryId;
    }

    public String getcInvAddCode() {
        return cInvAddCode;
    }

    public void setcInvAddCode(String cInvAddCode) {
        this.cInvAddCode = cInvAddCode;
    }

    public String getcMoDocNo() {
        return cMoDocNo;
    }

    public void setcMoDocNo(String cMoDocNo) {
        this.cMoDocNo = cMoDocNo;
    }

    public Date getdPlanDate() {
        return dPlanDate;
    }

    public void setdPlanDate(Date dPlanDate) {
        this.dPlanDate = dPlanDate;
    }

    public Long getiDepartmentId() {
        return iDepartmentId;
    }

    public void setiDepartmentId(Long iDepartmentId) {
        this.iDepartmentId = iDepartmentId;
    }

    public BigDecimal getiQty() {
        return iQty;
    }

    public void setiQty(BigDecimal iQty) {
        this.iQty = iQty;
    }

    public BigDecimal getiCompQty() {
        return iCompQty;
    }

    public void setiCompQty(BigDecimal iCompQty) {
        this.iCompQty = iCompQty;
    }

    public Integer getiPersonNum() {
        return iPersonNum;
    }

    public void setiPersonNum(Integer iPersonNum) {
        this.iPersonNum = iPersonNum;
    }

    public Long getiDutyPersonId() {
        return iDutyPersonId;
    }

    public void setiDutyPersonId(Long iDutyPersonId) {
        this.iDutyPersonId = iDutyPersonId;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public String getcInvCode() {
        return cInvCode;
    }

    public void setcInvCode(String cInvCode) {
        this.cInvCode = cInvCode;
    }

    public String getcInvCode1() {
        return cInvCode1;
    }

    public void setcInvCode1(String cInvCode1) {
        this.cInvCode1 = cInvCode1;
    }

    public String getcInvName1() {
        return cInvName1;
    }

    public void setcInvName1(String cInvName1) {
        this.cInvName1 = cInvName1;
    }

    public String getcDepName() {
        return cDepName;
    }

    public void setcDepName(String cDepName) {
        this.cDepName = cDepName;
    }

    public String getcWorkName() {
        return cWorkName;
    }

    public void setcWorkName(String cWorkName) {
        this.cWorkName = cWorkName;
    }

    public String getcWorkShiftName() {
        return cWorkShiftName;
    }

    public void setcWorkShiftName(String cWorkShiftName) {
        this.cWorkShiftName = cWorkShiftName;
    }

    public String getiStatusName() {
        return iStatusName;
    }

    public void setiStatusName(String iStatusName) {
        this.iStatusName = iStatusName;
    }
}
