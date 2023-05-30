package cn.rjtech.api.momaterialsscansum;

import cn.jbolt.core.api.OpenAPI;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
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
    private  MoMaterialsscansumApiService moMaterialsscansumApiService;

    /**
     *
     * @param barcoce
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    @OpenAPI
    public void addBarcode(@Para(value = "barcode") String  barcoce){
        renderJBoltApiRet(moMaterialsscansumApiService.add(barcoce));
    }

}
