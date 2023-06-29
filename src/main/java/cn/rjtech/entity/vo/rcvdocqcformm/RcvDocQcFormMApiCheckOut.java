package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/28 9:20
 * @Description 点击检验按钮，跳转到检验页面
 */
public class RcvDocQcFormMApiCheckOut implements Serializable {

    /*批次号*/
    private String              cbatchno;
    private String              ccreatename;
    /*设变号*/
    private String              cdcno;
    /*机型*/
    private String              cequipmentname;
    /*存货编码*/
    private String              cinvaddcode;
    /*客户部番*/
    private String              cinvcode1;
    /*部品名称*/
    private String              cinvname1;
    /*规格*/
    private String              cinvstd;
    /*测定目的*/
    private String              cmeasurepurpose;
    /*测定原因*/
    private String              cmeasurereason;
    /*测定单位*/
    private String              cmeasureunit;
    private String              cmemo;
    private String              corgcode;
    private String              corgname;
    /*检验单号*/
    private String              crcvdocqcformno;
    private String              cupdatename;
    /*是否合格*/
    private String              isok;
    private String              isdeleted;
    private String              iscpksigned;
    private String              iscompleted;
    /*供应商名称*/
    private String              cvenname;
    private Long                iautoid;
    private Long                icreateby;
    private Long                icustomerid;
    /*存货id*/
    private Long                iinventoryid;
    private Long                iinventoryuomid1;
    private Long                iorgid;
    /*质检表格id*/
    private Long                iqcformid;
    /*检验员Id, 系统用户ID*/
    private Long                iqcuserid;
    private Long                iqty;
    private Long                iupdateby;
    /*收料单ID*/
    private Long                ircvdocid;
    /*供应商档案主表id，bd_vendor*/
    private Long                ivendorid;
    /*收料单号*/
    private String              cRcvDocNo;
    private Integer             istatus;
    /*标记：1. 正常 2. 初物*/
    private Integer             imask;
    private Date                dcreatetime;
    private Date                dqcdate;
    private Date                dupdatetime;
    private Date                drcvdate;
    /*检测日期*/
    private Date                dqctime;
    /*在库检-检验单号*/
    private String cInvQcFormNo;
    /*出货检-检验单号*/
    private String cstockoutqcformno;
    /*table的头部*/
    public  List<tableHeadData> columns;

    public  List<RcvDocQcFormD> docparamlist;

    public static class tableHeadData {

        /*项目名*/
        public String        cqcitemname;
        public String        iautoid;
        public String        iqcformid;
        /*检验项目id*/
        public String        iqcitemid;
        public String        isdeleted;
        /*次序*/
        public String        iseq;
        /*在质量建模-质量表格设置页面下，点击编辑按钮，最后一个table的数据*/
        public List<compare> compares;

        public static class compare {

            public String  ccreatename;
            public String  corgcode;
            public String  corgname;
            public String  cqcitemcode;
            public String  cqcitemnames;
            public String  cqcparamname;
            public String  cqcparamnames;
            public String  cupdatename;
            public String  isdeleted;
            public String  isenabled;
            public Date    dcreatetime;
            public Date    dupdatetime;
            public Long    iautoid;
            public Long    icreateby;
            public Long    iorgid;
            public Long    iqcitemid;
            public Long    iqcparamid;
            public Long    iupdateby;
            public Integer iitemparamseq;
            public Integer iitemseq;

            public String getCcreatename() {
                return ccreatename;
            }

            public void setCcreatename(String ccreatename) {
                this.ccreatename = ccreatename;
            }

            public String getCorgcode() {
                return corgcode;
            }

            public void setCorgcode(String corgcode) {
                this.corgcode = corgcode;
            }

            public String getCorgname() {
                return corgname;
            }

            public void setCorgname(String corgname) {
                this.corgname = corgname;
            }

            public String getCqcitemcode() {
                return cqcitemcode;
            }

            public void setCqcitemcode(String cqcitemcode) {
                this.cqcitemcode = cqcitemcode;
            }

            public String getCqcitemnames() {
                return cqcitemnames;
            }

            public void setCqcitemnames(String cqcitemnames) {
                this.cqcitemnames = cqcitemnames;
            }

            public String getCqcparamname() {
                return cqcparamname;
            }

            public void setCqcparamname(String cqcparamname) {
                this.cqcparamname = cqcparamname;
            }

