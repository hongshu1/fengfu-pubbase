package cn.rjtech.api.instockqcformm;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.model.momdata.InStockQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/4 14:27
 * @Description 在库检
 */
public class InStockQcFormMApiService extends JBoltApiBaseService {

    @Inject
    private InStockQcFormMService service;
    @Inject
    private InStockDefectService  defectService;
    @Inject
    private InStockQcFormDService inStockQcFormDService;

    /*
     * 点击左侧导航栏-在库检，显示主页面数据
     *
     * @param cInvQcFormNo 检验单号
     * @param cinvaddcode       存货编码
     * @param cinvcode1         客户部番
     * @param cinvname1         部品名称
     * @param name              检验员
     * @param istatus           检验结果
     * @param iscompleted       状态
     * @param cqcformname       检验表格名称
     * @param starttime         开始时间
     * @param endtime           结束时间
     * */
    public JBoltApiRet getDatas(Kv kv) {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.pageList(kv));
    }

    /*
     * @desc: 生成
     * @param iautoid: 主键
     * @param cqcformname: 表格名称
     * */
    public JBoltApiRet createTable(Long iautoid, String cqcformname) {
        service.createTable(iautoid, cqcformname);
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 点击检验按钮，跳转到checkout页面
     *
     * @param iautoid 主键
     * */
    public JBoltApiRet jumpCheckOut(Long iautoid) {
        //1、查找子页面需要的传参
        InStockQcFormM inStockQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(inStockQcFormM.getIAutoId());
        record.set("cbarcode", inStockQcFormM.getCBarcode());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 跳转到checkout页面后，自动加载table数据
     * */
    public JBoltApiRet autoGetCheckOutTableDatas(Long iinstockqcformmid) {
        Kv kv = new Kv();
        kv.set("iinstockqcformmid", iinstockqcformmid);
        //1、查询
        List<Record> recordList = service.getCheckOutTableDatas(kv);
        //2、遍历
        recordList.stream().forEach(record -> {
            record.keep("iautoid", "iformparamid", "iqcformid", "iinstockqcformmid", "iseq", "isubseq",
                "itype", "coptions", "cqcformparamids", "cqcitemname", "cqcparamname", "imaxval", "iminval", "istdval");
        });
        return JBoltApiRet.API_SUCCESS_WITH_DATA(recordList);
    }

    /*
     * 在检验页面点击确定,保存数据
     * */
    public JBoltApiRet saveCheckOut(String cmeasurepurpose, String cdcno, Long iinstockqcformmid, String cmeasureunit,
                                    String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、开始保存检验页面的数据
        Boolean result = service.achiveChecOutSerializeSubmitList(JSON.parseArray(serializeSubmitList),
            iinstockqcformmid, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 删除在库检查表
     * */
    public JBoltApiRet deleteCheckoutByIautoid(Long iautoid) {
        Ret ret = service.deleteCheckoutByIautoid(iautoid);
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 点击查看按钮，跳转到onlysee页面
     */
    public JBoltApiRet jumpOnlySee(Long iautoid) {
        //1、查询子页面需要的数据
        InStockQcFormM inStockQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(inStockQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(inStockQcFormM.getIAutoId());
        record.set("cbarcode", inStockQcFormM.getCBarcode());
        record.set("size", stockoutqcformlist.size());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public JBoltApiRet jumpEdit(Long iautoid) {
        InStockQcFormM inStockQcFormM = service.findById(iautoid);
        Record record = service.getCheckoutListByIautoId(inStockQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(inStockQcFormM.getIAutoId());
        record.set("size", stockoutqcformlist.size());
        record.set("cbarcode", inStockQcFormM.getCBarcode());
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 跳转到onlysee页面，自动加载table数据
     */
    public JBoltApiRet autoGetOnlySeeOrEditTableDatas(Long iautoid) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        //1、查询
        List<Record> recordList = service.getonlyseelistByiautoid(kv);
        recordList.stream().forEach(record -> {
            record.keep("iautoid", "iformparamid", "iqcformid", "coptions", "cqcformparamids", "cqcitemname",
                "cqcparamname", "iseq", "isubseq", "itype", "imaxval", "iminval", "istdval", "cvaluelist");
        });
        return JBoltApiRet.API_SUCCESS_WITH_DATA(recordList);
    }

    /*
     * 在编辑页面点击确定，保存编辑页面的数据
     */
    public JBoltApiRet saveEdit(String cmeasurepurpose, String cdcno, Long iinstockqcformmid, String cmeasureunit, String isok,
                                String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、保存
        Boolean result = service.achiveEditSerializeSubmitList(JSON.parseArray(serializeSubmitList), iinstockqcformmid,
            cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
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
