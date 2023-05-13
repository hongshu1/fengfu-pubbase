package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;

public class RcvDocGetCoperationnameByModocId implements Serializable {
  /**
   * 存货工艺配置id
   */
  private String iautoid;
  /**
   * 工序名称
   */
  private String coperationname;

  public String getIautoid() {
    return iautoid;
  }

  public void setIautoid(String iautoid) {
    this.iautoid = iautoid;
  }

  public String getCoperationname() {
    return coperationname;
  }

  public void setCoperationname(String coperationname) {
    this.coperationname = coperationname;
  }
}
