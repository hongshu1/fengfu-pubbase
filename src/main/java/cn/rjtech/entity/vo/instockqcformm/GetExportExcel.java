package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/8 15:56
 * @Description 导出数据详情
 */
public class GetExportExcel implements Serializable {

    /*每页显示的数据*/
    private List<Page>      pages;
    /*sheet名称*/
    private List<SheetName> sheetNames;

    public static class Page {

        private String  sheetName;
        private String  currentPage;
        private String  totalPage;
        /*主表*/
        private Master  master;
        /*详细表*/
        private Details details;

        public static class Master {

            /*现品票（条码）*/
            private String  cbarcode;
            /*批次号*/
            private String  cbatchno;
            /**/
            private String  ccreatename;
            /*设变号*/
            private String  cdcno;
            /*在库检检验单号*/
            private String  cinvqcformno;
            /*来料检检验单号*/
            private String  crcvdocqcformno;
            /*出库检检检验单号*/
            private String  cstockoutqcformno;
            /*定期检查,初物检查,委托测定,特别检查*/
            private String  cmeasurepurpose;
            /*测定理由*/
            private String  cmeasurereason;
            /*测定单位*/
            private String  cmeasureunit;
            /*备注*/
            private String  cmemo;
            /**/
            private String  corgcode;
            private String  corgname;
            private String  cupdatename;
            private String  iscompleted;
            private String  iscpksigned;
            private String  isdeleted;
            private String  isok;
            private Date    dcreatetime;
            /*检验时间*/
            private Date    dqcdate;
            private Date    dupdatetime;
            /*主键*/
            private Long    iautoid;
            private Long    icreateby;
            /*存货id*/
            private Long    iinventoryid;
            private Long    iorgid;
            /*质检表格id*/
            private Long    iqcformid;
            /*检验员Id, 系统用户ID*/
            private Long    iqcuserid;
            private Long    iupdateby;
            /*数量*/
            private Integer iqty;
            /*检验结果：0. 未生成 1. 待检 2. 不合格 3. 合格*/
            private Integer istatus;
            /*检验图片*/
            private String imageList;

            public String getImageList() {
                return imageList;
            }

            public void setImageList(String imageList) {
                this.imageList = imageList;
            }

            public String getCbarcode() {
                return cbarcode;
            }

            public void setCbarcode(String cbarcode) {
                this.cbarcode = cbarcode;
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

            public String getCinvqcformno() {
                return cinvqcformno;
            }

            public void setCinvqcformno(String cinvqcformno) {
                this.cinvqcformno = cinvqcformno;
            }

            public String getCrcvdocqcformno() {
                return crcvdocqcformno;
            }

            public void setCrcvdocqcformno(String crcvdocqcformno) {
                this.crcvdocqcformno = crcvdocqcformno;
            }

            public String getCstockoutqcformno() {
                return cstockoutqcformno;
            }

            public void setCstockoutqcformno(String cstockoutqcformno) {
                this.cstockoutqcformno = cstockoutqcformno;
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

            public String getCupdatename() {
                return cupdatename;
            }

            public void setCupdatename(String cupdatename) {
                this.cupdatename = cupdatename;
            }

            public String getIscompleted() {
                return iscompleted;
            }

            public void setIscompleted(String iscompleted) {
                this.iscompleted = iscompleted;
            }

            public String getIscpksigned() {
                return iscpksigned;
            }

            public void setIscpksigned(String iscpksigned) {
                this.iscpksigned = iscpksigned;
            }

            public String getIsdeleted() {
                return isdeleted;
            }

            public void setIsdeleted(String isdeleted) {
                this.isdeleted = isdeleted;
            }

            public String getIsok() {
                return isok;
            }

            public void setIsok(String isok) {
                this.isok = isok;
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

            public Long getIinventoryid() {
                return iinventoryid;
            }

            public void setIinventoryid(Long iinventoryid) {
                this.iinventoryid = iinventoryid;
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

            public Long getIupdateby() {
                return iupdateby;
            }

            public void setIupdateby(Long iupdateby) {
                this.iupdateby = iupdateby;
            }

            public Integer getIqty() {
                return iqty;
            }

            public void setIqty(Integer iqty) {
                this.iqty = iqty;
            }

            public Integer getIstatus() {
                return istatus;
            }

            public void setIstatus(Integer istatus) {
                this.istatus = istatus;
            }
        }

        public static class Details {

            /*检验项目名*/
            private String       cqcitemname;
            /*检验参数名*/
            private String       cqcparamname;
            /*最大值*/
            private String       imaxval;
            /*最小值*/
            private String       iminval;
            /*平均值*/
            private String       istdval;
            /*传给规格公差的值*/
            private String       options;
            /*类型*/
            private Integer      itype;
            /*序号*/
            private Integer      seq;
            /*每列的值*/
            private List<String> cvaluelist;

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

            public String getImaxval() {
                return imaxval;
            }

            public void setImaxval(String imaxval) {
                this.imaxval = imaxval;
            }

            public String getIminval() {
                return iminval;
            }

            public void setIminval(String iminval) {
                this.iminval = iminval;
            }

            public String getIstdval() {
                return istdval;
            }

            public void setIstdval(String istdval) {
                this.istdval = istdval;
            }

            public String getOptions() {
                return options;
            }

            public void setOptions(String options) {
                this.options = options;
            }

            public Integer getItype() {
                return itype;
            }

            public void setItype(Integer itype) {
                this.itype = itype;
            }

            public Integer getSeq() {
                return seq;
            }

            public void setSeq(Integer seq) {
                this.seq = seq;
            }

            public List<String> getCvaluelist() {
                return cvaluelist;
            }

            public void setCvaluelist(List<String> cvaluelist) {
                this.cvaluelist = cvaluelist;
            }
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public Master getMaster() {
            return master;
        }

        public void setMaster(Master master) {
            this.master = master;
        }

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }
    }

    public static class SheetName {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public List<SheetName> getSheetNames() {
        return sheetNames;
    }

    public void setSheetNames(List<SheetName> sheetNames) {
        this.sheetNames = sheetNames;
    }
}
