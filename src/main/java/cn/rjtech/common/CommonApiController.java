package cn.rjtech.common;

import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.common.columsmap.ColumsmapService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.Map;

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
        Map map = commonService.vouchProcessSubmit(getKv());
        if ("200".equals(map.get("code").toString())) {
            renderJBoltApiRet(JBoltApiRet.API_SUCCESS_WITH_DATA(map));
        } else {
            renderJson(Kv.by("code", map.get("code").toString()).set("data", map));
        }
    }

    /**
     * 动态单据提交
     */
    @UnCheck
    public void vouchProcessDynamicSubmit() {
        Map map = commonService.vouchProcessDynamicSubmit(getKv());
        if ("200".equals(map.get("code").toString())) {
            renderJBoltApiRet(JBoltApiRet.API_SUCCESS_WITH_DATA(map));
        } else {
            renderJson(Kv.by("code", map.get("code").toString()).set("data", map));
        }
    }

}
