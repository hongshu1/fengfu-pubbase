package cn.rjtech.api.rcvdocqcformm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.model.momdata.RcvDocQcFormD;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.io.IOException;
import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 17:41
 * @Description 质量管理-来料检api接口
 */
public class RcvDocQcFormMApiService extends JBoltApiBaseService {

    @Inject
    private RcvDocQcFormMService service;              //质量管理-来料检验表
    @Inject
    private RcvDocQcFormDService rcvDocQcFormDService; //质量管理-来料检单行配置表

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
        Record checkoutList = service.getCheckoutListByIautoId(iautoid);
        ValidationUtils.notNull(checkoutList, JBoltMsg.DATA_NOT_EXIST);

        //判断是否要先生成从表数据
        service.checkAutoCreateRcvDocQcFormD(iautoid);
        // 表头项目
        List tableHeadData = service.getTableHeadData(checkoutList.getLong("iqcformid"));
        record.set("columns", tableHeadData);//项目列名
        record.set("record", checkoutList);//存放检验信息、检查成绩表的数据
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 跳转到检验页面后，自动加载table的数据
     */
    public JBoltApiRet autoGetRcvCheckOutTableDatas(Long ircvdocqcformmid) {
        //1、查询table的数据
        Kv kv = new Kv();
        kv.set("ircvdocqcformmid", ircvdocqcformmid);
        List<Record> recordList = service.getCheckOutTableDatas(kv);
        //2、将数据传到vo
        for (Record record : recordList) {
            record.keep("coptions","cqcformparamids","cvaluelist","iautoid", "iformparamid", "imaxval", "iminval", "istdval",
                "iqcformid", "ircvdocqcformmid", "iseq", "itype", "paramnamelist");
        }
        //3、最后返回vo
        return JBoltApiRet.API_SUCCESS_WITH_DATA(recordList);
    }

    /**
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     */
    public JBoltApiRet jumpOnlysee(Long iautoid) {
        Record record = new Record();
        //1、查询跳转到另一页面需要的数据
        Record checkOutRecord = service.getCheckoutListByIautoId(iautoid);
        ValidationUtils.notNull(checkOutRecord, JBoltMsg.DATA_NOT_EXIST);
        // 表头项目
        List tableHeadData = service.getTableHeadData(checkOutRecord.getLong("iqcformid"));
        List<RcvDocQcFormD> docparamlist = rcvDocQcFormDService.findByIRcvDocQcFormMId(iautoid);
        record.set("docparamlist", docparamlist);
        record.set("columns", tableHeadData);
        record.set("record", checkOutRecord);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public JBoltApiRet jumpEdit(Long iautoid){
        Record record = new Record();
        //1、查询跳转到另一页面需要的数据
        Record checkoutRecord = service.getCheckoutListByIautoId(iautoid);
        ValidationUtils.notNull(checkoutRecord, JBoltMsg.DATA_NOT_EXIST);

        //判断是否要先生成从表数据
        service.checkAutoCreateRcvDocQcFormD(iautoid);
        // 表头项目
        List tableHeadData = service.getTableHeadData(checkoutRecord.getLong("iqcformid"));
        List<RcvDocQcFormD> docparamlist = rcvDocQcFormDService.findByIRcvDocQcFormMId(iautoid);
        record.set("docparamlist", docparamlist);
        record.set("columns", tableHeadData);
        record.set("record", checkoutRecord);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 在检验页面点击“确定”按钮，将数据带到后台保存
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
        Boolean result = service.achieveSerializeSubmitList(JSON.parseArray(serializeSubmitList), ircvdocqcformmiautoid,
            cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 导出详情页
     * */
    public JBoltApiRet getExportData(Long iautoid) throws IOException {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.getExportData(iautoid));
    }
}
