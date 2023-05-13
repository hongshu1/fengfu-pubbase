package cn.rjtech.api.modoc;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetCoperationnameByModocIdVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetMoroutingsopByInventoryroutingconfigIdVo;
import cn.rjtech.model.momdata.Personswipelog;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @version 1.0
 * @Author XLL
 * @Create 2023/5/12 11:08
 * @Description 制造工单
 */
@ApiDoc
public class ModocApiController extends BaseApiController {

  @Inject
  private ModocApiService moDocApiService;


  /**
   * 根据制造工单id查询工序名称信息
   *
   * @param modocid 制造工单id
   */
  @ApiDoc(result = RcvDocGetCoperationnameByModocIdVo.class)
  @UnCheck
  public void getCoperationnameByModocId(@Para(value = "modocid") String modocid) {
    renderJBoltApiSuccessWithData(moDocApiService.getCoperationnameByModocId(modocid));
  }

  /**
   * 根据料品工艺档案配置ID查询工单工序作业指导书信息
   *
   * @param inventoryroutingconfigid 料品工艺档案配置ID
   */
  @ApiDoc(result = RcvDocGetMoroutingsopByInventoryroutingconfigIdVo.class)
  @UnCheck
  public void getMoroutingsopByInventoryroutingconfigId(@Para(value = "inventoryroutingconfigid") String inventoryroutingconfigid) {
    renderJBoltApiSuccessWithData(moDocApiService.getMoroutingsopByInventoryroutingconfigId(inventoryroutingconfigid));
  }

}
