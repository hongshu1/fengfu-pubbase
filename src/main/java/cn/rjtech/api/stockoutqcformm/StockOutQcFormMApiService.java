package cn.rjtech.api.stockoutqcformm;


import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import cn.rjtech.admin.stockoutqcformd.StockoutQcFormDService;
import cn.rjtech.admin.stockoutqcformdline.StockoutqcformdLineService;
import cn.rjtech.admin.stockoutqcformm.StockoutQcFormMService;
import cn.rjtech.model.momdata.StockoutQcFormM;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

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

    /**
     * 点击左侧导航栏-出库检，显示主页面数据
     */
    public JBoltApiRet getDatas(Kv kv) {
        return JBoltApiRet.successWithData(service.pageList(kv));
    }

    /**
     * 生成
     */
    public JBoltApiRet createTable(Long iautoid, String cqcformname) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.createTable(iautoid, cqcformname));
    }

    /**
     * 点击检验按钮，跳转到检验页面
     */
    public JBoltApiRet jumpCheckout(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        StockoutQcFormM stockoutQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
        record.set("cstockoutqcformno",stockoutQcFormM.getCStockoutQcFormNo());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 跳转到检验页面后，自动加载table的数据
     */
    public JBoltApiRet autoGetCheckOutTableDatas(Long istockoutqcformmid) {
        //1、查询table的数据
        Kv kv = new Kv();
        kv.set("istockoutqcformmid", istockoutqcformmid);
        List<Record> recordList = service.getCheckOutTableDatas(kv);
        //2、将数据传到
        for (Record record : recordList) {
            record.keep("iautoid", "iformparamid", "iqcformid", "istockoutqcformmid", "iseq", "isubseq",
                "itype", "coptions", "cqcformparamids", "cqcitemname", "cqcparamname", "imaxval", "iminval", "istdval");
        }
        return JBoltApiRet.API_SUCCESS_WITH_DATA(recordList);
    }

    /**
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     */
    public JBoltApiRet jumpOnlysee(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        StockoutQcFormM stockoutQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(stockoutQcFormM.getIAutoId());
        record.set("size",stockoutqcformlist.size());
        record.set("cstockoutqcformno",stockoutQcFormM.getCStockoutQcFormNo());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public JBoltApiRet jumpEdit(Long iautoid){
        StockoutQcFormM stockoutQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(stockoutQcFormM.getIAutoId());
        record.set("size", stockoutqcformlist.size());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 跳转到"查看"页面后，自动加载查看页面table的数据
     */
    public JBoltApiRet autoGetOnlyseeOrEditTableDatas(Long iautoid) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        //1、调用方法获得table数据
        List<Record> recordList = service.getonlyseelistByiautoid(kv);
        //2、传到实体类里面
        for (Record record : recordList) {
            record.keep("iautoid", "iformparamid", "iqcformid", "coptions", "cqcformparamids",
                "cqcitemname", "cqcparamname", "iseq", "isubseq", "itype", "imaxval", "iminval", "istdval", "cvaluelist");
        }
        return JBoltApiRet.API_SUCCESS_WITH_DATA(recordList);
    }

    /**
     * 点击“检验”按钮，在检验页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveCheckOut(String cmeasurepurpose, String cdcno, Long istockqcformmiautoid, String cmeasureunit,
                                    String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、开始更新编辑页面的数据
        Boolean result = service.achiveChecOutSerializeSubmitList(JSON.parseArray(serializeSubmitList), istockqcformmiautoid,
            cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 点击“编辑”按钮，在编辑页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveEdit(String cmeasurepurpose, String cdcno, Long stockqcformmiautoid, String cmeasureunit, String isok,
                                String cmeasurereason, String serializeSubmitList, String cmemo) {
        // 1、开始更新编辑页面的数据
        Boolean result = service
            .achiveEditSerializeSubmitList(JSON.parseArray(serializeSubmitList), stockqcformmiautoid, cmeasurepurpose,
                cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 自动加载图片
     * */
    public JBoltApiRet uploadImage(List<UploadFile> files) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.uploadImage(files));
    }

    /*
     * 导出详情页
     * */
    public JBoltApiRet getExportData(Long iautoid){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.getExportData(iautoid));
    }
}
