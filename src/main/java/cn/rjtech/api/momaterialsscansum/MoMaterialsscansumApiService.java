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
    private MoMaterialscanlogService moMaterialscanlogService;

    public JBoltApiRet add(String barcoce,Long imodocid) {
        moMaterialsscansumService.add(barcoce,imodocid);
       return JBoltApiRet.success();
    }
    public JBoltApiRet getMoMaterialNotScanLogList(Integer pageNumber, Integer pageSize,Long imodocid,Integer isScanned){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialscanlogService.getMoMaterialNotScanLogList(pageNumber,pageSize,Kv.by("imodocid",imodocid).set("isScanned",isScanned)));
    }
    public JBoltApiRet getBarcodeAll(Integer pageNumber, Integer pageSize,Long imodocid){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(moMaterialscanlogService.getBarcodeAll(pageNumber,pageSize,Kv.by("imodocid",imodocid)));
    }

}
