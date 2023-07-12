package cn.rjtech.api.uptimem;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.uptimem.UptimeMService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

/**
 * 稼动时间管理
 *
 * @author chentao
 */
public class UptimeMApiService extends JBoltApiBaseService {

    @Inject
    private UptimeMService service;
    @Inject
    private WorkshiftmService workshiftmService;

    @Inject
    private WorkregionmService workregionmService;

    public JBoltApiRet getPageList(int pageNumber, int pageSize, Kv kv) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.paginateAdminDatas(pageNumber,pageSize,kv));
    }

    public JBoltApiRet getUptimeTplInfoList(Kv kv) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.getUptimeTplInfoList(kv));
    }

    public JBoltApiRet getUptimeMInfoList(Kv kv) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.getUptimeMInfoList(kv));
    }

    public JBoltApiRet updateAndSaveApi(Integer updateOrSaveType,String data,String dataList) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.updateAndSaveApi(updateOrSaveType,data,dataList));
    }

    public JBoltApiRet revocationUptimeMById(Long id) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.revocationUptimeMById(id));
    }

    public JBoltApiRet deleteUptimeMById(Long id) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.delete(id));
    }

    public JBoltApiRet getWorkshiftmSelect() {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(workshiftmService.getSelect());
    }

    public JBoltApiRet getWorkregionmSelect() {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(workregionmService.list(Kv.by("isenabled","true")));
    }

}