            public String getCqcparamnames() {
                return cqcparamnames;
            }

            public void setCqcparamnames(String cqcparamnames) {
                this.cqcparamnames = cqcparamnames;
            }

            public String getCupdatename() {
                return cupdatename;
            }

            public void setCupdatename(String cupdatename) {
                this.cupdatename = cupdatename;
            }

            public String getIsdeleted() {
                return isdeleted;
            }

            public void setIsdeleted(String isdeleted) {
                this.isdeleted = isdeleted;
            }

            public String getIsenabled() {
                return isenabled;
            }

            public void setIsenabled(String isenabled) {
                this.isenabled = isenabled;
            }

            public Date getDcreatetime() {
                return dcreatetime;
            }

            public void setDcreatetime(Date dcreatetime) {
                this.dcreatetime = dcreatetime;
            }

            public Date getDupdatetime() {
                return dupdatetime;
            }

            public void setDupdatetime(Date dupdatetime) {
                this.dupdatetime = dupdatetime;
            }

            public Long getIautoid() {
                return iautoid;
            }

            public void setIautoid(Long iautoid) {
                this.iautoid = iautoid;
            }

            public Long getIcreateby() {
                return icreateby;
            }

            public void setIcreateby(Long icreateby) {
                this.icreateby = icreateby;
            }

            public Long getIorgid() {
                return iorgid;
            }

            public void setIorgid(Long iorgid) {
                this.iorgid = iorgid;
            }

            public Long getIqcitemid() {
                return iqcitemid;
            }

            public void setIqcitemid(Long iqcitemid) {
                this.iqcitemid = iqcitemid;
            }

            public Long getIqcparamid() {
                return iqcparamid;
            }

            public void setIqcparamid(Long iqcparamid) {
                this.iqcparamid = iqcparamid;
            }

            public Long getIupdateby() {
                return iupdateby;
            }

            public void setIupdateby(Long iupdateby) {
                this.iupdateby = iupdateby;
            }

            public Integer getIitemparamseq() {
                return iitemparamseq;
            }

            public void setIitemparamseq(Integer iitemparamseq) {
                this.iitemparamseq = iitemparamseq;
            }

            public Integer getIitemseq() {
                return iitemseq;
            }

            public void setIitemseq(Integer iitemseq) {
                this.iitemseq = iitemseq;
            }
        }

        public String getCqcitemname() {
            return cqcitemname;
        }

        public void setCqcitemname(String cqcitemname) {
            this.cqcitemname = cqcitemname;
        }

        public String getIautoid() {
            return iautoid;
        }

        public void setIautoid(String iautoid) {
            this.iautoid = iautoid;
        }

        public String getIqcformid() {
            return iqcformid;
        }

        public void setIqcformid(String iqcformid) {
            this.iqcformid = iqcformid;
        }

        public String getIqcitemid() {
            return iqcitemid;
        }

        public void setIqcitemid(String iqcitemid) {
            this.iqcitemid = iqcitemid;
        }

        public String getIsdeleted() {
            return isdeleted;
        }

        public void setIsdeleted(String isdeleted) {
            this.isdeleted = isdeleted;
        }

        public String getIseq() {
            return iseq;
        }

        public void setIseq(String iseq) {
            this.iseq = iseq;
        }

        public List<compare> getCompares() {
            return compares;
        }

        public void setCompares(List<compare> compares) {
            this.compares = compares;
        }
    }

    public static class RcvDocQcFormD{
        public String iAutoId;
        /**来料检ID*/
        public String iRcvDocQcFormMid;
        /**检验表格ID*/
        public String iQcFormId;
        /**检验项目ID，Bd_QcFormTableParam.iAutoId*/
        public String iFormParamId;
        /**项目次序，固定取参数项目名为“项目”的参数名称次序值*/
        public  String iSeq;
        /**子次序*/
        public String iSubSeq;
        /**检验参数值ID，点检方法允许为空（拼接“-”）多个逗号分隔*/
        public String cQcFormParamIds;
        /**参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间*/
        public String iType;
        /**标准值*/
        public String iStdVal;
        /**最大设定值*/
        public String iMaxVal;
        /**最小设定值*/
        public String iMinVal;
        /**列表可选值，多个";"分隔*/
        public String cOptions;

        public String getiAutoId() {
            return iAutoId;
        }

        public void setiAutoId(String iAutoId) {
            this.iAutoId = iAutoId;
        }

