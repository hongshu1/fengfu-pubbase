package cn.rjtech.api.momaterialsreturnm;

import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.momaterialsreturnm.MoMaterialsreturnmService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 生产退料
 *
 * @author Kephon
 */
@ApiDoc
public class MoMaterialsreturnmApiController extends BaseApiController {

    @Inject
    private MoMaterialsreturnmApiService moMaterialsreturnmApiService;


    /**
     * 查询单条生产退料现品票
     * @param barcode  现品票
     */
    @UnCheck
    public void addBarcode(@Para(value = "barcode") String barcode){
        ValidationUtils.notNull(barcode,"缺少现品票");
        renderJBoltApiRet(moMaterialsreturnmApiService.getBycBarcodeInfo(barcode));
    }


    /**
     * 查询全部生产退料现品票
     */
    @UnCheck
    public void getmomaterialscanusedlogList(){
        renderJBoltApiRet(moMaterialsreturnmApiService.getBycBarcodeList());
    }

    /**
     * 保存生产退料
     */
    @UnCheck
    public void saveTableSubmit(@Para(value ="jboltTable" )JBoltTable jBoltTable){
        renderJBoltApiRet(moMaterialsreturnmApiService.saveTableSubmit(jBoltTable));
    }

}
