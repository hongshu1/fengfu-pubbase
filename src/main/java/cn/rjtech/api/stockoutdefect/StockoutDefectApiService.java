package cn.rjtech.api.stockoutdefect;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import cn.rjtech.entity.vo.stockoutdefect.StockoutDefectDatas;
import cn.rjtech.entity.vo.stockoutdefect.StockoutDefectVo;
import cn.rjtech.model.momdata.StockoutDefect;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 制造异常品管理
 */
public class StockoutDefectApiService extends JBoltApiBaseService {


    @Inject
    private StockoutDefectService stockoutDefectService;
    @Inject
    private RcvDocQcFormMService rcvDocQcFormMService;

    /**
     * 显示主页面数据
     */
    public JBoltApiRet getAdminDatas(int pageSize, int pageNumber, Kv kv) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(stockoutDefectService.getPageListApi(pageNumber,pageSize,kv));
    }


    /**
     * 点击编辑或者查看，跳转页面明细
     */
    public JBoltApiRet add(Long iautoid, Long stockoutqcformmid, String type) {
        StockoutDefect  stockoutDefect = stockoutDefectService.findById(iautoid);
        Record stockoutQcFormM = stockoutDefectService.getstockoutQcFormMList(stockoutqcformmid);
        //2、set到实体类
        StockoutDefectDatas stockoutDefectDatas = new StockoutDefectDatas();
        stockoutDefectDatas.setStockoutQcFormM(stockoutQcFormM);
        stockoutDefectDatas.setStockoutDefect(stockoutDefect);
        //3、最后返回vo
        StockoutDefectVo stockoutDefectVo = new StockoutDefectVo();
        stockoutDefectVo.setCode(0);
        stockoutDefectVo.setData(stockoutDefectDatas);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(stockoutDefectVo);
    }

    /**
     * 点击保存，保存页面明细信息
     */
    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(stockoutDefectService.updateEditTable(formRecord));
    }


}
