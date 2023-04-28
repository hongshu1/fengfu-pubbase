package cn.rjtech.api.RcvDocDefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.RcvDocDefect.RcvDocDefectService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 工段（产线） API Service
 *
 * @author Kephon
 */
public class RcvDocDefectApiService extends JBoltApiBaseService {

    @Inject
    private RcvDocDefectService rcvDocDefectService;



    public JBoltApiRet AdminDatas(int pageSize, int pageNumber, Okv kv) {
        Page<Record> list = rcvDocDefectService.paginateAdminDatas(pageSize, pageNumber, kv);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(list);
    }

    public JBoltApiRet update(JBoltTable jBoltTable, Kv formRecord) {
        Ret list = rcvDocDefectService.updateEditTable(jBoltTable, formRecord);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(list);
    }

}
