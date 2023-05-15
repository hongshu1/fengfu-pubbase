package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author xll
 * @Create 2023/5/13 13:39
 * @Description 产线刷卡查询创建数据
 */
public class RcvDocNfcswipecard implements Serializable {
  /**
   * 产线编码
   */
  private String cworkcode;
  /**
   * 产线名称
   */
  private String cworkname;
  /**
   * 刷卡用户账号
   */
  private String cardcode;

  /**
   * 刷卡用户名
   */
  private String cpsnname;
  /**
   * 刷卡时间
   */
  private String datetoday;
  //产线刷卡结果
  private String result;

  public String getCworkcode() {
    return cworkcode;
  }

  public void setCworkcode(String cworkcode) {
    this.cworkcode = cworkcode;
  }

  public String getCworkname() {
    return cworkname;
  }

  public void setCworkname(String cworkname) {
    this.cworkname = cworkname;
  }

  public String getCardcode() {
    return cardcode;
  }

  public void setCardcode(String cardcode) {
    this.cardcode = cardcode;
  }

  public String getCpsnname() {
    return cpsnname;
  }

  public void setCpsnname(String cpsnname) {
    this.cpsnname = cpsnname;
  }

  public String getDatetoday() {
    return datetoday;
  }

  public void setDatetoday(String datetoday) {
    this.datetoday = datetoday;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

}
