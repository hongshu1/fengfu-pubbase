package cn.rjtech.common;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.common.columsmap.ColumsmapService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

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
        Kv result = commonService.vouchProcessSubmit(getKv());
        if (ObjUtil.equals("200", result.getStr("code"))) {
            renderJBoltApiRet(JBoltApiRet.API_SUCCESS_WITH_DATA(result));
        } else {
            renderJson(Kv.by("code", result.getStr("code")).set("data", result));
        }
    }

    /**
     * 动态单据提交
     */
    @UnCheck
    public void vouchProcessDynamicSubmit() {
        Kv result = commonService.vouchProcessDynamicSubmit(getKv());
        if (ObjUtil.equals("200", result.getStr("code"))) {
            renderJBoltApiRet(JBoltApiRet.API_SUCCESS_WITH_DATA(result));
        } else {
            renderJson(Kv.by("code", result.getStr("code")).set("data", result));
        }
    }

}
