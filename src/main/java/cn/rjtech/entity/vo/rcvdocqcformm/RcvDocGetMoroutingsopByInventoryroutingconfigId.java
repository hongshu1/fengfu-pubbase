package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;

public class RcvDocGetMoroutingsopByInventoryroutingconfigId implements Serializable {
  /**
   * 文件名称
   */
  private String cname;
  /**
   * 文件路径
   */
  private String cpath;
  /**
   * 文件类型
   */
  private String csuffix;
  /**
   * 文件版本号
   */
  private String iversion;
  /**
   * 文件启用时间
   */
  private String dfromdate;
  /**
   * 文件失效时间
   */
  private String dtodate;

  public String getCname() {
    return cname;
  }

  public void setCname(String cname) {
    this.cname = cname;
  }

  public String getCpath() {
    return cpath;
  }

  public void setCpath(String cpath) {
    this.cpath = cpath;
  }

  public String getCsuffix() {
    return csuffix;
  }

  public void setCsuffix(String csuffix) {
    this.csuffix = csuffix;
  }

  public String getIversion() {
    return iversion;
  }

  public void setIversion(String iversion) {
    this.iversion = iversion;
  }

  public String getDfromdate() {
    return dfromdate;
  }

  public void setDfromdate(String dfromdate) {
    this.dfromdate = dfromdate;
  }

  public String getDtodate() {
    return dtodate;
  }

  public void setDtodate(String dtodate) {
    this.dtodate = dtodate;
  }
}
