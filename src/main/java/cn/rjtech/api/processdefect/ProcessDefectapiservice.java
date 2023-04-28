package cn.rjtech.api.processdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.ProcessDefect.ProcessDefectService;
import cn.rjtech.admin.SpecMaterialsRcvM.SpecMaterialsRcvMService;
import cn.rjtech.model.momdata.ProcessDefect;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;

import static oshi.util.GlobalConfig.set;

/**
 * 制造异常品管理
 *
 */
public class ProcessDefectapiservice extends JBoltApiBaseService {


    @Inject
    private ProcessDefectService processDefectService;

    @Inject
    private SpecMaterialsRcvMService specMaterialsRcvMService;



    /*
     * 显示主页面数据
     * */
    public JBoltApiRet AdminDatas(int pageSize, int pageNumber, Okv kv) {
        return JBoltApiRet.successWithData(processDefectService.paginateAdminDatas(pageSize, pageNumber, kv));
    }


    public JBoltApiRet add(Long iautoid, Long iissueid,String type){

        ProcessDefect processDefect = processDefectService.findById(iautoid);
        SpecMaterialsRcvM specMaterialsRcvM = specMaterialsRcvMService.findById(iissueid);
        set("iautoid", iautoid);
        set("type",type);
        set("iissueid", iissueid);
        if (isNull( iautoid)){
            set("processDefect", processDefect);
            set("specMaterialsRcvM", specMaterialsRcvM);

        }else {
            if (processDefect.getIStatus() == 1) {
                set("processDefect", processDefect);
                set("specMaterialsRcvM", specMaterialsRcvM);
                set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");

            } else if (processDefect.getIStatus() == 2) {
                int getCApproach = Integer.parseInt(processDefect.getCApproach());
                set("capproach", (getCApproach == 1) ? "返修" : "报废");
                set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
                set("processDefect", processDefect);
                set("specMaterialsRcvM", specMaterialsRcvM);

            }
        }
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(processDefectService.updateEditTable(formRecord));
    }





}
