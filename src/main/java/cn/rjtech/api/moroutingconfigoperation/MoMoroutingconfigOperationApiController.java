package cn.rjtech.api.moroutingconfigoperation;

import cn.jbolt.core.api.OpenAPI;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.modoc.ModocApiPageVo;
import cn.rjtech.entity.vo.moroutingconfigoperation.MoMoroutingconfigOperationVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @ClassName :
 * @Description : * 工单工序
 * @Author : dongjunjun
 * @Date: 2023-06-16
 */
@ApiDoc
public class MoMoroutingconfigOperationApiController extends BaseApiController {
    @Inject
    private  MoMoroutingconfigOperationApiService moMoroutingconfigOperationApiService;

    /**
     * 工单工序ID
     * @param imodocid
     */
    @ApiDoc(result = MoMoroutingconfigOperationVo.class)
    @UnCheck
    @OpenAPI
    public void dataList( @Para(value = "imodocid") Long imodocid){
        ValidationUtils.notNull(imodocid,"缺少工单ID");
        renderJBoltApiRet(moMoroutingconfigOperationApiService.dataList(imodocid));
    }
}
