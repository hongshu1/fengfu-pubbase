package cn.rjtech.api.workregion;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.model.momdata.Workregionm;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.List;

/**
 * 工段（产线） API Service
 *
 * @author Kephon
 */
public class WorkRegionmApiService extends JBoltApiBaseService {

    @Inject
    private WorkregionmService workregionmService;

    public JBoltApiRet getOptions(Long idepid) {
        List<Workregionm> list = workregionmService.getWorkregionmList(getOrgId(), idepid);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(list);
    }

    /**
     *记录上传使用
     */
    public JBoltApiRet getOptions2(Kv kv) {
        kv.set("isenabled", "true");
        return JBoltApiRet.API_SUCCESS_WITH_DATA(workregionmService.list(kv));
    }
}
