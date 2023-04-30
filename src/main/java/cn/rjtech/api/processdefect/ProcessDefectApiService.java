package cn.rjtech.api.processdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.processdefect.ProcessdefectService;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
import cn.rjtech.model.momdata.ProcessDefect;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;

import static oshi.util.GlobalConfig.set;

/**
 * 制造异常品管理
 */
public class ProcessDefectApiService extends JBoltApiBaseService {

    @Inject
    private ProcessdefectService processDefectService;
    @Inject
    private SpecMaterialsRcvMService specMaterialsRcvMService;

    /**
     * 显示主页面数据
     */
    public JBoltApiRet getAdminDatas(int pageSize, int pageNumber, Kv kv) {
        return JBoltApiRet.successWithData(processDefectService.paginateAdminDatas(pageSize, pageNumber, kv));
    }


    public JBoltApiRet add(Long iautoid, Long iissueid, String type) {
        return JBoltApiRet.successWithData(processDefectService.getProcessDefectApi(iautoid,iissueid,type));
    }


    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(processDefectService.updateEditTable(formRecord));
    }

}
