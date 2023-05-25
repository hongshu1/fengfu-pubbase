package cn.rjtech.api.momaterialsscansum;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.momaterialsscansum.MoMaterialsscansumService;
import com.jfinal.aop.Inject;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunju
 * @Date: 2023-05-24
 */
public class MoMaterialsscansumApiService extends JBoltApiBaseService {
    @Inject
    private MoMaterialsscansumService moMaterialsscansumService;

    public JBoltApiRet add(String barcoce) {
        moMaterialsscansumService.add(barcoce);
       return JBoltApiRet.success();
    }
}
