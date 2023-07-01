package cn.rjtech.api.moroutingconfigoperation;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.moroutingconfigoperation.MoMoroutingconfigOperationVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 工单工序
 */
@ApiDoc
public class MoMoroutingconfigOperationApiController extends BaseApiController {

    @Inject
    private MoMoroutingconfigOperationApiService moMoroutingconfigOperationApiService;

    /**
     * 工单工序明细
     *
     * @param imodocid 制造工单ID
     */
    @ApiDoc(result = MoMoroutingconfigOperationVo.class)
    @UnCheck
    public void dataList(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.validateId(imodocid, "工单ID");

        renderJBoltApiRet(moMoroutingconfigOperationApiService.dataList(imodocid));
    }

}
