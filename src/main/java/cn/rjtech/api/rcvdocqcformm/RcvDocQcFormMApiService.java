package cn.rjtech.api.rcvdocqcformm;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.entity.vo.rcvdocqcformm.AutoGetCheckOutTableDatas;
import cn.rjtech.entity.vo.rcvdocqcformm.AutoGetCheckOutTableDatasVo;
import cn.rjtech.entity.vo.rcvdocqcformm.AutoGetRcvOnlyseeTableDatas;
import cn.rjtech.entity.vo.rcvdocqcformm.AutoGetRcvOnlyseeTableDatas.Cvalue;
import cn.rjtech.entity.vo.rcvdocqcformm.AutoGetRcvOnlyseeTableDatasVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiCheckOut;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiCheckOutVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiSaveEdit;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiSaveEdit.CValue;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiSaveEdit.SerializeElement;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMOnlyseeApi;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMOnlyseeApiVo;
import cn.rjtech.model.momdata.RcvDocDefect;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.model.momdata.RcvdocqcformdLine;
import cn.rjtech.model.momdata.StockoutDefect;
import cn.rjtech.util.JBoltModelKit;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 17:41
 * @Description 质量管理-来料检api接口
 */
public class RcvDocQcFormMApiService {

    @Inject
    private RcvDocQcFormMApiService  apiService;
    @Inject
    private RcvDocQcFormMService     service;              //质量管理-来料检验表
    @Inject
    private RcvDocQcFormDService     rcvDocQcFormDService; //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService rcvdocqcformdLineService; //质量管理-来料检明细列值表
    @Inject
    private JBoltBaseService         jBoltBaseService;
    @Inject
    private RcvDocDefectService      rcvDocDefectService;      ////质量管理-来料异常品记录

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
        RcvDocQcFormM rcvDocQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(rcvDocQcFormM.getIAutoId());
        //2、set到实体类
        RcvDocQcFormMApiCheckOut rcvDocQcFormMApiCheckOut = new RcvDocQcFormMApiCheckOut();
        rcvDocQcFormMApiCheckOut.setRcvDocQcFormM(rcvDocQcFormM);
        rcvDocQcFormMApiCheckOut.setRecord(record);
        //3、最后返回vo
        RcvDocQcFormMApiCheckOutVo checkoutVo = new RcvDocQcFormMApiCheckOutVo();
        checkoutVo.setCode(0);
        checkoutVo.setData(rcvDocQcFormMApiCheckOut);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(checkoutVo);
    }

    /**
     * 跳转到检验页面后，自动加载table的数据
     */
    public JBoltApiRet autoGetRcvCheckOutTableDatas(Long ircvdocqcformmid) {
        //1、查询table的数据
        Kv kv = new Kv();
        kv.set("ircvdocqcformmid", ircvdocqcformmid);
        List<Record> recordList = service
            .clearZero(jBoltBaseService.dbTemplate("rcvdocqcformm.findChecoutListByIformParamid", kv).find());
        //2、将数据传到vo
        List<AutoGetCheckOutTableDatas> autoList = new ArrayList<>();
        for (Record record : recordList) {
            AutoGetCheckOutTableDatas auto = new AutoGetCheckOutTableDatas();
            auto.setIautoid(record.get("iautoid"));
            auto.setIformparamid(record.get("iformparamid"));
            auto.setIqcformid(record.get("iqcformid"));
            auto.setIrcvdocqcformmid(record.get("ircvdocqcformmid"));
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

    /**
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     * @param iautoid：主键
     */
    public JBoltApiRet jumpOnlysee(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        RcvDocQcFormM rcvDocQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(rcvDocQcFormM.getIAutoId());
        List<Record> docparamlist = service.getonlyseelistByiautoid(rcvDocQcFormM.getIAutoId());
        //2、set到实体类
        RcvDocQcFormMOnlyseeApi rcvDocQcFormMOnlyseeApi = new RcvDocQcFormMOnlyseeApi();
        rcvDocQcFormMOnlyseeApi.setRcvDocQcFormM(rcvDocQcFormM);
        rcvDocQcFormMOnlyseeApi.setRecord(record);
        rcvDocQcFormMOnlyseeApi.setDocparamlist(docparamlist);
        //3、最后返回vo
        RcvDocQcFormMOnlyseeApiVo rcvDocQcFormMOnlyseeApiVo = new RcvDocQcFormMOnlyseeApiVo();
        rcvDocQcFormMOnlyseeApiVo.setCode(0);
        rcvDocQcFormMOnlyseeApiVo.setData(rcvDocQcFormMOnlyseeApi);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(rcvDocQcFormMOnlyseeApiVo);
    }

    /**
     * 跳转到"查看"页面后，自动加载查看页面table的数据
     * @param iautoid：主键
     */
    public JBoltApiRet autoGetRcvOnlyseeTableDatas(Long iautoid) {
        //1、调用方法获得table数据
        List<Record> recordList = service.getonlyseelistByiautoid(iautoid);
        //2、传到实体类里面
        ArrayList<AutoGetRcvOnlyseeTableDatas> autoList = new ArrayList<>();
        for (Record record : recordList) {
            AutoGetRcvOnlyseeTableDatas auto = new AutoGetRcvOnlyseeTableDatas();
            auto.setIautoid(record.get("iautoid"));
            auto.setIformparamid(record.get("iformparamid"));
            auto.setIqcformid(record.get("iqcformid"));
            auto.setIrcvdocqcformmid(record.get("ircvdocqcformmid"));
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
            /*JSONArray cvalueArrayList = JSONArray.parseArray(record.get("cvaluelist"));
            for (int arr=0;arr<cvalueArrayList.size();arr++){
                Cvalue cvalue = new Cvalue();
            }*/
            List<Cvalue> cvaluelist = JBoltModelKit.getBeanList(Cvalue.class, JSON.parseArray(record.get("cvaluelist")));
            auto.setCvaluelist(cvaluelist);
            autoList.add(auto);
        }
        //3、最后返回vo
        AutoGetRcvOnlyseeTableDatasVo datasVo = new AutoGetRcvOnlyseeTableDatasVo();
        datasVo.setCode(0);
        datasVo.setData(autoList);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(datasVo);
    }

    /**
     * 点击“检验”按钮，在检验页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveCheckOut(String cmeasurepurpose, String cdcno, Long ircvdocqcformmiautoid, String cmeasureunit,
                                    String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、将serializeSubmitList转换出来
        List<RcvDocQcFormMApiSaveEdit> saveEditList = JBoltModelKit
            .getBeanList(RcvDocQcFormMApiSaveEdit.class, JSON.parseArray(serializeSubmitList));
        //2、开始更新编辑页面的数据
        List<RcvdocqcformdLine> rcvdocqcformdLines = new ArrayList<RcvdocqcformdLine>();
        for (RcvDocQcFormMApiSaveEdit stock : saveEditList) {
            List<CValue> cvalueList = stock.getCvalueList();
            List<SerializeElement> serializeElementList = stock.getSerializeElementList();
            Integer iseq = stock.getIseq();
            for (int i = 0; i < cvalueList.size(); i++) {
                CValue cValue = cvalueList.get(i);
                SerializeElement element = serializeElementList.get(i);
                String cvalue = element.getValue();
                //质量管理-来料检明细列值表
                RcvdocqcformdLine rcvdocqcformdLine = new RcvdocqcformdLine();//质量管理-来料检明细列值表
                saveRcvdocqcformdLineModel(rcvdocqcformdLine, ircvdocqcformmiautoid, iseq, cvalue);
                rcvdocqcformdLines.add(rcvdocqcformdLine);
            }
        }
        //3、保存line表
        if (!rcvdocqcformdLines.isEmpty()) {
            rcvdocqcformdLineService.batchSave(rcvdocqcformdLines);
        }
        /** 4、出库检表
         * 1.如果isok=0，代表不合格，将iStatus更新为2，isCompleted更新为1；
         * 2.如果isok=1，代表合格，将iStatus更新为3，isCompleted更新为1
         */
        RcvDocQcFormM rcvDocQcFormM = service.findById(ircvdocqcformmiautoid);
        saveRcvDocQcFormMModel(rcvDocQcFormM,cmeasurepurpose, cdcno, cmeasureunit, isok, cmeasurereason, cmemo);
        service.update(rcvDocQcFormM);
        //5、如果检验结果不合格，生成在库异常品记录
        if (isok.equals("0")){
            RcvDocDefect defect = rcvDocDefectService
                .findStockoutDefectByiRcvDocQcFormMid(ircvdocqcformmiautoid);
            if (null == defect) {
                RcvDocDefect rcvDocDefect = new RcvDocDefect();
                rcvDocDefectService.saveRcvDocDefectModel(rcvDocDefect, rcvDocQcFormM);
                rcvDocDefectService.save(rcvDocDefect);
            }
        }
        //6、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 给Line表传参
     */
    public void saveRcvdocqcformdLineModel(RcvdocqcformdLine rcvdocqcformdLine, Long ircvdocqcformmiautoid,
                                           Integer iseq, String cvalue) {
        rcvdocqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
        rcvdocqcformdLine.setIRcvDocQcFormDid(ircvdocqcformmiautoid);
        rcvdocqcformdLine.setISeq(iseq);
        rcvdocqcformdLine.setCValue(cvalue);
    }

    /**
     * 给来料检主表传参
     */
    public void saveRcvDocQcFormMModel(RcvDocQcFormM docQcFormM,String cmeasurepurpose,String cdcno,
                                    String cmeasureunit,String isok,String cmeasurereason, String cmemo) {
        docQcFormM.setCMeasurePurpose(cmeasurepurpose);//测定目的
        docQcFormM.setCMeasureReason(cmeasurereason);//测定理由
        docQcFormM.setCMeasureUnit(cmeasureunit); //测定单位
        docQcFormM.setCMemo(cmemo);//备注
        docQcFormM.setCDcNo(cdcno); //设变号
        docQcFormM.setIsOk(isok.equalsIgnoreCase("0") ? false : true);//是否合格
        docQcFormM.setIStatus(isok.equalsIgnoreCase("0") ? 2 : 3);
        docQcFormM.setIsCompleted(true);
    }

    /**
     * 点击“编辑”按钮，在编辑页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveEdit(String cmeasurepurpose, String cdcno, Long ircvdocqcformmiautoid, String cmeasureunit,
                                String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、将serializeSubmitList转换出来
        List<RcvDocQcFormMApiSaveEdit> saveEditList = JBoltModelKit
            .getBeanList(RcvDocQcFormMApiSaveEdit.class, JSON.parseArray(serializeSubmitList));
        //2、开始更新编辑页面的数据
        List<RcvdocqcformdLine> rcvdocqcformdLines = new ArrayList<RcvdocqcformdLine>();
        for (RcvDocQcFormMApiSaveEdit stock : saveEditList) {
            List<CValue> cvalueList = stock.getCvalueList();
            List<SerializeElement> serializeElementList = stock.getSerializeElementList();
            for (int i = 0; i < cvalueList.size(); i++) {
                CValue cValue = cvalueList.get(i);
                SerializeElement element = serializeElementList.get(i);
                String cvalue = element.getValue();
                //质量管理-来料检明细列值表
                RcvdocqcformdLine rcvdocqcformdLine = rcvdocqcformdLineService.findById(cValue.getLineiautoid());
                rcvdocqcformdLine.setCValue(cvalue);
                rcvdocqcformdLines.add(rcvdocqcformdLine);
            }
        }
        //3、保存line表
        if (!rcvdocqcformdLines.isEmpty()) {
            rcvdocqcformdLineService.batchUpdate(rcvdocqcformdLines);
        }
        //4、更新主表
        RcvDocQcFormM rcvDocQcFormM = service.findById(ircvdocqcformmiautoid);
        saveRcvDocQcFormMModel(rcvDocQcFormM, cmeasurepurpose, cdcno, cmeasureunit, isok, cmeasurereason, cmemo);
        Ret ret = service.update(rcvDocQcFormM);
        //5、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }
}
