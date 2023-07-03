package cn.rjtech.api.momaterialsreturnm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.momaterialsreturnm.MoMaterialsreturnmService;
import com.jfinal.aop.Inject;

/**
 * @author Kephon
 */
public class MoMaterialsreturnmApiService extends JBoltApiBaseService {

   @Inject
   private MoMaterialsreturnmService moMaterialsreturnmService;


    public JBoltApiRet getBycBarcodeInfo(String barcode) {
        moMaterialsreturnmService.getBycBarcodeInfo(barcode);
        return JBoltApiRet.success();
    }


    public JBoltApiRet getBycBarcodeList() {
        moMaterialsreturnmService.getBycBarcodeList();
        return JBoltApiRet.success();
    }

    public JBoltApiRet saveTableSubmit(JBoltTable jBoltTable) {
       return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.saveTableSubmit(jBoltTable));
    }
}
