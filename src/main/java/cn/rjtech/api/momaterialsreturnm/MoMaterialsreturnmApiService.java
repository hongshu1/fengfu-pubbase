package cn.rjtech.api.momaterialsreturnm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.momaterialsreturnm.MoMaterialsreturnmService;
import cn.rjtech.cache.AuditFormConfigCache;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.MoMaterialsreturnm;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

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


    public JBoltApiRet getModandMomlist(String iautoid) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.getModandMomlist(iautoid));
    }

    public JBoltApiRet AddFromMoMaterialsreturnm(List<Record> SaveTableData, MoMaterialsreturnm moMaterialsreturnm) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialsreturnmService.saveTableSubmitV2(SaveTableData, moMaterialsreturnm));
    }
}
