package cn.rjtech.api.momaterialsreturnm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.momaterialsreturnm.MoMaterialsreturnmService;
import cn.rjtech.model.momdata.MoMaterialsreturnm;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * @author Kephon
 */
public class MoMaterialsreturnmApiService extends JBoltApiBaseService {

   @Inject
   private MoMaterialsreturnmService moMaterialsreturnmService;


    public JBoltApiRet getBycBarcodeInfo(String barcode) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(  moMaterialsreturnmService.getBycBarcodeInfo(barcode));
    }


    public JBoltApiRet getBycBarcodeList(Integer pageNumber,Integer pageSize) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.getBycBarcodeList(pageNumber,pageSize));
    }


    public JBoltApiRet getModandMomlist(String iautoid) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.getModandMomlist(iautoid));
    }

    public JBoltApiRet AddFromMoMaterialsreturnm(List<Record> SaveTableData, MoMaterialsreturnm moMaterialsreturnm) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.saveTableSubmitV2(SaveTableData, moMaterialsreturnm));
    }

    public JBoltApiRet aginateAdminDatas(Integer pageNumber, Integer pageSize,Long IMoDocId,String billno) {
        Kv kv=Kv.by("IMoDocId",IMoDocId).set("billno",billno);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.paginateAdminDatas(pageNumber,pageSize,kv));
    }
}
