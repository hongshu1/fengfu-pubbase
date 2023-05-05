package cn.rjtech.api.instockdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.model.momdata.InStockDefect;
import cn.rjtech.model.momdata.InStockQcFormM;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;

import static oshi.util.GlobalConfig.set;

/**
 * 工段（产线） API Service
 *
 * @author Kephon
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
        return JBoltApiRet.successWithData(inStockDefectService.updateEditTable(formRecord));
    }


}
