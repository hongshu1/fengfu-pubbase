package cn.rjtech.api.stockoutqcformm;


import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import cn.rjtech.admin.stockoutqcformd.StockoutQcFormDService;
import cn.rjtech.admin.stockoutqcformdline.StockoutqcformdLineService;
import cn.rjtech.admin.stockoutqcformm.StockoutQcFormMService;
import cn.rjtech.model.momdata.StockoutQcFormD;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 9:24
 * @Description 质量管理-出库检api接口
 */
public class StockOutQcFormMApiService extends JBoltApiBaseService {

    @Inject
    private StockoutQcFormMService     service;
    @Inject
    private StockoutqcformdLineService stockoutqcformdLineService;
    @Inject
    private StockoutQcFormDService     stockoutQcFormDService;
    @Inject
    private StockoutDefectService      stockoutDefectService; //出库检异常品单
    @Inject
    private RcvDocQcFormMService       rcvDocQcFormMService;

    /**
     * 点击左侧导航栏-出库检，显示主页面数据
     */
    public JBoltApiRet getDatas(Kv kv) {
        return JBoltApiRet.successWithData(service.pageList(kv));
    }

    /**
     * 生成
     */
    public JBoltApiRet createTable(Long iautoid) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.createTable(iautoid));
    }

    /**
     * 点击检验按钮，跳转到检验页面
     */
    public JBoltApiRet jumpCheckout(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        Record record = new Record();
        Record checkoutrecord = service.getCheckoutListByIautoId(iautoid);
        if (checkoutrecord == null) {
            return JBoltApiRet.API_FAIL("数据不存在");
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(checkoutrecord.getLong("iqcformid"));
        record.set("columns", tableHeadData);
        record.set("record", record);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     */
    public JBoltApiRet jumpOnlysee(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        Record record = new Record();
        Record checkoutrecord = service.getCheckoutListByIautoId(iautoid);
        if (checkoutrecord == null) {
            return JBoltApiRet.API_FAIL("数据不存在");
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(checkoutrecord.getLong("iqcformid"));
        List<StockoutQcFormD> stockoutqcformlist = stockoutQcFormDService.findByIStockoutQcFormMid(iautoid);
        record.set("stockoutqcformlist", stockoutqcformlist);
        record.set("columns", tableHeadData);
        record.set("record", checkoutrecord);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public JBoltApiRet jumpEdit(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        Record record = new Record();
        Record checkoutrecord = service.getCheckoutListByIautoId(iautoid);
        if (checkoutrecord == null) {
            return JBoltApiRet.API_FAIL("数据不存在");
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(checkoutrecord.getLong("iqcformid"));
        List<StockoutQcFormD> stockoutqcformlist = stockoutQcFormDService.findByIStockoutQcFormMid(iautoid);
        record.set("stockoutqcformlist", stockoutqcformlist);
        record.set("columns", tableHeadData);
        record.set("record", checkoutrecord);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    public JBoltApiRet getTableDatas(Long istockoutqcformmid) {
        Kv kv = new Kv();
        kv.set("istockoutqcformmid", istockoutqcformmid);
        //1、查询
        List<Record> tableDatas = service.getTableDatas(kv);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(tableDatas);
    }

    /**
     * 点击“检验”按钮，在检验页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveCheckOut(String cmeasurepurpose, String cdcno, Long istockqcformmiautoid, String cmeasureunit,
                                    String isok, String cmeasurereason, String serializeSubmitList, String cmemo,String cbatchno) {
        //1、开始更新编辑页面的数据
        Boolean result = service.achieveSerializeSubmitList(JSON.parseArray(serializeSubmitList), istockqcformmiautoid,
            cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok, cbatchno);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 点击“编辑”按钮，在编辑页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveEdit(String cmeasurepurpose, String cdcno, Long stockqcformmiautoid, String cmeasureunit, String isok,
                                String cmeasurereason, String serializeSubmitList, String cmemo,String cbatchno) {
        // 1、开始更新编辑页面的数据
        Boolean result = service
            .achieveSerializeSubmitList(JSON.parseArray(serializeSubmitList), stockqcformmiautoid, cmeasurepurpose,
                cmeasurereason, cmeasureunit, cmemo, cdcno, isok,cbatchno);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 导出详情页
     * */
    public JBoltApiRet getExportData(Long iautoid) throws Exception{
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.getExportData(iautoid));
    }
}