        public String getiRcvDocQcFormMid() {
            return iRcvDocQcFormMid;
        }

        public void setiRcvDocQcFormMid(String iRcvDocQcFormMid) {
            this.iRcvDocQcFormMid = iRcvDocQcFormMid;
        }

        public String getiQcFormId() {
            return iQcFormId;
        }

        public void setiQcFormId(String iQcFormId) {
            this.iQcFormId = iQcFormId;
        }

        public String getiFormParamId() {
            return iFormParamId;
        }

        public void setiFormParamId(String iFormParamId) {
            this.iFormParamId = iFormParamId;
        }

        public String getiSeq() {
            return iSeq;
        }

        public void setiSeq(String iSeq) {
            this.iSeq = iSeq;
        }

        public String getiSubSeq() {
            return iSubSeq;
        }

        public void setiSubSeq(String iSubSeq) {
            this.iSubSeq = iSubSeq;
        }

        public String getcQcFormParamIds() {
            return cQcFormParamIds;
        }

        public void setcQcFormParamIds(String cQcFormParamIds) {
            this.cQcFormParamIds = cQcFormParamIds;
        }

        public String getiType() {
            return iType;
        }

        public void setiType(String iType) {
            this.iType = iType;
        }

        public String getiStdVal() {
            return iStdVal;
        }

        public void setiStdVal(String iStdVal) {
            this.iStdVal = iStdVal;
        }

        public String getiMaxVal() {
            return iMaxVal;
        }

        public void setiMaxVal(String iMaxVal) {
            this.iMaxVal = iMaxVal;
        }

        public String getiMinVal() {
            return iMinVal;
        }

        public void setiMinVal(String iMinVal) {
            this.iMinVal = iMinVal;
        }

        public String getcOptions() {
            return cOptions;
        }

        public void setcOptions(String cOptions) {
            this.cOptions = cOptions;
        }
    }

    public String getCstockoutqcformno() {
        return cstockoutqcformno;
    }

    public void setCstockoutqcformno(String cstockoutqcformno) {
        this.cstockoutqcformno = cstockoutqcformno;
    }

    public String getcInvQcFormNo() {
        return cInvQcFormNo;
    }

    public void setcInvQcFormNo(String cInvQcFormNo) {
        this.cInvQcFormNo = cInvQcFormNo;
    }

    public List<RcvDocQcFormD> getDocparamlist() {
        return docparamlist;
    }

    public void setDocparamlist(List<RcvDocQcFormD> docparamlist) {
        this.docparamlist = docparamlist;
    }

    public String getCbatchno() {
        return cbatchno;
    }

    public void setCbatchno(String cbatchno) {
        this.cbatchno = cbatchno;
    }

    public String getCcreatename() {
        return ccreatename;
    }

    public void setCcreatename(String ccreatename) {
        this.ccreatename = ccreatename;
    }

    public String getCdcno() {
        return cdcno;
    }

    public void setCdcno(String cdcno) {
        this.cdcno = cdcno;
    }

    public String getCequipmentname() {
        return cequipmentname;
    }

    public void setCequipmentname(String cequipmentname) {
        this.cequipmentname = cequipmentname;
    }

    public String getCinvaddcode() {
        return cinvaddcode;
    }

