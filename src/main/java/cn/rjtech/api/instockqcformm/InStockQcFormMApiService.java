package cn.rjtech.api.instockqcformm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.model.momdata.InStockQcFormD;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.io.IOException;
import java.util.List;

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
    @Inject
    private RcvDocQcFormMService  rcvDocQcFormMService;

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
    public JBoltApiRet createTable(Long iautoid) {
        service.createTable(iautoid);
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 点击检验按钮，跳转到checkout页面
     *
     * @param iautoid 主键
     * */
    public JBoltApiRet jumpCheckOut(Long iautoid) {
        //1、查找子页面需要的传参
        Record record = new Record();
        Record checkoutrecord = service.getCheckoutListByIautoId(iautoid);
        if (checkoutrecord == null) {
            return JBoltApiRet.API_FAIL("数据不存在");
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(checkoutrecord.getLong("iqcformid"));
        record.set("columns", tableHeadData);
        record.set("record", checkoutrecord);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /**
     * 点击查看按钮，跳转到onlysee页面
     */
    public JBoltApiRet jumpOnlySee(Long iautoid) {
        //1、查询子页面需要的数据
        Record record = new Record();
        Record checkoutrecord = service.getCheckoutListByIautoId(iautoid);
        if (checkoutrecord == null) {
            return JBoltApiRet.API_FAIL("数据不存在");
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(checkoutrecord.getLong("iqcformid"));
        List<InStockQcFormD> stockoutqcformlist = inStockQcFormDService.findByIInStockQcFormMid(iautoid);
        record.set("record", checkoutrecord);
        record.set("columns", tableHeadData);
        record.set("stockoutqcformlist", stockoutqcformlist);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public JBoltApiRet jumpEdit(Long iautoid) {
        //1、查询子页面需要的数据
        Record record = new Record();
        Record checkoutrecord = service.getCheckoutListByIautoId(iautoid);
        if (checkoutrecord == null) {
            return JBoltApiRet.API_FAIL("数据不存在");
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(checkoutrecord.getLong("iqcformid"));
        List<InStockQcFormD> stockoutqcformlist = inStockQcFormDService.findByIInStockQcFormMid(iautoid);
        record.set("record", checkoutrecord);
        record.set("columns", tableHeadData);
        record.set("stockoutqcformlist", stockoutqcformlist);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(record);
    }

    /*
     * 跳转到checkout页面后，自动加载table数据
     * */
    public JBoltApiRet autoGetCheckOutTableDatas(Long iinstockqcformmid) {
        Kv kv = new Kv();
        kv.set("iinstockqcformmid", iinstockqcformmid);
        //1、查询
        List<Record> tableDatas = service.getTableDatas(kv);
        //2、遍历
        return JBoltApiRet.API_SUCCESS_WITH_DATA(tableDatas);
    }

    /*
     * 在检验页面点击确定,保存数据
     * */
    public JBoltApiRet saveCheckOut(String cmeasurepurpose, String cdcno, Long iinstockqcformmid, String cmeasureunit,
                                    String isok, String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、开始保存检验页面的数据
        Boolean result = service.achiveSerializeSubmitList(JSON.parseArray(serializeSubmitList),
            iinstockqcformmid, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
        //2、最后返回成功
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 在编辑页面点击确定，保存编辑页面的数据
     */
    public JBoltApiRet saveEdit(String cmeasurepurpose, String cdcno, Long iinstockqcformmid, String cmeasureunit, String isok,
                                String cmeasurereason, String serializeSubmitList, String cmemo) {
        //1、保存
        Boolean result = service.achiveSerializeSubmitList(JSON.parseArray(serializeSubmitList), iinstockqcformmid,
            cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
        return JBoltApiRet.API_SUCCESS;
    }

    /*
     * 删除在库检查表
     * */
    public JBoltApiRet deleteCheckoutByIautoid(Long iautoid) {
        Ret ret = service.deleteCheckoutByIautoid(iautoid);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(ret);
    }

    /*
     * 导出详情页
     * */
    public JBoltApiRet getExportData(Long iautoid) throws IOException {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.getExportData(iautoid));
    }


    public JBoltApiRet findDetailByBarcode(String cbarcode){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.findDetailByBarcode(cbarcode));
    }

    public JBoltApiRet saveInStockQcFormByCbarcode(String cbarcode, Integer iqty, String invcode, String cinvcode1, String cinvname1,
                                                   String iinventoryid, String cdcno, String cmeasurereason, Long iqcformid){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(service.saveInStockQcFormByCbarcode(cbarcode, iqty, invcode, cinvcode1, cinvname1,iinventoryid, cdcno,
            cmeasurereason,Long.valueOf(iqcformid)));
    }
}
