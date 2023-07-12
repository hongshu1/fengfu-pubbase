package cn.rjtech.api.momopatchweldm;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 补焊纪录
 *
 * @author chentao
 */
@ApiDoc
public class MoMopatchweldmApiController extends BaseApiController {

    @Inject
    private MoMopatchweldmApiService service;

    /**
     * 获取补焊纪录数据
     * @param iMoDocId 工单主键id
     */
    @UnCheck
    public void getList(@Para(value = "iMoDocId") Long iMoDocId) {
        ValidationUtils.validateId(iMoDocId,"工单主键id");
        renderJBoltApiRet(service.getList(iMoDocId));
    }
    /**
     * 补焊纪录保存接口
     * @param iMoDocId 工单主键id
     * @param data [{
     * 			      "cequipmentname": "热成型机1/测试设备2",
     * 			      "coperationname": "发货",
     * 			      "imodocid": "1668511400023326720",
     * 			      "imoroutingconfigid": "1668511400023326722",
     * 			      "iqty": 500
     *             }]
     */
    @UnCheck
    public void saveData(@Para(value = "iMoDocId") Long iMoDocId,
                         @Para(value = "data") String data) {
        ValidationUtils.notNull(iMoDocId,"工单主键id不能为空！");
        renderJBoltApiRet(service.saveData(iMoDocId,data));
    }

}
