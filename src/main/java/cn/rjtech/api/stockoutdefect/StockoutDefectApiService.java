package cn.rjtech.api.stockoutdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

/**
 * 出库异常品管理api接口
 */
public class StockoutDefectApiService extends JBoltApiBaseService {


    @Inject
    private StockoutDefectService stockoutDefectService;
    @Inject
    private RcvDocQcFormMService rcvDocQcFormMService;

    /**
     * 显示主页面数据
     */
    public JBoltApiRet getAdminDatas(int pageNumber ,int pageSize, Kv kv) {
        return JBoltApiRet.successWithData(stockoutDefectService.getPageListApi(pageNumber,pageSize,kv));
    }


    public JBoltApiRet add(Long iautoid, Long stockoutqcformmid, String type) {
        return JBoltApiRet.successWithData(stockoutDefectService.getstockoutDefectListApi(iautoid,stockoutqcformmid,type));
    }
    /**
     * 点击保存，保存页面明细信息
     */
    public JBoltApiRet update(Kv formRecord) {
        stockoutDefectService.updateEditTable(formRecord);
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet QRCode(Kv kv) {
        stockoutDefectService.getQRCodeCheck(kv);
        return JBoltApiRet.API_SUCCESS;
    }




}
