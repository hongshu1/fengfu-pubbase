package cn.rjtech.api.workregion;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.workregionm.WorkRegionmVo;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
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
        //ValidationUtils.validateId(idepid, "部门ID");

        renderJBoltApiRet(service.getOptions(idepid));
    }

    /**
     * 获取产线列表 记录上传使用
     */
    @ApiDoc(result = WorkRegionmVo.class)
    @UnCheck
    public void options2(@Para(value = "cworkcode") String cworkcode,
                        @Para(value = "cworkname") String cworkname){
        Kv kv = Kv.by("cworkcode", cworkcode).set("cworkname", cworkname);
        renderJBoltApiRet(service.getOptions2(kv));
    }


}
