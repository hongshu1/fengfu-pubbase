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

import static oshi.util.GlobalConfig.set;

/**
 * 制造异常品管理
 *
 */
public class rcvDocDefectapiservice extends JBoltApiBaseService {


    @Inject
    private RcvDocDefectService rcvDocDefectService;

    @Inject
    private RcvDocQcFormMService rcvDocQcFormMService;



    /*
     * 显示主页面数据
     * */
    public JBoltApiRet AdminDatas(int pageSize, int pageNumber, Okv kv) {
        return JBoltApiRet.successWithData(rcvDocDefectService.paginateAdminDatas(pageSize, pageNumber, kv));
    }


    public JBoltApiRet add(Long iautoid, Long istockoutqcformmid,String type){

        RcvDocDefect rcvDocDefect = rcvDocDefectService.findById(iautoid);
        RcvDocQcFormM rcvDocQcFormM = rcvDocQcFormMService.findFirst("select t1.*, t2.cInvCode, t2.cInvName, t2.cInvCode1, t3.cVenName\n" +
                "from PL_RcvDocQcFormM t1\n" +
                "LEFT JOIN Bd_Inventory t2 ON t2.iAutoId = t1.iInventoryId \n" +
                "LEFT JOIN Bd_Vendor t3 ON t3.iAutoId = t1.iVendorId \n" +
                "where t1.iAutoId = '"+istockoutqcformmid+"'");
        set("iautoid", iautoid);
        set("type", type);
        if (isNull(iautoid)) {
            set("rcvDocDefect", rcvDocDefect);
            set("rcvDocQcFormM", rcvDocQcFormM);
        } else {
            if (rcvDocDefect.getIStatus() == 1) {
                set("istatus", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
                set("rcvDocDefect", rcvDocDefect);
                set("rcvDocQcFormM", rcvDocQcFormM);
            } else if (rcvDocDefect.getIStatus() == 2) {
                int getCApproach = Integer.parseInt(rcvDocDefect.getCApproach());
                set("capproach", (getCApproach == 1) ? "特采" : "拒收");
                set("istatus", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
                set("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
                set("rcvDocDefect", rcvDocDefect);
                set("rcvDocQcFormM", rcvDocQcFormM);
            }
        }
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(rcvDocDefectService.updateEditTable(formRecord));
    }




}
