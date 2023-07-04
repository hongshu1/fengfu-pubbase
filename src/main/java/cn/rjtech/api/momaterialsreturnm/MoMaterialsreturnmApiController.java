package cn.rjtech.api.momaterialsreturnm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.momaterialsreturnm.MoMaterialsreturnmService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.momaterialsreturnm.MomaterialsreturnmVo;
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
     * 查询单条生产退料现品票
     *
     * @param barcode 现品票
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @UnCheck
    public void addBarcode(@Para(value = "barcode") String barcode) {
        ValidationUtils.notNull(barcode, "缺少现品票");

        renderJBoltApiRet(moMaterialsreturnmApiService.getBycBarcodeInfo(barcode));
    }

    /**
     * 查询全部生产退料现品票
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @UnCheck
    public void getmomaterialscanusedlogList() {
        renderJBoltApiRet(moMaterialsreturnmApiService.getBycBarcodeList());
    }

    /**
     * 保存生产退料
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @CheckPermission(PermissionKey.API_MOMATERIALSRETURNM)
    public void saveTableSubmit(@Para(value = "jboltTable") JBoltTable jBoltTable) {
        ValidationUtils.isTrue(moMaterialsreturnmService.saveTableSubmit(jBoltTable).isOk(), "保存失败");
        renderJBoltApiSuccess();
    }

    /**
     * 查看生产退料详情
     */
    @ApiDoc(result = MomaterialsreturnmVo.class)
    @UnCheck
    public void getmomaterialscanuseMList(@Para(value = "iautoid") String iautoid) {
        ValidationUtils.notNull(iautoid, "缺少退料表单ID");
        renderJBoltApiRet(moMaterialsreturnmApiService.getModandMomlist(iautoid));
    }

}
