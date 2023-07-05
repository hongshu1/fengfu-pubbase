package cn.rjtech.api.momaterialsreturnm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.momaterialsreturnm.MoMaterialsreturnmService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.momaterialsreturnm.MomaterialsreturnmVo;
import cn.rjtech.model.momdata.MoMaterialsreturnm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 生产退料
 *
 * @author Kephon
 */
@CheckPermission(PermissionKey.API_MOMATERIALSRETURNM)
@UnCheckIfSystemAdmin
@ApiDoc
public class MoMaterialsreturnmApiController extends BaseApiController {

    @Inject
    private MoMaterialsreturnmApiService moMaterialsreturnmApiService;

    @Inject
    private MoMaterialsreturnmService moMaterialsreturnmService;



    /**
     *搜索单条生产退料现品票
     * @param barcode 现品票
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @UnCheck
    public void getomaterialscanusedlogBarcode(@Para(value = "barcode") String barcode
                                               ) {
        ValidationUtils.notNull(barcode, "缺少现品票");

        renderJBoltApiRet(moMaterialsreturnmApiService.getBycBarcodeInfo(barcode));
    }

    /**
     * 查询全部生产退料现品票
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @UnCheck
    public void getmomaterialscanusedlogList(@Para(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                                             @Para(value = "pageSize",defaultValue = "15") Integer pageSize
                                             ) {
        renderJBoltApiRet(moMaterialsreturnmApiService.getBycBarcodeList(pageNumber,pageSize));
    }

    /**
     * 保存生产退料
     * @param SaveTableData 接收退料 JSON格式 [{"imodocid":"生产工单ID","cbarcode":"现品票",""cinvcode:"存货编码",
     *                      "cinvaddcode":"存货代码","cinvname":"存货名称","cinvstd":"规格型号","empty":"品牌",
     *                      "iuomclassid":"主计量单位","iqtys":"数量","cmome":"备注"}]
     * @param IMoDocId  生产工单ID
     *
     */
    @ApiDoc(result = NullDataResult.class)
    @CheckPermission(PermissionKey.API_MOMATERIALSRETURNM)
    public void saveTableSubmit(@Para(value = "SaveTableData") String SaveTableData,
                                @Para(value = "IMoDocId") Long IMoDocId)
                                                                        {
        ValidationUtils.notNull(SaveTableData, "缺少退料表单数据");
        ValidationUtils.notNull(IMoDocId, "缺少生产工单ID");
        MoMaterialsreturnm moMaterialsreturnm = new MoMaterialsreturnm();
        moMaterialsreturnm.setIMoDocId(IMoDocId);
        moMaterialsreturnmApiService.AddFromMoMaterialsreturnm(JBoltModelKit.getFromRecords(SaveTableData), moMaterialsreturnm);
        renderJBoltApiSuccess();
    }

    /**
     * 查看生产退料详情
     * @param iautoid 生产退料主表ID
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @UnCheck
    public void getmomaterialscanuseMList(@Para(value = "iautoid") String iautoid) {
        ValidationUtils.notNull(iautoid, "缺少退料表单ID");
        renderJBoltApiRet(moMaterialsreturnmApiService.getModandMomlist(iautoid));
    }

    /**
     * 生产退料主页
     * @param IMoDocId  生产工单ID
     * @param billno    调拨单号
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @UnCheck
    public void datas(@Para(value = "IMoDocId") Long IMoDocId,
                      @Para(value = "billno") String billno,
                      @Para(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                      @Para(value = "pageSize",defaultValue = "15") Integer pageSize){
        renderJBoltApiRet(moMaterialsreturnmApiService.aginateAdminDatas(pageNumber,pageSize,IMoDocId,billno));

    }

}
