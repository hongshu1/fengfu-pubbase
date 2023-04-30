package cn.rjtech.api.rcvdocdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.model.momdata.RcvDocDefect;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;

import static oshi.util.GlobalConfig.set;

/**
 * 制造异常品管理
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
        return JBoltApiRet.API_SUCCESS_WITH_DATA(rcvDocDefectService.paginateAdminDatas(pageNumber,pageSize,kv));

    }

    public JBoltApiRet add(Long iautoid, Long ircvdocqcformmid, String type) {
        return JBoltApiRet.successWithData(rcvDocDefectService.getRcvDocDefectListApi(iautoid,ircvdocqcformmid,type));
    }

    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(rcvDocDefectService.updateEditTable(formRecord));
    }


}
