package cn.rjtech.api.moroutingconfigoperation;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.moroutingconfigoperation.MoMoroutingconfigOperationService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunjun
 * @Date: 2023-06-16
 */
public class MoMoroutingconfigOperationApiService extends JBoltApiBaseService {
    @Inject
    private MoMoroutingconfigOperationService moMoroutingconfigOperationService;
    public JBoltApiRet dataList(Long imodocid) {

        return  JBoltApiRet.API_SUCCESS_WITH_DATA(moMoroutingconfigOperationService.dataList(
                Kv.create().setIfNotNull("imodocid",imodocid)));//暂时放开
    }
}
