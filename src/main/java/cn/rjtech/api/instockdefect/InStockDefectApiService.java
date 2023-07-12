package cn.rjtech.api.instockdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

/**
 *在库异常品管理api接口
 */
public class InStockDefectApiService extends JBoltApiBaseService {

    @Inject
    private InStockDefectService inStockDefectService;
    @Inject
    private InStockQcFormMService inStockQcFormMService;

    /**
     * 显示主页面数据
     */
    public JBoltApiRet AdminDatas(int pageSize, int pageNumber, Kv kv) {
        return JBoltApiRet.successWithData(inStockDefectService.getPageListApi(pageSize, pageNumber, kv));
    }

    public JBoltApiRet add(Long iautoid, Long iinstockqcformmid, String type) {
        return JBoltApiRet.successWithData(inStockDefectService.getInStockDefectListApi(iautoid,iinstockqcformmid,type));
    }

    public JBoltApiRet update(Kv formRecord) {
        inStockDefectService.updateEditTable(formRecord);
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet QRCode(Kv kv) {
        inStockDefectService.getQRCodeCheck(kv);
        return JBoltApiRet.API_SUCCESS;
    }

}
