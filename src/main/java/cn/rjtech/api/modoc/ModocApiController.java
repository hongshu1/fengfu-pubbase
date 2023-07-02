package cn.rjtech.api.modoc;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.momaterialscanusedlog.MoMaterialscanusedlogmService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.modoc.ModocApiPageVo;
import cn.rjtech.entity.vo.modoc.ModocApiResVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetCoperationnameByModocIdVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetMoroutingsopByInventoryroutingconfigIdVo;
import cn.rjtech.model.momdata.MoMaterialscanusedlogm;
import cn.rjtech.model.momdata.Personswipelog;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.sun.jna.platform.win32.WinDef;
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
    @Inject
    private SpecMaterialsRcvMService specMaterialsRcvMService;
    @Inject
    private MoMaterialscanusedlogmService moMaterialscanusedlogmService;

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
     * *制造工单查询API
     *
     * @param page           页数 必要参数
     * @param pageSize       条数 必要参数
     * @param cmodocno       工单号
     * @param cinvcode       存货编码
     * @param cinvcode1      客户布番
     * @param cinvname1      部品名称
     * @param cdepname       生产部门
     * @param iworkregionmid 产线ID 必要参数
     * @param status         状态
     * @param starttime      开始日期
     * @param endtime        截止日期
     */
    @ApiDoc(result = ModocApiPageVo.class)
    @UnCheck
    public void page(@Para(value = "page") Integer page,
                     @Para(value = "pageSize") Integer pageSize,
                     @Para(value = "cmodocno") String cmodocno,
                     @Para(value = "cinvcode") String cinvcode,
                     @Para(value = "cinvcode1") String cinvcode1,
                     @Para(value = "cinvname1") String cinvname1,
                     @Para(value = "cdepname") String cdepname,
                     @Para(value = "iworkregionmid") Long iworkregionmid,
                     @Para(value = "status") Integer status,
                     @Para(value = "starttime") Date starttime,
                     @Para(value = "endtime") Date endtime) {
        ValidationUtils.validateIdInt(page, "缺少页数");
        ValidationUtils.validateIdInt(pageSize, "缺少条数");
        // ValidationUtils.notNull(iworkregionmid,"缺少产线ID")

        renderJBoltApiRet(moDocApiService.page(page, pageSize, cmodocno, cinvcode, cinvcode1, cinvname1, cdepname, iworkregionmid, status, starttime, endtime));
    }

    /**
     * 获取工单详情信息
     *
     * @param imodocid 工单ID
     */
    @ApiDoc(result = ModocApiResVo.class)
    @UnCheck
    public void getModocdetails(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.notNull(imodocid, "缺少工单ID");
        renderJBoltApiRet(moDocApiService.getModocdetails(imodocid));
    }

    /**
     * 根据制造工单id查询特殊领料数据源
     *
     * @param imodocid   制造工单id，必传
     * @param pageNumber 页数，必传
     * @param pageSize   页面数量，必传
     */
