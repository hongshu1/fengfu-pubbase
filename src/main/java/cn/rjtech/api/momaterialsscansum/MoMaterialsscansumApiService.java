package cn.rjtech.api.momaterialsscansum;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.momaterialsscansum.MoMaterialscanlogService;
import cn.rjtech.admin.momaterialsscansum.MoMaterialsscansumService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunju
 * @Date: 2023-05-24
 */
public class MoMaterialsscansumApiService extends JBoltApiBaseService {
    @Inject
    private MoMaterialsscansumService moMaterialsscansumService;
    @Inject
    MoMaterialscanlogService moMaterialscanlogService;

    public JBoltApiRet add(String barcoce,Long imodocid) {
        moMaterialsscansumService.add(barcoce,imodocid);
       return JBoltApiRet.success();
    }
    public JBoltApiRet getMoMaterialNotScanLogList(Integer page, Integer pageSize,String barcode){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialscanlogService.
                getMoMaterialNotScanLogList(page,pageSize, Kv.create().setIfNotBlank("barcode",barcode)));
    }
}
