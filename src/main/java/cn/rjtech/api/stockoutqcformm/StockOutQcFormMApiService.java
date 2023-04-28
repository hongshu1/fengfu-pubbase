package cn.rjtech.api.stockoutqcformm;


import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import cn.rjtech.admin.stockoutqcformd.StockoutQcFormDService;
import cn.rjtech.admin.stockoutqcformdline.StockoutqcformdLineService;
import cn.rjtech.admin.stockoutqcformm.StockoutQcFormMService;
<<<<<<< HEAD
import cn.rjtech.entity.vo.stockoutqcformm.*;
=======
import cn.rjtech.entity.vo.stockoutqcformm.AutoGetCheckOutTableDatas;
import cn.rjtech.entity.vo.stockoutqcformm.AutoGetCheckOutTableDatasVo;
import cn.rjtech.entity.vo.stockoutqcformm.AutoGetOnlyseeTableDatas;
import cn.rjtech.entity.vo.stockoutqcformm.AutoGetOnlyseeTableDatas.Cvalue;
import cn.rjtech.entity.vo.stockoutqcformm.AutoGetOnlyseeTableDatasVo;
import cn.rjtech.entity.vo.stockoutqcformm.StockOutQcFormMApiSaveEdit;
>>>>>>> c49e959840a7e8dd3e3c1f4afdfd99c42f03b9d3
import cn.rjtech.entity.vo.stockoutqcformm.StockOutQcFormMApiSaveEdit.CValue;
import cn.rjtech.entity.vo.stockoutqcformm.StockOutQcFormMApiSaveEdit.SerializeElement;
import cn.rjtech.model.momdata.StockoutDefect;
import cn.rjtech.model.momdata.StockoutQcFormM;
import cn.rjtech.model.momdata.StockoutqcformdLine;
import cn.rjtech.util.JBoltModelKit;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
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
    private JBoltBaseService           jBoltBaseService;
    @Inject
    private StockoutDefectService stockoutDefectService; //出库检异常品单

    /*
     * 点击左侧导航栏-出库检，显示主页面数据
     * */
    public JBoltApiRet getDatas(Kv kv) {
        return JBoltApiRet.successWithData(service.pageList(kv));
    }

    /*
     * 生成
     * */
    public JBoltApiRet createTable(Long iautoid, String cqcformname) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.createTable(iautoid, cqcformname));
    }

    /*
     * 点击检验按钮，跳转到检验页面
     * */
    public JBoltApiRet jumpCheckout(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        StockoutQcFormM stockoutQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
        //2、set到实体类
        StockoutQcFormMCheckout stockoutQcFormMCheckout = new StockoutQcFormMCheckout();
        stockoutQcFormMCheckout.setStockoutQcFormM(stockoutQcFormM);
        stockoutQcFormMCheckout.setRecord(record);
        //3、最后返回vo
        StockoutQcFormMCheckoutVo checkoutVo = new StockoutQcFormMCheckoutVo();
        checkoutVo.setCode(0);
        checkoutVo.setData(stockoutQcFormMCheckout);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(checkoutVo);
    }

    /*
     * 跳转到检验页面后，自动加载table的数据
     * */
    public JBoltApiRet autoGetCheckOutTableDatas(Long istockoutqcformmid) {
        //1、查询table的数据
        Kv kv = new Kv();
        kv.set("istockoutqcformmid", istockoutqcformmid);
        List<Record> recordList = service
            .clearZero(jBoltBaseService.dbTemplate("stockoutqcformm.findChecoutListByIformParamid", kv).find());
        //2、将数据传到vo
        List<AutoGetCheckOutTableDatas> autoList = new ArrayList<>();
        for (Record record : recordList) {
            AutoGetCheckOutTableDatas auto = new AutoGetCheckOutTableDatas();
            auto.setIautoid(record.get("iautoid"));
            auto.setIformparamid(record.get("iformparamid"));
            auto.setIqcformid(record.get("iqcformid"));
            auto.setIstockoutqcformmid(record.get("istockoutqcformmid"));
            auto.setIseq(record.get("iseq"));
            auto.setIsubseq(record.get("isubseq"));
            auto.setItype(record.get("itype"));
            auto.setCoptions(record.get("coptions"));
            auto.setCqcformparamids(record.get("cqcformparamids"));
            auto.setCqcitemname(record.get("cqcitemname"));
            auto.setCqcparamname(record.get("cqcparamname"));
            auto.setImaxval(record.get("imaxval"));
            auto.setIminval(record.get("iminval"));
            auto.setIstdval(record.get("istdval"));
            autoList.add(auto);
        }
        //3、最后返回vo
        AutoGetCheckOutTableDatasVo autoGetCheckOutTableDatasVo = new AutoGetCheckOutTableDatasVo();
        autoGetCheckOutTableDatasVo.setCode(0);
        autoGetCheckOutTableDatasVo.setData(autoList);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(autoGetCheckOutTableDatasVo);
    }

    /*
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     * @param iautoid：主键
     * */
    public JBoltApiRet jumpOnlysee(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        StockoutQcFormM stockoutQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(stockoutQcFormM.getIAutoId());
        //2、set到实体类
        StockoutQcFormMOnlysee onlysee = new StockoutQcFormMOnlysee();
        onlysee.setStockoutQcFormM(stockoutQcFormM);
        onlysee.setRecord(record);
        onlysee.setStockoutqcformlist(stockoutqcformlist);
        //3、最后返回vo
        StockoutQcFormMOnlyseeVo stockoutQcFormMOnlyseeVo = new StockoutQcFormMOnlyseeVo();
        stockoutQcFormMOnlyseeVo.setCode(0);
        stockoutQcFormMOnlyseeVo.setData(onlysee);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(stockoutQcFormMOnlyseeVo);
    }

    /*
     * 跳转到"查看"页面后，自动加载查看页面table的数据
     * @param iautoid：主键
     * */
    public JBoltApiRet autoGetOnlyseeTableDatas(Long iautoid) {
        //1、调用方法获得table数据
        List<Record> recordList = service.getonlyseelistByiautoid(iautoid);
        //2、传到实体类里面
        ArrayList<AutoGetOnlyseeTableDatas> autoList = new ArrayList<>();
        for (Record record : recordList) {
            AutoGetOnlyseeTableDatas auto = new AutoGetOnlyseeTableDatas();
            auto.setIautoid(record.get("iautoid"));
            auto.setIformparamid(record.get("iformparamid"));
            auto.setIqcformid(record.get("iqcformid"));
            auto.setCoptions(record.get("coptions"));
            auto.setCqcformparamids(record.get("cqcformparamids"));
            auto.setCqcitemname(record.get("cqcitemname"));
            auto.setCqcparamname(record.get("cqcparamname"));
            auto.setIseq(record.get("iseq"));
            auto.setIsubseq(record.get("isubseq"));
            auto.setItype(record.get("itype"));
            auto.setImaxval(record.get("imaxval"));
            auto.setIminval(record.get("iminval"));
            auto.setIstdval(record.get("istdval"));
            List<Cvalue> cvaluelist = JBoltModelKit.getBeanList(Cvalue.class, JSON.parseArray(record.get("cvaluelist")));
            auto.setCvaluelist(cvaluelist);
            autoList.add(auto);
        }
        //3、最后返回vo
        AutoGetOnlyseeTableDatasVo datasVo = new AutoGetOnlyseeTableDatasVo();
        datasVo.setCode(0);
        datasVo.setData(autoList);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(datasVo);
    }

    /*
     * 点击“检验”按钮，在检验页面点击“确定”按钮，将数据带到后台保存
     * */
    public JBoltApiRet saveCheckOut(String cmeasurepurpose, String cdcno, Long istockqcformmiautoid, String cmeasureunit,
                                    String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、将serializeSubmitList转换出来
        List<StockOutQcFormMApiSaveEdit> saveEditList = JBoltModelKit
            .getBeanList(StockOutQcFormMApiSaveEdit.class, JSON.parseArray(serializeSubmitList));
        //2、开始更新编辑页面的数据
        List<StockoutqcformdLine> stockoutqcformdLines = new ArrayList<>();
        for (StockOutQcFormMApiSaveEdit stock : saveEditList) {
            List<CValue> cvalueList = stock.getCvalueList();
            List<SerializeElement> serializeElementList = stock.getSerializeElementList();
            Integer iseq = stock.getIseq();
            for (int i = 0; i < cvalueList.size(); i++) {
                CValue cValue = cvalueList.get(i);
                SerializeElement element = serializeElementList.get(i);
                String cvalue = element.getValue();
                //质量管理-来料检明细列值表
                StockoutqcformdLine stockoutqcformdLine = stockoutqcformdLineService.findById(cValue.getLineiautoid());
                saveStockQcFormdLineModel(stockoutqcformdLine, istockqcformmiautoid, iseq, cvalue);
                stockoutqcformdLines.add(stockoutqcformdLine);
            }
        }
        //3、保存line表
        if (!stockoutqcformdLines.isEmpty()){
            stockoutqcformdLineService.batchSave(stockoutqcformdLines);
        }
        /*
         * 4、出库检表
         * 1.如果isok=0，代表不合格，将iStatus更新为2，isCompleted更新为1；
         * 2.如果isok=1，代表合格，将iStatus更新为3，isCompleted更新为1
         * */
        StockoutQcFormM stockoutQcFormM = service.findById(istockqcformmiautoid);
        saveStockoutQcFormmModel(stockoutQcFormM,cmeasurepurpose, cdcno, cmeasureunit, isok, cmeasurereason, cmemo);
        service.update(stockoutQcFormM);
        //5、如果检验结果不合格，生成在库异常品记录
        StockoutDefect defect = stockoutDefectService
            .findStockoutDefectByiStockoutQcFormMid(istockqcformmiautoid);
        if (null == defect) {
            StockoutDefect stockoutDefect = new StockoutDefect();
            stockoutDefectService.savestockoutDefectmodel(stockoutDefect, stockoutQcFormM);
            stockoutDefectService.save(stockoutDefect);
        }
        //6、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }
    /*
     * 给出库检明细列值表传参
     * */
    public void saveStockQcFormdLineModel(StockoutqcformdLine stockoutqcformdLine, Long iautoid, Integer iseq, String cvalue) {
        stockoutqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
        stockoutqcformdLine.setIStockoutQcFormDid(iautoid);
        stockoutqcformdLine.setISeq(iseq);
        stockoutqcformdLine.setCValue(cvalue);
    }

    /*
     * 点击“编辑”按钮，在编辑页面点击“确定”按钮，将数据带到后台保存
     * */
    public JBoltApiRet saveEdit(String cmeasurepurpose, String cdcno, Long stockqcformmiautoid, String cmeasureunit,
                                String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、将serializeSubmitList转换出来
        List<StockOutQcFormMApiSaveEdit> saveEditList = JBoltModelKit
            .getBeanList(StockOutQcFormMApiSaveEdit.class, JSON.parseArray(serializeSubmitList));
        //2、开始更新编辑页面的数据
        List<StockoutqcformdLine> stockoutqcformdLines = new ArrayList<>();
        for (StockOutQcFormMApiSaveEdit stock : saveEditList) {
            List<CValue> cvalueList = stock.getCvalueList();
            List<SerializeElement> serializeElementList = stock.getSerializeElementList();
            for (int i = 0; i < cvalueList.size(); i++) {
                CValue cValue = cvalueList.get(i);
                SerializeElement element = serializeElementList.get(i);
                //质量管理-来料检明细列值表
                StockoutqcformdLine stockoutqcformdLine = stockoutqcformdLineService.findById(cValue.getLineiautoid());
                stockoutqcformdLine.setCValue(element.getValue());
                stockoutqcformdLines.add(stockoutqcformdLine);
            }
        }
        //3、更新line表
        if (!stockoutqcformdLines.isEmpty()) {
            stockoutqcformdLineService.batchUpdate(stockoutqcformdLines);
        }
        //4、更新主表
        StockoutQcFormM stockoutQcFormM = service.findById(stockqcformmiautoid);
        saveStockoutQcFormmModel(stockoutQcFormM, cmeasurepurpose, cdcno, cmeasureunit, isok, cmeasurereason, cmemo);
        Ret ret = service.update(stockoutQcFormM);
        //5、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 给出库检主表传参
     * */
    public void saveStockoutQcFormmModel(StockoutQcFormM stockoutQcFormM, String cmeasurepurpose, String cdcno,
                                         String cmeasureunit, String isok, String cmeasurereason, String cmemo) {
        stockoutQcFormM.setCMeasurePurpose(cmeasurepurpose);//测定目的
        stockoutQcFormM.setCMeasureReason(cmeasurereason);//测定理由
        stockoutQcFormM.setCMeasureUnit(cmeasureunit); //测定单位
        stockoutQcFormM.setCMemo(cmemo);//备注
        stockoutQcFormM.setCDcNo(cdcno); //设变号
        stockoutQcFormM.setIsOk(isok.equalsIgnoreCase("0") ? false : true);//是否合格
        stockoutQcFormM.setIStatus(isok.equalsIgnoreCase("0") ? 2 : 3);
        stockoutQcFormM.setIsCompleted(true);
    }

}