    public void setCinvaddcode(String cinvaddcode) {
        this.cinvaddcode = cinvaddcode;
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

    public String getCinvstd() {
        return cinvstd;
    }

    public void setCinvstd(String cinvstd) {
        this.cinvstd = cinvstd;
    }

    public String getCmeasurepurpose() {
        return cmeasurepurpose;
    }

    public void setCmeasurepurpose(String cmeasurepurpose) {
        this.cmeasurepurpose = cmeasurepurpose;
    }

    public String getCmeasurereason() {
        return cmeasurereason;
    }

    public void setCmeasurereason(String cmeasurereason) {
        this.cmeasurereason = cmeasurereason;
    }

    public String getCmeasureunit() {
        return cmeasureunit;
    }

    public void setCmeasureunit(String cmeasureunit) {
        this.cmeasureunit = cmeasureunit;
    }

    public String getCmemo() {
        return cmemo;
    }

    public void setCmemo(String cmemo) {
        this.cmemo = cmemo;
    }

    public String getCorgcode() {
        return corgcode;
    }

    public void setCorgcode(String corgcode) {
        this.corgcode = corgcode;
    }

    public String getCorgname() {
        return corgname;
    }

    public void setCorgname(String corgname) {
        this.corgname = corgname;
    }

    public String getCrcvdocqcformno() {
        return crcvdocqcformno;
    }

    public void setCrcvdocqcformno(String crcvdocqcformno) {
        this.crcvdocqcformno = crcvdocqcformno;
    }

    public String getCupdatename() {
        return cupdatename;
    }

    public void setCupdatename(String cupdatename) {
        this.cupdatename = cupdatename;
    }

    public String getIsok() {
        return isok;
    }

    public void setIsok(String isok) {
        this.isok = isok;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getIscpksigned() {
        return iscpksigned;
    }

    public void setIscpksigned(String iscpksigned) {
        this.iscpksigned = iscpksigned;
    }

    public String getIscompleted() {
        return iscompleted;
    }

    public void setIscompleted(String iscompleted) {
        this.iscompleted = iscompleted;
    }

    public String getCvenname() {
        return cvenname;
    }

    public void setCvenname(String cvenname) {
        this.cvenname = cvenname;
    }

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public Long getIcreateby() {
        return icreateby;
    }

    public void setIcreateby(Long icreateby) {
        this.icreateby = icreateby;
    }

    public Long getIcustomerid() {
        return icustomerid;
    }

    public void setIcustomerid(Long icustomerid) {
        this.icustomerid = icustomerid;
    }

    public Long getIinventoryid() {
        return iinventoryid;
    }

    public void setIinventoryid(Long iinventoryid) {
        this.iinventoryid = iinventoryid;
    }

    public Long getIinventoryuomid1() {
        return iinventoryuomid1;
    }

    public void setIinventoryuomid1(Long iinventoryuomid1) {
        this.iinventoryuomid1 = iinventoryuomid1;
    }

    public Long getIorgid() {
        return iorgid;
    }

    public void setIorgid(Long iorgid) {
        this.iorgid = iorgid;
    }

    public Long getIqcformid() {
        return iqcformid;
    }

    public void setIqcformid(Long iqcformid) {
        this.iqcformid = iqcformid;
    }

    public Long getIqcuserid() {
        return iqcuserid;
    }

    public void setIqcuserid(Long iqcuserid) {
        this.iqcuserid = iqcuserid;
    }

    public Long getIqty() {
        return iqty;
    }

    public void setIqty(Long iqty) {
        this.iqty = iqty;
    }

    public Long getIupdateby() {
        return iupdateby;
    }

    public void setIupdateby(Long iupdateby) {
        this.iupdateby = iupdateby;
    }

    public Long getIrcvdocid() {
        return ircvdocid;
    }

    public void setIrcvdocid(Long ircvdocid) {
        this.ircvdocid = ircvdocid;
    }

    public Long getIvendorid() {
        return ivendorid;
    }

    public void setIvendorid(Long ivendorid) {
        this.ivendorid = ivendorid;
    }

    public String getcRcvDocNo() {
        return cRcvDocNo;
    }

    public void setcRcvDocNo(String cRcvDocNo) {
        this.cRcvDocNo = cRcvDocNo;
    }

    public Integer getIstatus() {
        return istatus;
    }

    public void setIstatus(Integer istatus) {
        this.istatus = istatus;
    }

    public Integer getImask() {
        return imask;
    }

    public void setImask(Integer imask) {
        this.imask = imask;
    }

    public Date getDcreatetime() {
        return dcreatetime;
    }

    public void setDcreatetime(Date dcreatetime) {
        this.dcreatetime = dcreatetime;
    }

    public Date getDqcdate() {
        return dqcdate;
    }

    public void setDqcdate(Date dqcdate) {
        this.dqcdate = dqcdate;
    }

    public Date getDupdatetime() {
        return dupdatetime;
    }

    public void setDupdatetime(Date dupdatetime) {
        this.dupdatetime = dupdatetime;
    }

    public Date getDrcvdate() {
        return drcvdate;
    }

    public void setDrcvdate(Date drcvdate) {
        this.drcvdate = drcvdate;
    }

    public Date getDqctime() {
        return dqctime;
    }

    public void setDqctime(Date dqctime) {
        this.dqctime = dqctime;
    }

    public List<tableHeadData> getColumns() {
        return columns;
    }

    public void setColumns(List<tableHeadData> columns) {
        this.columns = columns;
    }
}
