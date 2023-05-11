package cn.rjtech.api.rcvdocqcformm;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.model.momdata.RcvDocQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 17:41
 * @Description 质量管理-来料检api接口
 */
public class RcvDocQcFormMApiService extends JBoltApiBaseService {

    @Inject
    private RcvDocQcFormMApiService  apiService;
    @Inject
    private RcvDocQcFormMService     service;              //质量管理-来料检验表
    @Inject
    private RcvDocQcFormDService     rcvDocQcFormDService; //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService rcvdocqcformdLineService; //质量管理-来料检明细列值表
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

        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 跳转到检验页面后，自动加载table的数据
     */
    public JBoltApiRet autoGetRcvCheckOutTableDatas(Long ircvdocqcformmid) {
        //1、查询table的数据
        Kv kv = new Kv();
        kv.set("ircvdocqcformmid", ircvdocqcformmid);
        ;
        List<Record> recordList = service.getCheckOutTableDatas(kv);
        //2、将数据传到vo
        for (Record record : recordList) {
            record.keep("iautoid", "iformparamid", "iqcformid", "ircvdocqcformmid", "iseq", "isubseq", "itype", "coptions",
                "cqcformparamids", "cqcitemname", "cqcparamname", "imaxval", "iminval", "istdval");
        }
        //3、最后返回vo
        return JBoltApiRet.API_SUCCESS_WITH_DATA(recordList);
    }

    /**
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     */
    public JBoltApiRet jumpOnlysee(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        RcvDocQcFormM rcvDocQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(rcvDocQcFormM.getIAutoId());
        List<Record> docparamlist = service.getonlyseelistByiautoid(rcvDocQcFormM.getIAutoId());
        record.set("size", docparamlist.size());
        record.set("cRcvDocNo",rcvDocQcFormM.getCRcvDocNo());
        record.set("drcvdate",rcvDocQcFormM.getDRcvDate());
        record.set("imask",rcvDocQcFormM.getIMask());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public JBoltApiRet jumpEdit(Long iautoid){
        RcvDocQcFormM rcvDocQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(rcvDocQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(rcvDocQcFormM.getIAutoId());
        record.set("size", stockoutqcformlist.size());
        record.set("cRcvDocNo",rcvDocQcFormM.getCRcvDocNo());
        record.set("drcvdate",rcvDocQcFormM.getDRcvDate());
        record.set("imask",rcvDocQcFormM.getIMask());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 跳转到"查看"页面后，自动加载查看页面table的数据
     */
    public JBoltApiRet autoGetRcvOnlyseeOrEditTableDatas(Long iautoid) {
        //1、调用方法获得table数据
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        List<Record> recordList = service.getonlyseelistByiautoid(kv);//getonlyseelistByiautoid\getonlyseelistByiautoid
        //2、传到实体类里面
        for (Record record : recordList) {
            record.keep("iautoid", "iformparamid", "iqcformid", "ircvdocqcformmid", "iseq", "isubseq", "itype", "coptions",
                "cqcformparamids", "cqcitemname", "cqcparamname", "imaxval", "iminval", "istdval", "cvaluelist");
        }
        return JBoltApiRet.API_SUCCESS_WITH_DATA(recordList);
    }

    /**
     * 点击“检验”按钮，跳转到检验页面，在检验页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveCheckOut(String cmeasurepurpose, String cdcno, Long ircvdocqcformmiautoid, String cmeasureunit,
                                    String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、实现serializeSubmitList
        Boolean result = service.achieveSerializeSubmitList(JSON.parseArray(serializeSubmitList),
            ircvdocqcformmiautoid, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 点击“编辑”按钮，跳转到编辑页面，在编辑页面点击“确定”按钮，将数据带到后台保存
     */
    public JBoltApiRet saveEdit(String cmeasurepurpose, String cdcno, Long ircvdocqcformmiautoid, String cmeasureunit,
                                String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、实现编辑页面的SerializeSubmitList
        Boolean result = service.achiveEditSerializeSubmitList(JSON.parseArray(serializeSubmitList), ircvdocqcformmiautoid,
            cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
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
    public JBoltApiRet getExportData(Long iautoid) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.getExportData(iautoid));
    }
}
