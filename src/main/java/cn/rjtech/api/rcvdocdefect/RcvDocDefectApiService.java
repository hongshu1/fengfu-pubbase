package cn.rjtech.api.rcvdocdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

/**
 * 来料异常品管理api接口
 */
public class RcvDocDefectApiService extends JBoltApiBaseService {


    @Inject
    private RcvDocDefectService rcvDocDefectService;
    @Inject
    private RcvDocQcFormMService rcvDocQcFormMService;

    /**
     * 显示主页面数据
     */
    public JBoltApiRet getAdminDatas(int pageNumber ,int pageSize, Kv kv) {
        return JBoltApiRet.successWithData(rcvDocDefectService.getPageListApi(pageNumber,pageSize,kv));
    }

    public JBoltApiRet add(Long iautoid, Long ircvdocqcformmid, String type) {
        return JBoltApiRet.successWithData(rcvDocDefectService.getRcvDocDefectListApi(iautoid,ircvdocqcformmid,type));
    }

    public JBoltApiRet update(Kv formRecord) {
        rcvDocDefectService.updateEditTable(formRecord);
        return JBoltApiRet.API_SUCCESS;
    }


    public JBoltApiRet QRCode(Kv kv) {
        rcvDocDefectService.getQRCodeCheck(kv);
        return JBoltApiRet.API_SUCCESS;
    }

}
