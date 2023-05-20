package cn.rjtech.api.nfcswipecard;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocNfcswipecardVo;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @version 1.0
 * @Author XLL
 * @Create 2023/5/5 14:00
 * @Description 产线刷卡接口
 */
@ApiDoc
public class NfcSwipeCardApiController extends BaseApiController {

  @Inject
  private NfcSwipeCardApiService nfcSwipeCardApiService;


  /**
   * 产线刷卡
   *
   * @param deviceaddress 设备标识(mac地址)
   * @param cardusercode  刷卡用户账号
   * @param cardcode      卡号
   * @param cpadname      平板名称
   */
  @ApiDoc(result = RcvDocNfcswipecardVo.class)
  @UnCheck
  public void nfcswipecard(@Para(value = "deviceaddress") String deviceaddress,
                           @Para(value = "cardusercode") String cardusercode,
                           @Para(value = "cardcode") String cardcode,
                           @Para(value = "cpadname") String cpadname) {
    renderJBoltApiSuccessWithData(nfcSwipeCardApiService.nfcswipecard(deviceaddress, cardusercode, cardcode, cpadname));
  }


}
