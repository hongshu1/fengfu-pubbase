package cn.rjtech.api.workregion;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.workregionm.WorkRegionmVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 工段（产线） API
 *
 * @author
 */
@ApiDoc
public class WorkRegionmApiController extends BaseApiController {

    @Inject
    private WorkRegionmApiService service;

    /**
     * 根据部门获取产线列表
     *
     * @param idepid 部门ID
     */
    @ApiDoc(result = WorkRegionmVo.class)
    @UnCheck
    public void options(@Para(value = "idepid") Long idepid) {
        ValidationUtils.validateId(idepid, "部门ID");

        renderJBoltApiRet(service.getOptions(idepid));
    }


}