//  @ApiDoc(result = ModocApiResVo.class)
    @UnCheck
    public void getSpecmaterialsrcvmDatas(@Para(value = "imodocid") Long imodocid,
                                          @Para(value = "pageNumber") Integer pageNumber,
                                          @Para(value = "pageSize") Integer pageSize) {
        ValidationUtils.notNull(imodocid, "缺少工单ID");
        ValidationUtils.notNull(pageNumber, "缺少页数");
        ValidationUtils.notNull(pageSize, "缺少页面数量");

        renderJBoltApiSuccessWithData(moDocApiService.getSpecmaterialsrcvmDatas(imodocid, pageNumber, pageSize));
    }

    /**
     * 根据制造工单id查询当前工单的存货档案数据
     *
     * @param imodocid   制造工单id，必传
     * @param pageNumber 页数，必传
     * @param pageSize   页面数量，必传
     * @param cinvcode   存货编码
     * @param cinvcode1  客户部番
     * @param cinvname1  部品名称
     */
    @UnCheck
    public void getInventoryDatasByDocid(@Para(value = "imodocid") Long imodocid,
                                         @Para(value = "pageNumber") Integer pageNumber,
                                         @Para(value = "pageSize") Integer pageSize,
                                         @Para(value = "cinvcode") String cinvcode,
                                         @Para(value = "cinvcode1") String cinvcode1,
                                         @Para(value = "cinvname1") String cinvname1) {
        ValidationUtils.notNull(imodocid, "缺少工单ID");
        ValidationUtils.notNull(pageNumber, "缺少页数");
        ValidationUtils.notNull(pageSize, "缺少页面数量");

        renderJBoltApiSuccessWithData(moDocApiService.getInventoryDatasByDocid(imodocid, pageNumber, pageSize, cinvcode, cinvcode1, cinvname1));
    }

    /**
     * 特殊领料保存接口
     *
     * @param rcvm 特殊领料主表数据JSON
     * @param rcvd 特殊领料细表数据JSON
     */
    @UnCheck
    public void saveSpecMaterialsRcv(@Para(value = "rcvm") String rcvm,
                                     @Para(value = "rcvd") String rcvd,
                                     @Para(value = "type") String type,
                                     @Para(value = "id") Long id) {
        ValidationUtils.notNull(rcvm, "特殊领料主表数据不能为空");
        ValidationUtils.notNull(rcvd, "特殊领料细表数据不能为空");

        renderJBoltApiRet(specMaterialsRcvMService.saveSpecMaterialsRcv(rcvm, rcvd, type, id));
    }

    /**
     * 特殊领料删除接口
     *
     * @param iautoid 特殊领料主表ID
     */
    @UnCheck
    public void deleteSpecMaterialsRcv(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, "ID不能为空");

        renderJBoltApiRet(specMaterialsRcvMService.deleteSpecMaterialsRcv(iautoid));
    }

    /**
     * 根据特殊领料ID查询主细表数据
     *
     * @param iautoid 主表ID
     */
    @UnCheck
    public void getSpecMaterialsRcvDatas(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, "ID不能为空");
        renderJBoltApiSuccessWithData(specMaterialsRcvMService.getSpecMaterialsRcvDatas(iautoid));
    }

    /**
     * 根据特殊领料ID撤回
     *
     * @param iautoid 主表ID
     */
    @UnCheck
    public void revocationSpecMaterialsRcvMById(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, "ID不能为空");

        renderJBoltApiSuccessWithData(specMaterialsRcvMService.revocationSpecMaterialsRcvMById(iautoid));
    }

  /**
   *获取材料耗用为扫码数据
   * @param pageNumber
   * @param pageSize
   * @param imodocid
   * @param iautoids 传入逗号隔开的多个齐料检查主键id,用于扫码时排除扫码的那条数据，
   *                 由于未扫码数据没有表记录扫码状态，前端在扫码使用隐藏域记录id传入后端
   */
  @UnCheck
  public void getBarcodeAllBycBarcodeApi(@Para(value = "pageNumber") Integer pageNumber,
                                         @Para(value = "pageSize") Integer pageSize,
                                         @Para(value = "imodocid") Long imodocid,
                                         @Para(value = "iautoids") String iautoids){
      ValidationUtils.notNull(imodocid, "缺少工单ID");
      ValidationUtils.notNull(pageNumber, "缺少页数");
      ValidationUtils.notNull(pageSize, "缺少页面数量");
    renderJBoltApiSuccessWithData(moMaterialscanusedlogmService.getBarcodeAllBycBarcode(pageNumber, pageSize,Kv.by("imodocid",imodocid).set("iautoid",iautoids)));
  }

  /**
   * 获取材料耗用已扫描数据
   * @param imodocid 制造工单主表id
   * @param barcode 现品票
   */
  @UnCheck
  public void getMaterialScanUsedLogApi(@Para(value = "imodocid") Long imodocid,
                                        @Para(value = "barcode") String barcode){
      ValidationUtils.notNull(imodocid, "ID不能为空");
      ValidationUtils.notNull(barcode, "现品票不能为空");
    renderJBoltApiSuccessWithData(moMaterialscanusedlogmService.getMaterialScanUsedLog(Kv.by("imodocid",imodocid).set("barcode",barcode)));
  }

  /**
   * 新增材料耗用单，推送材料出库单
   * @param imodocid
   * @param jBoltTableList 详情数据
   */
  @UnCheck
  public void saveMoLogMApi(@Para(value = "imodocid")Long imodocid,
                            @Para(value = "jBoltTableList")String jBoltTableList){
      ValidationUtils.notNull(imodocid, "id不能为空");
      ValidationUtils.notNull(jBoltTableList, "data不能为空");
    renderJBoltApiSuccessWithData(moMaterialscanusedlogmService.saveMoLogMApi(imodocid, JSON.parseArray(jBoltTableList)));
  }
}
