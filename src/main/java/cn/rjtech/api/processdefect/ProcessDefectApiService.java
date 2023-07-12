package cn.rjtech.api.processdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.processdefect.ProcessdefectService;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;



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
        return JBoltApiRet.successWithData(processDefectService.getPageListApi(pageSize, pageNumber, kv));
    }


    public JBoltApiRet add(Long iautoid, Long iissueid, String type) {
        return JBoltApiRet.successWithData(processDefectService.getProcessDefectApi(iautoid,iissueid,type));
    }


    public JBoltApiRet update(Kv formRecord) {
        processDefectService.updateEditTable(formRecord);
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet QRCode(Kv kv) {
        processDefectService.getQRCodeCheck(kv);
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet editProcessDefect(Kv kv){
        processDefectService.editProcessDefect(kv);
        return  JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet deletes(Long iautoid) {
        processDefectService.delete(iautoid);
        return JBoltApiRet.API_SUCCESS;
    }


}
