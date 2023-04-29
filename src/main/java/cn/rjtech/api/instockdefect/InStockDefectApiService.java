package cn.rjtech.api.instockdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.model.momdata.InStockDefect;
import cn.rjtech.model.momdata.InStockQcFormM;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;

import static oshi.util.GlobalConfig.set;

/**
 * 工段（产线） API Service
 *
 * @author Kephon
 */
public class InStockDefectApiService extends JBoltApiBaseService {

    @Inject
    private InStockDefectService inStockDefectService;
    @Inject
    private InStockQcFormMService inStockQcFormMService;

    /**
     * 显示主页面数据
     */
    public JBoltApiRet AdminDatas(int pageSize, int pageNumber, Okv kv) {
        return JBoltApiRet.successWithData(inStockDefectService.paginateAdminDatas(pageSize, pageNumber, kv));
    }

    public JBoltApiRet add(Long iautoid, Long iinstockqcformmid, String type) {

        InStockDefect inStockDefect = inStockDefectService.findById(iautoid);
        InStockQcFormM inStockQcFormM = inStockQcFormMService.findFirst("select t1.*, t2.cInvCode, t2.cInvName, t2.cInvCode1\n" +
                "from PL_InStockQcFormM t1\n" +
                "LEFT JOIN Bd_Inventory t2 ON t2.iAutoId = t1.iInventoryId \n" +
                "where t1.iAutoId = '" + iinstockqcformmid + "'");
        set("iautoid", iautoid);
        set("type", type);
        if (isNull(iautoid)) {
            set("inStockDefect", inStockDefect);
            set("inStockQcFormM", inStockQcFormM);
        } else {
            if (inStockDefect.getIStatus() == 1) {
                set("istatus", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内" : "其他"));
                set("inStockDefect", inStockDefect);
                set("inStockQcFormM", inStockQcFormM);

            } else if (inStockDefect.getIStatus() == 2) {
                int getCApproach = Integer.parseInt(inStockDefect.getCApproach());
                set("capproach", (getCApproach == 1) ? "报废" : (getCApproach == 2 ? "返修" : "退货"));
                set("istatus", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内" : "其他"));
                set("inStockDefect", inStockDefect);
                set("inStockQcFormM", inStockQcFormM);

            }
        }
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(inStockDefectService.updateEditTable(formRecord));
    }


}
