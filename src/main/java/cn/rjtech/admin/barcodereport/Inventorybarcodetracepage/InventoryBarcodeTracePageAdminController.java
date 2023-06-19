package cn.rjtech.admin.barcodereport.Inventorybarcodetracepage;
import cn.hutool.core.date.DateUtil;

import cn.jbolt._admin.hiprint.HiprintTplService;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.util.ValidationUtils;

import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;


import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 物料现品票汇总管理 Controller
 *
 * @author Kephon
 */
@CheckPermission(PermissionKey.INVENTORY_BARCODE_TRACEPAGE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/barcodeReport/InventoryBarcodeTracePage", viewPath = "/_view/admin/barcodereport/inventorybarcodetracepage")
public class InventoryBarcodeTracePageAdminController extends BaseAdminController {

    @Inject
    private InventoryBarcodeTracePageAdminService service;
    @Inject
    private HiprintTplService tplService;


    /**
     * 首页
     */
    public void index(){
        render("index.html");
    }

    /**
     *详情
     */
    public void details(){
        Kv kv = getKv();
        set("barcode",kv.getStr("barcode"));
        render("details.html");
    }


    /**
     * 数据源
     */
    /**
     * 条码汇总表(新)数据源
     * */
    public void newdatas(){
        Kv kv = getKv();
        set("barcode",kv.getStr("barcode"));
        String startTime =(String) kv.get("starttime");
        String endtime =(String) kv.get("endtime");
        renderJsonData(service.newdatas(getPageSize(),getPageNumber(),kv));
    }



    /**
     * 生成二维码
     */
    public void QRCode() {
        Kv kv = new Kv();
        kv.setIfNotNull("ids", get("ids"));
        renderJsonData(service.getBarcodeTotalList(DataSourceConstants.U8,kv));
    }



    /***
     * 勾选导出
     */
    @SuppressWarnings("unchecked")
    public void downloadChecked() throws Exception{
        Kv kv = getKv();
        String ids = kv.getStr("ids");
        if (ids != null) {
            String[] split = ids.split(",");
            String sqlids = "";
            for (String id : split) {
                sqlids += "'" + id + "',";
            }
            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
            sqlids = sqlids.substring(0, sqlids.length() - 1);
            kv.set("sqlids", sqlids);
        }
        List<Record> records = service.getBarcodeTotalList(DataSourceConstants.U8,kv);
        JBoltCamelCaseUtil.keyToCamelCase(records);
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .create()//创建JBoltExcel
                .addSheet(//设置sheet
                        JBoltExcelSheet.create("物料现品票汇总管理")
                                .setHeaders(1,//sheet里添加表头
                                        JBoltExcelHeader.create("billno", "单据单号", 20),
                                        JBoltExcelHeader.create("vencode", "供应商编码", 20),
                                        JBoltExcelHeader.create("otherinvcode", "客户部番", 20),
                                        JBoltExcelHeader.create("otherinvname", "部品名称", 20),
                                        JBoltExcelHeader.create("invstd", "规格", 40),
                                        JBoltExcelHeader.create("poscode", "主记录单位编码", 40),
                                        JBoltExcelHeader.create("barcode", "现品票条码", 40),
                                        JBoltExcelHeader.create("version", "版本号", 40),
                                        JBoltExcelHeader.create("qty", "数量", 40),
                                        JBoltExcelHeader.create("ScheduleDate", "计划到货日期", 40),
                                        JBoltExcelHeader.create("whname", "仓库名称", 40),
                                        JBoltExcelHeader.create("posname", "库区名称", 40),
                                        JBoltExcelHeader.create("createdate", "创建日期", 40)
                                ).setDataChangeHandler((data, index) -> {
                        })
                                .setRecordDatas(2, records)//设置数据
                ).setFileName("物料现品票汇总管理-" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);

    }

//    /***
//     * 勾选导出
//     */
//    @SuppressWarnings("unchecked")
//    public void detailsDownloadChecked() throws Exception{
//        Kv kv = getKv();
//        String ids = kv.getStr("ids");
//        if (ids != null) {
//            String[] split = ids.split(",");
//            String sqlids = "";
//            for (String id : split) {
//                sqlids += "'" + id + "',";
//            }
//            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
//            sqlids = sqlids.substring(0, sqlids.length() - 1);
//            kv.set("sqlids", sqlids);
//        }
//        List<Record> records = service.detailsDatasList(kv);
//        renderJxls("barcode_details.xlsx", Kv.by("rows", records), "条码变更记录_" + DateUtil.today() + ".xlsx");
//
//    }
//
//
//    public void printEntityLabelAffirm(@Para(value = "barcode") String barcode) throws Exception{
//        ValidationUtils.notNull(barcode, JBoltMsg.PARAM_ERROR);
//
//
//        //指定自定义模板库模板
//        HiprintTpl hiprintTpl = null;
//
//        //条码号，V_sys_BarcodeDetail，查询MES来源
//        Record record = service.getMesSourceBarCode(Kv.by("cbarcode", barcode));
//        Object jsonData = null;
//
//        if(isOk(record) && isOk(record.getStr("cbarcode"))){
//            Kv kv = Kv.by("ids", record.getStr("iautoid"));
//            switch (record.getStr("csource")) {
//                case MesSourceBarCodeEnum.FT:
//                    //分条料/片料
//                    if(isOk(record.getStr("cmemo")) && record.getStr("cmemo").equals("0")){
//                        hiprintTpl = tplService.findHiprintTplBySn("StripingTwo");
//                        jsonData = cutstripprocessService.getListStriping(kv);
//                    }else{
//                        hiprintTpl = tplService.findHiprintTplBySn("Slicedmaterial");
//                        jsonData = cutstripprocessService.getListSlicedmaterial(kv);
//                    }
//                    break;
//                case MesSourceBarCodeEnum.FTYJ:
//                    //分条余卷
//                    hiprintTpl = tplService.findHiprintTplBySn("ResidualvolumeTwo");
//                    jsonData = cutstripprocessService.getListresidualvolume(kv);
//                    break;
//                case MesSourceBarCodeEnum.FTYL:
//                    //分条余料
//                    break;
//                case MesSourceBarCodeEnum.PL:
//                    //片料
//                    hiprintTpl = tplService.findHiprintTplBySn("Sheetmaterial");
//                    jsonData = sliceinvprocessService.getListSheetMaterial(kv);
//                    break;
//                case MesSourceBarCodeEnum.PLYL:
//                    //片料余料
//                    break;
//                case MesSourceBarCodeEnum.BL:
//                    //板料
//                    hiprintTpl = tplService.findHiprintTplBySn("CoiledSheet");
//                    jsonData = pieceinvprocessService.getListSelectPrint(kv);
//                    break;
//                case MesSourceBarCodeEnum.ZJ:
//                    //载具
//                    hiprintTpl = tplService.findHiprintTplBySn("loadmould");
//                    jsonData = loadMouldService.getList(kv);
//                    break;
//                case MesSourceBarCodeEnum.SCTM:
//                    //生产条码
//                    hiprintTpl = tplService.findHiprintTplBySn("entity_label_tpl");
//                    jsonData = assignpositionrecordService.selectPrintEntityLabelAffirmList(kv);
//                    break;
//                case MesSourceBarCodeEnum.SCBL:
//                    hiprintTpl = tplService.findHiprintTplBySn("entity_label_badness_tpl");
//                    jsonData = assignpositionrecordService.skipPrintEntityLabelBadnessAffirm(kv);
//                    break;
//                default:
//                    break;
//            }
//
//        }else{
//            //其它来源处理
//            Kv kv = getKv();
//            String type = kv.getStr("type");
//            String billstate = kv.getStr("billstate");
//
//            //期初卷料
//            boolean qcjl=type!=null&&type.equals("卷料")&&billstate.equals("期初");
//            //非期初卷料
//            boolean jl=type!=null&&type.equals("卷料")&&!billstate.equals("期初");
//            //非期初片料
//            boolean pl=type!=null&&type.equals("片料")&&!billstate.equals("期初");
//
//            if(qcjl){
//                //期初卷料
//                hiprintTpl=tplService.findHiprintTplBySn("labelJuan_total");
//                jsonData = service.barcodedetail_JL(barcode);
//            }else if(jl) {
//                //非期初卷料
//                hiprintTpl=tplService.findHiprintTplBySn("labelJuan");
//                jsonData = service.barcodedetail_JL(barcode);
//            }else if(pl) {
//                //非期初片料
//                hiprintTpl=tplService.findHiprintTplBySn("labelPian");
//                jsonData = service.barcodedetail_PL_new(barcode);
//            }else{
//                ValidationUtils.notNull(record, JBoltMsg.DATA_NOT_EXIST+ ",未设置数据来源或未找到条码"+barcode);
//            }
//        }
//
//        ValidationUtils.notNull(hiprintTpl, "未设置打印模板！");
//        set("printJsonData", jsonData);
//        set("hiprintTpl", hiprintTpl.getContent());
//        render("print_entity_label_affirm.html");
//    }








}
