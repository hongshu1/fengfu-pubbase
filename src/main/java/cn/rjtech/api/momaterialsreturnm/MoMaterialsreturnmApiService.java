package cn.rjtech.api.momaterialsreturnm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.momaterialsreturnm.MoMaterialsreturnmService;
import cn.rjtech.model.momdata.MoMaterialsreturnm;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Record;


import java.util.List;

/**
 * @author Kephon
 */
public class MoMaterialsreturnmApiService extends JBoltApiBaseService {

   @Inject
   private MoMaterialsreturnmService moMaterialsreturnmService;


    public JBoltApiRet getBycBarcodeInfo(String barcode,Integer pageNumber,Integer pageSize) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(  moMaterialsreturnmService.getBycBarcodeInfo(barcode,pageNumber,pageSize));
    }


    public JBoltApiRet getBycBarcodeList(Integer pageNumber,Integer pageSize) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.getBycBarcodeList(pageNumber,pageSize));
    }


    public JBoltApiRet getModandMomlist(String iautoid,Integer pageNumber,Integer pageSize) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.getModandMomlist(iautoid,pageNumber,pageSize));
    }

    public JBoltApiRet AddFromMoMaterialsreturnm(List<Record> SaveTableData, MoMaterialsreturnm moMaterialsreturnm) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.saveTableSubmitV2(SaveTableData, moMaterialsreturnm));
    }
}
