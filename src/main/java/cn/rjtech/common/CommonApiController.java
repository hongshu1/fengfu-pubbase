package cn.rjtech.common;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.common.columsmap.ColumsmapService;
import com.jfinal.aop.Inject;

/**
 * @author Kephon
 */
public class CommonApiController extends BaseApiController {

    @Inject
    private ColumsmapService commonService;

    /**
     * 单据提交
     */
    @UnCheck
    public void vouchProcessSubmit() {
        renderJson(commonService.vouchProcessSubmit(getKv()));
    }

    /**
     * 动态单据提交
     */
    @UnCheck
    public void vouchProcessDynamicSubmit() {
        renderJson(commonService.vouchProcessDynamicSubmit(getKv()));
    }

}
