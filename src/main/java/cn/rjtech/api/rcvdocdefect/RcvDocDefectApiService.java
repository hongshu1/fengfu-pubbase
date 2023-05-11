package cn.rjtech.api.rcvdocdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.model.momdata.RcvDocDefect;
import cn.rjtech.util.ValidationUtils;
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
    public JBoltApiRet getAdminDatas(int pageSize, int pageNumber, Kv kv) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(rcvDocDefectService.getPageListApi(pageNumber,pageSize,kv));
    }

    public JBoltApiRet add(Long iautoid, Long ircvdocqcformmid, String type) {
        return JBoltApiRet.successWithData(rcvDocDefectService.getRcvDocDefectListApi(iautoid,ircvdocqcformmid,type));
    }

    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(rcvDocDefectService.updateEditTable(formRecord));
    }

    public String rcvDocDefectId(Long id){
        ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
        RcvDocDefect byId = rcvDocDefectService.findById(id);
        String cDocNo = byId.getCDocNo();
        return cDocNo;
    }


}
