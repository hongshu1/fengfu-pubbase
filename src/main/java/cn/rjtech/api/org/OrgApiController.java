package cn.rjtech.api.org;

import cn.jbolt.core.api.OpenAPI;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.org.OrgVo;
import com.jfinal.aop.Inject;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 组织API
 *
 * @author Kephon
 */
public class OrgApiController extends BaseApiController {

    @Inject
    private OrgApiService service;

    /**
     * 获取启用的组织列表
     */
    @ApiDoc(result = OrgVo.class)
    @OpenAPI
    public void list() {
        renderJBoltApiRet(service.getList());
    }

}
