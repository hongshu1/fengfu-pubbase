package cn.rjtech.api.modoc;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.modoc.ModocApiPageVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetCoperationnameByModocIdVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetMoroutingsopByInventoryroutingconfigIdVo;
import cn.rjtech.model.momdata.Personswipelog;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

import java.util.Date;

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

  /**
   *
   * @param page  页数 必要参数
   * @param pageSize 条数 必要参数
   * @param cmodocno 工单号
   * @param cinvcode 存货编码
   * @param cinvcode1 客户布番
   * @param cinvname1  部品名称
   * @param cdepname 生产部门
   * @param iworkregionmid 产线ID 必要参数
   * @param status 状态
   * @param starttime 开始日期
   * @param endtime 截止日期
   */
  @ApiDoc(result = ModocApiPageVo.class)
  @UnCheck
  public void page(@Para(value = "page") Integer page,
                   @Para(value = "pageSize") Integer pageSize,
                   @Para(value = "cmodocno") String cmodocno,
                   @Para(value = "cinvcode") String cinvcode,
                   @Para(value = "cinvcode") String cinvcode1,
                   @Para(value = "cinvname1") String cinvname1,
                   @Para(value = "cdepname") String cdepname,
                   @Para(value = "iworkregionmid") Long iworkregionmid,
                   @Para(value = "status") Integer status,
                   @Para(value = "starttime") Date starttime,
                   @Para(value = "endtime") Date endtime){
    ValidationUtils.validateIdInt(page,"缺少页数");
    ValidationUtils.validateIdInt(pageSize,"缺少条数");
   // ValidationUtils.notNull(iworkregionmid,"缺少产线ID");
    renderJBoltApiRet(moDocApiService.page(page,pageSize,cmodocno,cinvcode,cinvcode1,cinvname1,
            cdepname,iworkregionmid,status,starttime,endtime));
  }
}
