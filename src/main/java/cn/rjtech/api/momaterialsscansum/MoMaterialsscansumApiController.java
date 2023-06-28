package cn.rjtech.api.momaterialsscansum;

import cn.jbolt.core.api.OpenAPI;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @ClassName :
 * @Description : *齐料检查
 * @Author : dongjunjun
 * @Date: 2023-05-24
 */
@ApiDoc
public class MoMaterialsscansumApiController extends BaseApiController {

    @Inject
    private MoMaterialsscansumApiService moMaterialsscansumApiService;

    /**
     * @param barcoce
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    @OpenAPI
    public void addBarcode(@Para(value = "barcode") String barcoce,
                           @Para(value = "imodocid") Long imodocid) {
        renderJBoltApiRet(moMaterialsscansumApiService.add(barcoce, imodocid));
    }

    /**
     * 获取备料现品票所有数据
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void getBarcodeAll(@Para(value = "pageNumber") Integer pageNumber,
                              @Para(value = "pageSize") Integer pageSize,
                              @Para(value = "imodocid") Long imodocid) {
        renderJBoltApiRet(moMaterialsscansumApiService.getBarcodeAll(pageNumber, pageSize, imodocid));
    }

    /**
     * 获取备料现品票明细（未扫描/已扫描）
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void getMoMaterialNotScanLogList(@Para(value = "pageNumber") Integer pageNumber,
                                            @Para(value = "pageSize") Integer pageSize,
                                            @Para(value = "imodocid") Long imodocid,
                                            @Para(value = "isScanned") Integer isScanned) {
        ValidationUtils.validatePageNumber(pageNumber);
        ValidationUtils.validatePageSize(pageSize);
        ValidationUtils.validateId(imodocid, "订单ID");
        ValidationUtils.notNull(isScanned, "缺少是否已扫描参数");

        renderJBoltApiRet(moMaterialsscansumApiService.getMoMaterialNotScanLogList(pageNumber, pageSize, imodocid, isScanned));
    }

}
