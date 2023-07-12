package cn.rjtech.api.momopatchweldm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.momopatchweldm.MoMopatchweldmService;
import com.jfinal.aop.Inject;

/**
 * 补焊纪录
 *
 * @author chentao
 */
public class MoMopatchweldmApiService extends JBoltApiBaseService {

    @Inject
    private MoMopatchweldmService moMopatchweldmService;

    public JBoltApiRet getList(Long iMoDocId) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMopatchweldmService.getMoMopatchwelddApiList(iMoDocId));
    }

    public JBoltApiRet saveData(Long iMoDocId,String data) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMopatchweldmService.saveData(iMoDocId,data));
    }

}
