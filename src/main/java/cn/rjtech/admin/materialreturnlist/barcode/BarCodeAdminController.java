//package cn.rjtech.admin.materialreturnlist.barcode;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.extra.qrcode.QrCodeUtil;
//import cn.jbolt._admin.hiprint.HiprintTplService;
//import cn.jbolt._admin.permission.PermissionKey;
//import cn.jbolt.common.model.HiprintTpl;
//import cn.jbolt.core.base.JBoltMsg;
//import cn.jbolt.core.bean.JBoltDateRange;
//import cn.jbolt.core.permission.CheckPermission;
//import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
//import cn.jbolt.core.permission.UnCheck;
//import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
//import cn.rjtech.base.controller.BaseAdminController;
//import cn.rjtech.constants.DataSourceConstants;
//import cn.rjtech.util.ValidationUtils;
//import com.google.zxing.BarcodeFormat;
//import com.jfinal.aop.Before;
//import com.jfinal.aop.Inject;
//import com.jfinal.core.Path;
//import com.jfinal.core.paragetter.Para;
//import com.jfinal.kit.Kv;
//import com.jfinal.plugin.activerecord.Page;
//import com.jfinal.plugin.activerecord.Record;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
///**
// * 条码汇总 Controller
// *
// * @author Kephon
// */
//@Before(JBoltAdminAuthInterceptor.class)
//@UnCheckIfSystemAdmin
//@UnCheck
//@Path(value = "/admin/report/barcode", viewPath = "/_view/admin/report/barcode")
//public class BarCodeAdminController extends BaseAdminController {
//
//    @Inject
//    private BarCodeAdminService service;
//    @Inject
//    private HiprintTplService tplService;
//
//
//    /**
//     * 首页
//     */
//    public void index(){
//        render("index.html");
//    }
//
//
//    /**
//     * 页面数据源
//     */
//    public void datas(){
//        JBoltDateRange dateRange=getDateRange();
//        Date startTime=dateRange.getStartDate();
//        Date endTime=dateRange.getEndDate();
//        Kv kv = getKv();
//        kv.put("startTime",startTime);
//        kv.put("endTime",endTime);
//        renderJsonData(service.datas(getPageSize(),getPageNumber(),kv));
//    }
//
//
//
//    /**
//     *详情
//     */
//    public void details(){
//        Kv kv = getKv();
//        Record record= service.findbyBillno(kv.getStr("barcode"));
//        set("barcode",kv.getStr("barcode"));
//        set("list",record);
//        render("details.html");
//    }
//
//    /**
//     * 详情数据
//     */
//    public void detailsDatas(){
//        JBoltDateRange dateRange=getDateRange();
//        Date startTime=dateRange.getStartDate();
//        Date endTime=dateRange.getEndDate();
//        Kv kv = getKv();
//        kv.put("startTime",startTime);
//        kv.put("endTime",endTime);
//        Page<Record> page = service.detailsDatas(getPageNumber(), getPageSize(), kv);
//        renderJBoltTableJsonData(page,kv);
//    }
//
//    /***
//     * 勾选导出
//     */
//    @SuppressWarnings("unchecked")
//    public void downloadChecked() throws Exception{
//        Kv kv = getKv();
//        String ids = kv.getStr("ids");
//        if (ids != null) {
//            String[] split = ids.split("。");
//            String sqlids = "";
//            for (String id : split) {
//                sqlids += "'" + id + "',";
//            }
//            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
//            sqlids = sqlids.substring(0, sqlids.length() - 1);
//            kv.set("sqlids", sqlids);
//        }
//        List<Record> records = service.getBarcodeTotalList(DataSourceConstants.U9,kv);
//        renderJxls("barcode.xlsx", Kv.by("rows", records), "条码汇总表_" + DateUtil.today() + ".xlsx");
//
//    }
//
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
//
//
//
//
//
//
//
//
//}
