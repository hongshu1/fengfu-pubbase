package cn.rjtech.api.nfcswipecard;

import cn.rjtech.base.controller.BaseApiController;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.Map;

/**
 * @author LiangLiangXie
 */
public class NfcSwipeCardApiController extends BaseApiController {

  @Inject
  private NfcSwipeCardApiService nfcSwipeCardApiService;

  /**
   * 产线刷卡
   */
  public void nfcswipecard() {
    renderJBoltApiSuccessWithData(nfcSwipeCardApiService.nfcswipecard(getKv()));
  }

}
