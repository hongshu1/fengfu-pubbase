package cn.rjtech.api.appversion;

import cn.jbolt.core.api.OpenAPI;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 应用版本
 *
 * @author Kephon
 */
@ApiDoc
public class AppversionApiController extends BaseApiController {

    @Inject
    private AppversionApiService service;

    /**
     * 获取应用最新版本
     *
     * @param appcode 应用编码, 区分应用单的参数
     */
    @ApiDoc
    @OpenAPI
    public void latestVersion(@Para(value = "appcode") String appcode) {
        ValidationUtils.notBlank(appcode, "应用编码");

        renderJBoltApiRet(service.getLatestVersion(appcode));
    }

}
